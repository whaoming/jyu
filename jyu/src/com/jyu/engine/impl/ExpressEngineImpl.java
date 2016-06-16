package com.jyu.engine.impl;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.jyu.domain.Express;
import com.jyu.domain.ExpressInfo;
import com.jyu.engine.ExpressEngine;
import com.jyu.net.HttpClientUtil;

public class ExpressEngineImpl implements ExpressEngine {

	@Override
	public ExpressInfo getExpressInfo(String name, String num) {
		String url = "http://api.ickd.cn/?id=102616&secret=16135ea51cb60246eff620f130a005bd&com="
				+ name + "&nu=" + num + "&type=json&encode=utf8&ord=desc";//asc
		ExpressInfo info = new ExpressInfo();
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendGet(url);
		try {
			JSONObject object = new JSONObject(json);
			/**
			 * 0表示查询失败，1正常，2派送中，3已签收，4退回,5其他问题
			 */
			String status = object.getString("status");
			/**
			 * 
			 */
			String message = object.getString("message");
			/**
			 * 错误代码，0无错误，1单号不存在，2验证码错误，
			 * 3链接查询服务器失败，4程序内部错误，5程序执行错误，
			 * 6快递单号格式错误，7快递公司错误，
			 * 10未知错误，20API错误，21API被禁用，22API查询量耗尽。
			 */
			String errCode = object.getString("errCode");
			String data = object.getString("data");
			String tel = object.getString("tel");
			info.setStatus(Integer.valueOf(status));
			info.setMessage(message);
			info.setErrcode(errCode);
			info.setTel(tel);
			List<Express> list = JSON.parseArray(data,
					Express.class);
			info.setData(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}

}
