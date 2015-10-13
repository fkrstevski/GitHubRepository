package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filip.edge.game.objects.AbstractGameObject;
import com.filip.edge.util.TextureManager;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public abstract class AbstractButtonObject extends AbstractGameObject {
    public static final String TAG = AbstractButtonObject.class.getName();

    protected Pixmap buttonPixmap = null;
    protected Texture texture = null;
    protected String atlasRegion;

    public AbstractButtonObject(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        init(width, height, x, y, outsideColor, insideColor, shared, region);
    }

    protected void init(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared, String r) {
        dimension.set(width, height);
        position.set(x, y);

        buttonPixmap = TextureManager.instance.getPixmap(width, height, shared, r);

        this.atlasRegion = (r.length() > 0 ? r : TextureManager.instance.getTextureRegion());

        fillPixmap(width, height, outsideColor, insideColor);

        texture = TextureManager.instance.addTextureRegion(this.atlasRegion, this.buttonPixmap);

        if (!shared) {
            buttonPixmap.dispose();
            buttonPixmap = null;
        }

        origin.set(dimension.x / 2, dimension.y / 2);
    }

    public abstract void fillPixmap(float width, float height, Color outsideColor, Color insideColor);

    public abstract void fillInside(float size);

    public void render(SpriteBatch batch) {
        if (visible) {
            batch.draw(texture,
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
