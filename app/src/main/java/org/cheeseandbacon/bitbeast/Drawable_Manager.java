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
