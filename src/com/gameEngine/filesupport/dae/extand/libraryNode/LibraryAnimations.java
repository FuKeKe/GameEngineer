package com.gameEngine.filesupport.dae.extand.libraryNode;

import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

import com.gameEngine.filesupport.dae.util.XMLLoadValueUtil;

public class LibraryAnimations implements LibraryTemplateInf{
	public HashMap<String , Animation> animations = new HashMap<String, LibraryAnimations.Animation>();
	public class Animation{
		public String id;
		public String name;
		public HashMap<String , Source> sources = new HashMap<String, LibraryAnimations.Source>();//INPUT:�����ʱ�� ���룩 OUTPUT:���� INTERPOLATION: ��ֵ��ʽ
		public int keyLength;
		public String targetId;					//�任����
		public String targetType;				//�任����
	}
	public class Source{
		public String id;
		public String[] array;
		public String accessor;					//��������
	}
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		Animation animation  = null;
		Source source = null;
		HashMap<String , Source> tempSources = null;
		int event = XMLLoadValueUtil.safeNext(parser);
		while (!(event == XmlPullParser.END_TAG
				&& "library_animations".equals(parser.getName()))) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if ("animation".equals(parser.getName())) {
					if(parser.getAttributeCount()>0){
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						animation = new Animation();
						animation.id = map.get("id");
						animation.name = map.get("name");
						tempSources = new HashMap<String, LibraryAnimations.Source>();
					}
				}

				if ("source".equals(parser.getName())) {
					source = new Source();
					HashMap<String, String> map = XMLLoadValueUtil
							.getValuesMap(parser);
					source.id = map.get("id");
					XMLLoadValueUtil.safeNext(parser);
					
					if ("float_array".equals(parser.getName())) {
						source.array = XMLLoadValueUtil.loadStringArrayByParser(parser);
					}
					if ("Name_array".equals(parser.getName())) {
						source.array = XMLLoadValueUtil.loadStringArrayByParser(parser);
						animation.keyLength = source.array.length;
					}
					
				}
				if ("accessor".equals(parser.getName())) {
					XMLLoadValueUtil.safeNext(parser);
					if("param".equals(parser.getName())){
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						source.accessor = map.get("type");
					}
				}
				if ("sampler".equals(parser.getName())) {
					XMLLoadValueUtil.safeNext(parser);
					while("input".equals(parser.getName())){
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						animation.sources.put(map.get("semantic"), tempSources.get(XMLLoadValueUtil.takeOutFirstChar(map.get("source"))));		//���������ͺ�����Դƥ��
						XMLLoadValueUtil.safeNext(parser);
					}
				}
				if ("channel".equals(parser.getName())) {
					HashMap<String, String> map = XMLLoadValueUtil
							.getValuesMap(parser);
					String[] separate = map.get("target").split("/");
					if(separate.length>1){
						animation.targetId = separate[0];
						animation.targetType = separate[1];
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if ("source".equals(parser.getName())) {
					tempSources.put(source.id, source);
					source = null;
				}
				if ("sampler".equals(parser.getName())) {
					tempSources = null;		//�����ʱ����
				}
				if ("animation".equals(parser.getName())) {
					if(animation != null){
						animations.put(animation.id, animation);
						animation = null;
					}
				}
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			}
			event = XMLLoadValueUtil.safeNext(parser);
		}
		Log.i("library_animations"+"�������",animations.size()+"��");
		return parser;
	}

}
