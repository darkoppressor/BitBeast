package org.cheeseandbacon.bitbeast;

import java.util.Calendar;

public class Moon_Phase{
	public static final short BEGIN=0;
	public static final short NEW=BEGIN;
	public static final short WAXING_CRESCENT=1;
	public static final short FIRST_QUARTER=2;
	public static final short WAXING_GIBBOUS=3;
	public static final short FULL=4;
	public static final short WANING_GIBBOUS=5;
	public static final short THIRD_QUARTER=6;
	public static final short WANING_CRESCENT=7;
	public static final short END=8;
	
	public static short get(){
		Calendar now=Calendar.getInstance();
		
		int goldn=(now.get(Calendar.YEAR)%19)+1;
	    int epact=(11*goldn+18)%30;

	    if((epact==25 && goldn>11) || epact==24){
	        epact++;
	    }

	    short phase=(short)((((((now.get(Calendar.DAY_OF_YEAR)+epact)*6)+11)%177)/22)&7);

	    //If the phase is not valid for some reason.
	    if(!(phase>=BEGIN && phase<END)){
	        phase=FULL;
	    }

	    return phase;
	}
}
