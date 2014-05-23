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

		//������Ҫ���ص����壺
		Girl girl;
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			//GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			//����Ҫ��ʾ�����壺
			girl = new Girl(MajorScene1.this);
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
            girl.drawAll();
		}
		
	}
	
	/*//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
    	
    	 float x = e.getX();
         float y = e.getY();
         switch (e.getAction()) {
         case MotionEvent.ACTION_MOVE:
        	 Log.i("���� Move ģʽ", "");
        	 //��������
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
             float dx = x - mPreviousX;//���㴥�ر�Xλ�� 
             float dy = y - mPreviousY;//���㴥�ر�Yλ�� 
             
             xAngle = dy * TOUCH_SCALE_FACTOR;//����̫����y����ת�ĽǶ�
             yAngle = dx * TOUCH_SCALE_FACTOR;
             GlobalMatrixState.rotate(xAngle, 1, 0, 0);
             GlobalMatrixState.rotate(yAngle, 0, 1, 0);
             break;
         case 261:
        	 mode += 1;
        	 Log.i("ʶ�𵽵ĵ����У�", ""+mode);
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
         mPreviousX = x;//��¼���ر�λ��
         mPreviousY = y;
         oldDis = newDis;
         return true; 
    }*/
}
