package com.jyu.engine.impl;

import org.json.JSONException;
import org.json.JSONObject;

import com.jyu.ConstantValue;
import com.jyu.engine.ServerEngine;
import com.jyu.net.HttpClientUtil;

public class ServerEngineImpl implements ServerEngine{

	@Override
	public boolean testIsConnect(){
		boolean result = false;
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(ConstantValue.LOTTERY_URI.concat(ConstantValue.TESTCONNECT));
		try { 
			JSONObject object = new JSONObject(json);
			String response = object.getString("response");
			if("success".equals(response)){
				result = true;
			}else{
				result = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return result;
	}
}
