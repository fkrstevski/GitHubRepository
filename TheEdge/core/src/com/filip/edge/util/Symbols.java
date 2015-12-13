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