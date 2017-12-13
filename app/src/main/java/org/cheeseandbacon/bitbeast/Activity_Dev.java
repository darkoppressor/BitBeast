/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Activity_Dev extends AppCompatActivity{
	private Pet_Status pet_status;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_dev));
    	System.gc();
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
    	
    	update();
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    }
    
    public void update(){
        final SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss zzz", Locale.US);
        final SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.US);

        ArrayList<String> data = new ArrayList<>();

        final long ms_since_last_tick=System.currentTimeMillis()-pet_status.last_tick;
        final long seconds_until_next_tick = GameView.SECONDS_PER_TICK-(long)Math.floor(ms_since_last_tick/1000);
        data.add("Time until next tick: " + seconds_until_next_tick + " seconds");
        data.add("\n");

        data.add("Type: " + pet_status.type.name());
        data.add("Age Tier: " + pet_status.age_tier.name());
        data.add("Name: " + pet_status.name);
        data.add("Bits: " + pet_status.bits);
        data.add("Age: " + pet_status.age + " seconds");
        data.add("Death counter: " + pet_status.death_counter + " ticks");
        data.add("\n");
        data.add("Wake time: " + simpleDateFormatTime.format(pet_status.wake_time.getTime()));
        data.add("Sleep time: " + simpleDateFormatTime.format(pet_status.sleep_time.getTime()));
        data.add("Should be sleeping: " + pet_status.sleeping);
        data.add("Awake during sleep time: " + pet_status.sleeping_woken_up);
        if (pet_status.sleeping_woken_up_time > -1L) {
            data.add("Last wake up during sleep: " + simpleDateFormatDate.format(new Date(pet_status.sleeping_woken_up_time)));
        } else {
            data.add("Last wake up during sleep: N/A");
        }
        data.add("Light on: " + pet_status.light);
        data.add("\n");
        data.add("Happiness: " + pet_status.happy);
        data.add("Base temperature: " + pet_status.base_temp);
        data.add("Temperature: " + pet_status.temp);
        data.add("Last base temperature update: " + simpleDateFormatDate.format(new Date(pet_status.last_base_temp)));
        data.add("AC: " + pet_status.ac);
        data.add("Heater: " + pet_status.heater);
        data.add("\n");
        data.add("Sick: " + pet_status.sick);
        data.add("Poop need: " + pet_status.poop);
        data.add("Bath need: " + pet_status.bath);
        data.add("Weight: " + pet_status.weight + " kg");
        data.add("Hunger: " + pet_status.hunger);
        data.add("Thirst: " + pet_status.thirst);
        data.add("\n");
        data.add("Strength: " + pet_status.strength + "/" + pet_status.strength_max);
        data.add("Dexterity: " + pet_status.dexterity + "/" + pet_status.dexterity_max);
        data.add("Stamina: " + pet_status.stamina + "/" + pet_status.stamina_max);
        data.add("Energy: " + pet_status.energy);
        data.add("\n");
        data.add("Color: " + pet_status.color);
        data.add("Favorite food: " + pet_status.favorite_food);
        data.add("\n");
        data.add("Hunger rise: " + pet_status.hunger_rise);
        data.add("Thirst rise: " + pet_status.thirst_rise);
        data.add("Strength rise: " + pet_status.strength_rise);
        data.add("Dexterity rise: " + pet_status.dexterity_rise);
        data.add("Stamina rise: " + pet_status.stamina_rise);
        data.add("Energy rise: " + pet_status.energy_rise);
        data.add("\n");
        data.add("Multiplayer battles won: " + pet_status.battles_won);
        data.add("Multiplayer battles lost: " + pet_status.battles_lost);
        data.add("Singleplayer battles won: " + pet_status.battles_won_sp);
        data.add("Singleplayer battles lost: " + pet_status.battles_lost_sp);
        data.add("\n");
        data.add("Stat points: " + pet_status.stat_points);
        data.add("Level: " + pet_status.level);
        data.add("Experience: " + pet_status.experience + "/" + pet_status.experience_max);
        data.add("Experience gained this tick: " + pet_status.experience_gain);
        data.add("\n");
        data.add("Hunger buff time remaining: " + pet_status.buff_hunger + " seconds");
        data.add("Thirst buff time remaining: " + pet_status.buff_thirst + " seconds");
        data.add("Poop buff time remaining: " + pet_status.buff_poop + " seconds");
        data.add("Dirty buff time remaining: " + pet_status.buff_dirty + " seconds");
        data.add("Weight buff time remaining: " + pet_status.buff_weight + " seconds");
        data.add("Sick buff time remaining: " + pet_status.buff_sick + " seconds");
        data.add("Happy buff time remaining: " + pet_status.buff_happy + " seconds");
        data.add("Energy regen buff time remaining: " + pet_status.buff_energy_regen + " seconds");
        data.add("Strength regen buff time remaining: " + pet_status.buff_strength_regen + " seconds");
        data.add("Dexterity regen buff time remaining: " + pet_status.buff_dexterity_regen + " seconds");
        data.add("Stamina regen buff time remaining: " + pet_status.buff_stamina_regen + " seconds");
        data.add("Energy max buff time remaining: " + pet_status.buff_energy_max + " seconds");
        data.add("Strength max buff time remaining: " + pet_status.buff_strength_max + " seconds");
        data.add("Dexterity max buff time remaining: " + pet_status.buff_dexterity_max + " seconds");
        data.add("Stamina max buff time remaining: " + pet_status.buff_stamina_max + " seconds");
        data.add("Death buff time remaining: " + pet_status.buff_death + " seconds");
        data.add("Magic find buff time remaining: " + pet_status.buff_magic_find + " seconds");
        data.add("\n");

        for(int i=0;i<Food_Category.END;i++){
            data.add("Amount of " + Food_Category.category_to_string(i) + " fed: " + pet_status.food_categories.get(i));
        }
        data.add("\n");

        String food_fed = "Foods fed:\n";
        for (String food : pet_status.food_fed) {
            food_fed += food + "\n";
        }
        data.add(food_fed);

        data.add("Poops: " + pet_status.poops.size());
        data.add("Pettings received this tick: " + pet_status.pettings);
        data.add("\n");

        String perma_items = "Permanent items:\n";
        for (Perma_Item item : pet_status.perma_items) {
            perma_items += item.name + "\n";
        }
        data.add(perma_items);

        String thoughts = "Held thoughts:\n";
        for (Integer type : pet_status.thoughts) {
            thoughts += Thought_Type.type_to_string(type) + "\n";
        }
        data.add(thoughts);

        if (pet_status.equipment_slots.get(Equipment.SLOT_WEAPON) != null) {
            data.add("Weapon equipment: " + pet_status.equipment_slots.get(Equipment.SLOT_WEAPON));
        } else {
            data.add("Weapon equipment: None");
        }

        if (pet_status.equipment_slots.get(Equipment.SLOT_HEAD) != null) {
            data.add("Head equipment: " + pet_status.equipment_slots.get(Equipment.SLOT_HEAD));
        } else {
            data.add("Head equipment: None");
        }

        if (pet_status.equipment_slots.get(Equipment.SLOT_CHEST) != null) {
            data.add("Chest equipment: " + pet_status.equipment_slots.get(Equipment.SLOT_CHEST));
        } else {
            data.add("Chest equipment: None");
        }

        if (pet_status.equipment_slots.get(Equipment.SLOT_FEET) != null) {
            data.add("Feet equipment: " + pet_status.equipment_slots.get(Equipment.SLOT_FEET));
        } else {
            data.add("Feet equipment: None");
        }

        data.add("\n");

        String items = "Other equipment:\n";
        for (Equipment equipment : pet_status.equipment) {
            items += equipment.full_name + "\n";
        }
        data.add(items);

        data.add("Well fed death counter changes allowed: " + pet_status.dc_well_fed);
        data.add("Well watered death counter changes allowed: " + pet_status.dc_well_watered);
        data.add("Not sick death counter changes allowed: " + pet_status.dc_not_sick);
        data.add("Not obese death counter changes allowed: " + pet_status.dc_not_obese);
        data.add("Starving death counter changes allowed: " + pet_status.dc_starving);
        data.add("Very thirsty death counter changes allowed: " + pet_status.dc_very_thirsty);
        data.add("Sick death counter changes allowed: " + pet_status.dc_sick);
        data.add("Obese death counter changes allowed: " + pet_status.dc_obese);

        String dataString = "";

        for (String line : data) {
            dataString += line + "\n";
        }

        TextView textView = findViewById(R.id.text_dev_data);
        textView.setText(dataString);
    }
}
