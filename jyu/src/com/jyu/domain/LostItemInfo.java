package com.jyu.domain;

import java.io.Serializable;


public class LostItemInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String llabel;
	private String ilabel;
	private String type;
	private String description;
	private String pic;
	private String location;
	private String ltime;
	private String ptime;
	private String contact;
	private int found;
	private int userid;
	private int reply;
	private int lost;
	public int getLost() {
		return lost;
	}
	public void setLost(int lost) {
		this.lost = lost;
	}
	private String uname;
	private String udescription;
	private String upic;
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUdescription() {
		return udescription;
	}
	public void setUdescription(String udescription) {
		this.udescription = udescription;
	}
	public String getUpic() {
		return upic;
	}
	public void setUpic(String upic) {
		this.upic = upic;
	}
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
	public String getLlabel() {
		return llabel;
	}
	public void setLlabel(String llabel) {
		this.llabel = llabel;
	}
	public String getIlabel() {
		return ilabel;
	}
	public void setIlabel(String ilabel) {
		this.ilabel = ilabel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLtime() {
		return ltime;
	}
	public void setLtime(String ltime) {
		this.ltime = ltime;
	}
	public String getPtime() {
		return ptime;
	}
	public void setPtime(String ptime) {
		this.ptime = ptime;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public int getFound() {
		return found;
	}
	public void setFound(int found) {
		this.found = found;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getReply() {
		return reply;
	}
	public void setReply(int reply) {
		this.reply = reply;
	}
	
	public LostItemInfo() {
		super();
	}
//	public LostItemInfo(String title, String llabel, String ilabel, String type,
//			String description, String pic, String location, String ltime,
//			String ptime, String contact, int found, int userid, int reply) {
//		super();
//		this.title = title;
//		this.llabel = llabel;
//		this.ilabel = ilabel;
//		this.type = type;
//		this.description = description;
//		this.pic = pic;
//		this.location = location;
//		this.ltime = ltime;
//		this.ptime = ptime;
//		this.contact = contact;
//		this.found = found;
//		this.userid = userid;
//		this.reply = reply;
//	}
//	public LostItemInfo(int id, String title, String llabel, String ilabel,
//			String type, String description, String pic, String location,
//			String ltime, String ptime, String contact, int found, int userid,
//			int reply) {
//		super();
//		this.id = id;
//		this.title = title;
//		this.llabel = llabel;
//		this.ilabel = ilabel;
//		this.type = type;
//		this.description = description;
//		this.pic = pic;
//		this.location = location;
//		this.ltime = ltime;
//		this.ptime = ptime;
//		this.contact = contact;
//		this.found = found;
//		this.userid = userid;
//		this.reply = reply;
//	}
//	public LostItemInfo(int id, String title, String llabel, String ilabel,
//			String type, String description, String pic, String location,
//			String ltime, String ptime, String contact, int found, int userid,
//			int reply, String uname, String udescription, String upic) {
//		super();
//		this.id = id;
//		this.title = title;
//		this.llabel = llabel;
//		this.ilabel = ilabel;
//		this.type = type;
//		this.description = description;
//		this.pic = pic;
//		this.location = location;
//		this.ltime = ltime;
//		this.ptime = ptime;
//		this.contact = contact;
//		this.found = found;
//		this.userid = userid;
//		this.reply = reply;
//		this.uname = uname;
//		this.udescription = udescription;
//		this.upic = upic;
//	}
	public LostItemInfo(String title, String llabel, String ilabel,
			String type, String description, String pic, String location,
			String ltime, String contact, int userid, String uname,
			String udescription, String upic,int lost) {
		super();
		this.title = title;
		this.llabel = llabel;
		this.ilabel = ilabel;
		this.type = type;
		this.description = description;
		this.pic = pic;
		this.location = location;
		this.ltime = ltime;
		this.contact = contact;
		this.userid = userid;
		this.uname = uname;
		this.udescription = udescription;
		this.upic = upic;
		this.lost = lost;
	}
	
	
	
	
}
