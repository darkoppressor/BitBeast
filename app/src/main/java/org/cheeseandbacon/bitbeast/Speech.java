package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;

public class Speech{
	//Speech commands:
	
	static final int BEGIN=0;
	static final int NONE=BEGIN;
	static final int HAPPY=1;
	static final int STATUS=2;
	static final int STORE=3;
	static final int CLEAN=4;
	static final int BATHE=5;
	static final int TEMPERATURE=6;
	static final int GAMES=7;
	static final int EQUIPMENT=8;
	static final int BATTLE_BLUETOOTH=9;
	static final int LIGHTS=10;
	static final int OPTIONS=11;
	static final int AC=12;
	static final int HEATER=13;
	static final int NO_TEMP=14;
	static final int BRICKS=15;
	static final int RPS=16;
	static final int ACCEL=17;
	static final int GPS=18;
	static final int SPEED_GPS=19;
	static final int SAD=20;
	static final int MUSIC=21;
	static final int NEEDS=22;
	static final int BATTLE_SHADOW=23;
	static final int STORE_FOOD=24;
	static final int STORE_DRINKS=25;
	static final int STORE_TREATMENTS=26;
	static final int STORE_PERMA=27;
	static final int TRAINING=28;
	static final int END=29;
	
	public static ArrayList<String> get_command_strings(int command,String pet_name){
		ArrayList<String> commands=new ArrayList<String>();
		
		switch(command){
		case HAPPY:
			commands.add("good");
			commands.add("love");
			commands.add("proud");
			commands.add("here");
			commands.add("boy");
			commands.add("girl");
			commands.add("beast");
			commands.add(pet_name);
			break;
		case STATUS:
			commands.add("status");
			break;
		case STORE:
			commands.add("store");
			commands.add("shop");
			commands.add("shopping");
			break;
		case CLEAN:
			commands.add("clean");
			commands.add("cleanup");
			break;
		case BATHE:
			commands.add("bathe");
			commands.add("bath");
			break;
		case TEMPERATURE:
			commands.add("temperature");
			commands.add("temp");
			break;
		case GAMES:
			commands.add("games");
			commands.add("game");
			commands.add("play");
			break;
		case EQUIPMENT:
			commands.add("equipment");
			commands.add("equip");
			commands.add("items");
			commands.add("inventory");
			break;
		case BATTLE_BLUETOOTH:
			commands.add("battle");
			commands.add("fight");
			break;
		case LIGHTS:
			commands.add("lights");
			commands.add("light");
			break;
		case OPTIONS:
			commands.add("options");
			break;
		case AC:
			commands.add("conditioner");
			commands.add("ac");
			commands.add("cool");
			break;
		case HEATER:
			commands.add("heater");
			commands.add("heat");
			break;
		case NO_TEMP:
			commands.add("neither");
			commands.add("none");
			break;
		case BRICKS:
			commands.add("bricks");
			break;
		case RPS:
			commands.add("rps");
			commands.add("rock");
			commands.add("paper");
			commands.add("scissors");
			break;
		case ACCEL:
			commands.add("shake");
			break;
		case GPS:
			commands.add("journey");
			commands.add("trip");
			commands.add("travel");
			break;
		case SPEED_GPS:
			commands.add("go");
			break;
		case SAD:
			commands.add("bad");
			commands.add("hate");
			commands.add("disappointed");
			break;
		case MUSIC:
			commands.add("sing");
			commands.add("song");
			commands.add("music");
			commands.add("tune");
			break;
		case NEEDS:
			commands.add("needs");
			commands.add("need");
			commands.add("how");
			commands.add("what");
			commands.add("feeling");
			break;
		case BATTLE_SHADOW:
			commands.add("shadowbox");
			commands.add("shadowboxing");
			commands.add("shadow");
			break;
		case STORE_FOOD:
			commands.add("food");
			commands.add("feed");
			commands.add("snack");
			commands.add("dinner");
			commands.add("supper");
			commands.add("lunch");
			commands.add("breakfast");
			break;
		case STORE_DRINKS:
			commands.add("water");
			commands.add("drink");
			commands.add("drinks");
			commands.add("thirst");
			commands.add("thirsty");
			break;
		case STORE_TREATMENTS:
			commands.add("remedies");
			commands.add("remedy");
			commands.add("medicine");
			commands.add("sick");
			commands.add("shot");
			commands.add("potion");
			break;
		case STORE_PERMA:
			commands.add("stuff");
			commands.add("decoration");
			commands.add("decorations");
			break;
		case TRAINING:
			commands.add("work");
			commands.add("workout");
			commands.add("train");
			commands.add("training");
		}
		
		return commands;
	}
	
	public static int process(String speech,String pet_name){
		ArrayList<String> words=new ArrayList<String>();
		
		String current_string="";
		for(int i=0;i<speech.length();i++){
			if(speech.charAt(i)!=' '){
				current_string+=speech.charAt(i);
			}
			else{
				words.add(current_string);
				current_string="";
			}
		}
		if(words.size()==0){
			words.add(current_string);
		}
		
		for(int i=0;i<words.size();i++){
			for(int command=BEGIN;command<END;command++){
				ArrayList<String> command_strings=get_command_strings(command,pet_name);
				
				for(int command_string=0;command_string<command_strings.size();command_string++){
					if(words.get(i).equalsIgnoreCase(command_strings.get(command_string))){
						return command;
					}
				}
			}
		}
		
		return NONE;
	}
}
