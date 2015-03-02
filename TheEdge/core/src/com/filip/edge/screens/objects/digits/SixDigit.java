package com.filip.edge.screens.objects.digits;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by FILIP on 3/1/2015.
 */
public class SixDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = SixDigit.class.getName();

    public SixDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();

        fillMiddleRect();

        fillBottomRect();
        fillBottomLeftRect();
        fillBottomRightRect();

    }
}