package com.gameEngine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;

import com.gameEngine.util.dds2bmp.ByteFileReader;
import com.gameEngine.util.dds2bmp.PluginDDS;
import com.gameEngine.util.dds2bmp.PluginDDSFactory;

public class TextureUtil {
	/**
	 * ���� ����ͼƬ�� assets�еĵ�ַ���м���
	 * @param fileAddress
	 * @param mv
	 * @return
	 */
	public static int initTextureFromAssets(String fileAddress, GLSurfaceView mv) {
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		int iTextureID = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTextureID);

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);							//���Թ���
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);													//���쵽��Ե
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		Bitmap bitmapTmp;
		fileAddress = fileAddress.replace("\\", "/");										//ͳһ��/�� �� ��\��
		try {
			if(fileAddress.matches(".*\\.(bmp|png|jpg|bmp|gif)$")){
				InputStream is = mv.getResources().getAssets().open(fileAddress);
				bitmapTmp = BitmapFactory.decodeStream(is);
				is.close();
				// ʵ�ʼ�������
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
				bitmapTmp.recycle();
			}
			if(fileAddress.matches(".*\\.(dds)$")){
				Log.e("�������ݲ�����֧��DDS �ļ�����������ʾ������ת����png���� ��", ""+fileAddress);
				String sdStatus = Environment.getExternalStorageState();  			//���sd��״̬
				if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {  
			        Log.d("������ʱ�ļ�ʧ��", "SD�������ã����� ���ܶ�д");  
			        return -1; 
			    }
				int index = fileAddress.lastIndexOf("/");
				String fileName = fileAddress.substring(index,fileAddress.length());
				File file = new File(mv.getContext().getExternalFilesDir(null),fileName);			//�����ַ(�ڳ���Ŀ¼�� data�е���ʱ�ļ�)
		        InputStream is = mv.getResources().getAssets().open(fileAddress);		//���������
		        FileOutputStream stream = new FileOutputStream(file);  				//��������
		        int b;
		        while (-1 != (b = is.read())) {										//д�ļ�
					stream.write(b);		
				}          
		        stream.close(); 
		        
				PluginDDS dds = PluginDDSFactory.getPluginDDS();
				bitmapTmp = dds.readDDSFile(new ByteFileReader(file,"r"));
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
				bitmapTmp.recycle();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			if(e.getClass().equals(FileNotFoundException.class)){
				Log.e("�����ļ�û���ҵ�:", fileAddress);
				Log.e("����Ĭ������", "default/default.png");
				InputStream is;
				try {
					is = mv.getResources().getAssets().open("default/default.png");
					bitmapTmp = BitmapFactory.decodeStream(is);
					is.close();
					// ʵ�ʼ�������
					GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
					bitmapTmp.recycle();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Log.e("Ĭ������û���ҵ���", "default/default.png");
				}
			}
		}
		return iTextureID;
	}
	/**
	 * �����ļ���Asset�е� ID ���м���
	 * @param drawableID
	 * @param mv
	 * @return
	 */
	public static int initTextureFromAssets(int drawableID,GLSurfaceView mv){
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		int iTextureID = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTextureID);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
		
		InputStream is = mv.getResources().openRawResource(drawableID);
		Bitmap bitmapTmp = BitmapFactory.decodeStream(is);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //ʵ�ʼ�������
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmapTmp,0);
        bitmapTmp.recycle();
		return iTextureID;
	}
}
