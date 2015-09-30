package com.filip.edge.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class Stage
{
    private static final String TAG = Stage.class.getName();

    private int stageID;
    private ArrayList<LevelPoint> points;

    public Stage(int id, ArrayList<LevelPoint> p)
    {
        this.stageID = id;
        this.points = p;
    }

    public ArrayList<LevelPoint> getPoints()
    {
        return points;
    }
}

