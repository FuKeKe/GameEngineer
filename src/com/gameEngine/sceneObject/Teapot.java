package com.gameEngine.sceneObject;

import android.opengl.GLSurfaceView;

import com.gameEngine.filesupport.dae.extand.LoadedDAE;

public class Teapot extends LoadedDAE{
	String fileName = "teapot/testTeapot.DAE";
	
	public Teapot(GLSurfaceView mv) {
		// TODO Auto-generated method stub
		super();
		super.initDae(fileName, mv);
	}
}
