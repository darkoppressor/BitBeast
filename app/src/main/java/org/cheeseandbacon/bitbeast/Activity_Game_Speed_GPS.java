package org.cheeseandbacon.bitbeast;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Activity_Game_Speed_GPS extends AppCompatActivity implements LocationListener{
	private LocationManager locations;
	
	//The minimum accuracy a new location must have to be counted, in meters
	private static final float ACCURACY_MIN=50.0f;
	
	//The speed threshold that meters per bit changes, in m/s.
	private static final float SPEED_THRESHOLD=8.9408f;
	
	Location last_location;
	
	int bits_this_session;
	double weight_loss_this_session;
	
	float accuracy_min;
	float accuracy_max;
	float accuracies;
	int accuracy_count;
	
	float speed_min;
	float speed_max;
	float speeds;
	int speed_count;
	
	float speed_pool;
	
	private Pet_Status pet_status;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_speed_gps);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        last_location=null;
        
        bits_this_session=0;
        weight_loss_this_session=0.0;
    	
    	accuracy_min=0.0f;
        accuracy_max=0.0f;
        accuracies=0.0f;
        accuracy_count=0;
        
        speed_min=0.0f;
        speed_max=0.0f;
        speeds=0.0f;
        speed_count=0;
        
        speed_pool=0.0f;
        
        Font.set_typeface((TextView)findViewById(R.id.text_game_speed_gps_bits_this_session));
        Font.set_typeface((TextView)findViewById(R.id.text_game_speed_gps_weight_loss_this_session));
        Font.set_typeface((TextView)findViewById(R.id.text_game_speed_gps_notice_off));
        Font.set_typeface((TextView)findViewById(R.id.text_game_speed_gps_notice_accuracy));
        Font.set_typeface((TextView)findViewById(R.id.text_game_speed_gps_accuracy));
        Font.set_typeface((TextView)findViewById(R.id.text_game_speed_gps_speed));
        
        if(savedInstanceState!=null){
        	bits_this_session=savedInstanceState.getInt("bits_this_session");
        	accuracy_count=savedInstanceState.getInt("accuracy_count");
        	speed_count=savedInstanceState.getInt("speed_count");
        	weight_loss_this_session=savedInstanceState.getDouble("weight_loss_this_session");
        	accuracy_min=savedInstanceState.getFloat("accuracy_min");
        	accuracy_max=savedInstanceState.getFloat("accuracy_max");
        	accuracies=savedInstanceState.getFloat("accuracies");
        	speed_min=savedInstanceState.getFloat("speed_min");
        	speed_max=savedInstanceState.getFloat("speed_max");
        	speeds=savedInstanceState.getFloat("speeds");
        	speed_pool=savedInstanceState.getFloat("speed_pool");
        }
    }
	@Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putInt("bits_this_session",bits_this_session);
		savedInstanceState.putInt("accuracy_count",accuracy_count);
		savedInstanceState.putInt("speed_count",speed_count);
		savedInstanceState.putDouble("weight_loss_this_session",weight_loss_this_session);
		savedInstanceState.putFloat("accuracy_min",accuracy_min);
		savedInstanceState.putFloat("accuracy_max",accuracy_max);
		savedInstanceState.putFloat("accuracies",accuracies);
		savedInstanceState.putFloat("speed_min",speed_min);
		savedInstanceState.putFloat("speed_max",speed_max);
		savedInstanceState.putFloat("speeds",speeds);
		savedInstanceState.putFloat("speed_pool",speed_pool);
    	
    	super.onSaveInstanceState(savedInstanceState);
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_game_speed_gps));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(false));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status,false);
    	
    	locations=(LocationManager)getSystemService(LOCATION_SERVICE);
    	locations.requestLocationUpdates(LocationManager.GPS_PROVIDER,Options.get_gps_update_time(),0.0f,this);
    	
    	last_location=null;
    	
    	set_text(0.0f,0.0f);
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	locations.removeUpdates(this);
    	
    	if(isFinishing()){
    		DecimalFormat df=new DecimalFormat("#.##");
    		
			int sound=-1;
			String message_begin="";
			String message_end="";
			long experience_points=0;
			int bits=0;
			int item_chance=0;
			
			pet_status.gain_weight(-weight_loss_this_session);
			
			message_begin="Done Go Go Go'ing!";
			
			experience_points=(long)Math.ceil((double)bits_this_session*Pet_Status.EXPERIENCE_SCALE_GPS_SPEED);
			
			experience_points+=(long)Math.ceil((double)bits_this_session*0.01*(double)Pet_Status.get_level_bonus_xp(pet_status.level,Pet_Status.EXPERIENCE_BONUS_GPS_SPEED,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_LOW));
			
			bits=bits_this_session;
			
			item_chance=(int)Math.ceil((double)bits_this_session*Pet_Status.ITEM_CHANCE_GPS_SPEED);
			
			if(!UnitConverter.is_weight_basically_zero(weight_loss_this_session,df)){
				message_end=pet_status.name+" lost "+UnitConverter.get_weight_string(weight_loss_this_session,df)+".";
			}
									
			pet_status.sleeping_wake_up();
	    	
			if(experience_points>0 || bits_this_session>0 || message_end.length()>0){
				Activity_Rewards.give_rewards(this,getPackageName(),pet_status,sound,message_begin,message_end,experience_points,bits,true,item_chance,pet_status.level);
			}
		}
    }
	@Override
    public boolean onSearchRequested(){
    	String save_location=StorageManager.save_screenshot(this,findViewById(R.id.root_game_speed_gps));
    	
    	if(save_location.length()>0){
    		Toast.makeText(getApplicationContext(),"Screenshot saved to "+save_location+".",Toast.LENGTH_SHORT).show();
    	}
    	
    	return false;
    }
	
	public void set_text(float accuracy,float speed){
		TextView tv=null;
		DecimalFormat df=new DecimalFormat("#.##");
		String message="";
		
		tv=(TextView)findViewById(R.id.text_game_speed_gps_bits_this_session);
		tv.setText("Bits earned this session: "+bits_this_session);
		
		tv=(TextView)findViewById(R.id.text_game_speed_gps_weight_loss_this_session);
		tv.setText("Weight lost this session: "+UnitConverter.get_weight_string(weight_loss_this_session,df));
		
		tv=(TextView)findViewById(R.id.text_game_speed_gps_notice_off);
		if(locations.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			tv.setText("");
			tv.setVisibility(TextView.GONE);
		}
		else{
			tv.setText("Note: GPS is currently disabled.\nEnable it to earn bits!");
			tv.setVisibility(TextView.VISIBLE);
		}
		
		tv=(TextView)findViewById(R.id.text_game_speed_gps_notice_accuracy);
		if(accuracy<=ACCURACY_MIN){
			tv.setText("");
			tv.setVisibility(TextView.GONE);
		}
		else{
			tv.setText("Note: GPS accuracy is currently very poor.\nThis may prevent you from earning bits.");
			tv.setVisibility(TextView.VISIBLE);
		}
		
		tv=(TextView)findViewById(R.id.text_game_speed_gps_speed);
		message="Speed: "+UnitConverter.get_speed_string(speed,df)+"\n";
		float speed_average=0.0f;
		if(speed_count>0){
			speed_average=speeds/speed_count;
		}
		message+="Minimum Speed: "+UnitConverter.get_speed_string(speed_min,df)+"\nMaximum Speed: "+UnitConverter.get_speed_string(speed_max,df)+"\nAverage Speed: "+UnitConverter.get_speed_string(speed_average,df);
		tv.setText(message);
		
		tv=(TextView)findViewById(R.id.text_game_speed_gps_accuracy);
		message="Accuracy: "+UnitConverter.get_distance_string(accuracy,df)+"\n";
		float accuracy_average=0.0f;
		if(accuracy_count>0){
			accuracy_average=accuracies/accuracy_count;
		}
		message+="Best Accuracy: "+UnitConverter.get_distance_string(accuracy_min,df)+"\nWorst Accuracy: "+UnitConverter.get_distance_string(accuracy_max,df)+"\nAverage Accuracy: "+UnitConverter.get_distance_string(accuracy_average,df);
		tv.setText(message);
		tv.setVisibility(TextView.GONE);
	}
	
	@Override
	public void onLocationChanged(Location location){
		float accuracy=location.getAccuracy();
		
		if(accuracy<accuracy_min || accuracy_min==0.0f){
			accuracy_min=accuracy;
		}
		if(accuracy>accuracy_max || accuracy_max==0.0f){
			accuracy_max=accuracy;
		}
		
		accuracies+=accuracy;
		accuracy_count++;
		
		//The speed in m/s.
		float speed=0.0f;
		
		if(last_location==null || (location.hasAccuracy() && accuracy<=ACCURACY_MIN)){
			//If we have no last location yet.
			if(last_location==null){
				last_location=location;
			}
			else{
				//The distance moved from the last location, in meters.
				float distance_moved=location.distanceTo(last_location);
				
				speed=distance_moved/((float)Math.abs(location.getTime()-last_location.getTime())/1000.0f);
				
				if(speed<speed_min || speed_min==0.0f){
					speed_min=speed;
				}
				if(speed>speed_max || speed_max==0.0f){
					speed_max=speed;
				}
				
				speeds+=speed;
				speed_count++;
				
				//Add the current speed to the speed pool, scaled based on the GPS update time.
				//This scaling prevents more frequent updates from causing faster bit gain.
				speed_pool+=speed*(((float)Options.get_gps_update_time()/1000.0f)/60.0f);
				
				float meters_per_bit=0.5f;
				if(speed>=SPEED_THRESHOLD){
					meters_per_bit=8.0f;
				}
				
				int bits_to_gain=Age_Tier.scale_bit_gain(pet_status.age_tier,(int)(Math.floor(speed_pool/meters_per_bit)));
				
				double weight_to_lose=Math.floor(speed_pool/(meters_per_bit*4.0f));
				if(pet_status.weight-weight_loss_this_session-weight_to_lose<Pet_Status.WEIGHT_MIN){
					weight_to_lose=0.0;
				}
				
				if(bits_to_gain>0 || weight_to_lose>0.0){
					if(bits_to_gain>0){
						speed_pool-=(float)bits_to_gain*meters_per_bit;
						
						bits_this_session+=bits_to_gain;
					}
					
					if(weight_to_lose>0.0){
						weight_loss_this_session+=weight_to_lose;
					}
				}
				
				last_location=location;
			}
		}
		
		set_text(accuracy,speed);
	}
	@Override
	public void onStatusChanged(String provider,int status,Bundle extras){
	}
	@Override
	public void onProviderEnabled(String provider){
		set_text(0.0f,0.0f);
	}
	@Override
    public void onProviderDisabled(String provider){
		set_text(0.0f,0.0f);
	}
}
