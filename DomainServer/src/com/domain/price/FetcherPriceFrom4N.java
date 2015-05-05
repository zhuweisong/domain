package com.domain.price;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.Struct.DomainPrice;

public class FetcherPriceFrom4N extends FetcherBase {
	static private final String preURL = "http://www.4.cn/search/result/tlds/0/tlds/1/tlds/56/kws/1/lmin/2/lmax/4/offer/1/price/1/auction/1/hotsale/1/detail/1/search/1/perpage/200/lang/zh/page/";
	static private final String preURL1 = "http://www.4.cn/search/result/tlds/0/tlds/1/tlds/56/kws/1/lmin/2/lmax/4/price/1/auction/1/hotsale/1/detail/1/search/1/perpage/200/lang/zh/page/";

	static public final int PRICE_ALL = 1;
	static public final int PRICE_PART = 2;
	List<DomainPrice> dpl = new ArrayList<DomainPrice>();
	int pg = 0;

	private String getUrl(int type, int page) {
		StringBuilder sb = new StringBuilder();
		switch (type) {
		case PRICE_ALL:
			sb.append(preURL);
			break;
		case PRICE_PART:
			sb.append(preURL1);
			break;
		}

		sb.append(page);
		return sb.toString();
	}

	public int getPage() {
		return pg;
	}

	private int calPageCount() {

		Element e = doc.select("ul.pager").first().child(0).child(0);
		Node node = e.childNode(0);
		if (node instanceof TextNode) {
			String txt = ((TextNode) node).text();
			int d1 = txt.indexOf("records,");
			int d2 = txt.indexOf("pages");
			
			if (d2>=0 && d2>0) {
				String page = txt.substring(d1 + 8, d2).trim();
				pg = Integer.parseInt(page);
			}
			else {
				d1 = txt.indexOf("¼ÇÂ¼,");
				d2 = txt.indexOf("Ò³");
				
				String page = txt.substring(d1 + 3, d2).trim();
				pg = Integer.parseInt(page);
			}
		}
		return pg;
	}

	public List<DomainPrice> getAllDominaPrice(int type, int page) {

		String url = getUrl(type, page);
		Boolean res = connect(url);
		if (res) {
			calPageCount();
			Element es = doc.select("table.grid").first().child(1);

			VistorPriceFrom4N formatter = new VistorPriceFrom4N(0, dpl);
			NodeTraversor traversor = new NodeTraversor(formatter);
			traversor.traverse(es);
		}

		return dpl;
	}

	public static void main(String... args) throws IOException {
		FetcherPriceFrom4N fetchor = new FetcherPriceFrom4N();
		DBUpdateProxy proxy = new DBUpdateProxy();
		proxy.init();

		int page = 1;
		List<DomainPrice> dpl = null;
		do {
			dpl = fetchor.getAllDominaPrice(PRICE_PART, page);

			if (dpl.size() > 100) {
				proxy.updateprice(dpl);
				dpl.clear();
			}

			page++;

			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}
		} while (page <= fetchor.getPage());

		if (dpl.size() > 0) {
			proxy.updateprice(dpl);
			dpl.clear();
		}

		System.out.println("-----------");
	}
}
