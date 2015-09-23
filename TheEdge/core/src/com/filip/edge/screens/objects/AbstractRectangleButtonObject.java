package com.filip.edge.screens.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.filip.edge.util.Constants;

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

    public void fillCenterTop()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(width / 2 - digitPartWidth / 2, height / 2, digitPartWidth, height / 2);
    }
    public void fillCenterBottom()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(width / 2 - digitPartWidth / 2, 0, digitPartWidth, height / 2);
    }

    public void fillTopRect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(0, height - digitPartWidth, width, digitPartWidth);
    }

    public void fillTopLeftRect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(0, height / 2 , digitPartWidth, height / 2);
    }

    public void fillTopRightRect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(width - digitPartWidth, height / 2 , digitPartWidth, height / 2);
    }

    public void fillMiddleRect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(0, height / 2 - digitPartWidth / 2, width, digitPartWidth);
    }

    public void fillTopRight80Rect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(width - digitPartWidth,
                (int)(height * (Constants.DIGIT_WIDTH_CELLS / (float)Constants.DIGIT_HEIGHT_CELLS)) ,
                digitPartWidth,
                (int)(height * ((Constants.DIGIT_WIDTH_CELLS - 1) / (float)Constants.DIGIT_HEIGHT_CELLS)));
    }

    public void fillTopLeft80Rect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(0,
                (int)(height * (Constants.DIGIT_WIDTH_CELLS / (float)Constants.DIGIT_HEIGHT_CELLS)) ,
                digitPartWidth,
                (int)(height * ((Constants.DIGIT_WIDTH_CELLS - 1) / (float)Constants.DIGIT_HEIGHT_CELLS)));
    }

    public void fillBottomRight80Rect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(width - digitPartWidth,
                0,
                digitPartWidth,
                (int)(height * ((Constants.DIGIT_WIDTH_CELLS - 1) / (float)Constants.DIGIT_HEIGHT_CELLS)));
    }

    public void fillBottomLeft80Rect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(0,
                0 ,
                digitPartWidth,
                (int)(height * ((Constants.DIGIT_WIDTH_CELLS - 1) / (float)Constants.DIGIT_HEIGHT_CELLS)));
    }

    public void fillMiddle60Rect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle((int)(width * 0.2),
                height / 2 - digitPartWidth / 2,
                (int)(width * 0.6),
                digitPartWidth);
    }

    public void fillBottomRect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(0, 0, width, digitPartWidth);
    }

    public void fillBottomLeftRect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(0, 0, digitPartWidth, height / 2);
    }

    public void fillBottomRightRect()
    {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / 3;
        buttonPixmap.fillRectangle(width - digitPartWidth, 0, digitPartWidth, height / 2);
    }
}