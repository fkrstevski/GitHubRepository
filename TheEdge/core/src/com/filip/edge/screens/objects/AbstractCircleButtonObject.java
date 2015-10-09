package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public abstract class AbstractCircleButtonObject extends AbstractButtonObject {
    public static final String TAG = AbstractButtonObject.class.getName();

    public float radius;

    public Circle bounds;

    public AbstractCircleButtonObject(float size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, size, x, y, outsideColor, insideColor);

        this.init(size, size, x, y, outsideColor, insideColor);
    }

    @Override
    protected void init(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super.init(width, height, x, y, outsideColor, insideColor);

        radius = width / 2;
        bounds = new Circle();
        bounds.set(position.x, position.y, radius);
    }

    public void updateBoundsRadius(int radius) {
        this.radius = radius;
        bounds.set(position, radius);
    }

    @Override
    public void fillPixmap(float width, float height, Color outsideColor, Color insideColor) {
        buttonPixmap.setColor(outsideColor);
        buttonPixmap.fillCircle((int) (width / 2), (int) (width / 2), (int) radius);
        buttonPixmap.setColor(insideColor);

        fillInside(width);
    }

    public abstract void fillInside(float size);

    public boolean isTouched(int x, int y) {
        float distance = Vector2.dst(position.x, position.y, x, y);

        if ((distance <= radius)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof AbstractCircleButtonObject) {
            AbstractCircleButtonObject point = ((AbstractCircleButtonObject) object);
            sameSame = bounds.equals(point.bounds);
        }

        return sameSame;
    }

}
