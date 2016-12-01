package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.view.View;

import java.util.Calendar;

public class Holiday{
	public static final short NONE=0;
	public static final short VALENTINES=1;
	public static final short ST_PATRICKS=2;
	public static final short EASTER=3;
	public static final short HALLOWEEN=4;
	public static final short CHRISTMAS=5;
	
	public static void remove_perma_item(Pet_Status pet_status,String name){
		for(int i=0;i<pet_status.perma_items.size();i++){
			if(pet_status.perma_items.get(i).name.equals(name)){
				pet_status.perma_items.remove(i);
				
				i--;
			}
		}
	}
	
	public static void add_perma_item(View view,Pet_Status pet_status){
		short holiday=get();
		
		int x=0;
		int y=0;
		
		if(view!=null){
			x=RNG.random_range(0,view.getWidth()-Activity_Store.PERMA_ITEM_SIZE);
			y=RNG.random_range(0,view.getHeight()-Activity_Store.PERMA_ITEM_SIZE);
		}
		
		if(holiday==VALENTINES && !pet_status.has_perma_item("holiday_valentines")){
			pet_status.perma_items.add(new Perma_Item(null,null,"holiday_valentines",x,y));
	    }
	    else if(holiday==ST_PATRICKS && !pet_status.has_perma_item("holiday_st_patricks")){
	    	pet_status.perma_items.add(new Perma_Item(null,null,"holiday_st_patricks",x,y));
	    }
	    else if(holiday==EASTER && !pet_status.has_perma_item("holiday_easter")){
	    	pet_status.perma_items.add(new Perma_Item(null,null,"holiday_easter",x,y));
	    }
	    else if(holiday==HALLOWEEN && !pet_status.has_perma_item("holiday_halloween")){
	    	pet_status.perma_items.add(new Perma_Item(null,null,"holiday_halloween",x,y));
	    }
	    else if(holiday==CHRISTMAS && !pet_status.has_perma_item("holiday_christmas")){
	    	pet_status.perma_items.add(new Perma_Item(null,null,"holiday_christmas",x,y));
	    }
		
		if(holiday!=VALENTINES && pet_status.has_perma_item("holiday_valentines")){
			remove_perma_item(pet_status,"holiday_valentines");
	    }
	    if(holiday!=ST_PATRICKS && pet_status.has_perma_item("holiday_st_patricks")){
	    	remove_perma_item(pet_status,"holiday_st_patricks");
	    }
	    if(holiday!=EASTER && pet_status.has_perma_item("holiday_easter")){
	    	remove_perma_item(pet_status,"holiday_easter");
	    }
	    if(holiday!=HALLOWEEN && pet_status.has_perma_item("holiday_halloween")){
	    	remove_perma_item(pet_status,"holiday_halloween");
	    }
	    if(holiday!=CHRISTMAS && pet_status.has_perma_item("holiday_christmas")){
	    	remove_perma_item(pet_status,"holiday_christmas");
	    }
	}
	
	public static String get_string(){
		short holiday=get();
		
		if(holiday==VALENTINES){
	        return "Happy Valentine's Day!";
	    }
	    else if(holiday==ST_PATRICKS){
	        return "Are you wearing green?";
	    }
	    else if(holiday==EASTER){
	        return "Happy Easter!";
	    }
	    else if(holiday==HALLOWEEN){
	        return "Happy Halloween!";
	    }
	    else if(holiday==CHRISTMAS){
	        return "Christmas has been detected.";
	    }
	    else{
	    	return "";
	    }
	}
	
	public static int get_color(Resources res){
		short holiday=get();
		
		if(holiday==VALENTINES){
	        return res.getColor(R.color.holiday_valentines);
	    }
	    else if(holiday==ST_PATRICKS){
	    	return res.getColor(R.color.holiday_st_patricks);
	    }
	    else if(holiday==EASTER){
	    	return res.getColor(R.color.holiday_easter);
	    }
	    else if(holiday==HALLOWEEN){
	    	return res.getColor(R.color.holiday_halloween);
	    }
	    else if(holiday==CHRISTMAS){
	    	return res.getColor(R.color.holiday_christmas);
	    }
	    else{
	    	return res.getColor(R.color.font);
	    }
	}
	
	public static short get(){
		Calendar now=Calendar.getInstance();
		
		if(now.get(Calendar.MONTH)==Calendar.FEBRUARY && now.get(Calendar.DAY_OF_MONTH)>=8 && now.get(Calendar.DAY_OF_MONTH)<=14){
			return VALENTINES;
		}
		else if(now.get(Calendar.MONTH)==Calendar.MARCH && now.get(Calendar.DAY_OF_MONTH)>=11 && now.get(Calendar.DAY_OF_MONTH)<=17){
			return ST_PATRICKS;
		}
		else if(now.get(Calendar.MONTH)==Calendar.APRIL && now.get(Calendar.DAY_OF_MONTH)>=24 && now.get(Calendar.DAY_OF_MONTH)<=30){
			return EASTER;
		}
		else if(now.get(Calendar.MONTH)==Calendar.OCTOBER && now.get(Calendar.DAY_OF_MONTH)>=18 && now.get(Calendar.DAY_OF_MONTH)<=31){
			return HALLOWEEN;
		}
		else if(now.get(Calendar.MONTH)==Calendar.DECEMBER && now.get(Calendar.DAY_OF_MONTH)>=1 && now.get(Calendar.DAY_OF_MONTH)<=30){
			return CHRISTMAS;
		}
		else{
			return NONE;
		}
	}
	
	public static boolean friday_13(){
		Calendar now=Calendar.getInstance();
		
		if(now.get(Calendar.DAY_OF_MONTH)==13 && now.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean full_moon(){
		if(Moon_Phase.get()==Moon_Phase.FULL){
			return true;
		}
		else{
			return false;
		}
	}
}
