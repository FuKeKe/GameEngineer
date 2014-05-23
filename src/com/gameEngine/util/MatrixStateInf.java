package com.gameEngine.util;

import java.nio.FloatBuffer;

public interface MatrixStateInf {
	public void initMatrix();//获取不变换初始矩阵
    
    public void setCameraLocation(
            float cx, float cy, float cz,           //摄影机位置
            float tx, float ty, float tz,           //目标位置
            float upx, float upy, float upz         //摄影机 up 方向
    );
    
    public void setCameraLocation(float[] camera);
    
    public void setProjectOrtho(             //设置正交投影
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
    
    public void pushMatrix();//保护变换矩阵
    
    public void popMatrix();//恢复变换矩阵
    
    public void translate(float x,float y,float z);//设置沿xyz轴移动
    
    public void rotate(float angle,float x,float y,float z);//设置绕xyz轴移动
    
    //获取具体物体的变换矩阵
    public float[] getMMatrix();
    
    public void setLightLocation(float x, float y, float z);

	public FloatBuffer getLightLocation();

	public FloatBuffer getCameraLocation();

	public float[] getmVMatrix();

	public void setmVMatrix(float[] mVMatrix);
	
	/**
	 * 添加一个变换矩阵到curMatrix（当前变换矩阵）中
	 * @param matrix
	 */
	public void addMatrix2Cur(float[] matrix);
	/**
	 * 添加一个变换矩阵与原始矩阵计算，再将生成的矩阵放到curMatrix中
	 * @param matrix
	 */
	public void addMatrix2Org(float[] matrix);
}
