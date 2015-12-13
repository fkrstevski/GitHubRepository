package com.filip.edge.screens.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.util.Constants;

/**
 * Created by fkrstevski on 2015-10-10.
 */
public class Orbiter extends EmptyCircle {
    public static final String TAG = Orbiter.class.getName();

    public float angle;
    public Ball ball;
    public boolean deactivate;
    public float startingAngle;

    public Orbiter(float size, float x, float y, Color outsideColor, Color insideColor, Ball ball, boolean vertical, boolean shared, String region) {
        super(size, x, y, outsideColor, insideColor, shared, region);
        this.ball = ball;
        this.startingAngle = angle;
        if (vertical) {
            this.scale.set(1, 0.3f);
        } else {
            this.scale.set(0.3f, 1);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (deactivate) {
            this.body.setActive(false);
            deactivate = false;
            this.visible = false;
        }

        int width = Gdx.graphics.getWidth();
        float horizontalScale = width / Constants.BASE_SCREEN_WIDTH;
        angle += Constants.ORBITER_ANGULAR_VELOCITY * deltaTime;
        float xPos = this.ball.position.x;
        float yPos = this.ball.position.y;
        this.position.set(xPos, yPos);
        this.body.setTransform(xPos / Constants.BOX2D_SCALE, yPos / Constants.BOX2D_SCALE, 0);
        this.rotation = angle;

    }

    public void destroy() {
        this.deactivate = true;
    }

    @Override
    public void reset() {
        this.body.setActive(false);
        deactivate = false;
        this.visible = false;

        int width = Gdx.graphics.getWidth();
        float horizontalScale = width / Constants.BASE_SCREEN_WIDTH;
        float xPos = this.ball.startingX;
        float yPos = this.ball.startingY;
        this.position.set(xPos, yPos);
        this.body.setTransform(xPos / Constants.BOX2D_SCALE, yPos / Constants.BOX2D_SCALE, 0);
    }
}
