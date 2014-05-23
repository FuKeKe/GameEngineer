package com.gameEngine.filesupport.obj.extend;

import java.util.HashMap;

import com.gameEngine.util.LightControl;


/**
 * 加载的mtl文件
 * @author keke
 *
 */
public class MtlObject {
	String mtlName;
	HashMap<String, MtlTexInfo> mtlTexInfos = new HashMap<String, MtlTexInfo>();
	
	public String getMtlName() {
		return mtlName;
	}

	public void setMtlName(String mtlName) {
		this.mtlName = mtlName;
	}

	public HashMap<String, MtlTexInfo> getMtlTexInfos() {
		return mtlTexInfos;
	}

	public void setMtlTexInfos(HashMap<String, MtlTexInfo> mtlTexInfos) {
		this.mtlTexInfos = mtlTexInfos;
	}

	public MtlTexInfo getTexInfoByName(String key){
		if(key == null){
			if(mtlTexInfos.size()== 1){
				return (MtlTexInfo)mtlTexInfos.values().toArray()[0]; //返回第一个
			}else {
				MtlTexInfo temp = new MtlTexInfo();
				temp.setAmbient(LightControl.getLightAmbient());
				temp.setDiffuse(LightControl.getLightDiffuse());
				temp.setSpecular(LightControl.getLightSpecular());
				temp.setTexName("default");
				temp.setMap_Kd("default.png");
				return temp;
			}
		}
		return mtlTexInfos.get(key);
	}
}
