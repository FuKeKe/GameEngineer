package com.gameEngine.objFile.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.res.Resources;

import com.gameEngine.objFile.extend.MtlObject;
import com.gameEngine.objFile.extend.MtlTexInfo;
import com.gameEngine.util.LightControl;

public class LoadMtlUtil {
	public static MtlObject loadMtlFromAssert(String fname, Resources r) {
		MtlObject mtlResult = new MtlObject();
		mtlResult.setMtlName(fname);
		try {
			InputStream is = r.getAssets().open(fname);
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(ir);
			String tempLine = null;
			MtlTexInfo texInfo = new MtlTexInfo();
			while (null != (tempLine = reader.readLine())) {
				String[] splitString = tempLine.trim().split("[ ]+");
				if ("newmtl".equals(splitString[0])) {
					if (null != texInfo.getTexName()) {				//�������Ʋ�Ϊ�վͽ�������Ϣ�ӵ�ӳ�����
						//�������Ϊ�գ��ͻ�ȡĬ�ϵĹ���ֵ
						if(texInfo.getAmbient() == null){texInfo.setAmbient(LightControl.getLightAmbient());}
						if(texInfo.getDiffuse() == null){texInfo.setDiffuse(LightControl.getLightAmbient());}
						if(texInfo.getSpecular() == null){texInfo.setSpecular(LightControl.getLightAmbient());}
						mtlResult.getMtlTexInfos().put(texInfo.getTexName(), texInfo);
						texInfo = new MtlTexInfo();					//��Ӻ�����һ���µĽڵ�
					}else {
						texInfo.setTexName(splitString[1]);
					}
				} else if ("Ka".equals(splitString[0])) {			//������ǿ��
					float[] ambient = new float[3];
					for(int i = 0; i < 3; i++)
					{
						ambient[i] = Float.parseFloat(splitString[i+1]);
					}
					texInfo.setAmbient(ambient);
				} else if ("Kd".equals(splitString[0])) {			//ɢ���ǿ��
					float[] diffuse = new float[3];
					for(int i = 0; i < 3; i++)
					{
						diffuse[i] = Float.parseFloat(splitString[i+1]);
					}
					texInfo.setDiffuse(diffuse);
				} else if ("Ks".equals(splitString[0])) {			//�����ǿ��
					float[] specular = new float[3];
					for(int i = 0; i < 3; i++)
					{
						specular[i] = Float.parseFloat(splitString[i+1]);
					}
					texInfo.setSpecular(specular);
				} else if ("map_Ka".equals(splitString[0])) {		//����������
					texInfo.setMap_Ka(splitString[1]);
				} else if ("map_Kd".equals(splitString[0])) {		//ɢ�������
					texInfo.setMap_Kd(splitString[1]);
				}
			}
			//������󣬽���һ���ڵ�ӵ������б���
			//�������Ϊ�գ��ͻ�ȡĬ�ϵĹ���ֵ
			if(texInfo.getAmbient() == null){texInfo.setAmbient(LightControl.getLightAmbient());}
			if(texInfo.getDiffuse() == null){texInfo.setDiffuse(LightControl.getLightAmbient());}
			if(texInfo.getSpecular() == null){texInfo.setSpecular(LightControl.getLightAmbient());}
			mtlResult.getMtlTexInfos().put(texInfo.getTexName(), texInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mtlResult;
	}
}
