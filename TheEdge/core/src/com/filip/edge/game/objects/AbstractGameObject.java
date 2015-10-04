package com.filip.edge.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.filip.edge.util.Constants;

public abstract class AbstractGameObject
{

    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    public Vector2 velocity;
    public Vector2 terminalVelocity;
    public Vector2 friction;
    public Vector2 acceleration;
    public boolean visible;

    public Body body;

    public AbstractGameObject()
    {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
        velocity = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        friction = new Vector2();
        acceleration = new Vector2();
        visible = true;
    }

    public void update(float deltaTime)
    {
        if (body != null)
        {
            position.set(new Vector2(body.getPosition().x * Constants.BOX2D_SCALE, body.getPosition().y * Constants.BOX2D_SCALE));
            rotation = body.getAngle() * MathUtils.radiansToDegrees;
        }
    }

    public abstract void render(SpriteBatch batch);

}
