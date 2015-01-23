package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.view.View;

public class Evolution{
	private AnimatedSprite sprite;
	private AnimatedSprite sprite_pet;
	
	private int w;
	private int h;
	
	//The dimensions of the pet evolving.
	private int old_pet_w;
	private int old_pet_h;
	
	//The type of pet evolving.
	private Pet_Type type;
	
	//If true, render this evolution's type instead of the pet's type.
	boolean render_type;
	
	//The number of times the initial frames have been alternated between.
	private int frame_repeats;
	
	private static final int FRAME_REPEAT_LIMIT=8;
	
	public Evolution(Image image,View view,Pet_Type get_type,int get_pet_w,int get_pet_h){
		sprite=null;
		sprite_pet=null;
		
		w=0;
		h=0;
		
		old_pet_w=get_pet_w;
		old_pet_h=get_pet_h;
		
		type=get_type;
		
		render_type=true;
		
		frame_repeats=0;
		
		if(image!=null && view!=null){
			reset_sprite(image,view,view.getResources());
		}
	}
	
	public void reset_sprite(Image image,View view,Resources res){
		Sprite_Data sd=image.evolution;
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
				
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		image.load_pet_evolution(res,type);
		sd=image.pet_evolution;
		
		old_pet_w=sd.bitmap.getWidth()/sd.frame_count;
		old_pet_h=sd.bitmap.getHeight();
		
		sprite_pet=new AnimatedSprite();
		sprite_pet.Initialize(view,image,sd.bitmap.getWidth()/sd.frame_count,sd.bitmap.getHeight(),sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public boolean animate(Vibrator vibrator,Image image,View view,Pet pet){
		if(frame_repeats<FRAME_REPEAT_LIMIT){
			int frame=0;
			if(sprite.frame==0){
				frame=1;
			}
			
			sprite.animate(-1,-1,frame,7);
			
			//If the frame changed.
			if(frame==sprite.frame){
				frame_repeats++;
			}
		}
		else{
			int frame_before=sprite.frame;
			
			sprite.animate(-1,-1,-1,-1);
			
			//If the sprite just reset, we are done animating.
			if(sprite.frame!=frame_before && sprite.frame==0){
				if(Options.vibrate){
					vibrator.cancel();
				}
				
				return false;
			}
			else if(sprite.frame!=frame_before && sprite.frame==3){
				Sound_Manager.play_sound(Sound.EVOLUTION_2);
				
				//Remember the dimensions of the previous type.
				int old_w=old_pet_w;
				int old_h=old_pet_h;
				
				pet.reset_sprite(image,view,view.getResources());
				
				//Center the newly evolved pet on the center of its old type.
				pet.x=pet.x+old_w/2-pet.w/2;
				pet.y=pet.y+old_h/2-pet.h/2;
				
				render_type=false;
			}
		}
		
		if(Options.vibrate){
			vibrator.vibrate(1000);
		}
		
		return true;
	}
	
	public void render(Canvas canvas,Resources res,Pet_Status pet_status,float pet_x,float pet_y,int pet_w,int pet_h){
		int w_to_use=pet_w;
		int h_to_use=pet_h;
		
		if(render_type){
			w_to_use=old_pet_w;
			h_to_use=old_pet_h;
		}
		
		if(sprite!=null){
			sprite.draw(canvas,res,(int)pet_x+w_to_use/2-w/2,(int)pet_y+h_to_use/2-h/2,w,h,Direction.LEFT,pet_status.color,false,1.0f,1.0f);
		}
	}
	
	public void render_pet_type(Canvas canvas,Resources res,Pet_Status pet_status,float pet_x,float pet_y){
		if(sprite_pet!=null){
			sprite_pet.draw(canvas,res,(int)pet_x,(int)pet_y,old_pet_w,old_pet_h,Direction.LEFT,pet_status.color,false,1.0f,1.0f);
		}
	}
}
