package com.ajayinkingston.ld39;

import com.badlogic.gdx.graphics.Texture;

public class Zombie extends Enemy{

	float speed = 200;
	
	public Zombie(float x, float y) {
		super(x, y);
		
		image = new Texture("zombie.png");
	}
	
	public void render(Main main){
		main.batch.draw(image, x, y);
	}
	
	public void update(Main main, double delta){
		super.update(main, delta);
		if(!toymode){
			yspeed -= 1000 * delta;
			if(main.player.x>x+image.getWidth()/2){
				x+=speed*delta;
				for(Incline incline: main.ground.inclines){
					if(incline.x + incline.width> x && incline.x < x + image.getWidth()){
						if(incline.y>y){
							x = incline.x - image.getWidth();
							yspeed = 400;
						}
					}
				}
			}else{
				x-=speed*delta;
				for(Incline incline: main.ground.inclines){
					if(incline.x + incline.width> x && incline.x < x + image.getWidth()){
						if(incline.y>y){
							x = incline.x + incline.width;
							yspeed = 400;
						}
					}
				}
			}
			
			y += yspeed * delta;
			float groundlevel = main.ground.getGroundLevel(x, image);
			if(y<groundlevel){
				y = groundlevel;
				yspeed = 0;
			}
		}
	}

}
