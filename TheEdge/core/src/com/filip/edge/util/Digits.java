package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by fkrstevski on 2015-09-21.
 */

class ZeroDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = ZeroDigit.class.getName();

    public ZeroDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();
        fillTopRightRect();

        fillBottomRect();
        fillBottomLeftRect();
        fillBottomRightRect();

    }
}


class OneDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = OneDigit.class.getName();

    public OneDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRightRect();
        fillBottomRightRect();
    }
}

class TwoDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = TwoDigit.class.getName();

    public TwoDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopRightRect();

        fillMiddleRect();

        fillBottomRect();
        fillBottomLeftRect();

    }
}

class ThreeDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = ThreeDigit.class.getName();

    public ThreeDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopRightRect();

        fillMiddleRect();

        fillBottomRect();
        fillBottomRightRect();

    }
}

class FourDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = FourDigit.class.getName();

    public FourDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopLeftRect();
        fillTopRightRect();

        fillMiddleRect();

        fillBottomRightRect();
    }
}

class FiveDigit extends AbstractRectangleButtonObject
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

class SixDigit extends AbstractRectangleButtonObject
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

class SevenDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = SevenDigit.class.getName();

    public SevenDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopRightRect();

        fillBottomRightRect();
    }
}


class EightDigit extends AbstractRectangleButtonObject
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

class NineDigit extends AbstractRectangleButtonObject
{
    public static final String TAG = NineDigit.class.getName();

    public NineDigit(int width, int height, float x, float y, Color outsideColor, Color insideColor)
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

        fillBottomRightRect();
    }
}