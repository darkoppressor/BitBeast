/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

public class Sound{
	static final int AC=0;
	static final int BATHE=1;
	static final int CLEAN=2;
	static final int DRINK=3;
	static final int EAT=4;
	static final int EVOLUTION_1=5;
	static final int EVOLUTION_2=6;
	static final int GAME_HIT_BRICK=7;
	static final int GAME_HIT_PADDLE=8;
	static final int GAME_HIT_WALL=9;
	static final int GAME_LOSS=10;
	static final int GAME_WIN=11;
	static final int HEATER=12;
	static final int MEDICINE=13;
	static final int POOP=14;
	static final int THOUGHT=15;
	static final int GAME_RESET_LOSS=16;
	static final int TOGGLE_LIGHT=17;
	static final int DIE=18;
	static final int SPEECH_START=19;
	static final int SPEECH_STOP=20;
	static final int BATTLE_HIT=21;
	static final int BATTLE_MISS=22;
	static final int PERMA_ITEM_GRAB=23;
	static final int PERMA_ITEM_DROP=24;
	static final int LEVEL_UP=25;
	static final int SPEND_STAT_POINT=26;
	static final int NO_STAT_POINTS=27;
	static final int GAME_DRAW=28;
	static final int GAME_RESET_WIN=29;
	static final int POWERUP_GET=30;
	static final int POWERUP_SPAWN=31;
	static final int ITEM_SOLD=32;
	static final int EQUIPPED=33;
	static final int UNEQUIPPED=34;
	static final int ITEM_SOLD_ALL=35;
	static final int END=36;

	private int soundPoolId;

    Sound (int soundPoolId) {
	    this.soundPoolId = soundPoolId;
    }

    public int getSoundPoolId() {
        return soundPoolId;
    }

    public void setSoundPoolId(int soundPoolId) {
        this.soundPoolId = soundPoolId;
    }

    static int getResourceId (int soundId) {
        switch (soundId) {
            case AC:
                return R.raw.ac;
            case BATHE:
                return R.raw.bathe;
            case CLEAN:
                return R.raw.clean;
            case DRINK:
                return R.raw.drink;
            case EAT:
                return R.raw.eat;
            case EVOLUTION_1:
                return R.raw.evolution_1;
            case EVOLUTION_2:
                return R.raw.evolution_2;
            case GAME_HIT_BRICK:
                return R.raw.game_hit_brick;
            case GAME_HIT_PADDLE:
                return R.raw.game_hit_paddle;
            case GAME_HIT_WALL:
                return R.raw.game_hit_wall;
            case GAME_LOSS:
                return R.raw.game_loss;
            case GAME_WIN:
                return R.raw.game_win;
            case HEATER:
                return R.raw.heater;
            case MEDICINE:
                return R.raw.medicine;
            case POOP:
                return R.raw.poop;
            case THOUGHT:
                return R.raw.thought;
            case GAME_RESET_LOSS:
                return R.raw.game_reset_loss;
            case TOGGLE_LIGHT:
                return R.raw.toggle_light;
            case DIE:
                return R.raw.die;
            case SPEECH_START:
                return R.raw.speech_start;
            case SPEECH_STOP:
                return R.raw.speech_stop;
            case BATTLE_HIT:
                return R.raw.battle_hit;
            case BATTLE_MISS:
                return R.raw.battle_miss;
            case PERMA_ITEM_GRAB:
                return R.raw.grab;
            case PERMA_ITEM_DROP:
                return R.raw.drop;
            case LEVEL_UP:
                return R.raw.level_up;
            case SPEND_STAT_POINT:
                return R.raw.spend_stat_point;
            case NO_STAT_POINTS:
                return R.raw.no_stat_points;
            case GAME_DRAW:
                return R.raw.game_draw;
            case GAME_RESET_WIN:
                return R.raw.game_reset_win;
            case POWERUP_GET:
                return R.raw.powerup_get;
            case POWERUP_SPAWN:
                return R.raw.powerup_spawn;
            case ITEM_SOLD:
                return R.raw.item_sold;
            case EQUIPPED:
                return R.raw.equipped;
            case UNEQUIPPED:
                return R.raw.unequipped;
            case ITEM_SOLD_ALL:
                return R.raw.item_sold_all;
            default:
                return 0;
        }
    }
}
