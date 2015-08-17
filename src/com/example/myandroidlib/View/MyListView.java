package com.example.myandroidlib.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自计算Listview高度，可在ScrollView嵌套使用
 * <br>
 * 仅适用于数据量较少的情况
 * @author Administrator
 *
 */
public class MyListView extends ListView {

	public MyListView(Context context) {
		this(context, null);
	}

	public MyListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mMeasureSpec);
	}
}
