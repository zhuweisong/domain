package com.house.model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.house.constvalue.DataStruct;
import com.house.constvalue.DataStruct.Item;

public class NewHouseInfoFetcher extends Fetcher {
	private static final String TAG = "HouseNewInfoFetcher"; 
	private static final String urlD = "http://ris.szpl.gov.cn/credit/showcjgs/ysfcjgs.aspx?cjType=0";
	private static final String urlM = "http://ris.szpl.gov.cn/credit/showcjgs/ysfcjgs.aspx?cjType=0";
	
	public NewHouseInfoFetcher() {	
	}

	@Override
	protected String getURL() {
		return urlM;
	}
	
	@Override
	protected String getTagOfDay() {
			return "clientList2"; 

	}
	
	@Override
	protected String getTagOfMonth() {
		
			return "clientList2"; 
	}
	
	@Override
	protected String getTagOfDate() {

			return "lbldistrict2";

	}
	
	@Override
	protected List<Item> realyGetData(String tag, Date date, int type) {
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
				item.Type = type;
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
			//1.用途
			Element e0 = td.get(0);
			String usefulness = e0.text();
			
			//2.成交套数
			Element e1 = td.get(1);
			String DealQuantity = e1.text();

			//3.成交面积(㎡)
			Element e2 = td.get(2);
			String area = e2.text().replaceAll("\u00A0", "");

			int pos = area.indexOf(".");
			if (pos > 0)
				area = area.substring(0, pos);

			//4.成交均价(元)
			Element e3 = td.get(3);
			String price = e3.text();
			
			//5.月末可售套数
			Element e4 = td.get(4);
			String forSaleQuantity = e4.text();
			
			//5.月末可售面积(㎡)
			Element e5 = td.get(5);
			String forSaleArea = e5.text();
			
			pos = forSaleArea.indexOf(".");
			if (pos > 0)
				forSaleArea = forSaleArea.substring(0, pos);
			
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
