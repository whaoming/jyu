package com.jyu.domain;

import java.io.Serializable;


public class Social implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int userid;
	private String title;
	private String time;
	private String location;
	private String contact;
	private String description;
	private int type;
	private int issignup;
	private int ischecked;
	private String need;
	private int signup;
	private String pic;
	private String name;
	
	public Social() {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIssignup() {
		return issignup;
	}
	public void setIssignup(int issignup) {
		this.issignup = issignup;
	}
	public int getIschecked() {
		return ischecked;
	}
	public void setIschecked(int ischecked) {
		this.ischecked = ischecked;
	}
	public String getNeed() {
		return need;
	}
	public void setNeed(String need) {
		this.need = need;
	}
	public int getSignup() {
		return signup;
	}
	public void setSignup(int signup) {
		this.signup = signup;
	}
	public Social(int id, int userid, String title, String time,
			String location, String contact, String description, int type,
			int issignup, int ischecked, String need, int signup, String pic,
			String name) {
		super();
		this.id = id;
		this.userid = userid;
		this.title = title;
		this.time = time;
		this.location = location;
		this.contact = contact;
		this.description = description;
		this.type = type;
		this.issignup = issignup;
		this.ischecked = ischecked;
		this.need = need;
		this.signup = signup;
		this.pic = pic;
		this.name = name;
	}
	
	
}
