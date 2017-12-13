package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.content.res.Resources;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Equipment{
	static final short SLOT_BEGIN=0;
	static final short SLOT_WEAPON=SLOT_BEGIN;
	static final short SLOT_HEAD=1;
	static final short SLOT_CHEST=2;
	static final short SLOT_FEET=3;
	static final short SLOT_END=4;
	
	static final short STAT_GAIN_RANDOM_PRIMARY_MIN=2;
	static final short STAT_GAIN_RANDOM_PRIMARY_MAX=3;
	
	static final short STAT_GAIN_RANDOM_SECONDARY_MIN=1;
	static final short STAT_GAIN_RANDOM_SECONDARY_MAX=2;
	
	static final short STAT_GAIN_RANDOM_TERTIARY_MIN=0;
	static final short STAT_GAIN_RANDOM_TERTIARY_MAX=1;
	
	static final short STAT_GAIN_RANDOM_GENERAL_MIN=0;
	static final short STAT_GAIN_RANDOM_GENERAL_MAX=2;
	
	static final int BUFF_RANDOM_MAX=99;
	
	static final int WEIGHT_MIN=100;
	static final int WEIGHT_MAX=1000;
		
	static final double BUFF_HUNGER=0.85;
	static final double BUFF_THIRST=0.85;
	static final double BUFF_POOP=1.75;
	static final double BUFF_DIRTY=0.85;
	static final double BUFF_WEIGHT=0.1;
	static final double BUFF_SICK=0.25;
	static final double BUFF_HAPPY=4.0;
	static final double BUFF_ENERGY_REGEN=0.2;
	static final double BUFF_STRENGTH_REGEN=0.1;
	static final double BUFF_DEXTERITY_REGEN=0.1;
	static final double BUFF_STAMINA_REGEN=0.1;
	static final double BUFF_ENERGY_MAX=3.0;
	static final double BUFF_DEATH=0.85;
	static final double BUFF_MAGIC_FIND=2.0;
	
	String name;
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
	
	public Equipment(){
		name="";
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
	
	public static short string_to_slot(String input){
		if(input.equals("weapon")){
			return SLOT_WEAPON;
		}
		else if(input.equals("head")){
			return SLOT_HEAD;
		}
		else if(input.equals("chest")){
			return SLOT_CHEST;
		}
		else if(input.equals("feet")){
			return SLOT_FEET;
		}
		else{
			return SLOT_WEAPON;
		}
	}
	
	public static String slot_to_string(short input){
		if(input==SLOT_WEAPON){
			return "weapon";
		}
		else if(input==SLOT_HEAD){
			return "head";
		}
		else if(input==SLOT_CHEST){
			return "chest";
		}
		else if(input==SLOT_FEET){
			return "feet";
		}
		else{
			return "weapon";
		}
	}
	
	public String get_list_string(Context context){
		String item_name="<font color='"+String.format("#%06x",0xFFFFFF&get_rarity_color(context))+"'>";
		
		item_name+=full_name;
		
		item_name+="</font>";
		
		String item_cost=bits+" bits";
		if(bits==0){
			item_cost="worthless";
		}
		
		return item_name+" - "+item_cost;
	}
	
	public String get_slot_string(Context context){
		String item_name="<font color='"+String.format("#%06x",0xFFFFFF&get_rarity_color(context))+"'>";
		
		item_name+=full_name;
		
		item_name+="</font>";
		
		return item_name;
	}
	
	public int get_rarity_color(Context context){
		Resources res=context.getResources();
		ArrayList<String> buff_names=get_buff_names();
		
		if(get_template(context).unique){
			return res.getColor(R.color.rarity_unique);
		}
		else{
			if(buff_names.size()==0){
				if(branch==Pet_Type.BRANCH_NONE){
					return res.getColor(R.color.rarity_crap);
				}
				else{
					return res.getColor(R.color.rarity_common);
				}
			}
			else if(buff_names.size()==1){
				return res.getColor(R.color.rarity_uncommon);
			}
			else if(buff_names.size()==2){
				return res.getColor(R.color.rarity_rare);
			}
			else if(buff_names.size()==3){
				return res.getColor(R.color.rarity_rarer);
			}
			else if(buff_names.size()==4){
				return res.getColor(R.color.rarity_rarest);
			}
			else if(buff_names.size()==5){
				return res.getColor(R.color.rarity_epic);
			}
			else if(buff_names.size()==6){
				return res.getColor(R.color.rarity_epicer);
			}
			else if(buff_names.size()>=7){
				return res.getColor(R.color.rarity_legendary);
			}
			else{
				return res.getColor(R.color.rarity_crap);
			}
		}
	}
	
	public static String get_stat_string(Context context,String stat_string,short this_item,short equipped_item){
		String message="";
		Resources res=context.getResources();
		
		if(this_item>equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
		}
		else if(this_item<equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_downgrade))+"'>";
		}
		else if(this_item==equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font))+"'>";
		}
		
		message+=stat_string;
		
		if(this_item>equipped_item){
			message+=" [+"+(this_item-equipped_item)+"]";
		}
		else if(this_item<equipped_item){
			message+=" [-"+(equipped_item-this_item)+"]";
		}
		message+="</font>\n";
		
		return message;
	}
	
	public static String get_stat_string(Context context,String stat_string,int this_item,int equipped_item){
		String message="";
		Resources res=context.getResources();
		
		if(this_item>equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
		}
		else if(this_item<equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_downgrade))+"'>";
		}
		else if(this_item==equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font))+"'>";
		}
		
		message+=stat_string;
		
		if(this_item>equipped_item){
			message+=" [+"+(this_item-equipped_item)+"]";
		}
		else if(this_item<equipped_item){
			message+=" [-"+(equipped_item-this_item)+"]";
		}
		message+="</font>\n";
		
		return message;
	}
	
	public static String get_stat_string(Context context,String stat_string,float this_item,float equipped_item){
		String message="";
		Resources res=context.getResources();
		
		if(this_item>equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
		}
		else if(this_item<equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_downgrade))+"'>";
		}
		else if(this_item==equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font))+"'>";
		}
		
		message+=stat_string;
		
		if(this_item>equipped_item){
			message+=" [+"+(int)Math.ceil(this_item-equipped_item)+"]";
		}
		else if(this_item<equipped_item){
			message+=" [-"+(int)Math.ceil(equipped_item-this_item)+"]";
		}
		message+="</font>\n";
		
		return message;
	}
	
	public static String get_stat_string(Context context,String stat_string,double this_item,double equipped_item){
		String message="";
		Resources res=context.getResources();
		
		if(this_item>equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
		}
		else if(this_item<equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_downgrade))+"'>";
		}
		else if(this_item==equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font))+"'>";
		}
		
		message+=stat_string;
		
		if(this_item>equipped_item){
			message+=" [+"+(int)Math.ceil(this_item-equipped_item)+"]";
		}
		else if(this_item<equipped_item){
			message+=" [-"+(int)Math.ceil(equipped_item-this_item)+"]";
		}
		message+="</font>\n";
		
		return message;
	}
	
	public static String get_weight_string(Context context,String stat_string,double this_item,double equipped_item){
		String message="";
		Resources res=context.getResources();
		DecimalFormat df=new DecimalFormat("#.##");
		
		if(this_item>equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_downgrade))+"'>";
		}
		else if(this_item<equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
		}
		else if(this_item==equipped_item){
			message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font))+"'>";
		}
		
		message+=stat_string;
		
		if(this_item>equipped_item){
			message+=" [+"+UnitConverter.get_weight_string(this_item-equipped_item,df)+"]";
		}
		else if(this_item<equipped_item){
			message+=" [-"+UnitConverter.get_weight_string(equipped_item-this_item,df)+"]";
		}
		message+="</font>\n";
		
		return message;
	}
	
	public String get_details(Context context,ArrayList<Equipment> equipment_slots){
		Equipment_Template template=get_template(context);
		DecimalFormat df=new DecimalFormat("#.##");
		
		String temp="";
		Equipment equipped=equipment_slots.get(string_to_slot(get_template(context).slot));
		if(equipped==null){
			equipped=new Equipment();
		}
		
		ArrayList<String> buff_names=get_buff_names();
		ArrayList<String> equipped_buff_names=equipped.get_buff_names();
		
		String message="<font color='"+String.format("#%06x",0xFFFFFF&get_rarity_color(context))+"'>";
		message+=full_name+"\n";
		message+="</font>";
		message+=Strings.first_letter_capital(template.slot)+"\n";
		
		temp="+"+buff_strength_max+" "+Pet_Status.get_buff_name("strength_max");
		message+=get_stat_string(context,temp,buff_strength_max,equipped.buff_strength_max);
		
		temp="+"+buff_dexterity_max+" "+Pet_Status.get_buff_name("dexterity_max");
		message+=get_stat_string(context,temp,buff_dexterity_max,equipped.buff_dexterity_max);
		
		temp="+"+buff_stamina_max+" "+Pet_Status.get_buff_name("stamina_max");
		message+=get_stat_string(context,temp,buff_stamina_max,equipped.buff_stamina_max);
		
		boolean showed_hunger=false;
		boolean showed_thirst=false;
		boolean showed_poop=false;
		boolean showed_dirty=false;
		boolean showed_weight=false;
		boolean showed_sick=false;
		boolean showed_happy=false;
		boolean showed_energy_regen=false;
		boolean showed_strength_regen=false;
		boolean showed_dexterity_regen=false;
		boolean showed_stamina_regen=false;
		boolean showed_energy_max=false;
		boolean showed_death=false;
		boolean showed_magic_find=false;
		
		for(int i=0;i<buff_names.size();i++){
			if(buff_names.get(i).equals("hunger")){
				showed_hunger=true;
				temp="+"+buff_hunger+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_hunger,equipped.buff_hunger);
			}
			else if(buff_names.get(i).equals("thirst")){
				showed_thirst=true;
				temp="+"+buff_thirst+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_thirst,equipped.buff_thirst);
			}
			else if(buff_names.get(i).equals("poop")){
				showed_poop=true;
				temp="+"+buff_poop+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_poop,equipped.buff_poop);
			}
			else if(buff_names.get(i).equals("dirty")){
				showed_dirty=true;
				temp="+"+buff_dirty+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_dirty,equipped.buff_dirty);
			}
			else if(buff_names.get(i).equals("weight")){
				showed_weight=true;
				temp="+"+(int)Math.ceil((1.0-buff_weight)*100.0)+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,(1.0-buff_weight)*100.0,(1.0-equipped.buff_weight)*100.0);
			}
			else if(buff_names.get(i).equals("sick")){
				showed_sick=true;
				temp="+"+buff_sick+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_sick,equipped.buff_sick);
			}
			else if(buff_names.get(i).equals("happy")){
				showed_happy=true;
				temp="+"+buff_happy+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_happy,equipped.buff_happy);
			}
			else if(buff_names.get(i).equals("energy_regen")){
				showed_energy_regen=true;
				temp="+"+(int)Math.ceil(buff_energy_regen*100.0)+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_energy_regen*100.0,equipped.buff_energy_regen*100.0);
			}
			else if(buff_names.get(i).equals("strength_regen")){
				showed_strength_regen=true;
				temp="+"+(int)Math.ceil(buff_strength_regen*100.0)+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_strength_regen*100.0,equipped.buff_strength_regen*100.0);
			}
			else if(buff_names.get(i).equals("dexterity_regen")){
				showed_dexterity_regen=true;
				temp="+"+(int)Math.ceil(buff_dexterity_regen*100.0)+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_dexterity_regen*100.0,equipped.buff_dexterity_regen*100.0);
			}
			else if(buff_names.get(i).equals("stamina_regen")){
				showed_stamina_regen=true;
				temp="+"+(int)Math.ceil(buff_stamina_regen*100.0)+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_stamina_regen*100.0,equipped.buff_stamina_regen*100.0);
			}
			else if(buff_names.get(i).equals("energy_max")){
				showed_energy_max=true;
				temp="+"+buff_energy_max+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_energy_max,equipped.buff_energy_max);
			}
			else if(buff_names.get(i).equals("death")){
				showed_death=true;
				temp="+"+buff_death+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_death,equipped.buff_death);
			}
			else if(buff_names.get(i).equals("magic_find")){
				showed_magic_find=true;
				temp="+"+buff_magic_find+" "+Pet_Status.get_buff_name(buff_names.get(i));
				message+=get_stat_string(context,temp,buff_magic_find,equipped.buff_magic_find);
			}
		}
		
		for(int i=0;i<equipped_buff_names.size();i++){
			if(!showed_hunger && equipped_buff_names.get(i).equals("hunger")){
				temp="+"+buff_hunger+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_hunger,equipped.buff_hunger);
			}
			else if(!showed_thirst && equipped_buff_names.get(i).equals("thirst")){
				temp="+"+buff_thirst+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_thirst,equipped.buff_thirst);
			}
			else if(!showed_poop && equipped_buff_names.get(i).equals("poop")){
				temp="+"+buff_poop+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_poop,equipped.buff_poop);
			}
			else if(!showed_dirty && equipped_buff_names.get(i).equals("dirty")){
				temp="+"+buff_dirty+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_dirty,equipped.buff_dirty);
			}
			else if(!showed_weight && equipped_buff_names.get(i).equals("weight")){
				temp="+"+(int)Math.ceil((1.0-buff_weight)*100.0)+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,(1.0-buff_weight)*100.0,(1.0-equipped.buff_weight)*100.0);
			}
			else if(!showed_sick && equipped_buff_names.get(i).equals("sick")){
				temp="+"+buff_sick+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_sick,equipped.buff_sick);
			}
			else if(!showed_happy && equipped_buff_names.get(i).equals("happy")){
				temp="+"+buff_happy+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_happy,equipped.buff_happy);
			}
			else if(!showed_energy_regen && equipped_buff_names.get(i).equals("energy_regen")){
				temp="+"+(int)Math.ceil(buff_energy_regen*100.0)+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_energy_regen*100.0,equipped.buff_energy_regen*100.0);
			}
			else if(!showed_strength_regen && equipped_buff_names.get(i).equals("strength_regen")){
				temp="+"+(int)Math.ceil(buff_strength_regen*100.0)+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_strength_regen*100.0,equipped.buff_strength_regen*100.0);
			}
			else if(!showed_dexterity_regen && equipped_buff_names.get(i).equals("dexterity_regen")){
				temp="+"+(int)Math.ceil(buff_dexterity_regen*100.0)+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_dexterity_regen*100.0,equipped.buff_dexterity_regen*100.0);
			}
			else if(!showed_stamina_regen && equipped_buff_names.get(i).equals("stamina_regen")){
				temp="+"+(int)Math.ceil(buff_stamina_regen*100.0)+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_stamina_regen*100.0,equipped.buff_stamina_regen*100.0);
			}
			else if(!showed_energy_max && equipped_buff_names.get(i).equals("energy_max")){
				temp="+"+buff_energy_max+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_energy_max,equipped.buff_energy_max);
			}
			else if(!showed_death && equipped_buff_names.get(i).equals("death")){
				temp="+"+buff_death+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_death,equipped.buff_death);
			}
			else if(!showed_magic_find && equipped_buff_names.get(i).equals("magic_find")){
				temp="+"+buff_magic_find+" "+Pet_Status.get_buff_name(equipped_buff_names.get(i));
				message+=get_stat_string(context,temp,buff_magic_find,equipped.buff_magic_find);
			}
		}
		
		temp="Weight "+UnitConverter.get_weight_string(weight,df);
		message+=get_weight_string(context,temp,weight,equipped.weight);
		
		message+="Item Level "+level+"\n";
		if(description.length()>0){
			message+="\""+description+"\"\n";
		}
		message+="Value "+bits+" bits";
		
		return message;
	}
	
	public Equipment_Template get_template(Context context){
		for(int i=0;i<Templates.equipment.size();i++){
			if(Templates.equipment.get(i).name.equals(name)){
				return Templates.equipment.get(i);
			}
		}
		
		StorageManager.error_log_add(context,"Equipment","Error accessing equipment template '"+name+"'",null);
		
		return null;
	}
	
	public void strength_max_increase(){
		short increase=0;
		
		if(branch==Pet_Type.BRANCH_TYRANNO || branch==Pet_Type.BRANCH_APATO){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_PRIMARY_MIN,(int)STAT_GAIN_RANDOM_PRIMARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_STEGO || branch==Pet_Type.BRANCH_PTERO){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_SECONDARY_MIN,(int)STAT_GAIN_RANDOM_SECONDARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_TRICERA){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_TERTIARY_MIN,(int)STAT_GAIN_RANDOM_TERTIARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_NONE){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_GENERAL_MIN,(int)STAT_GAIN_RANDOM_GENERAL_MAX);
		}
		
		buff_strength_max+=increase;
	}
	
	public void dexterity_max_increase(){
		short increase=0;
		
		if(branch==Pet_Type.BRANCH_PTERO){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_PRIMARY_MIN,(int)STAT_GAIN_RANDOM_PRIMARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_TYRANNO || branch==Pet_Type.BRANCH_TRICERA){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_SECONDARY_MIN,(int)STAT_GAIN_RANDOM_SECONDARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_APATO || branch==Pet_Type.BRANCH_STEGO){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_TERTIARY_MIN,(int)STAT_GAIN_RANDOM_TERTIARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_NONE){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_GENERAL_MIN,(int)STAT_GAIN_RANDOM_GENERAL_MAX);
		}
		
		buff_dexterity_max+=increase;
	}
	
	public void stamina_max_increase(){
		short increase=0;
				
		if(branch==Pet_Type.BRANCH_STEGO || branch==Pet_Type.BRANCH_TRICERA){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_PRIMARY_MIN,(int)STAT_GAIN_RANDOM_PRIMARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_APATO){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_SECONDARY_MIN,(int)STAT_GAIN_RANDOM_SECONDARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_PTERO || branch==Pet_Type.BRANCH_TYRANNO){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_TERTIARY_MIN,(int)STAT_GAIN_RANDOM_TERTIARY_MAX);
		}
		else if(branch==Pet_Type.BRANCH_NONE){
			increase=(short)RNG.random_range((int)STAT_GAIN_RANDOM_GENERAL_MIN,(int)STAT_GAIN_RANDOM_GENERAL_MAX);
		}
		
		buff_stamina_max+=increase;
	}
	
	//Returns a list of buff names for buffs that this item has.
	public ArrayList<String> get_buff_names(){
		ArrayList<String> buff_names=new ArrayList<String>();
		
		if(buff_hunger>0){
			buff_names.add("hunger");
		}
		if(buff_thirst>0){
			buff_names.add("thirst");
		}
		if(buff_poop>0){
			buff_names.add("poop");
		}
		if(buff_dirty>0){
			buff_names.add("dirty");
		}
		if(buff_weight<1.0){
			buff_names.add("weight");
		}
		if(buff_sick>0){
			buff_names.add("sick");
		}
		if(buff_happy>0){
			buff_names.add("happy");
		}
		if(buff_energy_regen>0.0f){
			buff_names.add("energy_regen");
		}
		if(buff_strength_regen>0.0f){
			buff_names.add("strength_regen");
		}
		if(buff_dexterity_regen>0.0f){
			buff_names.add("dexterity_regen");
		}
		if(buff_stamina_regen>0.0f){
			buff_names.add("stamina_regen");
		}
		if(buff_energy_max>0){
			buff_names.add("energy_max");
		}
		if(buff_death>0){
			buff_names.add("death");
		}
		if(buff_magic_find>0){
			buff_names.add("magic_find");
		}
		
		return buff_names;
	}
	
	public int fuzzy_level(int magic_find){
		int fuzzy=RNG.random_range(level-4+(int)Math.ceil(0.2*(double)magic_find),level+4+(int)Math.ceil(0.2*(double)magic_find));
		
		if(fuzzy<1){
			fuzzy=1;
		}
		
		return fuzzy;
	}
	
	public void try_for_buff_hunger(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_hunger=(short)Math.floor(Equipment.BUFF_HUNGER*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_thirst(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_thirst=(short)Math.floor(Equipment.BUFF_THIRST*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_poop(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_poop=(short)Math.floor(Equipment.BUFF_POOP*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_dirty(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_dirty=(short)Math.floor(Equipment.BUFF_DIRTY*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_weight(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_weight=1.0-(Equipment.BUFF_WEIGHT*0.05*(double)fuzzy_level(magic_find));
			if(buff_weight<0.0001){
				buff_weight=0.0001;
			}
		}
	}
	
	public void try_for_buff_sick(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_sick=(int)Math.floor(Equipment.BUFF_SICK*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_happy(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_happy=(short)Math.floor(Equipment.BUFF_HAPPY*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_energy_regen(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_energy_regen=(float)(Equipment.BUFF_ENERGY_REGEN*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_strength_regen(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_strength_regen=(float)(Equipment.BUFF_STRENGTH_REGEN*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_dexterity_regen(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_dexterity_regen=(float)(Equipment.BUFF_DEXTERITY_REGEN*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_stamina_regen(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_stamina_regen=(float)(Equipment.BUFF_STAMINA_REGEN*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_energy_max(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_energy_max=(short)Math.ceil(Equipment.BUFF_ENERGY_MAX*0.05*(double)fuzzy_level(magic_find));
			buff_energy_max=(short)Round.nearest_multiple(4.0,(double)buff_energy_max);
		}
	}
	
	public void try_for_buff_death(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_death=(int)Math.floor(Equipment.BUFF_DEATH*0.05*(double)fuzzy_level(magic_find));
		}
	}
	
	public void try_for_buff_magic_find(int magic_find){
		if(RNG.random_range(0,BUFF_RANDOM_MAX)<magic_find){
			buff_magic_find=(int)Math.floor(Equipment.BUFF_MAGIC_FIND*0.03*(double)fuzzy_level(magic_find));
		}
	}
}
