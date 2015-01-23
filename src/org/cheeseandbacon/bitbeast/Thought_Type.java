package org.cheeseandbacon.bitbeast;

public class Thought_Type{
	static final int HAPPY=0;
	static final int HOT=1;
	static final int COLD=2;
	static final int DIRTY=3;
	static final int SICK=4;
	static final int HUNGRY=5;
	static final int THIRSTY=6;
	static final int SAD=7;
	static final int MUSIC=8;
	static final int POOP=9;
	
	public static int get_sound_id(int type){
		return Sound.THOUGHT;
	}
}
