package com.house.model;
import com.house.constvalue.DataStruct;
import com.house.constvalue.DataStruct.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HouseSecondHandInfoFetcher extends Fetcher {
	private static final String TAG = "HouseSecondInfoFetcher"; 
	private static final String url = "http://ris.szpl.gov.cn/credit/showcjgs/esfcjgs.aspx";
	
	public HouseSecondHandInfoFetcher() {
	}
	
	@Override
	protected String getURL(int type) {
		return url;
	}
	
	@Override
	protected String getTag(int type) {
		if (type == TYPE_DAY) 
			return "ctl00_ContentPlaceHolder1_clientList1";
		else if (type == TYPE_MONTH) 
			return "ctl00_ContentPlaceHolder1_clientList2";
		return null;
	}
	
	@Override
	protected String getTagDate(int type) {
		return "ctl00_ContentPlaceHolder1_lblCurTime1";
	}

	/**
	 * 解析数据
	 * @param tag
	 * @param date
	 * @return
	 */
	@Override
	protected List<Item> realyGetData(String tag, java.sql.Date date) {
		String district = "全市";
		Element parenet = doc.getElementById(tag);
		Elements es =  parenet.child(0).children();
		List<DataStruct.Item> items = new ArrayList<DataStruct.Item>();
		
		int size = es.size();
		//从有效的第一列数据，到最后一更数据
		for (int i = 1; i < size - 1; i++) {
			Element element = es.get(i);
			DataStruct.Item item = parseElemment(element);
			if (item != null) {
				item.HouseDistrict = district;
				item.date = date;
				items.add(item);
				
				System.out.println( TAG + ":" + item.toString());
			}
		}
		return items;

	}
	

	/**
	 * 解析具体的数据
	 * @param element
	 * @return
	 */

	protected DataStruct.Item parseElemment(Element element) {
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
			
			dataBase = new DataStruct.Item();
			dataBase.Usefulness = usefulness;
			dataBase.DealArea = Integer.valueOf(area).intValue();
			dataBase.DealQuantity = Integer.valueOf(DealQuantity);

		} 
		else {
			System.out.println( TAG + " parse error");
		}

		return dataBase;
	}
	
	
}
