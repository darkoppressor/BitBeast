package org.cheeseandbacon.bitbeast;

import android.content.res.Resources;

public class Px{
	public static float dp(Resources res,float pixels){
		return pixels/((float)res.getDisplayMetrics().densityDpi/160.0f);
	}
	
	public static float px(Resources res,float dp){
		return dp*((float)res.getDisplayMetrics().densityDpi/160.0f);
	}
}
