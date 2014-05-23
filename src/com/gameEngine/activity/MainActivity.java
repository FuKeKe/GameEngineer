package com.gameEngine.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.lovelove.R;
import com.gameEngine.view.MajorScene3;

public class MainActivity extends ActionBarActivity {

	MajorScene3 view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);												//屏幕方向
		requestWindowFeature(Window.FEATURE_NO_TITLE);																	//无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	//全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 屏幕常亮
		
		view = new MajorScene3(this);
		
		setContentView(R.layout.activity_main);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_Linear);
        linearLayout.addView(view);
        
        Button button0 = (Button) findViewById(R.id.button0);
        button0.setOnClickListener(new OnClickListener() {
			
        	@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		view.teapot.animationInterpolation(0);
        		Log.i("播放动画", ""+0);
			}
		});
		view.requestFocus();
		view.setFocusableInTouchMode(true);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		view.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		view.onResume();
	}

	

}
