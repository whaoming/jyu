package com.jyu.engine.impl;



import android.content.Context;

import com.google.gson.Gson;
import com.jyu.ConstantValue;
import com.jyu.bean.R_News;
import com.jyu.commonUtils.SharePrefUtil;
import com.jyu.engine.NewsGetEngine;
import com.jyu.net.HttpClientUtil;

public class NewsGetEngineImpl implements NewsGetEngine {
	
	@Override
	public R_News getNewsList(int page, boolean isCache,Context ct) {
		String url = ConstantValue.LOTTERY_URI.concat(ConstantValue.NEWSGET)
				+ "&page=" + page;
		if (isCache) {
			String result = SharePrefUtil.getString(ct, url, "");
			if(result!=""){
//				List<News> limitList = JSON
//						.parseArray(result, News.class);
				Gson gson = new Gson();
				R_News r_news = gson.fromJson(result, R_News.class);
				return r_news;
			}else{
				return null;
			}
			
		} else {
			// 2.访问网络
			HttpClientUtil util = new HttpClientUtil();
			String json = util.sendGet(url);
			try {
//				JSONObject object = new JSONObject(json);
//				String responselist = object.getString("newslist");
//				if(page == 0){
//					SharePrefUtil.saveString(ct, url, responselist);
//				}
//				List<News> limitList = JSON
//						.parseArray(responselist, News.class);
//				return limitList;
				if(page == 0){
					SharePrefUtil.saveString(ct, url, json);
				}
				Gson gson = new Gson();
				R_News r_news = gson.fromJson(json, R_News.class);
				return r_news;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 4数据持久化
		return null;
	}

}
