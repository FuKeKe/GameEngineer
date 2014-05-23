package com.gameEngine.util;

public class TranslateUtil {
	public static float[] changeStringArrToFloatArr(String[] input){
		float[] re = new float[input.length];
		for (int i = 0; i < input.length; i++) {
			re[i] = Float.parseFloat(input[i]);
		}
		return re;
	}
}
