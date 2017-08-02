package com.ajayinkingston.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Battery {
	int fullenergy = 1000;
	int energy = fullenergy;
	
	float x,y;
	
	Texture image;
	Texture batterycharging;
	
	boolean emptyani;
	float emptyrotation;
	int stage;
	
	boolean touched;
	
	public Battery(){
		image = new Texture("battery.png");
		batterycharging = new Texture("batterycharging.png");
	}
	
	public void render(Main main){
		main.batch.end();
		x = 15;
		y = Gdx.graphics.getHeight()-image.getHeight()-15;
		
		Texture image = this.image;
		if(main.charger.isCharging) image = batterycharging;
		
		main.batteryShapeRenderer.begin(ShapeType.Filled);
		main.batteryShapeRenderer.setColor(Color.RED);
		main.batteryShapeRenderer.rect(x+2, y+7, image.getWidth()-3, image.getHeight()-25);
		main.batteryShapeRenderer.setColor(Color.GREEN);
		main.batteryShapeRenderer.rect(x+2, y+7, image.getWidth()-3, (image.getHeight()-25)/(float)fullenergy * energy);
//		System.out.println(image.getHeight()/fullenergy);
		main.batteryShapeRenderer.end();
		
		main.batteryBatch.begin();
		main.batteryBatch.draw(image, x, y);
		main.batteryBatch.end();
		
		main.batch.begin();
	}
	
	public void update(Main main, double delta){
		if(main.player.flashing){
			energy -= 250*delta;
			if(energy<0){
				energy = 0;
				emptyani = true;
				main.blurb = new Blurb("Battery empty", 1.5f);
			}
		}else if(Gdx.input.isTouched() && !touched){
			emptyani = true;
			if(energy<=0){
				main.blurb = new Blurb("Battery empty", 1.5f);
			}
		}
		touched = Gdx.input.isTouched();
		
		if(emptyani){
			float rotationspeed = (float) (250*delta);
			switch(stage){
			case 0:
//				main.screen.rotate(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), (float) (50*delta));
				main.batterycam.rotateAround(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), new Vector3(0,0,1), rotationspeed);
				emptyrotation += rotationspeed;
				if(emptyrotation>15){
					stage = 1;
				}
				break;
			case 1:
				main.batterycam.rotateAround(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), new Vector3(0,0,1), -rotationspeed);
				emptyrotation -= rotationspeed;
				if(emptyrotation<-15){
					stage = 2;
				}
				break;
			case 2:
				main.batterycam.rotateAround(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), new Vector3(0,0,1), rotationspeed);
				emptyrotation += rotationspeed;
				if(emptyrotation>0){
					main.batterycam.rotateAround(new Vector3(x+image.getWidth()/2,y+image.getHeight()/2,0), new Vector3(0,0,1), -emptyrotation);
					emptyrotation = 0;
					emptyani = false;
					stage = 0;
				}
				break;
			}
		}
	}
}
