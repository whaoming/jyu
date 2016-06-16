package com.jyu.engine.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.bean.R_User;
import com.jyu.commonUtils.SharePrefUtil;
import com.jyu.domain.User;
import com.jyu.engine.UserLoginEngine;
import com.jyu.net.HttpClientUtil;

public class UserEngineImpl implements UserLoginEngine {

	@Override
	public R_User getUserLoginInfoByList(boolean isCache, String username,
			String password, Context ct) {
		// 联网获取数据
		// 1.设置参数
		if (isCache) {
			return null;
		} else {

			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("username", username);
			jsonMap.put("password", password);
			// 界面收集
			// 数据库获取+配置文件
			// 2.访问网络
			HttpClientUtil util = new HttpClientUtil();
			String json = util.sendPost(
					ConstantValue.LOTTERY_URI.concat(ConstantValue.LOGIN),
					jsonMap);
			// 3数据处理
			// 获取处理结果(状态+数据)
			// 将服务器传回来的数据进行解析--转换成类是Map的格式
			try {
				// JSONObject object = new JSONObject(json);
				// if("login".equals(object.getString("response"))){
				// String products = object.getString("userinfo");
				// //// Log.i("wang", "products=="+products);
				// // Log.i("wang", products);
				// SharePrefUtil.saveString(ct, "userinfo", products);
				//
				// List<User> users = JSON.parseArray(products,
				// User.class);
				// GlobalParams.user = users.get(0);
				// flag = true;
				// }else{
				// flag = false;
				// }
				Log.i("wang", json);
				Gson gson = new Gson();
				R_User fromJson = gson.fromJson(json, R_User.class);
				return fromJson;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 4数据持久化
		}
		return null;
	}

	@Override
	public boolean updateUserInfo(String column, String value, Context ct) {
		boolean flag = false;
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("userid", GlobalParams.userInfo.id + "");
		jsonMap.put("column", column);
		jsonMap.put("value", value);
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(
				ConstantValue.LOTTERY_URI.concat(ConstantValue.UPDATEUSERINFO),
				jsonMap);
		try {
			JSONObject object = new JSONObject(json);
			String result = object.getString("response");
			if ("success".equals(result)) {
				flag = true;
				String results = JSON.toJSONString(GlobalParams.userInfo);
				SharePrefUtil.saveString(ct, "userinfo", "[" + results + "]");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return flag;
	}

	@Override
	public User registerUser(User user) {
		User u = null;
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", user.getUsername());
		jsonMap.put("name", user.getName());
		jsonMap.put("password", user.getPassword());
		jsonMap.put("upic", user.getPic());
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(
				ConstantValue.LOTTERY_URI.concat(ConstantValue.REGISTERUSER),
				jsonMap);
		try {
			JSONObject object = new JSONObject(json);
			String result = object.getString("response");
			String responselist = object.getString("userinfo");
			if ("success".equals(result)) {
				List<User> us = JSON.parseArray(responselist, User.class);
				u = us.get(0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return u;
	}

	@Override
	public void updateUserInfoToCache(User user, Context ct) {
//		GlobalParams.user = user;
		String result = JSON.toJSONString(user);
		SharePrefUtil.saveString(ct, "userinfo", "[" + result + "]");
	}

	// @Override
	// public boolean updateUserInfo(User user) {
	// boolean flag = false;
	// Map<String, String> jsonMap = new HashMap<String, String>();
	// // User user = new User();
	// jsonMap.put("user", user.getUsername());
	// // 界面收集
	// // 数据库获取+配置文件
	// // 2.访问网络
	// HttpClientUtil util = new HttpClientUtil();
	// String json =
	// util.sendPost(ConstantValue.LOTTERY_URI.concat(ConstantValue.LOGIN),
	// jsonMap);
	// // 3数据处理
	// // 获取处理结果(状态+数据)
	// // 将服务器传回来的数据进行解析--转换成类是Map的格式
	// try {
	// JSONObject object = new JSONObject(json);
	// if("login".equals(object.getString("response"))){
	// String products = object.getString("userinfo");
	// List<User> users = JSON.parseArray(products,
	// User.class);
	// GlobalParams.user = users.get(0);
	// flag = true;
	// }else{
	// flag = false;
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// // 4数据持久化
	// return flag;
	// }

}
