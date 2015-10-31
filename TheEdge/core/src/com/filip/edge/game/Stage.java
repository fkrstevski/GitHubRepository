package com.filip.edge.game;

import java.util.ArrayList;

class Looper {
    private static final String TAG = Looper.class.getName();

    public int speedIndex;
    public int startupTimeIndex;
    public ArrayList<LevelPoint> points;

    public Looper(int startupTimeIndex, int speedIndex, ArrayList<LevelPoint> points) {

        this.speedIndex = speedIndex;
        this.startupTimeIndex = startupTimeIndex;
        this.points = points;
    }
}

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
    public ArrayList<Looper> loopers;

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
                else if (properties.get(i).property == LevelProperties.Looper) {
                    if(loopers == null) {
                        loopers = new ArrayList<Looper>();
                    }

                    ArrayList<LevelPoint> looperPoints = new ArrayList<LevelPoint>();
                    String[] cells = properties.get(i).points.split("@");
                    for (int pointIndex = 0; pointIndex < cells.length; ++pointIndex) {
                        looperPoints.add(points.get(Integer.parseInt(cells[pointIndex])));
                    }

                    loopers.add(new Looper(properties.get(i).startupTime, properties.get(i).speed, looperPoints));
                }
            }
        }
    }

    public ArrayList<LevelPoint> getPoints() {
        return points;
    }
}

