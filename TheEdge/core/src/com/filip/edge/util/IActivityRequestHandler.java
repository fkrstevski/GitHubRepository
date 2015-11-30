package com.filip.edge.util;

public interface IActivityRequestHandler {
    //for google analytcis
    //public void setTrackerScreenName(String path);

    // Ads
    void showAds(boolean show);
    void showInterstitialAd();

    // for google play services
    void login();
    void logOut();
    boolean isSignedIn();

    // Leaderboards
    void showScores();
    void submitScore(long score);

    // Achievements
    void unlockAchievement(String achievementID);
    void showAchievements();

    // Method tracing
    void startMethodTracing(String name);
    void stopMethodTracing();
}