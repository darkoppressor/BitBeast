package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class Object_Paddle{
	private AnimatedSprite sprite;
	
	float x;
	float y;
	int w;
	int h;
	
	private float target_x;
	
	float move_speed_x;
	
	int move_state;
	//Did the paddle move due to a keypress last frame?
	boolean moved_with_key;
	
	boolean ai_controlled;
	float ai_difficulty;
	int counter_ai_update;
	
	int score;
	int color;
	
	private static final float MOVE_SPEED_X_PLAYER=80;
	
	private static final int MOVE_SPEED_X_AI_EASY_MIN=9;
	private static final int MOVE_SPEED_X_AI_EASY_MAX=14;
	private static final int MOVE_SPEED_X_AI_NORMAL_MIN=14;
	private static final int MOVE_SPEED_X_AI_NORMAL_MAX=16;
	private static final int MOVE_SPEED_X_AI_HARD_MIN=16;
	private static final int MOVE_SPEED_X_AI_HARD_MAX=18;
	
	static final float AI_DIFF_EASY=0.55f;
	static final float AI_DIFF_NORMAL=0.45f;
	static final float AI_DIFF_HARD=0.35f;
	
	public Object_Paddle(Image image,View view,float get_x,float get_y,int get_color,boolean get_ai_controlled,float get_ai_difficulty,int get_counter_ai_update,int get_score){
		sprite=null;
		
		x=get_x;
		y=get_y;
		w=0;
		h=0;
		
		move_state=Direction.NONE;
		moved_with_key=false;
		
		ai_controlled=get_ai_controlled;
		ai_difficulty=get_ai_difficulty;
		counter_ai_update=get_counter_ai_update;
		
		score=get_score;
		color=get_color;
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
		}
		
		target_x=x+w/2;
		
		if(!ai_controlled){
			move_speed_x=MOVE_SPEED_X_PLAYER;
		}
		else{
			if(ai_difficulty==AI_DIFF_EASY){
				move_speed_x=MOVE_SPEED_X_AI_EASY_MIN;
			}
			else if(ai_difficulty==AI_DIFF_NORMAL){
				move_speed_x=MOVE_SPEED_X_AI_NORMAL_MIN;
			}
			else if(ai_difficulty==AI_DIFF_HARD){
				move_speed_x=MOVE_SPEED_X_AI_HARD_MIN;
			}
		}
	}
	
	public void ai_new_speed(){
		if(ai_controlled){
			if(ai_difficulty==AI_DIFF_EASY && move_speed_x==MOVE_SPEED_X_AI_EASY_MAX){
				move_speed_x=MOVE_SPEED_X_AI_EASY_MIN;
			}
			else if(ai_difficulty==AI_DIFF_NORMAL && move_speed_x==MOVE_SPEED_X_AI_NORMAL_MAX){
				move_speed_x=MOVE_SPEED_X_AI_NORMAL_MIN;
			}
			else if(ai_difficulty==AI_DIFF_HARD && move_speed_x==MOVE_SPEED_X_AI_HARD_MAX){
				move_speed_x=MOVE_SPEED_X_AI_HARD_MIN;
			}
			else if(ai_difficulty==AI_DIFF_EASY && move_speed_x==MOVE_SPEED_X_AI_EASY_MIN){
				move_speed_x=MOVE_SPEED_X_AI_EASY_MAX;
			}
			else if(ai_difficulty==AI_DIFF_NORMAL && move_speed_x==MOVE_SPEED_X_AI_NORMAL_MIN){
				move_speed_x=MOVE_SPEED_X_AI_NORMAL_MAX;
			}
			else if(ai_difficulty==AI_DIFF_HARD && move_speed_x==MOVE_SPEED_X_AI_HARD_MIN){
				move_speed_x=MOVE_SPEED_X_AI_HARD_MAX;
			}
		}
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd=image.object_paddle;
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public void set_target(float get_target_x){
		target_x=get_target_x;
	}
	
	public void move_target(float get_target_move_x){
		target_x+=get_target_move_x;
	}
	
	public void add_score(int get_score){
		score+=get_score;
	}
	
	public void on_trackball_event(Resources res,MotionEvent event){
		if(!ai_controlled){
			float move_x=event.getX();
			///float move_y=event.getY();
			
			move_target(move_x*Px.px(res,move_speed_x)*2.0f);
		}
	}
	
	public void input(Resources res,Key_States key_states){
		if(!ai_controlled){
			if(key_states.key(KeyEvent.KEYCODE_DPAD_LEFT) ||
					key_states.key(KeyEvent.KEYCODE_DPAD_UP)){
				set_target(x+(float)w/2.0f-move_speed_x);
				moved_with_key=true;
			}
			else if(key_states.key(KeyEvent.KEYCODE_DPAD_RIGHT) ||
					key_states.key(KeyEvent.KEYCODE_DPAD_DOWN)){
				set_target(x+(float)w/2.0f+move_speed_x);
				moved_with_key=true;
			}
			else{
				if(moved_with_key){
					set_target(x+(float)w/2.0f);
					moved_with_key=false;
				}
			}
		}
	}
	
	public void ai(View view,ArrayList<Object_Ball> balls,ArrayList<Object_Powerup> powerups){
		if(ai_controlled){
			if(--counter_ai_update<=0){
				counter_ai_update=(int)Math.floor(ai_difficulty*(float)FPS.fps);
				
				ai_new_speed();
			}
			
			target_x=x+w/2;
			
			Object_Powerup nearest_powerup=Distance.nearest_powerup(this,powerups);
			if(nearest_powerup!=null){
				target_x=nearest_powerup.x+nearest_powerup.w/2;
			}
			
			Object_Ball nearest_ball=Distance.nearest_ball(this,balls);
			if(nearest_ball!=null){
				target_x=nearest_ball.x+nearest_ball.w/2;
			}
		}
	}
	
	public void move(View view){
		Resources res=view.getResources();
		
		move_state=Direction.NONE;
		
		float recall_x=x;
		
		if(Math.abs(target_x-(x+w/2))<Px.px(res,move_speed_x)){
			x=target_x-w/2;
		}
		else{
			if(target_x<x+w/2){
				x-=Px.px(res,move_speed_x);
			}
			else if(target_x>x+w){
				x+=Px.px(res,move_speed_x);
			}
		}
		
		if(x<recall_x){
			move_state=Direction.LEFT;
		}
		else if(x>recall_x){
			move_state=Direction.RIGHT;
		}
		
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
