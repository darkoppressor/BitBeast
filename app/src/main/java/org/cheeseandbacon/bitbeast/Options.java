package org.cheeseandbacon.bitbeast;

import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;


public class Options{
	private static Options instance;
	
	static boolean pause;
	static int temp_units;
	static int units;
	static int screen_orientation;
	static boolean keep_screen_on;
	static boolean vibrate;
	static boolean show_thermometer;
	//The minimum time between GPS updates, in milliseconds.
	static int gps_update_time;
	//The time the user would like the pet to fall asleep.
	//Sleep time can only be updated once a day.
	static Calendar desired_sleep_time;
	//The last time the sleep time was changed.
	static Calendar last_sleep_time_change;
	
	static boolean show_stat_bars;
	
	private Options(){
	}
	
	public static synchronized void startup(){
		get_instance();
		
		init();
	}
	
	public static synchronized Options get_instance(){
		if(instance==null){
			instance=new Options();
		}
		
		return instance;
	}
	
	public static synchronized void init(){
		/**int orientation=res.getConfiguration().orientation;
		if(orientation==Configuration.ORIENTATION_LANDSCAPE){
			screen_orientation=Screen_Orientation.LANDSCAPE;
		}
		else{
			screen_orientation=Screen_Orientation.PORTRAIT;
		}*/
		screen_orientation=Screen_Orientation.PORTRAIT;
		
		pause=false;
		temp_units=Temperature_Units.CELSIUS;
		units=Units.SI;
		keep_screen_on=true;
		vibrate=true;
		show_thermometer=true;
		gps_update_time=Gps_Update_Time.NORMAL;
		
		desired_sleep_time=Calendar.getInstance();
		desired_sleep_time.set(Calendar.HOUR_OF_DAY,22);
		desired_sleep_time.set(Calendar.MINUTE,0);
		
		last_sleep_time_change=Calendar.getInstance();
		last_sleep_time_change.add(Calendar.DAY_OF_YEAR,-1);
		
		show_stat_bars=true;
	}
	
	public static synchronized void cleanup(){
		if(instance!=null){
			instance=null;
		}
	}
	
	public static synchronized void set_keep_screen_on(Window window){
		if(keep_screen_on){
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		else{
			window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
	//This fixes screen orientation if it was saved using the old system where AUTO was an option.
	public static synchronized int get_fake_orientation(){
		if(screen_orientation==2){
			screen_orientation--;
		}
		
		return screen_orientation;
	}
	
	public static synchronized int get_orientation(boolean change_allowed){
		screen_orientation=get_fake_orientation();
		
		if(screen_orientation==Screen_Orientation.LANDSCAPE){
			return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}
		else if(screen_orientation==Screen_Orientation.PORTRAIT){
			return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
		else{
			return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
		/**else if(change_allowed){
			if(screen_orientation==Screen_Orientation.AUTO){
				return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
			}
			else{
				return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
			}
		}
		else{
			if(screen_orientation==Screen_Orientation.AUTO){
				return ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
			}
			else{
				return ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
			}
		}*/
	}
	
	public static synchronized long get_gps_update_time(){
		if(gps_update_time==Gps_Update_Time.VERY_FAST){
			return 1000;
		}
		else if(gps_update_time==Gps_Update_Time.FAST){
			return 30000;
		}
		else if(gps_update_time==Gps_Update_Time.NORMAL){
			return 60000;
		}
		else if(gps_update_time==Gps_Update_Time.SLOW){
			return 120000;
		}
		else{
			return 60000;
		}
	}
}
