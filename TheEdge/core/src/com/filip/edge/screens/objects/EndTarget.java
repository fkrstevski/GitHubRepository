package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.util.Constants;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class EndTarget extends AbstractCircleButtonObject {
    public static final String TAG = EndTarget.class.getName();

    public EndTarget(int size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int size) {
        buttonPixmap.fillCircle(size / 2, size / 2, (int) (size / 2 * Constants.END_CIRCLE_OUTLINE_RADIUS_MULTIPLIER));
    }
}