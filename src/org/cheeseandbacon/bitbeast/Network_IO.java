package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;

public class Network_IO{
	public static void send_done(BluetoothService bluetooth_service){
		if(bluetooth_service.get_state()!=BluetoothService.STATE_CONNECTED){
			return;
		}
		
		String data="";
		
		//First, add the packet id.
		data+=""+Packet_ID.DONE+"\n";
		
		//Send the end of text character.
		data+="\u0003";
		
		if(data.length()>0){
			bluetooth_service.write(data.getBytes());
		}
	}
	
	public static int send_battle_data(BluetoothService bluetooth_service,Pet_Status pet_status){
		int seed=RNG.random_range(0,Integer.MAX_VALUE);
		
		if(bluetooth_service.get_state()!=BluetoothService.STATE_CONNECTED){
			return seed;
		}
		
		String data="";
		
		//First, add the packet id.
		data+=""+Packet_ID.BATTLE_DATA+"\n";
		
		data+=""+seed+"\n";
		
		data+=""+pet_status.type.toString()+"\n";
		
		data+=""+pet_status.age_tier.toString()+"\n";
		
		data+=""+pet_status.name+"\n";
		
		data+=""+pet_status.sick+"\n";
		
		data+=""+pet_status.weight+"\n";
		
		data+=""+pet_status.hunger+"\n";
		
		data+=""+pet_status.thirst+"\n";
		
		data+=""+pet_status.strength+"\n";
		
		data+=""+pet_status.strength_max+"\n";
		
		data+=""+pet_status.dexterity+"\n";
		
		data+=""+pet_status.dexterity_max+"\n";
		
		data+=""+pet_status.stamina+"\n";
		
		data+=""+pet_status.stamina_max+"\n";
		
		data+=""+pet_status.energy+"\n";
		
		data+=""+pet_status.color+"\n";
		
		data+=""+pet_status.battles_won+"\n";
		
		data+=""+pet_status.battles_lost+"\n";
		
		data+=""+pet_status.battles_won_sp+"\n";
		
		data+=""+pet_status.battles_lost_sp+"\n";
		
		data+=""+pet_status.level+"\n";
		
		data+=""+pet_status.buff_hunger+"\n";
		data+=""+pet_status.buff_thirst+"\n";
		data+=""+pet_status.buff_poop+"\n";
		data+=""+pet_status.buff_dirty+"\n";
		data+=""+pet_status.buff_weight+"\n";
		data+=""+pet_status.buff_sick+"\n";
		data+=""+pet_status.buff_happy+"\n";
		data+=""+pet_status.buff_energy_regen+"\n";
		data+=""+pet_status.buff_strength_regen+"\n";
		data+=""+pet_status.buff_dexterity_regen+"\n";
		data+=""+pet_status.buff_stamina_regen+"\n";
		data+=""+pet_status.buff_energy_max+"\n";
		data+=""+pet_status.buff_strength_max+"\n";
		data+=""+pet_status.buff_dexterity_max+"\n";
		data+=""+pet_status.buff_stamina_max+"\n";
		data+=""+pet_status.buff_death+"\n";
		data+=""+pet_status.buff_magic_find+"\n";
		
		data+=""+pet_status.perma_items.size()+"\n";
		for(int i=0;i<pet_status.perma_items.size();i++){
			data+=""+pet_status.perma_items.get(i).name+"\n";
		}
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			if(pet_status.equipment_slots.get(i)==null){
				data+=""+false+"\n";
			}
			else{
				data+=""+true+"\n";
				
				data+=""+pet_status.equipment_slots.get(i).name+"\n";
				data+=""+pet_status.equipment_slots.get(i).full_name+"\n";
				data+=""+pet_status.equipment_slots.get(i).description+"\n";
				data+=""+pet_status.equipment_slots.get(i).level+"\n";
				data+=""+pet_status.equipment_slots.get(i).bits+"\n";
				data+=""+pet_status.equipment_slots.get(i).branch+"\n";
				data+=""+pet_status.equipment_slots.get(i).weight+"\n";
				
				data+=""+pet_status.equipment_slots.get(i).buff_hunger+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_thirst+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_poop+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_dirty+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_weight+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_sick+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_happy+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_energy_regen+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_strength_regen+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_dexterity_regen+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_stamina_regen+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_energy_max+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_strength_max+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_dexterity_max+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_stamina_max+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_death+"\n";
				data+=""+pet_status.equipment_slots.get(i).buff_magic_find+"\n";
			}
		}
		
		//Send the end of text character.
		data+="\u0003";
		
		if(data.length()>0){
			bluetooth_service.write(data.getBytes());
		}
		
		return seed;
	}
	
	public static Pet_Status read_battle_data(ArrayList<String> data){
		Pet_Status pet_status=new Pet_Status();
		
		pet_status.type=Pet_Type.valueOf(data.get(0).trim());
		data.remove(0);
		
		pet_status.age_tier=Age_Tier.valueOf(data.get(0).trim());
		data.remove(0);
		
		pet_status.name=data.get(0).trim();
		data.remove(0);
		
		pet_status.sick=Boolean.parseBoolean(data.get(0).trim());
		data.remove(0);
		
		pet_status.weight=Double.parseDouble(data.get(0).trim());
		data.remove(0);
		
		pet_status.hunger=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.thirst=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.strength=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.strength_max=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.dexterity=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.dexterity_max=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.stamina=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.stamina_max=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.energy=Short.parseShort(data.get(0).trim());
		data.remove(0);
		
		pet_status.color=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.battles_won=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.battles_lost=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.battles_won_sp=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.battles_lost_sp=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.level=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_hunger=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_thirst=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_poop=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_dirty=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_weight=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_sick=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_happy=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_energy_regen=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_strength_regen=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_dexterity_regen=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_stamina_regen=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_energy_max=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_strength_max=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_dexterity_max=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_stamina_max=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_death=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.buff_magic_find=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		pet_status.perma_items.clear();
		int perma_items_size=Integer.parseInt(data.get(0).trim());
		data.remove(0);
		
		for(int i=0;i<perma_items_size;i++){
			String name=data.get(0).trim();
			data.remove(0);
			
			pet_status.perma_items.add(new Perma_Item(null,null,name,0.0f,0.0f));
		}
		
		for(int i=Equipment.SLOT_BEGIN;i<Equipment.SLOT_END;i++){
			boolean slot_filled=Boolean.parseBoolean(data.get(0).trim());
			data.remove(0);
			
			if(slot_filled){
				pet_status.equipment_slots.set(i,new Equipment());
				
				pet_status.equipment_slots.get(i).name=data.get(0).trim();
				data.remove(0);
				
				pet_status.equipment_slots.get(i).full_name=data.get(0).trim();
				data.remove(0);
				
				pet_status.equipment_slots.get(i).description=data.get(0).trim();
				data.remove(0);
				
				pet_status.equipment_slots.get(i).level=Integer.parseInt(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).bits=Integer.parseInt(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).branch=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).weight=Double.parseDouble(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_hunger=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_thirst=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_poop=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_dirty=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_weight=Double.parseDouble(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_sick=Integer.parseInt(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_happy=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_energy_regen=Float.parseFloat(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_strength_regen=Float.parseFloat(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_dexterity_regen=Float.parseFloat(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_stamina_regen=Float.parseFloat(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_energy_max=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_strength_max=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_dexterity_max=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_stamina_max=Short.parseShort(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_death=Integer.parseInt(data.get(0).trim());
				data.remove(0);
				
				pet_status.equipment_slots.get(i).buff_magic_find=Integer.parseInt(data.get(0).trim());
				data.remove(0);
			}
			else{
				pet_status.equipment_slots.set(i,null);
			}
		}
		
		return pet_status;
	}
}
