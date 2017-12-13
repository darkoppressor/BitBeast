/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Locale;

public class Templates{
	private static Templates instance;
	
	static ArrayList<Food> food;
	static ArrayList<Brick_Layout> bricks;
	static ArrayList<Equipment_Template> equipment;
	
	//Lists of food sorted into their respective sections.
	static ArrayList<Food> food_food;
	static ArrayList<Food> food_drinks;
	static ArrayList<Food> food_treatments;
	static ArrayList<Food> food_perma;
	
	private Templates(){
	}
	
	public static synchronized void startup(){
		get_instance();
		
		init();
	}
	
	public static synchronized Templates get_instance(){
		if(instance==null){
			instance=new Templates();
		}
		
		return instance;
	}
	
	public static synchronized void init(){
		food=new ArrayList<Food>();
		bricks=new ArrayList<Brick_Layout>();
		equipment=new ArrayList<Equipment_Template>();
		
		food_food=new ArrayList<Food>();
		food_drinks=new ArrayList<Food>();
		food_treatments=new ArrayList<Food>();
		food_perma=new ArrayList<Food>();
	}
	
	public static synchronized void cleanup(){
		if(food!=null){
			food.clear();
			food=null;
		}
		if(bricks!=null){
			bricks.clear();
			bricks=null;
		}
		if(equipment!=null){
			equipment.clear();
			equipment=null;
		}
		
		if(food_food!=null){
			food_food.clear();
			food_food=null;
		}
		if(food_drinks!=null){
			food_drinks.clear();
			food_drinks=null;
		}
		if(food_treatments!=null){
			food_treatments.clear();
			food_treatments=null;
		}
		if(food_perma!=null){
			food_perma.clear();
			food_perma=null;
		}
		
		if(instance!=null){
			instance=null;
		}
	}
	
	public static synchronized ArrayList<Food> get_food_list(int section){
		if(section==Store_Section.FOOD){
			return food_food;
		}
		else if(section==Store_Section.DRINKS){
			return food_drinks;
		}
		else if(section==Store_Section.TREATMENTS){
			return food_treatments;
		}
		else if(section==Store_Section.PERMA){
			return food_perma;
		}
		else{
			return food_food;
		}
	}
	
	public static synchronized void create_food_lists(){
		for(int i=0;i<food.size();i++){
			if(food.get(i).store_section==Store_Section.FOOD){
				food_food.add(food.get(i));
			}
			else if(food.get(i).store_section==Store_Section.DRINKS){
				food_drinks.add(food.get(i));
			}
			else if(food.get(i).store_section==Store_Section.TREATMENTS){
				food_treatments.add(food.get(i));
			}
			else if(food.get(i).store_section==Store_Section.PERMA){
			}
			else{
				food_food.add(food.get(i));
			}
		}
	}
	
	public static synchronized void rebuild_perma_list(Pet_Status pet_status){
		food_perma.clear();
		
		for(int i=0;i<food.size();i++){
			if(food.get(i).store_section==Store_Section.PERMA && food.get(i).cost>0){
				if(!pet_status.has_perma_item(food.get(i).perma_item)){
					food_perma.add(food.get(i));
				}
				else if(pet_status.has_perma_item(food.get(i).perma_item)){
				}
			}
		}
	}
	
	public static synchronized void load_template_equipment(ArrayList<String> data,Context context,Resources res){
		equipment.add(new Equipment_Template());
		
	    boolean multi_line_comment=false;

	    //As long as we haven't reached the end of the file.
	    while(data.size()>0){
	        //The item data name strings used in the file.

	        String name="name:";
	        String plural_name="plural_name:";
	        String prefix_article="prefix_article:";
	        String resource_id="resource_id:";
	        String slot="slot:";
	        String unique="<UNIQUE>";
	        String full_name="full_name:";
	        String description="description:";
	        String level="level:";
	        String bits="bits:";
	        String branch="branch:";
	        String weight="weight:";
	        String buff_hunger="buff_hunger:";
	        String buff_thirst="buff_thirst:";
	        String buff_poop="buff_poop:";
	        String buff_dirty="buff_dirty:";
	        String buff_weight="buff_weight:";
	        String buff_sick="buff_sick:";
	        String buff_happy="buff_happy:";
	        String buff_energy_regen="buff_energy_regen:";
	        String buff_strength_regen="buff_strength_regen:";
	        String buff_dexterity_regen="buff_dexterity_regen:";
	        String buff_stamina_regen="buff_stamina_regen:";
	        String buff_energy_max="buff_energy_max:";
	        String buff_strength_max="buff_strength_max:";
	        String buff_dexterity_max="buff_dexterity_max:";
	        String buff_stamina_max="buff_stamina_max:";
	        String buff_death="buff_death:";
	        String buff_magic_find="buff_magic_find:";
	        
	        //Grab the next line of the file.
            String line=data.get(0).trim();
            data.remove(0);

            //If the line ends a multi-line comment.
            if(line.toLowerCase(Locale.US).contains("*/".toLowerCase(Locale.US))){
                multi_line_comment=false;
            }
            //If the line starts a multi-line comment.
            if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("/*".toLowerCase(Locale.US))){
                multi_line_comment=true;
            }
            //If the line is a comment.
            else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("//".toLowerCase(Locale.US))){
                //Ignore this line.
            }

	        //Load item data based on the line.

            //name
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(name.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(name.length(),line.length());

	        	equipment.get(equipment.size()-1).name=line;
	        }
            //plural name
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(plural_name.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(plural_name.length(),line.length());

	        	equipment.get(equipment.size()-1).plural_name=line;
	        }
            //prefix article
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(prefix_article.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(prefix_article.length(),line.length());

	        	equipment.get(equipment.size()-1).prefix_article=line;
	        }
            //resource id
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(resource_id.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(resource_id.length(),line.length());

	        	equipment.get(equipment.size()-1).resource_id=res.getIdentifier(context.getPackageName()+":drawable/"+line,null,null);
	        }
            //slot
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(slot.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(slot.length(),line.length());

	        	equipment.get(equipment.size()-1).slot=line;
	        }
            
            //unique
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(unique.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(unique.length(),line.length());

	        	equipment.get(equipment.size()-1).unique=true;
	        }
            
            //full_name
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(full_name.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(full_name.length(),line.length());

	        	equipment.get(equipment.size()-1).full_name=line;
	        }
            //description
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(description.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(description.length(),line.length());

	        	equipment.get(equipment.size()-1).description=line;
	        }
            //level
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(level.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(level.length(),line.length());

	        	equipment.get(equipment.size()-1).level=Integer.parseInt(line);
	        }
            //bits
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(bits.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(bits.length(),line.length());

	        	equipment.get(equipment.size()-1).bits=Integer.parseInt(line);
	        }
            //branch
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(branch.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(branch.length(),line.length());

	        	equipment.get(equipment.size()-1).branch=Short.parseShort(line);
	        }
            //weight
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(weight.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(weight.length(),line.length());

	        	equipment.get(equipment.size()-1).weight=Double.parseDouble(line);
	        }
            //buff_hunger
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_hunger.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_hunger.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_hunger=Short.parseShort(line);
	        }
            //buff_thirst
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_thirst.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_thirst.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_thirst=Short.parseShort(line);
	        }
            //buff_poop
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_poop.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_poop.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_poop=Short.parseShort(line);
	        }
            //buff_dirty
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_dirty.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_dirty.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_dirty=Short.parseShort(line);
	        }
            //buff_weight
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_weight.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_weight.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_weight=Double.parseDouble(line);
	        }
            //buff_sick
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_sick.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_sick.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_sick=Integer.parseInt(line);
	        }
            //buff_happy
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_happy.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_happy.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_happy=Short.parseShort(line);
	        }
            //buff_energy_regen
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_energy_regen.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_energy_regen.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_energy_regen=Float.parseFloat(line);
	        }
            //buff_strength_regen
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_strength_regen.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_strength_regen.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_strength_regen=Float.parseFloat(line);
	        }
            //buff_dexterity_regen
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_dexterity_regen.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_dexterity_regen.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_dexterity_regen=Float.parseFloat(line);
	        }
            //buff_stamina_regen
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_stamina_regen.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_stamina_regen.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_stamina_regen=Float.parseFloat(line);
	        }
            //buff_energy_max
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_energy_max.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_energy_max.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_energy_max=Short.parseShort(line);
	        }
            //buff_strength_max
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_strength_max.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_strength_max.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_strength_max=Short.parseShort(line);
	        }
            //buff_dexterity_max
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_dexterity_max.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_dexterity_max.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_dexterity_max=Short.parseShort(line);
	        }
            //buff_stamina_max
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_stamina_max.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_stamina_max.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_stamina_max=Short.parseShort(line);
	        }
            //buff_death
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_death.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_death.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_death=Integer.parseInt(line);
	        }
            //buff_magic_find
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff_magic_find.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff_magic_find.length(),line.length());

	        	equipment.get(equipment.size()-1).buff_magic_find=Integer.parseInt(line);
	        }

	        //If the line ends the equipment.
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).contains("</equipment>".toLowerCase(Locale.US))){
	            return;
	        }
	    }
	}
	
	public static synchronized void load_template_item(ArrayList<String> data,Context context,Resources res){
		food.add(new Food());
		
	    boolean multi_line_comment=false;

	    //As long as we haven't reached the end of the file.
	    while(data.size()>0){
	        //The item data name strings used in the file.

	        String name="name:";
	        String plural_name="plural name:";
	        String prefix_article="prefix article:";
	        String resource_id="resource id:";
	        String cost="cost:";
	        String hunger="hunger:";
	        String weight="weight:";
	        String strength="strength:";
	        String dexterity="dexterity:";
	        String stamina="stamina:";
	        String energy="energy:";
	        String happy="happy:";
	        String category="category:";
	        String store_section="store_section:";
	        String primary_effect="primary_effect:";
	        String perma_item="perma_item:";
	        String buff="buff:";
	        String is_water="<WATER>";
	        String is_medicine="<MEDICINE>";
	        String cannot_be_favorite="<CANNOT BE FAVORITE>";
	        
	        //Grab the next line of the file.
            String line=data.get(0).trim();
            data.remove(0);

            //If the line ends a multi-line comment.
            if(line.toLowerCase(Locale.US).contains("*/".toLowerCase(Locale.US))){
                multi_line_comment=false;
            }
            //If the line starts a multi-line comment.
            if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("/*".toLowerCase(Locale.US))){
                multi_line_comment=true;
            }
            //If the line is a comment.
            else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("//".toLowerCase(Locale.US))){
                //Ignore this line.
            }

	        //Load item data based on the line.

            //name
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(name.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(name.length(),line.length());

	        	food.get(food.size()-1).name=line;
	        }
            //plural name
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(plural_name.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(plural_name.length(),line.length());

	        	food.get(food.size()-1).plural_name=line;
	        }
            //prefix article
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(prefix_article.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(prefix_article.length(),line.length());

	        	food.get(food.size()-1).prefix_article=line;
	        }
            //resource id
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(resource_id.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(resource_id.length(),line.length());

	        	food.get(food.size()-1).resource_id=res.getIdentifier(context.getPackageName()+":drawable/"+line,null,null);
	        }
            //cost
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(cost.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(cost.length(),line.length());

	        	food.get(food.size()-1).cost=Integer.parseInt(line);
	        }
            //hunger
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(hunger.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(hunger.length(),line.length());

	        	food.get(food.size()-1).hunger=Short.parseShort(line);
	        }
            //weight
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(weight.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(weight.length(),line.length());

	        	food.get(food.size()-1).weight=Double.parseDouble(line);
	        }
            //strength
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(strength.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(strength.length(),line.length());

	        	food.get(food.size()-1).strength=Short.parseShort(line);
	        }
            //dexterity
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(dexterity.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(dexterity.length(),line.length());

	        	food.get(food.size()-1).dexterity=Short.parseShort(line);
	        }
            //stamina
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(stamina.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(stamina.length(),line.length());

	        	food.get(food.size()-1).stamina=Short.parseShort(line);
	        }
            //energy
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(energy.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(energy.length(),line.length());

	        	food.get(food.size()-1).energy=Short.parseShort(line);
	        }
            //happy
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(happy.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(happy.length(),line.length());

	        	food.get(food.size()-1).happy=Short.parseShort(line);
	        }
            //category
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(category.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(category.length(),line.length());

	        	food.get(food.size()-1).category=Food_Category.string_to_category(line);
	        }
            //store_section
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(store_section.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(store_section.length(),line.length());

	        	food.get(food.size()-1).store_section=Store_Section.string_to_section(line);
	        }
            //primary_effect
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(primary_effect.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(primary_effect.length(),line.length());

	        	food.get(food.size()-1).primary_effect=line;
	        }
            //perma_item
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(perma_item.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(perma_item.length(),line.length());

	        	food.get(food.size()-1).perma_item=line;
	        }
            //buff
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(buff.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(buff.length(),line.length());

	        	food.get(food.size()-1).buff=line;
	        }
            //is water
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(is_water.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(is_water.length(),line.length());

	        	food.get(food.size()-1).is_water=true;
	        }
            //is medicine
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(is_medicine.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(is_medicine.length(),line.length());

	        	food.get(food.size()-1).is_medicine=true;
	        }
            //cannot be favorite
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith(cannot_be_favorite.toLowerCase(Locale.US))){
	            //Clear the data name.
	        	line=line.substring(cannot_be_favorite.length(),line.length());

	        	food.get(food.size()-1).can_be_favorite=false;
	        }

	        //If the line ends the item.
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).contains("</item>".toLowerCase(Locale.US))){
	            return;
	        }
	    }
	}
	
	public static synchronized void load_template_bricks(ArrayList<String> data,int width,int height,Context context,Resources res){
		bricks.add(new Brick_Layout(width,height));
		int int_y=0;
		
	    boolean multi_line_comment=false;

	    //As long as we haven't reached the end of the file.
	    while(data.size()>0){
	        //Grab the next line of the file.
            String line=data.get(0);
            data.remove(0);

            //If the line ends a multi-line comment.
            if(line.toLowerCase(Locale.US).contains("*/".toLowerCase(Locale.US))){
                multi_line_comment=false;
            }
            //If the line starts a multi-line comment.
            if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("/*".toLowerCase(Locale.US))){
                multi_line_comment=true;
            }
            //If the line is a comment.
            else if(!multi_line_comment && line.toLowerCase(Locale.US).startsWith("//".toLowerCase(Locale.US))){
                //Ignore this line.
            }

	        //If the line ends the bricks layout.
	        else if(!multi_line_comment && line.toLowerCase(Locale.US).contains("</bricks>".toLowerCase(Locale.US))){
	            return;
	        }
            
            //Load bricks data based on the line.
            
	        else{
	        	for(int int_x=0;int_x<line.length();int_x++){
	        		if(line.charAt(int_x)=='x'){
	        			bricks.get(bricks.size()-1).bricks[int_x][int_y]=true;
	        		}
	        		else if(line.charAt(int_x)==' '){
	        			bricks.get(bricks.size()-1).bricks[int_x][int_y]=false;
	        		}
	        	}
	        	
	        	int_y++;
	        }
	    }
	}
}
