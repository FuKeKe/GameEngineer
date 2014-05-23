package com.gameEngine.touch;

import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.gameEngine.util.GlobalMatrixState;
import com.gameEngine.util.Normal;
import com.gameEngine.view.MajorScene1;
import com.gameEngine.view.MajorScene2;

public class ZoomListener implements OnTouchListener{
	int mode = 0;			//识别到的点数
	float oldDist = 0;		//上次两点距离
	float[] camera = {0.0f,0.0f,-500,0f,0f,0f,0.0f,1.0f,0.0f};
    float yAngle=0;//摄像机绕y轴旋转的角度
    float xAngle=0;//摄像机绕X轴旋转的角度
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
	private float mPreviousX;//上次的触控位置X坐标
    private float mPreviousY;//上次的触控位置Y坐标
    String clazzName = "View";
    
	public ZoomListener(Object o){
		clazzName = o.getClass().getName(); 
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//Log.i("接到点击", ""+(event.getAction() & MotionEvent.ACTION_MASK));
		// TODO Auto-generated method stub
		/*if("MajorScene1".equals(clazzName)){
			camera = ((MajorScene1)v).camera;
		}
		if("MajorScene2".equals(clazzName)){
			camera = ((MajorScene2)v).camera;
		}*/
        float x = event.getX();
        float y = event.getY();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {  
        case MotionEvent.ACTION_DOWN:  
            mode = 1;
            Log.i("ACTION_DOWN",""+mode);
            mPreviousX = event.getX();
            mPreviousY = event.getY();
            break;  
        case MotionEvent.ACTION_UP:  
            mode = 0;
            Log.i("ACTION_UP",""+mode);
            mPreviousX = 0;
            mPreviousY = 0;
            break;  
        case MotionEvent.ACTION_POINTER_UP:
        	Log.e("--------",""+event.getActionIndex());
        	if(event.getPointerCount()<3){
        		if(event.getActionIndex() == 0){
        			mPreviousX = event.getX(1);
        			mPreviousY = event.getY(1);
        		}
        		if(event.getActionIndex() == 1){
        			mPreviousX = event.getX(0);
        			mPreviousY = event.getY(0);
        		}
        	}
            mode -= 1;
            Log.i("ACTION_POINTER_UP",""+mode);
            break;  
        case MotionEvent.ACTION_POINTER_DOWN:  
            oldDist = spacing(event);  
            mode += 1;
            Log.i("ACTION_POINTER_DOWN",""+mode);
            break;  
  
        case MotionEvent.ACTION_MOVE:
        	float flag = 2;			//开始变换的阀值
        	if(mode == 1){
        		float dx = x - mPreviousX;//计算触控笔X位移 
                float dy = y - mPreviousY;//计算触控笔Y位移 
                float step = 1;
                if(Math.abs(dy) > flag){
                	xAngle = dy * TOUCH_SCALE_FACTOR / step;//设置绕y轴旋转的角度
                	GlobalMatrixState.rotate(xAngle, 1, 0, 0);
                }
                if(Math.abs(dx) > flag){
                	yAngle = dx * TOUCH_SCALE_FACTOR / step;
                	GlobalMatrixState.rotate(yAngle, 0, 1, 0);
                }
                mPreviousX = x;
                mPreviousY = y;
        	}
            if (mode >= 2) {  
            	//float normal[] = Normal.vectorNormal(new float[]{camera[0],camera[1],camera[2]});
                float newDist = spacing(event);
                //float maxDis = LoadedDAE.maxDistance * 1.2f;
                float maxDis = 1000;
            	float minDis = 5;
            	float step = 100;
            	//放大、拉近
                if ((newDist > oldDist + flag) && (newDist < 700)) {
                	if(0 < camera[2]){
	                    camera[2] -= newDist/step;
	                    
	                    if(camera[2] < minDis){
	                    	 camera[2] = minDis;
	                    }
	                }
                	if(0 > camera[2]){
                		camera[2] += newDist/step;
	                    if(camera[2] > -minDis){
	                    	 camera[2] = -minDis;
	                    }
                	}
                	GlobalMatrixState.setCameraLocation(camera);
                    oldDist = newDist;
                	//GlobalMatrixState.translate(-normal[0]*newDist/step, -normal[1]*newDist/step, -normal[2]*newDist/step);
                }
                //缩小、拉远
                if (newDist < oldDist - flag && (newDist > 0)) {
                	if(0 < camera[2]){
	                    camera[2] += newDist/step;
	                    
	                    if(camera[2] > maxDis){
	                    	 camera[2] = maxDis;
	                    }
	                }
                	if(0 > camera[2]){
                		camera[2] -= newDist/step;
	                    if(camera[2] < -maxDis){
	                    	 camera[2] = -maxDis;
	                    }
                	}
                    GlobalMatrixState.setCameraLocation(camera); 
                    oldDist = newDist;
                	//GlobalMatrixState.translate(normal[0]*newDist/step, normal[1]*newDist/step, normal[2]*newDist/step);
                }  
            }
            Log.i("ACTION_MOVE","点数："+mode+" 两点距离："+oldDist+" 最终摄像机距离："+camera[2]);
            break;
        }  
        return true;  
	}
	 private float spacing(MotionEvent event) {  
	        float x = event.getX(0) - event.getX(1);  
	        float y = event.getY(0) - event.getY(1);  
	        return FloatMath.sqrt(x * x + y * y);  
	}

}
