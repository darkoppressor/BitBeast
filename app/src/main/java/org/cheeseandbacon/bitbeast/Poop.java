/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class Poop{
	private AnimatedSprite sprite;
	
	float x;
	float y;
	private int w;
	private int h;
	
	public Poop(Image image,View view,float get_x,float get_y){
		sprite=null;
		
		x=get_x;
		y=get_y;
		w=0;
		h=0;
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
		}
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd=image.poop;
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public void move(View view){
		//Keep the poop in the screen bounds.
		if(x<0){
			x=0;
		}
		if(x+w>view.getWidth()){
			x=view.getWidth()-w;
		}
		if(y<0){
			y=0;
		}
		if(y+h>view.getHeight()){
			y=view.getHeight()-h;
		}
	}
	
	public void render(Canvas canvas,Resources res){
		if(sprite!=null){
			sprite.draw(canvas,res,(int)x,(int)y,w,h,Direction.LEFT,Color.WHITE,false,1.0f,1.0f);
		}
	}
}
