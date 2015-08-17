package com.example.myandroidlib.View;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GifImageView extends ImageView {

	/**
	 * 播放GIF的必要类
	 */
	private Movie mMovie;
	
	int mWidth,mHeight;
	long mMovieStart;
	
	public GifImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	

	public GifImageView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	
	public GifImageView(Context context) {
		this(context,null);
	}

	private void init() {
		//获取图片资源的ID
//		this.g
		int resID=0;
		InputStream is=getResources().openRawResource(resID);
		mMovie=Movie.decodeStream(is);
		if(mMovie!=null){
			Bitmap bitmap=BitmapFactory.decodeStream(is);
			mWidth=bitmap.getWidth();
			mHeight=bitmap.getHeight();
			bitmap.recycle();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(mMovie!=null){
			setMeasuredDimension(mWidth, mHeight);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(mMovie==null){
		super.onDraw(canvas);
		}
		else {
			long now=SystemClock.uptimeMillis();
			if(mMovieStart==0){
				mMovieStart=now;
			}
			int duration=mMovie.duration();
			if(duration==0){
				duration=1000;
			}
			int ralTime=(int)((now-mMovieStart)%duration);
			mMovie.setTime(ralTime);
			mMovie.draw(canvas, 0, 0);
			if((now-mMovieStart)>=duration){
				mMovieStart=0;
			}
			invalidate();
		}
	}


}
