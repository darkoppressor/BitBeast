/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class Perma_Item{
	String name;
	
	private AnimatedSprite sprite;
	
	float x;
	float y;
	int w;
	int h;
	
	//Is the item currently held by the user on the touch screen?
	boolean held;
	
	public Perma_Item(Image image,View view,String get_name,float get_x,float get_y){
		sprite=null;
		
		name=get_name;
		
		x=get_x;
		y=get_y;
		w=0;
		h=0;
		
		held=false;
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
		}
	}
	
	public Sprite_Data get_sprite_data(Image image,String sprite_name){
		if(sprite_name.equals("duckie")){
			return image.perma_item_duckie;
		}
		else if(sprite_name.equals("bunny")){
			return image.perma_item_bunny;
		}
		else if(sprite_name.equals("beach_ball")){
			return image.perma_item_beach_ball;
		}
		else if(sprite_name.equals("robot")){
			return image.perma_item_robot;
		}
		else if(sprite_name.equals("computer")){
			return image.perma_item_computer;
		}
		else if(sprite_name.equals("at")){
			return image.perma_item_at;
		}
		else if(sprite_name.equals("berry_bush")){
			return image.perma_item_berry_bush;
		}
		else if(sprite_name.equals("key")){
			return image.perma_item_key;
		}
		else if(sprite_name.equals("leaf")){
			return image.perma_item_leaf;
		}
		else if(sprite_name.equals("sign")){
			return image.perma_item_sign;
		}
		else if(sprite_name.equals("stone")){
			return image.perma_item_stone;
		}
		else if(sprite_name.equals("trampoline")){
			return image.perma_item_trampoline;
		}
		else if(sprite_name.equals("tree")){
			return image.perma_item_tree;
		}
		else if(sprite_name.equals("wheat")){
			return image.perma_item_wheat;
		}
		else if(sprite_name.equals("towel")){
			return image.perma_item_towel;
		}
		else if(sprite_name.equals("barbell")){
			return image.perma_item_barbell;
		}
		else if(sprite_name.equals("chest")){
			return image.perma_item_chest;
		}
		else if(sprite_name.equals("jump_rope")){
			return image.perma_item_jump_rope;
		}
		else if(sprite_name.equals("piano")){
			return image.perma_item_piano;
		}
		else if(sprite_name.equals("shoes")){
			return image.perma_item_shoes;
		}
		else if(sprite_name.equals("holiday_valentines")){
			return image.perma_item_holiday_valentines;
		}
		else if(sprite_name.equals("holiday_st_patricks")){
			return image.perma_item_holiday_st_patricks;
		}
		else if(sprite_name.equals("holiday_easter")){
			return image.perma_item_holiday_easter;
		}
		else if(sprite_name.equals("holiday_halloween")){
			return image.perma_item_holiday_halloween;
		}
		else if(sprite_name.equals("holiday_christmas")){
			return image.perma_item_holiday_christmas;
		}
		else{
			return image.perma_item_duckie;
		}
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd=get_sprite_data(image,name);
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public void move(View view){
		//Keep the perma item in the screen bounds.
		if(x+w<0){
			x=0;
		}
		if(x>view.getWidth()){
			x=view.getWidth()-w;
		}
		if(y+h<0){
			y=0;
		}
		if(y>view.getHeight()){
			y=view.getHeight()-h;
		}
	}
	
	public void animate(){
		if(sprite!=null){
			sprite.animate(-1,-1,-1,-1);
		}
	}
	
	public void render(Canvas canvas,Resources res){
		if(sprite!=null){
			sprite.draw(canvas,res,(int)x,(int)y,w,h,Direction.LEFT,Color.WHITE,false,1.0f,1.0f);
		}
	}
}
