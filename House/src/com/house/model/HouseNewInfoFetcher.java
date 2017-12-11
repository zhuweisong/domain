package com.house.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.house.constvalue.DataStruct;
import com.house.constvalue.DataStruct.Item;

public class HouseNewInfoFetcher extends Fetcher {
	private static final String TAG = "HouseNewInfoFetcher"; 
	private static final String urlD = "http://ris.szpl.gov.cn/credit/showcjgs/ysfcjgs.aspx?cjType=0";
	private static final String urlM = "http://ris.szpl.gov.cn/credit/showcjgs/ysfcjgs.aspx?cjType=0";
	
	public HouseNewInfoFetcher() {	
	}

	@Override
	protected String getURL(int type) {
		if (type == TYPE_DAY) 
			return urlD;
		else if (type == TYPE_MONTH) 
			return urlM;
		return null;
	}
	
	@Override
	protected String getTag(int type) {
		if (type == TYPE_DAY) 
			return "TrClientList2";
		else if (type == TYPE_MONTH) 
			return "TrClientList4";
		return null;
	}

	@Override
	protected List<Item> realyGetData(String tag, Date date) {
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
			area = area.replaceAll(" ", "");
			
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

	@Override
	protected Date parseDate() {
		
		return null;
	}






	
}
