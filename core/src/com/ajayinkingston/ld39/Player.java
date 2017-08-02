package com.ajayinkingston.ld39;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Player {
	float speed = 400;
	float yspeed;
	float x,y=400;
	float flashlightx = x;
	float flashlighty = y;
	
	Texture image;
	Texture flashlight;
	Texture flash;
	Texture heart;
	
	boolean flashing;
	boolean hasFlashlight = true;
	boolean washoldinge;

	boolean flashleft;
	
	boolean scaleani;
	float scale = -1;
	
	int lives = 3;
	
	double flashlightAngle;
	
	Sound jump;
	Sound flashsound;
	Sound ground;
	Sound dead;
	public Player(Main main){
		image = new Texture("player.png");
		
		flashlight = new Texture("flashlight.png");
		
		flash = new Texture("flash.png");
		
		heart = new Texture("heart.png");
		
		jump = Gdx.audio.newSound(Gdx.files.internal("data/jump.ogg"));
		flashsound = Gdx.audio.newSound(Gdx.files.internal("data/flash.ogg"));
		ground = Gdx.audio.newSound(Gdx.files.internal("data/ground.ogg"));
		dead = Gdx.audio.newSound(Gdx.files.internal("data/death.ogg"));

		x = main.charger.x;
		y = main.ground.getGroundLevel(this);
	}
	
	public void render(Main main){
//		main.batch.draw(image, x, y);
		main.batch.draw(image, x, y, image.getWidth()/2, image.getHeight()/2, image.getWidth(), image.getHeight(), scale, 1, 0, 0, 0, image.getWidth(), image.getHeight(), false, false);
		
		if(hasFlashlight){
			main.batch.draw(flashlight, flashlightx, flashlighty, flashlight.getWidth(), flashlight.getHeight()/2, flashlight.getWidth(), flashlight.getHeight(), 1, 1, (float) Math.toDegrees(flashlightAngle), 0, 0, flashlight.getWidth(), flashlight.getHeight(), false, false);
		
			if(flashing) main.batch.draw(flash, flashlightx-flash.getWidth()+flashlight.getWidth(), flashlighty-flash.getHeight()/2+flashlight.getHeight()/2, flash.getWidth(), flash.getHeight()/2, flash.getWidth(), flash.getHeight(), 1, 1, (float) Math.toDegrees(flashlightAngle), 0, 0, flash.getWidth(), flash.getHeight(), false, false);
		}
		
		main.batch.end();
		main.screenBatch.begin();
		for(int i=0;i<lives;i++){
			main.screenBatch.draw(heart, Gdx.graphics.getWidth()-heart.getWidth()*(i+1), Gdx.graphics.getHeight()-heart.getHeight());
		}
		main.screenBatch.end();
		main.batch.begin();
	}
	
	public void update(Main main, double delta){
		if(scaleani){
			if(flashleft){
				scale += 10 * delta;
				if(scale >= 1){
					scale = 1;
					scaleani = false;
				}
			}else{
				scale -= 10 * delta;
				if(scale <= -1){
					scale = -1;
					scaleani = false;
				}
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)){
			x+=speed * delta;
			
			for(Incline incline: main.ground.inclines){
				if(incline.x + incline.width> x && incline.x < x + image.getWidth()){
					if(incline.y>y){
						x = incline.x - image.getWidth();
					}
				}
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)){
			x-=speed * delta;
			
			for(Incline incline: main.ground.inclines){
				if(incline.x + incline.width> x && incline.x < x + image.getWidth()){
					if(incline.y>y){
						x = incline.x + incline.width;
					}
				}
			}
		}
		
		y+=yspeed*delta;
		
		if(main.ground.isTouching(this)){
			if(yspeed != 0){
//				ground.play(0.2f);
			}
			yspeed = 0;
			y = main.ground.getGroundLevel(this);
			if(Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.W)){
				yspeed = 700;
				jump.play(0.2f);
			}
		}else{
			yspeed -= 1800 * delta;
		}
		
		float lerp = 2f;
		main.cam.position.x += (((x) - main.cam.position.x) * lerp * delta);
		if(y>Gdx.graphics.getHeight()*0.5) main.cam.position.y += (((y) - main.cam.position.y + 50) * lerp * delta);
		else if(main.cam.position.y != 0){
			if(Math.abs(Gdx.graphics.getHeight()/2 - main.cam.position.y) < (Gdx.graphics.getHeight()/2 - main.cam.position.y) * lerp * delta){
				main.cam.position.y = 0;
			}else{
				main.cam.position.y += (Gdx.graphics.getHeight()/2 - main.cam.position.y) * lerp * delta;
			}
		}
		main.cam.update();

		if(Gdx.input.isKeyPressed(Keys.E) && !washoldinge){
			if(main.charger.x + main.charger.image.getWidth() > x && main.charger.x < x+image.getWidth() && main.charger.y+main.charger.image.getHeight()>y && main.charger.y<y+image.getHeight()){
				washoldinge = true;
				if(hasFlashlight){
					main.charger.isCharging = true;
					hasFlashlight = false;
					flashing = false;
				}else if(main.charger.isCharging){
					main.charger.isCharging = false;
					hasFlashlight = true;
				}
			}
		}else if(!Gdx.input.isKeyPressed(Keys.E)){
			washoldinge = false;
		}
		
		boolean wasflash = flashing;
		flashing = Gdx.input.isTouched() && (main.battery.energy>0) && hasFlashlight;
		if(flashing && !wasflash){
			flashsound.play(0.2f);
		}
//		System.out.println(flashlightAngle);
		Vector3 pos = main.cam.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
		
		double playerangle = Math.atan2(y - pos.y, x - pos.x);
		if(playerangle<-1.6 || playerangle>1.6){
			if(flashleft) scaleani = true;
			flashleft = false;
			flashlightx = x + image.getWidth() - flashlight.getWidth() - 20;
		}else{
			if(!flashleft) scaleani = true;
			flashleft = true;
			flashlightx = x - flashlight.getWidth() + 20;
		}
		flashlighty = y + image.getHeight()/2 - flashlight.getHeight()/2 - 15;
		
		flashlightAngle = Math.atan2(flashlighty - pos.y, flashlightx - pos.x);
		
		for(Enemy enemy: new ArrayList<Enemy>(main.enemies)){
			if(!enemy.toymode){
				if(x+image.getWidth()>enemy.x && x<enemy.x+enemy.image.getWidth() && y+image.getHeight()>enemy.y && y<enemy.y+enemy.image.getHeight()){
					lives--;
					main.enemies.remove(enemy);
					dead.play(0.3f);
					if(lives<=0){
						main.dead = true;
						main.endtime = System.currentTimeMillis();
					}
					break;
				}
			}
		}
	}
	
	public void dispose(){
		image.dispose();
	}
}
