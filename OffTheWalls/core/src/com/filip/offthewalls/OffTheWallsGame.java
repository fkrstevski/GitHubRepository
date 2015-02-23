package com.filip.offthewalls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.filip.offthewalls.game.StageLoader;
import com.filip.offthewalls.game.WorldController;
import com.filip.offthewalls.game.WorldRenderer;
import com.filip.offthewalls.util.Constants;

public class OffTheWallsGame extends ApplicationAdapter
{
    private static final String TAG = OffTheWallsGame.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    private boolean paused;

    @Override public void create ()
    {
        // Set Libgdx log level to DEBUG
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Initialize stage loader
        StageLoader.init();

        // Initialize controller and renderer
        worldController = new WorldController(0, 1, 0);
        worldRenderer = new WorldRenderer(worldController);

        // Game world is active on start
        paused = false;
    }

    @Override public void render ()
    {
        // Do not update game world when paused.
        if (!paused)
        {
            // Update game world by the time that has passed
            // since last rendered frame.
            worldController.update(Gdx.graphics.getDeltaTime());

            //Gdx.app.debug(TAG, "FPS: " + Gdx.graphics.getFramesPerSecond());
        }


        // Render game world to screen
        worldRenderer.render();
    }

    @Override public void resize (int width, int height)
    {
        worldRenderer.resize(width, height);
    }

    @Override public void pause ()
    {
        paused = true;
    }

    @Override public void resume ()
    {
        paused = false;
    }

    @Override public void dispose ()
    {
        worldRenderer.dispose();
    }
}
