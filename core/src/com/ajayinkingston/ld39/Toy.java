package com.ajayinkingston.ld39;

import com.badlogic.gdx.graphics.Texture;

public class Toy {

	Texture[] images = new Texture[2];
	
	public Toy(){
		for(int i=0;i<images.length;i++){
			images[i] = new Texture("toy"+i+".png");
		}
	}
	
}
