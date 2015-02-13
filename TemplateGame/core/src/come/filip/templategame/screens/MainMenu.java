package come.filip.templategame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import come.filip.templategame.game.objects.Ball;
import come.filip.templategame.game.objects.Clouds;
import come.filip.templategame.game.objects.Goal;
import come.filip.templategame.screens.objects.InfoButton;
import come.filip.templategame.screens.objects.PlayButton;
import come.filip.templategame.util.Constants;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class MainMenu {
    enum MainMenuState{
        Active,
        ZoomInToPlay,
        ZoomInToInfo,
        Done
    }

    public static final String TAG = MainMenu.class.getName();

    public PlayButton playButton;
    public InfoButton infoButton;

    public MainMenuState state;

    public MainMenu () {
        init();
    }

    private void init () {

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        this.state = MainMenuState.Active;

        // player character
        playButton = new PlayButton((int) (width * 0.25f),     // size
                                    width / 2,              // x
                                    height / 2,             // y
                                    Constants.WHITE,       // outside color
                                    Constants.BLUE);       // inside color

        infoButton = new InfoButton((int) (width * 0.05f),   // size
                                    (int) (width * 0.03),    // x
                                    height - (int) (width * 0.03),     // y
                                    Constants.WHITE,         // outside color
                                    Constants.BLUE);      // inside color
    }

    public void update (float deltaTime) {

        if(this.state == MainMenuState.Active) {
            playButton.update(deltaTime);
            infoButton.update(deltaTime);
        }
        else if(this.state == MainMenuState.ZoomInToInfo)
        {
            infoButton.update(deltaTime);
        }
        else if(this.state == MainMenuState.ZoomInToPlay)
        {
            playButton.update(deltaTime);
        }
        else{
            Gdx.app.error(TAG, "INVALID MENU STATE");
        }
    }

    public void render (SpriteBatch batch)
    {
        if(this.state == MainMenuState.Active) {
            playButton.render(batch);
            infoButton.render(batch);
        }
        else if(this.state == MainMenuState.ZoomInToInfo)
        {
            infoButton.render(batch);
        }
        else if(this.state == MainMenuState.ZoomInToPlay)
        {
            playButton.render(batch);
        }
        else{
            Gdx.app.error(TAG, "INVALID MENU STATE");
        }
    }
}
