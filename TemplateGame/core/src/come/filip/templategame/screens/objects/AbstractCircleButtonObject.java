package come.filip.templategame.screens.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public abstract class AbstractCircleButtonObject extends AbstractButtonObject {
    public static final String TAG = AbstractButtonObject.class.getName();

    private int radius;

    public AbstractCircleButtonObject (int size, float x, float y, Color outsideColor, Color insideColor) {
        super(size, size, x, y, outsideColor, insideColor);
        init(size, size, x, y, outsideColor, insideColor);
    }

    @Override
    protected void init (int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super.init(width, height, x, y, outsideColor, insideColor);
        // Make the radius slightly smaller than half the size (looks more like a circle)
        radius = width / 2 - 2;
    }

    @Override
    public void fillPixmap(int width, int height, Color outsideColor, Color insideColor)
    {
        buttonPixmap.setColor(outsideColor);
        buttonPixmap.fillCircle(width / 2, width / 2, radius);
        buttonPixmap.setColor(insideColor);

        fillInside(width);
    }

    public abstract void fillInside(int size);

    public boolean isTouched(int x, int y)
    {
        float distance = Vector2.dst(position.x, position.y, x, y);

        Gdx.app.debug(TAG, "x = " + x + " y = " + y);
        Gdx.app.debug(TAG, "position.x = " + position.x + " position.y = " + position.y);
        Gdx.app.debug(TAG, "distance = " + distance + " radius = " + radius);

        if ((distance <= radius))
        {
            return  true;
        }
        return false;
    }

}
