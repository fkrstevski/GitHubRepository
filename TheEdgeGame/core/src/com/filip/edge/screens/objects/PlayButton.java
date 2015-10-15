package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-11.
 */
public class PlayButton extends AbstractCircleButtonObject {
    public static final String TAG = PlayButton.class.getName();

    public PlayButton(float size, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        super(size, x, y, outsideColor, insideColor, shared, region);
    }

    @Override
    public void fillInside(float size) {
        buttonPixmap.fillTriangle((int) (size / 2 - size / 5), (int) (size / 2 - size / 4),
                (int) (size / 2 + size / 3), (int) (size / 2),
                (int) (size / 2 - size / 5), (int) (size / 2 + size / 4)
        );
    }
}
