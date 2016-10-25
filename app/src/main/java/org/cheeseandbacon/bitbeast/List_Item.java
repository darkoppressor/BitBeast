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
