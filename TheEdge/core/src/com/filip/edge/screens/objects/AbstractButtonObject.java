package com.filip.edge.screens.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.game.objects.AbstractGameObject;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public abstract class AbstractButtonObject extends AbstractGameObject {
    public static final String TAG = AbstractButtonObject.class.getName();

    protected Texture pixmapTexture = null;
    protected Pixmap buttonPixmap = null;

    protected static Pixmap sharedButtonPixmap = null;
    protected static Texture sharedPixmapTexture = null;

    public AbstractButtonObject(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared) {
        init(width, height, x, y, outsideColor, insideColor, shared);
    }

    protected void init(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared) {
        dimension.set(width, height);
        position.set(x, y);

        if(shared) {
            if(sharedButtonPixmap == null) {
                sharedButtonPixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA4444);
            }
            buttonPixmap = sharedButtonPixmap;
        }
        else {
            buttonPixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA4444);
        }


        if(shared) {
            if(sharedPixmapTexture == null) {
                fillPixmap(width, height, outsideColor, insideColor);
                sharedPixmapTexture = new Texture(buttonPixmap, Pixmap.Format.RGBA4444, false);
            }
            pixmapTexture = sharedPixmapTexture;

        } else {
            fillPixmap(width, height, outsideColor, insideColor);
            this.pixmapTexture = new Texture(buttonPixmap, Pixmap.Format.RGBA4444, false);
        }

        pixmapTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        origin.set(dimension.x / 2, dimension.y / 2);
    }

    public abstract void fillPixmap(float width, float height, Color outsideColor, Color insideColor);

    public abstract void fillInside(float size);

    public void render(SpriteBatch batch) {
        if (visible) {
            batch.draw(pixmapTexture,
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
                    (int) dimension.x,
                    (int) dimension.y,
                    false,
                    false);
        }
    }

    public abstract boolean isTouched(int x, int y);
}
