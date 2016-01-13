package com.filip.edge.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.filip.edge.game.StageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamePreferences {

    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();

    public enum AdType {
        NONE(0), ADMOB(1);
        private final int value;

        private AdType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // These are saved in the preferences
    // ---------------BEGIN----------------
    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public String userID;
    public String email;
    public boolean showingLevelResults;
    public boolean completedLevelTweet;
    public boolean completedLevelVideoReward;
    // these need to be reset when a score is successfully submitted or game is over
    public boolean scoreNeedsToBeSubmitted;
    public long currentScore;
    public String tries;
    public String times;
    public int level;
    public int stage;
    public int zone;
    public int adsSuccessfullyWatched;
    public int tweetsMade;
    // ---------------END----------------

    // Used to help store info, need to be reset
    public List<Integer> levelTries;
    public List<Integer> levelTimes;

    private Preferences prefs;

    public int playSessionDeaths;

    // singleton: prevent instantiation from other classes
    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        levelTries = new ArrayList<Integer>();
        levelTimes = new ArrayList<Integer>();

        // Clear preferences every time the game runs on the desktop
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            prefs.clear();
            prefs.flush();
        }

        playSessionDeaths = 0;
    }

    public void reset() {
        scoreNeedsToBeSubmitted = false;
        currentScore = Constants.MAX_SCORE;
        tries = "";
        times = "";
        level = 0;
        stage = 0;
        zone = 0;
        levelTries.clear();
        levelTimes.clear();
        tweetsMade = 0;
        adsSuccessfullyWatched = 0;
    }

    public void load() {
        sound = prefs.getBoolean("sound", false);
        music = prefs.getBoolean("music", false);
        scoreNeedsToBeSubmitted = prefs.getBoolean("scoreNeedsToBeSubmitted", false);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
        level = prefs.getInteger("level", 0);
        stage = prefs.getInteger("stage", 0);
        zone = prefs.getInteger("zone", 0);
        userID = prefs.getString("userID", "");
        email = prefs.getString("email", "");
        tries = prefs.getString("tries", "");
        times = prefs.getString("times", "");
        adsSuccessfullyWatched = prefs.getInteger("ads", 0);
        tweetsMade = prefs.getInteger("tweets", 0);
        showingLevelResults = prefs.getBoolean("lr", false);
        completedLevelTweet = prefs.getBoolean("tweet", false);
        completedLevelVideoReward = prefs.getBoolean("video", false);

        if (!tries.isEmpty()) {
            String[] triesPerLevelArray = tries.split(",");
            for (int x = 0; x < triesPerLevelArray.length; ++x) {
                levelTries.add(Integer.parseInt(triesPerLevelArray[x]));
            }
        }

        if (!times.isEmpty()) {
            String[] timePerLevelArray = times.split(",");
            for (int x = 0; x < timePerLevelArray.length; ++x) {
                levelTimes.add(Integer.parseInt(timePerLevelArray[x]));
            }
        }

        currentScore = prefs.getLong("currentScore", Constants.MAX_SCORE);
    }

    public void save() {
        prefs.putString("userID", userID);
        prefs.putString("email", email);

        String levelTriesAsString = levelTries.toString().replaceAll(" ", "");

        tries = levelTriesAsString.substring(1, levelTriesAsString.length() - 1);

        prefs.putString("tries", tries);

        String levelTimesAsString = levelTimes.toString().replaceAll(" ", "");

        times = levelTimesAsString.substring(1, levelTimesAsString.length() - 1);

        prefs.putString("times", times);

        prefs.putBoolean("lr", showingLevelResults);
        prefs.putBoolean("tweet", completedLevelTweet);
        prefs.putBoolean("video", completedLevelVideoReward);

        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putBoolean("scoreNeedsToBeSubmitted", scoreNeedsToBeSubmitted);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("level", level);
        prefs.putInteger("stage", stage);
        prefs.putInteger("zone", zone);
        prefs.putInteger("ads", adsSuccessfullyWatched);
        prefs.putInteger("tweets", tweetsMade);
        prefs.putLong("currentScore", currentScore);
        prefs.flush();
    }

    public void submitGameOverData() {
        GamePreferences.instance.getUserID();
        if (!GamePreferences.instance.userID.isEmpty()) {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("userID", "" + GamePreferences.instance.userID);
            parameters.put("tries", GamePreferences.instance.tries);
            parameters.put("times", GamePreferences.instance.times);
            parameters.put("tweets", "" + GamePreferences.instance.tweetsMade);
            parameters.put("ads", "" + GamePreferences.instance.adsSuccessfullyWatched);
            parameters.put("extraData", "data from game");
            parameters.put("version", "" + Constants.GAME_VERSION);
            parameters.put("isProduction", "" + Constants.PRODUCTION);
            Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
            request.setUrl("https://secure.bluehost.com/~alimalim/absolutegames/TheEdgeSubmitGameOverData.php");

            request.setContent(HttpParametersUtils.convertHttpParameters(parameters));
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

            Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {

                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.error("GamePreferences submitGameOverData sendHttpRequest Failed", t.getMessage());
                }

                @Override
                public void cancelled() {
                }
            });
        }
    }

    public void submitData() {
        GamePreferences.instance.getUserID();
        if (!GamePreferences.instance.userID.isEmpty()) {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("userID", "" + GamePreferences.instance.userID);
            parameters.put("score", "" + GamePreferences.instance.currentScore);
            parameters.put("tries", GamePreferences.instance.tries);
            parameters.put("times", GamePreferences.instance.times);
            parameters.put("tweets", "" + GamePreferences.instance.tweetsMade);
            parameters.put("ads", "" + GamePreferences.instance.adsSuccessfullyWatched);
            parameters.put("extraData", "data from game");
            parameters.put("version", "" + Constants.GAME_VERSION);
            parameters.put("isProduction", "" + Constants.PRODUCTION);
            Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
            request.setUrl("https://secure.bluehost.com/~alimalim/absolutegames/TheEdgeSubmitInProgressScore.php");

            request.setContent(HttpParametersUtils.convertHttpParameters(parameters));
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

            Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {

                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.error("GamePreferences submitData sendHttpRequest Failed", t.getMessage());
                }

                @Override
                public void cancelled() {
                }
            });
        }
    }

    public void getUserID() {
        if (userID.isEmpty()) {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("extraData", "data from game");
            parameters.put("isProduction", "" + Constants.PRODUCTION);
            Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
            request.setUrl("https://secure.bluehost.com/~alimalim/absolutegames/TheEdgeGetUserID.php");

            request.setContent(HttpParametersUtils.convertHttpParameters(parameters));
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

            Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    userID = httpResponse.getResultAsString();
                    save();
                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.error("GamePreferences getUserID sendHttpRequest Failed", t.getMessage());
                }

                @Override
                public void cancelled() {

                }
            });
        }
    }

    public int getCurrentLevel() {
        int currentLevel = GamePreferences.instance.stage;
        for (int i = GamePreferences.instance.zone - 1; i >= 0; --i) {
            currentLevel += StageLoader.getZone(i).getNumberOfStages();
        }
        return currentLevel;
    }
}
