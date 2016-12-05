package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;
import java.util.Random;

public class RNG {
	private static class Generator {
		private Random random = new Random();

		public Generator () {
			seed();
		}

		public Generator (long seed) {
			seed(seed);
		}

		public void seed () {
			seed(System.currentTimeMillis());
		}

		public void seed (long seed) {
			random.setSeed(seed);
		}

		public int random_range (int lownum, int highnum) {
			if(lownum>highnum){
				int temp=lownum;
				lownum=highnum;
				highnum=temp;
			}

			int range=highnum-lownum+1;

			return (Math.abs(random.nextInt())%range)+lownum;
		}
	}

	//The different RNGs
	private static final int BEGIN=0;
	private static final int STANDARD=BEGIN;
	static final int TICK=1;
	static final int BATTLE=2;
	private static final int END=3;
	
	private static RNG instance = null;
	private static ArrayList<Generator> rngs;
	
	private RNG(){
	}
	
	public static synchronized void startup(){
		get_instance();
		
		if(rngs==null){
			rngs=new ArrayList<>();
			
			for(int i=BEGIN;i<END;i++){
				rngs.add(new Generator());
			}
		}
	}
	
	private static synchronized RNG get_instance(){
		if(instance==null){
			instance=new RNG();
		}
		
		return instance;
	}
	
	public static synchronized void set_seed(int position,int seed){
		if(rngs==null){
			startup();
		}

		if (rngs.size()>=position+1) {
			rngs.get(position).seed(seed);
		}
	}
	
	public static synchronized int random_range(int lownum,int highnum){
		if(rngs==null){
			startup();
		}

		if (rngs.size()>=STANDARD+1) {
			return determine_random_number(rngs.get(STANDARD),lownum,highnum);
		} else {
			return 0;
		}
	}
	
	public static synchronized int random_range(int position,int lownum,int highnum){
		if(rngs==null){
			startup();
		}

		if (rngs.size()>=position+1) {
			return determine_random_number(rngs.get(position),lownum,highnum);
		} else {
			return 0;
		}
	}
	
	private static synchronized int determine_random_number(Generator generator, int lownum, int highnum){
		return generator.random_range(lownum, highnum);
	}
	
	public static synchronized void cleanup(){
		if(rngs!=null){
			rngs.clear();
			rngs=null;
		}
		
		if(instance!=null){
			instance=null;
		}
	}
}
