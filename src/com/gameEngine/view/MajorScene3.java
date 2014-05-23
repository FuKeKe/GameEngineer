package com.gameEngine.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.gameEngine.sceneObject.Teapot;
import com.gameEngine.touch.ZoomListener;
import com.gameEngine.util.GlobalMatrixState;

public class MajorScene3 extends GLSurfaceView{
	SceneRender render;
	//������Ҫ���ص����壺
	public Teapot teapot;
    public float[] camera = {0.0f,0.0f,-300f,0f,0f,0f,0.0f,1.0f,0.0f};
	
	public MajorScene3(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.setEGLContextClientVersion(2);
		render = new SceneRender();
		this.setRenderer(render);
		this.setRenderMode(RENDERMODE_CONTINUOUSLY);
		this.setOnTouchListener(new ZoomListener(this));
	}
	
	public class SceneRender implements Renderer{
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			//����Ҫ��ʾ�����壺
			teapot = new Teapot(MajorScene3.this);
			GlobalMatrixState.initMatrix();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// TODO Auto-generated method stub
			
        	GLES20.glViewport(0, 0, width, height); 						//�����Ӵ���С��λ�� 
            float ratio= (float) width / height;							//����GLSurfaceView�Ŀ�߱�
            GlobalMatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 3f, 2000);	//���ô˷���������� ȫ�� ͸��ͶӰ����
            GlobalMatrixState.setCameraLocation(camera);   								//���ô˷������� ȫ�� �����9����λ�þ���    
            //GLES20.glEnable(GLES20.GL_CULL_FACE);  							//�򿪱������
            //GLES20.glFrontFace(GLES20.GL_BACK);
            GlobalMatrixState.setLightLocation(-50,-80,80);							//����ȫ�ֵƹ�ĳ�ʼλ��
            

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			
			//�����Ȼ�������ɫ����
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            teapot.drawAllGeomatry();
		}
		
	}
}
