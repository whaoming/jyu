package com.jyu.domain;


public class Repair {

	private int id;
	private int userid;
	private String dorm;
	private String detail;
	private String contact;
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
	public String getDorm() {
		return dorm;
	}
	public void setDorm(String dorm) {
		this.dorm = dorm;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public Repair(int userid, String dorm, String detail, String contact) {
		super();
		this.userid = userid;
		this.dorm = dorm;
		this.detail = detail;
		this.contact = contact;
	}
	public Repair() {
		super();
	}
	
	
	
}
