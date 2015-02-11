package come.filip.templategame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import come.filip.templategame.game.Assets;
import come.filip.templategame.screens.DirectedGame;
import come.filip.templategame.screens.MenuScreen;
import come.filip.templategame.screens.transitions.ScreenTransition;
import come.filip.templategame.screens.transitions.ScreenTransitionSlice;
import come.filip.templategame.util.AudioManager;
import come.filip.templategame.util.GamePreferences;

public class MyTemplateGame extends DirectedGame {

    @Override
    public void create () {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Load assets
        Assets.instance.init(new AssetManager());

        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);

        // Start game at menu screen
        ScreenTransition transition = ScreenTransitionSlice.init(2, ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
        setScreen(new MenuScreen(this), transition);
    }

}