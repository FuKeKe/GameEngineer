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
 * 骨骼动画文件的加载和处理 包括：静态显示，动态线性动画，加载所有geometry，根据Scene 加载动画，
 * 本类中的动画插值算法，有java实现版本 和 ndk 实现两个版本，java的效率过低，不能满足实时插入，想要实时插入的话，可以使用c实现的版本
 * @author keke
 *
 */
public class LoadedDAE {
	protected LoadedDAEInfo daeInfo;										//从dae文件中读取到的信息
	public static float maxDistance = 0.0f;									//场景中点的最大坐标
	ArrayList<ObjectRender> objs = new ArrayList<ObjectRender>();			//用来渲染组件的信息
	
	//运行环境
	String daeFileName;
	GLSurfaceView mv;
	
	Stack<float[]> nodeMatrixStack;												//用来保存上一个节点的变换矩阵	
	
	
	
	/**
	 * 加载dae 文件信息：生成组件
	 * @param objFileName
	 * @param mv
	 * @return
	 */
	public boolean initDae(String daeFileName, GLSurfaceView mv){
		
		//读取文件信息：
		daeInfo = LoadDAEUtil.loadDAEFromAssets(daeFileName, mv.getResources());
		
		//生成渲染器：
		if(daeInfo == null){
			Log.e("Dae文件读取失败", "加载失败："+daeFileName);
			return false;
		}else{
			Log.i("Dae文件读取成功", "开始加载三角形：");
		}
		this.daeFileName = daeFileName;
		this.mv = mv;
		loadByScene();			//根据场景加载组件
		
		//loadAllGeometries();
		return true;
	}
	
	
	/**
	 * 播放普通动画（支持插值动画）
	 * @param n 要加载的动画序号
	 */
	public void animationInterpolation(int n){
		 Set<String> idSet = daeInfo.libraryAnimations.animations.keySet();
		 int length = idSet.size();
		 if(n >= length){
			 Log.e("动画序号不存在", ""+n);
		 }
		 animationInterpolation((String)(idSet.toArray()[n]));
	}
	
	/**
	 * 播放普通动画
	 * @param animId	动画的ID
	 */
	public void animationInterpolation(final String animId){
		
		Thread task = new Thread() {
			
			float time = 0.0f;						//当前运行时间	毫秒
			LibraryAnimations.Animation animation = daeInfo.libraryAnimations.animations.get(animId);
			LibraryAnimations.Source timeSource = animation.sources.get("INPUT");
			LibraryAnimations.Source matrixSource = animation.sources.get("OUTPUT");
			LibraryAnimations.Source interTypeSource = animation.sources.get("INTERPOLATION");
			
			//Java版本的插值计算：
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < timeSource.array.length; i++) {					//时间控制层
					
					//生成终止变换矩阵
					float[] endMatrix = MatrixUtil.cutOutMatrixFromA2B(matrixSource.array,i*16,i*16+15);
					
					while(time <= (Float.parseFloat(timeSource.array[i])*1000)){	//时间是否超时判断
						
						float alpha = 0.0f;											//动画前进比
						
						if(0 < i){
							alpha = (time-Float.parseFloat(timeSource.array[i-1])*1000)/(Float.parseFloat(timeSource.array[i])*1000-Float.parseFloat(timeSource.array[i-1])*1000);	
						}else{
							alpha = time / (Float.parseFloat(timeSource.array[i])*1000);
						}
						
						if("LINEAR".equals(interTypeSource.array[i])){				//判断动画形式是否为线性
							
							//生成启示变换矩阵
							if(0 < i){	//如果大于0
								
								float[] startMatrix = MatrixUtil.cutOutMatrixFromA2B(matrixSource.array,(i-1)*16,(i-1)*16+15);
							
								for (ObjectRender obj : objs) {							//遍历要显示的组件
									if(obj.getObjectID().equals(animation.targetId)){	//动画文件的矩阵要转置
										obj.getMatrixState().addMatrix2Org(MatrixUtil.matrixToMatrixT(MatrixUtil.interpolationMatrix(startMatrix, endMatrix, alpha)));
									}
								}
							
							
							}else{
								
								float[] startMatrix	= MatrixUtil.getInitMatrix(4, 4);
								
								for (ObjectRender obj : objs) {							//遍历要显示的组件
									if(obj.getObjectID().equals(animation.targetId)){
										obj.getMatrixState().addMatrix2Org(MatrixUtil.matrixToMatrixT(MatrixUtil.interpolationMatrix(startMatrix, endMatrix, alpha)));
									}
								}
							}
							
						}
						time = System.currentTimeMillis() - startTime;
					}
				}
				Log.e("动画显示完毕", ""+animId);
			}
			
			
		};
		
		task.start();
	}
	
	public void animationInterpolationByC(int n){
		Set<String> idSet = daeInfo.libraryAnimations.animations.keySet();
		 int length = idSet.size();
		 if(n >= length){
			 Log.e("动画序号不存在", ""+n);
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
	 * 要使用的C文件所在
	 */
	static{
		System.loadLibrary("");
	}
	public native float[] animationInterpolationByC (float[] time, float[] matrix, String[] type);
	
	
	
	public void animationBone(final String animId){
		
	}
	
	/**
	 * 绘制所有的模型顶点
	 */
	public void drawAllGeomatry(){
		//long startTime=System.currentTimeMillis();   //获取开始时间  
		for (ObjectRender render : objs) {
			render.drawSelf();
		}
		//long endTime=System.currentTimeMillis(); //获取结束时间  
		//Log.i("绘制完成", "用时 "+(endTime-startTime)+" ms完成");
	}
	
	
	/**
	 * 加载所有的组件
	 * @param daeFileName
	 */
	@SuppressWarnings("unused")
	private void loadAllGeometries(){
		objs.clear();//清空原有的obj
		for (Entry<String, LibraryGeometries.Geometry> entry: daeInfo.libraryGeometries.geometries.entrySet()) {
			generateObjectRender(entry.getValue(),null,null);
		}
		Log.e("加载三角形成功", "数量："+objs.size());
		Log.e("加载的信息：", "场景中最大坐标："+maxDistance);
	}
	
	
	/**
	 * 根据场景scene加载 ObjectRender
	 * @param daeFileName
	 * @param mv
	 */
	private void loadByScene(){
		objs.clear();//清空原有的obj
		String rootScene = daeInfo.libraryScene.instance_visual_scene;
		LibraryVisualScenes.VisualScene visualScene = daeInfo.libraryVisualScenes.visualScenes.get(rootScene);
		loadChildNodes(visualScene.nodesId);	//开始递归调用
	}
	
	/**
	 * 根据 NodeId 加载场景
	 * @param nodeId
	 */
	@SuppressWarnings("unused")
	private void loadByNodeId(String nodeId){
		objs.clear();//清空原有的obj
		ArrayList<String> nodeList = new ArrayList<String>();
		nodeList.add(nodeId);
		loadChildNodes(nodeList);	//开始递归调用
	}
	
	//加载 列表中的所有ID 的 node
	private void loadChildNodes(ArrayList<String> nodeList){
		
		nodeMatrixStack = new Stack<float[]>();
		
		for (String id : nodeList) {	//遍历HashMap
			LibraryVisualScenes.Node node = daeInfo.libraryVisualScenes.nodes.get(id);
			float[] finalMatrix = new float[16];
			if(nodeMatrixStack.isEmpty()){
				finalMatrix = node.matrix;
			}else{
				Matrix.multiplyMM(finalMatrix, 0, nodeMatrixStack.peek(), 0, node.matrix,0);
			}
			generateObjectRender(daeInfo.libraryGeometries.geometries.get(node.geometryId),node.id, MatrixUtil.matrixToMatrixT(finalMatrix));
			if(node.childNodes.isEmpty()){
				return;					//如果没子节点就返回
			}
			nodeMatrixStack.push(node.matrix);
			loadChildNodes(node.childNodes);
			nodeMatrixStack.pop();
			}
	}
	
	
	/**
	 * 根据 geometry，文件地址，view 加载成 ObjectRender的形式
	 * @param geometry
	 * @param objectId   给生成的渲染组件 一个ID。在加载node动画时是Node的ID 加载动画用
	 * @param daeFileName
	 * @param mv
	 * @param matrix
	 */
	private void generateObjectRender(LibraryGeometries.Geometry geometry,String objectId, float[] matrix){
		for (LibraryGeometries.Triangle triangle : geometry.triangles) {
			boolean show = true;
			String materialid = triangle.materialId;
			String effectId = daeInfo.libraryMaterials.materials.get(materialid).instance_effect;
			LibraryEffects.Effect effect = daeInfo.libraryEffects.effects.get(effectId);//光照强度 不同光的纹理
			String texAdres;
			if(null == effect){
				System.out.println(materialid);
				System.out.println(effectId);
				System.out.println(effect);
			}
			//生成纹理地址与DAE文件同文件夹下
			if((null != effect.diffuseTexture) && (!"".equals(effect.diffuseTexture))){
				int node = daeFileName.lastIndexOf("/");
				String root = daeFileName.substring(0,node);
				String address = daeInfo.libraryImages.images.get(effect.diffuseTexture).init_from.replace("\\", "/");
				node = address.lastIndexOf("/");
				String imgName = address.substring(node+1,address.length());
				texAdres = root + "/" + imgName;
			}else {
				texAdres = "defaultTex/default.png";//默认纹理地址
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
