package com.stay4it.db.utilities;

import java.util.ArrayList;

/** 
 * @author Stay  
 * @version create time：Nov 12, 2014 1:45:35 PM 
 */
public class TextUtil {
	public static boolean isValidate(String content){
		if(content != null && !"".equals(content.trim())){
			return true;
		}
		return false;
	}
	
	public static boolean isValidate(ArrayList list){
		if (list != null && list.size() >0) {
			return true;
		}
		return false;
	}
}
