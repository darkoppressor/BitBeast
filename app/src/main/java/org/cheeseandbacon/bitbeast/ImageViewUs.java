/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageViewUs extends ImageView{
	Activity_Battle ab;
	boolean attacking;
	
	public ImageViewUs(Context context){
		super(context);
		
		ab=null;
		attacking=true;
	}
	
	public ImageViewUs(Context context,AttributeSet attrs){
		super(context,attrs);
		
		ab=null;
		attacking=true;
	}
	
	public ImageViewUs(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		
		ab=null;
		attacking=true;
	}
	
	public void set_ab(Activity_Battle get_ab){
		ab=get_ab;
	}
	
	@Override
	protected void onAnimationEnd(){
		super.onAnimationEnd();
		
		//If we just finished the attack animation.
		if(attacking){
			attacking=false;
			
			short damage=ab.hits.get(0).damage;
			
			ab.strength_us=ab.hits.get(0).strength_attacker;
			ab.dexterity_us=ab.hits.get(0).dexterity_attacker;
			ab.stamina_us=ab.hits.get(0).stamina_attacker;
			
			ab.strength_them=ab.hits.get(0).strength_defender;
			ab.dexterity_them=ab.hits.get(0).dexterity_defender;
			ab.stamina_them=ab.hits.get(0).stamina_defender;
			
			ab.hits.remove(0);
			
			TextView tv=null;
			
			Activity_Battle.set_pet_stats(ab.getResources(),(TextView)ab.findViewById(R.id.battle_text_us),(TextView)ab.findViewById(R.id.battle_text_them),ab.us,ab.them,ab.strength_us,ab.dexterity_us,ab.stamina_us,ab.strength_them,ab.dexterity_them,ab.stamina_them);

	        String damage_message=""+damage;
	        if(damage==0){
	        	damage_message="Miss";
	        }
	        tv=(TextView)ab.findViewById(R.id.battle_text_numbers);
	        tv.setText(damage_message);
	        tv.startAnimation(AnimationUtils.loadAnimation(ab,R.anim.battle_numbers));
	        
            startAnimation(AnimationUtils.loadAnimation(ab,R.anim.battle_return_us));
            
            if(damage==0){
            	Sound_Manager.playSound(getContext(), Sound.BATTLE_MISS);
            }
            else{
            	Sound_Manager.playSound(getContext(), Sound.BATTLE_HIT);
            }
		}
		//If we just finished the return animation.
		else{
			attacking=true;
			
			if(ab.hits.size()>0){
		        ImageViewThem ivt=null;
		        
				ivt=(ImageViewThem)ab.findViewById(R.id.battle_image_them);
	            ivt.startAnimation(AnimationUtils.loadAnimation(ab,R.anim.battle_attack_them));
			}
			else{
				TextView tv=null;
				ImageView iv=null;
		        
				tv=(TextView)ab.findViewById(R.id.battle_text_numbers);
				tv.setVisibility(GONE);
				
				tv=(TextView)ab.findViewById(R.id.battle_text_result);
				tv.setText(ab.us.name+" wins!");
				
				iv=(ImageView)ab.findViewById(R.id.battle_image_stars_them);
				iv.setVisibility(ImageView.VISIBLE);
		        ((AnimationDrawable)iv.getDrawable()).start();
		        
		        ab.winner=true;
				
				Sound_Manager.playSound(getContext(), Sound.GAME_WIN);
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		((View)this.getParent()).invalidate();
	}
}
