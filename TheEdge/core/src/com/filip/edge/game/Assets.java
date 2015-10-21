/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.filip.edge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();
    public AssetFonts fonts;
    public AssetSounds sounds;
    public AssetMusic music;
    private AssetManager assetManager;

    // singleton: prevent instantiation from other classes
    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load sounds
        assetManager.load("sounds/TickSound.wav", Sound.class);
        assetManager.load("sounds/live_lost.wav", Sound.class);
        // load music
        assetManager.load("music/keith303_-_brand_new_highscore.mp3", Music.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        // create game resource objects
        fonts = new AssetFonts();
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/UnscreenMK.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 20;
            parameter.flip = true;
            BitmapFont font12 = generator.generateFont(parameter);
            parameter.size = 30;
            BitmapFont font16 = generator.generateFont(parameter);
            parameter.size = 40;
            BitmapFont font20 = generator.generateFont(parameter);
            generator.dispose(); // don't forget to dispose to avoid memory leaks!

            defaultSmall = font12;
            defaultNormal = font16;
            defaultBig = font20;
        }
    }

    public class AssetSounds {
        public final Sound tickSound;
        public final Sound liveLost;

        public AssetSounds(AssetManager am) {
            tickSound = am.get("sounds/TickSound.wav", Sound.class);
            liveLost = am.get("sounds/live_lost.wav", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music song01;

        public AssetMusic(AssetManager am) {
            song01 = am.get("music/keith303_-_brand_new_highscore.mp3", Music.class);
        }
    }

}
