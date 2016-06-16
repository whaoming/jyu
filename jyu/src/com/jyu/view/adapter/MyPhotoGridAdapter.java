package com.jyu.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.view.custom.photoutil.Bimp;
import com.lidroid.xutils.BitmapUtils;

public class MyPhotoGridAdapter extends BaseAdapter {

	private LayoutInflater inflater; // 视图容器
	private int selectedPosition = -1;// 选中的位置
	private boolean shape;
	private String[] piclist;
	BitmapUtils bitmapUtil;
	private Context ct;
	boolean isPublish;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	/**
	 * 
	 * @param context
	 * @param piclist
	 * @param isPublish
	 *            true 就是要显示添加图片的，而且图片显示是本地的，false就代表是网络上的,也就是item的
	 * 
	 * @param isEdit
	 *            但是如果是编辑页面，就要显示添加图片的并且图片有可能是网络上的或者本地的(新添加的就是本地的)
	 */
	public MyPhotoGridAdapter(Context context, String[] piclist,
			boolean isPublish) {
		inflater = LayoutInflater.from(context);
		this.piclist = piclist;
		this.ct = context;
		bitmapUtil = new BitmapUtils(context);
		this.isPublish = isPublish;
	}

	public int getCount() {
		if (isPublish) {
			return (Bimp.bmp.size() + 1);
		} else {
			return piclist.length;
		}

	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return arg0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_published_grida,
					parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (isPublish) {// 发布页面
			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						ct.getResources(), R.drawable.add_item_hover));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}
//
		} else {
			try {
				if (!piclist[position].startsWith("/image")) {
					bitmapUtil.display(holder.image, piclist[position]);
				} else {
					bitmapUtil.display(holder.image, ConstantValue.LOTTERY_URI
							+ piclist[position]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return convertView;
	}

	public class ViewHolder {
		ImageView image;

	}

}
