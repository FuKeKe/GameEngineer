package com.gameEngine.dae.extand.libraryNode;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.gameEngine.dae.util.XMLLoadValueUtil;

/**
 * 图片信息
 * @author keke
 *
 */
public class LibraryImages implements LibraryTemplateInf{
	public HashMap<String, Image> images = new HashMap<String, LibraryImages.Image>();
	public class Image{
		public String id;
		public String name;
		public String init_from;				//图片的绝对地址
	}
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		// TODO Auto-generated method stub
		Image imageTemp = null;
		try {
			int event = XMLLoadValueUtil.safeNext(parser);
			while(!(event == XmlPullParser.END_TAG && "library_images".equals(parser.getName()))){
				switch(event){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if("image".equals(parser.getName())){
						imageTemp = new Image();
						HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
						imageTemp.id = map.get("id");
						imageTemp.name = map.get("name");
					}
					
					if("init_from".equals(parser.getName())){
						imageTemp.init_from = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if("image".equals(parser.getName())){
						images.put(imageTemp.id, imageTemp);
						imageTemp = null;
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
		Log.i("library_images"+"加载完成",images.size()+"个");
		return parser;
	}
}
