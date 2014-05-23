package com.gameEngine.filesupport.dae.extand.libraryNode;

import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

import com.gameEngine.filesupport.dae.util.XMLLoadValueUtil;

public class LibraryMaterials implements LibraryTemplateInf{
	public HashMap<String, Material> materials = new HashMap<String, LibraryMaterials.Material>();
	public class Material{
		public String id;
		public String name;
		public String instance_effect;
	}
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		Material material = null;
		int event = XMLLoadValueUtil.safeNext(parser);
		while(!(event == XmlPullParser.END_TAG && "library_materials".equals(parser.getName()))){
			switch(event){
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if("material".equals(parser.getName())){
					material = new Material();
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					material.id = map.get("id");
					material.name = map.get("name");
				}
				
				if("instance_effect".equals(parser.getName())){
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					material.instance_effect = XMLLoadValueUtil.takeOutFirstChar(map.get("url"));
				}
				break;
			case XmlPullParser.END_TAG:
				if("material".equals(parser.getName())){
					materials.put(material.id, material);
					material = null;
				}
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			}
			event = XMLLoadValueUtil.safeNext(parser);
		}
		Log.i("library_materials"+"加载完成",materials.size()+"个");
		return parser;
	}

}
