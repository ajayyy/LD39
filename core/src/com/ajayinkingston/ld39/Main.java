package com.ajayinkingston.ld39;

import java.util.ArrayList;

import javax.xml.stream.events.StartDocument;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	SpriteBatch batteryBatch;
	SpriteBatch screenBatch;
	ShapeRenderer shapeRenderer;
	ShapeRenderer batteryShapeRenderer;
	OrthographicCamera cam;
	OrthographicCamera batterycam;
	OrthographicCamera screen;
	
	Level level;
	
	Player player;
	Ground ground;
	Battery battery;
	Charger charger;
	Pump pump;
	Toy toy;
	Wall wall;
	
	Blurb blurb;
	
	Music music;
	
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	boolean start = true;
	boolean intro;
	Texture title;
	Texture[] introimages = new Texture[4];
	int introimage;
	boolean dead;
	
	long starttime;
	long endtime;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		batteryBatch = new SpriteBatch();
		screenBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		batteryShapeRenderer = new ShapeRenderer();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();
		batterycam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batterycam.position.set(batterycam.viewportWidth / 2f, batterycam.viewportHeight / 2f, 0);
		batterycam.update();
		screen = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		screen.position.set(screen.viewportWidth / 2f, screen.viewportHeight / 2f, 0);
		screen.update();
		
		title = new Texture("title.png");
		for(int i=0;i<introimages.length;i++){
			introimages[i] = new Texture("title"+i+".png");
		}
		setup();
		music = Gdx.audio.newMusic(Gdx.files.internal("data/music.ogg"));
		music.setLooping(true);
	}
	
	public void setup(){
		enemies = new ArrayList<Enemy>();
		level = new Level();
		ground = new Ground();
		battery = new Battery();
		charger = new Charger(this);
		player = new Player(this);
		pump = new Pump(this);
//		enemies.add(new Ghost(1000, 200));
		toy = new Toy();
		wall = new Wall();
		blurb = new Blurb("This message should not appear", 0);
		
		starttime = System.currentTimeMillis();
	}
	
	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width*1f;
        cam.viewportHeight = height*1f;
        batterycam.viewportWidth = width*1f;
        batterycam.viewportHeight = height*1f;
        batterycam.position.set(batterycam.viewportWidth / 2f, batterycam.viewportHeight / 2f, 0);
		batterycam.update();
		screen.viewportWidth = width*1f;
		screen.viewportHeight = height*1f;
		screen.position.set(screen.viewportWidth / 2f, screen.viewportHeight / 2f, 0);
		screen.update();
    }

	@Override
	public void render () {
		batch.setProjectionMatrix(cam.combined);
		shapeRenderer.setProjectionMatrix(cam.combined);
		batteryBatch.setProjectionMatrix(batterycam.combined);
		batteryShapeRenderer.setProjectionMatrix(batterycam.combined);
		screenBatch.setProjectionMatrix(screen.combined);
//		cam.rotate(1);
		cam.update();
		batterycam.update();
		screen.update();
		
		draw();
		
		update();
	}
	
	public void draw(){
		music.play();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
		
		batch.begin();
//		screenBatch.begin();
//		screenShapeRenderer.begin(ShapeType.Filled);
		
		if(start){
			wall.render(this);
			ground.render(this);
			
			screenBatch.begin();
			screenBatch.draw(title, Gdx.graphics.getWidth()/2-title.getWidth()/2, Gdx.graphics.getHeight()-title.getHeight());
			screenBatch.end();
			
			batch.end();
			return;
		}else if(intro){
			
			batch.draw(introimages[introimage],0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			
			batch.end();
			return;
		}else if(dead){
//			blurb = new Blurb("You lasted " + ((starttime-endtime)/1000) + ":" + ((starttime-endtime)/60000) + "!", -1);
			batch.end();
			screenBatch.begin();
			blurb.font.setColor(Color.WHITE);
			String message = "You lasted " + ((endtime-starttime)/1000) + ":" + ((endtime-starttime)/60000) + "!";
			blurb.layout = new GlyphLayout(blurb.font, message);
			blurb.font.draw(screenBatch, message, Gdx.graphics.getWidth()/2-blurb.layout.width/2, Gdx.graphics.getHeight()*0.2f);
			message = "Click to restart!";
			blurb.layout = new GlyphLayout(blurb.font, message);
			blurb.font.draw(screenBatch, message, Gdx.graphics.getWidth()/2-blurb.layout.width/2, Gdx.graphics.getHeight()*0.4f);
			screenBatch.end();
			return;
		}
		
		wall.render(this);
		
		ground.render(this);
		charger.render(this);
		pump.render(this);
		
		for(Enemy enemy: new ArrayList<Enemy>(enemies)){
			enemy.render(this);
		}
		player.render(this);
		
		
		battery.render(this);
		blurb.render(this);
		
		batch.end();
//		screenBatch.end();
//		screenShapeRenderer.end();
	}
	
	public void update(){
		double delta = Gdx.graphics.getDeltaTime();
		if(delta>0.1) delta = 0.1;
		if(start){
			cam.position.x -= 2000*delta;
			
			if(Gdx.input.isTouched()){
				start = false;
				intro = true;
				cam.position.x = 0;
			}
			
			return;
		}else if(intro){			
			cam.position.x = Gdx.graphics.getWidth()/2;

			if(Gdx.input.justTouched()){
				introimage++;
				if(introimage>=introimages.length){
					intro = false;
					cam.position.x = 0;
					setup();
				}
			}
			return;
		}else if(dead){
			
			if(Gdx.input.justTouched()){
				dead = false;
				setup();
			}
			
			return;
		}

		player.update(this, delta);
		battery.update(this, delta);
		charger.update(this, delta);
		pump.update(this, delta);
		level.update(this, delta);
		blurb.update(this, delta);
		
		for(Enemy enemy: new ArrayList<Enemy>(enemies)){
			if(enemy == null) continue;
			enemy.update(this, delta);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();
	}
}
