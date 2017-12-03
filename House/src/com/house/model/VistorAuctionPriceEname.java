package com.house.model;


import com.house.constvalue.DataStruct.DealInfoSecondHand;


public class VistorAuctionPriceEname extends VistorBase {

	
	DealInfoSecondHand da = null;
	String s1;

	
	String keyYear;
	String keyMonth;
	String keyDay;
	
	VistorAuctionPriceEname(DealInfoSecondHand da) {
		super(0);
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
			// out put
			
			System.out.println("price:" + da.toString());
		}
	}
}
