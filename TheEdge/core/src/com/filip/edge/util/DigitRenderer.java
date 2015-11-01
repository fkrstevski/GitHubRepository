package com.filip.edge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

import java.util.ArrayList;

/**
 * Created by FILIP on 3/1/2015.
 */
public class DigitRenderer {
    public static final String TAG = DigitRenderer.class.getName();

    public static final DigitRenderer instance = new DigitRenderer();

    public ArrayList<AbstractRectangleButtonObject> digits;
    public ArrayList<AbstractRectangleButtonObject> letters;

    public int digitWidth;
    public int digitHeight;
    private static final float EXTRA_SPACING = 1.1f;

    private DigitRenderer() {

    }

    public void load() {
        int height = Gdx.graphics.getHeight();
        digits = new ArrayList<AbstractRectangleButtonObject>();
        letters = new ArrayList<AbstractRectangleButtonObject>();

        digitHeight = (int) (Gdx.graphics.getWidth() * 0.054);
        digitWidth = (int) (digitHeight / Constants.DIGIT_ASPECT_RATIO);

        digits.add(new Digit0(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit1(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit2(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit3(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit4(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit5(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit6(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit7(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit8(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        digits.add(new Digit9(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));

        letters.add(new LetterA(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterB(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterC(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterD(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterE(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterF(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterG(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterH(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterI(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterJ(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterK(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterL(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterM(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterN(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterO(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterP(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterQ(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterR(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterS(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterT(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterU(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterV(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterW(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterX(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterY(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));
        letters.add(new LetterZ(digitWidth, digitHeight, 0, 0, Constants.TRANSPARENT, Constants.WHITE));

    }

    public void renderNumber(long number, int x, int y, SpriteBatch batch) {
        int count = 0;
        while (number > 0){
            int digit = (int)(number % 10);
            AbstractRectangleButtonObject digitObject = digits.get(digit);
            digitObject.position.set(x - count * digitWidth * EXTRA_SPACING, y);
            digitObject.render(batch);
            number /= 10;
            ++count;
        }
    }

    // Less memory efficient version to render a string as a number, so that
    // the buildup sequence can render correctly.
    public void renderNumber(String number, int x, int y, SpriteBatch batch) {
        int count = 0;
        for (int i = number.length() - 1; i >= 0; --i, ++count) {
            int digit = Character.getNumericValue(number.charAt(i));
            AbstractRectangleButtonObject digitObject = digits.get(digit);
            digitObject.position.set(x - count * digitWidth * EXTRA_SPACING, y);
            digitObject.render(batch);
        }
    }

    public void renderString(String str, int x, int y, SpriteBatch batch) {
        renderString(str, x, y, batch, 1);
    }

    public void renderString(String str, int x, int y, SpriteBatch batch, float scale) {
        int count = 0;
        for (int i = str.length() - 1; i >= 0; --i, ++count) {
            // not space
            if (str.charAt(i) != ' ') {
                int index = str.charAt(i) - 'A';
                AbstractRectangleButtonObject digitObject = letters.get(index);
                digitObject.position.set(x - count * digitWidth * EXTRA_SPACING * scale, y);
                digitObject.scale.set(scale, scale);
                digitObject.render(batch);
            }
        }
    }

    public void renderStringAtCenterXPoint(String str, int x, int y, SpriteBatch batch, float scale) {
        this.renderString(str, (int)(x - digitWidth * 0.5f +  str.length() * digitWidth * EXTRA_SPACING * 0.5f * scale), y, batch, scale);
    }


    public void renderStringCentered(String str, int y, SpriteBatch batch) {
        this.renderString(str, (int)(Gdx.graphics.getWidth() * 0.5f - digitWidth * 0.5f +  str.length() * digitWidth * EXTRA_SPACING * 0.5f), y, batch);
    }
}
