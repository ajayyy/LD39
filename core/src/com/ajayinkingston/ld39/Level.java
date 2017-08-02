package com.ajayinkingston.ld39;

import java.util.Random;

public class Level {
	int[] stages;
	int[] types;
	int[] timings;
//	int[] x;
//	int[] y;
	
	long last1;
	long last;
	int stage;
	
	boolean done;
	
	int spawnradius = 4000;
	
	float seconds = 4;
	float amount = 1;
	
	Random rand = new Random();
	
	public Level(){
		stages = new int[]{1,2,4};
		types = new int[]{1,0,0};
		timings = new int[]{3,7,20};
		
		last = System.currentTimeMillis();
		last1 = last;
//		x = new int[]{1000,1000};
//		y = new int[]{600,600};
	}
	
	public void update(Main main, double delta){
//		if(!done && System.currentTimeMillis() - last > timings[stage]*1000){
//			for(int i=0;i<stages[stage];i++){
//				main.enemies.add(getEnemy(types[stage], rand.nextInt(spawnradius)-spawnradius/2, 500));
//			}
//			stage++;
//			if(stage >= stages.length){
//				done = true;
//			}
//		}
		
		if(!done && System.currentTimeMillis() - last1 >= seconds*1000){
			amount+= 0.3f;
//			if(seconds > 3) seconds -= seconds/5;
			if(amount<4) amount*=1.3;
			if(amount>40) amount = 40;
			for(int i=0;i<(int)amount;i++){
				int x = rand.nextInt(spawnradius)-spawnradius/2;
				while(Math.abs(x - main.player.x) < 1000){
					x  += 2000 * (rand.nextInt(2)-1);
				}
				main.enemies.add(getEnemy(rand.nextInt(2), x, 500));
			}
			last1 = System.currentTimeMillis();
		}
	}
	
	public static Enemy getEnemy(int type, float x, float y){
		switch(type){
		case 0:
			return new Ghost(x,y);
		case 1:
			return new Zombie(x, y);
		}
		return null;
	}
}
