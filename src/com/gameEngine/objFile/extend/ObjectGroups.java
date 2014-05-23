package com.gameEngine.objFile.extend;

import java.util.ArrayList;

import android.opengl.GLSurfaceView;

public class ObjectGroups {
	GLSurfaceView view;			//创建环境
	String mtlName;   			// 材质库的名字
	float[] vertices; 			// 全文件的点信息（选 用）
	float[] textures; 			// 全文件的纹理信息（选 用）
	ArrayList<ObjectGroupInfo> alPartsInfos = new ArrayList<ObjectGroupInfo>();
	
	public float[] getVertices() {
		return vertices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public ArrayList<ObjectGroupInfo> getAlPartsInfos() {
		return alPartsInfos;
	}

	public void setAlPartsInfos(ArrayList<ObjectGroupInfo> alPartsInfos) {
		this.alPartsInfos = alPartsInfos;
	}

	public String getMtlName() {
		return mtlName;
	}

	public void setMtlName(String mtlName) {
		this.mtlName = mtlName;
	}

	public float[] getTextures() {
		return textures;
	}

	public void setTextures(float[] textures) {
		this.textures = textures;
	}

}
