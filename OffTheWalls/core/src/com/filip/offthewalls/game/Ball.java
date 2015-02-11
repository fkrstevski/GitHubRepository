package com.filip.offthewalls.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class Ball
{
    private static final String TAG = Ball.class.getName();

    private Vector2 position;

    public Ball(Vector2 pos)
    {
        this.position = new Vector2(pos);
    }

    public float getX()
    {
        return this.position.x;
    }

    public float getY()
    {
        return this.position.y;
    }

    public void update(float dt)
    {
        float x = Gdx.input.getAccelerometerX();
        float y = Gdx.input.getAccelerometerY();

        position.add(-y * 70 * dt, x * 70 * dt);
    }
}
