package com.house.main;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;

import com.domain.constvalue.Struct.DomainAuction;
import com.domain.constvalue.Struct.DomainPrice;
import com.house.constvalue.DataStruct.DealInfoSecondHand;

public class FetcherImpl extends Fetcher {

	final String URL = "http://ris.szpl.gov.cn/credit/showcjgs/esfcjgs.aspx";
	
	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return URL;
	}
	
	@Override
	protected List<DealInfoSecondHand> getInfoBySecondHand() {
		List<DealInfoSecondHand> dpl = new ArrayList<DealInfoSecondHand>();
		
		for (java.util.Map.Entry<String, DomainAuction> entry : dplA) {
			DomainAuction da = entry.getValue();
			String url = da.AuctionUrl;
			getAuctionPriceByUrl(url, da);
			dpl.add(da);
		}
		
		return dpl;
	}
	
	void update(String url) {
		Boolean res = connect(url);
		if (res) {
			Element es = doc.select("div.bid_info").first().child(1)
					.getElementsByTag("tbody").first();
			VistorAuctionPriceEname formatter = new VistorAuctionPriceEname(da);
			NodeTraversor traversor = new NodeTraversor(formatter);
			traversor.traverse(es);
		}
	}

}
