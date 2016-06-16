package com.jyu.view.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.domain.Repair;
import com.jyu.engine.RepairEngine;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.util.DialogUtils;


public class RepairActivity extends BaseActivity {

	private Repair repair;
	private EditText contact;
	private EditText dorm;
	private EditText detail;
	private Dialog dialog;
	
	
	@SuppressLint("CutPasteId")
	@Override
	protected void initView() {
		setContentView(R.layout.activity_repair);
		initTitleBar();
		initRight("完成");
		titleTv.setText(" 宿舍报修");
		contact = (EditText) findViewById(R.id.contact);
		dorm = (EditText) findViewById(R.id.contact);
		detail = (EditText) findViewById(R.id.detail);

	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.rightbutton:
			showMyDialog();
			repair = new Repair();
			repair.setContact(contact.getText().toString().trim());
			repair.setDetail(detail.getText().toString().trim());
			repair.setDorm(dorm.getText().toString().trim());
			sendData();
			break;

		default:
			break;
		}

	}

	private void sendData() {
		new AsyncTask<Repair, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Repair... params) {
//				RepairEngineImpl engine = new RepairEngineImpl();
				RepairEngine engine = BeanFactory.getInstance().getImpl(RepairEngine.class);
				boolean result = engine.addRepair(repair);
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
					provessData(result);
				
				super.onPostExecute(result);
			}
		}.execute();

		
	}

	protected void provessData(Boolean result) {
		dialog.dismiss();
		if(result){
			DialogUtils.showToast(ct, "已提交到维修部");
			finish();
		}else{
			DialogUtils.showToast(ct, "提交失败");
		}
		
	}

	private void showMyDialog() {
		dialog = DialogUtils.createProgressDialog(ct, "");
		dialog.show();
	}

}
