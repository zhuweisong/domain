package com.domain.price;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.Struct.DomainPrice;

public class FetcherIntFromEname extends  FetcherBase {

	static private final String preURL_4COM = "http://auction.ename.com/auction/domainlist?domaintld[]=1&domainlenstart=1&domaingroup=5&bidpricestart=1&transtype=4&sort=1&type=0&submit=%E6%8F%90%E4%BA%A4&per_page=";
	static private final String preURL_4CN ="http://auction.ename.com/auction/domainlist?domaintld[]=2&domainlenstart=1&domaingroup=5&bidpricestart=1&transtype=4&sort=1&type=0&submit=%E6%8F%90%E4%BA%A4&per_page=";
	static private final String preURL_3CN = "http://auction.ename.com/auction/domainlist?domaintld[]=2&domainlenstart=1&domaingroup=4&bidpricestart=1&transtype=4&sort=1&type=0&submit=%E6%8F%90%E4%BA%A4&per_page=";
	static private final String preURL_3COM = "http://auction.ename.com/auction/domainlist?domainsld=&domaingroup=4&transtype=4&registrar=&bidpricestart=1&bidpriceend=&skipword1=&skip_type=0&domaintld%5B%5D=1&tld_type=0&sort=1&finishtime=&domainlenstart=1&domainlenend=&skipword2=&name=";
	static private final String preURL_3NET = "http://auction.ename.com/auction/domainlist?domaintld[]=4&domainlenstart=1&domaingroup=4&bidpricestart=1&transtype=4&sort=1&type=0&submit=%E6%8F%90%E4%BA%A4&per_page=";
	static private final String preURL_4NET = "http://auction.ename.com/auction/domainlist?domaintld[]=4&domainlenstart=1&domaingroup=5&bidpricestart=1&transtype=4&sort=1&type=0&submit=%E6%8F%90%E4%BA%A4&per_page=";
	
	List<DomainPrice> dpl = new ArrayList<DomainPrice>();

	private String getUrl(int type, int record) {
		StringBuilder sb = new StringBuilder();
		switch (type) {
		case DomainConst.DOMAIN_TYPE_3_COM:
			sb.append(preURL_3COM);
			break;
			
		case DomainConst.DOMAIN_TYPE_3_CN:
			sb.append(preURL_3CN);
			break;
			
		case DomainConst.DOMAIN_TYPE_3_NET:
			sb.append(preURL_3NET);
			break;
			
		case DomainConst.DOMAIN_TYPE_4_COM:
			sb.append(preURL_4COM);
			break;
			
		case DomainConst.DOMAIN_TYPE_4_CN:
			sb.append(preURL_4CN);
			break;
			
		case DomainConst.DOMAIN_TYPE_4_NET:
			sb.append(preURL_4NET);
			break;
		}

		sb.append(record);
		return sb.toString();
	}

	public List<DomainPrice> getAllDominaPrice(int type, int page) {

		dpl.clear();

		String url = getUrl(type, page);
		Boolean res = connect(url);
		if (res) {
			Element es = doc.select("table.com_table").first().child(2);

			VistorIntFromEname formatter = new VistorIntFromEname(type, dpl);
			NodeTraversor traversor = new NodeTraversor(formatter);
			traversor.traverse(es);
		}

		return dpl;
	}

	public static void main(String... args) throws IOException {
		FetcherIntFromEname fetchor = new FetcherIntFromEname();
		DBUpdateProxy proxy = new DBUpdateProxy();
		proxy.init();
		int i = 0;
		int page = 0;

		do {
			List<DomainPrice> dpl = fetchor.getAllDominaPrice(
					DomainConst.DOMAIN_TYPE_3_CN, page * 30);
			proxy.updateprice(dpl);
			page++;
			i = dpl.size();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		} while (i >= 30);

		System.out.println("-----------");
	}
}
