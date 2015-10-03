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
    public int followerSpeedIndex;
    public int followerStartupTimeIndex;
    public boolean disappears;
    public int disappearSpeedIndex;
    public int disappearsStartupTimeIndex;

    public Stage(int id, ArrayList<LevelPoint> p, ArrayList<LevelProperty> properties)
    {
        this.stageID = id;
        this.points = p;
        for (LevelProperty property :
                properties) {
            if(property.set){
                if(property.property == LevelProperties.Disappears) {
                    disappears = true;
                    disappearsStartupTimeIndex = property.startupTime;
                    disappearSpeedIndex = property.speed;
                }
                else if (property.property == LevelProperties.Follower){
                    hasFollowerObject = true;
                    followerStartupTimeIndex = property.startupTime;
                    followerSpeedIndex = property.speed;
                }
            }
        }
    }

    public ArrayList<LevelPoint> getPoints()
    {
        return points;
    }
}

