/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

public class Coords{
	float x;
	float y;
	
	public Coords(){
		x=0.0f;
		y=0.0f;
	}
	
	public Coords(float get_x,float get_y){
		x=get_x;
		y=get_y;
	}
	
	public void set(float get_x,float get_y){
		x=get_x;
		y=get_y;
	}
}
