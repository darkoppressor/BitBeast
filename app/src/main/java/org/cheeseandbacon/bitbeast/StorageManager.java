package org.cheeseandbacon.bitbeast;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StorageManager{
	private static final String TAG=StorageManager.class.getCanonicalName();
	private static final String SAVE_SUFFIX_TEMPORARY="_TEMPORARY";

	// Returns true if the file was saved successfully, else false
	private static boolean saveFile(Context context, String fileName, String data, boolean append) {
		try {
			int mode = Context.MODE_PRIVATE;

			if (append) {
				mode = Context.MODE_APPEND;
			}

			// Save the data to a temporary location
			FileOutputStream fileOutputStream = context.openFileOutput(fileName + SAVE_SUFFIX_TEMPORARY, mode);
			fileOutputStream.write(data.getBytes());
			fileOutputStream.close();

			// Rename the temporary file to the final file name
			File tempFile = context.getFileStreamPath(fileName + SAVE_SUFFIX_TEMPORARY);
			File finalFile = context.getFileStreamPath(fileName);
			return tempFile.renameTo(finalFile);
		} catch (FileNotFoundException e) {
			Log.w(TAG, "FileNotFoundException occurred while saving data to internal storage", e);
		} catch (IOException e) {
			Log.w(TAG, "IOException occurred while saving data to internal storage", e);
		} catch (SecurityException e) {
			Log.w(TAG, "SecurityException occurred while saving data to internal storage", e);
		} catch (NullPointerException e) {
			Log.w(TAG, "NullPointerException occurred while saving data to internal storage", e);
		}

		return false;
	}

	private static ArrayList<String> loadFile(Context context, String fileName) {
		ArrayList<String> data = new ArrayList<>();

		try {
			FileInputStream fileInputStream = context.openFileInput(fileName);
			byte[] bytes = new byte[(int)fileInputStream.getChannel().size()];
			fileInputStream.read(bytes);
			fileInputStream.close();

			//Convert the bytes into strings
			String raw_data = new String(bytes);
			String current_string="";
			for(int i=0;i<raw_data.length();i++){
				if(raw_data.charAt(i)!='\n'){
					current_string+=raw_data.charAt(i);
				}
				else{
					data.add(current_string);
					current_string="";
				}
			}
			if(data.size()==0){
				data.add(current_string);
			}
		} catch (FileNotFoundException e) {
			Log.w(TAG, "FileNotFoundException occurred while loading data from internal storage", e);
		} catch (IOException e) {
			Log.w(TAG, "IOException occurred while loading data from internal storage", e);
		}

		return data;
	}

	// Returns true if the file was deleted successfully, else false
	private static boolean deleteFile(Context context, String fileName) {
		return context.deleteFile(fileName);
	}
	
	public static void delete_pet_status(Context context){
		deleteFile(context, "pet");
		deleteFile(context, "pet2");
		deleteFile(context, "equipment");
		deleteFile(context, "perma_items");
	}

	// Returns true if the pet status was saved successfully, else false
	public static void save_pet_status(Context context,Pet_Status pet_status){
		//As long as the pet data is legitimate.
		if(pet_status.name.length()>0){
			String data="";

			data+=""+System.currentTimeMillis()+"\n";

			data+=""+pet_status.last_tick+"\n";

			data+=""+pet_status.type.toString()+"\n";

			data+=""+pet_status.age_tier.toString()+"\n";

			data+=""+pet_status.name+"\n";

			data+=""+pet_status.bits+"\n";

			data+=""+pet_status.age+"\n";

			data+=""+pet_status.death_counter+"\n";

			data+=""+pet_status.sleep_time.get(Calendar.HOUR_OF_DAY)+"\n";

			data+=""+pet_status.sleep_time.get(Calendar.MINUTE)+"\n";

			data+=""+pet_status.wake_time.get(Calendar.YEAR)+"\n";

			data+=""+pet_status.wake_time.get(Calendar.DAY_OF_YEAR)+"\n";

			data+=""+pet_status.wake_time.get(Calendar.HOUR_OF_DAY)+"\n";

			data+=""+pet_status.wake_time.get(Calendar.MINUTE)+"\n";

			data+=""+pet_status.sleeping+"\n";

			data+=""+pet_status.sleeping_woken_up+"\n";

			data+=""+pet_status.sleeping_woken_up_time+"\n";

			data+=""+pet_status.light+"\n";

			data+=""+pet_status.happy+"\n";

			data+=""+pet_status.base_temp+"\n";

			data+=""+pet_status.temp+"\n";

			data+=""+pet_status.last_base_temp+"\n";

			data+=""+pet_status.ac+"\n";

			data+=""+pet_status.heater+"\n";

			data+=""+pet_status.sick+"\n";

			data+=""+pet_status.poop+"\n";

			data+=""+pet_status.bath+"\n";

			data+=""+pet_status.weight+"\n";

			data+=""+pet_status.hunger+"\n";

			data+=""+pet_status.thirst+"\n";

			data+=""+pet_status.strength+"\n";

			data+=""+pet_status.strength_max+"\n";

			data+=""+pet_status.energy+"\n";

			data+=""+pet_status.color+"\n";

			//This is how favorite food was stored pre-1.1.0.
			int fav_food_old=-1;
			data+=""+fav_food_old+"\n";

			data+=""+pet_status.battles_won+"\n";

			data+=""+pet_status.battles_lost+"\n";

			for(int i=0;i<Food_Category.END;i++){
				data+=""+pet_status.food_categories.get(i)+"\n";
			}

			//This is how food fed was stored pre-1.1.0.
			int food_fed_size=0;
			data+=""+food_fed_size+"\n";

			data+=""+pet_status.poops.size()+"\n";

			for(int i=0;i<pet_status.poops.size();i++){
				data+=""+pet_status.poops.get(i).x+"\n";

				data+=""+pet_status.poops.get(i).y+"\n";
			}

			data+=""+pet_status.pettings+"\n";

			data+=""+pet_status.thoughts.size()+"\n";

			for(int i=0;i<pet_status.thoughts.size();i++){
				data+=""+pet_status.thoughts.get(i)+"\n";
			}

			data+=""+pet_status.dc_starving+"\n";

			data+=""+pet_status.dc_very_thirsty+"\n";

			data+=""+pet_status.dc_sick+"\n";

			data+=""+pet_status.dc_obese+"\n";

			data+=""+pet_status.dc_not_obese+"\n";

			data+=""+pet_status.dc_well_fed+"\n";

			data+=""+pet_status.dc_well_watered+"\n";

			data+=""+pet_status.dc_not_sick+"\n";

			saveFile(context, "pet", data, false);
	    	
	    	save_pet_status_2(context,pet_status);
	    	save_pet_status_equipment(context,pet_status);
	    	save_pet_status_perma_items(context,pet_status);
	    	
	    	AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
			int[] ids=appWidgetManager.getAppWidgetIds(new ComponentName(context,org.cheeseandbacon.bitbeast.Widget_Provider.class));
			
			Intent intent=new Intent(context,Widget_Provider.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
			
			context.sendBroadcast(intent);
		}
	}
	
	public static long load_pet_status(Context context,View view,Pet_Status pet_status){
		long ms_last_run;

		ArrayList<String> data=loadFile(context, "pet");

		ms_last_run=Long.parseLong(data.get(0).trim());
		data.remove(0);

		pet_status.last_tick=Long.parseLong(data.get(0).trim());
		data.remove(0);

		pet_status.type=Pet_Type.valueOf(data.get(0).trim());
		data.remove(0);

		pet_status.age_tier=Age_Tier.valueOf(data.get(0).trim());
		data.remove(0);

		pet_status.name=data.get(0).trim();
		data.remove(0);

		pet_status.bits=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.age=Long.parseLong(data.get(0).trim());
		data.remove(0);

		pet_status.death_counter=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.sleep_time.set(Calendar.HOUR_OF_DAY,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		pet_status.sleep_time.set(Calendar.MINUTE,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		pet_status.wake_time.set(Calendar.YEAR,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		pet_status.wake_time.set(Calendar.DAY_OF_YEAR,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		pet_status.wake_time.set(Calendar.HOUR_OF_DAY,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		pet_status.wake_time.set(Calendar.MINUTE,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		pet_status.sleeping=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		pet_status.sleeping_woken_up=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		pet_status.sleeping_woken_up_time=Long.parseLong(data.get(0).trim());
		data.remove(0);

		pet_status.light=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		pet_status.happy=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.base_temp=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.temp=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.last_base_temp=Long.parseLong(data.get(0).trim());
		data.remove(0);

		pet_status.ac=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		pet_status.heater=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		pet_status.sick=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		pet_status.poop=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.bath=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.weight=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.hunger=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.thirst=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.strength=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.strength_max=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.energy=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.color=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		//This is how favorite food was stored pre-1.1.0.
		int fav_food_old=0;
		fav_food_old=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.battles_won=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.battles_lost=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.food_categories.clear();
		for(int i=0;i<Food_Category.END;i++){
			pet_status.food_categories.add(Integer.parseInt(data.get(0).trim()));
			data.remove(0);
		}

		int food_fed_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<food_fed_size;i++){
			//This is how food fed was stored pre-1.1.0.
			int food_fed_old=0;
			food_fed_old=Integer.parseInt(data.get(0).trim());
			data.remove(0);
		}

		pet_status.poops.clear();
		int poops_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<poops_size;i++){
			float x=Float.parseFloat(data.get(0).trim());
			data.remove(0);

			float y=Float.parseFloat(data.get(0).trim());
			data.remove(0);

			pet_status.poops.add(new Poop(null,null,x,y));
		}

		pet_status.pettings=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.thoughts.clear();
		int thoughts_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<thoughts_size;i++){
			pet_status.thoughts.add(Integer.parseInt(data.get(0).trim()));
			data.remove(0);
		}

		pet_status.dc_starving=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dc_very_thirsty=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dc_sick=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dc_obese=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dc_not_obese=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dc_well_fed=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dc_well_watered=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dc_not_sick=Short.parseShort(data.get(0).trim());
		data.remove(0);
    	
    	load_pet_status_2(context,pet_status);
    	load_pet_status_equipment(context,pet_status);
    	load_pet_status_perma_items(context,view,pet_status);
    	
    	return ms_last_run;
	}
	
	private static void save_pet_status_2(Context context, Pet_Status pet_status){
		String data="";

		int app_version=0;
		String app_versionname="";
		try{
			app_version=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
			app_versionname=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
		}
		catch(NameNotFoundException e){
			StorageManager.error_log_add(context,TAG,e.getMessage(),e);
		}
		data+=""+app_version+"\n";
		data+=""+app_versionname+"\n";

		data+=""+pet_status.experience_gain+"\n";

		data+=""+pet_status.favorite_food+"\n";

		data+=""+pet_status.food_fed.size()+"\n";

		for(int i=0;i<pet_status.food_fed.size();i++){
			data+=""+pet_status.food_fed.get(i)+"\n";
		}

		data+=""+pet_status.dexterity+"\n";

		data+=""+pet_status.dexterity_max+"\n";

		data+=""+pet_status.stamina+"\n";

		data+=""+pet_status.stamina_max+"\n";

		data+=""+pet_status.hunger_rise+"\n";
		data+=""+pet_status.thirst_rise+"\n";
		data+=""+pet_status.strength_rise+"\n";
		data+=""+pet_status.dexterity_rise+"\n";
		data+=""+pet_status.stamina_rise+"\n";
		data+=""+pet_status.energy_rise+"\n";

		data+=""+pet_status.battles_won_sp+"\n";
		data+=""+pet_status.battles_lost_sp+"\n";

		data+=""+pet_status.stat_points+"\n";
		data+=""+pet_status.level+"\n";
		data+=""+pet_status.experience+"\n";
		data+=""+pet_status.experience_max+"\n";

		data+=""+pet_status.buff_hunger+"\n";
		data+=""+pet_status.buff_thirst+"\n";
		data+=""+pet_status.buff_poop+"\n";
		data+=""+pet_status.buff_dirty+"\n";
		data+=""+pet_status.buff_weight+"\n";
		data+=""+pet_status.buff_sick+"\n";
		data+=""+pet_status.buff_happy+"\n";
		data+=""+pet_status.buff_energy_regen+"\n";
		data+=""+pet_status.buff_strength_regen+"\n";
		data+=""+pet_status.buff_dexterity_regen+"\n";
		data+=""+pet_status.buff_stamina_regen+"\n";
		data+=""+pet_status.buff_energy_max+"\n";
		data+=""+pet_status.buff_strength_max+"\n";
		data+=""+pet_status.buff_dexterity_max+"\n";
		data+=""+pet_status.buff_stamina_max+"\n";
		data+=""+pet_status.buff_death+"\n";
		data+=""+pet_status.buff_magic_find+"\n";

		saveFile(context, "pet2", data, false);
	}
	
	private static void load_pet_status_2(Context context, Pet_Status pet_status){
		ArrayList<String> data=loadFile(context, "pet2");

		int app_version=0;
		String app_versionname="";

		app_version=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		app_versionname=data.get(0).trim();
		data.remove(0);

		pet_status.experience_gain=Long.parseLong(data.get(0).trim());
		data.remove(0);

		pet_status.favorite_food=data.get(0).trim();
		data.remove(0);

		pet_status.food_fed.clear();
		int food_fed_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<food_fed_size;i++){
			pet_status.food_fed.add(data.get(0).trim());
			data.remove(0);
		}

		pet_status.dexterity=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dexterity_max=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.stamina=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.stamina_max=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.hunger_rise=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.thirst_rise=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.strength_rise=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.dexterity_rise=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.stamina_rise=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.energy_rise=Double.parseDouble(data.get(0).trim());
		data.remove(0);

		pet_status.battles_won_sp=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.battles_lost_sp=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.stat_points=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.level=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.experience=Long.parseLong(data.get(0).trim());
		data.remove(0);

		pet_status.experience_max=Long.parseLong(data.get(0).trim());
		data.remove(0);

		pet_status.buff_hunger=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_thirst=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_poop=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_dirty=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_weight=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_sick=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_happy=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_energy_regen=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_strength_regen=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_dexterity_regen=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_stamina_regen=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_energy_max=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_strength_max=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_dexterity_max=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_stamina_max=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_death=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.buff_magic_find=Integer.parseInt(data.get(0).trim());
		data.remove(0);
	}
	
	private static void save_pet_status_equipment(Context context, Pet_Status pet_status){
		String data="";

		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(pet_status.equipment_slots.get(i)==null){
				data+=""+false+"\n";
			}
			else{
				data+=""+true+"\n";

				data+=""+pet_status.equipment_slots.get(i).name+"\n";
				data+=""+pet_status.equipment_slots.get(i).full_name+"\n";
				data+=""+pet_status.equipment_slots.get(i).description+"\n";
				data+=""+pet_status.equipment_slots.get(i).level+"\n";
				data+=""+pet_status.equipment_slots.get(i).bits+"\n";
				data+=""+pet_status.equipment_slots.get(i).branch+"\n";
				data+=""+pet_status.equipment_slots.get(i).weight+"\n";

				data+=""+pet_status.equipment_slots.get(i).buff_hunger+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_thirst+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_poop+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_dirty+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_weight+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_sick+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_happy+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_energy_regen+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_strength_regen+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_dexterity_regen+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_stamina_regen+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_energy_max+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_strength_max+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_dexterity_max+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_stamina_max+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_death+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_magic_find+"\n";
			}
		}

		data+=""+pet_status.equipment.size()+"\n";

		for(int i=0;i<pet_status.equipment.size();i++){
			data+=""+pet_status.equipment.get(i).name+"\n";
			data+=""+pet_status.equipment.get(i).full_name+"\n";
			data+=""+pet_status.equipment.get(i).description+"\n";
			data+=""+pet_status.equipment.get(i).level+"\n";
			data+=""+pet_status.equipment.get(i).bits+"\n";
			data+=""+pet_status.equipment.get(i).branch+"\n";
			data+=""+pet_status.equipment.get(i).weight+"\n";

			data+=""+pet_status.equipment.get(i).buff_hunger+"\n";
			data+=""+pet_status.equipment.get(i).buff_thirst+"\n";
			data+=""+pet_status.equipment.get(i).buff_poop+"\n";
			data+=""+pet_status.equipment.get(i).buff_dirty+"\n";
			data+=""+pet_status.equipment.get(i).buff_weight+"\n";
			data+=""+pet_status.equipment.get(i).buff_sick+"\n";
			data+=""+pet_status.equipment.get(i).buff_happy+"\n";
			data+=""+pet_status.equipment.get(i).buff_energy_regen+"\n";
			data+=""+pet_status.equipment.get(i).buff_strength_regen+"\n";
			data+=""+pet_status.equipment.get(i).buff_dexterity_regen+"\n";
			data+=""+pet_status.equipment.get(i).buff_stamina_regen+"\n";
			data+=""+pet_status.equipment.get(i).buff_energy_max+"\n";
			data+=""+pet_status.equipment.get(i).buff_strength_max+"\n";
			data+=""+pet_status.equipment.get(i).buff_dexterity_max+"\n";
			data+=""+pet_status.equipment.get(i).buff_stamina_max+"\n";
			data+=""+pet_status.equipment.get(i).buff_death+"\n";
			data+=""+pet_status.equipment.get(i).buff_magic_find+"\n";
		}

		saveFile(context, "equipment", data, false);
	}
	
	private static void load_pet_status_equipment(Context context, Pet_Status pet_status){
		ArrayList<String> data=loadFile(context, "equipment");

		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			boolean slot_filled=Boolean.parseBoolean(data.get(0).trim());
			data.remove(0);

			if(slot_filled){
				pet_status.equipment_slots.set(i,new Equipment());

				pet_status.equipment_slots.get(i).name=data.get(0).trim();
				data.remove(0);

				pet_status.equipment_slots.get(i).full_name=data.get(0).trim();
				data.remove(0);

				pet_status.equipment_slots.get(i).description=data.get(0).trim();
				data.remove(0);

				pet_status.equipment_slots.get(i).level=Integer.parseInt(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).bits=Integer.parseInt(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).branch=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).weight=Double.parseDouble(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_hunger=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_thirst=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_poop=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_dirty=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_weight=Double.parseDouble(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_sick=Integer.parseInt(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_happy=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_energy_regen=Float.parseFloat(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_strength_regen=Float.parseFloat(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_dexterity_regen=Float.parseFloat(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_stamina_regen=Float.parseFloat(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_energy_max=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_strength_max=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_dexterity_max=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_stamina_max=Short.parseShort(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_death=Integer.parseInt(data.get(0).trim());
				data.remove(0);

				pet_status.equipment_slots.get(i).buff_magic_find=Integer.parseInt(data.get(0).trim());
				data.remove(0);
			}
			else{
				pet_status.equipment_slots.set(i,null);
			}
		}

		pet_status.equipment.clear();
		int equipment_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<equipment_size;i++){
			pet_status.equipment.add(new Equipment());

			pet_status.equipment.get(i).name=data.get(0).trim();
			data.remove(0);

			pet_status.equipment.get(i).full_name=data.get(0).trim();
			data.remove(0);

			pet_status.equipment.get(i).description=data.get(0).trim();
			data.remove(0);

			pet_status.equipment.get(i).level=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).bits=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).branch=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).weight=Double.parseDouble(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_hunger=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_thirst=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_poop=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_dirty=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_weight=Double.parseDouble(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_sick=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_happy=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_energy_regen=Float.parseFloat(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_strength_regen=Float.parseFloat(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_dexterity_regen=Float.parseFloat(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_stamina_regen=Float.parseFloat(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_energy_max=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_strength_max=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_dexterity_max=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_stamina_max=Short.parseShort(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_death=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			pet_status.equipment.get(i).buff_magic_find=Integer.parseInt(data.get(0).trim());
			data.remove(0);
		}
	}
	
	private static void save_pet_status_perma_items(Context context, Pet_Status pet_status){
		String data="";

		data+=""+pet_status.perma_items.size()+"\n";

		for(int i=0;i<pet_status.perma_items.size();i++){
			data+=""+pet_status.perma_items.get(i).x+"\n";

			data+=""+pet_status.perma_items.get(i).y+"\n";

			data+=""+pet_status.perma_items.get(i).name+"\n";
		}

		saveFile(context, "perma_items", data, false);
	}
	
	private static void load_pet_status_perma_items(Context context, View view, Pet_Status pet_status){
		ArrayList<String> data=loadFile(context, "perma_items");

		pet_status.perma_items.clear();
		int perma_items_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<perma_items_size;i++){
			float x=Float.parseFloat(data.get(0).trim());
			data.remove(0);

			float y=Float.parseFloat(data.get(0).trim());
			data.remove(0);

			String name=data.get(0).trim();
			data.remove(0);

			pet_status.perma_items.add(new Perma_Item(null,null,name,x,y));
		}
    	
    	Holiday.add_perma_item(view,pet_status);
	}
	
	public static void save_options(Context context){
		String data="";

		data+=""+Options.pause+"\n";

		data+=""+Options.temp_units+"\n";

		data+=""+Options.units+"\n";

		data+=""+Options.screen_orientation+"\n";

		data+=""+Options.keep_screen_on+"\n";

		data+=""+Options.vibrate+"\n";

		data+=""+Options.show_thermometer+"\n";

		data+=""+Options.gps_update_time+"\n";

		data+=""+Options.desired_sleep_time.get(Calendar.HOUR_OF_DAY)+"\n";

		data+=""+Options.desired_sleep_time.get(Calendar.MINUTE)+"\n";

		data+=""+Options.last_sleep_time_change.get(Calendar.YEAR)+"\n";

		data+=""+Options.last_sleep_time_change.get(Calendar.DAY_OF_YEAR)+"\n";

		saveFile(context, "options", data, false);
    	
    	save_options_2(context);
	}
	
	public static void load_options(Context context){
		ArrayList<String> data=loadFile(context, "options");

		Options.pause=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		Options.temp_units=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		Options.units=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		Options.screen_orientation=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		Options.keep_screen_on=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		Options.vibrate=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		Options.show_thermometer=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);

		Options.gps_update_time=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		Options.desired_sleep_time.set(Calendar.HOUR_OF_DAY,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		Options.desired_sleep_time.set(Calendar.MINUTE,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		Options.last_sleep_time_change.set(Calendar.YEAR,Integer.parseInt(data.get(0).trim()));
		data.remove(0);

		Options.last_sleep_time_change.set(Calendar.DAY_OF_YEAR,Integer.parseInt(data.get(0).trim()));
		data.remove(0);
    	
    	load_options_2(context);
	}
	
	private static void save_options_2(Context context){
		String data="";

		data+=""+Options.show_stat_bars+"\n";

		saveFile(context, "options2", data, false);
	}
	
	private static void load_options_2(Context context){
		ArrayList<String> data=loadFile(context, "options2");

		Options.show_stat_bars=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);
	}
	
	public static void save_records(Context context,Records records){
		String data="";

		int old_records=0;
		data+=""+old_records+"\n";

		saveFile(context, "records", data, false);
		
		save_records_2(context,records);
	}
	
	public static void load_records(Context context,Records records){
		ArrayList<String> data=loadFile(context, "records");

		records.pets.clear();
		int pets_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<pets_size;i++){
			records.pets.add(new Record_Pet());

			records.pets.get(records.pets.size()-1).name=data.get(0).trim();
			data.remove(0);

			records.pets.get(records.pets.size()-1).bits=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).age=Long.parseLong(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).type=Pet_Type.valueOf(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).age_tier=Age_Tier.valueOf(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).weight=Double.parseDouble(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).strength_max=Short.parseShort(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).battles_won=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).battles_lost=Integer.parseInt(data.get(0).trim());
			data.remove(0);
		}

		load_records_2(context,records);
	}
	
	private static void save_records_2(Context context, Records records){
		//If there is at least one record.
		if(records.pets.size()>0){
			String data="";

			data+=""+records.pets.size()+"\n";

			for(int i=0;i<records.pets.size();i++){
				data+=""+records.pets.get(i).name+"\n";

				data+=""+records.pets.get(i).bits+"\n";

				data+=""+records.pets.get(i).age+"\n";

				data+=""+records.pets.get(i).type.toString()+"\n";

				data+=""+records.pets.get(i).age_tier.toString()+"\n";

				data+=""+records.pets.get(i).weight+"\n";

				data+=""+records.pets.get(i).strength_max+"\n";

				data+=""+records.pets.get(i).dexterity_max+"\n";

				data+=""+records.pets.get(i).stamina_max+"\n";

				data+=""+records.pets.get(i).battles_won+"\n";

				data+=""+records.pets.get(i).battles_lost+"\n";

				data+=""+records.pets.get(i).battles_won_sp+"\n";

				data+=""+records.pets.get(i).battles_lost_sp+"\n";

				data+=""+records.pets.get(i).level+"\n";
			}

			saveFile(context, "records2", data, false);
		}
	}
	
	private static void load_records_2(Context context, Records records){
		ArrayList<String> data=loadFile(context, "records2");

		int pets_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<pets_size;i++){
			records.pets.add(new Record_Pet());

			records.pets.get(records.pets.size()-1).name=data.get(0).trim();
			data.remove(0);

			records.pets.get(records.pets.size()-1).bits=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).age=Long.parseLong(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).type=Pet_Type.valueOf(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).age_tier=Age_Tier.valueOf(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).weight=Double.parseDouble(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).strength_max=Short.parseShort(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).dexterity_max=Short.parseShort(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).stamina_max=Short.parseShort(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).battles_won=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).battles_lost=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).battles_won_sp=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).battles_lost_sp=Integer.parseInt(data.get(0).trim());
			data.remove(0);

			records.pets.get(records.pets.size()-1).level=Integer.parseInt(data.get(0).trim());
			data.remove(0);
		}
	}
	
	public static boolean load_templates(Context context,Resources res){
		InputStream file=res.openRawResource(R.raw.food);
		
		try{
			BufferedInputStream in=new BufferedInputStream(file);
			
			byte[] buffer=new byte[2048];
			int bytes_read;
			String raw_data="";
			ArrayList<String> data= new ArrayList<>();
			
			//Read the file's bytes into a string.
			while((bytes_read=in.read(buffer))!=-1){
				String chunk=new String(buffer,0,bytes_read);
				raw_data+=chunk;
			}
			
			//Convert the bytes into strings.
			String current_string="";
			for(int i=0;i<raw_data.length();i++){
				if(raw_data.charAt(i)!='\n'){
					current_string+=raw_data.charAt(i);
				}
				else{
					data.add(current_string);
					current_string="";
				}
			}
			if(data.size()==0){
				data.add(current_string);
			}
			
			log_add(context,TAG,"Template file loaded: food");

            boolean multi_line_comment=false;

            //As long as we haven't reached the end of the file.
            while(data.size()>0){
                //Grab the next line of the file.
                String line=data.get(0).trim();
                data.remove(0);

                //If the line ends a multi-line comment.
                if(line.toLowerCase(Locale.US).contains("*/".toLowerCase(Locale.US))){
                    multi_line_comment=false;
                }
                //If the line starts a multi-line comment.
                if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("/*".toLowerCase(Locale.US))){
                    multi_line_comment=true;
                }
                //If the line is a comment.
                else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("//".toLowerCase(Locale.US))){
                    //Ignore this line.
                }

                //If the line begins an item.
                else if(!multi_line_comment && line.toLowerCase(Locale.US).contains("<item".toLowerCase(Locale.US))){
                    //Determine the item's category.

                    //If the line begins a food item.
                    if(line.toLowerCase(Locale.US).contains("food".toLowerCase(Locale.US))){
                        Templates.load_template_item(data,context,res);
                    }
                    else{
                    	in.close();
                    	
                        error_log_add(context,TAG,"Error while loading templates: Attempted to load an item with an unknown category.",null);
                        
                        return false;
                    }
                }
            }
		
			in.close();
		}
		catch(IOException e){
			error_log_add(context,TAG,"Error reading "+file,e);
			
			return false;
		}
		
		file=res.openRawResource(R.raw.bricks);
		
		try{
			BufferedInputStream in=new BufferedInputStream(file);
			
			byte[] buffer=new byte[2048];
			int bytes_read;
			String raw_data="";
			ArrayList<String> data= new ArrayList<>();
			
			//Read the file's bytes into a string.
			while((bytes_read=in.read(buffer))!=-1){
				String chunk=new String(buffer,0,bytes_read);
				raw_data+=chunk;
			}
			
			//Convert the bytes into strings.
			String current_string="";
			for(int i=0;i<raw_data.length();i++){
				if(raw_data.charAt(i)!='\n'){
					current_string+=raw_data.charAt(i);
				}
				else{
					data.add(current_string);
					current_string="";
				}
			}
			if(data.size()==0){
				data.add(current_string);
			}
			
			log_add(context,TAG,"Template file loaded: bricks");

            boolean multi_line_comment=false;

            //As long as we haven't reached the end of the file.
            while(data.size()>0){
                //Grab the next line of the file.
                String line=data.get(0).trim();
                data.remove(0);

                //If the line ends a multi-line comment.
                if(line.toLowerCase(Locale.US).contains("*/".toLowerCase(Locale.US))){
                    multi_line_comment=false;
                }
                //If the line starts a multi-line comment.
                if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("/*".toLowerCase(Locale.US))){
                    multi_line_comment=true;
                }
                //If the line is a comment.
                else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("//".toLowerCase(Locale.US))){
                    //Ignore this line.
                }

                //If the line begins a bricks layout.
                else if(!multi_line_comment && line.toLowerCase(Locale.US).contains("<bricks".toLowerCase(Locale.US))){
                	line=line.substring("<bricks ".length(),line.length());
                	String[] new_lines=line.split(" ");
                	new_lines[1]=new_lines[1].replaceAll(">"," ");
                	
                	int width=Integer.parseInt(new_lines[0].trim());
                	int height=Integer.parseInt(new_lines[1].trim());
                	
                	Templates.load_template_bricks(data,width,height,context,res);
                }
            }
		
			in.close();
		}
		catch(IOException e){
			error_log_add(context,TAG,"Error reading "+file,e);
			
			return false;
		}
		
		file=res.openRawResource(R.raw.equipment);
		
		try{
			BufferedInputStream in=new BufferedInputStream(file);
			
			byte[] buffer=new byte[2048];
			int bytes_read;
			String raw_data="";
			ArrayList<String> data= new ArrayList<>();
			
			//Read the file's bytes into a string.
			while((bytes_read=in.read(buffer))!=-1){
				String chunk=new String(buffer,0,bytes_read);
				raw_data+=chunk;
			}
			
			//Convert the bytes into strings.
			String current_string="";
			for(int i=0;i<raw_data.length();i++){
				if(raw_data.charAt(i)!='\n'){
					current_string+=raw_data.charAt(i);
				}
				else{
					data.add(current_string);
					current_string="";
				}
			}
			if(data.size()==0){
				data.add(current_string);
			}
			
			log_add(context,TAG,"Template file loaded: equipment");

            boolean multi_line_comment=false;

            //As long as we haven't reached the end of the file.
            while(data.size()>0){
                //Grab the next line of the file.
                String line=data.get(0).trim();
                data.remove(0);

                //If the line ends a multi-line comment.
                if(line.toLowerCase(Locale.US).contains("*/".toLowerCase(Locale.US))){
                    multi_line_comment=false;
                }
                //If the line starts a multi-line comment.
                if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("/*".toLowerCase(Locale.US))){
                    multi_line_comment=true;
                }
                //If the line is a comment.
                else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("//".toLowerCase(Locale.US))){
                    //Ignore this line.
                }

                //If the line begins an equipment layout.
                else if(!multi_line_comment && line.toLowerCase(Locale.US).contains("<equipment>".toLowerCase(Locale.US))){
                	Templates.load_template_equipment(data,context,res);
                }
            }
		
			in.close();
		}
		catch(IOException e){
			error_log_add(context,TAG,"Error reading "+file,e);
			
			return false;
		}
		
		file=res.openRawResource(R.raw.equipment_unique);
		
		try{
			BufferedInputStream in=new BufferedInputStream(file);
			
			byte[] buffer=new byte[2048];
			int bytes_read;
			String raw_data="";
			ArrayList<String> data= new ArrayList<>();
			
			//Read the file's bytes into a string.
			while((bytes_read=in.read(buffer))!=-1){
				String chunk=new String(buffer,0,bytes_read);
				raw_data+=chunk;
			}
			
			//Convert the bytes into strings.
			String current_string="";
			for(int i=0;i<raw_data.length();i++){
				if(raw_data.charAt(i)!='\n'){
					current_string+=raw_data.charAt(i);
				}
				else{
					data.add(current_string);
					current_string="";
				}
			}
			if(data.size()==0){
				data.add(current_string);
			}
			
			log_add(context,TAG,"Template file loaded: equipment_unique");

            boolean multi_line_comment=false;

            //As long as we haven't reached the end of the file.
            while(data.size()>0){
                //Grab the next line of the file.
                String line=data.get(0).trim();
                data.remove(0);

                //If the line ends a multi-line comment.
                if(line.toLowerCase(Locale.US).contains("*/".toLowerCase(Locale.US))){
                    multi_line_comment=false;
                }
                //If the line starts a multi-line comment.
                if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("/*".toLowerCase(Locale.US))){
                    multi_line_comment=true;
                }
                //If the line is a comment.
                else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("//".toLowerCase(Locale.US))){
                    //Ignore this line.
				}

                //If the line begins an equipment layout.
                else if(!multi_line_comment && line.toLowerCase(Locale.US).contains("<equipment>".toLowerCase(Locale.US))){
                	Templates.load_template_equipment(data,context,res);
                }
            }
		
			in.close();
		}
		catch(IOException e){
			error_log_add(context,TAG,"Error reading "+file,e);
			
			return false;
		}
		
		Templates.create_food_lists();
		
		return true;
	}
	
	public static void log_reset(Context context){
		deleteFile(context, "log");
	}
	
	private static boolean log_add(Context context, String tag, String msg){
		Log.i(tag,msg);

		String data;

		Calendar c=Calendar.getInstance();
		String timestamp="["+c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"]\n";

		String message=timestamp+tag+"\n"+msg;

		data=""+message+"\n\n";

		return saveFile(context, "log", data, true);
	}
	
	public static void error_log_reset(Context context){
		deleteFile(context, "error_log");
	}
	
	public static boolean error_log_add(Context context,String tag,String msg,Throwable tr){
		Log.e(tag, msg, tr);

		String data;

		Calendar c=Calendar.getInstance();
		String timestamp="["+c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"]\n";

		String message=timestamp+tag+"\n"+msg;

		data=""+message+"\n\n";

		return saveFile(context, "error_log", data, true);
	}
}
