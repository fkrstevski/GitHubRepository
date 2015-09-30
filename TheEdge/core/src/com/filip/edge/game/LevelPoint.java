package com.filip.edge.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by fkrstevski on 2015-09-29.
 */
public class LevelPoint extends Vector2 {
    public boolean hasAHole;

    public LevelPoint(float x, float y){
        super(x, y);
    }

    public LevelPoint(float x, float y, boolean h){
        super(x, y);
        this.hasAHole = h;
    }
}
