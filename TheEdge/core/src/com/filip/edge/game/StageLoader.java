package com.filip.edge.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

enum LevelProperties {
    None,
    Follower,
    Disappears
}

class LevelProperty {
    LevelProperties property;
    boolean set;
    int startupTime;
    int speed;
    int zone;
    int stage;

    LevelProperty(LevelProperties p, int startup, int speed, int z, int s) {
        this.set = true;
        this.property = p;
        this.startupTime = startup;
        this.speed = speed;
        this.zone = z;
        this.stage = s;
    }
}

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class StageLoader {
    private static final String TAG = StageLoader.class.getName();
    private static ArrayList<Zone> zones;

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
        int numberOfStages = 1;
        int numberOfZones = 4;

        ArrayList<LevelProperty> levelProperties = new ArrayList<LevelProperty>();

        String nl = System.getProperty("line.separator");

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            final int maxPoints = 60;
            final int sheetWidth = 81;
            final int sheetHeight = 29;

            LevelPoint[] points = new LevelPoint[maxPoints];

            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            StringBuffer sb = new StringBuffer();

            for (int currentZone = 0; currentZone < numberOfZones; ++currentZone) {
                for (int currentStage = 0; currentStage < numberOfStages; ++currentStage) {
                    String filename = "levels/Zone" + currentZone + "Stage" + currentStage + ".csv";
                    FileHandle fileHandle = Gdx.files.internal(filename);
                    assert (fileHandle != null);

                    try {

                        br = new BufferedReader(new FileReader(filename));
                        int y = 0;
                        while ((line = br.readLine()) != null) {

                            // use comma as separator
                            String[] fullCells = line.split(cvsSplitBy);
                            for (int x = 0; x < fullCells.length; ++x) {
                                String fullCell = fullCells[x].toUpperCase();
                                String[] cells = fullCell.split(";");
                                for (int i = 0; i < cells.length; ++i) {
                                    String cell = cells[i].toUpperCase();
                                    if (!cell.isEmpty()) {
                                        if (cell.equalsIgnoreCase("M") || cell.equalsIgnoreCase("Q") || cell.equalsIgnoreCase("E")) {
                                            // do nothing
                                        }
                                        // if the first char in the cell is not an int, it means we are setting the level properties
                                        else if (!isInteger("" + cell.charAt(0))) {
                                            String results[] = cell.split("(?<=[0-9])(?=[a-zA-Z])");

                                            for (String r : results) {
                                                if (r.charAt(0) == 'F') {
                                                    levelProperties.add(
                                                            new LevelProperty(LevelProperties.Follower,
                                                                    Integer.parseInt(r.substring(1, 2)),
                                                                    Integer.parseInt(r.substring(2, 3)),
                                                                    currentZone, currentStage
                                                            )
                                                    );
                                                } else if (r.charAt(0) == 'D') {
                                                    levelProperties.add(
                                                            new LevelProperty(LevelProperties.Disappears,
                                                                    Integer.parseInt(r.substring(1, 2)),
                                                                    Integer.parseInt(r.substring(2, 3)),
                                                                    currentZone, currentStage
                                                            )
                                                    );
                                                }
                                            }


                                        } else {
                                            if (isInteger(cell)) { /* if the cell is an int the value, it means there is no additional properties */
                                                points[Integer.parseInt(cell)] = new LevelPoint(sheetWidth * 0.00065f + x / (sheetWidth * 1.13f),
                                                        sheetHeight * 0.0066f + y / (sheetHeight * 1.43f));
                                            } else {

                                                // Check to see if the point index in the cell is 1 or 2 digits
                                                int pointIndex;
                                                if (Character.isDigit(cell.charAt(1))) {
                                                    pointIndex = Integer.parseInt(cell.substring(0, 2));
                                                } else {
                                                    pointIndex = Integer.parseInt(cell.substring(0, 1));
                                                }

                                                System.out.println("pointIndex = " + pointIndex);
                                                points[pointIndex] = new LevelPoint(sheetWidth * 0.00065f + x / (sheetWidth * 1.13f),
                                                        sheetHeight * 0.0066f + y / (sheetHeight * 1.43f));

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
                                                            points[pointIndex].disappearsStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                            points[pointIndex].disappearsTimeIndex = (Integer.parseInt(r.substring(2, 3)));
                                                        }
                                                    }
                                                    else if (r.charAt(0) == 'P') {
                                                        points[pointIndex].pacer = true;
                                                        if (r.length() > 1) {
                                                            points[pointIndex].pacerStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                            points[pointIndex].pacerSpeedIndex = (Integer.parseInt(r.substring(2, 3)));
                                                        }
                                                    }
                                                    else if (r.charAt(0) == 'O') {
                                                        points[pointIndex].orbiterPickup = true;
                                                        if (r.length() > 1) {
                                                            points[pointIndex].orbiterStartupIndex = (Integer.parseInt(r.substring(1, 2)));
                                                            points[pointIndex].orbiterDisappearIndex = (Integer.parseInt(r.substring(2, 3)));
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

                    if (levelProperties.size() > 0) {
                        for (LevelProperty property : levelProperties) {
                            if (property.zone == currentZone && property.stage == currentStage) {
                                sb.append(String.format("%s,%b,%d,%d;", property.property.name(), property.set, property.startupTime, property.speed));
                            }
                        }
                    } else {
                        sb.append(String.format("%s,%b,%d,%d;", LevelProperties.Follower, false, 0, 0));
                        sb.append(String.format("%s,%b,%d,%d;", LevelProperties.Disappears, false, 0, 0));
                    }

                    sb.append(nl);

                    for (int i = 0; i < points.length; i++) {
                        if (points[i] != null) {
                            sb.append(String.format("%.02f,%.02f,%b,%d,%d,%b,%d,%d,%d,%b,%b,%d,%d,%b,%d,%d,%b,%d,%d,%b,%d,%d;",
                                    points[i].x, points[i].y,
                                    points[i].hasAHole, points[i].holeStartupIndex, points[i].holeScaleIndex,
                                    points[i].followerIsBackAndForth, points[i].followerDirection, points[i].followStartupIndex, points[i].followSpeedIndex,
                                    points[i].hasHorizontalOscillator, points[i].hasVerticalOscillator, points[i].oscillatorStartupIndex, points[i].oscillatorSpeedIndex,
                                    points[i].disappears, points[i].disappearsStartupIndex, points[i].disappearsTimeIndex,
                                    points[i].pacer, points[i].pacerStartupIndex, points[i].pacerSpeedIndex,
                                    points[i].orbiterPickup, points[i].orbiterStartupIndex, points[i].orbiterDisappearIndex));
                            points[i] = null;
                        } else {
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

        for (int i = 0; i < linesInFile.length; i++) {
            currentZone = i / (numberOfStages * 2);
            currentStage = i % (numberOfStages * 2); // * 2, since we are moving 2 lines per each loop

            Gdx.app.debug(TAG, "Zone = " + currentZone);
            Gdx.app.debug(TAG, "Stage = " + currentStage);

            ArrayList<LevelProperty> stageProperties = new ArrayList<LevelProperty>();

            // Level line
            String line = linesInFile[i];
            Gdx.app.debug(TAG, "Level line = " + line);
            if (line.length() > 0) {
                String[] levelPropertiesString = line.split(";");

                for (int j = 0; j < levelPropertiesString.length; j++) {
                    String[] propertyValue = levelPropertiesString[j].split(",");
                    if(propertyValue.length > 1) {
                        Gdx.app.debug(TAG, "Level Property: " + propertyValue[0] + " = " + propertyValue[1]);
                        Gdx.app.debug(TAG, "Level Property start time: = " + propertyValue[2] + " speed time: = " + propertyValue[3]);
                        if (Boolean.parseBoolean(propertyValue[1])) {
                            stageProperties.add(new LevelProperty(LevelProperties.valueOf(propertyValue[0]),
                                    Integer.parseInt(propertyValue[2]), Integer.parseInt(propertyValue[3]), currentZone, currentStage));
                        }
                    }
                }
            }


            i++; // move to points line
            line = linesInFile[i];
            Gdx.app.debug(TAG, "line = " + line);

            String[] pointsInLine = line.split(";");

            ArrayList<LevelPoint> stagePoints = new ArrayList<LevelPoint>();

            for (int j = 0; j < pointsInLine.length; j++) {
                String[] pointProperty = pointsInLine[j].split(",");
                if(pointProperty.length > 1) {
                    Gdx.app.debug(TAG, "Point = x:" + pointProperty[0] + " y:" + pointProperty[1]);

                    stagePoints.add(
                            new LevelPoint(
                                    Float.parseFloat(pointProperty[0]) * width,
                                    Float.parseFloat(pointProperty[1]) * height,
                                    Boolean.parseBoolean(pointProperty[2]), Integer.parseInt(pointProperty[3]), Integer.parseInt(pointProperty[4]),
                                    Boolean.parseBoolean(pointProperty[5]), Integer.parseInt(pointProperty[6]), Integer.parseInt(pointProperty[7]), Integer.parseInt(pointProperty[8]),
                                    Boolean.parseBoolean(pointProperty[9]), Boolean.parseBoolean(pointProperty[10]), Integer.parseInt(pointProperty[11]), Integer.parseInt(pointProperty[12]),
                                    Boolean.parseBoolean(pointProperty[13]), Integer.parseInt(pointProperty[14]), Integer.parseInt(pointProperty[15]),
                                    Boolean.parseBoolean(pointProperty[16]), Integer.parseInt(pointProperty[17]), Integer.parseInt(pointProperty[18]),
                                    Boolean.parseBoolean(pointProperty[19]), Integer.parseInt(pointProperty[20]), Integer.parseInt(pointProperty[21])
                            )
                    );
                }
            }
            zones.get(currentZone).AddStage(currentStage, stagePoints, stageProperties);
        }
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
        return zones.get(zone).getStage(stage).getPoints();
    }
}