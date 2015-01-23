package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;

public class Sort{
	public static ArrayList<Integer> quick_sort(ArrayList<Integer> list){
		if(list.size()<=1){
			return list;
		}
		
		ArrayList<Integer> less=new ArrayList<Integer>();
		ArrayList<Integer> greater=new ArrayList<Integer>();
		ArrayList<Integer> result=new ArrayList<Integer>();
		
		int pivot=(int)Math.floor((float)list.size()-1/2.0f);
		
		for(int i=0;i<list.size();i++){
			if(i!=pivot && list.get(i).compareTo(list.get(pivot))<=0){
				less.add(list.get(i));
			}
			else{
				greater.add(list.get(i));
			}
		}
		
		result.addAll(quick_sort(less));
		result.add(list.get(pivot));
		result.addAll(quick_sort(greater));
		
		return result;
	}
	
	public static int ties_at_end(ArrayList<Integer> list){
		if(list.size()<=1){
			return 0;
		}
		
		//The amount being checked for ties.
		int amount=list.get(list.size()-1);

		//The number of ties.
		int ties=0;
		
		for(int i=list.size()-2;i>=0;i--){
			if(list.get(i)==amount){
				ties++;
			}
			else{
				break;
			}
		}
		
		return ties;
	}
}
