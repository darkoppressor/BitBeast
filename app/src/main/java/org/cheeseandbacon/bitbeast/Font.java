/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class Font{
	static Typeface font1;
	
	public static synchronized void startup(AssetManager assets){
		if(font1==null){
			font1=Typeface.createFromAsset(assets,"fonts/font1.ttf");
		}
	}
	
	public static synchronized void set_typeface(AssetManager assets, TextView...params){
	    startup(assets);

		if(font1!=null){
			for(TextView view:params){
				view.setTypeface(font1);
			}
		}
	}
	
	public static synchronized void cleanup(){
		if(font1!=null){
			font1=null;
		}
	}
}
