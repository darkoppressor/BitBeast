package org.cheeseandbacon.bitbeast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Activity_Battle_Menu extends AppCompatActivity {
	static final int REQUEST_MAKE_DISCOVERABLE=1;
	
	private Pet_Status pet_status;
	private Pet_Status them;
	private int our_seed;
	private int their_seed;
	
	BluetoothAdapter bluetooth_adapter;
	BluetoothService bluetooth_service;
	ArrayList<BluetoothDevice> devices;
	
	private class BluetoothArrayAdapter extends ArrayAdapter<BluetoothDevice>{
		public BluetoothArrayAdapter(Context context,int textViewResourceId,List<BluetoothDevice> objects){
			super(context,textViewResourceId,objects);
		}
		
		@Override
		public View getView(int position,View convertView,ViewGroup parent){
			View view=convertView;
            if(view==null){
            	LayoutInflater li=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	view=li.inflate(R.layout.list_item_battle_menu,null);
            }
            
            TextView tv=(TextView)view.findViewById(R.id.list_name_battle_menu);
            if(tv!=null){
            	String message="";
            	
            	if(devices.get(position).getBondState()==BluetoothDevice.BOND_BONDED){
            		message="(Paired) ";
            	}
            	
            	message+=devices.get(position).getName()+"\n"+devices.get(position).getAddress();
            	tv.setText(message);
            	tv.setTypeface(Font.font1);
            }

            return view;
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_menu);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        bluetooth_adapter=BluetoothAdapter.getDefaultAdapter();
        bluetooth_service=null;
        devices=new ArrayList<BluetoothDevice>();
        
        Font.set_typeface((Button)findViewById(R.id.button_battle_menu_scan));
        Font.set_typeface((Button)findViewById(R.id.button_battle_menu_make_discoverable));
        Font.set_typeface((TextView)findViewById(R.id.text_battle_menu_available_devices));
        Font.set_typeface((TextView)findViewById(R.id.text_battle_menu_transmitting));
        
        show_menu();
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_battle_menu));
    	System.gc();
    }
	@Override
    protected void onStart(){
    	super.onStart();
    	
    	reset();
    	
    	if(bluetooth_service==null){
    		bluetooth_service=new BluetoothService(this,handler);
    	}
    }
	@Override
    protected synchronized void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status,false);
    	
    	IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver,filter);
    	
    	update_paired_devices();
    	
    	if(bluetooth_service!=null){
    		if(bluetooth_service.get_state()==BluetoothService.STATE_NONE){
    			bluetooth_service.start_all();
    		}
    	}
    	
    	if(pet_status.get_energy()<Pet_Status.ENERGY_LOSS_BATTLE){
			int energy_short=Pet_Status.ENERGY_LOSS_BATTLE-pet_status.get_energy();
			Toast.makeText(getApplicationContext(),pet_status.name+" needs "+energy_short+" more energy to battle!",Toast.LENGTH_SHORT).show();
			
			finish();
			return;
		}
    }
	@Override
    protected synchronized void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	bluetooth_adapter.cancelDiscovery();
    	
    	unregisterReceiver(receiver);
    	
    	if(bluetooth_service!=null){
    		bluetooth_service.stop();
    	}
    }
	@Override
	protected synchronized void onStop(){
		super.onStop();
		
		if(bluetooth_service!=null){
    		bluetooth_service.stop();
    		bluetooth_service=null;
    	}
	}
	@Override
    public boolean onSearchRequested(){
    	String save_location=StorageManager.save_screenshot(this,findViewById(R.id.root_battle_menu));
    	
    	if(save_location.length()>0){
    		Toast.makeText(getApplicationContext(),"Screenshot saved to "+save_location+".",Toast.LENGTH_SHORT).show();
    	}
    	
    	return false;
    }
	@Override
    protected void onActivityResult(int get_request_code,int get_result_code,Intent get_data){
    	super.onActivityResult(get_request_code,get_result_code,get_data);
    	
    	switch(get_request_code){
    	case REQUEST_MAKE_DISCOVERABLE:
    		if(get_result_code==RESULT_CANCELED){
    			///Toast.makeText(getApplicationContext(),"Your device was not made discoverable.",Toast.LENGTH_SHORT).show();
    		}
    		else{
    			Toast.makeText(getApplicationContext(),"Your device will be discoverable for "+get_result_code+" seconds.",Toast.LENGTH_SHORT).show();
    		}
    		break;
    	}
    }
	
	public void reset(){
		pet_status=null;
		them=null;
		
		our_seed=0;
        their_seed=0;
	}
	
	//Show the battle menu.
	public void show_menu(){
		Button b=null;
		TextView tv=null;
		ListView lv=null;
		
		try{
			tv=(TextView)findViewById(R.id.text_battle_menu_transmitting);
			tv.setVisibility(TextView.GONE);
			
			b=(Button)findViewById(R.id.button_battle_menu_scan);
			b.setVisibility(Button.VISIBLE);
			
			b=(Button)findViewById(R.id.button_battle_menu_make_discoverable);
			b.setVisibility(Button.VISIBLE);
			
			tv=(TextView)findViewById(R.id.text_battle_menu_available_devices);
			tv.setVisibility(TextView.VISIBLE);
			
			lv=(ListView)findViewById(R.id.list_view_battle_menu);
			lv.setVisibility(ListView.VISIBLE);
		}
		catch(NullPointerException e){
			StorageManager.error_log_add(this,"Activity_Battle_Menu","Failed to show menu!",e);
		}
	}
	
	//Show only the transmitting message.
	public void show_transmitting(){
		Button b=null;
		TextView tv=null;
		ListView lv=null;
		
		try{
			tv=(TextView)findViewById(R.id.text_battle_menu_transmitting);
			tv.setVisibility(TextView.VISIBLE);
			
			b=(Button)findViewById(R.id.button_battle_menu_scan);
			b.setVisibility(Button.GONE);
			
			b=(Button)findViewById(R.id.button_battle_menu_make_discoverable);
			b.setVisibility(Button.GONE);
			
			tv=(TextView)findViewById(R.id.text_battle_menu_available_devices);
			tv.setVisibility(TextView.GONE);
			
			lv=(ListView)findViewById(R.id.list_view_battle_menu);
			lv.setVisibility(ListView.GONE);
		}
		catch(NullPointerException e){
			StorageManager.error_log_add(this,"Activity_Battle_Menu","Failed to show transmitting message!",e);
		}
	}
	
	public void button_scan(View view){
		if(bluetooth_adapter.isDiscovering()){
			bluetooth_adapter.cancelDiscovery();
		}
		
		bluetooth_adapter.startDiscovery();
	}
	public void button_make_discoverable(View view){
		Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
    	startActivityForResult(intent,REQUEST_MAKE_DISCOVERABLE);
	}
	
	public void update_paired_devices(){
		Set<BluetoothDevice> paired_devices=bluetooth_adapter.getBondedDevices();
    	
    	if(paired_devices.size()>0){
    		for(BluetoothDevice device:paired_devices){
    			devices.add(device);
    		}
    	}
		
		rebuild_list();
	}
	
	public void rebuild_list(){
		//Remove any duplicate entries from the device list.
		for(int i=0;i<devices.size();i++){
			for(int n=i+1;n<devices.size();n++){
				if(devices.get(i).getAddress().equals(devices.get(n).getAddress())){
					devices.remove(n);
					n--;
				}
			}
		}
		
		ListView lv=(ListView)findViewById(R.id.list_view_battle_menu);
        lv.setTextFilterEnabled(true);
        lv.clearChoices();
        lv.setDividerHeight(0);
        lv.setAdapter(new BluetoothArrayAdapter(Activity_Battle_Menu.this,R.layout.list_item_battle_menu,devices));

        lv.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        		connect_to_device(devices.get(position),true);
        	}
        });
	}
	
	private final BroadcastReceiver receiver=new BroadcastReceiver(){
		public void onReceive(Context context,Intent intent){
			String action=intent.getAction();
			
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				
				devices.add(device);
				
				rebuild_list();
			}
			else if(BluetoothDevice.ACTION_NAME_CHANGED.equals(action) ||
					BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
				BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
								
				for(int i=0;i<devices.size();i++){
					if(devices.get(i).getAddress().equals(device.getAddress())){
						devices.add(device);
						
						rebuild_list();
						
						break;
					}
				}
			}
		}
	};
	
	public void connect_to_device(BluetoothDevice device,boolean secure){
		bluetooth_service.connect(device,secure);
	}
	
	static final int HANDLER_READ=0;
	static final int HANDLER_READY=1;
	static final int HANDLER_STATE_CHANGE=2;
	static final int HANDLER_DEVICE_NAME=3;
	static final int HANDLER_TOAST=4;
    
    private Handler handler=new Handler(){
    	@Override
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case HANDLER_STATE_CHANGE:
    			if(!isFinishing()){
	    			switch(msg.arg1){
	                case BluetoothService.STATE_CONNECTED:
	                	///Toast.makeText(getApplicationContext(),"State: Connected",Toast.LENGTH_SHORT).show();
	                    break;
	                case BluetoothService.STATE_CONNECTING:
	                	///Toast.makeText(getApplicationContext(),"State: Connecting",Toast.LENGTH_SHORT).show();
	                	show_menu();
	                    break;
	                case BluetoothService.STATE_LISTEN:
	                	///Toast.makeText(getApplicationContext(),"State: Listen",Toast.LENGTH_SHORT).show();
	                	show_menu();
	                    break;
	                case BluetoothService.STATE_NONE:
	                	///Toast.makeText(getApplicationContext(),"State: None",Toast.LENGTH_SHORT).show();
	                	show_menu();
	                    break;
	                }
    			}
    			break;
    		case HANDLER_READ:
    			if(!isFinishing()){
    				if(bluetooth_service!=null){
    					///Toast.makeText(getApplicationContext(),"Received some data",Toast.LENGTH_SHORT).show();
    					
    					String raw_data=new String((String)msg.obj);
	    				ArrayList<String> data=new ArrayList<String>();
	    				
	    				//Convert the bytes into strings.
	    				String current_string="";
	    				for(int i=0;i<raw_data.length();i++){
	    					if(raw_data.charAt(i)!='\n'){
	    						current_string+=raw_data.charAt(i);
	    					}
	    					else{
	    						data.add(current_string);
	    						current_string="";
	    					}
	    				}
	    				if(data.size()==0){
	    					data.add(current_string);
	    				}
	    				
	    				///Toast.makeText(getApplicationContext(),"data size: "+data.size(),Toast.LENGTH_SHORT).show();
	    				
	    				//Read the packet id before passing the data on.
	    				int packet_id=Integer.parseInt(data.get(0).trim());
	    				data.remove(0);
	    				
	    				if(packet_id==Packet_ID.BATTLE_DATA){
	    					//Read the pet's seed before passing the data on.
		    				their_seed=Integer.parseInt(data.get(0).trim());
		    				data.remove(0);
	    					
	    					them=Network_IO.read_battle_data(data);
	    					
	    					bluetooth_service.set_done();
	    					Network_IO.send_done(bluetooth_service);
	    				}
	    				else if(packet_id==Packet_ID.DONE){
	    					bluetooth_service.set_they_done();
	    				}
    				}
			    }
    			break;
    		case HANDLER_READY:
    			if(!isFinishing()){
    				if(bluetooth_service!=null){
    					bluetooth_service.stop();
    					
    					//Start the actual battle activity, passing it the two pets' data.
    					Intent intent=new Intent(Activity_Battle_Menu.this,Activity_Battle.class);
    					
    					Bundle bundle=new Bundle();
    					bundle.putBoolean(getPackageName()+".server",bluetooth_service.get_server());
    					bundle.putBoolean(getPackageName()+".shadow",false);
    					bundle.putInt(getPackageName()+".our_seed",our_seed);
    					bundle.putInt(getPackageName()+".their_seed",their_seed);
    					bundle.putAll(them.write_bundle_battle_data(getPackageName()));
    					
    					intent.putExtras(bundle);
    			    	startActivity(intent);
    				}
			    }
    			break;
    		case HANDLER_DEVICE_NAME:
    			if(!isFinishing()){
	    			if(bluetooth_service!=null && pet_status!=null){
	    				///Toast.makeText(getApplicationContext(),"Connected to "+msg.getData().getString("device_name"),Toast.LENGTH_SHORT).show();
	    				
	    				show_transmitting();
	    				
	    				our_seed=Network_IO.send_battle_data(bluetooth_service,pet_status);
	    			}
    			}
    			break;
    		case HANDLER_TOAST:
    			if(!isFinishing()){
    				Toast.makeText(getApplicationContext(),msg.getData().getString("toast"),msg.arg1).show();
    			}
    			break;
    		}
    	}
	};
}
