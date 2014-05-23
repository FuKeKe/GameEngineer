package com.gameEngine.dae.extand.libraryNode;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.gameEngine.dae.util.XMLLoadValueUtil;

/**
 * collada dae 文件的创建信息
 * @author keke
 *
 */
public class LibraryAsset implements LibraryTemplateInf{
	public class Contributor{
		public String author;
		public String author_tool;
		public String comment;
	}
	public Contributor contributor;
	public String created;
	public String keywords;
	public String modified;
	public String revision;
	public String subject;
	public String title;
	public class Unit{
		public String meter;			//单位长度
		public String name;				//单位名称
	}
	public Unit unit;
	public String up_axis;
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			int event = XMLLoadValueUtil.safeNext(parser);
			while(!(event == XmlPullParser.END_TAG
					&& "asset".equals(parser.getName()))
					){
				switch(event){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if("contributor".equals(parser.getName())){
						contributor = new Contributor();
					}
					if(contributor!=null){
						if("author".equals(parser.getName())){
							contributor.author = parser.nextText();
						}
						if("author_tool".equals(parser.getName())){
							contributor.author_tool = parser.nextText();
						}
						if("authoring_tool".equals(parser.getName())){
							contributor.author_tool = parser.nextText();
						}
						if("comment".equals(parser.getName())){
							contributor.comment = parser.nextText();
						}
					}
					if("created".equals(parser.getName())){
						created = parser.nextText();
					}
					if("keywords".equals(parser.getName())){
						keywords = parser.nextText();
					}
					if("modified".equals(parser.getName())){
						modified = parser.nextText();
					}
					if("revision".equals(parser.getName())){
						revision = parser.nextText();
					}
					if("subject".equals(parser.getName())){
						subject = parser.nextText();
					}
					if("title".equals(parser.getName())){
						title = parser.nextText();
					}
					if("unit".equals(parser.getName())){
						unit = new Unit();
						unit.meter = parser.getAttributeValue(0);
						unit.name = parser.getAttributeValue(1);
					}
					if("up_axis".equals(parser.getName())){
						up_axis = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
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
		Log.i("asset"+"加载完成",""+contributor.author_tool);
		return parser;
	}
	
}
