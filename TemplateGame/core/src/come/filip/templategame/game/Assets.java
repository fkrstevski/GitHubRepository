/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package come.filip.templategame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

import come.filip.templategame.util.Constants;

public class Assets implements Disposable, AssetErrorListener
{

    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();
    public AssetFonts fonts;
    public AssetSounds sounds;
    public AssetMusic music;
    private AssetManager assetManager;

    // singleton: prevent instantiation from other classes
    private Assets()
    {
    }

    public void init(AssetManager assetManager)
    {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // load sounds
        assetManager.load("sounds/jump.wav", Sound.class);
        assetManager.load("sounds/jump_with_feather.wav", Sound.class);
        assetManager.load("sounds/pickup_coin.wav", Sound.class);
        assetManager.load("sounds/pickup_feather.wav", Sound.class);
        assetManager.load("sounds/live_lost.wav", Sound.class);
        // load music
        assetManager.load("music/keith303_-_brand_new_highscore.mp3",
                Music.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG,
                "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
        {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures())
        {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        // create game resource objects
        fonts = new AssetFonts();
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    @Override
    public void dispose()
    {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable)
    {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'",
                (Exception) throwable);

    }

    public class AssetFonts
    {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts()
        {
            // create three fonts using Libgdx's built-in 15px bitmap font
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(
                    Gdx.files.internal("images/arial-15.fnt"), true);
            // set font sizes
            defaultSmall.setScale(0.75f);
            defaultNormal.setScale(1.0f);
            defaultBig.setScale(2.0f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture()
                    .setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture()
                    .setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture()
                    .setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    public class AssetSounds
    {
        public final Sound jump;
        public final Sound jumpWithFeather;
        public final Sound pickupCoin;
        public final Sound pickupFeather;
        public final Sound liveLost;

        public AssetSounds(AssetManager am)
        {
            jump = am.get("sounds/jump.wav", Sound.class);
            jumpWithFeather = am.get("sounds/jump_with_feather.wav",
                    Sound.class);
            pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
            pickupFeather = am.get("sounds/pickup_feather.wav", Sound.class);
            liveLost = am.get("sounds/live_lost.wav", Sound.class);
        }
    }

    public class AssetMusic
    {
        public final Music song01;

        public AssetMusic(AssetManager am)
        {
            song01 = am.get("music/keith303_-_brand_new_highscore.mp3",
                    Music.class);
        }
    }

}
