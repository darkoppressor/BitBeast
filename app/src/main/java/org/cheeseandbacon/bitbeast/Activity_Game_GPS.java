package org.cheeseandbacon.bitbeast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Activity_Game_GPS extends AppCompatActivity implements LocationListener{
	private static final String TAG=Activity_Game_GPS.class.getName();
	private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;

	private LocationManager locations;
	
	//The minimum accuracy a new location must have to be counted, in meters.
	private static final float ACCURACY_MIN=50.0f;
	
	//The speed threshold that meters per bit changes, in m/s.
	private static final float SPEED_THRESHOLD=8.9408f;
	
	Location last_location;
	
	int bits_this_session;
	double weight_loss_this_session;
	float distance_this_session;
	float distance_pool;
	
	float accuracy_min;
	float accuracy_max;
	float accuracies;
	int accuracy_count;
	
	private Pet_Status pet_status;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_gps);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        last_location=null;
        
        bits_this_session=0;
        weight_loss_this_session=0.0;
    	distance_this_session=0.0f;
    	distance_pool=0.0f;
    	
    	accuracy_min=0.0f;
        accuracy_max=0.0f;
        accuracies=0.0f;
        accuracy_count=0;
        
        Font.set_typeface((TextView)findViewById(R.id.text_game_gps_bits_this_session));
        Font.set_typeface((TextView)findViewById(R.id.text_game_gps_weight_loss_this_session));
        Font.set_typeface((TextView)findViewById(R.id.text_game_gps_distance_this_session));
        Font.set_typeface((TextView)findViewById(R.id.text_game_gps_notice_off));
        Font.set_typeface((TextView)findViewById(R.id.text_game_gps_notice_accuracy));
        Font.set_typeface((TextView)findViewById(R.id.text_game_gps_accuracy));
        
        if(savedInstanceState!=null){
        	bits_this_session=savedInstanceState.getInt("bits_this_session");
        	accuracy_count=savedInstanceState.getInt("accuracy_count");
        	weight_loss_this_session=savedInstanceState.getDouble("weight_loss_this_session");
        	distance_this_session=savedInstanceState.getFloat("distance_this_session");
        	distance_pool=savedInstanceState.getFloat("distance_pool");
        	accuracy_min=savedInstanceState.getFloat("accuracy_min");
        	accuracy_max=savedInstanceState.getFloat("accuracy_max");
        	accuracies=savedInstanceState.getFloat("accuracies");
        }
    }
	@Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putInt("bits_this_session",bits_this_session);
		savedInstanceState.putInt("accuracy_count",accuracy_count);
		savedInstanceState.putDouble("weight_loss_this_session",weight_loss_this_session);
		savedInstanceState.putFloat("distance_this_session",distance_this_session);
		savedInstanceState.putFloat("distance_pool",distance_pool);
		savedInstanceState.putFloat("accuracy_min",accuracy_min);
		savedInstanceState.putFloat("accuracy_max",accuracy_max);
		savedInstanceState.putFloat("accuracies",accuracies);
    	
    	super.onSaveInstanceState(savedInstanceState);
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_game_gps));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(false));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
    	
    	locations=(LocationManager)getSystemService(LOCATION_SERVICE);
    	startGps();
    	
    	last_location=null;
    	
    	set_text(0.0f);
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	stopGps();
    	
    	if(isFinishing()){
    		DecimalFormat df=new DecimalFormat("#.##");
    		
			int sound=-1;
			String message_begin="";
			String message_end="";
			long experience_points=0;
			int bits=0;
			int item_chance=0;
			
			pet_status.gain_weight(-weight_loss_this_session);
			
			message_begin="Done Journeying!";
			
			experience_points=(long)Math.ceil((double)distance_this_session*Pet_Status.EXPERIENCE_SCALE_GPS);
			
			experience_points+=(long)Math.ceil((double)distance_this_session*0.01*(double)Pet_Status.get_level_bonus_xp(pet_status.level,Pet_Status.EXPERIENCE_BONUS_GPS,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_LOW));
			
			bits=bits_this_session;
			
			item_chance=(int)Math.ceil((double)distance_this_session*Pet_Status.ITEM_CHANCE_GPS);
			
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
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_ACCESS_FINE_LOCATION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					startGps();
				}
				else{
					Log.d(TAG, "ACCESS_FINE_LOCATION permission not granted");
				}
				break;
		}
	}

	private void startGps() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			locations.requestLocationUpdates(LocationManager.GPS_PROVIDER, Options.get_gps_update_time(), 0.0f, this);
		} else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
		}
	}

	private void stopGps() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			locations.removeUpdates(this);
		}
	}
	
	public void set_text(float accuracy){
		TextView tv=null;
		DecimalFormat df=new DecimalFormat("#.##");
		String message="";
		
		tv=(TextView)findViewById(R.id.text_game_gps_bits_this_session);
		tv.setText("Bits earned this session: "+bits_this_session);
		
		tv=(TextView)findViewById(R.id.text_game_gps_weight_loss_this_session);
		tv.setText("Weight lost this session: "+UnitConverter.get_weight_string(weight_loss_this_session,df));

		tv=(TextView)findViewById(R.id.text_game_gps_distance_this_session);
		tv.setText("Distance traveled this session: "+UnitConverter.get_distance_string(distance_this_session,df));
		
		tv=(TextView)findViewById(R.id.text_game_gps_notice_off);
		if(locations.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			tv.setText("");
			tv.setVisibility(TextView.GONE);
		}
		else{
			tv.setText("Note: GPS is currently disabled.\nEnable it to earn bits!");
			tv.setVisibility(TextView.VISIBLE);
		}
		
		tv=(TextView)findViewById(R.id.text_game_gps_notice_accuracy);
		if(accuracy<=ACCURACY_MIN){
			tv.setText("");
			tv.setVisibility(TextView.GONE);
		}
		else{
			tv.setText("Note: GPS accuracy is currently very poor.\nThis may prevent you from earning bits.");
			tv.setVisibility(TextView.VISIBLE);
		}
		
		tv=(TextView)findViewById(R.id.text_game_gps_accuracy);
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
		
		if(last_location==null || (location.hasAccuracy() && accuracy<=ACCURACY_MIN)){
			//If we have no last location yet.
			if(last_location==null){
				last_location=location;
			}
			else{
				//The distance moved from the last location, in meters.
				float distance_moved=location.distanceTo(last_location);
				
				//The speed in m/s.
				float speed=distance_moved/((float)Math.abs(location.getTime()-last_location.getTime())/1000.0f);
				
				distance_this_session+=distance_moved;
				distance_pool+=distance_moved;
				
				float meters_per_bit=32.0f;
				if(speed>=SPEED_THRESHOLD){
					meters_per_bit=128.0f;
				}
				
				int bits_to_gain=Age_Tier.scale_bit_gain(pet_status.age_tier,(int)(Math.floor(distance_pool/meters_per_bit)));
				
				double weight_to_lose=Math.floor(distance_pool/(meters_per_bit*4.0f));
				if(pet_status.weight-weight_loss_this_session-weight_to_lose<Pet_Status.WEIGHT_MIN){
					weight_to_lose=0.0;
				}
				
				if(bits_to_gain>0 || weight_to_lose>0.0){
					if(bits_to_gain>0){
						distance_pool-=(float)bits_to_gain*meters_per_bit;
						
						bits_this_session+=bits_to_gain;
					}
					
					if(weight_to_lose>0.0){
						weight_loss_this_session+=weight_to_lose;
					}
				}
				
				last_location=location;
			}
		}
		
		set_text(accuracy);
	}
	@Override
	public void onStatusChanged(String provider,int status,Bundle extras){
	}
	@Override
	public void onProviderEnabled(String provider){
		set_text(0.0f);
	}
	@Override
    public void onProviderDisabled(String provider){
		set_text(0.0f);
	}
}
