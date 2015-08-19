package com.example.myandroidlib;

import java.io.File;
import java.io.InputStream;

import com.example.myandroidlib.View.GifImageView;

import android.app.Activity;
import android.os.Bundle;

public class GifShowActivity extends Activity {

	GifImageView gifView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		InputStream is=getResources().openRawResource(R.drawable.gif_test);
		setContentView(R.layout.activity_gifshow);
//		gifView=(GifImageView) findViewById(R.id.gifview);
//		gifView.setIs(is);
	}
}
