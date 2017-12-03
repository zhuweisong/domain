package com.house.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {

	static public java.sql.Date getCurrentSqlDate() {
		java.util.Date date1 = new java.util.Date();  
		return new java.sql.Date(date1.getTime()); 
	}
	
	static public java.sql.Date getPreDate(java.sql.Date d, long diff) {
		 d.setTime(d.getTime() + 1000*60*60*24*diff);
		 return d;
	}
	
	public static String getSystemTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());
	}
	static public Boolean dateEquals(java.util.Date A, java.util.Date B) {
		return A.toString().equals(B.toString());
	}
	
	static public java.sql.Date StringtoDate(String d) {
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Date sqldate=new java.sql.Date(getCurrentSqlDate().getTime());
		try {
			java.util.Date date = sdf.parse(d);
			sqldate.setTime(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqldate;
	}
	
}
