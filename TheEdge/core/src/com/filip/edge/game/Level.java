package com.filip.edge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.screens.objects.*;
import com.filip.edge.util.Constants;
import com.filip.edge.util.DigitRenderer;
import com.filip.edge.util.GamePreferences;

import java.util.ArrayList;

public class Level {

    public static final String TAG = Level.class.getName();

    public Ball ball;
    public BackButton backButton;
    public ArrayList<AbstractCircleButtonObject> circleShapes;
    public ArrayList<MiddlePart> rectangleShapes;
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

    public Follower levelFollower;
    public Follower levelPacer;
    public ArrayList<Follower> loopers;

    public ArrayList<Hole> holes;
    public ArrayList<Follower> followers;
    public ArrayList<Follower> oscillators;
    public ArrayList<OrbiterPickup> orbiterPickups;
    public ArrayList<GoldPickup> goldPickups;

    public PropertyState disappearingState;
    public boolean disappearing;
    private float disappearingStartTime;
    private float disappearingSpeed;
    private float scaledDisappearingSpeed;
    private float disappearingTime;
    private int disappearingIndex;
    private Vector2 disappearingVectorDirection;

    public String levelInstructions;

    private int creditsIndex;
    private float creditTime;
    private static final float CREDIT_MAX_TIME = 4f;
    private static final float CREDIT_PAUSE_TIME = 1f;
    private boolean displayCredits;

    public int numberOfOrbitersFinishedWith;

    public int currentLevel;

    public Level() {
        this.creditsIndex = -1;
        this.displayCredits = this.lastStage();
        this.disappearingVectorDirection = new Vector2();
        init();
    }

    public void reset(){
        // BALL RESET
        ball.reset();

        // HOLES RESET
        for (int i = 0; i < holes.size(); ++i) {
            holes.get(i).reset();
        }

        // ORBITER PICKUP RESET
        for (int i = 0; i < orbiterPickups.size(); ++i) {
            orbiterPickups.get(i).reset();
        }

        // GOLD RESET
        for (int i = 0; i < goldPickups.size(); ++i) {
            goldPickups.get(i).reset();
        }

        // OSCILLATOR RESET
        for (int i = 0; i < oscillators.size(); ++i) {
            oscillators.get(i).reset();
        }

        // FOLLOWERS RESET
        for (int i = 0; i < followers.size(); ++i) {
            followers.get(i).reset();
        }

        // LOOPERS RESET
        for (int i = 0; i < loopers.size(); ++i) {
            loopers.get(i).reset();
        }

        // PACER RESET
        if(this.hasPacerObject()) {
            levelPacer.reset();
        }

        // LEVEL FOLLOWER RESET
        if(this.hasFollowerObject()) {
            levelFollower.reset();
        }

        // SCALING RESET
        for (int i = 0; i < rectangleShapes.size(); ++i) {
            rectangleShapes.get(i).reset();
        }

        // DISAPPEARING RESET
        if(this.disappearing) {
            disappearingTime = 0;
            disappearingIndex = 0;

            // Circles
            for (int i = circleShapes.size() - 1; i >= 0; --i) {
                circleShapes.get(i).visible = true;
                circleShapes.get(i).body.setActive(true);
            }

            // Rectangles
            for (int i = 0; i < rectangleShapes.size(); ++i) {
                rectangleShapes.get(i).visible = true;
                rectangleShapes.get(i).body.setActive(true);
            }

            calculateScaledDisappearingSpeed();
        }
    }

    private void init() {
        Gdx.app.log(TAG, "LEVEL init zone = " + GamePreferences.instance.zone + " stage = " + GamePreferences.instance.stage);

        currentLevel = GamePreferences.instance.stage;
        for(int i = GamePreferences.instance.zone - 1; i >= 0; --i ){
            currentLevel += StageLoader.getZone(i).getNumberOfStages();
        }
        Gdx.app.log(TAG, "LEVEL init levelNumber = " + currentLevel);
        Gdx.app.log(TAG, "LEVEL init GamePreferences.instance.levelTries.size() = " + GamePreferences.instance.levelTries.size());

        if(GamePreferences.instance.levelTries.size() == currentLevel){
            GamePreferences.instance.levelTries.add(currentLevel, 1);
            GamePreferences.instance.save();
        }

        this.points = StageLoader.getPoints(GamePreferences.instance.zone, GamePreferences.instance.stage);
        this.levelInstructions = StageLoader.getStageInstructions(GamePreferences.instance.zone, GamePreferences.instance.stage);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        float horizontalScale = width / Constants.BASE_SCREEN_WIDTH;
        float verticalScale = height / Constants.BASE_SCREEN_HEIGHT;

        circleShapes = new ArrayList<AbstractCircleButtonObject>();
        rectangleShapes = new ArrayList<MiddlePart>();

        holes = new ArrayList<Hole>();
        orbiterPickups = new ArrayList<OrbiterPickup>();
        followers = new ArrayList<Follower>();
        loopers = new ArrayList<Follower>();
        oscillators = new ArrayList<Follower>();
        goldPickups = new ArrayList<GoldPickup>();

        // Ball
        ball = new Ball(Constants.BALL_RADIUS * 2 * horizontalScale,
                this.getFirstPoint().x, this.getFirstPoint().y, Constants.BLACK, Constants.WHITE, false, "CircleBall");

        // Backbutton
        backButton = new BackButton(width * 0.05f,   // size
                (int) (width * 0.03),    // x
                (int) (width * 0.03),     // y
                Constants.WHITE,         // outside color
                Constants.ZONE_COLORS[GamePreferences.instance.zone],
                false, "CircleBack" + GamePreferences.instance.zone);      // inside color

        // Add Start Circle
        EndTarget st = new EndTarget(Constants.END_CIRCLE_RADIUS * 2 * horizontalScale,
                this.getFirstPoint().x, this.getFirstPoint().y, Constants.GREEN, Constants.WHITE, false, "CircleE");
        st.scale.set(points.get(0).extraCircleScale, points.get(0).extraCircleScale);
        circleShapes.add(st);

        startCircleGreenIcon = new EndTarget(Constants.END_CIRCLE_RADIUS * 2 * horizontalScale,
                this.getFirstPoint().x, this.getFirstPoint().y, Constants.GREEN, Constants.WHITE, false, "CircleG");
        startCircleGreenIcon.scale.set(points.get(0).extraCircleScale, points.get(0).extraCircleScale);
        startCircleYellowIcon = new EndTarget(Constants.END_CIRCLE_RADIUS * 2 * horizontalScale,
                this.getFirstPoint().x, this.getFirstPoint().y, Constants.YELLOW, Constants.WHITE, false, "CircleY");
        startCircleYellowIcon.scale.set(points.get(0).extraCircleScale, points.get(0).extraCircleScale);
        startCircleRedIcon = new EndTarget(Constants.END_CIRCLE_RADIUS * 2 * horizontalScale,
                this.getFirstPoint().x, this.getFirstPoint().y, Constants.RED, Constants.WHITE, false, "CircleR");
        startCircleRedIcon.scale.set(points.get(0).extraCircleScale, points.get(0).extraCircleScale);
        startCircle = startCircleRedIcon;

        // Add Middle Circles
        for (int i = 1; i < this.getNumberOfPoints() - 1; ++i) {
            EmptyCircle m = new EmptyCircle(Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                    points.get(i).x, points.get(i).y, Constants.WHITE, Constants.TRANSPARENT, true, "Circle");
            m.scale.set(points.get(i).extraCircleScale, points.get(i).extraCircleScale);

            // Do not add multiple circles that are the same
            if (!circleShapes.contains(m)) {
                circleShapes.add(m);
            }

            // Add a hole to the point
            if (points.get(i).hasAHole) {
                holes.add(new Hole(Constants.HOLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                        this.points.get(i).x, this.points.get(i).y,
                        this.points.get(i).holeStartupIndex, this.points.get(i).holeScaleIndex, false, "CircleHole" + GamePreferences.instance.zone));
            }

            // Add a orbiter pickup to the hole
            if (points.get(i).orbiterPickup) {
                orbiterPickups.add(new OrbiterPickup(Constants.ORBITER_PICKUP_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                        this.points.get(i).x, this.points.get(i).y,
                        this.points.get(i).orbiterStartupIndex, this.points.get(i).orbiterDisappearIndex, false, "CircleOrbiterPickup"));
            }

            // Add gold
            if (points.get(i).gold) {
                goldPickups.add(new GoldPickup(Constants.GOLD_BODY_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                        this.points.get(i).x, this.points.get(i).y, false, "CircleGold"));
            }

            // Add a vertical oscillator to the point
            if (points.get(i).hasVerticalOscillator) {
                float dis = Constants.OSCILLATION_DISTANCE * this.getLevelMultiplier() * horizontalScale;
                ArrayList<Vector2> localPoints = new ArrayList<Vector2>();
                localPoints.add(new Vector2(points.get(i).x + dis, points.get(i).y));
                localPoints.add(new Vector2(points.get(i).x - dis, points.get(i).y));
                oscillators.add(new Follower(Follower.Type.VerticalOscillator, Constants.RED,
                        Constants.OSCILLATOR_STARTTIME[points.get(i).oscillatorStartupIndex],
                        new Vector2(Constants.OSCILLATOR_SPEED[points.get(i).oscillatorSpeedIndex] * verticalScale,
                                Constants.OSCILLATOR_SPEED[points.get(i).oscillatorSpeedIndex] * verticalScale),
                        Constants.OSCILLATOR_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                        points.get(i), localPoints, 1, true, false,
                        false, "CircleOV"
                ));
            }

            // Add a horizontal oscillator
            if (points.get(i).hasHorizontalOscillator) {
                float dis = Constants.OSCILLATION_DISTANCE * this.getLevelMultiplier() * horizontalScale;
                ArrayList<Vector2> localPoints = new ArrayList<Vector2>();
                localPoints.add(new Vector2(points.get(i).x, points.get(i).y + dis));
                localPoints.add(new Vector2(points.get(i).x, points.get(i).y - dis));
                oscillators.add(new Follower(Follower.Type.HorizontalOscillator, Constants.RED,
                        Constants.OSCILLATOR_STARTTIME[points.get(i).oscillatorStartupIndex],
                        new Vector2(Constants.OSCILLATOR_SPEED[points.get(i).oscillatorSpeedIndex] * horizontalScale,
                                Constants.OSCILLATOR_SPEED[points.get(i).oscillatorSpeedIndex] * horizontalScale),
                        Constants.OSCILLATOR_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                        points.get(i), localPoints, 1, true, false,
                        false, "CircleOH"
                ));
            }

            // Add the followers
            if (points.get(i).followerDirection != 0) {
                ArrayList<Vector2> localPoints = new ArrayList<Vector2>();
                localPoints.add(points.get(i));
                localPoints.add(points.get(i + points.get(i).followerDirection));
                followers.add(new Follower(Follower.Type.AreaFollower, Constants.RED,
                        Constants.FOLLOWER_STARTTIME[points.get(i).followStartupIndex],
                        new Vector2(Constants.FOLLOWER_SPEED[points.get(i).followSpeedIndex] * horizontalScale,
                                Constants.FOLLOWER_SPEED[points.get(i).followSpeedIndex] * verticalScale),
                        Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                        points.get(i), localPoints, points.get(i).followerDirection, points.get(i).followerIsBackAndForth, false,
                        true, "CircleFollowers"

                ));
            }
        }

        // Add EndCircle - for target collision
        endCircle = new EmptyCircle(Constants.END_CIRCLE_RADIUS * 2 * Constants.END_CIRCLE_OUTLINE_RADIUS_MULTIPLIER * horizontalScale,
                this.getLastPoint().x, this.getLastPoint().y, Constants.WHITE, Constants.WHITE, false, "CircleEndTarget");
        endCircle.scale.set(points.get(points.size()-1).extraCircleScale, points.get(points.size()-1).extraCircleScale);

        // Add EndCircle - for boundary collision
        EndTarget et = new EndTarget(Constants.END_CIRCLE_RADIUS * 2 * horizontalScale,
                this.getLastPoint().x, this.getLastPoint().y, Constants.GREEN, Constants.WHITE, false, "CircleE");
        et.scale.set(points.get(points.size()-1).extraCircleScale, points.get(points.size()-1).extraCircleScale);
        circleShapes.add(et);

        finishCircleGreenIcon = new EndTarget(Constants.END_CIRCLE_RADIUS * 2 * horizontalScale,
                this.getLastPoint().x, this.getLastPoint().y, Constants.GREEN, Constants.WHITE, false, "CircleG");
        finishCircleGreenIcon.scale.set(points.get(points.size()-1).extraCircleScale, points.get(points.size()-1).extraCircleScale);
        finishCircleYellowIcon = new EndTarget(Constants.END_CIRCLE_RADIUS * 2 * horizontalScale,
                this.getLastPoint().x, this.getLastPoint().y, Constants.YELLOW, Constants.WHITE, false, "CircleY");
        finishCircleYellowIcon.scale.set(points.get(points.size()-1).extraCircleScale, points.get(points.size()-1).extraCircleScale);
        finishCircleRedIcon = new EndTarget(Constants.END_CIRCLE_RADIUS * 2 * horizontalScale,
                this.getLastPoint().x, this.getLastPoint().y, Constants.RED, Constants.WHITE, false, "CircleR");
        finishCircleRedIcon.scale.set(points.get(points.size()-1).extraCircleScale, points.get(points.size()-1).extraCircleScale);

        finishCircle = finishCircleRedIcon;

        // Add Rectangles
        for (int i = 0; i < this.getNumberOfPoints() - 1; ++i) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i + 1);

            if(!p1.epsilonEquals(p2, 0.01f)) {

                Vector2 midpoint = new Vector2((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
                float angle = (float) Math.toDegrees(Math.atan2(p1.y - p2.y, p1.x - p2.x));

                MiddlePart s = new MiddlePart(Vector2.dst(p1.x, p1.y, p2.x, p2.y),
                        Constants.RECTANGLE_WIDTH * this.getLevelMultiplier() * horizontalScale * points.get(i).extraRectangleScale,
                        midpoint.x, midpoint.y, Constants.WHITE, Constants.TURQUOISE, true, "Rectangle");
                s.rotation = angle;

                if (points.get(i).disappears) {
                    s.disappears = true;
                    s.disappearsAppearsStartupTime = Constants.DISAPPEARING_OBJECT_STARTTIME[points.get(i).disappearsAppearsStartupIndex];
                    s.disappearsAppearsTime = Constants.DISAPPEARING_OBJECT_TIME[points.get(i).disappearsAppearsTimeIndex];
                }
                if (points.get(i).appears) {
                    s.appears = true;
                    if (points.get(i).disappearsAppearsStartupIndex != -1 && points.get(i).disappearsAppearsTimeIndex != -1) {
                        s.disappearsAppearsStartupTime = Constants.DISAPPEARING_OBJECT_STARTTIME[points.get(i).disappearsAppearsStartupIndex];
                        s.disappearsAppearsTime = Constants.DISAPPEARING_OBJECT_TIME[points.get(i).disappearsAppearsTimeIndex];
                    } else {
                        s.disappearsAppearsStartupTime = -1;
                        s.disappearsAppearsTime = -1;
                    }
                }

                // Do not add multiple rectangles that are the same
                if (!rectangleShapes.contains(s)) {
                    rectangleShapes.add(s);
                }
            }
        }

        Stage s = StageLoader.getZone(GamePreferences.instance.zone).getStage(GamePreferences.instance.stage);

        // Add disappearing stage
        if (s.disappears) {
            disappearingState = PropertyState.Inactive;
            disappearing = s.disappears;
            disappearingStartTime = Constants.DISAPPEARING_STARTTIME[s.disappearsStartupTimeIndex];
            disappearingSpeed = Constants.DISAPPEARING_TIME[s.disappearSpeedIndex];
            disappearingIndex = 0;

            calculateScaledDisappearingSpeed();
        }

        // Add loopers
        if (s.loopers != null) {
            for (int looperIndex = 0; looperIndex < s.loopers.size(); ++looperIndex) {

                ArrayList<Vector2> p = (ArrayList<Vector2>) (ArrayList<?>) (s.loopers.get(looperIndex).points);

                loopers.add(new Follower(Follower.Type.Looper, Constants.RED,
                        Constants.FOLLOWER_STARTTIME[s.loopers.get(looperIndex).startupTimeIndex],
                        new Vector2(Constants.FOLLOWER_SPEED[s.loopers.get(looperIndex).speedIndex] * horizontalScale,
                                Constants.FOLLOWER_SPEED[s.loopers.get(looperIndex).speedIndex] * verticalScale),
                        Constants.INSIDE_CIRCLE_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                        s.loopers.get(looperIndex).points.get(0), p, 1, false, false,
                        true, "CircleLooper"
                ));
            }
        }

        // Add follower
        if (s.follower != null) {
            ArrayList<Vector2> p = (ArrayList<Vector2>) (ArrayList<?>) (s.follower.points);

            levelFollower = new Follower(Follower.Type.LevelFollower, Constants.RED,
                    Constants.FOLLOWER_STARTTIME[s.follower.startupTimeIndex],
                    new Vector2(Constants.FOLLOWER_SPEED[s.follower.speedIndex] * horizontalScale,
                            Constants.FOLLOWER_SPEED[s.follower.speedIndex] * verticalScale),
                    Constants.PACER_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                    s.follower.points.get(0), p, 1, true, false, false, "CircleFollower"
            );
        }

        // Add the pacer
        if (s.pacer != null) {
            ArrayList<Vector2> p = (ArrayList<Vector2>) (ArrayList<?>) (s.pacer.points);

            levelPacer = new Follower(Follower.Type.Pacer, Constants.YELLOW,
                    Constants.PACER_STARTTIME[s.pacer.startupTimeIndex],
                    new Vector2(Constants.PACER_SPEED[s.pacer.speedIndex] * horizontalScale,
                            Constants.PACER_SPEED[s.pacer.speedIndex] * verticalScale),
                    Constants.PACER_RADIUS * 2 * this.getLevelMultiplier() * horizontalScale,
                    s.pacer.points.get(0), p, 1, false, true, false, "CirclePacer"
            );
        }
    }

    private void calculateScaledDisappearingSpeed() {;
        disappearingVectorDirection.set(circleShapes.get(disappearingIndex).position.x - circleShapes.get(disappearingIndex + 1).position.x,
                circleShapes.get(disappearingIndex).position.y - circleShapes.get(disappearingIndex + 1).position.y);
        disappearingVectorDirection.nor();
        float xPart = Math.abs((disappearingSpeed) * disappearingVectorDirection.x) *
                (StageLoader.getDistanceBetweenTwoSideBySidePointsInY() / StageLoader.getDistanceBetweenTwoSideBySidePointsInX());
        float yPart = Math.abs((disappearingSpeed) * disappearingVectorDirection.y);
        scaledDisappearingSpeed =  xPart + yPart;
    }

    public void update(float deltaTime) {
        ball.update(deltaTime);
        int i = 0;
        for (i = 0; i < holes.size(); ++i) {
            holes.get(i).update(deltaTime);
        }

        for (i = 0; i < orbiterPickups.size(); ++i) {
            orbiterPickups.get(i).update(deltaTime);
        }

        for (i = 0; i < goldPickups.size(); ++i) {
            goldPickups.get(i).update(deltaTime);
        }

        for (i = 0; i < followers.size(); ++i) {
            followers.get(i).update(deltaTime);
        }

        for (i = 0; i < loopers.size(); ++i) {
            loopers.get(i).update(deltaTime);
        }

        for (i = 0; i < oscillators.size(); ++i) {
            oscillators.get(i).update(deltaTime);
        }

        for (i = 0; i < rectangleShapes.size(); ++i) {
            rectangleShapes.get(i).update(deltaTime);
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
                    if (disappearingTime > scaledDisappearingSpeed) {
                        disappearingTime = 0;
                        if (disappearingIndex > 0 && disappearingIndex < circleShapes.size() - 1 /*do not remove the final circle*/) {
                            circleShapes.get(disappearingIndex).visible = false;
                            circleShapes.get(disappearingIndex).body.setActive(false);
                        }
                        if (disappearingIndex < rectangleShapes.size()) {
                            rectangleShapes.get(disappearingIndex).visible = false;
                            rectangleShapes.get(disappearingIndex).body.setActive(false);
                        }
                        disappearingIndex++;

                        if (disappearingIndex > 0 && disappearingIndex < circleShapes.size() - 1) {
                            calculateScaledDisappearingSpeed();
                        }
                    }
                    break;
            }
        }

        if (levelFollower != null) {
            levelFollower.update(deltaTime);
        }

        if(levelPacer != null) {
            levelPacer.update(deltaTime);
        }
    }

    public void updateCredits(float deltaTime) {
        if(this.displayCredits) {
            this.creditTime += deltaTime;
            if(this.creditTime > CREDIT_MAX_TIME + CREDIT_PAUSE_TIME) {
                this.creditTime = 0;
                this.creditsIndex++;
                if(this.creditsIndex > Constants.NUMBER_OF_CREDITS - 1){
                    this.displayCredits = false;
                }
            }
        }
    }

    public boolean lastStage() {
        return GamePreferences.instance.zone == StageLoader.getNumberOfZones() - 1 && GamePreferences.instance.stage == StageLoader.getZoneStages(GamePreferences.instance.zone) - 1;
    }

    public void render(SpriteBatch batch) {
        int i;
        for (i = circleShapes.size() - 1; i >= 0; --i) {
            circleShapes.get(i).render(batch);
        }

        if (this.startCircle != null) {
            startCircle.render(batch);
        }

        if (this.finishCircle != null) {
            finishCircle.render(batch);
        }

        for (i = 0; i < rectangleShapes.size(); ++i) {
            rectangleShapes.get(i).render(batch);
        }

        for (i = 0; i < holes.size(); ++i) {
            holes.get(i).render(batch);
        }

        for (i = 0; i < orbiterPickups.size(); ++i) {
            orbiterPickups.get(i).render(batch);
        }

        for (i = 0; i < goldPickups.size(); ++i) {
            goldPickups.get(i).render(batch);
        }

        for (i = 0; i < followers.size(); ++i) {
            followers.get(i).render(batch);
        }

        for (i = 0; i < loopers.size(); ++i) {
            loopers.get(i).render(batch);
        }

        for (i = 0; i < oscillators.size(); ++i) {
            oscillators.get(i).render(batch);
        }

        if (levelFollower != null) {
            levelFollower.render(batch);
        }

        if(levelPacer != null)  {
            levelPacer.render(batch);
        }

        ball.render(batch);

        if(levelInstructions != ""){
            DigitRenderer.instance.renderStringCentered(levelInstructions, Gdx.graphics.getHeight() -
                    DigitRenderer.instance.digitHeight / 2 -
                    DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS, batch);
        }
        if(this.displayCredits && this.creditsIndex != -1 && creditTime < CREDIT_MAX_TIME){
            DigitRenderer.instance.renderStringCentered(Constants.CREDIT_ARRAY[this.creditsIndex][0], Gdx.graphics.getHeight() -
                    DigitRenderer.instance.digitHeight / 2 -
                    DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS -
                    DigitRenderer.instance.digitHeight - DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS, batch);
            DigitRenderer.instance.renderStringCentered(Constants.CREDIT_ARRAY[this.creditsIndex][1], Gdx.graphics.getHeight() -
                    DigitRenderer.instance.digitHeight / 2 -
                    DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS, batch);

        }

        if(GamePreferences.instance.zone == 0 && GamePreferences.instance.stage == 0) {
            DigitRenderer.instance.renderStringAtCenterXPoint("MEET", (int) this.points.get(0).x, (int) (this.points.get(0).y - this.ball.radius * 6), batch, 0.6f);
            DigitRenderer.instance.renderStringAtCenterXPoint("EDGY", (int) this.points.get(0).x, (int) (this.points.get(0).y - this.ball.radius * 3.7), batch, 0.6f);
        }
    }

    public void renderBackButton(SpriteBatch batch) {
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

    public boolean hasPacerObject() { return levelPacer != null; }

    public void tearDownPacer() {
        if (this.levelPacer != null &&
                (this.levelPacer.followerObjectState != PropertyState.Gone && this.levelPacer.followerObjectState != PropertyState.Inactive)) {
            this.levelPacer.followerObjectTime = 0;
            this.levelPacer.followerObjectState = Level.PropertyState.Teardown;
        }
    }

    public Body getPacerBody() {
        return this.levelPacer.followerObject.body;
    }

    public void setPacerBody(Body body) {
        this.levelPacer.followerObject.body = body;
    }

    public Vector2 getPacerPos() {
        return this.levelPacer.followerObject.position;
    }

    public float getPacerRadius() {
        return this.levelPacer.followerObject.radius;
    }

    public void updatePacerScale(float scale) {
        this.levelPacer.scale(scale);
    }

    public boolean hasFollowerObject() {
        return levelFollower != null;
    }

    public void tearDownFollower() {
        if(this.levelFollower != null &&
                (this.levelFollower.followerObjectState != PropertyState.Gone && this.levelFollower.followerObjectState != PropertyState.Inactive)) {
            this.levelFollower.followerObjectTime = 0;
            this.levelFollower.followerObjectState = Level.PropertyState.Teardown;
        }
    }

    public Body getFollowerBody() {
        return this.levelFollower.followerObject.body;
    }

    public void setFollowerBody(Body body) {
        this.levelFollower.followerObject.body = body;
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

    public enum PropertyState {
        Inactive,
        Buildup,
        Active,
        Teardown,
        Gone
    }
}
