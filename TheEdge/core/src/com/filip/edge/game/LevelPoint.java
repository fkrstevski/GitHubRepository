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
    public boolean appears;
    public int disappearsAppearsStartupIndex;
    public int disappearsAppearsTimeIndex;
    public boolean pacer;
    public int pacerStartupIndex;
    public int pacerSpeedIndex;
    public boolean orbiterPickup;
    public int orbiterStartupIndex;
    public int orbiterDisappearIndex;


    public LevelPoint(float x, float y) {
        super(x, y);
    }

    public LevelPoint(float x, float y,
                      boolean h, int holeStartupIndex, int holeScaleIndex,
                      boolean backAndForth, int followerDirection, int followStartupIndex, int followSpeedIndex,
                      boolean vertical, boolean horizontal, int oscillatorStartupIndex, int oscillatorSpeedIndex,
                      boolean disappears, boolean appears, int disappearsAppearsStartupIndex, int disappearsAppearsTimeIndex,
                      boolean pacer, int pacerStartupIndex, int pacerSpeedIndex,
                      boolean orbiterPickup, int orbiterStartupIndex, int orbiterDisappearIndex) {
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
        this.appears = appears;
        this.disappearsAppearsStartupIndex = disappearsAppearsStartupIndex;
        this.disappearsAppearsTimeIndex = disappearsAppearsTimeIndex;

        this.pacer = pacer;
        this.pacerStartupIndex = pacerStartupIndex;
        this.pacerSpeedIndex = pacerSpeedIndex;

        this.orbiterPickup = orbiterPickup;
        this.orbiterStartupIndex = orbiterStartupIndex;
        this.orbiterDisappearIndex = orbiterDisappearIndex;
    }
}
