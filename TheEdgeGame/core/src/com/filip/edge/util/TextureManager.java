package com.filip.edge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by fkrstevski on 2015-10-12.
 */
public class TextureManager {
    public static final String TAG = TextureManager.class.getName();
    public static final TextureManager instance = new TextureManager();

    protected int pixmapCount;
    protected int textureCount;

    public TextureAtlas atlas;
    protected int textureRegion = 0;
    protected Pixmap sharedMiddleCirclePixmap = null;
    protected Pixmap sharedMiddleRectanglePixmap = null;
    protected Pixmap sharedLetterPixmap = null;
    protected Pixmap sharedDigitPixmap = null;

    private TextureManager() {

    }

    public void load() {
        if(atlas == null) {
            atlas = new TextureAtlas();
        }
    }

    public Pixmap getPixmap(float width, float height, boolean shared, String r) {
        Pixmap buttonPixmap = null;
        if(shared) {
            if(r.startsWith("Circle")) {
                if(sharedMiddleCirclePixmap == null) {
                    sharedMiddleCirclePixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA4444);
                    pixmapCount++;
                    Gdx.app.log(TAG, "pixmaps = " + pixmapCount);
                }
                buttonPixmap = sharedMiddleCirclePixmap;
            }
            if(r.equalsIgnoreCase("Rectangle")) {
                if(sharedMiddleRectanglePixmap == null) {
                    sharedMiddleRectanglePixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA4444);
                    pixmapCount++;
                    Gdx.app.log(TAG, "pixmaps = " + pixmapCount);
                }
                buttonPixmap = sharedMiddleRectanglePixmap;
            }
            if(r.startsWith("Letter")) {
                if(sharedLetterPixmap == null) {
                    sharedLetterPixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA4444);
                    pixmapCount++;
                    Gdx.app.log(TAG, "pixmaps = " + pixmapCount);
                }
                buttonPixmap = sharedLetterPixmap;
            }
            if(r.endsWith("Digit")) {
                if(sharedDigitPixmap == null) {
                    sharedDigitPixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA4444);
                    pixmapCount++;
                    Gdx.app.log(TAG, "pixmaps = " + pixmapCount);
                }
                buttonPixmap = sharedDigitPixmap;
            }
        }
        else {
            buttonPixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA4444);
            pixmapCount++;
            Gdx.app.log(TAG, "pixmaps = " + pixmapCount);
        }

        return buttonPixmap;
    }

    public String getTextureRegion(){
        return (textureRegion++) + "";
    }

    public Texture addTextureRegion(String atlasRegion, Pixmap buttonPixmap) {
        if(atlas.findRegion(atlasRegion) == null) {
            atlas.addRegion(atlasRegion, new TextureRegion(new Texture(buttonPixmap)));
            textureCount++;
            Gdx.app.log(TAG, "textures = " + textureCount);
        }

        return atlas.findRegion(atlasRegion).getTexture();
    }
}
