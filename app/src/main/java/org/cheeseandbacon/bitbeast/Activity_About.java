/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;


import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Activity_About extends AppCompatActivity {
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.about_title));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.about_version));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.message_about));
        
        String app_version="";
        try{
        	app_version=getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
        }
        catch(NameNotFoundException e){
        	StorageManager.error_log_add(this,"Activity_About",e.getMessage(),e);
        }
        
        TextView tv=null;
    	
    	tv=(TextView)findViewById(R.id.about_version);
    	tv.setText(getResources().getString(R.string.version)+" "+app_version);
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_about));
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
