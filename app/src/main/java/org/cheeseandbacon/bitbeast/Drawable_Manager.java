/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class Drawable_Manager{
	public static void unbind_drawables(View view){
		if(view.getBackground()!=null){
	        view.getBackground().setCallback(null);
	    }
		
	    if(view instanceof ViewGroup && !(view instanceof AdapterView)){
	        for(int i=0;i<((ViewGroup)view).getChildCount();i++){
	            unbind_drawables(((ViewGroup)view).getChildAt(i));
	        }
	        
	        ((ViewGroup)view).removeAllViews();
	    }
	}
}
