package com.jyu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.jyu.commonUtils.SharePrefUtil;
import com.jyu.engine.impl.ServerEngineImpl;
import com.jyu.view.activity.FrameActivity;
import com.jyu.view.activity.StartActivity;
import com.jyu.view.util.DialogUtils;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_start01);
		LinearLayout mLinear = (LinearLayout) findViewById(R.id.Fragment01Linear);
		mLinear.setBackgroundResource(R.drawable.ic_splash_screen);
		initData();
		// getTopLine();
		copyDB();
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = hand.obtainMessage();
				hand.sendMessage(msg);
			}

		}.start();
	}

	private void initData() {
		// 获取缓存中的hasdot
		ConstantValue.LOTTERY_URI = SharePrefUtil.getString(MainActivity.this,
				"LOTTERY_URI", "") == "" ? ConstantValue.LOTTERY_URI
				: SharePrefUtil.getString(MainActivity.this, "LOTTERY_URI", "");
		GlobalParams.socials = new ArrayList<Integer>();
		/**
		 * 获取缓存中的userinfo
		 */
//		String userinfo = SharePrefUtil.getString(MainActivity.this,
//				"userinfo", null);
//		if (userinfo != "" && userinfo != null) {
//			GlobalParams.user = JSON.parseArray(userinfo, User.class).get(0);
//		}

		/**
		 * 测试是否能连接上服务器
		 */
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				ServerEngineImpl impl = new ServerEngineImpl();
				boolean result = impl.testIsConnect();
				return result;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				GlobalParams.isConnect = result;
				if (!result) {
					DialogUtils.showToast(MainActivity.this, "不能连接到服务器");
				}

			}
		}.execute();

	}

	// private void getTopLine() {

	//
	// }

	@SuppressLint("HandlerLeak")
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (isFristRun()) {
				Intent intent = new Intent(MainActivity.this,
						StartActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(MainActivity.this,
						FrameActivity.class);
				startActivity(intent);
			}
			finish();
		};
	};

	private boolean isFristRun() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (!isFirstRun) {
			return false;
		} else {
			editor.putBoolean("isFirstRun", false);
			editor.commit();
			return true;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return true;
	}

	private void copyDB() {
		try {
			File file = new File(getFilesDir(), "phone.db");
			if (file.exists() && file.length() > 0) {
				Log.i("wang", "db存在了");
			} else {
				// 数据库文件只需要拷贝一下，如果拷贝了，不需要重新拷贝了。
				AssetManager am = getAssets();
				InputStream is = am.open("phone.db");
				// 创建一个文件/data/data/包名/files/address.db
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
