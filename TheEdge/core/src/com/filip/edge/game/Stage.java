package com.filip.edge.game;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class Stage
{
    private static final String TAG = Stage.class.getName();

    private int stageID;
    private ArrayList<LevelPoint> points;
    public boolean hasFollowerObject;

    public Stage(int id, ArrayList<LevelPoint> p, boolean follower)
    {
        this.stageID = id;
        this.points = p;
        this.hasFollowerObject = follower;
    }

    public ArrayList<LevelPoint> getPoints()
    {
        return points;
    }
}

