package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

public class Sound_Manager{
	private static final int MAX_AUDIO_STREAMS=12;
	
	private static Sound_Manager instance;
	private static SoundPool soundpool;
	private static SparseIntArray soundpool_map;
	private static AudioManager audio_manager;
	private static Context context;
	
	//Keeps track of whether or not a given sound is loaded into memory.
	private static ArrayList<Boolean> load_status_sounds;
	
	private Sound_Manager(){
	}
	
	public static synchronized void startup(Context get_context){
		get_instance();
		init_sounds(get_context);
		load_sounds();
	}
	
	public static synchronized Sound_Manager get_instance(){
		if(instance==null){
			instance=new Sound_Manager();
		}
		
		return instance;
	}
	
	public static synchronized void init_sounds(Context get_context){
		context=get_context;
		soundpool=new SoundPool(MAX_AUDIO_STREAMS,AudioManager.STREAM_MUSIC,0);
		soundpool_map=new SparseIntArray();
		audio_manager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		
		soundpool.setOnLoadCompleteListener(listener);
	}
	
	public static synchronized void add_sound(int index,int sound_id){
		load_status_sounds.add(false);
		
		soundpool_map.put(index,soundpool.load(context,sound_id,1));
	}
	
	public static synchronized void load_sounds(){
		load_status_sounds=new ArrayList<Boolean>();
		load_status_sounds.add(false);
		
		add_sound(Sound.AC,R.raw.ac);
		add_sound(Sound.BATHE,R.raw.bathe);
		add_sound(Sound.CLEAN,R.raw.clean);
		add_sound(Sound.DRINK,R.raw.drink);
		add_sound(Sound.EAT,R.raw.eat);
		add_sound(Sound.EVOLUTION_1,R.raw.evolution_1);
		add_sound(Sound.EVOLUTION_2,R.raw.evolution_2);
		add_sound(Sound.GAME_HIT_BRICK,R.raw.game_hit_brick);
		add_sound(Sound.GAME_HIT_PADDLE,R.raw.game_hit_paddle);
		add_sound(Sound.GAME_HIT_WALL,R.raw.game_hit_wall);
		add_sound(Sound.GAME_LOSS,R.raw.game_loss);
		add_sound(Sound.GAME_RESET_LOSS,R.raw.game_reset_loss);
		add_sound(Sound.GAME_WIN,R.raw.game_win);
		add_sound(Sound.HEATER,R.raw.heater);
		add_sound(Sound.MEDICINE,R.raw.medicine);
		add_sound(Sound.POOP,R.raw.poop);
		add_sound(Sound.THOUGHT,R.raw.thought);
		add_sound(Sound.TOGGLE_LIGHT,R.raw.toggle_light);
		add_sound(Sound.DIE,R.raw.die);
		add_sound(Sound.SPEECH_START,R.raw.speech_start);
		add_sound(Sound.SPEECH_STOP,R.raw.speech_stop);
		add_sound(Sound.BATTLE_HIT,R.raw.battle_hit);
		add_sound(Sound.BATTLE_MISS,R.raw.battle_miss);
		add_sound(Sound.PERMA_ITEM_GRAB,R.raw.grab);
		add_sound(Sound.PERMA_ITEM_DROP,R.raw.drop);
		add_sound(Sound.LEVEL_UP,R.raw.level_up);
		add_sound(Sound.SPEND_STAT_POINT,R.raw.spend_stat_point);
		add_sound(Sound.NO_STAT_POINTS,R.raw.no_stat_points);
		add_sound(Sound.GAME_DRAW,R.raw.game_draw);
		add_sound(Sound.GAME_RESET_WIN,R.raw.game_reset_win);
		add_sound(Sound.POWERUP_GET,R.raw.powerup_get);
		add_sound(Sound.POWERUP_SPAWN,R.raw.powerup_spawn);
		add_sound(Sound.ITEM_SOLD,R.raw.item_sold);
		add_sound(Sound.EQUIPPED,R.raw.equipped);
		add_sound(Sound.UNEQUIPPED,R.raw.unequipped);
		add_sound(Sound.ITEM_SOLD_ALL,R.raw.item_sold_all);
	}
	
	public static synchronized void play_sound(int index){
		try{
			float stream_volume=(float)audio_manager.getStreamVolume(AudioManager.STREAM_MUSIC);
			stream_volume=stream_volume/(float)audio_manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			
			//If this sound is currently loaded into memory.
			if(load_status_sounds.get(index)){
				soundpool.play(soundpool_map.get(index),stream_volume,stream_volume,1,0,1.0f);
			}
		}
		catch(NullPointerException e){
			StorageManager.error_log_add(context,"Sound_Manager","Failed to play sound!",e);
		}
	}
	
	public static synchronized void cleanup(){
		if(soundpool!=null){
			soundpool.release();
			soundpool=null;
		}
		
		if(soundpool_map!=null){
			soundpool_map.clear();
		}
		
		if(audio_manager!=null){
			audio_manager.unloadSoundEffects();
		}
		
		if(instance!=null){
			instance=null;
		}
	}
	
	private static SoundPool.OnLoadCompleteListener listener=new SoundPool.OnLoadCompleteListener(){
		@Override
		public void onLoadComplete(SoundPool soundPool,int sampleId,int status){
			//If the sound was successfully loaded into memory.
			if(status==0){
				load_status_sounds.set(sampleId,true);
			}
		}
	};
}
