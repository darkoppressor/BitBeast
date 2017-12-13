/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import java.text.DecimalFormat;

public class UnitConverter{
	public static double celsius_to_fahrenheit(double celsius){
		return (celsius*9.0)/5.0+32.0;
	}
	
	public static double celsius_to_kelvin(double celsius){
		return celsius+273.15;
	}
	
	public static float meters_to_feet(float meters){
		return meters*3.2808399f;
	}
	
	public static double kilograms_to_pounds(double kilograms){
		return kilograms*2.20462262;
	}
	
	public static String get_temperature_string(double temperature,DecimalFormat df){
		if(Options.temp_units==Temperature_Units.CELSIUS){
    		return df.format(temperature)+" \u00B0C";
    	}
		else if(Options.temp_units==Temperature_Units.FAHRENHEIT){
			return df.format(celsius_to_fahrenheit(temperature))+" \u00B0F";
		}
		else if(Options.temp_units==Temperature_Units.KELVIN){
			return df.format(celsius_to_kelvin(temperature))+" K";
		}
    	else{
    		return df.format(temperature)+" \u00B0C";
    	}
	}
	
	public static String get_distance_string(float distance,DecimalFormat df){
		if(Options.units==Units.SI){
    		return df.format(distance)+" m";
    	}
		else if(Options.units==Units.US){
			return df.format(meters_to_feet(distance))+" ft";
		}
    	else{
    		return df.format(distance)+" m";
    	}
	}
	
	public static String get_speed_string(float speed,DecimalFormat df){
		if(Options.units==Units.SI){
    		return df.format(speed)+" m/s";
    	}
		else if(Options.units==Units.US){
			return df.format(meters_to_feet(speed))+" ft/s";
		}
    	else{
    		return df.format(speed)+" m/s";
    	}
	}
	
	public static String get_jerk_string(float jerk,DecimalFormat df){
		if(Options.units==Units.SI){
    		return df.format(jerk)+" m/s\u00B3";
    	}
		else if(Options.units==Units.US){
			return df.format(meters_to_feet(jerk))+" ft/s\u00B3";
		}
    	else{
    		return df.format(jerk)+" m/s\u00B3";
    	}
	}
	
	public static String get_weight_string(double weight,DecimalFormat df){
		if(Options.units==Units.SI){
    		return df.format(weight)+" kg";
    	}
		else if(Options.units==Units.US){
			return df.format(kilograms_to_pounds(weight))+" lb";
		}
    	else{
    		return df.format(weight)+" kg";
    	}
	}
	
	//Returns true if the passed weight, once formatted, is 0.0.
	public static boolean is_weight_basically_zero(double weight,DecimalFormat df){
		if(Options.units==Units.SI){
    		return Double.parseDouble(df.format(weight))==0.0;
    	}
		else if(Options.units==Units.US){
			return Double.parseDouble(df.format(kilograms_to_pounds(weight)))==0.0;
		}
    	else{
    		return Double.parseDouble(df.format(weight))==0.0;
    	}
	}
}
