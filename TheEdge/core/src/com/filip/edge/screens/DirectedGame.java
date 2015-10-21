package com.filip.edge.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.util.IActivityRequestHandler;

public abstract class DirectedGame implements ApplicationListener {
    private static final String TAG = DirectedGame.class.getName();
    public SpriteBatch batch;
    protected IActivityRequestHandler activityRequestHandler;
    private boolean init;
    private AbstractGameScreen currScreen;

    public DirectedGame(IActivityRequestHandler activityRequestHandler) {
        this.activityRequestHandler = activityRequestHandler;
    }

    public void setScreen(AbstractGameScreen screen) {
        if (!init) {

            Gdx.app.log(TAG, " create Sprite Batch 1");
            batch = new SpriteBatch();
            Gdx.app.log(TAG, " create Sprite Batch 2");
            init = true;
        }
        if (this.currScreen != null) {
            this.currScreen.hide();
        }
        this.currScreen = screen;
        if (this.currScreen != null) {
            this.currScreen.show();
            this.currScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        Gdx.input.setInputProcessor(currScreen.getInputProcessor());
    }

    public void showAds(boolean show) {
        if (this.activityRequestHandler != null) {
            this.activityRequestHandler.showAds(show);
        }
    }

    public void submitScore(long score) {
        if (this.activityRequestHandler != null) {
            this.activityRequestHandler.submitScore(score);
        }
    }

    public void startMethodTracing(String name){
        if (this.activityRequestHandler != null) {
            this.activityRequestHandler.startMethodTracing(name);
        }
    }

    public void stopMethodTracing(){
        if (this.activityRequestHandler != null) {
            this.activityRequestHandler.stopMethodTracing();
        }
    }

    @Override
    public void render() {
        // get delta time and ensure an upper limit of one 60th second
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);

        // no ongoing transition
        if (currScreen != null) {
            currScreen.render(deltaTime);
        }
    }

    @Override
    public void resize(int width, int height) {
        if (currScreen != null) {
            currScreen.resize(width, height);
        }

    }

    @Override
    public void pause() {
        if (currScreen != null) {
            currScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (currScreen != null) {
            currScreen.resume();
        }
    }

    @Override
    public void dispose() {
        if (currScreen != null) {
            currScreen.hide();
        }
        if (init) {
            currScreen = null;
            Gdx.app.log(TAG, " dispose Sprite Batch 1");
            batch.dispose();
            Gdx.app.log(TAG, " dispose Sprite Batch 2");
            init = false;
        }
    }

}
