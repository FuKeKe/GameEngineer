package com.gameEngine.filesupport.dae.extand.libraryNode;

import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

import com.gameEngine.filesupport.dae.util.XMLLoadValueUtil;

public class LibraryScene implements LibraryTemplateInf{
	public String instance_visual_scene;
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		int event = XMLLoadValueUtil.safeNext(parser);
		while(!(event == XmlPullParser.END_TAG && "scene".equals(parser.getName()))){
			switch(event){
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if("instance_visual_scene".equals(parser.getName())){
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					instance_visual_scene = XMLLoadValueUtil.takeOutFirstChar(map.get("url"));
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			}
			event = XMLLoadValueUtil.safeNext(parser);
		}
		Log.i("scene"+"º”‘ÿÕÍ≥…",""+instance_visual_scene);
		return parser;
	}

}
