package com.gameEngine.object;

import android.opengl.GLSurfaceView;

import com.gameEngine.dae.extand.LoadedDAE;

public class QinBingGirl extends LoadedDAE{
	String daeName = "bing/testHaiwangXing.DAE";						//加载进来的
	public QinBingGirl(GLSurfaceView mv){
		
		super.initDae(daeName, mv);
	}
}
