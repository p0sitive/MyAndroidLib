package com.example.myandroidlib;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myandroidlib.Base.AppManager;
import com.example.myandroidlib.View.MyListView;


public class ScrollActivity extends Activity {
	ListView listView;
	List<String> dataList;
	MyListView listView2;
	
	ImageView iv;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_scroll);
		AppManager.getAppManager().addActivity(this);
		dataList = InitData();
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView = (ListView) findViewById(R.id.lv_data);
		listView2=(MyListView) findViewById(R.id.lv_data2);
		listView2.setAdapter(adapter);
		listView.setAdapter(adapter);
		setListViewHeight(listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(ScrollActivity.this,
						dataList.get(position) + "", Toast.LENGTH_SHORT).show();
			}

		});
		listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(ScrollActivity.this,
						dataList.get(position) + "", Toast.LENGTH_SHORT).show();
			}

		});
		
	}

	/**
	 * 代码设置listview的高度
	 * 
	 * @param listView
	 */
	private void setListViewHeight(ListView listView) {
		ListAdapter adapter = listView.getAdapter();
		if (adapter == null)
			return;
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View itemView = adapter.getView(i, null, listView);
			itemView.measure(0, 0);
			totalHeight += itemView.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listView.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private List<String> InitData() {
		dataList = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			dataList.add("测试数据" + i);
		}
		return dataList;
	}

}
