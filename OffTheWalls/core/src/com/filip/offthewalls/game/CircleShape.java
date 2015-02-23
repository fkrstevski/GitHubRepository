package com.filip.offthewalls.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.filip.offthewalls.util.Constants;

/**
 * Created by fkrstevski on 2015-02-10.
 */
public class CircleShape extends Shape
{
    private static final String TAG = CircleShape.class.getName();

    enum CircleType
    {
        First,
        Middle,
        Last
    }

    private CircleType type;

    private Vector2 position;
    private float radius;

    public CircleShape(Vector2 pos, float r, CircleType t)
    {
        this.position = pos;
        this.radius = r;
        this.type = t;
    }

    @Override
    public boolean containsBall(Ball ball, float levelMult)
    {
        float distance = Vector2.dst(ball.getX(), ball.getY(), this.position.x, this.position.y );

        if ((distance <= Math.abs(Constants.BALL_RADIUS - Constants.END_CIRCLE_OUTLINE_RADIUS * levelMult)))
        {
            return  true;
        }
        return false;
    }

    public CircleType getType()
    {
        return type;
    }

    public float getRadius() {
        return radius;
    }

    public Vector2 getPosition() {
        return position;
    }
}
