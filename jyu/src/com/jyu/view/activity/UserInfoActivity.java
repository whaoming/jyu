package com.jyu.view.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.domain.User;
import com.jyu.engine.ImageEngine;
import com.jyu.engine.UserLoginEngine;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;
import com.jyu.view.custom.mydialog.AlertDialog.setEditPositiveClickListener;
import com.jyu.view.util.DialogUtils;
import com.lidroid.xutils.BitmapUtils;


public class UserInfoActivity extends BaseActivity {

	private static final int PHOTO_SUCCESS = 1;
	private static final int CAMERA_SUCCESS = 2;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int PHOTORESOULT = 3;// 结果

	private RelativeLayout rl_userpic;
	private RelativeLayout rl_name;
	private RelativeLayout rl_description;
	private RelativeLayout rl_tname;
	private RelativeLayout rl_snum;
	private RelativeLayout rl_sex;
	private RelativeLayout rl_hobit;

	private TextView tv_username;
	private TextView tv_name;
	private TextView tv_description;
	private TextView tv_tname;
	private TextView tv_snum;
	private TextView tv_sex;
	private TextView tv_hobit;
	private ImageView iv_userpic;
	
	private LinearLayout title_bar;


//	private String path = GlobalParams.user.getPic();
	Bitmap photo;

	BitmapUtils bitmapUtil;

	/**
	 * 记录服务机器是否修改成功
	 */
	boolean r = false;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_edituserinfo);
		title_bar = (LinearLayout) findViewById(R.id.title_bar);
		title_bar.setBackgroundColor(Color.WHITE);
		initTitleBar();

		titleTv.setText("个人资料编辑");
		rightbutton.setOnClickListener(this);
//		rl_username = (RelativeLayout) findViewById(R.id.rl_username);
		rl_name = (RelativeLayout) findViewById(R.id.rl_name);
		rl_name.setOnClickListener(this);
		rl_description = (RelativeLayout) findViewById(R.id.rl_description);
		rl_description.setOnClickListener(this);
		rl_tname = (RelativeLayout) findViewById(R.id.rl_tname);
		rl_tname.setOnClickListener(this);
		rl_snum = (RelativeLayout) findViewById(R.id.rl_snum);
		rl_snum.setOnClickListener(this);
		rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
		rl_sex.setOnClickListener(this);
		rl_hobit = (RelativeLayout) findViewById(R.id.rl_hobit);
		rl_hobit.setOnClickListener(this);
		rl_userpic = (RelativeLayout) findViewById(R.id.rl_userpic);
		rl_userpic.setOnClickListener(this);

		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_description = (TextView) findViewById(R.id.tv_description);
		tv_tname = (TextView) findViewById(R.id.tv_tname);
		tv_snum = (TextView) findViewById(R.id.tv_snum);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_hobit = (TextView) findViewById(R.id.tv_hobit);
		iv_userpic = (ImageView) findViewById(R.id.iv_userpic);

	}

	@Override
	protected void initData() {
		bitmapUtil = new BitmapUtils(ct);
		bitmapUtil.display(iv_userpic, ConstantValue.LOTTERY_URI
				+ GlobalParams.userInfo.pic);
		tv_username.setText(makeData(GlobalParams.userInfo.username));
		tv_name.setText(makeData(GlobalParams.userInfo.name));
		tv_description.setText(makeData(GlobalParams.userInfo.description));
		tv_tname.setText(makeData(GlobalParams.userInfo.tname));
		tv_snum.setText("");
		tv_snum.setText(makeData(GlobalParams.userInfo.snum + ""));
		tv_sex.setText(GlobalParams.userInfo.sex == 1 ? "男" : "女");
		tv_hobit.setText(makeData(GlobalParams.userInfo.hobit));
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.rl_name:
			showMyEditDialog("昵称", tv_name, "name");
			break;
		case R.id.rl_description:
			showMyEditDialog("随笔", tv_description, "description");
			break;
		case R.id.rl_tname:
			showMyEditDialog("真实姓名", tv_tname, "tname");
			break;
		case R.id.rl_snum:
			showMyEditDialog("短号", tv_snum, "snum");
			break;
		case R.id.rl_sex:
			new AlertDialog(ct).builder().setTitle("请选择性别")
			.addSheetItem("男", SheetItemColors.Blue, new OnSheetItemClickListeners() {
				@Override
				public void onClick(int which) {
					savaData("sex", "1");
					tv_sex.setText("男");
				}
			})
			.addSheetItem("女", SheetItemColors.Blue, new OnSheetItemClickListeners() {
				@Override
				public void onClick(int which) {
					tv_sex.setText("女");
					savaData("sex", "2");
				}
			})
			.show();
			break;
		case R.id.rl_hobit:
			showMyEditDialog("兴趣爱好", tv_hobit, "hobit");
			break;
		case R.id.rl_userpic:

//			final CharSequence[] items = { "手机相册", "相机拍摄" };
//			AlertDialog dlg = new AlertDialog.Builder(UserInfoActivity.this)
//					.setTitle("选择图片")
//					.setItems(items, new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int item) {
//							// 这里item是根据选择的方式,
//							// 在items数组里面定义了两种方式, 拍照的下标为1所以就调用拍照方法
//							if (item == 1) {// 拍照
//								Intent getImageByCamera = new Intent(
//										MediaStore.ACTION_IMAGE_CAPTURE);
//								getImageByCamera.putExtra(
//										MediaStore.EXTRA_OUTPUT,
//										Uri.fromFile(new File(Environment
//												.getExternalStorageDirectory(),
//												"myImage/newtemp.jpg")));
//								startActivityForResult(getImageByCamera,
//										CAMERA_SUCCESS);
//							} else {
//								Intent getImage = new Intent(
//										Intent.ACTION_GET_CONTENT);
//								getImage.addCategory(Intent.CATEGORY_OPENABLE);
//								getImage.setType("image/*");
//								startActivityForResult(getImage, PHOTO_SUCCESS);
//								// startActivityForResult(getImage,
//								// PHOTORESOULT);
//								// Intent intent = new
//								// Intent(Intent.ACTION_PICK, null);
//								// intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//								// "image/*");
//								// startActivityForResult(intent, PHOTORESOULT);
//							}
//						}
//					}).create();
//			dlg.show();
			
			new AlertDialog(ct).builder().setTitle("照片来源")
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
		}
	}

	// private void update(final User user) {
	// new AsyncTask<User, Void, Boolean>() {
	// @Override
	// protected Boolean doInBackground(User... params) {
	// UserEngineImpl impl = new UserEngineImpl();
	// // Log.i("aaa", "result=="+result);
	// boolean result = impl.updateUserInfo(user);
	// return result;
	// }
	//
	// @Override
	// protected void onPostExecute(Boolean result) {
	// if (result != null) {
	//
	// } else {
	// Log.i("LostActivity", "获取数据失败,result为空");
	// }
	// super.onPostExecute(result);
	// }
	// }.execute();
	//
	// }


	private void uploadPhoto() {
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				ImageEngine engine = BeanFactory.getInstance().getImpl(ImageEngine.class);
				String result = engine.savaUserHead(photo);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					DialogUtils.showToast(ct, "修改头像成功");
					GlobalParams.userInfo.pic = result;
					UserLoginEngine engine = BeanFactory.getInstance().getImpl(UserLoginEngine.class);
//					engine.updateUserInfoToCache(GlobalParams.user, ct);
				} else {
					DialogUtils.showToast(ct, "修改头像失败");
				}
				super.onPostExecute(result);
			}
		}.execute();

	}

//	protected void doData() {
////		Drawable drawable = new BitmapDrawable(photo);
////		iv_userpic.setBackgroundDrawable(drawable);
//	}

	private void showMyEditDialog(String title, final TextView vv,
			final String column) {
		// AlertDialog.Builder builder = new Builder(UserInfoActivity.this);
		// View view = View.inflate(UserInfoActivity.this,
		// R.layout.dialog_setup_userinfo, null);
		// tv_title = (TextView) view.findViewById(R.id.tv_title);
		// et_body = (MyEditText) view.findViewById(R.id.et_body);
		// ok = (TextView) view.findViewById(R.id.ok);
		// cancel = (TextView) view.findViewById(R.id.cancel);
		// tv_title.setText(title);
		// et_body.setText(vv.getText().toString().trim());
		// et_body.requestFocus();
		// cancel.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });
		// ok.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// String body = et_body.getText().toString().trim();
		// if(body.equals(vv.getText().toString().trim())){
		// }else{
		// boolean result = savaData(column,body,vv);
		// }
		// dialog.dismiss();
		// }
		// });
		// dialog = builder.create();
		// dialog.setView(view, 0, 0, 0, 0);
		// dialog.show();

		new AlertDialog(ct).builder().setTitle(title).setCancelable(false)
				.setPositiveButton("确认", new setEditPositiveClickListener() {
					@Override
					public void onClick(View v, String msg) {
						if(!msg.equals(vv.getText().toString().trim())){
							vv.setText(msg);
							savaData(column, msg);
						}
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					}
				}).addEdit("",vv.getText().toString().trim()).show();
	}

	protected void savaData(final String column, final String body) {

		new AsyncTask<User, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(User... params) {
//				CommonUtil.invokeSet(GlobalParams.user, column.equals("sex")?, body);
				if(column.equals("sex")){
//					CommonUtil.invokeSet(GlobalParams.user, column, Integer.valueOf(body));
				}else{
//					CommonUtil.invokeSet(GlobalParams.user, column, body);
				}
				UserLoginEngine engine = BeanFactory.getInstance().getImpl(UserLoginEngine.class);
				boolean result = engine.updateUserInfo(column, body,ct);
				return result;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					r = result;
				} else {
					DialogUtils.showToast(ct, "修改失败");
				}
				super.onPostExecute(result);
			}
		}.execute();
	}

	/**
	 * 处理user信息为空的情况
	 * 
	 * @param a
	 * @return
	 */
	public String makeData(String a) {
		return a == null ? "" : a;
	}

	

	/**
	 * 关于这方面的思路
	 * 然后这时候要先修改ui，给用户最好的体验
	 * 然后G里面的顺便改了，不管是否上传成功，
	 * 但是缓存里的就要result返回回来再改
	 * 这时候要考虑修改成功和修改失败的问题了
	 * 成功的话就修改到缓存中，失败的话要把G里面的改回来I
	 * 
	 */
//	@Override
//	protected void onStop() {
//		String name = tv_name.getText().toString().trim();
//		String description = tv_description.getText().toString().trim();
//		String tname = tv_tname.getText().toString().trim();
//		int snum = Integer.valueOf(tv_snum.getText().toString().trim());
//		int sex = tv_sex.getText().toString().trim()=="男"?1:2;
//		String hobit = tv_hobit.getText().toString().trim();
//		String pic = path;
//		String username = GlobalParams.user.getUsername();
//		int id = GlobalParams.user.getId();
//		 User user = new User(id,username,name,description,tname,snum,sex,1,hobit,pic);
//		 UserLoginEngine engine = BeanFactory.getInstance().getImpl(UserLoginEngine.class);
//		 engine.updateUserInfoToCache(user,ct);
//		super.onStop();
//	}
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
					iv_userpic.setImageBitmap(photo);
					uploadPhoto();
				}
			}
		}
		
	}
	
	
}
