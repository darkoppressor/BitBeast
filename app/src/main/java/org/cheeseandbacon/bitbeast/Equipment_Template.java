package org.cheeseandbacon.bitbeast;


public class Equipment_Template{
	String name;
	String plural_name;
	String prefix_article;
	int resource_id;
	String slot;
	
	boolean unique;
	
	String full_name;
	String description;
	int level;
	int bits;
	short branch;
	double weight;
	short buff_hunger;
	short buff_thirst;
	short buff_poop;
	short buff_dirty;
	double buff_weight;
	int buff_sick;
	short buff_happy;
	float buff_energy_regen;
	float buff_strength_regen;
	float buff_dexterity_regen;
	float buff_stamina_regen;
	short buff_energy_max;
	short buff_strength_max;
	short buff_dexterity_max;
	short buff_stamina_max;
	int buff_death;
	int buff_magic_find;
	
	public Equipment_Template(){
		name="";
		plural_name="";
		prefix_article="";
		resource_id=0;
		slot="";
		
		unique=false;
		
		full_name="";
		description="";
		level=1;
		bits=0;
		branch=Pet_Type.BRANCH_NONE;
		weight=0.0;
		buff_hunger=0;
		buff_thirst=0;
		buff_poop=0;
		buff_dirty=0;
		buff_weight=1.0;
		buff_sick=0;
		buff_happy=0;
		buff_energy_regen=0.0f;
		buff_strength_regen=0.0f;
		buff_dexterity_regen=0.0f;
		buff_stamina_regen=0.0f;
		buff_energy_max=0;
		buff_strength_max=0;
		buff_dexterity_max=0;
		buff_stamina_max=0;
		buff_death=0;
		buff_magic_find=0;
	}
}
