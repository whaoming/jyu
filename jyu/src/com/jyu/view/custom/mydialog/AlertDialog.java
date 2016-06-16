package com.jyu.view.custom.mydialog;

import java.util.ArrayList;
import java.util.List;

import com.jyu.R;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


@SuppressLint("InflateParams")
public class AlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
//	private TextView txt_msg;
	private Button btn_neg;
	private Button btn_pos;
	private ImageView img_line;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
	
	private ImageView line;
	
//	private LinearLayout lin;
	
	private LinearLayout lLayout_content;
	private ScrollView sLayout_content;
	private EditText et;

	public AlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	@SuppressWarnings("deprecation")
	public AlertDialog builder() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_alertdialog, null);

//		lin = (LinearLayout) view.findViewById(R.id.lin);
//		lin.setVisibility(View.GONE);
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
//		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
//		txt_msg.setVisibility(View.GONE);
		line = (ImageView) view.findViewById(R.id.line);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);

		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.80), LayoutParams.WRAP_CONTENT));
		
		
		lLayout_content = (LinearLayout) view
				.findViewById(R.id.lLayout_content);
		sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);

		return this;
	}

	public AlertDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("");
		} else {
			txt_title.setText(title);
		}
		return this;
	}

	public AlertDialog setMsg(String msg) {
		showMsg = true;
//		if ("".equals(msg)) {
//			txt_msg.setText("����");
//		} else {
//			txt_msg.setText(msg);
//		}
		return this;
	}

	public AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public interface setEditPositiveClickListener{
		void onClick(View v,String msg);
	}
	
	public AlertDialog setPositiveButton(String text,
			final setEditPositiveClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText("确定");
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v,et.getText().toString().trim());
				dialog.dismiss();
			}
		});
		return this;
	}

	public AlertDialog setNegativeButton(String text,
			final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	private void setLayout() {
		if (!showTitle && !showMsg) {
			txt_title.setText("��ʾ");
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}

//		if (showMsg) {
//			txt_msg.setVisibility(View.VISIBLE);
//		}

		if (!showPosBtn && !showNegBtn) {
//			btn_pos.setText("ȷ��");
//			btn_pos.setVisibility(View.VISIBLE);
//			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
//			btn_pos.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//			img_line.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}

		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
	}

	public void show() {
		setLayout();
		setSheetItems();
		dialog.show();
	}
	
	@SuppressLint("NewApi")
	public AlertDialog addEdit(String hint){
		et = new EditText(context);
		et.setTextSize(18);
		et.setGravity(Gravity.CENTER);
		float scale = context.getResources().getDisplayMetrics().density;
		int height = (int) (45 * scale + 1f);
		et.setBackground(null);
		et.setMaxLines(1);
		et.setHint(hint);
//		et.setLayoutParams(new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		et.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, height));
		lLayout_content.addView(et);
		return this;
	}
	@SuppressLint("NewApi")
	public AlertDialog addEdit(String hint,String msg){
		et = new EditText(context);
		et.setTextSize(18);
		et.setGravity(Gravity.CENTER);
		float scale = context.getResources().getDisplayMetrics().density;
		int height = (int) (45 * scale + 1f);
		et.setBackground(null);
		et.setMaxLines(1);
		if(hint==""){
			et.setText(msg);
		}else{
			et.setHint(hint);
		}
		
		et.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, height));
		lLayout_content.addView(et);
		return this;
	}
	
	private List<SheetItem> sheetItemList;
	
	public AlertDialog addSheetItem(String strItem, SheetItemColors color,
			OnSheetItemClickListeners listener) {
		if (sheetItemList == null) {
			sheetItemList = new ArrayList<SheetItem>();
		}
		sheetItemList.add(new SheetItem(strItem, color, listener));
		return this;
	}

	@SuppressWarnings("deprecation")
	private void setSheetItems() {
		if (sheetItemList == null || sheetItemList.size() <= 0) {
			return;
		}
		
		int size = sheetItemList.size();
		
		if (size >= 7) {
			LinearLayout.LayoutParams params = (LayoutParams) sLayout_content
					.getLayoutParams();
			params.height = display.getHeight() / 2;
			sLayout_content.setLayoutParams(params);
		}
				for (int i = 1; i <= size; i++) {
					final int index = i;
					SheetItem sheetItem = sheetItemList.get(i - 1);
					String strItem = sheetItem.name;
					SheetItemColors color = sheetItem.color;
					final OnSheetItemClickListeners listener = (OnSheetItemClickListeners) sheetItem.itemClickListener;

					TextView textView = new TextView(context);
					textView.setText(strItem);
					textView.setTextSize(18);
					textView.setGravity(Gravity.CENTER);

					// ����ͼƬ
					if (size == 1) {
						if (showTitle) {
							textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
						} else {
							textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
						}
					} else {
						if (showTitle) {
							if (i >= 1 && i < size) {
								textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
							} else {
								textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
							}
						} else {
							if (i == 1) {
								textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
							} else if (i < size) {
								textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
							} else {
								textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
							}
						}
					}

					// ������ɫ
					if (color == null) {
						textView.setTextColor(Color.parseColor(SheetItemColors.Blue
								.getName()));
					} else {
						textView.setTextColor(Color.parseColor(color.getName()));
					}

					// �߶�
					float scale = context.getResources().getDisplayMetrics().density;
					int height = (int) (45 * scale + 0.5f);
//					int height = (int) (45 * scale);
					textView.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, height));

					// ����¼�
					textView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							listener.onClick(index);
							dialog.dismiss();
						}
					});

					lLayout_content.addView(textView);
				}
	}
	
	public interface OnSheetItemClickListeners {
		void onClick(int which);
	}

	public class SheetItem {
		String name;
		OnSheetItemClickListeners itemClickListener;
		SheetItemColors color;

		public SheetItem(String name, SheetItemColors color,
				OnSheetItemClickListeners itemClickListener) {
			this.name = name;
			this.color = color;
			this.itemClickListener = itemClickListener;
		}
		
	}
	
	public enum SheetItemColors{
		Blue("#037BFF"), Red("#FD4A2E");

		private String name;

		private SheetItemColors(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
