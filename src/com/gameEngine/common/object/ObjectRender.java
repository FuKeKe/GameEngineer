package com.gameEngine.common.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.gameEngine.util.MatrixState;
import com.gameEngine.util.ShaderUtil;
import com.gameEngine.util.TextureUtil;


/**
 * ������� ��Ⱦ��
 * @author keke
 *
 */
public class ObjectRender implements ObjectRenderInf{
	
	//��Ҫ����ı���
	GLSurfaceView view;					//��ʾ�Ĵ���
	String texName;						//�����ļ�����
	String objectID;					//��render�ı�ʾ
	String vertexShaderName;			//������ɫ���ļ�����
	String fragmentShaderName;			//ƬԪ��ɫ���ļ�����
	float[] vertices;
	float[] normal;
	float[] texture;
	float[] texMult = {1.0f,1.0f};		//�������챶����Ĭ��1.0
	float[] lightAmbientStr = {0.3f,0.3f,0.3f,1.0f};			//Ĭ�ϻ�������
	float[] lightDiffuseStr = {0.7f,0.7f,0.7f,1.0f};			//Ĭ��ɢ�����
	float[] lightSpecularStr = {1.0f,1.0f,1.0f,1.0f};			//Ĭ�Ͼ������
	boolean show = false;
	
	//�����õı���
	int iCount = 0;
	FloatBuffer fVertexBuffer;
	FloatBuffer fNormalBuffer;
	FloatBuffer fTexCoorBuffer;
	FloatBuffer fTexCoorMultBuffer;
	FloatBuffer fLightAmbientStr;			
	FloatBuffer fLightDiffuseStr;			
	FloatBuffer fLightSpecularStr;			
	String sVertexShader;
	String sFragmentShader;
	
	//��ɫ�����ã�
	int iProgram;				//������
	int iMVPMatrixHandle;		//�ܱ任����
	int iMMatrixHandle;			//����任����
	int iCameraHandle;			//�������
	int iLightLocationHandle;	//�ƹ�λ��
	int iPositionHandle;		//����Ԫ��λ��
	int iTexCoorHandle;			//������λ��
	int iTexCoordMult;			//������ͼ���챶��
	int iNormalHandle;			//������
	int iLightAmbientHandle;	//������ǿ��
	int iLightDiffuseHandle;	//������ǿ��
	int iLightSpecularHandle;	//���淴��ǿ��
	
	//��������
	int iTexID;					//���ɵ�����ID
	int iTextureHandle;			//��������ĵ�ַ
	
	//�任�����գ���������ƣ�
	MatrixState matrixState;	//������
	
	/**
	 * 
	 ����obj�ļ�����һ��Object����
	 * @param mv					��ʾ����
	 * @param objectID			.obj�ļ�����
	 * @param TexResourceID			��ͼ�ļ�  R�е�����
	 * @param vertexNameString		������ɫ������
	 * @param fragmentNameString	ƬԪ��ɫ������
	 * @param vertices				��������
	 * @param normal				����������
	 * @param texture				��������
	 * @param lightAmbientStr		������ǿ��
	 * @param lightDiffuseStr		ɢ���ǿ��
	 * @param lightSpecularStr		�����ǿ��
	 */
	public ObjectRender(
			GLSurfaceView mv, 						//���컷��
			String objectID, 						//��������
			String texName, 						//������assert�µ�����
			String vertexNameString, 				//������ɫ���� assert�µ�����
			String fragmentNameString, 				//ƬԪ��ɫ���� assert�µ�����
			float[] vertices, 						//��������
			float[] normal, 						//����������
			float[] texture,						//��������
			float[] texMult,						//������ͼ����
			float[] lightAmbientStr,				//������ǿ��
			float[] lightDiffuseStr,				//ɢ���ǿ��
			float[] lightSpecularStr,				//�����ǿ��
			boolean show
			)
	{
		super();
		this.view = mv;
		this.texName = texName;
		this.objectID = objectID;
		this.vertexShaderName = vertexNameString;
		this.fragmentShaderName = fragmentNameString;
		this.vertices = vertices;
		this.normal = normal;
		this.texture = texture;
		if(null != texMult){
			this.texMult = texMult;
		}
		if(null != lightAmbientStr){
			this.lightAmbientStr = lightAmbientStr;
		}
		if(null != lightDiffuseStr){
			this.lightDiffuseStr = lightAmbientStr;
		}
		if(null != lightSpecularStr){
			this.lightSpecularStr = lightAmbientStr;
		}
		matrixState = new MatrixState();
		setShow(show);								//������ʾ  -->  ����Ҫ�õ���buffer;
		
	}

	@Override
	public void initVertexData(float[] vertices, float[] normal, float[] texture) {
		// TODO Auto-generated method stub
		iCount = vertices.length/3;
		//�������ݻ���
		ByteBuffer buffer1 = ByteBuffer.allocateDirect(vertices.length*4);
		buffer1.order(ByteOrder.nativeOrder());
		fVertexBuffer = buffer1.asFloatBuffer();
		fVertexBuffer.put(vertices);
		fVertexBuffer.flip();
		
		//���������ݻ���
		ByteBuffer buffer2 = ByteBuffer.allocateDirect(normal.length*4);
		buffer2.order(ByteOrder.nativeOrder());
		fNormalBuffer = buffer2.asFloatBuffer();
		fNormalBuffer.put(normal);
		fNormalBuffer.flip();
		
		//�������ݻ���
		ByteBuffer buffer3 = ByteBuffer.allocateDirect(texture.length*4);
		buffer3.order(ByteOrder.nativeOrder());
		fTexCoorBuffer = buffer3.asFloatBuffer();
		fTexCoorBuffer.put(texture);
		fTexCoorBuffer.flip();
	}

	@Override
	public void initShader(String vertexNameString, String fragmentNameString) {
		// TODO Auto-generated method stub
		sVertexShader = ShaderUtil.loadFromAssertsFile(vertexNameString, view.getResources());
		sFragmentShader = ShaderUtil.loadFromAssertsFile(fragmentNameString, view.getResources());
		iProgram = ShaderUtil.createProgram(sVertexShader, sFragmentShader);
		
		iMVPMatrixHandle = GLES20.glGetUniformLocation(iProgram, "uMVPMatrix");
		iMMatrixHandle = GLES20.glGetUniformLocation(iProgram, "uMMatrix");
		iCameraHandle = GLES20.glGetUniformLocation(iProgram, "uCameraLocation");
		iLightLocationHandle = GLES20.glGetUniformLocation(iProgram, "uLightLocation");
		iLightAmbientHandle = GLES20.glGetUniformLocation(iProgram, "uLightAmbient");
		iLightDiffuseHandle = GLES20.glGetUniformLocation(iProgram, "uLightDiffuse");
		iLightSpecularHandle = GLES20.glGetUniformLocation(iProgram, "uLightSpecular");
		iTextureHandle = GLES20.glGetUniformLocation(iProgram, "sTexture");
		iTexCoordMult = GLES20.glGetUniformLocation(iProgram, "vTexCoordMult");
		
		iPositionHandle = GLES20.glGetAttribLocation(iProgram, "aPosition");
		iTexCoorHandle = GLES20.glGetAttribLocation(iProgram, "aTexCoor");
		iNormalHandle = GLES20.glGetAttribLocation(iProgram, "aNormal");
	}

	@Override
	public void initLightInfo(float[] ambient, float[] diffuse, float[] specular) {
		// TODO Auto-generated method stub
		
		//������
		ByteBuffer buffer1 = ByteBuffer.allocateDirect(ambient.length*4);
		buffer1.order(ByteOrder.nativeOrder());
		fLightAmbientStr = buffer1.asFloatBuffer();
		fLightAmbientStr.put(ambient);
		fLightAmbientStr.position(0);
		
		//ɢ���
		ByteBuffer buffer2 = ByteBuffer.allocateDirect(diffuse.length*4);
		buffer2.order(ByteOrder.nativeOrder());
		fLightDiffuseStr = buffer2.asFloatBuffer();
		fLightDiffuseStr.put(diffuse);
		fLightDiffuseStr.position(0);
		
		//�����
		ByteBuffer buffer3 = ByteBuffer.allocateDirect(specular.length*4);
		buffer3.order(ByteOrder.nativeOrder());
		fLightSpecularStr = buffer3.asFloatBuffer();
		fLightSpecularStr.put(specular);
		fLightSpecularStr.position(0);
			
	}
	
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		if(!show){return;}						//���showΪfalse,�Ͳ�������ʾ
		//Log.i("���ڻ��� ", ""+objectName);
		GLES20.glUseProgram(iProgram);
		GLES20.glUniformMatrix4fv(iMVPMatrixHandle, 1, false, matrixState.getFinalMatrix(), 0);
		GLES20.glUniformMatrix4fv(iMMatrixHandle, 1, false, matrixState.getMMatrix(), 0);
		GLES20.glUniform3fv(iCameraHandle, 1, matrixState.getCameraLocation());
		GLES20.glUniform3fv(iLightLocationHandle, 1, matrixState.getLightLocation());
		GLES20.glUniform4fv(iLightAmbientHandle, 1, fLightAmbientStr);
		GLES20.glUniform4fv(iLightDiffuseHandle, 1, fLightDiffuseStr);
		GLES20.glUniform4fv(iLightSpecularHandle, 1, fLightSpecularStr);
		GLES20.glUniform2fv(iTexCoordMult,1,fTexCoorMultBuffer);
		
		GLES20.glVertexAttribPointer(iPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, fVertexBuffer);
		GLES20.glVertexAttribPointer(iNormalHandle, 3, GLES20.GL_FLOAT, false, 3*4, fNormalBuffer);
		GLES20.glVertexAttribPointer(iTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2*4, fTexCoorBuffer);
		
		GLES20.glEnableVertexAttribArray(iPositionHandle);
		GLES20.glEnableVertexAttribArray(iNormalHandle);
		GLES20.glEnableVertexAttribArray(iTexCoorHandle);
		
		//������
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);					//����1
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexID);			//���������
		GLES20.glUniform1i(iTextureHandle, 0);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, iCount);
	}

	public String getObjectName() {
		return objectID;
	}

	public void setObjectName(String objectName) {
		this.objectID = objectName;
	}

	public String getVertexShaderName() {
		return vertexShaderName;
	}

	public void setVertexShaderName(String vertexShaderName) {
		this.vertexShaderName = vertexShaderName;
	}

	public String getFragmentShaderName() {
		return fragmentShaderName;
	}

	public void setFragmentShaderName(String fragmentShaderName) {
		this.fragmentShaderName = fragmentShaderName;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
		if(show){
			initShader(vertexShaderName,fragmentShaderName);
			initVertexData(vertices,normal,texture);
			initLightInfo(lightAmbientStr, lightDiffuseStr, lightSpecularStr);
			if(texName.endsWith("null")){
				this.show = false;
				Log.e("������������Ʋ���Ϊnull", "Object�ļ�����"+objectID+"; ��������"+texName);
				return;
			}
			//��������ͼƬ
			iTexID = TextureUtil.initTextureFromAssets(texName,view);
			//�����������챶��
			ByteBuffer buffer = ByteBuffer.allocateDirect(texMult.length*4);
			buffer.order(ByteOrder.nativeOrder());
			fTexCoorMultBuffer = buffer.asFloatBuffer();
			fTexCoorMultBuffer.put(texMult);
			fTexCoorMultBuffer.flip();
		}
	}

	public MatrixState getMatrixState() {
		return matrixState;
	}

	public void setMatrixState(MatrixState matrixState) {
		this.matrixState = matrixState;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}
	
	
}
