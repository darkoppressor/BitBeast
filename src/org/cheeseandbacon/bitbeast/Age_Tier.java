package org.cheeseandbacon.bitbeast;

public enum Age_Tier{
	EGG,
	BABY,
	CHILD,
	TEEN,
	YOUNG_ADULT,
	ADULT,
	SENIOR,
	DEAD;
	
	public Age_Tier get_next(){
		int increment=1;
		
		if(this.ordinal()+increment<=Age_Tier.values().length-1){
			return Age_Tier.values()[this.ordinal()+increment];
		}
		else{
			return this;
		}
	}
	
	public Age_Tier get_previous(){
		int increment=1;
		
		if(this.ordinal()-increment>=0){
			return Age_Tier.values()[this.ordinal()-increment];
		}
		else{
			return this;
		}
	}
	
	public static Pet_Type get_random_pet_type(Age_Tier age_tier){
		int random=0;
		
		switch(age_tier){
		case EGG:
			return Pet_Type.YEETSOBIT;
		case BABY:
			return Pet_Type.MATKABIT;
		case CHILD:
			return Pet_Type.SHARBIT;
		case TEEN:
			random=RNG.random_range(0,4);
			if(random==0){
				return Pet_Type.GOLOBIT;
			}
			else if(random==1){
				return Pet_Type.VROONBIT;
			}
			else if(random==2){
				return Pet_Type.LYTSOBIT;
			}
			else if(random==3){
				return Pet_Type.POKRIBIT;
			}
			else if(random==4){
				return Pet_Type.VOSDUBIT;
			}
		case YOUNG_ADULT:
			random=RNG.random_range(0,4);
			if(random==0){
				return Pet_Type.NAZBIT;
			}
			else if(random==1){
				return Pet_Type.MOSHENBIT;
			}
			else if(random==2){
				return Pet_Type.ROGBIT;
			}
			else if(random==3){
				return Pet_Type.KRISHABIT;
			}
			else if(random==4){
				return Pet_Type.LETABIT;
			}
		case ADULT:
			random=RNG.random_range(0,4);
			if(random==0){
				return Pet_Type.KOROLBIT;
			}
			else if(random==1){
				return Pet_Type.OBMABIT;
			}
			else if(random==2){
				return Pet_Type.TRYBIT;
			}
			else if(random==3){
				return Pet_Type.PLYTABIT;
			}
			else if(random==4){
				return Pet_Type.KRILABIT;
			}
		case SENIOR:
			random=RNG.random_range(0,4);
			if(random==0){
				return Pet_Type.TYRANBIT;
			}
			else if(random==1){
				return Pet_Type.PATOBIT;
			}
			else if(random==2){
				return Pet_Type.SERABIT;
			}
			else if(random==3){
				return Pet_Type.DOSPEKYBIT;
			}
			else if(random==4){
				return Pet_Type.UJASBIT;
			}
		case DEAD:
			return Pet_Type.DEAD;
		default:
			return Pet_Type.YEETSOBIT;
		}
	}
	
	public static int scale_bit_gain(Age_Tier age_tier,int bit_gain){
		return bit_gain+(int)Math.ceil(((float)bit_gain/6.0f*((float)age_tier.ordinal()*0.75f)));
	}
	
	public static short get_stat_bonus(Age_Tier age_tier){
		switch(age_tier){
		default: case EGG: case DEAD:
			return 0;
		case BABY:
			return 4;
		case CHILD:
			return 8;
		case TEEN:
			return 16;
		case YOUNG_ADULT:
			return 32;
		case ADULT:
			return 64;
		case SENIOR:
			return 128;
		}
	}
	
	public static short get_hunger_max(Age_Tier age_tier){
		switch(age_tier){
		case EGG:
			return 0;
		case BABY:
			return 10;
		case CHILD:
			return 80;
		case TEEN:
			return 120;
		case YOUNG_ADULT:
			return 140;
		case ADULT:
			return 160;
		case SENIOR:
			return 180;
		case DEAD:
			return 0;
		default:
			return 0;
		}
	}
	
	public static short get_energy_max(Age_Tier age_tier){
		switch(age_tier){
		case EGG:
			return 0;
		case BABY:
			return 8;
		case CHILD:
			return 16;
		case TEEN:
			return 24;
		case YOUNG_ADULT:
			return 32;
		case ADULT:
			return 40;
		case SENIOR:
			return 48;
		case DEAD:
			return 0;
		default:
			return 0;
		}
	}
	
	//Returns the number of seconds old a pet must be to advance to the next age tier.
	public static long get_next_age_point(Age_Tier age_tier){
		switch(age_tier){
		case EGG:
			return 300L;
		case BABY:
			return 11100L;
		case CHILD:
			return 270300L;
		case TEEN:
			return 788700L;
		case YOUNG_ADULT:
			return 1479900L;
		case ADULT:
			return 1998300L;
		case SENIOR:
			return 0L;
		case DEAD:
			return 0L;
		default:
			return 0L;
		}
	}
}
