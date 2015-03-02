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
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 5;

        // top
        buttonPixmap.fillRectangle(0, height - digitPartWidth, width, digitPartWidth);

        // top left
        buttonPixmap.fillRectangle(0, height / 2 , digitPartWidth, height / 2);

        // top right
        buttonPixmap.fillRectangle(width - digitPartWidth, height / 2 , digitPartWidth, height / 2);

        // middle
        buttonPixmap.fillRectangle(0, height / 2 - digitPartWidth / 2, width, digitPartWidth);


        // bottom
        buttonPixmap.fillRectangle(0, 0, width, digitPartWidth);

        // bottom left
        buttonPixmap.fillRectangle(0, 0, digitPartWidth, height / 2);

        // bottom right
        buttonPixmap.fillRectangle(width - digitPartWidth, 0, digitPartWidth, height / 2);
    }
}