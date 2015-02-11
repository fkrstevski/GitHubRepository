package com.filip.offthewalls.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class Stage
{
    private static final String TAG = Stage.class.getName();

    private int stageID;
    private ArrayList<Vector2> points;

    public Stage(int id, ArrayList<Vector2> p)
    {
        this.stageID = id;
        this.points = p;
    }

    public ArrayList<Vector2> getPoints()
    {
        return points;
    }
}
