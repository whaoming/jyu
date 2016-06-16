package com.jyu.engine.impl;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.jyu.ConstantValue;
import com.jyu.commonUtils.SharePrefUtil;
import com.jyu.domain.TopLine;
import com.jyu.engine.TopLineEngine;
import com.jyu.net.HttpClientUtil;

import android.content.Context;
import android.util.Log;

public class TopLineEngineImpl implements TopLineEngine{

	private String url;


	@Override
	public List<TopLine> getTopLineList(boolean isCache,Context ct) {
		url = ConstantValue.LOTTERY_URI.concat(ConstantValue.GETTOPLINELIST);
		if (isCache) {
			String result = SharePrefUtil.getString(ct, "topline", "");
			if (result != null) {
				List<TopLine> limitList = JSON
						.parseArray(result, TopLine.class);
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
				String responselist = object.getString("toplinelist");
//				Log.i("wang", "responselist="+responselist);
				if(responselist!=null){
					SharePrefUtil.saveString(ct, "topline", responselist);
					List<TopLine> limitList = JSON.parseArray(responselist,
							TopLine.class);
					return limitList;
				}else{
					return null;
				}
			} catch (JSONException e) {
				Log.i("wang", "出错啦");
				e.printStackTrace();
			}
			return null;
		}
	}
	
	@Override
	public List<TopLine> getTopLineListFromCache(Context ct){
		String result = SharePrefUtil.getString(ct, "topline", "");
		if(result!=""){
			List<TopLine> limitList = JSON.parseArray(result,
					TopLine.class);
			return limitList;
		}else{
			return null;
		}
		
		
	}
}
