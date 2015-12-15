package com.filip.edge.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.filip.edge.EdgeGame;
import com.filip.edge.util.GamePreferences;
import com.filip.edge.util.IActivityRequestHandler;
import com.google.android.gms.ads.*;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.IncentivizedAd;
import com.heyzap.sdk.ads.VideoAd;


public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, GameHelper.GameHelperListener {

    public static final String TAG = AndroidLauncher.class.getName();
    private GameHelper gameHelper;
    private AdView adView;
    private InterstitialAd interstitialAd;
    private EdgeGame game;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    private final int SHOW_INTERSTITIAL_AD = 2;
    private Animation bannerSlideDownAnimation;
    private Animation bannerSlideUpAnimation;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ADS: {
                    Gdx.app.debug(TAG, "!!!!!!!!!!!!!!!!!!! SHOW AD!");
                    adView.setVisibility(View.VISIBLE);
                    adView.startAnimation(bannerSlideDownAnimation);
                    //startAdvertising();
                    break;
                }
                case HIDE_ADS: {
                    Gdx.app.debug(TAG, "!!!!!!!!!!!!!!!!!!! HIDE AD!");
                    adView.setVisibility(View.GONE);
                    adView.startAnimation(bannerSlideUpAnimation);
                    break;
                }
                case SHOW_INTERSTITIAL_AD: {
                    Gdx.app.debug(TAG, "!!!!!!!!!!!!!!!!!!! SHOW INTERSTITIAL AD!");
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
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

        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            // Add the AdMob view
            RelativeLayout.LayoutParams adParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


            bannerSlideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
            bannerSlideDownAnimation.setDuration(500);

            bannerSlideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
            bannerSlideUpAnimation.setDuration(500);

            adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
            adView.setVisibility(View.GONE);
            startAdvertising();

            layout.addView(adView, adParams);

            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitialAd();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {

                }
            });

            requestNewInterstitialAd();
        } else {
            Activity activity = this; // must be an Activity
            HeyzapAds.start("7a7e1ff2afbec7f965b0d0a9a16f650c", activity);

            VideoAd.fetch();
            IncentivizedAd.fetch();

            HeyzapAds.OnStatusListener adListener = new HeyzapAds.OnStatusListener() {
                @Override
                public void onShow(String s) {
                    game.onShowAd(s);
                }

                @Override
                public void onClick(String s) {
                    game.onClickAd(s);
                }

                @Override
                public void onHide(String s) {
                    VideoAd.fetch();
                    IncentivizedAd.fetch();
                    game.onHideAd(s);
                }

                @Override
                public void onFailedToShow(String s) {
                    VideoAd.fetch();
                    IncentivizedAd.fetch();
                    game.onFailedToShowAd(s);
                }

                @Override
                public void onAvailable(String s) {
                    game.onReceivedAd(s);
                }

                @Override
                public void onFailedToFetch(String s) {
                    VideoAd.fetch();
                    IncentivizedAd.fetch();
                    game.onFailedToReceiveAd(s);
                }

                @Override
                public void onAudioStarted() {
                    game.onAudioStartedForAd();
                }

                @Override
                public void onAudioFinished() {
                    game.onAudioFinishedForAd();
                }
            };

            com.heyzap.sdk.ads.InterstitialAd.setOnStatusListener(adListener);
            VideoAd.setOnStatusListener(adListener);
            IncentivizedAd.setOnStatusListener(adListener);

            IncentivizedAd.setOnIncentiveResultListener(new HeyzapAds.OnIncentiveResultListener() {
                @Override
                public void onComplete(String tag) {
                    game.onCompleteRewardVideoAd(tag);
                }

                @Override
                public void onIncomplete(String tag) {
                    game.onIncompleteRewardVideoAd(tag);
                }
            });

            //HeyzapAds.startTestActivity(activity);
        }

        setContentView(layout);

        /*GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        globalTracker=analytics.newTracker(R.xml.global_tracker)**/

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);
        gameHelper.setup(this);


    }

    private void requestNewInterstitialAd() {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("2502D554C3E469427B643A3BB7F2E807")
                    .build();

            interstitialAd.loadAd(adRequest);
        }
    }


    private void startAdvertising() {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("2502D554C3E469427B643A3BB7F2E807").build();
            adView.loadAd(adRequest);
        }
    }

    /*@Override
    public void setTrackerScreenName(String path) {
        //globalTracker.setScreenName(path);
        // globalTracker.send(new HitBuilders.AppViewBuilder().build());
    }*/

    @Override
    public void showTweetSheet(String message, String png){

    }

    @Override
    public void showBannerAds(boolean show) {
        System.out.println("AndroidLauncher: showBannerAds");
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
        } else {

        }
    }

    @Override
    public void showInterstitialAd() {
        System.out.println("AndroidLauncher: showInterstitialAd");
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            handler.sendEmptyMessage(SHOW_INTERSTITIAL_AD);
        } else {
            if (com.heyzap.sdk.ads.InterstitialAd.isAvailable()) {
                com.heyzap.sdk.ads.InterstitialAd.display(this);
            }
        }
    }

    @Override
    public void showVideoAd() {
        System.out.println("AndroidLauncher: showVideoAd");
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            handler.sendEmptyMessage(SHOW_INTERSTITIAL_AD);
        } else {
            if (VideoAd.isAvailable()) {
                VideoAd.display(this);
            } else {
                System.out.println("AndroidLauncher: showVideoAd - NO AD AVAILABLE");
            }
        }
    }

    @Override
    public void showRewardVideoAd() {
        System.out.println("AndroidLauncher: showRewardVideoAd");
        if (EdgeGame.adType != GamePreferences.AdType.ADMOB) {
            if (IncentivizedAd.isAvailable()) {
                IncentivizedAd.display(this);
            } else {
                System.out.println("AndroidLauncher: showRewardVideoAd - NO AD AVAILABLE");
            }
        }
    }

    @Override
    public void startMethodTracing(String name) {
        android.os.Debug.startMethodTracing(name);
    }

    @Override
    public void stopMethodTracing() {
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
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            if (adView != null) adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            if (adView != null) adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            if (adView != null) adView.destroy();
        }
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
        } else {
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
    public void showAchievements() {
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
