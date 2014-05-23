package com.gameEngine.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.gameEngine.util.GlobalMatrixState;

public class MajorScene extends GLSurfaceView{
	SceneRender render;
	
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
	private float mPreviousX;//上次的触控位置X坐标
    private float mPreviousY;//上次的触控位置Y坐标
	
	public MajorScene(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.setEGLContextClientVersion(2);
		render = new SceneRender();
		this.setRenderer(render);
		this.setRenderMode(RENDERMODE_CONTINUOUSLY);
	}
	
	public class SceneRender implements Renderer{

		//场景中要加载的物体：
		
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			
			//创建要显示的物体：
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// TODO Auto-generated method stub
			
        	GLES20.glViewport(0, 0, width, height); 						//设置视窗大小及位置 
            float ratio= (float) width / height;							//计算GLSurfaceView的宽高比
            GlobalMatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 3f, 100);	//调用此方法计算产生透视投影矩阵
            GlobalMatrixState.setCameraLocation(0,0,7.2f,0f,0f,0f,0f,1.0f,0.0f);   		//调用此方法产生摄像机9参数位置矩阵    
            GLES20.glEnable(GLES20.GL_CULL_FACE);  							//打开背面剪裁
            GlobalMatrixState.setLightLocation(100,5,0);							//设置灯光的初始位置

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			
			//清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            
            //xxx.pushMatrix();
            
            //xxx.popMatrix();
		}
		
	}
	
	//触摸事件回调方法
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
		return true;
    	
    }
}
