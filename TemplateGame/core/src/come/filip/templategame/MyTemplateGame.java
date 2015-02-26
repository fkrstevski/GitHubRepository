package come.filip.templategame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import come.filip.templategame.game.Assets;
import come.filip.templategame.game.StageLoader;
import come.filip.templategame.screens.DirectedGame;
import come.filip.templategame.screens.MenuScreen;
import come.filip.templategame.util.AudioManager;
import come.filip.templategame.util.GamePreferences;
import come.filip.templategame.util.IActivityRequestHandler;

public class MyTemplateGame extends DirectedGame
{
    public MyTemplateGame(IActivityRequestHandler activityRequestHandler)
    {
        super(activityRequestHandler);
    }

    @Override
    public void create()
    {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Initialize stage loader
        StageLoader.init();

        // Load assets
        Assets.instance.init(new AssetManager());

        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);

        if (this.activityRequestHandler != null)
        {
            this.activityRequestHandler.login();
        }

        // Start game at menu screen
        setScreen(new MenuScreen(this));
    }

}