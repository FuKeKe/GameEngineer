package com.gameEngine.object;

import android.opengl.GLSurfaceView;

import com.gameEngine.dae.extand.LoadedDAE;

public class QinBingGirl extends LoadedDAE{
	String daeName = "bing/testHaiwangXing.DAE";						//���ؽ�����
	public QinBingGirl(GLSurfaceView mv){
		
		super.initDae(daeName, mv);
	}
}
