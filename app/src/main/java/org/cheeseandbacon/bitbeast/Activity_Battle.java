package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Activity_Battle extends AppCompatActivity {
	Pet_Status us;
	Pet_Status them;
	Pet_Status us_at_start;
	
	ArrayList<Battle_Turn> hits;
	
	short strength_us;
	short strength_them;
	short dexterity_us;
	short dexterity_them;
	short stamina_us;
	short stamina_them;
	
	int bits_reward;
	int bit_level_diff_bonus;
	
	//If true, we should initiate a battle.
	//If false, this activity has been restarted, and the battle has already been fought.
	boolean battle;
	
	//Only used for saving and restoring state upon activity restart.
	boolean winner;
	
	//Used for rewards.
	boolean won;
	double weight_loss_this_session;
	int our_level;
	int their_level;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        us_at_start=null;
        
        bits_reward=0;
        bit_level_diff_bonus=0;
        
        battle=true;
        
        winner=true;
        
        won=false;
        weight_loss_this_session=0.0;
        our_level=0;
        their_level=0;
        
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.battle_text_us));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.battle_text_them));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.battle_text_numbers));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.battle_text_result));
        Font.set_typeface(getAssets(), (Button)findViewById(R.id.battle_button_replay));
        
        if(savedInstanceState!=null){
	        TextView tv=null;
	    	
	    	tv=(TextView)findViewById(R.id.battle_text_us);
	    	tv.setText(savedInstanceState.getString("text_us"));
	        
	        tv=(TextView)findViewById(R.id.battle_text_them);
	    	tv.setText(savedInstanceState.getString("text_them"));
	        
	        tv=(TextView)findViewById(R.id.battle_text_result);
	    	tv.setText(savedInstanceState.getString("text_result"));
	    	
	    	us_at_start=new Pet_Status();
	    	if(savedInstanceState.getString(getPackageName()+"type")!=null){
	    		us_at_start.read_bundle_battle_data(getPackageName() + ".us",savedInstanceState);
	    	}
	    	
	    	bits_reward=savedInstanceState.getInt("bits_reward");
	    	
	    	bit_level_diff_bonus=savedInstanceState.getInt("bit_level_diff_bonus");
	    	
	        battle=savedInstanceState.getBoolean("battle");
	        
	        winner=savedInstanceState.getBoolean("winner");
	        
	        won=savedInstanceState.getBoolean("won");
	        our_level=savedInstanceState.getInt("our_level");
	        their_level=savedInstanceState.getInt("their_level");
	        weight_loss_this_session=savedInstanceState.getDouble("weight_loss_this_session");
        }
    }
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	Drawable_Manager.unbind_drawables(findViewById(R.id.root_battle));
    	System.gc();
    }
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	setRequestedOrientation(Options.get_orientation(true));
    	Options.set_keep_screen_on(getWindow());
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	if(battle){
    		initiate_battle();
    	}
    	else{
    		no_battle();
    	}
    }
	@Override
    protected void onPause(){
    	super.onPause();
    	
    	overridePendingTransition(R.anim.transition_in,R.anim.transition_out);
    	
    	if(isFinishing()){
    		Pet_Status pet_status=new Pet_Status();
        	StorageManager.load_pet_status(this,null,pet_status);
        	
			DecimalFormat df=new DecimalFormat("#.##");
			
			int sound=-1;
			String message_begin="";
			String message_end="";
			long experience_points=0;
			int bits=0;
			int item_chance=0;
			
			message_begin="Done Battling!\n\n";
			
			if(won){
				message_begin+=pet_status.name+" won!";
			}
			else{
				message_begin+=pet_status.name+" lost!";
			}
			        	
			experience_points=(long)RNG.random_range((int)Pet_Status.EXPERIENCE_BASE_BATTLE_MIN,(int)Pet_Status.EXPERIENCE_BASE_BATTLE_MAX);
			
			experience_points+=Pet_Status.get_level_bonus_xp(pet_status.level,Pet_Status.EXPERIENCE_BONUS_BATTLE,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_HIGH);
			
        	if(won){
				long experience_mod=(long)Math.ceil((double)(Pet_Status.get_level_bonus_xp(our_level,Pet_Status.EXPERIENCE_BONUS_BATTLE,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_HIGH)-Pet_Status.get_level_bonus_xp(their_level,Pet_Status.EXPERIENCE_BONUS_BATTLE,Pet_Status.EXPERIENCE_VIRTUAL_LEVEL_HIGH))/1.5);
				if(experience_points-experience_mod>=1){
	        		experience_points-=experience_mod;
	        	}
        	}
			
			if(won){
				bits=bits_reward-bit_level_diff_bonus;
			}
			else{
				bits=(int)Math.ceil((float)bits_reward/2.0f);
			}
			
			item_chance=Pet_Status.ITEM_CHANCE_BATTLE;
			if(!won){
				item_chance=(int)Math.ceil((float)item_chance/2.0f);
			}
			
			if(!won){
				experience_points=(long)Math.ceil((double)experience_points/2.0);
			}
			
			if(!UnitConverter.is_weight_basically_zero(weight_loss_this_session,df)){
				message_end=pet_status.name+" lost "+UnitConverter.get_weight_string(weight_loss_this_session,df)+".";
			}
			
			pet_status.sleeping_wake_up();
	    	
			Activity_Rewards.give_rewards(this,getPackageName(),pet_status,sound,message_begin,message_end,experience_points,bits,false,item_chance,their_level);
		}
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
    	TextView tv=null;
    	
    	tv=(TextView)findViewById(R.id.battle_text_us);
    	savedInstanceState.putString("text_us",tv.getText().toString());
        
        tv=(TextView)findViewById(R.id.battle_text_them);
        savedInstanceState.putString("text_them",tv.getText().toString());
        
        tv=(TextView)findViewById(R.id.battle_text_result);
        savedInstanceState.putString("text_result",tv.getText().toString());
        
        if(us_at_start!=null){
        	savedInstanceState.putAll(us_at_start.write_bundle_battle_data(getPackageName() + ".us"));
        }
        
        savedInstanceState.putInt("bits_reward",bits_reward);
        
        savedInstanceState.putInt("bit_level_diff_bonus",bit_level_diff_bonus);
        
        savedInstanceState.putBoolean("battle",battle);
        
        savedInstanceState.putBoolean("winner",winner);
    	
    	savedInstanceState.putBoolean("won",won);
    	savedInstanceState.putInt("our_level",our_level);
    	savedInstanceState.putInt("their_level",their_level);
    	savedInstanceState.putDouble("weight_loss_this_session",weight_loss_this_session);
    	
    	super.onSaveInstanceState(savedInstanceState);
    }
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		
		ImageView iv=null;
		
		if(hasFocus){
			iv=(ImageView)findViewById(R.id.battle_image_stars_us);
	        ((AnimationDrawable)iv.getDrawable()).start();
	        
	        iv=(ImageView)findViewById(R.id.battle_image_stars_them);
	        ((AnimationDrawable)iv.getDrawable()).start();
		}
		else{
			iv=(ImageView)findViewById(R.id.battle_image_stars_us);
	        ((AnimationDrawable)iv.getDrawable()).stop();
	        
	        iv=(ImageView)findViewById(R.id.battle_image_stars_them);
	        ((AnimationDrawable)iv.getDrawable()).stop();
		}
	}
	
	public void button_replay(View view){
		//Only works once the battle has actually been processed and saved.
		if(!battle){
			initiate_battle();
		}
	}
	
	public static void set_pet_stats(Resources res,TextView tv_us,TextView tv_them,Pet_Status us_data,Pet_Status them_data,
			short our_strength,short our_dexterity,short our_stamina,short their_strength,short their_dexterity,short their_stamina){
		String message="";
        
        message=us_data.name+"\nLevel: "+us_data.level;
        
        if(us_data.get_strength_upgrade()>0){
        	message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
        }
        message+="\n"+Pet_Status.get_buff_name("strength_max")+": "+our_strength+"/"+us_data.get_strength_max();
        if(us_data.get_strength_upgrade()>0){
        	message+=" [+"+us_data.get_strength_upgrade()+"]</font>";
        }
        
        if(us_data.get_dexterity_upgrade()>0){
        	message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
        }
        message+="\n"+Pet_Status.get_buff_name("dexterity_max")+": "+our_dexterity+"/"+us_data.get_dexterity_max();
        if(us_data.get_dexterity_upgrade()>0){
        	message+=" [+"+us_data.get_dexterity_upgrade()+"]</font>";
        }
        
        if(us_data.get_stamina_upgrade()>0){
        	message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
        }
        message+="\n"+Pet_Status.get_buff_name("stamina_max")+": "+our_stamina+"/"+us_data.get_stamina_max();
        if(us_data.get_stamina_upgrade()>0){
        	message+=" [+"+us_data.get_stamina_upgrade()+"]</font>";
        }
        
        tv_us.setText(Html.fromHtml(Strings.newline_to_br(message)));
        
        message=them_data.name+"\nLevel: "+them_data.level;
        
        if(them_data.get_strength_upgrade()>0){
        	message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
        }
        message+="\n"+Pet_Status.get_buff_name("strength_max")+": "+their_strength+"/"+them_data.get_strength_max();
        if(them_data.get_strength_upgrade()>0){
        	message+=" [+"+them_data.get_strength_upgrade()+"]</font>";
        }
        
        if(them_data.get_dexterity_upgrade()>0){
        	message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
        }
        message+="\n"+Pet_Status.get_buff_name("dexterity_max")+": "+their_dexterity+"/"+them_data.get_dexterity_max();
        if(them_data.get_dexterity_upgrade()>0){
        	message+=" [+"+them_data.get_dexterity_upgrade()+"]</font>";
        }
        
        if(them_data.get_stamina_upgrade()>0){
        	message+="<font color='"+String.format("#%06x",0xFFFFFF&res.getColor(R.color.font_upgrade))+"'>";
        }
        message+="\n"+Pet_Status.get_buff_name("stamina_max")+": "+their_stamina+"/"+them_data.get_stamina_max();
        if(them_data.get_stamina_upgrade()>0){
        	message+=" [+"+them_data.get_stamina_upgrade()+"]</font>";
        }
        
        tv_them.setText(Html.fromHtml(Strings.newline_to_br(message)));
	}
	
	public void initiate_battle(){
		Resources res=getResources();
        
        Bundle bundle=getIntent().getExtras();
        boolean server=bundle.getBoolean(getPackageName()+".server");
        boolean shadow=bundle.getBoolean(getPackageName()+".shadow");
        int our_seed=bundle.getInt(getPackageName()+".our_seed");
        int their_seed=bundle.getInt(getPackageName()+".their_seed");
        
        //If this is the initial battle.
        if(battle){
            us=new Pet_Status();
            us.read_bundle_battle_data(getPackageName() + ".us",bundle);

            us_at_start=new Pet_Status();
            us_at_start.read_bundle_battle_data(getPackageName() + ".us",bundle);
        }
        else{
        	//Transfer the relevant data from the initial pet data holder to our pet data holder for the battle.
        	Bundle temp_bundle=new Bundle();
        	temp_bundle.putAll(us_at_start.write_bundle_battle_data(getPackageName() + ".us"));
        	us.read_bundle_battle_data(getPackageName() + ".us",temp_bundle);
        }
        
        them=new Pet_Status();
        them.read_bundle_battle_data(getPackageName() + ".them",bundle);
        
        setTitle(us.name+" vs. "+them.name);
        
        strength_us=us.get_strength();
        strength_them=them.get_strength();
        dexterity_us=us.get_dexterity();
        dexterity_them=them.get_dexterity();
        stamina_us=us.get_stamina();
        stamina_them=them.get_stamina();
        
        int bits_reward_them=0;
        int bit_level_diff_bonus_them=0;
        
        if(battle){
        	//Base bit gain.
        	int bit_gain=RNG.random_range(Pet_Status.BIT_GAIN_BATTLE_MIN,Pet_Status.BIT_GAIN_BATTLE_MAX);
        	
        	//Determine average stat levels of both pets.
        	short us_stat_max=(short)((us.get_strength_max()+us.get_dexterity_max()+us.get_stamina_max())/3);
        	short them_stat_max=(short)((them.get_strength_max()+them.get_dexterity_max()+them.get_stamina_max())/3);
        	
        	//Determine a scale for bit gain based on relative stat level of the two pets.
        	int bit_gain_mod=(int)Math.ceil(((double)us_stat_max-(double)them_stat_max)*Pet_Status.BIT_GAIN_BATTLE_LEVEL_SCALE_FACTOR);
        	
        	if(bit_gain-bit_gain_mod<Pet_Status.BIT_GAIN_BATTLE_MIN){
        		bit_gain_mod=0;
        	}
        	
        	bits_reward=Age_Tier.scale_bit_gain(us.age_tier,bit_gain);
        	bit_level_diff_bonus=Age_Tier.scale_bit_gain(us.age_tier,bit_gain_mod);
        	
        	//Determine a scale for bit gain based on relative stat level of the two pets.
        	bit_gain_mod=(int)Math.ceil(((double)them_stat_max-(double)us_stat_max)*Pet_Status.BIT_GAIN_BATTLE_LEVEL_SCALE_FACTOR);
        	
        	if(bit_gain-bit_gain_mod<Pet_Status.BIT_GAIN_BATTLE_MIN){
        		bit_gain_mod=0;
        	}
        	
        	bits_reward_them=Age_Tier.scale_bit_gain(them.age_tier,bit_gain);
        	bit_level_diff_bonus_them=Age_Tier.scale_bit_gain(them.age_tier,bit_gain_mod);
        }
        
        our_level=us.level;
        their_level=them.level;
        
        weight_loss_this_session=us.get_weight();
        
        hits=Battle.battle(this,us,them,server,our_seed,their_seed,bits_reward_them,bit_level_diff_bonus_them,shadow);
        
        weight_loss_this_session-=us.get_weight();
        
        us.sleeping_wake_up();
        if(battle){
        	StorageManager.save_pet_status(this,us);
        }
        
        //The battle has been fought, and should not be fought again if the activity is restarted.
        battle=false;
        
        TextView tv;
        ImageViewUs ivu;
        ImageViewThem ivt;
        ImageView iv;
        
        ivu=(ImageViewUs)findViewById(R.id.battle_image_us);
        ivu.setImageDrawable(Pet_Type.get_drawable(res,us.type,true,1.0f,1.0f));
		ivu.setColorFilter(us.color,PorterDuff.Mode.MULTIPLY);
        ivu.set_ab(Activity_Battle.this);
        
        ivt=(ImageViewThem)findViewById(R.id.battle_image_them);
        ivt.setImageDrawable(Pet_Type.get_drawable(res,them.type,false,1.0f,1.0f));
		ivt.setColorFilter(them.color,PorterDuff.Mode.MULTIPLY);
        ivt.set_ab(Activity_Battle.this);
        
        set_pet_stats(res,(TextView)findViewById(R.id.battle_text_us),(TextView)findViewById(R.id.battle_text_them),us,them,strength_us,dexterity_us,stamina_us,strength_them,dexterity_them,stamina_them);
        
        tv=(TextView)findViewById(R.id.battle_text_result);
    	tv.setText("");
        
        iv=(ImageView)findViewById(R.id.battle_image_stars_us);
        iv.setVisibility(ImageView.GONE);
        
        iv=(ImageView)findViewById(R.id.battle_image_stars_them);
        iv.setVisibility(ImageView.GONE);
        
        boolean we_start=false;
        won=false;
        
        if(!server){
        	if(hits.get(0).damage==0){
        		we_start=true;
        	}
        	if(hits.get(1).damage==0){
        		won=true;
        	}
		}
		else{
			if(hits.get(0).damage==1){
				we_start=true;
        	}
			if(hits.get(1).damage==1){
        		won=true;
        	}
		}
        hits.remove(0);
        hits.remove(0);
        
        if(hits.size()>0){
	        if(we_start){
	        	ivu=(ImageViewUs)findViewById(R.id.battle_image_us);
	            ivu.startAnimation(AnimationUtils.loadAnimation(this,R.anim.battle_attack_us));
	        }
	        else{
	        	ivt=(ImageViewThem)findViewById(R.id.battle_image_them);
	            ivt.startAnimation(AnimationUtils.loadAnimation(this,R.anim.battle_attack_them));
	        }
        }
	}
	
	public void no_battle(){
		Resources res=getResources();
		
		Bundle bundle=getIntent().getExtras();

        us=new Pet_Status();
        us.read_bundle_battle_data(getPackageName() + ".us",bundle);

        them=new Pet_Status();
        them.read_bundle_battle_data(getPackageName() + ".them",bundle);
        
        setTitle(us.name+" vs. "+them.name);
        
        ImageViewUs ivu;
        ImageViewThem ivt;
        ImageView iv;
        
        ivu=(ImageViewUs)findViewById(R.id.battle_image_us);
        ivu.setImageDrawable(Pet_Type.get_drawable(res,us.type,true,1.0f,1.0f));
		ivu.setColorFilter(us.color,PorterDuff.Mode.MULTIPLY);
        
        ivt=(ImageViewThem)findViewById(R.id.battle_image_them);
        ivt.setImageDrawable(Pet_Type.get_drawable(res,them.type,false,1.0f,1.0f));
		ivt.setColorFilter(them.color,PorterDuff.Mode.MULTIPLY);
        
        iv=(ImageView)findViewById(R.id.battle_image_stars_us);
        if(!winner){
        	iv.setVisibility(ImageView.VISIBLE);
        }
        else{
        	iv.setVisibility(ImageView.GONE);
        }
        
        iv=(ImageView)findViewById(R.id.battle_image_stars_them);
        if(winner){
        	iv.setVisibility(ImageView.VISIBLE);
        }
        else{
        	iv.setVisibility(ImageView.GONE);
        }
	}
}
