package com.filip.edge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.screens.objects.AbstractCircleButtonObject;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;
import com.filip.edge.screens.objects.BackButton;
import com.filip.edge.screens.objects.EndTarget;
import com.filip.edge.screens.objects.MiddlePart;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

public class Level
{

    public static final String TAG = Level.class.getName();

    public EmptyCircle ball;
    public BackButton backButton;
    public MiddlePart topBackground;
    public ArrayList<AbstractCircleButtonObject> circleShapes;
    public ArrayList<AbstractRectangleButtonObject> rectangleShapes;
    public AbstractCircleButtonObject endCircle;
    public EndTarget startCircleGreenIcon;
    public EndTarget startCircleYellowIcon;
    public EndTarget startCircleRedIcon;
    public EndTarget finishCircleGreenIcon;
    public EndTarget finishCircleYellowIcon;
    public EndTarget finishCircleRedIcon;
    public EndTarget startCircle;
    public EndTarget finishCircle;
    private ArrayList<Vector2> points;

    public Level()
    {
        init();
    }

    private void init()
    {
        this.points = StageLoader.getPoints(GamePreferences.instance.zone, GamePreferences.instance.stage);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        float scale = width / 1334.0f; // iPhone 6 width

        circleShapes = new ArrayList<AbstractCircleButtonObject>();
        rectangleShapes = new ArrayList<AbstractRectangleButtonObject>();

        ball = new EmptyCircle((int) (Constants.BALL_RADIUS * 2 * scale), this.getFirstPoint().x, this.getFirstPoint().y, Constants.BLUE, Constants.WHITE);
        backButton = new BackButton((int) (height * 0.1f),   // size
                (int) (height * 0.055),    // x
                (int) (height * 0.055),     // y
                Constants.WHITE,         // outside color
                Constants.TURQUOISE);      // inside color

        int topWidth = width;
        int topHeight = (int)(height * 0.11);
        int topX = width/2;
        int topY = topHeight/2;

        topBackground = new MiddlePart(topWidth, topHeight, topX, topY,Constants.TURQUOISE, Constants.WHITE);

        // Add Start Circle
        EndTarget st = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * scale), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.GREEN, Constants.WHITE);
        circleShapes.add(st);

        startCircleGreenIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * scale), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.GREEN, Constants.WHITE);
        startCircleYellowIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * scale), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.YELLOW, Constants.WHITE);
        startCircleRedIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * scale), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.RED, Constants.WHITE);

        startCircle = startCircleRedIcon;

        // Add EndCircle - for target collision
        endCircle = new EmptyCircle((int) (Constants.END_CIRCLE_RADIUS * 2 * Constants.END_CIRCLE_OUTLINE_RADIUS_MULTIPLIER * scale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.WHITE, Constants.WHITE);

        // Add EndCircle - for boundary collision
        EndTarget et = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * scale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.GREEN, Constants.WHITE);
        circleShapes.add(et);

        finishCircleGreenIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * scale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.GREEN, Constants.WHITE);
        finishCircleYellowIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * scale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.YELLOW, Constants.WHITE);
        finishCircleRedIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * scale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.RED, Constants.WHITE);

        finishCircle = finishCircleRedIcon;


        // Add Middle Circles
        for (int i = 1; i < this.getNumberOfPoints() - 1; ++i)
        {

            EmptyCircle m = new EmptyCircle((int) (Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * scale),
                    points.get(i).x, points.get(i).y, Constants.WHITE, Constants.TURQUOISE);
            circleShapes.add(m);
        }

        // Add Rectangles
        for (int i = 0; i < this.getNumberOfPoints() - 1; ++i)
        {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i + 1);

            Vector2 midpoint = new Vector2((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
            float angle = (float) Math.toDegrees(Math.atan2(p1.y - p2.y, p1.x - p2.x));

            AbstractRectangleButtonObject s = new MiddlePart((int) Vector2.dst(p1.x, p1.y, p2.x, p2.y),
                    (int) (Constants.RECTANGLE_WIDTH * this.getLevelMultiplier() * scale),
                    midpoint.x, midpoint.y,
                    Constants.WHITE, Constants.TURQUOISE);
            s.rotation = angle;

            rectangleShapes.add(s);
        }
    }

    public void update(float deltaTime)
    {
        ball.update(deltaTime);
    }

    public void render(SpriteBatch batch)
    {
        for (int i = 0; i < circleShapes.size(); ++i)
        {
            circleShapes.get(i).render(batch);
        }

        if (this.startCircle != null)
        {
            startCircle.render(batch);
        }

        if (this.finishCircle != null)
        {
            finishCircle.render(batch);
        }

        for (int i = 0; i < rectangleShapes.size(); ++i)
        {
            rectangleShapes.get(i).render(batch);
        }

        //endCircle.render(batch);

        ball.render(batch);

    }

    public void renderBackButton(SpriteBatch batch)
    {
        topBackground.render(batch);
        backButton.render(batch);
    }

    public float getLevelMultiplier()
    {
        return Constants.LEVEL_MULTIPLIERS[GamePreferences.instance.level];
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

    public EmptyCircle getBall()
    {
        return ball;
    }
}
