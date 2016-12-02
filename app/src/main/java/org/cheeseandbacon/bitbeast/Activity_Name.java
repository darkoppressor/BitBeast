package org.cheeseandbacon.bitbeast;


import android.app.Dialog;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Name extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener{
	static final int DIALOG_ID_COLOR=0;
	
	private Pet_Status pet_status;
	
	public void colorChanged(int color){
        pet_status.color=color;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        pet_status=new Pet_Status();
        pet_status.color=Color.rgb(RNG.random_range(0,255),RNG.random_range(0,255),RNG.random_range(0,255));
        
        Font.set_typeface((TextView)findViewById(R.id.text_name_label));
        Font.set_typeface((EditText)findViewById(R.id.text_name_entry));
        Font.set_typeface((Button)findViewById(R.id.button_name_ok));
        Font.set_typeface((Button)findViewById(R.id.button_name_exit));
        Font.set_typeface((Button)findViewById(R.id.button_name_color));
        Font.set_typeface((TextView)findViewById(R.id.text_name_intro));
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_name));
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
	protected Dialog onCreateDialog(int id){
		Dialog dialog;
		
		switch(id){
		case DIALOG_ID_COLOR:
			ColorPickerDialog cp_dialog=new ColorPickerDialog(this,this,pet_status.color);
			dialog=cp_dialog;
			break;
		default:
			dialog=null;
		}
		
		return dialog;
	}
    
    public void button_ok(View view){
    	EditText et=(EditText)findViewById(R.id.text_name_entry);
    	//If the name is legitimate.
    	if(et.getEditableText().toString().trim().length()>0 && et.getEditableText().toString().trim().length()<=14){
    		pet_status.name=et.getEditableText().toString().trim();
    	
    		//Attempt to save the newly created pet, then close this screen
    		StorageManager.save_pet_status(this,pet_status);

			//New pets should always begin with pause off.
			Options.pause=false;
			StorageManager.save_options(this);

			setResult(RESULT_OK);
			this.finish();
    	}
    	else if(et.getEditableText().toString().trim().length()==0){
    		Toast.makeText(this,"The name must be at least 1 character long.",Toast.LENGTH_SHORT).show();
    	}
    	else if(et.getEditableText().toString().trim().length()>14){
    		Toast.makeText(this,"The name must be at most 14 characters long.",Toast.LENGTH_SHORT).show();
    	}
    }
    public void button_exit(View view){
    	setResult(RESULT_CANCELED);
    	this.finish();
    	return;
    }
    public void button_color(View view){
    	showDialog(DIALOG_ID_COLOR);
    }
}
