package com.ajayinkingston.ld39;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue.Triangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class Enemy {
	float x,y;
	
	float yspeed;
	boolean toymode;
	
	long last;
	
	Texture image;
	
	Random rand = new Random();
	public Enemy(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void render(Main main){
		
	}
	
	public void update(Main main, double delta){
		Sprite sprite = new Sprite(main.player.flash);
	    sprite.translate(main.player.flashlightx-main.player.flash.getWidth()+main.player.flashlight.getWidth(), main.player.flashlighty-main.player.flash.getHeight()/2+main.player.flashlight.getHeight()/2);
	    sprite.setOrigin(main.player.flash.getWidth(), main.player.flash.getHeight()/2);
	    sprite.setRotation((float) Math.toDegrees(main.player.flashlightAngle));
	    Sprite sprite1 = new Sprite(main.player.flashlight);
	    sprite1.translate(main.player.flashlightx, main.player.flashlighty);
	    sprite1.setOrigin(main.player.flashlight.getWidth(), main.player.flashlight.getHeight()/2);
	    sprite1.setRotation((float) Math.toDegrees(main.player.flashlightAngle));
	    float x1 = sprite.getVertices()[SpriteBatch.X1];
	    float y1 = sprite.getVertices()[SpriteBatch.Y1];
	    float x2 = (float) (sprite1.getVertices()[SpriteBatch.X1] - Math.sin(main.player.flashlightAngle)*main.player.flashlight.getHeight()/2);
	    float y2 = (float) (sprite1.getVertices()[SpriteBatch.Y1] + Math.cos(main.player.flashlightAngle)*main.player.flashlight.getHeight()/2);
	    float x3 = sprite.getVertices()[SpriteBatch.X2];
	    float y3 = sprite.getVertices()[SpriteBatch.Y2];
		
		Polygon triangle = new Polygon(new float[]{x1,y1,x2,y2,x3,y3});
		
		Polygon enemy = new Polygon(new float[]{x,y,x,y+image.getHeight(),x+image.getWidth(),y+image.getHeight(),y,x+image.getWidth()});
		
		if(main.player.flashing && !toymode){
			if(Intersector.overlapConvexPolygons(triangle, enemy)){
				image = main.toy.images[rand.nextInt(main.toy.images.length)];
				y += image.getHeight();
				toymode = true;
				last = System.currentTimeMillis();
			}
		}
		if(toymode){
			yspeed -= 1000 * delta;
			y += yspeed * delta;
			float groundlevel = main.ground.getGroundLevel(x, image);
			if(y<groundlevel){
				y = groundlevel;
				yspeed = 0;
			}
			if(System.currentTimeMillis() - last >= 7000){
				main.enemies.remove(this);
			}
		}
		
	}
}
