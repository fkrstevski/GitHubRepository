package come.filip.templategame.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-11.
 */
public class PlayButton extends AbstractCircleButtonObject
{
    public static final String TAG = PlayButton.class.getName();

    public PlayButton (int size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int size)
    {
        buttonPixmap.fillTriangle(size / 2 - size / 5, size / 2 - size / 4,
                size / 2 + size / 3, size / 2,
                size / 2 - size / 5, size / 2 + size / 4
        );
    }
}
