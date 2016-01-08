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

        playButton = new PlayButton(width * 0.25f,     // size
                width / 2 - 1,              // x
                height / 2 - 1,             // y
                Constants.WHITE,       // outside color
                Constants.ZONE_COLORS[GamePreferences.instance.zone], false, "CirclePlay" + GamePreferences.instance.zone);       // inside color

        infoButton = new InfoButton(width * 0.05f,   // size
                (int) (width * 0.03),    // x
                height - (int) (width * 0.03),     // y
                Constants.WHITE,         // outside color
                Constants.ZONE_COLORS[GamePreferences.instance.zone], false, "CircleInfo" + GamePreferences.instance.zone);      // inside color

        leaderboardButton = new LeaderboardButton(width * 0.05f,   // size
                width - (int) (width * 0.03),    // x
                height - (int) (width * 0.03),     // y
                Constants.WHITE,         // outside color
                Constants.ZONE_COLORS[GamePreferences.instance.zone], false, "CircleLeaderboard" + GamePreferences.instance.zone);      // inside color
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

            DigitRenderer.instance.renderString("THE EDGE", (int) (width * .3), height / 2, batch, 1);
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
