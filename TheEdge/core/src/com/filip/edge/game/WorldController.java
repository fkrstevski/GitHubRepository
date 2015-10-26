package com.filip.edge.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.filip.edge.screens.DirectedGame;
import com.filip.edge.screens.GameOverScreen;
import com.filip.edge.screens.MenuScreen;
import com.filip.edge.screens.ResultsScreen;
import com.filip.edge.screens.objects.AbstractCircleButtonObject;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;
import com.filip.edge.screens.objects.Follower;
import com.filip.edge.screens.objects.Hole;
import com.filip.edge.screens.objects.Orbiter;
import com.filip.edge.screens.objects.OrbiterPickup;
import com.filip.edge.util.AudioManager;
import com.filip.edge.util.CameraHelper;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

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

    private static final int MAX_NUMBER_ORBITERS = 2;
    private boolean[] newlyVisibleOrbiters = new boolean[MAX_NUMBER_ORBITERS];
    private int visibleOrbiters;

    public WorldController(DirectedGame game) {
        Box2D.init();
        this.game = game;
        this.renderPhysics = false;
        this.readyTime = 0;
        this.endTime = 0;
        this.greenTime = 0;
        this.readyTimeRatio = this.readyTime / READY_TIME;
        this.state = LevelState.Countdown;
        init();

        this.level.startCircle = this.level.startCircleRedIcon;
        this.level.finishCircle = this.level.finishCircleRedIcon;
        AudioManager.instance.play(Assets.instance.sounds.tickSound);
    }

    private void init() {
        cameraHelper = new CameraHelper();
        initLevel();
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
                    GamePreferences.instance.zone = 0;
                    GamePreferences.instance.scoreNeedsToBeSubmitted = true;
                    // Make sure we save the highest score ASAP
                    GamePreferences.instance.save();
                    this.game.submitScore(GamePreferences.instance.currentScore);
                    game.setScreen(new ResultsScreen(game));

                    // Early out
                    return;
                }
            }
        }

        // Save the scores
        GamePreferences.instance.save();

        Gdx.app.debug(TAG, "Zone = " + GamePreferences.instance.zone + " Stage = " + GamePreferences.instance.stage + " Level = " + GamePreferences.instance.level);
        initLevel();
    }

    private void resetLevel() {
        levelScore = 0;
        visibleOrbiters = 0;
        level.reset();
    }

    private void initLevel() {
        //this.game.startMethodTracing("initLevel");

        levelScore = 0;
        visibleOrbiters = 0;
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
                circleShape.setRadius((level.ball.orbiters.get(i).radius) / Constants.BOX2D_SCALE);
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
                circleShape1.setRadius((level.circleShapes.get(i).radius) / Constants.BOX2D_SCALE);
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
            }
        }
    }

    public void update(float deltaTime) {

        cameraHelper.update(deltaTime);

        handleDebugInput(deltaTime);

        if (state == LevelState.Countdown) {
            this.readyTime += deltaTime;
            this.readyTimeRatio = this.readyTime / READY_TIME;

            // step the physics so that the world updates before the game starts
            // fixes an issue when we reset a level
            b2world.step(deltaTime, 8, 3);

            if (this.level.startCircle != null && this.level.startCircle != this.level.startCircleRedIcon && this.readyTimeRatio >= 0.0f && this.readyTimeRatio < 0.5f) {
                this.game.showAds(false);
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

                // RESET STATE FOR DISAPPEARING
                if(level.disappearing) {
                    level.disappearingState = Level.PropertyState.Inactive;
                }

                // RESET STATE FOR SCALING
                for (int i = 0; i < level.rectangleShapes.size(); ++i) {
                    if(level.rectangleShapes.get(i).disapears) {
                        level.rectangleShapes.get(i).start();
                    }
                }

                this.game.showAds(true);
            }

        } else if (state == LevelState.LevelComplete) {
            this.endTime += deltaTime;

            // Move the ball and orbiters into the end point
            this.level.ball.position.lerp(this.level.getLastPoint(), endTime / Constants.END_TIME);
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

            for (int i = 0; i < level.oscillators.size(); ++i) {
                level.oscillators.get(i).scale((1 - endTime / Constants.END_TIME));
            }

            if(this.endTime / Constants.END_TIME > 0.5f && level.numberOfOrbitersFinishedWith > 0) {
                GamePreferences.instance.currentScore += Constants.SCORE_INCREMENT_FOR_SAVED_ORBITER * level.numberOfOrbitersFinishedWith;
                level.numberOfOrbitersFinishedWith = 0;
                AudioManager.instance.play(Assets.instance.sounds.tickSound);
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
                GamePreferences.instance.currentScore -= Constants.SCORE_DECREMENT_FOR_COLLISION;
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
            // Update the score
            // need to do something fishy since current score is a long and subtracting
            // a small float might not update the actual value
            levelScore += deltaTime * 100 ;
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
                for (int i = 0; i < MAX_NUMBER_ORBITERS; ++i) {
                    if (newlyVisibleOrbiters[i]) {
                        newlyVisibleOrbiters[i] = false;
                        this.level.ball.orbiters.get(i).visible = true;
                        this.level.ball.orbiters.get(i).body.setActive(true);
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
            float ballMoveSpeed = (10000 / Constants.BOX2D_SCALE * Gdx.graphics.getWidth() / Constants.BASE_SCREEN_WIDTH) * deltaTime;
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

            moveBall(y * Constants.BALL_SPEED[GamePreferences.instance.zone] * horizontalScale * deltaTime,
                     x * Constants.BALL_SPEED[GamePreferences.instance.zone] * verticalScale * deltaTime);

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
        game.setScreen(new MenuScreen(game));
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
            numberOfContacts--;
        }
        else if (contact.getFixtureA().getBody() == level.ball.body) {
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
                    level.ball.orbiters.get(i).destroy();
                }
                else if (level.hasPacerObject() && contact.getFixtureA().getBody() == level.getPacerBody()) {
                    level.levelPacer.destroy();
                    level.ball.orbiters.get(i).destroy();
                }

                else {
                    for (j = 0; j < level.followers.size(); ++j) {
                        if (contact.getFixtureA().getBody() == level.followers.get(j).followerObject.body) {
                            level.followers.get(j).destroy();
                            level.ball.orbiters.get(i).destroy();
                            break;
                        }
                    }

                    for (j = 0; j < level.oscillators.size(); ++j) {
                        if (contact.getFixtureA().getBody() == level.oscillators.get(j).followerObject.body) {
                            level.oscillators.get(j).destroy();
                            level.ball.orbiters.get(i).destroy();
                            break;
                        }
                    }
                }
            }
            if (contact.getFixtureA().getBody() == level.ball.orbiters.get(i).body) {
                if (level.hasFollowerObject() && contact.getFixtureB().getBody() == level.getFollowerBody()) {
                    level.levelFollower.destroy();
                    level.ball.orbiters.get(i).destroy();
                }
                else if (level.hasPacerObject() && contact.getFixtureB().getBody() == level.getPacerBody()) {
                    level.levelPacer.destroy();
                    level.ball.orbiters.get(i).destroy();
                }

                else {
                    for (j = 0; j < level.followers.size(); ++j) {
                        if (contact.getFixtureB().getBody() == level.followers.get(j).followerObject.body) {
                            level.followers.get(j).destroy();
                            level.ball.orbiters.get(i).destroy();
                            break;
                        }
                    }

                    for (j = 0; j < level.oscillators.size(); ++j) {
                        if (contact.getFixtureB().getBody() == level.oscillators.get(j).followerObject.body) {
                            level.oscillators.get(j).destroy();
                            level.ball.orbiters.get(i).destroy();
                            break;
                        }
                    }
                }
            }

        }

        // Ball collision -> fixture B
        if (contact.getFixtureB().getBody() == level.ball.body) {
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

                for (j = 0; j < level.followers.size(); ++j) {
                    if (contact.getFixtureA().getBody() == level.followers.get(j).followerObject.body) {
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

                for (j = 0; j < level.followers.size(); ++j) {
                    if (contact.getFixtureB().getBody() == level.followers.get(j).followerObject.body) {
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

    private void levelComplete() {
        this.state = LevelState.LevelComplete;
        this.level.startCircle = this.level.startCircleGreenIcon;
        this.level.finishCircle = this.level.finishCircleGreenIcon;
        this.level.numberOfOrbitersFinishedWith = 0;
        for (int i = 0; i < this.level.ball.orbiters.size(); ++i) {
            if(this.level.ball.orbiters.get(i).body.isActive()) {
                this.level.numberOfOrbitersFinishedWith++;
            }
        }
    }

    private void addOrbiters() {
        if(visibleOrbiters > MAX_NUMBER_ORBITERS - 1) {
            Gdx.app.error(TAG, "CANNOT ADD ANY MORE ORBITERS");
        }
        else {
            newlyVisibleOrbiters[this.visibleOrbiters] = true;
            this.visibleOrbiters++;
        }
    }

    private void fallOff() {
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
