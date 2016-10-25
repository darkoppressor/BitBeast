package org.cheeseandbacon.bitbeast;

public class Time_String{
	public static String seconds_to_years(long seconds){
		String text="";
		
        long minutes=0;
        long hours=0;
        long days=0;
        long weeks=0;
        long months=0;
        long years=0;
        
        for(long i=seconds;i>=60;){
            minutes++;
            i-=60;
            if(i<60){
                seconds=i;
            }
        }
        for(long i=minutes;i>=60;){
            hours++;
            i-=60;
            if(i<60){
                minutes=i;
            }
        }
        for(long i=hours;i>=24;){
            days++;
            i-=24;
            if(i<24){
                hours=i;
            }
        }
        for(long i=days;i>=7;){
            weeks++;
            i-=7;
            if(i<7){
                days=i;
            }
        }
        for(long i=weeks;i>=4;){
            months++;
            i-=4;
            if(i<4){
                weeks=i;
            }
        }
        for(long i=months;i>=12;){
            years++;
            i-=12;
            if(i<12){
                months=i;
            }
        }
        
        if(years>0){
	        text+=years+" year";
	        if(years!=1){
	        	text+="s";
	        }
	        text+=", ";
        }
        
        if(months>0){
	        text+=months+" month";
	        if(months!=1){
	        	text+="s";
	        }
	        text+=", ";
        }
        
        if(weeks>0){
	        text+=weeks+" week";
	        if(weeks!=1){
	        	text+="s";
	        }
	        text+=", ";
        }
        
        if(days>0){
	        text+=days+" day";
	        if(days!=1){
	        	text+="s";
	        }
	        text+=", ";
        }
        
        if(hours>0){
	        text+=hours+" hour";
	        if(hours!=1){
	        	text+="s";
	        }
	        text+=", ";
        }

        if(minutes>0){
	        text+=minutes+" minute";
	        if(minutes!=1){
	        	text+="s";
	        }
	        text+=", ";
        }

        text+=seconds+" second";
        if(seconds!=1){
        	text+="s";
        }
        
        return text;
	}
	
	public static String seconds_to_years_highest(long seconds){
		String text="";
		
        long minutes=0;
        long hours=0;
        long days=0;
        long weeks=0;
        long months=0;
        long years=0;
        
        for(long i=seconds;i>=60;){
            minutes++;
            i-=60;
            if(i<60){
                seconds=i;
            }
        }
        for(long i=minutes;i>=60;){
            hours++;
            i-=60;
            if(i<60){
                minutes=i;
            }
        }
        for(long i=hours;i>=24;){
            days++;
            i-=24;
            if(i<24){
                hours=i;
            }
        }
        for(long i=days;i>=7;){
            weeks++;
            i-=7;
            if(i<7){
                days=i;
            }
        }
        for(long i=weeks;i>=4;){
            months++;
            i-=4;
            if(i<4){
                weeks=i;
            }
        }
        for(long i=months;i>=12;){
            years++;
            i-=12;
            if(i<12){
                months=i;
            }
        }
        
        if(years>0){
	        text+=years+" year";
	        if(years!=1){
	        	text+="s";
	        }
        }
        
        else if(months>0){
	        text+=months+" month";
	        if(months!=1){
	        	text+="s";
	        }
        }
        
        else if(weeks>0){
	        text+=weeks+" week";
	        if(weeks!=1){
	        	text+="s";
	        }
        }
        
        else if(days>0){
	        text+=days+" day";
	        if(days!=1){
	        	text+="s";
	        }
        }
        
        else if(hours>0){
	        text+=hours+" hour";
	        if(hours!=1){
	        	text+="s";
	        }
        }

        else if(minutes>0){
	        text+=minutes+" minute";
	        if(minutes!=1){
	        	text+="s";
	        }
        }

        else{
	        text+=seconds+" second";
	        if(seconds!=1){
	        	text+="s";
	        }
        }
        
        return text;
	}
}
