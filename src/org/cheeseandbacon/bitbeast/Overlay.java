package org.cheeseandbacon.bitbeast;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;

public class Overlay{
	private final static float MOVE_SPEED_HORIZONTAL=96;
	private final static float MOVE_SPEED_VERTICAL=96;
	static final int OVERLAY_LIMIT=6;
	
	private AnimatedSprite sprite;
	
	private float x;
	private float y;
	private int w;
	private int h;
	
	private int move_state;
	float move_speed;
	
	int type;
	
	public Overlay(Image image,View view,float get_x,float get_y,int get_move_state,int get_type){
		sprite=null;
		
		x=get_x;
		y=get_y;
		w=0;
		h=0;
		
		move_state=get_move_state;
		type=get_type;
		
		if(move_state==Direction.LEFT || move_state==Direction.RIGHT){
			move_speed=Px.px(view.getResources(),MOVE_SPEED_HORIZONTAL);
		}
		else if(move_state==Direction.UP || move_state==Direction.DOWN){
			move_speed=Px.px(view.getResources(),MOVE_SPEED_VERTICAL);
		}
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
		}
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd=null;
		
		if(type==Overlay_Type.CLEAN_YARD){
			sd=image.overlay_clean_yard;
		}
		else if(type==Overlay_Type.CLEAN_PET){
			sd=image.overlay_clean_pet;
		}
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public void check_sprites(Image image,View view){
		if(sprite==null){
			reset_sprite(image,view);
		}
	}
	
	public void move(View view,Pet pet){
		if(move_state==Direction.LEFT || move_state==Direction.LEFT_UP ||move_state==Direction.LEFT_DOWN){
			x-=move_speed;
			
			if(x+w<0){
				move_speed=0;
			}
		}
		else if(move_state==Direction.RIGHT || move_state==Direction.RIGHT_UP ||move_state==Direction.RIGHT_DOWN){
			x+=move_speed;
			
			if(x>view.getWidth()){
				move_speed=0;
			}
		}
		if(move_state==Direction.UP || move_state==Direction.LEFT_UP ||move_state==Direction.RIGHT_UP){
			y-=move_speed;
			
			if(y+h<0){
				move_speed=0;
			}
		}
		if(move_state==Direction.DOWN || move_state==Direction.LEFT_DOWN ||move_state==Direction.RIGHT_DOWN){
			y+=move_speed;
			
			if(y>view.getHeight()){
				move_speed=0;
			}
		}
		
		if(type==Overlay_Type.CLEAN_YARD){
			for(int i=0;i<pet.get_status().poops.size();i++){
				if(pet.get_status().poops.get(i).x>x){
					//Erase this poop.
					pet.get_status().poops.remove(i);
					i--;
				}
			}
		}
		else if(type==Overlay_Type.CLEAN_PET){
			if(pet.y+h<y+h){
				//Bathe the pet.
		    	pet.get_status().bath=0;
		    	
		    	pet.get_status().sleeping_wake_up();
			}
		}
	}
	
	public void render(Canvas canvas,Resources res){
		if(sprite!=null){
			sprite.draw(canvas,res,(int)x,(int)y,canvas.getWidth(),canvas.getHeight(),Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
		}
	}
}
