package com.example.myandroidlib;

import com.example.myandroidlib.Base.AppManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	Button btn_delete_editview;
	Button btn_riseNumber;
	Button btn_photoButton;
	Button btn_scrollButton;
	Button btn_progress;
	Button btn_lockButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppManager.getAppManager().addActivity(this);

		initView();
	}

	private void initView() {

		btn_delete_editview = (Button) findViewById(R.id.btn_delete_edittext);
		btn_delete_editview.setOnClickListener(this);

		btn_riseNumber = (Button) findViewById(R.id.btn_risenumber);
		btn_riseNumber.setOnClickListener(this);

		btn_photoButton = (Button) findViewById(R.id.btn_photoview);
		btn_photoButton.setOnClickListener(this);

		btn_scrollButton = (Button) findViewById(R.id.btn_scroll_list);
		btn_scrollButton.setOnClickListener(this);

		btn_progress = (Button) findViewById(R.id.btn_progressbar);
		btn_progress.setOnClickListener(this);

		btn_lockButton = (Button) findViewById(R.id.btn_lockpatter);
		btn_lockButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_delete_edittext:
			intent = new Intent(this, CanClearEditTextActivity.class);
			break;
		case R.id.btn_risenumber:
			intent = new Intent(this, RiseNumberActivity.class);
			break;
		case R.id.btn_photoview:
			intent = new Intent(this, PhotoViewActivity.class);
			break;
		case R.id.btn_scroll_list:
			intent = new Intent(this, ScrollActivity.class);
			break;
		case R.id.btn_progressbar:
			intent = new Intent(this, ProgressBarActivity.class);
			break;
		case R.id.btn_lockpatter:
			intent = new Intent(this, LockPatterActivity.class);
		default:
			break;
		}
		startActivity(intent);
	}

}
