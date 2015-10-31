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
    public boolean disappears;
    public int disappearSpeedIndex;
    public int disappearsStartupTimeIndex;
    private int stageID;
    private ArrayList<LevelPoint> stagePoints;
    public String instructions;
    public ArrayList<Looper> loopers;
    public Looper pacer;
    public Looper follower;

    public Stage(int id, ArrayList<LevelPoint> p, ArrayList<LevelProperty> properties, String instructions) {
        this.stageID = id;
        this.stagePoints = p;
        this.instructions = instructions;
        for (int i = 0; i < properties.size(); ++i) {
            if (properties.get(i).set) {
                if (properties.get(i).property == LevelProperties.Disappears) {
                    disappears = true;
                    disappearsStartupTimeIndex = properties.get(i).startupTime;
                    disappearSpeedIndex = properties.get(i).speed;
                } else if (properties.get(i).property == LevelProperties.Follower) {
                    if(follower == null) {
                        ArrayList<LevelPoint> followerPoints = parsePoints(properties.get(i).points);
                        follower = new Looper(properties.get(i).startupTime, properties.get(i).speed, followerPoints);
                    }
                }
                else if (properties.get(i).property == LevelProperties.Looper) {
                    if(loopers == null) {
                        loopers = new ArrayList<Looper>();
                    }

                    ArrayList<LevelPoint> looperPoints = parsePoints(properties.get(i).points);
                    loopers.add(new Looper(properties.get(i).startupTime, properties.get(i).speed, looperPoints));
                }
                else if (properties.get(i).property == LevelProperties.Pacer) {
                    if(pacer == null) {
                        ArrayList<LevelPoint> pacerPoints = parsePoints(properties.get(i).points);
                        pacer = new Looper(properties.get(i).startupTime, properties.get(i).speed, pacerPoints);
                    }
                }
            }
        }
    }

    private ArrayList<LevelPoint> parsePoints(String points) {
        ArrayList<LevelPoint> pointsList = new ArrayList<LevelPoint>();
        String[] allPoints = points.split("@");
        for (int pointIndex = 0; pointIndex < allPoints.length; ++pointIndex) {
            String[] continuousPoints = allPoints[pointIndex].split("-");
            if(continuousPoints.length == 1) {
                pointsList.add(stagePoints.get(Integer.parseInt(allPoints[pointIndex])));
            }
            else {
                for(int continuousIndex = Integer.parseInt(continuousPoints[0]);
                    continuousIndex <= Integer.parseInt(continuousPoints[1]); ++continuousIndex) {
                    pointsList.add(stagePoints.get(continuousIndex));
                }
            }
        }

        return pointsList;
    }

    public ArrayList<LevelPoint> getStagePoints() {
        return stagePoints;
    }
}

