package org.cheeseandbacon.bitbeast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;

public class Pet{
	private Pet_Status status;
	
	private AnimatedSprite sprite;
	private AnimatedSprite sprite_sleep;
	private AnimatedSprite sprite_ac;
	private AnimatedSprite sprite_heater;
	private AnimatedSprite sprite_dirty;
	
	private AnimatedSprite sprite_lit_computer;
	private AnimatedSprite sprite_lit_robot;
	private AnimatedSprite sprite_lit_christmas_tree;
	
	ArrayList<Overlay> overlays;
	ArrayList<Thought> thoughts;
	ArrayList<Evolution> evolutions;
	
	private int counter_feedback_cold;
	private int counter_feedback_dirty;
	private int counter_feedback_hot;
	private int counter_feedback_hungry;
	private int counter_feedback_sick;
	private int counter_feedback_thirsty;
	private int counter_feedback_unhappy;
	private int counter_feedback_poop;
	private int counter_feedback_music;
	
	float x;
	float y;
	int w;
	int h;
	private int facing;
	private int move_state;
	private int move_speed;
	
	//The starting location of the last "petting" action.
	float pet_start_x;
	float pet_start_y;
	
	private static final int SPEED_MIN=2;
	private static final int SPEED_MAX=4;
	private static final long THOUGHT_VIBRATE_TIME=1000;
	//The length of a thought bubble, in seconds.
	private static final long THOUGHT_LENGTH=4;
	private static final long OVERLAY_VIBRATE_TIME=500;
	//The minimum and maximum time allowed between feedback, in seconds.
	private static final int COUNTER_FEEDBACK_SHORT_MIN=5;
	private static final int COUNTER_FEEDBACK_SHORT_MAX=18;
	private static final int COUNTER_FEEDBACK_LONG_MIN=30;
	private static final int COUNTER_FEEDBACK_LONG_MAX=120;
	private static final int COUNTER_FEEDBACK_FUN_MIN=60;
	private static final int COUNTER_FEEDBACK_FUN_MAX=300;
	
	private static final float THERMOMETER_WIDTH=24.0f;
	private static final float THERMOMETER_HEIGHT=128.0f;
	private static final float THERMOMETER_PADDING=8.0f;
	
	private static final float STAT_BAR_WIDTH=128.0f;
	private static final float STAT_BAR_HEIGHT=20.0f;
	private static final float STAT_BAR_PADDING=4.0f;
	
	//Counts down to next wander change.
	private int wander_counter;
	//The min and max frequency for wander changes.
	private int wander_frequency_min;
	private int wander_frequency_max;
	
	public Pet(){
		status=new Pet_Status();
		
		sprite=null;
		sprite_sleep=null;
		sprite_ac=null;
		sprite_heater=null;
		sprite_dirty=null;
		
		sprite_lit_computer=null;
		sprite_lit_robot=null;
		sprite_lit_christmas_tree=null;
		
		overlays=new ArrayList<Overlay>();
		thoughts=new ArrayList<Thought>();
		evolutions=new ArrayList<Evolution>();
		
		counter_feedback_cold=get_feedback_time_short();
		counter_feedback_dirty=get_feedback_time_short();
		counter_feedback_hot=get_feedback_time_short();
		counter_feedback_hungry=get_feedback_time_short();
		counter_feedback_sick=get_feedback_time_short();
		counter_feedback_thirsty=get_feedback_time_short();
		counter_feedback_unhappy=get_feedback_time_short();
		counter_feedback_poop=get_feedback_time_short();
		counter_feedback_music=get_feedback_time_fun();
		
		x=-1000.0f;
		y=-1000.0f;
		w=0;
		h=0;
		facing=Direction.RIGHT;
		move_state=Direction.NONE;
		move_speed=RNG.random_range(SPEED_MIN,SPEED_MAX);
		
		pet_start_x=-1;
		pet_start_y=-1;
		
		wander_frequency_min=2*(int)FPS.fps;
		wander_frequency_max=4*(int)FPS.fps;
		wander_counter=0;
	}
	
	public void reset_sprite(Image image,View view,Resources res){
		image.load_pet(res,status.type);
		Sprite_Data sd=image.pet;
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		sd=image.sleep;
		
		sprite_sleep=new AnimatedSprite();
		sprite_sleep.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		sd=image.ac;
		
		sprite_ac=new AnimatedSprite();
		sprite_ac.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	
		sd=image.heater;
		
		sprite_heater=new AnimatedSprite();
		sprite_heater.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		sd=image.dirty;
		
		sprite_dirty=new AnimatedSprite();
		sprite_dirty.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		sd=image.lit_computer;
		
		sprite_lit_computer=new AnimatedSprite();
		sprite_lit_computer.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		sd=image.lit_robot;
		
		sprite_lit_robot=new AnimatedSprite();
		sprite_lit_robot.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		sd=image.lit_christmas_tree;
		
		sprite_lit_christmas_tree=new AnimatedSprite();
		sprite_lit_christmas_tree.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public void reset_overlay_sprites(Image image,View view){
		for(int i=0;i<overlays.size();i++){
			overlays.get(i).reset_sprite(image,view);
		}
	}
	
	public void reset_thought_sprites(Image image,View view){
		for(int i=0;i<thoughts.size();i++){
			thoughts.get(i).reset_sprite(image,view);
		}
	}
	
	public void reset_evolution_sprites(Image image,View view){
		for(int i=0;i<evolutions.size();i++){
			evolutions.get(i).reset_sprite(image,view,view.getResources());
		}
	}
	
	public Pet_Status get_status(){
		return status;
	}
	
	public AnimatedSprite get_sprite(){
		return sprite;
	}
	
	public static int get_feedback_time_short(){
		return (int)FPS.fps*RNG.random_range(COUNTER_FEEDBACK_SHORT_MIN,COUNTER_FEEDBACK_SHORT_MAX);
	}
	
	public static int get_feedback_time_long(){
		return (int)FPS.fps*RNG.random_range(COUNTER_FEEDBACK_LONG_MIN,COUNTER_FEEDBACK_LONG_MAX);
	}
	
	public static int get_feedback_time_fun(){
		return (int)FPS.fps*RNG.random_range(COUNTER_FEEDBACK_FUN_MIN,COUNTER_FEEDBACK_FUN_MAX);
	}
	
	public void set_status(Pet_Status new_status){
		status=new_status;
	}
	
	public void evolve(Image image,View view,Context context,boolean play_sound,Handler handler){
		if(play_sound){
			Sound_Manager.play_sound(Sound.EVOLUTION_1);
		}
		
		Pet_Type old_type=status.type;
		
		//Increment the age tier.
		status.age_tier=status.age_tier.get_next();
		
		//If the pet has become a child, choose its favorite food.
		if(status.age_tier==Age_Tier.CHILD){
			//If the pet was fed some food before now, and we pass a random check.
			if(status.food_fed.size()>0 && RNG.random_range(0,99)<75){
				//Pick a food from the fed foods list randomly.
				status.favorite_food=status.food_fed.get(RNG.random_range(0,status.food_fed.size()-1));
			}
			//Otherwise, we just assign a favorite food completely at random.
			else{
				//Create a list of foods eligible for being the favorite.
				ArrayList<String> eligible_foods=new ArrayList<String>();
				for(int i=0;i<Templates.food.size();i++){
					if(Templates.food.get(i).can_be_favorite){
						eligible_foods.add(Templates.food.get(i).name);
					}
				}
				
				//Randomly assign the favorite food from the created list.
				status.favorite_food=eligible_foods.get(RNG.random_range(0,eligible_foods.size()-1));
			}
			
			//We no longer need the fed foods list.
			status.food_fed.clear();
		}
		
		//Determine the new type.
		if(status.age_tier==Age_Tier.BABY){
			status.type=Pet_Type.MATKABIT;
		}
		else if(status.age_tier==Age_Tier.CHILD){
			status.type=Pet_Type.SHARBIT;
		}
		else if(status.age_tier==Age_Tier.TEEN){
			status.type=status.get_teen_type();
		}
		else if(status.age_tier==Age_Tier.YOUNG_ADULT || status.age_tier==Age_Tier.ADULT || status.age_tier==Age_Tier.SENIOR){
			status.type=status.type.get_next_type(status.age_tier);
		}
		
		status.energy=Age_Tier.get_energy_max(status.age_tier);
		status.strength=status.strength_max;
		status.dexterity=status.dexterity_max;
		status.stamina=status.stamina_max;
		
		//Reset wandering.
		wander_counter=0;
		
		//Reset death counter changes allowed.
		
		status.dc_starving=Pet_Status.DEATH_COUNTER_CHANGES_PER_AGE_TIER;
		status.dc_very_thirsty=Pet_Status.DEATH_COUNTER_CHANGES_PER_AGE_TIER;
		status.dc_sick=Pet_Status.DEATH_COUNTER_CHANGES_PER_AGE_TIER;
		status.dc_obese=Pet_Status.DEATH_COUNTER_CHANGES_PER_AGE_TIER;
		status.dc_not_obese=Pet_Status.DEATH_COUNTER_CHANGES_PER_AGE_TIER;
		status.dc_well_fed=Pet_Status.DEATH_COUNTER_CHANGES_PER_AGE_TIER;
		status.dc_well_watered=Pet_Status.DEATH_COUNTER_CHANGES_PER_AGE_TIER;
		status.dc_not_sick=Pet_Status.DEATH_COUNTER_CHANGES_PER_AGE_TIER;
		
		if(evolutions.size()==0){
			evolutions.add(new Evolution(image,view,old_type,w,h));
		}
		
		if(handler!=null){
			Message msg=handler.obtainMessage();
			msg.what=BitBeast.HANDLER_SET_BUTTON_COLORS;
			handler.sendMessage(msg);
		}
	}
	
	public void die(Image image,View view,Context context,Records records,Handler handler){
		if(records!=null){
			//Add this pet to the records.
			records.add_pet(status.name,status.bits,status.age,status.type,status.age_tier,status.get_weight(),status.get_strength_max(),status.get_dexterity_max(),status.get_stamina_max(),status.battles_won,status.battles_lost,status.battles_won_sp,status.battles_lost_sp,status.level);
			StorageManager.save_records(context,records);
		}
		
		//Show the death dialog.
		Bundle bundle=new Bundle();
		bundle.putString("name",status.name);
		
		if(handler!=null){
			Message msg=handler.obtainMessage();
			msg.what=BitBeast.HANDLER_DIE;
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
		
		status.age_tier=Age_Tier.DEAD;
		status.type=Pet_Type.DEAD;
		x=-1000;
		y=-1000;
		facing=Direction.RIGHT;
		move_state=Direction.NONE;
		
		//Remove all consumable buffs.
		status.reset_food_buffs();
		
		//Set might, deftness, hardiness, and energy to 0.
		status.strength=0;
		status.dexterity=0;
		status.stamina=0;
		status.energy=0;
		
		//Clean the pet.
		status.bath=0;
		
		if(image!=null && view!=null){
			reset_sprite(image,view,context.getResources());
		}
		
		//Reset wandering.
		wander_counter=0;
		
		if(handler!=null){
			Message msg=handler.obtainMessage();
			msg.what=BitBeast.HANDLER_SET_BUTTON_COLORS;
			handler.sendMessage(msg);
		}
	}
	
	public void process_time_tick(long get_age_increment,Image image,View view,Context context,Handler handler){
		if(status.age_tier!=Age_Tier.DEAD){
			status.age+=get_age_increment;
			
			if(status.sleeping && status.sleeping_woken_up && System.currentTimeMillis()-status.sleeping_woken_up_time>=300000L){
				status.sleeping_woken_up=false;
				status.sleeping_woken_up_time=-1L;
			}
		}
		
		status.process_temperature(get_age_increment);
		
		if(status.age_tier!=Age_Tier.DEAD){
			status.process_food_buffs(get_age_increment);
			
			status.process_stat_rises(get_age_increment);
			
			//Used to determine whether we have evolved once this call.
			//If so, we won't allow the evolution sound to be played anymore this call.
			boolean evolved_once=false;
			while(true){
				if(status.age>=Age_Tier.get_next_age_point(status.age_tier)){
					if(status.age_tier.ordinal()<Age_Tier.ADULT.ordinal() || (status.age_tier==Age_Tier.ADULT && status.eligible_for_senior())){
						evolve(image,view,context,!evolved_once,handler);
						evolved_once=true;
					}
					else{
						break;
					}
				}
				else{
					break;
				}
			}
		}
	}
	
	public void process_tick(long ticks,Image image,View view,Context context,Records records,Handler handler){
		if(status.age_tier!=Age_Tier.EGG && status.age_tier!=Age_Tier.DEAD){
			int poops_created=0;
			
			for(long i=0;i<ticks;i++){
				// Petting //
				
				status.pettings=0;
				
				// Experience //
				
				status.experience_gain=0;
				
				// Sleep //
				
				//Update sleep time.
				Calendar c=Calendar.getInstance();
				//If it is a different day since the last sleep time change.
				if(Options.last_sleep_time_change.get(Calendar.DAY_OF_YEAR)!=c.get(Calendar.DAY_OF_YEAR) ||
						(Options.last_sleep_time_change.get(Calendar.DAY_OF_YEAR)==c.get(Calendar.DAY_OF_YEAR) && Options.last_sleep_time_change.get(Calendar.YEAR)!=c.get(Calendar.YEAR))){
					status.sleep_time.set(Calendar.HOUR_OF_DAY,Options.desired_sleep_time.get(Calendar.HOUR_OF_DAY));
					status.sleep_time.set(Calendar.MINUTE,Options.desired_sleep_time.get(Calendar.MINUTE));
					
					Options.last_sleep_time_change=Calendar.getInstance();
				}
				
				if(!status.sleeping){
					if(status.should_be_sleeping()){
						status.sleeping=true;
						
						status.wake_time=status.get_wake_time();
					}
				}
				else{
					if(!status.should_be_sleeping()){
						status.sleeping=false;
						
						status.sleeping_woken_up=false;
						status.sleeping_woken_up_time=-1L;
						
						if(RNG.random_range(0,99)<50){
							status.happy+=(short)RNG.random_range((int)(Math.ceil((double)Pet_Status.HAPPY_MAX*0.10)),(int)(Math.ceil((double)Pet_Status.HAPPY_MAX*0.20)));
						}
						else{
							status.happy-=(short)RNG.random_range((int)(Math.ceil((double)Pet_Status.HAPPY_MAX*0.10)),(int)(Math.ceil((double)Pet_Status.HAPPY_MAX*0.20)));
						}
						
						status.happy_bound();
					}
				}
				
				// Sick //
				
				if(!status.sick){
					if(status.needs_poop_cleaned() || status.needs_bath() || status.is_starving() || status.is_very_thirsty() ||
							status.temp_is_very_bad()){
						if(RNG.random_range(0,999)<status.get_sick_chance()){
							status.sick=true;
						}
					}
				}
				
				// Poop //
				
				status.poop+=status.get_poop_rate();
				if(status.poop>=Pet_Status.POOP_MAX){
					status.poop=0;
					
					//Don't allow more than 10 poops for performance reasons.
					if(status.poops.size()<10){
						//Only allow the poop sound on the first poop.
						if(poops_created==0){
							Sound_Manager.play_sound(Sound.POOP);
						}
						
						poops_created++;
						
						float poop_x=x+w/2;
						float poop_y=y+h/2;
						
						if(x==-1000.0f && y==-1000.0f){
							if(image!=null && view!=null){
								poop_x=RNG.random_range(0,view.getWidth()-image.poop.get_width());
								poop_y=RNG.random_range(0,view.getHeight()-image.poop.get_height());
							}
						}
						
						status.poops.add(new Poop(image,view,poop_x,poop_y));
					}
				}
				
				// Bath Need //
				
				if(status.bath<Pet_Status.BATH_MAX){
					status.bath+=status.get_dirty_rate();
					if(status.bath>Pet_Status.BATH_MAX){
						status.bath=Pet_Status.BATH_MAX;
					}
				}
				
				// Happiness //
				
				//Decrease happiness based on various things.
				
				if(status.sick){
					if(RNG.random_range(0,99)<25){
						status.happy-=Pet_Status.HAPPY_LOSS_SICK;
						status.happy_bound();
					}
				}
				for(int n=0;n<status.poops.size();n++){
					if(RNG.random_range(0,99)<25){
						status.happy-=Pet_Status.HAPPY_LOSS_POOP;
						status.happy_bound();
					}
				}
				if(status.needs_bath()){
					if(RNG.random_range(0,99)<25){
						status.happy-=Pet_Status.HAPPY_LOSS_DIRTY;
						status.happy_bound();
					}
				}
				if(status.is_starving()){
					if(RNG.random_range(0,99)<25){
						status.happy-=Pet_Status.HAPPY_LOSS_HUNGER;
						status.happy_bound();
					}
				}
				if(status.is_very_thirsty()){
					if(RNG.random_range(0,99)<25){
						status.happy-=Pet_Status.HAPPY_LOSS_THIRST;
						status.happy_bound();
					}
				}
				if(status.temp_is_bad()){
					if(RNG.random_range(0,99)<25){
						status.happy-=Pet_Status.HAPPY_LOSS_TEMP;
						status.happy_bound();
					}
				}
				if(status.temp_is_very_bad()){
					if(RNG.random_range(0,99)<50){
						status.happy-=Pet_Status.HAPPY_LOSS_TEMP;
						status.happy_bound();
					}
				}
				
				//Increase happiness based on various things.
				
				if(status.temp_is_good()){
					if(RNG.random_range(0,99)<25){
						status.happy+=Pet_Status.HAPPY_GAIN_TEMP;
						status.happy_bound();
					}
				}
				if(status.temp_is_optimum()){
					if(RNG.random_range(0,99)<50){
						status.happy+=Pet_Status.HAPPY_GAIN_TEMP;
						status.happy_bound();
					}
				}
				for(int n=Equipment.SLOT_BEGIN;n<Equipment.SLOT_END;n++){
					if(status.equipment_slots.get(n)!=null){
						status.happy+=status.equipment_slots.get(n).buff_happy;
						status.happy_bound();
					}
				}
				if(status.buff_happy>0){
					status.happy+=Pet_Status.FOOD_BUFF_HAPPY;
					status.happy_bound();
				}
				if(status.has_perma_item("duckie")){
					status.happy+=Pet_Status.ITEM_BUFF_DUCKIE;
					status.happy_bound();
				}
				if(status.has_perma_item("key")){
					status.happy+=Pet_Status.ITEM_BUFF_KEY;
					status.happy_bound();
				}
				
				// Death Counter //
				
				//Decrease the counter the standard amount.
				if(status.death_counter>0){
					status.death_counter--;
					status.death_counter_bound();
				}
				
				//Decrease the counter based on various things.
				
				if(status.is_starving() && status.dc_starving>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_SMALL){
						status.death_counter--;
						status.death_counter_bound();
						status.dc_starving--;
					}
				}
				if(status.is_very_thirsty() && status.dc_very_thirsty>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_SMALL){
						status.death_counter--;
						status.death_counter_bound();
						status.dc_very_thirsty--;
					}
				}
				if(status.sick && status.dc_sick>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_SMALL){
						status.death_counter--;
						status.death_counter_bound();
						status.dc_sick--;
					}
				}
				if(status.is_obese() && status.dc_obese>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_SMALL){
						status.death_counter--;
						status.death_counter_bound();
						status.dc_obese--;
					}
				}
				
				//Increase the counter based on various things.
				
				if(!status.is_obese() && status.dc_not_obese>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_TINY){
						status.death_counter++;
						status.death_counter_bound();
						status.dc_not_obese--;
					}
				}
				if(status.is_well_fed() && status.dc_well_fed>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_SMALL){
						status.death_counter++;
						status.death_counter_bound();
						status.dc_well_fed--;
					}
				}
				if(status.is_well_watered() && status.dc_well_watered>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_SMALL){
						status.death_counter++;
						status.death_counter_bound();
						status.dc_well_watered--;
					}
				}
				if(!status.sick && status.dc_not_sick>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_SMALL){
						status.death_counter++;
						status.death_counter_bound();
						status.dc_not_sick--;
					}
				}
				for(int n=Equipment.SLOT_BEGIN;n<Equipment.SLOT_END;n++){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_TINY){
						if(status.equipment_slots.get(n)!=null){
							status.death_counter+=status.equipment_slots.get(n).buff_death;
							status.death_counter_bound();
						}
					}
				}
				if(status.buff_death>0){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_TINY){
						status.death_counter+=Pet_Status.FOOD_BUFF_DEATH;
						status.death_counter_bound();
					}
				}
				if(status.has_perma_item("bunny")){
					if(RNG.random_range(0,99)<Pet_Status.CHANCE_TINY){
						status.death_counter+=Pet_Status.ITEM_BUFF_BUNNY;
						status.death_counter_bound();
					}
				}
				
				//Handle death.
				if(status.death_counter<=0 && RNG.random_range(0,99)<5){
					die(image,view,context,records,handler);
					
					break;
				}
			}
		}
	}
	
	public void add_thought(Image image,View view,Vibrator vibrator,int get_type){
		if(status.age_tier!=Age_Tier.EGG && status.age_tier!=Age_Tier.DEAD && (!status.sleeping || (status.sleeping && status.sleeping_woken_up))){
			boolean thought_already_present=false;
			
			for(int i=0;i<thoughts.size();i++){
				if(thoughts.get(i).type==get_type){
					thought_already_present=true;
					break;
				}
			}
			
			if(!thought_already_present){
				thoughts.add(new Thought(image,view,this,get_type,THOUGHT_LENGTH*FPS.fps));
				
				//If the thought just added is the only one.
				if(thoughts.size()==1){
					Sound_Manager.play_sound(Thought_Type.get_sound_id(get_type));
					
					thought_vibration(vibrator);
				}
			}
		}
	}
	
	public void add_overlay(Image image,View view,Vibrator vibrator,int get_type,float get_x,float get_y,int get_move_state){
		if(status.age_tier!=Age_Tier.EGG){
			overlays.add(new Overlay(image,view,get_x,get_y,get_move_state,get_type));
			
			//If the overlay just added is the only one.
			if(overlays.size()==1){
				Sound_Manager.play_sound(Overlay_Type.get_sound_id(get_type));
				
				overlay_vibration(vibrator);
			}
		}
	}
	
	public Coords get_midpoint(){
		Coords coords=new Coords();
		
		coords.x=x+w/2;
		coords.y=y+h/2;
		
		return coords;
	}
	
	public RectF get_rect(){
		RectF rect=new RectF(x,y,x+w,y+h);
		
		return rect;
	}
	
	public void start_pet_action(float get_x,float get_y){
		pet_start_x=get_x;
		pet_start_y=get_y;
	}
	
	public Coords get_pet_start_action(){
		Coords coords=new Coords();
		
		coords.x=pet_start_x;
		coords.y=pet_start_y;
		
		return coords;
	}
	
	public void receive_petting(){
		if(status.pettings<Pet_Status.PETTINGS_ALLOWED_PER_TICK){
			status.pettings++;
			
			status.happy+=Pet_Status.HAPPY_GAIN_PETTING;
			status.happy_bound();
		}
		
		//Regardless of whether this petting increased happiness, create a happy thought.
		status.queue_thought(Thought_Type.HAPPY);
		
		status.sleeping_wake_up();
	}
	
	public void thought_vibration(Vibrator vibrator){
		if(Options.vibrate){
			vibrator.cancel();
			vibrator.vibrate(THOUGHT_VIBRATE_TIME);
		}
	}
	
	public void overlay_vibration(Vibrator vibrator){
		if(Options.vibrate){
			vibrator.cancel();
			vibrator.vibrate(OVERLAY_VIBRATE_TIME);
		}
	}
	
	public void clear_need_feedback_timers(){
		counter_feedback_cold=0;
		counter_feedback_dirty=0;
		counter_feedback_hot=0;
		counter_feedback_hungry=0;
		counter_feedback_sick=0;
		counter_feedback_thirsty=0;
		counter_feedback_poop=0;
		counter_feedback_unhappy=0;
	}
	
	public void ai(Image image,View view,Vibrator vibrator){
		if(evolutions.size()==0){
			//Decrement feedback timers.
			
			if(counter_feedback_cold>0){
				counter_feedback_cold--;
			}
			if(counter_feedback_dirty>0){
				counter_feedback_dirty--;
			}
			if(counter_feedback_hot>0){
				counter_feedback_hot--;
			}
			if(counter_feedback_hungry>0){
				counter_feedback_hungry--;
			}
			if(counter_feedback_sick>0){
				counter_feedback_sick--;
			}
			if(counter_feedback_thirsty>0){
				counter_feedback_thirsty--;
			}
			if(counter_feedback_unhappy>0){
				counter_feedback_unhappy--;
			}
			if(counter_feedback_poop>0){
				counter_feedback_poop--;
			}
			if(counter_feedback_music>0){
				counter_feedback_music--;
			}
			
			//Provide feedback.
			
			if(counter_feedback_sick==0 && status.sick){
				counter_feedback_sick=get_feedback_time_long();
				
				status.queue_thought(Thought_Type.SICK);
			}
			if(counter_feedback_dirty==0 && status.needs_bath()){
				counter_feedback_dirty=get_feedback_time_long();
				
				status.queue_thought(Thought_Type.DIRTY);
			}
			if(counter_feedback_hungry==0 && status.is_starving()){
				counter_feedback_hungry=get_feedback_time_long();
				
				status.queue_thought(Thought_Type.HUNGRY);
			}
			if(counter_feedback_thirsty==0 && status.is_very_thirsty()){
				counter_feedback_thirsty=get_feedback_time_long();
				
				status.queue_thought(Thought_Type.THIRSTY);
			}
			if(counter_feedback_cold==0 && status.temp_is_cold()){
				counter_feedback_cold=get_feedback_time_long();
				
				status.queue_thought(Thought_Type.COLD);
			}
			if(counter_feedback_hot==0 && status.temp_is_hot()){
				counter_feedback_hot=get_feedback_time_long();
				
				status.queue_thought(Thought_Type.HOT);
			}
			if(counter_feedback_unhappy==0 && !status.is_happy()){
				counter_feedback_unhappy=get_feedback_time_long();
				
				status.queue_thought(Thought_Type.SAD);
			}
			if(counter_feedback_poop==0 && status.needs_poop_cleaned()){
				counter_feedback_poop=get_feedback_time_long();
				
				status.queue_thought(Thought_Type.POOP);
			}
			if(counter_feedback_music==0){
				counter_feedback_music=get_feedback_time_fun();
				
				status.queue_thought(Thought_Type.MUSIC);
			}
			
			//Process thoughts queue.
			for(int i=0;i<status.thoughts.size();i++){
				add_thought(image,view,vibrator,status.thoughts.get(i));
				
				status.thoughts.remove(i);
				i--;
			}
			
			//Handle movement.
			if(--wander_counter<=0){
				wander_counter=RNG.random_range(wander_frequency_min,wander_frequency_max);
				
				move_speed=RNG.random_range(SPEED_MIN,SPEED_MAX);
				
				int random=RNG.random_range(0,99);
				if(random>=0 && random<10){
					move_state=Direction.NONE;
				}
				else if(random>=10 && random<30){
					move_state=RNG.random_range(Direction.LEFT,Direction.RIGHT);
				}
				else if(random>=30 && random<100){
					move_state=RNG.random_range(Direction.LEFT_UP,Direction.LEFT_DOWN);
				}
			}
		}
	}
	
	public void move(View view,Vibrator vibrator){
		//If the pet is in its initial position.
		if((x==-1000 || y==-1000) && view.getWidth()!=0 && view.getHeight()!=0){
			x=(view.getWidth()-w)/2;
			y=(view.getHeight()-h)/2;
		}
		
		int virtual_move_state=move_state;
		
		//Move towards a held item.
		Perma_Item held=status.get_held_item();
		if(held!=null){
			float r_x=x+w/8.0f;
			float r_y=y+h/8.0f;
			float r_w=w/4.0f;
			float r_h=h/4.0f;
			
			float r_held_x=held.x+held.w/8.0f;
			float r_held_y=held.y+held.h/8.0f;
			float r_held_w=held.w/4.0f;
			float r_held_h=held.h/4.0f;
			
			if(!Collision.check(r_x,r_y,r_w,r_h,r_held_x,r_held_y,r_held_w,r_held_h)){
				if(r_x<r_held_x && r_y<r_held_y){
					virtual_move_state=Direction.RIGHT_DOWN;
		        }
		        else if(r_x>r_held_x+r_held_w && r_y<r_held_y){
		        	virtual_move_state=Direction.LEFT_DOWN;
		        }
		        else if(r_x>r_held_x+r_held_w && r_y>r_held_y+r_held_h){
		        	virtual_move_state=Direction.LEFT_UP;
		        }
		        else if(r_x<r_held_x && r_y>r_held_y+r_held_h){
		        	virtual_move_state=Direction.RIGHT_UP;
		        }
		        else if(r_x>r_held_x+r_held_w){
		        	virtual_move_state=Direction.LEFT;
		        }
		        else if(r_x<r_held_x){
		        	virtual_move_state=Direction.RIGHT;
		        }
		        else if(r_y>r_held_y+r_held_h){
		        	virtual_move_state=Direction.UP;
		        }
		        else if(r_y<r_held_y){
		        	virtual_move_state=Direction.DOWN;
		        }
			}
			else{
				move_state=virtual_move_state=Direction.NONE;
				
				wander_counter=RNG.random_range(wander_frequency_min,wander_frequency_max);
			}
		}
		
		if(evolutions.size()==0){
			if(status.age_tier!=Age_Tier.EGG && status.age_tier!=Age_Tier.DEAD && (!status.sleeping || (status.sleeping && status.sleeping_woken_up))){
				if(virtual_move_state==Direction.LEFT || virtual_move_state==Direction.LEFT_UP || virtual_move_state==Direction.LEFT_DOWN){
					x-=move_speed;
					facing=Direction.LEFT;
				}
				else if(virtual_move_state==Direction.RIGHT || virtual_move_state==Direction.RIGHT_UP || virtual_move_state==Direction.RIGHT_DOWN){
					x+=move_speed;
					facing=Direction.RIGHT;
				}
				if(virtual_move_state==Direction.UP || virtual_move_state==Direction.LEFT_UP || virtual_move_state==Direction.RIGHT_UP){
					y-=move_speed;
				}
				if(virtual_move_state==Direction.DOWN || virtual_move_state==Direction.LEFT_DOWN || virtual_move_state==Direction.RIGHT_DOWN){
					y+=move_speed;
				}
			}
		}
		
		if(x!=-1000 && y!=-1000){
			//Keep the pet in the screen bounds.
			if(x<0){
				x=0;
				wander_counter=0;
			}
			if(x+w>view.getWidth()){
				x=view.getWidth()-w;
				wander_counter=0;
			}
			if(y<0){
				y=0;
				wander_counter=0;
			}
			if(y+h>view.getHeight()){
				y=view.getHeight()-h;
				wander_counter=0;
			}
		}
		
		//Move poops.
		for(int i=0;i<status.poops.size();i++){
			status.poops.get(i).move(view);
		}
		
		//Move perma_items.
		for(int i=0;i<status.perma_items.size();i++){
			status.perma_items.get(i).move(view);
		}
		
		//Move thoughts.
		if(thoughts.size()>0){
			boolean keep=thoughts.get(0).move(view,this);
			
			//If this thought is finished.
			if(!keep){
				//Erase this thought.
				thoughts.remove(0);
				
				//If there is still another thought.
				if(thoughts.size()>0){
					Sound_Manager.play_sound(Thought_Type.get_sound_id(thoughts.get(0).type));
					
					thought_vibration(vibrator);
				}
			}
		}
		
		//Move overlays.
		if(overlays.size()>0){
			overlays.get(0).move(view,this);
			
			//If this overlay is finished.
			if(overlays.get(0).move_speed==0){
				//Erase this overlay.
				overlays.remove(0);
				
				//If there is still another overlay.
				if(overlays.size()>0){
					Sound_Manager.play_sound(Overlay_Type.get_sound_id(overlays.get(0).type));
					
					overlay_vibration(vibrator);
				}
			}
		}
	}
	
	public void animate(Vibrator vibrator,Image image,View view){
		if(sprite!=null){
			//There is a special final frame that is ignored when not sleeping.
			int frame_ignore=sprite.frame_count-1;
			int frame_override=-1;
			if(status.sleeping && !status.sleeping_woken_up){
				frame_ignore=-1;
				frame_override=sprite.frame_count-1;
			}
			if(status.age_tier==Age_Tier.EGG || status.age_tier==Age_Tier.DEAD){
				frame_ignore=-1;
				frame_override=-1;
			}
			
			sprite.animate(frame_ignore,frame_override,-1,-1);
		}
		
		if(sprite_sleep!=null){
			sprite_sleep.animate(-1,-1,-1,-1);
		}
		
		if(sprite_ac!=null){
			sprite_ac.animate(-1,-1,-1,-1);
		}
		
		if(sprite_heater!=null){
			sprite_heater.animate(-1,-1,-1,-1);
		}
		
		if(sprite_dirty!=null){
			sprite_dirty.animate(-1,-1,-1,-1);
		}
		
		//Animate evolution.
		if(evolutions.size()>0){
			boolean keep=evolutions.get(0).animate(vibrator,image,view,this);
			
			if(!keep){
				evolutions.remove(0);
			}
		}
		
		//Animate perma items.
		for(int i=0;i<status.perma_items.size();i++){
			status.perma_items.get(i).animate();
		}
		
		//Animate the lit part of perma items.
		if(status.has_perma_item("computer")){
			if(sprite_lit_computer!=null){
				sprite_lit_computer.animate(-1,-1,-1,-1);
			}
		}
		if(status.has_perma_item("robot")){
			if(sprite_lit_robot!=null){
				sprite_lit_robot.animate(-1,-1,-1,-1);
			}
		}
		if(status.has_perma_item("holiday_christmas")){
			if(sprite_lit_christmas_tree!=null){
				sprite_lit_christmas_tree.animate(-1,-1,-1,-1);
			}
		}
	}
	
	public void render_before_dark(Canvas canvas,Resources res){
		//Render poops.
		for(int i=0;i<status.poops.size();i++){
			status.poops.get(i).render(canvas,res);
		}
		
		if(status.needs_bath()){
			if(sprite_dirty!=null){
				sprite_dirty.draw(canvas,res,(int)(x+w/2-sprite_dirty.get_width()/2),(int)(y-sprite_dirty.get_height()*0.65f),sprite_dirty.get_width(),sprite_dirty.get_height(),Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
			}
		}
		
		if(evolutions.size()==0 || (evolutions.size()>0 && !evolutions.get(0).render_type)){
			if(sprite!=null){
				sprite.draw(canvas,res,(int)x,(int)y,w,h,facing,status.color,false,1.0f,1.0f);
			}
		}
		else if(evolutions.size()>0 && evolutions.get(0).render_type){
			evolutions.get(0).render_pet_type(canvas,res,status,x,y);
		}
		
		//Render evolution.
		if(evolutions.size()>0){
			evolutions.get(0).render(canvas,res,status,x,y,w,h);
		}
		
		//Render perma_items.
		for(int i=0;i<status.perma_items.size();i++){
			status.perma_items.get(i).render(canvas,res);
		}
	}
	
	public void render_after_dark(Canvas canvas,Resources res){
		Paint paint=new Paint();
		RectF rect=null;
		DecimalFormat df=new DecimalFormat("#.##");
		float font_size_temp=Px.px(res,22.0f);
		float font_size_stat_bar=Px.px(res,18.0f);
		int opacity_stat_bar=191;
		int opacity_temp=191;
		
		//Render the lit part of perma items.
		if(status.has_perma_item("computer")){
			if(sprite_lit_computer!=null){
				Perma_Item item=status.get_item("computer");
				if(item!=null){
					sprite_lit_computer.draw(canvas,res,(int)item.x,(int)item.y,(int)item.w,(int)item.h,Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
				}
			}
		}
		if(status.has_perma_item("robot")){
			if(sprite_lit_robot!=null){
				Perma_Item item=status.get_item("robot");
				if(item!=null){
					sprite_lit_robot.draw(canvas,res,(int)item.x,(int)item.y,(int)item.w,(int)item.h,Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
				}
			}
		}
		if(status.has_perma_item("holiday_christmas")){
			if(sprite_lit_christmas_tree!=null){
				Perma_Item item=status.get_item("holiday_christmas");
				if(item!=null){
					sprite_lit_christmas_tree.draw(canvas,res,(int)item.x,(int)item.y,(int)item.w,(int)item.h,Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
				}
			}
		}
		
		if(status.sleeping && !status.sleeping_woken_up){
			if(status.age_tier!=Age_Tier.EGG && status.age_tier!=Age_Tier.DEAD){
				if(sprite_sleep!=null){
					sprite_sleep.draw(canvas,res,(int)(x+w),(int)(y-sprite_sleep.get_height()),sprite_sleep.get_width(),sprite_sleep.get_height(),Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
				}
			}
		}
		else{
			if(status.age_tier!=Age_Tier.EGG && status.age_tier!=Age_Tier.DEAD && (!status.sleeping || (status.sleeping && status.sleeping_woken_up))){
				//Render thoughts.
				if(thoughts.size()>0){
					thoughts.get(0).render(canvas,res,status);
				}
			}
		}
		
		//Render overlays.
		if(overlays.size()>0){
			overlays.get(0).render(canvas,res);
		}
		
		//Draw stat bars.
		
		if(Options.show_stat_bars){
			for(int i=0;i<4;i++){
				float BAR_MAX=0.0f;
				float bar=0.0f;
				int bar_color=0;
				int text_color=R.color.font;
				int bar_back_color=R.color.status_bar_background;
				int bar_border_color=R.color.status_bar_border;
				String stat_message="";
				paint.setTextSize(font_size_stat_bar);
				paint.setTypeface(Font.font1);
				float i_adjust=(float)i*(Px.px(res,STAT_BAR_PADDING)*2.0f+Px.px(res,STAT_BAR_HEIGHT));
				
				if(i==0){
					BAR_MAX=(float)status.get_strength_max();
					bar=(float)status.get_strength();
					bar_color=R.color.status_strength;
					stat_message=status.get_strength()+"/"+status.get_strength_max();
					if(status.get_strength_upgrade()>0){
						text_color=R.color.font_upgrade;
						
						stat_message+=" [+"+status.get_strength_upgrade()+"]";
					}
				}
				else if(i==1){
					BAR_MAX=(float)status.get_dexterity_max();
					bar=(float)status.get_dexterity();
					bar_color=R.color.status_dexterity;
					stat_message=status.get_dexterity()+"/"+status.get_dexterity_max();
					if(status.get_dexterity_upgrade()>0){
						text_color=R.color.font_upgrade;
						
						stat_message+=" [+"+status.get_dexterity_upgrade()+"]";
					}
				}
				else if(i==2){
					BAR_MAX=(float)status.get_stamina_max();
					bar=(float)status.get_stamina();
					bar_color=R.color.status_stamina;
					stat_message=status.get_stamina()+"/"+status.get_stamina_max();
					if(status.get_stamina_upgrade()>0){
						text_color=R.color.font_upgrade;
						
						stat_message+=" [+"+status.get_stamina_upgrade()+"]";
					}
				}
				else if(i==3){
					BAR_MAX=(float)status.get_energy_max();
					bar=(float)status.get_energy();
					bar_color=R.color.status_energy;
					stat_message=status.get_energy()+"/"+status.get_energy_max();
					if(status.get_energy_upgrade()>0){
						text_color=R.color.font_upgrade;
						
						stat_message+=" [+"+status.get_energy_upgrade()+"]";
					}
				}
				
				float percentage=bar/BAR_MAX;
				float bar_width=STAT_BAR_WIDTH*percentage;
				
				paint.setColor(res.getColor(bar_back_color));
				paint.setAlpha(opacity_stat_bar);
				rect=new RectF(Px.px(res,STAT_BAR_PADDING),i_adjust+Px.px(res,STAT_BAR_PADDING),Px.px(res,STAT_BAR_PADDING)+Px.px(res,STAT_BAR_WIDTH),i_adjust+Px.px(res,STAT_BAR_PADDING)+Px.px(res,STAT_BAR_HEIGHT));
				canvas.drawRoundRect(rect,12.0f,12.0f,paint);
				
				paint.setColor(res.getColor(bar_color));
				paint.setAlpha(opacity_stat_bar);
				rect=new RectF(Px.px(res,STAT_BAR_PADDING),i_adjust+Px.px(res,STAT_BAR_PADDING),Px.px(res,STAT_BAR_PADDING)+Px.px(res,bar_width),i_adjust+Px.px(res,STAT_BAR_PADDING)+Px.px(res,STAT_BAR_HEIGHT));
				canvas.drawRoundRect(rect,12.0f,12.0f,paint);
				
				Paint border_paint=new Paint();
				border_paint.setColor(res.getColor(bar_border_color));
				border_paint.setStyle(Paint.Style.STROKE);
				border_paint.setStrokeWidth(2.0f);
				border_paint.setPathEffect(new DashPathEffect(new float[]{4,2},0));
				rect=new RectF(Px.px(res,STAT_BAR_PADDING),i_adjust+Px.px(res,STAT_BAR_PADDING),Px.px(res,STAT_BAR_PADDING)+Px.px(res,STAT_BAR_WIDTH),i_adjust+Px.px(res,STAT_BAR_PADDING)+Px.px(res,STAT_BAR_HEIGHT));
				canvas.drawRoundRect(rect,12.0f,12.0f,border_paint);
				
				paint.setColor(res.getColor(text_color));
				paint.setAlpha(opacity_stat_bar);
				canvas.drawText(stat_message,Px.px(res,STAT_BAR_PADDING)+(Px.px(res,STAT_BAR_WIDTH)-paint.measureText(stat_message))/2.0f,i_adjust+Px.px(res,STAT_BAR_PADDING)+font_size_stat_bar*0.85f,paint);
			}
		}
		
		//Draw a background behind the thermometer/machine.
		
		String temperature_message=UnitConverter.get_temperature_string(status.temp,df);
		paint.setTextSize(font_size_temp);
		paint.setTypeface(Font.font1);
		float machine_offset=0.0f;
		if(Options.show_thermometer){
			machine_offset=Px.px(res,THERMOMETER_WIDTH)+Px.px(res,THERMOMETER_PADDING)*2.0f;
		}
		
		float back_x=canvas.getWidth();
		float back_h=0.0f;
		
		if(Options.show_thermometer && (status.ac || status.heater)){
			back_x=canvas.getWidth()-sprite_ac.get_width()-machine_offset-Px.px(res,THERMOMETER_PADDING);
			back_h=Px.px(res,THERMOMETER_HEIGHT)+Px.px(res,THERMOMETER_PADDING)*2.0f+font_size_temp;
		}
		else if(Options.show_thermometer && !(status.ac || status.heater)){
			back_x=canvas.getWidth()-paint.measureText(temperature_message)-Px.px(res,THERMOMETER_PADDING);
			back_h=Px.px(res,THERMOMETER_HEIGHT)+Px.px(res,THERMOMETER_PADDING)*2.0f+font_size_temp;
		}
		else if(!Options.show_thermometer && (status.ac || status.heater)){
			back_x=canvas.getWidth()-sprite_ac.get_width()-Px.px(res,THERMOMETER_PADDING);
			back_h=sprite_ac.get_height()+Px.px(res,THERMOMETER_PADDING);
		}
		
		//Only render if there is something to put on the background.
		if(back_h>0.0f){
			paint.setColor(res.getColor(R.color.button_1));
			paint.setAlpha(127);
			rect=new RectF(back_x,0,canvas.getWidth(),back_h);
			canvas.drawRoundRect(rect,12.0f,12.0f,paint);
		}
		
		//Render thermometer.
		
		if(Options.show_thermometer){
			float TEMP_BAR_MAX=40.0f;
			float shrunk_temp=(float)status.temp;
			if(shrunk_temp>TEMP_BAR_MAX){
				shrunk_temp=TEMP_BAR_MAX;
			}
			
			float percentage=shrunk_temp/TEMP_BAR_MAX;
			float percentage_inverse=(100.0f-percentage*100.0f)/100.0f;
			float bar_height=THERMOMETER_HEIGHT*percentage;
			float bar_height_inverse=THERMOMETER_HEIGHT*percentage_inverse;
			
			paint.setColor(res.getColor(R.color.status_bar_background));
			paint.setAlpha(opacity_temp);
			rect=new RectF(canvas.getWidth()-Px.px(res,THERMOMETER_WIDTH)-Px.px(res,THERMOMETER_PADDING),Px.px(res,THERMOMETER_PADDING),canvas.getWidth()-Px.px(res,THERMOMETER_PADDING),Px.px(res,THERMOMETER_HEIGHT)+Px.px(res,THERMOMETER_PADDING));
			canvas.drawRoundRect(rect,12.0f,12.0f,paint);
			
			paint.setColor(res.getColor(R.color.status_temp));
			paint.setAlpha(opacity_temp);
			rect=new RectF(canvas.getWidth()-Px.px(res,THERMOMETER_WIDTH)-Px.px(res,THERMOMETER_PADDING),Px.px(res,bar_height_inverse)+Px.px(res,THERMOMETER_PADDING),canvas.getWidth()-Px.px(res,THERMOMETER_PADDING),Px.px(res,bar_height_inverse)+Px.px(res,bar_height)+Px.px(res,THERMOMETER_PADDING));
			canvas.drawRoundRect(rect,12.0f,12.0f,paint);
			
			Paint border_paint=new Paint();
			border_paint.setColor(res.getColor(R.color.status_bar_border));
			border_paint.setStyle(Paint.Style.STROKE);
			border_paint.setStrokeWidth(2.0f);
			border_paint.setPathEffect(new DashPathEffect(new float[]{4,2},0));
			rect=new RectF(canvas.getWidth()-Px.px(res,THERMOMETER_WIDTH)-Px.px(res,THERMOMETER_PADDING),Px.px(res,THERMOMETER_PADDING),canvas.getWidth()-Px.px(res,THERMOMETER_PADDING),Px.px(res,THERMOMETER_HEIGHT)+Px.px(res,THERMOMETER_PADDING));
			canvas.drawRoundRect(rect,12.0f,12.0f,border_paint);
			
			paint.setColor(res.getColor(R.color.font));
			paint.setAlpha(opacity_temp);
			canvas.drawText(temperature_message,canvas.getWidth()-paint.measureText(temperature_message),Px.px(res,THERMOMETER_HEIGHT)+Px.px(res,THERMOMETER_PADDING)+font_size_temp,paint);
		}
		
		//Render temperature machine.
		
		if(status.ac){
			if(sprite_ac!=null){
				sprite_ac.draw(canvas,res,canvas.getWidth()-sprite_ac.get_width()-(int)machine_offset,0,sprite_ac.get_width(),sprite_ac.get_height(),Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
			}
		}
		else if(status.heater){
			if(sprite_heater!=null){
				sprite_heater.draw(canvas,res,canvas.getWidth()-sprite_heater.get_width()-(int)machine_offset,0,sprite_heater.get_width(),sprite_heater.get_height(),Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
			}
		}
	}
}
