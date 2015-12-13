package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.util.Constants;

/**
 * Created by fkrstevski on 2015-09-29.
 */
public class OrbiterPickup extends EmptyCircle {
    public static final String TAG = OrbiterPickup.class.getName();
    private float currentDisappearTime;
    private float currentTime;
    private float scaleTime;
    private float startupTime;
    private float disappearTime;
    private float originalSize;
    private boolean scalingUp;
    private float minScale;
    private boolean deactivate;

    private State state;

    public OrbiterPickup(float size, float x, float y, int startupTimeIndex, int disappearTimeIndex, boolean shared, String region) {
        super(size, x, y, Constants.GREEN, Constants.GREEN, shared, region);

        this.state = State.StartingUp;
        this.originalSize = size;
        this.scale.set(0, 0);
        this.startupTime = (startupTimeIndex == -1 ? 0 : Constants.ORBITER_STARTUP_TIME[startupTimeIndex]);
        this.disappearTime = (disappearTimeIndex == -1 ? Integer.MAX_VALUE : Constants.ORBITER_DISAPPEAR_TIME[disappearTimeIndex]);
        this.scaleTime = 0.2f;
        this.scalingUp = true;
        this.minScale = 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(deactivate) {
            deactivate = false;
            body.setActive(false);
        }

        if(disappearTime != Integer.MAX_VALUE) {
            switch (state) {
                case StartingUp:
                    currentTime += deltaTime;
                    if (currentTime > startupTime) {
                        currentTime = 0;
                        currentDisappearTime = 0;
                        state = State.Active;
                        body.setActive(true);
                    }
                    break;
                case Active:
                    currentTime += deltaTime;
                    currentDisappearTime += deltaTime;
                    minScale = (currentDisappearTime / disappearTime) / 2;
                    if(scalingUp) {
                        this.scale.set(currentTime / scaleTime + minScale, currentTime / scaleTime + minScale);
                        //body.getFixtureList().get(0).getShape().setRadius(((currentTime / scaleTime) * originalSize / 2.0f) / Constants.BOX2D_SCALE);
                        if(this.scale.epsilonEquals(1, 1, 0.1f)) {
                            scalingUp = !scalingUp;
                            currentTime = 0;
                        }
                    }
                    else {

                        if (currentDisappearTime > disappearTime) {
                            currentDisappearTime = 0;
                            state = State.Disappearing;
                            minScale = 0;
                        }

                        this.scale.set(1 - currentTime / scaleTime, 1 - currentTime / scaleTime);
                        //body.getFixtureList().get(0).getShape().setRadius(((1 - currentTime / scaleTime) * originalSize / 2.0f) / Constants.BOX2D_SCALE);
                        if(this.scale.epsilonEquals(minScale, minScale, 0.1f)) {
                            scalingUp = !scalingUp;
                            currentTime = 0;
                        }
                    }
                    break;

                case Disappearing:
                    currentTime += deltaTime;
                    this.scale.set(1 - currentTime / scaleTime, 1 - currentTime / scaleTime);
                    body.getFixtureList().get(0).getShape().setRadius(((1 - currentTime / scaleTime) * originalSize / 2.0f) / Constants.BOX2D_SCALE);
                    if(this.scale.epsilonEquals(0, 0, 0.1f)) {
                        currentTime = 0;
                        state = State.Gone;
                        body.setActive(false);
                    }
                    break;
            }
        }
    }

    public void pickedUp(){
        this.state = State.Gone;
        deactivate = true;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(state != State.Gone) {
            super.render(batch);
        }
    }

    @Override
    public void reset() {
        this.scale.set(0,0);
        if(disappearTime != Integer.MAX_VALUE) {
            this.body.getFixtureList().get(0).getShape().setRadius(Constants.ORBITER_PICKUP_BODY_RADIUS / Constants.BOX2D_SCALE);
        }
        this.body.setActive(true);
        this.currentTime = 0;
        this.minScale = 0;
    }

    public void start(){
        if(disappearTime == Integer.MAX_VALUE) {
            this.scale.set(0.5f, 0.5f);
        }

        this.currentTime = 0;
        this.scalingUp = true;
        this.state = State.StartingUp;
    }

    enum State {
        StartingUp,
        Active,
        Disappearing,
        Gone
    }
}
