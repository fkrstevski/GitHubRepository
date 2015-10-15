package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class MiddlePart extends AbstractRectangleButtonObject {
    public static final String TAG = MiddlePart.class.getName();

    public MiddlePart(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        super(width, height, x, y, outsideColor, insideColor, shared, region);
    }

    @Override
    public void fillInside(float size) {

    }
}