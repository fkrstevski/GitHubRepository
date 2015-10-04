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
import com.filip.edge.screens.MenuScreen;
import com.filip.edge.screens.ResultsScreen;
import com.filip.edge.screens.objects.AbstractCircleButtonObject;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;
import com.filip.edge.screens.objects.Hole;
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
    private DirectedGame game;
    private float readyTime;
    public float readyTimeRatio;
    private float endTime;
    private float greenTime;
    private float levelScore;

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
                    this.game.submitScore((long) GamePreferences.instance.currentScore);
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

    private void initLevel() {
        levelScore = 0;
        level = new Level();

        if (b2world != null) {
            b2world.setContactListener(null);
            b2world.dispose();
        }
        b2world = new World(new Vector2(0, 0), true);
        b2world.setContactListener(this);

        initPhysics();
    }

    private void initPhysics() {
        numberOfContacts = 0;

        float extraScale = 0;//level.ball.radius * 2;

        // Follower object body
        if (level.followerObject != null) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.KinematicBody;
            bodyDef.position.set(new Vector2(level.followerObject.position.x / Constants.BOX2D_SCALE, level.followerObject.position.y / Constants.BOX2D_SCALE));
            Body body = b2world.createBody(bodyDef);
            body.setActive(false);
            level.followerObject.body = body;
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius((level.followerObject.radius - extraScale) / Constants.BOX2D_SCALE);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            circleShape.dispose();
        }

        // Ball Physics Body
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

        // Hole Physics Body
        for (Hole hole : level.holes) {
            BodyDef holeBodyDef = new BodyDef();
            holeBodyDef.type = BodyType.StaticBody;
            holeBodyDef.position.set(new Vector2(hole.position.x / Constants.BOX2D_SCALE, hole.position.y / Constants.BOX2D_SCALE));
            Body holeBody = b2world.createBody(holeBodyDef);
            hole.body = holeBody;
            CircleShape circleHoleShape = new CircleShape();
            circleHoleShape.setRadius((hole.radius - extraScale) / Constants.BOX2D_SCALE);
            FixtureDef fixtureHoleDef = new FixtureDef();
            fixtureHoleDef.shape = circleHoleShape;
            fixtureHoleDef.isSensor = true;
            holeBody.createFixture(fixtureHoleDef);
            circleHoleShape.dispose();
        }

        // EndTarget Physics Body
        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyType.StaticBody;
        bodyDef2.position.set(new Vector2(level.endCircle.position.x / Constants.BOX2D_SCALE, level.endCircle.position.y / Constants.BOX2D_SCALE));
        Body body2 = b2world.createBody(bodyDef2);
        level.endCircle.body = body2;
        CircleShape circleShape2 = new CircleShape();
        circleShape2.setRadius((level.endCircle.radius - extraScale) / Constants.BOX2D_SCALE);
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = circleShape2;
        fixtureDef2.isSensor = true;
        body2.createFixture(fixtureDef2);
        circleShape2.dispose();

        // Middle Circles Physics Bodies
        for (AbstractCircleButtonObject c : level.circleShapes) {
            BodyDef bodyDef1 = new BodyDef();
            bodyDef1.type = BodyType.StaticBody;
            bodyDef1.position.set(new Vector2(c.position.x / Constants.BOX2D_SCALE, c.position.y / Constants.BOX2D_SCALE));
            Body body1 = b2world.createBody(bodyDef1);
            c.body = body1;
            CircleShape circleShape1 = new CircleShape();
            circleShape1.setRadius((c.radius - extraScale) / Constants.BOX2D_SCALE);
            FixtureDef fixtureDef1 = new FixtureDef();
            fixtureDef1.shape = circleShape1;
            fixtureDef1.isSensor = true;
            body1.createFixture(fixtureDef1);
            circleShape1.dispose();
        }

        // Rectangles Physics Bodies
        for (AbstractRectangleButtonObject c : level.rectangleShapes) {
            BodyDef bodyDef1 = new BodyDef();
            bodyDef1.type = BodyType.StaticBody;
            bodyDef1.position.set(new Vector2(c.position.x / Constants.BOX2D_SCALE, c.position.y / Constants.BOX2D_SCALE));
            Body body1 = b2world.createBody(bodyDef1);
            c.body = body1;
            PolygonShape polygonShape = new PolygonShape();
            Vector2 o = new Vector2(0, 0);
            polygonShape.setAsBox((c.dimension.x / 2) / Constants.BOX2D_SCALE, (c.dimension.y / 2 - extraScale) / Constants.BOX2D_SCALE, o, MathUtils.degreesToRadians * c.rotation);
            FixtureDef fixtureDef1 = new FixtureDef();
            fixtureDef1.shape = polygonShape;
            fixtureDef1.isSensor = true;
            body1.createFixture(fixtureDef1);
            polygonShape.dispose();
        }
    }

    public void update(float deltaTime) {

        cameraHelper.update(deltaTime);

        handleDebugInput(deltaTime);

        if (state == LevelState.Countdown) {
            this.readyTime += deltaTime;
            this.readyTimeRatio = this.readyTime / READY_TIME;

            if (this.level.startCircle != null && !this.level.startCircle.equals(this.level.startCircleRedIcon) && this.readyTimeRatio >= 0.0f && this.readyTimeRatio < 0.5f) {
                this.game.showAds(false);
                this.level.startCircle = this.level.startCircleRedIcon;
                this.level.finishCircle = this.level.finishCircleRedIcon;
                AudioManager.instance.play(Assets.instance.sounds.tickSound);
            } else if (this.level.startCircle != null && !this.level.startCircle.equals(this.level.startCircleYellowIcon) && this.readyTimeRatio > 0.5f && this.readyTimeRatio < 1f) {
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

                this.game.showAds(true);
            }

        } else if (state == LevelState.LevelComplete) {
            this.endTime += deltaTime;

            this.level.ball.position.lerp(this.level.getLastPoint(), endTime / Constants.END_TIME);
            if(level.followerObject != null) {
                level.followerObject.scale.set(level.followerObject.scale.x * (1 - endTime / Constants.END_TIME), level.followerObject.scale.y * (1 - endTime / Constants.END_TIME));
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
                GamePreferences.instance.currentScore -= 10000;
                if (GamePreferences.instance.currentScore <= 0) {
                    GamePreferences.instance.currentScore = 0;
                    state = LevelState.GameOver;
                    AudioManager.instance.play(Assets.instance.sounds.liveLost);
                } else {
                    this.state = LevelState.Countdown;
                    this.level.startCircle = this.level.startCircleRedIcon;
                    this.level.finishCircle = this.level.finishCircleRedIcon;
                    AudioManager.instance.play(Assets.instance.sounds.tickSound);
                    this.initLevel();
                }
            }
        } else if (state == LevelState.Gameplay) {
            // Update the score
            // need to do something fishy since current score is a long and subtracting
            // a small float might not update the actual value
            levelScore += deltaTime * 5;
            if (levelScore > 1) {
                GamePreferences.instance.currentScore -= (int) levelScore;
                levelScore = levelScore % 1;
            }

            if (GamePreferences.instance.currentScore <= 0) {
                GamePreferences.instance.currentScore = 0;
                state = LevelState.GameOver;
                AudioManager.instance.play(Assets.instance.sounds.liveLost);
            } else {
                level.update(deltaTime);
                handleInputGame(deltaTime);
                b2world.step(deltaTime, 8, 3);

                this.greenTime += deltaTime;
                if (this.greenTime > GREEN_ICON_TIME) {
                    this.greenTime = 0;
                    this.level.finishCircle = null;
                    this.level.startCircle = null;
                }

                if (this.numberOfContacts == 0 || this.b2world.getContactCount() == 0) {
                    fallOff();
                }
            }
        }
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
            float x = Gdx.input.getAccelerometerX();
            float y = Gdx.input.getAccelerometerY();
            // TODO: make movement be the same on different screen devices;
            moveBall(y * 700 * deltaTime, x * 700 * deltaTime);

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
            this.state = LevelState.LevelComplete;
            this.level.startCircle = this.level.startCircleGreenIcon;
            this.level.finishCircle = this.level.finishCircleGreenIcon;
        }
        // Back to Menu
        else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            backToMenu();
        } else if (keycode == Keys.SPACE) {
            //
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
            this.state = LevelState.LevelComplete;
            this.level.startCircle = this.level.startCircleGreenIcon;
            this.level.finishCircle = this.level.finishCircleGreenIcon;
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
        numberOfContacts--;
    }

    @Override
    public void beginContact(Contact contact) {
        numberOfContacts++;

        if (contact.getFixtureB().getBody() == level.ball.body) {
            if (contact.getFixtureA().getBody() == level.endCircle.body) {
                this.state = LevelState.LevelComplete;
                this.level.startCircle = this.level.startCircleGreenIcon;
                this.level.finishCircle = this.level.finishCircleGreenIcon;
            } else if (level.followerObject != null && contact.getFixtureA().getBody() == level.followerObject.body) {
                Gdx.app.log(TAG, "BALL FOLLOWER COLLISION");
                fallOff();
            } else {
                for (Hole hole : level.holes) {
                    if (contact.getFixtureA().getBody() == hole.body) {
                        Gdx.app.log(TAG, "BALL HOLE COLLISION");
                        fallOff();
                    }
                }
            }
        } else {
            if (contact.getFixtureB().getBody() == level.endCircle.body) {
                this.state = LevelState.LevelComplete;
                this.level.startCircle = this.level.startCircleGreenIcon;
                this.level.finishCircle = this.level.finishCircleGreenIcon;
            } else if (level.followerObject != null && contact.getFixtureB().getBody() == level.followerObject.body) {
                Gdx.app.log(TAG, "BALL FOLLOWER COLLISION");
                fallOff();
            } else {
                for (Hole hole : level.holes) {
                    if (contact.getFixtureB().getBody() == hole.body) {
                        Gdx.app.log(TAG, "BALL HOLE COLLISION");
                        fallOff();
                    }
                }
            }
        }
    }

    private void fallOff() {
        this.state = LevelState.OffTheEdge;
        if(this.level.followerObject != null) {
            this.level.followerObjectTime = 0;
            this.level.followerObjectState = Level.PropertyState.Teardown;
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
