/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package come.filip.templategame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import come.filip.templategame.util.Constants;
import come.filip.templategame.util.GamePreferences;

public class WorldRenderer implements Disposable {

	private static final String TAG = WorldRenderer.class.getName();

	private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;

	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	private Box2DDebugRenderer b2debugRenderer;

	private ShaderProgram shaderMonochrome;

	public WorldRenderer (WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	private void init () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.setToOrtho(true); // flip y-axis
		camera.update();
		cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
		b2debugRenderer = new Box2DDebugRenderer();
		shaderMonochrome = new ShaderProgram(Gdx.files.internal(Constants.shaderMonochromeVertex),
			Gdx.files.internal(Constants.shaderMonochromeFragment));
		if (!shaderMonochrome.isCompiled()) {
			String msg = "Could not compile shader program: " + shaderMonochrome.getLog();
			throw new GdxRuntimeException(msg);
		}
	}

	public void render () {
		renderWorld(batch);
		renderGui(batch);
	}

	private void renderWorld (SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//if (GamePreferences.instance.useMonochromeShader) {
		//	batch.setShader(shaderMonochrome);
		//	shaderMonochrome.setUniformf("u_amount", 1.0f);
		//}
		worldController.level.render(batch);

		batch.setShader(null);
		batch.end();
		if (DEBUG_DRAW_BOX2D_WORLD) {
			b2debugRenderer.render(worldController.b2world, camera.combined);
		}
	}

	private void renderGui (SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();

		if (GamePreferences.instance.showFpsCounter) renderGuiFpsCounter(batch);
		// draw game over text
		//renderGuiGameOverMessage(batch);

		batch.end();
	}

	private void renderGuiFpsCounter (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if (fps >= 45) {
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1);
		} else if (fps >= 30) {
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		} else {
			// less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1);
		}

		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // white
	}

	private void renderGuiGameOverMessage (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		if (worldController.isGameOver()) {
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.drawMultiLine(batch, "GAME OVER", x, y, 1, BitmapFont.HAlignment.CENTER);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}

	public void resize (int width, int height) {

        // TODO: Look at viewport width
		/*camera.viewportWidth = (Gdx.graphics.getHeight() / (float)height) * (float)width;
		camera.update();
		cameraGUI.viewportHeight = Gdx.graphics.getHeight();
		cameraGUI.viewportWidth = (Gdx.graphics.getHeight() / (float)height) * (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();*/
	}

	@Override
	public void dispose () {
		batch.dispose();
		shaderMonochrome.dispose();
	}

}
