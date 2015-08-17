package com.example.myandroidlib;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myandroidlib.View.RoundProgressBar;

public class ProgressBarActivity extends Activity implements
		View.OnClickListener {

	RoundProgressBar roundProgressBar;
	private Button btn_go, btn_reset, btn_break;
	private int progress = 0;

	boolean isBreak = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progressbar);

		roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
		btn_go = (Button) findViewById(R.id.btn_go);
		btn_reset = (Button) findViewById(R.id.btn_reset);
		btn_break = (Button) findViewById(R.id.btn_break);
		if (savedInstanceState == null) {
		}
		btn_go.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
		btn_break.setOnClickListener(this);
		roundProgressBar
				.setOnLoadFinishListener(new RoundProgressBar.OnLoadFinishListener() {

					@Override
					public void onLoadFinished() {
						Toast.makeText(ProgressBarActivity.this, "加载完成",
								Toast.LENGTH_LONG).show();
					}
				});
	}

	@Override
	public void onClick(View v) {

		myThread thread = new myThread();
		switch (v.getId()) {
		case R.id.btn_go:
			if (isBreak) {

			}
			thread.start();
			break;

		case R.id.btn_reset:
			progress = 0;
			isBreak = false;
			roundProgressBar.setProgress(progress);
			break;
		case R.id.btn_break:
			isBreak = !isBreak;
			if (isBreak) {
				btn_break.setText("继续");
				try {
					thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				roundProgressBar.setProgress(roundProgressBar.getProgress());
			} else {
				btn_break.setText("暂停");
				// roundProgressBar.s
			}
			onClick(btn_go);
			break;
		}
	}

	public class myThread extends Thread {
		@Override
		public void run() {
			while (progress <= 100) {

				progress += 3;
				roundProgressBar.setProgress(progress);

				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			}
		}
	}
}
