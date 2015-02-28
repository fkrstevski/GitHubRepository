package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;

import com.filip.edge.util.CameraHelper;

public class MainMenuController extends InputAdapter implements Disposable
{

    private static final String TAG = MainMenuController.class.getName();
    private final float MAX_ZOOM_TIME = 0.5f;
    public MainMenu mainMenu;
    public CameraHelper cameraHelper;
    private DirectedGame game;
    private float zoomTime;

    public MainMenuController(DirectedGame game)
    {
        this.game = game;
        init();
    }

    private void init()
    {
        cameraHelper = new CameraHelper();
        initMenu();
        zoomTime = 0;
    }

    private void initMenu()
    {
        mainMenu = new MainMenu();
    }

    public void update(float deltaTime)
    {
        mainMenu.update(deltaTime);
        cameraHelper.update(deltaTime);

        if (this.mainMenu.state == MainMenu.MainMenuState.ZoomInToPlay)
        {
            float camZoomSpeed = 2 * deltaTime;
            cameraHelper.addZoom(-camZoomSpeed);

            zoomTime += deltaTime;
            if (zoomTime > MAX_ZOOM_TIME)
            {
                this.mainMenu.state = MainMenu.MainMenuState.Done;
                game.setScreen(new GameScreen(game));
            }
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if (mainMenu.playButton.isTouched(screenX, screenY))
        {
            this.mainMenu.state = MainMenu.MainMenuState.ZoomInToPlay;
        }
        else if (mainMenu.infoButton.isTouched(screenX, screenY))
        {
            Gdx.net.openURI("http://www.nba.com");
        }
        else if (mainMenu.leaderboardButton.isTouched(screenX, screenY))
        {
            if (this.game.activityRequestHandler != null)
            {
                this.game.activityRequestHandler.showScores();
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK)
        {
            Gdx.app.exit();
        }

        return false;
    }

    @Override
    public void dispose()
    {

    }

}
