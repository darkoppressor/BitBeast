/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Activity_Game_Accel extends AppCompatActivity implements SensorEventListener{
	private SensorManager sensors;
	private Sensor accelerometer;
	
	private boolean initialized;
	
	private float last_x;
	private float last_y;
	private float last_z;
	private static final float NOISE=2.0f;
	
	private Timer_Cheesy timer_update;
	//The cumulative delta number since the last update.
	private float delta;
	//The number of deltas added since the last update.
	private int delta_count;
	
	private float last_accel;
	//The last non-zero jerk. Used for display only.
	private float last_jerk;
	
	int bits_this_session;
	double weight_loss_this_session;
	
	private Pet_Status pet_status;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_accel);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        initialized=false;
        
        last_x=0.0f;
        last_y=0.0f;
        last_z=0.0f;
        
        timer_update=new Timer_Cheesy();
        delta=0.0f;
        delta_count=0;
        
        last_accel=0.0f;
        last_jerk=0.0f;
        
        bits_this_session=0;
        weight_loss_this_session=0.0;
        
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.text_game_accel_bits_this_session));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.text_game_accel_weight_loss_this_session));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.text_game_accel_jerk));
        
        if(savedInstanceState!=null){
        	bits_this_session=savedInstanceState.getInt("bits_this_session");
        	weight_loss_this_session=savedInstanceState.getDouble("weight_loss_this_session");
        	last_jerk=savedInstanceState.getFloat("last_jerk");
        }
    }
	@Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putInt("bits_this_session",bits_this_session);
		savedInstanceState.putDouble("weight_loss_this_session",weight_loss_this_session);
		savedInstanceState.putFloat("last_jerk",last_jerk);
    	
    	super.onSaveInstanceState(savedInstanceState);
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_game_accel));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
    	
    	sensors=(SensorManager)getSystemService(SENSOR_SERVICE);
    	accelerometer=sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	sensors.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
    	
    	reset();
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	sensors.unregisterListener(this);
    	
		if(isFinishing()){
			DecimalFormat df=new DecimalFormat("#.##");
			
			int sound=-1;
			String message_begin="";
			String message_end="";
			long experience_points=0;
			int bits=0;
			int item_chance=0;
			
			pet_status.gain_weight(-weight_loss_this_session);
			
			message_begin="Done Shakin' It!";
			
			experience_points=(long)Math.ceil((double)bits_this_session*Pet_Status.EXPERIENCE_SCALE_ACCEL);
			
			experience_points+=(long)Math.ceil((double)bits_this_session*0.01*(double)Pet_Status.get_level_bonus_xp(pet_status.level,Pet_Status.EXPERIENCE_BONUS_ACCEL,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_LOW));
			
			bits=bits_this_session;
			
			item_chance=(int)Math.ceil((double)bits_this_session*Pet_Status.ITEM_CHANCE_ACCEL);
			
			if(!UnitConverter.is_weight_basically_zero(weight_loss_this_session,df)){
				message_end=pet_status.name+" lost "+UnitConverter.get_weight_string(weight_loss_this_session,df)+".";
			}
			
			pet_status.sleeping_wake_up();
	    	
	    	if(experience_points>0 || bits_this_session>0 || message_end.length()>0){
	    		Activity_Rewards.give_rewards(this,getPackageName(),pet_status,sound,message_begin,message_end,experience_points,bits,true,item_chance,pet_status.level);
	    	}
		}
    }
	
	public void reset(){
		timer_update.start();
    	delta=0.0f;
    	delta_count=0;
	}
	
	@Override
    public void onSensorChanged(SensorEvent event){
		TextView tv=null;
		DecimalFormat df=new DecimalFormat("#.##");
		
		float x=event.values[0];
		float y=event.values[1];
		float z=event.values[2];
		
		float jerk=0.0f;
		
		if(!initialized){
			initialized=true;
			
			last_x=x;
			last_y=y;
			last_z=z;
		}
		else{
			float delta_x=Math.abs(last_x-x);
			float delta_y=Math.abs(last_y-y);
			float delta_z=Math.abs(last_z-z);
			
			if(delta_x<NOISE){
				delta_x=0.0f;
			}
			if(delta_y<NOISE){
				delta_y=0.0f;
			}
			if(delta_z<NOISE){
				delta_z=0.0f;
			}
			
			last_x=x;
			last_y=y;
			last_z=z;
			
			float delta_all=delta_x+delta_y+delta_z;
			
			delta+=delta_all;
			delta_count++;
		}
		
		if(timer_update.get_ticks()>=1000){
			float delta_average=delta/(float)delta_count;
			
			jerk=Math.abs(delta_average-last_accel)/((float)timer_update.get_ticks()/1000.0f);
			
			last_accel=delta_average;
			
			int bits_to_gain=Age_Tier.scale_bit_gain(pet_status.age_tier,(int)Math.ceil(jerk/2.75f));
			
			double weight_to_lose=(double)(jerk/64.0f);
			if(pet_status.weight-weight_loss_this_session-weight_to_lose<Pet_Status.WEIGHT_MIN){
				weight_to_lose=0.0;
			}
			
			if(bits_to_gain>0 || weight_to_lose>0.0){
				if(bits_to_gain>0){
					bits_this_session+=bits_to_gain;
				}
				
				if(weight_to_lose>0.0){
					weight_loss_this_session+=weight_to_lose;
				}
			}
			
			reset();
		}
		
		if(jerk!=0.0f){
			last_jerk=jerk;
		}
		
		tv=(TextView)findViewById(R.id.text_game_accel_bits_this_session);
		tv.setText("Bits earned this session: "+bits_this_session);
		
		tv=(TextView)findViewById(R.id.text_game_accel_weight_loss_this_session);
		tv.setText("Weight lost this session: "+UnitConverter.get_weight_string(weight_loss_this_session,df));
		
		tv=(TextView)findViewById(R.id.text_game_accel_jerk);
		tv.setText("Last jerk: "+UnitConverter.get_jerk_string(last_jerk,df));
    }
	@Override
	public void onAccuracyChanged(Sensor sensor,int accuracy){
    }
}
