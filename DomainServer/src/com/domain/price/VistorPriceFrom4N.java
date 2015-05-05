package com.domain.price;

import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct.DomainPrice;

public class VistorPriceFrom4N extends VistorBase {
	List<DomainPrice> dpl = null;
	private int pricetype = DomainConst.DOMAIN_ISNOT_PRICE;
	
	public VistorPriceFrom4N(int mtype, List<DomainPrice> dpl) {
		super(0, mtype);
		this.dpl = dpl;
	}
	
	@Override
	public void head(Node node, int arg1) {
		//
		if (node.nodeName().equals("span")) {
			String cls = node.attr("class");
			if (StringUtil.in(cls, "icoHotsale"))
				pricetype = DomainConst.DOMAIN_HOT_SALE;
			else if (StringUtil.in(cls, "icoBuynow"))
				pricetype = DomainConst.DOMAIN_BIN_BUYNOW;
			else if (StringUtil.in(cls, "icoStock"))
				pricetype = DomainConst.DOMAIN_BIN_BUYNOW;
		}
		
		super.head(node, arg1);
	}
	
	private int getPriceType(String ptype) {
		int type = DomainConst.DOMAIN_ISNOT_PRICE;

		if (pricetype != DomainConst.DOMAIN_ISNOT_PRICE)
			type = pricetype;
		else if (ptype.equals("买方报价"))
			type = DomainConst.DOMAIN_MAKE_OFFER;
		else if (ptype.length() >= 4) // 69000元
			type = DomainConst.DOMAIN_SALE_OFFER;

		return type;
	}
	
	public void compose() {
		String words[] = accum.toString().split(" ");

		if (words.length >= 6) {
			String domain = words[1].toLowerCase();
			int type = DomainUtil.getDomainType(domain);
			if (	   type == DomainConst.DOMAIN_TYPE_4_COM
					|| type == DomainConst.DOMAIN_TYPE_3_COM
					|| type == DomainConst.DOMAIN_TYPE_2_COM
					|| type == DomainConst.DOMAIN_TYPE_4_CN
					|| type == DomainConst.DOMAIN_TYPE_3_CN
					|| type == DomainConst.DOMAIN_TYPE_2_CN
					|| type == DomainConst.DOMAIN_TYPE_4_NET
					|| type == DomainConst.DOMAIN_TYPE_3_NET
					|| type == DomainConst.DOMAIN_TYPE_2_NET
					) {

				String price = "0"; // no price
				int len = words[3].length();
				String lastch = words[3].substring(len - 1, len);
				if (len > 2 && lastch.equals("元")) {
					String last2ch = words[3].substring(len - 2, len);
					if (last2ch.equals("美元")) 
						price = words[3].substring(0, len - 2).replace(",", "");
					else 
						price = words[3].substring(0, len - 1).replace(",", "");
				}

				int mprice = (int) Long.parseLong(price);
				int mpricetype = getPriceType(words[3]);
				int mfromWeb = DomainConst.DOMAIN_PRICE_FROM_KingMing;
				java.sql.Date mpriceDate = DomainUtil.getCurrentSqlDate();

				DomainPrice DomainPrice = new DomainPrice(domain, type,
						mprice, mpricetype, mfromWeb, mpriceDate);
				// out put

				System.out.println("price:" + DomainPrice.toString());

				dpl.add(DomainPrice);
			}
		}
		pricetype = DomainConst.DOMAIN_ISNOT_PRICE;
	}
}
