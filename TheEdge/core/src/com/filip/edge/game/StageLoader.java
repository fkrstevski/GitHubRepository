package com.filip.edge.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.filip.edge.util.Constants;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class StageLoader
{
    private static final String TAG = StageLoader.class.getName();
    private static ArrayList<Zone> zones;

    public static boolean isInteger(String s) {
        boolean isValidInteger = false;
        try
        {
            Integer.parseInt(s);

            // s is a valid integer

            isValidInteger = true;
        }
        catch (NumberFormatException ex)
        {
            // s is not an integer
        }

        return isValidInteger;
    }

    public static void init()
    {
        int numberOfStages = 1;
        int numberOfZones = 4;

        String nl = System.getProperty("line.separator");

        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            final int maxPoints = 30;
            final int sheetWidth = 80;
            final int sheetHeight = 27;

            Vector2[] points = new Vector2[maxPoints];

            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            StringBuffer sb = new StringBuffer();

            for (int currentZone = 0; currentZone < numberOfZones; ++currentZone)
            {
                for (int currentStage = 0; currentStage < numberOfStages; ++currentStage)
                {
                    String filename = "levels/Zone" + currentZone + "/stage" + currentStage + ".csv";
                    FileHandle fileHandle = Gdx.files.internal(filename);
                    assert (fileHandle != null);

                    try {

                        br = new BufferedReader(new FileReader(filename));
                        int y = 0;
                        while ((line = br.readLine()) != null) {

                            // use comma as separator
                            String[] cells = line.split(cvsSplitBy);
                            for(int x = 0; x < cells.length; ++x) {
                                String cell = cells[x];
                                if(!cell.isEmpty() && isInteger(cell)) {
                                    points[Integer.parseInt(cell)] = new Vector2(sheetWidth * 0.00083f + x / (sheetWidth * 1.17f),
                                            sheetHeight * 0.0085f + y / (sheetHeight * 1.58f));
                                }
                            }
                            ++y;
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    for (int i = 0; i < points.length; i++)
                    {
                        if (points[i] != null)
                        {
                            sb.append(String.format("%.02f,%.02f;", points[i].x, points[i].y));
                            points[i] = null;
                        }
                        else
                        {
                            break;
                        }
                    }
                    sb.append(nl);
                }
            }

            FileHandle output = new FileHandle("TheEdge.output");
            // Delete the file first
            output.file().delete();

            output = new FileHandle("TheEdge.output");

            OutputStream os = output.write(true);
            try
            {
                os.write(sb.toString().getBytes());
            }
            catch (IOException e)
            {
                Gdx.app.error(TAG, "Failed to write to output file!!!!!!", e);
            }
            finally
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                    Gdx.app.error(TAG, "Failed to close output file!!!!!!", e);
                }
            }
        }

        zones = new ArrayList<Zone>();
        for (int i = 0; i < numberOfZones; i++)
        {
            zones.add(new Zone(i));
        }

        FileHandle fileHandle = Gdx.files.internal("TheEdge.output");
        String wholeFile = fileHandle.readString();
        String[] linesInFile = wholeFile.split(nl);

        int currentZone = 0;
        int currentStage = 0;

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        for (int i = 0; i < linesInFile.length; i++)
        {
            currentZone = i / numberOfStages;
            currentStage = i % numberOfStages;

            Gdx.app.debug(TAG, "Zone = " + currentZone);
            Gdx.app.debug(TAG, "Stage = " + currentStage);
            String line = linesInFile[i];
            Gdx.app.debug(TAG, "line = " + line);
            String[] pointsInLine = line.split(";");

            ArrayList<Vector2> stagePoints = new ArrayList<Vector2>();

            for (int j = 0; j < pointsInLine.length; j++)
            {
                String[] xANDy = pointsInLine[j].split(",");

                Gdx.app.debug(TAG, "Point = x:" + xANDy[0] + " y:" + xANDy[1]);

                stagePoints.add(new Vector2(Float.parseFloat(xANDy[0]) * width, Float.parseFloat(xANDy[1]) * height));
            }
            zones.get(currentZone).AddStage(currentStage, stagePoints);
        }
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