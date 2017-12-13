/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

public class Round{
	public static double nearest_multiple(double multiple,double input){
		input=Math.floor((input+(multiple/2.0))/multiple)*multiple;
		
		return input;
	}
}
