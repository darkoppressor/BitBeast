package org.cheeseandbacon.bitbeast;

public class Round{
	public static double nearest_multiple(double multiple,double input){
		input=Math.floor((input+(multiple/2.0))/multiple)*multiple;
		
		return input;
	}
}
