package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class InfoButton extends AbstractCircleButtonObject
{
    public static final String TAG = InfoButton.class.getName();

    public InfoButton(int size, float x, float y, Color outsideColor, Color insideColor)
    {
        super(size, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int size)
    {
        buttonPixmap.fillRectangle(size / 2 - size / 10,
                size / 2 - (int) (size / 2.5),
                size / 5,
                size / 2);
        buttonPixmap.fillRectangle(size / 2 - size / 10,
                size / 2 + size / 5,
                size / 5,
                size / 5);
    }
}
