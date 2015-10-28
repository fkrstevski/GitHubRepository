package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.filip.edge.game.Level;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class MiddlePart extends AbstractRectangleButtonObject {
    public static final String TAG = MiddlePart.class.getName();
    private static final float SCALE_TIME = 0.5f;
    public boolean disappears;
    public boolean appears;
    public float disappearsAppearsStartupTime;
    public float disappearsAppearsTime;
    public float currentTime;
    float hx, hy, angle;
    Vector2 center;

    private Level.PropertyState disappearingState;

    public MiddlePart(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        super(width, height, x, y, outsideColor, insideColor, shared, region);
        disappearingState = Level.PropertyState.Inactive;
    }

    @Override
    public void fillInside(float size) {

    }

    @Override
    public void update(float deltaTime) {
        if (disappears) {
            currentTime += deltaTime;
            switch (disappearingState) {
                case Inactive:
                    if (currentTime > disappearsAppearsStartupTime) {
                        currentTime = 0;
                        this.scale.set(1, 1);
                        disappearingState = Level.PropertyState.Buildup;
                    }
                    break;
                case Buildup:
                    if (currentTime > SCALE_TIME) {
                        currentTime = 0;
                        this.scale.set(0, 0);
                        disappearingState = Level.PropertyState.Active;
                    } else {
                        Fixture f = body.getFixtureList().get(0);
                        PolygonShape s = (PolygonShape) f.getShape();
                        // scale down
                        float scale = 1 - this.currentTime / SCALE_TIME;
                        this.scale.set(scale, scale);
                        s.setAsBox(scale * hx, scale * hy, center, angle);
                    }
                    break;
                case Active:
                    if (currentTime > disappearsAppearsTime) {
                        currentTime = 0;
                        this.scale.set(0, 0);
                        disappearingState = Level.PropertyState.Teardown;
                    }
                    break;
                case Teardown:
                    if (currentTime > SCALE_TIME) {
                        currentTime = 0;
                        this.scale.set(1, 1);
                        disappearingState = Level.PropertyState.Inactive;
                    } else {
                        Fixture f = body.getFixtureList().get(0);
                        PolygonShape s = (PolygonShape) f.getShape();
                        // scale up
                        float scale = this.currentTime / SCALE_TIME;
                        this.scale.set(scale, scale);
                        s.setAsBox(scale * hx, scale * hy, center, angle);
                        break;
                    }

            }
        }
        else if(appears && disappearsAppearsStartupTime != -1 && disappearsAppearsTime != -1) {
            currentTime += deltaTime;
            switch (disappearingState) {
                case Inactive:
                    if (currentTime > disappearsAppearsStartupTime) {
                        currentTime = 0;
                        Fixture f = body.getFixtureList().get(0);
                        PolygonShape s = (PolygonShape) f.getShape();
                        this.scale.set(0, 0);
                        s.setAsBox(0, 0, center, angle);
                        disappearingState = Level.PropertyState.Buildup;
                    }
                    break;
                case Buildup: // SCALE UP
                    if (currentTime > disappearsAppearsTime) {
                        currentTime = 0;
                        this.scale.set(1, 1);
                        disappearingState = Level.PropertyState.Active;
                    } else {
                        Fixture f = body.getFixtureList().get(0);
                        PolygonShape s = (PolygonShape) f.getShape();
                        // scale up
                        float scale = this.currentTime / disappearsAppearsTime;
                        this.scale.set(scale, scale);
                        s.setAsBox(scale * hx, scale * hy, center, angle);
                        break;
                    }
                case Active:
                    if (currentTime > disappearsAppearsTime) {
                        currentTime = 0;
                        this.scale.set(1, 1);
                        disappearingState = Level.PropertyState.Teardown;
                    }
                    break;
                case Teardown: // SCALE DOWN
                    if (currentTime > disappearsAppearsTime) {
                        currentTime = 0;
                        this.scale.set(0, 0);
                        disappearingState = Level.PropertyState.Buildup;
                    } else {
                        Fixture f = body.getFixtureList().get(0);
                        PolygonShape s = (PolygonShape) f.getShape();
                        // scale down
                        float scale = 1 - this.currentTime / disappearsAppearsTime;
                        this.scale.set(scale, scale);
                        s.setAsBox(scale * hx, scale * hy, center, angle);
                    }
                    break;
            }
        }
    }

    public void setBox(float hx, float hy, Vector2 center, float angle) {
        this.hx = hx;
        this.hy = hy;
        this.center = center;
        this.angle = angle;
    }

    @Override
    public void reset() {
        if(disappears) {    
            currentTime = 0;
            Fixture f = body.getFixtureList().get(0);
            PolygonShape s = (PolygonShape) f.getShape();
            this.scale.set(1, 1);
            s.setAsBox(hx, hy, center, angle);
        }
        if(appears) {
            currentTime = 0;
            Fixture f = body.getFixtureList().get(0);
            PolygonShape s = (PolygonShape) f.getShape();
            this.scale.set(0, 0);
            s.setAsBox(hx, hy, center, angle);
        }
    }

    public void start()
    {
        if(disappears) {
            this.currentTime = 0;
            this.disappearingState = Level.PropertyState.Inactive;
        }
        else if (appears) {
            this.currentTime = 0;
            this.disappearingState = Level.PropertyState.Inactive;
            Fixture f = body.getFixtureList().get(0);
            PolygonShape s = (PolygonShape) f.getShape();
            this.scale.set(0, 0);
            s.setAsBox(0, 0, center, angle);
        }
    }
}