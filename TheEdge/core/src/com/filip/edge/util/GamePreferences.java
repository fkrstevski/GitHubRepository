package com.filip.edge.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences
{

    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public long currentScore;
    public long highestScore;

    public int level;
    public int stage;
    public int zone;
    public boolean useMonochromeShader;

    private Preferences prefs;

    // singleton: prevent instantiation from other classes
    private GamePreferences()
    {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);

        // Clear preferences every time the game runs on the desktop
        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            prefs.clear();
            prefs.flush();
        }
    }

    public void load()
    {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
        useMonochromeShader = prefs.getBoolean("useMonochromeShader", false);
        level = prefs.getInteger("level", 0);
        stage = prefs.getInteger("stage", 0);
        zone = prefs.getInteger("zone", 0);
        currentScore = prefs.getLong("currentScore", Constants.MAX_SCORE);
        highestScore = prefs.getLong("highestScore", Constants.MAX_SCORE);
    }

    public void save()
    {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putBoolean("useMonochromeShader", useMonochromeShader);
        prefs.putInteger("level", level);
        prefs.putInteger("stage", stage);
        prefs.putInteger("zone", zone);
        prefs.putLong("currentScore", currentScore);
        prefs.putLong("highestScore", highestScore);
        prefs.flush();
    }

}
