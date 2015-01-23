package org.cheeseandbacon.bitbeast;

public class Overlay_Type{
	static final int CLEAN_YARD=0;
	static final int CLEAN_PET=1;
	
	public static int get_sound_id(int type){
		switch(type){
		case CLEAN_YARD:
			return Sound.CLEAN;
		case CLEAN_PET:
			return Sound.BATHE;
		default:
			return Sound.CLEAN;
		}
	}
}
