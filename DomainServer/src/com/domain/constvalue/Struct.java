package com.domain.constvalue;

public class Struct {

	// /-------------------price-------------------
	public static class DomainPrice {
		public String domain;
		public int type;
		public int price;
		public int pricetype;
		public int fromWeb;
		public java.sql.Date priceDate;
		public int op;

		public DomainPrice(String domain, int mtype, int mprice,
				int mpricetype, int mfromWeb, java.sql.Date mpriceDate) {
			super();
			this.domain = domain;
			this.type = mtype;
			this.price = mprice;
			this.pricetype = mpricetype;
			this.fromWeb = mfromWeb;
			this.priceDate = mpriceDate;
			op = DomainConst.DOMAIN_PRICE_OPRATOR_NOTHING;
		}

		public Boolean equals(DomainPrice dw) {
			return this.domain.equals(dw.domain)
					&& this.type== dw.type
					&& this.price == dw.price;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(domain);
			sb.append("|");
			sb.append(type);
			sb.append("|");
			sb.append(price);
			sb.append("|");
			sb.append(pricetype);
			sb.append("|");
			sb.append(fromWeb);
			sb.append("|");
			sb.append(priceDate);

			return sb.toString();
		}
	}

	// /-------------------price-------------------
	public static class DomainAuction extends DomainPrice {

		public java.sql.Time AuctionEnd;
		public String AuctionUrl;

		public DomainAuction(String domain, int mtype, int mprice,
				int mpricetype, int mfromWeb, java.sql.Date mpriceDate,
				String BiddingUrl, java.sql.Time BiddingEnd) {
			super(domain, mtype, mprice, mpricetype, mfromWeb, mpriceDate);
			this.AuctionEnd = BiddingEnd;
			this.AuctionUrl = BiddingUrl;
		}

		public String toString() {
			return super.toString() + "|"+ AuctionUrl + "|" + AuctionEnd;
		}
		
		public Boolean equals(DomainAuction dw) {
			return super.equals(dw) 
					&& this.AuctionUrl.equals(dw.AuctionUrl);
		}
	}

	// /-------------------whois-------------------
	public static class DomainWhois {
		public String domain;
		public int type;
		public String reg_email;
		public String reg_name;
		public String reg_phone;
		public String reg_URL;
		public String reg_country;
		public java.sql.Date reg_date;
		public int op; // 可进行的操作

		public DomainWhois(String domain, int type, String reg_email,
				String reg_name, String reg_phone, String reg_URL,
				String reg_country, java.sql.Date regDate) {
			super();
			this.domain = domain;
			this.type = type;
			this.reg_email = reg_email;
			this.reg_name = reg_name;
			this.reg_phone = reg_phone;
			this.reg_URL = reg_URL;
			this.reg_country = reg_country;
			this.reg_date = regDate;
			this.op = DomainConst.DOMAIN_PRICE_OPRATOR_NOTHING;
		}

		public Boolean equals(DomainWhois dw) {
			Boolean res = false;
			if (!this.reg_email.isEmpty() && !this.reg_name.isEmpty()) {
				res = this.domain.equals(dw.domain) &&
					(this.reg_email.equals(dw.reg_email) || this.reg_name.equals(dw.reg_name));
			}
			else {
				res = this.domain.equals(dw.domain) &&
						(this.reg_email.equals(dw.reg_email) && this.reg_name.equals(dw.reg_name));
			}
//			if (!this.domain.equals("1601.com")) { //1601的用户名特殊
//				res = res && this.reg_name.equals(dw.reg_name);
//			}
			return res;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(domain);
			sb.append("|");
			sb.append(type);
			sb.append("|");
			sb.append(reg_email);
			sb.append("|");
			sb.append(reg_name);
			sb.append("|");
			sb.append(reg_phone);
			sb.append("|");
			sb.append(reg_URL);
			sb.append("|");
			sb.append(reg_country);
			sb.append("|");
			sb.append(reg_date);

			return sb.toString();
		}
	}
}
