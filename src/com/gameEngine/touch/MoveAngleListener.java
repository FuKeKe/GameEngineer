package com.gameEngine.touch;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.gameEngine.util.GlobalMatrixState;
import com.gameEngine.view.MajorScene1;
import com.gameEngine.view.MajorScene2;

public class MoveAngleListener implements OnTouchListener {
	int mode = 0; // 识别到的点数
	float oldDist = 0; // 上次两点距离
	float[] camera = { 0.0f, 80f, -500, 0f, 80f, 0f, 0.0f, 1.0f, 0.0f };
	float yAngle = 0;// 摄像机绕y轴旋转的角度
	float xAngle = 0;// 摄像机绕X轴旋转的角度
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// 角度缩放比例
	private float mPreviousX;// 上次的触控位置X坐标
	private float mPreviousY;// 上次的触控位置Y坐标
	String clazzName = "View";

	public MoveAngleListener(Object o) {
		clazzName = o.getClass().getName();
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		// TODO Auto-generated method stub
		// 触摸事件回调方法
		if("MajorScene1".equals(clazzName)){
			camera = ((MajorScene1)v).camera;
		}
		if("MajorScene2".equals(clazzName)){
			camera = ((MajorScene2)v).camera;
		}
		float x = e.getX();
		float y = e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// 触控横向位移太阳绕y轴旋转
			float dx = x - mPreviousX;// 计算触控笔X位移
			yAngle += dx * TOUCH_SCALE_FACTOR;// 设置太阳绕y轴旋转的角度

			/*
			 * if(yAngle>90) { yAngle=90; } else if(yAngle<-90) { yAngle=-90; }
			 */

			// 触控纵向位移摄像机绕Y轴旋转 -90～+90
			float dy = y - mPreviousY;// 计算触控笔Y位移
			xAngle += dy * TOUCH_SCALE_FACTOR;// 设置太阳绕y轴旋转的角度

			/*
			 * if(xAngle>90) { xAngle=90; } else if(xAngle<-90) { xAngle=-90; }
			 */
			// 旋转弧度
			double radianY = Math.toRadians(yAngle);
			double radianX = Math.toRadians(xAngle);

			// 绕Y
			double disY = Math.sqrt(camera[0] * camera[0]
					+ camera[2] * camera[2]); // 距离原点的xz平面距离
			camera[0] = (float) (disY * Math.cos(radianY)); // x
			camera[2] = (float) (disY * Math.sin(radianY)); // y

			disY = Math.sqrt(camera[0] * camera[0] + camera[2]
					* camera[2]); // 距离原点的xz平面距离
			camera[6] = (float) (disY * Math.cos(radianY)); // 方向x
			camera[8] = (float) (disY * Math.cos(radianY)); // 方向y
			// 绕X
			double disX = Math.sqrt(camera[1] * camera[1]
					+ camera[2] * camera[2]); // 距离原点的yz平面距离
			camera[1] = (float) (disX * Math.cos(radianX)); // y
			camera[2] = (float) (disX * Math.sin(radianX)); // z

			disX = Math.sqrt(camera[7] * camera[7] + camera[8]
					* camera[8]); // 距离原点的yz平面距离
			camera[7] = (float) (disX * Math.cos(radianX)); // 方向x
			camera[8] = (float) (disX * Math.cos(radianX)); // 方向y
			System.out.println(camera[0] + "  |  " + camera[1]
					+ "  |  " + camera[2] + "  |  " + camera[3]
					+ "  |  " + camera[4] + "  |  " + camera[5]
					+ "  |  " + camera[6] + "  |  " + camera[7]
					+ "  |  " + camera[8] + "  |  ");
			GlobalMatrixState.setCameraLocation(camera);
		}
		mPreviousX = x;// 记录触控笔位置
		mPreviousY = y;
		return true;
	}

}
