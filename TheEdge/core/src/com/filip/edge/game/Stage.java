package com.filip.edge.game;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class Stage {
    private static final String TAG = Stage.class.getName();
    public boolean hasFollowerObject;
    public int followerSpeedIndex;
    public int followerStartupTimeIndex;
    public boolean disappears;
    public int disappearSpeedIndex;
    public int disappearsStartupTimeIndex;
    private int stageID;
    private ArrayList<LevelPoint> points;
    public String instructions;

    public Stage(int id, ArrayList<LevelPoint> p, ArrayList<LevelProperty> properties, String instructions) {
        this.stageID = id;
        this.points = p;
        this.instructions = instructions;
        for (int i = 0; i < properties.size(); ++i) {
            if (properties.get(i).set) {
                if (properties.get(i).property == LevelProperties.Disappears) {
                    disappears = true;
                    disappearsStartupTimeIndex = properties.get(i).startupTime;
                    disappearSpeedIndex = properties.get(i).speed;
                } else if (properties.get(i).property == LevelProperties.Follower) {
                    hasFollowerObject = true;
                    followerStartupTimeIndex = properties.get(i).startupTime;
                    followerSpeedIndex = properties.get(i).speed;
                }
            }
        }
    }

    public ArrayList<LevelPoint> getPoints() {
        return points;
    }
}

