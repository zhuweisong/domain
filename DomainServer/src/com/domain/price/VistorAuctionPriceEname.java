package com.domain.price;


import com.domain.constvalue.Struct.DomainPrice;

public class VistorAuctionPriceEname extends VistorBase {
	DomainPrice da = null;
	VistorAuctionPriceEname(DomainPrice da) {
		super(0, 0);
		this.da = da;
		// TODO Auto-generated constructor stub
	}

	Boolean isSuccessAuction(String t1, String t2) {
		
		Boolean isEnd = t1.indexOf("交易已结束")>=0;
		Boolean isBidding = !(t2.indexOf("暂无人出价")>=0);
		
		return isEnd && isBidding;
	}
	
	public void compose() {
		String words[] = accum.toString().split(" ");
		
		if (words.length > 3) {
			
			//成功结拍
			if (isSuccessAuction(words[1], words[2])) {
				int d1 = words[0].indexOf("元");
				String price = words[0].substring(4, d1);
				da.price = Integer.parseInt(price);
			}
			else 
				da.price = 99999999; //流拍
			// out put
			
			System.out.println("price:" + da.toString());
		}
	}
}
