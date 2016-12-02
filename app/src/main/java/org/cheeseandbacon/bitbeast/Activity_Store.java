package org.cheeseandbacon.bitbeast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Activity_Store extends AppCompatActivity {
	static final int DIALOG_CONSUME=0;
	
	private Pet_Status pet_status;
	
	AlertDialog dialog_consume;
	
	//What section of the store are we viewing?
	private int section;
	
	private int move_direction;
	
	public static final int PERMA_ITEM_SIZE=16;
	
	private class StoreAdapter extends ArrayAdapter<Food>{
		public StoreAdapter(Context context,int textViewResourceId,List<Food> objects){
			super(context,textViewResourceId,objects);
		}
		
		@Override
		public View getView(int position,View convertView,ViewGroup parent){
			View view=convertView;
            if(view==null){
            	LayoutInflater li=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	view=li.inflate(R.layout.list_item_store_food,null);
            }
            
            TextView tv=(TextView)view.findViewById(R.id.list_name_food);
            if(tv!=null){
            	String message="";
            	if(pet_status.favorite_food.equals(Templates.get_food_list(section).get(position).name)){
            		message="\u2605 ";
            	}
            	message+=Templates.get_food_list(section).get(position).toString();
            	tv.setText(message);
            	tv.setTypeface(Font.font1);
            	
            	tv.setCompoundDrawablesWithIntrinsicBounds(Templates.get_food_list(section).get(position).resource_id,0,0,0);
            }

            return view;
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
        
        Templates.rebuild_perma_list(pet_status);
    	
    	Bundle bundle=getIntent().getExtras();
    	section=bundle.getInt(getPackageName()+"section",Store_Section.FOOD);
    	move_direction=bundle.getInt(getPackageName()+"move_direction",Direction.NONE);
    	
    	if(section==Store_Section.FOOD){
    		setTitle(getResources().getString(R.string.name_store_food));
    	}
    	else if(section==Store_Section.DRINKS){
    		setTitle(getResources().getString(R.string.name_store_drinks));
    	}
    	else if(section==Store_Section.TREATMENTS){
    		setTitle(getResources().getString(R.string.name_store_treatments));
    	}
    	else if(section==Store_Section.PERMA){
    		setTitle(getResources().getString(R.string.name_store_perma));
    	}
    	
    	showDialog(DIALOG_CONSUME);
        dismissDialog(DIALOG_CONSUME);

        ListView lv=(ListView)findViewById(R.id.list_view_store);
        lv.setTextFilterEnabled(true);
        lv.clearChoices();
        lv.setDividerHeight(0);
        lv.setAdapter(new StoreAdapter(Activity_Store.this,R.layout.list_item_store_food,Templates.get_food_list(section)));

        lv.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        		if(Templates.get_food_list(section).get(position).perma_item.length()>0){
        			if(pet_status.bits<Templates.get_food_list(section).get(position).cost){
        				Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
        				
	        			int bits_short=Templates.get_food_list(section).get(position).cost-pet_status.bits;
	        			Toast.makeText(getApplicationContext(),"You are "+bits_short+" bits short!",Toast.LENGTH_SHORT).show();
	        		}
        			else{
        				pet_status.bits-=Templates.get_food_list(section).get(position).cost;
        				
        				pet_status.perma_items.add(new Perma_Item(null,null,Templates.get_food_list(section).get(position).perma_item,RNG.random_range(0,view.getWidth()-PERMA_ITEM_SIZE),RNG.random_range(0,view.getHeight()-PERMA_ITEM_SIZE)));
	    				
        				Toast.makeText(getApplicationContext(),"You bought "+pet_status.name+" "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+"!",Toast.LENGTH_SHORT).show();
        				
        				pet_status.sleeping_wake_up();
	    				
	    				pet_status.queue_thought(Thought_Type.HAPPY);
	    				
	    				finish();
	    				return;
    				}
    			}
        		else{
        			if(Templates.get_food_list(section).get(position).primary_effect.equals("hunger") && pet_status.hunger>=Age_Tier.get_hunger_max(pet_status.age_tier)){
        				Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
        				Toast.makeText(getApplicationContext(),pet_status.name+" doesn't look hungry.",Toast.LENGTH_SHORT).show();
	        		}
	        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("happy") && pet_status.happy>=Pet_Status.HAPPY_MAX){
	        			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	        			Toast.makeText(getApplicationContext(),pet_status.name+" looks content.",Toast.LENGTH_SHORT).show();
	        		}
	        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("water") && pet_status.is_max_watered()){
	        			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	        			Toast.makeText(getApplicationContext(),pet_status.name+" doesn't look thirsty.",Toast.LENGTH_SHORT).show();
	        		}
	        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("energy") && pet_status.get_energy()>=pet_status.get_energy_max()){
	        			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	        			Toast.makeText(getApplicationContext(),pet_status.name+" seems energetic enough.",Toast.LENGTH_SHORT).show();
	        		}
	        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("strength") && pet_status.get_strength()>=pet_status.get_strength_max()){
	        			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	        			Toast.makeText(getApplicationContext(),pet_status.name+" seems strong enough.",Toast.LENGTH_SHORT).show();
	        		}
	        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("dexterity") && pet_status.get_dexterity()>=pet_status.get_dexterity_max()){
	        			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	        			Toast.makeText(getApplicationContext(),pet_status.name+" seems dexterous enough.",Toast.LENGTH_SHORT).show();
	        		}
	        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("stamina") && pet_status.get_stamina()>=pet_status.get_stamina_max()){
	        			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	        			Toast.makeText(getApplicationContext(),pet_status.name+" seems hearty enough.",Toast.LENGTH_SHORT).show();
	        		}
	        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("sick") && !pet_status.sick){
	        			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	        			Toast.makeText(getApplicationContext(),pet_status.name+" looks perfectly healthy.",Toast.LENGTH_SHORT).show();
	        		}
	        		else if(pet_status.bits<Templates.get_food_list(section).get(position).cost){
	        			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	        			
	        			int bits_short=Templates.get_food_list(section).get(position).cost-pet_status.bits;
	        			Toast.makeText(getApplicationContext(),"You are "+bits_short+" bits short!",Toast.LENGTH_SHORT).show();
	        		}
	        		else{
	        			boolean sound_played=false;
	        			
	        			pet_status.bits-=Templates.get_food_list(section).get(position).cost;
	        			
	        			pet_status.hunger+=Templates.get_food_list(section).get(position).hunger;
	        			pet_status.hunger_bound();
	        			
	        			pet_status.gain_weight(Templates.get_food_list(section).get(position).weight);
	    					        			
	        			pet_status.strength+=(short)Math.ceil((1.0f+(float)pet_status.get_strength_max()*0.01f)*(float)Templates.get_food_list(section).get(position).strength);
	        			pet_status.strength_bound();
	        			
	        			pet_status.dexterity+=(short)Math.ceil((1.0f+(float)pet_status.get_dexterity_max()*0.01f)*(float)Templates.get_food_list(section).get(position).dexterity);
	        			pet_status.dexterity_bound();
	        			
	        			pet_status.stamina+=(short)Math.ceil((1.0f+(float)pet_status.get_stamina_max()*0.01f)*(float)Templates.get_food_list(section).get(position).stamina);
	        			pet_status.stamina_bound();
	        			
	        			pet_status.energy+=(short)Math.ceil((1.0f+(float)pet_status.get_energy_max()*0.01f)*(float)Templates.get_food_list(section).get(position).energy);
	        			pet_status.energy_bound();
	        			
	        			pet_status.happy+=Templates.get_food_list(section).get(position).happy;
	        			pet_status.happy_bound();
	        			if(Templates.get_food_list(section).get(position).happy>0){
	        				pet_status.queue_thought(Thought_Type.HAPPY);
	        			}
	        			else if(Templates.get_food_list(section).get(position).happy<0){
	        				pet_status.queue_thought(Thought_Type.SAD);
	        			}
	        			
	        			if(Templates.get_food_list(section).get(position).is_water){
	        				Sound_Manager.play_sound(Sound.DRINK);
	        				sound_played=true;
	        				
	        				pet_status.thirst=Pet_Status.THIRST_MAX;
	        			}
	        			
	        			if(Templates.get_food_list(section).get(position).is_medicine){
	        				Sound_Manager.play_sound(Sound.MEDICINE);
	        				sound_played=true;
	        				
	        				pet_status.sick=false;
	        			}
	        			
	        			if(!sound_played){
	        				Sound_Manager.play_sound(Sound.EAT);
	        			}
	        			
	        			if(Templates.get_food_list(section).get(position).buff.equals("hunger")){
	        				pet_status.buff_hunger=Pet_Status.FOOD_BUFF_SECONDS_HUNGER;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("thirst")){
	        				pet_status.buff_thirst=Pet_Status.FOOD_BUFF_SECONDS_THIRST;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("poop")){
	        				pet_status.buff_poop=Pet_Status.FOOD_BUFF_SECONDS_POOP;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("dirty")){
	        				pet_status.buff_dirty=Pet_Status.FOOD_BUFF_SECONDS_DIRTY;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("weight")){
	        				pet_status.buff_weight=Pet_Status.FOOD_BUFF_SECONDS_WEIGHT;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("sick")){
	        				pet_status.buff_sick=Pet_Status.FOOD_BUFF_SECONDS_SICK;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("happy")){
	        				pet_status.buff_happy=Pet_Status.FOOD_BUFF_SECONDS_HAPPY;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("energy_regen")){
	        				pet_status.buff_energy_regen=Pet_Status.FOOD_BUFF_SECONDS_ENERGY_REGEN;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("strength_regen")){
	        				pet_status.buff_strength_regen=Pet_Status.FOOD_BUFF_SECONDS_STRENGTH_REGEN;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("dexterity_regen")){
	        				pet_status.buff_dexterity_regen=Pet_Status.FOOD_BUFF_SECONDS_DEXTERITY_REGEN;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("stamina_regen")){
	        				pet_status.buff_stamina_regen=Pet_Status.FOOD_BUFF_SECONDS_STAMINA_REGEN;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("energy_max")){
	        				pet_status.buff_energy_max=Pet_Status.FOOD_BUFF_SECONDS_ENERGY_MAX;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("strength_max")){
	        				pet_status.buff_strength_max=Pet_Status.FOOD_BUFF_SECONDS_STRENGTH_MAX;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("dexterity_max")){
	        				pet_status.buff_dexterity_max=Pet_Status.FOOD_BUFF_SECONDS_DEXTERITY_MAX;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("stamina_max")){
	        				pet_status.buff_stamina_max=Pet_Status.FOOD_BUFF_SECONDS_STAMINA_MAX;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("death")){
	        				pet_status.buff_death=Pet_Status.FOOD_BUFF_SECONDS_DEATH;
	        			}
	        			else if(Templates.get_food_list(section).get(position).buff.equals("magic_find")){
	        				pet_status.buff_magic_find=Pet_Status.FOOD_BUFF_SECONDS_MAGIC_FIND;
	        			}
	        			
	        			if(Templates.get_food_list(section).get(position).primary_effect.equals("hunger")){
	        				Toast.makeText(getApplicationContext(),"You fed "+pet_status.name+" "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+".",Toast.LENGTH_SHORT).show();
		        		}
		        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("happy")){
		        			Toast.makeText(getApplicationContext(),"You fed "+pet_status.name+" "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+" as a special treat!",Toast.LENGTH_SHORT).show();
		        		}
		        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("water")){
		        			Toast.makeText(getApplicationContext(),"You gave "+pet_status.name+" "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+" to drink.",Toast.LENGTH_SHORT).show();
		        		}
		        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("energy")){
		        			Toast.makeText(getApplicationContext(),"You gave "+pet_status.name+" "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+" to replenish some energy.",Toast.LENGTH_SHORT).show();
		        		}
		        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("strength")){
		        			Toast.makeText(getApplicationContext(),"You gave "+pet_status.name+" "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+" to replenish some strength.",Toast.LENGTH_SHORT).show();
		        		}
		        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("dexterity")){
		        			Toast.makeText(getApplicationContext(),"You gave "+pet_status.name+" "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+" to replenish some dexterity.",Toast.LENGTH_SHORT).show();
		        		}
		        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("stamina")){
		        			Toast.makeText(getApplicationContext(),"You gave "+pet_status.name+" "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+" to replenish some stamina.",Toast.LENGTH_SHORT).show();
		        		}
		        		else if(Templates.get_food_list(section).get(position).primary_effect.equals("sick")){
		        			Toast.makeText(getApplicationContext(),"You treated "+pet_status.name+" with "+Templates.get_food_list(section).get(position).prefix_article+" "+Templates.get_food_list(section).get(position).name+".",Toast.LENGTH_SHORT).show();
		        		}
	        			
	        			//If the pet is a pre-teen, their evolutionary branch has not yet been determined.
	    				if(pet_status.age_tier==Age_Tier.EGG || pet_status.age_tier==Age_Tier.BABY
	    						|| pet_status.age_tier==Age_Tier.CHILD){
	    					//Increment the category of the food.	    					
	    					Integer integer=pet_status.food_categories.get(Templates.get_food_list(section).get(position).category);
	    					integer=Integer.valueOf(integer.intValue()+1);
	    					pet_status.food_categories.set(Templates.get_food_list(section).get(position).category,integer);
	    				}
	        			
	        			//If the pet is not yet a child, and thus doesn't yet have a favorite food.
	        			if(pet_status.age_tier==Age_Tier.EGG || pet_status.age_tier==Age_Tier.BABY){
	        				if(Templates.get_food_list(section).get(position).can_be_favorite){
	        					//Is this food already in the list?
	        					boolean already_present=false;
	        					
	        					for(int i=0;i<pet_status.food_fed.size();i++){
	        						if(pet_status.food_fed.get(i).equals(Templates.get_food_list(section).get(position).name)){
	        							already_present=true;
	        							break;
	        						}
	        					}
	        					
	        					if(!already_present){
	        						pet_status.food_fed.add(Templates.get_food_list(section).get(position).name);
	        					}
	        				}
	        			}
	        			//Otherwise, the pet has a favorite food.
	        			else{
	        				//If this food is the pet's favorite.
	        				if(pet_status.favorite_food.equals(Templates.get_food_list(section).get(position).name)){
	        					pet_status.happy+=Pet_Status.HAPPY_GAIN_FAVORITE_FOOD;
	                			pet_status.happy_bound();
	                			
	                			pet_status.queue_thought(Thought_Type.HAPPY);
	        				}
	        			}
	        			
	        			pet_status.sleeping_wake_up();
	        			
	        			TextView tv=null;
	        	    	ProgressBar pb=null;
	        	    	String message="";
	        	    	
	        	    	String sick_string="";
	        	    	if(pet_status.sick){
	        	    		sick_string="<    Sick!    >";
	        	    	}
	        			
	        			tv=(TextView)findViewById(R.id.text_store_bits);
	        	    	tv.setText(getResources().getString(R.string.status_bits)+pet_status.bits);
	        	    	
	        	    	tv=(TextView)findViewById(R.id.text_store_sick);
	        	    	tv.setText(sick_string);
	        	    	if(section!=Store_Section.TREATMENTS || !pet_status.sick){
	        	    		tv.setVisibility(TextView.GONE);
	        	    	}
	        	    	else{
	        	    		tv.setVisibility(TextView.VISIBLE);
	        	    	}
	        	    	
	        	    	pb=(ProgressBar)findViewById(R.id.bar_store_hunger);
	        	    	pb.setMax(Age_Tier.get_hunger_max(pet_status.age_tier));
	        	    	pb.setProgress(pet_status.hunger);
	        	    	tv=(TextView)findViewById(R.id.text_store_hunger);
	        	    	tv.setText(pet_status.hunger+"/"+Age_Tier.get_hunger_max(pet_status.age_tier));
	        	    	
	        	    	pb=(ProgressBar)findViewById(R.id.bar_store_happy);
	        	    	pb.setMax(Pet_Status.HAPPY_MAX+Pet_Status.HAPPY_MAX);
	        	    	pb.setProgress(pet_status.happy+Pet_Status.HAPPY_MAX);
	        	    	tv=(TextView)findViewById(R.id.text_store_happy);
	        	    	tv.setText(pet_status.happy+"/"+Pet_Status.HAPPY_MAX);
	        	    	
	        	    	pb=(ProgressBar)findViewById(R.id.bar_store_thirst);
	        	    	pb.setMax(Pet_Status.THIRST_MAX);
	        	    	pb.setProgress(pet_status.thirst);
	        	    	tv=(TextView)findViewById(R.id.text_store_thirst);
	        	    	tv.setText(pet_status.thirst+"/"+Pet_Status.THIRST_MAX);
	        	    	
	        	    	pb=(ProgressBar)findViewById(R.id.bar_store_strength);
	        	    	pb.setMax(pet_status.get_strength_max());
	        	    	pb.setProgress(pet_status.get_strength());
	        	    	tv=(TextView)findViewById(R.id.text_store_strength);
	        	    	message=pet_status.get_strength()+"/"+pet_status.get_strength_max();
	        	    	if(pet_status.get_strength_upgrade()>0){
	        	    		tv.setTextColor(getResources().getColor(R.color.font_upgrade));
	        	    		
	        	    		message+=" [+"+pet_status.get_strength_upgrade()+"]";
	        	    	}
	        	    	else{
	        	    		tv.setTextColor(getResources().getColor(R.color.font));
	        	    	}
	        	    	tv.setText(message);
	        	    	
	        	    	pb=(ProgressBar)findViewById(R.id.bar_store_dexterity);
	        	    	pb.setMax(pet_status.get_dexterity_max());
	        	    	pb.setProgress(pet_status.get_dexterity());
	        	    	tv=(TextView)findViewById(R.id.text_store_dexterity);
	        	    	message=pet_status.get_dexterity()+"/"+pet_status.get_dexterity_max();
	        	    	if(pet_status.get_dexterity_upgrade()>0){
	        	    		tv.setTextColor(getResources().getColor(R.color.font_upgrade));
	        	    		
	        	    		message+=" [+"+pet_status.get_dexterity_upgrade()+"]";
	        	    	}
	        	    	else{
	        	    		tv.setTextColor(getResources().getColor(R.color.font));
	        	    	}
	        	    	tv.setText(message);
	        	    	
	        	    	pb=(ProgressBar)findViewById(R.id.bar_store_stamina);
	        	    	pb.setMax(pet_status.get_stamina_max());
	        	    	pb.setProgress(pet_status.get_stamina());
	        	    	tv=(TextView)findViewById(R.id.text_store_stamina);
	        	    	message=pet_status.get_stamina()+"/"+pet_status.get_stamina_max();
	        	    	if(pet_status.get_stamina_upgrade()>0){
	        	    		tv.setTextColor(getResources().getColor(R.color.font_upgrade));
	        	    		
	        	    		message+=" [+"+pet_status.get_stamina_upgrade()+"]";
	        	    	}
	        	    	else{
	        	    		tv.setTextColor(getResources().getColor(R.color.font));
	        	    	}
	        	    	tv.setText(message);
	        	    	
	        	    	pb=(ProgressBar)findViewById(R.id.bar_store_energy);
	        	    	pb.setMax(pet_status.get_energy_max());
	        	    	pb.setProgress(pet_status.get_energy());
	        	    	tv=(TextView)findViewById(R.id.text_store_energy);
	        	    	message=pet_status.get_energy()+"/"+pet_status.get_energy_max();
	        	    	if(pet_status.get_energy_upgrade()>0){
	        	    		tv.setTextColor(getResources().getColor(R.color.font_upgrade));
	        	    		
	        	    		message+=" [+"+pet_status.get_energy_upgrade()+"]";
	        	    	}
	        	    	else{
	        	    		tv.setTextColor(getResources().getColor(R.color.font));
	        	    	}
	        	    	tv.setText(message);
	        	    	
	        	    	showDialog(DIALOG_CONSUME);
	        	    	
	        	    	ImageViewFood ivf=(ImageViewFood)dialog_consume.findViewById(R.id.food_image_consume);
	        	    	ImageView iv=(ImageView)dialog_consume.findViewById(R.id.image_food_consume_pet);
	        	    	
	        	    	iv.setImageDrawable(Pet_Type.get_drawable(getResources(),pet_status.type,false,1.0f,1.0f));
						iv.setColorFilter(pet_status.color, PorterDuff.Mode.MULTIPLY);
	        	    	
	        	    	Bitmap bitmap;
	        	        bitmap=BitmapFactory.decodeResource(getResources(),Templates.get_food_list(section).get(position).resource_id);
	        	        BitmapDrawable bd=new BitmapDrawable(getResources(),bitmap);
	        	        ivf.setBackground(bd);
	        	        ivf.set_as(Activity_Store.this);
	        	        
	        	        if(Templates.get_food_list(section).get(position).store_section==Store_Section.FOOD){
	        	        	ivf.startAnimation(AnimationUtils.loadAnimation(Activity_Store.this,R.anim.consume_food));
	        	        }
	        	        else if(Templates.get_food_list(section).get(position).store_section==Store_Section.DRINKS){
	        	        	ivf.startAnimation(AnimationUtils.loadAnimation(Activity_Store.this,R.anim.consume_drink));
	        	        }
	        	        else if(Templates.get_food_list(section).get(position).store_section==Store_Section.TREATMENTS){
	        	        	ivf.startAnimation(AnimationUtils.loadAnimation(Activity_Store.this,R.anim.consume_treatment));
	        	        }
	        		}
        		}
        	}
        });
	}
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_store));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	if(move_direction==Direction.LEFT){
    		overridePendingTransition(R.anim.transition_in_right,R.anim.transition_out_right);
    	}
    	else if(move_direction==Direction.RIGHT){
    		overridePendingTransition(R.anim.transition_in_left,R.anim.transition_out_left);
    	}
    	else{
    		overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	}
    	
    	//We have used a move animation, so reset it.
    	move_direction=Direction.NONE;
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
    	
    	Font.set_typeface((Button)findViewById(R.id.button_store_back));
    	Font.set_typeface((Button)findViewById(R.id.button_store_forward));
    	
    	TextView tv=null;
    	ProgressBar pb=null;
    	LinearLayout ll=null;
    	String message="";
    	
    	String sick_string="";
    	if(pet_status.sick){
    		sick_string="<    Sick!    >";
    	}
    	
    	tv=(TextView)findViewById(R.id.text_store_bits);
    	tv.setText(this.getString(R.string.status_bits)+pet_status.bits);
    	Font.set_typeface(tv);
    	
    	tv=(TextView)findViewById(R.id.text_store_sick);
    	tv.setText(sick_string);
    	Font.set_typeface(tv);
    	if(section!=Store_Section.TREATMENTS || !pet_status.sick){
    		tv.setVisibility(TextView.GONE);
    	}
    	
    	pb=(ProgressBar)findViewById(R.id.bar_store_hunger);
    	pb.setMax(Age_Tier.get_hunger_max(pet_status.age_tier));
    	pb.setProgress(pet_status.hunger);
    	tv=(TextView)findViewById(R.id.text_store_title_hunger);
    	tv.setText(Pet_Status.get_buff_name("hunger")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_store_hunger);
    	tv.setText(pet_status.hunger+"/"+Age_Tier.get_hunger_max(pet_status.age_tier));
    	Font.set_typeface(tv);
    	tv.setVisibility(TextView.INVISIBLE);
    	if(section!=Store_Section.FOOD){
    		ll=(LinearLayout)findViewById(R.id.ll_store_hunger);
    		ll.setVisibility(LinearLayout.GONE);
    	}
    	
    	pb=(ProgressBar)findViewById(R.id.bar_store_happy);
    	pb.setMax(Pet_Status.HAPPY_MAX+Pet_Status.HAPPY_MAX);
    	pb.setProgress(pet_status.happy+Pet_Status.HAPPY_MAX);
    	tv=(TextView)findViewById(R.id.text_store_title_happy);
    	tv.setText(Pet_Status.get_buff_name("happy")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_store_happy);
    	tv.setText(pet_status.happy+"/"+Pet_Status.HAPPY_MAX);
    	Font.set_typeface(tv);
    	tv.setVisibility(TextView.INVISIBLE);
    	if(section!=Store_Section.FOOD){
    		ll=(LinearLayout)findViewById(R.id.ll_store_happy);
    		ll.setVisibility(LinearLayout.GONE);
    	}
    	
    	pb=(ProgressBar)findViewById(R.id.bar_store_thirst);
    	pb.setMax(Pet_Status.THIRST_MAX);
    	pb.setProgress(pet_status.thirst);
    	tv=(TextView)findViewById(R.id.text_store_title_thirst);
    	tv.setText(Pet_Status.get_buff_name("thirst")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_store_thirst);
    	tv.setText(pet_status.thirst+"/"+Pet_Status.THIRST_MAX);
    	Font.set_typeface(tv);
    	tv.setVisibility(TextView.INVISIBLE);
    	if(section!=Store_Section.DRINKS){
    		ll=(LinearLayout)findViewById(R.id.ll_store_thirst);
    		ll.setVisibility(LinearLayout.GONE);
    	}
    	
    	pb=(ProgressBar)findViewById(R.id.bar_store_strength);
    	pb.setMax(pet_status.get_strength_max());
    	pb.setProgress(pet_status.get_strength());
    	tv=(TextView)findViewById(R.id.text_store_title_strength);
    	tv.setText(Pet_Status.get_buff_name("strength_max")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_store_strength);
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
    	if(section!=Store_Section.TREATMENTS){
    		ll=(LinearLayout)findViewById(R.id.ll_store_strength);
    		ll.setVisibility(LinearLayout.GONE);
    	}
    	
    	pb=(ProgressBar)findViewById(R.id.bar_store_dexterity);
    	pb.setMax(pet_status.get_dexterity_max());
    	pb.setProgress(pet_status.get_dexterity());
    	tv=(TextView)findViewById(R.id.text_store_title_dexterity);
    	tv.setText(Pet_Status.get_buff_name("dexterity_max")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_store_dexterity);
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
    	if(section!=Store_Section.TREATMENTS){
    		ll=(LinearLayout)findViewById(R.id.ll_store_dexterity);
    		ll.setVisibility(LinearLayout.GONE);
    	}
    	
    	pb=(ProgressBar)findViewById(R.id.bar_store_stamina);
    	pb.setMax(pet_status.get_stamina_max());
    	pb.setProgress(pet_status.get_stamina());
    	tv=(TextView)findViewById(R.id.text_store_title_stamina);
    	tv.setText(Pet_Status.get_buff_name("stamina_max")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_store_stamina);
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
    	if(section!=Store_Section.TREATMENTS){
    		ll=(LinearLayout)findViewById(R.id.ll_store_stamina);
    		ll.setVisibility(LinearLayout.GONE);
    	}
    	
    	pb=(ProgressBar)findViewById(R.id.bar_store_energy);
    	pb.setMax(pet_status.get_energy_max());
    	pb.setProgress(pet_status.get_energy());
    	tv=(TextView)findViewById(R.id.text_store_title_energy);
    	tv.setText(Pet_Status.get_buff_name("energy_max")+": ");
    	Font.set_typeface(tv);
    	tv=(TextView)findViewById(R.id.text_store_energy);
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
    	if(section!=Store_Section.TREATMENTS){
    		ll=(LinearLayout)findViewById(R.id.ll_store_energy);
    		ll.setVisibility(LinearLayout.GONE);
    	}
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	close_dialogs();
    	
    	if(move_direction==Direction.LEFT){
    	}
    	else if(move_direction==Direction.RIGHT){
    	}
    	else{
    		overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	}
    	
    	//We have used a move animation, so reset it.
    	move_direction=Direction.NONE;
    	
    	StorageManager.save_pet_status(this,pet_status);
    }
	@Override
    protected Dialog onCreateDialog(int id){
    	AlertDialog.Builder builder=null;
    	LayoutInflater inflater=null;
    	View layout=null;
    	
        switch(id){
        case DIALOG_CONSUME:
        	inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	layout=inflater.inflate(R.layout.dialog_store_consume,(ViewGroup)findViewById(R.id.root_dialog_store_consume));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_consume=builder.create();
			return dialog_consume;
        default:
            return null;
        }
    }
	
	public void button_back(View view){
		section--;
		if(section<Store_Section.BEGIN){
			section=Store_Section.END-1;
		}
		move_direction=Direction.LEFT;
		
		Intent intent=getIntent();
    	
    	Bundle bundle=new Bundle();
    	bundle.putInt(getPackageName()+"section",section);
    	bundle.putInt(getPackageName()+"move_direction",move_direction);
    	
    	intent.replaceExtras(bundle);
    	
    	finish();
    				    	
    	startActivity(intent);
	}
	
	public void button_forward(View view){
		section++;
		if(section>=Store_Section.END){
			section=Store_Section.BEGIN;
		}
		move_direction=Direction.RIGHT;
		
		Intent intent=getIntent();
    	
    	Bundle bundle=new Bundle();
    	bundle.putInt(getPackageName()+"section",section);
    	bundle.putInt(getPackageName()+"move_direction",move_direction);
    	
    	intent.replaceExtras(bundle);
    	
    	finish();
    	
    	startActivity(intent);
	}
	
	void close_dialogs(){
		if(dialog_consume.isShowing()){
			dismissDialog(DIALOG_CONSUME);
	    }
	}
}
