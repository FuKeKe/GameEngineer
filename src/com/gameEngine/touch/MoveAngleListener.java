package com.gameEngine.touch;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.gameEngine.util.GlobalMatrixState;
import com.gameEngine.view.MajorScene1;
import com.gameEngine.view.MajorScene2;

public class MoveAngleListener implements OnTouchListener {
	int mode = 0; // ʶ�𵽵ĵ���
	float oldDist = 0; // �ϴ��������
	float[] camera = { 0.0f, 80f, -500, 0f, 80f, 0f, 0.0f, 1.0f, 0.0f };
	float yAngle = 0;// �������y����ת�ĽǶ�
	float xAngle = 0;// �������X����ת�ĽǶ�
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���
	private float mPreviousX;// �ϴεĴ���λ��X����
	private float mPreviousY;// �ϴεĴ���λ��Y����
	String clazzName = "View";

	public MoveAngleListener(Object o) {
		clazzName = o.getClass().getName();
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		// TODO Auto-generated method stub
		// �����¼��ص�����
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
			// ���غ���λ��̫����y����ת
			float dx = x - mPreviousX;// ���㴥�ر�Xλ��
			yAngle += dx * TOUCH_SCALE_FACTOR;// ����̫����y����ת�ĽǶ�

			/*
			 * if(yAngle>90) { yAngle=90; } else if(yAngle<-90) { yAngle=-90; }
			 */

			// ��������λ���������Y����ת -90��+90
			float dy = y - mPreviousY;// ���㴥�ر�Yλ��
			xAngle += dy * TOUCH_SCALE_FACTOR;// ����̫����y����ת�ĽǶ�

			/*
			 * if(xAngle>90) { xAngle=90; } else if(xAngle<-90) { xAngle=-90; }
			 */
			// ��ת����
			double radianY = Math.toRadians(yAngle);
			double radianX = Math.toRadians(xAngle);

			// ��Y
			double disY = Math.sqrt(camera[0] * camera[0]
					+ camera[2] * camera[2]); // ����ԭ���xzƽ�����
			camera[0] = (float) (disY * Math.cos(radianY)); // x
			camera[2] = (float) (disY * Math.sin(radianY)); // y

			disY = Math.sqrt(camera[0] * camera[0] + camera[2]
					* camera[2]); // ����ԭ���xzƽ�����
			camera[6] = (float) (disY * Math.cos(radianY)); // ����x
			camera[8] = (float) (disY * Math.cos(radianY)); // ����y
			// ��X
			double disX = Math.sqrt(camera[1] * camera[1]
					+ camera[2] * camera[2]); // ����ԭ���yzƽ�����
			camera[1] = (float) (disX * Math.cos(radianX)); // y
			camera[2] = (float) (disX * Math.sin(radianX)); // z

			disX = Math.sqrt(camera[7] * camera[7] + camera[8]
					* camera[8]); // ����ԭ���yzƽ�����
			camera[7] = (float) (disX * Math.cos(radianX)); // ����x
			camera[8] = (float) (disX * Math.cos(radianX)); // ����y
			System.out.println(camera[0] + "  |  " + camera[1]
					+ "  |  " + camera[2] + "  |  " + camera[3]
					+ "  |  " + camera[4] + "  |  " + camera[5]
					+ "  |  " + camera[6] + "  |  " + camera[7]
					+ "  |  " + camera[8] + "  |  ");
			GlobalMatrixState.setCameraLocation(camera);
		}
		mPreviousX = x;// ��¼���ر�λ��
		mPreviousY = y;
		return true;
	}

}
