package com.filip.edge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;
import com.filip.edge.util.IActivityRequestHandler;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.*;
import org.robovm.apple.gamekit.GKAchievement;
import org.robovm.apple.gamekit.GKLeaderboard;
import org.robovm.apple.glkit.GLKViewDrawableMultisample;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.bindings.gamecenter.GameCenterListener;
import org.robovm.bindings.gamecenter.GameCenterManager;
import org.robovm.pods.google.GGLContextMobileAds;
import org.robovm.pods.google.mobileads.*;

import java.util.ArrayList;
import java.util.Arrays;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler, GameCenterListener
{
    private GameCenterManager gcManager;
    private boolean isSignedIn;
    private EdgeGame game;
    private IOSApplication app;
    private boolean adsInitialized = false;

    private static final boolean USE_TEST_DEVICES = true;
    private GADBannerView adview;
    private GADInterstitial interstitial;

    public static void main(String[] argv)
    {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    protected IOSApplication createApplication()
    {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.multisample = GLKViewDrawableMultisample._4X;
        game = new EdgeGame(this);
        app = new IOSApplication(game, config);
        return app;
    }

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions)
    {
        boolean r = super.didFinishLaunching(application, launchOptions);
        gcManager = new GameCenterManager(application.getKeyWindow(), this);

        if(EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            try {
                GGLContextMobileAds.getSharedInstance().configure();
            } catch (NSErrorException e) {
                System.out.println("IOSLauncher: didFinishLaunching" + e.toString());
            }
        }

        return r;
    }

    @Override
    public void didBecomeActive (UIApplication application) {
        super.didBecomeActive(application);
        if(EdgeGame.adType == GamePreferences.AdType.ADMOB) {

            if(!adsInitialized) {
                adsInitialized = true;
                adview = new GADBannerView(GADAdSize.SmartBannerLandscape());
                adview.setAdUnitID("ca-app-pub-0265459346558615/1087335221");
                adview.setHidden(true);
                adview.setRootViewController(app.getUIViewController());
                app.getUIViewController().getView().addSubview(adview);

                GADRequest request = new GADRequest();
                if (USE_TEST_DEVICES) {
                    request.setTestDevices(Arrays.asList("ab618865a1c907a38d04edf1b6516624"));
                    System.out.println("Test devices: " + request.getTestDevices());
                }

                adview.setDelegate(new GADBannerViewDelegateAdapter() {
                    @Override
                    public void didReceiveAd(GADBannerView view) {
                        super.didReceiveAd(view);
                    }

                    @Override
                    public void didFailToReceiveAd(GADBannerView view,
                                                   GADRequestError error) {
                        super.didFailToReceiveAd(view, error);
                    }
                });

                adview.loadRequest(request);

                // Make height a little bigger
                final CGSize screenSize = UIScreen.getMainScreen().getBounds().getSize();
                float bannerWidth = (float) screenSize.getWidth();
                float bannerHeight = (float) (screenSize.getHeight() * 0.15);
                adview.setFrame(new CGRect(0, 0, bannerWidth, bannerHeight));

                initializeInterstitialAd();
            }
        }
    }

    public void initializeInterstitialAd() {
        interstitial = new GADInterstitial("ca-app-pub-0265459346558615/3563123625");
        interstitial.setDelegate(new GADInterstitialDelegateAdapter() {
            @Override
            public void didReceiveAd (GADInterstitial ad) {
                System.out.println("Did receive ad.");
            }

            @Override
            public void didDismissScreen(GADInterstitial ad) {
                initializeInterstitialAd();
            }

            @Override
            public void didFailToReceiveAd (GADInterstitial ad, GADRequestError error) {
                System.out.println(error.description());
                System.out.println(error.getErrorCode());
            }
        });

        GADRequest request = new GADRequest();
        if (USE_TEST_DEVICES) {
            request.setTestDevices(Arrays.asList("ab618865a1c907a38d04edf1b6516624"));
        }
        interstitial.loadRequest(request);
    }

    @Override
    public void playerLoginFailed(NSError error)
    {
        System.out.println("playerLoginFailed. error: " + error);
        this.isSignedIn = false;
    }

    @Override
    public void playerLoginCompleted()
    {
        System.out.println("playerLoginCompleted");
        this.isSignedIn = true;
        gcManager.loadLeaderboards();
    }

    @Override
    public void achievementReportCompleted()
    {
        System.out.println("achievementReportCompleted");
    }

    @Override
    public void achievementReportFailed(NSError error)
    {
        System.out.println("achievementReportFailed. error: " + error);
    }

    @Override
    public void achievementsLoadCompleted(ArrayList<GKAchievement> achievements)
    {
        System.out.println("achievementsLoadCompleted: " + achievements.size());
    }

    @Override
    public void achievementsLoadFailed(NSError error)
    {
        System.out.println("achievementsLoadFailed. error: " + error);
    }

    @Override
    public void achievementsResetCompleted()
    {
        System.out.println("achievementsResetCompleted");
    }

    @Override
    public void achievementsResetFailed(NSError error)
    {
        System.out.println("achievementsResetFailed. error: " + error);
    }

    @Override
    public void scoreReportCompleted()
    {
        System.out.println("scoreReportCompleted");
    }

    @Override
    public void scoreReportFailed(NSError error)
    {
        System.out.println("scoreReportFailed. error: " + error);
    }

    @Override
    public void leaderboardsLoadCompleted(ArrayList<GKLeaderboard> scores)
    {
        System.out.println("scoresLoadCompleted: " + scores.size());
    }

    @Override
    public void leaderboardsLoadFailed(NSError error)
    {
        System.out.println("scoresLoadFailed. error: " + error);
    }

    @Override
    public void leaderboardViewDismissed()
    {
        System.out.println("leaderboardViewDismissed");
    }

    @Override
    public void achievementViewDismissed()
    {
        System.out.println("achievementViewDismissed");
    }

    @Override
    public void login()
    {
        gcManager.login();
    }

    @Override
    public void logOut()
    {

    }

    @Override
    public void showAds(boolean show) {
        System.out.println("@@@@@@@@@@@@@@@@@@@showAds " + show);
        if(EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            adview.setHidden(!show);
        }
    }

    @Override
    public void showInterstitialAd(){
        System.out.println("showInterstitialAd");
        if(EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            interstitial.present(app.getUIViewController());
        }
    }


    @Override
    public void startMethodTracing(String name){

    }

    @Override
    public void stopMethodTracing(){

    }

    @Override
    public boolean isSignedIn()
    {
        return this.isSignedIn;
    }

    @Override
    public void showScores()
    {
        if (this.isSignedIn)
        {
            gcManager.showLeaderboardView(Constants.IOS_LEADERBOARD_ID);
        }
    }

    @Override
    public void submitScore(long score)
    {
        if (this.isSignedIn)
        {
            gcManager.reportScore(Constants.IOS_LEADERBOARD_ID, score);
        }
    }

    @Override
    public void showAchievements(){
        if (this.isSignedIn)
        {
            gcManager.showAchievementsView();
        }
    }

    @Override
    public void unlockAchievement(String achievementID) {
        if (this.isSignedIn) {
            System.out.println("unlockAchievement " + achievementID);
        }
    }
}