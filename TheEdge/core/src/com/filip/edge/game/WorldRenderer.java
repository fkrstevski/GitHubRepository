/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Align;
import com.filip.edge.util.Constants;
import com.filip.edge.util.DigitRenderer;
import com.filip.edge.util.GamePreferences;

public class WorldRenderer implements Disposable {

    private static final String TAG = WorldRenderer.class.getName();

    private OrthographicCamera camera;
    private WorldController worldController;
    private Box2DDebugRenderer b2debugRenderer;

    private String score;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.setToOrtho(true); // flip y-axis
        camera.update();
        b2debugRenderer = new Box2DDebugRenderer();
    }

    public void render(SpriteBatch batch) {
        renderWorld(batch);
    }

    private void renderWorld(SpriteBatch batch) {
        worldController.cameraHelper.applyTo(camera);

        batch.begin();
        worldController.level.renderBackButton(batch);
        if(worldController.state != WorldController.LevelState.GameOver){
            renderGuiScore(batch);
        }

        if (Constants.DEBUG_BUILD) {
            renderGuiLevel(batch);
            renderGuiFpsCounter(batch);
        }
        if(worldController.state != WorldController.LevelState.InterstitialAd) {
            worldController.level.render(batch);
        }

        batch.setShader(null);
        batch.end();

        if (worldController.renderPhysics) {
            b2debugRenderer.render(worldController.b2world, camera.combined.scl(Constants.BOX2D_SCALE));
        }
    }

    private void renderGuiFpsCounter(SpriteBatch batch) {
        float x = camera.viewportWidth - 55;
        float y = camera.viewportHeight - 15;
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

        fpsFont.draw(batch, " " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }

    private void renderGuiLevel(SpriteBatch batch) {
        float x = camera.viewportWidth / 2;
        float y = camera.viewportHeight - 30;
        String level = "Level: " + GamePreferences.instance.zone + "-" +
                GamePreferences.instance.stage + "-" +
                GamePreferences.instance.level;

        BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
        fontGameOver.setColor(Constants.WHITE);
        fontGameOver.draw(batch, level, x, y);
    }

    private void renderGuiScore(SpriteBatch batch) {
        int y = (int)(DigitRenderer.instance.digitHeight / 2) +
                DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS;
        int x = (int) (camera.viewportWidth - DigitRenderer.instance.digitWidth / 2 - DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS);
        if (GamePreferences.instance.currentScore == Constants.MAX_SCORE) {
            score = "" + GamePreferences.instance.currentScore;
            String scoreSub = score.substring((int) (score.length() - this.worldController.readyTimeRatio * score.length()));
            DigitRenderer.instance.renderNumber(scoreSub, x, y, batch);
        } else {
            DigitRenderer.instance.renderNumber(GamePreferences.instance.currentScore, x, y, batch);
        }
    }

    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

}
