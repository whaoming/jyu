package com.jyu.domain;


public class SecondHand {

	private int id;
	private int userid;
	private String title;
	private String pic;
	private String data;
	private String description;
	private String price;
	private String label;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public SecondHand() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SecondHand(int userid, String title, String pic, String data,
			String description,String price) {
		super();
		this.userid = userid;
		this.title = title;
		this.pic = pic;
		this.data = data;
		this.description = description;
		this.price = price;
	}
	public SecondHand(int id, int userid, String title, String pic,
			String data, String description,String price) {
		super();
		this.id = id;
		this.userid = userid;
		this.title = title;
		this.pic = pic;
		this.data = data;
		this.description = description;
		this.price = price;
	}
	
	
	
}
