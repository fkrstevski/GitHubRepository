package come.filip.templategame.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class MiddlePart extends AbstractRectangleButtonObject
{
    public static final String TAG = StartTarget.class.getName();

    public MiddlePart (int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int size)
    {

    }
}