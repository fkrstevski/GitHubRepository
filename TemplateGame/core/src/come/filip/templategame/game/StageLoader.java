package come.filip.templategame.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class StageLoader
{
    private static final String TAG = StageLoader.class.getName();

    private static ArrayList<Zone> zones;

    public static void init()
    {
        zones = new ArrayList<Zone>();

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();


        Zone z2 = new Zone(0);
        ArrayList<Vector2> stage1PointsZone2 = new ArrayList<Vector2>();
        stage1PointsZone2.add(new Vector2(width * 0.15f, height * 0.85f));
        stage1PointsZone2.add(new Vector2(width * 0.15f, height * 0.15f));
        stage1PointsZone2.add(new Vector2(width * 0.85f, height * 0.15f));
        z2.AddStage(0, stage1PointsZone2);

        ArrayList<Vector2> stage2PointsZone2 = new ArrayList<Vector2>();
        stage2PointsZone2.add(new Vector2(width * 0.15f, height * 0.15f));
        stage2PointsZone2.add(new Vector2(width * 0.15f, height * 0.85f));
        stage2PointsZone2.add(new Vector2(width * 0.85f, height * 0.85f));
        z2.AddStage(1, stage2PointsZone2);

        ArrayList<Vector2> stage3PointsZone2 = new ArrayList<Vector2>();
        stage3PointsZone2.add(new Vector2(width * 0.15f, height * 0.85f));
        stage3PointsZone2.add(new Vector2(width * 0.85f, height * 0.15f));
        stage3PointsZone2.add(new Vector2(width * 0.85f, height * 0.85f));
        z2.AddStage(2, stage3PointsZone2);

        zones.add(z2);

        Zone z3 = new Zone(1);
        ArrayList<Vector2> stage1PointsZone3 = new ArrayList<Vector2>();
        stage1PointsZone3.add(new Vector2(width * 0.15f, height * 0.85f));
        stage1PointsZone3.add(new Vector2(width * 0.85f, height * 0.85f));
        stage1PointsZone3.add(new Vector2(width * 0.15f, height * 0.15f));
        stage1PointsZone3.add(new Vector2(width * 0.85f, height * 0.15f));
        z3.AddStage(0, stage1PointsZone3);

        ArrayList<Vector2> stage2PointsZone3 = new ArrayList<Vector2>();
        stage2PointsZone3.add(new Vector2(width * 0.15f, height * 0.85f));
        stage2PointsZone3.add(new Vector2(width * 0.15f, height * 0.15f));
        stage2PointsZone3.add(new Vector2(width * 0.85f, height * 0.15f));
        stage2PointsZone3.add(new Vector2(width * 0.85f, height * 0.85f));
        z3.AddStage(1, stage2PointsZone3);

        ArrayList<Vector2> stage3PointsZone3 = new ArrayList<Vector2>();
        stage3PointsZone3.add(new Vector2(width * 0.15f, height * 0.85f));
        stage3PointsZone3.add(new Vector2(width * 0.15f, height * 0.15f));
        stage3PointsZone3.add(new Vector2(width * 0.50f, height * 0.5f));
        stage3PointsZone3.add(new Vector2(width * 0.85f, height * 0.15f));
        z3.AddStage(2, stage3PointsZone3);

        zones.add(z3);
    }

    public static Zone getZone(int zoneID)
    {
        return zones.get(zoneID);
    }

    public static int getZoneStages(int zoneID)
    {
        return zones.get(zoneID).getNumberOfStages();
    }

    public static int getNumberOfZones()
    {
        return zones.size();
    }

    public static ArrayList<Vector2> getPoints(int zone, int stage)
    {
        return zones.get(zone).getStage(stage).getPoints();
    }
}