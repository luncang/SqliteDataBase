package com.stay4it.db.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/** 
 * @author Stay  
 * @version create time：Nov 11, 2014 2:02:51 PM 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {
	boolean id() default false;
	String name() default "";
	ColumnType type() default ColumnType.UNKNOWN;
	boolean autofresh() default false;
	public enum ColumnType{
		TONE, TMANY, SERIALIZABLE , UNKNOWN
	}
}
