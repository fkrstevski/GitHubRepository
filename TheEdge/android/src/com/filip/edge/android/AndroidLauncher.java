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
import com.filip.edge.util.IActivityRequestHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, GameHelper.GameHelperListener {
    private GameHelper gameHelper;
    private static final String AD_UNIT_ID = "ca-app-pub-0265459346558615/8626109621";
    AdView adView;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ADS: {
                    Gdx.app.debug("TS", "!!!!!!!!!!!!!!!!!!! SHOW AD!");
                    adView.setVisibility(View.VISIBLE);
                    break;
                }
                case HIDE_ADS: {
                    Gdx.app.debug("TS", "!!!!!!!!!!!!!!!!!!! HIDE AD!");
                    adView.setVisibility(View.GONE);
                    break;
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

        View gameView = initializeForView(new EdgeGame(this), config);
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        startAdvertising();

        layout.addView(adView, adParams);

        setContentView(layout);

        /*GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        globalTracker=analytics.newTracker(R.xml.global_tracker)**/

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);
        gameHelper.setup(this);


    }

    private void startAdvertising() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    /*@Override
    public void setTrackerScreenName(String path) {
        //globalTracker.setScreenName(path);
        // globalTracker.send(new HitBuilders.AppViewBuilder().build());
    }*/

    @Override
    public void showAds(boolean show) {
        handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
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
                //@Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MyTemplateGame", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void logOut() {
        try {
            runOnUiThread(new Runnable() {
                //@Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MyTemplateGame", "Log out failed: " + e.getMessage() + ".");
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
            //startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), getString(R.string.leaderboard_id)), 9002);
        } else {
            // Maybe sign in here then redirect to submitting score?
        }
    }


    public void unlockAchievements(String achievementID) {
        if (isSignedIn()) {
            //Games.Achievements.unlock(gameHelper.getApiClient(), achievementID);
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
    public void onSignInFailed() {
        Gdx.app.log("MyTemplateGame", "Sign in fail");
    }

    @Override
    public void onSignInSucceeded() {
        Gdx.app.log("MyTemplateGame", "SignedIn");
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
