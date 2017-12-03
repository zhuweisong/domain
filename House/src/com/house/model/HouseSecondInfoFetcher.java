package com.house.model;
import com.house.constvalue.DataStruct;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.JarEntry;

import javax.tools.JavaCompiler;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class HouseSecondInfoFetcher extends FetcherImpl {
	private final static String TAG = "HouseSecondInfoFetcher"; 
	private static final String url = "http://ris.szpl.gov.cn/credit/showcjgs/esfcjgs.aspx";
	
	public HouseSecondInfoFetcher() {
	}
	
	public List<DataStruct.Item> getData() {
		List<DataStruct.Item> items = getAuctionPriceByUrl(url);
		return items;
	}
	
	private List<DataStruct.Item> getAuctionPriceByUrl(String url) {
		

 		Boolean res = connect(url);
		if (res) {
			java.sql.Date date = parseDate();
			List<DataStruct.Item> days = getDataForDay("ctl00_ContentPlaceHolder1_clientList1", date);
			
			List<DataStruct.Item> months = getDataForDay("ctl00_ContentPlaceHolder1_clientList2", date);
			
			getDataForMonth();
			
			return days;
		}
		
		return null;
	}
	
	private void getDataForMonth() {
		String district = "全市";
		
		java.sql.Date date = parseDate();
		
	}

	private List<DataStruct.Item> getDataForDay(String tag, java.sql.Date date) {
		String district = "全市";
		Element parenet = doc.getElementById(tag);
		Elements es =  parenet.child(0).children();
		List<DataStruct.Item> items = new ArrayList<DataStruct.Item>();
		
		int size = es.size();
		//从有效的第一列数据，到最后一更数据
		for (int i = 1; i < size - 1; i++) {
			Element element = es.get(i);
			DataStruct.Item item = parse(element);
			if (item != null) {
				item.HouseDistrict = district;
				item.date = date;
				items.add(item);
				
				System.out.println( TAG + ":" + item.toString());
			}
		}
		return items;
	}
	
	DataStruct.Item parse(Element element) {
		Elements td = element.children();
		DataStruct.Item dataBase = null;
		int size = td.size();
		
		if (size >= 3) {
			Element e0 = td.get(0);
			String usefulness = e0.text();
			
			Element e1 = td.get(1);
			String area = e1.text().replaceAll("\u00A0", "");

			int pos = area.indexOf(".");
			if (pos > 0)
				area = area.substring(0, pos);
			
			Element e2 = td.get(2);
			String DealQuantity = e2.text();
			area = area.replaceAll(" ", "");
			
			dataBase = new DataStruct.Item();
			dataBase.usefulness = usefulness;
			dataBase.DealArea = Integer.valueOf(area).intValue();
			dataBase.DealQuantity = Integer.valueOf(DealQuantity);

		} 
		else {
			System.out.println( TAG + " parse error");
		}

		return dataBase;
	}
	
	
	java.sql.Date parseDate() {
		Element parenet = doc.getElementById("ctl00_ContentPlaceHolder1_lblCurTime1");
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
			Date date = sdf.parse(year + "-" + month + "-" + day);
			sqldate = new java.sql.Date(date.getTime());
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		
		return sqldate;
	}
	//
	
	
	
}
