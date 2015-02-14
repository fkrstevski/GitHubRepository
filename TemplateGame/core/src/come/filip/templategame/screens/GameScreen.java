package come.filip.templategame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import come.filip.templategame.game.WorldController;
import come.filip.templategame.game.WorldRenderer;
import come.filip.templategame.util.GamePreferences;

public class GameScreen extends AbstractGameScreen {

	private static final String TAG = GameScreen.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;

	private boolean paused;

	public GameScreen (DirectedGame game) {
		super(game);
	}

	@Override
	public void render (float deltaTime) {
		// Do not update game world when paused.
		if (!paused) {
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(deltaTime);
		}

        Color bgColor = worldController.getBgColor();

		// Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Render game world to screen
		worldRenderer.render();
	}

	@Override
	public void resize (int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show () {
		GamePreferences.instance.load();
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide () {
		worldController.dispose();
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void pause () {
		paused = true;
	}

	@Override
	public void resume () {
		super.resume();
		// Only called on Android!
		paused = false;
	}

	@Override
	public InputProcessor getInputProcessor () {
		return worldController;
	}

}
