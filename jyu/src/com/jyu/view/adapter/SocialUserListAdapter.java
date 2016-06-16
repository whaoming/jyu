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

public class SocialUserListAdapter extends BaseAdapter {

	private Context ct;
	private List<UserCommentInfo> list;
	BitmapUtils bitmapUtil;
	
	
	public SocialUserListAdapter(Context ct, List<UserCommentInfo> list) {
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
					.inflate(ct, R.layout.item_social_user_list, null);
			holder.upic = (ImageView) convertView.findViewById(R.id.upic);
			holder.uname = (TextView) convertView.findViewById(R.id.uname);
			holder.udescription = (TextView) convertView
					.findViewById(R.id.udescription);
			holder.contact = (TextView) convertView
					.findViewById(R.id.contact);
			holder.tname = (TextView) convertView
					.findViewById(R.id.tname);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.uname.setText(info.getUname());
		holder.udescription.setText(info.getUdescription());
		bitmapUtil.display(holder.upic, ConstantValue.LOTTERY_URI + info.getUpic());
		holder.contact.setText(ct.getString(R.string.contacts_of,info.getUsnum()));
		holder.tname.setText(ct.getString(R.string.ture_name,info.getUtname()));
		
		return convertView;
	}

	public class ViewHolder {
		ImageView upic;
		TextView uname;
		TextView udescription;
		TextView contact;
		TextView tname;
	}

}
