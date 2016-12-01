package org.cheeseandbacon.bitbeast;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

public enum Pet_Type{
	//Egg
	YEETSOBIT,
	
	//Baby
	MATKABIT,
	
	//Child
	SHARBIT,
	
	//Teen
	GOLOBIT,
	VROONBIT,
	LYTSOBIT,
	POKRIBIT,
	VOSDUBIT,
	
	//Young Adult
	NAZBIT,
	MOSHENBIT,
	ROGBIT,
	KRISHABIT,
	LETABIT,
	
	//Adult
	KOROLBIT,
	OBMABIT,
	TRYBIT,
	PLYTABIT,
	KRILABIT,
	
	//Senior
	TYRANBIT,
	PATOBIT,
	SERABIT,
	DOSPEKYBIT,
	UJASBIT,
	
	//Dead
	DEAD;
	
	public static final short BRANCH_BEGIN=0;
	public static final short BRANCH_NONE=BRANCH_BEGIN;
	public static final short BRANCH_TYRANNO=1;
	public static final short BRANCH_APATO=2;
	public static final short BRANCH_TRICERA=3;
	public static final short BRANCH_STEGO=4;
	public static final short BRANCH_PTERO=5;
	public static final short BRANCH_END=6;
	
	public static final short STAT_BONUS_PRIMARY=128;
	public static final short STAT_BONUS_SECONDARY=48;
	public static final short STAT_BONUS_TERTIARY=8;
	
	public static Pet_Type get_evolutionary_branch(int category){
		if(category==Food_Category.FRUITS){
			return Pet_Type.LYTSOBIT;
		}
		else if(category==Food_Category.VEGETABLES){
			return Pet_Type.POKRIBIT;
		}
		else if(category==Food_Category.MEATS){
			return Pet_Type.GOLOBIT;
		}
		else if(category==Food_Category.BREADS){
			return Pet_Type.VROONBIT;
		}
		else if(category==Food_Category.DAIRY){
			return Pet_Type.VOSDUBIT;
		}
		else{
			return get_random_evolutionary_branch();
		}
	}
	
	public static Pet_Type get_random_evolutionary_branch(){
		int random=RNG.random_range(0,4);
		
		if(random==0){
			return Pet_Type.LYTSOBIT;
		}
		else if(random==1){
			return Pet_Type.POKRIBIT;
		}
		else if(random==2){
			return Pet_Type.GOLOBIT;
		}
		else if(random==3){
			return Pet_Type.VROONBIT;
		}
		else if(random==4){
			return Pet_Type.VOSDUBIT;
		}
		//This is only here because the compiler was freaking out.
		else{
			return Pet_Type.LYTSOBIT;
		}
	}
	
	public static short get_pet_branch(Pet_Type pet_type){
		switch(pet_type){
		default: case YEETSOBIT: case MATKABIT: case SHARBIT: case DEAD:
			return BRANCH_NONE;
			
		case GOLOBIT: case NAZBIT: case KOROLBIT: case TYRANBIT: 
			return BRANCH_TYRANNO;
			
		case VROONBIT: case MOSHENBIT: case OBMABIT: case PATOBIT:
			return BRANCH_APATO;
			
		case LYTSOBIT: case ROGBIT: case TRYBIT: case SERABIT:
			return BRANCH_TRICERA;
			
		case POKRIBIT: case KRISHABIT: case PLYTABIT: case DOSPEKYBIT:
			return BRANCH_STEGO;
			
		case VOSDUBIT: case LETABIT: case KRILABIT: case UJASBIT:
			return BRANCH_PTERO;
		}
	}
	
	public static short get_strength_bonus(Pet_Type pet_type){
		switch(get_pet_branch(pet_type)){
		default: case BRANCH_NONE:
			return 0;
		case BRANCH_TYRANNO:
			return STAT_BONUS_PRIMARY;
		case BRANCH_APATO:
			return STAT_BONUS_PRIMARY;
		case BRANCH_TRICERA:
			return STAT_BONUS_TERTIARY;
		case BRANCH_STEGO:
			return STAT_BONUS_SECONDARY;
		case BRANCH_PTERO:
			return STAT_BONUS_SECONDARY;
		}
	}
	
	public static short get_dexterity_bonus(Pet_Type pet_type){
		switch(get_pet_branch(pet_type)){
		default: case BRANCH_NONE:
			return 0;
		case BRANCH_TYRANNO:
			return STAT_BONUS_SECONDARY;
		case BRANCH_APATO:
			return STAT_BONUS_TERTIARY;
		case BRANCH_TRICERA:
			return STAT_BONUS_SECONDARY;
		case BRANCH_STEGO:
			return STAT_BONUS_TERTIARY;
		case BRANCH_PTERO:
			return STAT_BONUS_PRIMARY;
		}
	}
	
	public static short get_stamina_bonus(Pet_Type pet_type){
		switch(get_pet_branch(pet_type)){
		default: case BRANCH_NONE:
			return 0;
		case BRANCH_TYRANNO:
			return STAT_BONUS_TERTIARY;
		case BRANCH_APATO:
			return STAT_BONUS_SECONDARY;
		case BRANCH_TRICERA:
			return STAT_BONUS_PRIMARY;
		case BRANCH_STEGO:
			return STAT_BONUS_PRIMARY;
		case BRANCH_PTERO:
			return STAT_BONUS_TERTIARY;
		}
	}
	
	public Pet_Type get_next_type(Age_Tier age_tier){
		if(age_tier==Age_Tier.YOUNG_ADULT || age_tier==Age_Tier.ADULT || age_tier==Age_Tier.SENIOR){
			int increment=5;
			
			if(this.ordinal()+increment<=Pet_Type.values().length-1){
				return Pet_Type.values()[this.ordinal()+increment];
			}
			else{
				return this;
			}
		}
		else{
			return this;
		}
	}
	
	public static BitmapDrawable get_drawable(Resources res,Pet_Type type,boolean flipped,float scale_x,float scale_y){
		Bitmap bitmap;
		
		//Most sprites will have the same count.
		int sprite_count=3;
		
		switch(type){
		case YEETSOBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_egg_1);
			sprite_count=4;
			break;
		case MATKABIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_baby_1);
			break;
		case SHARBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_child_1);
			break;
			
		case GOLOBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_teen_1);
			break;
		case VROONBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_teen_2);
			break;
		case LYTSOBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_teen_3);
			break;
		case POKRIBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_teen_4);
			break;
		case VOSDUBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_teen_5);
			break;
			
		case NAZBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_1);
			break;
		case MOSHENBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_2);
			break;
		case ROGBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_3);
			break;
		case KRISHABIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_4);
			break;
		case LETABIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_young_adult_5);
			break;
			
		case KOROLBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_adult_1);
			break;
		case OBMABIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_adult_2);
			break;
		case TRYBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_adult_3);
			break;
		case PLYTABIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_adult_4);
			break;
		case KRILABIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_adult_5);
			break;
			
		case TYRANBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_senior_1);
			break;
		case PATOBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_senior_2);
			break;
		case SERABIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_senior_3);
			break;
		case DOSPEKYBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_senior_4);
			break;
		case UJASBIT:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_senior_5);
			break;
			
		case DEAD:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_dead_1);
			sprite_count=1;
			break;
			
		default:
			bitmap=BitmapFactory.decodeResource(res,R.drawable.pet_egg_1);
			sprite_count=4;
			break;
		}
		
		Bitmap bitmap_final=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth()/sprite_count,bitmap.getHeight());
		
		if(flipped){
			Matrix m=new Matrix();
			m.preScale(-1.0f,1.0f);
			
			bitmap_final=Bitmap.createBitmap(bitmap_final,0,0,bitmap_final.getWidth(),bitmap_final.getHeight(),m,false);
		}
		
		if(scale_x!=1.0f || scale_y!=1.0f){
			Matrix m=new Matrix();
			m.preScale(scale_x,scale_y);
			
			bitmap_final=Bitmap.createBitmap(bitmap_final,0,0,bitmap_final.getWidth(),bitmap_final.getHeight(),m,false);
		}
		
		return new BitmapDrawable(res,bitmap_final);
	}
}
