package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

public class MenuScreen extends AbstractGameScreen
{

    private static final String TAG = MenuScreen.class.getName();

    private MainMenuController worldController;
    private MainMenuRenderer worldRenderer;

    private boolean paused;

    public MenuScreen(DirectedGame game)
    {
        super(game);
    }

    @Override
    public void render(float deltaTime)
    {
        // Do not update game world when paused.
        if (!paused)
        {
            // Update game world by the time that has passed
            // since last rendered frame.
            worldController.update(deltaTime);
        }
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(Constants.BLUE.r, Constants.BLUE.g, Constants.BLUE.b, Constants.BLUE.a);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        worldRenderer.render();
    }


    @Override
    public void resize(int width, int height)
    {
        worldRenderer.resize(width, height);
    }

    @Override
    public void show()
    {
        GamePreferences.instance.load();
        worldController = new MainMenuController(game);
        worldRenderer = new MainMenuRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide()
    {
        worldController.dispose();
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause()
    {
        paused = true;
    }

    @Override
    public void resume()
    {
        super.resume();
        // Only called on Android!
        paused = false;
    }

    @Override
    public InputProcessor getInputProcessor()
    {
        return worldController;
    }

}