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
		
		Boolean isEnd = t1.indexOf("�����ѽ���")>=0;
		Boolean isBidding = !(t2.indexOf("�����˳���")>=0);
		
		return isEnd && isBidding;
	}
	
	public void compose() {
		String words[] = accum.toString().split(" ");
		
		if (words.length > 3) {
			
			//�ɹ�����
			if (isSuccessAuction(words[1], words[2])) {
				int d1 = words[0].indexOf("Ԫ");
				String price = words[0].substring(4, d1);
				da.price = Integer.parseInt(price);
			}
			else 
				da.price = 99999999; //����
			// out put
			
			System.out.println("price:" + da.toString());
		}
	}
}
