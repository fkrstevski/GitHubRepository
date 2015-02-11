package com.filip.offthewalls.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.filip.offthewalls.util.Constants;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class WorldController extends InputAdapter
{
    private static final String TAG = WorldController.class.getName();

    private Ball ball;

    private ArrayList<Vector2> points;

    private int level;
    private int zone;
    private int stage;

    public WorldController (int zone, int stage, int level)
    {
        init(zone, stage, level);
    }

    public void init (int zone, int stage, int level)
    {
        Gdx.input.setInputProcessor(this);
        this.points = StageLoader.getPoints(zone, stage);
        this.level = level;
        this.zone = zone;
        this.stage = stage;

        ball = new Ball(points.get(0));
    }

    public void update (float deltaTime)
    {
        ball.update(deltaTime);


        float distance = Vector2.dst(ball.getX(), ball.getY(), points.get(points.size()-1).x, points.get(points.size()-1).y );

        if ((distance <= Math.abs(Constants.BALL_RADIUS - Constants.END_CIRCLE_OUTLINE_RADIUS * getLevelMultiplier())))
        {
            // Inside
            Gdx.app.debug(TAG, "Circle2 is inside Circle1");

            this.level++;
            if(this.level > Constants.MAX_LEVELS - 1)
            {
                this.level = 0;
                this.stage++;
                if(this.stage > StageLoader.getZone(this.zone).getNumberOfStages() - 1)
                {
                    this.stage = 0;
                    this.zone++;
                    if(this.zone > StageLoader.getNumberOfZones() - 1)
                    {
                        this.zone = 0;
                    }
                }
            }
            Gdx.app.debug(TAG, "Zone = " + this.zone + " Stage = " + this.stage + " Level = " + this.level);
            this.init(zone, stage, level);

        }
    }

    @Override
    public boolean keyUp (int keycode)
    {
        if (keycode == Input.Keys.SPACE)
        {

        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {

        return false;
    }

    public float getLevelMultiplier() {
        return Constants.LEVEL_MULTIPLIERS[level];
    }

    public Vector2 getPoint(int index)
    {
        return points.get(index);
    }

    public Vector2 getFirstPoint()
    {
        return points.get(0);
    }

    public Vector2 getLastPoint()
    {
        return points.get(points.size() - 1);
    }

    public int getNumberOfPoints()
    {
        return points.size();
    }

    public Ball getBall()
    {
        return ball;
    }
}
