package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class ImageViewFood extends ImageView{
	Activity_Store as;
	
	public ImageViewFood(Context context){
		super(context);
		
		as=null;
	}
	
	public ImageViewFood(Context context,AttributeSet attrs){
		super(context,attrs);
		
		as=null;
	}
	
	public ImageViewFood(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		
		as=null;
	}
	
	public void set_as(Activity_Store get_as){
		as=get_as;
	}
	
	@Override
	protected void onAnimationEnd(){
		super.onAnimationEnd();
		
		if(as.dialog_consume.isShowing()){
			as.dismissDialog(Activity_Store.DIALOG_CONSUME);
	    }
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		((View)this.getParent()).invalidate();
	}
}
