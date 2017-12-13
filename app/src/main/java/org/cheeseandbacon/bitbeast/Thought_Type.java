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

	public static String type_to_string (int type) {
	    switch (type) {
	        case HAPPY:
	            return "happy";
            case HOT:
                return "hot";
            case COLD:
                return "cold";
            case DIRTY:
                return "dirty";
            case SICK:
                return "sick";
            case HUNGRY:
                return "hungry";
            case THIRSTY:
                return "thirsty";
            case SAD:
                return "sad";
            case MUSIC:
                return "music";
            case POOP:
                return "poop";
            default:
                return "";
        }
    }
}
