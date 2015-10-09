package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-13.
 */
public class BackButton extends AbstractCircleButtonObject {
    public static final String TAG = BackButton.class.getName();

    public BackButton(float size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(float size) {
        buttonPixmap.fillTriangle((int)(size / 2 + size / 5), (int)(size / 2 - size / 4),
                (int)(size / 2 + size / 5), (int)(size / 2 + size / 4),
                (int)(size / 2 - size / 3), (int)(size / 2));

    }
}