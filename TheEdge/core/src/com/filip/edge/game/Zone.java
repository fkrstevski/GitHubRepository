package come.filip.templategame.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class Zone
{
    private static final String TAG = Zone.class.getName();

    private int zoneID;
    private ArrayList<Stage> stages;

    public Zone(int id)
    {
        this.zoneID = id;
        stages = new ArrayList<Stage>();
    }

    public void AddStage(int stageID, ArrayList<Vector2> points)
    {
        stages.add(new Stage(stageID, points));
    }

    public Stage getStage(int stage)
    {
        return stages.get(stage);
    }

    public int getNumberOfStages()
    {
        return stages.size();
    }
}
