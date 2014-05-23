package com.gameEngine.filesupport.obj.extend;

import java.util.ArrayList;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.gameEngine.common.object.ObjectRender;
import com.gameEngine.filesupport.obj.util.LoadMtlUtil;
import com.gameEngine.filesupport.obj.util.LoadObjUtil;

public class LoadedObject {
	ObjectGroups objGroups;																//顶点纹理等 坐标信息
	MtlObject mtlObject;																//纹理材质信息
	public static float maxDistance = 0;
	
	ArrayList<ObjectRender> alGroupParts = new ArrayList<ObjectRender>();				//所有组件

	public LoadedObject() {
		super();

	}
	/**
	 * 加载obj文件信息，生成组件
	 * @param objFileName
	 * @param mv
	 */
	public void initObject(String objFileName, GLSurfaceView mv) {
		int node = objFileName.lastIndexOf("/");
		String fileRoot = "";									//同一目录下的文件（obj,mtl
		String texRoot = "";
		
		if (-1 != node) {
			fileRoot = objFileName.substring(0, node);
			texRoot = fileRoot+"_tex";
			Log.i("读取文件的根", fileRoot);
			Log.i("读取文件的名字", objFileName);
		}
		objGroups = LoadObjUtil.loadObjFromAssert(objFileName, mv.getResources());
		mtlObject = LoadMtlUtil.loadMtlFromAssert(fileRoot+"/"+objGroups.getMtlName(), mv.getResources());
		Log.i("读取mtl文件名称", fileRoot+"/"+objGroups.getMtlName());
		for (ObjectGroupInfo info : objGroups.getAlPartsInfos()) {
			MtlTexInfo texInfo = mtlObject.getTexInfoByName(info.getTextureName());
			boolean show = true;
			if(texInfo.getMap_Kd() == null){show = false;}
			ObjectRender objRender = new ObjectRender(
					mv,
					info.getGroupName(),
					texRoot+"/"+texInfo.getMap_Kd(),
					"vertex_one_object.sh", "fragment_one_object.sh",
					info.getVertices(),
					info.getNormal(),
					info.getTexture(),
					null,
					texInfo.getAmbient(),
					texInfo.getDiffuse(),
					texInfo.getSpecular(),
					show
					);
			objRender.getMatrixState().initMatrix();
			alGroupParts.add(objRender); 
		}
		
	}
	
	public void drawAll(){
		for (ObjectRender object : alGroupParts) {
			object.drawSelf();
		}
	}
}
