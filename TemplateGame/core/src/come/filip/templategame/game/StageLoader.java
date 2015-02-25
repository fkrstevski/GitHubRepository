package come.filip.templategame.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class StageLoader
{
    private static final String TAG = StageLoader.class.getName();

    public enum BLOCK_TYPE {
        START(255, 0, 0),       // RED
        FINISH(0, 255, 0),      // GREEN
        POINT1(255, 127, 0),    // ORANGE
        POINT2(255, 255, 0),    // YELLOW
        POINT3(0, 127, 255),    // BLUEISH
        POINT4(0, 255, 255),    // TURQUOISE
        POINT5(0, 0, 255),      // BLUE
        POINT6(127, 0, 255),    // PURPLE
        POINT7(255, 0, 255),    // PINK
        EMPTY(0, 0, 0),         // BLACK
        BORDER(255, 255, 255);  // WHITE

        private int color;

        private BLOCK_TYPE (int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor (int color) {
            return this.color == color;
        }

        public int getColor () {
            return color;
        }
    }

    private static ArrayList<Zone> zones;

    public static void init()
    {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        int numberOfStages = 4;
        int numberOfZones = 3;
        int maxPoints = 7;

        Vector2[] points = new Vector2[maxPoints];
        for (int i = 0; i < maxPoints; i++)
        {
            points[i] = new Vector2();
        }

        String nl = System.getProperty("line.separator");

        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            Vector2 startPos = new Vector2();
            Vector2 finishPos = new Vector2();


            StringBuffer sb = new StringBuffer();

            for(int currentZone = 0; currentZone < numberOfZones; ++currentZone)
            {
                for(int currentStage = 0; currentStage < numberOfStages; ++currentStage)
                {
                    String filename = "levels/Zone" + currentZone + "/stage" + currentStage +".png";
                    FileHandle fileHandle = Gdx.files.internal(filename);
                    assert (fileHandle != null);

                    // load image file that represents the level data
                    Pixmap pixmap = new Pixmap(fileHandle);

                    int pixmapHeight = pixmap.getHeight();
                    int pixmapWidth = pixmap.getWidth();

                    // scan pixels from top-left to bottom-right
                    int lastPixel = -1;
                    for (int pixelY = 0; pixelY < pixmapHeight; pixelY++)
                    {
                        for (int pixelX = 0; pixelX < pixmapWidth; pixelX++)
                        {
                            float offsetHeight = 0;
                            // height grows from bottom to top
                            float baseHeight = pixmapHeight - pixelY;
                            // get color of current pixel as 32-bit RGBA value
                            int currentPixel = pixmap.getPixel(pixelX, pixelY);
                            // find matching color value to identify block type at (x,y)
                            // point and create the corresponding game object if there is
                            // a match

                            // empty space
                            if (BLOCK_TYPE.EMPTY.sameColor(currentPixel) || BLOCK_TYPE.BORDER.sameColor(currentPixel)) {
                                // do nothing
                            }
                            // START
                            else if (BLOCK_TYPE.START.sameColor(currentPixel)) {
                                startPos.set((float)pixelX/pixmapWidth*width, (float)pixelY/pixmapHeight * height);
                            }
                            // FINISH
                            else if (BLOCK_TYPE.FINISH.sameColor(currentPixel)) {
                                finishPos.set((float)pixelX/pixmapWidth*width, (float)pixelY/pixmapHeight * height);
                            }
                            // Point 1
                            else if (BLOCK_TYPE.POINT1.sameColor(currentPixel)) {
                                points[0].set((float)pixelX/pixmapWidth*width, (float)pixelY/pixmapHeight * height);
                            }
                            // Point 2
                            else if (BLOCK_TYPE.POINT2.sameColor(currentPixel)) {
                                points[1].set((float)pixelX / pixmapWidth * width, (float)pixelY / pixmapHeight * height);
                            }
                            // Point 3
                            else if (BLOCK_TYPE.POINT3.sameColor(currentPixel)) {
                                points[2].set((float)pixelX / pixmapWidth * width, (float)pixelY / pixmapHeight * height);
                            }
                            // Point 4
                            else if (BLOCK_TYPE.POINT4.sameColor(currentPixel)) {
                                points[3].set((float)pixelX / pixmapWidth * width, (float)pixelY / pixmapHeight * height);
                            }
                            // Point 5
                            else if (BLOCK_TYPE.POINT5.sameColor(currentPixel)) {
                                points[4].set((float)pixelX / pixmapWidth * width, (float)pixelY / pixmapHeight * height);
                            }
                            // Point 6
                            else if (BLOCK_TYPE.POINT6.sameColor(currentPixel)) {
                                points[5].set((float)pixelX / pixmapWidth * width, (float)pixelY / pixmapHeight * height);
                            }
                            // Point 7
                            else if (BLOCK_TYPE.POINT7.sameColor(currentPixel)) {
                                points[6].set((float)pixelX / pixmapWidth * width, (float)pixelY / pixmapHeight * height);
                            }
                            // unknown object/pixel color
                            else {
                                // red color channel
                                int r = 0xff & (currentPixel >>> 24);
                                // green color channel
                                int g = 0xff & (currentPixel >>> 16);
                                // blue color channel
                                int b = 0xff & (currentPixel >>> 8);
                                // alpha channel
                                int a = 0xff & currentPixel;
                                Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b
                                        + "> a<" + a + ">");
                            }
                            lastPixel = currentPixel;
                        }
                    }

                    // reset vectors
                    sb.append(startPos.x + "," + startPos.y+";");
                    startPos.set(0,0);
                    for (int i = 0; i < maxPoints; i++)
                    {
                        if(!points[i].isZero())
                        {
                            sb.append(points[i].x + "," + points[i].y+";");
                            points[i].set(0,0);
                        }
                        else
                        {
                            break;
                        }
                    }
                    sb.append(finishPos.x + "," + finishPos.y+"");
                    finishPos.set(0,0);
                    sb.append(nl);


                    // free memory
                    pixmap.dispose();
                    Gdx.app.debug(TAG, "level '" + filename + "' loaded");
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
                catch(IOException e)
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

        for (int i = 0; i < linesInFile.length; i++)
        {
            currentZone = i / numberOfStages;
            currentStage = i % numberOfStages;

            Gdx.app.debug(TAG,"Zone = " + currentZone);
            Gdx.app.debug(TAG,"Stage = " +  currentStage);
            String line = linesInFile[i];
            Gdx.app.debug(TAG, "line = " + line);
            String[] pointsInLine = line.split(";");

            ArrayList<Vector2> stagePoints = new ArrayList<Vector2>();

            for (int j = 0; j < pointsInLine.length; j++)
            {
                String[] xANDy = pointsInLine[j].split(",");

                Gdx.app.debug(TAG, "Point = x:" + xANDy[0] + " y:" + xANDy[1]);

                stagePoints.add(new Vector2(Float.parseFloat(xANDy[0]), Float.parseFloat(xANDy[1])));
            }
            zones.get(currentZone).AddStage(currentStage, stagePoints);

        }





        /*Zone z2 = new Zone(0);
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

        zones.add(z3);*/
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