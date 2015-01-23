package org.cheeseandbacon.bitbeast;

import android.os.SystemClock;

public class Timer_Cheesy{
    //The clock time when the timer started.
	private long start_ticks;

    //The ticks stored when the timer was paused.
	private long paused_ticks;

    //Timer status.
	private boolean paused;
	private boolean started;

    public Timer_Cheesy(){
        start_ticks=0;
        paused_ticks=0;
        paused=false;
        started=false;
    }

    //The various clock actions.
    public void start(){
    	//Start the timer.
        started=true;

        //Unpause the timer.
        paused=false;

        //Get the current clock time.
        start_ticks=SystemClock.uptimeMillis();
    }
    
    public void stop(){
    	//Stop the timer.
        started=false;

        //Unpause the timer.
        paused=false;
    }
    
    public void pause(){
    	//If the timer is running and isn't already paused.
        if(started && !paused){
            //Pause the timer.
            paused=true;

            //Calculate the paused ticks.
            paused_ticks=Math.abs(SystemClock.uptimeMillis()-start_ticks);
        }
    }
    
    public void unpause(){
    	//If the timer is paused.
        if(paused){
            //Unpause the timer.
            paused=false;

            //Reset the starting ticks.
            start_ticks=Math.abs(SystemClock.uptimeMillis()-paused_ticks);

            //Reset the paused ticks.
            paused_ticks=0;
        }
    }

    //Get the timer's time.
    public long get_ticks(){
    	//If the timer is running.
        if(started){
            //If the timer is paused.
            if(paused){
                //Return the number of ticks when the timer was paused.
                return paused_ticks;
            }
            else{
                //Return the current time minus the start time.
                return Math.abs(SystemClock.uptimeMillis()-start_ticks);
            }
        }
        //If the timer isn't running.
        return 0;
    }

    //Checks the status of the timer.
    public boolean is_started(){
    	return started;
    }
    
    public boolean is_paused(){
    	return paused;
    }
}
