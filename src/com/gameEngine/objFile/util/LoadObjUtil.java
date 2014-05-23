package com.gameEngine.objFile.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Log;

import com.gameEngine.objFile.extend.ObjectGroupInfo;
import com.gameEngine.objFile.extend.ObjectGroups;
import com.gameEngine.util.Normal;

public class LoadObjUtil {
	@SuppressLint("UseSparseArrays")
	public static ObjectGroups loadObjFromAssert(String fname, Resources r){
		ObjectGroups resultGroups = new ObjectGroups();						//返回用的结果集
			
	    	ArrayList<Float> alv=new ArrayList<Float>();					//原始顶点坐标列表--直接从obj文件中加载
	    	
	    	ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();		//顶点组装面索引列表--根据面的信息从文件中加载
	    	
	    	ArrayList<Float> alvResult=new ArrayList<Float>(); 				//结果顶点坐标列表--按面组织好
	    	
	    	
	    	//平均前各个索引对应的点的法向量集合Map
	    	//此HashMap的key为点的索引， value为点所在的各个面的法向量的集合
	    	HashMap<Integer,HashSet<Normal>> hmn=new HashMap<Integer,HashSet<Normal>>();
	    	
	    	ArrayList<Float> aln = new ArrayList<Float>();
	    	
	    	ArrayList<Float> alnResult = new ArrayList<Float>();
	    
	    	
	    	ArrayList<Float> alt=new ArrayList<Float>();  					//原始纹理坐标列表
	    	
	    	ArrayList<Float> altResult=new ArrayList<Float>(); 				//纹理坐标结果列表
	    	
	    	try
	    	{
	    		InputStream in=r.getAssets().open(fname);
	    		InputStreamReader isr=new InputStreamReader(in);
	    		BufferedReader br=new BufferedReader(isr);
	    		String temps=null;
	    		
	    		ObjectGroupInfo info = new ObjectGroupInfo();							//第一个信息节点
	    		String groupName = fname;									//当前节点的名字
	    		String textureName = null;									//当前节点的纹理
			    while((temps=br.readLine())!=null) 
			    {
			    	String[] tempsa=temps.split("[ ]+");					//用空格分割行中的各个组成部分
			      	if("v".equals(tempsa[0].trim()))						//此行为顶点坐标
			      	{
			      	    //若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
			      		alv.add(Float.parseFloat(tempsa[1]));
			      		alv.add(Float.parseFloat(tempsa[2]));
			      		alv.add(Float.parseFloat(tempsa[3]));
			      	}
			      	else if("vn".equals(tempsa[0].trim())){
			      		aln.add(Float.parseFloat(tempsa[1]));
			      		aln.add(Float.parseFloat(tempsa[2]));
			      		aln.add(Float.parseFloat(tempsa[3]));
			      	}
			      	else if("vt".equals(tempsa[0].trim()))					//此行为纹理坐标行
			      	{
			      		//若为纹理坐标行则提取ST坐标并添加进原始纹理坐标列表中
			      		alt.add(Float.parseFloat(tempsa[1]));
			      		alt.add(Float.parseFloat(tempsa[2])); 
			      	}
			      	else if("f".equals(tempsa[0].trim())) 					//此行为三角形面
			      	{
			      		/*
			      		 *若为三角形面行则根据 组成面的顶点的索引从原始顶点坐标列表中
			      		 *提取相应的顶点坐标值添加到结果顶点坐标列表中，同时根据三个
			      		 *顶点的坐标计算出此面的法向量并添加到平均前各个索引对应的点
			      		 *的法向量集合组成的Map中
			      		*/
			      		
			      		int[] index=new int[4];//四个顶点索引值的数组
			      		
			      		//计算第0个顶点的索引，并获取此顶点的XYZ三个坐标	      		
			      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
			      		float x0=alv.get(3*index[0]);
			      		float y0=alv.get(3*index[0]+1);
			      		float z0=alv.get(3*index[0]+2);
			      		alvResult.add(x0);
			      		alvResult.add(y0);
			      		alvResult.add(z0);		
			      		
			      	    //计算第1个顶点的索引，并获取此顶点的XYZ三个坐标	  
			      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
			      		float x1=alv.get(3*index[1]);
			      		float y1=alv.get(3*index[1]+1);
			      		float z1=alv.get(3*index[1]+2);
			      		alvResult.add(x1);
			      		alvResult.add(y1);
			      		alvResult.add(z1);
			      		
			      	    //计算第2个顶点的索引，并获取此顶点的XYZ三个坐标	
			      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
			      		float x2=alv.get(3*index[2]);
			      		float y2=alv.get(3*index[2]+1);
			      		float z2=alv.get(3*index[2]+2);
			      		alvResult.add(x2);
			      		alvResult.add(y2); 
			      		alvResult.add(z2);	
			      		
			      		//记录此面的顶点索引
			      		alFaceIndex.add(index[0]);
			      		alFaceIndex.add(index[1]);
			      		alFaceIndex.add(index[2]);
			      		
			      		if(tempsa[1].split("/").length > 2){//长度大于2说明有法向量
			      		//计算第0个顶点的索引，并获取此顶点的XYZ三个坐标	      		
				      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
				      		float nx0=aln.get(3*index[0]);
				      		float ny0=aln.get(3*index[0]+1);
				      		float nz0=aln.get(3*index[0]+2);
				      		alnResult.add(nx0);
				      		alnResult.add(ny0);
				      		alnResult.add(nz0);		
				      		
				      	    //计算第1个顶点的索引，并获取此顶点的XYZ三个坐标	  
				      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
				      		float nx1=aln.get(3*index[1]);
				      		float ny1=aln.get(3*index[1]+1);
				      		float nz1=aln.get(3*index[1]+2);
				      		alnResult.add(nx1);
				      		alnResult.add(ny1);
				      		alnResult.add(nz1);
				      		
				      	    //计算第2个顶点的索引，并获取此顶点的XYZ三个坐标	
				      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
				      		float nx2=aln.get(3*index[2]);
				      		float ny2=aln.get(3*index[2]+1);
				      		float nz2=aln.get(3*index[2]+2);
				      		alnResult.add(nx2);
				      		alnResult.add(ny2); 
				      		alnResult.add(nz2);
			      		}else{
				      		//通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
				      	    //求0号点到1号点的向量
				      		float vxa=x1-x0;
				      		float vya=y1-y0;
				      		float vza=z1-z0;
				      	    //求0号点到2号点的向量
				      		float vxb=x2-x0;
				      		float vyb=y2-y0;
				      		float vzb=z2-z0;
				      	    //通过求两个向量的叉积计算法向量
				      		float[] vNormal=Normal.vectorNormal(Normal.getCrossProduct(vxa,vya,vza,vxb,vyb,vzb));		      	    
				      		for(int tempInxex:index){
				      			HashSet<Normal> hsn=hmn.get(tempInxex);
				      			if(hsn==null){
				      				hsn=new HashSet<Normal>();
				      			}
				      			hsn.add(new Normal(vNormal[0],vNormal[1],vNormal[2]));
				      			hmn.put(tempInxex, hsn);//将集合放进HsahMap中
				      		}
			      		}
			      		//将纹理坐标组织到结果纹理坐标列表中
			      		//第1个顶点的纹理坐标
			      		int indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;
			      		altResult.add(alt.get(indexTex*2));
			      		altResult.add(alt.get(indexTex*2+1));
			      	    //第2个顶点的纹理坐标
			      		indexTex=Integer.parseInt(tempsa[2].split("/")[1])-1;
			      		altResult.add(alt.get(indexTex*2));
			      		altResult.add(alt.get(indexTex*2+1));
			      	    //第3个顶点的纹理坐标
			      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;
			      		altResult.add(alt.get(indexTex*2));
			      		altResult.add(alt.get(indexTex*2+1));
			      		
			      		
			      		
			      		
			      		
			      		
			      		
			      		
			      		
			      		
			      		//如果有第4个顶点的纹理坐标：
			      		if(tempsa.length > 4){
			      			//如果有第四个顶点，就加载第四个点 并 再生成一个面
					      	index[3]=Integer.parseInt(tempsa[4].split("/")[0])-1;
					      	float x3=alv.get(3*index[3]);
					      	float y3=alv.get(3*index[3]+1);
					      	float z3=alv.get(3*index[3]+2);
					      	
					      	alvResult.add(x0);
					      	alvResult.add(y0); 
					      	alvResult.add(z0);
					      	alvResult.add(x2);
					      	alvResult.add(y2); 
					      	alvResult.add(z2);
					      	alvResult.add(x3);
					      	alvResult.add(y3); 
					      	alvResult.add(z3);
					      	
					      	alFaceIndex.add(index[0]);
				      		alFaceIndex.add(index[2]);
				      		alFaceIndex.add(index[3]);
				      		if(tempsa[1].split("/").length > 2){//长度大于2说明有法向量
					      			//计算第0个顶点的索引，并获取此顶点的XYZ三个坐标	      		
						      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
						      		float nx0=aln.get(3*index[0]);
						      		float ny0=aln.get(3*index[0]+1);
						      		float nz0=aln.get(3*index[0]+2);
						      		alnResult.add(nx0);
						      		alnResult.add(ny0);
						      		alnResult.add(nz0);
						      		
						      	    //计算第2个顶点的索引，并获取此顶点的XYZ三个坐标	
						      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
						      		float nx2=aln.get(3*index[2]);
						      		float ny2=aln.get(3*index[2]+1);
						      		float nz2=aln.get(3*index[2]+2);
						      		alnResult.add(nx2);
						      		alnResult.add(ny2); 
						      		alnResult.add(nz2);
						      		
						      		//计算第3个顶点的索引，并获取此顶点的XYZ三个坐标	
						      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
						      		float nx3=aln.get(3*index[3]);
						      		float ny3=aln.get(3*index[3]+1);
						      		float nz3=aln.get(3*index[3]+2);
						      		alnResult.add(nx3);
						      		alnResult.add(ny3); 
						      		alnResult.add(nz3);
					      		}else{
						      		//通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
						      	    //求0号点到1号点的向量
					      			float vxa1=x2-x0;
						      		float vya1=y2-y0;
						      		float vza1=z2-z0;
						      	    //求0号点到2号点的向量
						      		float vxb1=x3-x0;
						      		float vyb1=y3-y0;
						      		float vzb1=z3-z0;
						      	    //通过求两个向量的叉积计算法向量
						      		float[] vNormal1=Normal.vectorNormal(Normal.getCrossProduct(vxa1,vya1,vza1,vxb1,vyb1,vzb1));		      	    
						      		for(int tempInxex:index){
						      			HashSet<Normal> hsn=hmn.get(tempInxex);
						      			if(hsn==null){
						      				hsn=new HashSet<Normal>();
						      			}
						      			hsn.add(new Normal(vNormal1[0],vNormal1[1],vNormal1[2]));
						      			hmn.put(tempInxex, hsn);//将集合放进HsahMap中
						      		}
					      		}
				      		
				      		
				      		indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;
				      		altResult.add(alt.get(indexTex*2));
				      		altResult.add(alt.get(indexTex*2+1));
				      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;
				      		altResult.add(alt.get(indexTex*2));
				      		altResult.add(alt.get(indexTex*2+1));
			      			indexTex=Integer.parseInt(tempsa[4].split("/")[1])-1;
				      		altResult.add(alt.get(indexTex*2));
				      		altResult.add(alt.get(indexTex*2+1));
			      		}
			      	}
			      	else if("g".equals(tempsa[0].trim())){
			      		//将已有的组生成出去
						if(0 != alFaceIndex.size()){
							//info.setIndex(alFaceIndex);//(选 用)
							
						    if(0 == alnResult.size()){		//如果原文件里没有法向量
						    	float[] nXYZ=new float[alFaceIndex.size()*3];
						    	int c=0;
							    for(Integer i:alFaceIndex)
							    {
							    	//根据当前点的索引从Map中取出一个法向量的集合
							    	HashSet<Normal> hsn=hmn.get(i);
							    	//求出平均法向量
							    	float[] tn=Normal.getAverage(hsn);	
							    	//将计算出的平均法向量存放到法向量数组中
							    	nXYZ[c++]=tn[0];
							    	nXYZ[c++]=tn[1];
							    	nXYZ[c++]=tn[2];
							    }
							    info.setNormal(nXYZ);				//法向量	
						    }else{
						    	info.setNormal(alnResult);				//法向量
						    }
							info.setVertices(alvResult);		//顶点坐标
							info.setTexture(altResult);			//纹理坐标结果列表
							info.setGroupName(groupName);		//设置组的名字
							info.setTextureName(textureName);	//纹理在mtl中的名称
							resultGroups.getAlPartsInfos().add(info);
							
							//添加后,清空原信息
							alFaceIndex.clear();
							alvResult.clear();
							altResult.clear();
							groupName = null;
							textureName = null;
							info = new ObjectGroupInfo();
						}else{
							groupName = tempsa[1];
						}
					}
			      	else if("mtllib".equals(tempsa[0].trim())){		//材质库文件名字
			      		resultGroups.setMtlName(tempsa[1]);
					}
			      	else if("usemtl".equals(tempsa[0].trim())){		//材质库文件内的名字
			      		textureName = tempsa[1];
					}
			    } 
			    
			    //循环完再生成个组
			    //info.setIndex(alFaceIndex);//(选 用)
			    if(0 == alnResult.size()){		//如果原文件里没有法向量
					float[] nXYZ=new float[alFaceIndex.size()*3];
				    int c=0;
				    for(Integer i:alFaceIndex)
				    {
				    	//根据当前点的索引从Map中取出一个法向量的集合
				    	HashSet<Normal> hsn=hmn.get(i);
				    	//求出平均法向量
				    	float[] tn=Normal.getAverage(hsn);	
				    	//将计算出的平均法向量存放到法向量数组中
				    	nXYZ[c++]=tn[0];
				    	nXYZ[c++]=tn[1];
				    	nXYZ[c++]=tn[2];
				    }
					info.setNormal(nXYZ);				//法向量
			    }else{
			    	info.setNormal(alnResult);				//法向量
			    }
				info.setVertices(alvResult);		//顶点坐标
				info.setTexture(altResult);			//纹理坐标结果列表
				info.setGroupName(groupName);		//设置组的名字
				info.setTextureName(textureName);	//纹理在mtl中的名称
				resultGroups.getAlPartsInfos().add(info);
				
				//添加后,清空原信息
				alFaceIndex.clear();
				alvResult.clear();
				altResult.clear();
				alnResult.clear();
				groupName = null;
				textureName = null;
	    	}
	    	catch(Exception e)
	    	{
	    		Log.d("load error", "load error");
	    		e.printStackTrace();
	    	}
	    	
	    	
	    		    	
	    	return resultGroups;
		}
		
		
		
}
