package org.cheeseandbacon.bitbeast;


import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_About extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Font.set_typeface((TextView)findViewById(R.id.about_title));
        Font.set_typeface((TextView)findViewById(R.id.about_version));
        Font.set_typeface((TextView)findViewById(R.id.message_about));
        
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
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    }
	@Override
    public boolean onSearchRequested(){
    	String save_location=StorageManager.save_screenshot(this,findViewById(R.id.root_about));
    	
    	if(save_location.length()>0){
    		Toast.makeText(getApplicationContext(),"Screenshot saved to "+save_location+".",Toast.LENGTH_SHORT).show();
    	}
    	
    	return false;
    }
}
