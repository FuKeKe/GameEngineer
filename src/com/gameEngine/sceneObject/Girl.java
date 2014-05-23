package com.gameEngine.sceneObject;

import java.io.IOException;
import java.util.ArrayList;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.gameEngine.filesupport.obj.extend.LoadedObject;

public class Girl extends LoadedObject{
	String objsFile = "girls";
	ArrayList<String> alObjName = new ArrayList<String>();
	public Girl(GLSurfaceView mv) {
		super();
		try {
			String [] files = mv.getResources().getAssets().list(objsFile);
			Log.i("assert��girls�µ��ļ�������", ""+files.length);
			for (String string : files) {
				if(string.endsWith(".obj"))
				{
					alObjName.add(objsFile+"/"+string);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String  name : alObjName) {
			super.initObject(name, mv);
		}
		Log.e("-----ȫ���ļ��������------", "----------������꣺"+LoadedObject.maxDistance);
		//super.initObject(alObjName.get(0), mv);//���Ƶ�һ��
	}
}
