package come.filip.templategame.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import come.filip.templategame.game.Assets;


public class Ball extends AbstractGameObject {

	public static final String TAG = Ball.class.getName();

	private Animation animNormal;

	public Ball() {
		init();
	}

	public void init () {
		dimension.set(1, 1);

		animNormal = Assets.instance.bunny.animNormal;
		setAnimation(animNormal);

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);

		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);

		// Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
	}


	@Override
	public void render (SpriteBatch batch) {
		TextureRegion reg = null;

		float dimCorrectionX = 0;
		float dimCorrectionY = 0;
		if (animation != animNormal) {
			dimCorrectionX = 0.05f;
			dimCorrectionY = 0.2f;
		}

		// Draw image
		reg = animation.getKeyFrame(stateTime, true);

		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x + dimCorrectionX, dimension.y
			+ dimCorrectionY, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
			reg.getRegionHeight(), true, false);

		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}
}
