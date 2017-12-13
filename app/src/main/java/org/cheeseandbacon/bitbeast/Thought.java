/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class Thought{
	private AnimatedSprite sprite_bubble;
	private AnimatedSprite sprite_thought;
	
	private float x;
	private float y;
	private int w;
	private int h;
	
	//Whether or not the thought bubble should be flipped on rendering.
	private boolean flip_x;
	private boolean flip_y;
	
	//Counts down to the deletion of this thought.
	private int counter_length;
	
	int type;
	
	private static final float FLIP_MARGIN=8.0f;
	
	public Thought(Image image,View view,Pet pet,int get_type,long get_length){
		sprite_bubble=null;
		sprite_thought=null;
		
		x=0.0f;
		y=0.0f;
		w=0;
		h=0;
		
		flip_x=false;
		flip_y=false;
		
		counter_length=(int)get_length;

		type=get_type;
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
			
			set_position(view,pet);
		}
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd;
		
		sd=image.bubble_thought;
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite_bubble=new AnimatedSprite();
		sprite_bubble.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		if(type==Thought_Type.HAPPY){
			sd=image.thought_happy;
		}
		else if(type==Thought_Type.COLD){
			sd=image.thought_cold;
		}
		else if(type==Thought_Type.DIRTY){
			sd=image.thought_dirty;
		}
		else if(type==Thought_Type.HOT){
			sd=image.thought_hot;
		}
		else if(type==Thought_Type.HUNGRY){
			sd=image.thought_hungry;
		}
		else if(type==Thought_Type.SAD){
			sd=image.thought_sad;
		}
		else if(type==Thought_Type.SICK){
			sd=image.thought_sick;
		}
		else if(type==Thought_Type.THIRSTY){
			sd=image.thought_thirsty;
		}
		else if(type==Thought_Type.MUSIC){
			sd=image.thought_music;
		}
		else if(type==Thought_Type.POOP){
			sd=image.thought_poop;
		}
		
		sprite_thought=new AnimatedSprite();
		sprite_thought.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	private void set_position(View view, Pet pet){
		x=pet.x+pet.w;
		y=pet.y-h;
		flip_x=false;
		flip_y=false;
		
		if(x+w>=view.getWidth() && (pet.x-w>0 || x+Px.px(view.getResources(),FLIP_MARGIN)>=view.getWidth())){
			x=pet.x-w;
			flip_x=true;
		}
		if(y<=0 && (pet.y+pet.h+h<view.getHeight() || y+h-Px.px(view.getResources(),FLIP_MARGIN)<=0)){
			y=pet.y+pet.h;
			flip_y=true;
		}
	}
	
	//Returns true if the thought should continue existing.
	//Returns false if the thought should be deleted
	public boolean move(View view,Pet pet){
		set_position(view,pet);

		return --counter_length > 0;
	}
	
	public void render(Canvas canvas,Resources res,Pet_Status pet_status){
		if(sprite_bubble!=null){
			int direction=Direction.LEFT;
			if(flip_x){
				direction=Direction.RIGHT;
			}
			sprite_bubble.draw(canvas,res,(int)x,(int)y,w,h,direction,Color.WHITE,flip_y,1.0f,1.0f);
		}
		
		if(sprite_thought!=null){
			float thought_x=6*(w/18);
			float thought_y=3*(h/17);
			if(flip_x){
				thought_x=3*(w/18);
			}
			if(flip_y){
				thought_y=7*(h/17);
			}
			
			sprite_thought.draw(canvas,res,(int)(x+thought_x),(int)(y+thought_y),sprite_thought.get_width(),sprite_thought.get_height(),Direction.LEFT,Color.WHITE,false,1.0f,1.0f);
		}
	}
}
