package com.gameEngine.objFile.extend;

import java.util.ArrayList;

import android.opengl.GLSurfaceView;

public class ObjectGroups {
	GLSurfaceView view;			//��������
	String mtlName;   			// ���ʿ������
	float[] vertices; 			// ȫ�ļ��ĵ���Ϣ��ѡ �ã�
	float[] textures; 			// ȫ�ļ���������Ϣ��ѡ �ã�
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
