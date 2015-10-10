package com.filip.edge.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by fkrstevski on 2015-09-29.
 */
public class LevelPoint extends Vector2 {
    public boolean hasAHole;
    public int holeStartupIndex;
    public int holeScaleIndex;
    public boolean followerIsBackAndForth;
    public int followerDirection; // has a follower if not 0
    public int followStartupIndex;
    public int followSpeedIndex;
    public boolean hasVerticalOscillator;
    public boolean hasHorizontalOscillator;
    public int oscillatorStartupIndex;
    public int oscillatorSpeedIndex;
    public boolean disappears;
    public int disappearsStartupIndex;
    public int disappearsTimeIndex;
    public boolean pacer;
    public int pacerStartupIndex;
    public int pacerSpeedIndex;


    public LevelPoint(float x, float y) {
        super(x, y);
    }

    public LevelPoint(float x, float y,
                      boolean h, int holeStartupIndex, int holeScaleIndex,
                      boolean backAndForth, int followerDirection, int followStartupIndex, int followSpeedIndex,
                      boolean vertical, boolean horizontal, int oscillatorStartupIndex, int oscillatorSpeedIndex,
                      boolean disappears, int disappearsStartupIndex, int disappearsTimeIndex,
                      boolean pacer, int pacerStartupIndex, int pacerSpeedIndex) {
        super(x, y);
        this.hasAHole = h;
        this.holeStartupIndex = holeStartupIndex;
        this.holeScaleIndex = holeScaleIndex;

        this.followerIsBackAndForth = backAndForth;
        this.followerDirection = followerDirection;
        this.followStartupIndex = followStartupIndex;
        this.followSpeedIndex = followSpeedIndex;

        this.hasVerticalOscillator = vertical;
        this.hasHorizontalOscillator = horizontal;
        this.oscillatorStartupIndex = oscillatorStartupIndex;
        this.oscillatorSpeedIndex = oscillatorSpeedIndex;

        this.disappears = disappears;
        this.disappearsStartupIndex = disappearsStartupIndex;
        this.disappearsTimeIndex = disappearsTimeIndex;

        this.pacer = pacer;
        this.pacerStartupIndex = pacerStartupIndex;
        this.pacerSpeedIndex = pacerSpeedIndex;
    }
}
