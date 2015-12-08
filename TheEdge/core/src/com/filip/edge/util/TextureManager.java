package com.filip.edge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

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

    protected Map pixmapMap;

    private TextureManager() {

    }

    public void load() {
        if(atlas == null) {
            atlas = new TextureAtlas();
        }

        if(pixmapMap == null) {
            pixmapMap = new HashMap();
        }
    }

    public Pixmap getPixmap(float width, float height, boolean shared, String r) {
        Pixmap buttonPixmap;

        if(pixmapMap.containsKey(r))
        {
            buttonPixmap = (Pixmap)pixmapMap.get(r);
        }
        else {
            buttonPixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA8888);
            pixmapCount++;
            //Gdx.app.log(TAG, r + " pixmaps = " + pixmapCount);
            pixmapMap.put(r, buttonPixmap);
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
            //Gdx.app.log(TAG, atlasRegion + " textures = " + textureCount);
        }

        return atlas.findRegion(atlasRegion).getTexture();
    }

}
