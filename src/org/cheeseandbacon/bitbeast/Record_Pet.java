package org.cheeseandbacon.bitbeast;

public class Record_Pet{
	String name;
	int bits;
	long age;
	Pet_Type type;
	Age_Tier age_tier;
	double weight;
	short strength_max;
	short dexterity_max;
	short stamina_max;
	int battles_won;
	int battles_lost;
	int battles_won_sp;
	int battles_lost_sp;
	int level;
	
	public Record_Pet(){
		name="";
		bits=0;
		age=0L;
		type=Pet_Type.YEETSOBIT;
		age_tier=Age_Tier.EGG;
		weight=0.0;
		strength_max=0;
		dexterity_max=0;
		stamina_max=0;
		battles_won=0;
		battles_lost=0;
		battles_won_sp=0;
		battles_lost_sp=0;
		level=0;
	}
}
