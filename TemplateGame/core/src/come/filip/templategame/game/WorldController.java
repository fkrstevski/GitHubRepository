/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package come.filip.templategame.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import come.filip.templategame.screens.DirectedGame;
import come.filip.templategame.screens.MenuScreen;
import come.filip.templategame.screens.transitions.ScreenTransition;
import come.filip.templategame.screens.transitions.ScreenTransitionSlide;
import come.filip.templategame.util.AudioManager;
import come.filip.templategame.util.CameraHelper;
import come.filip.templategame.util.Constants;

public class WorldController extends InputAdapter implements Disposable {

    private static final String TAG = WorldController.class.getName();

    private DirectedGame game;
    public Level level;
    private boolean goalReached;
    private boolean accelerometerAvailable;
    private boolean gameOver;

    public CameraHelper cameraHelper;

    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    private float timeLeftGameOverDelay;

    public World b2world;

    public WorldController (DirectedGame game) {
        this.game = game;
        init();
    }

    private void init () {
        accelerometerAvailable = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
        cameraHelper = new CameraHelper();
        timeLeftGameOverDelay = 0;
        initLevel();
    }

    private void initLevel () {
        goalReached = false;
        gameOver = false;
        level = new Level(0, 0, 0);
        //cameraHelper.setTarget(level.ball);
        initPhysics();
    }

    private void initPhysics () {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, -9.81f), true);

        // TODO: add physycs for all circles and rectangles in level
        // Rocks
		/*Vector2 origin = new Vector2();
		for (Rock rock : level.rocks) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(rock.position);
			Body body = b2world.createBody(bodyDef);
			rock.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = rock.bounds.width / 2.0f;
			origin.y = rock.bounds.height / 2.0f;
			polygonShape.setAsBox(rock.bounds.width / 2.0f, rock.bounds.height / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}*/
    }

    public void update (float deltaTime) {
        handleDebugInput(deltaTime);

            handleInputGame(deltaTime);
        level.update(deltaTime);

        b2world.step(deltaTime, 8, 3);
        cameraHelper.update(deltaTime);
        if (!isGameOver()) {
            AudioManager.instance.play(Assets.instance.sounds.liveLost);
            gameOver = true;
            //if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            //else
            //    initLevel();
        }
        //level.mountains.updateScrollPosition(cameraHelper.getPosition());
     }

    public boolean isGameOver () {
        return gameOver;
    }

    public Color getBgColor()
    {
        return (level.collision ? Constants.RED : Constants.BLUE);
    }


    private void onCollisionBallWithGoal() {
        goalReached = true;
        timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
}

    private void handleDebugInput (float deltaTime) {
        if (Gdx.app.getType() != ApplicationType.Desktop) return;

        if (!cameraHelper.hasTarget(level.ball)) {
            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
    }

    private void handleInputGame (float deltaTime) {
        if (cameraHelper.hasTarget(level.ball)) {
            // Player Movement
            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                level.ball.velocity.x = -level.ball.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                level.ball.velocity.x = level.ball.terminalVelocity.x;
            } else {
                // Use accelerometer for movement if available
                if (accelerometerAvailable) {
                    // normalize accelerometer values from [-10, 10] to [-1, 1]
                    // which translate to rotations of [-90, 90] degrees
                    float amount = Gdx.input.getAccelerometerY() / 10.0f;
                    amount *= 90.0f;
                    // is angle of rotation inside dead zone?
                    if (Math.abs(amount) < Constants.ACCEL_ANGLE_DEAD_ZONE) {
                        amount = 0;
                    } else {
                        // use the defined max angle of rotation instead of
                        // the full 90 degrees for maximum velocity
                        amount /= Constants.ACCEL_MAX_ANGLE_MAX_MOVEMENT;
                    }
                    level.ball.velocity.x = level.ball.terminalVelocity.x * amount;
                }
                // Execute auto-forward movement on non-desktop platform
                else if (Gdx.app.getType() != ApplicationType.Desktop) {
                    level.ball.velocity.x = level.ball.terminalVelocity.x;
                }
            }
        }
    }

    private void moveCamera (float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    @Override
    public boolean keyUp (int keycode) {
        // Reset game world
        if (keycode == Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        // Toggle camera follow
        else if (keycode == Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.ball);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        // Back to Menu
        else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            backToMenu();
        }
        else if (keycode == Keys.SPACE) {
            level.next();
        }

        return false;
    }

    private void backToMenu () {
        // switch to menu screen
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
        game.setScreen(new MenuScreen(game), transition);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {

        Gdx.app.debug(TAG, "Touch at screenX = " + screenX + " screenY = " + screenY);

        if(level.backButton.isTouched(screenX, screenY))
        {
            backToMenu();
        }
        else{
            level.next();
        }
        return false;
    }

    @Override
    public void dispose () {
        if (b2world != null) b2world.dispose();
    }

}
