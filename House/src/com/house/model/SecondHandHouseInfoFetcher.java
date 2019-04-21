package com.house.model;
import com.house.constvalue.DataStruct;
import com.house.constvalue.DataStruct.Item;
import com.house.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SecondHandHouseInfoFetcher extends Fetcher {
	private static final String TAG = "HouseSecondInfoFetcher"; 
	private static final String url = "http://zjj.sz.gov.cn/ris/szfdc/showcjgs/esfcjgs.aspx";
	
	public SecondHandHouseInfoFetcher() {
	}
	
	@Override
	protected String getURL() {
		return url;
	}
	
	@Override
	protected String getTagOfDay() {
		return "lbldistrict1";
	}
	
	@Override
	protected String getTagOfMonth() {
		return "lbldistrict2";
	}
	
	@Override
	protected String getTagOfDate() {
		return "lblCurTime1";
	}

	/**
	 * 解析数据
	 * @param tag
	 * @param date
	 * @return
	 */
	@Override
	protected List<Item> realyGetData(String tag, java.sql.Date date, int type) {
		
		Element tagEle = doc.getElementById(tag);
		
		if (tagEle!=null) {
			final String district = tagEle.text();
			Element parenet = tagEle.parent();
			
			Elements es =  parenet.child(2).child(0).children();
			List<DataStruct.Item> items = new ArrayList<DataStruct.Item>();
			
			int size = es.size();
			//从有效的第一列数据，到最后一更数据
			for (int i = 1; i < size - 2; i++) {
				Element element = es.get(i);
				DataStruct.Item item = parseElemment(element);
				if (item != null) {
					item.HouseDistrict = district;
					item.date = date;
					item.Type = type;
					items.add(item);
					Utils.Log(tag, item.toString());
				}
			}
			return items;
		} 
		else {
			System.out.println( TAG + ": realyGetData Error ");
		}

		return null;

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
			String area = e1.text();

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
			Utils.Log(TAG, " parse error");
		}

		return dataBase;
	}
	
	
}
