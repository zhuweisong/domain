package com.house.model;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;

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
		
		update(URL);
		
		return dpl;
	}
	
	void update(String url) {
		Boolean res = connect(url);
		if (res) {
			Element es = doc.select("div.bid_info").first().child(1)
					.getElementsByTag("tbody").first();
			DealInfoSecondHand da = new  DealInfoSecondHand();
			VistorAuctionPriceEname formatter = new VistorAuctionPriceEname(da);
			NodeTraversor traversor = new NodeTraversor(formatter);
			traversor.traverse(es);
		}
	}

}
