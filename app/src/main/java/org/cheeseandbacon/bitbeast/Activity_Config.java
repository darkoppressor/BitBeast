package org.cheeseandbacon.bitbeast;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Activity_Config extends AppCompatActivity {
	static final int DIALOG_ID_TIME=0;
	
	TimePickerDialog dialog_time;
	
	private Vibrator vibrator;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        
        showDialog(DIALOG_ID_TIME);
        dismissDialog(DIALOG_ID_TIME);
        
        Spinner spinner=null;
        ArrayAdapter<CharSequence> adapter=null;

        spinner=(Spinner)findViewById(R.id.spinner_option_screen_orientation);
        adapter=ArrayAdapter.createFromResource(this,R.array.screen_orientation,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner_Listener_Screen_Orientation());
        
        spinner=(Spinner)findViewById(R.id.spinner_option_temp_units);
        adapter=ArrayAdapter.createFromResource(this,R.array.temp_units,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner_Listener_Temp_Units());
        
        spinner=(Spinner)findViewById(R.id.spinner_option_units);
        adapter=ArrayAdapter.createFromResource(this,R.array.units,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner_Listener_Units());
        
        spinner=(Spinner)findViewById(R.id.spinner_option_gps_update_time);
        adapter=ArrayAdapter.createFromResource(this,R.array.gps_update_time,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner_Listener_Gps_Update_Time());
        
        Font.set_typeface((CheckBox)findViewById(R.id.checkbox_config_pause));
        Font.set_typeface((CheckBox)findViewById(R.id.checkbox_config_keep_screen_on));
        Font.set_typeface((CheckBox)findViewById(R.id.checkbox_config_vibrate));
        Font.set_typeface((CheckBox)findViewById(R.id.checkbox_config_show_stat_bars));
        Font.set_typeface((CheckBox)findViewById(R.id.checkbox_config_show_thermometer));
        Font.set_typeface((TextView)findViewById(R.id.text_config_screen_orientation));
        Font.set_typeface((TextView)findViewById(R.id.text_config_temp_units));
        Font.set_typeface((TextView)findViewById(R.id.text_config_units));
        Font.set_typeface((TextView)findViewById(R.id.text_config_gps_update_time));
        Font.set_typeface((TextView)findViewById(R.id.text_config_header_pet));
        Font.set_typeface((TextView)findViewById(R.id.text_config_header_android));
        Font.set_typeface((TextView)findViewById(R.id.text_config_header_display));
        Font.set_typeface((Button)findViewById(R.id.button_config_desired_sleep_time));
        
        CheckBox cb=null;
        
        cb=(CheckBox)findViewById(R.id.checkbox_config_pause);
        cb.setNextFocusUpId(R.id.spinner_option_units);
        
        spinner=(Spinner)findViewById(R.id.spinner_option_units);
        spinner.setNextFocusDownId(R.id.checkbox_config_pause);
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_config));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	CheckBox cb=null;
    	Spinner spinner=null;
    	
    	cb=(CheckBox)findViewById(R.id.checkbox_config_pause);
    	cb.setChecked(Options.pause);
    	
    	cb=(CheckBox)findViewById(R.id.checkbox_config_keep_screen_on);
    	cb.setChecked(Options.keep_screen_on);
    	
    	cb=(CheckBox)findViewById(R.id.checkbox_config_vibrate);
    	cb.setChecked(Options.vibrate);
    	
    	cb=(CheckBox)findViewById(R.id.checkbox_config_show_stat_bars);
    	cb.setChecked(Options.show_stat_bars);
    	
    	cb=(CheckBox)findViewById(R.id.checkbox_config_show_thermometer);
    	cb.setChecked(Options.show_thermometer);
    	
    	spinner=(Spinner)findViewById(R.id.spinner_option_screen_orientation);
    	spinner.setSelection(Options.get_fake_orientation());
    	
    	spinner=(Spinner)findViewById(R.id.spinner_option_temp_units);
    	spinner.setSelection(Options.temp_units);
    	
    	spinner=(Spinner)findViewById(R.id.spinner_option_units);
    	spinner.setSelection(Options.units);
    	
    	spinner=(Spinner)findViewById(R.id.spinner_option_gps_update_time);
    	spinner.setSelection(Options.gps_update_time);
    	
    	update_time_button();
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    }
	@Override
    public boolean onSearchRequested(){
    	String save_location=StorageManager.save_screenshot(this,findViewById(R.id.root_config));
    	
    	if(save_location.length()>0){
    		Toast.makeText(getApplicationContext(),"Screenshot saved to "+save_location+".",Toast.LENGTH_SHORT).show();
    	}
    	
    	return false;
    }
	@Override
	protected Dialog onCreateDialog(int id){
	    switch(id){
	    case DIALOG_ID_TIME:
	    	dialog_time=new TimePickerDialog(this,time_set_listener,Options.desired_sleep_time.get(Calendar.HOUR_OF_DAY),Options.desired_sleep_time.get(Calendar.MINUTE),false);
	        return dialog_time;
	    default:
	    	return null;
	    }
	}
	
	public void button_pause(View view){
		Options.pause=((CheckBox)view).isChecked();
		
		StorageManager.save_options(this);
	}
	public void button_keep_screen_on(View view){
		Options.keep_screen_on=((CheckBox)view).isChecked();
		
		StorageManager.save_options(this);
		
		Options.set_keep_screen_on(getWindow());
	}
	public void button_vibrate(View view){
		Options.vibrate=((CheckBox)view).isChecked();
		
		StorageManager.save_options(this);
		
		vibrator.cancel();
	}
	public void button_show_stat_bars(View view){
		Options.show_stat_bars=((CheckBox)view).isChecked();
		
		StorageManager.save_options(this);
	}
	public void button_show_thermometer(View view){
		Options.show_thermometer=((CheckBox)view).isChecked();
		
		StorageManager.save_options(this);
	}
	public void button_desired_sleep_time(View view){
    	showDialog(DIALOG_ID_TIME);
    }
	
	public void update_time_button(){
		String am_pm="";
		if(Options.desired_sleep_time.get(Calendar.AM_PM)==Calendar.AM){
			am_pm=" AM";
		}
		else{
			am_pm=" PM";
		}
		
		String minutes="";
		if(Options.desired_sleep_time.get(Calendar.MINUTE)<10){
			minutes="0";
		}
		minutes+=Options.desired_sleep_time.get(Calendar.MINUTE);
		
		Button b=(Button)findViewById(R.id.button_config_desired_sleep_time);
    	b.setText(getResources().getString(R.string.config_desired_sleep_time)+Options.desired_sleep_time.get(Calendar.HOUR)+":"+minutes+am_pm);
	
    	dialog_time.updateTime(Options.desired_sleep_time.get(Calendar.HOUR_OF_DAY),Options.desired_sleep_time.get(Calendar.MINUTE));
	}
	
	private TimePickerDialog.OnTimeSetListener time_set_listener=new TimePickerDialog.OnTimeSetListener(){
		@Override
		public void onTimeSet(TimePicker view,int hourOfDay,int minute){
			Options.desired_sleep_time.set(Calendar.HOUR_OF_DAY,hourOfDay);
			
			Options.desired_sleep_time.set(Calendar.MINUTE,minute);
			
			StorageManager.save_options(Activity_Config.this);
			
			update_time_button();
		}
	};
	
	public class Spinner_Listener_Screen_Orientation implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> parent,View view,int pos,long id){
			Options.screen_orientation=pos;
			
			StorageManager.save_options(Activity_Config.this);
			
			setRequestedOrientation(Options.get_orientation(true));
		}
		
		public void onNothingSelected(AdapterView<?> parent){
		}
	}
	public class Spinner_Listener_Temp_Units implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> parent,View view,int pos,long id){
			Options.temp_units=pos;
			
			StorageManager.save_options(Activity_Config.this);
		}
		
		public void onNothingSelected(AdapterView<?> parent){
		}
	}
	public class Spinner_Listener_Units implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> parent,View view,int pos,long id){
			Options.units=pos;
			
			StorageManager.save_options(Activity_Config.this);
		}
		
		public void onNothingSelected(AdapterView<?> parent){
		}
	}
	public class Spinner_Listener_Gps_Update_Time implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> parent,View view,int pos,long id){
			Options.gps_update_time=pos;
			
			StorageManager.save_options(Activity_Config.this);
		}
		
		public void onNothingSelected(AdapterView<?> parent){
		}
	}
}
