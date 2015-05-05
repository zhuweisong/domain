package com.domain.whois;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct;
import com.domain.constvalue.Struct.DomainWhois;
import com.domain.helper.HttpConnection;
import com.domain.price.FetcherBase;

public class FetchWhoisFromWan extends FetcherBase {
	static private final String WhoisURL = "http://whois.hichina.com/whois/api_whois_full?web_server=whois.ename.com&host=";
	//whois.ename.net/index.php/tools/whoisdetail/580.com/whois.ename.com/c44c2ec55707d278d2aa7d42ac26a2e3/1403280383/
	List<Struct.DomainWhois> dpl = new ArrayList<Struct.DomainWhois>();
	
	public Boolean getWhois(String url, int type) {

			Connection con = HttpConnection.connect(url);
			con.timeout(10000);
			Connection.Response response;
			
			try {
				response = con.execute();
				String body = response.body();
				parsewhois(body,type);

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return true;
	}

	private String getDetail(String meta_data, String key) {
		String value = "";
		int index = meta_data.indexOf(key);
		if (index >= 0) {
			int brIndex = meta_data.indexOf("\n", index);
			value = meta_data.substring(index + key.length(), brIndex);
		}
		return value;
	}
	
	private void parsewhois(String meta_data, int type) {
		
		if (!meta_data.isEmpty()) {
			System.out.println("whois: "+meta_data.length());
			
			java.sql.Date date = DomainUtil.getCurrentSqlDate();
			
			String domain = getDetail(meta_data, "Domain Name: ").toLowerCase().trim();
			String reg_name = getDetail(meta_data, "Registrant Name: ").replace("'", "");
			String reg_email = getDetail(meta_data, "Registrant Email: ").replace("'", "");
			String reg_phone = getDetail(meta_data, "Registrant Phone: ").replace("'", "");
			if (reg_phone.length()>32)
				reg_phone.substring(0, 31);
			
			String reg_url = getDetail(meta_data, "Registrar: ");
			String reg_country = getDetail(meta_data, "Registrant Country: ").replace("'", "");
			
			if (!domain.isEmpty()) {
				Struct.DomainWhois info = new DomainWhois(domain, type,
						reg_email, reg_name, reg_phone, reg_url, reg_country, date);
		
				System.out.println("whois:"+info.toString());
				dpl.add(info);
			}
		}
	}
	
	public List<DomainWhois> getAllWhois(int type, int start, int end) {
		
		int i = start;
		int len = DomainUtil.getLenthFromDomainType(type);
		String suffix = DomainUtil.getSuffixFromDomainType(type);
		String format = "%0" + String.valueOf(len) + "d";
		dpl.clear();

		do {
			String url = WhoisURL + String.format(format, i) + "." + suffix;
			getWhois(url,type);

			i++;
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}
		} while (i < end);

		return dpl;
	}
	
	public static void main(String... args) throws IOException {

		FetchWhoisFromWan fetchor = new FetchWhoisFromWan();
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
