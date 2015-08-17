package com.example.myandroidlib.View;

import java.util.ArrayList;
import java.util.List;

import com.example.myandroidlib.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * "All manual skills, by oral teaching that inspires true understanding within." - Chanel chief shoemaker
 * Success is nothing more than hard work -LiWei  2015年3月24日
 * <li>使用时修改initPoints中的图片资源
 */
public class LockPatterView extends View {
	
	private static final int POINT_SIZE = 5;
	
	private Point[][] points = new Point[3][3];
	
	private boolean isInit,isSelect,isFinish,movePoint;
	
	private Matrix matrix = new Matrix();
	
	private float width,height,offstartX,offstartY,moveX,moveY;;
	
	private Bitmap bitmap_pressed,bitmap_normal,bitmap_error,bitmap_line,bitmap_line_error;
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private List<Point> pointList = new ArrayList<LockPatterView.Point>();
	
	private OnPatterChangeLister onPatterChangeLister;

	public LockPatterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LockPatterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LockPatterView(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (!isInit) {
			initPoints();
		}
		points2Canvas(canvas);
		
		if (pointList.size() > 0) {
			Point a = pointList.get(0);
			//绘制九宫格坐标点
			for (int i = 0; i < pointList.size(); i++) {
				Point b = pointList.get(i);
				line2Canvas(canvas, a, b);
				a = b;
			}
			//绘制鼠标坐标点
			if (movePoint) {
				line2Canvas(canvas, a, new Point(moveX, moveY));
			}
		}
	}
	
	/**
	 * 将点绘制到画布
	 * @param canvas
	 */
	private void points2Canvas(Canvas canvas) {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];
				if (point.state == Point.STATE_PRESSED) {
					canvas.drawBitmap(bitmap_pressed, point.x - bitmap_normal.getWidth()/2, point.y - bitmap_normal.getHeight() / 2, paint);
				}else if (point.state == Point.STATE_ERROR) {
					canvas.drawBitmap(bitmap_error, point.x - bitmap_normal.getWidth()/2, point.y - bitmap_normal.getHeight() / 2, paint);
				}else{
					canvas.drawBitmap(bitmap_normal, point.x - bitmap_normal.getWidth()/2, point.y - bitmap_normal.getHeight() / 2, paint);
				}
			}
		}
	}
	
	
	/**
	 * 画线
	 * @param canvas
	 * @param x
	 * @param y
	 */
	public void line2Canvas(Canvas canvas,Point a,Point b){
		//线的长度
		float linelength = (float) Point.distance(a, b);
		float degress = getDegrees(a,b);
		canvas.rotate(degress, a.x, a.y);
		
		if (a.state == Point.STATE_PRESSED) {
			matrix.setScale(linelength / bitmap_line.getWidth(), 1);
			matrix.postTranslate(a.x - bitmap_line.getWidth() / 2 , a.y - bitmap_line.getHeight() /2);
			canvas.drawBitmap(bitmap_line, matrix, paint);
		}else{
			matrix.setScale(linelength / bitmap_line.getWidth(), 1);
			matrix.postTranslate(a.x - bitmap_line.getWidth() / 2 , a.y - bitmap_line.getHeight() /2);
			canvas.drawBitmap(bitmap_line_error, matrix, paint);
		}
		canvas.rotate(-degress, a.x, a.y);
	}

	
	// 获取角度
	public float getDegrees(Point pointA, Point pointB) {
	    return (float) Math.toDegrees(Math.atan2(pointB.y - pointA.y, pointB.x - pointA.x));
	}

	/**
	 * 初始化点
	 */
	private void initPoints() {
		
		//获取布局宽高
		width = getWidth();
		height = getHeight();
		
		//横屏和竖屏
		
		if (width > height) {//横屏
			offstartX = (width - height) / 2;
			width = height;
		}else{//竖屏
			offstartY = (height - width) / 2;
			height = width;
		}
		
		//图片资源
		bitmap_normal = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		bitmap_pressed = BitmapFactory.decodeResource(getResources(), R.drawable.edit_indicator);
		bitmap_error = BitmapFactory.decodeResource(getResources(), R.drawable.search_clear_pressed);
		bitmap_line = BitmapFactory.decodeResource(getResources(), R.drawable.btn_style_one_pressed);
		bitmap_line_error = BitmapFactory.decodeResource(getResources(), R.drawable.login_edit_pressed);
		
		//点的坐标
		points[0][0] = new Point(offstartX + width / 4,offstartY + height / 4);
		points[0][1] = new Point(offstartX + width / 2,offstartY + height / 4);
		points[0][2] = new Point(offstartX + width - width / 4,offstartY + height / 4);
		
		points[1][0] = new Point(offstartX + width / 4,offstartY + width / 2);
		points[1][1] = new Point(offstartX + width / 2,offstartY + width / 2);
		points[1][2] = new Point(offstartX + width - width / 4,offstartY + width / 2);
		
		points[2][0] = new Point(offstartX + width / 4,offstartY + width - width / 4);
		points[2][1] = new Point(offstartX + width / 2,offstartY + width - width / 4);
		points[2][2] = new Point(offstartX + width - width / 4,offstartY + width - width / 4);
		
		//设置密码
		int index = 1;
		for(Point[] points1 : points)
		{
			for (Point point : points1) {
				point.index = index;
				index ++;
			}
		}
		//初始化完成
		isInit = true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		moveX = event.getX();
		moveY = event.getY();
		movePoint = false;
		isFinish = false;
		
		Point point = null;
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (onPatterChangeLister != null) {
				onPatterChangeLister.onPatterStart(true);
			}
			resetPoint();
			
			point = chechSelectPoint();
			if (point != null) {
				isSelect = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isSelect) {
				point = chechSelectPoint();
				if (point == null) {
					movePoint = true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			isFinish = true;
			isSelect = false;
			break;
			
		}
		//选中重复检查
		if (!isFinish && isSelect && point != null) {
			//交叉点
			if (crossPoint(point)) {
				movePoint = true;
			}else{//新点
				point.state = Point.STATE_PRESSED;
				pointList.add(point);
			}
		}
		
		//绘制结束
		if (isFinish) {
			//绘制不成立
			if (pointList.size() == 1) {
//				resetPoint();
				errorPoint();
			}else if(pointList.size() < POINT_SIZE && pointList.size() > 0 ){//绘制错误
				 errorPoint();
				 if (onPatterChangeLister != null) {
						onPatterChangeLister.onPatterChange(null);
					}
			}else{
				if (onPatterChangeLister != null) {
					String pass = "";
					for (int i = 0; i < pointList.size(); i++) {
						pass = pass + pointList.get(i).index;
					}
					if (!TextUtils.isEmpty(pass)) {
						onPatterChangeLister.onPatterChange(pass);
					}
				}
			}
		}
		
		postInvalidate();
		return true;
	}
	
	/**
	 * 交叉点
	 * @param point
	 * @return 是否交叉
	 */
	private boolean crossPoint(Point point){
		if (pointList.contains(point)) {
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 重新绘制
	 */
	public void resetPoint(){
		for (int i = 0; i < pointList.size(); i++) {
			Point point = pointList.get(i);
			point.state = Point.STATE_NORMAL;
		}
		pointList.clear();
	}
	
	/**
	 * 绘制错误
	 */
	public void errorPoint(){
		for (Point point : pointList) {
			point.state = Point.STATE_ERROR;
		}
	}
	
	/**
	 * 检查是否选中
	 * @return
	 */
	private Point chechSelectPoint(){
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];
				if (Point.with(point.x, point.y, bitmap_normal.getWidth() / 2, moveX, moveY)) {
					return point;
				}
			}
		}
		
		return null;
	}

	/**
	 * 自定义的点
	 * "All manual skills, by oral teaching that inspires true understanding within." - Chanel chief shoemaker
	 * Success is nothing more than hard work -LiWei  2015年3月24日
	 */
	public static class Point{
		//正常
		public static int STATE_NORMAL = 0;
		//选中
		public static int STATE_PRESSED = 1;
		//错误
		public static int STATE_ERROR = 2;
		public float x,y;
		public int index = 0,state = 0;
		public Point(){};
		
		public Point(float x,float y){
			this.x = x;
			this.y = y;
		}
	
		/**
		 * 两点之间的距离
		 * @param a
		 * @param b
		 * @return
		 */
		public static double distance(Point a,Point b){
			return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x) + Math.abs(a.y - b.y) * Math.abs(a.y - b.y)) ;
		}
		
		/**
		 * 
		 * @param paintX
		 * @param pointY
		 * @param r
		 * @param moveX
		 * @param moveY
		 * @return
		 */
		public static boolean with(float paintX,float pointY,float r,float moveX,float moveY){
			return Math.sqrt((paintX - moveX) * (paintX - moveX) + (pointY - moveY) * (pointY - moveY)) < r ;
		}
	}
	
	/**
	 * 图案监听器
	 * "All manual skills, by oral teaching that inspires true understanding within." - Chanel chief shoemaker
	 * Success is nothing more than hard work -LiWei  2015年3月24日
	 */
	public static interface OnPatterChangeLister{
		void onPatterChange(String passwordStr);
		
		void onPatterStart(boolean isStart);
	}
	/**
	 * 设置图案监听器
	 * @param changeLister
	 */
	public void SetOnPatterChangeLister(OnPatterChangeLister changeLister){
		if (changeLister != null) {
			this.onPatterChangeLister = changeLister;
		}
	}
}
