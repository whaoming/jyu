package com.jyu.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.Social;
import com.jyu.engine.SocialEngine;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;
import com.jyu.view.custom.mydialog.AlertDialog.setEditPositiveClickListener;
import com.jyu.view.util.DialogUtils;
import com.lidroid.xutils.BitmapUtils;

public class SocialPublishActivity extends BaseActivity {

	private int isSignUp;
	private int type;
	private int neednum;
	private EditText et_title;
	private EditText et_time;
	private EditText et_location;
	private EditText et_contact;
	private EditText et_detail;
	private TextView signupnum;
	private TextView tv_type;
	private LinearLayout issignup;
	private LinearLayout stype;

	String img_path;

	private TextView ok_text;
	private ImageView pic;
	private RelativeLayout addpic;
	private RelativeLayout btn_ok;

	BitmapUtils bitmapUtil;
	private Social info;
	private boolean isEdit;
	private Social social;
	
	private int position;
	private boolean editPhoto = false;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_publish_social);
		initTitleBar();
		// initRight("完成");
		ok_text = (TextView) findViewById(R.id.ok_text);
		pic = (ImageView) findViewById(R.id.pic);
		signupnum = (TextView) findViewById(R.id.signupnum);
		tv_type = (TextView) findViewById(R.id.tv_type);
		addpic = (RelativeLayout) findViewById(R.id.addpic);
		btn_ok = (RelativeLayout) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		issignup = (LinearLayout) findViewById(R.id.issignup);
		issignup.setOnClickListener(this);
		stype = (LinearLayout) findViewById(R.id.type);
		stype.setOnClickListener(this);
		et_title = (EditText) findViewById(R.id.et_title);
		et_time = (EditText) findViewById(R.id.et_time);
		et_location = (EditText) findViewById(R.id.et_location);
		et_contact = (EditText) findViewById(R.id.et_contact);
		et_detail = (EditText) findViewById(R.id.et_detail);
		bitmapUtil = new BitmapUtils(ct);
		addpic.setOnClickListener(this);
		Intent intent = getIntent();
		isEdit = intent.getBooleanExtra("isEdit", false);
		if(isEdit){
			Bundle bund = intent.getBundleExtra("value");
			info = (Social) bund.getSerializable("info");
			position = intent.getIntExtra("position", -1);
		}
		
	}

	@Override
	protected void initData() {
		if (isEdit) {
			ok_text.setText("修改完成");
			et_title.setText(info.getTitle());
			et_time.setText(info.getTime());
			et_detail.setText(info.getDescription());
			tv_type.setText(info.getType() == 1 ? "社团" : "个人");
			et_location.setText(info.getLocation());
			et_contact.setText(info.getContact());
			bitmapUtil.display(pic, ConstantValue.LOTTERY_URI + info.getPic());
			signupnum.setText(info.getIssignup() == 1 ? "是，需要人数为"
					+ info.getNeed() + "人" : "否");
		}
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.addpic:
			Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
			getImage.addCategory(Intent.CATEGORY_OPENABLE);
			getImage.setType("image/*");
			startActivityForResult(getImage, 10);
			break;
		case R.id.issignup:
			new AlertDialog(ct)
					.builder()
					.setTitle("请输入报名人数")
					.setCancelable(false)
					.addSheetItem("是", SheetItemColors.Blue,
							new OnSheetItemClickListeners() {
								@Override
								public void onClick(int which) {
									isSignUp = 1;
									new AlertDialog(ct)
											.builder()
											.setTitle("请输入报名人数")
											.setCancelable(false)
											.setPositiveButton(
													"确认",
													new setEditPositiveClickListener() {

														@Override
														public void onClick(
																View v,
																String msg) {
															neednum = Integer
																	.valueOf(msg);
															signupnum
																	.setText("是，需要人数为"
																			+ neednum
																			+ "人");
														}
													})
											.setNegativeButton("取消",
													new OnClickListener() {
														@Override
														public void onClick(
																View v) {

														}
													}).addEdit("").show();
								}
							})
					.addSheetItem("否", SheetItemColors.Blue,
							new OnSheetItemClickListeners() {
								@Override
								public void onClick(int which) {
									isSignUp = 0;
									signupnum.setText("否");
								}
							}).show();
			break;
		case R.id.type:
			new AlertDialog(ct)
					.builder()
					.setTitle("请选择活动类型")
					.setCancelable(false)
					.addSheetItem("个人", SheetItemColors.Blue,
							new OnSheetItemClickListeners() {
								@Override
								public void onClick(int which) {
									tv_type.setText("个人");
								}
							})
					.addSheetItem("社团", SheetItemColors.Blue,
							new OnSheetItemClickListeners() {
								@Override
								public void onClick(int which) {
									tv_type.setText("社团");
								}
							}).show();
			break;
		case R.id.btn_ok:
			showProgressDialog("");
			social = new Social();
			if(isEdit){
				social.setUserid(info.getUserid());
				social.setId(info.getId());
				social.setName(info.getName());
			}
			social.setTitle(et_title.getText().toString());
			social.setIssignup(isSignUp);
			social.setLocation(et_location.getText().toString());
			social.setTime(et_time.getText().toString().trim());
			social.setType(type);
			if(editPhoto){
				social.setPic(CommonUtil.processPic(img_path));
			}else{
				social.setPic(info.getPic());
			}
			social.setContact(et_contact.getText().toString());
			social.setDescription(et_detail.getText().toString());
			social.setNeed(neednum + "");
			processData(social);
			break;
		}
	}

	private void processData(final Social social) {
		new AsyncTask<Social, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Social... params) {
//				SocialEngineImpl impl = new SocialEngineImpl(ct);
				SocialEngine engine = BeanFactory.getInstance().getImpl(SocialEngine.class);
				boolean result = false;
				if(isEdit){
					result = engine.updateSocial(social);
				}else{
					result = engine.publishSocial(social);
				}
				return result;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				closeProgressDialog();
				if (result) {
					DialogUtils.showToast(ct, "发布成功");
					if(isEdit){
						Intent intent = new Intent(ct, MySocialAboutActivity.class);
						intent.putExtra("position", position);
						Bundle bund = new Bundle();
						if(editPhoto){
							social.setPic(img_path);
						}
						bund.putSerializable("info", social);
						intent.putExtra("value", bund);
						setResult(1,intent); 
					}else{
						setResult(1,getIntent()); 
					}
					
//					intent.putExtra("position", position);
					finish();
				} else {
					DialogUtils.showToast(ct, "发布失败");
				}
				
				super.onPostExecute(result);
			}
		}.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == 10) {
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor actualimagecursor = managedQuery(uri, proj, null, null,
						null);
				int actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				actualimagecursor.moveToFirst();
				img_path = actualimagecursor
						.getString(actual_image_column_index);
				bitmapUtil.display(pic, img_path);
				editPhoto = true;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
