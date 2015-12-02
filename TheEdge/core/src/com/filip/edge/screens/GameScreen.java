package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.filip.edge.game.WorldController;
import com.filip.edge.game.WorldRenderer;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

public class GameScreen extends AbstractGameScreen {

    private static final String TAG = GameScreen.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    private boolean paused;

    public GameScreen(DirectedGame game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        // Do not update game world when paused.
        if (!paused) {
            if(worldController.colorChange) {
                Gdx.gl.glClearColor(worldController.clearColor.r,
                        worldController.clearColor.g,
                        worldController.clearColor.b,
                        worldController.clearColor.a);
            }
            else {
                // Sets the clear screen color
                Gdx.gl.glClearColor(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                        Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                        Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                        Constants.ZONE_COLORS[GamePreferences.instance.zone].a);
            }


            // Clears the screen
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Render game world to screen
            worldRenderer.render(game.batch);

            // Update game world by the time that has passed
            // since last rendered frame.
            worldController.update(deltaTime);
        }
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void show() {
        //GamePreferences.instance.load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {

        worldRenderer.dispose();
        worldController.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        // Only called on Android!
        paused = false;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController;
    }

}
