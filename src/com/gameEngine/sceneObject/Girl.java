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
			Log.i("assert中girls下的文件名数量", ""+files.length);
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
		Log.e("-----全部文件加载完成------", "----------最大坐标："+LoadedObject.maxDistance);
		//super.initObject(alObjName.get(0), mv);//绘制第一个
	}
}
