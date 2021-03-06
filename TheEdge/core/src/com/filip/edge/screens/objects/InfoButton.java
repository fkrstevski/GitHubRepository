package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class InfoButton extends AbstractCircleButtonObject {
    public static final String TAG = InfoButton.class.getName();

    public InfoButton(float size, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        super(size, x, y, outsideColor, insideColor, shared, region);
    }

    @Override
    public void fillInside(float size) {
        buttonPixmap.fillRectangle((int) (size / 2 - size / 10),
                (int) (size / 2 - (size / 2.5)),
                (int) (size / 5),
                (int) (size / 2));
        buttonPixmap.fillRectangle((int) (size / 2 - size / 10),
                (int) (size / 2 + size / 5),
                (int) (size / 5),
                (int) (size / 5));
    }
}
