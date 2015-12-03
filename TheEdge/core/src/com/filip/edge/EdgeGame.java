package com.filip.edge;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.filip.edge.game.Assets;
import com.filip.edge.game.StageLoader;
import com.filip.edge.screens.DirectedGame;
import com.filip.edge.screens.GameScreen;
import com.filip.edge.screens.MenuScreen;
import com.filip.edge.screens.ResultsScreen;
import com.filip.edge.util.AudioManager;
import com.filip.edge.util.DigitRenderer;
import com.filip.edge.util.GamePreferences;
import com.filip.edge.util.IActivityRequestHandler;
import com.filip.edge.util.TextureManager;

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

        if (GamePreferences.instance.scoreNeedsToBeSubmitted) {
            setScreen(new ResultsScreen(this));
        } else {
            // Start game at menu screen
            setScreen(new MenuScreen(this, true));
        }
    }

}