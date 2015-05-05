package com.domain.whois;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct;
import com.domain.constvalue.Struct.DomainWhois;
import com.domain.helper.HttpConnection;
import com.domain.price.FetcherBase;
import com.domain.price.VistorPriceFrom4N;

public class FetchWhoisFrompheenix  extends FetchWhoisBase  {

	static private final String WhoisURL = "https://www.pheenix.com/whois.php?domain=";
	
	public void getWhois(String url, int type) {

			Connection con = HttpConnection.connect(url);
			con.timeout(8000);
			Connection.Response response;
			
			try {
				response = con.execute();
				String body = response.body();
				int in0 = body.indexOf("<div class=\"swData\">");
				int in1 = body.indexOf("Last update of whois database", in0);
				if (in0>=0 && in1>20) {
					body = body.substring(in0+20, in1);
					parsewhois(body,type);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	protected String getDetail(String meta_data, String key) {
		String value = "";
		int index = meta_data.indexOf(key);
		if (index >= 0) {
			int brIndex = meta_data.indexOf("<br", index);
			value = meta_data.substring(index + key.length(), brIndex);
		}
		return value;
	}

	
	protected String getURL(int i, int type) {
		int len = DomainUtil.getLenthFromDomainType(type);
		String suffix = DomainUtil.getSuffixFromDomainType(type);
		String format = "%0" + String.valueOf(len) + "d";
		String url = WhoisURL + String.format(format, i) + "&tld=" + suffix + "&lookup=%3E%3E";
		return url;
	}
	

	public static void main(String... args) throws IOException {

		FetchWhoisFrompheenix fetchor = new FetchWhoisFrompheenix();
		DBUpdateProxy proxy = new DBUpdateProxy();
		proxy.init();

		int start = 550;
		int step = 2;

		do {
			List<DomainWhois> dpl = fetchor.getAllWhois(
					DomainConst.DOMAIN_TYPE_3_NET, start, start + step);
			proxy.updateWhois(dpl, DomainConst.DOMAIN_TYPE_3_NET, start, start
					+ step);
			dpl.clear();

			start += step;
		} while (start < 1002);

		System.out.println("-----------");
	}


}
