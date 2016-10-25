package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;

public class Records{
	ArrayList<Record_Pet> pets;
	
	public Records(){
		pets=new ArrayList<Record_Pet>();
	}
	
	public void add_pet(String name,int bits,long age,Pet_Type type,Age_Tier age_tier,double weight,short strength_max,short dexterity_max,short stamina_max,int battles_won,int battles_lost,int battles_won_sp,int battles_lost_sp,int level){
		pets.add(new Record_Pet());
		
		pets.get(pets.size()-1).name=name;
		pets.get(pets.size()-1).bits=bits;
		pets.get(pets.size()-1).age=age;
		pets.get(pets.size()-1).type=type;
		pets.get(pets.size()-1).age_tier=age_tier;
		pets.get(pets.size()-1).weight=weight;
		pets.get(pets.size()-1).strength_max=strength_max;
		pets.get(pets.size()-1).dexterity_max=dexterity_max;
		pets.get(pets.size()-1).stamina_max=stamina_max;
		pets.get(pets.size()-1).battles_won=battles_won;
		pets.get(pets.size()-1).battles_lost=battles_lost;
		pets.get(pets.size()-1).battles_won_sp=battles_won_sp;
		pets.get(pets.size()-1).battles_lost_sp=battles_lost_sp;
		pets.get(pets.size()-1).level=level;
	}
	
	public int get_number_of_pets(){
		return pets.size();
	}
	
	public int get_battles_won(){
		int battles_won=0;
		
		for(int i=0;i<pets.size();i++){
			battles_won+=pets.get(i).battles_won;
		}
		
		return battles_won;
	}
	
	public int get_battles_lost(){
		int battles_lost=0;
		
		for(int i=0;i<pets.size();i++){
			battles_lost+=pets.get(i).battles_lost;
		}
		
		return battles_lost;
	}
	
	public int get_battles_won_sp(){
		int battles_won_sp=0;
		
		for(int i=0;i<pets.size();i++){
			battles_won_sp+=pets.get(i).battles_won_sp;
		}
		
		return battles_won_sp;
	}
	
	public int get_battles_lost_sp(){
		int battles_lost_sp=0;
		
		for(int i=0;i<pets.size();i++){
			battles_lost_sp+=pets.get(i).battles_lost_sp;
		}
		
		return battles_lost_sp;
	}
}
