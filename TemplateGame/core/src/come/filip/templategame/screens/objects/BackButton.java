package come.filip.templategame.screens.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fkrstevski on 2015-02-13.
 */
public class BackButton extends AbstractCircleButtonObject
{
    public static final String TAG = BackButton.class.getName();

    public BackButton (int size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int size)
    {
        /*buttonPixmap.fillTriangle(size / 2 - size / 5, size / 2 - size / 4,
                size / 2 + size / 3, size / 2,
                size / 2 - size / 5, size / 2 + size / 4
        );*/
        buttonPixmap.fillTriangle(  size / 2 + size / 5, size / 2 - size / 4,
                                    size / 2 + size / 5,size / 2 + size / 4,
                                    size / 2 - size / 3,size / 2);

    }
}