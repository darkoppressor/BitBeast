/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;


import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Activity_Help_Page extends AppCompatActivity {
	private int page;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_page);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        page=getIntent().getIntExtra(getPackageName()+".page",Help_Page.HOW_TO_PLAY);
        
        TextView tv=(TextView)findViewById(R.id.message_help_page);
        
        if(page==Help_Page.HOW_TO_PLAY){
        	setTitle(getResources().getString(R.string.name_help_how_to_play));
        	tv.setText(getResources().getString(R.string.help_how_to_play));
        }
        else if(page==Help_Page.PET){
        	setTitle(getResources().getString(R.string.name_help_pet));
        	tv.setText(getResources().getString(R.string.help_pet));
        }
        else if(page==Help_Page.STORE){
        	setTitle(getResources().getString(R.string.name_help_store));
        	tv.setText(getResources().getString(R.string.help_store));
        }
        else if(page==Help_Page.PLAY){
        	setTitle(getResources().getString(R.string.name_help_play));
        	tv.setText(getResources().getString(R.string.help_play));
        }
        else if(page==Help_Page.TRAIN){
        	setTitle(getResources().getString(R.string.name_help_train));
        	tv.setText(getResources().getString(R.string.help_train));
        }
        else if(page==Help_Page.BATTLE){
        	setTitle(getResources().getString(R.string.name_help_battle));
        	tv.setText(getResources().getString(R.string.help_battle));
        }
        else if(page==Help_Page.SPEECH_RECOGNITION){
        	setTitle(getResources().getString(R.string.name_help_speech_recognition));
        	tv.setText(getResources().getString(R.string.help_speech_recognition));
        }
        else if(page==Help_Page.LEGAL){
        	setTitle(getResources().getString(R.string.name_help_legal));
        	tv.setText(getResources().getString(R.string.help_legal));
        }
        
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.message_help_page));
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_help_page));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    }
}
