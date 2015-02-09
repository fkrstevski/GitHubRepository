package com.filip.offthewalls.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.filip.offthewalls.OffTheWallsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 750;
        config.width = 1334;
		new LwjglApplication(new OffTheWallsGame(), config);
	}
}
