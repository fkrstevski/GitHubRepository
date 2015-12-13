package com.filip.edge.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.filip.edge.screens.*;
import com.filip.edge.screens.objects.ScoreUpdateObject;
import com.filip.edge.util.*;

public class WorldController extends InputAdapter implements Disposable, ContactListener {
    private static final String TAG = WorldController.class.getName();
    private static final float READY_TIME = 2.0f;
    private static final float GREEN_ICON_TIME = 0.75f;
    public Level level;
    public boolean renderPhysics;
    public int numberOfContacts;
    public CameraHelper cameraHelper;
    public World b2world;
    public LevelState state;
    public float readyTimeRatio;
    private DirectedGame game;
    private float readyTime;
    private float endTime;
    private float greenTime;
    private float levelScore;

    private float levelTime;

    private static final int MAX_NUMBER_ORBITERS = 2;
    private boolean Orbiter1Visible = false;
    private boolean Orbiter2Visible = false;

    private boolean startMovement;

    public Color clearColor;
    public boolean colorChange;
    static final float transitionTime = 0.5f;
    float currentAdTime;

    private boolean gottenScreenshot;

    public final Array<ScoreUpdateObject> activeScoreUpdates = new Array<ScoreUpdateObject>();
    private final Pool<ScoreUpdateObject> scoreUpdateObjectPool = new Pool<ScoreUpdateObject>() {
        @Override
        protected ScoreUpdateObject newObject() {
            return new ScoreUpdateObject();
        }
    };

    public WorldController(DirectedGame game) {
        Box2D.init();
        this.game = game;
        this.renderPhysics = false;
        this.readyTime = 0;
        this.endTime = 0;
        this.greenTime = 0;
        this.readyTimeRatio = this.readyTime / READY_TIME;
        this.state = LevelState.Countdown;
        this.clearColor = new Color();
        this.gottenScreenshot = false;

        init();

        this.level.startCircle = this.level.startCircleRedIcon;
        this.level.finishCircle = this.level.finishCircleRedIcon;
        AudioManager.instance.play(Assets.instance.sounds.tickSound);
    }

    private void init() {
        cameraHelper = new CameraHelper();
        initLevel();
        GamePreferences.instance.submitData();
    }

    public void nextLevel() {
        GamePreferences.instance.level++;
        if (GamePreferences.instance.level > Constants.MAX_LEVELS - 1) {
            GamePreferences.instance.level = 0;
            GamePreferences.instance.stage++;
            if (GamePreferences.instance.stage > StageLoader.getZone(GamePreferences.instance.zone).getNumberOfStages() - 1) {
                GamePreferences.instance.stage = 0;
                GamePreferences.instance.zone++;
                if (GamePreferences.instance.zone > StageLoader.getNumberOfZones() - 1) {
                    state = LevelState.GameBeat;
                    GamePreferences.instance.scoreNeedsToBeSubmitted = true;
                    clearColor.set(Constants.ZONE_COLORS[GamePreferences.instance.zone -1]);
                    colorChange = true;
                    // Make sure we save the highest score ASAP
                    GamePreferences.instance.save();
                    this.game.submitScore(GamePreferences.instance.currentScore);

                    // Early out
                    return;
                }
                else {
                    GamePreferences.instance.save();
                    game.setScreen(new LevelResultsScreen(game, true));
                    //Early out
                    return;
                }
            }
        }

        // Save the scores
        GamePreferences.instance.save();
        game.setScreen(new LevelResultsScreen(game, false));
    }

    private void resetLevel() {
        levelScore = 0;
        Orbiter1Visible = false;
        Orbiter2Visible = false;
        startMovement = false;
        gottenScreenshot = false;
        level.reset();
    }

    public void initLevel() {
        //this.game.startMethodTracing("initLevel");

        levelScore = 0;
        Orbiter1Visible = false;
        Orbiter2Visible = false;
        startMovement = false;
        gottenScreenshot = false;
        level = new Level();

        if (b2world != null) {
            b2world.setContactListener(null);
            b2world.dispose();
        }
        b2world = new World(new Vector2(0, 0), true);
        b2world.setContactListener(this);

        initPhysics();
        //this.game.stopMethodTracing();
    }

    private void initPhysics() {
        numberOfContacts = 0;

        // Pacer object body
        if (level.hasPacerObject() && level.levelPacer.followerObject.body == null) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.KinematicBody;
            bodyDef.position.set(new Vector2(level.getPacerPos().x / Constants.BOX2D_SCALE, level.getPacerPos().y / Constants.BOX2D_SCALE));
            Body body = b2world.createBody(bodyDef);
            body.setActive(false);
            level.setPacerBody(body);
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius((level.getPacerRadius()) / Constants.BOX2D_SCALE);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            circleShape.dispose();
        }

        // Follower object body
        if (level.hasFollowerObject() && level.levelFollower.followerObject.body == null) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.KinematicBody;
            bodyDef.position.set(new Vector2(level.getFollowerPos().x / Constants.BOX2D_SCALE, level.getFollowerPos().y / Constants.BOX2D_SCALE));
            Body body = b2world.createBody(bodyDef);
            body.setActive(false);
            level.setFollowerBody(body);
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius((level.getFollowerRadius()) / Constants.BOX2D_SCALE);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            circleShape.dispose();
        }

        // Ball Physics Body
        if(level.ball.body == null)
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.position.set(new Vector2(level.ball.position.x / Constants.BOX2D_SCALE, level.ball.position.y / Constants.BOX2D_SCALE));
            Body body = b2world.createBody(bodyDef);
            level.ball.body = body;
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius((level.ball.radius * 0.1f) / Constants.BOX2D_SCALE);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            circleShape.dispose();
        }

        // Orbiter Physics Body
        for (int i = 0; i < MAX_NUMBER_ORBITERS; ++i) {
            if(level.ball.orbiters.get(i).body == null) {
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyType.DynamicBody;
                bodyDef.position.set(new Vector2(level.ball.orbiters.get(i).position.x / Constants.BOX2D_SCALE, level.ball.orbiters.get(i).position.y / Constants.BOX2D_SCALE));
                Body body = b2world.createBody(bodyDef);
                body.setActive(false);
                level.ball.orbiters.get(i).body = body;
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius((level.ball.orbiters.get(i).radius * 0.8f) / Constants.BOX2D_SCALE);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circleShape;
                fixtureDef.isSensor = true;
                body.createFixture(fixtureDef);
                circleShape.dispose();
            }
        }

        // Orbiter Pickup Physics Bodies
        for (int i = 0; i < level.orbiterPickups.size(); ++i) {
            if(level.orbiterPickups.get(i).body == null) {
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyType.StaticBody;
                bodyDef.position.set(new Vector2(level.orbiterPickups.get(i).position.x / Constants.BOX2D_SCALE, level.orbiterPickups.get(i).position.y / Constants.BOX2D_SCALE));
                Body body = b2world.createBody(bodyDef);
                level.orbiterPickups.get(i).body = body;
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius((Constants.ORBITER_PICKUP_BODY_RADIUS) / Constants.BOX2D_SCALE);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circleShape;
                fixtureDef.isSensor = true;
                body.createFixture(fixtureDef);
                circleShape.dispose();
            }
        }

        // Gold Physics Bodies
        for (int i = 0; i < level.goldPickups.size(); ++i) {
            if(level.goldPickups.get(i).body == null) {
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyType.StaticBody;
                bodyDef.position.set(new Vector2(level.goldPickups.get(i).position.x / Constants.BOX2D_SCALE, level.goldPickups.get(i).position.y / Constants.BOX2D_SCALE));
                Body body = b2world.createBody(bodyDef);
                level.goldPickups.get(i).body = body;
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius((Constants.GOLD_BODY_RADIUS) / Constants.BOX2D_SCALE);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circleShape;
                fixtureDef.isSensor = true;
                body.createFixture(fixtureDef);
                circleShape.dispose();
            }
        }

        // Hole Physics Bodies
        for (int i = 0; i < level.holes.size(); ++i) {
            if(level.holes.get(i).body == null) {
                BodyDef holeBodyDef = new BodyDef();
                holeBodyDef.type = BodyType.StaticBody;
                holeBodyDef.position.set(new Vector2(level.holes.get(i).position.x / Constants.BOX2D_SCALE, level.holes.get(i).position.y / Constants.BOX2D_SCALE));
                Body holeBody = b2world.createBody(holeBodyDef);
                level.holes.get(i).body = holeBody;
                CircleShape circleHoleShape = new CircleShape();
                circleHoleShape.setRadius((level.holes.get(i).radius) / Constants.BOX2D_SCALE);
                FixtureDef fixtureHoleDef = new FixtureDef();
                fixtureHoleDef.shape = circleHoleShape;
                fixtureHoleDef.isSensor = true;
                holeBody.createFixture(fixtureHoleDef);
                circleHoleShape.dispose();
            }
        }

        // Followers Physics Bodies
        for (int i = 0; i < level.followers.size(); ++i) {
            if(level.followers.get(i).followerObject.body == null) {
                BodyDef followerBodyDef = new BodyDef();
                followerBodyDef.type = BodyType.KinematicBody;
                followerBodyDef.position.set(new Vector2(level.followers.get(i).followerObject.position.x / Constants.BOX2D_SCALE, level.followers.get(i).followerObject.position.y / Constants.BOX2D_SCALE));
                Body followerBody = b2world.createBody(followerBodyDef);
                followerBody.setActive(false);
                level.followers.get(i).followerObject.body = followerBody;
                CircleShape circleFollowerShape = new CircleShape();
                circleFollowerShape.setRadius((level.followers.get(i).followerObject.radius) / Constants.BOX2D_SCALE);
                FixtureDef fixtureFollowerDef = new FixtureDef();
                fixtureFollowerDef.shape = circleFollowerShape;
                fixtureFollowerDef.isSensor = true;
                followerBody.createFixture(fixtureFollowerDef);
                circleFollowerShape.dispose();
            }
        }

        // Loopers Physics Bodies
        for (int i = 0; i < level.loopers.size(); ++i) {
            if(level.loopers.get(i).followerObject.body == null) {
                BodyDef followerBodyDef = new BodyDef();
                followerBodyDef.type = BodyType.KinematicBody;
                followerBodyDef.position.set(new Vector2(level.loopers.get(i).followerObject.position.x / Constants.BOX2D_SCALE, level.loopers.get(i).followerObject.position.y / Constants.BOX2D_SCALE));
                Body followerBody = b2world.createBody(followerBodyDef);
                followerBody.setActive(false);
                level.loopers.get(i).followerObject.body = followerBody;
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius((level.loopers.get(i).followerObject.radius) / Constants.BOX2D_SCALE);
                FixtureDef fixtureFollowerDef = new FixtureDef();
                fixtureFollowerDef.shape = circleShape;
                fixtureFollowerDef.isSensor = true;
                followerBody.createFixture(fixtureFollowerDef);
                circleShape.dispose();
            }
        }

        // Oscillators Physics Bodies
        for (int i = 0; i < level.oscillators.size(); ++i) {
            if(level.oscillators.get(i).followerObject.body == null) {
                BodyDef followerBodyDef = new BodyDef();
                followerBodyDef.type = BodyType.KinematicBody;
                followerBodyDef.position.set(new Vector2(level.oscillators.get(i).followerObject.position.x / Constants.BOX2D_SCALE, level.oscillators.get(i).followerObject.position.y / Constants.BOX2D_SCALE));
                Body followerBody = b2world.createBody(followerBodyDef);
                followerBody.setActive(false);
                level.oscillators.get(i).followerObject.body = followerBody;
                CircleShape circleFollowerShape = new CircleShape();
                circleFollowerShape.setRadius((level.oscillators.get(i).followerObject.radius) / Constants.BOX2D_SCALE);
                FixtureDef fixtureFollowerDef = new FixtureDef();
                fixtureFollowerDef.shape = circleFollowerShape;
                fixtureFollowerDef.isSensor = true;
                followerBody.createFixture(fixtureFollowerDef);
                circleFollowerShape.dispose();
            }
        }

        // EndTarget Physics Body
        if(level.endCircle.body == null)
        {
            BodyDef bodyDef2 = new BodyDef();
            bodyDef2.type = BodyType.StaticBody;
            bodyDef2.position.set(new Vector2(level.endCircle.position.x / Constants.BOX2D_SCALE, level.endCircle.position.y / Constants.BOX2D_SCALE));
            Body body2 = b2world.createBody(bodyDef2);
            level.endCircle.body = body2;
            CircleShape circleShape2 = new CircleShape();
            circleShape2.setRadius((level.endCircle.radius) / Constants.BOX2D_SCALE);
            FixtureDef fixtureDef2 = new FixtureDef();
            fixtureDef2.shape = circleShape2;
            fixtureDef2.isSensor = true;
            body2.createFixture(fixtureDef2);
            circleShape2.dispose();
        }

        // Middle Circles Physics Bodies
        for (int i = 0; i < level.circleShapes.size(); ++i) {
            if(level.circleShapes.get(i).body == null) {
                BodyDef bodyDef1 = new BodyDef();
                bodyDef1.type = BodyType.StaticBody;
                bodyDef1.position.set(new Vector2(level.circleShapes.get(i).position.x / Constants.BOX2D_SCALE, level.circleShapes.get(i).position.y / Constants.BOX2D_SCALE));
                Body body1 = b2world.createBody(bodyDef1);
                level.circleShapes.get(i).body = body1;
                CircleShape circleShape1 = new CircleShape();
                circleShape1.setRadius((level.circleShapes.get(i).radius * level.circleShapes.get(i).scale.x) / Constants.BOX2D_SCALE);
                FixtureDef fixtureDef1 = new FixtureDef();
                fixtureDef1.shape = circleShape1;
                fixtureDef1.isSensor = true;
                body1.createFixture(fixtureDef1);
                circleShape1.dispose();
            }
        }

        // Rectangles Physics Bodies
        for (int i = 0; i < level.rectangleShapes.size(); ++i) {
            if(level.rectangleShapes.get(i).body == null) {
                BodyDef bodyDef1 = new BodyDef();
                bodyDef1.type = BodyType.StaticBody;
                bodyDef1.position.set(new Vector2(level.rectangleShapes.get(i).position.x / Constants.BOX2D_SCALE, level.rectangleShapes.get(i).position.y / Constants.BOX2D_SCALE));
                Body body1 = b2world.createBody(bodyDef1);
                level.rectangleShapes.get(i).body = body1;
                PolygonShape polygonShape = new PolygonShape();
                Vector2 o = new Vector2(0, 0);
                polygonShape.setAsBox((level.rectangleShapes.get(i).dimension.x / 2) / Constants.BOX2D_SCALE, (level.rectangleShapes.get(i).dimension.y / 2) / Constants.BOX2D_SCALE, o, MathUtils.degreesToRadians * level.rectangleShapes.get(i).rotation);
                level.rectangleShapes.get(i).setBox((level.rectangleShapes.get(i).dimension.x / 2) / Constants.BOX2D_SCALE, (level.rectangleShapes.get(i).dimension.y / 2) / Constants.BOX2D_SCALE, o, MathUtils.degreesToRadians * level.rectangleShapes.get(i).rotation);
                FixtureDef fixtureDef1 = new FixtureDef();
                fixtureDef1.shape = polygonShape;
                fixtureDef1.isSensor = true;
                body1.createFixture(fixtureDef1);
                polygonShape.dispose();
                level.rectangleShapes.get(i).reset();
            }
        }
    }

    public void update(float deltaTime) {

        cameraHelper.update(deltaTime);

        handleDebugInput(deltaTime);

        level.updateCredits(deltaTime);

        ScoreUpdateObject item;
        for (int i = activeScoreUpdates.size; --i >= 0;) {
            item = activeScoreUpdates.get(i);
            if (item.isAlive == false) {
                activeScoreUpdates.removeIndex(i);
                scoreUpdateObjectPool.free(item);
            }
            else {
                item.update(deltaTime);
            }
        }

        if (state == LevelState.Countdown) {
            this.readyTime += deltaTime;
            this.readyTimeRatio = this.readyTime / READY_TIME;

            // step the physics so that the world updates before the game starts
            // fixes an issue when we reset a level
            b2world.step(deltaTime, 8, 3);

            if (this.level.startCircle != null && this.level.startCircle != this.level.startCircleRedIcon && this.readyTimeRatio >= 0.0f && this.readyTimeRatio < 0.5f) {
                this.level.startCircle = this.level.startCircleRedIcon;
                this.level.finishCircle = this.level.finishCircleRedIcon;
                AudioManager.instance.play(Assets.instance.sounds.tickSound);
            } else if (this.level.startCircle != null && this.level.startCircle != this.level.startCircleYellowIcon && this.readyTimeRatio > 0.5f && this.readyTimeRatio < 1f) {
                this.level.startCircle = this.level.startCircleYellowIcon;
                this.level.finishCircle = this.level.finishCircleYellowIcon;
                AudioManager.instance.play(Assets.instance.sounds.tickSound);
            }

            if (readyTime > READY_TIME) {
                readyTime = 0;
                //this.readyTimeRatio = this.readyTime / READY_TIME;
                this.level.startCircle = this.level.startCircleGreenIcon;
                this.level.finishCircle = this.level.finishCircleGreenIcon;
                this.greenTime = 0;
                AudioManager.instance.play(Assets.instance.sounds.tickSound, 1, 2);
                this.state = LevelState.Gameplay;

                levelTime = 0;

                // RESET STATE FOR PACER
                if(this.level.hasPacerObject()) {
                    this.level.levelPacer.start();
                }

                // RESET STATE FOR FOLLOWER
                if(this.level.hasFollowerObject()) {
                    this.level.levelFollower.start();
                }

                // RESET STATE FOR FOLLOWERS
                for (int i = 0; i < level.followers.size(); ++i) {
                    level.followers.get(i).start();
                }

                // RESET STATE FOR LOOPERS
                for (int i = 0; i < level.loopers.size(); ++i) {
                    level.loopers.get(i).start();
                }

                // RESET STATE FOR HOLES
                for (int i = 0; i < level.holes.size(); ++i) {
                    level.holes.get(i).start();
                }

                // RESET STATE FOR OSCILLATORS
                for (int i = 0; i < level.oscillators.size(); ++i) {
                    level.oscillators.get(i).start();
                }

                // RESET STATE FOR ORBITER PICKUPS
                for (int i = 0; i < level.orbiterPickups.size(); ++i) {
                    level.orbiterPickups.get(i).start();
                }

                // RESET STATE FOR GOLD
                for (int i = 0; i < level.goldPickups.size(); ++i) {
                    level.goldPickups.get(i).start();
                }

                // RESET STATE FOR DISAPPEARING
                if(level.disappearing) {
                    level.disappearingState = Level.PropertyState.Inactive;
                }

                // RESET STATE FOR SCALING
                for (int i = 0; i < level.rectangleShapes.size(); ++i) {
                    level.rectangleShapes.get(i).start();
                }
            }

        } else if (state == LevelState.LevelComplete) {
            this.endTime += deltaTime;

            // Move the ball and orbiters into the end point
            this.level.ball.position.lerp(this.level.getLastPoint(), endTime / Constants.END_TIME);
            if((int)((endTime / Constants.END_TIME) * 10) % 2 == 0) {
                this.level.ball.direction = 1;
            }
            else {
                this.level.ball.direction = -1;
            }
            for(int i = 0; i < this.level.ball.orbiters.size(); ++i) {
                this.level.ball.orbiters.get(i).position.lerp(this.level.getLastPoint(), endTime / Constants.END_TIME);
            }

            if (level.hasFollowerObject()) {
                level.updateFollowerScale((1 - endTime / Constants.END_TIME));
            }

            if (level.hasPacerObject()) {
                level.updatePacerScale((1 - endTime / Constants.END_TIME));
            }

            for (int i = 0; i < level.followers.size(); ++i) {
                level.followers.get(i).scale((1 - endTime / Constants.END_TIME));
            }

            for (int i = 0; i < level.loopers.size(); ++i) {
                level.loopers.get(i).scale((1 - endTime / Constants.END_TIME));
            }

            for (int i = 0; i < level.oscillators.size(); ++i) {
                level.oscillators.get(i).scale((1 - endTime / Constants.END_TIME));
            }

            if(level.numberOfOrbitersFinishedWith > 0) {
                GamePreferences.instance.currentScore += Constants.SCORE_INCREMENT_FOR_SAVED_ORBITER * level.numberOfOrbitersFinishedWith;
                addScoreUpdate(Constants.SCORE_INCREMENT_FOR_SAVED_ORBITER * level.numberOfOrbitersFinishedWith);
                level.numberOfOrbitersFinishedWith = 0;
                AudioManager.instance.play(Assets.instance.sounds.tickSound);
            }

            if(this.endTime / Constants.END_TIME > 0.5f) {
                if(!gottenScreenshot){
                    ScreenshotFactory.getScreenShot(false);
                    gottenScreenshot = true;
                }
                //System.gc();
            }

            if (endTime > Constants.END_TIME) {
                endTime = 0;
                this.state = LevelState.Countdown;
                this.level.startCircle = this.level.startCircleRedIcon;
                this.level.finishCircle = this.level.finishCircleRedIcon;
                AudioManager.instance.play(Assets.instance.sounds.tickSound);
                nextLevel();
            }
        } else if (state == LevelState.OffTheEdge) {
            this.endTime += deltaTime;

            level.ball.scale.set(level.ball.scale.x * (1 - endTime / Constants.END_TIME), level.ball.scale.y * (1 - endTime / Constants.END_TIME));
            if (endTime > Constants.END_TIME) {
                endTime = 0;
                if (GamePreferences.instance.currentScore <= 0) {
                    GameOver();
                } else {
                    this.state = LevelState.Countdown;
                    this.level.startCircle = this.level.startCircleRedIcon;
                    this.level.finishCircle = this.level.finishCircleRedIcon;
                    AudioManager.instance.play(Assets.instance.sounds.tickSound);
                    this.resetLevel();
                }
            }
        } else if (state == LevelState.Gameplay) {

            levelTime+=deltaTime;

            // Update the score
            // need to do something fishy since current score is a long and subtracting
            // a small float might not update the actual value
            levelScore += deltaTime * 400 ;
            if (levelScore > 1) {
                GamePreferences.instance.currentScore -= (int) levelScore;
                levelScore = levelScore % 1;
            }

            if (GamePreferences.instance.currentScore <= 0) {
                GameOver();
            } else {
                level.update(deltaTime);
                handleInputGame(deltaTime);
                b2world.step(deltaTime, 8, 3);

                // We can only set the orbit's body active outside of the
                // step for the physics world
                if(Orbiter1Visible) {
                    this.level.ball.orbiters.get(0).visible = true;
                    if(!this.level.ball.orbiters.get(1).body.isActive()) {
                        this.level.ball.orbiters.get(0).body.setActive(true);
                    }
                }

                if(Orbiter2Visible) {
                    this.level.ball.orbiters.get(1).visible = true;
                    if(!this.level.ball.orbiters.get(0).body.isActive()) {
                        this.level.ball.orbiters.get(1).body.setActive(true);
                    }
                }

                this.greenTime += deltaTime;
                if (this.greenTime > GREEN_ICON_TIME) {
                    this.greenTime = 0;
                    this.level.finishCircle = null;
                    this.level.startCircle = null;
                }

                if (this.numberOfContacts == 0) {
                    fallOff();
                }
            }
        }
        else if (state == LevelState.GameBeat) {

            clearColor.lerp(Constants.ZONE_COLORS[0], currentAdTime / transitionTime);

            currentAdTime+=deltaTime;
            if(currentAdTime > transitionTime) {
                currentAdTime = 0;
                GamePreferences.instance.zone = 0;
                game.setScreen(new ResultsScreen(game));
            }
        }
    }

    private void GameOver() {
        state = LevelState.GameOver;
        AudioManager.instance.play(Assets.instance.sounds.liveLost);
        game.setScreen(new GameOverScreen(game));
    }

    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != ApplicationType.Desktop) {
            return;
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        }
        if (Gdx.input.isKeyPressed(Keys.COMMA)) {
            cameraHelper.addZoom(camZoomSpeed);
        }
        if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
            cameraHelper.addZoom(-camZoomSpeed);
        }
        if (Gdx.input.isKeyPressed(Keys.SLASH)) {
            cameraHelper.setZoom(1);
        }
    }

    private void handleInputGame(float deltaTime) {
        if (Gdx.app.getType() == ApplicationType.Desktop) {
            // Camera Controls (move)
            float ballMoveSpeed = (15000 / Constants.BOX2D_SCALE * Gdx.graphics.getWidth() / Constants.BASE_SCREEN_WIDTH) * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
                ballMoveSpeed *= camMoveSpeedAccelerationFactor;
            }
            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                moveBall(-ballMoveSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                moveBall(ballMoveSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Keys.UP)) {
                moveBall(0, -ballMoveSpeed);
            }
            if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                moveBall(0, ballMoveSpeed);
            }
        } else {
            float x, y;
            int width = Gdx.graphics.getWidth();
            int height = Gdx.graphics.getHeight();

            float horizontalScale = width / Constants.BASE_SCREEN_WIDTH;
            float verticalScale = height / Constants.BASE_SCREEN_HEIGHT;

            x = -Gdx.input.getAccelerometerX();
            y = -Gdx.input.getAccelerometerY();

            if (startMovement == false) {
                if( x > 1 || x < -1 || y > 1 || y < -1) {
                    startMovement = true;
                }
            }
            else {
                moveBall(y * Constants.BALL_SPEED[GamePreferences.instance.zone] * horizontalScale * deltaTime,
                        x * Constants.BALL_SPEED[GamePreferences.instance.zone] * verticalScale * deltaTime);

            }
        }
    }

    private void moveBall(float x, float y) {
        level.ball.body.setLinearVelocity(x, y);
    }

    @Override
    public boolean keyUp(int keycode) {
        // Reset game world
        if (keycode == Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        // Toggle camera follow
        else if (keycode == Keys.ENTER) {
            //this.nextLevel();
            levelComplete();
        }
        // Back to Menu
        else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            backToMenu();
        } else if (keycode == Keys.SPACE) {
            this.renderPhysics = !this.renderPhysics;
        }

        return false;
    }

    private void backToMenu() {
        // switch to menu screen
        game.setScreen(new MenuScreen(game, false));
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (level.backButton.isTouched(screenX, screenY)) {
            backToMenu();
        } else if (level.endCircle.isTouched(screenX, screenY)) {
            levelComplete();
        } else if (level.startCircleGreenIcon.isTouched(screenX, screenY)) {
            Constants.DEBUG_BUILD = !Constants.DEBUG_BUILD;
        } else {
            if (Constants.DEBUG_BUILD) {
                this.renderPhysics = !this.renderPhysics;
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        if (b2world != null) {
            b2world.dispose();
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureB().getBody() == level.ball.body) {
            for(int j = 0; j < level.ball.orbiters.size(); ++j){
                if (contact.getFixtureA().getBody() == level.ball.orbiters.get(j).body) {
                    return;
                }
            }
            numberOfContacts--;
        }
        else if (contact.getFixtureA().getBody() == level.ball.body) {
            for(int j = 0; j < level.ball.orbiters.size(); ++j){
                if (contact.getFixtureB().getBody() == level.ball.orbiters.get(j).body) {
                    return;
                }
            }
            numberOfContacts--;
        }
    }

    @Override
    public void beginContact(Contact contact) {
        int j = 0;
        for (int i = 0; i < level.ball.orbiters.size(); ++i) {
            if (contact.getFixtureB().getBody() == level.ball.orbiters.get(i).body) {
                if (level.hasFollowerObject() && contact.getFixtureA().getBody() == level.getFollowerBody()) {
                    level.levelFollower.destroy();
                    doOrbiterCollision(i);
                }
                else if (level.hasPacerObject() && contact.getFixtureA().getBody() == level.getPacerBody()) {
                    level.levelPacer.destroy();
                    doOrbiterCollision(i);
                }

                else {
                    for (j = 0; j < level.followers.size(); ++j) {
                        if (contact.getFixtureA().getBody() == level.followers.get(j).followerObject.body) {
                            level.followers.get(j).destroy();
                            doOrbiterCollision(i);
                            break;
                        }
                    }
                    for (j = 0; j < level.loopers.size(); ++j) {
                        if (contact.getFixtureA().getBody() == level.loopers.get(j).followerObject.body) {
                            level.loopers.get(j).destroy();
                            doOrbiterCollision(i);
                            break;
                        }
                    }
                    for (j = 0; j < level.oscillators.size(); ++j) {
                        if (contact.getFixtureA().getBody() == level.oscillators.get(j).followerObject.body) {
                            level.oscillators.get(j).destroy();
                            doOrbiterCollision(i);
                            break;
                        }
                    }
                }
            }
            if (contact.getFixtureA().getBody() == level.ball.orbiters.get(i).body) {
                if (level.hasFollowerObject() && contact.getFixtureB().getBody() == level.getFollowerBody()) {
                    level.levelFollower.destroy();
                    doOrbiterCollision(i);
                }
                else if (level.hasPacerObject() && contact.getFixtureB().getBody() == level.getPacerBody()) {
                    level.levelPacer.destroy();
                    doOrbiterCollision(i);
                }

                else {
                    for (j = 0; j < level.followers.size(); ++j) {
                        if (contact.getFixtureB().getBody() == level.followers.get(j).followerObject.body) {
                            level.followers.get(j).destroy();
                            doOrbiterCollision(i);
                            break;
                        }
                    }
                    for (j = 0; j < level.loopers.size(); ++j) {
                        if (contact.getFixtureB().getBody() == level.loopers.get(j).followerObject.body) {
                            level.loopers.get(j).destroy();
                            doOrbiterCollision(i);
                            break;
                        }
                    }
                    for (j = 0; j < level.oscillators.size(); ++j) {
                        if (contact.getFixtureB().getBody() == level.oscillators.get(j).followerObject.body) {
                            level.oscillators.get(j).destroy();
                            doOrbiterCollision(i);
                            break;
                        }
                    }
                }
            }
        }

        // Ball collision -> fixture B
        if (contact.getFixtureB().getBody() == level.ball.body) {
            // early out if we contact our orbiters
            for(j = 0; j < level.ball.orbiters.size(); ++j){
                if (contact.getFixtureA().getBody() == level.ball.orbiters.get(j).body) {
                    return;
                }
            }
            numberOfContacts++;
            if (contact.getFixtureA().getBody() == level.endCircle.body) {
                levelComplete();
            } else if (level.hasFollowerObject() && contact.getFixtureA().getBody() == level.getFollowerBody()) {
                fallOff();
            } else if (level.hasPacerObject() && contact.getFixtureA().getBody() == level.getPacerBody()) {
                fallOff();
            } else {
                for (j = 0; j < level.holes.size(); ++j) {
                    if (contact.getFixtureA().getBody() == level.holes.get(j).body) {
                        fallOff();
                        break;
                    }
                }

                for (j = 0; j < level.orbiterPickups.size(); ++j) {
                    if (contact.getFixtureA().getBody() == level.orbiterPickups.get(j).body) {
                        level.orbiterPickups.get(j).pickedUp();
                        addOrbiters();
                        break;
                    }
                }

                for (j = 0; j < level.goldPickups.size(); ++j) {
                    if (contact.getFixtureA().getBody() == level.goldPickups.get(j).body) {
                        level.goldPickups.get(j).pickedUp();
                        pickupGold();
                        break;
                    }
                }

                for (j = 0; j < level.followers.size(); ++j) {
                    if (contact.getFixtureA().getBody() == level.followers.get(j).followerObject.body) {
                        fallOff();
                        break;
                    }
                }

                for (j = 0; j < level.loopers.size(); ++j) {
                    if (contact.getFixtureA().getBody() == level.loopers.get(j).followerObject.body) {
                        fallOff();
                        break;
                    }
                }

                for (j = 0; j < level.oscillators.size(); ++j) {
                    if (contact.getFixtureA().getBody() == level.oscillators.get(j).followerObject.body) {
                        fallOff();
                        break;
                    }
                }
            }
        }
        // Ball collision -> fixture A
        else if (contact.getFixtureA().getBody() == level.ball.body) {
            // early out if we contact our orbiters
            for(j = 0; j < level.ball.orbiters.size(); ++j){
                if (contact.getFixtureB().getBody() == level.ball.orbiters.get(j).body) {
                    return;
                }
            }
            numberOfContacts++;
            if (contact.getFixtureB().getBody() == level.endCircle.body) {
                levelComplete();
            } else if (level.hasFollowerObject() && contact.getFixtureB().getBody() == level.getFollowerBody()) {
                fallOff();
            }  else if (level.hasPacerObject() && contact.getFixtureB().getBody() == level.getPacerBody()) {
                fallOff();
            } else {
                for (j = 0; j < level.holes.size(); ++j) {
                    if (contact.getFixtureB().getBody() == level.holes.get(j).body) {
                        fallOff();
                        break;
                    }
                }

                for (j = 0; j < level.orbiterPickups.size(); ++j) {
                    if (contact.getFixtureB().getBody() == level.orbiterPickups.get(j).body) {
                        level.orbiterPickups.get(j).pickedUp();
                        addOrbiters();
                        break;
                    }
                }

                for (j = 0; j < level.goldPickups.size(); ++j) {
                    if (contact.getFixtureB().getBody() == level.goldPickups.get(j).body) {
                        level.goldPickups.get(j).pickedUp();
                        pickupGold();
                        break;
                    }
                }

                for (j = 0; j < level.followers.size(); ++j) {
                    if (contact.getFixtureB().getBody() == level.followers.get(j).followerObject.body) {
                        fallOff();
                        break;
                    }
                }

                for (j = 0; j < level.loopers.size(); ++j) {
                    if (contact.getFixtureB().getBody() == level.loopers.get(j).followerObject.body) {
                        fallOff();
                        break;
                    }
                }

                for (j = 0; j < level.oscillators.size(); ++j) {
                    if (contact.getFixtureB().getBody() == level.oscillators.get(j).followerObject.body) {
                        fallOff();
                        break;
                    }
                }
            }
        }


    }

    private void doOrbiterCollision(int i) {
        level.ball.orbiters.get(i).destroy();
        if(i == 0) {
            Orbiter1Visible = false;
            if(level.ball.orbiters.get(1).visible){
                Orbiter2Visible = true;
            }
        }
        else if (i == 1) {
            Orbiter2Visible = false;
            if(level.ball.orbiters.get(0).visible){
                Orbiter1Visible = true;
            }
        }
    }

    private void levelComplete() {
        this.state = LevelState.LevelComplete;
        this.level.ball.body.setLinearVelocity(0,0);
        this.level.startCircle = this.level.startCircleGreenIcon;
        this.level.finishCircle = this.level.finishCircleGreenIcon;
        this.level.numberOfOrbitersFinishedWith = 0;
        for (int i = 0; i < this.level.ball.orbiters.size(); ++i) {
            if(this.level.ball.orbiters.get(i).visible) {
                this.level.numberOfOrbitersFinishedWith++;
            }
        }

        if(GamePreferences.instance.getCurrentLevel() == GamePreferences.instance.levelTimes.size()) {
            GamePreferences.instance.levelTimes.add(GamePreferences.instance.getCurrentLevel(), (int) (levelTime * 10));
        }
        GamePreferences.instance.save();
        GamePreferences.instance.submitData();
    }

    private void addOrbiters() {
        if(!Orbiter1Visible){
            Orbiter1Visible = true;
        }
        else if(!Orbiter2Visible) {
            Orbiter2Visible = true;
        }
        else {
            GamePreferences.instance.currentScore += Constants.EXTRA_ORBITER_WORTH;
            addScoreUpdate(Constants.EXTRA_ORBITER_WORTH);
        }
    }

    private void pickupGold() {
        if(this.state == LevelState.Gameplay) {
            GamePreferences.instance.currentScore += Constants.GOLD_WORTH;
            addScoreUpdate(Constants.GOLD_WORTH);
        }
    }

    private void fallOff() {
        if(Constants.DEBUG_BUILD)
        {
            return;
        }
        GamePreferences.instance.currentScore += Constants.SCORE_DECREMENT_FOR_COLLISION;
        if(GamePreferences.instance.currentScore < 0)
        {
            GamePreferences.instance.currentScore = 0;
        }
        int currentLevel = GamePreferences.instance.getCurrentLevel();

        if(currentLevel == GamePreferences.instance.levelTries.size() - 1) {
            int thisTry = GamePreferences.instance.levelTries.get(currentLevel);
            GamePreferences.instance.levelTries.set(currentLevel, thisTry + 1);
            GamePreferences.instance.save();
            GamePreferences.instance.submitData();
        }

        addScoreUpdate(Constants.SCORE_DECREMENT_FOR_COLLISION);

        this.state = LevelState.OffTheEdge;
        if (this.level.hasFollowerObject()) {
            this.level.tearDownFollower();
        }
        if (this.level.hasPacerObject()) {
            this.level.tearDownPacer();
        }

        this.level.startCircle = this.level.startCircleRedIcon;
        this.level.finishCircle = this.level.finishCircleRedIcon;
    }

    public void addScoreUpdate(int score) {
        // SCORE UPDATES
        ScoreUpdateObject scoreUpdateObject = scoreUpdateObjectPool.obtain();
        int y = (int)(DigitRenderer.instance.digitHeight / 2) +
                DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS;
        int x = (int) (Gdx.graphics.getWidth() - DigitRenderer.instance.digitWidth / 2 - DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS);
        scoreUpdateObject.init(score, x, y + 50, x, y);
        activeScoreUpdates.add(scoreUpdateObject);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public enum LevelState {
        Countdown,
        Gameplay,
        LevelComplete,
        OffTheEdge,
        GameOver,
        GameBeat
    }
}
