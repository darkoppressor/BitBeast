package org.cheeseandbacon.bitbeast;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.backup.BackupAgent;
import android.app.backup.BackupDataOutput;
import android.os.ParcelFileDescriptor;

public class Backup_Agent extends BackupAgent{
	public void onBackup(ParcelFileDescriptor oldState,BackupDataOutput data,ParcelFileDescriptor newState){
		boolean do_backup=false;
		
		DataInputStream in=new DataInputStream(new FileInputStream(oldState.getFileDescriptor()));
		
		try{
			//Get the last modified timestamp from the state file and the data file.
			long timestamp_state=in.readLong();
			long timestamp_data=;
			
			if(timestamp_state!=timestamp_data){
				do_backup=true;
			}
			else{
				return;
			}
		}
		catch(IOException e){
			//Could not read state, so do a backup for sure.
			do_backup=true;
		}
		
		if(do_backup){
			
		}
	}
}
