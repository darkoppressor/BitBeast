package org.cheeseandbacon.bitbeast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService{
	private static final UUID UUID_SECURE=UUID.fromString("5af0b1d0-9735-11e1-a8b0-0800200c9a66");
	private static final UUID UUID_INSECURE=UUID.fromString("47b2dc30-971f-11e1-a8b0-0800200c9a66");
	private static final String NAME_SECURE="BitBeastSecure";
	private static final String NAME_INSECURE="BitBeastInsecure";
	private static final String TAG="BluetoothService";
	public static final int STATE_NONE=0;
	public static final int STATE_LISTEN=1;
	public static final int STATE_CONNECTING=2;
	public static final int STATE_CONNECTED=3;
	
	private final BluetoothAdapter bluetooth_adapter;
	private final Handler handler;
	private final Context context;
	private BluetoothServerThread bluetooth_server_thread;
	private BluetoothClientThread bluetooth_client_thread;
	private BluetoothConnectedThread bluetooth_connected_thread;
	private BluetoothLifeThread bluetooth_life_thread;
	private int state;
	private boolean server;
	
	public BluetoothService(Context get_context,Handler get_handler){
		bluetooth_adapter=BluetoothAdapter.getDefaultAdapter();
		state=STATE_NONE;
		server=true;
		handler=get_handler;
		context=get_context;
	}
	
	private synchronized void set_state(int get_state){
		state=get_state;
		
		handler.obtainMessage(Activity_Battle_Menu.HANDLER_STATE_CHANGE,state,-1).sendToTarget();
	}
	
	private synchronized void set_server(boolean get_server){
		server=get_server;
	}
	
	public synchronized int get_state(){
		return state;
	}
	
	public synchronized boolean get_server(){
		return server;
	}
	
	public synchronized void start(){
		if(bluetooth_client_thread!=null){
			bluetooth_client_thread.cancel();
			bluetooth_client_thread=null;
		}
		
		if(bluetooth_connected_thread!=null){
			bluetooth_connected_thread.cancel();
			bluetooth_connected_thread=null;
		}
		
		set_state(STATE_LISTEN);
		
		if(bluetooth_server_thread==null){
			bluetooth_server_thread=new BluetoothServerThread(true);
			bluetooth_server_thread.start();
		}
	}
	
	public synchronized void start_all(){
		start();
		
		if(bluetooth_life_thread!=null){
			bluetooth_life_thread.cancel();
			bluetooth_life_thread=null;
		}
	}
	
	public synchronized void connect(BluetoothDevice device,boolean secure){
		if(state==STATE_CONNECTING){
			if(bluetooth_client_thread!=null){
				bluetooth_client_thread.cancel();
				bluetooth_client_thread=null;
			}
		}
		
		if(bluetooth_connected_thread!=null){
			bluetooth_connected_thread.cancel();
			bluetooth_connected_thread=null;
		}
		
		if(bluetooth_life_thread!=null){
			bluetooth_life_thread.cancel();
			bluetooth_life_thread=null;
		}
		
		if(bluetooth_server_thread!=null){
			bluetooth_server_thread.cancel();
			bluetooth_server_thread=null;
		}
		
		bluetooth_client_thread=new BluetoothClientThread(device,secure);
		bluetooth_client_thread.start();
		
		set_state(STATE_CONNECTING);
	}
	
	private synchronized void connected(BluetoothSocket socket, BluetoothDevice device, final String socket_type, boolean get_server){
		set_server(get_server);
		
		bluetooth_adapter.cancelDiscovery();
		
		if(bluetooth_client_thread!=null){
			bluetooth_client_thread.cancel();
			bluetooth_client_thread=null;
		}
		
		if(bluetooth_connected_thread!=null){
			bluetooth_connected_thread.cancel();
			bluetooth_connected_thread=null;
		}
		
		if(bluetooth_life_thread!=null){
			bluetooth_life_thread.cancel();
			bluetooth_life_thread=null;
		}
		
		if(bluetooth_server_thread!=null){
			bluetooth_server_thread.cancel();
			bluetooth_server_thread=null;
		}
		
		bluetooth_life_thread=new BluetoothLifeThread();
		bluetooth_life_thread.start();
		
		bluetooth_connected_thread=new BluetoothConnectedThread(socket,socket_type);
		bluetooth_connected_thread.start();
		
		set_state(STATE_CONNECTED);
		
		Message msg=handler.obtainMessage(Activity_Battle_Menu.HANDLER_DEVICE_NAME);
		Bundle bundle=new Bundle();
		bundle.putString("device_name",device.getName());
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public synchronized void stop(){
		if(bluetooth_client_thread!=null){
			bluetooth_client_thread.cancel();
			bluetooth_client_thread=null;
		}
		
		if(bluetooth_connected_thread!=null){
			bluetooth_connected_thread.cancel();
			bluetooth_connected_thread=null;
		}
		
		if(bluetooth_life_thread!=null){
			bluetooth_life_thread.cancel();
			bluetooth_life_thread=null;
		}
		
		if(bluetooth_server_thread!=null){
			bluetooth_server_thread.cancel();
			bluetooth_server_thread=null;
		}
		
		set_state(STATE_NONE);
	}
	
	public void write(byte[] out){
		BluetoothConnectedThread ct;
		
		synchronized(this){
			if(state!=STATE_CONNECTED){
				return;
			}
			
			ct=bluetooth_connected_thread;
		}
		
		ct.write(out);
	}
	
	public void set_done(){
		BluetoothLifeThread lt;
		
		synchronized(this){
			if(state!=STATE_CONNECTED){
				return;
			}
			
			lt=bluetooth_life_thread;
		}
		
		lt.set_done();
	}
	
	public void set_they_done(){
		BluetoothLifeThread lt;
		
		synchronized(this){
			if(state!=STATE_CONNECTED){
				return;
			}
			
			lt=bluetooth_life_thread;
		}
		
		lt.set_they_done();
	}
	
	private void toast(String message,int length){
		Message msg=handler.obtainMessage(Activity_Battle_Menu.HANDLER_TOAST);
		msg.arg1=length;
		Bundle bundle=new Bundle();
		bundle.putString("toast",message);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	private void connection_failed(){
		toast("Unable to connect to device.",Toast.LENGTH_SHORT);
		
		BluetoothService.this.start_all();
	}
	
	private void connection_lost(){
		toast("Device connection was lost.",Toast.LENGTH_SHORT);
		
		BluetoothService.this.start();
	}
	
	private class BluetoothServerThread extends Thread{
		private final BluetoothServerSocket server_socket;
		private String socket_type;
		
		public BluetoothServerThread(boolean secure){
			BluetoothServerSocket temp=null;
			socket_type=secure?"Secure":"Insecure";
			
			try{
				if(secure){
					temp=bluetooth_adapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,UUID_SECURE);
				}
				else{
					temp=bluetooth_adapter.listenUsingRfcommWithServiceRecord(NAME_INSECURE,UUID_INSECURE);
				}
			}
			catch(IOException e){
				StorageManager.error_log_add(context,TAG,"listen() on "+socket_type+" server socket failed.",e);
			}
			
			server_socket=temp;
		}
		
		public void run(){
			BluetoothSocket socket=null;
			
			while(state!=STATE_CONNECTED){
				try{
					socket=server_socket.accept();
				}
				catch(IOException e){
					StorageManager.error_log_add(context,TAG,"accept() on "+socket_type+" server socket failed.",e);
					break;
				}
				
				if(socket!=null){
					synchronized(BluetoothService.this){
						switch(state){
						case STATE_LISTEN: case STATE_CONNECTING:
							connected(socket,socket.getRemoteDevice(),socket_type,true);
							return;
						case STATE_NONE: case STATE_CONNECTED:
							try{
								socket.close();
							}
							catch(IOException e){
								StorageManager.error_log_add(context,TAG,"Failed to close unwanted socket.",e);
							}
							break;
						}
					}
				}
			}
		}
		
		public void cancel(){
			try{
				server_socket.close();
			}
			catch(IOException e){
				StorageManager.error_log_add(context,TAG,"close() on "+socket_type+" server socket failed.",e);
			}
		}
	}
	
	private class BluetoothClientThread extends Thread{
		private final BluetoothSocket socket;
		private final BluetoothDevice device;
		private String socket_type;
		
		public BluetoothClientThread(BluetoothDevice get_device,boolean secure){
			BluetoothSocket temp=null;
			device=get_device;
			socket_type=secure?"Secure":"Insecure";
			
			try{
				if(secure){
					temp=device.createRfcommSocketToServiceRecord(UUID_SECURE);
				}
				else{
					temp=device.createRfcommSocketToServiceRecord(UUID_INSECURE);
				}
			}
			catch(IOException e){
				StorageManager.error_log_add(context,TAG,"create() on "+socket_type+" client socket failed.",e);
			}
			
			socket=temp;
		}
		
		public void run(){
			bluetooth_adapter.cancelDiscovery();
			
			try{
				socket.connect();
			}
			catch(IOException e){
				StorageManager.error_log_add(context,TAG,"Connection failed.",e);
				
				try{
					socket.close();
				}
				catch(IOException ioe){
					StorageManager.error_log_add(context,TAG,"close() on "+socket_type+" client socket failed during connection failure.",ioe);
				}
				
				connection_failed();
				
				return;
			}
			
			synchronized(BluetoothService.this){
				bluetooth_client_thread=null;
			}
			
			connected(socket,device,socket_type,false);
		}
		
		public void cancel(){
			try{
				socket.close();
			}
			catch(IOException e){
				StorageManager.error_log_add(context,TAG,"close() on "+socket_type+" client socket failed.",e);
			}
		}
	}
	
	private class BluetoothConnectedThread extends Thread{
		private final BluetoothSocket socket;
		private final InputStream input;
		private final OutputStream output;
		private String socket_type;
		
		public BluetoothConnectedThread(BluetoothSocket get_socket,String get_socket_type){
			socket=get_socket;
			socket_type=get_socket_type;
			
			InputStream temp_in=null;
			OutputStream temp_out=null;
			
			try{
				temp_in=socket.getInputStream();
				temp_out=socket.getOutputStream();
			}
			catch(IOException e){
				StorageManager.error_log_add(context,TAG,"Failed to create connected socket IO streams.",e);
			}
			
			input=temp_in;
			output=temp_out;
		}
		
		public void run(){
			String packet="";
			
			while(true){
				try{
					int get_byte=input.read();
					char get_char=(char)get_byte;
					
					if(get_char!='\u0003'){
						///StorageManager.error_log_add(context,"BluetoothService","Adding packet raw data: \""+get_char+"\"",null);
						packet+=get_char;
					}
					else{
						///StorageManager.error_log_add(context,"BluetoothService","Packet received:\n"+packet,null);
						handler.obtainMessage(Activity_Battle_Menu.HANDLER_READ,packet).sendToTarget();
						packet="";
					}
				}
				catch(IOException e){
					StorageManager.error_log_add(context,TAG,"Connection lost.",e);
					
					connection_lost();
					
					break;
				}
			}
		}
		
		public void write(byte[] buffer){
			try{
				///StorageManager.error_log_add(context,"BluetoothService","Writing data:\n"+new String(buffer),null);
				
				output.write(buffer);
			}
			catch(IOException e){
				StorageManager.error_log_add(context,TAG,"write() on "+socket_type+" connected socket failed.",e);
			}
		}
		
		public void cancel(){
			try{
				socket.close();
			}
			catch(IOException e){
				StorageManager.error_log_add(context,TAG,"close() on "+socket_type+" connected socket failed.",e);
			}
		}
	}
	
	private class BluetoothLifeThread extends Thread{
		private boolean running;
		
		//Are we done transmitting/receiving data?
		private boolean done;
		//Is the other player done transmitting/receiving data?
		private boolean they_done;
		
		public BluetoothLifeThread(){
			running=true;
			
			done=false;
			they_done=false;
		}
		
		public void run(){
			while(running){
				if(done && they_done){
					handler.obtainMessage(Activity_Battle_Menu.HANDLER_READY).sendToTarget();
					done=false;
					they_done=false;
				}
			}
		}
		
		public void set_done(){
			done=true;
		}
		
		public void set_they_done(){
			they_done=true;
		}
		
		public void cancel(){
			running=false;
		}
	}
}
