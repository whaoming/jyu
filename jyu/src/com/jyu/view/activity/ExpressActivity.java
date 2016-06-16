package com.jyu.view.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.R;
import com.jyu.domain.Express;
import com.jyu.domain.ExpressInfo;
import com.jyu.engine.impl.ExpressEngineImpl;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.ActionSheetDialog;
import com.jyu.view.custom.mydialog.ActionSheetDialog.OnSheetItemClickListener;
import com.jyu.view.util.DialogUtils;

public class ExpressActivity extends BaseActivity {

	String[] all_name;
	String[] all_code;
	private List<String> names;
	private LinearLayout expressname;
	private TextView tv_name;
	private EditText et_number;
	private RelativeLayout bt_ok;
	
	private String name;
	
	
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_express);
		initTitleBar();
		titleTv.setText("快递查询");
		expressname = (LinearLayout) findViewById(R.id.expressname);
		expressname.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.tv_name);
		et_number = (EditText) findViewById(R.id.et_number);
		bt_ok = (RelativeLayout) findViewById(R.id.bt_ok);
		bt_ok.setOnClickListener(this);
		}

	@Override
	protected void initData() {
		names = new ArrayList<String>();
		 all_name = getResources().getStringArray(R.array.common);
		 all_code = getResources().getStringArray(R.array.common_code);
		 for(int i = 0;i<all_name.length;i++){
			 names.add(all_name[i]);
		 }
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.expressname:
			new ActionSheetDialog(ct)
			.builder()
			.setTitle("请选择快递公司")
			.addSheetItems(all_name, new OnSheetItemClickListener() {
				@Override
				public void onClick(int which) {
					name = all_code[which-1];
					tv_name.setText(all_name[which-1]);
				}
			}).show();
			break;
		case R.id.bt_ok://提交数据并跳转页面
			showProgressDialog("正在查询中");
			queryInfo();
			break;
		}
	}

	private void queryInfo() {
		final String number = et_number.getText().toString();
		new AsyncTask<String, Void, ExpressInfo>() {
			@Override
			protected ExpressInfo doInBackground(String... params) {
				ExpressEngineImpl impl = new ExpressEngineImpl();
				ExpressInfo result = impl.getExpressInfo(name, number);
				return result;
			}

			@Override
			protected void onPostExecute(ExpressInfo result) {
				closeProgressDialog();
				if(result.getStatus() == 0){
					DialogUtils.showToast(ct, "单号不存在");
				}else if(result.getStatus() == 1 || result.getStatus() == 3){
					//跳转页面，还要把data带过去
					List<Express> data = result.getData();
					Intent intent = new Intent(ct,ExpressFollowInfoActivity.class);
					Bundle bund = new Bundle();
					bund.putSerializable("data", (Serializable) data);
					intent.putExtra("value", bund);
					startActivity(intent);
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

}
