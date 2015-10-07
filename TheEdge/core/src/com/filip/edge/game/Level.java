package com.filip.edge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.screens.objects.AbstractCircleButtonObject;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;
import com.filip.edge.screens.objects.BackButton;
import com.filip.edge.screens.objects.EndTarget;
import com.filip.edge.screens.objects.Follower;
import com.filip.edge.screens.objects.Hole;
import com.filip.edge.screens.objects.MiddlePart;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

import java.util.ArrayList;



public class Level {

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
    public ArrayList<LevelPoint> points;

    public ArrayList<Hole> holes;
    public ArrayList<Follower> followers;
    public ArrayList<Follower> oscillators;
    public ArrayList<LevelProperty> properties;

    public enum PropertyState {
        Inactive,
        Buildup,
        Active,
        Teardown
    }

    Follower levelFollower;

    private PropertyState disappearingState;
    private boolean disappearing;
    private float disappearingStartTime;
    private float disappearingSpeed;
    private float disappearingTime;
    private int disappearingIndex;

    public Level() {
        init();
    }

    private void init() {
        this.points = StageLoader.getPoints(GamePreferences.instance.zone, GamePreferences.instance.stage);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        float horizontalScale = width / Constants.BASE_SCREEN_WIDTH;
        float verticalScale = height / Constants.BASE_SCREEN_HEIGHT;

        float scale = horizontalScale * verticalScale;


        circleShapes = new ArrayList<AbstractCircleButtonObject>();
        rectangleShapes = new ArrayList<AbstractRectangleButtonObject>();

        holes = new ArrayList<Hole>();
        followers = new ArrayList<Follower>();
        oscillators = new ArrayList<Follower>();

        ball = new EmptyCircle((int) (Constants.BALL_RADIUS * 2 * horizontalScale), this.getFirstPoint().x, this.getFirstPoint().y, Constants.BLACK, Constants.WHITE);
        backButton = new BackButton((int) (height * 0.1f),   // size
                (int) (height * 0.055),    // x
                (int) (height * 0.055),     // y
                Constants.WHITE,         // outside color
                Constants.TURQUOISE);      // inside color

        int topWidth = width;
        int topHeight = (int) (height * 0.11);
        int topX = width / 2;
        int topY = topHeight / 2;

        topBackground = new MiddlePart(topWidth, topHeight, topX, topY, Constants.TURQUOISE, Constants.WHITE);

        // Add Start Circle
        EndTarget st = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * horizontalScale), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.GREEN, Constants.WHITE);
        circleShapes.add(st);

        startCircleGreenIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * horizontalScale), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.GREEN, Constants.WHITE);
        startCircleYellowIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * horizontalScale), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.YELLOW, Constants.WHITE);
        startCircleRedIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * horizontalScale), this.getFirstPoint().x,
                this.getFirstPoint().y, Constants.RED, Constants.WHITE);

        startCircle = startCircleRedIcon;


        // Add Middle Circles
        for (int i = 1; i < this.getNumberOfPoints() - 1; ++i) {
            EmptyCircle m = new EmptyCircle((int) (Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale),
                    points.get(i).x, points.get(i).y, Constants.WHITE, Constants.TURQUOISE);

            // Do not add multiple circles that are the same
            if(!circleShapes.contains(m)) {
                circleShapes.add(m);
            }

            if (points.get(i).hasAHole) {
                holes.add(new Hole((int) (Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale),
                        this.points.get(i).x, this.points.get(i).y,
                        this.points.get(i).holeStartupIndex, this.points.get(i).holeScaleIndex));
            }

            if(points.get(i).hasVerticalOscillator) {
                float dis = Constants.OSCILLATION_DISTANCE * this.getLevelMultiplier() * horizontalScale;
                ArrayList<Vector2> localPoints = new ArrayList<Vector2>();
                localPoints.add(new Vector2(points.get(i).x + dis, points.get(i).y));
                localPoints.add(new Vector2(points.get(i).x - dis, points.get(i).y));
                oscillators.add(new Follower(
                        Constants.OSCILLATOR_STARTTIME[points.get(i).oscillatorStartupIndex],
                        new Vector2(Constants.OSCILLATOR_SPEED[points.get(i).oscillatorSpeedIndex] * horizontalScale,
                                Constants.OSCILLATOR_SPEED[points.get(i).oscillatorSpeedIndex] * verticalScale),
                        (int) (Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale),
                        points.get(i), localPoints, 1, true
                ));
            }

            if(points.get(i).hasHorizontalOscillator) {
                float dis = Constants.OSCILLATION_DISTANCE * this.getLevelMultiplier() * horizontalScale;
                ArrayList<Vector2> localPoints = new ArrayList<Vector2>();
                localPoints.add(new Vector2(points.get(i).x, points.get(i).y + dis));
                localPoints.add(new Vector2(points.get(i).x, points.get(i).y - dis));
                oscillators.add(new Follower(
                        Constants.OSCILLATOR_STARTTIME[points.get(i).oscillatorStartupIndex],
                        new Vector2(Constants.OSCILLATOR_SPEED[points.get(i).oscillatorSpeedIndex] * horizontalScale,
                                Constants.OSCILLATOR_SPEED[points.get(i).oscillatorSpeedIndex] * verticalScale),
                        (int) (Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale),
                        points.get(i), localPoints, 1, true
                ));
            }

            if(points.get(i).followerDirection != 0) {
                ArrayList<Vector2> localPoints = new ArrayList<Vector2>();
                localPoints.add(points.get(i));
                localPoints.add(points.get(i+points.get(i).followerDirection));

                followers.add(new Follower(
                        Constants.FOLLOWER_STARTTIME[points.get(i).followStartupIndex],
                        new Vector2(Constants.FOLLOWER_SPEED[points.get(i).followSpeedIndex] * horizontalScale,
                                Constants.FOLLOWER_SPEED[points.get(i).followSpeedIndex] * verticalScale),
                        (int) (Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale),
                        points.get(i), localPoints, points.get(i).followerDirection, points.get(i).followerIsBackAndForth

                ));
            }
        }

        // Add EndCircle - for target collision
        endCircle = new EmptyCircle((int) (Constants.END_CIRCLE_RADIUS * 2 * Constants.END_CIRCLE_OUTLINE_RADIUS_MULTIPLIER * horizontalScale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.WHITE, Constants.WHITE);

        // Add EndCircle - for boundary collision
        EndTarget et = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * horizontalScale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.GREEN, Constants.WHITE);
        circleShapes.add(et);

        finishCircleGreenIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * horizontalScale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.GREEN, Constants.WHITE);
        finishCircleYellowIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * horizontalScale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.YELLOW, Constants.WHITE);
        finishCircleRedIcon = new EndTarget((int) (Constants.END_CIRCLE_RADIUS * 2 * horizontalScale), this.getLastPoint().x,
                this.getLastPoint().y, Constants.RED, Constants.WHITE);

        finishCircle = finishCircleRedIcon;

        // Add Rectangles
        for (int i = 0; i < this.getNumberOfPoints() - 1; ++i) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i + 1);

            Vector2 midpoint = new Vector2((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
            float angle = (float) Math.toDegrees(Math.atan2(p1.y - p2.y, p1.x - p2.x));

            AbstractRectangleButtonObject s = new MiddlePart((int) Vector2.dst(p1.x, p1.y, p2.x, p2.y),
                    (int) (Constants.RECTANGLE_WIDTH * this.getLevelMultiplier() * horizontalScale),
                    midpoint.x, midpoint.y,
                    Constants.WHITE, Constants.TURQUOISE);
            s.rotation = angle;

            if(points.get(i).disappears) {
                s.disapears = true;
                s.disappearsStartupTime = Constants.DISAPPEARING_OBJECT_STARTTIME[points.get(i).disappearsStartupIndex];
                s.disappearsTime = Constants.DISAPPEARING_OBJECT_TIME[points.get(i).disappearsTimeIndex];
            }

            // Do not add multiple rectangles that are the same
            if(!rectangleShapes.contains(s)) {
                rectangleShapes.add(s);
            }
        }

        // Add follower
        Stage s = StageLoader.getZone(GamePreferences.instance.zone).getStage(GamePreferences.instance.stage);
        if (s.hasFollowerObject) {

            levelFollower = new Follower(Constants.FOLLOWER_STARTTIME[s.followerStartupTimeIndex],
                                        new Vector2(Constants.FOLLOWER_SPEED[s.followerSpeedIndex] * horizontalScale,
                                            Constants.FOLLOWER_SPEED[s.followerSpeedIndex] * verticalScale),
                                        (int) (Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale),
                                        points.get(0), new ArrayList<Vector2>(points), 1, true);
        }

        if (s.disappears) {
            disappearingState = PropertyState.Inactive;
            disappearing = s.disappears;
            disappearingStartTime = Constants.DISAPPEARING_STARTTIME[s.disappearsStartupTimeIndex];
            disappearingSpeed = Constants.DISAPPEARING_TIME[s.disappearSpeedIndex];
            disappearingIndex = 0;
        }



        Gdx.app.log(TAG, "LEVEL = " + GamePreferences.instance.zone + "-" + GamePreferences.instance.stage);
        Gdx.app.log(TAG, "LEVEL points = " + points.size());
        Gdx.app.log(TAG, "LEVEL circles = " + circleShapes.size());
        Gdx.app.log(TAG, "LEVEL rectangles = " + rectangleShapes.size());

    }

    public void update(float deltaTime) {
        ball.update(deltaTime);
        for (Hole hole : holes) {
            hole.update(deltaTime);
        }

        for (Follower follower : followers) {
            follower.update(deltaTime);
        }

        for (Follower oscillator : oscillators) {
            oscillator.update(deltaTime);
        }

        for (AbstractRectangleButtonObject rect : rectangleShapes) {
            rect.update(deltaTime);
        }

        if (disappearing) {
            disappearingTime += deltaTime;
            switch (disappearingState) {
                case Inactive:
                    if (disappearingTime > disappearingStartTime) {
                        disappearingTime = 0;
                        disappearingState = PropertyState.Active;
                    }
                    break;

                case Active:
                    if (disappearingTime > disappearingSpeed) {
                        disappearingTime = 0;
                        Gdx.app.log(TAG, "disappearingIndex = " + disappearingIndex);
                        if (disappearingIndex > 0 && disappearingIndex < circleShapes.size() - 1 /*do not remove the final circle*/) {
                            circleShapes.get(disappearingIndex).visible = false;
                            circleShapes.get(disappearingIndex).body.setActive(false);
                        }
                        if (disappearingIndex < rectangleShapes.size()) {
                            rectangleShapes.get(disappearingIndex).visible = false;
                            rectangleShapes.get(disappearingIndex).body.setActive(false);
                        }
                        disappearingIndex++;

                    }
                    break;
            }
        }

        if (levelFollower != null) {
            levelFollower.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = circleShapes.size() - 1; i >= 0; --i) {
            circleShapes.get(i).render(batch);
        }

        if (this.startCircle != null) {
            startCircle.render(batch);
        }

        if (this.finishCircle != null) {
            finishCircle.render(batch);
        }

        for (int i = 0; i < rectangleShapes.size(); ++i) {
            rectangleShapes.get(i).render(batch);
        }

        //endCircle.render(batch);

        for (Hole hole : holes) {
            hole.render(batch);
        }

        for (Follower follower : followers) {
            follower.render(batch);
        }

        for (Follower oscillator : oscillators) {
            oscillator.render(batch);
        }

        if (levelFollower != null) {
            levelFollower.render(batch);
        }

        ball.render(batch);

    }

    public void renderBackButton(SpriteBatch batch) {
        topBackground.render(batch);
        backButton.render(batch);
    }

    public float getLevelMultiplier() {
        return Constants.LEVEL_MULTIPLIERS[GamePreferences.instance.level];
    }

    public Vector2 getPoint(int index) {
        return points.get(index);
    }

    public Vector2 getFirstPoint() {
        return points.get(0);
    }

    public Vector2 getLastPoint() {
        return points.get(points.size() - 1);
    }

    public int getNumberOfPoints() {
        return points.size();
    }

    public EmptyCircle getBall() {
        return ball;
    }

    public boolean hasFollowerObject() {
        return levelFollower != null;
    }

    public void tearDownFollower() {
        this.levelFollower.followerObjectTime = 0;
        this.levelFollower.followerObjectState = Level.PropertyState.Teardown;
    }

    public void setFollowerBody(Body body){
        this.levelFollower.followerObject.body = body;
    }

    public Body getFollowerBody() {
        return this.levelFollower.followerObject.body;
    }

    public Vector2 getFollowerPos() {
        return this.levelFollower.followerObject.position;
    }

    public float getFollowerRadius() {
        return this.levelFollower.followerObject.radius;
    }

    public void updateFollowerScale(float scale) {
        this.levelFollower.scale(scale);
    }
}
