package org.cheeseandbacon.bitbeast;

public class Food{
	String name;
	String plural_name;
	String prefix_article;
	int cost;
	short hunger;
	double weight;
	short strength;
	short dexterity;
	short stamina;
	short energy;
	short happy;
	boolean is_water;
	boolean is_medicine;
	int resource_id;
	boolean can_be_favorite;
	int category;
	int store_section;
	//The primary stat that this item affects.
	String primary_effect;
	String perma_item;
	String buff;
	
	public Food(){
		name="";
		plural_name="";
		prefix_article="";
		cost=0;
		hunger=0;
		weight=0.0;
		strength=0;
		dexterity=0;
		stamina=0;
		energy=0;
		happy=0;
		is_water=false;
		is_medicine=false;
		resource_id=0;
		can_be_favorite=true;
		category=Food_Category.NONE;
		store_section=Store_Section.FOOD;
		primary_effect="none";
		perma_item="";
		buff="none";
	}
	
	@Override
	public String toString(){
		String item_name=Strings.first_letter_capital(name);
		
		String item_cost=""+cost;
		if(cost==0){
			item_cost="free";
		}
		
		return item_name+" - "+item_cost;
	}
}
