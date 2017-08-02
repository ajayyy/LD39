package com.ajayinkingston.ld39;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

public class Pump {
	float x,y;
	float rotation;
	boolean clickedlast;
	
	Texture image,text;
	
	Random rand = new Random();

	public Pump(Main main){
		image = new Texture("pump.png");
		text = new Texture("pumptext.png");
		
		x = main.charger.x+rand.nextInt(450+300)+300;
		y = main.ground.getGroundLevel(x, image)+image.getWidth();
	}
	
	public void render(Main main){
		main.batch.draw(text, x + image.getWidth()/2 - text.getWidth()/2, y + image.getHeight() + 25);
		main.batch.draw(image, x, y, 0, image.getHeight()/2, image.getWidth(), image.getHeight(), 1, 1, rotation, 0, 0, image.getWidth(), image.getHeight(), false, false);
	}
	
	public void update(Main main, double delta){
		if(Gdx.input.isKeyPressed(Keys.E)){
			if(x + image.getWidth() > main.player.x && x-image.getWidth() < main.player.x+main.player.image.getWidth() && y+image.getHeight()>y && y-image.getHeight()<main.player.y+main.player.image.getHeight()){
				if(main.charger.energy < main.charger.fullenergy && !main.player.hasFlashlight){
					main.charger.energy += 150 * delta;
					if(main.charger.energy > main.charger.fullenergy){
						main.charger.energy = main.charger.fullenergy;
					}
					rotation += 200 * delta;
				}else if(!clickedlast){
					main.charger.fullani = true;
					if(main.player.hasFlashlight){
						main.blurb = new Blurb("Put down the flashlight", 1.5f);
					}else if(main.charger.energy >= main.charger.fullenergy){
						main.blurb = new Blurb("Energy full", 1.5f);
					}
				}
			}
			clickedlast = true;
		}else{
			clickedlast = false;
		}
		
	}
}
