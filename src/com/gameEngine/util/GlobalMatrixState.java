package com.gameEngine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.Matrix;

/**
 * 控制全局变化
 * @author keke
 *
 */
public class GlobalMatrixState{
	public static float[] mVMatrix  	 = new float[16];
	public static float[] mProjMatrix 	 = new float[16];
    public static float[] currMatrix	 = new float[16];
	//摄像机位置
    private static float[] fCameraLocation = new float[]{0,0,0};
    private static FloatBuffer cameraLocationBuffer;
    
    //光源位置
    private static float[] fLightLocation = new float[]{0,0,0};
    private static FloatBuffer lightPositionBuffer;
    
    public GlobalMatrixState(){
    	initMatrix();
    }
    
    public static void initMatrix()//获取不变换初始矩阵
    {
    	Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);//初始化矩阵
    	
    	ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(3*4);
        byteBuffer1.order(ByteOrder.nativeOrder());
        cameraLocationBuffer = byteBuffer1.asFloatBuffer();
        cameraLocationBuffer.put(fCameraLocation);
        cameraLocationBuffer.flip();
        
        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(3*4);
        byteBuffer2.order(ByteOrder.nativeOrder());
        lightPositionBuffer = byteBuffer2.asFloatBuffer();
        lightPositionBuffer.put(fLightLocation);
        lightPositionBuffer.flip();
    }
    
    public static void setCameraLocation(
            float cx, float cy, float cz,           //摄影机位置
            float tx, float ty, float tz,           //目标位置
            float upx, float upy, float upz         //摄影机 up 方向
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
    
    public static void setCameraLocation(float[] camera)
    {
        Matrix.setLookAtM(mVMatrix,0,camera[0],camera[1],camera[2],camera[3],camera[4],camera[5],camera[6],camera[7],camera[8]);
        fCameraLocation[0] = camera[0]; fCameraLocation[1] = camera[1]; fCameraLocation[2] = camera[2];
        
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        
        cameraLocationBuffer = byteBuffer.asFloatBuffer();
        cameraLocationBuffer.put(fCameraLocation);
        cameraLocationBuffer.flip();
    }
    public static FloatBuffer getCameraLocation(){
    	return cameraLocationBuffer;
    }
    public static void setProjectOrtho(             //设置正交投影
            float left, float right,
            float bottom, float top,
            float near, float far
    )
    {
        Matrix.orthoM(mProjMatrix,0,left,right,bottom,top,near,far);
    }
    
    public static  void setProjectFrustum(
    		float left, float right,
    		float bottom, float top,
    		float near, float far
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static void setLightLocation(float x, float y, float z) 
    {
    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3*4);
    	fLightLocation[0] = x; fLightLocation[1] = y; fLightLocation[2] = z;
    	
    	byteBuffer.order(ByteOrder.nativeOrder());
    	lightPositionBuffer = byteBuffer.asFloatBuffer();
    	lightPositionBuffer.put(fLightLocation);
    	lightPositionBuffer.flip();
	}

	public static FloatBuffer getLightLocation() {
		return lightPositionBuffer;
	}

    public static void translate(float x,float y,float z)//设置沿xyz轴移动
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)//设置绕xyz轴移动
    {
    	Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }
}
