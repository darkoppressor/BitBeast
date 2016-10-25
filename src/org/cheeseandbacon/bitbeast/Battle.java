package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;

import android.content.Context;

public class Battle{
	private static final int INITIATIVE_MIN=-99;
	private static final int INITIATIVE_MAX=999;
	private static final int INITIATIVE_PENALTY_SICK=200;
	private static final int INITIATIVE_PENALTY_HUNGER=100;
	private static final int INITIATIVE_PENALTY_THIRST=100;
	private static final int INITIATIVE_BONUS_HUNGER=100;
	private static final int INITIATIVE_BONUS_THIRST=100;
	private static final int HIT_MIN=75;
	private static final int HIT_MAX=100;
	private static final int HIT_PENALTY_SICK=10;
	private static final int HIT_PENALTY_HUNGER=5;
	private static final int HIT_PENALTY_THIRST=5;
	private static final int HIT_PENALTY_WEIGHT_MAX=15;
	private static final int HIT_BONUS_HUNGER=5;
	private static final int HIT_BONUS_THIRST=5;
	private static final short DAMAGE_PENALTY_SICK=12;
	private static final short DAMAGE_PENALTY_HUNGER=6;
	private static final short DAMAGE_PENALTY_THIRST=6;
	private static final short DAMAGE_BONUS_HUNGER=6;
	private static final short DAMAGE_BONUS_THIRST=6;
	private static final short DAMAGE_RANDOM=3;
	
	private static int initiative_bound(int initiative){
		if(initiative<INITIATIVE_MIN){
			initiative=INITIATIVE_MIN;
		}
		else if(initiative>INITIATIVE_MAX){
			initiative=INITIATIVE_MAX;
		}
		
		return initiative;
	}
	
	private static int hit_chance_bound(int hit){
		if(hit<HIT_MIN){
			hit=HIT_MIN;
		}
		else if(hit>HIT_MAX){
			hit=HIT_MAX;
		}
		
		return hit;
	}
	
	private static int get_initiative(Pet_Status pet_status){
		int initiative=RNG.random_range(RNG.BATTLE,0,INITIATIVE_MAX);
		
		if(pet_status.is_obese()){
			initiative-=(int)Math.ceil((pet_status.get_weight()*4.0));
			initiative=initiative_bound(initiative);
		}
		else{
			initiative-=(int)pet_status.get_weight();
			initiative=initiative_bound(initiative);
		}
		
		if(pet_status.sick){
			initiative-=INITIATIVE_PENALTY_SICK;
			initiative=initiative_bound(initiative);
		}
		
		if(pet_status.is_starving()){
			initiative-=INITIATIVE_PENALTY_HUNGER;
			initiative=initiative_bound(initiative);
		}
		else if(pet_status.is_well_fed()){
			initiative+=INITIATIVE_BONUS_HUNGER;
			initiative=initiative_bound(initiative);
		}
		
		if(pet_status.is_very_thirsty()){
			initiative-=INITIATIVE_PENALTY_THIRST;
			initiative=initiative_bound(initiative);
		}
		else if(pet_status.is_well_watered()){
			initiative+=INITIATIVE_BONUS_THIRST;
			initiative=initiative_bound(initiative);
		}
		
		double energy_percentage=(double)pet_status.get_energy()/(double)pet_status.get_energy_max();
		if(energy_percentage<0.75){
			energy_percentage=0.75;
		}
		initiative=(int)Math.ceil(((double)initiative*energy_percentage));
		
		return initiative;
	}
	
	private static boolean get_hit(Pet_Status pet_status){
		int hit_chance=RNG.random_range(RNG.BATTLE,HIT_MIN,HIT_MAX);
		
		double weight_divisor=10.0;
		if(pet_status.is_obese()){
			weight_divisor=6.0;
		}
		
		int weight_penalty=(int)Math.ceil((pet_status.get_weight()/weight_divisor));
		if(weight_penalty>HIT_PENALTY_WEIGHT_MAX){
			weight_penalty=HIT_PENALTY_WEIGHT_MAX;
		}
		hit_chance-=weight_penalty;
		hit_chance=hit_chance_bound(hit_chance);
		
		if(pet_status.sick){
			hit_chance-=HIT_PENALTY_SICK;
			hit_chance=hit_chance_bound(hit_chance);
		}
		
		if(pet_status.is_starving()){
			hit_chance-=HIT_PENALTY_HUNGER;
			hit_chance=hit_chance_bound(hit_chance);
		}
		else if(pet_status.is_well_fed()){
			hit_chance+=HIT_BONUS_HUNGER;
			hit_chance=hit_chance_bound(hit_chance);
		}
		
		if(pet_status.is_very_thirsty()){
			hit_chance-=HIT_PENALTY_THIRST;
			hit_chance=hit_chance_bound(hit_chance);
		}
		else if(pet_status.is_well_watered()){
			hit_chance+=HIT_BONUS_THIRST;
			hit_chance=hit_chance_bound(hit_chance);
		}
		
		boolean hit=false;
		
		if(RNG.random_range(RNG.BATTLE,0,99)<hit_chance){
			hit=true;
		}
		
		return hit;
	}
	
	private static short get_damage(Pet_Status pet_status){
		//Begin with base strength damage.
		short damage=(short)Math.ceil((double)pet_status.get_strength()/4.0);
		
		//Add the dexterity damage, if any.
		if(pet_status.get_dexterity()>0){
			damage+=(short)Math.ceil((double)pet_status.get_dexterity()*0.75);
		}
		
		if(pet_status.sick){
			damage-=DAMAGE_PENALTY_SICK;
		}
		
		if(pet_status.is_starving()){
			damage-=DAMAGE_PENALTY_HUNGER;
		}
		else if(pet_status.is_well_fed()){
			damage+=DAMAGE_BONUS_HUNGER;
		}
		
		if(pet_status.is_very_thirsty()){
			damage-=DAMAGE_PENALTY_THIRST;
		}
		else if(pet_status.is_well_watered()){
			damage+=DAMAGE_BONUS_THIRST;
		}
		
		//Add a small random fluctuation in damage.
		if(RNG.random_range(RNG.BATTLE,0,99)<85){
			damage+=(short)RNG.random_range(RNG.BATTLE,0,DAMAGE_RANDOM);
		}
		else{
			damage-=(short)RNG.random_range(RNG.BATTLE,0,DAMAGE_RANDOM);
		}
		
		if(damage<1){
			damage=1;
		}
		
		return damage;
	}
	
	public static ArrayList<Battle_Turn> battle(Context context,Pet_Status us,Pet_Status them,boolean server,int our_seed,int their_seed,int bits_reward_them,int bit_level_diff_bonus_them,boolean shadow){
		int seed=0;
		
		ArrayList<Pet_Status> stats=new ArrayList<Pet_Status>();
		if(!server){
			stats.add(us);
			stats.add(them);
			seed=their_seed;
		}
		else{
			stats.add(them);
			stats.add(us);
			seed=our_seed;
		}
		
		//The first turn's damage is the initial attacker.
		//The second turn's damage is the winner.
		//A 0 damage represents a miss.
		ArrayList<Battle_Turn> hits=new ArrayList<Battle_Turn>();
		
		//Seed the random number generator.
		RNG.set_seed(RNG.BATTLE,seed);
		
		//Determine which pet attacks first.
		
		int attacker=-1;
		
		int initiative_0=get_initiative(stats.get(0));
		int initiative_1=get_initiative(stats.get(1));
		
		if(initiative_0>initiative_1){
			attacker=0;
		}
		else if(initiative_0<initiative_1){
			attacker=1;
		}
		else{
			attacker=RNG.random_range(RNG.BATTLE,0,1);
		}
		
		hits.add(new Battle_Turn((short)attacker,(short)0,(short)0,(short)0,(short)0,(short)0,(short)0));
		
		//While both pets have health left.
		while(stats.get(0).get_strength()>0 && stats.get(1).get_strength()>0){
			int defender=-1;
			
			if(attacker==0){
				defender=1;
			}
			else if(attacker==1){
				defender=0;
			}
			
			//Determine whether this hit lands.
			boolean hit=get_hit(stats.get(attacker));
			
			if(hit){
				//Determine the attack's damage.
				short damage=get_damage(stats.get(attacker));
				
				//Subtract the attacker's used dexterity, if any.
				if(stats.get(attacker).get_dexterity()>0){
					stats.get(attacker).dexterity-=(short)Math.ceil((double)stats.get(attacker).get_dexterity()/4.0);
					
					stats.get(attacker).dexterity_bound();
				}
				
				short stamina=(short)Math.ceil((double)stats.get(defender).get_stamina()*1.25);
				short stamina_lost=0;
				
				//If the defender has enough stamina, just subtract the damage from it.
				if(stamina>=damage){
					stamina_lost=damage;
				}
				else{
					short remaining_damage=(short)(damage-stamina);
					
					stamina_lost=stamina;
					
					//The rest of the damage is subtracted from strength.
					stats.get(defender).strength-=remaining_damage;
				}
				
				stats.get(defender).stamina-=(short)Math.floor((double)stamina_lost*0.75);
				
				hits.add(new Battle_Turn(damage,stats.get(attacker).get_strength(),stats.get(attacker).get_dexterity(),stats.get(attacker).get_stamina(),
						stats.get(defender).get_strength(),stats.get(defender).get_dexterity(),stats.get(defender).get_stamina()));
			}
			else{
				hits.add(new Battle_Turn((short)0,stats.get(attacker).get_strength(),stats.get(attacker).get_dexterity(),stats.get(attacker).get_stamina(),
						stats.get(defender).get_strength(),stats.get(defender).get_dexterity(),stats.get(defender).get_stamina()));
			}
			
			if(attacker==0){
				attacker=1;
			}
			else if(attacker==1){
				attacker=0;
			}
		}
		
		int winner=-1;
		int loser=-1;
		if(stats.get(0).get_strength()>0){
			winner=0;
			loser=1;
		}
		else{
			winner=1;
			loser=0;
		}
		
		hits.add(1,new Battle_Turn((short)winner,(short)0,(short)0,(short)0,(short)0,(short)0,(short)0));
		
		int our_pet=-1;
		//int their_pet=-1;
		if(!server){
        	if(hits.get(1).damage==0){
        		our_pet=winner;
        		//their_pet=loser;
        	}
        	else{
        		our_pet=loser;
        		//their_pet=winner;
        	}
		}
		else{
			if(hits.get(1).damage==1){
				our_pet=winner;
        		//their_pet=loser;
        	}
			else{
        		our_pet=loser;
        		//their_pet=winner;
        	}
		}
		
		///
		/**String log="Us: "+stats.get(our_pet).name+"\n";
		log+="Level: "+stats.get(our_pet).level+"\n";
		log+="Strength: "+stats.get(our_pet).get_strength()+"/"+stats.get(our_pet).get_strength_max()+"\n";
		log+="Dexterity: "+stats.get(our_pet).get_dexterity()+"/"+stats.get(our_pet).get_dexterity_max()+"\n";
		log+="Stamina: "+stats.get(our_pet).get_stamina()+"/"+stats.get(our_pet).get_stamina_max()+"\n";
		
		log+="\nThem: "+stats.get(their_pet).name+"\n";
		log+="Level: "+stats.get(their_pet).level+"\n";
		log+="Strength: "+stats.get(their_pet).get_strength()+"/"+stats.get(their_pet).get_strength_max()+"\n";
		log+="Dexterity: "+stats.get(their_pet).get_dexterity()+"/"+stats.get(their_pet).get_dexterity_max()+"\n";
		log+="Stamina: "+stats.get(their_pet).get_stamina()+"/"+stats.get(their_pet).get_stamina_max()+"\n";
		StorageManager.log_add(context,"Battle",log);*/
		///
		
		stats.get(winner).strength+=(short)Math.ceil((double)stats.get(winner).get_strength_max()*stats.get(winner).get_strength_regen_rate());
		stats.get(winner).strength_bound();
		
		stats.get(winner).dexterity+=(short)Math.ceil((double)stats.get(winner).get_dexterity_max()*stats.get(winner).get_dexterity_regen_rate());
		stats.get(winner).dexterity_bound();
		
		stats.get(winner).stamina+=(short)Math.ceil((double)stats.get(winner).get_stamina_max()*stats.get(winner).get_stamina_regen_rate());
		stats.get(winner).stamina_bound();
		
		stats.get(loser).strength=0;
		stats.get(loser).strength+=(short)Math.ceil((double)stats.get(loser).get_strength_max()*(stats.get(loser).get_strength_regen_rate()/2.0f));
		stats.get(loser).strength_bound();
		
		stats.get(loser).dexterity+=(short)Math.ceil((double)stats.get(loser).get_dexterity_max()*(stats.get(loser).get_dexterity_regen_rate()/2.0f));
		stats.get(loser).dexterity_bound();
		
		stats.get(loser).stamina+=(short)Math.ceil((double)stats.get(loser).get_stamina_max()*(stats.get(loser).get_stamina_regen_rate()/2.0f));
		stats.get(loser).stamina_bound();
		
		stats.get(winner).energy-=Pet_Status.ENERGY_LOSS_BATTLE;
		stats.get(winner).energy_bound();
		
		stats.get(loser).energy-=Pet_Status.ENERGY_LOSS_BATTLE;
		stats.get(loser).energy_bound();
		
		if(!shadow){
			stats.get(winner).battles_won++;
		
			stats.get(loser).battles_lost++;
		}
		else{
			stats.get(winner).battles_won_sp++;
			
			stats.get(loser).battles_lost_sp++;
		}
		
		stats.get(winner).queue_thought(Thought_Type.HAPPY);
		stats.get(winner).happy+=Pet_Status.HAPPY_GAIN_BATTLE;
		stats.get(winner).happy_bound();
		
		stats.get(loser).queue_thought(Thought_Type.SAD);
		stats.get(loser).happy-=Pet_Status.HAPPY_LOSS_BATTLE;
		stats.get(loser).happy_bound();
		
		if(our_pet!=winner){
			stats.get(winner).bits+=bits_reward_them-bit_level_diff_bonus_them;
			stats.get(winner).bits_bound();
		}
		
		if(our_pet!=loser){
			stats.get(loser).bits+=(int)Math.ceil((double)bits_reward_them/2.0);
			stats.get(loser).bits_bound();
		}
		
		if(RNG.random_range(RNG.BATTLE,0,99)<Pet_Status.CHANCE_SMALL){
			stats.get(loser).death_counter--;
			stats.get(loser).death_counter_bound();
		}
		else{
			if(RNG.random_range(RNG.BATTLE,0,99)<Pet_Status.CHANCE_SMALL){
				stats.get(loser).death_counter++;
				stats.get(loser).death_counter_bound();
			}
		}
		
		if(RNG.random_range(RNG.BATTLE,0,99)<Pet_Status.CHANCE_SMALL){
			stats.get(winner).death_counter++;
			stats.get(winner).death_counter_bound();
		}
		
		stats.get(loser).gain_weight(-(0.01*RNG.random_range(RNG.BATTLE,Pet_Status.WEIGHT_LOSS_BATTLE_MIN,Pet_Status.WEIGHT_LOSS_BATTLE_MAX)));
		
		stats.get(winner).gain_weight(-(0.01*RNG.random_range(RNG.BATTLE,Pet_Status.WEIGHT_LOSS_BATTLE_MIN,Pet_Status.WEIGHT_LOSS_BATTLE_MAX)));
		
		return hits;
	}
}
