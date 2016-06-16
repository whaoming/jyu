package com.jyu.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.domain.SecondHandInfo;
import com.lidroid.xutils.BitmapUtils;

public class SecondHandAdapter extends BaseAdapter {

	private Context ct;
	private List<SecondHandInfo> list;
	BitmapUtils bitmapUtil;

	public SecondHandAdapter(Context ct, List<SecondHandInfo> list) {
		super();
		this.ct = ct;
		this.list = list;
		bitmapUtil = new BitmapUtils(ct);
	}

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
		final Holder hold;
		final SecondHandInfo item = list.get(position);
		if (convertView == null) {
			hold = new Holder();
			convertView = View.inflate(ct, R.layout.item_secondhand, null);
			hold.pic = (ImageView) convertView.findViewById(R.id.pic);
			hold.time = (TextView) convertView.findViewById(R.id.tv_data);
			hold.title = (TextView) convertView.findViewById(R.id.tv_title);
			hold.price = (TextView) convertView.findViewById(R.id.tv_price);
			hold.label  = (TextView) convertView.findViewById(R.id.tv_label);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}
		if(item.getPic().split(",")[0].startsWith("/image")){
			bitmapUtil.display(hold.pic, ConstantValue.LOTTERY_URI + item.getPic().split(",")[0]);
		}else{
			bitmapUtil.display(hold.pic, item.getPic().split(",")[0]);
		}
		hold.time.setText(item.getData());
		hold.title.setText(item.getTitle());
		hold.price.setText("$"+item.getPrice());
		hold.label.setText(item.getLabel());
		return convertView;
	}

	static class Holder {
		ImageView pic;
		TextView time;
		TextView title;
		TextView label;
		TextView price;
	}


}
