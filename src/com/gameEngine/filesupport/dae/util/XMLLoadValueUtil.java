package com.gameEngine.filesupport.dae.util;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XMLLoadValueUtil {
	/**
	 * ���ݽڵ� ��attribute ������ ��� ����ֵ
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
	 * ��ȡ text ����ת����float����
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
	 * ȥ�س� �� �ո� ���� float ����
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
			//Log.i("��ȡ��text�����ǣ�",""+line.length());
			String[] szStrings = line.split("[ ]+");
			int length = szStrings.length;
			float[] re = new float[length];
			for(int i = 0; i < length; i++){
				re[i] = Float.parseFloat(szStrings[i]);
			}
			//Log.i("���������С��",""+re.length);
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
	 * ȥ�س� �� �ո� ���� int ����
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
			//Log.i("��ȡ��text�����ǣ�",""+line.length());
			String[] szStrings = line.split("[ ]+");
			int length = szStrings.length;
			int[] re = new int[length];
			for(int i = 0; i < length; i++){
				re[i] = Integer.parseInt(szStrings[i]);
			}
			//Log.i("���������С��",""+re.length);
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
	 * ȥ�س� �� �ո� ���� int ����
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
			//Log.i("��ȡ��text�����ǣ�",""+line.length());
			String[] szStrings = line.split("[ ]+");
			//Log.i("���������С��",""+re.length);
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
	 * ȥ����һ�ַ�
	 * @param input
	 * @return
	 */
	public static String takeOutFirstChar(String input){
		if(input.charAt(0) == '#'){
			String re = input.substring(1, input.length());
			//System.out.println("���к���ID��"+re);
			return re;
		}									//��#��ͷ���и���Ǿ�ֱ�ӷ���
		return input;
	}
	/**
	 * ���Ҫ��ȡ����һ���ڵ���һ���µ��У���ôԭparser.next ���ȷ���һ��null��
	 * Ϊ��ֹ������������Ӧ�ö�ȡnextֱ���ҵ���һ���ڵ�Ϊֹ
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
