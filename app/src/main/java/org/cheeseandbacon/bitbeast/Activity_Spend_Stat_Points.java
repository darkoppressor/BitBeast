/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;


import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Spend_Stat_Points extends AppCompatActivity {
	private Pet_Status pet_status;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spend_stat_points);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_spend_stat_points));
    	System.gc();
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
    	
    	Font.set_typeface(getAssets(), (TextView)findViewById(R.id.spend_stat_points));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.spend_stat_points_points));
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_spend_stat_points_strength));
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_spend_stat_points_dexterity));
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_spend_stat_points_stamina));
    	
		Button b=null;
		
		b=(Button)findViewById(R.id.button_spend_stat_points_strength);
        b.setNextFocusUpId(R.id.button_spend_stat_points_stamina);
        
        b=(Button)findViewById(R.id.button_spend_stat_points_stamina);
        b.setNextFocusDownId(R.id.button_spend_stat_points_strength);
        
        update();
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	StorageManager.save_pet_status(this,pet_status);
    }
    
    public void update(){
    	TextView tv=null;
		Button b=null;
		
		tv=(TextView)findViewById(R.id.spend_stat_points_points);
    	tv.setText("Available points: "+pet_status.stat_points);
    	
    	b=(Button)findViewById(R.id.button_spend_stat_points_strength);
    	b.setText(Pet_Status.get_buff_name("strength_max")+": "+(pet_status.get_strength_max()-pet_status.get_strength_upgrade()));
    	
    	b=(Button)findViewById(R.id.button_spend_stat_points_dexterity);
    	b.setText(Pet_Status.get_buff_name("dexterity_max")+": "+(pet_status.get_dexterity_max()-pet_status.get_dexterity_upgrade()));
    	
    	b=(Button)findViewById(R.id.button_spend_stat_points_stamina);
    	b.setText(Pet_Status.get_buff_name("stamina_max")+": "+(pet_status.get_stamina_max()-pet_status.get_stamina_upgrade()));
    }
    
    public void button_spend_strength(View view){
    	if(pet_status.stat_points>0){
			Sound_Manager.playSound(this, Sound.SPEND_STAT_POINT);

			pet_status.strength_max+=Pet_Status.STAT_GAIN_SELECTION;
			pet_status.strength_max_bound();

			pet_status.strength+=Pet_Status.STAT_GAIN_SELECTION;
			pet_status.strength_bound();
			
			pet_status.stat_points--;
			
			update();
		}
		else{
			Sound_Manager.playSound(this, Sound.NO_STAT_POINTS);
			
			Toast.makeText(getApplicationContext(),"Out of stat points!",Toast.LENGTH_SHORT).show();
		}
    }
    
    public void button_spend_dexterity(View view){
    	if(pet_status.stat_points>0){
			Sound_Manager.playSound(this, Sound.SPEND_STAT_POINT);

            pet_status.dexterity_max+=Pet_Status.STAT_GAIN_SELECTION;
            pet_status.dexterity_max_bound();

            pet_status.dexterity+=Pet_Status.STAT_GAIN_SELECTION;
            pet_status.dexterity_bound();
			
			pet_status.stat_points--;
			
			update();
		}
		else{
			Sound_Manager.playSound(this, Sound.NO_STAT_POINTS);
			
			Toast.makeText(getApplicationContext(),"Out of stat points!",Toast.LENGTH_SHORT).show();
		}
    }
    
    public void button_spend_stamina(View view){
    	if(pet_status.stat_points>0){
			Sound_Manager.playSound(this, Sound.SPEND_STAT_POINT);

            pet_status.stamina_max+=Pet_Status.STAT_GAIN_SELECTION;
            pet_status.stamina_max_bound();

            pet_status.stamina+=Pet_Status.STAT_GAIN_SELECTION;
            pet_status.stamina_bound();
			
			pet_status.stat_points--;
			
			update();
		}
		else{
			Sound_Manager.playSound(this, Sound.NO_STAT_POINTS);
			
			Toast.makeText(getApplicationContext(),"Out of stat points!",Toast.LENGTH_SHORT).show();
		}
    }
}
