package com.domain.price;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.Struct.DomainAuction;


public class FetcherAuctionURLFromEname extends  FetcherBase {

	List<DomainAuction> dpl = new ArrayList<DomainAuction>();
	static private final String preURL_4COM = "http://auction.ename.com/auction/domainlist?domainsld=&domaingroup=5&transtype=1&registrar=&bidpricestart=1&bidpriceend=&skipword1=&skip_type=0&domaintld%5B%5D=1&tld_type=0&sort=1&finishtime=&domainlenstart=1&domainlenend=&skipword2=&name=per_page=";
	static private final String preURL_4CN = "http://auction.ename.com/auction/domainlist?domaintld[]=2&domainlenstart=1&domaingroup=5&bidpricestart=1&transtype=1&sort=1&type=0&submit=%E6%8F%90%E4%BA%A4&per_page=";
	static private final String preURL_3COM = "http://auction.ename.com/auction/domainlist?domainsld=&domaingroup=4&transtype=1&registrar=&bidpricestart=1&bidpriceend=&skipword1=&skip_type=0&domaintld%5B%5D=1&tld_type=0&sort=1&finishtime=&domainlenstart=1&domainlenend=&skipword2=&name=&per_page=";
	static private final String preURL_3CN = "http://auction.ename.com/auction/domainlist?domainsld=&domaingroup=4&transtype=1&registrar=&bidpricestart=1&bidpriceend=&skipword1=&skip_type=0&domaintld%5B%5D=2&tld_type=0&sort=1&finishtime=&domainlenstart=1&domainlenend=&skipword2=&name=&per_page=";
	
	
	private String getUrl(int type, int record) {
		StringBuilder sb = new StringBuilder();
		switch (type) {
		case DomainConst.DOMAIN_TYPE_3_COM:
			sb.append(preURL_3COM);
			break;
			
		case DomainConst.DOMAIN_TYPE_3_CN:
			sb.append(preURL_3CN);
			break;
			
		case DomainConst.DOMAIN_TYPE_4_COM:
			sb.append(preURL_4COM);
			break;
			
		case DomainConst.DOMAIN_TYPE_4_CN:
			sb.append(preURL_4CN);
			break;
		}

		sb.append(record);
		return sb.toString();
	}
	
	//1. get the Bidding url, and store in DB
	public List<DomainAuction> getAllAuctionURL(int type, int page) {
		
		dpl.clear();

		String url = getUrl(type, page);
		Boolean res = connect(url);
		if (res) {
			Element es = doc.select("table.com_table").first().child(2);
			VistorAuctionURLFromEname formatter = new VistorAuctionURLFromEname(type, dpl);
			NodeTraversor traversor = new NodeTraversor(formatter);
			traversor.traverse(es);
		}

		return dpl;
	}
	
	public static void main(String... args) throws IOException {
		FetcherAuctionURLFromEname fetchor = new FetcherAuctionURLFromEname();
		DBUpdateProxy proxy = new DBUpdateProxy();
		proxy.init();
		int i = 0;
		int page = 0;

		do {
			List<DomainAuction> dpl = fetchor.getAllAuctionURL(
					DomainConst.DOMAIN_TYPE_4_COM, page * 30);
			i = dpl.size();
			if (i>0)
				proxy.updateBiddingURL(dpl);
			page++;
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		} while (i > 0);

		System.out.println("-----------");
	}
}
