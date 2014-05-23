package com.gameEngine.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.gameEngine.object.TestDae;
import com.gameEngine.touch.ZoomListener;
import com.gameEngine.util.GlobalMatrixState;

public class MajorScene2 extends GLSurfaceView{
	SceneRender render;
    public float[] camera = {0.0f,500f,-500f,0f,0f,0f,0.0f,1.0f,0.0f};
	
	public MajorScene2(Context context) {
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
		//QinBingGirl girl;
		TestDae testDae;
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			//创建要显示的物体：
			//girl = new QinBingGirl(MajorScene2.this);
			testDae = new TestDae(MajorScene2.this);
			GlobalMatrixState.initMatrix();				//启用全局变换矩阵
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
            GlobalMatrixState.setLightLocation(-500,-800,800);							//设置全局灯光的初始位置
            

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			
			//清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //girl.drawAll();
            testDae.drawAllGeomatry();
		}
		
	}
}
