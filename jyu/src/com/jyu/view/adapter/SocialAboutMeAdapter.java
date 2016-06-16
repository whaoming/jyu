package com.jyu.view.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.domain.Social;
import com.jyu.view.activity.SocialPublishActivity;
import com.jyu.view.activity.SocialUserListActivity;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;
import com.lidroid.xutils.BitmapUtils;

public class SocialAboutMeAdapter extends BaseAdapter {

	private List<Social> list;
	private Context ct;
	BitmapUtils bitmapUtil;
	boolean ifShowJoin;

	public SocialAboutMeAdapter( Context ct,List<Social> list,boolean ifShowJoin) {
		this.list = list;
		this.ct = ct;
		bitmapUtil = new BitmapUtils(ct);
		this.ifShowJoin = ifShowJoin;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder hold = null;
		final Social social = list.get(position);
		if (convertView == null) {
			hold = new Holder();
			convertView = View.inflate(ct, R.layout.item_social_aboutme, null);
			hold.title = (TextView) convertView.findViewById(R.id.title);
			hold.time = (TextView) convertView
					.findViewById(R.id.time);
			hold.location = (TextView) convertView.findViewById(R.id.location);
			hold.contact = (TextView) convertView.findViewById(R.id.contact);
			hold.number = (TextView) convertView
					.findViewById(R.id.number);
			hold.name = (TextView) convertView.findViewById(R.id.name);
			hold.pic = (ImageView) convertView.findViewById(R.id.pic);
			hold.btn_join  = (Button) convertView.findViewById(R.id.btn_join);
			hold.btn_join.setFocusable(false);
		hold.more = (ImageView) convertView.findViewById(R.id.more);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}

		hold.title.setText(social.getTitle());

//		hold.begin_date.setText(ct.getString(R.string.begin_time_at,
//				social.getStarttime() == null ? "无" : social.getStarttime()));
//		hold.end_date.setText(ct.getString(R.string.end_time_at,
//				social.getEndtime() == null ? "无" : social.getEndtime()));
		hold.location.setText(ct.getString(R.string.place_at,
				social.getLocation() == null ? "无" : social.getLocation()));
		hold.name.setText(ct.getString(R.string.publish_name, social.getName()));
//		if(social.getType() == 1){
//			hold.tv_name.setVisibility(View.VISIBLE);
//			hold.iv_pic.setVisibility(View.VISIBLE);
////			Log.i("wang", "ttt=="+social.getPic());
//			bitmapUtil.display(hold.iv_pic, ConstantValue.LOTTERY_URI + social.getPic());
//			
//		}else{
//			hold.iv_pic.setVisibility(View.GONE);
//			hold.tv_name.setVisibility(View.GONE);
//		}
		hold.time.setText(ct.getString(R.string.time_at,social.getTime()));
		if(social.getIssignup() == 1){//需要报名
			hold.number.setText(ct.getString(R.string.join_number, social.getSignup()));
		}else{
			hold.number.setText("无须报名");
		}
		
		hold.contact.setText(ct.getString(R.string.contacts_of,social.getContact()));
		if(social.getPic().startsWith("/image")){
			bitmapUtil.display(hold.pic, ConstantValue.LOTTERY_URI + social.getPic());
		}else{
			bitmapUtil.display(hold.pic, social.getPic());
		}
		
		
		
		//判断是点击活动页面还是我的活动页面
		
		//我参加过的也可能是我自己发布的
//		if(social.getUserid() == GlobalParams.user.getId()){
//			hold.btn_join.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//		}else{
//			
//		}
		if(ifShowJoin){
			hold.more.setVisibility(View.VISIBLE);
			hold.more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog(ct)
					.builder()
					.setTitle("请选择操作")
					.addSheetItem("编辑", SheetItemColors.Blue, new OnSheetItemClickListeners() {
						@Override
						public void onClick(int which) {
							Intent intent = new Intent(ct,
									SocialPublishActivity.class);
							intent.putExtra("position", position);
							Bundle bund = new Bundle();
							bund.putSerializable("info", social);
							intent.putExtra("value", bund);
							intent.putExtra("isEdit", true);
							Activity activity = (Activity) ct;
							
							activity.startActivityForResult(intent, 1);
						}
					})
					.addSheetItem("删除", SheetItemColors.Blue, new OnSheetItemClickListeners() {
						
						@Override
						public void onClick(int which) {
							
						}
					})
					.show();
					
				}
			});
			if(social.getIssignup() == 1){
				hold.btn_join.setVisibility(View.VISIBLE);
				hold.btn_join.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ct,
								SocialUserListActivity.class);
						intent.putExtra("socialid", social.getId());
						ct.startActivity(intent);
					}
				});
			}else{
				hold.btn_join.setVisibility(View.GONE);
			}
		}else{
			hold.btn_join.setVisibility(View.GONE);
			hold.more.setVisibility(View.GONE);
		}
		
		return convertView;
	}


	static class Holder {
		TextView title;
		TextView time;
		TextView location;
		TextView contact;
		TextView number;
		TextView name;
		ImageView pic;
		Button btn_join;
		ImageView more;
	}

}
