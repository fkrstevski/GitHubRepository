package com.filip.edge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;

/**
 * Created by fkrstevski on 2015-12-07.
 */
public class ScreenshotFactory {
    private static final String TAG = ScreenshotFactory.class.getName();
    private static boolean needToGetScreenshot = false;
    private static boolean flipY = false;
    private static String fileName = "";


    public static void getScreenShot(boolean y, String fn) {
        needToGetScreenshot = true;
        flipY = y;
        fileName = fn;
    }

    public static boolean needsToGetScreenshot() {
        return needToGetScreenshot;
    }

    public static void saveScreenshot() {
        needToGetScreenshot = false;
        //Then retrieve the Pixmap from the buffer.
        Pixmap pm = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (flipY) {
            // Flip the pixmap upside down
            ByteBuffer pixels = pm.getPixels();
            int numBytes = Gdx.graphics.getWidth() * Gdx.graphics.getHeight() * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = Gdx.graphics.getWidth() * 4;
            for (int i = 0; i < Gdx.graphics.getHeight(); i++) {
                pixels.position((Gdx.graphics.getHeight() - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
            pixels.clear();
        }

        Gdx.files.local(fileName).delete();
        //Save the pixmap as png to the disk.
        FileHandle levelTexture = Gdx.files.local(fileName);
        PixmapIO.writePNG(levelTexture, pm);
        pm.dispose();
    }
}