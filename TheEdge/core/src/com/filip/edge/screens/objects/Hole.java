package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

/**
 * Created by fkrstevski on 2015-09-29.
 */
public class Hole extends EmptyCircle {
    public static final String TAG = Hole.class.getName();
    private float currentScaleTime = 0;
    private float currentTime = 0;
    private final float SCALE_TIME = 5.5f;
    private final float COOLDOWN_TIME = 1.5f;
    private int originalSize;

    private State state = State.ScalingUp;

    enum State {
        ScalingUp,
        ScalingDown
    }

    public Hole(int size, float x, float y)
    {
        super(size, x, y, new Color(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a), new Color(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a));

        this.originalSize = size;
        this.scale.set(0, 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        switch (state){
            case ScalingUp:
                currentScaleTime += deltaTime;
                if(currentScaleTime > SCALE_TIME)
                {
                    currentScaleTime = 0;
                    state = State.ScalingDown;
                }
                else {
                    this.scale.set(currentScaleTime / SCALE_TIME, currentScaleTime / SCALE_TIME);
                    body.getFixtureList().get(0).getShape().setRadius(((currentScaleTime / SCALE_TIME) * originalSize / 2.0f) / Constants.BOX2D_SCALE);

                }
                break;

            case ScalingDown:
                currentScaleTime += deltaTime;
                if(currentScaleTime > SCALE_TIME)
                {
                    currentScaleTime = 0;
                    state = State.ScalingUp;
                }
                else {
                    this.scale.set(1 - currentScaleTime / SCALE_TIME, 1 - currentScaleTime / SCALE_TIME);
                    body.getFixtureList().get(0).getShape().setRadius(((1 - currentScaleTime / SCALE_TIME) * originalSize / 2.0f) / Constants.BOX2D_SCALE);
                }

                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        switch (state){
            case ScalingUp:
            case ScalingDown:
                super.render(batch);
                break;
        }
    }
}
