package com.domain.whois;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct;
import com.domain.constvalue.Struct.DomainPrice;
import com.domain.constvalue.Struct.DomainWhois;

public class FetchWhoisBase {
	protected List<Struct.DomainWhois> dpl = new ArrayList<Struct.DomainWhois>();
	
	public List<DomainWhois> getAllWhois(int type, int start, int end) {
		
		int i = start;
		dpl.clear();

		do {
			String url = getURL(i, type);
			getWhois(url,type);

			i++;
			DomainUtil.Sleep(1500);
		} while (i < end);

		return dpl;
	}

	public List<Struct.DomainWhois> getAllWhois(Map<String, DomainWhois> dwl) {
		return dpl;
	}
	
	public List<Struct.DomainWhois> getAllWhois(Map<String, DomainPrice> pdpl, int type) {
		return dpl;
	}
	
	protected void parsewhois(String meta_data, int type) {
		
		if (!meta_data.isEmpty()) {
			
			java.sql.Date date = DomainUtil.getCurrentSqlDate();
			
			String domain = getDetail(meta_data, "Domain Name: ").toLowerCase().trim();
			String reg_name = getDetail(meta_data, "Registrant Name: ");
			String reg_email = getDetail(meta_data, "Registrant Email: ");
			String reg_phone = getDetail(meta_data, "Registrant Phone: ");
			if (reg_phone.length()>32)
				reg_phone.substring(0, 30);
			
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
	
	protected String getDetail(String meta_data, String key) {
		// TODO Auto-generated method stub
		return "";
	}

	protected void getWhois(String url, int type) {
		// TODO Auto-generated method stub
		
	}

	protected String getURL(int i, int type) {
		// TODO Auto-generated method stub
		return "";
	}
	
}
