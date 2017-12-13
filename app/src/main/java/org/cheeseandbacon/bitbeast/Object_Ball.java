/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.view.View;

import java.util.ArrayList;

public class Object_Ball{
	private AnimatedSprite sprite;
	
	float x;
	float y;
	int w;
	int h;
	
	float move_speed_x;
	float move_speed_y;
	
	int counter_move;
	
	int noclip;
	
	static final int SPEED_MIN=12;
	static final int SPEED_MAX=14;
	static final long VIBRATE_TIME_HIT=50;
	static final int POWERUP_CHANCE=65;
	
	float PIXEL_SAFETY;
	
	public Object_Ball(Image image,View view,float get_x,float get_y,float get_move_speed_x,float get_move_speed_y,long get_counter_move,int get_noclip){
		sprite=null;
		
		x=get_x;
		y=get_y;
		w=0;
		h=0;
		
		move_speed_x=get_move_speed_x;
		move_speed_y=get_move_speed_y;
		
		counter_move=(int)get_counter_move;
		
		noclip=get_noclip;
		
		if(image!=null && view!=null){
			reset_sprite(image,view);
		}
		
		PIXEL_SAFETY=Px.px(view.getResources(),2.0f);
	}
	
	public void reset_sprite(Image image,View view){
		Sprite_Data sd=image.object_ball;
		
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
	
	public int move_workout(View view,ArrayList<Object_Paddle> paddles,Vibrator vibrator){
		int scorer=-1;
		
		if(counter_move>0){
			counter_move--;
		}
		
		if(counter_move<=0){
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

                scorer=handle_events_workout(view,paddles,vibrator);
                if(scorer!=-1){
                	break;
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

                scorer=handle_events_workout(view,paddles,vibrator);
                if(scorer!=-1){
                	break;
                }
            }
		}
		
		return scorer;
	}
	
	public int handle_events_workout(View view,ArrayList<Object_Paddle> paddles,Vibrator vibrator){
		int scorer=-1;

		Resources res=view.getResources();
		
		if(noclip==0){
			for(int i=0;i<paddles.size();i++){
				boolean paddle_hit=handle_collision(paddles.get(i).x,paddles.get(i).y,paddles.get(i).w,paddles.get(i).h,paddles.get(i).move_state,res);
			
				if(paddle_hit){
					Sound_Manager.play_sound(Sound.GAME_HIT_PADDLE);
					
					//If the player's paddle was hit.
					if(i==0){
						vibrate_hit(vibrator);
					}
				}
			}
		}
		
		//Keep the object in the screen bounds.
		if(x<0){
			Sound_Manager.play_sound(Sound.GAME_HIT_WALL);
			
			x=0;
			
			move_speed_x=-move_speed_x;
			
			if(noclip>0){
				noclip--;
			}
		}
		if(x+w>view.getWidth()){
			Sound_Manager.play_sound(Sound.GAME_HIT_WALL);
			
			x=view.getWidth()-w;
			
			move_speed_x=-move_speed_x;
			
			if(noclip>0){
				noclip--;
			}
		}
		if(y+h<0){
			scorer=0;
			
			if(noclip>0){
				noclip--;
			}
		}
		if(y>view.getHeight()){
			scorer=1;
			
			if(noclip>0){
				noclip--;
			}
		}
		
		return scorer;
	}
	
	//Returns true if the ball is still in play.
	//Returns false if the ball is gone.
	public boolean move_bricks(Game_Bricks game,View view,Image image,ArrayList<Object_Paddle> paddles,ArrayList<Object_Brick> bricks,ArrayList<Object_Powerup> powerups,Vibrator vibrator){
		boolean ball_in_play=true;

		if(counter_move>0){
			counter_move--;
		}
		
		if(counter_move<=0){
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

                ball_in_play=handle_events_bricks(game,view,image,paddles,bricks,powerups,vibrator);
                if(!ball_in_play){
                	break;
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

                ball_in_play=handle_events_bricks(game,view,image,paddles,bricks,powerups,vibrator);
                if(!ball_in_play){
                	break;
                }
            }
		}
		
		return ball_in_play;
	}
	
	public boolean handle_events_bricks(Game_Bricks game,View view,Image image,ArrayList<Object_Paddle> paddles,ArrayList<Object_Brick> bricks,ArrayList<Object_Powerup> powerups,Vibrator vibrator){
		boolean ball_in_play=true;
		Resources res=view.getResources();
		
		if(noclip==0){
			for(int i=0;i<paddles.size();i++){
				boolean paddle_hit=handle_collision(paddles.get(i).x,paddles.get(i).y,paddles.get(i).w,paddles.get(i).h,paddles.get(i).move_state,res);
			
				//If the player's paddle was hit.
				if(paddle_hit){
					Sound_Manager.play_sound(Sound.GAME_HIT_PADDLE);
					
					vibrate_hit(vibrator);
				}
			}
		}
		
		for(int i=0;i<bricks.size();i++){
			boolean brick_hit=false;
			
			if(noclip==0){
				brick_hit=handle_collision(bricks.get(i).x,bricks.get(i).y,bricks.get(i).w,bricks.get(i).h,Direction.NONE,res);
			}
			else{
				brick_hit=check_collision(bricks.get(i).x,bricks.get(i).y,bricks.get(i).w,bricks.get(i).h);
			}
			
			if(brick_hit){
				Sound_Manager.play_sound(Sound.GAME_HIT_BRICK);
				
				if(RNG.random_range(0,99)<POWERUP_CHANCE){
					short type=(short)RNG.random_range(Object_Powerup.POWER_BEGIN,Object_Powerup.POWER_END-1);
					
					float speed_x=Px.px(res,(float)RNG.random_range(Object_Powerup.SPEED_MIN,Object_Powerup.SPEED_MAX));
					float speed_y=Px.px(res,(float)RNG.random_range(Object_Powerup.SPEED_MIN,Object_Powerup.SPEED_MAX));
					if(RNG.random_range(0,99)<50){
						speed_x*=-1.0f;
					}
					float new_x=bricks.get(i).x;
					float new_y=bricks.get(i).y;
					
					Sound_Manager.play_sound(Sound.POWERUP_SPAWN);
					
					powerups.add(new Object_Powerup(image,view,type,new_x,new_y,speed_x,speed_y));
				}
				
				game.brick_count++;
				
				bricks.remove(i);
				i--;
			}
		}
		
		//Keep the object in the screen bounds.
		if(x<0){
			Sound_Manager.play_sound(Sound.GAME_HIT_WALL);
			
			x=0;
			
			move_speed_x=-move_speed_x;
			
			if(noclip>0){
				noclip--;
			}
		}
		if(x+w>view.getWidth()){
			Sound_Manager.play_sound(Sound.GAME_HIT_WALL);
			
			x=view.getWidth()-w;
			
			move_speed_x=-move_speed_x;
			
			if(noclip>0){
				noclip--;
			}
		}
		if(y<0){
			Sound_Manager.play_sound(Sound.GAME_HIT_WALL);
			
			y=0;
			
			move_speed_y=-move_speed_y;
			
			if(noclip>0){
				noclip--;
			}
		}
		if(y>view.getHeight()){
			ball_in_play=false;
			
			if(noclip>0){
				noclip--;
			}
		}
		
		return ball_in_play;
	}
	
	public boolean check_collision(float object_x,float object_y,int object_w,int object_h){
		boolean hit=false;
		
		if(Collision.check(x+5,y,w-10,h,object_x,object_y,object_w,object_h)){
			hit=true;
        }
		
		if(Collision.check(x,y+5,w,h-10,object_x,object_y,object_w,object_h)){
			hit=true;
        }
		
		return hit;
	}
	
	public boolean handle_collision(float object_x,float object_y,int object_w,int object_h,int object_move_state,Resources res){
		boolean hit=false;
		
		if(Collision.check(x+5,y,w-10,h,object_x,object_y,object_w,object_h)){
			hit=true;
			
			move_speed_y=-move_speed_y;
			
			if(object_move_state==Direction.LEFT){
            	move_speed_x-=Px.px(res,(float)RNG.random_range(1,2));
            }
			else if(object_move_state==Direction.RIGHT){
				move_speed_x+=Px.px(res,(float)RNG.random_range(1,2));
			}
			
            if(y<object_y){
                y=object_y-h;
            }
            else if(y+h>object_y+object_h){
                y=object_y+object_h;
            }
        }
		
		if(Collision.check(x,y+5,w,h-10,object_x,object_y,object_w,object_h)){
			hit=true;
			
			move_speed_x=-move_speed_x;
			
            if(x<object_x){
                x=object_x-w;
            }
            else if(x+w>object_x+object_w){
                x=object_x+object_w;
            }
        }
		
		return hit;
	}
	
	public void render(Canvas canvas,Resources res){
		if(sprite!=null){
			int color=res.getColor(R.color.game_white);
			
			if(noclip==2){
				color=res.getColor(R.color.game_ball_noclip_2);
			}
			else if(noclip==1){
				color=res.getColor(R.color.game_ball_noclip_1);
			}
			
			sprite.draw(canvas,res,(int)x,(int)y,w,h,Direction.LEFT,color,false,1.0f,1.0f);
		}
	}
}
