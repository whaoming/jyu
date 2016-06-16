package com.jyu.bean;

import java.util.List;

public class R_News extends R_Base{

	public List<NormalNewsItem> normalNewsList;
	public List<TopicNewsItem> topicNewsList;
	
	public static class NormalNewsItem{
		public int id;
		public String data;
		public String picurl;
		public String title;
		public String url;
	}
	
	public static class TopicNewsItem{
		public int id;
		public String data;
		public String picurl;
		public String title;
		public String url;
	}
}
