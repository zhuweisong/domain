package com.domain.whois;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct;
import com.domain.constvalue.Struct.DomainPrice;
import com.domain.constvalue.Struct.DomainWhois;
import com.domain.helper.HttpConnection;

public class FetchWhois extends FetchWhoisBase  {

	Document doc = null;
	static private final String WhoisURL = "http://123.4.cn/index/whoisquery/?r=&s=";

	public void getWhois(String url, int type) {
		Boolean res = true;

		try {
			Connection con = HttpConnection.connect(url);
			con.timeout(8000);
			Connection.Response response = con.execute();
			String body = response.body();
			parsewhois(body,type);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected String getDetail(String meta_data, String key) {
		String value = "";
		int index = meta_data.indexOf(key);
		if (index > 0) {
			int brIndex = meta_data.indexOf("\n", index);
			value = meta_data.substring(index + key.length(), brIndex);
		}
		return value;
	}
	

	public void parsewhois(String json, int type) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		// System.out.println(jsonObject);
		Struct.DomainWhois info = null;

		String state_code = (String) jsonObject.get("state_code");
		if (state_code.equals("200")) {
			JSONObject jbody = jsonObject.getJSONObject("body");

			String domain = jbody.getString("domain_name");
			String reg_name = jbody.getString("owner_name").replace("'", "");
			String reg_email = jbody.getString("owner_email").replace("'", "");
			String meta_data = jbody.getString("meta_data");
			java.sql.Date date = DomainUtil.getCurrentSqlDate();
		
			if (reg_email == null || reg_email.isEmpty())
				reg_email = getDetail(meta_data, "Registrant Email: ");
			
			String reg_phone = getDetail(meta_data, "Registrant Phone: ");
			if (reg_phone.length() > 31)
				reg_phone = reg_phone.substring(0, 31);

			String reg_url = getDetail(meta_data, "Registrar: ");
			String reg_country = getDetail(meta_data, "Registrant Country: ");
			
			info = new DomainWhois(domain, type,
					reg_email, reg_name, reg_phone, reg_url, reg_country, date);

			System.out.println("whois:"+info.toString());
			dpl.add(info);
		}

		return;
	}

	protected String getURL(int i, int type) {
		int len = DomainUtil.getLenthFromDomainType(type);
		String suffix = DomainUtil.getSuffixFromDomainType(type);
		String format = "%0" + String.valueOf(len) + "d";
		String url = WhoisURL + String.format(format, i) + "." + suffix;
		return url;
	}

	public List<Struct.DomainWhois> getAllWhois(Map<String, DomainWhois> dwl) {

		dpl.clear();

		List<Map.Entry<String, DomainWhois>> dwB = new ArrayList<Map.Entry<String, DomainWhois>>();
		dwB.addAll(dwl.entrySet());
		for (java.util.Map.Entry<String, DomainWhois> entry : dwB) {
			DomainWhois dw = entry.getValue();
			String url = WhoisURL + dw.domain;
			getWhois(url,dw.type);
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
			}
		}
		
		return dpl;
	}
	
	public List<Struct.DomainWhois> getAllWhois(Map<String, DomainPrice> pdpl, int type) {

		dpl.clear();

		List<Map.Entry<String, DomainPrice>> dwB = new ArrayList<Map.Entry<String, DomainPrice>>();
		dwB.addAll(pdpl.entrySet());
		for (java.util.Map.Entry<String, DomainPrice> entry : dwB) {
			DomainPrice dw = entry.getValue();
			String url = WhoisURL + dw.domain;
			getWhois(url,dw.type);
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
			}
		}
		
		return dpl;
	}
	
	public static void main(String... args) throws IOException {

		FetchWhois fetchor = new FetchWhois();
		DBUpdateProxy proxy = new DBUpdateProxy();
		proxy.init();

		int start = 4761;
		int step = 12;

		do {
			List<DomainWhois> dpl = fetchor.getAllWhois(
					DomainConst.DOMAIN_TYPE_4_COM, start, start + step);
			proxy.updateWhois(dpl, DomainConst.DOMAIN_TYPE_4_COM, start, start
					+ step);
			dpl.clear();

			start += step;
		} while (start < 1002);

		System.out.println("-----------");
	}
}
