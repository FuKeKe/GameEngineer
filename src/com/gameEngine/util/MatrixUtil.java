package com.gameEngine.util;


public class MatrixUtil {
	/**
	 * 矩阵转置
	 * @param input
	 * @return
	 */
	public static float[] matrixToMatrixT(float[] input){
		float[] output = new float[input.length];
		int lie = 0;
		int hang = 0;
		for(int i = 0; i < input.length; i++){
			lie = i/4;
			hang = i%4;
			output[hang*4+lie] = input[i];
		}
		return output;
	}
	/**
	 * 线性插值动画的矩阵
	 * @param startMatrix
	 * @param endMatrix
	 * @param alpha
	 * @return
	 */
	public static float[] interpolationMatrix(float[] startMatrix, float[] endMatrix , float alpha){
		float[] result = new float[startMatrix.length];
		for (int i = 0; i < startMatrix.length; i++) {
			result[i] = startMatrix[i] * alpha + endMatrix[i] * (1-alpha);
		}
		return result;
	}
	
	public static float[] cutOutMatrixFromA2B (String[] input,int a,int b){
		
		if(b<a){return null;}
		
		int length = b-a+1;
		float[] matrix = new float[length];
		for (int i = 0; i < length; i++) {
			matrix[i] = Float.parseFloat(input[a+i]);
		}
		return matrix;
	}
	
	/**
	 * 行列式
	 * @param a		行
	 * @param b		列
	 * @return
	 */
	public static float[] getInitMatrix(int a, int b){
		
		float[] matrix = new float[a*b];
		
		for(int i = 0; i < a*b; i++){
			matrix[i] = 1;
		}
		
		return matrix;
	}
	
}
