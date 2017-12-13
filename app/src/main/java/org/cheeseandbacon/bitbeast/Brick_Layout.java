/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

public class Brick_Layout{
	boolean[][] bricks;
	int width;
	int height;
	
	public Brick_Layout(int get_width,int get_height){
		width=get_width;
		height=get_height;
		
		bricks=new boolean[width][height];
	}
}
