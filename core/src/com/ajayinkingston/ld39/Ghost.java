package com.ajayinkingston.ld39;

import com.badlogic.gdx.graphics.Texture;

public class Ghost extends Enemy{

	double bobheight;
	boolean bobflip;
	float bobspeed = 75;
	float speed = 200;
	
	public Ghost(float x, float y) {
		super(x, y);
		
		image = new Texture("ghost.png");
	}
	
	public void render(Main main){
		main.batch.draw(image, x, y);
	}
	
	public void update(Main main, double delta){
		super.update(main, delta);
		if(!toymode){
			double angle = Math.atan2(main.player.y - y, main.player.x - x);
			double distance = Math.sqrt(Math.pow(main.player.x - x, 2) + Math.pow(main.player.y - y, 2));
			x += Math.cos(angle) * speed * delta;
			y += Math.sin(angle) * speed * delta;
			
			if(bobflip){
				y -= bobspeed*delta;
				bobheight -= bobspeed*delta;
				if(bobheight<-50){
					bobflip = false;
				}
				if(Math.sin(angle) * 1000/distance * speed * delta < 0){
					y += Math.sin(angle) * 1000/distance * speed * delta;
				}
			}else{
				y += bobspeed*delta;
				bobheight += bobspeed*delta;
				if(bobheight>50){
					bobflip = true;
				}
				if(Math.sin(angle) * 1000/distance * speed * delta > 0){
					y += Math.sin(angle) * 1000/distance * speed * delta;
				}
			}
		}
	}
	
}
