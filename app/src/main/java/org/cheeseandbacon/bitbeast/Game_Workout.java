/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Game_Workout{
	ArrayList<Object_Ball> balls;
	ArrayList<Object_Paddle> paddles;
	ArrayList<Object_Powerup> powerups;
	ArrayList<Object_Powerup_Spawner> powerup_spawner;
	
	//Used to keep track of balls about to be spawned.
	//This is pretty hacky but oh well.
	//The value is actually meaningless.
	ArrayList<Boolean> new_balls;
	
	Key_States key_states;
	
	boolean in_progress;
	
	private Handler handler;
	
	static final int score_goal=3;
	static final long VIBRATE_TIME_RESET=500;
	
	public Game_Workout(){
		balls=new ArrayList<Object_Ball>();
		paddles=new ArrayList<Object_Paddle>();
		powerups=new ArrayList<Object_Powerup>();
		powerup_spawner=new ArrayList<Object_Powerup_Spawner>();
		
		new_balls=new ArrayList<Boolean>();
		
		key_states=new Key_States();
		
		in_progress=false;
		
		handler=null;
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
		
		savedInstanceState.putInt("powerups_size",powerups.size());
		for(int i=0;i<powerups.size();i++){
			savedInstanceState.putFloat("powerup_"+i+"_x",powerups.get(i).x);
			savedInstanceState.putFloat("powerup_"+i+"_y",powerups.get(i).y);
			savedInstanceState.putFloat("powerup_"+i+"_move_speed_x",powerups.get(i).move_speed_x);
			savedInstanceState.putFloat("powerup_"+i+"_move_speed_y",powerups.get(i).move_speed_y);
			savedInstanceState.putShort("powerup_"+i+"_type",powerups.get(i).type);
		}
		
		savedInstanceState.putInt("powerup_spawner_size",powerup_spawner.size());
		for(int i=0;i<powerup_spawner.size();i++){
			savedInstanceState.putFloat("powerup_spawner_"+i+"_x",powerup_spawner.get(i).x);
			savedInstanceState.putFloat("powerup_spawner_"+i+"_y",powerup_spawner.get(i).y);
			savedInstanceState.putFloat("powerup_spawner_"+i+"_move_speed_x",powerup_spawner.get(i).move_speed_x);
		}
		
		savedInstanceState.putInt("new_balls_size",new_balls.size());
	}
	
	public void load_game(Bundle savedInstanceState,Image image,View view){
		in_progress=true;
		
		balls.clear();
		paddles.clear();
		powerups.clear();
		powerup_spawner.clear();
		
		new_balls.clear();
		
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
		
		int powerups_size=savedInstanceState.getInt("powerups_size");
		for(int i=0;i<powerups_size;i++){
			float x=savedInstanceState.getFloat("powerup_"+i+"_x");
			float y=savedInstanceState.getFloat("powerup_"+i+"_y");
			float speed_x=savedInstanceState.getFloat("powerup_"+i+"_move_speed_x");
			float speed_y=savedInstanceState.getFloat("powerup_"+i+"_move_speed_y");
			short type=savedInstanceState.getShort("powerup_"+i+"_type");
			powerups.add(new Object_Powerup(image,view,type,x,y,speed_x,speed_y));
		}
		
		int powerup_spawner_size=savedInstanceState.getInt("powerup_spawner_size");
		for(int i=0;i<powerup_spawner_size;i++){
			float x=savedInstanceState.getFloat("powerup_spawner_"+i+"_x");
			float y=savedInstanceState.getFloat("powerup_spawner_"+i+"_y");
			float speed_x=savedInstanceState.getFloat("powerup_spawner_"+i+"_move_speed_x");
			powerup_spawner.add(new Object_Powerup_Spawner(image,view,x,y,speed_x));
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
		powerups.clear();
		powerup_spawner.clear();
		
		new_balls.clear();
		
		float powerup_spawner_x=(view.getWidth()-image.object_powerup_spawner_on.get_width())/2;
		float powerup_spawner_y=(view.getHeight()-image.object_powerup_spawner_on.get_height())/2;
		float powerup_spawner_speed_x=Px.px(res,(float)RNG.random_range(Object_Powerup_Spawner.SPEED_MIN,Object_Powerup_Spawner.SPEED_MAX));
		
		powerup_spawner.add(new Object_Powerup_Spawner(image,view,powerup_spawner_x,powerup_spawner_y,powerup_spawner_speed_x));
		
		float ai_diff=0.0f;
		int random=RNG.random_range(0,99);
		if(random>=0 && random<30){
			ai_diff=Object_Paddle.AI_DIFF_EASY;
		}
		else if(random>=30 && random<90){
			ai_diff=Object_Paddle.AI_DIFF_NORMAL;
		}
		else if(random>=90 && random<100){
			ai_diff=Object_Paddle.AI_DIFF_HARD;
		}
		
		paddles.add(new Object_Paddle(image,view,(view.getWidth()-image.object_paddle.get_width())/2,view.getHeight()-image.object_paddle.get_height(),pet_status.color,false,0.0f,0,0));
		paddles.add(new Object_Paddle(image,view,(view.getWidth()-image.object_paddle.get_width())/2,0,res.getColor(R.color.game_white),true,ai_diff,0,0));
		
		float speed_x=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
		float speed_y=-(Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX)));
		float new_x=(view.getWidth()-image.object_ball.get_width())/2;
		float new_y=(view.getHeight()-image.object_ball.get_height())/2;
		
		if(RNG.random_range(0,99)<50){
			speed_x=-speed_x;
		}
		if(RNG.random_range(0,99)<50){
			speed_y=-speed_y;
		}
		
		balls.add(new Object_Ball(image,view,new_x,new_y,speed_x,speed_y,1*FPS.fps,0));
	}
	
	public void end_game(int winner,Pet_Status status,GameView game_view){
		in_progress=false;
		
		float ai_difficulty=-1.0f;
		if(winner!=-1){
			ai_difficulty=paddles.get(1).ai_difficulty;
		}
		
		balls.clear();
		paddles.clear();
		powerups.clear();
		powerup_spawner.clear();
		
		new_balls.clear();
		
		status.sleeping_wake_up();
		
		if(winner!=-1){
			DecimalFormat df=new DecimalFormat("#.##");
			
			int sound=-1;
			String message_begin="";
			String message_end="";
			long experience_points=0;
			int bits=0;
			int item_chance=0;
			
			double weight_loss=status.get_weight();
			status.gain_weight(-(0.01*RNG.random_range(Pet_Status.WEIGHT_LOSS_WORKOUT_MIN,Pet_Status.WEIGHT_LOSS_WORKOUT_MAX)));
			weight_loss-=status.get_weight();
			
			status.energy-=Pet_Status.ENERGY_LOSS_WORKOUT;
			status.energy_bound();
						
			experience_points=(long)RNG.random_range((int)Pet_Status.EXPERIENCE_BASE_WORKOUT_MIN,(int)Pet_Status.EXPERIENCE_BASE_WORKOUT_MAX);
			
			experience_points+=Pet_Status.get_level_bonus_xp(status.level,Pet_Status.EXPERIENCE_BONUS_WORKOUT,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_HIGH);
			
			bits=Age_Tier.scale_bit_gain(status.age_tier,RNG.random_range(Pet_Status.BIT_GAIN_WORKOUT_MIN,Pet_Status.BIT_GAIN_WORKOUT_MAX));
			
			item_chance=Pet_Status.ITEM_CHANCE_WORKOUT;
			
			if(ai_difficulty==Object_Paddle.AI_DIFF_EASY){
				experience_points=(long)Math.ceil((double)experience_points*Pet_Status.EXPERIENCE_SCALE_WORKOUT_EASY);
				
				bits=(int)Math.ceil((double)bits*Pet_Status.BIT_SCALE_WORKOUT_EASY);
				
				item_chance+=Pet_Status.ITEM_CHANCE_BONUS_WORKOUT_EASY;
			}
			else if(ai_difficulty==Object_Paddle.AI_DIFF_NORMAL){
				experience_points=(long)Math.ceil((double)experience_points*Pet_Status.EXPERIENCE_SCALE_WORKOUT_NORMAL);
				
				bits=(int)Math.ceil((double)bits*Pet_Status.BIT_SCALE_WORKOUT_NORMAL);
				
				item_chance+=Pet_Status.ITEM_CHANCE_BONUS_WORKOUT_NORMAL;
			}
			else if(ai_difficulty==Object_Paddle.AI_DIFF_HARD){
				experience_points=(long)Math.ceil((double)experience_points*Pet_Status.EXPERIENCE_SCALE_WORKOUT_HARD);
				
				bits=(int)Math.ceil((double)bits*Pet_Status.BIT_SCALE_WORKOUT_HARD);
				
				item_chance+=Pet_Status.ITEM_CHANCE_BONUS_WORKOUT_HARD;
			}
			
			message_begin="Done Training!\n\n";
			
			//If the player won.
			if(winner==0){
				sound=Sound.GAME_WIN;
				
				message_begin+="You won!";
			}
        	else{
        		sound=Sound.GAME_LOSS;
        		
        		message_begin+="You lost!";
        		
        		experience_points=(long)Math.ceil((double)experience_points/2.0);
        		
        		bits=(int)Math.ceil((float)bits/2.0f);
        		
        		item_chance=(int)Math.ceil((float)item_chance/2.0f);
        	}
			
			if(!UnitConverter.is_weight_basically_zero(weight_loss,df)){
				message_end=status.name+" lost "+UnitConverter.get_weight_string(weight_loss,df)+".";
			}
			
			Activity_Rewards.give_rewards_handler(game_view.getContext(),handler,game_view.bitbeast.getPackageName(),status,sound,message_begin,message_end,experience_points,bits,true,item_chance,status.level);
		}
		
		game_view.bitbeast.set_game_mode(Game_Mode.PET,null);
	}
	
	public boolean check_for_winner(GameView game_view,Pet_Status pet_status){
		int winner=get_winner();
		
		if(winner!=-1){
			end_game(winner,pet_status,game_view);
			return true;
		}
		else{
			return false;
		}
	}
	
	//Returns the index of the winner, if there is one.
	//Otherwise, returns -1;
	public int get_winner(){
		for(int i=0;i<paddles.size();i++){
			if(paddles.get(i).score>=score_goal){
				return i;
			}
		}
		
		return -1;
	}
	
	public void reset_sprites(Image image,View view){
		for(int i=0;i<balls.size();i++){
			balls.get(i).reset_sprite(image,view);
		}
		
		for(int i=0;i<powerups.size();i++){
			powerups.get(i).reset_sprite(image,view);
		}
		
		for(int i=0;i<powerup_spawner.size();i++){
			powerup_spawner.get(i).reset_sprite(image,view);
		}
		
		for(int i=0;i<paddles.size();i++){
			paddles.get(i).reset_sprite(image,view);
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
	
	public void ai(View view){
		if(in_progress){
			for(int i=0;i<paddles.size();i++){
				paddles.get(i).ai(view,balls,powerups);
			}
		}
	}
	
	public void move(Image image,GameView game_view,Resources res,Pet_Status pet_status,Vibrator vibrator){
		if(in_progress){
			ArrayList<Integer> scorers=new ArrayList<Integer>();
			
			for(int i=0;i<balls.size();i++){
				if(balls.get(i).counter_move==0 && powerup_spawner.get(0).counter_reset==0){
					if(Collision.check(powerup_spawner.get(0).x,powerup_spawner.get(0).y,powerup_spawner.get(0).w,powerup_spawner.get(0).h,balls.get(i).x,balls.get(i).y,balls.get(i).w,balls.get(i).h)){
						powerup_spawner.get(0).counter_reset=(int)(Object_Powerup_Spawner.POWERUP_SPAWN_TIME*FPS.fps);
						
						short type=(short)RNG.random_range(Object_Powerup.POWER_BEGIN,Object_Powerup.POWER_END-1);
						
						float speed_x=Px.px(res,(float)RNG.random_range(Object_Powerup.SPEED_MIN,Object_Powerup.SPEED_MAX));
						float speed_y=Px.px(res,(float)RNG.random_range(Object_Powerup.SPEED_MIN,Object_Powerup.SPEED_MAX));
						if(RNG.random_range(0,99)<50){
							speed_x*=-1.0f;
						}
						if(RNG.random_range(0,99)<20){
							speed_y*=-1.0f;
						}
						float new_x=powerup_spawner.get(0).x;
						float new_y=powerup_spawner.get(0).y;
						
						Sound_Manager.play_sound(Sound.POWERUP_SPAWN);
						
						powerups.add(new Object_Powerup(image,game_view,type,new_x,new_y,speed_x,speed_y));
					}
				}
				
				int scorer=balls.get(i).move_workout((View)game_view,paddles,vibrator);
				
				//If someone just scored.
				if(scorer>=0){
					scorers.add(Integer.valueOf(scorer));
					
					balls.remove(i);
					i--;
					
					vibrate_reset(vibrator);
				}
			}
			
			for(int i=0;i<powerups.size();i++){
				boolean still_active=powerups.get(i).move_workout(game_view,this,pet_status,paddles,balls,new_balls,vibrator);
				
				if(!still_active){
					powerups.remove(i);
					i--;
				}
			}
			
			for(int i=0;i<powerup_spawner.size();i++){
				powerup_spawner.get(i).move_workout((View)game_view,res);
			}
			
			//Add all of the scores to their scorers.
			for(int i=0;i<scorers.size();i++){
				paddles.get(scorers.get(i)).add_score(1);
				
				if(!check_for_winner(game_view,pet_status)){
					//If the CPU was the scorer.
					if(scorers.get(i)==1){
						Sound_Manager.play_sound(Sound.GAME_RESET_LOSS);
					}
					else{
						Sound_Manager.play_sound(Sound.GAME_RESET_WIN);
					}
					
					if(balls.size()==0){
						float speed_x=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
						float speed_y=-(Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX)));
						float new_x=(game_view.getWidth()-image.object_ball.get_width())/2;
						float new_y=(game_view.getHeight()-image.object_ball.get_height())/2;
						
						if(RNG.random_range(0,99)<50){
							speed_x=-speed_x;
						}
						
						//If the CPU was the scorer.
						if(scorers.get(i)==1){
							speed_y=-speed_y;
						}
					
						balls.add(new Object_Ball(image,(View)game_view,new_x,new_y,speed_x,speed_y,1*FPS.fps,0));
					}
				}
			}
			
			for(int i=0;i<paddles.size();i++){
				paddles.get(i).move((View)game_view);
			}
			
			for(int i=0;i<new_balls.size();i++){
				float speed_x=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
				float speed_y=Px.px(res,(float)RNG.random_range(Object_Ball.SPEED_MIN,Object_Ball.SPEED_MAX));
				float new_x=(game_view.getWidth()-image.object_ball.get_width())/2;
				float new_y=(game_view.getHeight()-image.object_ball.get_height())/2;
				
				if(RNG.random_range(0,99)<50){
					speed_x=-speed_x;
				}
				if(RNG.random_range(0,99)<50){
					speed_y=-speed_y;
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
			
			for(int i=0;i<powerup_spawner.size();i++){
				powerup_spawner.get(i).animate();
			}
		}
	}
	
	public void render(Canvas canvas,Resources res,View view,Image image){
		if(in_progress){
			String message="";
			Paint text_paint=new Paint();
			text_paint.setColor(res.getColor(R.color.font));
			text_paint.setTextSize(Px.px(res,24.0f));
			text_paint.setTypeface(Font.font1);
			
			if(paddles.size()>1){
				message="You: "+paddles.get(0).score;
				canvas.drawText(message,0,view.getHeight()/2,text_paint);
				
				message="CPU: "+paddles.get(1).score;
				canvas.drawText(message,view.getWidth()-text_paint.measureText(message),view.getHeight()/2,text_paint);
			}
			
			for(int i=0;i<powerup_spawner.size();i++){
				powerup_spawner.get(i).render(canvas,res);
			}
			
			for(int i=0;i<powerups.size();i++){
				powerups.get(i).render(canvas,res);
			}
			
			for(int i=0;i<balls.size();i++){
				balls.get(i).render(canvas,res);
			}
			
			for(int i=0;i<paddles.size();i++){
				paddles.get(i).render(canvas,res);
			}
		}
	}
}
