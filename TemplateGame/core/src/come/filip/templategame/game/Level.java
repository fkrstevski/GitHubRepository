package come.filip.templategame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import come.filip.templategame.game.objects.Ball;
import come.filip.templategame.screens.objects.AbstractCircleButtonObject;
import come.filip.templategame.screens.objects.AbstractRectangleButtonObject;
import come.filip.templategame.screens.objects.BackButton;
import come.filip.templategame.screens.objects.EndTarget;
import come.filip.templategame.screens.objects.MiddlePart;
import come.filip.templategame.screens.objects.StartTarget;
import come.filip.templategame.util.Constants;

public class Level
{

    public static final String TAG = Level.class.getName();

    public Ball ball;
    public BackButton backButton;
    public ArrayList<AbstractCircleButtonObject> circleShapes;
    public ArrayList<AbstractRectangleButtonObject> rectangleShapes;
    public AbstractCircleButtonObject endCircle;
    private ArrayList<Vector2> points;
    private int level;
    private int zone;
    private int stage;

    public Level(int zone, int stage, int level)
    {
        init(zone, stage, level);
    }

    private void init(int zone, int stage, int level)
    {

        this.level = level;
        this.zone = zone;
        this.stage = stage;
        this.points = StageLoader.getPoints(this.zone, this.stage);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        circleShapes = new ArrayList<AbstractCircleButtonObject>();
        rectangleShapes = new ArrayList<AbstractRectangleButtonObject>();

        ball = new Ball((int) (Constants.BALL_RADIUS * 2), this.getFirstPoint().x, this.getFirstPoint().y, Constants.BLUE, Constants.WHITE);
        backButton = new BackButton((int) (width * 0.05f),   // size
                (int) (width * 0.03),    // x
                (int) (width * 0.03),     // y
                Constants.WHITE,         // outside color
                Constants.BLUE);      // inside color

        // Add Start Circle
        StartTarget st = new StartTarget((int) (Constants.END_CIRCLE_RADIUS * 2), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.GREY, Constants.WHITE);
        circleShapes.add(st);

        // Add End Circle - for target collision
        endCircle = new Ball((int) (Constants.END_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * 0.25), this.getLastPoint().x,
                this.getLastPoint().y, Constants.WHITE, Constants.WHITE);

        // Add End Circle - for boundary collision
        EndTarget et = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 ), this.getLastPoint().x,
                this.getLastPoint().y, Constants.TURQUOISE, Constants.WHITE);
        circleShapes.add(et);


        // Add Middle Circles
        for (int i = 1; i < this.getNumberOfPoints() - 1; ++i)
        {

            Ball m = new Ball((int) (Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier()),
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
                    (int) (Constants.RECTANGLE_WIDTH * this.getLevelMultiplier()),
                    midpoint.x, midpoint.y,
                    Constants.WHITE, Constants.TURQUOISE);
            s.rotation = angle;

            Gdx.app.debug(TAG, "Angle = " + s.rotation);
            Gdx.app.debug(TAG, "Rad Angle = " + MathUtils.degreesToRadians * s.rotation);

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

        for (int i = 0; i < rectangleShapes.size(); ++i)
        {
            rectangleShapes.get(i).render(batch);
        }

        //endCircle.render(batch);

        ball.render(batch);
        backButton.render(batch);

    }

    public float getLevelMultiplier()
    {
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
