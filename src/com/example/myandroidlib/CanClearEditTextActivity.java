package com.example.myandroidlib;


import com.example.myandroidlib.Base.AppManager;
import com.example.myandroidlib.View.ClearEditText;
import com.example.myandroidlib.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CanClearEditTextActivity extends Activity {
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancleartext);
		AppManager.getAppManager().addActivity(this);
		final ClearEditText username = (ClearEditText) findViewById(R.id.username);
		final ClearEditText password = (ClearEditText) findViewById(R.id.password);
		
		((Button) findViewById(R.id.login)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(username.getText())){
					password.requestFocus();
					//设置晃动
					username.startShakeAnimation();//.setShakeAnimation();
					//设置提示
					showToast("用户名不能为空");
					return;
				}
				
				if(TextUtils.isEmpty(password.getText())){
					password.requestFocus();
					password.startShakeAnimation();//.setShakeAnimation();
					showToast("密码不能为空");
					return;
				}
			}
		});
	}
	
	/**
	 * 显示Toast消息
	 * @param msg
	 */
	private void showToast(String msg){
		if(mToast == null){
			mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		}else{
			mToast.setText(msg);
		}
		mToast.show();
	}

}
