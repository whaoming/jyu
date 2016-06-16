package com.jyu.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.domain.LostItemInfo;
import com.jyu.view.adapter.MyPhotoGridAdapter;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.photoutil.Bimp;
import com.jyu.view.custom.photoutil.MyGridView;
import com.lidroid.xutils.BitmapUtils;

public class LostFoundDetailActivity extends BaseActivity{

	
	BitmapUtils bitmapUtil;
	// 带过来的关于这条东东的一个信息
	private LostItemInfo info;
	// 发送评论按钮，用户头像,大图
	private ImageView upic;
	
	MyGridView noScrollgridview;
	
	private TextView tv_uname;
	private TextView tv_udescription;
	private TextView tv_title;
	private TextView tv_location;
	private TextView tv_ltime;
	private TextView tv_type;
	private TextView tv_ilabel;
	private TextView tv_llabel;
	private TextView tv_contact;
	private TextView tv_description;

	

	@Override
	protected void initView() {
		setContentView(R.layout.activity_lost_found_detail);
		bitmapUtil = new BitmapUtils(ct);
		initTitleBar();
		titleTv.setText("详情");
		upic = (ImageView) findViewById(R.id.upic);
		tv_uname = (TextView) findViewById(R.id.tv_uname);
		tv_udescription = (TextView) findViewById(R.id.tv_udescription);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_ltime = (TextView) findViewById(R.id.tv_ltime);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_ilabel = (TextView) findViewById(R.id.tv_ilabel);
		tv_llabel = (TextView) findViewById(R.id.tv_llabel);
		tv_contact = (TextView) findViewById(R.id.tv_contact);
		tv_description = (TextView) findViewById(R.id.tv_description);
		noScrollgridview = (MyGridView) findViewById(R.id.noScrollgridview);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (LostItemInfo) bund.getSerializable("LostItem");
	}


	@Override
	protected void initData() {
		tv_uname.setText(info.getUname());
		tv_udescription.setText(info.getUdescription());
		tv_title.setText(info.getTitle());
		tv_type.setText(getString(R.string.lost_type, info.getType()));
		tv_ilabel.setText(getString(R.string.lost_ilabel, info.getIlabel()));
		tv_llabel.setText(getString(R.string.lost_llabel, info.getLlabel()));
		tv_contact.setText(getString(R.string.contacts_of, info.getContact()));
		tv_description.setText(getString(R.string.lost_description, info.getDescription()));
		if(info.getLost() == 1){
			tv_location.setText(getString(R.string.lost_location, info.getLocation()));
			tv_ltime.setText(getString(R.string.lost_time,info.getLtime()));
		}else{
			tv_location.setText(getString(R.string.found_location, info.getLocation()));
			tv_ltime.setText(getString(R.string.found_time,info.getLtime()));
		}
		bitmapUtil.display(upic, ConstantValue.LOTTERY_URI+info.getUpic());
		MyPhotoGridAdapter adapter = new MyPhotoGridAdapter(ct, info.getPic().split(","),false);
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ct, PhotoActivity.class);
				intent.putExtra("ID", position);
				intent.putExtra("isNet", true);
				for(String pic : info.getPic().split(",")){
					Bimp.drr.add(pic);
					Bimp.max = info.getPic().split(",").length;
				}
				startActivity(intent);
			}
		});
	}



	@Override
	protected void processClick(View v) {

	}


}
