/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class Game_Bricks{
	ArrayList<Object_Ball> balls;
	ArrayList<Object_Paddle> paddles;
	ArrayList<Object_Brick> bricks;
	ArrayList<Object_Powerup> powerups;
	
	//Used to keep track of balls about to be spawned.
	//This is pretty hacky but oh well.
	//The value is actually meaningless.
	ArrayList<Boolean> new_balls;
	
	Key_States key_states;
	
	boolean in_progress;
	
	private Handler handler;
	
	int bricks_vertical;
	
	int lives;
	
	//The number of bricks destroyed in this round.
	int brick_count;
	
	static final long VIBRATE_TIME_RESET=500;
	
	public Game_Bricks(Context context,Resources res,Pet_Status pet_status){
		balls=new ArrayList<Object_Ball>();
		paddles=new ArrayList<Object_Paddle>();
		bricks=new ArrayList<Object_Brick>();
		powerups=new ArrayList<Object_Powerup>();
		
		new_balls=new ArrayList<Boolean>();
		
		key_states=new Key_States();
		
		in_progress=false;
		
		handler=null;
		
		bricks_vertical=0;
		
		lives=0;
		
		brick_count=0;
	}
	
	public void set_handler(Handler get_handler){
		handler=get_handler;
	}
	
	public boolean on_key_down(int keyCode){
		if(in_progress){
			return key_states.on_key_down(keyCode);
		}
		else{
			return false;
		}
	}
	
	public boolean on_key_up(int keyCode){
		if(in_progress){
			return key_states.on_key_up(keyCode);
		}
		else{
			return false;
		}
	}
	
	public boolean on_trackball_event(Resources res,MotionEvent event){
		if(in_progress){
			for(int i=0;i<paddles.size();i++){
				paddles.get(i).on_trackball_event(res,event);
			}
			
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean on_touch_event(MotionEvent event){
		if(in_progress){
			int x=(int)event.getX();
			///int y=(int)event.getY();
			
			switch(event.getActionMasked()){
			case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_MOVE:
				paddles.get(0).set_target(x);
				return true;
			}
			
			return false;
		}
		else{
			return false;
		}
	}
	
	public void save_game(Bundle savedInstanceState){
		savedInstanceState.putInt("bricks_vertical",bricks_vertical);
		
		savedInstanceState.putInt("lives",lives);
		
		savedInstanceState.putInt("brick_count",brick_count);
		
		savedInstanceState.putInt("paddles_size",paddles.size());
		for(int i=0;i<paddles.size();i++){
			savedInstanceState.putFloat("paddle_"+i+"_x",paddles.get(i).x);
			savedInstanceState.putFloat("paddle_"+i+"_y",paddles.get(i).y);
			savedInstanceState.putBoolean("paddle_"+i+"_ai",paddles.get(i).ai_controlled);
			savedInstanceState.putFloat("paddle_"+i+"_ai_difficulty",paddles.get(i).ai_difficulty);
			savedInstanceState.putInt("paddle_"+i+"_counter_ai_update",paddles.get(i).counter_ai_update);
			savedInstanceState.putInt("paddle_"+i+"_score",paddles.get(i).score);
			savedInstanceState.putInt("paddle_"+i+"_color",paddles.get(i).color);
		}
		
		savedInstanceState.putInt("balls_size",balls.size());
		for(int i=0;i<balls.size();i++){
			savedInstanceState.putFloat("ball_"+i+"_x",balls.get(i).x);
			savedInstanceState.putFloat("ball_"+i+"_y",balls.get(i).y);
			savedInstanceState.putFloat("ball_"+i+"_move_speed_x",balls.get(i).move_speed_x);
			savedInstanceState.putFloat("ball_"+i+"_move_speed_y",balls.get(i).move_speed_y);
			savedInstanceState.putInt("ball_"+i+"_counter_move",balls.get(i).counter_move);
			savedInstanceState.putInt("ball_"+i+"_noclip",balls.get(i).noclip);
		}
		
		savedInstanceState.putInt("bricks_size",bricks.size());
		for(int i=0;i<bricks.size();i++){
			savedInstanceState.putFloat("brick_"+i+"_x",bricks.get(i).x);
			savedInstanceState.putFloat("brick_"+i+"_y",bricks.get(i).y);
			savedInstanceState.putInt("brick_"+i+"_color",bricks.get(i).color);
		}
		
		savedInstanceState.putInt("powerups_size",powerups.size());
		for(int i=0;i<powerups.size();i++){
			savedInstanceState.putFloat("powerup_"+i+"_x",powerups.get(i).x);
			savedInstanceState.putFloat("powerup_"+i+"_y",powerups.get(i).y);
			savedInstanceState.putFloat("powerup_"+i+"_move_speed_x",powerups.get(i).move_speed_x);
			savedInstanceState.putFloat("powerup_"+i+"_move_speed_y",powerups.get(i).move_speed_y);
			savedInstanceState.putShort("powerup_"+i+"_type",powerups.get(i).type);
		}
		
		savedInstanceState.putInt("new_balls_size",new_balls.size());
	}
	
	public void load_game(Bundle savedInstanceState,Image image,View view){
		in_progress=true;
		
		balls.clear();
		paddles.clear();
		bricks.clear();
		powerups.clear();
		
		new_balls.clear();
		
		bricks_vertical=savedInstanceState.getInt("bricks_vertical");
		
		lives=savedInstanceState.getInt("lives");
		
		brick_count=savedInstanceState.getInt("brick_count");
		
		int paddles_size=savedInstanceState.getInt("paddles_size");
		for(int i=0;i<paddles_size;i++){
			float x=savedInstanceState.getFloat("paddle_"+i+"_x");
			float y=savedInstanceState.getFloat("paddle_"+i+"_y");
			int color=savedInstanceState.getInt("paddle_"+i+"_color");
			boolean ai=savedInstanceState.getBoolean("paddle_"+i+"_ai");
			float ai_diff=savedInstanceState.getFloat("paddle_"+i+"_ai_difficulty");
			int counter_ai_update=savedInstanceState.getInt("paddle_"+i+"_counter_ai_update");
			int score=savedInstanceState.getInt("paddle_"+i+"_score");
			
			if(i==0){
				y=view.getHeight()-image.object_paddle.get_height();
			}
			
			paddles.add(new Object_Paddle(image,view,x,y,color,ai,ai_diff,counter_ai_update,score));
		}
		
		int balls_size=savedInstanceState.getInt("balls_size");
		for(int i=0;i<balls_size;i++){
			float x=savedInstanceState.getFloat("ball_"+i+"_x");
			float y=savedInstanceState.getFloat("ball_"+i+"_y");
			float speed_x=savedInstanceState.getFloat("ball_"+i+"_move_speed_x");
			float speed_y=savedInstanceState.getFloat("ball_"+i+"_move_speed_y");
			int counter_move=savedInstanceState.getInt("ball_"+i+"_counter_move");
			int noclip=savedInstanceState.getInt("ball_"+i+"_noclip");
			balls.add(new Object_Ball(image,view,x,y,speed_x,speed_y,counter_move,noclip));
		}
		
		int bricks_size=savedInstanceState.getInt("bricks_size");
		for(int i=0;i<bricks_size;i++){
			float x=savedInstanceState.getFloat("brick_"+i+"_x");
			float y=savedInstanceState.getFloat("brick_"+i+"_y");
			int color=savedInstanceState.getInt("brick_"+i+"_color");
			
			bricks.add(new Object_Brick(image,view,x,y,color));
		}
		
		int powerups_size=savedInstanceState.getInt("powerups_size");
		for(int i=0;i<powerups_size;i++){
			float x=savedInstanceState.getFloat("powerup_"+i+"_x");
			float y=savedInstanceState.getFloat("powerup_"+i+"_y");
			float speed_x=savedInstanceState.getFloat("powerup_"+i+"_move_speed_x");
			float speed_y=savedInstanceState.getFloat("powerup_"+i+"_move_speed_y");
			short type=savedInstanceState.getShort("powerup_"+i+"_type");
			powerups.add(new Object_Powerup(image,view,type,x,y,speed_x,speed_y));
		}
		
		int new_balls_size=savedInstanceState.getInt("new_balls_size");
		for(int i=0;i<new_balls_size;i++){
			new_balls.add(true);
		}
	}
	
	public void new_game(Image image,View view,Resources res,Pet_Status pet_status){
		in_progress=true;
		
		balls.clear();
		paddles.clear();
		bricks.clear();
		powerups.clear();
		
		new_balls.clear();
		
		lives=3;
		
		brick_count=0;
		
		paddles.add(new Object_Paddle(image,view,(view.getWidth()-image.object_paddle.get_width())/2,view.getHeight()-image.object_paddle.get_height(),pet_status.color,false,0.0f,0,0));
		
		int bricks_horizontal=view.getWidth()/image.object_brick.get_width();
		bricks_vertical=(view.getHeight()/2)/image.object_brick.get_height();
		
		int[] colors=new int[8];
		colors[0]=res.getColor(R.color.game_bricks_1);
		colors[1]=res.getColor(R.color.game_bricks_2);
		colors[2]=res.getColor(R.color.game_bricks_3);
		colors[3]=res.getColor(R.color.game_bricks_4);
		colors[4]=res.getColor(R.color.game_bricks_5);
		colors[5]=res.getColor(R.color.game_bricks_6);
		colors[6]=res.getColor(R.color.game_bricks_7);
		colors[7]=res.getColor(R.color.game_bricks_8);
		
		int brick_layout=RNG.random_range(0,Templates.bricks.size()-1);
		if(bricks_horizontal>Templates.bricks.get(brick_layout).width){
			bricks_horizontal=Templates.bricks.get(brick_layout).width;
		}
		if(bricks_vertical>Templates.bricks.get(brick_layout).height){
			bricks_vertical=Templates.bricks.get(brick_layout).height;
		}
		
		for(int int_x=0;int_x<bricks_horizontal;int_x++){
			for(int int_y=0,color=0;int_y<bricks_vertical;int_y++,color++){
				if(color>7){
					color=0;
				}
				
				if(Templates.bricks.get(brick_layout).bricks[int_x][int_y]){
					bricks.add(new Object_Brick(image,view,int_x*image.object_brick.get_width(),int_y*image.object_brick.get_height(),colors[color]));
				}
			}
		}
		
		int brick_x_adjust=(view.getWidth()-bricks_horizontal*image.object_brick.get_width())/2;
		
		for(int i=0;i<bricks.size();i++){
			bricks.get(i).x+=brick_x_adjust;
		}
		
		float speed_x=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
		float speed_y=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
		float new_x=(view.getWidth()-image.object_ball.get_width())/2;
		float new_y=bricks_vertical*image.object_brick.get_height()+4;
		
		if(RNG.random_range(0,99)<50){
			speed_x=-speed_x;
		}
		
		balls.add(new Object_Ball(image,view,new_x,new_y,speed_x,speed_y,1*FPS.fps,0));
	}
	
	public void end_game(int winner,Pet_Status status,GameView game_view){
		in_progress=false;
		
		balls.clear();
		paddles.clear();
		bricks.clear();
		powerups.clear();
		
		new_balls.clear();
		
		status.sleeping_wake_up();
		
		if(winner!=-1){
			int sound=-1;
			String message_begin="";
			String message_end="";
			long experience_points=0;
			int bits=0;
			int item_chance=0;
			
			short happy_gain=(short)RNG.random_range(Pet_Status.HAPPY_GAIN_GAME_BRICKS_MIN,Pet_Status.HAPPY_GAIN_GAME_BRICKS_MAX);
			happy_gain+=(short)Math.ceil((double)happy_gain*0.01*(double)brick_count*1.5);
			status.happy+=happy_gain;
			status.happy_bound();
			status.queue_thought(Thought_Type.HAPPY);
						
			experience_points=(long)RNG.random_range((int)Pet_Status.EXPERIENCE_BASE_BRICKS_MIN,(int)Pet_Status.EXPERIENCE_BASE_BRICKS_MAX);
			
			experience_points+=Pet_Status.get_level_bonus_xp(status.level,Pet_Status.EXPERIENCE_BONUS_BRICKS,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_HIGH);
			
			experience_points+=(long)Math.ceil((double)experience_points*0.01*(double)brick_count*1.5);
			
			bits=Age_Tier.scale_bit_gain(status.age_tier,RNG.random_range(Pet_Status.BIT_GAIN_BRICKS_MIN,Pet_Status.BIT_GAIN_BRICKS_MAX));
			
			bits+=(int)Math.ceil((double)bits*0.01*(double)brick_count*1.5);
			
			item_chance=Pet_Status.ITEM_CHANCE_BRICKS;
			
			item_chance+=(int)Math.ceil((double)item_chance*0.01*(double)brick_count*1.5);
			
			message_begin="Done Bricking!\n\n";
			
			//If the player won.
			if(winner==0){
				status.happy+=RNG.random_range(Pet_Status.HAPPY_GAIN_GAME_BRICKS_MIN,Pet_Status.HAPPY_GAIN_GAME_BRICKS_MAX)*2;
				status.happy_bound();
				
				sound=Sound.GAME_WIN;
				
				message_begin+="You won!";
				
				message_end=status.name+" looks very happy!";
			}
			else{
				sound=Sound.GAME_LOSS;
        		
        		message_begin+="You lost!";
        		
        		message_end=status.name+" has looked happier...";
        		
        		experience_points=(long)Math.ceil((double)experience_points*0.85);
        		
        		bits=(int)Math.ceil((double)bits*0.85);
        		
        		item_chance=(int)Math.ceil((double)item_chance*0.85);
			}
			
			Activity_Rewards.give_rewards_handler(game_view.getContext(),handler,game_view.bitbeast.getPackageName(),status,sound,message_begin,message_end,experience_points,bits,true,item_chance,status.level);
		}
		
		game_view.bitbeast.set_game_mode(Game_Mode.PET,null);
	}
	
	public void reset_sprites(Image image,View view){
		for(int i=0;i<balls.size();i++){
			balls.get(i).reset_sprite(image,view);
		}
		
		for(int i=0;i<paddles.size();i++){
			paddles.get(i).reset_sprite(image,view);
		}
		
		for(int i=0;i<bricks.size();i++){
			bricks.get(i).reset_sprite(image,view);
		}
		
		for(int i=0;i<powerups.size();i++){
			powerups.get(i).reset_sprite(image,view);
		}
	}
	
	public void vibrate_reset(Vibrator vibrator){
		if(Options.vibrate){
			vibrator.cancel();
			vibrator.vibrate(VIBRATE_TIME_RESET);
		}
	}
	
	public void input(Resources res){
		if(in_progress){
			for(int i=0;i<paddles.size();i++){
				paddles.get(i).input(res,key_states);
			}
		}
	}
	
	public void ai(){
		if(in_progress){
		}
	}
	
	public void move(Image image,GameView game_view,Resources res,Pet_Status pet_status,Vibrator vibrator){
		if(in_progress){
			for(int i=0;i<balls.size();i++){
				boolean ball_in_play=balls.get(i).move_bricks(this,(View)game_view,image,paddles,bricks,powerups,vibrator);
				
				//If the ball just left the screen.
				if(!ball_in_play){
					Sound_Manager.play_sound(Sound.GAME_RESET_LOSS);
					
					if(--lives<=0){
						end_game(1,pet_status,game_view);
						return;
					}
					
					balls.remove(i);
					i--;
					
					vibrate_reset(vibrator);
				}
			}
			
			for(int i=0;i<powerups.size();i++){
				boolean still_active=powerups.get(i).move_bricks(game_view,this,pet_status,paddles,balls,new_balls,bricks,bricks_vertical,vibrator);
				
				if(!still_active){
					powerups.remove(i);
					i--;
				}
			}
			
			//If there are no more bricks.
			if(bricks.size()==0){
				end_game(0,pet_status,game_view);
				return;
			}
	
			//If all balls were lost.
			if(balls.size()==0){
				Sound_Manager.play_sound(Sound.GAME_RESET_LOSS);
				
				float speed_x=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
				float speed_y=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
				float new_x=(game_view.getWidth()-image.object_ball.get_width())/2;
				float new_y=bricks_vertical*image.object_brick.get_height()+4;
				
				if(RNG.random_range(0,99)<50){
					speed_x=-speed_x;
				}
			
				balls.add(new Object_Ball(image,(View)game_view,new_x,new_y,speed_x,speed_y,1*FPS.fps,0));
			}
			
			for(int i=0;i<paddles.size();i++){
				paddles.get(i).move((View)game_view);
			}
			
			for(int i=0;i<bricks.size();i++){
				bricks.get(i).move((View)game_view);
			}
			
			for(int i=0;i<new_balls.size();i++){
				float speed_x=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
				float speed_y=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
				float new_x=(game_view.getWidth()-image.object_ball.get_width())/2;
				float new_y=bricks_vertical*image.object_brick.get_height()+4;
				
				if(RNG.random_range(0,99)<50){
					speed_x=-speed_x;
				}
				
				balls.add(new Object_Ball(image,(View)game_view,new_x,new_y,speed_x,speed_y,1*FPS.fps,0));
				
				new_balls.remove(i);
				i--;
			}
		}
	}
	
	public void animate(){
		if(in_progress){
			for(int i=0;i<powerups.size();i++){
				powerups.get(i).animate();
			}
		}
	}
	
	public void render(Canvas canvas,Resources res,View view){
		if(in_progress){
			String message="";
			Paint text_paint=new Paint();
			text_paint.setColor(res.getColor(R.color.font));
			text_paint.setTextSize(Px.px(res,24.0f));
			text_paint.setTypeface(Font.font1);
			
			message="Lives: "+lives;
			canvas.drawText(message,(view.getWidth()-text_paint.measureText(message))/2,view.getHeight()/2,text_paint);
			
			for(int i=0;i<powerups.size();i++){
				powerups.get(i).render(canvas,res);
			}
			
			for(int i=0;i<balls.size();i++){
				balls.get(i).render(canvas,res);
			}
			
			for(int i=0;i<paddles.size();i++){
				paddles.get(i).render(canvas,res);
			}
			
			for(int i=0;i<bricks.size();i++){
				bricks.get(i).render(canvas,res);
			}
		}
	}
}
