package org.cheeseandbacon.bitbeast;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.widget.RemoteViews;

public class Widget_Provider extends AppWidgetProvider{
	@Override
	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds){
		final int widget_count=appWidgetIds.length;
		
		Options.startup();
        StorageManager.load_options(context);
		
		for(int i=0;i<widget_count;i++){
			int appWidgetId=appWidgetIds[i];
			
			updateAppWidget(context,appWidgetManager,appWidgetIds,appWidgetId);
		}
		
		Options.cleanup();
	}
	
	public static void updateAppWidget(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds,int appWidgetId){
		RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.widget);
		
		PendingIntent pendingIntent_game=PendingIntent.getActivity(context,0,new Intent(context,BitBeast.class),0);
		views.setOnClickPendingIntent(R.id.button_widget_game,pendingIntent_game);
		
		Intent intent=new Intent(context,Widget_Provider.class);
		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
		PendingIntent pendingIntent_update=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button_widget_update,pendingIntent_update);
		
		Pet pet=new Pet();
		
		long ms_last_run=StorageManager.load_pet_status(context,null,pet.get_status());
    	
		if(ms_last_run>=0){
			//Determine the ms since the last run.
			long ms_since_last_run=System.currentTimeMillis()-ms_last_run;
			
			//Determine the number of time ticks since the last run.
			long time_ticks=(long)Math.floor((ms_since_last_run/1000)/GameView.SECONDS_PER_TIME_TICK);
            
			if(!Options.pause && time_ticks>0){
				pet.process_time_tick(time_ticks,null,null,context,null);
			}
			
			ms_last_run=-1L;
		}
	
		//Determine the ms since the last run.
		long ms_since_last_tick=System.currentTimeMillis()-pet.get_status().last_tick;
		
		//Determine the number of ticks since the last run.
		long ticks=(long)Math.floor((ms_since_last_tick/1000)/GameView.SECONDS_PER_TICK);
		
		//Don't allow more ticks than the max.
		if(ticks>GameView.MAX_TICKS_PER_UPDATE){
			ticks=GameView.MAX_TICKS_PER_UPDATE;
		}
		
		if(ticks>0){
    		if(!Options.pause){
    			pet.process_tick(ticks,null,null,context,null,null);
    		}
    		
    		pet.get_status().last_tick=System.currentTimeMillis();
    	}

		BitmapDrawable bitmapDrawable=Pet_Type.get_drawable(context.getResources(),pet.get_status().type,true,0.5f,0.5f);
		bitmapDrawable.setColorFilter(pet.get_status().color,PorterDuff.Mode.MULTIPLY);
    	views.setImageViewBitmap(R.id.image_widget_pet,bitmapDrawable.getBitmap());

    	String sick_string="";
    	if(pet.get_status().sick){
    		sick_string=" (sick)";
    	}
    	
    	views.setTextViewText(R.id.text_widget_name,pet.get_status().name+sick_string);
    	
    	views.setTextViewText(R.id.text_widget_level,context.getString(R.string.status_level)+pet.get_status().level);
    	
    	views.setTextViewText(R.id.text_widget_title_happy,Pet_Status.get_buff_name("happy")+": ");
    	views.setProgressBar(R.id.bar_widget_happy,Pet_Status.HAPPY_MAX+Pet_Status.HAPPY_MAX,pet.get_status().happy+Pet_Status.HAPPY_MAX,false);
    	
    	float TEMP_BAR_MAX=40.0f;
    	float shrunk_temp=(float)pet.get_status().temp;
		if(shrunk_temp>TEMP_BAR_MAX){
			shrunk_temp=TEMP_BAR_MAX;
		}
		views.setTextViewText(R.id.text_widget_title_temp,context.getString(R.string.status_temp));
    	views.setProgressBar(R.id.bar_widget_temp,(int)TEMP_BAR_MAX,(int)shrunk_temp,false);
    	
    	views.setTextViewText(R.id.text_widget_title_hunger,Pet_Status.get_buff_name("hunger")+": ");
    	views.setProgressBar(R.id.bar_widget_hunger,Age_Tier.get_hunger_max(pet.get_status().age_tier),pet.get_status().hunger,false);
    	
    	views.setTextViewText(R.id.text_widget_title_thirst,Pet_Status.get_buff_name("thirst")+": ");
    	views.setProgressBar(R.id.bar_widget_thirst,Pet_Status.THIRST_MAX,pet.get_status().thirst,false);
    	
    	views.setTextViewText(R.id.text_widget_title_strength,Pet_Status.get_buff_name("strength_max")+": ");
    	views.setProgressBar(R.id.bar_widget_strength,pet.get_status().get_strength_max(),pet.get_status().get_strength(),false);
    	
    	views.setTextViewText(R.id.text_widget_title_dexterity,Pet_Status.get_buff_name("dexterity_max")+": ");
    	views.setProgressBar(R.id.bar_widget_dexterity,pet.get_status().get_dexterity_max(),pet.get_status().get_dexterity(),false);
    	
    	views.setTextViewText(R.id.text_widget_title_stamina,Pet_Status.get_buff_name("stamina_max")+": ");
    	views.setProgressBar(R.id.bar_widget_stamina,pet.get_status().get_stamina_max(),pet.get_status().get_stamina(),false);
    	
    	views.setTextViewText(R.id.text_widget_title_energy,Pet_Status.get_buff_name("energy_max")+": ");
    	views.setProgressBar(R.id.bar_widget_energy,pet.get_status().get_energy_max(),pet.get_status().get_energy(),false);

		appWidgetManager.updateAppWidget(appWidgetId,views);
	}
}
