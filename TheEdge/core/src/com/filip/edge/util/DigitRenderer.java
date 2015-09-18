package com.filip.edge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;
import com.filip.edge.screens.objects.digits.EightDigit;
import com.filip.edge.screens.objects.digits.FiveDigit;
import com.filip.edge.screens.objects.digits.FourDigit;
import com.filip.edge.screens.objects.digits.NineDigit;
import com.filip.edge.screens.objects.digits.OneDigit;
import com.filip.edge.screens.objects.digits.SevenDigit;
import com.filip.edge.screens.objects.digits.SixDigit;
import com.filip.edge.screens.objects.digits.ThreeDigit;
import com.filip.edge.screens.objects.digits.TwoDigit;
import com.filip.edge.screens.objects.digits.ZeroDigit;

import java.util.ArrayList;

/**
 * Created by FILIP on 3/1/2015.
 */
public class DigitRenderer
{
    public static final String TAG = DigitRenderer.class.getName();

    public static final DigitRenderer instance = new DigitRenderer();

    public ArrayList<AbstractRectangleButtonObject> digits;

    private int digitWidth;
    private int digitHeight;

    private DigitRenderer()
    {

    }

    public void load()
    {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        digits = new ArrayList<AbstractRectangleButtonObject>();

        //digitWidth = (int) (width * 0.03f);
        //digitHeight = (int) (digitWidth * Constants.DIGIT_ASPECT_RATIO);

        digitHeight = (int) (height * 0.096);
        digitWidth = (int) (digitHeight / Constants.DIGIT_ASPECT_RATIO);

        Gdx.app.debug(TAG, "width = " + width);
        Gdx.app.debug(TAG, "height = " + height);
        Gdx.app.debug(TAG, "digitWidth = " + digitWidth);
        Gdx.app.debug(TAG, "digitHeight = " + digitHeight);


        digits.add(new ZeroDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new OneDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new TwoDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new ThreeDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new FourDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new FiveDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new SixDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new SevenDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new EightDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new NineDigit(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
    }

    public void renderNumber(String number, int x, int y, SpriteBatch batch)
    {
        int count = 0;
        for (int i = number.length() - 1; i >= 0; --i, ++count) {
            int digit = Character.getNumericValue(number.charAt(i));
            AbstractRectangleButtonObject digitObject = digits.get(digit);
            digitObject.position.set(x - count * digitWidth * 1.1f, y);
            digitObject.render(batch);
        }
    }
}
