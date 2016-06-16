package com.jyu.engine.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.commonUtils.Base64Coder;
import com.jyu.engine.ImageEngine;
import com.jyu.net.HttpClientUtil;

public class ImageEngineImpl implements ImageEngine {


	@Override
	public String savaUserHead(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
		byte[] b = stream.toByteArray();
		// 将图片流以字符串形式存储下来
		String file = new String(Base64Coder.encodeLines(b));
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("userid", GlobalParams.userInfo.id+"");
		jsonMap.put("file", file);
		// 2.访问网络
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(
				ConstantValue.LOTTERY_URI.concat(ConstantValue.SAVAUSERHEAD),
				jsonMap);
		String path = "";
		try {
			JSONObject object = new JSONObject(json);
			String result = object.getString("response");
			if("success".equals(result)){
				path = object.getString("path");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return path;
	}

}
