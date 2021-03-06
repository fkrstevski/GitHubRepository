package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();

    private MainMenuController worldController;
    private MainMenuRenderer worldRenderer;

    private boolean lerpColor;
    private boolean paused;

    public MenuScreen(DirectedGame game, boolean lerpColor) {
        super(game);
        this.lerpColor = lerpColor;
    }

    @Override
    public void render(float deltaTime) {
        // Do not update game world when paused.
        if (!paused) {
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
        worldController = new MainMenuController(game, lerpColor);
        worldRenderer = new MainMenuRenderer(worldController);
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        worldController.dispose();
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
