/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;


import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;

public class Activity_Name extends AppCompatActivity implements ColorPickerSwatch.OnColorSelectedListener {
	private static final int COLORS = 17;
	private static final int COLOR_PICKER_COLUMNS = 4;
	private static final String COLOR_PICKER_TAG = "colorPickerDialog";

	private int[] colors;
	private Pet_Status pet_status;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

		colors = new int[COLORS];
		colors[0] = getResources().getColor(R.color.pet_color_0);
		colors[1] = getResources().getColor(R.color.pet_color_1);
		colors[2] = getResources().getColor(R.color.pet_color_2);
		colors[3] = getResources().getColor(R.color.pet_color_3);
		colors[4] = getResources().getColor(R.color.pet_color_4);
		colors[5] = getResources().getColor(R.color.pet_color_5);
		colors[6] = getResources().getColor(R.color.pet_color_6);
		colors[7] = getResources().getColor(R.color.pet_color_7);
		colors[8] = getResources().getColor(R.color.pet_color_8);
		colors[9] = getResources().getColor(R.color.pet_color_9);
		colors[10] = getResources().getColor(R.color.pet_color_10);
		colors[11] = getResources().getColor(R.color.pet_color_11);
		colors[12] = getResources().getColor(R.color.pet_color_12);
		colors[13] = getResources().getColor(R.color.pet_color_13);
		colors[14] = getResources().getColor(R.color.pet_color_14);
		colors[15] = getResources().getColor(R.color.pet_color_15);
		colors[16] = getResources().getColor(R.color.pet_color_16);
        
        pet_status=new Pet_Status();
        pet_status.color = colors[RNG.random_range(0, COLORS - 1)];
        
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.text_name_label));
        Font.set_typeface(getAssets(), (EditText)findViewById(R.id.text_name_entry));
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_name_ok));
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_name_exit));
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.button_name_color));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.text_name_intro));
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

	public void onColorSelected (int color) {
		pet_status.color = color;
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
	}

    public void button_color(View view){
		ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(R.string.name_color_picker, R.style.ColorPickerDialog, colors, pet_status.color, COLOR_PICKER_COLUMNS, colors.length);
		colorPickerDialog.setOnColorSelectedListener(this);
		colorPickerDialog.show(getFragmentManager(), COLOR_PICKER_TAG);
    }
}
