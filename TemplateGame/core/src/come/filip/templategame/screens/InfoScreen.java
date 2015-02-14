package come.filip.templategame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;

import come.filip.templategame.screens.transitions.ScreenTransition;
import come.filip.templategame.screens.transitions.ScreenTransitionSlide;
import come.filip.templategame.util.Constants;
import come.filip.templategame.util.GamePreferences;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class InfoScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();

    public InfoScreen (DirectedGame game) {
        super(game);
    }

    @Override
    public void render (float deltaTime) {
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(Constants.BLUE.r, Constants.BLUE.g, Constants.BLUE.b, Constants.BLUE.a);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    @Override
    public void resize (int width, int height) {

    }

    @Override
    public void show () {
        GamePreferences.instance.load();

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide () {

        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {
        super.resume();

    }

    @Override
    public InputProcessor getInputProcessor () {

        InputProcessor ip = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {

                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                    // switch to menu screen
                    ScreenTransition transition = ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
                    game.setScreen(new MenuScreen(game), transition);
                }
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        };

        return ip;

    }

}
