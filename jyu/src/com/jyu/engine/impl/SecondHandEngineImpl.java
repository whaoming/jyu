package com.jyu.engine.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.commonUtils.SharePrefUtil;
import com.jyu.domain.SecondHandInfo;
import com.jyu.domain.UserCommentInfo;
import com.jyu.engine.SecondHandEngine;
import com.jyu.net.HttpClientUtil;

public class SecondHandEngineImpl implements SecondHandEngine {

//	private  Context ct;
//
//	public SecondHandEngineImpl(Context ct) {
//		super();
//		this.ct = ct;
//		
//		
//	}
	
//	public SecondHandEngineImpl(){
//	}

	@Override
	public List<SecondHandInfo> getSecondHandList(int page, boolean isCache,Context ct) {
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.GETSECONDHANDLIST) + "&page=" + page;
		if (isCache) {
			String result = SharePrefUtil.getString(ct, url, "");
			if (result != "") {
				List<SecondHandInfo> limitList = JSON.parseArray(result,
						SecondHandInfo.class);
				return limitList;
			} else {
				return null;
			}
		} else {
			// 2.访问网络
			HttpClientUtil util = new HttpClientUtil();
			String json = util.sendGet(url);
			try {
				JSONObject object = new JSONObject(json);

				String responselist = object.getString("secondlist");
				if (page == 0) {
					SharePrefUtil.saveString(ct, url, responselist);
				}
				List<SecondHandInfo> limitList = JSON.parseArray(responselist,
						SecondHandInfo.class);
				return limitList;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 4数据持久化
			return null;
		}
	}

	@Override
	public boolean addSecondHand(SecondHandInfo info) {
		boolean flag = false;
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.INSERTSECONDHAND);
		String pic = CommonUtil.processPic(info.getPic());
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("title", info.getTitle());
		jsonMap.put("description", info.getDescription());
		jsonMap.put("data", info.getData());
		jsonMap.put("userid", info.getUserid() + "");
		jsonMap.put("pic", pic);
		jsonMap.put("price", info.getPrice());
		jsonMap.put("label", info.getLabel());
	
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(url, jsonMap);
		try {
			JSONObject object = new JSONObject(json);
			String response = object.getString("response");
			if ("success".equals(response)) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return flag;
	}

	/**
	 * 获得一个用户的所有二手货的实体
	 */
	@Override
	public List<SecondHandInfo> getSecondHandPublisher(int page,
			boolean isCache,Context ct) {
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.GETSECONDHANDPUBLISHER) + "&page=" + page + "&userid=" + GlobalParams.userInfo.id;
		if (isCache) {
			String result = SharePrefUtil.getString(ct, url, "");
			if (result != "") {
				List<SecondHandInfo> limitList = JSON.parseArray(result,
						SecondHandInfo.class);
				return limitList;
			} else {
				return null;
			}
		} else {
			// 2.访问网络
			HttpClientUtil util = new HttpClientUtil();
			String json = util.sendGet(url);
			try {
				JSONObject object = new JSONObject(json);

				String responselist = object.getString("secondhandlist");
				if (page == 0) {
					SharePrefUtil.saveString(ct, url, responselist);
				}
				List<SecondHandInfo> limitList = JSON.parseArray(responselist,
						SecondHandInfo.class);
				return limitList;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 4数据持久化
			return null;
		}
	}
	
	/**
	 * 获取一个二手货，用户留下的所有留言
	 * @param page
	 * @param shid
	 * @param isCache
	 * @return
	 */
	@Override
	public List<UserCommentInfo> getSecondHandPlayer(int page,int shid,boolean isCache){
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.GETSECONDHANDPLAYER) + "&page=" + page + "&shid=" + shid;
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(url);
		try {
			JSONObject object = new JSONObject(json);
			String responselist = object.getString("playerlist");
//			Log.i("wang", responselist);
//			if (page == 0) {
//				SharePrefUtil.saveString(ct, url, responselist);
//			}
			List<UserCommentInfo> limitList = JSON.parseArray(responselist,
					UserCommentInfo.class);
			return limitList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return null;
	}
	
	/**
	 * 给一个二手货发送评论
	 * @param msg
	 * @return
	 */
	@Override
	public boolean sendReply(int shid){
		boolean flag = false;
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.SENDSECONDHANDREPLY);
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("shid", shid+"");
		jsonMap.put("userid", GlobalParams.userInfo.id+"");
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(url, jsonMap);
		try {
			JSONObject object = new JSONObject(json);
			String response = object.getString("response");
			if ("success".equals(response)) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flag;
	}

}
