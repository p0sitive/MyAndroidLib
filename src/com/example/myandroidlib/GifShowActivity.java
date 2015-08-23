package com.example.myandroidlib;

import android.app.Activity;
import android.os.Bundle;

import com.example.myandroidlib.View.CircleImageView;
import com.example.myandroidlib.View.GifImageView;

public class GifShowActivity extends Activity {

	GifImageView gifView;
	CircleImageView circleImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		InputStream is=getResources().openRawResource(R.drawable.gif_test);
		setContentView(R.layout.activity_gifshow);
//		gifView=(GifImageView) findViewById(R.id.gifview);
//		gifView.setIs(is);
		circleImageView=(CircleImageView) findViewById(R.id.circle_imageview);
		circleImageView.setImageResource(R.drawable.subway_map);
	}
}
