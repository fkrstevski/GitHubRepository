package come.filip.templategame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import come.filip.templategame.game.objects.Ball;
import come.filip.templategame.game.objects.Clouds;
import come.filip.templategame.game.objects.Goal;
import come.filip.templategame.screens.objects.AbstractButtonObject;
import come.filip.templategame.screens.objects.AbstractRectangleButtonObject;
import come.filip.templategame.screens.objects.BackButton;
import come.filip.templategame.screens.objects.MiddlePart;
import come.filip.templategame.screens.objects.StartTarget;
import come.filip.templategame.util.Constants;

public class Level {

	public static final String TAG = Level.class.getName();

	public Ball ball;
    public BackButton backButton;
    public ArrayList<AbstractButtonObject> shapes;

    private ArrayList<Vector2> points;

    private int level;
    private int zone;
    private int stage;
    public boolean collision;

	public Level (int zone, int stage, int level) {
		init(zone, stage, level);
	}

	private void init (int zone, int stage, int level) {

        this.level = level;
        this.zone = zone;
        this.stage = stage;
        this.points = StageLoader.getPoints(this.zone, this.stage);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        shapes = new ArrayList<AbstractButtonObject>();

        ball = new Ball((int)(Constants.BALL_RADIUS * 2), this.getFirstPoint().x, this.getFirstPoint().y, Constants.PURPLE, Constants.WHITE);
        backButton = new BackButton((int) (width * 0.05f),   // size
                                    (int) (width * 0.03),    // x
                                    (int) (width * 0.03),     // y
                                    Constants.WHITE,         // outside color
                                    Constants.BLUE);      // inside color

        // Add Start Circle
        shapes.add(
                /*new CircleShape( this.getFirstPoint(),
                        Constants.END_CIRCLE_RADIUS * this.getLevelMultiplier(),
                        CircleShape.CircleType.First)*/
                new StartTarget((int)(Constants.END_CIRCLE_RADIUS * 2 * this.getLevelMultiplier()),this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.GREY, Constants.WHITE)
                        );

        // Add End Circle
        shapes.add(
                /*new CircleShape( this.getLastPoint(),
                        Constants.END_CIRCLE_RADIUS * this.getLevelMultiplier(),
                        CircleShape.CircleType.Last)*/
                new StartTarget((int)(Constants.END_CIRCLE_RADIUS * 2 * this.getLevelMultiplier()),this.getLastPoint().x,
                        this.getLastPoint().y, Constants.TURQUOISE, Constants.WHITE)
        );

        // Add Middle Circles
        for(int i = 1; i < this.getNumberOfPoints() - 1; ++i)
        {
            shapes.add(
                    /*new CircleShape(points.get(i),
                            Constants.INSIDE_CIRCLE_RADIUS * this.getLevelMultiplier(),
                            CircleShape.CircleType.Middle)*/
                    new Ball((int)(Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier()),
                            points.get(i).x, points.get(i).y, Constants.WHITE, Constants.TURQUOISE)
            );
        }

        // Add Rectangles
        for(int i = 0; i < this.getNumberOfPoints() - 1; ++i) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i + 1);

            Vector2 midpoint = new Vector2((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
            float angle = (float) Math.toDegrees(Math.atan2(p1.y - p2.y, p1.x - p2.x));

            AbstractRectangleButtonObject s = new MiddlePart(   (int)Vector2.dst(p1.x, p1.y, p2.x, p2.y),
                                                                (int)(Constants.RECTANGLE_WIDTH * this.getLevelMultiplier()),
                                                                midpoint.x, midpoint.y,
                                                                Constants.WHITE, Constants.TURQUOISE);
            s.rotation = (float)angle;
            shapes.add(s);
        }
	}

	public void update (float deltaTime) {
		ball.update(deltaTime);

        this.collision = true;
        for(int i = 0; i < this.shapes.size(); ++i)
        {
            AbstractButtonObject s = shapes.get(i);
            /*if(s.containsBall(ball, getLevelMultiplier()))
            {
                collision = false;
                break;
            }*/
        }

        float distance = Vector2.dst(ball.position.x, ball.position.y, this.getLastPoint().x, this.getLastPoint().y );

        if ((distance <= Math.abs(Constants.BALL_RADIUS)))
        {
            // Inside
            Gdx.app.debug(TAG, "Circle2 is inside Circle1");

            next();

        }
	}

	public void render (SpriteBatch batch) {


        for(int i = 0; i < shapes.size(); ++i)
        {
            shapes.get(i).render(batch);
        }
        ball.render(batch);
        backButton.render(batch);

	}


    public void next() {
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
