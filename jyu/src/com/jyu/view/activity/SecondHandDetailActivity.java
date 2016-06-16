package com.jyu.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.domain.SecondHandInfo;
import com.jyu.engine.SecondHandEngine;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;
import com.jyu.view.custom.photoutil.Bimp;
import com.jyu.view.util.DialogUtils;
import com.lidroid.xutils.BitmapUtils;

public class SecondHandDetailActivity extends BaseActivity {

	private SecondHandInfo info;
	
	BitmapUtils bitmapUtil;
	
	private TextView price;
	private TextView data;
	private ImageView userHead;
	private TextView userName;
	private TextView udespripion;
	private TextView description;
	private ImageView pic;
	private ImageView iv_buy;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_secondhand_detail);
		initTitleBar();
		titleTv.setText("二手货详情");
		bitmapUtil = new BitmapUtils(ct);
		iv_buy = (ImageView) findViewById(R.id.iv_buy);
		iv_buy.setOnClickListener(this);
		pic = (ImageView) findViewById(R.id.pic);
		price = (TextView) findViewById(R.id.price);
		data = (TextView) findViewById(R.id.data);
		userHead = (ImageView) findViewById(R.id.userHead);
		userName = (TextView) findViewById(R.id.userName);
		udespripion = (TextView) findViewById(R.id.udespripion);
		description = (TextView) findViewById(R.id.description);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (SecondHandInfo) bund.getSerializable("SecondHandItem");
	}

	@Override
	protected void initData() {
		userName.setText(info.getUname());
		udespripion.setText(info.getUdescription());
		description.setText(info.getDescription());
		data.setText(info.getData());
		price.setText("$"+info.getPrice());
		bitmapUtil.display(userHead,
				ConstantValue.LOTTERY_URI+info.getUpic());
		bitmapUtil.display(pic,
				ConstantValue.LOTTERY_URI+info.getPic().split(",")[0]);
		pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, PhotoActivity.class);
				intent.putExtra("ID", 0);
				intent.putExtra("isNet", true);
				String[] piclist = info.getPic().split(",");
				for (String pic : piclist) {
					Bimp.drr.add(pic);
					Bimp.max = piclist.length;
				}
				startActivity(intent);
			}
		});
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_buy:
			new AlertDialog(ct).builder().setTitle("由于本软件无支付系统，所以请买家自行根据卖家留下的联系方式联络卖家，点击确定将会在卖家方留下信息")
			.addSheetItem("确定", SheetItemColors.Blue, new OnSheetItemClickListeners() {
				
				@Override
				public void onClick(int which) {
					new AsyncTask<String, Void, Boolean>() {
						@Override
						protected Boolean doInBackground(
								String... params) {
//							SecondHandEngineImpl impl = new SecondHandEngineImpl();
							SecondHandEngine engine = BeanFactory.getInstance().getImpl(SecondHandEngine.class);
							boolean result = engine.sendReply(info.getId());
							return result;
						}

						@Override
						protected void onPostExecute(
								Boolean result) {
							if (result) {
								DialogUtils.showToast(
										ct, "发送成功");
								
							} else {
								DialogUtils.showToast(
										ct, "发送失败");
							}
							super.onPostExecute(result);
						}
					}.execute(); 
					
				}
			})
			.addSheetItem("取消", SheetItemColors.Blue, new OnSheetItemClickListeners() {
				
				@Override
				public void onClick(int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
			break;
		}

	}

}
