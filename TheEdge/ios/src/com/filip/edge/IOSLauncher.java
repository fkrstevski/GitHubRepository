package com.filip.edge;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.gamekit.GKAchievement;
import org.robovm.apple.gamekit.GKLeaderboard;
import org.robovm.apple.glkit.GLKViewDrawableMultisample;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.bindings.gamecenter.GameCenterListener;
import org.robovm.bindings.gamecenter.GameCenterManager;

import java.util.ArrayList;

import com.filip.edge.util.Constants;
import com.filip.edge.util.IActivityRequestHandler;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler, GameCenterListener
{
    private GameCenterManager gcManager;
    private boolean isSignedIn;

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
        IOSApplication app = new IOSApplication(new EdgeGame(this), config);
        return app;
    }

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions)
    {
        boolean r = super.didFinishLaunching(application, launchOptions);

        gcManager = new GameCenterManager(application.getKeyWindow(), this);

        return r;
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
        System.out.println("showAds");
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
}