package com.gameEngine.filesupport.dae.util;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XMLLoadValueUtil {
	/**
	 * 根据节点 的attribute 属性名 获得 属性值
	 * @param parser
	 * @return
	 */
	public static HashMap<String, String> getValuesMap(XmlPullParser parser){
		HashMap<String, String> map = new HashMap<String, String>();
		int length = parser.getAttributeCount();
		for(int i = 0; i < length; i++){
			map.put(parser.getAttributeName(i), parser.getAttributeValue(i));
		}
		return map;
	}
	/**
	 * 获取 text 数据转换成float类型
	 * @param parser
	 * @return
	 */
	public static float loadFloatByParser(XmlPullParser parser){
		String line;
		try {
			line = parser.nextText();
			line = line.trim();
			return Float.parseFloat(line);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Float) null;
	}
	/**
	 * 去回车 和 空格 返回 float 数组
	 * @param parser
	 * @return
	 */
	public static float[] loadFloatArrayByParser(XmlPullParser parser){
		String line;
		try {
			line = parser.nextText();
			line = line.trim();
			line = line.replace("\\r\\n", " ");
			line = line.replace("\n", " ");
			//Log.i("读取的text长度是：",""+line.length());
			String[] szStrings = line.split("[ ]+");
			int length = szStrings.length;
			float[] re = new float[length];
			for(int i = 0; i < length; i++){
				re[i] = Float.parseFloat(szStrings[i]);
			}
			//Log.i("生成数组大小：",""+re.length);
			return re;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 去回车 和 空格 返回 int 数组
	 * @param parser
	 * @return
	 */
	public static int[] loadIntArrayByParser(XmlPullParser parser){
		String line;
		try {
			line = parser.nextText();
			line = line.trim();
			line = line.replace("\\r\\n", " ");
			line = line.replace("\n", " ");
			//Log.i("读取的text长度是：",""+line.length());
			String[] szStrings = line.split("[ ]+");
			int length = szStrings.length;
			int[] re = new int[length];
			for(int i = 0; i < length; i++){
				re[i] = Integer.parseInt(szStrings[i]);
			}
			//Log.i("生成数组大小：",""+re.length);
			return re;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 去回车 和 空格 返回 int 数组
	 * @param parser
	 * @return
	 */
	public static String[] loadStringArrayByParser(XmlPullParser parser){
		String line;
		try {
			line = parser.nextText();
			line = line.trim();
			line = line.replace("\\r\\n", " ");
			line = line.replace("\n", " ");
			//Log.i("读取的text长度是：",""+line.length());
			String[] szStrings = line.split("[ ]+");
			//Log.i("生成数组大小：",""+re.length);
			return szStrings;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 去掉第一字符
	 * @param input
	 * @return
	 */
	public static String takeOutFirstChar(String input){
		if(input.charAt(0) == '#'){
			String re = input.substring(1, input.length());
			//System.out.println("剪切后听ID："+re);
			return re;
		}									//是#开头就切割。不是就直接返回
		return input;
	}
	/**
	 * 如果要读取的下一个节点在一个新的行，那么原parser.next 会先返回一个null。
	 * 为防止这个情况，我们应该读取next直到找到下一个节点为止
	 * @param parser
	 * @return
	 */
	public static int safeNext(XmlPullParser parser){
		try {
			parser.next();
			while(null == parser.getName()){
				parser.next();
			}
			return parser.getEventType();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1; 
	}
}
