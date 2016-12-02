package org.cheeseandbacon.bitbeast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_Game_RPS extends AppCompatActivity {
	static final int ROCK=0;
	static final int PAPER=1;
	static final int SCISSORS=2;
	
	static final int WIN=0;
	static final int LOSE=1;
	static final int DRAW=2;
	
	static final int DIALOG_ID_GAME_DONE=0;
	
	private Pet_Status pet_status;
	
	AlertDialog dialog_game_done;
	
	private ArrayList<Integer> results;
	private short happy_this_session;
	private int bits_this_session;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_rps);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        results=new ArrayList<Integer>();
        happy_this_session=0;
        bits_this_session=0;
        
        showDialog(DIALOG_ID_GAME_DONE);
        dismissDialog(DIALOG_ID_GAME_DONE);
        
        Font.set_typeface((TextView)dialog_game_done.findViewById(R.id.dialog_rps_vs));
        Font.set_typeface((TextView)dialog_game_done.findViewById(R.id.dialog_rps_message));
        Font.set_typeface((Button)dialog_game_done.findViewById(R.id.button_dialog_game_rps_ok));
        
        Button b=null;
        
        b=(Button)findViewById(R.id.button_game_rps_rock);
        b.setNextFocusLeftId(R.id.button_game_rps_scissors);
        
        b=(Button)findViewById(R.id.button_game_rps_scissors);
        b.setNextFocusRightId(R.id.button_game_rps_rock);
        
        if(savedInstanceState!=null){
        	bits_this_session=savedInstanceState.getInt("bits_this_session");
        	happy_this_session=savedInstanceState.getShort("happy_this_session");
    		
    		int results_size=savedInstanceState.getInt("results_size");
    		for(int i=0;i<results_size;i++){
    			results.add(savedInstanceState.getInt("results_"+i));
    		}
        }
    }
	@Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putInt("bits_this_session",bits_this_session);
		savedInstanceState.putShort("happy_this_session",happy_this_session);
		
		savedInstanceState.putInt("results_size",results.size());
		for(int i=0;i<results.size();i++){
			savedInstanceState.putInt("results_"+i,results.get(i));
		}
    	
    	super.onSaveInstanceState(savedInstanceState);
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_game_rps));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	set_dialog_buttons();
    	
    	if(dialog_game_done.isShowing()){
        	dismissDialog(DIALOG_ID_GAME_DONE);
        }
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	pet_status=new Pet_Status();
    	StorageManager.load_pet_status(this,null,pet_status);
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	close_dialogs();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	if(isFinishing()){
			int sound=-1;
			String message_begin="";
			String message_end="";
			long experience_points=0;
			int bits=0;
			int item_chance=0;
			
			int wins=0;
			int draws=0;
			int losses=0;
			
			for(int i=0;i<results.size();i++){
				if(results.get(i)==WIN){
					wins++;
				}
				else if(results.get(i)==DRAW){
					draws++;
				}
				else if(results.get(i)==LOSE){
					losses++;
				}
			}
			
			pet_status.happy+=happy_this_session;
			pet_status.happy_bound();
			pet_status.queue_thought(Thought_Type.HAPPY);
			
			message_begin="Done Rock Paper Scissoring!\n\n";
			if(wins+losses+draws>0){
				message_begin+="Total: "+(wins+losses+draws)+"\n";
				message_begin+="Wins: "+wins+"\n";
				message_begin+="Draws: "+draws+"\n";
				message_begin+="Losses: "+losses;
			}
			
			int experience_factor=(wins*4)+(draws*2)+(losses);
			experience_points=(long)Math.ceil((double)experience_factor*Pet_Status.EXPERIENCE_SCALE_RPS);
			
			experience_points+=(long)Math.ceil((double)experience_factor*0.01*(double)Pet_Status.get_level_bonus_xp(pet_status.level,Pet_Status.EXPERIENCE_BONUS_RPS,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_LOW));
			
			bits=bits_this_session;
			
			item_chance=(int)Math.ceil((double)experience_factor*Pet_Status.ITEM_CHANCE_RPS);
			
			if(wins+losses+draws>0){
				if(wins>draws+losses){
					sound=Sound.GAME_WIN;
					message_end=pet_status.name+" looks very happy!";
				}
				else if(draws>wins+losses){
					sound=Sound.GAME_DRAW;
					message_end=pet_status.name+" looks happy... ish.";
				}
				else if(losses>wins+draws){
					sound=Sound.GAME_LOSS;
					message_end=pet_status.name+" has looked happier...";
				}
				else{
					sound=Sound.GAME_DRAW;
					message_end=pet_status.name+" looks happy... ish.";
				}
			}
									
			pet_status.sleeping_wake_up();
	    	
			if(experience_points>0 || bits_this_session>0 || message_end.length()>0 || happy_this_session>0){
				Activity_Rewards.give_rewards(this,getPackageName(),pet_status,sound,message_begin,message_end,experience_points,bits,true,item_chance,pet_status.level);
			}
		}
    }
	@Override
    protected Dialog onCreateDialog(int id){
		AlertDialog.Builder builder=null;
		
        switch(id){
        case DIALOG_ID_GAME_DONE:
        	LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        	View layout=inflater.inflate(R.layout.dialog_game_rps,(ViewGroup)findViewById(R.id.root_dialog_game_rps));
        	builder=new AlertDialog.Builder(this);
        	builder.setView(layout);
			builder.setCancelable(true);
			dialog_game_done=builder.create();
			return dialog_game_done;
        default:
            return null;
        }
    }
	
	public void end_game(int pc){
		pet_status.sleeping_wake_up();
		
		int game_result=DRAW;
		
		int npc=RNG.random_range(ROCK,SCISSORS);
		
		if(pc==npc){
			game_result=DRAW;
		}
		else if(pc==ROCK){
			if(npc==PAPER){
				game_result=LOSE;
			}
			else if(npc==SCISSORS){
				game_result=WIN;
			}
		}
		else if(pc==PAPER){
			if(npc==ROCK){
				game_result=WIN;
			}
			else if(npc==SCISSORS){
				game_result=LOSE;
			}
		}
		else if(pc==SCISSORS){
			if(npc==ROCK){
				game_result=LOSE;
			}
			else if(npc==PAPER){
				game_result=WIN;
			}
		}
		
		results.add(game_result);
		
		happy_this_session+=RNG.random_range(Pet_Status.HAPPY_GAIN_GAME_RPS_MIN,Pet_Status.HAPPY_GAIN_GAME_RPS_MAX);
		
		if(game_result==WIN){
			happy_this_session+=RNG.random_range(Pet_Status.HAPPY_GAIN_GAME_RPS_MIN,Pet_Status.HAPPY_GAIN_GAME_RPS_MAX);
			
			bits_this_session+=Age_Tier.scale_bit_gain(pet_status.age_tier,RNG.random_range(Pet_Status.BIT_GAIN_RPS_MIN,Pet_Status.BIT_GAIN_RPS_MAX));
		}
		
		TextView tv=null;
		
		int image_pc=0;
		int image_npc=0;
		if(pc==ROCK){
			image_pc=R.drawable.rps_rock;
		}
		else if(pc==PAPER){
			image_pc=R.drawable.rps_paper;
		}
		else if(pc==SCISSORS){
			image_pc=R.drawable.rps_scissors;
		}
		
		if(npc==ROCK){
			image_npc=R.drawable.rps_rock;
		}
		else if(npc==PAPER){
			image_npc=R.drawable.rps_paper;
		}
		else if(npc==SCISSORS){
			image_npc=R.drawable.rps_scissors;
		}
		
		tv=(TextView)dialog_game_done.findViewById(R.id.dialog_rps_image_pc);
		tv.setBackgroundResource(image_pc);
		
		tv=(TextView)dialog_game_done.findViewById(R.id.dialog_rps_image_npc);
		tv.setBackgroundResource(image_npc);
		
		String message="";
    	if(game_result==WIN){
    		Sound_Manager.play_sound(Sound.GAME_WIN);
    		
    		message="You won!";
    	}
    	else if(game_result==LOSE){
    		Sound_Manager.play_sound(Sound.GAME_LOSS);
    		
    		message="You lost!";
    	}
    	else if(game_result==DRAW){
    		Sound_Manager.play_sound(Sound.GAME_DRAW);
    		
    		message="It was a draw!";
    	}
    	
    	tv=(TextView)dialog_game_done.findViewById(R.id.dialog_rps_message);
    	tv.setText(message);
    	
		showDialog(DIALOG_ID_GAME_DONE);
	}
	
	public void button_rock(View view){
    	end_game(ROCK);
    }
	public void button_paper(View view){
		end_game(PAPER);
    }
	public void button_scissors(View view){
		end_game(SCISSORS);
    }
	
	public void close_dialogs(){
		if(dialog_game_done.isShowing()){
			dismissDialog(DIALOG_ID_GAME_DONE);
	    }
	}
	
	public void set_dialog_buttons(){
		((Button)dialog_game_done.findViewById(R.id.button_dialog_game_rps_ok)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(dialog_game_done.isShowing()){
					dismissDialog(DIALOG_ID_GAME_DONE);
			    }
			}
		});
	}
}
