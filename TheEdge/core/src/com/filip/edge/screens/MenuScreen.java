package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();

    private MainMenuController worldController;
    private MainMenuRenderer worldRenderer;
    private Color startingColor;
    private static final float colorLerpTime = 1;
    private float currentTime;

    private boolean paused;

    public MenuScreen(DirectedGame game, boolean lerpColor) {
        super(game);
        if(lerpColor) {
            startingColor = new Color(Constants.WHITE);
        }
        else {
            startingColor = Constants.ZONE_COLORS[GamePreferences.instance.zone];
        }
    }

    @Override
    public void render(float deltaTime) {
        // Do not update game world when paused.
        if (!paused) {
            currentTime += deltaTime;
            if(currentTime < colorLerpTime){
                startingColor.lerp(Constants.ZONE_COLORS[GamePreferences.instance.zone], currentTime / colorLerpTime);
            }

            // Sets the clear screen color
            Gdx.gl.glClearColor(startingColor.r,
                    startingColor.g,
                    startingColor.b,
                    startingColor.a);

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
        worldController = new MainMenuController(game);
        worldRenderer = new MainMenuRenderer(worldController);
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
