package come.filip.templategame.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import come.filip.templategame.screens.objects.AbstractButtonObject;
import come.filip.templategame.screens.objects.AbstractCircleButtonObject;


public class Ball extends AbstractCircleButtonObject {

	public static final String TAG = Ball.class.getName();

	public Ball(int size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, x, y, outsideColor, insideColor);
		//init();
	}

/*	public void init () {
		dimension.set(1, 1);

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);

		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);

		// Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
	}
*/
    @Override
    public void fillInside(int size)
    {

    }

    @Override
    public void update(float deltaTime)
    {
        float x = Gdx.input.getAccelerometerX();
        float y = Gdx.input.getAccelerometerY();

        position.add(y * 70 * deltaTime, x * 70 * deltaTime);
    }
}
