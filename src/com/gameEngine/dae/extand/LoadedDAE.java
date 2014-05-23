package com.gameEngine.dae.extand;



import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.gameEngine.common.object.ObjectRender;
import com.gameEngine.dae.extand.libraryNode.LibraryAnimations;
import com.gameEngine.dae.extand.libraryNode.LibraryEffects;
import com.gameEngine.dae.extand.libraryNode.LibraryGeometries;
import com.gameEngine.dae.extand.libraryNode.LibraryVisualScenes;
import com.gameEngine.dae.util.LoadDAEUtil;
import com.gameEngine.util.MatrixUtil;
import com.gameEngine.util.TranslateUtil;
/**
 * ���������ļ��ļ��غʹ��� ��������̬��ʾ����̬���Զ�������������geometry������Scene ���ض�����
 * �����еĶ�����ֵ�㷨����javaʵ�ְ汾 �� ndk ʵ�������汾��java��Ч�ʹ��ͣ���������ʵʱ���룬��Ҫʵʱ����Ļ�������ʹ��cʵ�ֵİ汾
 * @author keke
 *
 */
public class LoadedDAE {
	protected LoadedDAEInfo daeInfo;										//��dae�ļ��ж�ȡ������Ϣ
	public static float maxDistance = 0.0f;									//�����е���������
	ArrayList<ObjectRender> objs = new ArrayList<ObjectRender>();			//������Ⱦ�������Ϣ
	
	//���л���
	String daeFileName;
	GLSurfaceView mv;
	
	Stack<float[]> nodeMatrixStack;												//����������һ���ڵ�ı任����	
	
	
	
	/**
	 * ����dae �ļ���Ϣ���������
	 * @param objFileName
	 * @param mv
	 * @return
	 */
	public boolean initDae(String daeFileName, GLSurfaceView mv){
		
		//��ȡ�ļ���Ϣ��
		daeInfo = LoadDAEUtil.loadDAEFromAssets(daeFileName, mv.getResources());
		
		//������Ⱦ����
		if(daeInfo == null){
			Log.e("Dae�ļ���ȡʧ��", "����ʧ�ܣ�"+daeFileName);
			return false;
		}else{
			Log.i("Dae�ļ���ȡ�ɹ�", "��ʼ���������Σ�");
		}
		this.daeFileName = daeFileName;
		this.mv = mv;
		loadByScene();			//���ݳ����������
		
		//loadAllGeometries();
		return true;
	}
	
	
	/**
	 * ������ͨ������֧�ֲ�ֵ������
	 * @param n Ҫ���صĶ������
	 */
	public void animationInterpolation(int n){
		 Set<String> idSet = daeInfo.libraryAnimations.animations.keySet();
		 int length = idSet.size();
		 if(n >= length){
			 Log.e("������Ų�����", ""+n);
		 }
		 animationInterpolation((String)(idSet.toArray()[n]));
	}
	
	/**
	 * ������ͨ����
	 * @param animId	������ID
	 */
	public void animationInterpolation(final String animId){
		
		Thread task = new Thread() {
			
			float time = 0.0f;						//��ǰ����ʱ��	����
			LibraryAnimations.Animation animation = daeInfo.libraryAnimations.animations.get(animId);
			LibraryAnimations.Source timeSource = animation.sources.get("INPUT");
			LibraryAnimations.Source matrixSource = animation.sources.get("OUTPUT");
			LibraryAnimations.Source interTypeSource = animation.sources.get("INTERPOLATION");
			
			//Java�汾�Ĳ�ֵ���㣺
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < timeSource.array.length; i++) {					//ʱ����Ʋ�
					
					//������ֹ�任����
					float[] endMatrix = MatrixUtil.cutOutMatrixFromA2B(matrixSource.array,i*16,i*16+15);
					
					while(time <= (Float.parseFloat(timeSource.array[i])*1000)){	//ʱ���Ƿ�ʱ�ж�
						
						float alpha = 0.0f;											//����ǰ����
						
						if(0 < i){
							alpha = (time-Float.parseFloat(timeSource.array[i-1])*1000)/(Float.parseFloat(timeSource.array[i])*1000-Float.parseFloat(timeSource.array[i-1])*1000);	
						}else{
							alpha = time / (Float.parseFloat(timeSource.array[i])*1000);
						}
						
						if("LINEAR".equals(interTypeSource.array[i])){				//�ж϶�����ʽ�Ƿ�Ϊ����
							
							//������ʾ�任����
							if(0 < i){	//�������0
								
								float[] startMatrix = MatrixUtil.cutOutMatrixFromA2B(matrixSource.array,(i-1)*16,(i-1)*16+15);
							
								for (ObjectRender obj : objs) {							//����Ҫ��ʾ�����
									if(obj.getObjectID().equals(animation.targetId)){	//�����ļ��ľ���Ҫת��
										obj.getMatrixState().addMatrix2Org(MatrixUtil.matrixToMatrixT(MatrixUtil.interpolationMatrix(startMatrix, endMatrix, alpha)));
									}
								}
							
							
							}else{
								
								float[] startMatrix	= MatrixUtil.getInitMatrix(4, 4);
								
								for (ObjectRender obj : objs) {							//����Ҫ��ʾ�����
									if(obj.getObjectID().equals(animation.targetId)){
										obj.getMatrixState().addMatrix2Org(MatrixUtil.matrixToMatrixT(MatrixUtil.interpolationMatrix(startMatrix, endMatrix, alpha)));
									}
								}
							}
							
						}
						time = System.currentTimeMillis() - startTime;
					}
				}
				Log.e("������ʾ���", ""+animId);
			}
			
			
		};
		
		task.start();
	}
	
	public void animationInterpolationByC(int n){
		Set<String> idSet = daeInfo.libraryAnimations.animations.keySet();
		 int length = idSet.size();
		 if(n >= length){
			 Log.e("������Ų�����", ""+n);
		 }
		 LibraryAnimations.Animation animation = daeInfo.libraryAnimations.animations.get((String)(idSet.toArray()[n]));
		 LibraryAnimations.Source timeSource = animation.sources.get("INPUT");
		 LibraryAnimations.Source matrixSource = animation.sources.get("OUTPUT");
		 LibraryAnimations.Source interTypeSource = animation.sources.get("INTERPOLATION");
		 float[] time = TranslateUtil.changeStringArrToFloatArr(timeSource.array);
		 float[] matrix = TranslateUtil.changeStringArrToFloatArr(matrixSource.array);
		 String[] type = interTypeSource.array;
		 
		 animationInterpolationByC(time,matrix,type);
	}
	
	/**
	 * Ҫʹ�õ�C�ļ�����
	 */
	static{
		System.loadLibrary("");
	}
	public native float[] animationInterpolationByC (float[] time, float[] matrix, String[] type);
	
	
	
	public void animationBone(final String animId){
		
	}
	
	/**
	 * �������е�ģ�Ͷ���
	 */
	public void drawAllGeomatry(){
		//long startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��  
		for (ObjectRender render : objs) {
			render.drawSelf();
		}
		//long endTime=System.currentTimeMillis(); //��ȡ����ʱ��  
		//Log.i("�������", "��ʱ "+(endTime-startTime)+" ms���");
	}
	
	
	/**
	 * �������е����
	 * @param daeFileName
	 */
	@SuppressWarnings("unused")
	private void loadAllGeometries(){
		objs.clear();//���ԭ�е�obj
		for (Entry<String, LibraryGeometries.Geometry> entry: daeInfo.libraryGeometries.geometries.entrySet()) {
			generateObjectRender(entry.getValue(),null,null);
		}
		Log.e("���������γɹ�", "������"+objs.size());
		Log.e("���ص���Ϣ��", "������������꣺"+maxDistance);
	}
	
	
	/**
	 * ���ݳ���scene���� ObjectRender
	 * @param daeFileName
	 * @param mv
	 */
	private void loadByScene(){
		objs.clear();//���ԭ�е�obj
		String rootScene = daeInfo.libraryScene.instance_visual_scene;
		LibraryVisualScenes.VisualScene visualScene = daeInfo.libraryVisualScenes.visualScenes.get(rootScene);
		loadChildNodes(visualScene.nodesId);	//��ʼ�ݹ����
	}
	
	/**
	 * ���� NodeId ���س���
	 * @param nodeId
	 */
	@SuppressWarnings("unused")
	private void loadByNodeId(String nodeId){
		objs.clear();//���ԭ�е�obj
		ArrayList<String> nodeList = new ArrayList<String>();
		nodeList.add(nodeId);
		loadChildNodes(nodeList);	//��ʼ�ݹ����
	}
	
	//���� �б��е�����ID �� node
	private void loadChildNodes(ArrayList<String> nodeList){
		
		nodeMatrixStack = new Stack<float[]>();
		
		for (String id : nodeList) {	//����HashMap
			LibraryVisualScenes.Node node = daeInfo.libraryVisualScenes.nodes.get(id);
			float[] finalMatrix = new float[16];
			if(nodeMatrixStack.isEmpty()){
				finalMatrix = node.matrix;
			}else{
				Matrix.multiplyMM(finalMatrix, 0, nodeMatrixStack.peek(), 0, node.matrix,0);
			}
			generateObjectRender(daeInfo.libraryGeometries.geometries.get(node.geometryId),node.id, MatrixUtil.matrixToMatrixT(finalMatrix));
			if(node.childNodes.isEmpty()){
				return;					//���û�ӽڵ�ͷ���
			}
			nodeMatrixStack.push(node.matrix);
			loadChildNodes(node.childNodes);
			nodeMatrixStack.pop();
			}
	}
	
	
	/**
	 * ���� geometry���ļ���ַ��view ���س� ObjectRender����ʽ
	 * @param geometry
	 * @param objectId   �����ɵ���Ⱦ��� һ��ID���ڼ���node����ʱ��Node��ID ���ض�����
	 * @param daeFileName
	 * @param mv
	 * @param matrix
	 */
	private void generateObjectRender(LibraryGeometries.Geometry geometry,String objectId, float[] matrix){
		for (LibraryGeometries.Triangle triangle : geometry.triangles) {
			boolean show = true;
			String materialid = triangle.materialId;
			String effectId = daeInfo.libraryMaterials.materials.get(materialid).instance_effect;
			LibraryEffects.Effect effect = daeInfo.libraryEffects.effects.get(effectId);//����ǿ�� ��ͬ�������
			String texAdres;
			if(null == effect){
				System.out.println(materialid);
				System.out.println(effectId);
				System.out.println(effect);
			}
			//���������ַ��DAE�ļ�ͬ�ļ�����
			if((null != effect.diffuseTexture) && (!"".equals(effect.diffuseTexture))){
				int node = daeFileName.lastIndexOf("/");
				String root = daeFileName.substring(0,node);
				String address = daeInfo.libraryImages.images.get(effect.diffuseTexture).init_from.replace("\\", "/");
				node = address.lastIndexOf("/");
				String imgName = address.substring(node+1,address.length());
				texAdres = root + "/" + imgName;
			}else {
				texAdres = "defaultTex/default.png";//Ĭ�������ַ
			}
			ObjectRender objectRender = new ObjectRender(
					mv,
					objectId, 
					texAdres, 
					"vertex_one_object.sh", 
					"fragment_one_object.sh", 
					triangle.vertices, 
					triangle.normals, 
					triangle.textures, 
					null, 
					effect.ambient, 
					effect.diffuse, 
					effect.specular, 
					show
					);
			if(null != matrix){
				objectRender.getMatrixState().addMatrix2Cur(matrix);
			}
			objs.add(objectRender);
		}
	}
}
