package com.example.myandroidlib;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.myandroidlib.Base.AppManager;
import com.example.myandroidlib.View.RiseNumberTextView;
import com.example.myandroidlib.View.RiseNumberTextView.EndListener;

public class RiseNumberActivity extends Activity {
	private RiseNumberTextView rnTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_risenumber);
		AppManager.getAppManager().addActivity(this);
		setupViews();

	}

	private void setupViews() {
		// 获取到RiseNumberTextView对象
		rnTextView = (RiseNumberTextView) findViewById(R.id.risenumber_textview);
		// 设置数据
		rnTextView.withNumber(12666.50f);
		// 设置动画播放时间
		//rnTextView.setDuration(5000);
		// 监听动画播放结束
		rnTextView.setOnEndListener(new EndListener() {

			@Override
			public void onEndFinish() {
				Toast.makeText(RiseNumberActivity.this, "数据增长完毕...",
						Toast.LENGTH_SHORT).show();
				
			}
		});
		
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(rnTextView.isRunning()){
					Toast.makeText(RiseNumberActivity.this, "数字还没增长完，请稍候尝试...", Toast.LENGTH_SHORT).show();
				}else{
					// 开始播放动画
					rnTextView.start();
				}
				
			}
		});
	}
}
