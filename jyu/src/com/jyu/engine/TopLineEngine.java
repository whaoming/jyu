package com.jyu.engine;

import java.util.List;

import android.content.Context;

import com.jyu.domain.TopLine;

public interface TopLineEngine {

	List<TopLine> getTopLineList(boolean isCache, Context ct);

	List<TopLine> getTopLineListFromCache(Context ct);

}
