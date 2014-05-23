package com.gameEngine.filesupport.dae.extand.libraryNode;

import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

import com.gameEngine.filesupport.dae.extand.LoadedDAE;
import com.gameEngine.filesupport.dae.util.XMLLoadValueUtil;

public class LibraryGeometries implements LibraryTemplateInf{
	public HashMap<String,Geometry> geometries = new HashMap<String,LibraryGeometries.Geometry>();			//������Դ
	public class Geometry{
		public String id;
		public String name;
		public ArrayList<Triangle> triangles;
	}
	public class Triangle{
		public float[] vertices;																		//������Ϣ
		public float[] normals;																			//��������Ϣ
		public float[] textures;																		//��ͼ��Ϣ
		public String materialId;																		//����ID
	}
	public class Source{
		public String id;
		public String float_array_id;
		public int float_array_count;
		public float[] floatArray;
		public String accessor;																			//�������ͣ�XYZ��YXZ��ZYX
	}
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		HashMap<String, Source> sources = null;
		// TODO Auto-generated method stub
		Geometry geometry = null;
		Source source = null;
		Triangle triangle = null;
		int event = XMLLoadValueUtil.safeNext(parser);
		StringBuffer strBuffer = new StringBuffer();
		while(!(event == XmlPullParser.END_TAG && "library_geometries".equals(parser.getName()))){
			switch(event){
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if("geometry".equals(parser.getName())){
					geometry = new Geometry();
					geometry.triangles = new ArrayList<LibraryGeometries.Triangle>();
					sources = new HashMap<String, LibraryGeometries.Source>();
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					geometry.id = map.get("id");
					geometry.name = map.get("name");
				}
				
				if("source".equals(parser.getName())){
					source = new Source();
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					source.id = map.get("id");
				}
				if("float_array".equals(parser.getName())){
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					source.float_array_id = map.get("id");
					source.float_array_count = Integer.parseInt(map.get("count"));
					source.floatArray = XMLLoadValueUtil.loadFloatArrayByParser(parser);
					
				}
				if("param".equals(parser.getName())){
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					strBuffer.append(map.get("name"));
				}
				if("vertices".equals(parser.getName())){
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					String id = map.get("id");
					XMLLoadValueUtil.safeNext(parser);
					if("input".equals(parser.getName())){
						map = XMLLoadValueUtil.getValuesMap(parser);
						sources.put(id, sources.get(XMLLoadValueUtil.takeOutFirstChar(map.get("source"))));
					}
				}
				if("triangles".equals(parser.getName())){
					triangle = new Triangle();
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					triangle.materialId = XMLLoadValueUtil.takeOutFirstChar(map.get("material"));
					convertTrangleToVNT(triangle, sources, parser);
					geometry.triangles.add(triangle);
				}
				break;
			case XmlPullParser.END_TAG:
				if("accessor".equals(parser.getName())){
					source.accessor = strBuffer.toString();	//�����ɵ� ������ʽ���� �磺 XYZ YXZ 
					strBuffer.setLength(0);					//���buffer
				}
				if("source".equals(parser.getName())){
					sources.put(source.id, source);
				}
				if("geometry".equals(parser.getName())){
					geometries.put(geometry.id,geometry);
				}
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			}
			event = XMLLoadValueUtil.safeNext(parser);
		}
		Log.i("library_geometries"+"�������",geometries.size()+"��");
		return parser;
	}
	/**
	 * ��float����ת���� �� ������ ����
	 * @param parser
	 */
	public void convertTrangleToVNT(Triangle triangle, HashMap<String, Source> sources, XmlPullParser parser){
		ArrayList<Input> inputs = new ArrayList<LibraryGeometries.Input>();		//��ʱ�洢����
		float[] nodes = null;													//p�ڵ��µ�����
		int event = XMLLoadValueUtil.safeNext(parser);
		while(!(event == XmlPullParser.END_TAG && "triangles".equals(parser.getName()))){
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if("input".equals(parser.getName())){
					HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
					String semantic = map.get("semantic");
					int offset = Integer.parseInt(map.get("offset"));
					String source = XMLLoadValueUtil.takeOutFirstChar(map.get("source"));
					inputs.add(new Input(semantic, offset, source));
				}
				if("p".equals(parser.getName())){
					nodes = XMLLoadValueUtil.loadFloatArrayByParser(parser);
					//Log.e("node ����", ""+nodes.length);
					int step = inputs.size();						//ǰ���Ĳ���
					int lenght = nodes.length/inputs.size();		//�ۺϲ�����Ķ�ȡ
					for(int i = 0; i < lenght; i++){				//������ȡ�㡢������������
						for(int s = 0; s < step; s++){
							Input tempInput = inputs.get(s);
							Source source = sources.get(tempInput.source);
							int p = (int) nodes[i*step + tempInput.offset];				//����ǵڼ����ڵ㣺
							if("XYZ".equals(source.accessor.toUpperCase())){
								tempInput.contentList.add(source.floatArray[p*3]);
								tempInput.contentList.add(source.floatArray[p*3+1]);
								tempInput.contentList.add(source.floatArray[p*3+2]);
							}
							if("YXZ".equals(source.accessor.toUpperCase())){
								tempInput.contentList.add(source.floatArray[p*3+1]);
								tempInput.contentList.add(source.floatArray[p*3]);
								tempInput.contentList.add(source.floatArray[p*3+2]);
							}
							//ʡ�Բ����õ�����
							if("ST".equals(source.accessor.toUpperCase())){
								tempInput.contentList.add(source.floatArray[p*2]);
								tempInput.contentList.add(source.floatArray[p*2+1]);
							}
							if("TS".equals(source.accessor.toUpperCase())){
								tempInput.contentList.add(source.floatArray[p*2+1]);
								tempInput.contentList.add(source.floatArray[p*2]);
							}
						}
					}
					for(Input input : inputs){
						//�Ե��������ͽ��з���
						if("VERTEX".equals(input.semantic.toUpperCase())){
							int length = input.contentList.size();
							triangle.vertices = new float[length];
							for (int i = 0; i < length; i++) {
								triangle.vertices[i] = input.contentList.get(i);
								if(triangle.vertices[i] > LoadedDAE.maxDistance){				//������������
									LoadedDAE.maxDistance = triangle.vertices[i];				//�ҵ�������������
								}
							}
						}
						if("NORMAL".equals(input.semantic.toUpperCase())){
							int length = input.contentList.size();
							triangle.normals = new float[length];
							for (int i = 0; i < length; i++) {
								triangle.normals[i] = input.contentList.get(i);
							}
						}
						if("TEXCOORD".equals(input.semantic.toUpperCase())){
							int length = input.contentList.size();
							triangle.textures = new float[length];
							for (int i = 0; i < length; i++) {
								triangle.textures[i] = input.contentList.get(i);
							}
						}
						Log.i("���ɵĵ㳤��", ""+triangle.vertices.length);
					}
				}
				break;
			case XmlPullParser.END_TAG:
				//����p�µ�����
				
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			}
			event = XMLLoadValueUtil.safeNext(parser);
		}
	}
	/**
	 * ���� ��Ӧ�� ���� ���� ���� ����
	 * @author keke
	 *
	 */
	public class Input{
		public String semantic;
		public int offset;
		public String source;
		public ArrayList<Float> contentList;
		public Input(String semantic, int offset, String source){
			this.semantic = semantic;
			this.offset = offset;
			this.source = source;
			contentList = new ArrayList<Float>();
		}
	}
}
