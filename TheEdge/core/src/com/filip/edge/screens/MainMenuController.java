package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.filip.edge.util.CameraHelper;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

public class MainMenuController extends InputAdapter implements Disposable {

    private static final String TAG = MainMenuController.class.getName();
    private final float MAX_ZOOM_TIME = 0.5f;
    public MainMenu mainMenu;
    public CameraHelper cameraHelper;
    private DirectedGame game;
    private float zoomTime;

    public Color startingColor;
    private static final float colorLerpTime = 1;
    private float currentTime;

    public MainMenuController(DirectedGame game, boolean lerpColor) {
        this.game = game;
        init();
        if (lerpColor) {
            startingColor = new Color(Constants.WHITE);
        } else {
            startingColor = Constants.ZONE_COLORS[GamePreferences.instance.zone];
        }
    }

    private void init() {
        cameraHelper = new CameraHelper();
        initMenu();
        zoomTime = 0;
        GamePreferences.instance.getUserID();
    }

    private void initMenu() {
        mainMenu = new MainMenu();
    }

    public void update(float deltaTime) {
        currentTime += deltaTime;
        if (currentTime < colorLerpTime) {
            startingColor.lerp(Constants.ZONE_COLORS[GamePreferences.instance.zone], currentTime / colorLerpTime);
        }

        mainMenu.update(deltaTime);
        cameraHelper.update(deltaTime);

        if (this.mainMenu.state == MainMenu.MainMenuState.ZoomInToPlay) {
            float camZoomSpeed = 2 * deltaTime;
            cameraHelper.addZoom(-camZoomSpeed);

            zoomTime += deltaTime;
            if (zoomTime > MAX_ZOOM_TIME) {
                this.mainMenu.state = MainMenu.MainMenuState.Done;
                game.setScreen(new GameScreen(game));
            }
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (mainMenu.playButton.isTouched(screenX, screenY)) {
            this.mainMenu.state = MainMenu.MainMenuState.ZoomInToPlay;
        } else if (mainMenu.infoButton.isTouched(screenX, screenY)) {
            //Gdx.net.openURI("http://www.absolutegames.ca/TheEdgeShowScores.php");

            // Used to beat the game early
            GamePreferences.instance.zone = 0;
            GamePreferences.instance.stage = 0;
            GamePreferences.instance.scoreNeedsToBeSubmitted = true;
            // Make sure we save the highest score ASAP
            GamePreferences.instance.save();
            this.game.submitScore(GamePreferences.instance.currentScore);
            game.setScreen(new ResultsScreen(game));

            //TwitterManager.instance.uploadPhoto();
        } else if (mainMenu.leaderboardButton.isTouched(screenX, screenY)) {
            if (this.game.activityRequestHandler != null) {
                this.game.activityRequestHandler.showScores();
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            Gdx.app.exit();
        }

        return false;
    }

    @Override
    public void dispose() {

    }

}
