/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Activity_Equip extends AppCompatActivity {
	static final int DIALOG_DETAILS=0;
	
	private Pet_Status pet_status;
	
	AlertDialog dialog_details;
	
	int detail_position;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equip);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        showDialog(DIALOG_DETAILS);
        dismissDialog(DIALOG_DETAILS);
        
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_equip_inventory));
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_equip_weapon));
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_equip_head));
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_equip_chest));
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_equip_feet));
    	Font.set_typeface(getAssets(), (TextView)findViewById(R.id.equip_message_weapon));
    	Font.set_typeface(getAssets(), (TextView)findViewById(R.id.equip_message_head));
    	Font.set_typeface(getAssets(), (TextView)findViewById(R.id.equip_message_chest));
    	Font.set_typeface(getAssets(), (TextView)findViewById(R.id.equip_message_feet));
    	
    	Font.set_typeface(getAssets(), (TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_ok));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_equip));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_unequip));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_sell));
    	
		Button b=null;
		
		b=(Button)findViewById(R.id.button_equip_inventory);
        b.setNextFocusUpId(R.id.button_equip_feet);
        
        b=(Button)findViewById(R.id.button_equip_feet);
        b.setNextFocusDownId(R.id.button_equip_inventory);
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_equip));
    	System.gc();
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	set_dialog_buttons();
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
        
        update();
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
    
    public void update(){
    	detail_position=-1;
    	
		Button b=null;
    	
		b=(Button)findViewById(R.id.button_equip_weapon);
    	if(pet_status.equipment_slots.get(Equipment.SLOT_WEAPON)!=null){
    		b.setText(Html.fromHtml(Strings.newline_to_br(pet_status.equipment_slots.get(Equipment.SLOT_WEAPON).get_slot_string(this))));
    		
    		b.setCompoundDrawablesWithIntrinsicBounds(pet_status.equipment_slots.get(Equipment.SLOT_WEAPON).get_template(this).resource_id,0,0,0);
    	}
    	else{
    		b.setText("Empty");
    		
    		b.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    	}
    	b.setOnLongClickListener(new OnLongClickListener(){
        	public boolean onLongClick(View view){
        		if(pet_status.equipment_slots.get(Equipment.SLOT_WEAPON)!=null){
	        		String message=pet_status.equipment_slots.get(Equipment.SLOT_WEAPON).get_details(getApplicationContext(),pet_status.equipment_slots);
	    			
	    			TextView tv=(TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message);
	    			tv.setText(Html.fromHtml(Strings.newline_to_br(message)));
	    			
	    			tv.setCompoundDrawablesWithIntrinsicBounds(0,0,pet_status.equipment_slots.get(Equipment.SLOT_WEAPON).get_template(getApplicationContext()).resource_id,0);
	    			
	    			detail_position=Equipment.SLOT_WEAPON;
	    			
	    			showDialog(DIALOG_DETAILS);
	        		
	        		return true;
        		}
        		else{
        			return false;
        		}
        	}
        });
    	
    	b=(Button)findViewById(R.id.button_equip_head);
    	if(pet_status.equipment_slots.get(Equipment.SLOT_HEAD)!=null){
    		b.setText(Html.fromHtml(Strings.newline_to_br(pet_status.equipment_slots.get(Equipment.SLOT_HEAD).get_slot_string(this))));
    		
    		b.setCompoundDrawablesWithIntrinsicBounds(pet_status.equipment_slots.get(Equipment.SLOT_HEAD).get_template(this).resource_id,0,0,0);
    	}
    	else{
    		b.setText("Empty");
    		
    		b.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    	}
    	b.setOnLongClickListener(new OnLongClickListener(){
        	public boolean onLongClick(View view){
        		if(pet_status.equipment_slots.get(Equipment.SLOT_HEAD)!=null){
	        		String message=pet_status.equipment_slots.get(Equipment.SLOT_HEAD).get_details(getApplicationContext(),pet_status.equipment_slots);
	    			
	    			TextView tv=(TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message);
	    			tv.setText(Html.fromHtml(Strings.newline_to_br(message)));
	    			
	    			tv.setCompoundDrawablesWithIntrinsicBounds(0,0,pet_status.equipment_slots.get(Equipment.SLOT_HEAD).get_template(getApplicationContext()).resource_id,0);
	    			
	    			detail_position=Equipment.SLOT_HEAD;
	    			
	    			showDialog(DIALOG_DETAILS);
	        		
	        		return true;
        		}
        		else{
        			return false;
        		}
        	}
        });
    	
    	b=(Button)findViewById(R.id.button_equip_chest);
    	if(pet_status.equipment_slots.get(Equipment.SLOT_CHEST)!=null){
    		b.setText(Html.fromHtml(Strings.newline_to_br(pet_status.equipment_slots.get(Equipment.SLOT_CHEST).get_slot_string(this))));
    		
    		b.setCompoundDrawablesWithIntrinsicBounds(pet_status.equipment_slots.get(Equipment.SLOT_CHEST).get_template(this).resource_id,0,0,0);
    	}
    	else{
    		b.setText("Empty");
    		
    		b.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    	}
    	b.setOnLongClickListener(new OnLongClickListener(){
        	public boolean onLongClick(View view){
        		if(pet_status.equipment_slots.get(Equipment.SLOT_CHEST)!=null){
	        		String message=pet_status.equipment_slots.get(Equipment.SLOT_CHEST).get_details(getApplicationContext(),pet_status.equipment_slots);
	    			
	    			TextView tv=(TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message);
	    			tv.setText(Html.fromHtml(Strings.newline_to_br(message)));
	    			
	    			tv.setCompoundDrawablesWithIntrinsicBounds(0,0,pet_status.equipment_slots.get(Equipment.SLOT_CHEST).get_template(getApplicationContext()).resource_id,0);
	    			
	    			detail_position=Equipment.SLOT_CHEST;
	    			
	    			showDialog(DIALOG_DETAILS);
	        		
	        		return true;
        		}
        		else{
        			return false;
        		}
        	}
        });
    	
    	b=(Button)findViewById(R.id.button_equip_feet);
    	if(pet_status.equipment_slots.get(Equipment.SLOT_FEET)!=null){
    		b.setText(Html.fromHtml(Strings.newline_to_br(pet_status.equipment_slots.get(Equipment.SLOT_FEET).get_slot_string(this))));
    		
    		b.setCompoundDrawablesWithIntrinsicBounds(pet_status.equipment_slots.get(Equipment.SLOT_FEET).get_template(this).resource_id,0,0,0);
    	}
    	else{
    		b.setText("Empty");
    		
    		b.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    	}
    	b.setOnLongClickListener(new OnLongClickListener(){
        	public boolean onLongClick(View view){
        		if(pet_status.equipment_slots.get(Equipment.SLOT_FEET)!=null){
	        		String message=pet_status.equipment_slots.get(Equipment.SLOT_FEET).get_details(getApplicationContext(),pet_status.equipment_slots);
	    			
	    			TextView tv=(TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message);
	    			tv.setText(Html.fromHtml(Strings.newline_to_br(message)));
	    			
	    			tv.setCompoundDrawablesWithIntrinsicBounds(0,0,pet_status.equipment_slots.get(Equipment.SLOT_FEET).get_template(getApplicationContext()).resource_id,0);
	    			
	    			detail_position=Equipment.SLOT_FEET;
	    			
	    			showDialog(DIALOG_DETAILS);
	        		
	        		return true;
        		}
        		else{
        			return false;
        		}
        	}
        });
    	
    	((Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_equip)).setVisibility(Button.GONE);
    }
    
    public void button_inventory(View view){
    	Intent intent=new Intent(this,Activity_Inventory.class);
    	
    	Bundle bundle=new Bundle();
    	bundle.putString(getPackageName()+"section","all");
    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
    	bundle.putBoolean(getPackageName()+"equip_mode",false);
    	
    	intent.putExtras(bundle);
    	startActivity(intent);
    }
    
    public void button_equip_weapon(View view){
    	Intent intent=new Intent(this,Activity_Inventory.class);
    	
    	Bundle bundle=new Bundle();
    	bundle.putString(getPackageName()+"section","weapon");
    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
    	bundle.putBoolean(getPackageName()+"equip_mode",true);
    	
    	intent.putExtras(bundle);
    	startActivity(intent);
    }
    
    public void button_equip_head(View view){
    	Intent intent=new Intent(this,Activity_Inventory.class);
    	
    	Bundle bundle=new Bundle();
    	bundle.putString(getPackageName()+"section","head");
    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
    	bundle.putBoolean(getPackageName()+"equip_mode",true);
    	
    	intent.putExtras(bundle);
    	startActivity(intent);
    }
    
    public void button_equip_chest(View view){
    	Intent intent=new Intent(this,Activity_Inventory.class);
    	
    	Bundle bundle=new Bundle();
    	bundle.putString(getPackageName()+"section","chest");
    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
    	bundle.putBoolean(getPackageName()+"equip_mode",true);
    	
    	intent.putExtras(bundle);
    	startActivity(intent);
    }
    
    public void button_equip_feet(View view){
    	Intent intent=new Intent(this,Activity_Inventory.class);
    	
    	Bundle bundle=new Bundle();
    	bundle.putString(getPackageName()+"section","feet");
    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
    	bundle.putBoolean(getPackageName()+"equip_mode",true);
    	
    	intent.putExtras(bundle);
    	startActivity(intent);
    }
    
    public void unequip_item(int position){
    	if(pet_status.equipment_slots.get(position)!=null){
    		Sound_Manager.play_sound(Sound.UNEQUIPPED);
    		
    		pet_status.equipment.add(0,pet_status.equipment_slots.get(position));
    		
    		pet_status.equipment_slots.set(position,null);
    		
    		update();
    	}
    }
    
    public void sell_item(int position){
    	if(pet_status.equipment_slots.get(position)!=null){
    		Sound_Manager.play_sound(Sound.ITEM_SOLD);
    		
    		pet_status.bits+=pet_status.equipment_slots.get(position).bits;
    		pet_status.bits_bound();
    		
    		pet_status.equipment_slots.set(position,null);
    		
    		update();
    	}
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
		((Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_unequip)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(detail_position!=-1){
					unequip_item(detail_position);
				}
				
				if(dialog_details.isShowing()){
					dismissDialog(DIALOG_DETAILS);
			    }
			}
		});
		((Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_sell)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(detail_position!=-1){
					sell_item(detail_position);
				}
				
				if(dialog_details.isShowing()){
					dismissDialog(DIALOG_DETAILS);
			    }
			}
		});
	}
}
