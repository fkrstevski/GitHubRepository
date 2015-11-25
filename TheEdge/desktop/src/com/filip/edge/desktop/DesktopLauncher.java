package com.filip.edge.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.filip.edge.EdgeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// iPhone 6
		config.height = 750;
		config.width = 1334;

		// iPad Mini
		//config.height = 760;
        //config.width = 1024;

		// iPhone 5
		config.height = 640;
		config.width = 1136;


		// old iPhone
		//config.height = 320;
		//config.width = 480;
		new LwjglApplication(new EdgeGame(null), config);
	}
}
