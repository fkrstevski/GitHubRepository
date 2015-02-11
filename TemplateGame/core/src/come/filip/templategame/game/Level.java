package come.filip.templategame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import come.filip.templategame.game.objects.Ball;
import come.filip.templategame.game.objects.Clouds;
import come.filip.templategame.game.objects.Goal;

public class Level {

	public static final String TAG = Level.class.getName();

	// player character
	public Ball ball;

	// objects
	//public Array<Rock> rocks;

	// decoration
	public Clouds clouds;

    public Goal goal;

	public Level (String filename) {
		init(filename);
	}

	private void init (String filename) {
		// player character
		ball = new Ball();
        ball.position.set(0,20);
        ball.scale.set(100,100);


        // load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

		// objects
		//rocks = new Array<Rock>();

        // TODO:
        goal = new Goal();
        goal.position.set(0, 0);
        goal.scale.set(100,100);

		// decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
        clouds.scale.set(100,100);

		// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	public void update (float deltaTime) {
		// Bunny Head
		ball.update(deltaTime);
		// Rocks
		//for (Rock rock : rocks)
		//	rock.update(deltaTime);

		// Clouds
		clouds.update(deltaTime);
	}

	public void render (SpriteBatch batch) {

		// Draw Rocks
		//for (Rock rock : rocks)
		//	rock.render(batch);

        // Draw Goal
        goal.render(batch);

		// Draw Clouds
		clouds.render(batch);

        ball.render(batch);
	}

}
