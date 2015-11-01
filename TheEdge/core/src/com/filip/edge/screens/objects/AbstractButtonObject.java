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
    protected int direction = 1;

    public AbstractButtonObject(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        init(width, height, x, y, outsideColor, insideColor, shared, region);
    }

    // Create a texture named r (eg. CirclePlay0)
    // Using pixmap named r-1 (eg. CirclePlay)
    // used to share pixmaps of objects that are the same size
    protected void init(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared, String r) {
        dimension.set(width, height);
        position.set(x, y);

        buttonPixmap = TextureManager.instance.getPixmap(width, height, shared, r.substring(0, r.length()-1));

        fillPixmap(width, height, outsideColor, insideColor);

        texture = TextureManager.instance.addTextureRegion(r, this.buttonPixmap);

        // TODO: Look into memory issues if we are not disposing this
        //if (!shared) {
        //    buttonPixmap.dispose();
        //    buttonPixmap = null;
        //}

        origin.set(dimension.x / 2, dimension.y / 2);
    }

    public abstract void fillPixmap(float width, float height, Color outsideColor, Color insideColor);

    public abstract void fillInside(float size);

    public void render(SpriteBatch batch) {
        if (visible) {
            if(this.body != null) {
                if (this.body.getLinearVelocity().x < -1) {
                    direction = -1;
                }
                else if (this.body.getLinearVelocity().x > 1) {
                    direction = 1;
                }
            }

            batch.draw(texture,
                    position.x - origin.x,
                    position.y - origin.y,
                    origin.x,
                    origin.y,
                    dimension.x,
                    dimension.y,
                    scale.x * direction,
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

    public abstract void reset();

    @Override
    public String toString() {
        return super.toString() + "AbstractButtonObject{" +
                "buttonPixmap=" + buttonPixmap +
                ", texture=" + texture +
                '}';
    }
}
