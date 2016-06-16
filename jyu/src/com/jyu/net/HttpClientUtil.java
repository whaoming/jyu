package com.jyu.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.jyu.ConstantValue;
import com.jyu.GlobalParams;

/**
 * 通用的网络链接工具
 * 
 * @author Administrator
 * 
 */
public class HttpClientUtil {
	private HttpClient client;

	private HttpGet get;
	private HttpPost post;

	private HttpResponse response;

	private static Header[] headers;
	static {
		headers = new BasicHeader[10];
		headers[0] = new BasicHeader("Appkey", "12343");
		headers[1] = new BasicHeader("Udid", "");// 手机串号
		headers[2] = new BasicHeader("Os", "android");//
		headers[3] = new BasicHeader("Osversion", "");//
		headers[4] = new BasicHeader("Appversion", "");// 1.0
		headers[5] = new BasicHeader("Sourceid", "");//
		headers[6] = new BasicHeader("Ver", "");

		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");

		headers[9] = new BasicHeader("Unique", "");
	}

	public HttpClientUtil() {
		client = new DefaultHttpClient();
		// 设置代理信息
		if (StringUtils.isNotBlank(GlobalParams.PROXY_IP)) {
			HttpHost host = new HttpHost(GlobalParams.PROXY_IP,
					GlobalParams.PROXY_PORT);

			client.getParams()
					.setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
		}

	}

	/**
	 * 发送请求
	 * 
	 * @param uri
	 * @param xml
	 */
	// public InputStream sendPost(String uri, String xml) {
	// post = new HttpPost(uri);
	//
	// // 超时
	//
	// try {
	// HttpEntity entity = new StringEntity(xml, ConstantValue.ENCODING);
	// post.setEntity(entity);
	//
	// response = client.execute(post);
	// if (response.getStatusLine().getStatusCode() == 200) {
	// return response.getEntity().getContent();
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	/*************************** 电商 **********************************/
	// post或get请求带参数
	// Get:url?params=xxx&
	// post：带参数（HttpEntity：参数+编码）

	// 细节：设置头，设置超时限制
	/**
	 * 发送Post请求
	 * 
	 * @param uri
	 * @param params
	 *            ：参数
	 * @return
	 */
	public String sendPost(String uri, Map<String, String> params) {
		post = new HttpPost(uri);

		post.setHeaders(headers);

		// 处理超时
		HttpParams httpParams = new BasicHttpParams();//
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		post.setParams(httpParams);

		// 设置参数
		if (params != null && params.size() > 0) {
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> item : params.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(item.getKey(),
						item.getValue());
				parameters.add(pair);
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						parameters, ConstantValue.ENCODING);
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		try {
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(),
						ConstantValue.ENCODING);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 发送get请求
	 */
	public String sendGet(String uri) {
		// uri = "http://192.168.1.102:8080/redbaby/home";
		// uri = "http://192.168.1.102:8080/redbaby/search/recommend";
		get = new HttpGet(uri);
		get.setHeaders(headers);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(),
						ConstantValue.ENCODING);
			} else {
//				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public InputStream loadImg(String uri) {
		get = new HttpGet(uri);

		HttpParams httpParams = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		get.setParams(httpParams);

		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				return response.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
