package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.filip.edge.screens.objects.AbstractRectangleButtonObject;
import com.filip.edge.screens.objects.InfoButton;
import com.filip.edge.screens.objects.LeaderboardButton;
import com.filip.edge.screens.objects.PlayButton;
import com.filip.edge.screens.objects.digits.EightDigit;
import com.filip.edge.screens.objects.digits.FiveDigit;
import com.filip.edge.screens.objects.digits.FourDigit;
import com.filip.edge.screens.objects.digits.NineDigit;
import com.filip.edge.screens.objects.digits.OneDigit;
import com.filip.edge.screens.objects.digits.SevenDigit;
import com.filip.edge.screens.objects.digits.SixDigit;
import com.filip.edge.screens.objects.digits.ThreeDigit;
import com.filip.edge.screens.objects.digits.TwoDigit;
import com.filip.edge.screens.objects.digits.ZeroDigit;
import com.filip.edge.util.Constants;
import com.filip.edge.util.DigitRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class MainMenu
{
    public static final String TAG = MainMenu.class.getName();
    public PlayButton playButton;
    public InfoButton infoButton;
    public LeaderboardButton leaderboardButton;

    public MainMenuState state;

    public MainMenu()
    {
        init();
    }

    private void init()
    {

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        this.state = MainMenuState.Active;

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

        leaderboardButton = new LeaderboardButton((int) (width * 0.05f),   // size
                width - (int) (width * 0.03),    // x
                height - (int) (width * 0.03),     // y
                Constants.WHITE,         // outside color
                Constants.BLUE);      // inside color

    }

    public void update(float deltaTime)
    {

        if (this.state == MainMenuState.Active)
        {
            playButton.update(deltaTime);
            infoButton.update(deltaTime);
            leaderboardButton.update(deltaTime);
        }
        else if (this.state == MainMenuState.ZoomInToPlay)
        {
            playButton.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch)
    {
        if (this.state == MainMenuState.Active)
        {
            playButton.render(batch);
            infoButton.render(batch);
            leaderboardButton.render(batch);



        }
        else if (this.state == MainMenuState.ZoomInToPlay)
        {
            playButton.render(batch);
        }
    }

    enum MainMenuState
    {
        Active,
        ZoomInToPlay,
        Done
    }
}
