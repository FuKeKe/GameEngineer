package com.gameEngine.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by keke on 14-3-24.
 */
public class ShaderUtil
{
    /**
     * 读取 shader文件
     * @param shaderType
     * @param source
     * @return
     */
    public static int loadShader(int shaderType, String source)
    {
        int shader = GLES20.glCreateShader(shaderType);
        if(0 != shader)
        {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0);
            if(compiled[0] == 0)
            {
                Log.e("ES20_ERROR","不能编译 Shader "+shaderType + ":");
                Log.e("ES20_ERROR",""+GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 创建 program主体
     * @param vertexSource
     * @param fragmentSource
     * @return
     */
    public static int createProgram(String vertexSource, String fragmentSource)
    {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexSource);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);

        if(0 == vertexShader || 0 == fragmentShader)
        {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if(0 != program)
        {
            GLES20.glAttachShader(program,vertexShader);
            checkGlError("连接 vertexShader");
            GLES20.glAttachShader(program,fragmentShader);
            checkGlError("连接 fragmentShader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);
            if(linkStatus[0] != GLES20.GL_TRUE)
            {
                Log.e("ES20_ERROR", "不能链接 program :");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }
    public static void checkGlError(String op)
    {
        int error = 0;
        while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        {
            Log.e("ES20_ERROR", op + ":glError "+ error);
            throw new RuntimeException(op + ": glError "+ error);
        }
    }
    public static String loadFromAssertsFile(String fname, Resources r){
        String result = null;
        InputStream input = null;
        ByteArrayOutputStream baos = null;

        try {
            input = r.getAssets().open(fname);
            int ch = 0;
            baos = new ByteArrayOutputStream();
            while((ch = input.read())!= -1)
            {
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            result = new String(buff,"UTF-8");
            result = result.replace("\\r\\n","\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("assert 输入输出流错误 : "+e.toString());
        } finally {
            try{
                if(baos != null)baos.close();
                if(input != null)input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

}
