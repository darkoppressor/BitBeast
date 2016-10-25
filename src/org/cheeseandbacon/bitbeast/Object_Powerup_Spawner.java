package org.cheeseandbacon.bitbeast;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;

public class Object_Powerup_Spawner{
	private AnimatedSprite sprite;
	private AnimatedSprite sprite_off;
	
	float x;
	float y;
	int w;
	int h;
	
	float move_speed_x;
	
	int counter_reset;
	
	static final int SPEED_MIN=4;
	static final int SPEED_MAX=16;
	static final int POWERUP_SPAWN_TIME=5;
	
	float PIXEL_SAFETY;
	
	public Object_Powerup_Spawner(Image image,View view,float get_x,float get_y,float get_move_speed_x){
		sprite=null;
		sprite_off=null;
		
		x=get_x;
		y=get_y;
		w=0;
		h=0;
		
		move_speed_x=get_move_speed_x;
		
		counter_reset=0;
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
		}
		
		PIXEL_SAFETY=Px.px(view.getResources(),2.0f);
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd=image.object_powerup_spawner_on;
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
		
		sd=image.object_powerup_spawner_off;
		
		sprite_off=new AnimatedSprite();
		sprite_off.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public void move_workout(View view,Resources res){
		if(counter_reset>0){
			counter_reset--;
		}
		
		float move_chunk;
		
        if(Math.abs(move_speed_x)<PIXEL_SAFETY){
        	move_chunk=Math.abs(move_speed_x);
        }
        else{
        	move_chunk=PIXEL_SAFETY;
        }

        for(float i=Math.abs(move_speed_x);i>0.0f;){
            //If we have run_chunk or more pixels left to move,
            //we will move run_chunk pixels, call handle_events(), and loop back up here.

            //Or, if we have less than run_chunk pixels left to move,
            //we will move whatever pixels we have left and call handle_events() once more.
            if(i<move_chunk){
            	move_chunk=i;
                i=0.0f;
            }

            //Move.
            if(move_speed_x<0.0f){
            	move_chunk*=-1.0f;
            }
            x+=move_chunk;
            if(move_speed_x<0.0f){
            	move_chunk*=-1.0f;
            }

            //If we still have pixels left to move.
            if(i!=0.0f){
                i-=move_chunk;
            }

            handle_events_workout(view,res);
        }
	}
	
	public void handle_events_workout(View view,Resources res){
		if(x<0){
			x=0;
			
			if(move_speed_x<=0){
				move_speed_x=Px.px(res,(float)RNG.random_range(Object_Powerup_Spawner.SPEED_MIN,Object_Powerup_Spawner.SPEED_MAX));
			}
			else{
				move_speed_x=-(Px.px(res,(float)RNG.random_range(Object_Powerup_Spawner.SPEED_MIN,Object_Powerup_Spawner.SPEED_MAX)));
			}
		}
		if(x+w>view.getWidth()){
			x=view.getWidth()-w;
			
			if(move_speed_x<=0){
				move_speed_x=Px.px(res,(float)RNG.random_range(Object_Powerup_Spawner.SPEED_MIN,Object_Powerup_Spawner.SPEED_MAX));
			}
			else{
				move_speed_x=-(Px.px(res,(float)RNG.random_range(Object_Powerup_Spawner.SPEED_MIN,Object_Powerup_Spawner.SPEED_MAX)));
			}
		}
	}
	
	public void animate(){
		if(sprite!=null){
			sprite.animate(-1,-1,-1,-1);
		}
		
		if(sprite_off!=null){
			sprite_off.animate(-1,-1,-1,-1);
		}
	}
	
	public void render(Canvas canvas,Resources res){
		if(counter_reset==0 && sprite!=null){
			sprite.draw(canvas,res,(int)x,(int)y,w,h,Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
		}
		else if(counter_reset>0 && sprite_off!=null){
			sprite_off.draw(canvas,res,(int)x,(int)y,w,h,Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
		}
	}
}
