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
 * 最基本的 渲染类
 * @author keke
 *
 */
public class ObjectRender implements ObjectRenderInf{
	
	//需要输入的变量
	GLSurfaceView view;					//显示的窗口
	String texName;						//纹理文件名称
	String objectID;					//此render的标示
	String vertexShaderName;			//顶点着色器文件名称
	String fragmentShaderName;			//片元着色器文件名称
	float[] vertices;
	float[] normal;
	float[] texture;
	float[] texMult = {1.0f,1.0f};		//纹理拉伸倍数，默认1.0
	float[] lightAmbientStr = {0.3f,0.3f,0.3f,1.0f};			//默认环境光照
	float[] lightDiffuseStr = {0.7f,0.7f,0.7f,1.0f};			//默认散射光照
	float[] lightSpecularStr = {1.0f,1.0f,1.0f,1.0f};			//默认镜面光照
	boolean show = false;
	
	//加载用的变量
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
	
	//着色器引用：
	int iProgram;				//主程序
	int iMVPMatrixHandle;		//总变换矩阵
	int iMMatrixHandle;			//物体变换矩阵
	int iCameraHandle;			//摄像矩阵
	int iLightLocationHandle;	//灯光位置
	int iPositionHandle;		//顶点元素位置
	int iTexCoorHandle;			//纹理顶点位置
	int iTexCoordMult;			//纹理贴图拉伸倍数
	int iNormalHandle;			//法向量
	int iLightAmbientHandle;	//环境光强度
	int iLightDiffuseHandle;	//漫反射强度
	int iLightSpecularHandle;	//镜面反射强度
	
	//纹理引用
	int iTexID;					//生成的纹理ID
	int iTextureHandle;			//输入纹理的地址
	
	//变换，光照，摄像机控制：
	MatrixState matrixState;	//控制类
	
	/**
	 * 
	 根据obj文件生成一个Object对象
	 * @param mv					显示容器
	 * @param objectID			.obj文件名称
	 * @param TexResourceID			贴图文件  R中的名称
	 * @param vertexNameString		顶点着色器名称
	 * @param fragmentNameString	片元着色器名称
	 * @param vertices				顶点坐标
	 * @param normal				法向量坐标
	 * @param texture				纹理坐标
	 * @param lightAmbientStr		环境光强度
	 * @param lightDiffuseStr		散射光强度
	 * @param lightSpecularStr		镜面光强度
	 */
	public ObjectRender(
			GLSurfaceView mv, 						//构造环境
			String objectID, 						//部件名称
			String texName, 						//纹理在assert下的名称
			String vertexNameString, 				//顶点着色器在 assert下的名称
			String fragmentNameString, 				//片元着色器在 assert下的名称
			float[] vertices, 						//顶点坐标
			float[] normal, 						//法向量坐标
			float[] texture,						//纹理坐标
			float[] texMult,						//纹理贴图倍数
			float[] lightAmbientStr,				//环境光强度
			float[] lightDiffuseStr,				//散射光强度
			float[] lightSpecularStr,				//镜面光强度
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
		setShow(show);								//启动显示  -->  生成要用到的buffer;
		
	}

	@Override
	public void initVertexData(float[] vertices, float[] normal, float[] texture) {
		// TODO Auto-generated method stub
		iCount = vertices.length/3;
		//顶点数据缓冲
		ByteBuffer buffer1 = ByteBuffer.allocateDirect(vertices.length*4);
		buffer1.order(ByteOrder.nativeOrder());
		fVertexBuffer = buffer1.asFloatBuffer();
		fVertexBuffer.put(vertices);
		fVertexBuffer.flip();
		
		//法向量数据缓冲
		ByteBuffer buffer2 = ByteBuffer.allocateDirect(normal.length*4);
		buffer2.order(ByteOrder.nativeOrder());
		fNormalBuffer = buffer2.asFloatBuffer();
		fNormalBuffer.put(normal);
		fNormalBuffer.flip();
		
		//纹理数据缓冲
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
		
		//环境光
		ByteBuffer buffer1 = ByteBuffer.allocateDirect(ambient.length*4);
		buffer1.order(ByteOrder.nativeOrder());
		fLightAmbientStr = buffer1.asFloatBuffer();
		fLightAmbientStr.put(ambient);
		fLightAmbientStr.position(0);
		
		//散射光
		ByteBuffer buffer2 = ByteBuffer.allocateDirect(diffuse.length*4);
		buffer2.order(ByteOrder.nativeOrder());
		fLightDiffuseStr = buffer2.asFloatBuffer();
		fLightDiffuseStr.put(diffuse);
		fLightDiffuseStr.position(0);
		
		//镜面光
		ByteBuffer buffer3 = ByteBuffer.allocateDirect(specular.length*4);
		buffer3.order(ByteOrder.nativeOrder());
		fLightSpecularStr = buffer3.asFloatBuffer();
		fLightSpecularStr.put(specular);
		fLightSpecularStr.position(0);
			
	}
	
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		if(!show){return;}						//如果show为false,就不进行显示
		//Log.i("正在绘制 ", ""+objectName);
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
		
		//绑定纹理
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);					//纹理1
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexID);			//白天的纹理
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
				Log.e("传入的纹理名称不能为null", "Object文件名："+objectID+"; 纹理名："+texName);
				return;
			}
			//加载纹理图片
			iTexID = TextureUtil.initTextureFromAssets(texName,view);
			//生成纹理拉伸倍数
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
