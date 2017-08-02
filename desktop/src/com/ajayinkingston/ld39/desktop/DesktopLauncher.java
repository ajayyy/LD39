package com.ajayinkingston.ld39.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ajayinkingston.ld39.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 1000;
		config.height = 600;
		config.samples=3;
		
	    config.depth = 15;
	    
	    config.title = "Achluophobia";
		
		new LwjglApplication(new Main(), config);
	}
}
