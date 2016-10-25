/**package org.cheeseandbacon.bitbeast;

import java.nio.charset.Charset;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.media.AudioManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_NFC extends Activity{
	private Pet_Status pet_status;
	
	private NfcAdapter adapter;
	
	private boolean sender;
	
	PendingIntent pending_intent;
	IntentFilter[] intent_filters;
	String[][] tech_lists;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Bundle bundle=getIntent().getExtras();
    	sender=bundle.getBoolean("sender",false);
        
        Font.set_typeface((TextView)findViewById(R.id.nfc_test));
        Font.set_typeface((Button)findViewById(R.id.button_nfc_send));
        
        /**TextView tv=null;
    	
    	tv=(TextView)findViewById(R.id.about_version);
    	tv.setText(getResources().getString(R.string.version)+" "+app_version);*/
        
        /**adapter=NfcAdapter.getDefaultAdapter();
        
    	if(adapter==null){
    		Toast.makeText(getApplicationContext(),"Your device doesn't seem to support NFC!",Toast.LENGTH_SHORT).show();
    		
    		finish();
    		return;
    	}
    	
    	///Should also check for whether the NFC adapter is actually on.
        ///Handle this like the other minigames handle their respective features.
    	if(!adapter.isEnabled()){
    		Toast.makeText(getApplicationContext(),"NFC is currently disabled!",Toast.LENGTH_SHORT).show();
    		
    		finish();
    		return;
    	}
    	
    	if(!sender){
	    	pending_intent=PendingIntent.getActivity(this,0,new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
	    	
	    	IntentFilter ndef=new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
	    	try{
	    		ndef.addDataType("application/org.cheeseandbacon.bitbeast");
	    	}
	    	catch(MalformedMimeTypeException e){
	    	}
	    	
	    	intent_filters=new IntentFilter[]{ndef};
	    	
	    	tech_lists=new String[][]{new String[]{Ndef.class.getName()}};
    	}
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_nfc));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,pet_status);
    	
	    if(!sender){
	    	adapter.enableForegroundDispatch(this,pending_intent,intent_filters,tech_lists);
	    }
	    else{
	    	send_nfc();
	    }
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	if(sender){
    		adapter.disableForegroundNdefPush(this);
    	}
    	else{
    		adapter.disableForegroundDispatch(this);
    	}
    }
	@Override
    public boolean onSearchRequested(){
    	String save_location=StorageManager.save_screenshot(this,findViewById(R.id.root_nfc));
    	
    	if(save_location.length()>0){
    		Toast.makeText(getApplicationContext(),"Screenshot saved to "+save_location+".",Toast.LENGTH_SHORT).show();
    	}
    	
    	return false;
    }
	@Override
	public void onNewIntent(Intent intent){
		///Tag tag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		process_intent(intent);
	}
	
	public void button_send(View view){
		send_nfc();
	}
	
	public void send_nfc(){
		NdefRecord[] records=new NdefRecord[1];
		records[0]=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"application/org.cheeseandbacon.bitbeast".getBytes(Charset.forName("UTF-8")),new byte[0],
				"Hello :-D".getBytes(Charset.forName("UTF-8")));
		
		NdefMessage msg=new NdefMessage(records);
		
		adapter.enableForegroundNdefPush(this,msg);
		
		byte[] payload_bytes=msg.getRecords()[0].getPayload();
		String payload=new String(payload_bytes);
		
		Toast.makeText(getApplicationContext(),"Sending: "+payload,Toast.LENGTH_SHORT).show();
	}
	
	public void process_intent(Intent intent){
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
			///
			Toast.makeText(getApplicationContext(),"Whoa dude!",Toast.LENGTH_SHORT).show();
			
			Parcelable[] raw_msgs=intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage msg;
			
			if(raw_msgs!=null){
				msg=(NdefMessage)raw_msgs[0];
				
				///Do stuff with data.
				byte[] payload_bytes=msg.getRecords()[0].getPayload();
				String payload=new String(payload_bytes);
				
				Toast.makeText(getApplicationContext(),payload,Toast.LENGTH_SHORT).show();
			}
		}
	}
}*/
