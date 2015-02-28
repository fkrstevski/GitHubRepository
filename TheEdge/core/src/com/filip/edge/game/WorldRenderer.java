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


package com.filip.edge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

public class WorldRenderer implements Disposable
{

    private static final String TAG = WorldRenderer.class.getName();

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;
    private Box2DDebugRenderer b2debugRenderer;

    private ShaderProgram shaderMonochrome;

    public WorldRenderer(WorldController worldController)
    {
        this.worldController = worldController;
        init();
    }

    private void init()
    {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.setToOrtho(true); // flip y-axis
        camera.update();
        b2debugRenderer = new Box2DDebugRenderer();
        shaderMonochrome = new ShaderProgram(Gdx.files.internal(Constants.shaderMonochromeVertex),
                Gdx.files.internal(Constants.shaderMonochromeFragment));
        if (!shaderMonochrome.isCompiled())
        {
            String msg = "Could not compile shader program: " + shaderMonochrome.getLog();
            throw new GdxRuntimeException(msg);
        }
    }

    public void render()
    {
        renderWorld(batch);
    }

    private void renderWorld(SpriteBatch batch)
    {
        worldController.cameraHelper.applyTo(camera);

        if (worldController.renderPhysics)
        {
            b2debugRenderer.render(worldController.b2world, camera.combined.scl(Constants.BOX2D_SCALE));
        }
        else
        {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            //if (GamePreferences.instance.useMonochromeShader) {
            //	batch.setShader(shaderMonochrome);
            //	shaderMonochrome.setUniformf("u_amount", 1.0f);
            //}

            if (worldController.state == WorldController.LevelState.GameOver)
            {
                renderGuiGameOver(batch);
            }
            else if (worldController.state == WorldController.LevelState.GameBeat)
            {
                renderGuiGameBeat(batch);
            }
            else
            {
                worldController.level.render(batch);
                renderGuiScore(batch);
            }

            worldController.level.renderBackButton(batch);

            if (Constants.DEBUG_BUILD)
            {
                renderGuiLevel(batch);
                renderGuiFpsCounter(batch);
            }

            batch.setShader(null);
            batch.end();
        }
    }

    private void renderGuiFpsCounter(SpriteBatch batch)
    {
        float x = camera.viewportWidth - 55;
        float y = camera.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45)
        {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        }
        else if (fps >= 30)
        {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        }
        else
        {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }

        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }

    private void renderGuiLevel(SpriteBatch batch)
    {
        float x = camera.viewportWidth / 2;
        float y = camera.viewportHeight - 30;
        String level = "Level: " + GamePreferences.instance.zone + "-" +
                GamePreferences.instance.stage + "-" +
                GamePreferences.instance.level;

        BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
        fontGameOver.setColor(Constants.WHITE);
        fontGameOver.drawMultiLine(batch, level, x, y, 1, BitmapFont.HAlignment.CENTER);
    }

    private void renderGuiGameOver(SpriteBatch batch)
    {
        float x = camera.viewportWidth / 2;
        float y = camera.viewportHeight / 2;

        BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
        fontGameOver.setColor(Constants.RED);
        fontGameOver.drawMultiLine(batch, "GAME OVER", x, y, 1, BitmapFont.HAlignment.CENTER);
    }

    private void renderGuiGameBeat(SpriteBatch batch)
    {
        float x = camera.viewportWidth / 2;
        float y = camera.viewportHeight / 2;

        BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
        fontGameOver.setColor(Constants.GREEN);
        fontGameOver.drawMultiLine(batch, "YOU WIN\n" + (int) GamePreferences.instance.score, x, y, 1, BitmapFont.HAlignment.CENTER);
    }

    private void renderGuiScore(SpriteBatch batch)
    {
        float x = camera.viewportWidth / 2;
        float y = 0;
        String level = "" + (int) GamePreferences.instance.score;

        BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
        fontGameOver.setColor(Constants.WHITE);
        fontGameOver.drawMultiLine(batch, level, x, y, 1, BitmapFont.HAlignment.CENTER);
    }

    public void resize(int width, int height)
    {
        // TODO: Look at viewport width
        /*camera.viewportWidth = (Gdx.graphics.getHeight() / (float)height) * (float)width;
        camera.update();*/
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        shaderMonochrome.dispose();
    }

}
