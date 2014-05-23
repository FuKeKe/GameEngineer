package com.gameEngine.dae.extand.libraryNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.gameEngine.dae.util.XMLLoadValueUtil;

public class LibraryVisualScenes implements LibraryTemplateInf{
	public HashMap<String,VisualScene> visualScenes = new HashMap<String, LibraryVisualScenes.VisualScene>();//所有的Node节点
	public HashMap<String,Node> nodes = new HashMap<String, LibraryVisualScenes.Node>();//所有的Node节点
	public class VisualScene{
		public String id;
		public String name;
		public ArrayList<String> nodesId = new ArrayList<String>();//所有的Node节点
		public float frame_rate;
		public float start_time;
		public float end_time;
	}
	
	public class Node{
		public String name;
		public String id;
		public String sid; 
		public String type;
		public String layer;
		public float[] matrix;
		public float visibility;
		public String controllerId;
		public String geometryId;
		public String materialId;
		public String lightId;
		public ArrayList<String> childNodes = new ArrayList<String>();					//Node的子节点ID
	}
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			Stack<Node> stack = new Stack<Node>();
			VisualScene visualScene = null;
			Node node = null;
			int event = XMLLoadValueUtil.safeNext(parser);
			while (!(event == XmlPullParser.END_TAG
					&& "library_visual_scenes".equals(parser.getName()))) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if("visual_scene".equals(parser.getName())){
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						if(null != node){			//保存上个父节点
							stack.push(node);
						}
						visualScene = new VisualScene();
						visualScene.name = map.get("name");
						visualScene.id = map.get("id");
					}
					if ("node".equals(parser.getName())) {
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						if(null != node){			//保存上个父节点
							stack.push(node);
						}
						node = new Node();
						node.name = map.get("name");
						node.id = map.get("id");
						node.sid = map.get("sid");
						node.layer = map.get("layer");
					}

					if ("matrix".equals(parser.getName())) {
						node.matrix = XMLLoadValueUtil.loadFloatArrayByParser(parser);
					}
					if ("visibility".equals(parser.getName())) {
						node.visibility = Float.parseFloat(parser.nextText());

					}
					if ("instance_geometry".equals(parser.getName())) {
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						node.geometryId = XMLLoadValueUtil.takeOutFirstChar(map.get("url"));
					}
					if ("instance_controller".equals(parser.getName())) {
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						node.controllerId = XMLLoadValueUtil.takeOutFirstChar(map.get("url"));
					}
					if ("instance_material".equals(parser.getName())) {
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						node.materialId = XMLLoadValueUtil.takeOutFirstChar(map.get("target"));
					}
					if ("instance_light".equals(parser.getName())) {
						HashMap<String, String> map = XMLLoadValueUtil
								.getValuesMap(parser);
						node.lightId = XMLLoadValueUtil.takeOutFirstChar(map.get("url"));
					}
					if ("frame_rate".equals(parser.getName())) {
						visualScene.frame_rate = XMLLoadValueUtil.loadFloatByParser(parser);
					}
					if ("start_time".equals(parser.getName())) {
						visualScene.start_time = XMLLoadValueUtil.loadFloatByParser(parser);
					}
					if ("end_time".equals(parser.getName())) {
						visualScene.end_time = XMLLoadValueUtil.loadFloatByParser(parser);
					}
					break;
				case XmlPullParser.END_TAG:
					if ("node".equals(parser.getName())) {
						if(stack.isEmpty()){
							visualScene.nodesId.add(node.id);
							nodes.put(node.id,node);
							node = null;
						}else{
							Node father = stack.pop();
							father.childNodes.add(node.id);
							visualScene.nodesId.add(node.id);
							nodes.put(node.id,node);
							node = father;
						}
					}
					if("visual_scene".equals(parser.getName())){
						visualScenes.put(visualScene.id, visualScene);
						visualScene = null;
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
		Log.i("library_visual_scenes"+"加载完成",visualScenes.size()+"个");
		return parser;
	}

}
