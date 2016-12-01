package org.cheeseandbacon.bitbeast;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class AnimatedSprite{
	private Bitmap bitmap;
    private Rect rect;
    int animation_speed;
    int frame_count;
    int frame;
    private int frame_counter;
    private boolean anim_fuzzy;
    
    public AnimatedSprite(){
    	bitmap=null;
    	rect=new Rect(0,0,0,0);
    	animation_speed=0;
    	frame_count=0;
    	frame=0;
    	frame_counter=0;
    	anim_fuzzy=false;
    }
    
    public void Initialize(View view,Image image,int w,int h,Bitmap get_bitmap,int get_animation_speed,int get_frame_count,boolean get_anim_fuzzy){
		bitmap=get_bitmap;
        rect.top=0;
        rect.bottom=h;
        rect.left=0;
        rect.right=w;
        animation_speed=get_animation_speed;
        frame_count=get_frame_count;
        anim_fuzzy=get_anim_fuzzy;
        frame=0;
    	frame_counter=0;
    	
    	image.screen_w=view.getWidth();
		image.screen_h=view.getHeight();
    }
    
    public int get_width(){
    	return bitmap.getWidth()/frame_count;
    }
    public int get_height(){
    	return bitmap.getHeight();
    }
    
    public void animate(int frame_ignore,int frame_override,int next_frame_override,int animation_speed_override){
    	int anim_adjust=0;
    	int animation_speed_actual=animation_speed;
    	
    	if(animation_speed_override>=0){
    		animation_speed_actual=animation_speed_override;
    	}
    	
    	if(anim_fuzzy){
    		anim_adjust=RNG.random_range(0,10);
    	}
    	
        if(++frame_counter>=animation_speed_actual+anim_adjust){
            frame_counter=0;
            frame++;
            
            int final_frame=frame_count;
            if(frame_ignore>=0){
            	final_frame=frame_ignore;
            }

            if(frame>=final_frame){
                frame=0;
            }
            
            if(next_frame_override>=0){
            	frame=next_frame_override;
            }
        }
        
        if(frame_override>=0){
        	frame=frame_override;
        }
    	
    	rect.left=frame*get_width();
    	rect.right=rect.left+get_width();
    }
    
    public void draw(Canvas canvas,Resources res,int x,int y,int w,int h,int facing,int color,boolean flip_y,float scale_x,float scale_y){
    	RectF dest=new RectF(x,y,x+w/scale_x,y+h/scale_y);
		
		float horizontal=1.0f;
		float vertical=1.0f;
		if(facing==Direction.RIGHT){
			horizontal=-1.0f;
			
			dest.left=canvas.getWidth()-x-w/scale_x;
			dest.right=dest.left+w/scale_x;
		}
		if(flip_y){
			vertical=-1.0f;
			
			dest.top=canvas.getHeight()-y-h/scale_y;
			dest.bottom=dest.top+h/scale_y;
		}
		
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		
		canvas.scale(horizontal,vertical,(float)canvas.getWidth()/2.0f,(float)canvas.getHeight()/2.0f);

		if(color!=Color.WHITE){
			Paint paint=new Paint();
			paint.setColorFilter(new LightingColorFilter(color,0));
			canvas.drawBitmap(bitmap,rect,dest,paint);
		}
		else{
			canvas.drawBitmap(bitmap,rect,dest,null);
		}
		
		canvas.restore();
    }
}
