package org.cheeseandbacon.bitbeast;

import java.text.DecimalFormat;


import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Records extends Activity{
	private Records records;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Font.set_typeface((TextView)findViewById(R.id.message_records));
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_records));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	records=new Records();
    	StorageManager.load_records(this,records,false);
    	
    	TextView tv=null;
    	String message="";
    	DecimalFormat df=new DecimalFormat("#.##");
    	
    	message+="Pets interred: "+records.get_number_of_pets();
    	message+="\nTotal singleplayer battle win/loss ratio: "+records.get_battles_won_sp()+":"+records.get_battles_lost_sp();
    	message+="\nTotal multiplayer battle win/loss ratio: "+records.get_battles_won()+":"+records.get_battles_lost();
    	
    	for(int i=0;i<records.pets.size();i++){
    		message+="\n\n"+this.getString(R.string.status_name)+records.pets.get(i).name;
    		message+="\n\n"+this.getString(R.string.status_level)+records.pets.get(i).level;
    		message+="\n"+this.getString(R.string.status_bits)+records.pets.get(i).bits;
    		message+="\n"+this.getString(R.string.status_age)+Time_String.seconds_to_years(records.pets.get(i).age);
    		message+="\n"+this.getString(R.string.status_type)+Strings.first_letter_capital(records.pets.get(i).type.toString());
    		message+="\n"+this.getString(R.string.status_weight)+UnitConverter.get_weight_string(records.pets.get(i).weight,df);
    		message+="\n"+Pet_Status.get_buff_name("strength_max")+": "+records.pets.get(i).strength_max;
    		message+="\n"+Pet_Status.get_buff_name("dexterity_max")+": "+records.pets.get(i).dexterity_max;
    		message+="\n"+Pet_Status.get_buff_name("stamina_max")+": "+records.pets.get(i).stamina_max;
    		message+="\n"+this.getString(R.string.status_winloss_sp)+records.pets.get(i).battles_won_sp+":"+records.pets.get(i).battles_lost_sp;
    		message+="\n"+this.getString(R.string.status_winloss)+records.pets.get(i).battles_won+":"+records.pets.get(i).battles_lost;
    	}
    	
    	tv=(TextView)findViewById(R.id.message_records);
    	tv.setText(message);
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    }
	@Override
    public boolean onSearchRequested(){
    	String save_location=StorageManager.save_screenshot(this,findViewById(R.id.root_records));
    	
    	if(save_location.length()>0){
    		Toast.makeText(getApplicationContext(),"Screenshot saved to "+save_location+".",Toast.LENGTH_SHORT).show();
    	}
    	
    	return false;
    }
}
