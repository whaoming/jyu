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
import com.jyu.domain.UserCommentInfo;
import com.lidroid.xutils.BitmapUtils;

public class SecondHandPlayerListAdapter extends BaseAdapter {

	private Context ct;
	private List<UserCommentInfo> list;
	BitmapUtils bitmapUtil;
	
	
	public SecondHandPlayerListAdapter(Context ct, List<UserCommentInfo> list) {
		super();
		this.ct = ct;
		this.list = list;
		bitmapUtil = new BitmapUtils(ct);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		UserCommentInfo info = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(ct, R.layout.item_secondhand2_say, null);
			holder.upic = (ImageView) convertView.findViewById(R.id.upic);
			holder.uname = (TextView) convertView.findViewById(R.id.uname);
			holder.udescription = (TextView) convertView.findViewById(R.id.udescription);
			holder.tv_contact = (TextView) convertView.findViewById(R.id.tv_contact);
			holder.tv_tname = (TextView) convertView.findViewById(R.id.tv_tname);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		bitmapUtil.display(holder.upic, ConstantValue.LOTTERY_URI + info.getUpic());
		holder.udescription.setText(info.getUdescription());
		holder.uname.setText(info.getUname());
		holder.tv_contact.setText(ct.getString(R.string.contacts_of,info.getUsnum()));
		holder.tv_tname.setText(ct.getString(R.string.ture_name,info.getUtname()));
		return convertView;
	}
	
	public class ViewHolder {
		ImageView upic;
		TextView uname;
		TextView udescription;
		TextView tv_contact;
		TextView tv_tname;
	}

}
