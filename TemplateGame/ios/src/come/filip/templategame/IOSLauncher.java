package come.filip.templategame;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.glkit.GLKViewDrawableMultisample;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import come.filip.templategame.MyTemplateGame;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.multisample = GLKViewDrawableMultisample._4X;
        return new IOSApplication(new MyTemplateGame(), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}