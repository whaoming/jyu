package com.jyu.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.domain.Repair;
import com.jyu.engine.RepairEngine;
import com.jyu.net.HttpClientUtil;


public class RepairEngineImpl implements RepairEngine {

	@Override
	public boolean addRepair(Repair repair) {
		boolean flag = false;
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("userid",GlobalParams.userInfo.id+"");
		jsonMap.put("dorm", repair.getDorm());
		jsonMap.put("detail", repair.getDetail());
		jsonMap.put("contact", repair.getContact());
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(ConstantValue.LOTTERY_URI.concat(ConstantValue.ADDREPAIR),
				jsonMap);
		try { 
			JSONObject object = new JSONObject(json);
			String result = object.getString("response");
			if("success".equals(result)){
				flag = true;
			}else{
				flag = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 4数据持久化
		return flag;
	}

}
