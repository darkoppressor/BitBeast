/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

class GameView extends SurfaceView implements SurfaceHolder.Callback{
	class GameThread extends Thread{
		//Are we running?
		private boolean running;
		
		//Handle to the surface manager object we interact with
		private SurfaceHolder surface_holder;
		
		long ms_last_run;

		private Handler handler;
		
		private Image image;
		
		private Vibrator vibrator;
		
		public GameThread(SurfaceHolder temp_surface_holder,Handler get_handler,Vibrator get_vibrator){
			running=false;
			
			surface_holder=temp_surface_holder;
			
			ms_last_run=-1L;
						
			handler=get_handler;
			
			image=null;
			
			vibrator=get_vibrator;
			
			timer_time_tick.start();
		}
		
		//The actual game loop
		@Override
		public void run(){
			if(ms_last_run>=0){
				//Determine the ms since the last run.
				long ms_since_last_run=System.currentTimeMillis()-ms_last_run;
				
				//Determine the number of time ticks since the last run.
				long time_ticks=(long)Math.floor((ms_since_last_run/1000)/SECONDS_PER_TIME_TICK);
	            
    			if(!Options.pause && time_ticks>0){
    				pet.process_time_tick(time_ticks,image,(View)GameView.this,bitbeast,handler);
    			}
				
				ms_last_run=-1L;
			}
			
		    //In our logic update while() loop, we consume SKIP_TICKS sized chunks of time, which are added to next_game_tick.
		    long next_game_tick=SystemClock.uptimeMillis();

		    //The number of logic updates that have occurred since the last render.
		    long number_of_updates=0;

		    //Declare and start the frame rate timer. This keeps track of the frame rate, as well as milliseconds spent per frame.
		    //See the explanation at the beginning of the game loop for more details.
		    Timer_Cheesy timer_frame_rate=new Timer_Cheesy();
		    timer_frame_rate.start();

		    //The following three variables are used for displaying the FPS to the player.
		    //This integer keeps track of the number of frame each second. Every time a second has passed, frame_count's value is given to frame_rate, and frame_count is reset to 0.
		    int frame_count=0;

		    //At any given time (after the first second of runtime), frame_rate represents the number of frames that were rendered in the past second.
		    int frame_rate=0;

		    //For added performance information, ms_per_frame takes the FPS value each second and converts it to the number of milliseconds spent on each frame in the past second.
		    double ms_per_frame=0;

		    //Keeps track of the logic frames each second.
		    int logic_frame_count=0;
		    //At any given time (after the first second of runtime), logic_frame_rate represents the number of logic frames that were processed in the past second.
		    int logic_frame_rate=0;

		    //Here we have the game loop.
		    while(running){
		    	if(surface_holder.getSurface().isValid()){
			    	Canvas c=null;
			    	
			        //If at least a second(preferably exactly one second) has passed since the last restart of the frame rate timer, we do several things.
			        //First, we set the frame rate to the current frame count, which has been counting up since the last timer restart.
			        //Second, we reset the frame count to 0, to count the number of frames successfully completed in the next second.
			        //Third, we set the milliseconds per frame to 1000/our current frame rate. Since our frame rate is in seconds, this gives us the number of milliseconds being spent on
			        //each frame. Finally, we restart the frame rate timer.
			        if(timer_frame_rate.get_ticks()>=1000){
			            logic_frame_rate=logic_frame_count;
			            logic_frame_count=0;
	
			            frame_rate=frame_count;
			            frame_count=0;
			            ms_per_frame=1000.0/frame_rate;
			            timer_frame_rate.start();
			        }
	
			        //Here we are just incrementing the frame counter, which gives us the frames per second in the above if() statement.
			        frame_count++;
	
			        //For our game loop, we first update game logic, and then render. The two are kept separate and independent.
	
			        //First we reset our updates counter to 0.
			        number_of_updates=0;
	
			        try{
						c=surface_holder.lockCanvas();
						synchronized(surface_holder){
							//We consume SKIP_TICKS sized chunks of time, which ultimately translates to updating the logic UPDATE_LIMIT times a second.
					        //We also check to see if we've updated logic MAX_FRAMESKIP times without rendering, at which point we render.
					        while(SystemClock.uptimeMillis()>next_game_tick && number_of_updates<MAX_FRAMESKIP){
					            //We are doing another game logic update.
					            //If this reaches MAX_FRAMESKIP, we will render.
					            number_of_updates++;
	
					            //Increment the logic frame counter.
					            logic_frame_count++;
	
					            //Clamp the time step to something reasonable.
					            if(Math.abs(SystemClock.uptimeMillis()-next_game_tick)>FPS.SKIP_TICKS*2){
					                next_game_tick=SystemClock.uptimeMillis()-FPS.SKIP_TICKS*2;
					            }
	
					            //Consume another SKIP_TICKS sized chunk of time.
					            next_game_tick+=FPS.SKIP_TICKS;
	
					            //Now we update the game logic:
	
					            tick();
					            input();
					            ai();
					            movement();
								animation();
					        }
					        
					        if(c!=null){
					        	render(c,frame_rate,ms_per_frame,logic_frame_rate);
					        }
						}
					}
			        catch(Exception e){
			        	StorageManager.error_log_add(bitbeast,"GameView","GameThread crashed while running.",e);
			        }
					finally{
						if(c!=null){
							surface_holder.unlockCanvasAndPost(c);
						}
					}
		    	}
		    }
		}
		
		private void tick(){
			if(timer_time_tick.get_ticks()>=SECONDS_PER_TIME_TICK*1000){
				timer_time_tick.start();
				
				if(!Options.pause){
					pet.process_time_tick(1L,image,(View)GameView.this,bitbeast,handler);
				}
			}
			
			//Determine the ms since the last run.
			long ms_since_last_tick=System.currentTimeMillis()-pet.get_status().last_tick;
			
			//Determine the number of ticks since the last run.
			long ticks=(long)Math.floor((ms_since_last_tick/1000)/SECONDS_PER_TICK);
			
			//Don't allow more ticks than the max.
			if(ticks>MAX_TICKS_PER_UPDATE){
				ticks=MAX_TICKS_PER_UPDATE;
			}
			
			if(ticks>0){
				//Show the progress dialog.
				Message msg=handler.obtainMessage();
				msg.what=BitBeast.HANDLER_SHOW_PROGRESS;
				handler.sendMessage(msg);
				
        		if(!Options.pause){
        			pet.process_tick(ticks,image,(View)GameView.this,bitbeast,records,handler);
        		}
        		
        		pet.get_status().last_tick=System.currentTimeMillis();
        	}

			//Dismiss the progress dialog.
			Message msg=handler.obtainMessage();
			msg.what=BitBeast.HANDLER_HIDE_PROGRESS;
			handler.sendMessage(msg);
		}
		
		private void input(){
			if(game_mode==Game_Mode.WORKOUT){
				game_workout.input(getResources());
			}
			else if(game_mode==Game_Mode.BRICKS){
				game_bricks.input(getResources());
			}
		}
		
		private void ai(){
			if(game_mode==Game_Mode.PET){
				if(!Options.pause){
					pet.ai(image,(View)GameView.this,vibrator);
				}
			}
			else if(game_mode==Game_Mode.WORKOUT){
				game_workout.ai((View)GameView.this);
			}
			else if(game_mode==Game_Mode.BRICKS){
				game_bricks.ai();
			}
		}
		
		private void movement(){
			if(game_mode==Game_Mode.PET){
				if(!Options.pause){
					if(want_speech && !speech_listening){
						Message msg=game_thread.handler.obtainMessage();
						msg.what=BitBeast.HANDLER_SPEECH_RECOGNITION;
						game_thread.handler.sendMessage(msg);
					}
					else if(!want_speech && speech_listening){
						Message msg=game_thread.handler.obtainMessage();
						msg.what=BitBeast.HANDLER_SPEECH_RECOGNITION;
						game_thread.handler.sendMessage(msg);
					}
					
					pet.move((View)GameView.this,vibrator);
				}
			}
			else if(game_mode==Game_Mode.WORKOUT){
				game_workout.move(image,GameView.this,getResources(),pet.get_status(),vibrator);
			}
			else if(game_mode==Game_Mode.BRICKS){
				game_bricks.move(image,GameView.this,getResources(),pet.get_status(),vibrator);
			}
		}
		
		private void animation(){
			if(game_mode==Game_Mode.PET){
				if(!Options.pause){
					pet.animate(vibrator,image,(View)GameView.this);
				}
			}
			else if(game_mode==Game_Mode.WORKOUT){
				game_workout.animate();
			}
			else if(game_mode==Game_Mode.BRICKS){
				game_bricks.animate();
			}
		}
		
		//Draws to the provided Canvas.
		private void render(Canvas canvas,int frame_rate,double ms_per_frame,int logic_frame_rate){
			Resources res=getResources();
			String message="";
			float font_size=0.0f;
			
			if(image!=null && (image.screen_w!=getWidth() || image.screen_h!=getHeight())){
				reset_sprites(image);
			}
			
			Rect rect=new Rect(0,0,canvas.getWidth(),canvas.getHeight());
			
			int background_color=res.getColor(R.color.button_1);
			if(!pet.get_status().light){
				background_color=Color.BLACK;
			}
			
			canvas.drawColor(background_color);
			
			if(pet.get_status().light){
				canvas.drawBitmap(image.background_blueprint,null,rect,null);
			}
			
			if(game_mode==Game_Mode.PET){
				if(!Options.pause){
					pet.render_before_dark(canvas,res);
				}
			}
			else if(game_mode==Game_Mode.WORKOUT){
				game_workout.render(canvas,res,(View)GameView.this,image);
			}
			else if(game_mode==Game_Mode.BRICKS){
				game_bricks.render(canvas,res,(View)GameView.this);
			}
			
			if(!pet.get_status().light){
				int dark_color=Color.argb(191,0,0,0);
				canvas.drawColor(dark_color);
			}
			
			if(game_mode==Game_Mode.PET){
				if(!Options.pause){
					pet.render_after_dark(canvas,res);
				}
			}
			
			Paint text_paint=new Paint();
			font_size=Px.px(res,48.0f);
			text_paint.setColor(res.getColor(R.color.font));
			text_paint.setTextSize(font_size);
			text_paint.setTypeface(Font.font1);
			
			if(Options.pause){
				message="Paused";
				canvas.drawText(message,(getWidth()-text_paint.measureText(message))/2,font_size,text_paint);
			}
			
			if(speech_listening){
				font_size=Px.px(res,28.0f);
				text_paint.setTextSize(font_size);
				message=pet.get_status().name+" is listening...";
				RectF back_rect=new RectF(0,0,canvas.getWidth(),font_size*1.75f);
				Paint back_paint=new Paint();
				back_paint.setColor(res.getColor(R.color.button_1));
				back_paint.setAlpha(191);
				canvas.drawRoundRect(back_rect,12.0f,12.0f,back_paint);
				canvas.drawText(message,(getWidth()-text_paint.measureText(message))/2,font_size,text_paint);
			}
			
			//Display FPS.
			/**font_size=Px.px(res,48.0f);
			text_paint.setTextSize(font_size);
			message="FPS: "+frame_rate+" / MSPF: "+(float)ms_per_frame+" / LUPS: "+logic_frame_rate;
			canvas.drawText(message,(getWidth()-text_paint.measureText(message))/2,getHeight()/2,text_paint);*/
		}
		
		//So we can stop/pause the game loop
		public synchronized void set_running(boolean b){
			running=b;
		}
		
		public synchronized Handler get_handler(){
			return handler;
		}
	}
	
	//The maximum number of frames to be skipped.
    final static long MAX_FRAMESKIP=5;
	
    //The maximum number of ticks that can be processed when updating the pet.
    static final long MAX_TICKS_PER_UPDATE=7500;
    static final int SECONDS_PER_TIME_TICK=1;
    static final int SECONDS_PER_TICK=900;
    
    //The number of milliseconds a touch event must last before it is counted as a move and not a tap.
    private static final long TAP_THRESHOLD=100;
    
	private Timer_Cheesy timer_time_tick;
	
	private Pet pet;
	private Records records;
	private int game_mode;
	private Game_Workout game_workout;
	private Game_Bricks game_bricks;
	
	int new_game_mode;
	Bundle game_mode_bundle;
	
	BitBeast bitbeast;
	
	//If true, we are currently listening for speech.
	boolean speech_listening;
	//If true, we currently want to listen for speech.
	boolean want_speech;

	//The thread that actually draws the animation
	private GameThread game_thread;
	
	private SurfaceHolder surface_holder;
	
	public GameView(Context context,AttributeSet attrs){
		super(context,attrs);
		
		//Register our interest in hearing about changes to our surface
		surface_holder=getHolder();
		surface_holder.addCallback(this);
		
		timer_time_tick=new Timer_Cheesy();
		
		pet=new Pet();
		records=new Records();
		game_mode=Game_Mode.PET;
		game_workout=new Game_Workout();
		game_bricks=new Game_Bricks(context,getResources(),pet.get_status());
		
		new_game_mode=Game_Mode.NONE;
		game_mode_bundle=null;
		
		bitbeast=null;
		
		speech_listening=false;
		want_speech=false;
	}
	
	public void set_speech_listening(boolean get_speech_listening){
		speech_listening=get_speech_listening;
	}
	
	public void set_want_speech(boolean get_want_speech){
		want_speech=get_want_speech;
	}
	
	public int get_game_mode(){
		return game_mode;
	}
	
	public void resume(Handler get_handler,Image get_image,Vibrator get_vibrator,long get_ms_last_run){
		game_workout.set_handler(get_handler);
		game_bricks.set_handler(get_handler);
		
		game_thread=new GameThread(surface_holder,get_handler,get_vibrator);
		game_thread.ms_last_run=get_ms_last_run;
		game_thread.image=get_image;
		game_thread.set_running(true);
		game_thread.start();
	}
	
	public void pause(){
		if(game_thread!=null){
			//We have to tell thread to shut down and wait for it to finish,
			//or else it might touch the Surface after we return and explode.
			boolean retry=true;
			game_thread.set_running(false);
			while(retry){
				try{
					game_thread.join();
					retry=false;
				}
				catch(InterruptedException e){
				}
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		boolean used=false;
		
		if(game_mode==Game_Mode.WORKOUT){
			used=game_workout.on_key_down(keyCode);
		}
		else if(game_mode==Game_Mode.BRICKS){
			used=game_bricks.on_key_down(keyCode);
		}
		
		if(!used){
			return super.onKeyDown(keyCode,event);
		}
		else{
			return true;
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode,KeyEvent event){
		boolean used=false;
		
		if(game_mode==Game_Mode.WORKOUT){
			used=game_workout.on_key_up(keyCode);
		}
		else if(game_mode==Game_Mode.BRICKS){
			used=game_bricks.on_key_up(keyCode);
		}
		
		if(!used){
			return super.onKeyUp(keyCode,event);
		}
		else{
			return true;
		}
	}
	
	@Override
	public boolean onTrackballEvent(MotionEvent event){
		boolean used=false;
		
		if(game_mode==Game_Mode.WORKOUT){
			used=game_workout.on_trackball_event(getResources(),event);
		}
		else if(game_mode==Game_Mode.BRICKS){
			used=game_bricks.on_trackball_event(getResources(),event);
		}
		
		if(!used){
			return super.onTrackballEvent(event);
		}
		else{
			return true;
		}
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event){
		Resources res=getResources();
		
		boolean used=false;
		
		if(game_mode==Game_Mode.PET && !Options.pause){
			used=pet.get_status().on_touch_event(event,res);
			
			if(pet.get_status().age_tier!=Age_Tier.EGG){
				if(!used && pet.get_status().age_tier!=Age_Tier.DEAD){
					float x=event.getX();
					float y=event.getY();
					
					Coords pet_mid=null;
					Coords start_pet=null;
					Coords end_pet=null;
					RectF pet_rect=null;
					
					switch(event.getActionMasked()){
					case MotionEvent.ACTION_DOWN:
						pet.start_pet_action(x,y);
						
						used=true;
						break;
					case MotionEvent.ACTION_UP:
						if(Math.abs(event.getEventTime()-event.getDownTime())<TAP_THRESHOLD){
							want_speech=!want_speech;
							
							if(want_speech){
								Sound_Manager.play_sound(Sound.SPEECH_START);
							}
							else{
								Sound_Manager.play_sound(Sound.SPEECH_STOP);
							}
						}
						else{
							pet_mid=pet.get_midpoint();
							pet_rect=pet.get_rect();
							start_pet=pet.get_pet_start_action();
							
							if(start_pet.x>0.0f && start_pet.y>0.0f){
								end_pet=new Coords();
								end_pet.x=x;
								end_pet.y=y;
								if(start_pet.x>end_pet.x){
									float temp=start_pet.x;
									start_pet.x=end_pet.x;
									end_pet.x=temp;
								}
								if(start_pet.y>end_pet.y){
									float temp=start_pet.y;
									start_pet.y=end_pet.y;
									end_pet.y=temp;
								}
							
								if((pet_mid.x>=start_pet.x && pet_mid.x<=end_pet.x &&
										pet_mid.y>=start_pet.y && pet_mid.y<=end_pet.y) ||
										Collision.check(end_pet.x,end_pet.y,Px.px(res,2),Px.px(res,2),pet_rect.left,pet_rect.top,pet_rect.right-pet_rect.left,pet_rect.bottom-pet_rect.top)){
									pet.receive_petting();
								}
								
								pet.start_pet_action(-1,-1);
							}
						}
						
						used=true;
						break;
					}
				}
			}
			else if(pet.get_status().age_tier==Age_Tier.EGG){
				if(!used){
					float x=event.getX();
					float y=event.getY();
					
					switch(event.getActionMasked()){
					case MotionEvent.ACTION_DOWN:
						used=true;
						break;
					case MotionEvent.ACTION_UP:
						if(Collision.check(x,y,Px.px(res,2),Px.px(res,2),pet.x,pet.y,pet.w,pet.h)){
							pet.evolve(game_thread.image,(View)this,bitbeast,true,game_thread.get_handler());
							
							used=true;
						}
						break;
					}
				}
			}
		}
		else if(game_mode==Game_Mode.WORKOUT){
			used=game_workout.on_touch_event(event);
		}
		else if(game_mode==Game_Mode.BRICKS){
			used=game_bricks.on_touch_event(event);
		}
    	
		if(!used){
			return super.onTouchEvent(event);
		}
		else{
			return true;
		}
    }
	@Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
    	super.onSizeChanged(w,h,oldw,oldh);
    	
    	if(new_game_mode!=Game_Mode.NONE){
    		boolean bundle_is_null=game_mode_bundle.getBoolean("null");
    		
    		if(new_game_mode==Game_Mode.WORKOUT){
	    		get_game_workout().new_game(game_thread.image,(View)GameView.this,getResources(),pet.get_status());
	    		
	    		if(!bundle_is_null){
		        	game_workout.load_game(game_mode_bundle,game_thread.image,(View)GameView.this);
	    		}
	    	}
    		else if(new_game_mode==Game_Mode.BRICKS){
	    		get_game_bricks().new_game(game_thread.image,(View)GameView.this,getResources(),pet.get_status());
	    		
	    		if(!bundle_is_null){
		        	game_bricks.load_game(game_mode_bundle,game_thread.image,(View)GameView.this);
	    		}
	    	}
	    	
	    	game_mode=new_game_mode;
	    	
	    	new_game_mode=Game_Mode.NONE;
	    	game_mode_bundle=null;
    	}
    }
	
	public void reset_sprites(Image image){
		pet.reset_sprite(image,(View)GameView.this,getResources());
		pet.get_status().reset_poop_sprites(image,(View)GameView.this);
		pet.get_status().reset_perma_item_sprites(image,(View)GameView.this);
		pet.reset_overlay_sprites(image,(View)GameView.this);
		pet.reset_thought_sprites(image,(View)GameView.this);
		pet.reset_evolution_sprites(image,(View)GameView.this);
		game_workout.reset_sprites(image,(View)GameView.this);
		game_bricks.reset_sprites(image,(View)GameView.this);
	}
	
	public void reset_pet(){
		pet=new Pet();
	}
	
	public void reset_records(){
		records=new Records();
	}
	
	public Pet get_pet(){
		return pet;
	}
	
	public Records get_records(){
		return records;
	}
	
	public Game_Workout get_game_workout(){
		return game_workout;
	}
	
	public Game_Bricks get_game_bricks(){
		return game_bricks;
	}
	
	//Obligatory method that belongs to the implements SurfaceHolder.Callback
	
	//Callback invoked when the surface dimensions change.
	public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
	}
	
	//Callback invoked when the Surface has been created and is ready to use
	public void surfaceCreated(SurfaceHolder holder){
	}
	
	//Callback invoked when the Surface has been destroyed and must no longer be touched.
	//WARNING: after this method returns, the Surface/Canvas must never be touched again!
	public void surfaceDestroyed(SurfaceHolder holder){
	}
}
