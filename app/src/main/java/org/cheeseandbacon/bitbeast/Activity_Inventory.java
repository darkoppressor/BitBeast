package org.cheeseandbacon.bitbeast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Activity_Inventory extends AppCompatActivity {
	static final int DIALOG_SOLD=0;
	static final int DIALOG_DETAILS=1;
	
	private Pet_Status pet_status;
	
	AlertDialog dialog_sold;
	AlertDialog dialog_details;
	
	private ArrayList<Equipment> items;
	private ArrayList<Integer> equipment_indices;
	
	int detail_position;
	
	//What section of the inventory are we viewing?
	private String section;
	
	private boolean equip_mode;
	
	private int move_direction;
		
	private class InventoryAdapter extends ArrayAdapter<Equipment>{
		public InventoryAdapter(Context context,int textViewResourceId,List<Equipment> objects){
			super(context,textViewResourceId,objects);
		}
		
		@Override
		public View getView(int position,View convertView,ViewGroup parent){
			View view=convertView;
            if(view==null){
            	LayoutInflater li=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	view=li.inflate(R.layout.list_item_inventory_item,null);
            }
            
            CheckedTextView ctv=(CheckedTextView)view.findViewById(R.id.list_name_item);
            if(ctv!=null){
            	String message="";
            	message+=items.get(position).get_list_string(Activity_Inventory.this);
            	ctv.setText(Html.fromHtml(Strings.newline_to_br(message)));
            	ctv.setTypeface(Font.font1);
            	
            	ctv.setCompoundDrawablesWithIntrinsicBounds(items.get(position).get_template(getApplicationContext()).resource_id,0,0,0);
            	
            	if(equip_mode){
            		ctv.setCheckMarkDrawable(null);
            	}
            }

            return view;
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
    	
    	Bundle bundle=getIntent().getExtras();
    	section=bundle.getString(getPackageName()+"section");
    	move_direction=bundle.getInt(getPackageName()+"move_direction",Direction.NONE);
    	equip_mode=bundle.getBoolean(getPackageName()+"equip_mode");
    	
    	if(!equip_mode){
	    	if(section.equals("all")){
	    		setTitle(getResources().getString(R.string.name_inventory_all));
	    	}
	    	else if(section.equals("weapon")){
	    		setTitle(getResources().getString(R.string.name_inventory_weapons));
	    	}
	    	else if(section.equals("head")){
	    		setTitle(getResources().getString(R.string.name_inventory_head));
	    	}
	    	else if(section.equals("chest")){
	    		setTitle(getResources().getString(R.string.name_inventory_chest));
	    	}
	    	else if(section.equals("feet")){
	    		setTitle(getResources().getString(R.string.name_inventory_feet));
	    	}
    	}
    	else{
    		if(section.equals("weapon")){
	    		setTitle(getResources().getString(R.string.name_inventory_weapons_equip));
	    	}
	    	else if(section.equals("head")){
	    		setTitle(getResources().getString(R.string.name_inventory_head_equip));
	    	}
	    	else if(section.equals("chest")){
	    		setTitle(getResources().getString(R.string.name_inventory_chest_equip));
	    	}
	    	else if(section.equals("feet")){
	    		setTitle(getResources().getString(R.string.name_inventory_feet_equip));
	    	}
    	}
    	
    	showDialog(DIALOG_SOLD);
        dismissDialog(DIALOG_SOLD);
        
        showDialog(DIALOG_DETAILS);
        dismissDialog(DIALOG_DETAILS);
    	
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_inventory_sell));
    	Font.set_typeface(getAssets(), (CheckedTextView)findViewById(R.id.checkbox_inventory_all));
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_inventory_back));
    	Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_inventory_forward));
    	
    	Font.set_typeface(getAssets(), (TextView)dialog_sold.findViewById(R.id.dialog_inventory_sold_message));
    	Font.set_typeface(getAssets(), (Button)dialog_sold.findViewById(R.id.button_dialog_inventory_sold_ok));
    	
    	Font.set_typeface(getAssets(), (TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_ok));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_equip));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_unequip));
    	Font.set_typeface(getAssets(), (Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_sell));
	}
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_inventory));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	set_dialog_buttons();
    	
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
    	
    	rebuild_list();
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
        case DIALOG_SOLD:
        	inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	layout=inflater.inflate(R.layout.dialog_inventory_sold,(ViewGroup)findViewById(R.id.root_dialog_inventory_sold));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_sold=builder.create();
			return dialog_sold;
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
	
	public void rebuild_list(){
		if(!equip_mode){
    		((LinearLayout)findViewById(R.id.ll_inventory)).setVisibility(LinearLayout.VISIBLE);
    	}
    	else{
    		((LinearLayout)findViewById(R.id.ll_inventory)).setVisibility(LinearLayout.GONE);
    	}
    	
    	((Button)dialog_details.findViewById(R.id.button_dialog_inventory_details_unequip)).setVisibility(Button.GONE);
		
		detail_position=-1;
		
		items=new ArrayList<Equipment>();
		equipment_indices=new ArrayList<Integer>();
    	for(int i=0;i<pet_status.equipment.size();i++){
    		if(section.equals("all")){
				items.add(pet_status.equipment.get(i));
				equipment_indices.add(i);
			}
    		else if(section.equals("weapon") && pet_status.equipment.get(i).get_template(this).slot.equals("weapon")){
				items.add(pet_status.equipment.get(i));
				equipment_indices.add(i);
			}
    		else if(section.equals("head") && pet_status.equipment.get(i).get_template(this).slot.equals("head")){
				items.add(pet_status.equipment.get(i));
				equipment_indices.add(i);
			}
    		else if(section.equals("chest") && pet_status.equipment.get(i).get_template(this).slot.equals("chest")){
				items.add(pet_status.equipment.get(i));
				equipment_indices.add(i);
			}
    		else if(section.equals("feet") && pet_status.equipment.get(i).get_template(this).slot.equals("feet")){
				items.add(pet_status.equipment.get(i));
				equipment_indices.add(i);
			}
		}
    	
    	ListView lv=(ListView)findViewById(R.id.list_view_inventory);
        lv.setTextFilterEnabled(true);
        lv.clearChoices();
        lv.setDividerHeight(0);
        
        if(!equip_mode){
        	lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
        else{
        	lv.setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
        
        lv.setAdapter(new InventoryAdapter(Activity_Inventory.this,R.layout.list_item_inventory_item,items));

        lv.setOnItemLongClickListener(new OnItemLongClickListener(){
        	public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long id){
        		String message=items.get(position).get_details(getApplicationContext(),pet_status.equipment_slots);
    			
    			TextView tv=(TextView)dialog_details.findViewById(R.id.dialog_inventory_details_message);
    			tv.setText(Html.fromHtml(Strings.newline_to_br(message)));
    			
    			tv.setCompoundDrawablesWithIntrinsicBounds(0,0,items.get(position).get_template(getApplicationContext()).resource_id,0);
    			
    			detail_position=position;
    			
    			showDialog(DIALOG_DETAILS);
        		
        		return true;
        	}
        });
        
        lv.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        		if(equip_mode){
        			equip_item(position);
        		}
        	}
        });
	}
	
	public void equip_item(int position){
		Sound_Manager.play_sound(Sound.EQUIPPED);
		
		String slot=pet_status.equipment.get(equipment_indices.get(position).intValue()).get_template(this).slot;
		
		if(pet_status.equipment_slots.get(Equipment.string_to_slot(slot))==null){
			pet_status.equipment_slots.set(Equipment.string_to_slot(slot),pet_status.equipment.get(equipment_indices.get(position).intValue()));
			
			pet_status.equipment.remove(equipment_indices.get(position).intValue());
		}
		else{
			pet_status.equipment.add(equipment_indices.get(position).intValue(),pet_status.equipment_slots.get(Equipment.string_to_slot(slot)));
			
			pet_status.equipment_slots.set(Equipment.string_to_slot(slot),pet_status.equipment.get(equipment_indices.get(position).intValue()+1));
			
			pet_status.equipment.remove(equipment_indices.get(position).intValue()+1);
		}
		
		finish();
		return;
	}
	
	public void sell_item(int position){
		Sound_Manager.play_sound(Sound.ITEM_SOLD);
		
		pet_status.bits+=pet_status.equipment.get(equipment_indices.get(position).intValue()).bits;
		pet_status.bits_bound();
		
		pet_status.equipment.remove(equipment_indices.get(position).intValue());
		
		rebuild_list();
	}
	
	public void button_sell_checked(View view){
		ListView lv=(ListView)findViewById(R.id.list_view_inventory);
		
		CheckedTextView ctv=(CheckedTextView)findViewById(R.id.checkbox_inventory_all);
		ctv.setChecked(false);
		
		ArrayList<Integer> sell_indices=new ArrayList<Integer>();
		
		int bits_earned=0;
		int items_sold=0;
		
		for(int i=0;i<lv.getCount();i++){
			if(lv.isItemChecked(i)){
				sell_indices.add(equipment_indices.get(i));
				bits_earned+=items.get(i).bits;
				items_sold++;
			}
		}
		
		if(items_sold>0){
			Sound_Manager.play_sound(Sound.ITEM_SOLD_ALL);
			
			pet_status.sell_equipment(sell_indices);
			
			rebuild_list();
			
			String plural_items="";
			String plural_bits="";
			if(items_sold!=1){
				plural_items="s";
			}
			if(bits_earned!=1){
				plural_bits="s";
			}
			
			String message="Sold "+items_sold+" item"+plural_items+" for "+bits_earned+" bit"+plural_bits+".";
			
			TextView tv=(TextView)dialog_sold.findViewById(R.id.dialog_inventory_sold_message);
			tv.setText(message);
			
			showDialog(DIALOG_SOLD);
		}
	}
	
	public void button_toggle_all(View view){
		ListView lv=(ListView)findViewById(R.id.list_view_inventory);
		
		boolean new_value;
		if(((CheckedTextView)view).isChecked()){
			new_value=false;
			((CheckedTextView)view).setChecked(new_value);
		}
		else{
			new_value=true;
			((CheckedTextView)view).setChecked(new_value);
		}
		
		for(int i=0;i<lv.getCount();i++){
			lv.setItemChecked(i,new_value);
		}
	}
	
	public void button_back(View view){
		if(!equip_mode){
			if(section.equals("all")){
				section="feet";
			}
			else if(section.equals("weapon")){
				section="all";
			}
			else if(section.equals("feet")){
				section="chest";
			}
			else if(section.equals("chest")){
				section="head";
			}
			else if(section.equals("head")){
				section="weapon";
			}
			move_direction=Direction.LEFT;
			
			Intent intent=getIntent();
	    	
	    	Bundle bundle=new Bundle();
	    	bundle.putString(getPackageName()+"section",section);
	    	bundle.putInt(getPackageName()+"move_direction",move_direction);
	    	bundle.putBoolean(getPackageName()+"equip_mode",equip_mode);
	    	
	    	intent.replaceExtras(bundle);
	    	
	    	finish();
	    				    	
	    	startActivity(intent);
		}
	}
	
	public void button_forward(View view){
		if(!equip_mode){
			if(section.equals("all")){
				section="weapon";
			}
			else if(section.equals("weapon")){
				section="head";
			}
			else if(section.equals("head")){
				section="chest";
			}
			else if(section.equals("chest")){
				section="feet";
			}
			else if(section.equals("feet")){
				section="all";
			}
			move_direction=Direction.RIGHT;
			
			Intent intent=getIntent();
	    	
	    	Bundle bundle=new Bundle();
	    	bundle.putString(getPackageName()+"section",section);
	    	bundle.putInt(getPackageName()+"move_direction",move_direction);
	    	bundle.putBoolean(getPackageName()+"equip_mode",equip_mode);
	    	
	    	intent.replaceExtras(bundle);
	    	
	    	finish();
	    	
	    	startActivity(intent);
		}
	}
	
	public void close_dialogs(){
		if(dialog_sold.isShowing()){
			dismissDialog(DIALOG_SOLD);
	    }
		
		if(dialog_details.isShowing()){
			dismissDialog(DIALOG_DETAILS);
	    }
	}
	
	public void set_dialog_buttons(){
		((Button)dialog_sold.findViewById(R.id.button_dialog_inventory_sold_ok)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(dialog_sold.isShowing()){
					dismissDialog(DIALOG_SOLD);
			    }
			}
		});
		
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
				if(detail_position!=-1){
					equip_item(detail_position);
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
