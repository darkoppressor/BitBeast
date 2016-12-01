package org.cheeseandbacon.bitbeast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BitBeast extends Activity{
	static final int DIALOG_ID_PROGRESS=0;
	static final int DIALOG_ID_TEMP=1;
	static final int DIALOG_ID_GAMES=2;
	static final int DIALOG_ID_DIE=3;
	static final int DIALOG_ID_BATTLE=4;
	static final int DIALOG_ID_STORE=5;
	
	static final int REQUEST_OPTIONS=1;
	static final int REQUEST_NAME=2;
	static final int REQUEST_ENABLE_BLUETOOTH=3;
	///QQQ
	///static final int REQUEST_QR_CODE=4;
	
	private Image image;
	GameView game_view;
	
	ProgressDialog dialog_progress;
	AlertDialog dialog_store;
	AlertDialog dialog_temp;
	AlertDialog dialog_games;
	AlertDialog dialog_battle;
	AlertDialog dialog_die;
	
	SpeechRecognizer speech;
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Sound_Manager.startup(this);
        Templates.startup();
        StorageManager.load_templates(this,getResources());
        Font.startup(getAssets());
        RNG.startup();
        Options.startup();
        StorageManager.load_options(this,false);
        
        image=(Image)getLastNonConfigurationInstance();
        if(image==null){
        	image=new Image(this);
        }
        
        StorageManager.log_reset(this);
        StorageManager.error_log_reset(this);
        
        game_view=(GameView)findViewById(R.id.view_game);
        game_view.bitbeast=BitBeast.this;
        
        showDialog(DIALOG_ID_PROGRESS);
        dismissDialog(DIALOG_ID_PROGRESS);
        showDialog(DIALOG_ID_STORE);
        dismissDialog(DIALOG_ID_STORE);
        showDialog(DIALOG_ID_TEMP);
        dismissDialog(DIALOG_ID_TEMP);
        showDialog(DIALOG_ID_GAMES);
        dismissDialog(DIALOG_ID_GAMES);
        showDialog(DIALOG_ID_DIE);
        dismissDialog(DIALOG_ID_DIE);
        showDialog(DIALOG_ID_BATTLE);
        dismissDialog(DIALOG_ID_BATTLE);
        
        speech=null;
        
        Font.set_typeface((Button)findViewById(R.id.button_status));
        Font.set_typeface((Button)findViewById(R.id.button_store));
        Font.set_typeface((Button)findViewById(R.id.button_clean));
        Font.set_typeface((Button)findViewById(R.id.button_bathe));
        Font.set_typeface((Button)findViewById(R.id.button_temp));
        Font.set_typeface((Button)findViewById(R.id.button_games));
        Font.set_typeface((Button)findViewById(R.id.button_equipment));
        Font.set_typeface((Button)findViewById(R.id.button_battle));
        Font.set_typeface((Button)findViewById(R.id.button_light));
        Font.set_typeface((Button)findViewById(R.id.button_options));
        
        Font.set_typeface((TextView)dialog_store.findViewById(R.id.dialog_title_store));
        Font.set_typeface((Button)dialog_store.findViewById(R.id.button_dialog_main_store_food));
        Font.set_typeface((Button)dialog_store.findViewById(R.id.button_dialog_main_store_drinks));
        Font.set_typeface((Button)dialog_store.findViewById(R.id.button_dialog_main_store_treatments));
        Font.set_typeface((Button)dialog_store.findViewById(R.id.button_dialog_main_store_perma));
        
        Font.set_typeface((TextView)dialog_temp.findViewById(R.id.dialog_title_temp));
        Font.set_typeface((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_ac));
        Font.set_typeface((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_heater));
        Font.set_typeface((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_none));
        
        Font.set_typeface((TextView)dialog_games.findViewById(R.id.dialog_title_games));
        Font.set_typeface((Button)dialog_games.findViewById(R.id.button_dialog_main_games_training));
        Font.set_typeface((Button)dialog_games.findViewById(R.id.button_dialog_main_games_bricks));
        Font.set_typeface((Button)dialog_games.findViewById(R.id.button_dialog_main_games_rps));
        Font.set_typeface((Button)dialog_games.findViewById(R.id.button_dialog_main_games_accel));
        Font.set_typeface((Button)dialog_games.findViewById(R.id.button_dialog_main_games_gps));
        Font.set_typeface((Button)dialog_games.findViewById(R.id.button_dialog_main_games_speed_gps));
        
        Font.set_typeface((TextView)dialog_die.findViewById(R.id.dialog_die_message));
        Font.set_typeface((Button)dialog_die.findViewById(R.id.button_dialog_die_ok));
        
        Font.set_typeface((TextView)dialog_battle.findViewById(R.id.dialog_title_battle));
        Font.set_typeface((Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_shadow));
        Font.set_typeface((Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_bluetooth));
        Font.set_typeface((Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_cancel));
        
        Button b=null;
        
        b=(Button)findViewById(R.id.button_status);
        b.setNextFocusLeftId(R.id.button_options);
        
        b=(Button)findViewById(R.id.button_temp);
        b.setNextFocusRightId(R.id.button_games);
        
        b=(Button)findViewById(R.id.button_games);
        b.setNextFocusLeftId(R.id.button_temp);
        
        b=(Button)findViewById(R.id.button_options);
        b.setNextFocusRightId(R.id.button_status);
        
        b=(Button)dialog_store.findViewById(R.id.button_dialog_main_store_food);
        b.setNextFocusUpId(R.id.button_dialog_main_store_perma);
        
        b=(Button)dialog_store.findViewById(R.id.button_dialog_main_store_perma);
        b.setNextFocusDownId(R.id.button_dialog_main_store_food);
        
        b=(Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_ac);
        b.setNextFocusUpId(R.id.button_dialog_main_temp_none);
        
        b=(Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_none);
        b.setNextFocusDownId(R.id.button_dialog_main_temp_ac);
        
        b=(Button)dialog_games.findViewById(R.id.button_dialog_main_games_training);
        b.setNextFocusUpId(R.id.button_dialog_main_games_speed_gps);
        
        b=(Button)dialog_games.findViewById(R.id.button_dialog_main_games_speed_gps);
        b.setNextFocusDownId(R.id.button_dialog_main_games_training);
        
        b=(Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_shadow);
        b.setNextFocusUpId(R.id.button_dialog_main_battle_cancel);
        
        b=(Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_cancel);
        b.setNextFocusDownId(R.id.button_dialog_main_battle_shadow);
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Sound_Manager.cleanup();
    	Templates.cleanup();
    	Font.cleanup();
    	RNG.cleanup();
    	Options.cleanup();
    	
    	image=null;
    	game_view=null;
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_main));
    	System.gc();
    }
    @Override
    public Object onRetainNonConfigurationInstance(){
    	final Image keep_image=image;
    	return keep_image;
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
    	super.onRestoreInstanceState(savedInstanceState);
    	
    	int game_mode=savedInstanceState.getInt("game_mode");
        set_game_mode(game_mode,savedInstanceState);
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
    	savedInstanceState.putInt("game_mode",game_view.get_game_mode());
    	
    	if(game_view.get_game_mode()==Game_Mode.WORKOUT){
    		game_view.get_game_workout().save_game(savedInstanceState);
    	}
    	else if(game_view.get_game_mode()==Game_Mode.BRICKS){
    		game_view.get_game_bricks().save_game(savedInstanceState);
    	}
    	
    	super.onSaveInstanceState(savedInstanceState);
    }
    @Override  
    public boolean onCreateOptionsMenu(Menu menu){  
    	if(game_view.get_game_mode()==Game_Mode.PET){
    		button_options(game_view);
    	}
    	
    	return super.onCreateOptionsMenu(menu);  
    }
    @Override
    public void onBackPressed(){
    	if(game_view.get_game_mode()==Game_Mode.PET){
    		super.onBackPressed();
    	}
    	else if(game_view.get_game_mode()==Game_Mode.WORKOUT){
    		game_view.get_game_workout().end_game(-1,game_view.get_pet().get_status(),game_view);
    	}
    	else if(game_view.get_game_mode()==Game_Mode.BRICKS){
    		game_view.get_game_bricks().end_game(-1,game_view.get_pet().get_status(),game_view);
    	}
    }
    @Override
    public boolean onSearchRequested(){
    	game_view.save_screenshot(this);
    	
    	return false;
    }
    @Override
    protected Dialog onCreateDialog(int id){
    	AlertDialog.Builder builder=null;
    	LayoutInflater inflater=null;
    	View layout=null;
    	
        switch(id){
        case DIALOG_ID_PROGRESS:
        	dialog_progress=new ProgressDialog(BitBeast.this);
        	dialog_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        	dialog_progress.setMessage("Updating pet...");
        	dialog_progress.setCancelable(false);
            return dialog_progress;
        case DIALOG_ID_STORE:
        	inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	layout=inflater.inflate(R.layout.dialog_main_store,(ViewGroup)findViewById(R.id.root_dialog_main_store));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_store=builder.create();
			return dialog_store;
        case DIALOG_ID_TEMP:
        	inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	layout=inflater.inflate(R.layout.dialog_main_temp,(ViewGroup)findViewById(R.id.root_dialog_main_temp));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_temp=builder.create();
			return dialog_temp;
        case DIALOG_ID_GAMES:
        	inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	layout=inflater.inflate(R.layout.dialog_main_games,(ViewGroup)findViewById(R.id.root_dialog_main_games));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_games=builder.create();
			return dialog_games;
        case DIALOG_ID_DIE:
        	inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	layout=inflater.inflate(R.layout.dialog_main_die,(ViewGroup)findViewById(R.id.root_dialog_main_die));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_die=builder.create();
			return dialog_die;
        case DIALOG_ID_BATTLE:
        	inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	layout=inflater.inflate(R.layout.dialog_main_battle,(ViewGroup)findViewById(R.id.root_dialog_main_battle));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_battle=builder.create();
			return dialog_battle;
        default:
            return null;
        }
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	set_dialog_buttons();

    	if(dialog_die.isShowing()){
        	dismissDialog(DIALOG_ID_DIE);
        }
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	//Reset the pet and records.
    	game_view.reset_pet();
    	game_view.reset_records();
    	
    	long ms_last_run=StorageManager.load_pet_status(BitBeast.this,(View)game_view,game_view.get_pet().get_status(),false);
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	StorageManager.load_records(BitBeast.this,game_view.get_records(),false);
    	
    	LinearLayout ll=(LinearLayout)findViewById(R.id.root_main);
    	if(game_view.get_pet().get_status().light){
    		ll.setBackgroundResource(R.drawable.background_blueprint);
    	}
    	else{
    		ll.setBackgroundResource(android.R.color.black);
    	}
    	
    	set_button_colors();
    	
    	game_view.reset_sprites(image);
    	
    	game_view.resume(handler,image,(Vibrator)getSystemService(VIBRATOR_SERVICE),ms_last_run);
        
        //If no name has been given, the pet is not started yet.
    	if(game_view.get_pet().get_status().name.length()==0){
    		Intent intent=new Intent(BitBeast.this,Activity_Name.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        	startActivityForResult(intent,REQUEST_NAME);
    	}
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	
    	close_dialogs();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	game_view.set_want_speech(false);
    	stop_speech_recognition();
    	
    	game_view.pause();
    	
    	if(Options.vibrate){
    		Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
    		vibrator.cancel();
    	}
    	
    	//If a name has been given, the pet has been started.
    	if(game_view.get_pet().get_status().name.length()>0){
    		StorageManager.save_pet_status(this,game_view.get_pet().get_status());
    	}
    }
    
    @Override
    protected void onActivityResult(int get_request_code,int get_result_code,Intent get_data){
    	super.onActivityResult(get_request_code,get_result_code,get_data);
    	
    	switch(get_request_code){
    	case REQUEST_OPTIONS: case REQUEST_NAME:
    		if(get_result_code==RESULT_CANCELED){
    			finish();
    			return;
    		}
    		break;
    	case REQUEST_ENABLE_BLUETOOTH:
    		if(get_result_code==RESULT_OK){
    			start_battle_menu();
    		}
    		else if(get_result_code==RESULT_CANCELED){
    			///Toast.makeText(getApplicationContext(),"Bluetooth was not enabled. Not entering Battle mode.",Toast.LENGTH_SHORT).show();
    		}
    		break;
    	///QQQ
    	/**case REQUEST_QR_CODE:
    		if(get_result_code==RESULT_OK){
    			String contents=get_data.getStringExtra("SCAN_RESULT");
    			//String format=get_data.getStringExtra("SCAN_RESULT_FORMAT");
    			
    			Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_SHORT).show();
    		}
    		break;*/
    	///
    	}
    }
    
    public void set_button_colors(){
    	if(game_view!=null){
	        Button b=null;
	        
	        b=(Button)findViewById(R.id.button_status);
	        b.setTextColor(getResources().getColor(R.color.font));
	        
	        b=(Button)findViewById(R.id.button_store);
	        if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
	        	b.setTextColor(getResources().getColor(R.color.font));
	        }
	        else{
	        	b.setTextColor(getResources().getColor(R.color.font_grayed));
	        }
	        
	        b=(Button)findViewById(R.id.button_clean);
	        if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG){
	        	b.setTextColor(getResources().getColor(R.color.font));
	        }
	        else{
	        	b.setTextColor(getResources().getColor(R.color.font_grayed));
	        }
	        
	        b=(Button)findViewById(R.id.button_bathe);
	        if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
	        	b.setTextColor(getResources().getColor(R.color.font));
	        }
	        else{
	        	b.setTextColor(getResources().getColor(R.color.font_grayed));
	        }
	        
	        b=(Button)findViewById(R.id.button_temp);
	        if(!Options.pause){
	        	b.setTextColor(getResources().getColor(R.color.font));
	        }
	        else{
	        	b.setTextColor(getResources().getColor(R.color.font_grayed));
	        }
	        
	        b=(Button)findViewById(R.id.button_games);
	        if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
	        	b.setTextColor(getResources().getColor(R.color.font));
	        }
	        else{
	        	b.setTextColor(getResources().getColor(R.color.font_grayed));
	        }
	        
	        b=(Button)findViewById(R.id.button_equipment);
	        if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
	        	b.setTextColor(getResources().getColor(R.color.font));
	        }
	        else{
	        	b.setTextColor(getResources().getColor(R.color.font_grayed));
	        }
	        
	        b=(Button)findViewById(R.id.button_battle);
	        if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD &&
	        		game_view.get_pet().get_status().get_energy()>=Pet_Status.ENERGY_LOSS_BATTLE){
	        	b.setTextColor(getResources().getColor(R.color.font));
	        }
	        else{
	        	b.setTextColor(getResources().getColor(R.color.font_grayed));
	        }
	        
	        b=(Button)findViewById(R.id.button_light);
	        b.setTextColor(getResources().getColor(R.color.font));
	        
	        b=(Button)findViewById(R.id.button_options);
	        b.setTextColor(getResources().getColor(R.color.font));
    	}
    }
    
    public void deny_egg(){
    	Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
    	Toast.makeText(getApplicationContext(),game_view.get_pet().get_status().name+" is still just an egg!",Toast.LENGTH_SHORT).show();
    }
    public void deny_dead(){
    	Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
    	Toast.makeText(getApplicationContext(),game_view.get_pet().get_status().name+" is, unfortunately, dead!",Toast.LENGTH_SHORT).show();
    }
    public void deny_paused(){
    	Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
		Toast.makeText(getApplicationContext(),"The game is paused!",Toast.LENGTH_SHORT).show();
    }
    
    public void button_status(View view){
    	StorageManager.save_pet_status(this,game_view.get_pet().get_status());
    	
    	Intent intent=new Intent(this,Activity_Status.class);
    	startActivity(intent);
    }
    public void button_store(View view){
    	if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
    		showDialog(DIALOG_ID_STORE);
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.EGG){
    		deny_egg();
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.DEAD){
    		deny_dead();
    	}
    	else if(Options.pause){
    		deny_paused();
    	}
    }
    public void button_clean(View view){
    	if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG){
			if(game_view.get_pet().overlays.size()<=Overlay.OVERLAY_LIMIT){
				if(image!=null){
					game_view.get_pet().add_overlay(image,(View)game_view,(Vibrator)getSystemService(VIBRATOR_SERVICE),Overlay_Type.CLEAN_YARD,game_view.getWidth(),0,Direction.LEFT);
				}
			}
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.EGG){
    		deny_egg();
    	}
    	else if(Options.pause){
    		deny_paused();
    	}
    }
    public void button_bathe(View view){
    	if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
	    	if(game_view.get_pet().overlays.size()<=Overlay.OVERLAY_LIMIT){
	    		if(image!=null){
	    			game_view.get_pet().add_overlay(image,(View)game_view,(Vibrator)getSystemService(VIBRATOR_SERVICE),Overlay_Type.CLEAN_PET,0,0-image.overlay_clean_pet.bitmap.getHeight(),Direction.DOWN);
	    		}
    		}
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.EGG){
    		deny_egg();
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.DEAD){
    		deny_dead();
    	}
    	else if(Options.pause){
    		deny_paused();
    	}
    }
    public void button_temp(View view){
    	if(!Options.pause){
    		showDialog(DIALOG_ID_TEMP);
    	}
    	else if(Options.pause){
    		deny_paused();
    	}
    }
    public void button_games(View view){
    	if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
    		showDialog(DIALOG_ID_GAMES);
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.EGG){
    		deny_egg();
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.DEAD){
    		deny_dead();
    	}
    	else if(Options.pause){
    		deny_paused();
    	}
    }
    public void button_equipment(View view){
    	if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
    		StorageManager.save_pet_status(this,game_view.get_pet().get_status());
	    	
	    	Intent intent=new Intent(this,Activity_Equip.class);

	    	startActivity(intent);
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.EGG){
    		deny_egg();
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.DEAD){
    		deny_dead();
    	}
    	else if(Options.pause){
    		deny_paused();
    	}
    }
    public void button_battle(View view){
    	if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
    		if(game_view.get_pet().get_status().get_energy()>=Pet_Status.ENERGY_LOSS_BATTLE){
    			showDialog(DIALOG_ID_BATTLE);
    		}
    		else{
    			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
    			int energy_short=Pet_Status.ENERGY_LOSS_BATTLE-game_view.get_pet().get_status().get_energy();
    			Toast.makeText(getApplicationContext(),game_view.get_pet().get_status().name+" needs "+energy_short+" more energy to battle!",Toast.LENGTH_SHORT).show();
    		}
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.EGG){
    		deny_egg();
    	}
    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.DEAD){
    		deny_dead();
    	}
    	else if(Options.pause){
    		deny_paused();
    	}
    }
    public void button_light(View view){
    	Sound_Manager.play_sound(Sound.TOGGLE_LIGHT);
    	
    	//Toggle the light.
    	game_view.get_pet().get_status().light=!game_view.get_pet().get_status().light;
    	
    	LinearLayout ll=(LinearLayout)findViewById(R.id.root_main);
    	if(game_view.get_pet().get_status().light){
    		ll.setBackgroundResource(R.drawable.background_blueprint);
    	}
    	else{
    		ll.setBackgroundResource(android.R.color.black);
    	}
    	
    	///QQQ
    	///game_view.get_pet().die(image,(View)game_view,BitBeast.this,game_view.get_records(),handler);
    	/**game_view.get_pet().get_status().bits+=9999;
    	game_view.get_pet().get_status().bits_bound();*/
    }
    public void button_options(View view){
    	StorageManager.save_pet_status(this,game_view.get_pet().get_status());
    	
    	Intent intent=new Intent(this,Activity_Options.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	startActivityForResult(intent,REQUEST_OPTIONS);
    }
    
    public void start_battle_menu(){
    	game_view.get_pet().get_status().sleeping_wake_up();
		
		StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
    	
    	Intent intent=new Intent(BitBeast.this,Activity_Battle_Menu.class);
    	startActivity(intent);
    }
    
    public void start_speech_recognition(){
    	if(speech==null){
	    	speech=SpeechRecognizer.createSpeechRecognizer(this);
	    	speech.setRecognitionListener(listener);
	    	
	    	Intent intent=new Intent();
	    	intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getClass().getPackage().getName());
	    	intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    	intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
	    	
	    	speech.startListening(intent);
	    	
	    	game_view.set_speech_listening(true);
    	}
    }
    
    public void stop_speech_recognition(){
    	if(speech!=null){
    		speech.stopListening();
    		speech.destroy();
    		speech=null;
    		
    		game_view.set_speech_listening(false);
    	}
    }
    
    private RecognitionListener listener=new RecognitionListener(){
    	@Override
    	public void onBeginningOfSpeech(){
    		
    	}
    	
    	@Override
    	public void onBufferReceived(byte[] buffer){
    		
    	}
    	
    	@Override
    	public void onEndOfSpeech(){
    		
    	}
    	
    	@Override
    	public void onError(int error){
    		//If there is a problem, force speech recognition to stop.
    		//This is to prevent speech recognition from turning on and off over and over.
    		if(error==SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS){
    			Toast.makeText(getApplicationContext(),"Speech recognition failed due to insufficient permissions.",Toast.LENGTH_SHORT).show();
    			
    			game_view.set_want_speech(false);
    		}
    		else if(error==SpeechRecognizer.ERROR_NETWORK){
    			Toast.makeText(getApplicationContext(),"Speech recognition failed due to network error. Does your device have a network connection?",Toast.LENGTH_SHORT).show();
    			
    			game_view.set_want_speech(false);
    		}
    		else if(error==SpeechRecognizer.ERROR_NETWORK_TIMEOUT){
    			Toast.makeText(getApplicationContext(),"Speech recognition failed due to network timeout.",Toast.LENGTH_SHORT).show();
    			
    			game_view.set_want_speech(false);
    		}
    		else if(error==SpeechRecognizer.ERROR_SERVER){
    			Toast.makeText(getApplicationContext(),"Speech recognition failed due to server error.",Toast.LENGTH_SHORT).show();
    			
    			game_view.set_want_speech(false);
    		}
    		
    		stop_speech_recognition();
    	}
    	
    	@Override
    	public void onEvent(int event_type,Bundle params){
    		
    	}
    	
    	@Override
    	public void onPartialResults(Bundle partial_results){
    		
    	}
    	
    	@Override
    	public void onReadyForSpeech(Bundle params){
    		
    	}
    	
    	@Override
    	public void onResults(Bundle results){
    		process_speech(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
    		
    		stop_speech_recognition();
    	}
    	
    	@Override
    	public void onRmsChanged(float rmsdB){
    		
    	}
    };
    
    public void process_speech(ArrayList<String> speech_list){
    	//The index into the speech list of the "correct" speech.
    	int correct_speech=-1;
    	int speech_command=Speech.NONE;
    	
    	//Check for a speech item matching a command.
		for(int i=0;i<speech_list.size();i++){
			String speech=speech_list.get(i).trim().toLowerCase();
		
			int get_speech_command=Speech.process(speech,game_view.get_pet().get_status().name);
			
			if(get_speech_command!=Speech.NONE){
				correct_speech=i;
				speech_command=get_speech_command;
				break;
			}
		}
		
		//If none of the speech items match a command.
		if(correct_speech==-1 && speech_list.size()>0){
			correct_speech=0;
		}
		
		if(correct_speech>=0){
			String speech=speech_list.get(correct_speech).trim().toLowerCase();
			speech=Strings.first_letter_capital(speech);
			
			Toast.makeText(getApplicationContext(),"\""+speech+"\"",Toast.LENGTH_SHORT).show();
			
			handle_speech_command(speech_command);
		}
    }
    
    public void handle_speech_command(int speech_command){
    	if(speech_command==Speech.HAPPY){
    		game_view.get_pet().get_status().sleeping_wake_up();
    		
    		game_view.get_pet().get_status().queue_thought(Thought_Type.HAPPY);
		}
		else if(speech_command==Speech.STATUS){
			Button b=(Button)findViewById(R.id.button_status);
			button_status(b);
		}
		else if(speech_command==Speech.STORE){
			Button b=(Button)findViewById(R.id.button_store);
			button_store(b);
		}
		else if(speech_command==Speech.CLEAN){
			Button b=(Button)findViewById(R.id.button_clean);
			button_clean(b);
		}
		else if(speech_command==Speech.BATHE){
			Button b=(Button)findViewById(R.id.button_bathe);
			button_bathe(b);
		}
		else if(speech_command==Speech.TEMPERATURE){
			Button b=(Button)findViewById(R.id.button_temp);
			button_temp(b);
		}
		else if(speech_command==Speech.GAMES){
			Button b=(Button)findViewById(R.id.button_games);
			button_games(b);
		}
		else if(speech_command==Speech.EQUIPMENT){
			Button b=(Button)findViewById(R.id.button_equipment);
			button_equipment(b);
		}
		else if(speech_command==Speech.BATTLE_BLUETOOTH){
			((Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_bluetooth)).performClick();
		}
		else if(speech_command==Speech.LIGHTS){
			Button b=(Button)findViewById(R.id.button_light);
			button_light(b);
		}
		else if(speech_command==Speech.OPTIONS){
			Button b=(Button)findViewById(R.id.button_options);
			button_options(b);
		}
		else if(speech_command==Speech.STORE_FOOD){
			((Button)dialog_store.findViewById(R.id.button_dialog_main_store_food)).performClick();
		}
		else if(speech_command==Speech.STORE_DRINKS){
			((Button)dialog_store.findViewById(R.id.button_dialog_main_store_drinks)).performClick();
		}
		else if(speech_command==Speech.STORE_TREATMENTS){
			((Button)dialog_store.findViewById(R.id.button_dialog_main_store_treatments)).performClick();
		}
		else if(speech_command==Speech.STORE_PERMA){
			((Button)dialog_store.findViewById(R.id.button_dialog_main_store_perma)).performClick();
		}
		else if(speech_command==Speech.AC){
			((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_ac)).performClick();
		}
		else if(speech_command==Speech.HEATER){
			((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_heater)).performClick();
		}
		else if(speech_command==Speech.NO_TEMP){
			((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_none)).performClick();
		}
		else if(speech_command==Speech.BRICKS){
			((Button)dialog_games.findViewById(R.id.button_dialog_main_games_bricks)).performClick();
		}
		else if(speech_command==Speech.RPS){
			((Button)dialog_games.findViewById(R.id.button_dialog_main_games_rps)).performClick();
		}
		else if(speech_command==Speech.ACCEL){
			((Button)dialog_games.findViewById(R.id.button_dialog_main_games_accel)).performClick();
		}
		else if(speech_command==Speech.GPS){
			((Button)dialog_games.findViewById(R.id.button_dialog_main_games_gps)).performClick();
		}
		else if(speech_command==Speech.SPEED_GPS){
			((Button)dialog_games.findViewById(R.id.button_dialog_main_games_speed_gps)).performClick();
		}
		else if(speech_command==Speech.TRAINING){
			((Button)dialog_games.findViewById(R.id.button_dialog_main_games_training)).performClick();
		}
		else if(speech_command==Speech.SAD){
			game_view.get_pet().get_status().sleeping_wake_up();
			
    		game_view.get_pet().get_status().queue_thought(Thought_Type.SAD);
		}
		else if(speech_command==Speech.MUSIC){
			game_view.get_pet().get_status().sleeping_wake_up();
			
    		game_view.get_pet().get_status().queue_thought(Thought_Type.MUSIC);
		}
		else if(speech_command==Speech.NEEDS){
			game_view.get_pet().get_status().sleeping_wake_up();
			
    		game_view.get_pet().clear_need_feedback_timers();
    		
    		if(!game_view.get_pet().get_status().sick && !game_view.get_pet().get_status().needs_bath() && !game_view.get_pet().get_status().is_starving() &&
    				!game_view.get_pet().get_status().is_very_thirsty() && !game_view.get_pet().get_status().temp_is_cold() && !game_view.get_pet().get_status().temp_is_hot() &&
    				game_view.get_pet().get_status().is_happy() && !game_view.get_pet().get_status().needs_poop_cleaned()){
    			game_view.get_pet().get_status().queue_thought(Thought_Type.HAPPY);
    		}
		}
		else if(speech_command==Speech.BATTLE_SHADOW){
			((Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_shadow)).performClick();
		}
    }
    
    public void set_game_mode(int get_game_mode,Bundle bundle){
    	game_view.set_want_speech(false);
    	stop_speech_recognition();
    	
    	if(bundle==null){
	    	bundle=new Bundle();
	    	bundle.putBoolean("null",true);
    	}
    	else{
    		bundle.putBoolean("null",false);
    	}
    	
		if(get_game_mode==Game_Mode.PET){
			Message msg=handler.obtainMessage();
			msg.what=BitBeast.HANDLER_SHOW_MAIN_BUTTONS;
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
		else if(get_game_mode==Game_Mode.WORKOUT){
			Message msg=handler.obtainMessage();
			msg.what=BitBeast.HANDLER_START_GAME_WORKOUT;
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
		else if(get_game_mode==Game_Mode.BRICKS){
			Message msg=handler.obtainMessage();
			msg.what=BitBeast.HANDLER_START_GAME_BRICKS;
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	}
    
    static final int HANDLER_SHOW_PROGRESS=0;
    static final int HANDLER_HIDE_PROGRESS=1;
    static final int HANDLER_START_GAME_WORKOUT=2;
    static final int HANDLER_SHOW_MAIN_BUTTONS=3;
    static final int HANDLER_START_GAME_BRICKS=4;
    static final int HANDLER_DIE=5;
    static final int HANDLER_SPEECH_RECOGNITION=6;
    static final int HANDLER_SCREENSHOT=7;
    static final int HANDLER_REWARDS=8;
    static final int HANDLER_SET_BUTTON_COLORS=9;
    
    private Handler handler=new Handler(){
    	@Override
    	public void handleMessage(Message msg){
    		LinearLayout ll=null;
    		TextView tv=null;
    		
    		switch(msg.what){
    		case HANDLER_SHOW_PROGRESS:
    			if(!isFinishing()){
    				showDialog(DIALOG_ID_PROGRESS);
			    }
    			break;
    		case HANDLER_HIDE_PROGRESS:
    			if(!isFinishing() && dialog_progress.isShowing()){
    				dismissDialog(DIALOG_ID_PROGRESS);
			    }
    			break;
    		case HANDLER_REWARDS:
    			if(!isFinishing()){
    				if(game_view!=null){
    					StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
    					
    					Bundle bundle=msg.getData();
    					int sound=bundle.getInt(getPackageName()+"sound");
    					bundle.remove(getPackageName()+"sound");
    					
    					if(sound!=-1){
    						Sound_Manager.play_sound(sound);
    					}
	    		    	
	    		    	Intent intent=new Intent(BitBeast.this,Activity_Rewards.class);
	    		    	intent.putExtras(bundle);
	    		    	startActivity(intent);
    				}
    			}
    			break;
    		case HANDLER_START_GAME_WORKOUT:
    			if(!isFinishing()){
	    			ll=(LinearLayout)findViewById(R.id.view_main_buttons_1);
	    			if(ll!=null){
	    				ll.setVisibility(View.GONE);
	    			}
	    			
	    			ll=(LinearLayout)findViewById(R.id.view_main_buttons_2);
	    			if(ll!=null){
	    				ll.setVisibility(View.GONE);
	    			}
	    			    			
	    			if(game_view!=null){
	    				game_view.setFocusable(true);
	    				game_view.requestFocus();
	    				
	    				game_view.new_game_mode=Game_Mode.WORKOUT;
	    				game_view.game_mode_bundle=msg.getData();
	    			}
    			}
    			break;
    		case HANDLER_SHOW_MAIN_BUTTONS:
    			if(!isFinishing()){
	    			ll=(LinearLayout)findViewById(R.id.view_main_buttons_1);
	    			if(ll!=null){
	    				ll.setVisibility(View.VISIBLE);
	    			}
	    			
	    			ll=(LinearLayout)findViewById(R.id.view_main_buttons_2);
	    			if(ll!=null){
	    				ll.setVisibility(View.VISIBLE);
	    			}
	    			
	    			if(game_view!=null){
	    				game_view.new_game_mode=Game_Mode.PET;
	    				game_view.game_mode_bundle=msg.getData();
	    			}
    			}
    			break;
    		case HANDLER_START_GAME_BRICKS:
    			if(!isFinishing()){
	    			ll=(LinearLayout)findViewById(R.id.view_main_buttons_1);
	    			if(ll!=null){
	    				ll.setVisibility(View.GONE);
	    			}
	    			
	    			ll=(LinearLayout)findViewById(R.id.view_main_buttons_2);
	    			if(ll!=null){
	    				ll.setVisibility(View.GONE);
	    			}
	    			    			
	    			if(game_view!=null){
	    				game_view.setFocusable(true);
	    				game_view.requestFocus();
	    				
	    				game_view.new_game_mode=Game_Mode.BRICKS;
	    				game_view.game_mode_bundle=msg.getData();
	    			}
    			}
    			break;
    		case HANDLER_DIE:
    			if(!isFinishing()){
    				Sound_Manager.play_sound(Sound.DIE);
    				
    				String name=msg.getData().getString("name");
    				
    	        	String message="";
    	        	
    	        	message+=name+" has died!";
    	        	
    	        	tv=(TextView)dialog_die.findViewById(R.id.dialog_die_message);
    	        	tv.setText(message);
    	        	
    				showDialog(DIALOG_ID_DIE);
			    }
    			break;
    		case HANDLER_SPEECH_RECOGNITION:
    			if(!isFinishing()){
    				if(speech!=null){
    					stop_speech_recognition();
    				}
    				else{
    					if(game_view!=null){
		    				if(!Options.pause && game_view.get_pet().get_status().age_tier!=Age_Tier.EGG && game_view.get_pet().get_status().age_tier!=Age_Tier.DEAD){
		    					if(SpeechRecognizer.isRecognitionAvailable(BitBeast.this)){
		    		            	start_speech_recognition();
		    		            }
		    		            else{
		    		            	Toast.makeText(getApplicationContext(),"Your device doesn't seem to support speech recognition!",Toast.LENGTH_SHORT).show();
		    		            	game_view.set_want_speech(false);
		    		            }
		    		    	}
		    		    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.EGG){
		    		    		deny_egg();
		    		    		game_view.set_want_speech(false);
		    		    	}
		    		    	else if(game_view.get_pet().get_status().age_tier==Age_Tier.DEAD){
		    		    		deny_dead();
		    		    		game_view.set_want_speech(false);
		    		    	}
		    		    	else if(Options.pause){
		    		    		deny_paused();
		    		    		game_view.set_want_speech(false);
		    		    	}
    					}
    				}
    			}
    			break;
    		case HANDLER_SCREENSHOT:
    			if(!isFinishing()){
    				Bitmap bitmap=(Bitmap)msg.obj;
    				
    				String save_location=StorageManager.save_screenshot(BitBeast.this,bitmap,findViewById(R.id.root_main));
    		    	
    		    	if(save_location.length()>0){
    		    		Toast.makeText(BitBeast.this,"Screenshot saved to "+save_location+".",Toast.LENGTH_SHORT).show();
    		    	}
			    }
    			break;
    		case HANDLER_SET_BUTTON_COLORS:
    			if(!isFinishing()){
	    			set_button_colors();
    			}
    			break;
    		}
    	}
	};
	
	public void close_dialogs(){
		if(dialog_progress.isShowing()){
			dismissDialog(DIALOG_ID_PROGRESS);
	    }
		
		if(dialog_store.isShowing()){
			dismissDialog(DIALOG_ID_STORE);
	    }
		
		if(dialog_temp.isShowing()){
			dismissDialog(DIALOG_ID_TEMP);
	    }
		
		if(dialog_games.isShowing()){
			dismissDialog(DIALOG_ID_GAMES);
	    }
		
		if(dialog_die.isShowing()){
			dismissDialog(DIALOG_ID_DIE);
	    }
		
		if(dialog_battle.isShowing()){
			dismissDialog(DIALOG_ID_BATTLE);
	    }
	}
	
	public void set_dialog_buttons(){
		((Button)dialog_store.findViewById(R.id.button_dialog_main_store_food)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
		    	
		    	Intent intent=new Intent(BitBeast.this,Activity_Store.class);
		    	
		    	Bundle bundle=new Bundle();
		    	bundle.putInt(getPackageName()+"section",Store_Section.FOOD);
		    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
		    	
		    	intent.putExtras(bundle);
		    	startActivity(intent);
				
				if(dialog_store.isShowing()){
					dismissDialog(DIALOG_ID_STORE);
			    }
			}
		});
		((Button)dialog_store.findViewById(R.id.button_dialog_main_store_drinks)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
		    	
		    	Intent intent=new Intent(BitBeast.this,Activity_Store.class);
		    	
		    	Bundle bundle=new Bundle();
		    	bundle.putInt(getPackageName()+"section",Store_Section.DRINKS);
		    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
		    	
		    	intent.putExtras(bundle);
		    	startActivity(intent);
				
				if(dialog_store.isShowing()){
					dismissDialog(DIALOG_ID_STORE);
			    }
			}
		});
		((Button)dialog_store.findViewById(R.id.button_dialog_main_store_treatments)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
		    	
		    	Intent intent=new Intent(BitBeast.this,Activity_Store.class);
		    	
		    	Bundle bundle=new Bundle();
		    	bundle.putInt(getPackageName()+"section",Store_Section.TREATMENTS);
		    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
		    	
		    	intent.putExtras(bundle);
		    	startActivity(intent);
				
				if(dialog_store.isShowing()){
					dismissDialog(DIALOG_ID_STORE);
			    }
			}
		});
		((Button)dialog_store.findViewById(R.id.button_dialog_main_store_perma)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
		    	
		    	Intent intent=new Intent(BitBeast.this,Activity_Store.class);
		    	
		    	Bundle bundle=new Bundle();
		    	bundle.putInt(getPackageName()+"section",Store_Section.PERMA);
		    	bundle.putInt(getPackageName()+"move_direction",Direction.NONE);
		    	
		    	intent.putExtras(bundle);
		    	startActivity(intent);
				
				if(dialog_store.isShowing()){
					dismissDialog(DIALOG_ID_STORE);
			    }
			}
		});
		
		((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_ac)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Sound_Manager.play_sound(Sound.AC);
				
				game_view.get_pet().get_status().ac=true;
				game_view.get_pet().get_status().heater=false;
				
				if(dialog_temp.isShowing()){
					dismissDialog(DIALOG_ID_TEMP);
			    }
			}
		});
        ((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_heater)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Sound_Manager.play_sound(Sound.HEATER);
				
				game_view.get_pet().get_status().ac=false;
				game_view.get_pet().get_status().heater=true;
				
				if(dialog_temp.isShowing()){
					dismissDialog(DIALOG_ID_TEMP);
			    }
			}
		});
        ((Button)dialog_temp.findViewById(R.id.button_dialog_main_temp_none)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				game_view.get_pet().get_status().ac=false;
				game_view.get_pet().get_status().heater=false;
				
				if(dialog_temp.isShowing()){
					dismissDialog(DIALOG_ID_TEMP);
			    }
			}
		});
        
        ((Button)dialog_games.findViewById(R.id.button_dialog_main_games_training)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
	    		if(game_view.get_pet().get_status().get_energy()>=Pet_Status.ENERGY_LOSS_WORKOUT){
		    		game_view.get_pet().get_status().sleeping_wake_up();
		    		
		    		set_game_mode(Game_Mode.WORKOUT,null);
		    		
		    		if(dialog_games.isShowing()){
						dismissDialog(DIALOG_ID_GAMES);
				    }
	    		}
	    		else{
	    			Sound_Manager.play_sound(Sound.NO_STAT_POINTS);
	    			int energy_short=Pet_Status.ENERGY_LOSS_WORKOUT-game_view.get_pet().get_status().get_energy();
	    			Toast.makeText(getApplicationContext(),game_view.get_pet().get_status().name+" needs "+energy_short+" more energy to train!",Toast.LENGTH_SHORT).show();
	    		}
			}
		});
        ((Button)dialog_games.findViewById(R.id.button_dialog_main_games_bricks)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				game_view.get_pet().get_status().sleeping_wake_up();
	    		
	    		set_game_mode(Game_Mode.BRICKS,null);
				
				if(dialog_games.isShowing()){
					dismissDialog(DIALOG_ID_GAMES);
			    }
			}
		});
        ((Button)dialog_games.findViewById(R.id.button_dialog_main_games_rps)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				game_view.get_pet().get_status().sleeping_wake_up();
	    		
				StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
		    	
		    	Intent intent=new Intent(BitBeast.this,Activity_Game_RPS.class);
		    	startActivity(intent);
				
				if(dialog_games.isShowing()){
					dismissDialog(DIALOG_ID_GAMES);
			    }
			}
		});
        ((Button)dialog_games.findViewById(R.id.button_dialog_main_games_accel)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				SensorManager sensors=(SensorManager)getSystemService(SENSOR_SERVICE);
				List<Sensor> sensor_list=sensors.getSensorList(Sensor.TYPE_ACCELEROMETER);
				
				if(!sensor_list.isEmpty()){
					game_view.get_pet().get_status().sleeping_wake_up();
		    		
					StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
			    	
			    	Intent intent=new Intent(BitBeast.this,Activity_Game_Accel.class);
			    	startActivity(intent);
					
					if(dialog_games.isShowing()){
						dismissDialog(DIALOG_ID_GAMES);
				    }
				}
				else{
					Toast.makeText(getApplicationContext(),"Your device doesn't seem to have an accelerometer!",Toast.LENGTH_SHORT).show();
				}
			}
		});
        ((Button)dialog_games.findViewById(R.id.button_dialog_main_games_gps)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				LocationManager locations=(LocationManager)getSystemService(LOCATION_SERVICE);
				List<String> provider_list=locations.getProviders(false);
				
				if(provider_list.contains(LocationManager.GPS_PROVIDER)){
					game_view.get_pet().get_status().sleeping_wake_up();
		    		
					StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
			    	
			    	Intent intent=new Intent(BitBeast.this,Activity_Game_GPS.class);
			    	startActivity(intent);
					
					if(dialog_games.isShowing()){
						dismissDialog(DIALOG_ID_GAMES);
				    }
				}
				else{
					Toast.makeText(getApplicationContext(),"Your device doesn't seem to have a GPS receiver!",Toast.LENGTH_SHORT).show();
				}
			}
		});
        ((Button)dialog_games.findViewById(R.id.button_dialog_main_games_speed_gps)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				LocationManager locations=(LocationManager)getSystemService(LOCATION_SERVICE);
				List<String> provider_list=locations.getProviders(false);
				
				if(provider_list.contains(LocationManager.GPS_PROVIDER)){
					game_view.get_pet().get_status().sleeping_wake_up();
		    		
					StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
			    	
			    	Intent intent=new Intent(BitBeast.this,Activity_Game_Speed_GPS.class);
			    	startActivity(intent);
					
					if(dialog_games.isShowing()){
						dismissDialog(DIALOG_ID_GAMES);
				    }
				}
				else{
					Toast.makeText(getApplicationContext(),"Your device doesn't seem to have a GPS receiver!",Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        ((Button)dialog_die.findViewById(R.id.button_dialog_die_ok)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(dialog_die.isShowing()){
					dismissDialog(DIALOG_ID_DIE);
			    }
			}
		});
        
        ((Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_shadow)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				game_view.get_pet().get_status().sleeping_wake_up();
	    		
				StorageManager.save_pet_status(BitBeast.this,game_view.get_pet().get_status());
				
				Pet_Status them=new Pet_Status();
				
				//Randomly determine which perma items the shadow has.
				for(int i=0;i<game_view.get_pet().get_status().perma_items.size();i++){
					if(RNG.random_range(0,99)<65){
						them.perma_items.add(new Perma_Item(null,null,game_view.get_pet().get_status().perma_items.get(i).name,0.0f,0.0f));
					}
				}
				
				//Randomly determine which food buffs the shadow has.
				if(game_view.get_pet().get_status().buff_energy_max>0 && RNG.random_range(0,99)<65){
					them.buff_energy_max=1;
				}
				if(game_view.get_pet().get_status().buff_strength_max>0 && RNG.random_range(0,99)<65){
					them.buff_strength_max=1;
				}
				if(game_view.get_pet().get_status().buff_dexterity_max>0 && RNG.random_range(0,99)<65){
					them.buff_dexterity_max=1;
				}
				if(game_view.get_pet().get_status().buff_stamina_max>0 && RNG.random_range(0,99)<65){
					them.buff_stamina_max=1;
				}
				if(game_view.get_pet().get_status().buff_magic_find>0 && RNG.random_range(0,99)<65){
					them.buff_magic_find=1;
				}
				
				//Set the shadow's age tier to +/- 1 from the pet's age tier.
				int age_change=RNG.random_range(0,99);
				them.age_tier=game_view.get_pet().get_status().age_tier;
				if(age_change>=0 && age_change<85){
					//Age tier stays the same.
				}
				else if(age_change>=85 && age_change<95 && them.age_tier.get_previous()!=Age_Tier.EGG){
					them.age_tier=them.age_tier.get_previous();
				}
				else if(age_change>=95 && age_change<100 && them.age_tier.get_next()!=Age_Tier.DEAD){
					them.age_tier=them.age_tier.get_next();
				}
				
				//Randomly determine a pet type within the shadow's age tier.
		    	them.type=Age_Tier.get_random_pet_type(them.age_tier);
		    	
		    	//Set the shadow's name and color.
		    	them.name=game_view.get_pet().get_status().name+"'s shadow";
		    	them.color=Color.rgb(0,0,0);
		    	
		    	//Randomly determine the shadow's hunger and thirst levels.
		    	them.hunger=(short)RNG.random_range((int)Age_Tier.get_hunger_max(them.age_tier)/4,(int)Age_Tier.get_hunger_max(them.age_tier));
		    	them.thirst=(short)RNG.random_range((int)Pet_Status.THIRST_MAX/4,(int)Pet_Status.THIRST_MAX);
		    	
		    	//Randomly determine if the shadow is sick.
		    	if(RNG.random_range(0,99)<them.get_sick_chance()){
		    		them.sick=true;
		    	}
		    	
		    	//Randomly determine the shadow's weight.
		    	them.weight=0.01*(double)RNG.random_range((int)(Pet_Status.WEIGHT_MIN*100.0),(int)((Pet_Status.OBESITY/2.0)*1.25*100.0));
		    	
		    	//Determine shadow's energy.
		    	them.energy=(short)RNG.random_range(Age_Tier.get_energy_max(them.age_tier)/2,Age_Tier.get_energy_max(them.age_tier));
		    	
		    	//Determine shadow's level.
		    	int level_min=-3;
		    	int level_max=1;
		    	if(them.age_tier.ordinal()<game_view.get_pet().get_status().age_tier.ordinal()){
		    		level_max=-1;
		    	}
		    	else if(them.age_tier.ordinal()>game_view.get_pet().get_status().age_tier.ordinal()){
		    		level_min=0;
		    	}
		    	int random_level=RNG.random_range(game_view.get_pet().get_status().level+level_min,game_view.get_pet().get_status().level+level_max);
		    	if(random_level<1){
		    		random_level=1;
		    	}
		    	else if(random_level>Pet_Status.MAX_LEVEL){
		    		random_level=Pet_Status.MAX_LEVEL;
		    	}
		    	for(int i=1;i<random_level;i++){
		    		them.gain_experience(them.experience_max-them.experience,true);
		    	}
		    	
		    	//Spend shadow's stat points.
		    	while(them.stat_points>0){
		    		short branch=Pet_Type.get_pet_branch(them.type);
		    		
		    		int random=RNG.random_range(0,99);
		    		
		    		//Spend stat point on primary stat.
		    		if(random>=0 && random<80){
		    			if(branch==Pet_Type.BRANCH_NONE || branch==Pet_Type.BRANCH_TYRANNO || branch==Pet_Type.BRANCH_APATO){
		    				them.strength_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.strength_max_bound();
		    				them.stat_points--;
		    			}
		    			else if(branch==Pet_Type.BRANCH_PTERO){
		    				them.dexterity_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.dexterity_max_bound();
		    				them.stat_points--;
		    			}
		    			else if(branch==Pet_Type.BRANCH_STEGO || branch==Pet_Type.BRANCH_TRICERA){
		    				them.stamina_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.stamina_max_bound();
		    				them.stat_points--;
		    			}
		    		}
		    		//Spend stat point on secondary stat.
		    		else if(random>=80 && random<95){
		    			if(branch==Pet_Type.BRANCH_NONE || branch==Pet_Type.BRANCH_STEGO || branch==Pet_Type.BRANCH_PTERO){
		    				them.strength_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.strength_max_bound();
		    				them.stat_points--;
		    			}
		    			else if(branch==Pet_Type.BRANCH_TYRANNO || branch==Pet_Type.BRANCH_TRICERA){
		    				them.dexterity_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.dexterity_max_bound();
		    				them.stat_points--;
		    			}
		    			else if(branch==Pet_Type.BRANCH_APATO){
		    				them.stamina_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.stamina_max_bound();
		    				them.stat_points--;
		    			}
		    		}
		    		//Spend stat point on tertiary stat.
		    		else{
		    			if(branch==Pet_Type.BRANCH_NONE || branch==Pet_Type.BRANCH_TRICERA){
		    				them.strength_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.strength_max_bound();
		    				them.stat_points--;
		    			}
		    			else if(branch==Pet_Type.BRANCH_APATO || branch==Pet_Type.BRANCH_STEGO){
		    				them.dexterity_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.dexterity_max_bound();
		    				them.stat_points--;
		    			}
		    			else if(branch==Pet_Type.BRANCH_PTERO || branch==Pet_Type.BRANCH_TYRANNO){
		    				them.stamina_max+=Pet_Status.STAT_GAIN_SELECTION;
		    				them.stamina_max_bound();
		    				them.stat_points--;
		    			}
		    		}
		    	}
		    	
		    	them.strength=them.strength_max;
		    	them.dexterity=them.dexterity_max;
		    	them.stamina=them.stamina_max;
		    	
		    	///Once equipment has been added, randomly add equipment here, scaled near the player, and using the shadow's magic find.
		    	for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
		    		String equipment_gained=them.new_equipment(65,them.level,Equipment.slot_to_string((short)i));
		    		
		    		if(equipment_gained.length()>0){
		    			them.equipment_slots.set(i,them.equipment.get(0));
		    			
		    			them.equipment.remove(0);
		    		}
		    	}
		    	
		    	//Start the actual battle activity, passing it the two pets' data.
				Intent intent=new Intent(BitBeast.this,Activity_Battle.class);
				
				Bundle bundle=new Bundle();
				bundle.putBoolean(getPackageName()+".server",false);
				bundle.putBoolean(getPackageName()+".shadow",true);
				bundle.putInt(getPackageName()+".our_seed",RNG.random_range(0,Integer.MAX_VALUE));
				bundle.putInt(getPackageName()+".their_seed",RNG.random_range(0,Integer.MAX_VALUE));
				bundle.putAll(them.write_bundle_battle_data(getPackageName()));
				
				intent.putExtras(bundle);
		    	startActivity(intent);
				
				if(dialog_battle.isShowing()){
					dismissDialog(DIALOG_ID_BATTLE);
			    }
			}
		});
        ((Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_bluetooth)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				BluetoothAdapter bluetooth_adapter=BluetoothAdapter.getDefaultAdapter();
    			
    			if(bluetooth_adapter!=null){
    				if(!bluetooth_adapter.isEnabled()){
    					Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    			    	startActivityForResult(intent,REQUEST_ENABLE_BLUETOOTH);
    				}
    				else{
    					start_battle_menu();
    				}
    			}
    			else{
    				Toast.makeText(getApplicationContext(),"Your device doesn't seem to support Bluetooth!",Toast.LENGTH_SHORT).show();
    			}
				
				///QQQ
				/**Intent intent=new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE","QR_CODE_MODE");
				startActivityForResult(intent,REQUEST_QR_CODE);*/
				///
				
				if(dialog_battle.isShowing()){
					dismissDialog(DIALOG_ID_BATTLE);
			    }
			}
		});
        ((Button)dialog_battle.findViewById(R.id.button_dialog_main_battle_cancel)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(dialog_battle.isShowing()){
					dismissDialog(DIALOG_ID_BATTLE);
			    }
			}
		});
	}
}