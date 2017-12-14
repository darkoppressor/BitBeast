/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import java.util.ArrayList;

public class Sound_Manager{
	private static final int MAX_AUDIO_STREAMS=8;
	
	private static Sound_Manager instance;
	private static SoundPool soundpool;
	private static SparseIntArray soundpool_map;
	private static AudioManager audio_manager;

	//Keeps track of whether or not a given sound is loaded into memory.
	private static ArrayList<Boolean> load_status_sounds;
	
	private Sound_Manager(){
	}
	
	public static synchronized void startup(Context context){
		get_instance();
		init_sounds(context);
		load_sounds(context);
	}
	
	private static synchronized void get_instance(){
		if(instance==null){
			instance=new Sound_Manager();
		}
	}
	
	private static synchronized void init_sounds(Context context){
		soundpool = new SoundPool.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .build())
                .setMaxStreams(MAX_AUDIO_STREAMS)
                .build();
		soundpool_map=new SparseIntArray();
		audio_manager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		
		soundpool.setOnLoadCompleteListener(listener);
	}
	
	private static synchronized void add_sound(Context context, int index, int sound_id){
		load_status_sounds.add(false);
		
		soundpool_map.put(index,soundpool.load(context,sound_id,1));
	}
	
	private static synchronized void load_sounds(Context context){
		load_status_sounds= new ArrayList<>();
		load_status_sounds.add(false);
		
		add_sound(context, Sound.AC,R.raw.ac);
		add_sound(context, Sound.BATHE,R.raw.bathe);
		add_sound(context, Sound.CLEAN,R.raw.clean);
		add_sound(context, Sound.DRINK,R.raw.drink);
		add_sound(context, Sound.EAT,R.raw.eat);
		add_sound(context, Sound.EVOLUTION_1,R.raw.evolution_1);
		add_sound(context, Sound.EVOLUTION_2,R.raw.evolution_2);
		add_sound(context, Sound.GAME_HIT_BRICK,R.raw.game_hit_brick);
		add_sound(context, Sound.GAME_HIT_PADDLE,R.raw.game_hit_paddle);
		add_sound(context, Sound.GAME_HIT_WALL,R.raw.game_hit_wall);
		add_sound(context, Sound.GAME_LOSS,R.raw.game_loss);
		add_sound(context, Sound.GAME_RESET_LOSS,R.raw.game_reset_loss);
		add_sound(context, Sound.GAME_WIN,R.raw.game_win);
		add_sound(context, Sound.HEATER,R.raw.heater);
		add_sound(context, Sound.MEDICINE,R.raw.medicine);
		add_sound(context, Sound.POOP,R.raw.poop);
		add_sound(context, Sound.THOUGHT,R.raw.thought);
		add_sound(context, Sound.TOGGLE_LIGHT,R.raw.toggle_light);
		add_sound(context, Sound.DIE,R.raw.die);
		add_sound(context, Sound.SPEECH_START,R.raw.speech_start);
		add_sound(context, Sound.SPEECH_STOP,R.raw.speech_stop);
		add_sound(context, Sound.BATTLE_HIT,R.raw.battle_hit);
		add_sound(context, Sound.BATTLE_MISS,R.raw.battle_miss);
		add_sound(context, Sound.PERMA_ITEM_GRAB,R.raw.grab);
		add_sound(context, Sound.PERMA_ITEM_DROP,R.raw.drop);
		add_sound(context, Sound.LEVEL_UP,R.raw.level_up);
		add_sound(context, Sound.SPEND_STAT_POINT,R.raw.spend_stat_point);
		add_sound(context, Sound.NO_STAT_POINTS,R.raw.no_stat_points);
		add_sound(context, Sound.GAME_DRAW,R.raw.game_draw);
		add_sound(context, Sound.GAME_RESET_WIN,R.raw.game_reset_win);
		add_sound(context, Sound.POWERUP_GET,R.raw.powerup_get);
		add_sound(context, Sound.POWERUP_SPAWN,R.raw.powerup_spawn);
		add_sound(context, Sound.ITEM_SOLD,R.raw.item_sold);
		add_sound(context, Sound.EQUIPPED,R.raw.equipped);
		add_sound(context, Sound.UNEQUIPPED,R.raw.unequipped);
		add_sound(context, Sound.ITEM_SOLD_ALL,R.raw.item_sold_all);
	}
	
	public static synchronized void play_sound(Context context, int index){
        try {
            float stream_volume = (float) audio_manager.getStreamVolume(AudioManager.STREAM_MUSIC);
            stream_volume = stream_volume / (float) audio_manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            //If this sound is currently loaded into memory.
            if (load_status_sounds.get(index)) {
                if (soundpool.play(soundpool_map.get(index), stream_volume, stream_volume, 1, 0, 1.0f) == 0) {
                    StorageManager.error_log_add(context, "Sound_Manager", "Failed to play sound!", null);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            StorageManager.error_log_add(context, "Sound_Manager", "Failed to play sound!", e);
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
