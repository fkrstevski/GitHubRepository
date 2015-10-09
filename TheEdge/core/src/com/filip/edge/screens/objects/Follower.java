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
    private int followPointIndex;
    private float followObjectOriginalSize;
    private int direction;
    private boolean backAndForth;

    public Follower(float startUpTime, Vector2 speed, float size, Vector2 pos, ArrayList<Vector2> pointsToFollow, int dir, boolean backAndForth) {

        this.followerObjectTime = 0;
        this.followerObjectDisplayStartTime = startUpTime;
        this.followerObjectState = PropertyState.Inactive;
        this.followPointIndex = 0;
        this.followerObjectSpeed = speed;
        this.followObjectOriginalSize = size;
        this.pointsToFollow = pointsToFollow;
        this.direction = dir;
        this.backAndForth = backAndForth;

        this.followerObject = new EmptyCircle(this.followObjectOriginalSize,
                pos.x,
                pos.y,
                Color.BLACK,
                Color.BLACK);
    }

    public void update(float deltaTime) {
        this.followerObjectTime += deltaTime;
        switch (this.followerObjectState) {
            case Inactive:

                if (this.followerObjectTime > this.followerObjectDisplayStartTime) {
                    this.followerObject.body.setActive(true);
                    this.followerObjectTime = 0;
                    this.followObjectFrom = pointsToFollow.get(this.followPointIndex);
                    this.followObjectTo = pointsToFollow.get(this.followPointIndex + 1);
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

                break;
            case Active:
                Vector2 dir = new Vector2(this.followObjectTo.x - this.followObjectFrom.x, this.followObjectTo.y - this.followObjectFrom.y);
                Vector2 dirN = dir.nor();
                this.followerObject.body.setLinearVelocity(dirN.scl(this.followerObjectSpeed));
                if (this.followerObject.position.epsilonEquals(this.followObjectTo, this.followerObjectSpeed.len() / 7f)) {
                    this.followerObject.body.setLinearVelocity(new Vector2(0, 0));
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
                    } else {
                        if (followPointIndex >= pointsToFollow.size() - 1 || followPointIndex < 0) {
                            this.followPointIndex = 0;
                            this.followerObjectTime = 0;
                            this.followObjectFrom = pointsToFollow.get(this.followPointIndex);
                            this.followObjectTo = pointsToFollow.get(this.followPointIndex + 1);
                            this.followerObject.body.setLinearVelocity(new Vector2(0, 0));
                            this.followerObjectState = PropertyState.Teardown;
                        } else {
                            this.followObjectFrom = pointsToFollow.get(this.followPointIndex);
                            this.followerObject.body.setLinearVelocity(new Vector2(0, 0));
                            // Update the body to the new point TODO: doesnt work too well
                            this.followerObject.position.set(this.followObjectFrom.x, this.followObjectFrom.y);
                            this.followerObject.body.setTransform(this.followObjectFrom.x / Constants.BOX2D_SCALE, this.followObjectFrom.y / Constants.BOX2D_SCALE, 0);
                            this.followObjectTo = pointsToFollow.get(this.followPointIndex + 1);
                        }
                    }
                }
                this.followerObject.update(deltaTime);
                break;
            case Teardown:
                if (followerObjectTime > FOLLOW_OBJECT_SCALE_TIME) {
                    this.followerObjectTime = 0;
                    this.followerObject.body.setLinearVelocity(new Vector2(0, 0));
                    this.followerObject.position.set(this.followObjectFrom.x, this.followObjectFrom.y);
                    this.followerObject.body.setTransform(this.followObjectFrom.x / Constants.BOX2D_SCALE, this.followObjectFrom.y / Constants.BOX2D_SCALE, 0);
                    this.followerObjectState = PropertyState.Buildup;
                } else {

                    this.followerObject.scale.set(1 - this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME, 1 - this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME);
                    this.followerObject.body.getFixtureList().get(0).getShape().setRadius((((1 - this.followerObjectTime / FOLLOW_OBJECT_SCALE_TIME) * this.followObjectOriginalSize / 2.0f) / Constants.BOX2D_SCALE));
                }
                break;
        }
    }

    public void render(SpriteBatch batch) {
        if (followerObjectState != PropertyState.Inactive) {
            followerObject.render(batch);
        }
    }

    public void scale(float scale) {
        this.followerObject.scale.set(this.followerObject.scale.x * scale, this.followerObject.scale.y * scale);
    }
}
