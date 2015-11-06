package com.filip.edge.screens.objects;

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
    private float currentTime;
    private float scaleTime;
    private float startupTime;
    private float originalSize;

    private State state;

    public Hole(float size, float x, float y, int startupTimeIndex, int scaleTimeIndex, boolean shared, String region) {
        super(size, x, y, new Color(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a), new Color(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a), shared, region);

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
                currentTime += deltaTime;
                if (currentTime > scaleTime) {
                    currentTime = 0;
                    state = State.ScalingDown;
                } else {
                    this.scale.set(currentTime / scaleTime, currentTime / scaleTime);
                    body.getFixtureList().get(0).getShape().setRadius(((currentTime / scaleTime) * originalSize / 2.0f) / Constants.BOX2D_SCALE);
                    if (currentTime / scaleTime > 0.3f) {
                        body.setActive(true);
                    }
                }
                break;

            case ScalingDown:
                currentTime += deltaTime;
                if (currentTime > scaleTime) {
                    currentTime = 0;
                    state = State.ScalingUp;
                } else {
                    this.scale.set(1 - currentTime / scaleTime, 1 - currentTime / scaleTime);
                    body.getFixtureList().get(0).getShape().setRadius(((1 - currentTime / scaleTime) * originalSize / 2.0f) / Constants.BOX2D_SCALE);
                    if (1 - currentTime / scaleTime < 0.3f) {
                        body.setActive(false);
                    }
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

    @Override
    public void reset() {
        this.scale.set(0, 0);
        this.body.getFixtureList().get(0).getShape().setRadius(0);
        this.body.setActive(true);
        this.currentTime = 0;
    }

    public void start(){
        this.currentTime = 0;
        this.state = State.StartingUp;
        this.scale.set(0, 0);
        this.body.getFixtureList().get(0).getShape().setRadius(0);
        this.body.setActive(false);
    }

    enum State {
        StartingUp,
        ScalingUp,
        ScalingDown
    }
}
