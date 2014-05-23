package com.gameEngine.filesupport.obj.extend;

import java.util.ArrayList;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.gameEngine.common.object.ObjectRender;
import com.gameEngine.filesupport.obj.util.LoadMtlUtil;
import com.gameEngine.filesupport.obj.util.LoadObjUtil;

public class LoadedObject {
	ObjectGroups objGroups;																//��������� ������Ϣ
	MtlObject mtlObject;																//���������Ϣ
	public static float maxDistance = 0;
	
	ArrayList<ObjectRender> alGroupParts = new ArrayList<ObjectRender>();				//�������

	public LoadedObject() {
		super();

	}
	/**
	 * ����obj�ļ���Ϣ���������
	 * @param objFileName
	 * @param mv
	 */
	public void initObject(String objFileName, GLSurfaceView mv) {
		int node = objFileName.lastIndexOf("/");
		String fileRoot = "";									//ͬһĿ¼�µ��ļ���obj,mtl
		String texRoot = "";
		
		if (-1 != node) {
			fileRoot = objFileName.substring(0, node);
			texRoot = fileRoot+"_tex";
			Log.i("��ȡ�ļ��ĸ�", fileRoot);
			Log.i("��ȡ�ļ�������", objFileName);
		}
		objGroups = LoadObjUtil.loadObjFromAssert(objFileName, mv.getResources());
		mtlObject = LoadMtlUtil.loadMtlFromAssert(fileRoot+"/"+objGroups.getMtlName(), mv.getResources());
		Log.i("��ȡmtl�ļ�����", fileRoot+"/"+objGroups.getMtlName());
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
