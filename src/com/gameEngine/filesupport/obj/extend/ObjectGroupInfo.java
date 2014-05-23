package com.gameEngine.filesupport.obj.extend;

import java.util.ArrayList;

public class ObjectGroupInfo {
	String groupName; 			// 本组的名字
	String textureName; 		// 纹理名称（mtl中名称）
	float[] vertices;			// 顶点信息
	float[] texture; 			// 纹理坐标
	float[] normal; 			// 法向量
	int[] index; 				// 索引（选 用）
	public String getTextureName() {
		return textureName;
	}
	
	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}
	
	public float[] getVertices() {
		return vertices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public void setVertices(ArrayList<Float> vertices) {
		int i = 0;
		float[] temp = new float[vertices.size()];
		for (float f : vertices) {
			temp[i++] = f;
			if(f > LoadedObject.maxDistance){LoadedObject.maxDistance = f;}//找到最大坐标值
		}
		this.vertices = temp;
	}
	
	public float[] getTexture() {
		return texture;
	}
	
	public void setTexture(float[] texture) {
		this.texture = texture;
	}
	
	public void setTexture(ArrayList<Float> texture) {
		int i = 0;
		float[] temp = new float[texture.size()];
		for (float f : texture) {
			temp[i++] = f;
		}
		this.texture = temp;
	}
	
	public float[] getNormal() {
		return normal;
	}
	
	public void setNormal(float[] normal) {
		this.normal = normal;
	}
	
	public void setNormal(ArrayList<Float> normal) {
		int i = 0;
		float[] temp = new float[normal.size()];
		for (float f : normal) {
			temp[i++] = f;
		}
		this.normal = temp;
	}
	
	public int[] getIndex() {
		return index;
	}
	
	public void setIndex(int[] index) {
		this.index = index;
	}
	
	public void setIndex(ArrayList<Integer> index) {
		int i = 0;
		int[] temp = new int[index.size()];
		for (int f : index) {
			temp[i++] = f;
		}
		this.index = temp;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
