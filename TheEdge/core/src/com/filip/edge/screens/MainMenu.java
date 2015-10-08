package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.screens.objects.InfoButton;
import com.filip.edge.screens.objects.LeaderboardButton;
import com.filip.edge.screens.objects.PlayButton;
import com.filip.edge.util.Constants;
import com.filip.edge.util.DigitRenderer;
import com.filip.edge.util.GamePreferences;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class MainMenu {
    public static final String TAG = MainMenu.class.getName();
    public PlayButton playButton;
    public InfoButton infoButton;
    public LeaderboardButton leaderboardButton;

    public MainMenuState state;

    public MainMenu() {
        init();
    }

    private void init() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        this.state = MainMenuState.Active;

        int size = Constants.getOdd((int) (width * 0.25f));
        Gdx.app.log(TAG, "playButton size = " + size);
        playButton = new PlayButton(size,     // size
                width / 2 - 1,              // x
                height / 2 - 1,             // y
                Constants.WHITE,       // outside color
                Constants.ZONE_COLORS[GamePreferences.instance.zone]);       // inside color

        size = Constants.getOdd((int) (width * 0.05f));
        Gdx.app.log(TAG, "infoButton size = " + size);
        infoButton = new InfoButton(size,   // size
                (int) (width * 0.03),    // x
                height - (int) (width * 0.03),     // y
                Constants.WHITE,         // outside color
                Constants.ZONE_COLORS[GamePreferences.instance.zone]);      // inside color

        size = Constants.getOdd((int) (width * 0.05f));
        Gdx.app.log(TAG, "leaderboardButton size = " + size);
        leaderboardButton = new LeaderboardButton(size,   // size
                width - (int) (width * 0.03),    // x
                height - (int) (width * 0.03),     // y
                Constants.WHITE,         // outside color
                Constants.ZONE_COLORS[GamePreferences.instance.zone]);      // inside color

    }

    public void update(float deltaTime) {

        if (this.state == MainMenuState.Active) {
            playButton.update(deltaTime);
            infoButton.update(deltaTime);
            leaderboardButton.update(deltaTime);
        } else if (this.state == MainMenuState.ZoomInToPlay) {
            playButton.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        if (this.state == MainMenuState.Active) {
            int width = Gdx.graphics.getWidth();
            int height = Gdx.graphics.getHeight();

            playButton.render(batch);
            infoButton.render(batch);
            leaderboardButton.render(batch);

            DigitRenderer.instance.renderString("THE EDGE", (int) (width * .3), height / 2, batch);

            /*
            //Test numbers and font
            String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            DigitRenderer.instance.renderString(str, (int) (width - width / 54), height - 100, batch);
            str = "FILIP AND MATT";
            DigitRenderer.instance.renderString(str, (int) (width - width / 54), height - 200, batch);
            str = "ALIMA";
            DigitRenderer.instance.renderString(str, (int) (width - width / 54), height - 300, batch);
            str = "YOU WIN";
            DigitRenderer.instance.renderString(str, (int) (width - width / 54), height - 400, batch);
            str = "0123456789";
            DigitRenderer.instance.renderNumber(str, (int) (width - width / 54), height - 500, batch);
            str = "ZIPPER";
            DigitRenderer.instance.renderString(str, (int) (width - width / 54), height - 600, batch);
            */

        } else if (this.state == MainMenuState.ZoomInToPlay) {
            playButton.render(batch);
        }
    }

    enum MainMenuState {
        Active,
        ZoomInToPlay,
        Done
    }
}
