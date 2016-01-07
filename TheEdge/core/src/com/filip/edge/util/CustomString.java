package com.filip.edge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.game.StageLoader;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

import java.util.ArrayList;

/**
 * Created by filip on 2016-01-06.
 */
public class CustomString {

    public ArrayList<MyChar> characters;

    // Creates a custom string with center in x
    public CustomString(String chars, int y, float scale){
        this(chars, Gdx.graphics.getWidth() * 0.5f - DigitRenderer.instance.digitWidth * 0.5f * scale + chars.length() * DigitRenderer.instance.digitWidth * DigitRenderer.instance.EXTRA_SPACING * 0.5f * scale, y, scale);
    }

    public CustomString(String chars, float x, float y, float scale){
        characters = new ArrayList<MyChar>();

        int count = 0;
        for (int i = chars.length() - 1; i >= 0; --i, ++count) {
            // not space
            if (chars.charAt(i) != ' ') {
                AbstractRectangleButtonObject digitObject;
                if (StageLoader.isInteger(chars.charAt(i) + "")) { // TODO memory
                    int digit = Character.getNumericValue(chars.charAt(i));
                    digitObject = DigitRenderer.instance.digits.get(digit);
                }
                else if (chars.charAt(i) == '.') {
                    digitObject = DigitRenderer.instance.symbols.get(0);
                }
                else if (chars.charAt(i) == '+') {
                    digitObject = DigitRenderer.instance.symbols.get(1);
                }
                else if (chars.charAt(i) == '-') {
                    digitObject = DigitRenderer.instance.symbols.get(2);
                }
                else {
                    int index = chars.charAt(i) - 'A';
                    digitObject = DigitRenderer.instance.letters.get(index);
                }

                characters.add(
                        new MyChar(digitObject,
                                   x - count * DigitRenderer.instance.digitWidth * DigitRenderer.instance.EXTRA_SPACING * scale,
                                   y,
                                   scale)
                );
            }
        }
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < characters.size(); ++i){
            characters.get(i).render(batch);
        }
    }
}

class MyChar{
    public AbstractRectangleButtonObject character;
    public float xPos, yPos;
    public float scale;

    MyChar(AbstractRectangleButtonObject c, float x, float y, float s){
        this.character = c;
        this.xPos = x;
        this.yPos = y;
        this.scale = s;
    }

    public void render(SpriteBatch batch){
        character.position.set(xPos, yPos);
        character.scale.set(scale, scale);
        character.render(batch);
    }
}