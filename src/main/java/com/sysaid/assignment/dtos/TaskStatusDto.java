package com.sysaid.assignment.dtos;

import com.sysaid.assignment.domain.Status;

public class TaskStatusDto {
	
	private String key; 
	private Status status;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	} 
	
	
}
