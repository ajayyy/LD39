package com.ajayinkingston.ld39;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class Ground {
	float groundlevel = 100;
	
	ArrayList<Incline> inclines = new ArrayList<Incline>();
	
	float buffereddistance;
	float chunksize = 2000;
	
	Texture image;
	Texture normal;
	
	Random rand = new Random();
	public Ground(){
		image = new Texture("ground.png");
		normal = new Texture("normalground.png");
		
//		inclines.add(new Incline(100, 200, image.getWidth()*2));
//		
//		inclines.add(new Incline(100+image.getWidth()*2, 300, image.getWidth()*2));
//		
//		inclines.add(new Incline(100+image.getWidth()*4, 175, image.getWidth()*2));

		generate(0);
	}
	
	public void generate(float cx){
		float x = cx - chunksize;
		System.out.println(x + "wd");
		while(x<cx+chunksize/2){
			if(rand.nextInt(10)>3){
				x+=rand.nextInt(3+1)-1;
				continue;
			}
			int height = rand.nextInt(2);
			if(height == 0) height = 3;
			if(height==1) height = 5;
			int remainingspace = (int) ((cx+chunksize - x));
			System.out.println(height + " d " + remainingspace + " " + (cx+chunksize - x) + " " + x);
			float stepsizemax = ((float)remainingspace)/height;
			float stepsizemin = 1;
			if(stepsizemax < stepsizemin){
//				System.out.println("SDAsdasad");
//				return;
				if(remainingspace < 1){
					break;
				}
				continue;
			}
			for(int i=0;i<height;i++){
				remainingspace = (int) ((cx+chunksize - x));
				stepsizemax = ((float)remainingspace)/(height-i);
				System.out.println(remainingspace + " " + stepsizemax + " as");
				if(stepsizemax<=1) continue;
				int bigger = rand.nextInt(3);
				int length = rand.nextInt((int) (Math.min(stepsizemax, rand.nextInt(6-3)+3)-1))+1;
				if(length<3 && rand.nextDouble()>0.6){
					length = (int) Math.min(length+bigger, stepsizemax);
				}
				while (length<=0){
					length = rand.nextInt((int) (stepsizemax));
				}
				int stepheight = 1;
//				if(!halfsteptaken && halfstep && (rand.nextInt((int) height) == 0 || height-i<=1.5)){
//					stepheight = 1;
//					halfsteptaken = true;
//				}
				if(i>height/2-1){
//					stepheight = -stepheight;
					stepheight += (height/2);
					stepheight -= ((i-height/2));
//					if(halfsteptaken){
//						stepheight-=1;
//					}
				}else{
					stepheight += (i);
				}
				inclines.add(new Incline(x*image.getWidth(), (stepheight*2)*image.getHeight()+groundlevel, length*image.getWidth()));
//				inclines.add(new Incline(100+image.getWidth()*4, 175, image.getWidth()*2));
				System.out.println(x + " " + ((stepheight)*image.getHeight()+groundlevel) + " " + stepheight + " " + i);
				x+=length;
			}
		}
	}
	
	public void render(Main main){
		for(Incline incline: inclines){
			int xi=0;
			int yi = 0;
			while(true){
				yi=0;
				while(true){
					Vector3 pos = main.cam.project(new Vector3(incline.x + xi*image.getWidth(), incline.y - yi*image.getHeight(), 0));
					if(pos.y+image.getHeight()<0 || pos.y>Gdx.graphics.getHeight() || pos.x+image.getWidth()<0 || pos.x>Gdx.graphics.getWidth()){
						break;
					}
					main.batch.draw(image, incline.x + xi*image.getWidth(), incline.y - image.getHeight() - yi*image.getHeight());
					yi++;
				}
				xi++;
				if(incline.x + xi*image.getWidth() +image.getWidth()>incline.x+incline.width){
					break;
				}
			}
		}
		
		int xi=0;
		int yi = 0;
		Vector3 screenright = main.cam.unproject(new Vector3(Gdx.graphics.getWidth(),0,0));
		while(true){
			yi=0;
			while(true){
				Vector3 pos = main.cam.project(new Vector3(xi*image.getWidth()+(yi%2)*image.getWidth()/2, groundlevel - yi*image.getHeight(), 0));
				if(pos.y+image.getHeight()<0 || pos.y>Gdx.graphics.getHeight() || pos.x+image.getWidth()*1.5<0 || pos.x>Gdx.graphics.getWidth()){
					break;
				}
				main.batch.draw(image, xi*image.getWidth()+(yi%2)*image.getWidth()/2, groundlevel - image.getHeight() - yi*image.getHeight());
				yi++;
			}
			xi++;
			if(xi*image.getWidth()-image.getWidth()/2>screenright.x){
				break;
			}
		}
		xi=0;
		yi = 0;
		Vector3 screenleft = main.cam.unproject(new Vector3(0,0,0));
		while(true){
			yi=0;
			while(true){
				Vector3 pos = main.cam.project(new Vector3(xi*image.getWidth()+(yi%2)*image.getWidth()/2, groundlevel - yi*image.getHeight(), 0));
				if(pos.y+image.getHeight()<0 || pos.y>Gdx.graphics.getHeight() || pos.x+image.getWidth()*1.5<0 || pos.x>Gdx.graphics.getWidth()){
					break;
				}
				main.batch.draw(image, xi*image.getWidth()+(yi%2)*image.getWidth()/2, groundlevel - image.getHeight() - yi*image.getHeight());
				yi++;
			}
			if(xi*image.getWidth() + image.getWidth()/2+image.getWidth()<screenleft.x){
				break;
			}
			xi--;
		}
		
		if(main.player.flashing || main.start){
			main.batch.end();
			if(!main.start){
				Gdx.gl.glClearDepthf(1.0f);
			    Gdx.gl.glClear(GL30.GL_DEPTH_BUFFER_BIT);
			    Gdx.gl.glColorMask(false, false, false, false);
			    Gdx.gl.glDepthFunc(GL20.GL_LESS);
			    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
			    Gdx.gl.glDepthMask(true);
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
			    Gdx.gl.glColorMask(true, true, true, true);
			    Gdx.gl.glDepthMask(true);
			    Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
			}
		    main.batch.begin();
		    
		    for(Incline incline: inclines){
				xi=0;
				yi = 0;
				while(true){
					yi=0;
					while(true){
						Vector3 pos = main.cam.project(new Vector3(incline.x + xi*image.getWidth(), incline.y - yi*image.getHeight(), 0));
						if(pos.y+normal.getHeight()<0 || pos.y>Gdx.graphics.getHeight() || pos.x+normal.getWidth()<0 || pos.x>Gdx.graphics.getWidth()){
							break;
						}
						main.batch.draw(normal, incline.x + xi*normal.getWidth(), incline.y - normal.getHeight() - yi*normal.getHeight());
						yi++;
					}
					xi++;
					if(incline.x + xi*image.getWidth() +image.getWidth()>incline.x+incline.width){
						break;
					}
				}
			}
			
			xi=0;
			yi = 0;
			screenright = main.cam.unproject(new Vector3(Gdx.graphics.getWidth(),0,0));
			while(true){
				yi=0;
				while(true){
					Vector3 pos = main.cam.project(new Vector3(xi*normal.getWidth()+(yi%2)*normal.getWidth()/2, groundlevel - yi*normal.getHeight(), 0));
					if(pos.y+normal.getHeight()<0 || pos.y>Gdx.graphics.getHeight() || pos.x+normal.getWidth()*1.5<0 || pos.x>Gdx.graphics.getWidth()){
						break;
					}
					main.batch.draw(normal, xi*normal.getWidth()+(yi%2)*normal.getWidth()/2, groundlevel - normal.getHeight() - yi*normal.getHeight());
					yi++;
				}
				xi++;
				if(xi*normal.getWidth()-normal.getWidth()/2>screenright.x){
					break;
				}
			}
			xi=0;
			yi = 0;
			screenleft = main.cam.unproject(new Vector3(0,0,0));
			while(true){
				yi=0;
				while(true){
					Vector3 pos = main.cam.project(new Vector3(xi*normal.getWidth()+(yi%2)*normal.getWidth()/2, groundlevel - yi*normal.getHeight(), 0));
					if(pos.y+normal.getHeight()<0 || pos.y>Gdx.graphics.getHeight() || pos.x+normal.getWidth()*1.5<0 || pos.x>Gdx.graphics.getWidth()){
						break;
					}
					main.batch.draw(normal, xi*normal.getWidth()+(yi%2)*normal.getWidth()/2, groundlevel - normal.getHeight() - yi*normal.getHeight());
					yi++;
				}
				if(xi*normal.getWidth() + normal.getWidth()/2+normal.getWidth()<screenleft.x){
					break;
				}
				xi--;
			}
			
			if(!main.start){
				main.batch.end();
			    Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
				main.batch.begin();
			}
		}
	}
	
	public boolean isTouching(Player player){
		return player.y <= getGroundLevel(player);
	}
	
	public float getGroundLevel(Player player){
		return getGroundLevel(player.x, player.image);
	}
	
	public float getGroundLevel(float x, Texture image){
		float highest = 0;
		for(Incline incline: inclines){
			if(x+image.getWidth()>incline.x && x<incline.x+incline.width){
				if(incline.y > highest) highest = incline.y;
			}
		}
		
		if(groundlevel > highest) highest = groundlevel;
		
		return highest;
	}
}
