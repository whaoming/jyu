package com.jyu.domain;

import java.io.Serializable;


public class TopLine implements
Serializable{

	/**
	 * 
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	public TopLine() {
		super();
	}
	private int id;
	private String title;
	private String pic;
	private String url;
	private int type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public TopLine(int id, String title, String pic, String url, int type) {
		super();
		this.id = id;
		this.title = title;
		this.pic = pic;
		this.url = url;
		this.type = type;
	}
	
	
}
