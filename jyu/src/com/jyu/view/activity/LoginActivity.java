package com.jyu.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.bean.R_User;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.engine.UserLoginEngine;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.util.DialogUtils;


public class LoginActivity extends BaseActivity {

	private RelativeLayout button;
	private EditText et_username;
	private EditText et_password;
	private String username;
	private String password;
	private TextView tv_register;
	
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_login2);
		initTitleBar();
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_register.setOnClickListener(this);
		button = (RelativeLayout) findViewById(R.id.login);
		et_username = (EditText) findViewById(R.id.username);
		et_password = (EditText) findViewById(R.id.password);
		button.setOnClickListener(this);

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			showProgressDialog("");
			username = et_username.getText().toString();
			password = et_password.getText().toString();
			login(username, password);
			break;
		case R.id.tv_register:
			Intent intent = new Intent(ct,RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		}

	}

	private void login(final String username,final String password) {
		new AsyncTask<String, Void, R_User>() {
			@Override
			protected R_User doInBackground(String... params) {
				UserLoginEngine engine = BeanFactory.getInstance().getImpl(UserLoginEngine.class);
				R_User result = engine.getUserLoginInfoByList(false,username,password,LoginActivity.this);
				return result;
			}
			@Override
			protected void onPostExecute(R_User result) {
				if(result != null && "login".equals(result.response)){
					if(result.success == 1){//登陆成功
						GlobalParams.userInfo = result.userInfo;
						finish();
					}else{//密码错误
						DialogUtils.showToast(ct, result.error);
					}
				}else{//连接不到服务器
					DialogUtils.showToast(ct, "连接不到服务器");
				}
				closeProgressDialog();
//				dodata(result);
				super.onPostExecute(result);
			}
		}.execute();

	}

//	@SuppressLint("ShowToast")
//	protected void dodata(Boolean result) {
//		if (result) {
//			finish();
//		} else {
//			DialogUtils.showToast(ct, "登陆失败");
//		}
//
//	}

}
