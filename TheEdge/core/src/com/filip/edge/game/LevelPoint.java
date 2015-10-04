package com.filip.edge.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by fkrstevski on 2015-09-29.
 */
public class LevelPoint extends Vector2 {
    public boolean hasAHole;
    public int holeStartupIndex;
    public int holeScaleIndex;
    public boolean hasAFollower;
    public int followStartupIndex;
    public int followSpeedIndex;

    public LevelPoint(float x, float y) {
        super(x, y);
    }

    public LevelPoint(float x, float y,
                      boolean h, int holeStartupIndex, int holeScaleIndex,
                      boolean f, int followStartupIndex, int followSpeedIndex) {
        super(x, y);
        this.hasAHole = h;
        this.holeStartupIndex = holeStartupIndex;
        this.holeScaleIndex = holeScaleIndex;

        this.hasAFollower = f;
        this.followStartupIndex = followStartupIndex;
        this.followSpeedIndex = followSpeedIndex;

    }
}
