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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.Iterator;

import come.filip.templategame.screens.DirectedGame;
import come.filip.templategame.screens.MenuScreen;
import come.filip.templategame.screens.objects.AbstractCircleButtonObject;
import come.filip.templategame.screens.objects.AbstractRectangleButtonObject;
import come.filip.templategame.screens.transitions.ScreenTransition;
import come.filip.templategame.screens.transitions.ScreenTransitionSlide;
import come.filip.templategame.util.AudioManager;
import come.filip.templategame.util.CameraHelper;
import come.filip.templategame.util.Constants;

class MyBodyData
{
    public boolean isFlaggedForDelete;

    MyBodyData()
    {
        isFlaggedForDelete = false;
    }
}

public class WorldController extends InputAdapter implements Disposable, ContactListener
{

    private static final String TAG = WorldController.class.getName();
    public LevelState state = LevelState.Ready;
    public Level level;
    public boolean renderPhysics;
    public int numberOfContacts;
    public CameraHelper cameraHelper;
    public World b2world;
    private DirectedGame game;
    private boolean gameOver;

    private int currentLevel;
    private int currentZone;
    private int currentStage;

    public WorldController(DirectedGame game)
    {
        Box2D.init();
        this.game = game;
        this.renderPhysics = false;
        this.currentLevel = 0;
        this.currentStage = 0;
        this.currentZone = 0;
        init();
    }

    private void init()
    {
        cameraHelper = new CameraHelper();
        initLevel();
    }

    public void nextLevel()
    {
        this.currentLevel++;
        if (this.currentLevel > Constants.MAX_LEVELS - 1)
        {
            this.currentLevel = 0;
            this.currentStage++;
            if (this.currentStage > StageLoader.getZone(this.currentZone).getNumberOfStages() - 1)
            {
                this.currentStage = 0;
                this.currentZone++;
                if (this.currentZone > StageLoader.getNumberOfZones() - 1)
                {
                    this.currentZone = 0;
                }
            }
        }
        Gdx.app.debug(TAG, "Zone = " + this.currentZone + " Stage = " + this.currentStage + " Level = " + this.currentLevel);
        init();
    }

    private void initLevel()
    {
        gameOver = false;
        level = new Level(this.currentZone, this.currentStage, this.currentLevel);

        if (b2world != null)
        {
            b2world.setContactListener(null);
            b2world.dispose();
        }
        b2world = new World(new Vector2(0, 0), true);
        b2world.setContactListener(this);

        initPhysics();
    }

    private void initPhysics()
    {
        numberOfContacts = 0;

        if (b2world != null)
        {
            Array<Body> bodies = new Array<Body>();
            b2world.getBodies(bodies);
            for (int i = 0; i < b2world.getBodyCount(); ++i)
            {
                ((MyBodyData) bodies.get(i).getUserData()).isFlaggedForDelete = true;
            }
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(level.ball.position.x / Constants.BOX2D_SCALE, level.ball.position.y / Constants.BOX2D_SCALE));
        Body body = b2world.createBody(bodyDef);
        body.setUserData(new MyBodyData());
        level.ball.body = body;
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(level.ball.radius / Constants.BOX2D_SCALE);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        circleShape.dispose();

        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyType.StaticBody;
        bodyDef2.position.set(new Vector2(level.endCircle.position.x / Constants.BOX2D_SCALE, level.endCircle.position.y / Constants.BOX2D_SCALE));
        Body body2 = b2world.createBody(bodyDef2);
        body2.setUserData(new MyBodyData());
        level.endCircle.body = body2;
        CircleShape circleShape2 = new CircleShape();
        circleShape2.setRadius(level.endCircle.radius / Constants.BOX2D_SCALE);
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = circleShape2;
        fixtureDef2.isSensor = true;
        body2.createFixture(fixtureDef2);
        circleShape2.dispose();


        for (AbstractCircleButtonObject c : level.circleShapes)
        {
            BodyDef bodyDef1 = new BodyDef();
            bodyDef1.type = BodyType.StaticBody;
            bodyDef1.position.set(new Vector2(c.position.x / Constants.BOX2D_SCALE, c.position.y / Constants.BOX2D_SCALE));
            Body body1 = b2world.createBody(bodyDef1);
            body1.setUserData(new MyBodyData());
            c.body = body1;
            CircleShape circleShape1 = new CircleShape();
            circleShape1.setRadius(c.radius / Constants.BOX2D_SCALE);
            FixtureDef fixtureDef1 = new FixtureDef();
            fixtureDef1.shape = circleShape1;
            fixtureDef1.isSensor = true;
            body1.createFixture(fixtureDef1);
            circleShape1.dispose();
        }

        for (AbstractRectangleButtonObject c : level.rectangleShapes)
        {
            BodyDef bodyDef1 = new BodyDef();
            bodyDef1.type = BodyType.StaticBody;
            bodyDef1.position.set(new Vector2(c.position.x / Constants.BOX2D_SCALE, c.position.y / Constants.BOX2D_SCALE));
            Body body1 = b2world.createBody(bodyDef1);
            body1.setUserData(new MyBodyData());
            c.body = body1;
            PolygonShape polygonShape = new PolygonShape();
            Vector2 o = new Vector2(0, 0);
            polygonShape.setAsBox(c.dimension.x / 2 / Constants.BOX2D_SCALE, c.dimension.y / 2 / Constants.BOX2D_SCALE, o, MathUtils.degreesToRadians * c.rotation);
            FixtureDef fixtureDef1 = new FixtureDef();
            fixtureDef1.shape = polygonShape;
            fixtureDef1.isSensor = true;
            body1.createFixture(fixtureDef1);
            polygonShape.dispose();
        }
    }

    public void update(float deltaTime)
    {
        handleDebugInput(deltaTime);
        handleInputGame(deltaTime);

        level.update(deltaTime);

        b2world.step(deltaTime, 8, 3);

        Array<Body> bodies = new Array<Body>();
        b2world.getBodies(bodies);
        Iterator<Body> i = bodies.iterator();
        Body node = i.next();
        while (i.hasNext())
        {
            Body oBj = node;
            node = i.next();
            MyBodyData data = (MyBodyData) oBj.getUserData();
            if (data != null && data.isFlaggedForDelete)
            {
                b2world.destroyBody(oBj);
            }
        }

        cameraHelper.update(deltaTime);
        if (!isGameOver())
        {
            AudioManager.instance.play(Assets.instance.sounds.liveLost);
            gameOver = true;
        }
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public Color getBgColor()
    {
        return (this.numberOfContacts > 0 ? Constants.BLUE : Constants.RED);
    }

    private void handleDebugInput(float deltaTime)
    {
        if (Gdx.app.getType() != ApplicationType.Desktop)
        {
            return;
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
        {
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        }
        if (Gdx.input.isKeyPressed(Keys.COMMA))
        {
            cameraHelper.addZoom(camZoomSpeed);
        }
        if (Gdx.input.isKeyPressed(Keys.PERIOD))
        {
            cameraHelper.addZoom(-camZoomSpeed);
        }
        if (Gdx.input.isKeyPressed(Keys.SLASH))
        {
            cameraHelper.setZoom(1);
        }
    }

    private void handleInputGame(float deltaTime)
    {
        if (Gdx.app.getType() == ApplicationType.Desktop)
        {
            // Camera Controls (move)
            float ballMoveSpeed = 1000 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
            {
                ballMoveSpeed *= camMoveSpeedAccelerationFactor;
            }
            if (Gdx.input.isKeyPressed(Keys.LEFT))
            {
                moveBall(-ballMoveSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT))
            {
                moveBall(ballMoveSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Keys.UP))
            {
                moveBall(0, -ballMoveSpeed);
            }
            if (Gdx.input.isKeyPressed(Keys.DOWN))
            {
                moveBall(0, ballMoveSpeed);
            }
        }
        else
        {
            float x = Gdx.input.getAccelerometerX();
            float y = Gdx.input.getAccelerometerY();
            moveBall(y * 700 * deltaTime, x * 700 * deltaTime);

        }
    }

    private void moveBall(float x, float y)
    {
        level.ball.body.setLinearVelocity(x, y);
    }

    @Override
    public boolean keyUp(int keycode)
    {
        // Reset game world
        if (keycode == Keys.R)
        {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        // Toggle camera follow
        else if (keycode == Keys.ENTER)
        {
            this.nextLevel();
        }
        // Back to Menu
        else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
        {
            backToMenu();
        }
        else if (keycode == Keys.SPACE)
        {
            //
            this.renderPhysics = !this.renderPhysics;
        }

        return false;
    }

    private void backToMenu()
    {
        // switch to menu screen
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
        game.setScreen(new MenuScreen(game), transition);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {

        Gdx.app.debug(TAG, "Touch at screenX = " + screenX + " screenY = " + screenY);

        if (level.backButton.isTouched(screenX, screenY))
        {
            backToMenu();
        }
        else
        {
            //level.next();
            this.renderPhysics = !this.renderPhysics;
        }
        return false;
    }

    @Override
    public void dispose()
    {
        if (b2world != null)
        {
            b2world.dispose();
        }
    }

    @Override
    public void endContact(Contact contact)
    {

        numberOfContacts--;
        Gdx.app.debug(TAG, "endContact: " + numberOfContacts);
    }

    @Override
    public void beginContact(Contact contact)
    {
        numberOfContacts++;

        if (contact.getFixtureB().getBody() == level.ball.body)
        {
            if (contact.getFixtureA().getBody() == level.endCircle.body)
            {
                Gdx.app.debug(TAG, "beginContact: B-> Ball A-> Last");
                this.nextLevel();
            }
            else
            {
                Gdx.app.debug(TAG, "beginContact: B-> Ball " + contact.getFixtureA().toString());
            }
        }
        else
        {
            if (contact.getFixtureB().getBody() == level.endCircle.body)
            {
                Gdx.app.debug(TAG, "beginContact: A-> Ball B-> Last");
                this.nextLevel();
            }
            else
            {
                Gdx.app.debug(TAG, "beginContact: A-> Ball" + contact.getFixtureB().toString());
            }
        }

        Gdx.app.debug(TAG, "beginContact: " + numberOfContacts);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {

    }

    enum LevelState
    {
        Ready,
        Play,
        End
    }
}
