package com.filip.edge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;
import com.filip.edge.util.IActivityRequestHandler;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.*;
import org.robovm.apple.gamekit.GKAchievement;
import org.robovm.apple.gamekit.GKLeaderboard;
import org.robovm.apple.glkit.GLKViewDrawableMultisample;
import org.robovm.apple.social.SLComposeViewController;
import org.robovm.apple.social.SLComposeViewControllerResult;
import org.robovm.apple.social.SLServiceType;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.bindings.gamecenter.GameCenterListener;
import org.robovm.bindings.gamecenter.GameCenterManager;
import org.robovm.objc.block.VoidBlock1;
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

    private static final boolean USE_TEST_DEVICES = false;
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
                Gdx.app.error("IOSLauncher:",  "didFinishLaunching" + e.toString());
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
        interstitial = new GADInterstitial("ca-app-pub-0265459346558615/3563123625");
        interstitial.setDelegate(new GADInterstitialDelegateAdapter() {
            @Override
            public void didReceiveAd(GADInterstitial ad) {
            }

            @Override
            public void didDismissScreen(GADInterstitial ad) {
                initializeInterstitialAd();
            }

            @Override
            public void didFailToReceiveAd(GADInterstitial ad, GADRequestError error) {
                Gdx.app.error("IOSLauncher", "didFailToReceiveAd " + error.description() + " " + error.getErrorCode());
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
        Gdx.app.error("IOSLauncher", "playerLoginFailed error: " + error);
        this.isSignedIn = false;
    }

    @Override
    public void playerLoginCompleted() {
        this.isSignedIn = true;
        gcManager.loadLeaderboards();
    }

    @Override
    public void achievementReportCompleted() {
    }

    @Override
    public void achievementReportFailed(NSError error) {
        Gdx.app.error("IOSLauncher", "achievementReportFailed error: " + error);
    }

    @Override
    public void achievementsLoadCompleted(ArrayList<GKAchievement> achievements) {
    }

    @Override
    public void achievementsLoadFailed(NSError error) {
        Gdx.app.error("IOSLauncher", "achievementsLoadFailed error: " + error);
    }

    @Override
    public void achievementsResetCompleted() {
    }

    @Override
    public void achievementsResetFailed(NSError error) {
        Gdx.app.error("IOSLauncher", "achievementsResetFailed error: " + error);
    }

    @Override
    public void scoreReportCompleted() {
    }

    @Override
    public void scoreReportFailed(NSError error) {
        Gdx.app.error("IOSLauncher", "scoreReportFailed error: " + error);
    }

    @Override
    public void leaderboardsLoadCompleted(ArrayList<GKLeaderboard> scores) {
    }

    @Override
    public void leaderboardsLoadFailed(NSError error) {
        Gdx.app.error("IOSLauncher", "scoresLoadFailed error: " + error);
    }

    @Override
    public void leaderboardViewDismissed() {

    }

    @Override
    public void achievementViewDismissed() {

    }

    @Override
    public void login() {
        gcManager.login();
    }

    @Override
    public void logOut() {

    }

    @Override
    public void showTweetSheet(String message, String png){
        if( SLComposeViewController.isAvailable(SLServiceType.Twitter) ) {
            SLComposeViewController twitterPostVC = new SLComposeViewController(SLServiceType.Twitter);

            twitterPostVC.addImage(new UIImage(NSData.read(Gdx.files.local(png).file())));
            twitterPostVC.setInitialText(message);
            twitterPostVC.addURL(new NSURL("http://www.theedgecontest.com"));

            twitterPostVC.setCompletionHandler(
                new VoidBlock1<SLComposeViewControllerResult>() {
                    @Override
                    public void invoke(SLComposeViewControllerResult slComposeViewControllerResult) {
                        if(slComposeViewControllerResult == SLComposeViewControllerResult.Done) {
                            game.onCompleteTweet();
                        }
                    }
                }
            );

            app.getUIViewController().presentViewController(twitterPostVC, true, null);
        }
        else {
            game.showGenericOkDialog("Twitter", "Account not added");
        }
    }

    @Override
    public void showBannerAds(boolean show) {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            adview.setHidden(!show);
        } else {

        }
    }

    @Override
    public boolean hasBannerAd() {
        return false;
    }

    @Override
    public void showInterstitialAd() {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            interstitial.present(app.getUIViewController());
        } else {
            if(HZInterstitialAd.isAvailable()) {
                HZInterstitialAd.show();
            }
        }
    }

    @Override
    public boolean hasInterstitialAd() {
        return HZInterstitialAd.isAvailable();
    }


    @Override
    public void showVideoAd() {
        if (EdgeGame.adType == GamePreferences.AdType.ADMOB) {
            interstitial.present(app.getUIViewController());
        } else {
            if (HZVideoAd.isAvailable()) {
                HZVideoAd.show();
            }
        }
    }

    @Override
    public boolean hasVideoAd() {
        return HZVideoAd.isAvailable();
    }

    @Override
    public void showRewardVideoAd() {
        if (EdgeGame.adType != GamePreferences.AdType.ADMOB) {
            if (HZIncentivizedAd.isAvailable()) {
                HZIncentivizedAd.show();
            } else {
                game.showGenericOkDialog("Reward Video", "Not Available");
            }
        }
    }

    @Override
    public boolean hasRewardAd() {
        return HZIncentivizedAd.isAvailable();
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

        }
    }
}