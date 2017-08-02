package com.ajayinkingston.ld39;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Charger {
	float x,y;
	
	boolean isCharging;
	
	Texture image;
	Texture charging;
	Texture powerleft;
	
	float fullenergy = 1500;
	float energy = fullenergy;
	
	boolean fullani;
	float fullrotation;
	int stage;

	Random rand = new Random();

	public Charger(Main main){
		image = new Texture("charger.png");
		charging = new Texture("charging.png");
		powerleft = new Texture("powerleft.png");
		
		x = rand.nextInt(main.level.spawnradius)-main.level.spawnradius/2;
		y = main.ground.getGroundLevel(x, image);
	}
	
	public void render(Main main){
		main.batch.end();
		
		main.shapeRenderer.begin(ShapeType.Filled);
		main.shapeRenderer.setColor(Color.RED);
//		main.shapeRenderer.rect(x+2, y+25-13, 75, 13);
		main.shapeRenderer.rect(x+2, y+25-13, image.getHeight()/2-2, image.getHeight()/2+13-25, 75, 13, 1, 1, fullrotation);
		main.shapeRenderer.setColor(Color.GREEN);
//		main.shapeRenderer.rect(x+2, y+25-13, 75/fullenergy * energy, 13);
		main.shapeRenderer.rect(x+2, y+25-13, image.getHeight()/2-2, image.getHeight()/2+13-25, 75/fullenergy * energy, 13, 1, 1, fullrotation);

		main.shapeRenderer.end();
		
		main.batch.begin();
		
		if(isCharging){
//			main.batch.draw(charging, x, y);
			main.batch.draw(charging, x, y, image.getWidth()/2, image.getHeight()/2, image.getWidth(), image.getHeight(), 1, 1, fullrotation, 0, 0, image.getWidth(), image.getHeight(), false, false);
		}else{
			main.batch.draw(image, x, y, image.getWidth()/2, image.getHeight()/2, image.getWidth(), image.getHeight(), 1, 1, fullrotation, 0, 0, image.getWidth(), image.getHeight(), false, false);
		}
	}
	
	public void update(Main main, double delta){
		if(isCharging){
			float speed = (float) (250 * delta);
			if(main.battery.energy<main.battery.fullenergy && energy>0){
				main.battery.energy += speed;
				if(main.battery.energy>main.battery.fullenergy){
					main.battery.energy = main.battery.fullenergy;
				}
				energy -= speed;
				if(energy<0){
					energy = 0;
				}
			}
		}
		
		if(fullani){
			float rotationspeed = (float) (250*delta);
			switch(stage){
			case 0:
//				main.screen.rotateAround(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), new Vector3(0,0,1), rotationspeed);
				fullrotation += rotationspeed;
				if(fullrotation>15){
					stage = 1;
				}
				break;
			case 1:
//				main.screen.rotateAround(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), new Vector3(0,0,1), -rotationspeed);
				fullrotation -= rotationspeed;
				if(fullrotation<-15){
					stage = 2;
				}
				break;
			case 2:
//				main.screen.rotateAround(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), new Vector3(0,0,1), rotationspeed);
				fullrotation += rotationspeed;
				if(fullrotation>0){
//					main.screen.rotateAround(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), new Vector3(0,0,1), -fullrotation);
					fullrotation = 0;
					fullani = false;
					stage = 0;
				}
				break;
			}
		}
	}
}
