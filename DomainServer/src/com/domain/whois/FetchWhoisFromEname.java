package com.domain.whois;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct.DomainWhois;
import com.domain.helper.HttpConnection;

public class FetchWhoisFromEname  extends FetchWhoisBase {
	static private final String WhoisURL = "http://whois.ename.net//";
	static private final String WhoisURL_1 = "http://whois.ename.net/index.php/tools/whoisdetail/";//580.com/whois.ename.com/c44c2ec55707d278d2aa7d42ac26a2e3/1403280383/

	static private final String FirstString = "javascript:whoisDetail(";
	
	protected void getWhois(String url, int type) {

			Connection con = HttpConnection.connect(url);
			con.timeout(10000);
			Connection.Response response;
			
			try {
				response = con.execute();
				String body = response.body();
				int index1 = body.indexOf(FirstString);
				int index2 = body.indexOf(")", index1);
				if (index1>0 && index2>index1) {

					String serverName = "";
					String verificationCode = "";
					String time = "";
					
					String string = body.substring(index1, index2);
					index1 = string.indexOf("'");
					index2 = string.indexOf("','", index1);
					String domain = string.substring(index1+1,index2);
					
					index1 = index2+3;
					index2 = string.indexOf("','", index1);
					serverName = string.substring(index1,index2);
					
					index1 = index2+3;
					index2 = string.indexOf("','", index1);
					verificationCode = string.substring(index1,index2);
					
					index1 = index2+3;
					index2 = string.indexOf("',", index1);
					time = string.substring(index1,index2);	
					

					if (!domain.isEmpty() && !serverName.isEmpty()) {
						DomainUtil.Sleep(1000);
						url = WhoisURL_1 + domain+"/"+serverName+"/"+verificationCode+"/"+time;
						Connection con1 = HttpConnection.connect(url);
						con1.timeout(10000);
						Connection.Response response1 = con1.execute();
						body = response1.body();
						
						parsewhois(body, type);
					}
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
			int brIndex = meta_data.indexOf("<br>", index);
			value = meta_data.substring(index + key.length(), brIndex);
		}
		return value.replace("'", "").replace("\n", "").replace("\r", "");
	}
	
	
	protected String getURL(int i, int type) {
		int len = DomainUtil.getLenthFromDomainType(type);
		String suffix = DomainUtil.getSuffixFromDomainType(type);
		String format = "%0" + String.valueOf(len) + "d";
		String url = WhoisURL + String.format(format, i) + "." + suffix;
		return url;
	}
	
	public static void main(String... args) throws IOException {

		FetchWhoisFromEname fetchor = new FetchWhoisFromEname();
		DBUpdateProxy proxy = new DBUpdateProxy();
		proxy.init();

		int start = 550;
		int step = 100;

		do {
			List<DomainWhois> dpl = fetchor.getAllWhois(
					DomainConst.DOMAIN_TYPE_3_COM, start, start + step);
			proxy.updateWhois(dpl, DomainConst.DOMAIN_TYPE_3_COM, start, start
					+ step);
			dpl.clear();

			start += step;
		} while (start < 1002);

		System.out.println("-----------");
	}
}
