package com.jyu.view.adapter;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.bean.R_Lost.LostItem;
import com.jyu.view.activity.LostItemPublishActivity;
import com.jyu.view.activity.PhotoActivity;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;
import com.jyu.view.custom.photoutil.Bimp;
import com.jyu.view.custom.photoutil.MyGridView;
import com.lidroid.xutils.BitmapUtils;

public class LostItemAdapter extends BaseAdapter {

	private Context ctx;
	private List<LostItem> list;
	BitmapUtils bitmapUtil;
	boolean isMe;

//	 private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(  
//	            android.R.color.transparent);  
//	 /** 
//     * @author sunglasses 
//     * @category 图片加载效果 
//     * @param imageView 
//     * @param bitmap 
//     */  
//    private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {//目前流行的渐变效果  
//        final TransitionDrawable transitionDrawable = new TransitionDrawable(  
//                new Drawable[] { TRANSPARENT_DRAWABLE,  
//                        new BitmapDrawable(imageView.getResources(), bitmap) });  
//        imageView.setImageDrawable(transitionDrawable);  
//        transitionDrawable.startTransition(500);  
//    }  
	
	public LostItemAdapter(Context ctx, List<LostItem> list, boolean isMe) {
		this.ctx = ctx;
		this.list = list;
		bitmapUtil = new BitmapUtils(ctx);
		bitmapUtil.configDefaultLoadingImage(R.drawable.default_unknow_user);
		
		this.isMe = isMe;
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
		Holder hold;
		final LostItem lost = list.get(position);
		if (convertView == null) {
			hold = new Holder();
			convertView = View.inflate(ctx, R.layout.item_lost, null);
			hold.upic = (ImageView) convertView.findViewById(R.id.upic);
			hold.uname = (TextView) convertView.findViewById(R.id.uname);
			hold.udescription = (TextView) convertView
					.findViewById(R.id.udescription);
			hold.title = (TextView) convertView.findViewById(R.id.title);
			hold.noScrollgridview = (MyGridView) convertView
					.findViewById(R.id.noScrollgridview);
			hold.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			hold.type = (TextView) convertView.findViewById(R.id.type);
			hold.iv_edit = (ImageView) convertView.findViewById(R.id.iv_edit);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}
		if (isMe) {
			hold.iv_edit.setVisibility(View.VISIBLE);
			hold.iv_edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog(ctx)
							.builder()
							.setTitle("请选择操作")
							.addSheetItem("编辑", SheetItemColors.Blue,
									new OnSheetItemClickListeners() {
										@Override
										public void onClick(int which) {
											Intent intent = new Intent(
													ctx,
													LostItemPublishActivity.class);
											intent.putExtra("position",
													position);
											Bundle bund = new Bundle();
											bund.putSerializable("LostItem",
													lost);
											intent.putExtra("value", bund);
											intent.putExtra("isEdit", true);
											intent.putExtra("isLost", lost
													.lost == 1 ? true
													: false);
											Activity activity = (Activity) ctx;

											activity.startActivityForResult(
													intent, 1);
										}
									})
							.addSheetItem("删除", SheetItemColors.Blue,
									new OnSheetItemClickListeners() {
										@Override
										public void onClick(int which) {
											new AlertDialog(ctx)
													.builder()
													.setTitle("确定要删除此信息吗？")
													.addSheetItem(
															"是",
															SheetItemColors.Blue,
															new OnSheetItemClickListeners() {
																@Override
																public void onClick(
																		int which) {
																	
																}
															})
													.addSheetItem(
															"否",
															SheetItemColors.Blue,
															new OnSheetItemClickListeners() {

																@Override
																public void onClick(
																		int which) {

																}
															}).show();
										}
									}).show();
				}
			});
		} else {
			hold.iv_edit.setVisibility(View.GONE);
		}
		hold.title.setText(lost.title);
		hold.uname.setText(lost.userInfo.uname);
		hold.udescription.setText(lost.userInfo.udescription);
		bitmapUtil.display(hold.upic,
				ConstantValue.LOTTERY_URI + lost.userInfo.upic);
		if (lost.lost == 1) {
			hold.type.setText("寻物启事");
			hold.tv_time.setText("丢失时间:" + lost.ltime);
		} else {
			hold.type.setText("失物招领");
			hold.tv_time.setText("捡到时间:" + lost.ltime);
		}

		final String[] piclist = lost.pic.split(",");
		MyPhotoGridAdapter adapter = new MyPhotoGridAdapter(ctx, piclist, false);
		hold.noScrollgridview.setAdapter(adapter);
		hold.noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ctx, PhotoActivity.class);
				intent.putExtra("ID", position);
				intent.putExtra("isNet", true);
				for (String pic : piclist) {
					Bimp.drr.add(pic);
					Bimp.max = piclist.length;
				}
				ctx.startActivity(intent);

			}
		});

		return convertView;
	}

	static class Holder {
		ImageView upic;
		TextView uname;
		TextView udescription;
		TextView title;
		MyGridView noScrollgridview;
		TextView type;
		TextView tv_time;
		ImageView iv_edit;
	}

}
