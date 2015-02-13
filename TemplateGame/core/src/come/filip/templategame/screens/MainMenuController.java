package come.filip.templategame.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;

import come.filip.templategame.util.CameraHelper;

public class MainMenuController extends InputAdapter implements Disposable {

    private static final String TAG = MainMenuController.class.getName();
    private final float MAX_ZOOM_TIME = 0.5f;

    private DirectedGame game;
    public MainMenu mainMenu;

    private float zoomTime;

    public CameraHelper cameraHelper;

    public MainMenuController (DirectedGame game) {
        this.game = game;
        init();
    }

    private void init () {
        cameraHelper = new CameraHelper();
        initMenu();
        zoomTime = 0;
    }

    private void initMenu () {
        mainMenu = new MainMenu();
        //cameraHelper.setTarget(mainMenu.playButton);
    }
    public void update (float deltaTime) {
        handleInput(deltaTime);

        mainMenu.update(deltaTime);
        cameraHelper.update(deltaTime);

        if(this.mainMenu.state == MainMenu.MainMenuState.Active) {

        }
        else if(this.mainMenu.state == MainMenu.MainMenuState.ZoomInToInfo)
        {


        }
        else if(this.mainMenu.state == MainMenu.MainMenuState.ZoomInToPlay)
        {
            float camZoomSpeed = 2 * deltaTime;
            cameraHelper.addZoom(-camZoomSpeed);
            zoomTime += deltaTime;
            if(zoomTime > MAX_ZOOM_TIME) {
                game.setScreen(new GameScreen(game));
            }
        }
        else{
            Gdx.app.error(TAG, "INVALID MENU STATE");
        }
    }

    private void handleInput (float deltaTime) {
        if (Gdx.app.getType() != ApplicationType.Desktop) return;

        // Camera Controls (move)
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
        if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
        if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
    }

    private void moveCamera (float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    @Override
    public boolean keyUp (int keycode) {
        // Reset game world
        if (keycode == Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {

        Gdx.app.debug(TAG, "Touch at screenX = " + screenX + " screenY = " + screenY);

        if(mainMenu.playButton.isTouched(screenX, screenY))
        {
            Gdx.app.debug(TAG, "SET TARGET PLAY BUTTON");
            //cameraHelper.setTarget(mainMenu.playButton);
            this.mainMenu.state = MainMenu.MainMenuState.ZoomInToPlay;
            //game.setScreen(new GameScreen(game));
        }
        else if (mainMenu.infoButton.isTouched(screenX, screenY))
        {
            Gdx.app.debug(TAG, "SET TARGET INFO BUTTON");

            //cameraHelper.setTarget(mainMenu.infoButton);
            //this.mainMenu.state = MainMenu.MainMenuState.ZoomInToInfo;
            //cameraHelper.setPosition(screenX, screenY);
            Gdx.net.openURI("http://www.nba.com");
        }
        return false;
    }
    @Override
    public void dispose () {

    }

}
