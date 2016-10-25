package org.cheeseandbacon.bitbeast;

import java.text.DecimalFormat;


import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Status extends Activity{
	private Pet_Status pet_status;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_status));
    	System.gc();
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status,false);
    	
    	update();
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    }
    @Override
    public boolean onSearchRequested(){
    	String save_location=StorageManager.save_screenshot(this,findViewById(R.id.root_status));
    	
    	if(save_location.length()>0){
    		Toast.makeText(getApplicationContext(),"Screenshot saved to "+save_location+".",Toast.LENGTH_SHORT).show();
    	}
    	
    	return false;
    }
    
    public void update(){
    	TextView tv=null;
    	Button b=null;
    	ProgressBar pb=null;
    	DecimalFormat df=new DecimalFormat("#.##");
    	String message="";
    	
    	String sick_string="";
    	if(pet_status.sick){
    		sick_string=" (sick)";
    	}
    	
    	tv=(TextView)findViewById(R.id.text_status_name);
    	tv.setText(this.getString(R.string.status_name)+pet_status.name+sick_string);
    	Font.set_typeface(tv);
    	
    	tv=(TextView)findViewById(R.id.text_status_level);
    	tv.setText(this.getString(R.string.status_level)+pet_status.level);
    	Font.set_typeface(tv);
    	
    	b=(Button)findViewById(R.id.button_status_spend);
    	Font.set_typeface(b);
    	if(pet_status.stat_points>0){
    		b.setVisibility(Button.VISIBLE);
    	}
    	else{
    		b.setVisibility(Button.GONE);
    	}
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_experience);
    	pb.setMax((int)pet_status.experience_max);
    	pb.setProgress((int)pet_status.experience);
    	tv=(TextView)findViewById(R.id.text_status_title_experience);
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_experience);
    	tv.setText(pet_status.experience+"/"+pet_status.experience_max);
    	Font.set_typeface(tv);
    	
    	tv=(TextView)findViewById(R.id.text_status_type);
    	tv.setText(this.getString(R.string.status_type)+Strings.first_letter_capital(pet_status.type.toString()));
    	Font.set_typeface(tv);
    	
    	tv=(TextView)findViewById(R.id.text_status_bits);
    	tv.setText(this.getString(R.string.status_bits)+pet_status.bits);
    	Font.set_typeface(tv);

    	tv=(TextView)findViewById(R.id.text_status_age);
    	tv.setText(this.getString(R.string.status_age)+Time_String.seconds_to_years(pet_status.age));
    	Font.set_typeface(tv);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_happy);
    	pb.setMax(Pet_Status.HAPPY_MAX+Pet_Status.HAPPY_MAX);
    	pb.setProgress(pet_status.happy+Pet_Status.HAPPY_MAX);
    	tv=(TextView)findViewById(R.id.text_status_title_happy);
    	tv.setText(Pet_Status.get_buff_name("happy")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_happy);
    	tv.setText(pet_status.happy+"/"+Pet_Status.HAPPY_MAX);
    	Font.set_typeface(tv);
    	tv.setVisibility(TextView.INVISIBLE);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_temp);
    	float TEMP_BAR_MAX=40.0f;
    	float shrunk_temp=(float)pet_status.temp;
		if(shrunk_temp>TEMP_BAR_MAX){
			shrunk_temp=TEMP_BAR_MAX;
		}
    	pb.setMax((int)TEMP_BAR_MAX);
    	pb.setProgress((int)shrunk_temp);
    	tv=(TextView)findViewById(R.id.text_status_title_temp);
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_temp);
    	tv.setText(UnitConverter.get_temperature_string(pet_status.temp,df));
    	Font.set_typeface(tv);
    	
    	tv=(TextView)findViewById(R.id.text_status_weight);
    	message="<font color='"+String.format("#%06x",0xFFFFFF&getResources().getColor(R.color.font))+"'>"+this.getString(R.string.status_weight)+"</font>";
    	if(pet_status.is_obese()){
    		message+="<font color='"+String.format("#%06x",0xFFFFFF&getResources().getColor(R.color.font_downgrade))+"'>";
    	}
    	else{
    		message+="<font color='"+String.format("#%06x",0xFFFFFF&getResources().getColor(R.color.font))+"'>";
    	}
    	if(pet_status.get_weight_upgrade()>0){
    		message+=UnitConverter.get_weight_string(pet_status.get_weight(),df);
    		message+=" [+"+UnitConverter.get_weight_string(pet_status.get_weight_upgrade(),df)+"]";
    	}
    	else{
    		message+=UnitConverter.get_weight_string(pet_status.get_weight(),df);
    	}
    	message+="</font>";
    	tv.setText(Html.fromHtml(Strings.newline_to_br(message)));
    	Font.set_typeface(tv);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_hunger);
    	pb.setMax(Age_Tier.get_hunger_max(pet_status.age_tier));
    	pb.setProgress(pet_status.hunger);
    	tv=(TextView)findViewById(R.id.text_status_title_hunger);
    	tv.setText(Pet_Status.get_buff_name("hunger")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_hunger);
    	tv.setText(pet_status.hunger+"/"+Age_Tier.get_hunger_max(pet_status.age_tier));
    	Font.set_typeface(tv);
    	tv.setVisibility(TextView.INVISIBLE);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_thirst);
    	pb.setMax(Pet_Status.THIRST_MAX);
    	pb.setProgress(pet_status.thirst);
    	tv=(TextView)findViewById(R.id.text_status_title_thirst);
    	tv.setText(Pet_Status.get_buff_name("thirst")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_thirst);
    	tv.setText(pet_status.thirst+"/"+Pet_Status.THIRST_MAX);
    	Font.set_typeface(tv);
    	tv.setVisibility(TextView.INVISIBLE);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_strength);
    	pb.setMax(pet_status.get_strength_max());
    	pb.setProgress(pet_status.get_strength());
    	tv=(TextView)findViewById(R.id.text_status_title_strength);
    	tv.setText(Pet_Status.get_buff_name("strength_max")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_strength);
    	message=pet_status.get_strength()+"/"+pet_status.get_strength_max();
    	if(pet_status.get_strength_upgrade()>0){
    		tv.setTextColor(getResources().getColor(R.color.font_upgrade));
    		
    		message+=" [+"+pet_status.get_strength_upgrade()+"]";
    	}
    	else{
    		tv.setTextColor(getResources().getColor(R.color.font));
    	}
    	tv.setText(message);
    	Font.set_typeface(tv);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_dexterity);
    	pb.setMax(pet_status.get_dexterity_max());
    	pb.setProgress(pet_status.get_dexterity());
    	tv=(TextView)findViewById(R.id.text_status_title_dexterity);
    	tv.setText(Pet_Status.get_buff_name("dexterity_max")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_dexterity);
    	message=pet_status.get_dexterity()+"/"+pet_status.get_dexterity_max();
    	if(pet_status.get_dexterity_upgrade()>0){
    		tv.setTextColor(getResources().getColor(R.color.font_upgrade));
    		
    		message+=" [+"+pet_status.get_dexterity_upgrade()+"]";
    	}
    	else{
    		tv.setTextColor(getResources().getColor(R.color.font));
    	}
    	tv.setText(message);
    	Font.set_typeface(tv);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_stamina);
    	pb.setMax(pet_status.get_stamina_max());
    	pb.setProgress(pet_status.get_stamina());
    	tv=(TextView)findViewById(R.id.text_status_title_stamina);
    	tv.setText(Pet_Status.get_buff_name("stamina_max")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_stamina);
    	message=pet_status.get_stamina()+"/"+pet_status.get_stamina_max();
    	if(pet_status.get_stamina_upgrade()>0){
    		tv.setTextColor(getResources().getColor(R.color.font_upgrade));
    		
    		message+=" [+"+pet_status.get_stamina_upgrade()+"]";
    	}
    	else{
    		tv.setTextColor(getResources().getColor(R.color.font));
    	}
    	tv.setText(message);
    	Font.set_typeface(tv);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_energy);
    	pb.setMax(pet_status.get_energy_max());
    	pb.setProgress(pet_status.get_energy());
    	tv=(TextView)findViewById(R.id.text_status_title_energy);
    	tv.setText(Pet_Status.get_buff_name("energy_max")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_energy);
    	message=pet_status.get_energy()+"/"+pet_status.get_energy_max();
    	if(pet_status.get_energy_upgrade()>0){
    		tv.setTextColor(getResources().getColor(R.color.font_upgrade));
    		
    		message+=" [+"+pet_status.get_energy_upgrade()+"]";
    	}
    	else{
    		tv.setTextColor(getResources().getColor(R.color.font));
    	}
    	tv.setText(message);
    	Font.set_typeface(tv);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_winloss_sp);
    	pb.setMax(pet_status.battles_won_sp+pet_status.battles_lost_sp);
    	pb.setProgress(pet_status.battles_won_sp);
    	tv=(TextView)findViewById(R.id.text_status_title_winloss_sp);
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_winloss_sp);
    	tv.setText(pet_status.battles_won_sp+":"+pet_status.battles_lost_sp);
    	Font.set_typeface(tv);
    	
    	pb=(ProgressBar)findViewById(R.id.bar_status_winloss);
    	pb.setMax(pet_status.battles_won+pet_status.battles_lost);
    	pb.setProgress(pet_status.battles_won);
    	tv=(TextView)findViewById(R.id.text_status_title_winloss);
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_status_winloss);
    	tv.setText(pet_status.battles_won+":"+pet_status.battles_lost);
    	Font.set_typeface(tv);
    	
    	String buffs_begin=this.getString(R.string.status_buffs);
    	buffs_begin+="<font color='"+String.format("#%06x",0xFFFFFF&getResources().getColor(R.color.font_upgrade))+"'>";
    	
    	String buffs="";
    	if(pet_status.buff_hunger>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_HUNGER+" "+Pet_Status.get_buff_name("hunger")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_hunger)+"]";
    	}
    	if(pet_status.buff_thirst>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_THIRST+" "+Pet_Status.get_buff_name("thirst")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_thirst)+"]";
    	}
    	if(pet_status.buff_poop>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_POOP+" "+Pet_Status.get_buff_name("poop")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_poop)+"]";
    	}
    	if(pet_status.buff_dirty>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_DIRTY+" "+Pet_Status.get_buff_name("dirty")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_dirty)+"]";
    	}
    	if(pet_status.buff_weight>0){
    		buffs+="\n+"+(int)Math.ceil((1.0-Pet_Status.FOOD_BUFF_WEIGHT)*100.0)+" "+Pet_Status.get_buff_name("weight")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_weight)+"]";
    	}
    	if(pet_status.buff_sick>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_SICK+" "+Pet_Status.get_buff_name("sick")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_sick)+"]";
    	}
    	if(pet_status.buff_happy>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_HAPPY+" "+Pet_Status.get_buff_name("happy")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_happy)+"]";
    	}
    	if(pet_status.buff_energy_regen>0){
    		buffs+="\n+"+(int)Math.ceil(Pet_Status.FOOD_BUFF_ENERGY_REGEN*100.0)+" "+Pet_Status.get_buff_name("energy_regen")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_energy_regen)+"]";
    	}
    	if(pet_status.buff_strength_regen>0){
    		buffs+="\n+"+(int)Math.ceil(Pet_Status.FOOD_BUFF_STRENGTH_REGEN*100.0)+" "+Pet_Status.get_buff_name("strength_regen")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_strength_regen)+"]";
    	}
    	if(pet_status.buff_dexterity_regen>0){
    		buffs+="\n+"+(int)Math.ceil(Pet_Status.FOOD_BUFF_DEXTERITY_REGEN*100.0)+" "+Pet_Status.get_buff_name("dexterity_regen")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_dexterity_regen)+"]";
    	}
    	if(pet_status.buff_stamina_regen>0){
    		buffs+="\n+"+(int)Math.ceil(Pet_Status.FOOD_BUFF_STAMINA_REGEN*100.0)+" "+Pet_Status.get_buff_name("stamina_regen")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_stamina_regen)+"]";
    	}
    	if(pet_status.buff_energy_max>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_ENERGY_MAX+" "+Pet_Status.get_buff_name("energy_max")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_energy_max)+"]";
    	}
    	if(pet_status.buff_strength_max>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_STRENGTH_MAX+" "+Pet_Status.get_buff_name("strength_max")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_strength_max)+"]";
    	}
    	if(pet_status.buff_dexterity_max>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_DEXTERITY_MAX+" "+Pet_Status.get_buff_name("dexterity_max")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_dexterity_max)+"]";
    	}
    	if(pet_status.buff_stamina_max>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_STAMINA_MAX+" "+Pet_Status.get_buff_name("stamina_max")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_stamina_max)+"]";
    	}
    	if(pet_status.buff_death>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_DEATH+" "+Pet_Status.get_buff_name("death")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_death)+"]";
    	}
    	if(pet_status.buff_magic_find>0){
    		buffs+="\n+"+Pet_Status.FOOD_BUFF_MAGIC_FIND+" "+Pet_Status.get_buff_name("magic_find")+" ["+Time_String.seconds_to_years_highest(pet_status.buff_magic_find)+"]";
    	}
    	
    	String buffs_end="</font>";
    	
    	tv=(TextView)findViewById(R.id.text_status_buffs);
    	tv.setText(Html.fromHtml(Strings.newline_to_br(buffs_begin+buffs+buffs_end)));
    	Font.set_typeface(tv);
    	if(buffs.length()==0){
    		tv.setVisibility(TextView.GONE);
    	}
    	else{
    		tv.setVisibility(TextView.VISIBLE);
    	}
    }
    
    public void button_spend(View view){
		Intent intent=new Intent(this,Activity_Spend_Stat_Points.class);
    	startActivity(intent);
    }
}
