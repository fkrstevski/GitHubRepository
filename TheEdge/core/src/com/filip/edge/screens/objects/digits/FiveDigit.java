package com.filip.edge.screens.objects.digits;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by FILIP on 3/1/2015.
 */
public class FiveDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = FiveDigit.class.getName();

    public FiveDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
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
        fillBottomRightRect();

    }
}