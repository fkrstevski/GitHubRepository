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

    public ArrayList<Shape> shapes;

    private ArrayList<Vector2> points;

    private int level;
    private int zone;
    private int stage;

    public boolean collision;

    public WorldController (int zone, int stage, int level)
    {
        init(zone, stage, level);
    }

    public void init (int zone, int stage, int level)
    {
        Gdx.input.setInputProcessor(this);

        this.collision = true;

        this.level = level;
        this.zone = zone;
        this.stage = stage;
        this.points = StageLoader.getPoints(this.zone, this.stage);

        shapes = new ArrayList<Shape>();

        // Add Start Circle
        shapes.add(
                new CircleShape( this.getFirstPoint(),
                                 Constants.END_CIRCLE_RADIUS * this.getLevelMultiplier(),
                                 CircleShape.CircleType.First));

        // Add End Circle
        shapes.add(
                new CircleShape( this.getLastPoint(),
                        Constants.END_CIRCLE_RADIUS * this.getLevelMultiplier(),
                        CircleShape.CircleType.Last));

        // Add Middle Circles
        for(int i = 1; i < this.getNumberOfPoints() - 1; ++i)
        {
            shapes.add(
                    new CircleShape(points.get(i),
                            Constants.INSIDE_CIRCLE_RADIUS * this.getLevelMultiplier(),
                            CircleShape.CircleType.Middle));
        }

        // Add Rectangles
        for(int i = 0; i < this.getNumberOfPoints() - 1; ++i) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i + 1);

            shapes.add(
                    new RectangleShape(p1, p2,
                            Constants.RECTANGLE_WIDTH * this.getLevelMultiplier()));
        }



        ball = new Ball(points.get(0));
    }

    public void update (float deltaTime)
    {
        ball.update(deltaTime);

        this.collision = true;
        for(int i = 0; i < this.shapes.size(); ++i)
        {
            Shape s = shapes.get(i);
            if(s.containsBall(ball, getLevelMultiplier()))
            {
                collision = false;
                break;
            }
        }

        float distance = Vector2.dst(ball.getX(), ball.getY(), this.getLastPoint().x, this.getLastPoint().y );

        if ((distance <= Math.abs(Constants.BALL_RADIUS - Constants.END_CIRCLE_OUTLINE_RADIUS * getLevelMultiplier())))
        {
            // Inside
            Gdx.app.debug(TAG, "Circle2 is inside Circle1");

            next();

        }
    }

    private void next() {
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

    @Override
    public boolean keyUp (int keycode)
    {
        if (keycode == Input.Keys.SPACE)
        {
            next();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        next();
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
