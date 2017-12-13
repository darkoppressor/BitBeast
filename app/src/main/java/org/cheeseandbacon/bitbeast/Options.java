/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;

public class Options{
	private static Options instance;
	
	static boolean pause;
	static int temp_units;
	static int units;
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

	static boolean dev;
	
	private Options(){
	}
	
	public static synchronized void startup(){
		get_instance();
		
		init();
	}
	
	private static synchronized void get_instance(){
		if(instance==null){
			instance=new Options();
		}
	}
	
	private static synchronized void init(){
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

		dev = false;
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
