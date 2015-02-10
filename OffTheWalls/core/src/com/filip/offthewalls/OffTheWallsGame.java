package com.filip.offthewalls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.filip.offthewalls.game.WorldController;
import com.filip.offthewalls.game.WorldRenderer;

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

        // Initialize controller and renderer
        worldController = new WorldController();
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

        }

        // Sets the clear screen color to Blue
        Gdx.gl.glClearColor(0x00/255.0f, 0xa0/255.0f, 0xda/255.0f, 0xff/255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
