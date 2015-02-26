package come.filip.templategame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import come.filip.templategame.MyTemplateGame;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 750;
        config.width = 1334;
        new LwjglApplication(new MyTemplateGame(null), config);
    }
}
