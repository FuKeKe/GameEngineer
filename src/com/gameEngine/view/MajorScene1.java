package com.gameEngine.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.gameEngine.object.Girl;
import com.gameEngine.touch.ZoomListener;
import com.gameEngine.util.GlobalMatrixState;

public class MajorScene1 extends GLSurfaceView{
	SceneRender render;
    public float[] camera = {0.0f,80f,-300f,0f,80f,0f,0.0f,1.0f,0.0f};
	
	public MajorScene1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.setEGLContextClientVersion(2);
		render = new SceneRender();
		this.setRenderer(render);
		this.setRenderMode(RENDERMODE_CONTINUOUSLY);
		this.setOnTouchListener(new ZoomListener(this));
	}
	
	public class SceneRender implements Renderer{

		//场景中要加载的物体：
		Girl girl;
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			//GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			//创建要显示的物体：
			girl = new Girl(MajorScene1.this);
			GlobalMatrixState.initMatrix();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// TODO Auto-generated method stub
			
        	GLES20.glViewport(0, 0, width, height); 						//设置视窗大小及位置 
            float ratio= (float) width / height;							//计算GLSurfaceView的宽高比
            GlobalMatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 3f, 2000);	//调用此方法计算产生 全局 透视投影矩阵
            GlobalMatrixState.setCameraLocation(camera);   								//调用此方法产生 全局 摄像机9参数位置矩阵    
            //GLES20.glEnable(GLES20.GL_CULL_FACE);  							//打开背面剪裁
            //GLES20.glFrontFace(GLES20.GL_BACK);
            GlobalMatrixState.setLightLocation(-50,-80,80);							//设置全局灯光的初始位置
            

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			
			//清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            girl.drawAll();
		}
		
	}
	
	/*//触摸事件回调方法
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
    	
    	 float x = e.getX();
         float y = e.getY();
         switch (e.getAction()) {
         case MotionEvent.ACTION_MOVE:
        	 Log.i("进入 Move 模式", "");
        	 //两点缩放
        	 if(mode>=2){
        		 float ddx = e.getX(0)-e.getX(1);
        		 float ddy = e.getY(0)-e.getY(1);
	        	 newDis =  (float) Math.sqrt(ddx*ddx+ddy*ddy);
	             if(newDis > oldDis){
	            	 unit_size +=10;
	            	 }
	             else {
	            	 unit_size -=10;
				}
             }
             float dx = x - mPreviousX;//计算触控笔X位移 
             float dy = y - mPreviousY;//计算触控笔Y位移 
             
             xAngle = dy * TOUCH_SCALE_FACTOR;//设置太阳绕y轴旋转的角度
             yAngle = dx * TOUCH_SCALE_FACTOR;
             GlobalMatrixState.rotate(xAngle, 1, 0, 0);
             GlobalMatrixState.rotate(yAngle, 0, 1, 0);
             break;
         case 261:
        	 mode += 1;
        	 Log.i("识别到的点数有：", ""+mode);
        	 break;
         case 262: 
        	 mode -= 1;
        	 break;
         case MotionEvent.ACTION_DOWN:
        	 mode = 1;
        	 break;
         case MotionEvent.ACTION_UP:
        	 mode = 0;
        	 break;
         }
         mPreviousX = x;//记录触控笔位置
         mPreviousY = y;
         oldDis = newDis;
         return true; 
    }*/
}
