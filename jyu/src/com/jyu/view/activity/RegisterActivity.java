package com.jyu.view.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.bean.R_User.UserInfo;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.User;
import com.jyu.engine.UserLoginEngine;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;


public class RegisterActivity extends BaseActivity {

	private static final int PHOTO_SUCCESS = 1;
	private static final int CAMERA_SUCCESS = 2;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int PHOTORESOULT = 3;// 结果
	
	private RelativeLayout btn_ok;
	private EditText et_name;
	private EditText et_username;
	private EditText et_password;
	private ImageView iv_upic;
	
	Bitmap photo;
	private String upic;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_register);
		initTitleBar();
		btn_ok = (RelativeLayout) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		et_name = (EditText) findViewById(R.id.et_name);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		iv_upic = (ImageView) findViewById(R.id.iv_upic);
		iv_upic.setOnClickListener(this);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			String username = et_username.getText().toString().trim();
			String password= et_password.getText().toString().trim();
			String name = et_name.getText().toString().trim();
			User user = new User(username, password, upic, name);
			showProgressDialog("正在注册中");
//			registerUser(user);
			break;
		case R.id.iv_upic:
			new AlertDialog(ct).builder().setTitle("照片来源").setCancelable(false)
			.addSheetItem("手机相册", SheetItemColors.Blue, new OnSheetItemClickListeners() {
				@Override
				public void onClick(int which) {
					Intent getImage = new Intent(
							Intent.ACTION_GET_CONTENT);
					getImage.addCategory(Intent.CATEGORY_OPENABLE);
					getImage.setType("image/*");
					startActivityForResult(getImage, PHOTO_SUCCESS);
				}
			})
			.addSheetItem("照片拍摄",  SheetItemColors.Blue, new OnSheetItemClickListeners() {
				
				@Override
				public void onClick(int which) {
					Intent getImageByCamera = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					getImageByCamera.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									"myImage/newtemp.jpg")));
					startActivityForResult(getImageByCamera,
							CAMERA_SUCCESS);
				}
			})
			.show();
			break;
		default:
			break;
		}
	}
	
	private void registerUser(final UserInfo user) {
		
		new AsyncTask<UserInfo, Void, UserInfo>() {
			@Override
			protected UserInfo doInBackground(UserInfo... params) {
				UserLoginEngine engine = BeanFactory.getInstance().getImpl(UserLoginEngine.class);
//				User u = engine.registerUser(user);
				return null;
			}
			@Override
			protected void onPostExecute(UserInfo result) {
				closeProgressDialog();
				if(result!=null){
					showToast("注册成功");
					GlobalParams.userInfo = result;
					Intent intent = new Intent(ct, UserInfoActivity.class);
					startActivity(intent);
					finish();
				}else{
					showToast("连接不到服务器");
				}
				super.onPostExecute(result);
			}
		}.execute();
	}

	/*
	 * 进行图片剪裁
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		getContentResolver();
		if(data!=null){
			if (requestCode == CAMERA_SUCCESS) {// 拍照
				// 设置文件保存路径这里放在跟目录下
				File picture = new File(Environment.getExternalStorageDirectory()
						+ "/myImage/newtemp.jpg");
				startPhotoZoom(Uri.fromFile(picture));
			}
			if (requestCode == PHOTO_SUCCESS) {// 相册选择
				startPhotoZoom(data.getData());
			}
			// 处理结果
			if (requestCode == PHOTORESOULT) {// 裁剪成功后
				Bundle extras = data.getExtras();
				if (extras != null) {
					photo = extras.getParcelable("data");
					iv_upic.setImageBitmap(photo);
					upic = CommonUtil.getBitmapString(photo);
				}
			}
		}
		
	}

}
