package com.house.model;

import com.house.constvalue.DataStruct;
import com.house.constvalue.DataStruct.DealInfoSecondHand;

/**
 * 二手信息
 * @author zhuweisong
 *
 */
public class VistorSecondHandInfo extends VistorBase {
	DataStruct.Item da = null;

	String keyYear;
	String keyMonth;
	String keyDay;
	
	protected VistorSecondHandInfo(int mtype, DataStruct.Item da) {
		super(mtype);
		this.da = da;
	}
	
	Boolean isSuccessAuction(String t1, String t2) {
		
		Boolean isEnd = t1.indexOf("")>=0;
		Boolean isBidding = !(t2.indexOf("�����˳���")>=0);
		
		return isEnd && isBidding;
	}
	
	public void compose() {
		String words[] = accum.toString().split(" ");
		
		if (words.length > 3) {
			
			//
			if (isSuccessAuction(words[1], words[2])) {
				int d1 = words[0].indexOf("Ԫ");
				String price = words[0].substring(4, d1);
//				da.price = Integer.parseInt(price);
			}
			else 
//				da.price = 99999999; //

			
			System.out.println("price:" + da.toString());
		}
	}
}
