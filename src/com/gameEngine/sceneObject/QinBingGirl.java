package com.gameEngine.sceneObject;

import android.opengl.GLSurfaceView;

import com.gameEngine.filesupport.dae.extand.LoadedDAE;

public class QinBingGirl extends LoadedDAE{
	String daeName = "bing/testHaiwangXing.DAE";						//���ؽ�����
	public QinBingGirl(GLSurfaceView mv){
		
		super.initDae(daeName, mv);
	}
}
