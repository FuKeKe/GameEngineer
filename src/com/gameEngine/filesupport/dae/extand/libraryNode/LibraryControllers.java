package com.gameEngine.filesupport.dae.extand.libraryNode;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.util.Log;

import com.gameEngine.filesupport.dae.util.XMLLoadValueUtil;

public class LibraryControllers implements LibraryTemplateInf {
	public HashMap<String, Controller> controllers = new HashMap<String, LibraryControllers.Controller>();
	
	public class Controller {
		String id;
		public float[] bind_shape_matrix;
		public String jointId;
		public String matrixId;
		public String weightId;
		public HashMap<String, Source> sources;
		public int[] vcount;
		public int[] v;
	}

	public class Source {
		public String id;
		public String[] array;						//未知类型的数据
		public String accessor;						//array的数据类型
	}

	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			HashMap<String, Source> sources = null;
			// TODO Auto-generated method stub
			Controller controller = null;
			Source source = null;
			int event = XMLLoadValueUtil.safeNext(parser);
			while (!(event == XmlPullParser.END_TAG
					&& "library_controllers".equals(parser.getName()))) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("controller".equals(parser.getName())) {
						controller = new Controller();
						sources = new HashMap<String, Source>();
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						controller.id = map.get("id");
					}

					if ("bind_shape_matrix".equals(parser.getName())) {
						controller.bind_shape_matrix = XMLLoadValueUtil.loadFloatArrayByParser(parser);
					}
					if ("source".equals(parser.getName())) {
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						source = new Source();
						source.id = map.get("id");

					}
					if ("Name_array".equals(parser.getName())) {
						String temp = parser.nextText();
						source.array = (temp.trim()).split("[ ]+");
					}
					if ("float_array".equals(parser.getName())) {
						String temp = parser.nextText();
						source.array = (temp.trim()).split("[ ]+");
					}
					if ("accessor".equals(parser.getName())) {
						XMLLoadValueUtil.safeNext(parser);
						if("param".equals(parser.getName())){
							HashMap<String, String> map = XMLLoadValueUtil
									.getValuesMap(parser);
							source.accessor = map.get("type");
						}
					}
					if ("joints".equals(parser.getName())) {
						XMLLoadValueUtil.safeNext(parser);
						while("input".equals(parser.getName())){
							HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
							connectTypeAndSource(controller,map.get("semantic"),map.get("source"));
							XMLLoadValueUtil.safeNext(parser);
						}
					}
					if ("vertex_weights".equals(parser.getName())) {
						XMLLoadValueUtil.safeNext(parser);
						while("input".equals(parser.getName())){
							HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
							connectTypeAndSource(controller,map.get("semantic"),map.get("source"));
							XMLLoadValueUtil.safeNext(parser);
						}
						if("vcount".equals(parser.getName())){
							controller.vcount = XMLLoadValueUtil.loadIntArrayByParser(parser);
						}if("v".equals(parser.getName())){
							controller.v = XMLLoadValueUtil.loadIntArrayByParser(parser);
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("controller".equals(parser.getName())) {
						controller.sources = sources;
						controllers.put(controller.id, controller);
					}
					if ("source".equals(parser.getName())) {
						sources.put(source.id, source);
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
		Log.i("library_controllers"+"加载完成",controllers.size()+"个");
		return parser;
	}
	@SuppressLint("DefaultLocale")
	public void connectTypeAndSource(Controller controller, String type,String sourceId){
		String temp = type.toUpperCase();
		if("JOINT".equals(temp)){
			controller.jointId = sourceId;
			return;
		}
		if("WEIGHT".equals(temp)){
			controller.weightId = sourceId;
			return;
		}
		if("INV_BIND_MATRIX".equals(temp)){
			controller.matrixId = sourceId;
			return;
		}
	}
}
