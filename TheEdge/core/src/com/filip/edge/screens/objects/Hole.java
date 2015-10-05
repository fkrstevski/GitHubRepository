package com.filip.edge.screens.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

/**
 * Created by fkrstevski on 2015-09-29.
 */
public class Hole extends EmptyCircle {
    public static final String TAG = Hole.class.getName();
    private float currentScaleTime;
    private float currentTime;
    private float scaleTime;
    private float startupTime;
    private int originalSize;

    private State state;

    enum State {
        StartingUp,
        ScalingUp,
        ScalingDown
    }

    public Hole(int size, float x, float y, int startupTimeIndex, int scaleTimeIndex) {
        super(size, x, y, new Color(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a), new Color(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a));

        this.state = State.StartingUp;
        this.originalSize = size;
        this.scale.set(0, 0);
        this.startupTime = Constants.HOLE_STARTUP_TIME[startupTimeIndex];
        this.scaleTime = Constants.HOLE_SCALE_TIME[scaleTimeIndex];
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        switch (state) {
            case StartingUp:
                currentTime += deltaTime;
                if (currentTime > startupTime) {
                    currentTime = 0;
                    state = State.ScalingUp;
                }
                break;
            case ScalingUp:
                currentScaleTime += deltaTime;
                if (currentScaleTime > scaleTime) {
                    currentScaleTime = 0;
                    state = State.ScalingDown;
                } else {
                    this.scale.set(currentScaleTime / scaleTime, currentScaleTime / scaleTime);
                    body.getFixtureList().get(0).getShape().setRadius(((currentScaleTime / scaleTime) * originalSize / 2.0f) / Constants.BOX2D_SCALE);

                }
                break;

            case ScalingDown:
                currentScaleTime += deltaTime;
                if (currentScaleTime > scaleTime) {
                    currentScaleTime = 0;
                    state = State.ScalingUp;
                } else {
                    this.scale.set(1 - currentScaleTime / scaleTime, 1 - currentScaleTime / scaleTime);
                    body.getFixtureList().get(0).getShape().setRadius(((1 - currentScaleTime / scaleTime) * originalSize / 2.0f) / Constants.BOX2D_SCALE);
                }

                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        switch (state) {
            case ScalingUp:
            case ScalingDown:
                super.render(batch);
                break;
        }
    }
}
