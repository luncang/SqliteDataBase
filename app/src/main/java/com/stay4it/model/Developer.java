package com.stay4it.model;

import com.stay4it.db.annotation.Column;
import com.stay4it.db.annotation.Table;
import com.stay4it.db.utilities.JsonUtil;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author Stay
 * @version create time：Nov 10, 2014 1:26:48 PM
 */
@Table(name = "developer")
public class Developer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String _ID = "id";
	public static final String _NAME = "name";
	public static final String _AGE = "age";
	public static final String _COMPANY = "company";
	public static final String _SKILLS = "skills";
	@Column(id = true)
	private String id;
	@Column
	private String name;
	@Column
	private int age;
	@Column(type = Column.ColumnType.TONE, autofresh = true)
	private Company company;
	@Column(type = Column.ColumnType.SERIALIZABLE)
	private ArrayList<Skill> skills;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public ArrayList<Skill> getSkills() {
		return skills;
	}

	public void setSkills(ArrayList<Skill> skills) {
		this.skills = skills;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "id:" + id + ",name:" + name + ",age:" + age + ",skills:" + JsonUtil.toJson(skills) + ",company:" + (company != null ? company
				.toString() : "null");
	}
}
