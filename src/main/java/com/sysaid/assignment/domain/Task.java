package com.sysaid.assignment.domain;

import java.io.Serializable;


/**
 * representing simple task
 */
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	private String activity;
	private Float accessibility;
	private String type;
	private Integer participants;
	private Float price;
	private String link;
	private String key;


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Float getAccessibility() {
		return accessibility;
	}

	public void setAccessibility(Float accessibility) {
		this.accessibility = accessibility;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getParticipants() {
		return participants;
	}

	public void setParticipants(Integer participants) {
		this.participants = participants;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}




}

