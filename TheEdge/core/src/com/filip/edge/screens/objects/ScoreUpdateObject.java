package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.filip.edge.util.Constants;
import com.filip.edge.util.DigitRenderer;
import com.filip.edge.util.GamePreferences;

/**
 * Created by fkrstevski on 2015-12-06.
 */
public class ScoreUpdateObject implements Poolable {
    public int score;
    public Vector2 currentPosition;
    private Vector2 endPosition;
    public boolean isAlive;
    private float currentTime;
    private static final float UPDATE_TIME = 1;
    public float alpha;

    public ScoreUpdateObject() {
        this.score = 0;
        this.alpha = 1;
        this.currentTime = 0;
        this.isAlive = false;
        this.currentPosition = new Vector2();
        this.endPosition = new Vector2();
    }

    public void init(int score, float startXpos, float startYpos, float endXpos, float endYpos) {
        this.score = score;
        this.currentPosition.set(startXpos, startYpos);
        this.endPosition.set(endXpos, endYpos);
        this.isAlive = true;
        this.currentTime = 0;
        this.alpha = 1;
    }

    @Override
    public void reset() {
        this.currentPosition.set(0,0);
        this.endPosition.set(0,0);
        this.score = 0;
        this.alpha = 1;
        this.currentTime = 0;
        this.isAlive = false;
    }

    public void update (float delta) {
        if(this.isAlive) {
            this.currentTime+=delta;
            if(this.currentTime > UPDATE_TIME) {
                this.isAlive = false;
            }
            else {
                this.alpha = currentTime/UPDATE_TIME;
                this.currentPosition.lerp(this.endPosition, this.alpha);
            }
        }
    }
}