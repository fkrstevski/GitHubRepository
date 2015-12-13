package com.filip.edge;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;
import com.filip.edge.util.IActivityRequestHandler;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSErrorException;
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
import org.robovm.pods.heyzap.ads.*;

import java.util.ArrayList;
import java.util.Arrays;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler, GameCenterListener {
    private GameCenterManager gcManager;
    private boolean isSignedIn;
    private EdgeGame game;
    private IOSApplication app;
    private boolean adsInitialized = false;

    private static final boolean USE_TEST_DEVICES = true;
    private GADBannerView adview;
    private GADInterstitial interstitial;

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.multisample = GLKViewDrawableMultisample._4X;
        game = new EdgeGame(this);
        app = new IOSApplication(game, config);
        return app;
    }

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        boolean r = super.didFinishLaunching(application, launchOptions);
        gcManager = new GameCenterManager(application.getKeyWindow(), this);

        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            try {
                GGLContextMobileAds.getSharedInstance().configure();
            } catch (NSErrorException e) {
                System.out.println("IOSLauncher: didFinishLaunching" + e.toString());
            }
        } else {
            HeyzapAds.start("7a7e1ff2afbec7f965b0d0a9a16f650c");
            //HeyzapAds.presentMediationDebugViewController();
        }

        return r;
    }

    @Override
    public void didBecomeActive(UIApplication application) {
        super.didBecomeActive(application);
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {

            if (!adsInitialized) {
                adsInitialized = true;
                adview = new GADBannerView(GADAdSize.SmartBannerLandscape());
                adview.setAdUnitID("ca-app-pub-0265459346558615/1087335221");
                adview.setHidden(true);
                adview.setRootViewController(app.getUIViewController());
                //adview.animate(1, Animation);
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

                System.out.println("didBecomeActive !adsInitialized");
                initializeInterstitialAd();
            }
        } else {
            HZVideoAd.fetch();
            HZIncentivizedAd.fetch();

            final HZAdsDelegateAdapter adsDelegate = new HZAdsDelegateAdapter() {
                @Override
                public void didShowAd(String tag) {
                    game.onShowAd(tag);
                }

                @Override
                public void didFailToShowAd(String tag, NSError error) {
                    System.out.print(error);
                    HZVideoAd.fetch();
                    HZIncentivizedAd.fetch();
                    game.onFailedToShowAd(tag);
                }

                @Override
                public void didReceiveAd(String tag) {
                    game.onReceivedAd(tag);
                }

                @Override
                public void didFailToReceiveAd(String tag) {
                    HZVideoAd.fetch();
                    HZIncentivizedAd.fetch();
                    game.onFailedToReceiveAd(tag);
                }

                @Override
                public void didClickAd(String tag) {
                    game.onClickAd(tag);
                }

                @Override
                public void didHideAd(String tag) {
                    HZVideoAd.fetch();
                    HZIncentivizedAd.fetch();
                    game.onHideAd(tag);
                }

                @Override
                public void willStartAudio() {
                    game.onAudioStartedForAd();
                }

                @Override
                public void didFinishAudio() {
                    game.onAudioFinishedForAd();
                }
            };

            HZInterstitialAd.setDelegate(adsDelegate);
            HZVideoAd.setDelegate(adsDelegate);

            HZIncentivizedAdDelegateAdapter incentivizedAdDelegateAdapter = new HZIncentivizedAdDelegateAdapter() {
                @Override
                public void didCompleteAd(String tag) {
                    game.onCompleteRewardVideoAd(tag);
                }

                @Override
                public void didFailToCompleteAd(String tag) {
                    game.onIncompleteRewardVideoAd(tag);
                }

                @Override
                public void didShowAd(String tag) {
                    adsDelegate.didShowAd(tag);
                }

                @Override
                public void didFailToShowAd(String tag, NSError error) {
                    adsDelegate.didFailToShowAd(tag, error);
                }

                @Override
                public void didReceiveAd(String tag) {
                    adsDelegate.didReceiveAd(tag);
                }

                @Override
                public void didFailToReceiveAd(String tag) {
                    adsDelegate.didFailToReceiveAd(tag);
                }

                @Override
                public void didClickAd(String tag) {
                    adsDelegate.didClickAd(tag);
                }

                @Override
                public void didHideAd(String tag) {
                    adsDelegate.didHideAd(tag);
                }

                @Override
                public void willStartAudio() {
                    adsDelegate.willStartAudio();
                }

                @Override
                public void didFinishAudio() {
                    adsDelegate.didFinishAudio();
                }
            };

            HZIncentivizedAd.setDelegate(incentivizedAdDelegateAdapter);
        }
    }

    public void initializeInterstitialAd() {
        System.out.println("initialize Interstitial Ad");
        interstitial = new GADInterstitial("ca-app-pub-0265459346558615/3563123625");
        interstitial.setDelegate(new GADInterstitialDelegateAdapter() {
            @Override
            public void didReceiveAd(GADInterstitial ad) {
                System.out.println("Did receive ad.");
            }

            @Override
            public void didDismissScreen(GADInterstitial ad) {
                System.out.println("did Dismiss Screen");
                initializeInterstitialAd();
            }

            @Override
            public void didFailToReceiveAd(GADInterstitial ad, GADRequestError error) {
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
    public void playerLoginFailed(NSError error) {
        System.out.println("playerLoginFailed. error: " + error);
        this.isSignedIn = false;
    }

    @Override
    public void playerLoginCompleted() {
        System.out.println("playerLoginCompleted");
        this.isSignedIn = true;
        gcManager.loadLeaderboards();
    }

    @Override
    public void achievementReportCompleted() {
        System.out.println("achievementReportCompleted");
    }

    @Override
    public void achievementReportFailed(NSError error) {
        System.out.println("achievementReportFailed. error: " + error);
    }

    @Override
    public void achievementsLoadCompleted(ArrayList<GKAchievement> achievements) {
        System.out.println("achievementsLoadCompleted: " + achievements.size());
    }

    @Override
    public void achievementsLoadFailed(NSError error) {
        System.out.println("achievementsLoadFailed. error: " + error);
    }

    @Override
    public void achievementsResetCompleted() {
        System.out.println("achievementsResetCompleted");
    }

    @Override
    public void achievementsResetFailed(NSError error) {
        System.out.println("achievementsResetFailed. error: " + error);
    }

    @Override
    public void scoreReportCompleted() {
        System.out.println("scoreReportCompleted");
    }

    @Override
    public void scoreReportFailed(NSError error) {
        System.out.println("scoreReportFailed. error: " + error);
    }

    @Override
    public void leaderboardsLoadCompleted(ArrayList<GKLeaderboard> scores) {
        System.out.println("scoresLoadCompleted: " + scores.size());
    }

    @Override
    public void leaderboardsLoadFailed(NSError error) {
        System.out.println("scoresLoadFailed. error: " + error);
    }

    @Override
    public void leaderboardViewDismissed() {
        System.out.println("leaderboardViewDismissed");
    }

    @Override
    public void achievementViewDismissed() {
        System.out.println("achievementViewDismissed");
    }

    @Override
    public void login() {
        gcManager.login();
    }

    @Override
    public void logOut() {

    }

    @Override
    public void showBannerAds(boolean show) {
        System.out.println("IOSLauncher: showBannerAds " + show);
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            adview.setHidden(!show);
        } else {

        }
    }

    @Override
    public void showInterstitialAd() {
        System.out.println("IOSLauncher: showInterstitialAd");
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            interstitial.present(app.getUIViewController());
        } else {
            HZInterstitialAd.show();
        }
    }

    @Override
    public void showVideoAd() {
        System.out.println("IOSLauncher: showVideoAd");
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            interstitial.present(app.getUIViewController());
        } else {
            if (HZVideoAd.isAvailable()) {
                HZVideoAd.show();
            } else {
                System.out.println("IOSLauncher: showVideoAd - NO AD AVAILABLE");
            }
        }
    }

    @Override
    public void showRewardVideoAd() {
        System.out.println("IOSLauncher: showRewardVideoAd");
        if (EdgeGame.adType != GamePreferences.AdType.ADMOB) {
            if (HZIncentivizedAd.isAvailable()) {
                HZIncentivizedAd.show();
            } else {
                System.out.println("IOSLauncher: showRewardVideoAd - NO AD AVAILABLE");
            }
        }
    }


    @Override
    public void startMethodTracing(String name) {

    }

    @Override
    public void stopMethodTracing() {

    }

    @Override
    public boolean isSignedIn() {
        return this.isSignedIn;
    }

    @Override
    public void showScores() {
        if (this.isSignedIn) {
            gcManager.showLeaderboardView(Constants.IOS_LEADERBOARD_ID);
        }
    }

    @Override
    public void submitScore(long score) {
        if (this.isSignedIn) {
            gcManager.reportScore(Constants.IOS_LEADERBOARD_ID, score);
        }
    }

    @Override
    public void showAchievements() {
        if (this.isSignedIn) {
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