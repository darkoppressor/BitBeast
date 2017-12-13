/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Image{
	int screen_w;
    int screen_h;
    
    //The pet sprites are a special case.
    //Only the needed pet sprites are kept in memory, to reduce memory load.
    Sprite_Data pet;
    Sprite_Data pet_evolution;
    Pet_Type pet_type;
    Pet_Type pet_evolution_type;
	
	Sprite_Data sleep;
	Sprite_Data evolution;
	Sprite_Data dirty;
	
	Sprite_Data bubble_thought;
	Sprite_Data thought_happy;
	Sprite_Data thought_cold;
	Sprite_Data thought_dirty;
	Sprite_Data thought_hot;
	Sprite_Data thought_hungry;
	Sprite_Data thought_sad;
	Sprite_Data thought_sick;
	Sprite_Data thought_thirsty;
	Sprite_Data thought_music;
	Sprite_Data thought_poop;
	
	Sprite_Data poop;
	
	Sprite_Data perma_item_duckie;
	Sprite_Data perma_item_bunny;
	Sprite_Data perma_item_beach_ball;
	Sprite_Data perma_item_robot;
	Sprite_Data perma_item_computer;
	Sprite_Data perma_item_at;
	Sprite_Data perma_item_berry_bush;
	Sprite_Data perma_item_key;
	Sprite_Data perma_item_leaf;
	Sprite_Data perma_item_sign;
	Sprite_Data perma_item_stone;
	Sprite_Data perma_item_trampoline;
	Sprite_Data perma_item_tree;
	Sprite_Data perma_item_wheat;
	Sprite_Data perma_item_towel;
	Sprite_Data perma_item_barbell;
	Sprite_Data perma_item_chest;
	Sprite_Data perma_item_jump_rope;
	Sprite_Data perma_item_piano;
	Sprite_Data perma_item_shoes;
	Sprite_Data perma_item_holiday_valentines;
	Sprite_Data perma_item_holiday_st_patricks;
	Sprite_Data perma_item_holiday_easter;
	Sprite_Data perma_item_holiday_halloween;
	Sprite_Data perma_item_holiday_christmas;
	
	Sprite_Data lit_computer;
	Sprite_Data lit_robot;
	Sprite_Data lit_christmas_tree;
	
	Sprite_Data ac;
	Sprite_Data heater;
	
	Sprite_Data overlay_clean_yard;
	Sprite_Data overlay_clean_pet;
	
	Sprite_Data object_ball;
	Sprite_Data object_paddle;
	Sprite_Data object_brick;
	Sprite_Data object_powerup_spawner_on;
	Sprite_Data object_powerup_spawner_off;
	Sprite_Data object_powerup_ball_speed_up;
	Sprite_Data object_powerup_ball_speed_down;
	Sprite_Data object_powerup_ball_extra;
	Sprite_Data object_powerup_ball_noclip;
	Sprite_Data object_powerup_life;
	
	Bitmap background_blueprint;
	
	public Image(Context context){
		Resources res=context.getResources();
		
		screen_w=0;
    	screen_h=0;
    	
    	pet=null;
    	pet_evolution=null;
    	pet_type=null;
    	pet_evolution_type=null;
		
		sleep=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.sleep),6,5,false);
		evolution=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.evolution),2,6,false);
		dirty=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.dirty),7,2,true);
		
		bubble_thought=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.bubble_thought),0,1,false);
		thought_happy=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_happy),0,1,false);
		thought_cold=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_cold),0,1,false);
		thought_dirty=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_dirty),0,1,false);
		thought_hot=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_hot),0,1,false);
		thought_hungry=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_hungry),0,1,false);
		thought_sad=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_sad),0,1,false);
		thought_sick=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_sick),0,1,false);
		thought_thirsty=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_thirsty),0,1,false);
		thought_music=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_music),0,1,false);
		thought_poop=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.thought_poop),0,1,false);
		
		poop=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.poop),0,1,false);
		
		perma_item_duckie=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_duckie),0,1,false);
		perma_item_bunny=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_bunny),0,1,false);
		perma_item_beach_ball=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_beach_ball),0,1,false);
		perma_item_robot=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_robot),0,1,false);
		perma_item_computer=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_computer),0,1,false);
		perma_item_at=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_at),0,1,false);
		perma_item_berry_bush=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_berry_bush),0,1,false);
		perma_item_key=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_key),0,1,false);
		perma_item_leaf=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_leaf),0,1,false);
		perma_item_sign=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_sign),0,1,false);
		perma_item_stone=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_stone),0,1,false);
		perma_item_trampoline=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_trampoline),0,1,false);
		perma_item_tree=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_tree),0,1,false);
		perma_item_wheat=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_wheat),0,1,false);
		perma_item_towel=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_towel),0,1,false);
		perma_item_barbell=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_barbell),0,1,false);
		perma_item_chest=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_chest),0,1,false);
		perma_item_jump_rope=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_jump_rope),0,1,false);
		perma_item_piano=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_piano),0,1,false);
		perma_item_shoes=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_shoes),0,1,false);
		perma_item_holiday_valentines=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_holiday_valentines),0,1,false);
		perma_item_holiday_st_patricks=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_holiday_st_patricks),0,1,false);
		perma_item_holiday_easter=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_holiday_easter),0,1,false);
		perma_item_holiday_halloween=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_holiday_halloween),0,1,false);
		perma_item_holiday_christmas=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.food_icon_holiday_christmas),8,3,false);
		
		lit_computer=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.lit_computer),0,1,false);
		lit_robot=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.lit_robot),0,1,false);
		lit_christmas_tree=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.lit_christmas_tree),8,3,false);
		
		ac=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.ac),7,2,true);
		heater=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.heater),7,2,true);
		
		object_ball=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_ball),0,1,false);
		object_paddle=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_paddle),0,1,false);
		object_brick=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_brick),0,1,false);
		object_powerup_spawner_on=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_powerup_spawner_on),12,2,false);
		object_powerup_spawner_off=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_powerup_spawner_off),16,2,false);
		object_powerup_ball_speed_up=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_powerup_ball_speed_up),1,4,false);
		object_powerup_ball_speed_down=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_powerup_ball_speed_down),3,4,false);
		object_powerup_ball_extra=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_powerup_ball_extra),2,4,false);
		object_powerup_ball_noclip=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_powerup_ball_noclip),2,4,false);
		object_powerup_life=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.object_powerup_life),2,4,false);
		
		overlay_clean_yard=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.overlay_clean_yard),0,1,false);
		overlay_clean_pet=new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.overlay_clean_pet),0,1,false);
		
		background_blueprint=BitmapFactory.decodeResource(res,R.drawable.background_blueprint);
	}
	
	public void load_pet(Resources res,Pet_Type type){
		if(pet_type==null || pet_type!=type){
			pet_type=type;
			
			pet=get_sprite_data(res,type);
		}
	}
	
	public void load_pet_evolution(Resources res,Pet_Type type){
		if(pet_evolution_type==null || pet_evolution_type!=type){
			pet_evolution_type=type;
			
			pet_evolution=get_sprite_data(res,type);
		}
	}
	
	public Sprite_Data get_sprite_data(Resources res,Pet_Type type){
		switch(type){
		case YEETSOBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_egg_1),6,4,true);
		case MATKABIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_baby_1),10,3,true);
		case SHARBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_child_1),10,3,true);
			
		case GOLOBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_teen_1),10,3,true);
		case VROONBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_teen_2),10,3,true);
		case LYTSOBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_teen_3),10,3,true);
		case POKRIBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_teen_4),10,3,true);
		case VOSDUBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_teen_5),10,3,true);
			
		case NAZBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_1),10,3,true);
		case MOSHENBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_2),10,3,true);
		case ROGBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_3),10,3,true);
		case KRISHABIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_4),10,3,true);
		case LETABIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_5),10,3,true);
			
		case KOROLBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_adult_1),10,3,true);
		case OBMABIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_adult_2),10,3,true);
		case TRYBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_adult_3),10,3,true);
		case PLYTABIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_adult_4),10,3,true);
		case KRILABIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_adult_5),10,3,true);
			
		case TYRANBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_senior_1),10,3,true);
		case PATOBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_senior_2),10,3,true);
		case SERABIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_senior_3),10,3,true);
		case DOSPEKYBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_senior_4),10,3,true);
		case UJASBIT:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_senior_5),10,3,true);
			
		case DEAD:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_dead_1),0,1,false);
			
		default:
			return new Sprite_Data(BitmapFactory.decodeResource(res,R.drawable.pet_egg_1),6,4,true);
		}
	}
}
