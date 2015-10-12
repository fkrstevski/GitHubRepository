package com.filip.edge.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractCircleButtonObject;


public class EmptyCircle extends AbstractCircleButtonObject {

    public static final String TAG = EmptyCircle.class.getName();

    public EmptyCircle(float size, float x, float y, Color outsideColor, Color insideColor, boolean shared) {
        super(size, x, y, outsideColor, insideColor, shared);
    }

    @Override
    public void fillInside(float size) {

    }
}
