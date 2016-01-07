package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.filip.edge.screens.objects.BackButton;
import com.filip.edge.util.Constants;
import com.filip.edge.util.DigitRenderer;
import com.filip.edge.util.GamePreferences;

/**
 * Created by fkrstevski on 2015-12-23.
 */
public class InterstitialScreen extends AbstractGameScreen{
    private DirectedGame game;

    private static final float transitionTime = 0.4f;
    private float currentTime;
    private boolean showAd;

    public InterstitialScreen(DirectedGame game) {
        super(game);
        this.game = game;
        this.currentTime = 0;
        this.showAd = false;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {

        // Sets the clear screen color
        Gdx.gl.glClearColor(
                Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a);

        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.currentTime+=deltaTime;
        if(!this.showAd) {
            if(this.currentTime > transitionTime) {
                this.currentTime = 0;
                this.showAd = true;
                if(game.hasVideoAd()) {
                    game.showVideoAd();
                }
                else if (game.hasInterstitialAd()) {
                    game.showInterstitialAd();
                }
                else {
                    game.setScreen(new GameScreen(game));
                }
            }
        }
        else {
            if(this.currentTime > transitionTime) {
                this.currentTime = 0;
                game.setScreen(new GameScreen(game));
            }
        }
    }

    @Override
    public void hide() {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}
