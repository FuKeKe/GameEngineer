package com.gameEngine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.Matrix;

/**
 * Created by keke on 14-3-28.
 */
public class MatrixState implements MatrixStateInf{
	
	private  float[] mVMatrix	 = new float[16];			//�������
    private  float[] mProjMatrix = new float[16];			//ͶӰ����
    private  float[] mMVPMatrix	 = new float[16];			//���ձ任���󣨺�ͶӰ������
    private  float[] currMatrix	 = new float[16];			//���嵱ǰ�ı任����
    private  float[] originalMatrix = new float[16];		//�����ʼ�ı任����

    //�����任�����ջ
    static float[][] mStack=new float[10][16];
    static int stackTop = -1;
    
    //��Դλ��
    private  float[] fLightLocation = new float[]{0,0,0};
    private  FloatBuffer lightPositionBuffer;
    private boolean independentLight = false;				//�Ƿ���������Դ��Ĭ����falseҪ�������Ļ��뵥�����ã�
    
    //�����λ��
    private  float[] fCameraLocation = new float[]{0,0,0};
    private  FloatBuffer cameraLocationBuffer;
    private boolean independentCamera = false;				//�Ƿ��������ӽ�
    
    public MatrixState(){
    	initMatrix();		//��ʼ�任����
    }
    
    public  void initMatrix()//��ȡ���任��ʼ����
    {
    	Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);//��ʼ������
    	Matrix.setRotateM(originalMatrix, 0, 0, 1, 0, 0);//��ʼ������
    	lightPositionBuffer = GlobalMatrixState.getLightLocation();
    	cameraLocationBuffer = GlobalMatrixState.getCameraLocation();
    }
    
    
    public float[] getCurrMatrix() {
		return currMatrix;
	}

	public void setCurrMatrix(float[] currMatrix) {
		this.currMatrix = currMatrix;
	}

	public  void setCameraLocation(
            float cx, float cy, float cz,           //��Ӱ��λ��
            float tx, float ty, float tz,           //Ŀ��λ��
            float upx, float upy, float upz         //��Ӱ�� up ����
    )
    {
        Matrix.setLookAtM(mVMatrix,0,cx,cy,cz,tx,ty,tz,upx,upy,upz);
        fCameraLocation[0] = cx; fCameraLocation[1] = cy; fCameraLocation[2] = cz;
        
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        
        cameraLocationBuffer = byteBuffer.asFloatBuffer();
        cameraLocationBuffer.put(fCameraLocation);
        cameraLocationBuffer.flip();
    }
    
    public  void setCameraLocation(float[] camera)
    {
        Matrix.setLookAtM(mVMatrix,0,camera[0],camera[1],camera[2],camera[3],camera[4],camera[5],camera[6],camera[7],camera[8]);
        fCameraLocation[0] = camera[0]; fCameraLocation[1] = camera[1]; fCameraLocation[2] = camera[2];
        
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        
        cameraLocationBuffer = byteBuffer.asFloatBuffer();
        cameraLocationBuffer.put(fCameraLocation);
        cameraLocationBuffer.flip();
    }
    
    public  void setProjectOrtho(             //��������ͶӰ
            float left, float right,
            float bottom, float top,
            float near, float far
    )
    {
        Matrix.orthoM(mProjMatrix,0,left,right,bottom,top,near,far);
    }
    
    public  void setProjectFrustum(
    		float left, float right,
    		float bottom, float top,
    		float near, float far
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }
    
    public  float[] getFinalMatrix()
    {
        Matrix.multiplyMM(mMVPMatrix,0,GlobalMatrixState.currMatrix,0,currMatrix,0);	// ȫ����ת�仯
        Matrix.multiplyMM(mMVPMatrix,0,GlobalMatrixState.mVMatrix,0,mMVPMatrix,0); 		// ʹ��ȫ�������
        Matrix.multiplyMM(mMVPMatrix,0,GlobalMatrixState.mProjMatrix,0,mMVPMatrix,0);	// ʹ��ȫ�־���
        return mMVPMatrix;
    }
    
    public  void pushMatrix()//�����任����
    {
    	stackTop++;
    	for(int i=0;i<16;i++)
    	{
    		mStack[stackTop][i]=currMatrix[i];
    	}
    }
    
    public  void popMatrix()//�ָ��任����
    {
    	for(int i=0;i<16;i++)
    	{
    		currMatrix[i]=mStack[stackTop][i];
    	}
    	stackTop--;
    }
    
    public  void translate(float x,float y,float z)//������xyz���ƶ�
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    
    public  void rotate(float angle,float x,float y,float z)//������xyz���ƶ�
    {
    	Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }
    
    //��ȡ��������ı任����
    public  float[] getMMatrix()
    {       
        return currMatrix;
    }
    
    public  void setLightLocation(float x, float y, float z) 
    {
    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3*4);
    	fLightLocation[0] = x; fLightLocation[1] = y; fLightLocation[2] = z;
    	
    	byteBuffer.order(ByteOrder.nativeOrder());
    	lightPositionBuffer = byteBuffer.asFloatBuffer();
    	lightPositionBuffer.put(fLightLocation);
    	lightPositionBuffer.position(0);
	}

	public  FloatBuffer getLightLocation() {
		if(independentLight){
			return lightPositionBuffer;
		}else{
			return GlobalMatrixState.getLightLocation();					//���ع��գ�Ĭ��ʹ��ȫ��
		}
	}

	public  FloatBuffer getCameraLocation() {
		if(independentCamera){
			return cameraLocationBuffer;
		}else{
			return GlobalMatrixState.getCameraLocation();				//��������Ĭ��ʹ��ȫ��
		}
	}

	public  float[] getmVMatrix() {
		return mVMatrix;
	}

	public  void setmVMatrix(float[] mVMatrix) {
		this.mVMatrix = mVMatrix;
	}

	public boolean isIndependentLight() {
		return independentLight;
	}

	public void setIndependentLight(boolean independentLight) {
		this.independentLight = independentLight;
	}

	public boolean isIndependentCamera() {
		return independentCamera;
	}

	public void setIndependentCamera(boolean independentCamera) {
		this.independentCamera = independentCamera;
	}


	@Override
	public void addMatrix2Cur(float[] matrix) {
		// TODO Auto-generated method stub
		Matrix.multiplyMM(currMatrix, 0, matrix, 0, currMatrix, 0);
	}

	@Override
	public void addMatrix2Org(float[] matrix) {
		// TODO Auto-generated method stub
		Matrix.multiplyMM(currMatrix, 0, matrix, 0, originalMatrix, 0);
	}
	
	
	
}
