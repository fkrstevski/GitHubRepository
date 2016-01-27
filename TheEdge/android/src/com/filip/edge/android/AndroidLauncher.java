package com.filip.edge.android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.filip.edge.EdgeGame;
import com.filip.edge.util.GamePreferences;
import com.filip.edge.util.IActivityRequestHandler;
import com.google.android.gms.ads.*;
import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.IncentivizedAd;
import com.heyzap.sdk.ads.VideoAd;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import io.fabric.sdk.android.Fabric;

import java.io.File;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "vi3rGOvBhnCkSHLj4AGNJLUh3";
    private static final String TWITTER_SECRET = "ymkKGfN8gMh2p5Mv3FnqTlcKacHSWOEmu3KTKaBgqY21Qr57XF";

    private static final int TWEET_COMPOSER_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static final String TAG = AndroidLauncher.class.getName();
    private AdView adView;
    private InterstitialAd interstitialAd;
    private EdgeGame game;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    private final int SHOW_INTERSTITIAL_AD = 2;
    private Animation bannerSlideDownAnimation;
    private Animation bannerSlideUpAnimation;

    private String tweetMessage;
    private String tweetImage;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ADS: {
                    adView.setVisibility(View.VISIBLE);
                    adView.startAnimation(bannerSlideDownAnimation);
                    //startAdvertising();
                    break;
                }
                case HIDE_ADS: {
                    adView.setVisibility(View.GONE);
                    adView.startAnimation(bannerSlideUpAnimation);
                    break;
                }
                case SHOW_INTERSTITIAL_AD: {
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
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.hideStatusBar = true;
        config.useWakelock = true;
        config.useImmersiveMode = true;
        //config.numSamples = 4;

        RelativeLayout layout = new RelativeLayout(this);
        game = new EdgeGame(this);
        View gameView = initializeForView(game, config);
        layout.addView(gameView);

        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
        /*    // Add the AdMob view
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
            */
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

    public boolean isTwitterInstalled(){
        try{
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.twitter.android", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    showTweet(this.tweetMessage, this.tweetImage);
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL_STORAGE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this).setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null);

        this.runOnUiThread(new Runnable() {

            public void run() {
                AlertDialog d = dialog.create();
                d.show();
            }
        });

    }

    @Override
    public void showTweetSheet(String message, String png) {
        this.tweetMessage = message;
        this.tweetImage = png;

        if (isTwitterInstalled()) {

            if (Build.VERSION.SDK_INT >= 23) {
                int hasWriteExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showMessageOKCancel("You need to allow access to External Storage",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    }
                                });
                        return;
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }
            }

            showTweet(this.tweetMessage, this.tweetImage);
        }
        else {
            game.showGenericOkDialog("Twitter", "Not Installed");
        }
    }

    private void showTweet(String message, String png) {
        Intent intent;
        // We need to copy the screenshot over from local storage
        // to external storage
        Gdx.files.external(png).delete();
        FileHandle external = Gdx.files.external(png);

        FileHandle local = Gdx.files.local(png);
        Pixmap localPixmap = new Pixmap(local);

        try{
            PixmapIO.writePNG(external, localPixmap);
            File myimageFile = external.file();
            Uri myImageUri = Uri.fromFile(myimageFile);
            intent = new TweetComposer.Builder(this)
                    .text(message)
                    .image(myImageUri)
                    .createIntent();
        }
        catch (GdxRuntimeException e){
            intent = new TweetComposer.Builder(this)
                    .text(message)
                    .createIntent();
        }

        localPixmap.dispose();

        startActivityForResult(intent, TWEET_COMPOSER_REQUEST_CODE);
    }

    @Override
    public void showBannerAds(boolean show) {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
        }
    }

    @Override
    public boolean hasBannerAd() {
        return false;
    }

    @Override
    public void showInterstitialAd() {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            handler.sendEmptyMessage(SHOW_INTERSTITIAL_AD);
        } else {
            if (com.heyzap.sdk.ads.InterstitialAd.isAvailable()) {
                com.heyzap.sdk.ads.InterstitialAd.display(this);
            }
        }
    }

    @Override
    public boolean hasInterstitialAd() {
        return com.heyzap.sdk.ads.InterstitialAd.isAvailable();
    }

    @Override
    public void showVideoAd() {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            handler.sendEmptyMessage(SHOW_INTERSTITIAL_AD);
        } else {
            if (VideoAd.isAvailable()) {
                VideoAd.display(this);
            }
        }
    }

    @Override
    public boolean hasVideoAd() {
        return com.heyzap.sdk.ads.VideoAd.isAvailable();
    }

    @Override
    public void showRewardVideoAd() {
        if (EdgeGame.adType != GamePreferences.AdType.ADMOB) {
            if (IncentivizedAd.isAvailable()) {
                IncentivizedAd.display(this);
            } else {
                game.showGenericOkDialog("Reward Video", "Not Available");
            }
        }
    }

    @Override
    public boolean hasRewardAd() {
        return com.heyzap.sdk.ads.IncentivizedAd.isAvailable();
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
    protected void onStart() {
        super.onStart();
        //GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TWEET_COMPOSER_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                game.onCompleteTweet();
            }
            else {
            }
        }
        else {
        }
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

                }
            });
        } catch (Exception e) {
            Gdx.app.error(TAG, "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void logOut() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        } catch (Exception e) {
            Gdx.app.error(TAG, "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void submitScore(long score) {
        if (isSignedIn()) {

        } else {
            // Maybe sign in here then redirect to submitting score?
        }
    }

    @Override
    public void unlockAchievement(String achievementID) {
        if (isSignedIn()) {

        } else {
            // Maybe sign in here
        }
    }

    @Override
    public void showScores() {
        if (isSignedIn() == true) {

        } else {
            // Maybe sign in here then redirect to showing scores?
        }
    }

    @Override
    public void showAchievements() {
        if (isSignedIn() == true) {

        } else {
            // Maybe sign in here
        }
    }

/*    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }*/
    /*
    @Override
    public void rateGame()
    {
        String str ="https://play.google.com/store/apps/details?id=com.csharks.thrustcopter";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }
	*/
}
