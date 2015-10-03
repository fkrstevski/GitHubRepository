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
    public boolean moves;
    public boolean disappears;


    public LevelPoint(float x, float y){
        super(x, y);
    }

    public LevelPoint(float x, float y, boolean h, int holeStartupIndex, int holeScaleIndex /*, boolean f, boolean m, boolean d*/){
        super(x, y);
        this.hasAHole = h;
        this.holeStartupIndex = holeStartupIndex;
        this.holeScaleIndex = holeScaleIndex;
        //this.hasAFollower = f;
        //this.moves = m;
        //this.disappears = d;
    }
}
