package com.gameEngine.common.object;


/**
 * Created by keke on 14-3-24.
 */
public interface ObjectRenderInf {
	public void initVertexData(float[] vertices, float[] normal, float[] texture);			//���ض�����Ϣ
	public void initShader(String vertexNameString, String fragmentNameString);				//������ɫ��
	public void initLightInfo(float[] ambient, float[] diffuse, float[] specular);			//���ù�ǿ
    public void drawSelf();  																//���Ʒ���
}
