package com.stay4it.model;

import java.io.Serializable;

/** 
 * @author Stay  
 * @version create time：Nov 10, 2014 1:35:26 PM 
 */
public class Skill implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String desc;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
