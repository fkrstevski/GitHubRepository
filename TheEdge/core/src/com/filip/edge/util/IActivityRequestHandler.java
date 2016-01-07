package com.filip.edge.util;

public interface IActivityRequestHandler {
    //for google analytcis
    //public void setTrackerScreenName(String path);

    // Twitter
    void showTweetSheet(String message, String png);

    // Ads
    void showBannerAds(boolean show);
    boolean hasBannerAd();
    void showInterstitialAd();
    boolean hasInterstitialAd();
    void showVideoAd();
    boolean hasVideoAd();
    void showRewardVideoAd();
    boolean hasRewardAd();

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