package com.gameEngine.object;

import android.opengl.GLSurfaceView;

import com.gameEngine.dae.extand.LoadedDAE;

public class Teapot extends LoadedDAE{
	String fileName = "teapot/testTeapot.DAE";
	
	public Teapot(GLSurfaceView mv) {
		// TODO Auto-generated method stub
		super();
		super.initDae(fileName, mv);
	}
}
