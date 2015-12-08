package com.filip.edge.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.util.IActivityRequestHandler;
import com.filip.edge.util.MyTwitterConfig;
import de.tomgrill.gdxtwitter.core.TwitterAPI;

public abstract class DirectedGame implements ApplicationListener {
    private static final String TAG = DirectedGame.class.getName();
    public SpriteBatch batch;
    protected IActivityRequestHandler activityRequestHandler;
    private boolean init;
    private AbstractGameScreen currScreen;
    private AbstractGameScreen nextScreen;

    public DirectedGame(IActivityRequestHandler activityRequestHandler) {
        this.activityRequestHandler = activityRequestHandler;
    }

    public AbstractGameScreen getCurrScreen() {
        return this.currScreen;
    }

    public void setScreen(AbstractGameScreen screen) {
        if (!init) {
            batch = new SpriteBatch();
            init = true;
        }

        nextScreen = screen;
        nextScreen.show();
        nextScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        nextScreen.render(0);

        if (this.currScreen != null) {
            this.currScreen.pause();
        }

        nextScreen.pause();

        if (this.currScreen != null) {
            this.currScreen.hide();
        }

        nextScreen.resume();

        this.currScreen = nextScreen;

        Gdx.input.setInputProcessor(currScreen.getInputProcessor());
    }

    public void showAds(boolean show) {
        if (this.activityRequestHandler != null) {
            this.activityRequestHandler.showAds(show);
        }
    }

    public void showInterstitialAd() {
        if(this.activityRequestHandler != null) {
            this.activityRequestHandler.showInterstitialAd();
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
