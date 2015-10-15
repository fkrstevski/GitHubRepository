package com.filip.edge.game;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class Zone {
    private static final String TAG = Zone.class.getName();

    private int zoneID;
    private ArrayList<Stage> stages;

    public Zone(int id) {
        this.zoneID = id;
        stages = new ArrayList<Stage>();
    }

    public void AddStage(int stageID, ArrayList<LevelPoint> points, ArrayList<LevelProperty> properties) {
        stages.add(new Stage(stageID, points, properties));
    }

    public Stage getStage(int stage) {
        return stages.get(stage);
    }

    public int getNumberOfStages() {
        return stages.size();
    }
}
