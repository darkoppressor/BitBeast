/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;


//This class represents a snapshot of the end of a particular turn of battle.
//It also has the damage done that turn for the purpose of being displayed.
public class Battle_Turn{
	short damage;
	
	short strength_attacker;
	short strength_defender;
	short dexterity_attacker;
	short dexterity_defender;
	short stamina_attacker;
	short stamina_defender;
	
	public Battle_Turn(short get_damage,short get_strength_attacker,short get_dexterity_attacker,short get_stamina_attacker,
			short get_strength_defender,short get_dexterity_defender,short get_stamina_defender){
		damage=get_damage;
		
		strength_attacker=get_strength_attacker;
		strength_defender=get_strength_defender;
		dexterity_attacker=get_dexterity_attacker;
		dexterity_defender=get_dexterity_defender;
		stamina_attacker=get_stamina_attacker;
		stamina_defender=get_stamina_defender;
	}
}
