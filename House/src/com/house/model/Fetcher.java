package com.house.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.house.constvalue.DataStruct;
import com.house.constvalue.DataStruct.DealInfoSecondHand;



public abstract class Fetcher {
	protected Document doc = null;
	protected final static int TYPE_DAY = 0;
	protected final static int TYPE_MONTH = 1;
	
	protected abstract String getURL(int type);

	protected abstract List<DataStruct.Item> realyGetData(String tag, java.sql.Date date);
	
	protected abstract String getTag(int type);
	
	protected abstract String getTagDate(int type);

	
	protected Boolean connect(String url) {
		Boolean res = true;
		try {
			Connection con = Jsoup.connect(url);
			con.timeout(8000);
			doc = con.get();
		} catch (MalformedURLException e1) {
			res = false;
			e1.printStackTrace();
		} catch (IOException e1) {
			res = false;
			e1.printStackTrace();
		}
		return res && doc != null;
	}
	
	public List<DataStruct.Item> getDataForDay() {
		//1、获取URL
		String url = getURL(TYPE_DAY);
		
		//2、连接URL
 		Boolean res = connect(url);
		if (res) {
			
			 Calendar rightNow = Calendar.getInstance();
			 rightNow.add(Calendar.DAY_OF_YEAR,-1);//日期减1天
			 
			 String tagDateString = getTagDate(TYPE_DAY);
 			 java.sql.Date date = parseDate(tagDateString);
			
			Date yesterday = rightNow.getTime();
			
			//1、如果数据等于当天，则拉数据
			if (yesterday.getYear() == date.getYear() 
					&& yesterday.getMonth() == date.getMonth() 
					&& yesterday.getDay() == date.getDay()) {
				String tag = getTag(TYPE_DAY);
				List<DataStruct.Item> daydata = realyGetData(tag, date);
				return daydata;
			}
		}
		
		return null;
	}
	
	public List<DataStruct.Item> getDataForMonth() {
		Date today = new Date();
		if (today.getDay() == 1) {
			String url = getURL(TYPE_MONTH);
	 		Boolean res = connect(url);
	 		
			if (res) {
				java.sql.Date date = new java.sql.Date(today.getTime());
				String tag = getTag(TYPE_MONTH);
				List<DataStruct.Item> monthdata = realyGetData(tag, date);
				return monthdata;
			}
		}

		return null;
	}
	
	protected java.sql.Date parseDate(String dateTag) {
		Element parenet = doc.getElementById(dateTag);
		String dateString = parenet.text();
		int ypos = dateString.indexOf("年");
		int mpos = dateString.indexOf("月");
		int dpos = dateString.indexOf("日");
		
		String yearStr = dateString.substring(0, ypos);
		String monthStr = dateString.substring(ypos + 1, mpos);
		String dayStr = dateString.substring(mpos + 1, dpos);
		
		int year = Integer.valueOf(yearStr);
		int month = Integer.valueOf(monthStr);
		int day = Integer.valueOf(dayStr);
		
		java.sql.Date sqldate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse(year + "-" + month + "-" + day);
			sqldate = new java.sql.Date(date.getTime());
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		
		return sqldate;
	}

}
