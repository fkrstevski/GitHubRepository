package com.filip.edge.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.ArrayList;

enum LevelProperties {
    None,
    Follower,
    Disappears,
    Looper,
    Pacer
}

class LevelProperty {
    LevelProperties property;
    boolean set;
    int startupTime;
    int speed;
    int zone;
    int stage;
    String points;

    LevelProperty(LevelProperties p, int startup, int speed, int z, int s) {
        this.set = true;
        this.property = p;
        this.startupTime = startup;
        this.speed = speed;
        this.zone = z;
        this.stage = s;
    }

    LevelProperty(LevelProperties p, int startup, int speed, int z, int s, String points) {
        this(p, startup, speed, z, s);
        this.points = points;
    }
}

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class StageLoader {
    private static final String TAG = StageLoader.class.getName();
    private static ArrayList<Zone> zones;
    private static final int MAX_POINTS = 600;
    private static final int SHEET_WIDTH = 81;
    private static final int SHEET_HEIGHT = 29;

    public static boolean isInteger(String s) {
        boolean isValidInteger = false;
        try {
            Integer.parseInt(s);

            // s is a valid integer

            isValidInteger = true;
        } catch (NumberFormatException ex) {
            // s is not an integer
        }

        return isValidInteger;
    }

    public static void init() {
        int numberOfZones = 4;
        String levelInstructions;
        String nl = System.getProperty("line.separator");

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            LevelPoint[] points = new LevelPoint[MAX_POINTS];
            int[] numberOfStages = new int[numberOfZones];
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            StringBuffer sb = new StringBuffer();

            for (int currentZone = 0; currentZone < numberOfZones; ++currentZone) {
                int currentStage = 0;
                while (true) {
                    ArrayList<LevelProperty> levelProperties = new ArrayList<LevelProperty>();
                    String filename = "levels/Zone" + currentZone + "Stage" + currentStage + ".csv";
                    FileHandle fileHandle = Gdx.files.internal(filename);
                    levelInstructions = "";

                    // Break from the while loop if the file does not exist.
                    // It means we reach the end of the stages for the current zone.
                    if (!fileHandle.exists()) {
                        break;
                    }

                    try {

                        br = new BufferedReader(new FileReader(filename));
                        int y = 0;
                        //Gdx.app.log(TAG, "ZONE = " + currentZone + " Stage = " + currentStage);
                        while ((line = br.readLine()) != null) {

                            // use comma as separator
                            String[] fullCells = line.split(cvsSplitBy);
                            for (int x = 0; x < fullCells.length; ++x) {
                                String fullCell = fullCells[x].toUpperCase();
                                String[] cells = fullCell.split(";");
                                for (int i = 0; i < cells.length; ++i) {
                                    String cell = cells[i].toUpperCase();
                                    if (!cell.isEmpty()) {
                                        if (cell.equalsIgnoreCase("M") || cell.equalsIgnoreCase("Q") || cell.equalsIgnoreCase("E") || cell.equalsIgnoreCase("S")) {
                                            // do nothing
                                        }
                                        // Instruction
                                        else if(cell.charAt(0) == '#') {
                                            levelInstructions = cell.substring(1, cell.length());
                                        }
                                        // if the first char in the cell is not an int, it means we are setting the level properties
                                        else if (!isInteger("" + cell.charAt(0))) {
                                            if(cell.charAt(0) == 'L') {
                                                levelProperties.add(new LevelProperty(LevelProperties.Looper,
                                                        Integer.parseInt(cell.substring(1, 2)),
                                                        Integer.parseInt(cell.substring(2, 3)),
                                                        currentZone, currentStage, cell.substring(4)));
                                            }
                                            else if (cell.charAt(0) == 'F') {
                                                levelProperties.add(
                                                        new LevelProperty(LevelProperties.Follower,
                                                                Integer.parseInt(cell.substring(1, 2)),
                                                                Integer.parseInt(cell.substring(2, 3)),
                                                                currentZone, currentStage, cell.substring(4)
                                                        )
                                                );
                                            } else if (cell.charAt(0) == 'D') {
                                                levelProperties.add(
                                                        new LevelProperty(LevelProperties.Disappears,
                                                                Integer.parseInt(cell.substring(1, 2)),
                                                                Integer.parseInt(cell.substring(2, 3)),
                                                                currentZone, currentStage
                                                        )
                                                );
                                            }
                                            else if (cell.charAt(0) == 'P') {
                                                levelProperties.add(
                                                        new LevelProperty(LevelProperties.Pacer,
                                                                Integer.parseInt(cell.substring(1, 2)),
                                                                Integer.parseInt(cell.substring(2, 3)),
                                                                currentZone, currentStage, cell.substring(4)
                                                        )
                                                );
                                            }
                                        } else {
                                            if (isInteger(cell)) { /* if the cell is an int the value, it means there is no additional properties */
                                                points[Integer.parseInt(cell)] = new LevelPoint(getXpoint(x), getYpoint(y));
                                            } else {

                                                // Check to see if the point index in the cell is 1 or 2 digits
                                                int pointIndex;
                                                int digitLength = 0;
                                                if (Character.isDigit(cell.charAt(0))) {
                                                    digitLength = 1;
                                                    if (Character.isDigit(cell.charAt(1))) {
                                                        digitLength = 2;
                                                        if (Character.isDigit(cell.charAt(2))) {
                                                            digitLength = 3;
                                                        }
                                                    }
                                                }

                                                pointIndex = Integer.parseInt(cell.substring(0, digitLength));

                                                System.out.println("pointIndex = " + pointIndex);
                                                points[pointIndex] = new LevelPoint(getXpoint(x), getYpoint(y));

                                                String resultsArray[] = cell.split("^[0-9]{1,2}");

                                                String s = resultsArray[1]; //Ignore first empty result
                                                System.out.println(s);
                                                String results[] = s.split("(?<=[0-9])(?=[a-zA-Z])");

                                                for (String r : results) {
                                                    if (r.charAt(0) == 'H') {
                                                        points[pointIndex].hasAHole = true;
                                                        if (r.length() > 1) {
                                                            points[pointIndex].holeStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                            points[pointIndex].holeScaleIndex = (Integer.parseInt(r.substring(2, 3)));
                                                        }
                                                    } else if (r.charAt(0) == 'F' || r.charAt(0) == 'R') {
                                                        points[pointIndex].followerDirection = (r.charAt(0) == 'F' ? 1 : -1);
                                                        if (r.length() > 1) {
                                                            points[pointIndex].followStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                            points[pointIndex].followSpeedIndex = (Integer.parseInt(r.substring(2, 3)));
                                                        }
                                                    } else if (r.charAt(0) == 'B') {
                                                        if (r.charAt(1) == 'F' || r.charAt(1) == 'R') {
                                                            points[pointIndex].followerDirection = (r.charAt(1) == 'F' ? 1 : -1);
                                                            points[pointIndex].followerIsBackAndForth = true;
                                                            if (r.length() > 2) {
                                                                points[pointIndex].followStartupIndex = (Integer.parseInt(r.substring(2, 3)));
                                                                points[pointIndex].followSpeedIndex = (Integer.parseInt(r.substring(3, 4)));
                                                            }
                                                        }
                                                    } else if (r.charAt(0) == 'X' || r.charAt(0) == 'Y') {
                                                        if (r.charAt(0) == 'X') {
                                                            points[pointIndex].hasHorizontalOscillator = true;
                                                        } else {
                                                            points[pointIndex].hasVerticalOscillator = true;
                                                        }
                                                        if (r.length() > 1) {
                                                            points[pointIndex].oscillatorStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                            points[pointIndex].oscillatorSpeedIndex = (Integer.parseInt(r.substring(2, 3)));
                                                        }
                                                    } else if (r.charAt(0) == 'D') {
                                                        points[pointIndex].disappears = true;
                                                        if (r.length() > 1) {
                                                            points[pointIndex].disappearsAppearsStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                            points[pointIndex].disappearsAppearsTimeIndex = (Integer.parseInt(r.substring(2, 3)));
                                                        }
                                                    }
                                                    else if (r.charAt(0) == 'A') {
                                                        points[pointIndex].appears = true;
                                                        if (r.length() > 1) {
                                                            if(r.substring(1, 2).startsWith("-")) {
                                                                points[pointIndex].disappearsAppearsStartupIndex = -1;
                                                                points[pointIndex].disappearsAppearsTimeIndex = -1;
                                                            }
                                                            else {
                                                                points[pointIndex].disappearsAppearsStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                                points[pointIndex].disappearsAppearsTimeIndex = (Integer.parseInt(r.substring(2, 3)));
                                                            }
                                                        }
                                                    }
                                                    else if (r.charAt(0) == 'O') {
                                                        points[pointIndex].orbiterPickup = true;
                                                        if (r.length() > 1) {
                                                            if(r.substring(1, 2).startsWith("-")) {
                                                                points[pointIndex].orbiterStartupIndex = -1;
                                                                points[pointIndex].orbiterDisappearIndex = -1;
                                                            }
                                                            else {
                                                                points[pointIndex].orbiterStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                                points[pointIndex].orbiterDisappearIndex = (Integer.parseInt(r.substring(2, 3)));
                                                            }
                                                        }
                                                    }
                                                    else if (r.charAt(0) == 'G') {
                                                        points[pointIndex].gold = true;
                                                    }
                                                    else if (r.charAt(0) == 'S') {
                                                        if (r.charAt(1) == 'C') {
                                                            points[pointIndex].extraCircleScale = (Float.parseFloat(r.substring(2, r.length())));
                                                        }
                                                        else if (r.charAt(1) == 'R') {
                                                            points[pointIndex].extraRectangleScale = (Float.parseFloat(r.substring(2, r.length())));
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                            ++y;
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        points = new LevelPoint[MAX_POINTS];
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                        points = new LevelPoint[MAX_POINTS];
                        continue;
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (levelProperties.size() > 0) {
                        for (LevelProperty property : levelProperties) {
                            if (property.zone == currentZone && property.stage == currentStage) {
                                sb.append(String.format("%c,%b,%d,%d,%s;", property.property.name().charAt(0), property.set, property.startupTime, property.speed, property.points));
                            }
                        }
                    }

                    sb.append(nl);
                    sb.append(levelInstructions);
                    sb.append(nl);

                    for (int i = 0; i < points.length; i++) {
                        if (points[i] != null) {
                            sb.append(String.format("%.04f,%.04f,%c,%d,%d,%c,%d,%d,%d,%c,%c,%d,%d,%c,%c,%d,%d,%c,%d,%d,%f,%f,%c;",
                                    points[i].x, points[i].y,
                                    (points[i].hasAHole ? 't' : 'f'), points[i].holeStartupIndex, points[i].holeScaleIndex,
                                    (points[i].followerIsBackAndForth ? 't' : 'f'), points[i].followerDirection, points[i].followStartupIndex, points[i].followSpeedIndex,
                                    (points[i].hasHorizontalOscillator ? 't' : 'f'), (points[i].hasVerticalOscillator ? 't' : 'f'), points[i].oscillatorStartupIndex, points[i].oscillatorSpeedIndex,
                                    (points[i].disappears ? 't' : 'f'), (points[i].appears ? 't' : 'f'), points[i].disappearsAppearsStartupIndex, points[i].disappearsAppearsTimeIndex,
                                    (points[i].orbiterPickup ? 't' : 'f'), points[i].orbiterStartupIndex, points[i].orbiterDisappearIndex,
                                    points[i].extraCircleScale, points[i].extraRectangleScale ,
                                    (points[i].gold ? 't' : 'f')));
                            points[i] = null;
                        } else {
                            break;
                        }
                    }
                    sb.append(nl);
                    currentStage++;
                }

                numberOfStages[currentZone] = currentStage;
                //Gdx.app.log(TAG, "Zone " + currentZone + " has " + numberOfStages[currentZone] + " stages");
            }

            for(int i = 0; i < numberOfZones; ++i) {
                sb.append(numberOfStages[i] + ";");
            }
            sb.append(nl);


            FileHandle output = new FileHandle("TheEdge.output");
            // Delete the file first
            output.file().delete();

            output = new FileHandle("TheEdge.output");

            OutputStream os = output.write(true);
            try {
                os.write(sb.toString().getBytes());
            } catch (IOException e) {
                Gdx.app.error(TAG, "Failed to write to output file!!!!!!", e);
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                    Gdx.app.error(TAG, "Failed to close output file!!!!!!", e);
                }
            }
        }

        zones = new ArrayList<Zone>();
        for (int i = 0; i < numberOfZones; i++) {
            zones.add(new Zone(i));
        }

        FileHandle fileHandle = Gdx.files.internal("TheEdge.output");
        String wholeFile = fileHandle.readString();
        String[] linesInFile = wholeFile.split(nl);

        int currentZone = 0;
        int currentStage = 0;

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        int[] numberOfStages = new int[numberOfZones];

        // Stages line - Stored in the last line
        String stagesline = linesInFile[linesInFile.length - 1];
        //Gdx.app.debug(TAG, "Stages line = " + stagesline);
        if (stagesline.length() > 0) {
            String[] stagesString = stagesline.split(";");
            for (int j = 0; j < stagesString.length; j++) {
                numberOfStages[j] = Integer.parseInt(stagesString[j]);
            }
        }

        for (int i = 0; i < linesInFile.length - 1; i++) {
            if(i  < numberOfStages[0] * 3) {
                currentZone = 0;
                currentStage = i / 3;
            }
            else if (i < numberOfStages[0] * 3 + numberOfStages[1] * 3) {
                currentZone = 1;
                currentStage = (i - numberOfStages[0] * 3) / 3;
            }
            else if (i < numberOfStages[0] * 3 + numberOfStages[1] * 3 + numberOfStages[2] * 3) {
                currentZone = 2;
                currentStage = (i - numberOfStages[0] * 3 - numberOfStages[1] * 3) / 3;
            }
            else {
                currentZone = 3;
                currentStage = (i - numberOfStages[0] * 3 - numberOfStages[1] * 3 - numberOfStages[2] * 3) / 3;
            }

            //Gdx.app.debug(TAG, "i = " + i);
            //Gdx.app.debug(TAG, "Zone = " + currentZone);
            //Gdx.app.debug(TAG, "Stage = " + currentStage);

            ArrayList<LevelProperty> stageProperties = new ArrayList<LevelProperty>();

            // Level line
            String line = linesInFile[i];
            //Gdx.app.debug(TAG, "Level line = " + line);
            if (line.length() > 0) {
                String[] levelPropertiesString = line.split(";");
                for (int j = 0; j < levelPropertiesString.length; j++) {
                    String[] propertyValue = levelPropertiesString[j].split(",");
                    if(propertyValue.length > 1) {
                        if (Boolean.parseBoolean(propertyValue[1])) {
                            LevelProperties type = LevelProperties.None;
                            if(propertyValue[0].equalsIgnoreCase("f")) {
                                type = LevelProperties.Follower;
                            }
                            else if (propertyValue[0].equalsIgnoreCase("d")) {
                                type = LevelProperties.Disappears;
                            }
                            else if (propertyValue[0].equalsIgnoreCase("l")) {
                                type = LevelProperties.Looper;
                            }
                            else if (propertyValue[0].equalsIgnoreCase("p")) {
                                type = LevelProperties.Pacer;
                            }

                            if(type != LevelProperties.None) {
                                stageProperties.add(new LevelProperty(type, Integer.parseInt(propertyValue[2]),
                                        Integer.parseInt(propertyValue[3]), currentZone, currentStage, propertyValue[4]));
                            }
                        }
                    }
                }
            }

            i++; // move to instruction line
            levelInstructions = linesInFile[i];
            //Gdx.app.debug(TAG, "Level instruction = " + levelInstructions);

            i++; // move to points line
            line = linesInFile[i];
            //Gdx.app.debug(TAG, "line = " + line);

            String[] pointsInLine = line.split(";");

            ArrayList<LevelPoint> stagePoints = new ArrayList<LevelPoint>();

            for (int j = 0; j < pointsInLine.length; j++) {
                String[] pointProperty = pointsInLine[j].split(",");
                if(pointProperty.length > 1) {
                    stagePoints.add(
                            new LevelPoint(
                                    Float.parseFloat(pointProperty[0]) * width,
                                    Float.parseFloat(pointProperty[1]) * height,
                                    pointProperty[2].equalsIgnoreCase("t"), Integer.parseInt(pointProperty[3]), Integer.parseInt(pointProperty[4]),
                                    pointProperty[5].equalsIgnoreCase("t"), Integer.parseInt(pointProperty[6]), Integer.parseInt(pointProperty[7]), Integer.parseInt(pointProperty[8]),
                                    pointProperty[9].equalsIgnoreCase("t"), pointProperty[10].equalsIgnoreCase("t"), Integer.parseInt(pointProperty[11]), Integer.parseInt(pointProperty[12]),
                                    pointProperty[13].equalsIgnoreCase("t"), pointProperty[14].equalsIgnoreCase("t"), Integer.parseInt(pointProperty[15]), Integer.parseInt(pointProperty[16]),
                                    pointProperty[17].equalsIgnoreCase("t"), Integer.parseInt(pointProperty[18]), Integer.parseInt(pointProperty[19]),
                                    Float.parseFloat(pointProperty[20]), Float.parseFloat(pointProperty[21]),
                                    pointProperty[22].equalsIgnoreCase("t")
                            )
                    );
                }
            }
            zones.get(currentZone).AddStage(currentStage, stagePoints, stageProperties, levelInstructions);
        }
    }

    private static float getXpoint(int xIndex) {
        return SHEET_WIDTH * 0.00065f + xIndex / (SHEET_WIDTH * 1.13f);
    }

    private static float getYpoint(int yIndex) {
        return SHEET_HEIGHT * 0.0066f + yIndex / (SHEET_HEIGHT * 1.43f);
    }

    public static float getDistanceBetweenTwoSideBySidePointsInX()
    {
        return (Math.abs(getXpoint(1) - getXpoint(0))) * Gdx.graphics.getWidth();
    }

    public static float getDistanceBetweenTwoSideBySidePointsInY()
    {
        return (Math.abs(getYpoint(1) - getYpoint(0))) * Gdx.graphics.getHeight();
    }

    public static Zone getZone(int zoneID) {
        return zones.get(zoneID);
    }

    public static int getZoneStages(int zoneID) {
        return zones.get(zoneID).getNumberOfStages();
    }

    public static int getNumberOfZones() {
        return zones.size();
    }

    public static ArrayList<LevelPoint> getPoints(int zone, int stage) {
        return zones.get(zone).getStage(stage).getStagePoints();
    }

    public static String getStageInstructions(int zone, int stage) {
        return zones.get(zone).getStage(stage).instructions;
    }
}