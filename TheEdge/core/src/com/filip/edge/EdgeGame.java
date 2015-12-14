package com.filip.edge;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.filip.edge.game.Assets;
import com.filip.edge.game.StageLoader;
import com.filip.edge.screens.DirectedGame;
import com.filip.edge.screens.LevelResultsScreen;
import com.filip.edge.screens.MenuScreen;
import com.filip.edge.screens.ResultsScreen;
import com.filip.edge.util.*;

public class EdgeGame extends DirectedGame {
    private static final String TAG = EdgeGame.class.getName();
    public static final GamePreferences.AdType adType = GamePreferences.AdType.NONE;

    public EdgeGame(IActivityRequestHandler activityRequestHandler) {
        super(activityRequestHandler);
    }

    @Override
    public void create() {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Initialize stage loader
        StageLoader.init();

        // Load assets
        Assets.instance.init(new AssetManager());

        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();

        TextureManager.instance.load();

        DigitRenderer.instance.load();

        AudioManager.instance.play(Assets.instance.music.song01);

        if (this.activityRequestHandler != null) {
            this.activityRequestHandler.login();
        }

        TwitterManager.instance.load();

        if (GamePreferences.instance.scoreNeedsToBeSubmitted) {
            setScreen(new ResultsScreen(this));
        } else {
            // Start game at menu screen
            setScreen(new MenuScreen(this, true));
        }
    }

    public void onCompleteRewardVideoAd(String tag) {
        Gdx.app.log(TAG, "onCompleteRewardVideoAd " + tag);
        if(getCurrScreen() instanceof LevelResultsScreen){
            ((LevelResultsScreen) getCurrScreen()).giveVideoReward();
        }
    }

    public void onIncompleteRewardVideoAd(String tag) {
        Gdx.app.log(TAG, "onIncompleteRewardVideoAd " + tag);
    }

    public void onShowAd(String tag) {
        Gdx.app.log(TAG, "onShowAd " + tag);
    }

    public void onClickAd(String tag) {
        Gdx.app.log(TAG, "onClickAd " + tag);
    }

    public void onHideAd(String tag) {
        Gdx.app.log(TAG, "onHideAd " + tag);
    }

    public void onFailedToShowAd(String tag) {
        Gdx.app.log(TAG, "onFailedToShowAd " + tag);
    }

    public void onReceivedAd(String tag) {
        Gdx.app.log(TAG, "onReceivedAd " + tag);
    }

    public void onFailedToReceiveAd(String tag) {
        Gdx.app.log(TAG, "onFailedToReceiveAd " + tag);
    }

    public void onAudioStartedForAd() {
        Gdx.app.log(TAG, "onAudioStartedForAd");
    }

    public void onAudioFinishedForAd() {
        Gdx.app.log(TAG, "onAudioFinishedForAd");
    }
}