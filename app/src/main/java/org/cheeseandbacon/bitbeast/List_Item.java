/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

public class List_Item{
	String text;
	int resource_id;
	
	public List_Item(String get_text,int get_resource_id){
		text=get_text;
		resource_id=get_resource_id;
	}
	
	@Override
	public String toString(){
		return text;
	}
}
