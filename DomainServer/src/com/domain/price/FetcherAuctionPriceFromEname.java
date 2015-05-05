package com.domain.price;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.Struct.DomainAuction;
import com.domain.constvalue.Struct.DomainPrice;

public class FetcherAuctionPriceFromEname extends FetcherBase {

	public List<DomainPrice> getAutcionPrice(Map<String, DomainAuction> dpm) {
		List<DomainPrice> dpl = new ArrayList<DomainPrice>();
		List<Map.Entry<String, DomainAuction>> dplA = new ArrayList<Map.Entry<String, DomainAuction>>();
		dplA.addAll(dpm.entrySet());

		for (java.util.Map.Entry<String, DomainAuction> entry : dplA) {
			DomainAuction da = entry.getValue();
			String url = da.AuctionUrl;
			getAuctionPriceByUrl(url, da);
			dpl.add(da);
		}

		return dpl;
	}

	private void getAuctionPriceByUrl(String url, DomainAuction da) {

		Boolean res = connect(url);
		if (res) {
			Element es = doc.select("div.bid_info").first().child(1)
					.getElementsByTag("tbody").first();
			VistorAuctionPriceEname formatter = new VistorAuctionPriceEname(da);
			NodeTraversor traversor = new NodeTraversor(formatter);
			traversor.traverse(es);
		}
	}

	public static void main(String... args) throws IOException {

		DBUpdateProxy proxy = new DBUpdateProxy();
		proxy.init();

		FetcherAuctionPriceFromEname fetch = new FetcherAuctionPriceFromEname();
		Map<String, DomainAuction> da = proxy.getAuctionURL();
		List<DomainPrice> dpl = fetch.getAutcionPrice(da);
		proxy.updateprice(dpl);
		
	}
}
