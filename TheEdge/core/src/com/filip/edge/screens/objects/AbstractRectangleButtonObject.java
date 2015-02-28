package com.filip.edge.screens.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public abstract class AbstractRectangleButtonObject extends AbstractButtonObject
{
    public static final String TAG = AbstractButtonObject.class.getName();

    public Rectangle bounds;

    public AbstractRectangleButtonObject(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);

        init(width, height, x, y, outsideColor, insideColor);

    }

    @Override
    protected void init(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super.init(width, height, x, y, outsideColor, insideColor);
        bounds = new Rectangle();
        bounds.set(position.x - origin.x, position.y - origin.y, dimension.x, dimension.y);

    }

    @Override
    public void fillPixmap(int width, int height, Color outsideColor, Color insideColor)
    {
        buttonPixmap.setColor(outsideColor);
        buttonPixmap.fill();
        buttonPixmap.setColor(insideColor);

        fillInside(width);
    }

    public abstract void fillInside(int size);

    public boolean isTouched(int x, int y)
    {
        Gdx.app.debug(TAG, "position.x = " + position.x + " position.y = " + position.y);
        Gdx.app.debug(TAG, "bounds = " + bounds);

        if (this.bounds.contains(x, y))
        {
            return true;
        }
        return false;
    }

}