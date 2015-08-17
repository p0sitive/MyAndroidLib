package com.example.myandroidlib;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import com.example.myandroidlib.Base.AppManager;
import com.example.myandroidlib.View.ZoomImageView;

public class PhotoViewActivity extends Activity {
	ZoomImageView imageView;
	Bitmap mBitmap;
	
    private int mLeft;
    private int mTop;
    private float mScaleX;
    private float mScaleY;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photoview);
		AppManager.getAppManager().addActivity(this);
		mBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.subway_map);
        BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), mBitmap);
        final int left = getIntent().getIntExtra("locationX", 0);
        final int top = getIntent().getIntExtra("locationY", 0);
        final int width = getIntent().getIntExtra("width", 800);
        final int height = getIntent().getIntExtra("height", 480);
        imageView=(ZoomImageView) findViewById(R.id.photoview);
		imageView.setImageDrawable(mBitmapDrawable);
		imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                int location[] = new int[2];
                imageView.getLocationOnScreen(location);
                mLeft = left - location[0];
                mTop = top - location[1];
                mScaleX = width*1.0f / imageView.getWidth();
                mScaleY = height*1.0f / imageView.getHeight();
                Log.v("photo", "========resId========" + imageView.getWidth()) ;
                Log.v("photo", "========resId========" + mScaleY) ;
                activityEnterAnim();
                return true;
            }

        });
        Log.v("photo", "========mBitmap========" + mBitmap.getWidth()) ;
	}
	
	@SuppressLint("NewApi")
	private void activityEnterAnim() {
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.setScaleX(mScaleX);
        imageView.setScaleY(mScaleY);
        imageView.setTranslationX(mLeft);
        imageView.setTranslationY(mTop);
        imageView.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).
                setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBitmap,"alpha",0,255);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }

    @SuppressLint("NewApi")
	private void activityExitAnim(Runnable runnable) {
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.animate().scaleX(mScaleX).scaleY(mScaleY).translationX(mLeft).translationY(mTop).
                withEndAction(runnable).
                setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBitmap,"alpha",255,0);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        activityExitAnim(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
