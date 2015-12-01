package com.filip.edge.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.filip.edge.EdgeGame;
import com.filip.edge.util.GamePreferences;
import com.filip.edge.util.IActivityRequestHandler;
import com.google.android.gms.ads.*;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, GameHelper.GameHelperListener {

    public static final String TAG = AndroidLauncher.class.getName();
    private GameHelper gameHelper;
    private AdView adView;
    private InterstitialAd interstitialAd;
    private EdgeGame game;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    private final int SHOW_INTERSTITIAL_AD = 2;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ADS: {
                    Gdx.app.debug(TAG, "!!!!!!!!!!!!!!!!!!! SHOW AD!");
                    adView.setVisibility(View.VISIBLE);
                    //startAdvertising();
                    break;
                }
                case HIDE_ADS: {
                    Gdx.app.debug(TAG, "!!!!!!!!!!!!!!!!!!! HIDE AD!");
                    adView.setVisibility(View.GONE);
                    break;
                }
                case SHOW_INTERSTITIAL_AD: {
                    Gdx.app.debug(TAG, "!!!!!!!!!!!!!!!!!!! SHOW INTERSTITIAL AD!");
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    }
                    else {
                        if(game.getCurrScreen() != null) {
                            game.getCurrScreen().interstitialClosed();
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.numSamples = 4;
        //initialize(new EdgeGame(this), config);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        RelativeLayout layout = new RelativeLayout(this);
        game = new EdgeGame(this);
        View gameView = initializeForView(game, config);
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.setVisibility(View.GONE);
        startAdvertising();

        layout.addView(adView, adParams);

        setContentView(layout);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitialAd();
                if(game.getCurrScreen() != null) {
                    game.getCurrScreen().interstitialClosed();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(game.getCurrScreen() != null) {
                    game.getCurrScreen().interstitialClosed();
                }
            }
        });

        requestNewInterstitialAd();

        /*GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        globalTracker=analytics.newTracker(R.xml.global_tracker)**/

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);
        gameHelper.setup(this);


    }

    private void requestNewInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("2502D554C3E469427B643A3BB7F2E807")
                .build();

        interstitialAd.loadAd(adRequest);
    }


    private void startAdvertising() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("2502D554C3E469427B643A3BB7F2E807").build();
        adView.loadAd(adRequest);
    }

    /*@Override
    public void setTrackerScreenName(String path) {
        //globalTracker.setScreenName(path);
        // globalTracker.send(new HitBuilders.AppViewBuilder().build());
    }*/

    @Override
    public void showAds(boolean show) {
        if(GamePreferences.instance.getAdType() == GamePreferences.AdType.ADMOB){
            handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
        }
        else {
            Gdx.app.log(TAG, "ANDROID: No Add will be shown");
        }
    }

    @Override
    public void showInterstitialAd(){
        handler.sendEmptyMessage(SHOW_INTERSTITIAL_AD);
    }

    @Override
    public void startMethodTracing(String name){
        android.os.Debug.startMethodTracing(name);
    }

    @Override
    public void stopMethodTracing(){
        android.os.Debug.stopMethodTracing();
    }

    @Override
    protected void onStart() { // TODO: call gameHelper.OnStart from the main menu
        super.onStart();
        //GoogleAnalytics.getInstance(this).reportActivityStart(this);
        gameHelper.onStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //GoogleAnalytics.getInstance(this).reportActivityStop(this);
        gameHelper.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
    }

    @Override
    public void onPause() {
        if (adView != null) adView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) adView.destroy();
        super.onDestroy();
    }

    @Override
    public void login() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("The Edge", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void logOut() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("The Edge", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void submitScore(long score) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(R.string.leaderboard_id), score);
        } else {
            // Maybe sign in here then redirect to submitting score?
        }
    }

    @Override
    public void unlockAchievement(String achievementID) {
        if (isSignedIn()) {
            Games.Achievements.unlock(gameHelper.getApiClient(), achievementID);
        }else {
            // Maybe sign in here
        }
    }

    @Override
    public void showScores() {
        if (isSignedIn() == true) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), getString(R.string.leaderboard_id)), 9002);
        } else {
            // Maybe sign in here then redirect to showing scores?
        }
    }

    @Override
    public void showAchievements(){
        if (isSignedIn() == true) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 9003);
        } else {
            // Maybe sign in here
        }
    }

    @Override
    public void onSignInFailed() {
        Gdx.app.log("The Edge", "Sign in fail");
    }

    @Override
    public void onSignInSucceeded() {
        Gdx.app.log("The Edge", "SignedIn");
    }
    /*
	@Override
public void rateGame()
{
String str ="https://play.google.com/store/apps/details?id=com.csharks.thrustcopter";
startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
}
	 */
}
