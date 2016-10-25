package org.cheeseandbacon.bitbeast;

public class Store_Section{
	static final int BEGIN=0;
	static final int FOOD=BEGIN;
	static final int DRINKS=1;
	static final int TREATMENTS=2;
	static final int PERMA=3;
	static final int END=4;
	
	public static int string_to_section(String string){
		if(string.equals("food")){
			return FOOD;
		}
		else if(string.equals("drinks")){
			return DRINKS;
		}
		else if(string.equals("treatments")){
			return TREATMENTS;
		}
		else if(string.equals("perma")){
			return PERMA;
		}
		else{
			return FOOD;
		}
	}
}
