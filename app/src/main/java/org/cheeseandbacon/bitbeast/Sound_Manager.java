/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;

public class Sound_Manager{
	private static final int MAX_AUDIO_STREAMS=8;

    private static AudioManager audio_manager;
    private static SoundPool soundpool;
    private static SparseArray<Sound> sounds;
	
	public static synchronized void startup (Context context) {
		initSounds(context);
		loadSounds(context);
	}

    public static synchronized void cleanup () {
        if (audio_manager != null) {
            audio_manager.unloadSoundEffects();
            audio_manager = null;
        }

        if (soundpool != null) {
            soundpool.release();
            soundpool = null;
        }

        if (sounds != null) {
            sounds.clear();
            sounds = null;
        }
    }

    private static synchronized void initSounds (Context context) {
        if (audio_manager == null) {
            audio_manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        if (soundpool == null) {
            soundpool = new SoundPool.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .build())
                    .setMaxStreams(MAX_AUDIO_STREAMS)
                    .build();
        }

        if (sounds == null) {
            sounds = new SparseArray<>();
        }
    }
	
	private static synchronized void loadSounds (Context context) {
	    for (int i = 0; i < Sound.END; i++) {
	        loadSound(context, i);
        }
	}

    private static synchronized void loadSound (Context context, int soundId) {
        sounds.put(soundId, new Sound(soundpool.load(context, Sound.getResourceId(soundId), 1)));
    }
	
	public static synchronized void playSound (Context context, int soundId) {
	    initSounds(context);

	    Sound sound = sounds.get(soundId);

        if (sound != null) {
            float stream_volume = (float) audio_manager.getStreamVolume(AudioManager.STREAM_MUSIC);
            stream_volume = stream_volume / (float) audio_manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            if (soundpool.play(sound.getSoundPoolId(), stream_volume, stream_volume, 1, 0, 1.0f) == 0) {
                StorageManager.error_log_add(context, "Sound_Manager", "Failed to play sound!", null);
            }
        } else {
            loadSound(context, soundId);
        }
	}
}
