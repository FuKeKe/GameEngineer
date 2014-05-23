package com.gameEngine.filesupport.obj.extend;

public class MtlTexInfo {
	String texName;
	float[] ambient;
	float[] diffuse;
	float[] specular;
	String map_Kd;					//对应的散射光纹理文件名称
	String map_Ka;					//环境光纹理名称
	
	public String getMap_Ka() {
		return map_Ka;
	}
	public void setMap_Ka(String map_Ka) {
		this.map_Ka = map_Ka;
	}
	public String getTexName() {
		return texName;
	}
	public void setTexName(String texName) {
		this.texName = texName;
	}
	public float[] getAmbient() {
		return ambient;
	}
	public void setAmbient(float[] ambient) {
		this.ambient = ambient;
	}
	public float[] getDiffuse() {
		return diffuse;
	}
	public void setDiffuse(float[] diffuse) {
		this.diffuse = diffuse;
	}
	public float[] getSpecular() {
		return specular;
	}
	public void setSpecular(float[] specular) {
		this.specular = specular;
	}
	public String getMap_Kd() {
		return map_Kd;
	}
	public void setMap_Kd(String map_Kd) {
		this.map_Kd = map_Kd;
	}
	
}
