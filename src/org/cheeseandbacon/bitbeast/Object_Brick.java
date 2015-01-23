package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;

public class Object_Brick{
	private AnimatedSprite sprite;
	
	float x;
	float y;
	int w;
	int h;
	
	int color;
	
	public Object_Brick(Image image,View view,float get_x,float get_y,int get_color){
		sprite=null;
		
		x=get_x;
		y=get_y;
		w=0;
		h=0;
		
		color=get_color;
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
		}
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd=image.object_brick;
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public void move(View view){
		//Keep the object in the screen bounds.
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
			sprite.draw(canvas,res,(int)x,(int)y,w,h,Direction.LEFT,color,false,1.0f,1.0f);
		}
	}
}
