package cc.clv.BlueRobe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cc.clv.BlueRobe.BlueRobe;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 320;
        config.height = 568;
        new LwjglApplication(new BlueRobe(), config);
    }
}
