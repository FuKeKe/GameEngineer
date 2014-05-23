package com.gameEngine.filesupport.dae.extand.libraryNode;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.gameEngine.filesupport.dae.util.XMLLoadValueUtil;

public class LibraryLights implements LibraryTemplateInf{
	public HashMap<String , Light> lights = new HashMap<String, LibraryLights.Light>();
	public class Light{
		String id;
		String name;
		float[] point;
		float[] ambient;
		float[] specular;
		float[] diffuse;
		
		float intensity;
	}
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			Light light = null;
			int event = XMLLoadValueUtil.safeNext(parser);
			while (!(event == XmlPullParser.END_TAG
					&& "library_lights".equals(parser.getName()))) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("light".equals(parser.getName())) {
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						light = new Light();
						light.name = map.get("name");
						light.id = map.get("id");
					}

					if ("point".equals(parser.getName())) {
						XMLLoadValueUtil.safeNext(parser);
						if("color".equals(parser.getName())){
						light.point = XMLLoadValueUtil.loadFloatArrayByParser(parser);
						}
					}
					if ("ambient".equals(parser.getName())) {
						XMLLoadValueUtil.safeNext(parser);
						if("color".equals(parser.getName())){
						light.ambient = XMLLoadValueUtil.loadFloatArrayByParser(parser);
						}
					}
					if ("diffuse".equals(parser.getName())) {
						XMLLoadValueUtil.safeNext(parser);
						if("color".equals(parser.getName())){
						light.diffuse = XMLLoadValueUtil.loadFloatArrayByParser(parser);
						}
					}
					if ("specular".equals(parser.getName())) {
						XMLLoadValueUtil.safeNext(parser);
						if("color".equals(parser.getName())){
						light.specular = XMLLoadValueUtil.loadFloatArrayByParser(parser);
						}
					}
					if ("intensity".equals(parser.getName())) {
						light.intensity = Float.parseFloat(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if ("light".equals(parser.getName())) {
						lights.put(light.id, light);
						light = null;
					}
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				}
				event = XMLLoadValueUtil.safeNext(parser);
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("library_lights"+"加载完成",lights.size()+"个");
		return parser;
	}

}
