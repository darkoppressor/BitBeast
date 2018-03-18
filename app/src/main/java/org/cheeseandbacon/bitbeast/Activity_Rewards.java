/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Activity_Rewards extends AppCompatActivity {
	static final int DIALOG_DETAILS=0;
	
	private Pet_Status pet_status;
	
	AlertDialog dialog_details;
	
	int levels_gained;
	String equipment_gained;
	
	public static Bundle rewards_bundle(Context context,String package_name,Pet_Status status,int sound,String message_begin,String message_end,long experience_points,int bits,boolean add_bits,int equip_chance,int base_level){
		String bundle_equipment_gained=status.new_equipment(equip_chance,base_level,"");
		
		int new_item_color=0;
		if(bundle_equipment_gained.length()>0){
			new_item_color=status.equipment.get(0).get_rarity_color(context);
		}
		
		int level_before_exp=status.level;
		
		long experience_gained=status.gain_experience(experience_points,false);
		
		int bundle_levels_gained=status.level-level_before_exp;
		
		if(bundle_levels_gained>0){
			sound=Sound.LEVEL_UP;
		}
		
		if(add_bits){
			status.bits+=bits;
			status.bits_bound();
		}
		
		Bundle bundle=new Bundle();
		bundle.putInt(package_name+"sound",sound);
		bundle.putString(package_name+"message_begin",message_begin);
		bundle.putString(package_name+"message_end",message_end);
		bundle.putLong(package_name+"experience_gained",experience_gained);
		bundle.putInt(package_name+"levels_gained",bundle_levels_gained);
		bundle.putInt(package_name+"bits_gained",bits);
		bundle.putInt(package_name+"new_item_color",new_item_color);
		bundle.putString(package_name+"equipment_gained",bundle_equipment_gained);
		
		return bundle;
	}
	
	//Call this from a game running in GameView, and it will pass things to BitBeast with a handler.
	public static void give_rewards_handler(Context context,Handler handler,String package_name,Pet_Status status,int sound,String message_begin,String message_end,long experience_points,int bits,boolean add_bits,int equip_chance,int base_level){
		Bundle bundle=rewards_bundle(context,package_name,status,sound,message_begin,message_end,experience_points,bits,add_bits,equip_chance,base_level);

        StorageManager.save_pet_status(context,status);

		Message msg=handler.obtainMessage();
		msg.what=BitBeast.HANDLER_REWARDS;
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	//Call this from an activity.
	public static void give_rewards(Context context,String package_name,Pet_Status status,int sound,String message_begin,String message_end,long experience_points,int bits,boolean add_bits,int equip_chance,int base_level){
		Bundle bundle=rewards_bundle(context,package_name,status,sound,message_begin,message_end,experience_points,bits,add_bits,equip_chance,base_level);
		
		StorageManager.save_pet_status(context,status);
		
		int play_sound=bundle.getInt(package_name+"sound");
		bundle.remove(package_name+"sound");
		
		if(play_sound!=-1){
			Sound_Manager.playSound(context, play_sound);
		}
    	
    	Intent intent=new Intent(context,Activity_Rewards.class);
    	intent.putExtras(bundle);
    	context.startActivity(intent);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewards);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Pet_Status pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
        
        Bundle bundle=getIntent().getExtras();
        
        String message_begin=bundle.getString(getPackageName()+"message_begin");
        String message_end=bundle.getString(getPackageName()+"message_end");
        long experience_gained=bundle.getLong(getPackageName()+"experience_gained");
        levels_gained=bundle.getInt(getPackageName()+"levels_gained");
        int bits_gained=bundle.getInt(getPackageName()+"bits_gained");
        int new_item_color=bundle.getInt(getPackageName()+"new_item_color");
        equipment_gained=bundle.getString(getPackageName()+"equipment_gained");
        
        String info=message_begin+"\n\n";
        
        if(experience_gained>0){
        	String plural="";
        	if(experience_gained>1){
        		plural="s";
        	}
			info+=pet_status.name+" gained "+experience_gained+" experience point"+plural+"!\n\n";
		}
        
        if(levels_gained>0){
			info+=pet_status.name+" has leveled up!\n"+pet_status.name+" is now level "+pet_status.level+"!\n\n";
		}
        
        if(bits_gained>0){
        	String plural="";
        	if(bits_gained>1){
        		plural="s";
        	}
			info+=pet_status.name+" earned "+bits_gained+" bit"+plural+"!\n\n";
		}
        
        info+=message_end;
        
        if(message_end.length()==0){
        	info=info.trim();
        }
        
        showDialog(DIALOG_DETAILS);
        dismissDialog(DIALOG_DETAILS);
        
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.rewards_info));
    	Font.set_typeface(getAssets(), (TextView)findViewById(R.id.rewards_text_item));
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_rewards_item));
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_rewards_spend));
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_rewards_ok));
        
        Font.set_typeface(getAssets(), (TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_ok));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_equip));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_unequip));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_sell));
    	
		Button b=null;
		
		b=(Button)findViewById(R.id.button_rewards_item);
        if(equipment_gained.length()>0){
        	String message="<font color='"+String.format("#%06x",0xFFFFFF&new_item_color)+"'>";
        	message+=equipment_gained;
        	message+="</font>";
        	
        	b.setText(Html.fromHtml(Strings.newline_to_br(message)));
        }
		
		/**b=(Button)findViewById(R.id.button_rewards_spend);
        b.setNextFocusUpId(R.id.button_rewards_ok);
        
        b=(Button)findViewById(R.id.button_rewards_ok);
        b.setNextFocusDownId(R.id.button_rewards_spend);*/
        
        TextView tv=(TextView)findViewById(R.id.rewards_info);
        tv.setText(Html.fromHtml(Strings.newline_to_br(info)));
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_rewards));
    	System.gc();
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	set_dialog_buttons();
    	
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
        
        pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
    	
    	Button b=null;
    	
    	b=(Button)findViewById(R.id.button_rewards_spend);
        if(pet_status.stat_points > 0){
        	b.setVisibility(Button.VISIBLE);
        }
        else{
        	b.setVisibility(Button.GONE);
        }
        
        b=(Button)findViewById(R.id.button_rewards_item);
        if(equipment_gained.length()>0){
        	b.setVisibility(Button.VISIBLE);
        	((TextView)findViewById(R.id.rewards_text_item)).setVisibility(TextView.VISIBLE);
        }
        else{
        	b.setVisibility(Button.GONE);
        	((TextView)findViewById(R.id.rewards_text_item)).setVisibility(TextView.GONE);
        }
        
        ((Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_unequip)).setVisibility(Button.GONE);
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	
    	close_dialogs();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	StorageManager.save_pet_status(this,pet_status);
    }
    @Override
    protected Dialog onCreateDialog(int id){
    	AlertDialog.Builder builder=null;
    	LayoutInflater inflater=null;
    	View layout=null;
    	
        switch(id){
        case DIALOG_DETAILS:
        	inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	layout=inflater.inflate(R.layout.dialog_inventory_details,(ViewGroup)findViewById(R.id.root_dialog_inventory_details));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_details=builder.create();
			return dialog_details;
        default:
            return null;
        }
    }
    
    public void button_item(View view){
    	String message=pet_status.equipment.get(0).get_details(getApplicationContext(),pet_status.equipment_slots);
		
		TextView tv=(TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message);
		tv.setText(Html.fromHtml(Strings.newline_to_br(message)));
		
		tv.setCompoundDrawablesWithIntrinsicBounds(0,0,pet_status.equipment.get(0).get_template(getApplicationContext()).resource_id,0);
		
		showDialog(DIALOG_DETAILS);
    }
    
    public void button_spend(View view){
        Button b=(Button)findViewById(R.id.button_rewards_item);
        if (b.getVisibility() != View.VISIBLE) {
    	    equipment_gained = "";
        }

		Intent intent=new Intent(this,Activity_Spend_Stat_Points.class);
    	startActivity(intent);
    }
    
    public void button_ok(View view){
    	finish();
	}
    
    public void sell_item(){
    	Sound_Manager.playSound(this, Sound.ITEM_SOLD);
    	
    	pet_status.bits+=pet_status.equipment.get(0).bits;
		pet_status.bits_bound();
		
		pet_status.equipment.remove(0);

		Button b=(Button)findViewById(R.id.button_rewards_item);
        b.setVisibility(Button.GONE);
        ((TextView)findViewById(R.id.rewards_text_item)).setVisibility(TextView.GONE);
	}
    
    public void equip_item(){
    	Sound_Manager.playSound(this, Sound.EQUIPPED);
    	
    	String slot=pet_status.equipment.get(0).get_template(this).slot;
    	
    	if(pet_status.equipment_slots.get(Equipment.string_to_slot(slot))==null){
			pet_status.equipment_slots.set(Equipment.string_to_slot(slot),pet_status.equipment.get(0));
			
			pet_status.equipment.remove(0);
		}
		else{
			pet_status.equipment.add(0,pet_status.equipment_slots.get(Equipment.string_to_slot(slot)));
			
			pet_status.equipment_slots.set(Equipment.string_to_slot(slot),pet_status.equipment.get(1));
			
			pet_status.equipment.remove(1);
		}

        Button b=(Button)findViewById(R.id.button_rewards_item);
        b.setVisibility(Button.GONE);
        ((TextView)findViewById(R.id.rewards_text_item)).setVisibility(TextView.GONE);
	}
    
    public void close_dialogs(){
		if(dialog_details.isShowing()){
			dismissDialog(DIALOG_DETAILS);
	    }
	}
    
    public void set_dialog_buttons(){
		((Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_ok)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(dialog_details.isShowing()){
					dismissDialog(DIALOG_DETAILS);
			    }
			}
		});
		((Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_equip)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				equip_item();
				
				if(dialog_details.isShowing()){
					dismissDialog(DIALOG_DETAILS);
			    }
			}
		});
		((Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_sell)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				sell_item();
				
				if(dialog_details.isShowing()){
					dismissDialog(DIALOG_DETAILS);
			    }
			}
		});
	}
}
