package com.jyu.view.custom.mydialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.lidroid.xutils.BitmapUtils;

public class JoinSocialDialog extends Dialog {
	private Context context;
	public JoinSocialDialogListener listener;
	private RelativeLayout send;
	private EditText et_tname;
	private EditText et_snum;
	private TextView tv_name;
	private ImageView iv_upic;
	BitmapUtils bitmapUtil;
	
	private String upic;
	private String uname;

	public JoinSocialDialog(Context context,String upic,String uname, JoinSocialDialogListener joinSocialDialogListener) {
		super(context, R.style.blend_theme_dialog);
		this.context = context;
		this.listener = joinSocialDialogListener;
		this.upic = upic;
		this.uname = uname;
		bitmapUtil = new BitmapUtils(context);
	}

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(context);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_join_social, null);
		et_tname = (EditText) layout.findViewById(R.id.et_tname);
		et_tname.setText(GlobalParams.userInfo.tname==null?"":GlobalParams.userInfo.tname);
		et_snum = (EditText) layout.findViewById(R.id.et_snum);
		et_snum.setText((GlobalParams.userInfo.snum==0+""?"":GlobalParams.userInfo.snum+""));
		tv_name = (TextView) layout.findViewById(R.id.tv_name);
		iv_upic = (ImageView) layout.findViewById(R.id.iv_upic);
		send = (RelativeLayout) layout.findViewById(R.id.btn_ok);
		tv_name.setText(uname);
		bitmapUtil.display(iv_upic, ConstantValue.LOTTERY_URI+upic);
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.seng(et_tname.getText().toString().trim(),et_snum.getText().toString().trim());
				dismiss();

			}
		});
		
		this.setContentView(layout);
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int width = (int) (displayMetrics.widthPixels * 0.90);
		getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);

	}

	public interface JoinSocialDialogListener {
		void seng(String tname,String snum);
	}

}
