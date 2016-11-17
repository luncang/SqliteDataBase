package com.stay4it.model;

import java.util.ArrayList;

/** 
 * @author Stay  
 * @version create time：Nov 18, 2014 3:06:30 PM 
 */
public class DaoArray<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> ids;
	
	
	public DaoArray(ArrayList<String> ids){
		this.ids = ids;
	}
	
	@Override
	public T get(int index) {
		return super.get(index);
	}
}
