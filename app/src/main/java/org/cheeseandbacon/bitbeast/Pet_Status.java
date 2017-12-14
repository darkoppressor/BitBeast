/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class Pet_Status{
	//The last time a tick was processed.
	long last_tick;
	Pet_Type type;
	Age_Tier age_tier;
	//The name of the pet.
	String name;
	int bits;
	//The pet's age, in seconds.
	long age;
	int death_counter;
	//The time that the pet should fall asleep each night.
	Calendar sleep_time;
	//The time the pet should wake up.
	Calendar wake_time;
	//If true, the pet should be sleeping.
	//If false, the pet should be awake.
	boolean sleeping;
	//If true, the pet is sleeping, but has been woken up.
	//If false, the pet is awake.
	boolean sleeping_woken_up;
	//The last time that the pet was woken up while sleeping.
	long sleeping_woken_up_time;
	//If true, the light is on.
	//If false, the light is off.
	boolean light;
	short happy;
	//The base temperature of the environment.
	double base_temp;
	//The actual temperature.
	double temp;
	//The last time the base temperature was updated.
	long last_base_temp;
	//If true, the ac is on.
	//If false, the ac is off.
	boolean ac;
	//If true, the heater is on.
	//If false, the heater is off.
	boolean heater;
	boolean sick;
	short poop;
	short bath;
	//The pet's weight, in kilograms.
	double weight;
	short hunger;
	short thirst;
	short strength;
	short strength_max;
	short dexterity;
	short dexterity_max;
	short stamina;
	short stamina_max;
	short energy;
	int color;
	String favorite_food;
	
	double hunger_rise;
	double thirst_rise;
	double strength_rise;
	double dexterity_rise;
	double stamina_rise;
	double energy_rise;
	
	//Battle win ratio for multiplayer battles.
	int battles_won;
	int battles_lost;
	
	//Battle win ratio for singleplayer battles.
	int battles_won_sp;
	int battles_lost_sp;
	
	int stat_points;
	int level;
	long experience;
	long experience_max;
	
	//The number of experience points the pet has received this tick.
	long experience_gain;
	
	//The number of seconds left for each food buff.
	int buff_hunger;
	int buff_thirst;
	int buff_poop;
	int buff_dirty;
	int buff_weight;
	int buff_sick;
	int buff_happy;
	int buff_energy_regen;
	int buff_strength_regen;
	int buff_dexterity_regen;
	int buff_stamina_regen;
	int buff_energy_max;
	int buff_strength_max;
	int buff_dexterity_max;
	int buff_stamina_max;
	int buff_death;
	int buff_magic_find;
	
	//The amount of each food category the pet has been fed.
	//Only used to determine teen type.
	ArrayList<Integer> food_categories;
	//The different types of food the pet has been fed.
	//Only used to determine favorite food.
	ArrayList<String> food_fed;
	ArrayList<Poop> poops;
	ArrayList<Perma_Item> perma_items;
	//The number of pettings the pet has received this tick.
	short pettings;
	//A list of thought types to be created as soon as possible.
	ArrayList<Integer> thoughts;
	
	ArrayList<Equipment> equipment;
	ArrayList<Equipment> equipment_slots;
	
	//Death counter changes allowed.
	short dc_starving;
	short dc_very_thirsty;
	short dc_sick;
	short dc_obese;
	short dc_not_obese;
	short dc_well_fed;
	short dc_well_watered;
	short dc_not_sick;
	
	static final int MAX_LEVEL=500;
	
	static final long STARTING_EXPERIENCE_MAX=8;
	static final long EXPERIENCE_GAIN_ALLOWED_PER_TICK=3200;
	
	static final short STAT_GAIN_SELECTION=8;
	static final short STAT_GAIN_RANDOM_PRIMARY=4;
	static final short STAT_GAIN_RANDOM_SECONDARY=2;
	static final short STAT_GAIN_RANDOM_TERTIARY=1;
	
	static final double EXPERIENCE_VIRTUAL_LEVEL_HIGH=16.0;
	static final double EXPERIENCE_VIRTUAL_LEVEL_LOW=1.0;
	
	static final long EXPERIENCE_BASE_WORKOUT_MIN=16;
	static final long EXPERIENCE_BASE_WORKOUT_MAX=24;
	static final double EXPERIENCE_BONUS_WORKOUT=0.55;
	static final double EXPERIENCE_SCALE_WORKOUT_EASY=0.95;
	static final double EXPERIENCE_SCALE_WORKOUT_NORMAL=1.05;
	static final double EXPERIENCE_SCALE_WORKOUT_HARD=1.30;
	
	static final long EXPERIENCE_BASE_BRICKS_MIN=4;
	static final long EXPERIENCE_BASE_BRICKS_MAX=8;
	static final double EXPERIENCE_BONUS_BRICKS=0.25;
	
	static final long EXPERIENCE_BASE_BATTLE_MIN=8;
	static final long EXPERIENCE_BASE_BATTLE_MAX=16;
	static final double EXPERIENCE_BONUS_BATTLE=0.35;
	
	static final double EXPERIENCE_SCALE_ACCEL=0.1;
	static final double EXPERIENCE_SCALE_GPS=0.06;
	static final double EXPERIENCE_SCALE_GPS_SPEED=0.1;
	static final double EXPERIENCE_SCALE_RPS=0.06;
	
	static final double EXPERIENCE_BONUS_ACCEL=0.2;
	static final double EXPERIENCE_BONUS_GPS=0.08;
	static final double EXPERIENCE_BONUS_GPS_SPEED=0.2;
	static final double EXPERIENCE_BONUS_RPS=0.08;
	
	static final int BIT_GAIN_BRICKS_MIN=72;
	static final int BIT_GAIN_BRICKS_MAX=128;
	
	static final int BIT_GAIN_RPS_MIN=3;
	static final int BIT_GAIN_RPS_MAX=6;
	
	static final int BIT_GAIN_WORKOUT_MIN=24;
	static final int BIT_GAIN_WORKOUT_MAX=48;
	static final double BIT_SCALE_WORKOUT_EASY=0.9;
	static final double BIT_SCALE_WORKOUT_NORMAL=1.0;
	static final double BIT_SCALE_WORKOUT_HARD=1.25;
	
	static final int BIT_GAIN_BATTLE_MIN=24;
	static final int BIT_GAIN_BATTLE_MAX=48;
	
	static final double BIT_GAIN_BATTLE_LEVEL_SCALE_FACTOR=2.5;

    private static final int NEW_ITEM_MINIMUM_BITS = 5;

    static final int ITEM_CHANCE_WORKOUT=40;
	static final int ITEM_CHANCE_BONUS_WORKOUT_EASY=5;
	static final int ITEM_CHANCE_BONUS_WORKOUT_NORMAL=10;
	static final int ITEM_CHANCE_BONUS_WORKOUT_HARD=15;
	
	static final int ITEM_CHANCE_BRICKS=75;
	
	static final double ITEM_CHANCE_ACCEL=0.64;
	static final double ITEM_CHANCE_GPS=0.06;
	static final double ITEM_CHANCE_GPS_SPEED=0.32;
	static final double ITEM_CHANCE_RPS=0.48;
	
	static final int ITEM_CHANCE_BATTLE=75;
	
	static final double OBESITY=60.0;
	static final int DEATH_COUNTER_CHANGES_PER_AGE_TIER=5;
	static final int CHANCE_SMALL=5;
	static final int CHANCE_TINY=1;
	
	static final short HAPPY_MAX=100;
	static final short HAPPY_MIN=-HAPPY_MAX;
	static final short HAPPY_GAIN_GAME_BRICKS_MIN=16;
	static final short HAPPY_GAIN_GAME_BRICKS_MAX=32;
	static final short HAPPY_GAIN_GAME_RPS_MIN=1;
	static final short HAPPY_GAIN_GAME_RPS_MAX=2;
	static final short HAPPY_GAIN_PETTING=24;
	static final short HAPPY_GAIN_FAVORITE_FOOD=25;
	static final short HAPPY_LOSS_AWAKEN=65;
	static final short HAPPY_GAIN_BATTLE=32;
	static final short HAPPY_GAIN_TEMP=16;
	static final short HAPPY_LOSS_BATTLE=24;
	static final short HAPPY_LOSS_SICK=25;
	static final short HAPPY_LOSS_POOP=5;
	static final short HAPPY_LOSS_DIRTY=12;
	static final short HAPPY_LOSS_HUNGER=16;
	static final short HAPPY_LOSS_THIRST=16;
	static final short HAPPY_LOSS_TEMP=16;
	
	static final double TEMP_TURN_OFF_THRESHOLD=2.5;
	static final double TEMP_OPTIMUM=20.0;
	static final double TEMP_MIN=0.0;
	static final double TEMP_MAX=100.0;
	static final double TEMP_CHANGE_ALWAYS=0.125;
	static final double TEMP_CHANGE_NO_MACHINE=0.25;
	static final double TEMP_CHANGE_MACHINE=48.0;
	static final long BASE_TEMP_UPDATE_TIME=28800000L;
	static final short POOP_MAX=99;
	static final short BATH_MAX=99;
	static final double WEIGHT_MIN=1.0;
	static final double WEIGHT_START=1.0;
	static final int BASE_SICK_CHANCE=15;
	static final short BASE_HUNGER_RATE=5;
	static final short BASE_THIRST_RATE=6;
	static final short BASE_POOP_RATE_MIN=8;
	static final short BASE_POOP_RATE_MAX=12;
	static final short BASE_DIRTY_RATE_MIN=3;
	static final short BASE_DIRTY_RATE_MAX=6;
	static final float BASE_STRENGTH_REGEN_RATE=0.85f;
	static final float BASE_DEXTERITY_REGEN_RATE=0.85f;
	static final float BASE_STAMINA_REGEN_RATE=0.85f;
	static final float BASE_ENERGY_REGEN_RATE=0.75f;
	static final int BASE_MAGIC_FIND=5;
	
	//Weight loss variables are multiplied by 0.01.
	static final int WEIGHT_LOSS_WORKOUT_MIN=250;
	static final int WEIGHT_LOSS_WORKOUT_MAX=700;
	static final int WEIGHT_LOSS_BATTLE_MIN=200;
	static final int WEIGHT_LOSS_BATTLE_MAX=575;
	
	static final short THIRST_MAX=99;
	static final short STRENGTH_MAX_MIN=1;
	static final short DEXTERITY_MAX_MIN=1;
	static final short STAMINA_MAX_MIN=1;
	static final short ENERGY_LOSS_WORKOUT=4;
	static final short ENERGY_LOSS_BATTLE=8;
	static final short PETTINGS_ALLOWED_PER_TICK=6;
	static final int LEVEL_NEEDED_FOR_SENIOR=24;
	static final int MP_BATTLE_WINS_NEEDED_FOR_SENIOR=16;
	static final int SP_BATTLE_WINS_NEEDED_FOR_SENIOR=32;
	
	//--- Buffs ---//
	
	//Hunger rate decrease: Subtracted from the base hunger rate.
	static final int FOOD_BUFF_SECONDS_HUNGER=3600;
	static final short FOOD_BUFF_HUNGER=1;
	static final short ITEM_BUFF_STONE=1;
	
	//Thirst rate decrease: Subtracted from the base thirst rate.
	static final int FOOD_BUFF_SECONDS_THIRST=3600;
	static final short FOOD_BUFF_THIRST=1;
	static final short ITEM_BUFF_LEAF=1;
	
	//Poop rate decrease: Subtracted from the base poop rate.
	static final int FOOD_BUFF_SECONDS_POOP=3600;
	static final short FOOD_BUFF_POOP=3;
	static final short ITEM_BUFF_TREE=6;
	
	//Dirty rate decrease: Subtracted from the base dirty rate.
	static final int FOOD_BUFF_SECONDS_DIRTY=3600;
	static final short FOOD_BUFF_DIRTY=1;
	static final short ITEM_BUFF_TOWEL=3;
	
	//Weight gain decrease: Multiplies all weight gains by the buff.
	static final int FOOD_BUFF_SECONDS_WEIGHT=1800;
	static final double FOOD_BUFF_WEIGHT=0.8;
	static final double ITEM_BUFF_BEACH_BALL=0.75;
	
	//Sick chance decrease: Subtracts the buff from the base sick chance.
	//Note that this is a 0-999 chance, not a standard 0-99.
	static final int FOOD_BUFF_SECONDS_SICK=3600;
	static final int FOOD_BUFF_SICK=4;
	static final int ITEM_BUFF_WHEAT=8;
	
	//Happy regen increase: Buff is added to happy each tick.
	static final int FOOD_BUFF_SECONDS_HAPPY=1800;
	static final short FOOD_BUFF_HAPPY=1;
	static final short ITEM_BUFF_DUCKIE=1;
	static final short ITEM_BUFF_KEY=2;
	
	//Energy regen increase: Buff is added to the base energy regen rate.
	//Energy regen rate% of max energy is then added to energy.
	static final int FOOD_BUFF_SECONDS_ENERGY_REGEN=1800;
	static final float FOOD_BUFF_ENERGY_REGEN=0.1f;
	static final float ITEM_BUFF_BERRY_BUSH=0.25f;
	
	//Strength regen increase: Buff is added to the base strength regen rate.
	//Strength regen rate% of max strength is then added to strength.
	static final int FOOD_BUFF_SECONDS_STRENGTH_REGEN=1800;
	static final float FOOD_BUFF_STRENGTH_REGEN=0.05f;
	static final float ITEM_BUFF_TRAMPOLINE=0.15f;
	
	//Dexterity regen increase: Buff is added to the base dexterity regen rate.
	//Dexterity regen rate% of max dexterity is then added to dexterity.
	static final int FOOD_BUFF_SECONDS_DEXTERITY_REGEN=1800;
	static final float FOOD_BUFF_DEXTERITY_REGEN=0.05f;
	static final float ITEM_BUFF_JUMP_ROPE=0.15f;
		
	//Stamina regen increase: Buff is added to the base stamina regen rate.
	//Stamina regen rate% of max stamina is then added to stamina.
	static final int FOOD_BUFF_SECONDS_STAMINA_REGEN=1800;
	static final float FOOD_BUFF_STAMINA_REGEN=0.05f;
	static final float ITEM_BUFF_BARBELL=0.15f;
	
	//Energy max increase: Buff is added in get_energy() and get_energy_max().
	static final int FOOD_BUFF_SECONDS_ENERGY_MAX=3600;
	static final short FOOD_BUFF_ENERGY_MAX=8;
	static final short ITEM_BUFF_SIGN=16;
	static final short ITEM_BUFF_COMPUTER=32;
	
	//Strength max increase: Buff is added in get_strength() and get_strength_max().
	static final int FOOD_BUFF_SECONDS_STRENGTH_MAX=3600;
	static final short FOOD_BUFF_STRENGTH_MAX=32;
	static final short ITEM_BUFF_ROBOT=64;
	static final short ITEM_BUFF_AT=96;
	
	//Dexterity max increase: Buff is added in get_dexterity() and get_dexterity_max().
	static final int FOOD_BUFF_SECONDS_DEXTERITY_MAX=3600;
	static final short FOOD_BUFF_DEXTERITY_MAX=32;
	static final short ITEM_BUFF_PIANO=64;
	
	//Stamina max increase: Buff is added in get_stamina() and get_stamina_max().
	static final int FOOD_BUFF_SECONDS_STAMINA_MAX=3600;
	static final short FOOD_BUFF_STAMINA_MAX=32;
	static final short ITEM_BUFF_SHOES=64;
	
	//Death counter rate decrease: Each tick, there is a 1% chance that buff will be added to death_counter.
	static final int FOOD_BUFF_SECONDS_DEATH=3600;
	static final int FOOD_BUFF_DEATH=1;
	static final int ITEM_BUFF_BUNNY=1;
	
	//Magic find increase: Buff is added to magic find chance.
	static final int FOOD_BUFF_SECONDS_MAGIC_FIND=3600;
	static final int FOOD_BUFF_MAGIC_FIND=5;
	static final int ITEM_BUFF_CHEST=15;
	
	public Pet_Status(){
		last_tick=System.currentTimeMillis();
		type=Pet_Type.YEETSOBIT;
		age_tier=Age_Tier.EGG;
		name="";
		bits=100;
		age=0L;
		//Approximately 31 days worth of ticks.
		death_counter=2976;
		
		sleep_time=Calendar.getInstance();
		sleep_time.set(Calendar.HOUR_OF_DAY,22);
		sleep_time.set(Calendar.MINUTE,0);
		
		wake_time=Calendar.getInstance();
		wake_time.add(Calendar.DAY_OF_YEAR,-1);
		
		sleeping=false;
		sleeping_woken_up=false;
		sleeping_woken_up_time=-1L;
		light=true;
		happy=0;
		base_temp=determine_base_temp();
		temp=base_temp;
		last_base_temp=System.currentTimeMillis();
		ac=false;
		heater=false;
		sick=false;
		poop=POOP_MAX/2;
		bath=BATH_MAX/4;
		weight=WEIGHT_START;
		hunger=0;
		thirst=0;
		strength_max=4;
		strength=strength_max;
		dexterity_max=4;
		dexterity=dexterity_max;
		stamina_max=4;
		stamina=stamina_max;
		energy=Age_Tier.get_energy_max(age_tier);
		color=Color.GREEN;
		favorite_food="none";
		battles_won=0;
		battles_lost=0;
		battles_won_sp=0;
		battles_lost_sp=0;
		experience_gain=0;
		
		hunger_rise=0.0;
		thirst_rise=0.0;
		strength_rise=0.0;
		dexterity_rise=0.0;
		stamina_rise=0.0;
		energy_rise=0.0;
		
		stat_points=0;
		level=1;
		experience=0;
		experience_max=STARTING_EXPERIENCE_MAX;
		
		buff_hunger=0;
		buff_thirst=0;
		buff_poop=0;
		buff_dirty=0;
		buff_weight=0;
		buff_sick=0;
		buff_happy=0;
		buff_energy_regen=0;
		buff_strength_regen=0;
		buff_dexterity_regen=0;
		buff_stamina_regen=0;
		buff_energy_max=0;
		buff_strength_max=0;
		buff_dexterity_max=0;
		buff_stamina_max=0;
		buff_death=0;
		buff_magic_find=0;
		
		food_categories=new ArrayList<Integer>();
		for(int i=0;i<Food_Category.END;i++){
			food_categories.add(0);
		}
		food_fed=new ArrayList<String>();
		poops=new ArrayList<Poop>();
		perma_items=new ArrayList<Perma_Item>();
		pettings=0;
		thoughts=new ArrayList<Integer>();
		
		equipment=new ArrayList<Equipment>();
		equipment_slots=new ArrayList<Equipment>();
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			equipment_slots.add(null);
		}
		
		dc_starving=0;
		dc_very_thirsty=0;
		dc_sick=0;
		dc_obese=0;
		dc_not_obese=0;
		dc_well_fed=0;
		dc_well_watered=0;
		dc_not_sick=0;
	}

	public String getBattleData () {
		String data="";

		data+=""+type.toString()+"\n";

		data+=""+age_tier.toString()+"\n";

		data+=""+name+"\n";

		data+=""+sick+"\n";

		data+=""+weight+"\n";

		data+=""+hunger+"\n";

		data+=""+thirst+"\n";

		data+=""+strength+"\n";

		data+=""+strength_max+"\n";

		data+=""+dexterity+"\n";

		data+=""+dexterity_max+"\n";

		data+=""+stamina+"\n";

		data+=""+stamina_max+"\n";

		data+=""+energy+"\n";

		data+=""+color+"\n";

		data+=""+battles_won+"\n";

		data+=""+battles_lost+"\n";

		data+=""+battles_won_sp+"\n";

		data+=""+battles_lost_sp+"\n";

		data+=""+level+"\n";

		data+=""+buff_hunger+"\n";
		data+=""+buff_thirst+"\n";
		data+=""+buff_poop+"\n";
		data+=""+buff_dirty+"\n";
		data+=""+buff_weight+"\n";
		data+=""+buff_sick+"\n";
		data+=""+buff_happy+"\n";
		data+=""+buff_energy_regen+"\n";
		data+=""+buff_strength_regen+"\n";
		data+=""+buff_dexterity_regen+"\n";
		data+=""+buff_stamina_regen+"\n";
		data+=""+buff_energy_max+"\n";
		data+=""+buff_strength_max+"\n";
		data+=""+buff_dexterity_max+"\n";
		data+=""+buff_stamina_max+"\n";
		data+=""+buff_death+"\n";
		data+=""+buff_magic_find+"\n";

		data+=""+perma_items.size()+"\n";
		for(int i=0;i<perma_items.size();i++){
			data+=""+perma_items.get(i).name+"\n";
		}

		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)==null){
				data+=""+false+"\n";
			}
			else{
				data+=""+true+"\n";

				data+=""+equipment_slots.get(i).name+"\n";
				data+=""+equipment_slots.get(i).full_name+"\n";
				data+=""+equipment_slots.get(i).description+"\n";
				data+=""+equipment_slots.get(i).level+"\n";
				data+=""+equipment_slots.get(i).bits+"\n";
				data+=""+equipment_slots.get(i).branch+"\n";
				data+=""+equipment_slots.get(i).weight+"\n";

				data+=""+equipment_slots.get(i).buff_hunger+"\n";
				data+=""+equipment_slots.get(i).buff_thirst+"\n";
				data+=""+equipment_slots.get(i).buff_poop+"\n";
				data+=""+equipment_slots.get(i).buff_dirty+"\n";
				data+=""+equipment_slots.get(i).buff_weight+"\n";
				data+=""+equipment_slots.get(i).buff_sick+"\n";
				data+=""+equipment_slots.get(i).buff_happy+"\n";
				data+=""+equipment_slots.get(i).buff_energy_regen+"\n";
				data+=""+equipment_slots.get(i).buff_strength_regen+"\n";
				data+=""+equipment_slots.get(i).buff_dexterity_regen+"\n";
				data+=""+equipment_slots.get(i).buff_stamina_regen+"\n";
				data+=""+equipment_slots.get(i).buff_energy_max+"\n";
				data+=""+equipment_slots.get(i).buff_strength_max+"\n";
				data+=""+equipment_slots.get(i).buff_dexterity_max+"\n";
				data+=""+equipment_slots.get(i).buff_stamina_max+"\n";
				data+=""+equipment_slots.get(i).buff_death+"\n";
				data+=""+equipment_slots.get(i).buff_magic_find+"\n";
			}
		}

		return data;
	}

	public static Pet_Status createPetFromBattleData (ArrayList<String> data) {
		Pet_Status pet_status = new Pet_Status();

		pet_status.type=Pet_Type.valueOf(data.get(0).trim());
		data.remove(0);

		pet_status.age_tier=Age_Tier.valueOf(data.get(0).trim());
		data.remove(0);

		pet_status.name=data.get(0).trim();
		data.remove(0);

		pet_status.sick=Boolean.parseBoolean(data.get(0).trim());
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

		pet_status.dexterity=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.dexterity_max=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.stamina=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.stamina_max=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.energy=Short.parseShort(data.get(0).trim());
		data.remove(0);

		pet_status.color=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.battles_won=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.battles_lost=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.battles_won_sp=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.battles_lost_sp=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		pet_status.level=Integer.parseInt(data.get(0).trim());
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

		pet_status.perma_items.clear();
		int perma_items_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);

		for(int i=0;i<perma_items_size;i++){
			String name=data.get(0).trim();
			data.remove(0);

			pet_status.perma_items.add(new Perma_Item(null,null,name,0.0f,0.0f));
		}

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

		return pet_status;
	}
	
	public Bundle write_bundle_battle_data(String key_prefix){
		Bundle bundle=new Bundle();
		
		bundle.putString(key_prefix+"type",type.toString());
		
		bundle.putString(key_prefix+"age_tier",age_tier.toString());
		
		bundle.putString(key_prefix+"name",name);
		
		bundle.putBoolean(key_prefix+"sick",sick);
		
		bundle.putDouble(key_prefix+"weight",weight);
		
		bundle.putShort(key_prefix+"hunger",hunger);
		
		bundle.putShort(key_prefix+"thirst",thirst);
		
		bundle.putShort(key_prefix+"strength",strength);
		
		bundle.putShort(key_prefix+"strength_max",strength_max);
		
		bundle.putShort(key_prefix+"dexterity",dexterity);
		
		bundle.putShort(key_prefix+"dexterity_max",dexterity_max);
		
		bundle.putShort(key_prefix+"stamina",stamina);
		
		bundle.putShort(key_prefix+"stamina_max",stamina_max);
		
		bundle.putShort(key_prefix+"energy",energy);
		
		bundle.putInt(key_prefix+"color",color);
		
		bundle.putInt(key_prefix+"battles_won",battles_won);
		
		bundle.putInt(key_prefix+"battles_lost",battles_lost);
		
		bundle.putInt(key_prefix+"battles_won_sp",battles_won_sp);
		
		bundle.putInt(key_prefix+"battles_lost_sp",battles_lost_sp);
		
		bundle.putInt(key_prefix+"level",level);
		
		bundle.putInt(key_prefix+"buff_hunger",buff_hunger);
		bundle.putInt(key_prefix+"buff_thirst",buff_thirst);
		bundle.putInt(key_prefix+"buff_poop",buff_poop);
		bundle.putInt(key_prefix+"buff_dirty",buff_dirty);
		bundle.putInt(key_prefix+"buff_weight",buff_weight);
		bundle.putInt(key_prefix+"buff_sick",buff_sick);
		bundle.putInt(key_prefix+"buff_happy",buff_happy);
		bundle.putInt(key_prefix+"buff_energy_regen",buff_energy_regen);
		bundle.putInt(key_prefix+"buff_strength_regen",buff_strength_regen);
		bundle.putInt(key_prefix+"buff_dexterity_regen",buff_dexterity_regen);
		bundle.putInt(key_prefix+"buff_stamina_regen",buff_stamina_regen);
		bundle.putInt(key_prefix+"buff_energy_max",buff_energy_max);
		bundle.putInt(key_prefix+"buff_strength_max",buff_strength_max);
		bundle.putInt(key_prefix+"buff_dexterity_max",buff_dexterity_max);
		bundle.putInt(key_prefix+"buff_stamina_max",buff_stamina_max);
		bundle.putInt(key_prefix+"buff_death",buff_death);
		bundle.putInt(key_prefix+"buff_magic_find",buff_magic_find);
		
		String perma_item_list="";
		for(int i=0;i<perma_items.size();i++){
			perma_item_list+=perma_items.get(i).name+" ";
		}
		bundle.putString(key_prefix+"perma_items",perma_item_list);
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)==null){
				bundle.putBoolean(key_prefix+"equipment_slot_"+i+"_filled",false);
			}
			else{
				bundle.putBoolean(key_prefix+"equipment_slot_"+i+"_filled",true);
				
				bundle.putString(key_prefix+"equipment_slot_"+i+"_name",equipment_slots.get(i).name);
				bundle.putString(key_prefix+"equipment_slot_"+i+"_full_name",equipment_slots.get(i).full_name);
				bundle.putString(key_prefix+"equipment_slot_"+i+"_description",equipment_slots.get(i).description);
				bundle.putInt(key_prefix+"equipment_slot_"+i+"_level",equipment_slots.get(i).level);
				bundle.putInt(key_prefix+"equipment_slot_"+i+"_bits",equipment_slots.get(i).bits);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_branch",equipment_slots.get(i).branch);
				bundle.putDouble(key_prefix+"equipment_slot_"+i+"_weight",equipment_slots.get(i).weight);
				
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_hunger",equipment_slots.get(i).buff_hunger);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_thirst",equipment_slots.get(i).buff_thirst);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_poop",equipment_slots.get(i).buff_poop);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_dirty",equipment_slots.get(i).buff_dirty);
				bundle.putDouble(key_prefix+"equipment_slot_"+i+"_buff_weight",equipment_slots.get(i).buff_weight);
				bundle.putInt(key_prefix+"equipment_slot_"+i+"_buff_sick",equipment_slots.get(i).buff_sick);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_happy",equipment_slots.get(i).buff_happy);
				bundle.putFloat(key_prefix+"equipment_slot_"+i+"_buff_energy_regen",equipment_slots.get(i).buff_energy_regen);
				bundle.putFloat(key_prefix+"equipment_slot_"+i+"_buff_strength_regen",equipment_slots.get(i).buff_strength_regen);
				bundle.putFloat(key_prefix+"equipment_slot_"+i+"_buff_dexterity_regen",equipment_slots.get(i).buff_dexterity_regen);
				bundle.putFloat(key_prefix+"equipment_slot_"+i+"_buff_stamina_regen",equipment_slots.get(i).buff_stamina_regen);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_energy_max",equipment_slots.get(i).buff_energy_max);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_strength_max",equipment_slots.get(i).buff_strength_max);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_dexterity_max",equipment_slots.get(i).buff_dexterity_max);
				bundle.putShort(key_prefix+"equipment_slot_"+i+"_buff_stamina_max",equipment_slots.get(i).buff_stamina_max);
				bundle.putInt(key_prefix+"equipment_slot_"+i+"_buff_death",equipment_slots.get(i).buff_death);
				bundle.putInt(key_prefix+"equipment_slot_"+i+"_buff_magic_find",equipment_slots.get(i).buff_magic_find);
			}
		}
		
		return bundle;
	}
	
	public void read_bundle_battle_data(String key_prefix,Bundle bundle){
		type=Pet_Type.valueOf(bundle.getString(key_prefix+"type"));
		
		age_tier=Age_Tier.valueOf(bundle.getString(key_prefix+"age_tier"));
		
		name=bundle.getString(key_prefix+"name");
		
		sick=bundle.getBoolean(key_prefix+"sick");
		
		weight=bundle.getDouble(key_prefix+"weight");
		
		hunger=bundle.getShort(key_prefix+"hunger");
		
		thirst=bundle.getShort(key_prefix+"thirst");
		
		strength=bundle.getShort(key_prefix+"strength");
		
		strength_max=bundle.getShort(key_prefix+"strength_max");
		
		dexterity=bundle.getShort(key_prefix+"dexterity");
		
		dexterity_max=bundle.getShort(key_prefix+"dexterity_max");
		
		stamina=bundle.getShort(key_prefix+"stamina");
		
		stamina_max=bundle.getShort(key_prefix+"stamina_max");
		
		energy=bundle.getShort(key_prefix+"energy");
		
		color=bundle.getInt(key_prefix+"color");
		
		battles_won=bundle.getInt(key_prefix+"battles_won");
		
		battles_lost=bundle.getInt(key_prefix+"battles_lost");
		
		battles_won_sp=bundle.getInt(key_prefix+"battles_won_sp");
		
		battles_lost_sp=bundle.getInt(key_prefix+"battles_lost_sp");
		
		level=bundle.getInt(key_prefix+"level");
		
		buff_hunger=bundle.getInt(key_prefix+"buff_hunger");
		buff_thirst=bundle.getInt(key_prefix+"buff_thirst");
		buff_poop=bundle.getInt(key_prefix+"buff_poop");
		buff_dirty=bundle.getInt(key_prefix+"buff_dirty");
		buff_weight=bundle.getInt(key_prefix+"buff_weight");
		buff_sick=bundle.getInt(key_prefix+"buff_sick");
		buff_happy=bundle.getInt(key_prefix+"buff_happy");
		buff_energy_regen=bundle.getInt(key_prefix+"buff_energy_regen");
		buff_strength_regen=bundle.getInt(key_prefix+"buff_strength_regen");
		buff_dexterity_regen=bundle.getInt(key_prefix+"buff_dexterity_regen");
		buff_stamina_regen=bundle.getInt(key_prefix+"buff_stamina_regen");
		buff_energy_max=bundle.getInt(key_prefix+"buff_energy_max");
		buff_strength_max=bundle.getInt(key_prefix+"buff_strength_max");
		buff_dexterity_max=bundle.getInt(key_prefix+"buff_dexterity_max");
		buff_stamina_max=bundle.getInt(key_prefix+"buff_stamina_max");
		buff_death=bundle.getInt(key_prefix+"buff_death");
		buff_magic_find=bundle.getInt(key_prefix+"buff_magic_find");
		
		String perma_item_list="";		
		perma_item_list=bundle.getString(key_prefix+"perma_items");
		String[] perma_item_entries=perma_item_list.split(" ");
		for(int i=0;i<perma_item_entries.length;i++){
			perma_items.add(new Perma_Item(null,null,perma_item_entries[i],0.0f,0.0f));
		}
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			boolean slot_filled=bundle.getBoolean(key_prefix+"equipment_slot_"+i+"_filled");
			
			if(slot_filled){
				equipment_slots.set(i,new Equipment());
				
				equipment_slots.get(i).name=bundle.getString(key_prefix+"equipment_slot_"+i+"_name");
				equipment_slots.get(i).full_name=bundle.getString(key_prefix+"equipment_slot_"+i+"_full_name");
				equipment_slots.get(i).description=bundle.getString(key_prefix+"equipment_slot_"+i+"_description");
				equipment_slots.get(i).level=bundle.getInt(key_prefix+"equipment_slot_"+i+"_level");
				equipment_slots.get(i).bits=bundle.getInt(key_prefix+"equipment_slot_"+i+"_bits");
				equipment_slots.get(i).branch=bundle.getShort(key_prefix+"equipment_slot_"+i+"_branch");
				equipment_slots.get(i).weight=bundle.getDouble(key_prefix+"equipment_slot_"+i+"_weight");
				
				equipment_slots.get(i).buff_hunger=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_hunger");
				equipment_slots.get(i).buff_thirst=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_thirst");
				equipment_slots.get(i).buff_poop=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_poop");
				equipment_slots.get(i).buff_dirty=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_dirty");
				equipment_slots.get(i).buff_weight=bundle.getDouble(key_prefix+"equipment_slot_"+i+"_buff_weight");
				equipment_slots.get(i).buff_sick=bundle.getInt(key_prefix+"equipment_slot_"+i+"_buff_sick");
				equipment_slots.get(i).buff_happy=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_happy");
				equipment_slots.get(i).buff_energy_regen=bundle.getFloat(key_prefix+"equipment_slot_"+i+"_buff_energy_regen");
				equipment_slots.get(i).buff_strength_regen=bundle.getFloat(key_prefix+"equipment_slot_"+i+"_buff_strength_regen");
				equipment_slots.get(i).buff_dexterity_regen=bundle.getFloat(key_prefix+"equipment_slot_"+i+"_buff_dexterity_regen");
				equipment_slots.get(i).buff_stamina_regen=bundle.getFloat(key_prefix+"equipment_slot_"+i+"_buff_stamina_regen");
				equipment_slots.get(i).buff_energy_max=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_energy_max");
				equipment_slots.get(i).buff_strength_max=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_strength_max");
				equipment_slots.get(i).buff_dexterity_max=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_dexterity_max");
				equipment_slots.get(i).buff_stamina_max=bundle.getShort(key_prefix+"equipment_slot_"+i+"_buff_stamina_max");
				equipment_slots.get(i).buff_death=bundle.getInt(key_prefix+"equipment_slot_"+i+"_buff_death");
				equipment_slots.get(i).buff_magic_find=bundle.getInt(key_prefix+"equipment_slot_"+i+"_buff_magic_find");
			}
			else{
				equipment_slots.set(i,null);
			}
		}
	}
	
	public static long get_level_bonus_xp(int current_level,double bonus_scale,double virtual_level_start){
		double virtual_level=virtual_level_start;
		
        for(int i=0;i<current_level;i++){
            virtual_level+=bonus_scale*Math.sqrt(virtual_level);
        }
        
        return (long)Math.ceil(virtual_level/2.5);
	}
		
	//Returns the amount of experience points truly gained.
	public long gain_experience(long points_gained,boolean ignore_cap){
		long true_points_gained=points_gained;
		
		if(temp_is_optimum()){
			true_points_gained=(long)Math.ceil((double)true_points_gained*1.25);
		}
		else if(temp_is_good()){
			true_points_gained=(long)Math.ceil((double)true_points_gained*1.1);
		}
		else if(temp_is_bad()){
			true_points_gained=(long)Math.ceil((double)true_points_gained*0.9);
		}
		else if(temp_is_very_bad()){
			true_points_gained=(long)Math.ceil((double)true_points_gained*0.75);
		}
		
		if(!ignore_cap){
			long experience_gain_allowed=Pet_Status.EXPERIENCE_GAIN_ALLOWED_PER_TICK-experience_gain;
			
			if(true_points_gained<=experience_gain_allowed){
				experience_gain+=true_points_gained;
			}
			else{
				true_points_gained=experience_gain_allowed;
				
				experience_gain+=true_points_gained;
			}
		}
		
		experience+=true_points_gained;
		
		if(experience>=experience_max){
			if(level<MAX_LEVEL){
				level_up();
			}
			else{
				experience=experience_max;
			}
		}
		
		return true_points_gained;
	}
	
	private void level_up(){
		experience-=experience_max;
		
		long max_gain=(long)Math.ceil(2.0*Math.sqrt(experience_max));

		experience_max+=max_gain;
		
		level++;
		
		stat_points+=2;
		
		strength_max_increase();
		
		dexterity_max_increase();
		
		stamina_max_increase();
		
		energy=Age_Tier.get_energy_max(age_tier);
		strength=strength_max;
    	dexterity=dexterity_max;
    	stamina=stamina_max;
		
		if(experience>=experience_max){
			if(level<MAX_LEVEL){
				level_up();
			}
			else{
				experience=experience_max;
			}
		}
	}
	
	public void strength_max_increase(){
		short increase=0;
		
		short branch=Pet_Type.get_pet_branch(type);
		
		if(branch==Pet_Type.BRANCH_TYRANNO || branch==Pet_Type.BRANCH_APATO){
			increase=STAT_GAIN_RANDOM_PRIMARY;
		}
		else if(branch==Pet_Type.BRANCH_STEGO || branch==Pet_Type.BRANCH_PTERO){
			increase=STAT_GAIN_RANDOM_SECONDARY;
		}
		else if(branch==Pet_Type.BRANCH_NONE || branch==Pet_Type.BRANCH_TRICERA){
			increase=STAT_GAIN_RANDOM_TERTIARY;
		}
		
		strength_max+=increase;
		strength_max_bound();
	}
	
	public void dexterity_max_increase(){
		short increase=0;
		
		short branch=Pet_Type.get_pet_branch(type);
		
		if(branch==Pet_Type.BRANCH_PTERO){
			increase=STAT_GAIN_RANDOM_PRIMARY;
		}
		else if(branch==Pet_Type.BRANCH_TYRANNO || branch==Pet_Type.BRANCH_TRICERA){
			increase=STAT_GAIN_RANDOM_SECONDARY;
		}
		else if(branch==Pet_Type.BRANCH_NONE || branch==Pet_Type.BRANCH_APATO || branch==Pet_Type.BRANCH_STEGO){
			increase=STAT_GAIN_RANDOM_TERTIARY;
		}
		
		dexterity_max+=increase;
		dexterity_max_bound();
	}
	
	public void stamina_max_increase(){
		short increase=0;
		
		short branch=Pet_Type.get_pet_branch(type);
		
		if(branch==Pet_Type.BRANCH_STEGO || branch==Pet_Type.BRANCH_TRICERA){
			increase=STAT_GAIN_RANDOM_PRIMARY;
		}
		else if(branch==Pet_Type.BRANCH_APATO){
			increase=STAT_GAIN_RANDOM_SECONDARY;
		}
		else if(branch==Pet_Type.BRANCH_NONE || branch==Pet_Type.BRANCH_PTERO || branch==Pet_Type.BRANCH_TYRANNO){
			increase=STAT_GAIN_RANDOM_TERTIARY;
		}
		
		stamina_max+=increase;
		stamina_max_bound();
	}
	
	public static String get_buff_name(String buff){
		if(buff.equals("hunger")){
			return "Nourishment";
		}
		else if(buff.equals("thirst")){
			return "Hydration";
		}
		else if(buff.equals("poop")){
			return "Costiveness";
		}
		else if(buff.equals("dirty")){
			return "Cleanliness";
		}
		else if(buff.equals("weight")){
			return "Dieting";
		}
		else if(buff.equals("sick")){
			return "Healthiness";
		}
		else if(buff.equals("happy")){
			return "Happiness";
		}
		else if(buff.equals("energy_regen")){
			return "Animation";
		}
		else if(buff.equals("strength_regen")){
			return "Bolstering";
		}
		else if(buff.equals("dexterity_regen")){
			return "Honing";
		}
		else if(buff.equals("stamina_regen")){
			return "Hardening";
		}
		else if(buff.equals("energy_max")){
			return "Energy";
		}
		else if(buff.equals("strength_max")){
			return "Might";
		}
		else if(buff.equals("dexterity_max")){
			return "Deftness";
		}
		else if(buff.equals("stamina_max")){
			return "Hardiness";
		}
		else if(buff.equals("death")){
			return "Revivification";
		}
		else if(buff.equals("magic_find")){
			return "Fortuitousness";
		}
		else{
			return "<ERROR="+buff+">";
		}
	}
	
	public static String get_buff_prefix(String buff){
		if(buff.length()>0){
			if(buff.equals("hunger")){
				return "Nourishing";
			}
			else if(buff.equals("thirst")){
				return "Hydrating";
			}
			else if(buff.equals("poop")){
				return "Costive";
			}
			else if(buff.equals("dirty")){
				return "Clean";
			}
			else if(buff.equals("weight")){
				return "Dieting";
			}
			else if(buff.equals("sick")){
				return "Healthy";
			}
			else if(buff.equals("happy")){
				return "Happy";
			}
			else if(buff.equals("energy_regen")){
				return "Animating";
			}
			else if(buff.equals("strength_regen")){
				return "Bolstering";
			}
			else if(buff.equals("dexterity_regen")){
				return "Honing";
			}
			else if(buff.equals("stamina_regen")){
				return "Hardening";
			}
			else if(buff.equals("energy_max")){
				return "Energizing";
			}
			else if(buff.equals("death")){
				return "Revivifying";
			}
			else if(buff.equals("magic_find")){
				return "Fortuitous";
			}
			else{
				return "<ERROR="+buff+">";
			}
		}
		else{
			return "";
		}
	}
	
	public static String get_buff_suffix(short branch){
		if(branch==Pet_Type.BRANCH_NONE){
			return "Generality";
		}
		else if(branch==Pet_Type.BRANCH_TYRANNO){
			return "the Tyrant";
		}
		else if(branch==Pet_Type.BRANCH_APATO){
			return "the Trickster";
		}
		else if(branch==Pet_Type.BRANCH_TRICERA){
			return "the Horn";
		}
		else if(branch==Pet_Type.BRANCH_STEGO){
			return "the Scale";
		}
		else if(branch==Pet_Type.BRANCH_PTERO){
			return "the Wing";
		}
		else{
			return "<ERROR="+branch+">";
		}
	}
	
	public int get_magic_find(){
		double number=BASE_MAGIC_FIND;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_magic_find;
			}
		}
		if(buff_magic_find>0){
			number+=FOOD_BUFF_MAGIC_FIND;
		}
		if(has_perma_item("chest")){
			number+=ITEM_BUFF_CHEST;
		}
		
		return (int)number;
	}
	
	// Returns a String containing the name of the new equipment if equipment was added, or an empty String otherwise
	public String new_equipment(int chance,int base_level,String force_slot){
		//Use the passed equipment find chance to determine if we found anything.
		if(RNG.random_range(0,99)<chance){
			int magic_find=get_magic_find();
			//Small chance for magic find to be "scaled up" this time.
			for(int i=0;i<10000;i++){
				if(RNG.random_range(0,999)<get_magic_find()){
					magic_find=(int)Math.ceil(1.25*(double)magic_find);
				}
				else{
					break;
				}
			}
			
			//Determine slot.
			int random_slot=RNG.random_range(0,3);
			String slot_name="";
			if(random_slot==0){
				slot_name="weapon";
			}
			else if(random_slot==1){
				slot_name="head";
			}
			else if(random_slot==2){
				slot_name="chest";
			}
			else if(random_slot==3){
				slot_name="feet";
			}
			
			if(force_slot.length()>0){
				slot_name=force_slot;
			}
			
			equipment.add(0,new Equipment());
			
			///QQQ For this release, I'm disabling uniques
			/*if(RNG.random_range(0,999)<5){
				//Select a random item with the slot.
				int item_index=0;
				do{
					item_index=RNG.random_range(0,Templates.equipment.size()-1);
				}while(!Templates.equipment.get(item_index).slot.equals(slot_name) || !Templates.equipment.get(item_index).unique);
				
				equipment.get(0).name=Templates.equipment.get(item_index).name;
				
				equipment.get(0).full_name=Templates.equipment.get(item_index).full_name;
				equipment.get(0).description=Templates.equipment.get(item_index).description;
				equipment.get(0).level=Templates.equipment.get(item_index).level;
				equipment.get(0).bits=Templates.equipment.get(item_index).bits;
				equipment.get(0).branch=Templates.equipment.get(item_index).branch;
				equipment.get(0).weight=Templates.equipment.get(item_index).weight;
				
				equipment.get(0).buff_hunger=Templates.equipment.get(item_index).buff_hunger;
				equipment.get(0).buff_thirst=Templates.equipment.get(item_index).buff_thirst;
				equipment.get(0).buff_poop=Templates.equipment.get(item_index).buff_poop;
				equipment.get(0).buff_dirty=Templates.equipment.get(item_index).buff_dirty;
				equipment.get(0).buff_weight=Templates.equipment.get(item_index).buff_weight;
				equipment.get(0).buff_sick=Templates.equipment.get(item_index).buff_sick;
				equipment.get(0).buff_happy=Templates.equipment.get(item_index).buff_happy;
				equipment.get(0).buff_energy_regen=Templates.equipment.get(item_index).buff_energy_regen;
				equipment.get(0).buff_strength_regen=Templates.equipment.get(item_index).buff_strength_regen;
				equipment.get(0).buff_dexterity_regen=Templates.equipment.get(item_index).buff_dexterity_regen;
				equipment.get(0).buff_stamina_regen=Templates.equipment.get(item_index).buff_stamina_regen;
				equipment.get(0).buff_energy_max=Templates.equipment.get(item_index).buff_energy_max;
				equipment.get(0).buff_strength_max=Templates.equipment.get(item_index).buff_strength_max;
				equipment.get(0).buff_dexterity_max=Templates.equipment.get(item_index).buff_dexterity_max;
				equipment.get(0).buff_stamina_max=Templates.equipment.get(item_index).buff_stamina_max;
				equipment.get(0).buff_death=Templates.equipment.get(item_index).buff_death;
				equipment.get(0).buff_magic_find=Templates.equipment.get(item_index).buff_magic_find;
			}
			else{*/
				//Select a random item with the slot.
				int item_index=0;
				do{
					item_index=RNG.random_range(0,Templates.equipment.size()-1);
				}while(!Templates.equipment.get(item_index).slot.equals(slot_name) || Templates.equipment.get(item_index).unique);
				
				equipment.get(0).name=Templates.equipment.get(item_index).name;
				
				equipment.get(0).weight=0.01*(double)RNG.random_range(Equipment.WEIGHT_MIN,Equipment.WEIGHT_MAX);
				
				//Determine the item's level.
				int item_level=RNG.random_range(base_level-2,base_level+1+(int)Math.ceil(0.2*(double)magic_find));
				if(item_level<1){
					item_level=1;
				}
				
				//Determine the item's build.
				short branch=(short)RNG.random_range((int)Pet_Type.BRANCH_BEGIN,(int)Pet_Type.BRANCH_END-1);
				equipment.get(0).branch=branch;
				
				//Level the item up.
				for(;equipment.get(0).level<item_level;equipment.get(0).level++){
					equipment.get(0).strength_max_increase();
					
					equipment.get(0).dexterity_max_increase();
					
					equipment.get(0).stamina_max_increase();
				}
				
				//Determine the item's other buff(s), if any.
				//We skip the 3 main stat max buffs here, because they were already handled when leveling the item up.
				int buff_cap=2+(int)Math.ceil(0.1*(double)magic_find);
				int tries_cap=buff_cap;
				for(int tries=0;equipment.get(0).get_buff_names().size()<buff_cap && tries<tries_cap;tries++){
					int buff_to_try=RNG.random_range(0,13);
					
					if(buff_to_try==0){
						equipment.get(0).try_for_buff_hunger(magic_find);
					}
					else if(buff_to_try==1){
						equipment.get(0).try_for_buff_thirst(magic_find);
					}
					else if(buff_to_try==2){
						equipment.get(0).try_for_buff_poop(magic_find);
					}
					else if(buff_to_try==3){
						equipment.get(0).try_for_buff_dirty(magic_find);
					}
					else if(buff_to_try==4){
						equipment.get(0).try_for_buff_weight(magic_find);
					}
					else if(buff_to_try==5){
						equipment.get(0).try_for_buff_sick(magic_find);
					}
					else if(buff_to_try==6){
						equipment.get(0).try_for_buff_happy(magic_find);
					}
					else if(buff_to_try==7){
						equipment.get(0).try_for_buff_energy_regen(magic_find);
					}
					else if(buff_to_try==8){
						equipment.get(0).try_for_buff_strength_regen(magic_find);
					}
					else if(buff_to_try==9){
						equipment.get(0).try_for_buff_dexterity_regen(magic_find);
					}
					else if(buff_to_try==10){
						equipment.get(0).try_for_buff_stamina_regen(magic_find);
					}
					else if(buff_to_try==11){
						equipment.get(0).try_for_buff_energy_max(magic_find);
					}
					else if(buff_to_try==12){
						equipment.get(0).try_for_buff_death(magic_find);
					}
					else if(buff_to_try==13){
						equipment.get(0).try_for_buff_magic_find(magic_find);
					}
				}
				
				equipment.get(0).bits=(int)Math.ceil(0.5*(double)(equipment.get(0).buff_strength_max+equipment.get(0).buff_dexterity_max+equipment.get(0).buff_stamina_max));
				if(equipment.get(0).get_buff_names().size()>0){
					equipment.get(0).bits=(int)Math.ceil((double)equipment.get(0).bits*(double)equipment.get(0).get_buff_names().size()*1.25);
				}

				if (equipment.get(0).bits < NEW_ITEM_MINIMUM_BITS) {
                    equipment.get(0).bits = NEW_ITEM_MINIMUM_BITS;
                }
				
				ArrayList<String> buff_names=equipment.get(0).get_buff_names();
				String prefix_buff="";
				if(buff_names.size()>0){
					prefix_buff=buff_names.get(RNG.random_range(0,buff_names.size()-1));
				}
				
				String prefix=get_buff_prefix(prefix_buff);
				String suffix=get_buff_suffix(branch);
				
				String prefix_spacer="";
				if(prefix.length()>0){
					prefix_spacer=" ";
				}
				String suffix_spacer="";
				if(suffix.length()>0){
					suffix_spacer=" of ";
				}
				
				equipment.get(0).full_name=prefix+prefix_spacer+Strings.first_letter_capital_all_words(equipment.get(0).name)+suffix_spacer+suffix;
			///}
			
			return equipment.get(0).full_name;
		}
		
		return "";
	}
	
	public void sell_equipment(ArrayList<Integer> indices){
		//Look through the list of sold items, which is in the form of indices into the equipment list.
		for(int i=0;i<indices.size();i++){
			//Add the value of the item to bits.
			bits+=equipment.get(indices.get(i).intValue()).bits;
			bits_bound();
			
			//Remove the item from the equipment list.
			equipment.remove(indices.get(i).intValue());
			
			//Decrement all indices that are higher than the one we just processed.
			for(int n=0;n<indices.size();n++){
				if(indices.get(n).intValue()>indices.get(i).intValue()){
					indices.set(n,indices.get(n).intValue()-1);
				}
			}
			
			//Remove the index entry from the list.
			indices.remove(i);
			i--;
		}
	}
	
	public boolean has_perma_item(String item_name){
		for(int i=0;i<perma_items.size();i++){
			if(perma_items.get(i).name.equals(item_name)){
				return true;
			}
		}
		
		return false;
	}
	
	public Perma_Item get_held_item(){
		for(int i=0;i<perma_items.size();i++){
			if(perma_items.get(i).held){
				return perma_items.get(i);
			}
		}
		
		return null;
	}
	
	public Perma_Item get_item(String item_name){
		for(int i=0;i<perma_items.size();i++){
			if(perma_items.get(i).name.equals(item_name)){
				return perma_items.get(i);
			}
		}
		
		return null;
	}
	
	public boolean on_touch_event(MotionEvent event, Context context){
		Resources res = context.getResources();

		boolean used=false;
		
		float x=event.getX();
		float y=event.getY();
		
		switch(event.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
			//If no item is already held.
			if(get_held_item()==null){
				for(int i=perma_items.size()-1;i>=0;i--){
					if(Collision.check(perma_items.get(i).x,perma_items.get(i).y,perma_items.get(i).w,perma_items.get(i).h,x,y,Px.px(res,2),Px.px(res,2))){
						perma_items.get(i).held=true;
						
						Sound_Manager.play_sound(context, Sound.PERMA_ITEM_GRAB);
						
						used=true;
						
						break;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			//If an item is held.
			if(get_held_item()!=null){
				get_held_item().held=false;
				
				Sound_Manager.play_sound(context, Sound.PERMA_ITEM_DROP);
				
				used=true;
			}
			break;
		}
		
		if(!used){
			//If an item is held.
			if(get_held_item()!=null){
				get_held_item().x=x-get_held_item().w/2;
				get_held_item().y=y-get_held_item().h/2;
				
				used=true;
			}
		}
		
		return used;
	}
	
	//If the pet is sleeping, it has just been interacted with, so wake it up for a few minutes.
	public void sleeping_wake_up(){
		if(sleeping){
			if(!sleeping_woken_up){
				sleeping_woken_up=true;
				
				happy-=Pet_Status.HAPPY_LOSS_AWAKEN;
				happy_bound();
				queue_thought(Thought_Type.SAD);
			}
			
			sleeping_woken_up_time=System.currentTimeMillis();
		}
	}
	
	public static double determine_base_temp(){
		return (double)RNG.random_range(RNG.TICK,15,25);
	}
	
	public void reset_poop_sprites(Image image,View view){
		for(int i=0;i<poops.size();i++){
			poops.get(i).reset_sprite(image,view);
		}
	}
	
	public void reset_perma_item_sprites(Image image,View view){
		for(int i=0;i<perma_items.size();i++){
			perma_items.get(i).reset_sprite(image,view);
		}
	}
	
	public void queue_thought(int get_type){
		if(age_tier!=Age_Tier.EGG && age_tier!=Age_Tier.DEAD && (!sleeping || (sleeping && sleeping_woken_up))){
			boolean thought_already_present=false;
			
			for(int i=0;i<thoughts.size();i++){
				if(thoughts.get(i)==get_type){
					thought_already_present=true;
					break;
				}
			}
			
			if(!thought_already_present){
				thoughts.add(Integer.valueOf(get_type));
			}
		}
	}
	
	public void reset_food_buffs(){
		buff_hunger=0;
		buff_thirst=0;
		buff_poop=0;
		buff_dirty=0;
		buff_weight=0;
		buff_sick=0;
		buff_happy=0;
		buff_energy_regen=0;
		buff_strength_regen=0;
		buff_dexterity_regen=0;
		buff_stamina_regen=0;
		buff_energy_max=0;
		buff_strength_max=0;
		buff_dexterity_max=0;
		buff_stamina_max=0;
		buff_death=0;
		buff_magic_find=0;
	}
	
	public void process_food_buffs(long seconds_passed){
		for(long i=0;i<seconds_passed;i++){
			// Food buffs //
			
			if(buff_hunger>0){
				buff_hunger--;
			}
			if(buff_thirst>0){
				buff_thirst--;
			}
			if(buff_poop>0){
				buff_poop--;
			}
			if(buff_dirty>0){
				buff_dirty--;
			}
			if(buff_weight>0){
				buff_weight--;
			}
			if(buff_sick>0){
				buff_sick--;
			}
			if(buff_happy>0){
				buff_happy--;
			}
			if(buff_energy_regen>0){
				buff_energy_regen--;
			}
			if(buff_strength_regen>0){
				buff_strength_regen--;
			}
			if(buff_dexterity_regen>0){
				buff_dexterity_regen--;
			}
			if(buff_stamina_regen>0){
				buff_stamina_regen--;
			}
			if(buff_energy_max>0){
				buff_energy_max--;
			}
			if(buff_strength_max>0){
				buff_strength_max--;
			}
			if(buff_dexterity_max>0){
				buff_dexterity_max--;
			}
			if(buff_stamina_max>0){
				buff_stamina_max--;
			}
			if(buff_death>0){
				buff_death--;
			}
			if(buff_magic_find>0){
				buff_magic_find--;
			}
		}
	}
	
	public void process_stat_rises(long seconds_passed){
		//A scale factor for converting the numbers used for temp from 15 minute values to 1 second.
		double SCALE=(double)GameView.SECONDS_PER_TIME_TICK/(double)GameView.SECONDS_PER_TICK;
		
		for(long i=0;i<seconds_passed;i++){
			// Hunger //
			
			if(hunger>0){
				hunger_rise+=(double)get_hunger_rate()*SCALE;
				if(hunger_rise>1.0){
					hunger_rise-=1.0;
					
					hunger-=1;
					
					hunger_bound();
				}
			}
			
			// Thirst //
			
			if(thirst>0){
				thirst_rise+=(double)get_thirst_rate()*SCALE;
				if(thirst_rise>1.0){
					thirst_rise-=1.0;
					
					thirst-=1;
					
					thirst_bound();
				}
			}
			
			// Strength //
			
			if(get_strength()<get_strength_max()){
				strength_rise+=Math.ceil((double)get_strength_max()*(double)get_strength_regen_rate())*SCALE;
				if(strength_rise>1.0){
					strength_rise-=1.0;
					
					strength+=1;
					
					strength_bound();
				}
			}
			
			// Dexterity //
			
			if(get_dexterity()<get_dexterity_max()){
				dexterity_rise+=Math.ceil((double)get_dexterity_max()*(double)get_dexterity_regen_rate())*SCALE;
				if(dexterity_rise>1.0){
					dexterity_rise-=1.0;
					
					dexterity+=1;
					
					dexterity_bound();
				}
			}
			
			// Stamina //
			
			if(get_stamina()<get_stamina_max()){
				stamina_rise+=Math.ceil((double)get_stamina_max()*(double)get_stamina_regen_rate())*SCALE;
				if(stamina_rise>1.0){
					stamina_rise-=1.0;
					
					stamina+=1;
					
					stamina_bound();
				}
			}
			
			// Energy //
			
			if(get_energy()<get_energy_max()){
				energy_rise+=Math.ceil((double)get_energy_max()*(double)get_energy_regen_rate())*SCALE;
				if(energy_rise>1.0){
					energy_rise-=1.0;
					
					energy+=1;
					
					energy_bound();
				}
			}
		}
	}
	
	public void process_temperature(long seconds_passed){
		//If enough time has passed since the last base temperature update.
		if(System.currentTimeMillis()-last_base_temp>=BASE_TEMP_UPDATE_TIME){
			last_base_temp=System.currentTimeMillis();
			base_temp=determine_base_temp();
		}
		
		//A scale factor for converting the numbers used for temp from 15 minute values to 1 second.
		double SCALE=(double)GameView.SECONDS_PER_TIME_TICK/(double)GameView.SECONDS_PER_TICK;
		
		for(long i=0;i<seconds_passed;i++){
			//Change the temperature based on machine status.
			if(ac){
				temp-=TEMP_CHANGE_MACHINE*SCALE;
				temp_bound();
			}
			else if(heater){
				temp+=TEMP_CHANGE_MACHINE*SCALE;
				temp_bound();
			}
			else{
				if(Math.abs(base_temp-temp)<TEMP_CHANGE_NO_MACHINE*SCALE){
					temp=base_temp;
				}
				else{
					if(temp<base_temp){
						temp+=TEMP_CHANGE_NO_MACHINE*SCALE;
						temp_bound();
					}
					else{
						temp-=TEMP_CHANGE_NO_MACHINE*SCALE;
						temp_bound();
					}
				}
			}
			
			//Move the temperature towards the base temperature.
			if(temp!=base_temp){
				if(temp<base_temp){
					temp+=TEMP_CHANGE_ALWAYS*SCALE;
					temp_bound();
				}
				else{
					temp-=TEMP_CHANGE_ALWAYS*SCALE;
					temp_bound();
				}
			}
			
			//If the temperature is past the turn-off threshold, do that.
			if(ac && temp<=TEMP_OPTIMUM-TEMP_TURN_OFF_THRESHOLD){
				ac=false;
			}
			else if(heater && temp>=TEMP_OPTIMUM+TEMP_TURN_OFF_THRESHOLD){
				heater=false;
			}
		}
	}
	
	public boolean should_be_sleeping(){
		//The current time.
		Calendar current_time=Calendar.getInstance();
		
		//Bedtime today.
		Calendar bedtime=Calendar.getInstance();
		bedtime.set(Calendar.HOUR_OF_DAY,sleep_time.get(Calendar.HOUR_OF_DAY));
		bedtime.set(Calendar.MINUTE,sleep_time.get(Calendar.MINUTE));
		
		//The next wake time as derived from sleep_time.
		Calendar derived_wake_time=get_wake_time();
		
		if(!sleeping){
			//If current time is between today's bedtime and the wake time derived from it.
			if(current_time.compareTo(bedtime)>=0 && current_time.compareTo(derived_wake_time)<0){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			//If current time is equal to or after the saved wake time.
			if(current_time.compareTo(wake_time)>=0){
				return false;
			}
			else{
				return true;
			}
		}
	}
	
	public Calendar get_wake_time(){
		Calendar c=Calendar.getInstance();
		
		c.set(Calendar.HOUR_OF_DAY,sleep_time.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE,sleep_time.get(Calendar.MINUTE));
		c.add(Calendar.HOUR_OF_DAY,8);
		
		return c;
	}
	
	public Pet_Type get_teen_type(){
		//Determine the most-fed food categories.
		ArrayList<Integer> top_categories=new ArrayList<Integer>();
		int top_amount=1;
		
		for(int i=0;i<food_categories.size();i++){
			//None and Junk categories are completely ignored.
			if(food_categories.get(i)>=top_amount && i!=Food_Category.NONE && i!=Food_Category.JUNK){
				//Add this category to the top categories list.
				top_categories.add(0,i);
				
				top_amount=food_categories.get(i);
			}
		}
		
		if(top_categories.size()>0){
			//Remove all lesser categories and handle ties.
			while(top_categories.size()>1){
				//If the second entry is less than the first entry.
				if(top_categories.get(1)<top_categories.get(0)){
					top_categories.remove(1);
				}
				//If there is a tie.
				else{
					top_categories.remove(RNG.random_range(RNG.TICK,0,1));
				}
			}
			
			return Pet_Type.get_evolutionary_branch(top_categories.get(0));
		}
		else{
			return Pet_Type.get_random_evolutionary_branch();
		}
	}
	
	public boolean eligible_for_senior(){
		if(is_well_fed() && is_well_watered() && !sick && temp_is_optimum() && !needs_poop_cleaned() && !needs_bath() &&
				is_happy() && !is_obese() && level>=LEVEL_NEEDED_FOR_SENIOR && get_energy()>=get_energy_max() &&
				get_strength()>=get_strength_max() && get_dexterity()>=get_dexterity_max() && get_stamina()>=get_stamina_max() &&
				(battles_won>=MP_BATTLE_WINS_NEEDED_FOR_SENIOR || battles_won_sp>=SP_BATTLE_WINS_NEEDED_FOR_SENIOR)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public double get_weight_upgrade(){
		double number=0.0;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).weight;
			}
		}
		
		return number;
	}
	
	public short get_strength_upgrade(){
		double number=0.0;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_strength_max;
			}
		}
		if(buff_strength_max>0){
			number+=FOOD_BUFF_STRENGTH_MAX;
		}
		if(has_perma_item("robot")){
			number+=ITEM_BUFF_ROBOT;
		}
		if(has_perma_item("at")){
			number+=ITEM_BUFF_AT;
		}
		
		return (short)number;
	}
	
	public short get_dexterity_upgrade(){
		double number=0.0;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_dexterity_max;
			}
		}
		if(buff_dexterity_max>0){
			number+=FOOD_BUFF_DEXTERITY_MAX;
		}
		if(has_perma_item("piano")){
			number+=ITEM_BUFF_PIANO;
		}
		
		return (short)number;
	}
	
	public short get_stamina_upgrade(){
		double number=0.0;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_stamina_max;
			}
		}
		if(buff_stamina_max>0){
			number+=FOOD_BUFF_STAMINA_MAX;
		}
		if(has_perma_item("shoes")){
			number+=ITEM_BUFF_SHOES;
		}
		
		return (short)number;
	}
	
	public short get_energy_upgrade(){
		double number=0.0;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_energy_max;
			}
		}
		if(buff_energy_max>0){
			number+=FOOD_BUFF_ENERGY_MAX;
		}
		if(has_perma_item("sign")){
			number+=ITEM_BUFF_SIGN;
		}
		if(has_perma_item("computer")){
			number+=ITEM_BUFF_COMPUTER;
		}
		
		return (short)number;
	}
	
	public double get_weight(){
		double number=weight;
		
		number+=get_weight_upgrade();
		
		return number;
	}
	
	public short get_strength(){
		double number=strength;
		
		number+=Age_Tier.get_stat_bonus(age_tier);
		
		number+=Pet_Type.get_strength_bonus(type);
		
		number+=get_strength_upgrade();
		
		return (short)number;
	}
	
	public short get_strength_max(){
		double number=strength_max;
		
		number+=Age_Tier.get_stat_bonus(age_tier);
		
		number+=Pet_Type.get_strength_bonus(type);
		
		number+=get_strength_upgrade();
		
		return (short)number;
	}
	
	public short get_dexterity(){
		double number=dexterity;
		
		number+=Age_Tier.get_stat_bonus(age_tier);
		
		number+=Pet_Type.get_dexterity_bonus(type);
		
		number+=get_dexterity_upgrade();
		
		return (short)number;
	}
	
	public short get_dexterity_max(){
		double number=dexterity_max;
		
		number+=Age_Tier.get_stat_bonus(age_tier);
		
		number+=Pet_Type.get_dexterity_bonus(type);
		
		number+=get_dexterity_upgrade();
		
		return (short)number;
	}
	
	public short get_stamina(){
		double number=stamina;
		
		number+=Age_Tier.get_stat_bonus(age_tier);
		
		number+=Pet_Type.get_stamina_bonus(type);
		
		number+=get_stamina_upgrade();
		
		return (short)number;
	}
	
	public short get_stamina_max(){
		double number=stamina_max;
		
		number+=Age_Tier.get_stat_bonus(age_tier);
		
		number+=Pet_Type.get_stamina_bonus(type);
		
		number+=get_stamina_upgrade();
		
		return (short)number;
	}
	
	public short get_energy(){
		double number=energy;
		
		number+=get_energy_upgrade();
		
		return (short)number;
	}
	
	public short get_energy_max(){
		double number=Age_Tier.get_energy_max(age_tier);
		
		number+=get_energy_upgrade();
		
		return (short)number;
	}
	
	public void gain_weight(double change){
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				if(change>0.0){
					change*=equipment_slots.get(i).buff_weight;
				}
				else{
					change/=equipment_slots.get(i).buff_weight;
				}
			}
		}
		if(buff_weight>0){
			if(change>0.0){
				change*=FOOD_BUFF_WEIGHT;
			}
			else{
				change/=FOOD_BUFF_WEIGHT;
			}
		}
		if(has_perma_item("beach_ball")){
			if(change>0.0){
				change*=ITEM_BUFF_BEACH_BALL;
			}
			else{
				change/=ITEM_BUFF_BEACH_BALL;
			}
		}
		
		weight+=change;
		
		weight_bound();
	}
	
	public int get_sick_chance(){
		int number=BASE_SICK_CHANCE;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number-=equipment_slots.get(i).buff_sick;
			}
		}
		if(buff_sick>0){
			number-=FOOD_BUFF_SICK;
		}
		if(has_perma_item("wheat")){
			number-=ITEM_BUFF_WHEAT;
		}
		
		if(number<1){
			number=1;
		}
		
		return number;
	}
	
	public short get_hunger_rate(){
		double number=BASE_HUNGER_RATE;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number-=equipment_slots.get(i).buff_hunger;
			}
		}
		if(buff_hunger>0){
			number-=FOOD_BUFF_HUNGER;
		}
		if(has_perma_item("stone")){
			number-=ITEM_BUFF_STONE;
		}
		
		if(number<1){
			number=1;
		}
		
		return (short)number;
	}
	
	public short get_thirst_rate(){
		double number=BASE_THIRST_RATE;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number-=equipment_slots.get(i).buff_thirst;
			}
		}
		if(buff_thirst>0){
			number-=FOOD_BUFF_THIRST;
		}
		if(has_perma_item("leaf")){
			number-=ITEM_BUFF_LEAF;
		}
		
		if(number<1){
			number=1;
		}
		
		return (short)number;
	}
	
	public short get_poop_rate(){
		double number=(short)RNG.random_range(RNG.TICK,BASE_POOP_RATE_MIN,BASE_POOP_RATE_MAX);
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number-=equipment_slots.get(i).buff_poop;
			}
		}
		if(buff_poop>0){
			number-=FOOD_BUFF_POOP;
		}
		if(has_perma_item("tree")){
			number-=ITEM_BUFF_TREE;
		}
		
		if(number<1){
			number=1;
		}
		
		return (short)number;
	}
	
	public short get_dirty_rate(){
		double number=(short)RNG.random_range(RNG.TICK,BASE_DIRTY_RATE_MIN,BASE_DIRTY_RATE_MAX);
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number-=equipment_slots.get(i).buff_dirty;
			}
		}
		if(buff_dirty>0){
			number-=FOOD_BUFF_DIRTY;
		}
		if(has_perma_item("towel")){
			number-=ITEM_BUFF_TOWEL;
		}
		
		if(number<1){
			number=1;
		}
		
		return (short)number;
	}
	
	public float get_energy_regen_rate(){
		double number=BASE_ENERGY_REGEN_RATE;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_energy_regen;
			}
		}
		if(buff_energy_regen>0){
			number+=FOOD_BUFF_ENERGY_REGEN;
		}
		if(has_perma_item("berry_bush")){
			number+=ITEM_BUFF_BERRY_BUSH;
		}
		
		if(number>1.0){
			number=1.0;
		}
		
		return (float)number;
	}
	
	public float get_strength_regen_rate(){
		double number=BASE_STRENGTH_REGEN_RATE;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_strength_regen;
			}
		}
		if(buff_strength_regen>0){
			number+=FOOD_BUFF_STRENGTH_REGEN;
		}
		if(has_perma_item("trampoline")){
			number+=ITEM_BUFF_TRAMPOLINE;
		}
		
		if(number>1.0){
			number=1.0;
		}
		
		return (float)number;
	}
	
	public float get_dexterity_regen_rate(){
		double number=BASE_DEXTERITY_REGEN_RATE;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_dexterity_regen;
			}
		}
		if(buff_dexterity_regen>0){
			number+=FOOD_BUFF_DEXTERITY_REGEN;
		}
		if(has_perma_item("jump_rope")){
			number+=ITEM_BUFF_JUMP_ROPE;
		}
		
		if(number>1.0){
			number=1.0;
		}
		
		return (float)number;
	}
	
	public float get_stamina_regen_rate(){
		double number=BASE_STAMINA_REGEN_RATE;
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(equipment_slots.get(i)!=null){
				number+=equipment_slots.get(i).buff_stamina_regen;
			}
		}
		if(buff_stamina_regen>0){
			number+=FOOD_BUFF_STAMINA_REGEN;
		}
		if(has_perma_item("barbell")){
			number+=ITEM_BUFF_BARBELL;
		}
		
		if(number>1.0){
			number=1.0;
		}
		
		return (float)number;
	}
	
	public boolean is_obese(){
		if(get_weight()>=OBESITY){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean is_starving(){
		if(hunger<=(short)((double)Age_Tier.get_hunger_max(age_tier)*0.25)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean is_well_fed(){
		if(hunger>=(short)((float)Age_Tier.get_hunger_max(age_tier)*0.75f)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean is_very_thirsty(){
		if(thirst<=19){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean is_well_watered(){
		if(thirst>=49){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean is_max_watered(){
		if(thirst>=THIRST_MAX){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean is_happy(){
		if(happy>=(short)((float)HAPPY_MAX*0.5f)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean temp_is_optimum(){
		if(temp>TEMP_OPTIMUM-1.0 && temp<TEMP_OPTIMUM+1.0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean temp_is_good(){
		if((temp>=TEMP_OPTIMUM-3.0 && temp<=TEMP_OPTIMUM-1.0) ||
				(temp>=TEMP_OPTIMUM+1.0 && temp<=TEMP_OPTIMUM+3.0)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean temp_is_ok(){
		if((temp>=TEMP_OPTIMUM-9.0 && temp<=TEMP_OPTIMUM-4.0) ||
				(temp>=TEMP_OPTIMUM+4.0 && temp<=TEMP_OPTIMUM+9.0)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean temp_is_bad(){
		if((temp>=TEMP_OPTIMUM-14.0 && temp<=TEMP_OPTIMUM-10.0) ||
				(temp>=TEMP_OPTIMUM+10.0 && temp<=TEMP_OPTIMUM+14.0)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean temp_is_very_bad(){
		if(temp<=TEMP_OPTIMUM-15.0 || temp>=TEMP_OPTIMUM+15.0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean temp_is_cold(){
		if(temp_is_bad() || temp_is_very_bad()){
			if(temp<TEMP_OPTIMUM){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean temp_is_hot(){
		if(temp_is_bad() || temp_is_very_bad()){
			if(temp>TEMP_OPTIMUM){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean needs_poop_cleaned(){
		if(poops.size()>0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean needs_bath(){
		if(bath==99){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void bits_bound(){
		if(bits<0){
			bits=0;
		}
		else if(bits>Integer.MAX_VALUE-1000){
			bits=Integer.MAX_VALUE-1000;
		}
	}
	
	public void death_counter_bound(){
		if(death_counter<0){
			death_counter=0;
		}
		else if(death_counter>Integer.MAX_VALUE-1000){
			death_counter=Integer.MAX_VALUE-1000;
		}
	}
	
	public void happy_bound(){
		if(happy<HAPPY_MIN){
			happy=HAPPY_MIN;
		}
		else if(happy>HAPPY_MAX){
			happy=HAPPY_MAX;
		}
	}
	
	public void temp_bound(){
		if(temp<TEMP_MIN){
			temp=TEMP_MIN;
		}
		else if(temp>TEMP_MAX){
			temp=TEMP_MAX;
		}
	}
	
	public void weight_bound(){
		if(weight<WEIGHT_MIN){
			weight=WEIGHT_MIN;
		}
		else if(weight>Double.MAX_VALUE-1000.0){
			weight=Double.MAX_VALUE-1000.0;
		}
	}
	
	public void hunger_bound(){
		if(hunger<0){
			hunger=0;
		}
		else if(hunger>Age_Tier.get_hunger_max(age_tier)){
			hunger=Age_Tier.get_hunger_max(age_tier);
		}
	}
	
	public void thirst_bound(){
		if(thirst<0){
			thirst=0;
		}
		else if(thirst>THIRST_MAX){
			thirst=THIRST_MAX;
		}
	}
	
	public void strength_bound(){
		short min=(short)(0-get_strength_upgrade());
		
		if(strength<min){
			strength=min;
		}
		else if(strength>strength_max){
			strength=strength_max;
		}
	}
	
	public void strength_max_bound(){
		if(strength_max<STRENGTH_MAX_MIN){
			strength_max=STRENGTH_MAX_MIN;
		}
		else if(strength_max>Short.MAX_VALUE-1000){
			strength_max=Short.MAX_VALUE-1000;
		}
	}
	
	public void dexterity_bound(){
		short min=(short)(0-get_dexterity_upgrade());
		
		if(dexterity<min){
			dexterity=min;
		}
		else if(dexterity>dexterity_max){
			dexterity=dexterity_max;
		}
	}
	
	public void dexterity_max_bound(){
		if(dexterity_max<DEXTERITY_MAX_MIN){
			dexterity_max=DEXTERITY_MAX_MIN;
		}
		else if(dexterity_max>Short.MAX_VALUE-1000){
			dexterity_max=Short.MAX_VALUE-1000;
		}
	}
	
	public void stamina_bound(){
		short min=(short)(0-get_stamina_upgrade());
		
		if(stamina<min){
			stamina=min;
		}
		else if(stamina>stamina_max){
			stamina=stamina_max;
		}
	}
	
	public void stamina_max_bound(){
		if(stamina_max<STAMINA_MAX_MIN){
			stamina_max=STAMINA_MAX_MIN;
		}
		else if(stamina_max>Short.MAX_VALUE-1000){
			stamina_max=Short.MAX_VALUE-1000;
		}
	}
	
	public void energy_bound(){
		short min=(short)(0-get_energy_upgrade());
		
		if(energy<min){
			energy=min;
		}
		else if(energy>Age_Tier.get_energy_max(age_tier)){
			energy=Age_Tier.get_energy_max(age_tier);
		}
	}
}
