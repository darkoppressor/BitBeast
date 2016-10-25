package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.view.View;

public class Object_Powerup{
	private AnimatedSprite sprite;
	
	float x;
	float y;
	int w;
	int h;
	
	float move_speed_x;
	float move_speed_y;
	
	short type;
	
	static final int SPEED_MIN=4;
	static final int SPEED_MAX=16;
	static final long VIBRATE_TIME_HIT=50;
	static final int SPEED_BONUS_UP=3;
	static final int SPEED_BONUS_DOWN=-3;
	static final int NOCLIP_BOUNCES=2;
	
	float PIXEL_SAFETY;
	
	static final int GAME_MODE_WORKOUT=0;
	static final int GAME_MODE_BRICKS=1;
	
	//--- Powerups ---//
	
	static final short POWER_BEGIN=0;
	static final short BALL_SPEED_UP=POWER_BEGIN;
	static final short BALL_SPEED_DOWN=1;
	static final short BALL_EXTRA=2;
	static final short BALL_NOCLIP=3;
	static final short LIFE=4;
	static final short POWER_END=5;
	
	public Object_Powerup(Image image,View view,short get_type,float get_x,float get_y,float get_move_speed_x,float get_move_speed_y){
		sprite=null;
		
		type=get_type;
		
		x=get_x;
		y=get_y;
		w=0;
		h=0;
		
		move_speed_x=get_move_speed_x;
		move_speed_y=get_move_speed_y;
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
		}
		
		PIXEL_SAFETY=Px.px(view.getResources(),2.0f);
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd=null;
		
		if(type==BALL_SPEED_UP){
			sd=image.object_powerup_ball_speed_up;
		}
		else if(type==BALL_SPEED_DOWN){
			sd=image.object_powerup_ball_speed_down;
		}
		else if(type==BALL_EXTRA){
			sd=image.object_powerup_ball_extra;
		}
		else if(type==BALL_NOCLIP){
			sd=image.object_powerup_ball_noclip;
		}
		else if(type==LIFE){
			sd=image.object_powerup_life;
		}
		
		w=sd.bitmap.getWidth()/sd.frame_count;
		h=sd.bitmap.getHeight();
		
		sprite=new AnimatedSprite();
		sprite.Initialize(view,image,w,h,sd.bitmap,sd.animation_speed,sd.frame_count,sd.anim_fuzzy);
	}
	
	public void vibrate_hit(Vibrator vibrator){
		if(Options.vibrate){
			vibrator.cancel();
			vibrator.vibrate(VIBRATE_TIME_HIT);
		}
	}
	
	public void activate_effect(int game_mode,Game_Bricks game_bricks,Game_Workout game_workout,GameView game_view,Pet_Status pet_status,int collecting_paddle,ArrayList<Object_Paddle> paddles,ArrayList<Object_Ball> balls,ArrayList<Boolean> new_balls,Resources res){
		if(type==BALL_SPEED_UP || type==BALL_SPEED_DOWN){
			int speed_bonus=0;
			if(type==BALL_SPEED_UP){
				speed_bonus=SPEED_BONUS_UP;
			}
			else if(type==BALL_SPEED_DOWN){
				speed_bonus=SPEED_BONUS_DOWN;
			}
			
			for(int i=0;i<balls.size();i++){
				if(balls.get(i).move_speed_x<0){
					balls.get(i).move_speed_x-=Px.px(res,(float)speed_bonus);
				}
				else if(balls.get(i).move_speed_x>=0){
					balls.get(i).move_speed_x+=Px.px(res,(float)speed_bonus);
				}
				
				if(balls.get(i).move_speed_y<0){
					balls.get(i).move_speed_y-=Px.px(res,(float)speed_bonus);
				}
				else if(balls.get(i).move_speed_y>=0){
					balls.get(i).move_speed_y+=Px.px(res,(float)speed_bonus);
				}
				
				//Emergency protection from the ball stopping completely.
				if(balls.get(i).move_speed_x==0.0f){
					balls.get(i).move_speed_x=1.0f;
				}
				if(balls.get(i).move_speed_y==0.0f){
					balls.get(i).move_speed_y=1.0f;
				}
			}
		}
		else if(type==BALL_EXTRA){
			//Hard limit for technical reasons.
			if(balls.size()<25){
				new_balls.add(true);
			}
		}
		else if(type==BALL_NOCLIP){
			for(int i=0;i<balls.size();i++){
				balls.get(i).noclip=NOCLIP_BOUNCES;
			}
		}
		else if(type==LIFE){
			if(game_mode==GAME_MODE_WORKOUT){
				paddles.get(collecting_paddle).score++;
				
				game_workout.check_for_winner(game_view,pet_status);
			}
			else if(game_mode==GAME_MODE_BRICKS){
				game_bricks.lives++;
			}
		}
	}
	
	//Returns true if still active, false otherwise.
	public boolean move_workout(GameView game_view,Game_Workout game_workout,Pet_Status pet_status,ArrayList<Object_Paddle> paddles,ArrayList<Object_Ball> balls,ArrayList<Boolean> new_balls,Vibrator vibrator){
		boolean still_active=true;
		
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

            still_active=handle_events_workout(game_view,game_workout,pet_status,paddles,balls,new_balls,vibrator);
            if(!still_active){
            	return still_active;
            }
        }
        
        if(Math.abs(move_speed_y)<PIXEL_SAFETY){
        	move_chunk=Math.abs(move_speed_y);
        }
        else{
        	move_chunk=PIXEL_SAFETY;
        }

        for(float i=Math.abs(move_speed_y);i>0.0f;){
            //If we have run_chunk or more pixels left to move,
            //we will move run_chunk pixels, call handle_events(), and loop back up here.

            //Or, if we have less than run_chunk pixels left to move,
            //we will move whatever pixels we have left and call handle_events() once more.
            if(i<move_chunk){
            	move_chunk=i;
                i=0.0f;
            }

            //Move.
            if(move_speed_y<0.0f){
            	move_chunk*=-1.0f;
            }
            y+=move_chunk;
            if(move_speed_y<0.0f){
            	move_chunk*=-1.0f;
            }

            //If we still have pixels left to move.
            if(i!=0.0f){
                i-=move_chunk;
            }

            still_active=handle_events_workout(game_view,game_workout,pet_status,paddles,balls,new_balls,vibrator);
            if(!still_active){
            	return still_active;
            }
        }
		
		return still_active;
	}
	
	public boolean handle_events_workout(GameView game_view,Game_Workout game_workout,Pet_Status pet_status,ArrayList<Object_Paddle> paddles,ArrayList<Object_Ball> balls,ArrayList<Boolean> new_balls,Vibrator vibrator){
		Resources res=game_view.getResources();
		
		boolean still_active=true;
		
		for(int i=0;i<paddles.size();i++){
			boolean paddle_hit=handle_collision(paddles.get(i).x,paddles.get(i).y,paddles.get(i).w,paddles.get(i).h,paddles.get(i).move_state,res);
		
			if(paddle_hit){
				Sound_Manager.play_sound(Sound.POWERUP_GET);
				
				activate_effect(GAME_MODE_WORKOUT,null,game_workout,game_view,pet_status,i,paddles,balls,new_balls,res);
				
				//If the player's paddle was hit.
				if(i==0){
					vibrate_hit(vibrator);
				}
				
				still_active=false;
				
				break;
			}
		}
		
		//When the powerup leaves the screen, it vanishes.
		if(x<0){
			x=0;
			
			move_speed_x=-move_speed_x;
		}
		if(x+w>game_view.getWidth()){
			x=game_view.getWidth()-w;
			
			move_speed_x=-move_speed_x;
		}
		if(y+h<0){
			still_active=false;
		}
		if(y>game_view.getHeight()){
			still_active=false;
		}
		
		return still_active;
	}
	
	//Returns true if still active, false otherwise.
	public boolean move_bricks(GameView game_view,Game_Bricks game_bricks,Pet_Status pet_status,ArrayList<Object_Paddle> paddles,ArrayList<Object_Ball> balls,ArrayList<Boolean> new_balls,ArrayList<Object_Brick> bricks,int bricks_vertical,Vibrator vibrator){
		boolean still_active=true;
		
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

            still_active=handle_events_bricks(game_view,game_bricks,pet_status,paddles,balls,new_balls,bricks,bricks_vertical,vibrator);
            if(!still_active){
            	return still_active;
            }
        }
        
        if(Math.abs(move_speed_y)<PIXEL_SAFETY){
        	move_chunk=Math.abs(move_speed_y);
        }
        else{
        	move_chunk=PIXEL_SAFETY;
        }

        for(float i=Math.abs(move_speed_y);i>0.0f;){
            //If we have run_chunk or more pixels left to move,
            //we will move run_chunk pixels, call handle_events(), and loop back up here.

            //Or, if we have less than run_chunk pixels left to move,
            //we will move whatever pixels we have left and call handle_events() once more.
            if(i<move_chunk){
            	move_chunk=i;
                i=0.0f;
            }

            //Move.
            if(move_speed_y<0.0f){
            	move_chunk*=-1.0f;
            }
            y+=move_chunk;
            if(move_speed_y<0.0f){
            	move_chunk*=-1.0f;
            }

            //If we still have pixels left to move.
            if(i!=0.0f){
                i-=move_chunk;
            }

            still_active=handle_events_bricks(game_view,game_bricks,pet_status,paddles,balls,new_balls,bricks,bricks_vertical,vibrator);
            if(!still_active){
            	return still_active;
            }
        }
		
		return still_active;
	}
	
	public boolean handle_events_bricks(GameView game_view,Game_Bricks game_bricks,Pet_Status pet_status,ArrayList<Object_Paddle> paddles,ArrayList<Object_Ball> balls,ArrayList<Boolean> new_balls,ArrayList<Object_Brick> bricks,int bricks_vertical,Vibrator vibrator){
		Resources res=game_view.getResources();
		
		boolean still_active=true;
		
		for(int i=0;i<paddles.size();i++){
			boolean paddle_hit=handle_collision(paddles.get(i).x,paddles.get(i).y,paddles.get(i).w,paddles.get(i).h,paddles.get(i).move_state,res);
			
			//If the player's paddle was hit.
			if(paddle_hit){
				Sound_Manager.play_sound(Sound.POWERUP_GET);
				
				activate_effect(GAME_MODE_BRICKS,game_bricks,null,game_view,pet_status,i,paddles,balls,new_balls,res);
				
				vibrate_hit(vibrator);
				
				still_active=false;
				
				break;
			}
		}
		
		//When the powerup leaves the screen, it vanishes.
		if(x<0){
			x=0;
			
			move_speed_x=-move_speed_x;
		}
		if(x+w>game_view.getWidth()){
			x=game_view.getWidth()-w;
			
			move_speed_x=-move_speed_x;
		}
		if(y+h<0){
			still_active=false;
		}
		if(y>game_view.getHeight()){
			still_active=false;
		}
		
		return still_active;
	}
	
	public boolean handle_collision(float object_x,float object_y,int object_w,int object_h,int object_move_state,Resources res){
		boolean hit=false;
		
		if(Collision.check(x+5,y,w-10,h,object_x,object_y,object_w,object_h)){
			hit=true;
        }
		
		if(Collision.check(x,y+5,w,h-10,object_x,object_y,object_w,object_h)){
			hit=true;
        }
		
		return hit;
	}
	
	public void animate(){
		if(sprite!=null){
			sprite.animate(-1,-1,-1,-1);
		}
	}
	
	public void render(Canvas canvas,Resources res){
		if(sprite!=null){
			sprite.draw(canvas,res,(int)x,(int)y,w,h,Direction.LEFT,res.getColor(R.color.key_color),false,1.0f,1.0f);
		}
	}
}
