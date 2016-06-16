package com.jyu.engine.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.bean.R_Social;
import com.jyu.commonUtils.SharePrefUtil;
import com.jyu.domain.Social;
import com.jyu.domain.UserCommentInfo;
import com.jyu.engine.SocialEngine;
import com.jyu.net.HttpClientUtil;

public class SocialEngineImpl implements SocialEngine {

	public SocialEngineImpl() {
		super();
	}

	/**
	 * 获取当前活动列表
	 */
	@Override
	public R_Social getSocialList(int type, int page, boolean isCache,Context ct) {
		String url;
		if (GlobalParams.userInfo != null) {
			url = ConstantValue.LOTTERY_URI.concat(ConstantValue.GETSOCIALLIST)
					+ "&type=" + type + "&page=" + page + "&userid="
					+ GlobalParams.userInfo.id;
		} else {
			url = ConstantValue.LOTTERY_URI.concat(ConstantValue.GETSOCIALLIST)
					+ "&type=" + type + "&page=" + page;
		}

		// 这个userid有问题，因为不知道用户是否有登陆，如果登陆就有，没登陆就会空指针异常
		if (isCache) {
//			String result = SharePrefUtil
//					.getString(ct, "sociallist" + type, "");
			String result = SharePrefUtil.getString(ct, url, "");
			if (result != "") {
//				List<Social> cachelist = JSON.parseArray(result, Social.class);
//				if (usersocial != "") {
//					GlobalParams.socials = JSON.parseArray(usersocial, Integer.class);
//				} 
//				return cachelist;
				Gson gson = new Gson();
				R_Social r_social = gson.fromJson(result, R_Social.class);
				return r_social;
			} else {
				return null;
			}
		} else {
			HttpClientUtil util = new HttpClientUtil();
			String json = util.sendGet(url);
			try {
//				JSONObject object = new JSONObject(json);
//				String responselist = object.getString("sociallist");
//				/**
//				 * 只有page=0才有获取参加的东西，因为page不等于0就是下啦刷新，下啦刷新只要
//				 * activity不销毁，他的那个activity那个成员变量usersocial是不会销毁的,
//				 * 然后只要销毁的时候根据这个值有没有变化就可以判断要不要发送给服务器东西了
//				 */
//				if (page == 0) {
//					if (GlobalParams.user != null) {
//						String usersocial = object.getString("usersocial");
//						if (usersocial != null) {
//							SharePrefUtil.saveString(ct, "usersocial", usersocial);
//							GlobalParams.socials = JSON.parseArray(usersocial,
//									Integer.class);
//						}
//					} else {// 进来这里的 ，就是又没缓存，又没有登陆的
//						SharePrefUtil.saveString(ct, "usersocial", "");
//					}
//
//					SharePrefUtil.saveString(ct, "sociallist" + type,
//							responselist);
//				}
//				List<Social> socials = JSON.parseArray(responselist,
//						Social.class);
//				return socials;
				SharePrefUtil.saveString(ct, url, json);
				Gson gson = new Gson();
				R_Social r_social = gson.fromJson(json, R_Social.class);
				return r_social;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 4数据持久化
			return null;
		}

	}

	/**
	 * 参加一个活动
	 * 
	 * @param socialid
	 * @return 返回是否参加成功
	 */
	@Override
	public boolean joinSocial(int socialid) {
		boolean flag = false;
		String url = ConstantValue.LOTTERY_URI.concat(ConstantValue.JOINSOCIAL)
				+ "&socialid=" + socialid + "&userid="+GlobalParams.userInfo.id;
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(url);
		try {
			JSONObject object = new JSONObject(json);
			String result = object.getString("response");
			if ("success".equals(result)) {
				flag = true;
				GlobalParams.socials.add(socialid);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 发布一个活动
	 * 
	 * @return
	 */
	@Override
	public boolean publishSocial(Social social) {
		boolean flag = false;
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("userid", GlobalParams.userInfo.id + "");
		jsonMap.put("title", social.getTitle());
		jsonMap.put("time", social.getTime());
//		jsonMap.put("starttime", social.getStarttime());
//		jsonMap.put("endtime", social.getEndtime());
		jsonMap.put("location", social.getLocation());
		jsonMap.put("contact", social.getContact());
		jsonMap.put("description", social.getDescription());
		jsonMap.put("stype", social.getType() + "");
		jsonMap.put("issignup", social.getIssignup() + "");
		jsonMap.put("ischecked", 1 + "");
//		jsonMap.put("slike", 0 + "");
		jsonMap.put("need", social.getNeed());
		jsonMap.put("signup", 1 + "");
		jsonMap.put("pic", social.getPic());
		jsonMap.put("name", GlobalParams.userInfo.name);
//		jsonMap.put("teamname", social.getTeamname());
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(
				ConstantValue.LOTTERY_URI.concat(ConstantValue.PUBLISHSOCIAL),
				jsonMap);
		try {
			JSONObject object = new JSONObject(json);
			String result = object.getString("response");
			if ("success".equals(result)) {
				flag = true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return flag;
	}

	/**
	 * 获取一个用户参加过的活动的实体的列表
	 */
	@Override
	public List<Social> getUserSocialList(int page) {
		String url;
		url = ConstantValue.LOTTERY_URI.concat(ConstantValue.GETUSERSOCIAL)
				+ "&page=" + page + "&userid=" + GlobalParams.userInfo.id;
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(url);
		try {
			JSONObject object = new JSONObject(json);
			String responselist = object.getString("mysociallist");
			List<Social> socials = JSON.parseArray(responselist, Social.class);
			return socials;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return null;
	}

	/**
	 * 获取一个用户组织过的活动
	 * @param page
	 * @return
	 */
	@Override
	public List<Social> getMyOriginateSocialList(int page) {
		String url = ConstantValue.LOTTERY_URI.concat(ConstantValue.GETMYORIGINATESOCIALLIST)
				+ "&page=" + page + "&userid=" + GlobalParams.userInfo.id;
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(url);
		try {
			JSONObject object = new JSONObject(json);
			String responselist = object.getString("myoriginatelist");
			List<Social> socials = JSON.parseArray(responselist, Social.class);
			return socials;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return null;
	}
	
	/**
	 * 获取一个活动的所有报名者的信息的实体的集合
	 * @param page
	 * @param socialid
	 * @return
	 */
	@Override
	public List<UserCommentInfo> getSocialUserList(int page,int socialid){
		String url = ConstantValue.LOTTERY_URI.concat(ConstantValue.GETSOCIALUSERLIST)
				+ "&page=" + page + "&socialid=" + socialid;
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(url);
		try {
			JSONObject object = new JSONObject(json);
			String responselist = object.getString("socialuserlist");
			List<UserCommentInfo> infos = JSON.parseArray(responselist, UserCommentInfo.class);
			return infos;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return null;
	}
	@Override
	public boolean updateSocial(Social social){
		boolean flag = false;
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		jsonMap.put("socialid",social.getId()+"");
		jsonMap.put("userid",social.getUserid()+"");
		jsonMap.put("title", social.getTitle());
		jsonMap.put("time", social.getTime());
//		jsonMap.put("starttime", social.getStarttime());
//		jsonMap.put("endtime", social.getEndtime());
		jsonMap.put("location", social.getLocation());
		jsonMap.put("contact", social.getContact());
		jsonMap.put("description", social.getDescription());
		jsonMap.put("stype", social.getType() + "");
		jsonMap.put("issignup", social.getIssignup() + "");
		jsonMap.put("ischecked", 1 + "");
//		jsonMap.put("slike", 0 + "");
		jsonMap.put("need", social.getNeed());
		jsonMap.put("signup", 1 + "");
		jsonMap.put("pic", social.getPic());
		jsonMap.put("name", GlobalParams.userInfo.name);
//		jsonMap.put("teamname", social.getTeamname());
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(
				ConstantValue.LOTTERY_URI.concat(ConstantValue.UPDATESOCIAL),
				jsonMap);
		try {
			JSONObject object = new JSONObject(json);
			String result = object.getString("response");
			if ("success".equals(result)) {
				flag = true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flag;
	}

}

// }
