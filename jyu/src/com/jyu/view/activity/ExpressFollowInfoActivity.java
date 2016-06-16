package com.jyu.view.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jyu.R;
import com.jyu.domain.Express;
import com.jyu.view.base.BaseActivity;

public class ExpressFollowInfoActivity extends BaseActivity {

	private ListView listview;
	private MyExpressAdapter adapter;
	private List<Express> list;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_express_follow);
		initTitleBar();
		listview = (ListView) findViewById(R.id.listview);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initData() {
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		list = (List<Express>) bund.getSerializable("data");
		adapter = new MyExpressAdapter();
		listview.setAdapter(adapter);
	}

	@Override
	protected void processClick(View v) {
		
	}

	class MyExpressAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder hold;
			Express express = list.get(position);	
			if (convertView == null) {
				hold = new Holder();
				convertView = View.inflate(ct, R.layout.item_expressfollow_info, null);
				hold.info = (TextView) convertView.findViewById(R.id.item_follow_info);
				hold.time = (TextView) convertView.findViewById(R.id.item_time);
				convertView.setTag(hold);
			}else {
				hold = (Holder) convertView.getTag();
			}
			hold.info.setText(express.getContext().toString());
			hold.time.setText(express.getTime().toString());
			return convertView;
		}
	}

	static class Holder {
		TextView time;
		TextView info;
	}

}
