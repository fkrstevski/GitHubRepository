package come.filip.templategame.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import come.filip.templategame.screens.objects.AbstractCircleButtonObject;


public class Ball extends AbstractCircleButtonObject
{

    public static final String TAG = Ball.class.getName();

    public Ball(int size, float x, float y, Color outsideColor, Color insideColor)
    {
        super(size, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int size)
    {

    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        float x = Gdx.input.getAccelerometerX();
        float y = Gdx.input.getAccelerometerY();

        //position.add(y * 70 * deltaTime, x * 70 * deltaTime);

        //this.body.applyForceToCenter(y * 70 * deltaTime, x * 70 * deltaTime, false);
        //this.body.applyLinearImpulse(y * 70 * deltaTime, x * 70 * deltaTime, this.origin.x, this.origin.y, false);
        //this.body.setLinearVelocity(y * 700 * deltaTime, x * 700 * deltaTime);
        //this.body.setLinearVelocity(this.body.getLinearVelocity().x + y * 70 * deltaTime, this.body.getLinearVelocity().y + x * 70 * deltaTime);
    }
}
