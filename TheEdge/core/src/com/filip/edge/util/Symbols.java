package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by fkrstevski on 2015-12-12.
 */
class Period extends AbstractRectangleButtonObject {
    public static final String TAG = Period.class.getSimpleName();

    public Period(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillBottomCenterThirdRect(Constants.WIDTH_IN_PIXELS);
    }
}

class Plus extends AbstractRectangleButtonObject {
    public static final String TAG = Plus.class.getSimpleName();

    public Plus(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillCenterTop80(Constants.WIDTH_IN_PIXELS);
        fillCenterBottom80(Constants.WIDTH_IN_PIXELS);
    }
}

class Minus extends AbstractRectangleButtonObject {
    public static final String TAG = Minus.class.getSimpleName();

    public Minus(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
    }
}