package com.filip.edge;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Base64Coder;
import com.filip.edge.game.Assets;
import com.filip.edge.game.StageLoader;
import com.filip.edge.screens.DirectedGame;
import com.filip.edge.screens.GameScreen;
import com.filip.edge.screens.MenuScreen;
import com.filip.edge.screens.ResultsScreen;
import com.filip.edge.util.*;
import de.tomgrill.gdxtwitter.core.TwitterAPI;
import de.tomgrill.gdxtwitter.core.TwitterConfig;
import de.tomgrill.gdxtwitter.core.TwitterResponseListener;
import de.tomgrill.gdxtwitter.core.TwitterSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class EdgeGame extends DirectedGame {
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



}