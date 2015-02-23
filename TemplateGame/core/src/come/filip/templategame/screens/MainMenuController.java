package come.filip.templategame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;

import come.filip.templategame.util.CameraHelper;

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
        //cameraHelper.setTarget(mainMenu.playButton);
    }

    public void update(float deltaTime)
    {
        mainMenu.update(deltaTime);
        cameraHelper.update(deltaTime);

        if (this.mainMenu.state == MainMenu.MainMenuState.Active)
        {

        }
        else if (this.mainMenu.state == MainMenu.MainMenuState.ZoomInToInfo)
        {


        }
        else if (this.mainMenu.state == MainMenu.MainMenuState.ZoomInToPlay)
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
        else
        {
            Gdx.app.error(TAG, "INVALID MENU STATE");
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {

        Gdx.app.debug(TAG, "Touch at screenX = " + screenX + " screenY = " + screenY);

        if (mainMenu.playButton.isTouched(screenX, screenY))
        {
            Gdx.app.debug(TAG, "SET TARGET PLAY BUTTON");
            this.mainMenu.state = MainMenu.MainMenuState.ZoomInToPlay;
        }
        else if (mainMenu.infoButton.isTouched(screenX, screenY))
        {
            Gdx.app.debug(TAG, "SET TARGET INFO BUTTON");
            Gdx.net.openURI("http://www.nba.com");
        }
        return false;
    }

    @Override
    public void dispose()
    {

    }

}
