package come.filip.templategame.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import come.filip.templategame.game.objects.AbstractGameObject;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public abstract class AbstractButtonObject extends AbstractGameObject {
    public static final String TAG = AbstractButtonObject.class.getName();

    protected Texture pixmapTexture = null;
    protected Pixmap buttonPixmap = null;

    public AbstractButtonObject (int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        init(width, height, x, y, outsideColor, insideColor);
    }

    protected void init (int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        dimension.set(width, height);
        position.set(x, y);

        buttonPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        fillPixmap(width, height, outsideColor, insideColor);

        this.pixmapTexture = new Texture(buttonPixmap, Pixmap.Format.RGBA8888, false);
        pixmapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        origin.set(dimension.x / 2, dimension.y / 2);
    }

    public abstract void fillPixmap(int width, int height, Color outsideColor, Color insideColor);
    public abstract void fillInside(int size);

    public void render (SpriteBatch batch) {
        Texture tex = null;

        tex = pixmapTexture;
        batch.draw(tex,
                position.x - origin.x,
                position.y - origin.y,
                origin.x,
                origin.y,
                dimension.x,
                dimension.y,
                scale.x,
                scale.y,
                rotation,
                0,
                0,
                (int)dimension.x,
                (int)dimension.y,
                false,
                false);
    }

    public abstract boolean isTouched(int x, int y);
}
