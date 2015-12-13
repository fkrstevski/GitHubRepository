package com.filip.edge.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.filip.edge.EdgeGame;
import com.filip.edge.util.IActivityRequestHandler;

public class DesktopLauncher implements IActivityRequestHandler {
	public static EdgeGame game;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// iPhone 6
		config.height = 750;
		config.width = 1334;

		// iPad Mini
		//config.height = 760;
        //config.width = 1024;

		// iPhone 5
		//config.height = 640;
		//config.width = 1136;


		// old iPhone
		//config.height = 320;
		//config.width = 480;

		DesktopLauncher dl = new DesktopLauncher();
		game = new EdgeGame(dl);
		new LwjglApplication(game, config);
	}

	@Override
	public void showBannerAds(boolean show) {

	}
	@Override
	public void showInterstitialAd(){

	}

	@Override
	public void showVideoAd(){

	}
	@Override
	public void showRewardVideoAd(){

	}

	// for google play services
	@Override
	public void login(){

	}
	@Override
	public void logOut(){

	}
	@Override
	public boolean isSignedIn(){
		return false;
	}

	// Leaderboards
	@Override
	public void showScores(){

	}
	@Override
	public void submitScore(long score){

	}

	// Achievements
	@Override
	public void unlockAchievement(String achievementID){

	}
	@Override
	public void showAchievements(){

	}

	// Method tracing
	@Override
	public void startMethodTracing(String name){

	}
	@Override
	public void stopMethodTracing(){

	}
}
