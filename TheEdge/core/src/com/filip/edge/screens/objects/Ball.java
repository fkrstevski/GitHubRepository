package com.filip.edge.screens.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public void fillPixmap(float width, float height, Color outsideColor, Color insideColor) {
        // Ears
        //buttonPixmap.setColor(outsideColor);
        //buttonPixmap.fillTriangle((int) (width * 0.1f), (int) (width / 2), (int) (width / 2), (int) (width / 2), (int) (width * 0.1f), (int) width);
        //buttonPixmap.fillTriangle((int) (width * 0.9f), (int) (width / 2), (int) (width / 2), (int) (width / 2), (int) (width * 0.9f), (int) width);

        // Head
        super.fillPixmap(width, height, outsideColor, insideColor);
    }

    @Override
    public void fillInside(float size) {

        /*// Right Eyebrow
        buttonPixmap.setColor(Color.BLACK);
        buttonPixmap.fillCircle((int) (size * 0.7f), (int) (size * 0.7f), (int) (size / 10));
        buttonPixmap.setColor(Constants.BLACK);
        buttonPixmap.fillCircle((int) (size * 0.7f), (int) (size * 0.6f), (int) (size / 6.5));

        // Left Eyebrow
        buttonPixmap.setColor(Color.BLACK);
        buttonPixmap.fillCircle((int) (size * 0.3f), (int) (size * 0.7f), (int) (size / 10));
        buttonPixmap.setColor(Constants.BLACK);
        buttonPixmap.fillCircle((int) (size * 0.3f), (int) (size * 0.6f), (int) (size / 6.5));*/

        buttonPixmap.setColor(Color.WHITE);
        // Right Eye
        buttonPixmap.fillCircle((int) (size * 0.7f), (int) (size * 0.6f), (int) (size / 10));
        // Left Eye
        buttonPixmap.fillCircle((int) (size * 0.3f), (int) (size * 0.6f), (int) (size / 10));

        buttonPixmap.setColor(Color.BLACK);
        // Right Eye Ball
        buttonPixmap.fillCircle((int) (size * 0.75f), (int) (size * 0.6f), (int) (size / 20));
        // Left Eye Ball
        buttonPixmap.fillCircle((int) (size * 0.35f), (int) (size *0.6f), (int) (size / 20));

        // Mean look
        //buttonPixmap.setColor(Constants.BLACK);
        //buttonPixmap.fillRectangle((int) (size * 0.1f), (int) (size * 0.65f), (int) (size * .8f), (int) (size * .15f));

        // Bandanna
        buttonPixmap.setColor(Color.RED);
        buttonPixmap.fillTriangle(0, (int) (size / 2), (int) size, (int) (size / 2), (int) (size / 2), 0);

        // Beek
        //buttonPixmap.setColor(Color.ORANGE);
        //buttonPixmap.fillTriangle((int) (size * 0.25f), (int) (size * .4f), (int) (size * 0.75f), (int) (size * .4f), (int) (size / 2), (int) (size * 0.25f));
    }

    private Orbiter addNewOrbiter(){
        int width = Gdx.graphics.getWidth();
        float horizontalScale = width / Constants.BASE_SCREEN_WIDTH;
        Orbiter orbiter = null;
        if(orbiters.size() == 0) {
            float xpos = this.position.x;
            float ypos = this.position.y;
            orbiter = new Orbiter(Constants.ORBITER_RADIUS * 2 * Constants.LEVEL_MULTIPLIERS[GamePreferences.instance.level] * horizontalScale,
                    xpos, ypos, Constants.GREEN, Constants.GREEN, this, true, false, "CircleOrbiter");
            orbiter.visible = false;
            orbiters.add(orbiter);

        }
        else if(orbiters.size() == 1) {
            float xpos = this.position.x;
            float ypos = this.position.y;
            orbiter = new Orbiter(Constants.ORBITER_RADIUS * 2 * Constants.LEVEL_MULTIPLIERS[GamePreferences.instance.level] * horizontalScale,
                    xpos, ypos, Constants.GREEN, Constants.GREEN, this, false, false, "CircleOrbiter");
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
