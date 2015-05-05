package com.domain.constvalue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.helper.StringUtil;

public class DomainUtil {

	static public java.sql.Date getCurrentSqlDate() {
		java.util.Date date1 = new java.util.Date();  
		return new java.sql.Date(date1.getTime()); 
	}
	
	static public java.sql.Date getPreDate(java.sql.Date d, long diff) {
		 d.setTime(d.getTime() + 1000*60*60*24*diff);
		 return d;
	}
	
	public static String getSystemTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());
	}
	static public Boolean dateEquals(java.util.Date A, java.util.Date B) {
		return A.toString().equals(B.toString());
	}
	
	static public java.sql.Date StringtoDate(String d) {
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Date sqldate=new java.sql.Date(getCurrentSqlDate().getTime());
		try {
			java.util.Date date = sdf.parse(d);
			sqldate.setTime(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqldate;
	}
	
	static public int getLenthFromDomainType(int type) {
		int len = 1;
		switch (type) {
		case DomainConst.DOMAIN_TYPE_1_COM:
			len =1;
			break;
		case DomainConst.DOMAIN_TYPE_2_COM:
			len =2;
			break;
		case DomainConst.DOMAIN_TYPE_3_COM:
			len =3;
			break;
		case DomainConst.DOMAIN_TYPE_4_COM:
			len =4;
			break;
		case DomainConst.DOMAIN_TYPE_5_COM:
			len =5;
			break;
		case DomainConst.DOMAIN_TYPE_6_COM:
			len =6;
			break;
		case DomainConst.DOMAIN_TYPE_7_COM:
			len =7;
			break;
		case DomainConst.DOMAIN_TYPE_8_COM:
			len =8;
			break;
		case DomainConst.DOMAIN_TYPE_9_COM:
			len =9;
			break;
		case DomainConst.DOMAIN_TYPE_10_COM:
			len =10;
			break;
		
		case DomainConst.DOMAIN_TYPE_1_CN:
			len =1;
			break;
		case DomainConst.DOMAIN_TYPE_2_CN:
			len =2;
			break;
		case DomainConst.DOMAIN_TYPE_3_CN:
			len =3;
			break;
		case DomainConst.DOMAIN_TYPE_4_CN:
			len =4;
			break;
		case DomainConst.DOMAIN_TYPE_5_CN:
			len =5;
			break;
		case DomainConst.DOMAIN_TYPE_6_CN:
			len =6;
			break;
		case DomainConst.DOMAIN_TYPE_7_CN:
			len =7;
			break;
		case DomainConst.DOMAIN_TYPE_8_CN:
			len =8;
			break;
		case DomainConst.DOMAIN_TYPE_9_CN:
			len =9;
			break;
		case DomainConst.DOMAIN_TYPE_10_CN:
			len =10;
			break;

		case DomainConst.DOMAIN_TYPE_1_NET:
			len =1;
			break;
		case DomainConst.DOMAIN_TYPE_2_NET:
			len =2;
			break;
		case DomainConst.DOMAIN_TYPE_3_NET:
			len =3;
			break;
		case DomainConst.DOMAIN_TYPE_4_NET:
			len =4;
			break;
		case DomainConst.DOMAIN_TYPE_5_NET:
			len =5;
			break;
		case DomainConst.DOMAIN_TYPE_6_NET:
			len =6;
			break;
		case DomainConst.DOMAIN_TYPE_7_NET:
			len =7;
			break;
		case DomainConst.DOMAIN_TYPE_8_NET:
			len =8;
			break;
		case DomainConst.DOMAIN_TYPE_9_NET:
			len =9;
			break;
		case DomainConst.DOMAIN_TYPE_10_NET:
			len =10;
			break;
		}
		return len;
	}
	
	static public String getSuffixFromDomainType(int type) {

		switch (type) {
		case DomainConst.DOMAIN_TYPE_1_COM:
		case DomainConst.DOMAIN_TYPE_2_COM:
		case DomainConst.DOMAIN_TYPE_3_COM:
		case DomainConst.DOMAIN_TYPE_4_COM:
		case DomainConst.DOMAIN_TYPE_5_COM:
		case DomainConst.DOMAIN_TYPE_6_COM:
		case DomainConst.DOMAIN_TYPE_7_COM:
		case DomainConst.DOMAIN_TYPE_8_COM:
		case DomainConst.DOMAIN_TYPE_9_COM:
		case DomainConst.DOMAIN_TYPE_10_COM:
			return "com";

		
		case DomainConst.DOMAIN_TYPE_1_CN:
		case DomainConst.DOMAIN_TYPE_2_CN:
		case DomainConst.DOMAIN_TYPE_3_CN:
		case DomainConst.DOMAIN_TYPE_4_CN:
		case DomainConst.DOMAIN_TYPE_5_CN:
		case DomainConst.DOMAIN_TYPE_6_CN:
		case DomainConst.DOMAIN_TYPE_7_CN:
		case DomainConst.DOMAIN_TYPE_8_CN:
		case DomainConst.DOMAIN_TYPE_9_CN:
		case DomainConst.DOMAIN_TYPE_10_CN:
			return "cn";
			
		case DomainConst.DOMAIN_TYPE_1_NET:
		case DomainConst.DOMAIN_TYPE_2_NET:
		case DomainConst.DOMAIN_TYPE_3_NET:
		case DomainConst.DOMAIN_TYPE_4_NET:
		case DomainConst.DOMAIN_TYPE_5_NET:
		case DomainConst.DOMAIN_TYPE_6_NET:
		case DomainConst.DOMAIN_TYPE_7_NET:
		case DomainConst.DOMAIN_TYPE_8_NET:
		case DomainConst.DOMAIN_TYPE_9_NET:
		case DomainConst.DOMAIN_TYPE_10_NET:
			return "net";
		}
		
		return "";
	}
	

	
	static public int getDomainType(String d) {
		int result = DomainConst.DOMAIN_TYPE_NOT_DEF;
		String domain[] = d.split("[.]");
		if (domain.length == 2) {
			if (StringUtil.isNumeric(domain[0])) {
				int prelen = domain[0].length();
				switch (prelen) {
				case 4:
					if (domain[1].toLowerCase().equals("com"))
						result = DomainConst.DOMAIN_TYPE_4_COM;
					else if (domain[1].toLowerCase().equals("cn"))
						result = DomainConst.DOMAIN_TYPE_4_CN;
					else if (domain[1].toLowerCase().equals("net"))
						result = DomainConst.DOMAIN_TYPE_4_NET;
					break;
				case 3:
					if (domain[1].toLowerCase().equals("com"))
						result = DomainConst.DOMAIN_TYPE_3_COM;
					else if (domain[1].toLowerCase().equals("cn"))
						result = DomainConst.DOMAIN_TYPE_3_CN;
					else if (domain[1].toLowerCase().equals("net"))
						result = DomainConst.DOMAIN_TYPE_3_NET;
					break;
				case 2:
					if (domain[1].toLowerCase().equals("com"))
						result = DomainConst.DOMAIN_TYPE_2_COM;
					else if (domain[1].toLowerCase().equals("cn"))
						result = DomainConst.DOMAIN_TYPE_2_CN;
					else if (domain[1].toLowerCase().equals("net"))
						result = DomainConst.DOMAIN_TYPE_2_NET;
					break;
				}
			}
		}
		return result;
	}

	public static String getpreDomain(String domain) {
		int index = domain.indexOf(".");
		return domain.substring(0, index);
	}
	
	public static String getSuffDomin(String d) {
		String domain[] = d.split("[.]");
		if (domain.length==2)
			return domain[1];
		else if (domain.length==3)
			return domain[2];
		return "";
	}
	
	public static void Sleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
		}
	}
}
