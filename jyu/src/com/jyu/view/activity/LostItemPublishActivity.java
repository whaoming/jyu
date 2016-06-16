package com.jyu.view.activity;

import java.io.IOException;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.domain.LostItemInfo;
import com.jyu.view.adapter.MyPhotoGridAdapter;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.ActionSheetDialog;
import com.jyu.view.custom.mydialog.ActionSheetDialog.OnSheetItemClickListener;
import com.jyu.view.custom.photoutil.Bimp;
import com.jyu.view.custom.photoutil.FileUtils;

public class LostItemPublishActivity extends BaseActivity {

	private GridView noScrollgridview;
	private MyPhotoGridAdapter adapter;
	private EditText et_title;
	private EditText et_location;
	private EditText et_contact;
	private EditText et_description;
	private EditText et_type;
	private EditText et_time;

	private TextView tv_llabel;
	private TextView tv_ilabel;// 物品标签

	private RelativeLayout rl_llabel;
	private RelativeLayout rl_ilabel;
	
	/**
	 * true为寻物启事，false是失物招领
	 */
	private boolean isLost ;//是否为失物找回
	
	private TextView tv_location;
	private TextView tv_time;

	// 物品标签
	String[] ilabel = { "钱包", "银行卡", "饭卡", "书本", "鼠标", "钥匙", "手机", "文具", "背包",
			"其他" };
	// 地点标签
	String[] llabel = { "中区", "南区", "东区", "仙子大楼", "何侨生大楼", "锡科", "田师", "中区篮球场",
			"中区足球场" };
	
	/**
	 * 是否为编辑页面，还是发布页面
	 */
	private boolean isEdit;
	
	/**
	 * 如果为编辑页面，此字段记录所编辑的在他所在item的位置 ，方便编辑后可更新此item(position)
	 */
	private int position;
	
	/**
	 * 如果为编辑页面 ，此info为带过来的ietm(position)
	 */
	private LostItemInfo info;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_publish_lostitem);
		initTitleBar();
		initRight("完成");
		Intent intent = getIntent();
		isLost = intent.getBooleanExtra("isLost", false);
		isEdit = intent.getBooleanExtra("isEdit", false);
		if(isEdit){
			Bundle bund = intent.getBundleExtra("value");
			info = (LostItemInfo) bund.getSerializable("LostItem");
			position = intent.getIntExtra("position", -1);
		} 
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_time = (TextView) findViewById(R.id.tv_time);
		if(!isLost){
			tv_location.setText("捡到地点");
			tv_time.setText("捡到时间");
		}
		et_title = (EditText) findViewById(R.id.et_title);
		et_location = (EditText) findViewById(R.id.et_location);
		et_contact = (EditText) findViewById(R.id.et_contact);
		et_description = (EditText) findViewById(R.id.et_description);
		et_type = (EditText) findViewById(R.id.et_type);
		et_time = (EditText) findViewById(R.id.et_ltime);

		tv_ilabel = (TextView) findViewById(R.id.tv_ilabel);
		tv_llabel = (TextView) findViewById(R.id.tv_llabel);

		rl_llabel = (RelativeLayout) findViewById(R.id.rl_llabel);
		rl_llabel.setOnClickListener(this);
		rl_ilabel = (RelativeLayout) findViewById(R.id.rl_ilabel);
		rl_ilabel.setOnClickListener(this);

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		if(isEdit){
			adapter = new MyPhotoGridAdapter(ct, info.getPic().split(","), false);
		}else{
			adapter = new MyPhotoGridAdapter(ct,null,true);
		}
		
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 判断是点添加按钮还是照片
				if (arg2 == Bimp.bmp.size()) {
					// 点的是添加照片功能，所以接下来应该看他添加照片后要ganma
					// new PopupWindows(PublishActivity.this, noScrollgridview);
					Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
					getImage.addCategory(Intent.CATEGORY_OPENABLE);
					getImage.setType("image/*");
					startActivityForResult(getImage, 10);
				} else {
					// 点击某张照片
					Intent intent = new Intent(ct, PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
					//
				}
			}
		});
	}

	@Override
	protected void initData() {
		if(isEdit){
			et_title.setText(info.getTitle());
			et_location.setText(info.getLocation());
			et_contact.setText(info.getContact());
			et_description.setText(info.getDescription());
			et_type.setText(info.getType());
			et_time.setText(info.getLtime());
			tv_ilabel.setText(info.getIlabel());
			tv_llabel.setText(info.getLlabel());
		}
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.rl_llabel:
			new ActionSheetDialog(ct).builder().setTitle("请选择地点标签")
					.addSheetItems(llabel, new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							tv_llabel.setText(llabel[which - 1]);
						}
					}).show();
			break;
		case R.id.rl_ilabel:
			new ActionSheetDialog(ct).builder().setTitle("请选择物品标签")
					.addSheetItems(ilabel, new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							tv_ilabel.setText(ilabel[which - 1]);
						}
					}).show();
			break;
		case R.id.rightbutton://点击完成按钮
			if(isEdit){
				Intent intent = getIntent();
				String title =  et_title.getText().toString().trim();
				String contact = et_contact.getText().toString().trim();
				String description = et_description.getText().toString().trim();
				String type = et_type.getText().toString().trim();
				String time = et_time.getText().toString().trim();
				String ilabel = tv_ilabel.getText().toString().trim();
				String llabel = tv_llabel.getText().toString().trim();
				String location = et_location.getText().toString().trim();
				info.setTitle(title);
				info.setContact(contact);
				info.setDescription(description);
				info.setType(type);
				info.setLtime(time);
				info.setLocation(location);
				info.setIlabel(ilabel);
				info.setLlabel(llabel);
				intent.putExtra("isEdit", isEdit);
				
				intent.putExtra("position", position);
				Bundle bund = new Bundle();
				bund.putSerializable("updateitem", info);
				intent.putExtra("value", bund);
				setResult(1, intent);
				finish();// 结束焦点
			}else{
				String pic = "";
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).substring(
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					String item = FileUtils.SDPATH + Str + ".JPEG";
					if (i == 0) {
						pic = item;
					} else {
						pic += "," + item;
					}
				}
				Intent intent = getIntent();
				String title =  et_title.getText().toString().trim();
				String contact = et_contact.getText().toString().trim();
				String description = et_description.getText().toString().trim();
				String type = et_type.getText().toString().trim();
				String time = et_time.getText().toString().trim();
				String ilabel = tv_ilabel.getText().toString().trim();
				String llabel = tv_llabel.getText().toString().trim();
				String location = et_location.getText().toString().trim();
				
				int userid = GlobalParams.userInfo.id;
				String uname = GlobalParams.userInfo.name;
				String udescription = GlobalParams.userInfo.description;
				String upic= GlobalParams.userInfo.pic;
				
				int lost = isLost?1:0;
				
				LostItemInfo info = new LostItemInfo(title, llabel, ilabel, type, description, pic, location, time, contact, userid, uname, udescription, upic,lost);
				
				Bundle bund = new Bundle();
				bund.putSerializable("NewItem", info);
				intent.putExtra("value", bund);
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				setResult(1, intent);
				finish();// 结束焦点
			}
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			switch (requestCode) {
			case 10:// 相册选择的相片返回回来啦
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
				int actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				actualimagecursor.moveToFirst();
				String img_path = actualimagecursor
						.getString(actual_image_column_index);
				Bimp.drr.add(img_path);
				my();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 把选择的照片存在临时文件中
	 */
	public void my() {
		String path = Bimp.drr.get(Bimp.max);
		Bitmap bm;
		try {
			bm = Bimp.revitionImageSize(path);
			Bimp.bmp.add(bm);
			String newStr = path.substring(path.lastIndexOf("/") + 1,
					path.lastIndexOf("."));
			FileUtils.saveBitmap(bm, "" + newStr);
			Bimp.max += 1;
			adapter.notifyDataSetChanged();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
