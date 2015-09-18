package com.filip.edge.screens.objects.digits;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;
import com.filip.edge.util.Constants;

/**
 * Created by FILIP on 3/1/2015.
 */
public class EightDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = EightDigit.class.getName();

    public EightDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();
        fillTopRightRect();

        fillMiddleRect();

        fillBottomRect();
        fillBottomLeftRect();
        fillBottomRightRect();

    }
}