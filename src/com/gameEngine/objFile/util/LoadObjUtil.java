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
		ObjectGroups resultGroups = new ObjectGroups();						//�����õĽ����
			
	    	ArrayList<Float> alv=new ArrayList<Float>();					//ԭʼ���������б�--ֱ�Ӵ�obj�ļ��м���
	    	
	    	ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();		//������װ�������б�--���������Ϣ���ļ��м���
	    	
	    	ArrayList<Float> alvResult=new ArrayList<Float>(); 				//������������б�--������֯��
	    	
	    	
	    	//ƽ��ǰ����������Ӧ�ĵ�ķ���������Map
	    	//��HashMap��keyΪ��������� valueΪ�����ڵĸ�����ķ������ļ���
	    	HashMap<Integer,HashSet<Normal>> hmn=new HashMap<Integer,HashSet<Normal>>();
	    	
	    	ArrayList<Float> aln = new ArrayList<Float>();
	    	
	    	ArrayList<Float> alnResult = new ArrayList<Float>();
	    
	    	
	    	ArrayList<Float> alt=new ArrayList<Float>();  					//ԭʼ���������б�
	    	
	    	ArrayList<Float> altResult=new ArrayList<Float>(); 				//�����������б�
	    	
	    	try
	    	{
	    		InputStream in=r.getAssets().open(fname);
	    		InputStreamReader isr=new InputStreamReader(in);
	    		BufferedReader br=new BufferedReader(isr);
	    		String temps=null;
	    		
	    		ObjectGroupInfo info = new ObjectGroupInfo();							//��һ����Ϣ�ڵ�
	    		String groupName = fname;									//��ǰ�ڵ������
	    		String textureName = null;									//��ǰ�ڵ������
			    while((temps=br.readLine())!=null) 
			    {
			    	String[] tempsa=temps.split("[ ]+");					//�ÿո�ָ����еĸ�����ɲ���
			      	if("v".equals(tempsa[0].trim()))						//����Ϊ��������
			      	{
			      	    //��Ϊ��������������ȡ���˶����XYZ������ӵ�ԭʼ���������б���
			      		alv.add(Float.parseFloat(tempsa[1]));
			      		alv.add(Float.parseFloat(tempsa[2]));
			      		alv.add(Float.parseFloat(tempsa[3]));
			      	}
			      	else if("vn".equals(tempsa[0].trim())){
			      		aln.add(Float.parseFloat(tempsa[1]));
			      		aln.add(Float.parseFloat(tempsa[2]));
			      		aln.add(Float.parseFloat(tempsa[3]));
			      	}
			      	else if("vt".equals(tempsa[0].trim()))					//����Ϊ����������
			      	{
			      		//��Ϊ��������������ȡST���겢��ӽ�ԭʼ���������б���
			      		alt.add(Float.parseFloat(tempsa[1]));
			      		alt.add(Float.parseFloat(tempsa[2])); 
			      	}
			      	else if("f".equals(tempsa[0].trim())) 					//����Ϊ��������
			      	{
			      		/*
			      		 *��Ϊ��������������� �����Ķ����������ԭʼ���������б���
			      		 *��ȡ��Ӧ�Ķ�������ֵ��ӵ�������������б��У�ͬʱ��������
			      		 *�����������������ķ���������ӵ�ƽ��ǰ����������Ӧ�ĵ�
			      		 *�ķ�����������ɵ�Map��
			      		*/
			      		
			      		int[] index=new int[4];//�ĸ���������ֵ������
			      		
			      		//�����0�����������������ȡ�˶����XYZ��������	      		
			      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
			      		float x0=alv.get(3*index[0]);
			      		float y0=alv.get(3*index[0]+1);
			      		float z0=alv.get(3*index[0]+2);
			      		alvResult.add(x0);
			      		alvResult.add(y0);
			      		alvResult.add(z0);		
			      		
			      	    //�����1�����������������ȡ�˶����XYZ��������	  
			      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
			      		float x1=alv.get(3*index[1]);
			      		float y1=alv.get(3*index[1]+1);
			      		float z1=alv.get(3*index[1]+2);
			      		alvResult.add(x1);
			      		alvResult.add(y1);
			      		alvResult.add(z1);
			      		
			      	    //�����2�����������������ȡ�˶����XYZ��������	
			      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
			      		float x2=alv.get(3*index[2]);
			      		float y2=alv.get(3*index[2]+1);
			      		float z2=alv.get(3*index[2]+2);
			      		alvResult.add(x2);
			      		alvResult.add(y2); 
			      		alvResult.add(z2);	
			      		
			      		//��¼����Ķ�������
			      		alFaceIndex.add(index[0]);
			      		alFaceIndex.add(index[1]);
			      		alFaceIndex.add(index[2]);
			      		
			      		if(tempsa[1].split("/").length > 2){//���ȴ���2˵���з�����
			      		//�����0�����������������ȡ�˶����XYZ��������	      		
				      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
				      		float nx0=aln.get(3*index[0]);
				      		float ny0=aln.get(3*index[0]+1);
				      		float nz0=aln.get(3*index[0]+2);
				      		alnResult.add(nx0);
				      		alnResult.add(ny0);
				      		alnResult.add(nz0);		
				      		
				      	    //�����1�����������������ȡ�˶����XYZ��������	  
				      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
				      		float nx1=aln.get(3*index[1]);
				      		float ny1=aln.get(3*index[1]+1);
				      		float nz1=aln.get(3*index[1]+2);
				      		alnResult.add(nx1);
				      		alnResult.add(ny1);
				      		alnResult.add(nz1);
				      		
				      	    //�����2�����������������ȡ�˶����XYZ��������	
				      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
				      		float nx2=aln.get(3*index[2]);
				      		float ny2=aln.get(3*index[2]+1);
				      		float nz2=aln.get(3*index[2]+2);
				      		alnResult.add(nx2);
				      		alnResult.add(ny2); 
				      		alnResult.add(nz2);
			      		}else{
				      		//ͨ��������������������0-1��0-2�����õ�����ķ�����
				      	    //��0�ŵ㵽1�ŵ������
				      		float vxa=x1-x0;
				      		float vya=y1-y0;
				      		float vza=z1-z0;
				      	    //��0�ŵ㵽2�ŵ������
				      		float vxb=x2-x0;
				      		float vyb=y2-y0;
				      		float vzb=z2-z0;
				      	    //ͨ�������������Ĳ�����㷨����
				      		float[] vNormal=Normal.vectorNormal(Normal.getCrossProduct(vxa,vya,vza,vxb,vyb,vzb));		      	    
				      		for(int tempInxex:index){
				      			HashSet<Normal> hsn=hmn.get(tempInxex);
				      			if(hsn==null){
				      				hsn=new HashSet<Normal>();
				      			}
				      			hsn.add(new Normal(vNormal[0],vNormal[1],vNormal[2]));
				      			hmn.put(tempInxex, hsn);//�����ϷŽ�HsahMap��
				      		}
			      		}
			      		//������������֯��������������б���
			      		//��1���������������
			      		int indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;
			      		altResult.add(alt.get(indexTex*2));
			      		altResult.add(alt.get(indexTex*2+1));
			      	    //��2���������������
			      		indexTex=Integer.parseInt(tempsa[2].split("/")[1])-1;
			      		altResult.add(alt.get(indexTex*2));
			      		altResult.add(alt.get(indexTex*2+1));
			      	    //��3���������������
			      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;
			      		altResult.add(alt.get(indexTex*2));
			      		altResult.add(alt.get(indexTex*2+1));
			      		
			      		
			      		
			      		
			      		
			      		
			      		
			      		
			      		
			      		
			      		//����е�4��������������꣺
			      		if(tempsa.length > 4){
			      			//����е��ĸ����㣬�ͼ��ص��ĸ��� �� ������һ����
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
				      		if(tempsa[1].split("/").length > 2){//���ȴ���2˵���з�����
					      			//�����0�����������������ȡ�˶����XYZ��������	      		
						      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
						      		float nx0=aln.get(3*index[0]);
						      		float ny0=aln.get(3*index[0]+1);
						      		float nz0=aln.get(3*index[0]+2);
						      		alnResult.add(nx0);
						      		alnResult.add(ny0);
						      		alnResult.add(nz0);
						      		
						      	    //�����2�����������������ȡ�˶����XYZ��������	
						      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
						      		float nx2=aln.get(3*index[2]);
						      		float ny2=aln.get(3*index[2]+1);
						      		float nz2=aln.get(3*index[2]+2);
						      		alnResult.add(nx2);
						      		alnResult.add(ny2); 
						      		alnResult.add(nz2);
						      		
						      		//�����3�����������������ȡ�˶����XYZ��������	
						      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
						      		float nx3=aln.get(3*index[3]);
						      		float ny3=aln.get(3*index[3]+1);
						      		float nz3=aln.get(3*index[3]+2);
						      		alnResult.add(nx3);
						      		alnResult.add(ny3); 
						      		alnResult.add(nz3);
					      		}else{
						      		//ͨ��������������������0-1��0-2�����õ�����ķ�����
						      	    //��0�ŵ㵽1�ŵ������
					      			float vxa1=x2-x0;
						      		float vya1=y2-y0;
						      		float vza1=z2-z0;
						      	    //��0�ŵ㵽2�ŵ������
						      		float vxb1=x3-x0;
						      		float vyb1=y3-y0;
						      		float vzb1=z3-z0;
						      	    //ͨ�������������Ĳ�����㷨����
						      		float[] vNormal1=Normal.vectorNormal(Normal.getCrossProduct(vxa1,vya1,vza1,vxb1,vyb1,vzb1));		      	    
						      		for(int tempInxex:index){
						      			HashSet<Normal> hsn=hmn.get(tempInxex);
						      			if(hsn==null){
						      				hsn=new HashSet<Normal>();
						      			}
						      			hsn.add(new Normal(vNormal1[0],vNormal1[1],vNormal1[2]));
						      			hmn.put(tempInxex, hsn);//�����ϷŽ�HsahMap��
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
			      		//�����е������ɳ�ȥ
						if(0 != alFaceIndex.size()){
							//info.setIndex(alFaceIndex);//(ѡ ��)
							
						    if(0 == alnResult.size()){		//���ԭ�ļ���û�з�����
						    	float[] nXYZ=new float[alFaceIndex.size()*3];
						    	int c=0;
							    for(Integer i:alFaceIndex)
							    {
							    	//���ݵ�ǰ���������Map��ȡ��һ���������ļ���
							    	HashSet<Normal> hsn=hmn.get(i);
							    	//���ƽ��������
							    	float[] tn=Normal.getAverage(hsn);	
							    	//���������ƽ����������ŵ�������������
							    	nXYZ[c++]=tn[0];
							    	nXYZ[c++]=tn[1];
							    	nXYZ[c++]=tn[2];
							    }
							    info.setNormal(nXYZ);				//������	
						    }else{
						    	info.setNormal(alnResult);				//������
						    }
							info.setVertices(alvResult);		//��������
							info.setTexture(altResult);			//�����������б�
							info.setGroupName(groupName);		//�����������
							info.setTextureName(textureName);	//������mtl�е�����
							resultGroups.getAlPartsInfos().add(info);
							
							//��Ӻ�,���ԭ��Ϣ
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
			      	else if("mtllib".equals(tempsa[0].trim())){		//���ʿ��ļ�����
			      		resultGroups.setMtlName(tempsa[1]);
					}
			      	else if("usemtl".equals(tempsa[0].trim())){		//���ʿ��ļ��ڵ�����
			      		textureName = tempsa[1];
					}
			    } 
			    
			    //ѭ���������ɸ���
			    //info.setIndex(alFaceIndex);//(ѡ ��)
			    if(0 == alnResult.size()){		//���ԭ�ļ���û�з�����
					float[] nXYZ=new float[alFaceIndex.size()*3];
				    int c=0;
				    for(Integer i:alFaceIndex)
				    {
				    	//���ݵ�ǰ���������Map��ȡ��һ���������ļ���
				    	HashSet<Normal> hsn=hmn.get(i);
				    	//���ƽ��������
				    	float[] tn=Normal.getAverage(hsn);	
				    	//���������ƽ����������ŵ�������������
				    	nXYZ[c++]=tn[0];
				    	nXYZ[c++]=tn[1];
				    	nXYZ[c++]=tn[2];
				    }
					info.setNormal(nXYZ);				//������
			    }else{
			    	info.setNormal(alnResult);				//������
			    }
				info.setVertices(alvResult);		//��������
				info.setTexture(altResult);			//�����������б�
				info.setGroupName(groupName);		//�����������
				info.setTextureName(textureName);	//������mtl�е�����
				resultGroups.getAlPartsInfos().add(info);
				
				//��Ӻ�,���ԭ��Ϣ
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
