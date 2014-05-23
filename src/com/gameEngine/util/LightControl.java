package com.gameEngine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class LightControl {
	private static float[] lightAmbient = {0.3f,0.3f,0.29f,1.0f};
	private static float[] lightDiffuse = {0.7f,0.7f,0.69f,1.0f};
	private static float[] lightSpecular = {0.9f,0.9f,0.89f,1.0f};
	
	public static void resetLightStrength(){
		float[] lightAmbientT = {0.3f,0.3f,0.2f,1.0f};
		float[] lightDiffuseT = {0.7f,0.7f,0.6f,1.0f};
		float[] lightSpecularT = {1.0f,1.0f,0.8f,1.0f};
		setLightAmbient(lightAmbientT);
		setLightDiffuse(lightDiffuseT);
		setLightSpecular(lightSpecularT);
	}
	
	public static float[] getLightAmbient() {
		return lightAmbient;
	}
	
	public static FloatBuffer getLightAmbientBuffer() {
		ByteBuffer buffer = ByteBuffer.allocateDirect(lightAmbient.length*4);
		buffer.order(ByteOrder.nativeOrder());
		FloatBuffer lightAmbientBuffer = buffer.asFloatBuffer();
		lightAmbientBuffer.put(getLightAmbient());
		lightAmbientBuffer.position(0);
		return lightAmbientBuffer;
	}
	
	public static void setLightAmbient(float[] lightAmbient) {
		LightControl.lightAmbient = lightAmbient;
	}
	
	public static float[] getLightDiffuse() {
		return lightDiffuse;
	}
	
	public static FloatBuffer getLightDiffuseBuffer() {
		ByteBuffer buffer = ByteBuffer.allocateDirect(lightDiffuse.length*4);
		buffer.order(ByteOrder.nativeOrder());
		FloatBuffer lightDiffuseBuffer = buffer.asFloatBuffer();
		lightDiffuseBuffer.put(getLightDiffuse());
		lightDiffuseBuffer.position(0);
		return lightDiffuseBuffer;
	}
	
	public static void setLightDiffuse(float[] lightDiffuse) {
		LightControl.lightDiffuse = lightDiffuse;
	}
	
	public static float[] getLightSpecular() {
		return lightSpecular;
	}
	
	public static FloatBuffer getLightSpecularBuffer() {
		ByteBuffer buffer = ByteBuffer.allocateDirect(lightSpecular.length*4);
		buffer.order(ByteOrder.nativeOrder());
		FloatBuffer lightSpecularBuffer = buffer.asFloatBuffer();
		lightSpecularBuffer.put(getLightSpecular());
		lightSpecularBuffer.position(0);
		return lightSpecularBuffer;
	}
	
	public static void setLightSpecular(float[] lightSpecular) {
		LightControl.lightSpecular = lightSpecular;
	}
	
	
}
