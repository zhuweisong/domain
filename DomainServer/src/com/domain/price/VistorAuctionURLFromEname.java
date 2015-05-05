package com.domain.price;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct.DomainAuction;

public class VistorAuctionURLFromEname extends VistorBase {
	public List<DomainAuction> dprice = null;
	
	VistorAuctionURLFromEname(int mtype, List<DomainAuction> dp) {
		super(VistorBase.VISTOR_ATTR_WITH_URL, mtype);
		this.dprice = dp;
	}

	private int getAuctionType(String t) {
		int res = 0;
		int w0Len = t.length();
		if (w0Len > 20) {
			String last2Char = t.substring(w0Len - 2, w0Len);
			if(last2Char.equals("易拍") || last2Char.equals("元拍"))
				res = DomainConst.DOMAIN_BIN_AUCTION_ONE;
			else 
				res = DomainConst.DOMAIN_BIN_AUCTION_WITH_PRICE;
		}

		return res;
	}

	private String getBiddingUrl(String t) {
		String res = "";
		int w0Len = t.length();
		int p1 = t.indexOf("<");
		int p2 = t.indexOf(">");
		if (w0Len > 20 && p1 > 0 && p2 > 0) {
			res = t.substring(p1 + 1, p2);
		}
		return res;
	}

	private Date getBiddingEnd(String t) {
		Date res = new java.util.Date();
		int p1 = t.indexOf("天");
		int p2 = t.indexOf("时");

		if (p1>0) {
			String day = t.substring(0, p1);
			res.setTime(res.getTime() + 1000*60*60*24*Long.parseLong(day));
		}
		else if (p1 < 0) {
			int p3 = t.indexOf("分");
			if (p2>0) {
				String hour = t.substring(0, p2);
				res.setHours(res.getHours() + Integer.parseInt(hour));

				if (p3>0) {
					String minute = t.substring(p2+1, p3);
					res.setMinutes(res.getMinutes() + Integer.parseInt(minute));
				}
			}
			else {
				if (p3>0) {
					String minute = t.substring(p2+1, p3);
					res.setMinutes(res.getMinutes() + Integer.parseInt(minute));
				}
			}
		}
		return res;
	}

	public void compose() {
		String words[] = accum.toString().split(" ");

		if (words.length > 6) {

			String price = "";
			String RemainTime = "";
			if (words.length == 8) {
				price = words[5].substring(0, words[5].length() - 1);
				RemainTime = words[7];
			} else {
				price = words[4].substring(0, words[4].length() - 1);
				RemainTime = words[6];
			}
				
			int mtype = domaintype;
			int mprice = (int) Long.parseLong(price);
			int mfromWeb = DomainConst.DOMAIN_PRICE_FROM_ENAME;
			java.sql.Date mpriceDate = DomainUtil.getCurrentSqlDate();
			String biddingUrl = getBiddingUrl(words[0]);
			Date biddingEnd = getBiddingEnd(RemainTime);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String t1 = sdf.format(biddingEnd);
			String t2 = sdf.format(mpriceDate);
			
			if(t1.equals(t2)) {
				int preLen = DomainUtil.getLenthFromDomainType(domaintype);
				String suffix = DomainUtil.getSuffixFromDomainType(domaintype);
				int domainLenth = preLen + suffix.length() + 1;
	
				String domain = words[0].substring(0, domainLenth);
				int mpricetype = getAuctionType(words[0]);
	
				DomainAuction DomainPrice = new DomainAuction(domain, mtype,
						mprice, mpricetype, mfromWeb, mpriceDate, biddingUrl,
						new java.sql.Time(biddingEnd.getTime()));
				// out put
	
				System.out.println("bidding:" + DomainPrice.toString());
	
				dprice.add(DomainPrice);
			}
		}
	}
}
