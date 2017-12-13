/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.graphics.Bitmap;

public class Sprite_Data{
	Bitmap bitmap;
	int animation_speed;
	int frame_count;
	boolean anim_fuzzy;
	
	public Sprite_Data(Bitmap get_bitmap,int get_animation_speed,int get_frame_count,boolean get_anim_fuzzy){
		bitmap=get_bitmap;
		animation_speed=get_animation_speed;
		frame_count=get_frame_count;
		anim_fuzzy=get_anim_fuzzy;
	}
	
	public int get_width(){
    	return bitmap.getWidth()/frame_count;
    }
    public int get_height(){
    	return bitmap.getHeight();
    }
}
