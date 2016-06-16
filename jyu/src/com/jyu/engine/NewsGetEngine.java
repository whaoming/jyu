package com.jyu.engine;


import android.content.Context;

import com.jyu.bean.R_News;


public interface NewsGetEngine {

	R_News getNewsList(int page,boolean isCache,Context ct);
}
