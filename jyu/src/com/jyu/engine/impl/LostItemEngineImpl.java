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
import com.jyu.bean.R_Lost;
import com.jyu.bean.R_Lost_Comment;
import com.jyu.bean.R_Lost_Comment.Lost_CommentItem;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.commonUtils.SharePrefUtil;
import com.jyu.domain.LostItemInfo;
import com.jyu.engine.LostItemEngine;
import com.jyu.net.HttpClientUtil;

public class LostItemEngineImpl implements LostItemEngine {

	@Override
	public R_Lost_Comment deleteLostItemComment(int domainid, int commentId) {
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.DELETELOSTITEMCOMMENT)
				+ "&domainid="
				+ domainid + "&commentId=" + commentId;
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(url);
		try {
			Log.i("wang", "json:" + json);
			Gson gson = new Gson();
			R_Lost_Comment fromJson = gson.fromJson(json, R_Lost_Comment.class);
			return fromJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public R_Lost_Comment insertLostItemComment(Lost_CommentItem item) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("domainid", item.domainid + "");
		// jsonMap.put("userid", GlobalParams.userInfo.id+"");
		jsonMap.put("userid", item.userInfo.id + "");
		Log.i("wang", "item.userInfo.id="+item.userInfo.id);
		jsonMap.put("date", CommonUtil.getStringDate());
		jsonMap.put("content", item.content);
		jsonMap.put("del", 0 + "");
		jsonMap.put("floor", item.floor + "");
		if (item.reply == 1) {
			jsonMap.put("reply", 1 + "");
			jsonMap.put("receiverid", item.replyComment.userInfo.id + "");

			jsonMap.put("replyid", item.replyComment.id + "");
		} else {
			jsonMap.put("reply", 0 + "");
			jsonMap.put("receiverid", 0 + "");
			jsonMap.put("replyid", 0 + "");
		}

		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(ConstantValue.LOTTERY_URI
				.concat(ConstantValue.INSERTLOSTITEMCOMMENT), jsonMap);
		try {
			Gson gson = new Gson();
			R_Lost_Comment result = gson.fromJson(json, R_Lost_Comment.class);
			// JSONObject object = new JSONObject(json);
			// String result = object.getString("response");
			// String responselist = object.getString("userinfo");
			// if ("success".equals(result)) {
			// List<User> us = JSON.parseArray(responselist, User.class);
			// u = us.get(0);
			// }
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public R_Lost_Comment getLostItemCommentList(int page, int domainid) {
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.GETLOSTITEMCOMMENTLIST)
				+ "&page="
				+ page
				+ "&domainid=" + domainid;
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(url);
		try {
			Log.i("wang", "getLostItemCommentList" + json);
			Gson gson = new Gson();
			R_Lost_Comment fromJson = gson.fromJson(json, R_Lost_Comment.class);
			return fromJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public R_Lost getMyItemList(int page, boolean isCache, Context ct) {
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.GETMYITEMLIST)
				+ "&page="
				+ page
				+ "&userid=" + GlobalParams.userInfo.id;
		Gson gson = new Gson();
		if (isCache) {
			String result = SharePrefUtil.getString(ct, url, "");
			if (result != "") {
				// List<LostItemInfo> limitList = JSON.parseArray(result,
				// LostItemInfo.class);
				// return limitList;
				R_Lost r_lost = gson.fromJson(result, R_Lost.class);
				return r_lost;
			} else {
				return null;
			}
		} else {
			// 2.访问网络
			HttpClientUtil util = new HttpClientUtil();
			String json = util.sendGet(url);
			try {
				// JSONObject object = new JSONObject(json);
				//
				// // String text = ...; // [{ ... }, { ... }]
				// // List<User> users = JSON.parseArray(text, User.class);
				// // 类型集合的反序列化
				// // 开始解析数据--根据bean
				//
				// String responselist = object.getString("myitemlist");
				// if (page == 0) {
				// SharePrefUtil.saveString(ct, url, responselist);
				// }
				// List<LostItemInfo> limitList = JSON.parseArray(responselist,
				// LostItemInfo.class);
				// return limitList;
				if (page == 0) {
					SharePrefUtil.saveString(ct, url, json);
				}
				R_Lost r_Lost = gson.fromJson(json, R_Lost.class);
				return r_Lost;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 4数据持久化
			return null;
		}
	}

	@Override
	public R_Lost getItemList(int page, boolean isCache, Context ct) {
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.GETLOSTFOUNDITEMLIST) + "&page=" + page;
		if (isCache) {
			String result = SharePrefUtil.getString(ct, url, "");
			if (result != "") {
				// List<LostItemInfo> limitList = JSON.parseArray(result,
				// LostItemInfo.class);
				// return limitList;
				Gson gson = new Gson();
				R_Lost r_lost = gson.fromJson(result, R_Lost.class);
				return r_lost;
			} else {
				return null;
			}
		} else {
			// 2.访问网络
			HttpClientUtil util = new HttpClientUtil();
			String json = util.sendGet(url);
			try {
				// JSONObject object = new JSONObject(json);
				//
				// // String text = ...; // [{ ... }, { ... }]
				// // List<User> users = JSON.parseArray(text, User.class);
				// // 类型集合的反序列化
				// // 开始解析数据--根据bean
				//
				// String responselist = object.getString("itemlist");
				// if (size == 0) {
				// SharePrefUtil.saveString(ct, url, responselist);
				// }
				// List<LostItemInfo> limitList = JSON.parseArray(responselist,
				// LostItemInfo.class);
				// return limitList;
				if (page == 0) {
					SharePrefUtil.saveString(ct, url, json);
				}
				Gson gson = new Gson();
				R_Lost r_lost = gson.fromJson(json, R_Lost.class);
				return r_lost;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 4数据持久化
			return null;
		}

	}

	@Override
	public List<LostItemInfo> getLostList(int size, boolean isCache, Context ct) {
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.GETLOSTITEMLIST) + "&page=" + size;
		if (isCache) {
			String result = SharePrefUtil.getString(ct, url, "");
			if (result != "") {
				List<LostItemInfo> limitList = JSON.parseArray(result,
						LostItemInfo.class);
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

				// String text = ...; // [{ ... }, { ... }]
				// List<User> users = JSON.parseArray(text, User.class);
				// 类型集合的反序列化
				// 开始解析数据--根据bean

				String responselist = object.getString("lostitemlist");
				if (size == 0) {
					SharePrefUtil.saveString(ct, url, responselist);
				}
				List<LostItemInfo> limitList = JSON.parseArray(responselist,
						LostItemInfo.class);
				return limitList;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 4数据持久化
			return null;
		}

	}

	/**
	 * 添加一则寻物启事
	 */
	@Override
	public boolean addLost(LostItemInfo item) {
		boolean flag = false;
		String url = ConstantValue.LOTTERY_URI.concat(ConstantValue.INSERTLOST);
		String pic = CommonUtil.processPic(item.getPic());
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("title", item.getTitle());
		jsonMap.put("contact", item.getContact());
		jsonMap.put("description", item.getDescription());
		jsonMap.put("ptime", item.getPtime());
		jsonMap.put("ltime", item.getLtime());
		jsonMap.put("description", item.getDescription());
		jsonMap.put("type", item.getType());
		jsonMap.put("llabel", item.getLlabel());
		jsonMap.put("ilabel", item.getIlabel());
		jsonMap.put("lost", item.getLost() + "");
		jsonMap.put("location", item.getLocation());
		jsonMap.put("userid", GlobalParams.userInfo.id + "");
		jsonMap.put("pic", pic);
		// 2.访问网络
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
	 * 获取失物招领列表的item list
	 * 
	 * @param page
	 * @param isCache
	 * @return
	 */
	@Override
	public List<LostItemInfo> getFoundItemList(int page, boolean isCache,
			Context ct) {
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.GETFOUNDITEMLIST) + "&page=" + page;
		if (isCache) {
			String result = SharePrefUtil.getString(ct, url, "");
			if (result != "") {
				List<LostItemInfo> limitList = JSON.parseArray(result,
						LostItemInfo.class);
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

				// String text = ...; // [{ ... }, { ... }]
				// List<User> users = JSON.parseArray(text, User.class);
				// 类型集合的反序列化
				// 开始解析数据--根据bean

				String responselist = object.getString("founditemlist");
				if (page == 0) {
					SharePrefUtil.saveString(ct, url, responselist);
				}
				List<LostItemInfo> limitList = JSON.parseArray(responselist,
						LostItemInfo.class);
				return limitList;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 4数据持久化
			return null;
		}
	}

	@Override
	public boolean updateItem(LostItemInfo info) {
		boolean flag = false;
		String url = ConstantValue.LOTTERY_URI
				.concat(ConstantValue.UPDATEMYITEM);
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("title", info.getTitle());
		jsonMap.put("ltime", info.getLtime());
		jsonMap.put("description", info.getDescription());
		jsonMap.put("type", info.getType());
		jsonMap.put("llabel", info.getLlabel());
		jsonMap.put("ilabel", info.getIlabel());
		jsonMap.put("location", info.getLocation());
		jsonMap.put("contact", info.getContact());
		jsonMap.put("id", info.getId() + "");
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

}
