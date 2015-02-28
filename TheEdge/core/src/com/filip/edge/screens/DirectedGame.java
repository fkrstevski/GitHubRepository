package com.filip.edge.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.filip.edge.util.IActivityRequestHandler;

public abstract class DirectedGame implements ApplicationListener
{

    protected IActivityRequestHandler activityRequestHandler;
    private boolean init;
    private AbstractGameScreen currScreen;
    private SpriteBatch batch;

    public DirectedGame(IActivityRequestHandler activityRequestHandler)
    {
        this.activityRequestHandler = activityRequestHandler;
    }

    public void setScreen(AbstractGameScreen screen)
    {
        if (!init)
        {

            batch = new SpriteBatch();
            init = true;
        }
        if (this.currScreen != null)
        {
            this.currScreen.hide();
        }
        this.currScreen = screen;
        if (this.currScreen != null)
        {
            this.currScreen.show();
            this.currScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        Gdx.input.setInputProcessor(currScreen.getInputProcessor());
    }

    public void submitScore(int score)
    {
        if (this.activityRequestHandler != null)
        {
            this.activityRequestHandler.submitScore(score);
        }
    }

    @Override
    public void render()
    {
        // get delta time and ensure an upper limit of one 60th second
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);

        // no ongoing transition
        if (currScreen != null)
        {
            currScreen.render(deltaTime);
        }
    }

    @Override
    public void resize(int width, int height)
    {
        if (currScreen != null)
        {
            currScreen.resize(width, height);
        }

    }

    @Override
    public void pause()
    {
        if (currScreen != null)
        {
            currScreen.pause();
        }
    }

    @Override
    public void resume()
    {
        if (currScreen != null)
        {
            currScreen.resume();
        }
    }

    @Override
    public void dispose()
    {
        if (currScreen != null)
        {
            currScreen.hide();
        }
        if (init)
        {
            currScreen = null;
            batch.dispose();
            init = false;
        }
    }

}
