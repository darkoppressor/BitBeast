package org.cheeseandbacon.bitbeast;

import java.util.ArrayList;

public class Distance{
	public static double distance_between_points(double x1,double y1,double x2,double y2){
	    return Math.sqrt(((x2-x1)*(x2-x1))+((y2-y1)*(y2-y1)));
	}
	
	public static Object_Ball nearest_ball(Object_Paddle object,ArrayList<Object_Ball> objects){
		double nearest_distance=-1.0;
		int nearest_object=-1;
		
		for(int i=0;i<objects.size();i++){
			//As long as this ball is not moving away from the paddle.
			if(!(object.y>0 && objects.get(i).move_speed_y<0) && !(object.y<=0 && objects.get(i).move_speed_y>0)){
				double x1=object.x+object.w/2;
	            double y1=object.y+object.h/2;
	            double x2=objects.get(i).x+objects.get(i).w/2;
	            double y2=objects.get(i).y+objects.get(i).h/2;
	
	            double this_distance=distance_between_points(x1,y1,x2,y2);
	
	            if(nearest_distance==-1.0 || this_distance<nearest_distance){
	                nearest_object=i;
	                nearest_distance=this_distance;
	            }
			}
		}
		
		if(nearest_object!=-1){
			return objects.get(nearest_object);
		}
		else{
			return null;
		}
	}
	
	public static Object_Powerup nearest_powerup(Object_Paddle object,ArrayList<Object_Powerup> objects){
		double nearest_distance=-1.0;
		int nearest_object=-1;
		
		for(int i=0;i<objects.size();i++){
			if(objects.get(i).type!=Object_Powerup.BALL_SPEED_DOWN){
				//As long as this powerup is not moving away from the paddle.
				if(!(object.y>0 && objects.get(i).move_speed_y<0) && !(object.y<=0 && objects.get(i).move_speed_y>0)){
					double x1=object.x+object.w/2;
		            double y1=object.y+object.h/2;
		            double x2=objects.get(i).x+objects.get(i).w/2;
		            double y2=objects.get(i).y+objects.get(i).h/2;
		
		            double this_distance=distance_between_points(x1,y1,x2,y2);
		
		            if(nearest_distance==-1.0 || this_distance<nearest_distance){
		                nearest_object=i;
		                nearest_distance=this_distance;
		            }
				}
			}
		}
		
		if(nearest_object!=-1){
			return objects.get(nearest_object);
		}
		else{
			return null;
		}
	}
}
