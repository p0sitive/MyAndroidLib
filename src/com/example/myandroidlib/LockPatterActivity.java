package com.example.myandroidlib;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.myandroidlib.View.LockPatterView;
import com.example.myandroidlib.View.LockPatterView.OnPatterChangeLister;

public class LockPatterActivity extends Activity implements OnPatterChangeLister {
	
	LockPatterView lock;
	TextView text;
	String p = "14789";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockpatter);
		text = (TextView) findViewById(R.id.text);
		lock = (LockPatterView) findViewById(R.id.lock);
		lock.SetOnPatterChangeLister(this);
	}

	@Override
	public void onPatterChange(String passwordStr) {
		if (!TextUtils.isEmpty(passwordStr)) {
			if (passwordStr.equals(p)) {
				text.setText(passwordStr);
			}else{
				text.setText("密码错误");
				text.startAnimation(shakeAnimation(5));
				lock.errorPoint();
			}
		}
		
	}
	
	@Override
	public void onPatterStart(boolean isStart) {
		if (isStart) {
			text.setText("请绘制图案");
		}
	}
	
	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}
}
