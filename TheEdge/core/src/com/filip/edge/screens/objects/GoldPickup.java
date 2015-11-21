package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

/**
 * Created by fkrstevski on 2015-11-20.
 */
public class GoldPickup extends EmptyCircle {
    public static final String TAG = GoldPickup.class.getName();
    private float currentTime;
    private float scaleTime;
    private float originalSize;
    private boolean deactivate;

    private State state;

    public GoldPickup(float size, float x, float y, boolean shared, String region) {
        super(Constants.GOLD_PICKUP_RADIUS, x, y, Constants.YELLOW, Constants.YELLOW, shared, region);

        this.state = State.ScalingUp;
        this.originalSize = size;
        this.scale.set(0, 0);
        this.scaleTime = 0.1f;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(deactivate) {
            deactivate = false;
            body.setActive(false);
        }

        switch (state) {
            case ScalingUp:
                currentTime += deltaTime;
                if (currentTime > scaleTime) {
                    currentTime = 0;
                    state = State.Active;
                    body.setActive(true);
                } else {
                    this.scale.set(currentTime / scaleTime, currentTime / scaleTime);
                    body.getFixtureList().get(0).getShape().setRadius(((currentTime / scaleTime) * originalSize / 2.0f) / Constants.BOX2D_SCALE);
                }
                break;
            case Active:

                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if(state != State.Gone) {
            super.render(batch);
        }
    }

    @Override
    public void reset() {
        this.scale.set(0, 0);
        this.currentTime = 0;
        this.body.setActive(true);
    }

    public void start(){
        this.currentTime = 0;
        this.state = State.ScalingUp;
        this.scale.set(0, 0);
    }

    public void pickedUp(){
        this.state = State.Gone;
        deactivate = true;
    }

    enum State {
        ScalingUp,
        Active,
        Gone
    }
}
