package com.filip.edge.screens.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-10-10.
 */
public class Ball extends EmptyCircle {
    public static final String TAG = Ball.class.getName();

    public ArrayList<Orbiter> orbiters;

    public float startingX;
    public float startingY;

    public Ball(float size, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        super(size, x, y, outsideColor, insideColor, shared, region);
        orbiters = new ArrayList<Orbiter>();
        this.startingX = x;
        this.startingY = y;

        addNewOrbiter();
        addNewOrbiter();
    }

    @Override
    public void fillInside(float size) {
        buttonPixmap.setColor(Color.WHITE);
        buttonPixmap.fillCircle((int) (size * 0.7f), (int) (size * 0.6f), (int) (size / 10));
        buttonPixmap.fillCircle((int) (size * 0.3f), (int) (size * 0.6f), (int) (size / 10));
        buttonPixmap.setColor(Color.BLACK);
        buttonPixmap.fillCircle((int) (size * 0.75f), (int) (size *0.6f), (int) (size / 20));
        buttonPixmap.fillCircle((int) (size * 0.35f), (int) (size *0.6f), (int) (size / 20));

        buttonPixmap.setColor(Color.RED);
        buttonPixmap.fillTriangle(0, (int) (size / 2), (int)size, (int) (size / 2), (int) (size / 2), 0);

    }

    private Orbiter addNewOrbiter(){
        int width = Gdx.graphics.getWidth();
        float horizontalScale = width / Constants.BASE_SCREEN_WIDTH;
        Orbiter orbiter = null;
        if(orbiters.size() == 0) {
            float angle = 0;
            float xpos = this.position.x + (this.radius + Constants.ORBITER_OFFSET * horizontalScale) * MathUtils.cos(angle);
            float ypos = this.position.y + (this.radius + Constants.ORBITER_OFFSET * horizontalScale) * MathUtils.sin(angle);
            orbiter = new Orbiter(Constants.ORBITER_RADIUS * 2 * Constants.LEVEL_MULTIPLIERS[GamePreferences.instance.level] * horizontalScale,
                    xpos, ypos, Constants.GREEN, Constants.GREEN, this, angle, false, "CircleOrbiter");
            orbiter.visible = false;
            orbiters.add(orbiter);

        }
        else if(orbiters.size() == 1) {
            float angle = orbiters.get(0).angle + MathUtils.PI;
            float xpos = this.position.x + (this.radius + Constants.ORBITER_OFFSET * horizontalScale) * MathUtils.cos(angle);
            float ypos = this.position.y + (this.radius + Constants.ORBITER_OFFSET * horizontalScale) * MathUtils.sin(angle);
            orbiter = new Orbiter(Constants.ORBITER_RADIUS * 2 * Constants.LEVEL_MULTIPLIERS[GamePreferences.instance.level] * horizontalScale,
                    xpos, ypos, Constants.GREEN, Constants.GREEN, this, angle, false, "CircleOrbiter");
            orbiter.visible = false;
            orbiters.add(orbiter);
        }
        else {
            Gdx.app.error(TAG, "CANNOT ADD ANY MORE ORBITERS");
        }

        return orbiter;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (int i = 0; i < orbiters.size(); ++i) {
            orbiters.get(i).update(deltaTime);
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        // Render the orbiters first
        for (int i = 0; i < orbiters.size(); ++i) {
            if(orbiters.get(i).visible) {
                orbiters.get(i).render(batch);
            }
        }

        super.render(batch);
    }

    @Override
    public void reset(){
        this.position.x = this.startingX;
        this.position.y = this.startingY;
        this.scale.set(1,1);
        this.direction = 1;
        this.body.setLinearVelocity(0,0);
        this.body.setTransform(this.position.x / Constants.BOX2D_SCALE, this.position.y / Constants.BOX2D_SCALE, 0);

        for (int i = 0; i < orbiters.size(); ++i) {
            orbiters.get(i).reset();
        }
    }
}
