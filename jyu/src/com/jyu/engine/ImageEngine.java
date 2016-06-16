package com.jyu.engine;

import android.graphics.Bitmap;

public interface ImageEngine {

	/**
	 * 存储用户的头像
	 * @param bitmap 传进来图片对应的bitmap
	 * @return  返回图片存在服务器的地址
	 */
	public String savaUserHead(Bitmap bitmap);
	
}
