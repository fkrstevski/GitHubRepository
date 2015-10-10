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

    public Ball(float size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, x, y, outsideColor, insideColor);
        orbiters = new ArrayList<Orbiter>();
    }

    public Orbiter addNewOrbiter(){
        int width = Gdx.graphics.getWidth();
        float horizontalScale = width / Constants.BASE_SCREEN_WIDTH;
        Orbiter orbiter = null;
        if(orbiters.size() == 0) {
            float angle = 0;
            float xpos = this.position.x + (this.radius + Constants.ORBITER_OFFSET * horizontalScale) * MathUtils.cos(angle);
            float ypos = this.position.y + (this.radius + Constants.ORBITER_OFFSET * horizontalScale) * MathUtils.sin(angle);
            orbiter = new Orbiter(Constants.ORBITER_RADIUS * 2 * Constants.LEVEL_MULTIPLIERS[GamePreferences.instance.level] * horizontalScale,
                    xpos, ypos, Constants.GREEN, Constants.GREEN, this, angle);
            orbiters.add(orbiter);
        }
        else if(orbiters.size() == 1) {
            float angle = orbiters.get(0).angle + MathUtils.PI;
            float xpos = this.position.x + (this.radius + Constants.ORBITER_OFFSET * horizontalScale) * MathUtils.cos(angle);
            float ypos = this.position.y + (this.radius + Constants.ORBITER_OFFSET * horizontalScale) * MathUtils.sin(angle);
            orbiter = new Orbiter(Constants.ORBITER_RADIUS * 2 * Constants.LEVEL_MULTIPLIERS[GamePreferences.instance.level] * horizontalScale,
                    xpos, ypos, Constants.GREEN, Constants.GREEN, this, angle);
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
            if(orbiters.get(i).visible) {
                orbiters.get(i).update(deltaTime);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        for (int i = 0; i < orbiters.size(); ++i) {
            if(orbiters.get(i).visible) {
                orbiters.get(i).render(batch);
            }
        }
    }
}
