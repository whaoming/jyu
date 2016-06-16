package com.jyu.view.activity;

import java.io.IOException;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.SecondHandInfo;
import com.jyu.view.adapter.MyPhotoGridAdapter;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.ActionSheetDialog;
import com.jyu.view.custom.mydialog.ActionSheetDialog.OnSheetItemClickListener;
import com.jyu.view.custom.photoutil.Bimp;
import com.jyu.view.custom.photoutil.FileUtils;


public class SecondHandPublishActivity extends BaseActivity {

	private GridView noScrollgridview;
	private MyPhotoGridAdapter adapter;
	private EditText et_title;
	private EditText et_description;
	private EditText et_price;

	private RelativeLayout label;
	private TextView tv_label;
	private LinearLayout lin2;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_publish_secondhand);
		initTitleBar();
		initRight("发布");
		lin2 = (LinearLayout) findViewById(R.id.lin2);
		label = (RelativeLayout) findViewById(R.id.rl_label);
		tv_label = (TextView) findViewById(R.id.tv_label);
		et_title = (EditText) findViewById(R.id.title);
		et_price = (EditText) findViewById(R.id.price);
		et_description = (EditText) findViewById(R.id.description);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new MyPhotoGridAdapter(ct, null, true);
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 判断是点添加按钮还是照片
				if (arg2 == Bimp.bmp.size()) {
					// 点的是添加照片功能，所以接下来应该看他添加照片后要ganma
					Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
					getImage.addCategory(Intent.CATEGORY_OPENABLE);
					getImage.setType("image/*");
					startActivityForResult(getImage, 10);
				} else {
					// 点击某张照片
					Intent intent = new Intent(SecondHandPublishActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		label.setOnClickListener(this);
	}

	@Override
	protected void initData() {

	}

	String[] infos = { "摩托车", "电脑配件", "自行车", "服装", "家电", "电子产品", "书本", "其他" };

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.rl_label:
			new ActionSheetDialog(ct).builder().setTitle("请选择标签")
					.addSheetItems(infos, new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							lin2.setVisibility(View.VISIBLE);
							tv_label.setText(infos[which - 1]);
						}
					}).show();
			break;
		case R.id.rightbutton:
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

			SecondHandInfo info = new SecondHandInfo();
			info.setLabel(tv_label.getText().toString().trim());
			info.setUname(GlobalParams.userInfo.name);
			info.setUserid(GlobalParams.userInfo.id);
			info.setData(CommonUtil.getStringDate());
			info.setUdescription(GlobalParams.userInfo.description);
			info.setUname(GlobalParams.userInfo.name);
			info.setTitle(et_title.getText().toString().trim());
			info.setDescription(et_description.getText().toString().trim());

			info.setPic(pic);
			info.setUpic(GlobalParams.userInfo.pic);
			info.setPrice(et_price.getText().toString().trim());
			Bundle bund = new Bundle();
			bund.putSerializable("NewItem", info);
			intent.putExtra("value", bund);
			Bimp.bmp.clear();
			Bimp.drr.clear();
			Bimp.max = 0;
			setResult(1, intent);
			finish();// 结束焦点
			break;
		case R.id.activity_selectimg_back:
			// new AlertDialog(ct)
			// .builder()
			// .setTitle("编辑还未完成，是否退出?")
			// .setCancelable(false)
			// .addSheetItem("确定", SheetItemColors.Blue, new
			// OnSheetItemClickListeners() {
			//
			// @Override
			// public void onClick(int which) {
			//
			// }
			// }).addSheetItem("取消", SheetItemColors.Blue, listener)
			Bimp.bmp.clear();
			Bimp.drr.clear();
			Bimp.max = 0;
			FileUtils.deleteDir();
			break;
		}
	}

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

	// @SuppressLint("HandlerLeak")
	// public class GridAdapter extends BaseAdapter {
	// private LayoutInflater inflater; // 视图容器
	// private int selectedPosition = -1;// 选中的位置
	// private boolean shape;
	//
	// public boolean isShape() {
	// return shape;
	// }
	//
	// public void setShape(boolean shape) {
	// this.shape = shape;
	// }
	//
	// public GridAdapter(Context context) {
	// inflater = LayoutInflater.from(context);
	// }
	//
	// // public void update() {
	// // loading();
	// // }
	//
	// public int getCount() {
	// return (Bimp.bmp.size() + 1);
	// }
	//
	// public Object getItem(int arg0) {
	//
	// return null;
	// }
	//
	// public long getItemId(int arg0) {
	//
	// return 0;
	// }
	//
	// public void setSelectedPosition(int position) {
	// selectedPosition = position;
	// }
	//
	// public int getSelectedPosition() {
	// return selectedPosition;
	// }
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	// final int coord = position;
	// ViewHolder holder = null;
	// if (convertView == null) {
	//
	// convertView = inflater.inflate(R.layout.item_published_grida,
	// parent, false);
	// holder = new ViewHolder();
	// holder.image = (ImageView) convertView
	// .findViewById(R.id.item_grida_image);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	//
	// if (position == Bimp.bmp.size()) {
	// holder.image.setImageBitmap(BitmapFactory.decodeResource(
	// getResources(), R.drawable.add_item_hover));
	// if (position == 9) {
	// holder.image.setVisibility(View.GONE);
	// }
	// } else {
	// holder.image.setImageBitmap(Bimp.bmp.get(position));
	// }
	//
	// return convertView;
	// }
	//
	// public class ViewHolder {
	// public ImageView image;
	// }
	// }

}
