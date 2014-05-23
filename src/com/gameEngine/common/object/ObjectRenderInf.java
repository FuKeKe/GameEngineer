package com.gameEngine.common.object;


/**
 * Created by keke on 14-3-24.
 */
public interface ObjectRenderInf {
	public void initVertexData(float[] vertices, float[] normal, float[] texture);			//加载顶点信息
	public void initShader(String vertexNameString, String fragmentNameString);				//加载着色器
	public void initLightInfo(float[] ambient, float[] diffuse, float[] specular);			//设置光强
    public void drawSelf();  																//绘制方法
}
