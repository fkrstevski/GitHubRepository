package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-11.
 */
public class LeaderboardButton extends AbstractCircleButtonObject {
    public static final String TAG = LeaderboardButton.class.getName();

    public LeaderboardButton(float size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(float size) {
        // middle
        buttonPixmap.fillRectangle((int)(size / 2 - size / 10),           // x
                (int)(size / 2 - size / 5),    // y
                (int)(size / 5),                       // width
                (int)(size / 2));                      // height
        // right
        buttonPixmap.fillRectangle((int)(size / 2 + size / 6.5),  // x
                (int)(size / 2 - size / 5),    // y
                (int)(size / 5),                       // width
                (int)(size / 4));                      // height

        // left
        buttonPixmap.fillRectangle((int)(size / 2 - size / 3),            // x
                (int)(size / 2 - size / 5),    // y
                (int)(size / 5),                       // width
                (int)(size / 3));                      // height


    }
}
