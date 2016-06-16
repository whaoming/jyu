package com.jyu.view.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.bean.R_Social.SocialItem;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.engine.SocialEngine;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.JoinSocialDialog;
import com.jyu.view.custom.mydialog.JoinSocialDialog.JoinSocialDialogListener;
import com.jyu.view.util.CustomProgressDialog;
import com.jyu.view.util.DialogUtils;
import com.lidroid.xutils.BitmapUtils;

public class SocialDetailActivity extends BaseActivity {

	private SocialItem info;
	
	private TextView title;
	private TextView time;
	private TextView location;
	private TextView name;
	private TextView number;
	private TextView contact;
	private TextView description;
	
	private ImageView picture;
	
	private Button join;

	private BitmapUtils bitmapUtil;
	
	/**
	 * 记录上个页面带过来的join(表示此活动你是否已经参加)
	 */
	private boolean isJoin;
	/**
	 * 记录上个页面带过来的position
	 */
	private int position;
	
	/**
	 * 状态是否有改变
	 */
	private boolean temp = false;
	
	private LinearLayout title_bar;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_social_detail);
		title_bar = (LinearLayout) findViewById(R.id.title_bar);
		title_bar.setBackgroundColor(Color.WHITE);
		initTitleBar();
		titleTv.setText("活动详情");
		Intent intent = getIntent();
		isJoin = intent.getBooleanExtra("isJoin", false);
		position = intent.getIntExtra("location", -1);
				
		Bundle bund = intent.getBundleExtra("value");
		info = (SocialItem) bund.getSerializable("socialinfo");
		
		picture = (ImageView) findViewById(R.id.picture);
		title = (TextView)findViewById(R.id.title);
		description = (TextView) findViewById(R.id.tv_description);
		name = (TextView) findViewById(R.id.name);
		time = (TextView) findViewById(R.id.time);
		name = (TextView) findViewById(R.id.name);
		location = (TextView)findViewById(R.id.location);
		number = (TextView)findViewById(R.id.number);
		contact = (TextView) findViewById(R.id.tv_contact);
		join = (Button) findViewById(R.id.join);
		join.setOnClickListener(this);
		bitmapUtil = new BitmapUtils(ct);
	}

	@Override
	protected void initData() {
		if(info.issignup == 1){
			if(isJoin){
				join.setText("已报名");
			}
		}else{
			join.setVisibility(View.GONE);
		}
		
		title.setText(info.title);
		bitmapUtil.display(picture, ConstantValue.LOTTERY_URI + info.pic);
		name.setText(getString(R.string.publish_name,info.name));
		time.setText(getString(R.string.time_at,info.time));
		location.setText(getString(R.string.place_at,info.location));
		contact.setText(getString(R.string.contacts_of,info.contact));
//		mBeginDateTv.setText(getString(R.string.begin_time_at,info.getStarttime()==null?"无":info.getStarttime()));
//		mEndDateTv.setText(getString(R.string.end_time_at,info.getEndtime() == null?"无":info.getEndtime()));
//		mCategoryTv.setText(getString(R.string.category_at,info.getType() == 1?"社团":"个人"));
		location.setText(getString(R.string.place_at,info.location));
		description.setText(info.description);
//		mDetailTv.setText(info.getDescription() == null?"暂无":info.getDescription());
//		mContactsTv.setText(getString(R.string.contacts_of,info.getContact()==null?"暂无":info.getContact()));
		if(info.issignup == 1){
			number.setText(getString(R.string.join_and_need_number,info.signup,info.need+"" == ""?"无限制":info.need));
		}else{
			number.setText("无需要报名");
		}
		
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.join:
			if(isJoin){
				DialogUtils.showToast(SocialDetailActivity.this, "你已经报名了，无须重复报名");
			}else{
				if(GlobalParams.userInfo == null){
					DialogUtils.showToast(SocialDetailActivity.this, "亲，请先登录");
				}else{
//					showProgressDialog("正在报名");
					sendData();
				}
				
			}
			
			break;
		case R.id.activity_selectimg_back:
			/**
			 * 这个intent可以记录他在列表中的位置从而达到你回去了以后item会做更新
			 */
			Intent intent = new Intent(SocialDetailActivity.this, SocialActivity.class);
			intent.putExtra("position", position);
			intent.putExtra("isJoin", temp);
			if(info.type == 2){//1-社团  2-个人
				setResult(1,intent); 
			}else{
				setResult(2,intent); 
			}
			finish();
		}

	}

	private void sendData() {
//		new AsyncTask<Integer, Void, Boolean>() {
//			@Override
//			protected Boolean doInBackground(Integer... params) {
//				SocialEngineImpl impl = new SocialEngineImpl(SocialDetailActivity.this);
//				boolean result = impl.joinSocial(info.getId());
//				return result;
//			}
//			@Override
//			protected void onPostExecute(Boolean result) {
//				closeProgressDialog();
//				if(result){
//					DialogUtils.showToast(ct, "成功");
//					join.setText("已报名");
//					temp = true;
//					number.setText(getString(R.string.join_and_need_number,info.getSignup()+1,info.getNeed()));
//				}else{
//					DialogUtils.showToast(ct, "获取服务器数据失败");
//				}
//				super.onPostExecute(result);
//			}
//		}.execute();
		final CustomProgressDialog d = (CustomProgressDialog) DialogUtils
				.createProgressDialog(ct, "正在报名");
		new JoinSocialDialog(ct,
				GlobalParams.userInfo.pic,
				GlobalParams.userInfo.name,
				new JoinSocialDialogListener() {
					@Override
					public void seng(String tname,
							String snum) {
						d.show();
						new AsyncTask<String, Void, Boolean>() {
							@Override
							protected Boolean doInBackground(
									String... params) {
//								SocialEngineImpl impl = new SocialEngineImpl();
								SocialEngine impl = BeanFactory.getInstance().getImpl(SocialEngine.class);
								boolean result = impl
										.joinSocial(info
												.id);
								return result;
							}

							@Override
							protected void onPostExecute(
									Boolean result) {
								if (result) {
									DialogUtils.showToast(
											ct, "报名成功");
									number.setText(getString(R.string.join_and_need_number,info.signup+1,info.need));
									join.setText("已报名");
									isJoin = true;
								} else {
									DialogUtils.showToast(
											ct, "报名失败");
								}
								d.dismiss();
								super.onPostExecute(result);
							}
						}.execute();
					}
				}).show();
	}

}
