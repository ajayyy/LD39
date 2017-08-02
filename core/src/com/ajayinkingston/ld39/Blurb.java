package com.ajayinkingston.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Blurb {
	
	String message;
	float seconds;
	long last;
	
	boolean disabled;
	
	GlyphLayout layout;
	
	BitmapFont font;
	
	public Blurb(String message, float seconds){
		this.message = message;
		this.seconds = seconds;
		
		font = new BitmapFont(Gdx.files.internal("ubuntu.fnt"),Gdx.files.internal("ubuntu.png"),false);
		layout = new GlyphLayout(font, message);
		
		last = System.currentTimeMillis();
		update(null, 1/60);
	}
	
	public void render(Main main){
		if(disabled) return;
		main.batch.end();
		main.screenBatch.begin();
		font.setColor(Color.WHITE);
		font.draw(main.screenBatch, message, Gdx.graphics.getWidth()/2-layout.width/2, Gdx.graphics.getHeight()*0.2f);
		main.screenBatch.end();
		main.batch.begin();
	}
	
	public void update(Main main, double delta){
		if(disabled || seconds == -1) return;
		if(System.currentTimeMillis() - last >= seconds * 1000){
			disabled = true;
		}
	}
}
