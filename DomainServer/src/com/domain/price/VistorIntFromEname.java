package com.domain.price;

import java.util.List;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct.DomainPrice;

public class VistorIntFromEname extends VistorBase {
	List<DomainPrice> dprice = null;
	
	VistorIntFromEname(int mtype, List<DomainPrice> dp) {
		super(0, mtype);
		this.dprice = dp;
	}

	public void compose() {

		String words[] = accum.toString().split(" ");
		if (words.length > 6) {
			int preLen = DomainUtil.getLenthFromDomainType(domaintype);
			String suffix = DomainUtil.getSuffixFromDomainType(domaintype);
			int domainLenth = preLen + suffix.length() + 1;
			
			String domain = words[0].substring(0, domainLenth);
			int mpricetype = DomainConst.DOMAIN_BIN_BUYNOW;

			String price = "";
			if (words.length == 8)
				price = words[5].substring(0, words[5].length() - 1);
			else
				price = words[4].substring(0, words[4].length() - 1);

			int mtype = domaintype;
			int mprice = (int) Long.parseLong(price);
			int mfromWeb = DomainConst.DOMAIN_PRICE_FROM_ENAME;
			java.sql.Date mpriceDate = DomainUtil.getCurrentSqlDate();

			DomainPrice DomainPrice = new DomainPrice(domain, mtype, mprice,
					mpricetype, mfromWeb, mpriceDate);
			// out put

			System.out.println("price:" + DomainPrice.toString());

			dprice.add(DomainPrice);
		}
	}
}
