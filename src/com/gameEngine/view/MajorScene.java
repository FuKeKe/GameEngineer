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
	
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
	private float mPreviousX;//�ϴεĴ���λ��X����
    private float mPreviousY;//�ϴεĴ���λ��Y����
	
	public MajorScene(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.setEGLContextClientVersion(2);
		render = new SceneRender();
		this.setRenderer(render);
		this.setRenderMode(RENDERMODE_CONTINUOUSLY);
	}
	
	public class SceneRender implements Renderer{

		//������Ҫ���ص����壺
		
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			
			//����Ҫ��ʾ�����壺
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// TODO Auto-generated method stub
			
        	GLES20.glViewport(0, 0, width, height); 						//�����Ӵ���С��λ�� 
            float ratio= (float) width / height;							//����GLSurfaceView�Ŀ�߱�
            GlobalMatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 3f, 100);	//���ô˷����������͸��ͶӰ����
            GlobalMatrixState.setCameraLocation(0,0,7.2f,0f,0f,0f,0f,1.0f,0.0f);   		//���ô˷������������9����λ�þ���    
            GLES20.glEnable(GLES20.GL_CULL_FACE);  							//�򿪱������
            GlobalMatrixState.setLightLocation(100,5,0);							//���õƹ�ĳ�ʼλ��

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			
			//�����Ȼ�������ɫ����
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            
            //xxx.pushMatrix();
            
            //xxx.popMatrix();
		}
		
	}
	
	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
		return true;
    	
    }
}
