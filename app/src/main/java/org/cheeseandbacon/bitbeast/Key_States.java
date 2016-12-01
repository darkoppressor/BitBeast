package org.cheeseandbacon.bitbeast;

import android.view.KeyEvent;

import java.util.ArrayList;

public class Key_States{
	ArrayList<Boolean> keys;
	
	public Key_States(){
		keys=new ArrayList<Boolean>();
		
		//Not sure how many keycodes there are, so just make this a safe big number.
		for(int i=0;i<350;i++){
			keys.add(false);
		}
	}
	
	public boolean key_needed(int keyCode){
		if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT ||
				keyCode==KeyEvent.KEYCODE_DPAD_UP ||
				keyCode==KeyEvent.KEYCODE_DPAD_RIGHT ||
				keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
			return true;
		}
		
		return false;
	}
	
	public boolean on_key_down(int keyCode){
		if(key_needed(keyCode)){
			keys.set(keyCode,true);
		
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean on_key_up(int keyCode){
		if(key_needed(keyCode)){
			keys.set(keyCode,false);
			
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean key(int keyCode){
		return keys.get(keyCode).booleanValue();
	}
}
