package com.gameEngine.dae.util;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.gameEngine.dae.extand.LoadedDAEInfo;
import com.gameEngine.dae.extand.libraryNode.LibraryAnimations;
import com.gameEngine.dae.extand.libraryNode.LibraryAsset;
import com.gameEngine.dae.extand.libraryNode.LibraryCamera;
import com.gameEngine.dae.extand.libraryNode.LibraryControllers;
import com.gameEngine.dae.extand.libraryNode.LibraryEffects;
import com.gameEngine.dae.extand.libraryNode.LibraryGeometries;
import com.gameEngine.dae.extand.libraryNode.LibraryImages;
import com.gameEngine.dae.extand.libraryNode.LibraryLights;
import com.gameEngine.dae.extand.libraryNode.LibraryMaterials;
import com.gameEngine.dae.extand.libraryNode.LibraryScene;
import com.gameEngine.dae.extand.libraryNode.LibraryVisualScenes;

public class LoadDAEUtil {
	public static LoadedDAEInfo loadDAEFromAssets(String fname, Resources r){
		LoadedDAEInfo loadDaeInfo = new LoadedDAEInfo();
		loadDaeInfo.fileAddress = fname;
		try {
			InputStream input = r.getAssets().open(fname);
			
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);  
            XmlPullParser parser = factory.newPullParser();  
            parser.setInput(input, "UTF-8"); 
			int event = parser.getEventType();//������һ���¼� 
			while(event!= XmlResourceParser.END_DOCUMENT){
				switch(event){
				
				case XmlPullParser.START_DOCUMENT://�жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�  
	                
	                break;  
	            case XmlPullParser.START_TAG://�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�  
	                if("asset".equals(parser.getName())){					//�жϿ�ʼ��ǩԪ���Ƿ���asset 
	                	loadDaeInfo.libraryAsset = new LibraryAsset();
	                	loadDaeInfo.libraryAsset.loadContentByParser(parser);
	                }
	                if("library_images".equals(parser.getName())){
	                	loadDaeInfo.libraryImages = new LibraryImages();
	                	loadDaeInfo.libraryImages.loadContentByParser(parser);
	                }
	                if("library_materials".equals(parser.getName())){
	                	loadDaeInfo.libraryMaterials = new LibraryMaterials();
	                	loadDaeInfo.libraryMaterials.loadContentByParser(parser);
	                }
	                if("library_effects".equals(parser.getName())){
	                	loadDaeInfo.libraryEffects = new LibraryEffects();
	                	loadDaeInfo.libraryEffects.loadContentByParser(parser);
	                }
	                if("library_geometries".equals(parser.getName())){
	                	loadDaeInfo.libraryGeometries = new LibraryGeometries();
	                	loadDaeInfo.libraryGeometries.loadContentByParser(parser);
	                }
	                if("library_controllers".equals(parser.getName())){
	                	loadDaeInfo.libraryControllers = new LibraryControllers();
	                	loadDaeInfo.libraryControllers.loadContentByParser(parser);
	                }
	                if("library_animations".equals(parser.getName())){
	                	loadDaeInfo.libraryAnimations = new LibraryAnimations();
	                	loadDaeInfo.libraryAnimations.loadContentByParser(parser);
	                }
	                if("library_lights".equals(parser.getName())){
	                	loadDaeInfo.libraryLights = new LibraryLights();
	                	loadDaeInfo.libraryLights.loadContentByParser(parser);
	                }
	                if("library_cameras".equals(parser.getName())){
	                	loadDaeInfo.libraryCamera = new LibraryCamera();
	                	loadDaeInfo.libraryCamera.loadContentByParser(parser);
	                }
	                if("library_visual_scenes".equals(parser.getName())){
	                	loadDaeInfo.libraryVisualScenes = new LibraryVisualScenes();
	                	loadDaeInfo.libraryVisualScenes.loadContentByParser(parser);
	                }
	                if("scene".equals(parser.getName())){
	                	loadDaeInfo.libraryScene = new LibraryScene();
	                	loadDaeInfo.libraryScene.loadContentByParser(parser);
	                }
	                break;  
	            case XmlPullParser.END_TAG://�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�  
	                if("asset".equals(parser.getName())){//�жϽ�����ǩԪ���Ƿ���book  
	                    
	                }  
	                break;  
	            }
				event = parser.next();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("DAE�ļ��������", fname);
		return loadDaeInfo;
	}
}
