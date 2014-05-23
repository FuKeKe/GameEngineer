package com.gameEngine.util;

import java.nio.FloatBuffer;

public interface MatrixStateInf {
	public void initMatrix();//��ȡ���任��ʼ����
    
    public void setCameraLocation(
            float cx, float cy, float cz,           //��Ӱ��λ��
            float tx, float ty, float tz,           //Ŀ��λ��
            float upx, float upy, float upz         //��Ӱ�� up ����
    );
    
    public void setCameraLocation(float[] camera);
    
    public void setProjectOrtho(             //��������ͶӰ
            float left, float right,
            float bottom, float top,
            float near, float far
    );
    
    public void setProjectFrustum(
    		float left, float right,
    		float bottom, float top,
    		float near, float far
    );
    
    public float[] getFinalMatrix();
    
    public void pushMatrix();//�����任����
    
    public void popMatrix();//�ָ��任����
    
    public void translate(float x,float y,float z);//������xyz���ƶ�
    
    public void rotate(float angle,float x,float y,float z);//������xyz���ƶ�
    
    //��ȡ��������ı任����
    public float[] getMMatrix();
    
    public void setLightLocation(float x, float y, float z);

	public FloatBuffer getLightLocation();

	public FloatBuffer getCameraLocation();

	public float[] getmVMatrix();

	public void setmVMatrix(float[] mVMatrix);
	
	/**
	 * ���һ���任����curMatrix����ǰ�任������
	 * @param matrix
	 */
	public void addMatrix2Cur(float[] matrix);
	/**
	 * ���һ���任������ԭʼ������㣬�ٽ����ɵľ���ŵ�curMatrix��
	 * @param matrix
	 */
	public void addMatrix2Org(float[] matrix);
}
