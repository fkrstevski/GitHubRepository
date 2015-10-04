package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-11.
 */
public class LeaderboardButton extends AbstractCircleButtonObject {
    public static final String TAG = LeaderboardButton.class.getName();

    public LeaderboardButton(int size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int size) {
        // middle
        buttonPixmap.fillRectangle(size / 2 - size / 10,           // x
                size / 2 - (int) (size / 5),    // y
                size / 5,                       // width
                size / 2);                      // height
        // right
        buttonPixmap.fillRectangle(size / 2 + (int) (size / 6.5),  // x
                size / 2 - (int) (size / 5),    // y
                size / 5,                       // width
                size / 4);                      // height

        // left
        buttonPixmap.fillRectangle(size / 2 - size / 3,            // x
                size / 2 - (int) (size / 5),    // y
                size / 5,                       // width
                size / 3);                      // height


    }
}
