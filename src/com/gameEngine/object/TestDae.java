package com.gameEngine.object;

import android.opengl.GLSurfaceView;

import com.gameEngine.dae.extand.LoadedDAE;

public class TestDae extends LoadedDAE{
	String fileName = "bing/testHaiwangXing.DAE";
	
	public TestDae(GLSurfaceView mv) {
		// TODO Auto-generated method stub
		super();
		super.initDae(fileName, mv);
	}
}
