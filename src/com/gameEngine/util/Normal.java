package com.gameEngine.util;

import java.util.Set;
//��ʾ���������࣬�����һ�������ʾһ��������
public class Normal 
{
   public static final float DIFF=0.0000001f;//�ж������������Ƿ���ͬ����ֵ
   //��������XYZ���ϵķ���
   float nx;
   float ny;
   float nz;
   
   public Normal(float nx,float ny,float nz)
   {
	   this.nx=nx;
	   this.ny=ny;
	   this.nz=nz;
   }
   
   @Override 
   public boolean equals(Object o)
   {
	   if(o instanceof  Normal)
	   {//������������XYZ���������ĲС��ָ������ֵ����Ϊ���������������
		   Normal tn=(Normal)o;
		   if(Math.abs(nx-tn.nx)<DIFF&&
			  Math.abs(ny-tn.ny)<DIFF&&
			  Math.abs(ny-tn.ny)<DIFF
             )
		   {
			   return true;
		   }
		   else
		   {
			   return false;
		   }
	   }
	   else
	   {
		   return false;
	   }
   }
   
   //����Ҫ�õ�HashSet�����һ��Ҫ��дhashCode����
   @Override
   public int hashCode()
   {
	   return 1;
   }
   
   //������ƽ��ֵ�Ĺ��߷���
   public static float[] getAverage(Set<Normal> sn)
   {
	   //��ŷ������͵�����
	   float[] result=new float[3];
	   //�Ѽ��������еķ��������
	   for(Normal n:sn)
	   {
		   result[0]+=n.nx;
		   result[1]+=n.ny;
		   result[2]+=n.nz;
	   }	   
	   //����ͺ�ķ��������
	   return vectorNormal(result);
   }
	//�������
	public static float[] vectorNormal(float[] vector)
	{
		//��������ģ
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
	//�����������Ĳ��
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2)
	{		
		//�������ʸ�����ʸ����XYZ��ķ���ABC
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		
		return new float[]{A,B,C};
	}
}
