package com.ajayinkingston.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class Wall {
	Texture image;
	Texture normal;
	Texture bed;
	public Wall() {
		image = new Texture("wall.png");
		normal = new Texture("normal.png");
		bed = new Texture("bed.png");
	}
	
	public void render(Main main){
		Vector3 pos = main.cam.unproject(new Vector3(0,0,0));
		int start = (int) pos.x/image.getWidth() - 1;
		float endy = pos.y + 2;
		
		Vector3 pos2 = main.cam.unproject(new Vector3(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),0));
		float endx = pos2.x;
		int x = start;
		int y = (int) (pos2.y/image.getHeight() - 1);
		while(y*image.getHeight()<endy+image.getHeight()){
			x = start;
			while(x*image.getWidth()<endx){
				main.batch.draw(image, x*image.getWidth(), y*image.getHeight()+(x%2)*image.getHeight()/2);
				x++;
			}
			y++;
		}
		
		if(main.player.flashing || main.start){
			main.batch.end();
			if(!main.start){
				Gdx.gl.glClearDepthf(1.0f);
			    Gdx.gl.glClear(GL30.GL_DEPTH_BUFFER_BIT);
			    // Disable writing to frame buffer and
			    // Set up the depth test
			    Gdx.gl.glColorMask(false, false, false, false);
			    Gdx.gl.glDepthFunc(GL20.GL_LESS);
			    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
			    Gdx.gl.glDepthMask(true);
			    //Here add your mask shape rendering code i.e. rectangle
			    //triangle, or other polygonal shape mask
			    main.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			    main.shapeRenderer.setColor(1f, 1f, 1f, 0.5f);
			    
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
	//		    float[] rotated = getRotated(x1,y1,main.player.flashlightx+main.player.flashlight.getWidth(),main.player.flashlighty+main.player.flashlight.getHeight()/2,main.player.flashlightAngle);
	//		    x1 = rotated[0];
	//		    y1 = rotated[1];
			    float x2 = (float) (sprite1.getVertices()[SpriteBatch.X1] - Math.sin(main.player.flashlightAngle)*main.player.flashlight.getHeight()/2);
			    float y2 = (float) (sprite1.getVertices()[SpriteBatch.Y1] + Math.cos(main.player.flashlightAngle)*main.player.flashlight.getHeight()/2);
	//		    rotated = getRotated(x2,y2,main.player.flashlightx+main.player.flashlight.getWidth(),main.player.flashlighty+main.player.flashlight.getHeight()/2,main.player.flashlightAngle);
	//		    x2 = rotated[0];
	//		    y2 = rotated[1];
			    float x3 = sprite.getVertices()[SpriteBatch.X2];
			    float y3 = sprite.getVertices()[SpriteBatch.Y2];
	//		    rotated = getRotated(x3,y3,main.player.flashlightx+main.player.flashlight.getWidth(),main.player.flashlighty+main.player.flashlight.getHeight()/2,main.player.flashlightAngle);
	//		    x3 = rotated[0];
	//		    y3 = rotated[1];
			    main.shapeRenderer.triangle(x1,y1,x2,y2,x3,y3);
			    main.shapeRenderer.end();
			    // Enable writing to the FrameBuffer
			    // and set up the texture to render with the mask
			    // applied
			    Gdx.gl.glColorMask(true, true, true, true);
			    Gdx.gl.glDepthMask(true);
			    Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
			}
		    main.batch.begin();

		    pos = main.cam.unproject(new Vector3(0,0,0));
			start = (int) pos.x/normal.getWidth() - 1;
			endy = pos.y + 2;
			
			pos2 = main.cam.unproject(new Vector3(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),0));
			endx = pos2.x + normal.getWidth();
			x = start;
			y = (int) (pos2.y/normal.getHeight() - 1);
			while(y*normal.getHeight()<endy+normal.getHeight()){
				x = start;
				while(x*image.getWidth()<endx){
					main.batch.draw(normal, x*normal.getWidth(), y*normal.getHeight()+(x%2)*normal.getHeight()/2);
					x++;
				}
				y++;
			}
			main.batch.draw(bed,main.charger.x - 100 - bed.getWidth(), main.ground.getGroundLevel(main.charger.x - 100 - bed.getWidth(),bed));
		    
			if(!main.start){
				main.batch.end();
			    Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
				main.batch.begin();
			}
		}
	}
	
	public static float[] getRotated(float x, float y, float xaxis, float yaxis, double rotation){
		float tempX = x - yaxis;
		float tempY = y - yaxis;

		// now apply rotation
		float rotatedX = (float) (tempX*Math.cos(rotation) - tempY*Math.sin(rotation));
		float rotatedY = (float) (tempX*Math.sin(rotation) + tempY*Math.cos(rotation));
		
//		rect.x = (float) ((rect.x  - cx) * Math.cos(rotation) - (rect.y - cy) * Math.sin(rotation) + cx);
//		rect.y = (float) ((rect.y - cy) * Math.cos(rotation) + (rect.x - cx) * Math.sin(rotation) + cy);

//		// translate back
		x = rotatedX + yaxis;
		y = rotatedY + yaxis;
		
//		rect.width = (float) (rect.width*Math.cos(rotation));
//		rect.height = (float) (rect.height*Math.sin(rotation));

		return new float[] {x,y};
	}
}
