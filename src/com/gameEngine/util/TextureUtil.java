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
	 * 根据 纹理图片在 assets中的地址进行加载
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
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);							//线性过渡
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);													//拉伸到边缘
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		Bitmap bitmapTmp;
		fileAddress = fileAddress.replace("\\", "/");										//统一“/” 和 “\”
		try {
			if(fileAddress.matches(".*\\.(bmp|png|jpg|bmp|gif)$")){
				InputStream is = mv.getResources().getAssets().open(fileAddress);
				bitmapTmp = BitmapFactory.decodeStream(is);
				is.close();
				// 实际加载纹理
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
				bitmapTmp.recycle();
			}
			if(fileAddress.matches(".*\\.(dds)$")){
				Log.e("本引擎暂不完美支持DDS 文件：（如有显示问题请转换成png纹理 ）", ""+fileAddress);
				String sdStatus = Environment.getExternalStorageState();  			//获得sd卡状态
				if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {  
			        Log.d("生成临时文件失败", "SD卡不可用，或者 不能读写");  
			        return -1; 
			    }
				int index = fileAddress.lastIndexOf("/");
				String fileName = fileAddress.substring(index,fileAddress.length());
				File file = new File(mv.getContext().getExternalFilesDir(null),fileName);			//保存地址(在程序目录下 data中的临时文件)
		        InputStream is = mv.getResources().getAssets().open(fileAddress);		//获得输入流
		        FileOutputStream stream = new FileOutputStream(file);  				//获得输出流
		        int b;
		        while (-1 != (b = is.read())) {										//写文件
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
				Log.e("纹理文件没有找到:", fileAddress);
				Log.e("加载默认纹理：", "default/default.png");
				InputStream is;
				try {
					is = mv.getResources().getAssets().open("default/default.png");
					bitmapTmp = BitmapFactory.decodeStream(is);
					is.close();
					// 实际加载纹理
					GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
					bitmapTmp.recycle();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Log.e("默认纹理没有找到：", "default/default.png");
				}
			}
		}
		return iTextureID;
	}
	/**
	 * 根据文件在Asset中的 ID 进行加载
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
		 //实际加载纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmapTmp,0);
        bitmapTmp.recycle();
		return iTextureID;
	}
}
