package com.stay4it.model;

import com.stay4it.db.annotation.Column;
import com.stay4it.db.annotation.Table;
import com.stay4it.db.utilities.JsonUtil;

import java.util.ArrayList;


/**
 * @author Stay
 * @version create time：Nov 10, 2014 1:30:03 PM
 */
@Table(name = "company")
public class Company {
	@Column(id = true)
	private String id;
	@Column
	private String name;
	@Column
	private String url;
	@Column
	private String tel;
	@Column
	private String address;

	private String group;
	
	@Column(type = Column.ColumnType.TMANY,autofresh=true)
	private ArrayList<Developer> developers;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public ArrayList<Developer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(ArrayList<Developer> developers) {
		this.developers = developers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "id:" + id + ",name:" + name + ",url:" + url + ",tel:" + tel + ",address:" + address + ",developers" + (developers != null? JsonUtil.toJson(developers) :"null");
	}
}
