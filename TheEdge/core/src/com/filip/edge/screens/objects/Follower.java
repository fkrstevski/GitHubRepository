package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.filip.edge.game.Level.PropertyState;
import com.filip.edge.game.objects.EmptyCircle;
import com.filip.edge.util.Constants;

import java.util.ArrayList;


/**
 * Created by fkrstevski on 2015-10-04.
 */
public class Follower {
    public static final String TAG = Follower.class.getName();
    private static final float FOLLOW_OBJECT_SCALE_TIME = 0.5f;
    public EmptyCircle followerObject;
    public float followerObjectTime;
    public PropertyState followerObjectState;
    public ArrayList<Vector2> pointsToFollow;
    private float followerObjectDisplayStartTime;
    private Vector2 followerObjectSpeed;
    private Vector2 followObjectFrom;
    private Vector2 followObjectTo;
    private Vector2 vectorDirection;
    private int followPointIndex;
    private float followObjectOriginalSize;
    private int direction;
    private boolean backAndForth;
    private boolean oneTimeOnly;
    private Vector2 startingPosition;
    private int startingDirection;
    private Type type;

    public boolean deactivate;

    public Follower(Type t, Color color, float startUpTime, Vector2 speed, float size, Vector2 pos, ArrayList<Vector2> pointsToFollow, int dir, boolean backAndForth, boolean oneTimeOnly, boolean shared, String region) {

        this.type = t;
        this.followerObjectTime = 0;
        this.followerObjectDisplayStartTime = startUpTime;
        this.followerObjectState = PropertyState.Inactive;
        this.followPointIndex = 0;
        this.followerObjectSpeed = speed;
        this.followObjectOriginalSize = size;
        this.pointsToFollow = pointsToFollow;
        this.direction = dir;
        this.startingDirection = dir;
        this.backAndForth = backAndForth;
        this.oneTimeOnly = oneTimeOnly;
        this.startingPosition = pos;

        this.vectorDirection = new Vector2();

        this.followerObject = new EmptyCircle(this.followObjectOriginalSize,
                pos.x,
                pos.y,
                color,
                color, shared, region);
    }

    public void update(float deltaTime) {
        this.followerObjectTime += deltaTime;
        switch (this.followerObjectState) {
            case Inactive:

                if (this.followerObjectTime > this.followerObjectDisplayStartTime) {
                    this.followerObject.body.setActive(false);
                    this.followerObjectTime = 0;
                    this.followObjectFrom = pointsToFollow.get(this.followPointIndex);
                    this.followObjectTo = pointsToFollow.get(this.followPointIndex + 1);
                    this.vectorDirection.set(this.followObjectTo.x - this.followObjectFrom.x, this.followObjectTo.y - this.followObjectFrom.y);
                    vectorDirection.nor();
                    this.followerObject.scale.set(0, 0);
                    this.followerObject.body.getFixtureList().get(0).getShape().setRadius(0);

                    this.followerObjectState = PropertyState.Buildup;
                }
                break;
            case Buildup:
                if (followerObjectTime > FOLLOW_OBJECT_SCALE_TIME) {
                    this.followerObjectState = PropertyState.Active;
                }

                this.followerObject.scale.set(this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME,
                        this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME);
                this.followerObject.body.getFixtureList().get(0).getShape().setRadius(((this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME) * this.followObjectOriginalSize / 2.0f) / Constants.BOX2D_SCALE);
                if (followerObjectTime / FOLLOW_OBJECT_SCALE_TIME > Constants.COLLISION_ACTIVATION_PERCENT) {
                    this.followerObject.body.setActive(true);
                }
                break;
            case Active:
                float xDirection = vectorDirection.x / ((vectorDirection.x + vectorDirection.y));
                float yDirection = vectorDirection.y / ((vectorDirection.x + vectorDirection.y));
                float finalVelocity = xDirection * this.followerObjectSpeed.x + yDirection * this.followerObjectSpeed.y;
                this.followerObject.body.setLinearVelocity(vectorDirection.x * finalVelocity, vectorDirection.y * finalVelocity);
                if (this.followerObject.position.epsilonEquals(this.followObjectTo, this.followerObjectSpeed.len() / 7f)) {
                    this.followerObject.body.setLinearVelocity(0, 0);
                    this.followerObject.position.set(this.followObjectTo.x, this.followObjectTo.y);
                    this.followerObject.body.setTransform(this.followObjectTo.x / Constants.BOX2D_SCALE, this.followObjectTo.y / Constants.BOX2D_SCALE, 0);
                    this.followPointIndex += this.direction;

                    if (this.backAndForth) {
                        if (this.followPointIndex == 0 || this.followPointIndex == pointsToFollow.size() - 1) {
                            this.direction *= -1;
                        }
                        if (followPointIndex < 0) {
                            this.followPointIndex = 0;
                            this.direction *= -1;
                        }
                        this.followObjectFrom = pointsToFollow.get(this.followPointIndex);
                        this.followObjectTo = pointsToFollow.get(this.followPointIndex + this.direction);
                        this.vectorDirection.set(this.followObjectTo.x - this.followObjectFrom.x, this.followObjectTo.y - this.followObjectFrom.y);
                        vectorDirection.nor();
                    } else {
                        if (followPointIndex >= pointsToFollow.size() - 1 || followPointIndex < 0) {
                            this.followPointIndex = 0;
                            this.followerObjectTime = 0;
                            this.followObjectFrom = pointsToFollow.get(this.followPointIndex);
                            this.followObjectTo = pointsToFollow.get(this.followPointIndex + 1);
                            this.vectorDirection.set(this.followObjectTo.x - this.followObjectFrom.x, this.followObjectTo.y - this.followObjectFrom.y);
                            vectorDirection.nor();
                            this.followerObject.body.setLinearVelocity(0, 0);

                            if (this.type == Type.Looper) {
                                // if the first and last point are the same loop them
                                if (pointsToFollow.get(0).equals(pointsToFollow.get(pointsToFollow.size() - 1))) {
                                    this.followerObjectState = PropertyState.Active;
                                }
                                // otherwise just restart the looper
                                else {
                                    this.followerObjectState = PropertyState.Teardown;
                                }
                            } else {
                                this.followerObjectState = PropertyState.Teardown;
                            }

                        } else {
                            this.followObjectFrom = pointsToFollow.get(this.followPointIndex);
                            this.followObjectTo = pointsToFollow.get(this.followPointIndex + 1);
                            this.vectorDirection.set(this.followObjectTo.x - this.followObjectFrom.x, this.followObjectTo.y - this.followObjectFrom.y);
                            vectorDirection.nor();
                            this.followerObject.body.setLinearVelocity(0, 0);
                            this.followerObject.position.set(this.followObjectFrom.x, this.followObjectFrom.y);
                            this.followerObject.body.setTransform(this.followObjectFrom.x / Constants.BOX2D_SCALE, this.followObjectFrom.y / Constants.BOX2D_SCALE, 0);
                        }
                    }
                }
                this.followerObject.update(deltaTime);
                break;
            case Teardown:
                if (followerObjectTime > FOLLOW_OBJECT_SCALE_TIME) {
                    if (!oneTimeOnly) {
                        this.followerObjectTime = 0;
                        this.followerObject.body.setLinearVelocity(0, 0);
                        this.followerObject.position.set(this.followObjectFrom.x, this.followObjectFrom.y);
                        this.followerObject.body.setTransform(this.followObjectFrom.x / Constants.BOX2D_SCALE, this.followObjectFrom.y / Constants.BOX2D_SCALE, 0);
                        this.followerObjectState = PropertyState.Buildup;
                    }
                } else {

                    this.followerObject.scale.set(1 - this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME, 1 - this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME);
                    this.followerObject.body.getFixtureList().get(0).getShape().setRadius((((1 - this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME) * this.followObjectOriginalSize / 2.0f) / Constants.BOX2D_SCALE));
                    if (1 - followerObjectTime / FOLLOW_OBJECT_SCALE_TIME < Constants.COLLISION_ACTIVATION_PERCENT) {
                        this.followerObject.body.setActive(false);
                    }
                }
                break;
            case Gone:
                if (deactivate) {
                    this.followerObject.body.setActive(false);
                    deactivate = false;
                }
                break;
        }
    }

    public void render(SpriteBatch batch) {
        if (followerObjectState != PropertyState.Inactive && followerObjectState != PropertyState.Gone) {
            followerObject.render(batch);
        }
    }

    public void scale(float scale) {
        this.followerObject.scale.set(this.followerObject.scale.x * scale, this.followerObject.scale.y * scale);
    }

    public void destroy() {
        this.followerObjectState = PropertyState.Gone;
        this.deactivate = true;
    }

    public void reset() {
        this.followerObject.body.getFixtureList().get(0).getShape().setRadius(0);
        this.followerObject.body.setLinearVelocity(0, 0);
        this.followerObject.body.setActive(false);
        this.followerObject.body.setTransform(this.startingPosition.x / Constants.BOX2D_SCALE, this.startingPosition.y / Constants.BOX2D_SCALE, 0);

        this.followerObjectTime = 0;

        this.followPointIndex = 0;
        this.direction = this.startingDirection;

        this.vectorDirection.set(0, 0);

        this.followerObject.position.x = this.startingPosition.x;
        this.followerObject.position.y = this.startingPosition.y;

        this.followerObject.scale.set(0, 0);
    }

    public void start() {
        this.followerObjectTime = 0;
        this.followerObjectState = PropertyState.Inactive;
    }

    public enum Type {
        VerticalOscillator,
        HorizontalOscillator,
        Pacer,
        LevelFollower,
        AreaFollower,
        Looper
    }
}
