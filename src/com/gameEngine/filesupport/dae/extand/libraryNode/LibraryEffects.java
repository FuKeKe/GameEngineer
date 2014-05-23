package com.gameEngine.filesupport.dae.extand.libraryNode;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.gameEngine.filesupport.dae.util.XMLLoadValueUtil;

public class LibraryEffects implements LibraryTemplateInf{
	public HashMap<String, Effect> effects = new HashMap<String, LibraryEffects.Effect>();
	public class Effect{
		public String id;
		public String name;
		public float[] emission;				//自发光
		public float[] ambient;					//环境光
		public float[] diffuse;					//散射光
		public float[] specular;				//镜面光
		public String diffuseTexture;			//散射光纹理id
		public float shininess;					//粗糙强度/光滑度
		public float[] reflective;				//反射光强度
		public float reflectivity;				//反射率
		public float[] transparent;				//透明颜色比例
		public float transparency;				//透明度
	}
	@Override
	public XmlPullParser loadContentByParser(XmlPullParser parser) {
		// TODO Auto-generated method stub
		Effect effect = null;
		try {
			int event = XMLLoadValueUtil.safeNext(parser);
			while(!(event == XmlPullParser.END_TAG && "library_effects".equals(parser.getName()))){
				switch(event){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if("effect".equals(parser.getName())){
						effect = new Effect();
						HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
						effect.id = map.get("id");
						effect.name = map.get("name");
					}
					
					if("emission".equals(parser.getName())){
						//注意 parser next 返回的可能是null，在节点与节点之间会有空节点，请使用安全safeNext()函数
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("color".equals(parser.getName())){
								effect.emission = XMLLoadValueUtil.loadFloatArrayByParser(parser);
							}
						}
					}
					if("ambient".equals(parser.getName())){
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("color".equals(parser.getName())){
								effect.ambient = XMLLoadValueUtil.loadFloatArrayByParser(parser);
							}
						}
					}
					if("diffuse".equals(parser.getName())){
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("color".equals(parser.getName())){
								effect.diffuse = XMLLoadValueUtil.loadFloatArrayByParser(parser);
							}
							if("texture".equals(parser.getName())){
								HashMap<String, String> map = XMLLoadValueUtil.getValuesMap(parser);
								effect.diffuseTexture = map.get("texture");
							}
						}
					}
					if("specular".equals(parser.getName())){
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("color".equals(parser.getName())){
								effect.specular = XMLLoadValueUtil.loadFloatArrayByParser(parser);
							}
						}
					}
					if("shininess".equals(parser.getName())){
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("float".equals(parser.getName())){
								effect.shininess = Float.parseFloat(parser.nextText());
							}
						}
					}
					if("reflective".equals(parser.getName())){
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("color".equals(parser.getName())){
								effect.reflective = XMLLoadValueUtil.loadFloatArrayByParser(parser);
							}
						}
					}
					if("reflectivity".equals(parser.getName())){
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("float".equals(parser.getName())){
								effect.reflectivity = Float.parseFloat(parser.nextText());
							}
						}
					}
					if("transparent".equals(parser.getName())){
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("color".equals(parser.getName())){
								effect.transparent = XMLLoadValueUtil.loadFloatArrayByParser(parser);
							}
						}
					}
					if("transparency".equals(parser.getName())){
						if(XMLLoadValueUtil.safeNext(parser)!= XmlPullParser.END_TAG){
							if("float".equals(parser.getName())){
								effect.transparency = Float.parseFloat(parser.nextText());
							}
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if("effect".equals(parser.getName())){
						effects.put(effect.id, effect);
						effect = null;
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
		Log.i("library_effects"+"加载完成",effects.size()+"个");
		return parser;
	}

}
