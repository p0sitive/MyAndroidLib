package com.example.myandroidlib.View;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * 可自由缩放的ImageView
 * @author Administrator
 *
 */
public class ZoomImageView extends ImageView implements OnGlobalLayoutListener,
		OnScaleGestureListener, OnTouchListener {

	private boolean mOnce;

	/**
	 * 初始缩放值
	 */
	private float mInitScale;
	/**
	 * 最小缩放值
	 */
	private float mMidScale;
	/**
	 * 最大缩放值
	 */
	private float mMaxScale;

	private Matrix mMatrix;

	private ScaleGestureDetector mScaleGestureDetector;

	/**
	 * 记录上一次多点触控的数量
	 */
	private int mLastPointerCount;

	// 最后一次触摸时中心点的坐标
	private float mLastX;
	private float mLastY;

	private int mTouchSlop;
	private boolean isCanDrag;

	private boolean isCheckLiftOrRight;
	private boolean isCheckTopOrBottom;

	// 雙擊放大與縮小

	private GestureDetector mGestureDetector;

	private boolean isAutoScale;

	public ZoomImageView(Context context) {
		this(context, null);
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 初始化函数
		init(context);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context) {
		mOnce = false;
		ViewConfiguration.get(context);
		mTouchSlop = ViewConfiguration.getTouchSlop();
		setScaleType(ScaleType.MATRIX);
		mMatrix = new Matrix();
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		setOnTouchListener(this);
		mGestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onDoubleTap(MotionEvent e) {

						if (isAutoScale) {
							return true;
						}
						float x = e.getX();
						float y = e.getY();
						if (getScale() < mMidScale) {
							// mMatrix.postScale(mMidScale / getScale(),
							// mMidScale
							// / getScale(), x, y);
							postDelayed(new AutoScaleRunnable(mMidScale, x, y),
									16);
							isAutoScale = true;
						} else {
							// mMatrix.postScale(mInitScale / getScale(),
							// mInitScale / getScale(), x, y);
							postDelayed(
									new AutoScaleRunnable(mInitScale, x, y), 16);
							isAutoScale = true;
						}
						setImageMatrix(mMatrix);
						return super.onDoubleTap(e);
					}
				});
	}

	private class AutoScaleRunnable implements Runnable {

		private float mTargetScale;
		private float x, y;

		private float BIGGER = 1.07f;
		private float SMALL = 0.93f;
		private float tempScale;

		public AutoScaleRunnable(float mTargetScale, float x, float y) {
			this.mTargetScale = mTargetScale;
			this.x = x;
			this.y = y;

			if (getScale() < mTargetScale) {
				tempScale = BIGGER;
			} else {
				tempScale = SMALL;
			}

		}

		@Override
		public void run() {

			// 進行縮放
			mMatrix.postScale(tempScale, tempScale, x, y);
			CheckBorderAndCenter();
			setImageMatrix(mMatrix);
			float currentScale = getScale();
			if ((tempScale > 1.0f && currentScale < mTargetScale)
					|| (tempScale < 1.0f && currentScale > mTargetScale)) {
				postDelayed(this, 20);
			} else {
				float scale = mTargetScale / currentScale;
				mMatrix.postScale(scale, scale, x, y);
				CheckBorderAndCenter();
				setImageMatrix(mMatrix);
				isAutoScale = false;

			}
		}

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	/**
	 * 获取imageview加载完成后的图片
	 */
	@Override
	public void onGlobalLayout() {
		if (!mOnce) {
			// 得到控件的宽高
			int width = getWidth();
			int height = getHeight();
			Log.i("XXX", "view:w--->" + width + ",h---->" + height);
			// 获取图片
			Drawable drawable = getDrawable();
			if (drawable == null) {
				return;
			}
			// 图片宽高
			int dw = drawable.getIntrinsicWidth();
			int dh = drawable.getIntrinsicHeight();

			Log.i("XXX", "pic:w--->" + dw + ",h--->" + dh);

			float scale = 1.0f;
			if (dw > width && dh < height) {
				scale = width * 1.0f / dw;

			}
			if (dh > height && dw < width) {
				scale = height * 1.0f / dw;
			}
			if ((dw > width && dh > height) || (dw < width && dh < height)) {
				scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
			}
			mInitScale = scale;
			mMaxScale = 4 * scale;
			mMidScale = 2 * scale;
			Log.i("XXX", "scale--->" + scale);
			// 移動圖片至中心位置
			int dx = (getWidth() - dw) / 2;
			int dy = (getHeight() - dh) / 2;

			// 缩放平移的设置
			mMatrix.postTranslate(dx, dy);
			mMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
			setImageMatrix(mMatrix);

			mOnce = true;

		}
	}

	/**
	 * 获取图片缩放比例
	 * 
	 * @return
	 */
	public float getScale() {
		float[] values = new float[9];
		mMatrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		// 缩放区间 maxScale~initScale

		float scale = getScale();
		float scaleFactor = detector.getScaleFactor();
		Log.i("XXX", "pro:scale--->" + scale + ",scaleFactor--->" + scaleFactor);
		if (getDrawable() == null) {
			return true;
		}
		if ((scale < mMaxScale && scaleFactor > 1.0f)
				|| (scale > mInitScale && scaleFactor < 1.0f)) {

			if (scale * scaleFactor < mInitScale) {
				scaleFactor = mInitScale / scale;
			}
			if (scale * scaleFactor > mMaxScale) {
				scale = mMaxScale / scale;
			}
			Log.i("XXX", "next:scale--->" + scale + ",scaleFactor--->"
					+ scaleFactor);

			mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(),
					detector.getFocusY());

			CheckBorderAndCenter();

			setImageMatrix(mMatrix);
		}
		return true;
	}

	/**
	 * 获取图片缩放后的四至范围
	 * 
	 * @return
	 */
	private RectF getMatrixRectF() {
		Matrix matrix = mMatrix;
		RectF rectF = new RectF();
		Drawable drawable = getDrawable();
		if (drawable != null) {
			rectF.set(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			matrix.mapRect(rectF);
		}
		return rectF;
	}

	/**
	 * 缩放时控制边界和中心
	 */
	private void CheckBorderAndCenter() {
		RectF rectF = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		if (rectF.width() >= width) {
			if (rectF.left > 0) {
				deltaX = -rectF.left;
			}
			if (rectF.right < width) {
				deltaX = width - rectF.right;
			}
		}
		if (rectF.height() >= height) {
			if (rectF.top > 0) {
				deltaY = -rectF.top;
			}
			if (rectF.bottom < height) {
				deltaY = height - rectF.bottom;
			}
		}
		// 图片宽高小于控件宽高
		if (rectF.width() < width) {
			deltaX = (width + rectF.width()) / 2f - rectF.right;
		}
		if (rectF.height() < height) {
			deltaY = (height + rectF.height()) / 2f - rectF.bottom;
		}
		mMatrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * 移动时控制边界和中心
	 */
	private void CheckBorderAndCenterWhenTranslate() {
		RectF rectF = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		if (rectF.left > 0 && isCheckLiftOrRight) {
			deltaX = -rectF.left;
		}
		if (rectF.right < width && isCheckLiftOrRight) {
			deltaX = width - rectF.right;
		}

		if (rectF.top > 0 && isCheckTopOrBottom) {
			deltaY = -rectF.top;
		}
		if (rectF.bottom < height && isCheckTopOrBottom) {
			deltaY = height - rectF.bottom;
		}

		// // 图片宽高小于控件宽高
		// if (rectF.width() < width) {
		// deltaX = (width + rectF.width()) / 2f - rectF.right;
		// }
		// if (rectF.height() < height) {
		// deltaY = (height + rectF.height()) / 2f - rectF.bottom;
		// }
		mMatrix.postTranslate(deltaX, deltaY);
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}

		mScaleGestureDetector.onTouchEvent(event);

		float x = 0;
		float y = 0;

		int pointCount = event.getPointerCount();
		for (int i = 0; i < pointCount; i++) {
			x += event.getX();
			y += event.getY();
		}
		x /= pointCount;
		y /= pointCount;

		if (mLastPointerCount != pointCount) {
			mLastX = x;
			mLastY = y;
			isCanDrag = false;
		}
		mLastPointerCount = pointCount;

		// 处理移动图片
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - mLastX;
			float dy = y - mLastY;
			if (!isCanDrag) {
				isCanDrag = isMoveAction(dx, dy);
				Log.i("XXX", "isCanDrag--->" + isCanDrag + ",dx--->" + dx
						+ ",dy--->" + dy);
			}
			if (isCanDrag) {
				RectF rectF = getMatrixRectF();
				if (getDrawable() != null) {
					isCheckLiftOrRight = isCheckTopOrBottom = true;
					// 图片宽度小于控件宽度，不允许左右 移动
					if (rectF.width() < getWidth()) {
						isCheckLiftOrRight = false;
						dx = 0;
					}
					// 图片高小于控件高，不允许上下移动
					if (rectF.height() < getHeight()) {
						isCheckTopOrBottom = false;
						dy = 0;
					}
					mMatrix.postTranslate(dx, dy);
					CheckBorderAndCenterWhenTranslate();
					setImageMatrix(mMatrix);
				}
			}
			mLastX = x;
			mLastY = y;

			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mLastPointerCount = 0;

			break;
		}
		return true;
	}

	private boolean isMoveAction(float dx, float dy) {

		return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
	}

}
