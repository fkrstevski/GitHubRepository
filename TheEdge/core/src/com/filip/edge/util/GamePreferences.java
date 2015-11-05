package com.filip.edge.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.net.HttpParametersUtils;

import java.util.HashMap;
import java.util.Map;

public class GamePreferences {

    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public boolean music;
    public boolean scoreNeedsToBeSubmitted;
    public float volSound;
    public float volMusic;
    public long currentScore;
    public int numberOfDeaths;
    public String userID;
    public String email;


    public int level;
    public int stage;
    public int zone;

    private Preferences prefs;

    // singleton: prevent instantiation from other classes
    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);

        // Clear preferences every time the game runs on the desktop
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            prefs.clear();
            prefs.flush();
        }
    }

    public void load() {
        numberOfDeaths = prefs.getInteger("numberOfDeaths", 0);
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        scoreNeedsToBeSubmitted = prefs.getBoolean("scoreNeedsToBeSubmitted", false);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
        level = prefs.getInteger("level", 0);
        stage = prefs.getInteger("stage", 0);
        zone = prefs.getInteger("zone", 0);
        userID = prefs.getString("userID", "");
        email = prefs.getString("email", "");
        currentScore = prefs.getLong("currentScore", Constants.MAX_SCORE);
    }

    public void save() {
        prefs.putInteger("numberOfDeaths", numberOfDeaths);
        prefs.putString("userID", userID);
        prefs.putString("email", email);
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putBoolean("scoreNeedsToBeSubmitted", scoreNeedsToBeSubmitted);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("level", level);
        prefs.putInteger("stage", stage);
        prefs.putInteger("zone", zone);
        prefs.putLong("currentScore", currentScore);
        prefs.flush();
    }

    public void getUserID() {
        if(userID.isEmpty()) {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("data", "data from game");
            Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
            request.setUrl("http://www.absolutegames.ca/userID.php");

            request.setContent(HttpParametersUtils.convertHttpParameters(parameters));
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

            Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    Gdx.app.log("Status code ", "" + httpResponse.getStatus().getStatusCode());
                    userID = httpResponse.getResultAsString();
                    Gdx.app.log("UserID", userID);
                    save();
                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.error("Failed ", t.getMessage());
                }

                @Override
                public void cancelled() {

                }
            });
        }
    }
}
