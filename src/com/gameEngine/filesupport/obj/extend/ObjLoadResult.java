package com.gameEngine.filesupport.obj.extend;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ObjLoadResult {
	private int iCount;
	private FloatBuffer fVerticesBuffer;
	private FloatBuffer fNormalBuffer;
	private FloatBuffer fTextureBuffer;
	
	public FloatBuffer getfVerticesBuffer() {
		return fVerticesBuffer;
	}
	
	public void setfVerticesBuffer(FloatBuffer fVerticesBuffer,int count) {
		this.fVerticesBuffer = fVerticesBuffer;
		this.iCount = count;
	}
	
	public void setfVerticesBuffer(float[] fVertices){
		iCount = fVertices.length/3;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(fVertices.length*4);
		byteBuffer.order(ByteOrder.nativeOrder());
		this.fVerticesBuffer = byteBuffer.asFloatBuffer();
		this.fVerticesBuffer.put(fVertices);
		this.fVerticesBuffer.position(0);
	}
	
	public FloatBuffer getfNormalBuffer() {
		return fNormalBuffer;
	}
	
	public void setfNormalBuffer(FloatBuffer fNormalBuffer) {
		this.fNormalBuffer = fNormalBuffer;
	}
	
	public void setfNormalBuffer(float[] fNormal){
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(fNormal.length*4);
		byteBuffer.order(ByteOrder.nativeOrder());
		this.fNormalBuffer = byteBuffer.asFloatBuffer();
		this.fNormalBuffer.put(fNormal);
		this.fNormalBuffer.position(0);
	}
	
	public FloatBuffer getfTextureBuffer() {
		return fTextureBuffer;
	}
	
	public void setfTextureBuffer(FloatBuffer fTextureBuffer) {
		this.fTextureBuffer = fTextureBuffer;
	}
	
	public void setfTextureBuffer(float[] fTexture) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(fTexture.length*4);
		byteBuffer.order(ByteOrder.nativeOrder());
		this.fTextureBuffer = byteBuffer.asFloatBuffer();
		this.fTextureBuffer.put(fTexture);
		this.fTextureBuffer.position(0);
	}

	public int getiCount() {
		return iCount;
	}

	public void setiCount(int iCount) {
		this.iCount = iCount;
	}
	
}
