package com.domain.control;

import java.io.IOException;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.price.FetcherPriceFrom4N;
import com.domain.whois.FetchWhois;
import com.domain.whois.FetchWhoisFromEname;
import com.domain.whois.FetchWhoisFrompheenix;

public class Domain {

	private final static int START = 0;
	
	static final int ACTION_WHOIS_ALL = 1;
	static final int ACTION_PRICE_NORMAL = 2;
	static final int ACTION_AUCTION_URL = 4;
	static final int ACTION_AUCTION_PRICE = 8;
	static final int ACTION_STATIS = 16;
	static final int ACTION_PHEENIX = 32;

	public static void main(String[] args) throws IOException {

		System.out.println("-----start------");

		int ActionFlag = 0;

		for (int i = 0; i < args.length; i++) {
			System.out.println("parsing arg nr. " + i + ": '" + args[i] + "'");
			if (args[i].length() == 0) {
				System.out.println("EMPTY ARG!");
				return;
			}

			if (args[i].charAt(0) == '-') {
				if (args[i].length() != 2) {
					System.out.println("invalid argument: " + args[i] + "'");
					return;
				}

				switch (args[i].charAt(1)) {
				case '-':
					break;

				case 'a': //
					ActionFlag |= ACTION_AUCTION_PRICE;
					break;

				case 'p': //
					ActionFlag |= ACTION_PRICE_NORMAL;
					break;

				case 'w': // whois
					ActionFlag |= ACTION_WHOIS_ALL;
					break;

				case 'u':// Autcion URL
					ActionFlag |= ACTION_AUCTION_URL;
					break;

				case 's':// Autcion URL
					ActionFlag |= ACTION_STATIS;
					break;

				case 'n':// Autcion URL
					ActionFlag |= ACTION_PHEENIX;
					break;
				}
			}
		}

		controller con = new controller();
		con.init();

		// con.updateHan();

//		FetchWhoisFromPhenixxRunnable wh = new FetchWhoisFromPhenixxRunnable(con, ActionFlag);
//		new Thread(wh, "phineex").start();

//		FetchWhoisFromEnameRunable er = new FetchWhoisFromEnameRunable(con,
//				ActionFlag);
//		new Thread(er, "ename").start();

		FetchWhois whoise = new FetchWhois();

		if ((ActionFlag & ACTION_AUCTION_PRICE) > 0) {
			System.out.println("-----PRICE 1------"
					+ DomainUtil.getSystemTime());
			con.updateAuctionPrice(0);
		}

//		if ((ActionFlag & ACTION_WHOIS_ALL) > 0) {
			System.out.println("-----WHOIS_ALL 2------"
					+ DomainUtil.getSystemTime());
			con.updateWhois(whoise, DomainConst.DOMAIN_TYPE_2_COM, 0, 100,
					100);
			con.updateWhois(whoise, DomainConst.DOMAIN_TYPE_3_COM, 323,
					1000, 500);
//			con.updateWhois(whoise, DomainConst.DOMAIN_TYPE_4_COM, 0,
//					10000, 500);
//		}

		if ((ActionFlag & ACTION_AUCTION_URL) > 0) {
			System.out.println("-----AUCTION_URL 1------"
					+ DomainUtil.getSystemTime());
			con.updateAuctionURL(DomainConst.DOMAIN_TYPE_4_COM);
			con.updateAuctionURL(DomainConst.DOMAIN_TYPE_4_CN);
			con.updateAuctionURL(DomainConst.DOMAIN_TYPE_3_COM);
			con.updateAuctionURL(DomainConst.DOMAIN_TYPE_3_CN);
		}

		if ((ActionFlag & ACTION_PRICE_NORMAL) > 0) {
			System.out.println("-----PRICE_NORMAL 1------"
					+ DomainUtil.getSystemTime());

			con.updatePriceEname(DomainConst.DOMAIN_TYPE_3_CN);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_3_COM);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_3_NET);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_4_CN);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_4_COM);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_4_NET);

			con.updatePrice4N(FetcherPriceFrom4N.PRICE_PART);
		}

		if ((ActionFlag & ACTION_STATIS) > 0) {
			System.out.println("-----STATIS ------"
					+ DomainUtil.getSystemTime());
			con.updateStatis();
		}

		if ((ActionFlag & ACTION_WHOIS_ALL) > 0) {
			System.out.println("-----WHOIS_ALL 2------"
					+ DomainUtil.getSystemTime());
			
			con.updateWhois(whoise, DomainConst.DOMAIN_TYPE_2_CN, START, 100,
					100);
			con.updateWhois(whoise, DomainConst.DOMAIN_TYPE_3_CN, START, 1000,
					500);
			con.updateWhois(whoise, DomainConst.DOMAIN_TYPE_4_CN, START, 10000,
					500);
			con.updatePin(whoise);
		}
		
		if ((ActionFlag & ACTION_PRICE_NORMAL) > 0) {
			System.out.println("-----PRICE_NORMAL 2------"
					+ DomainUtil.getSystemTime());
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_3_CN);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_3_COM);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_3_NET);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_4_CN);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_4_COM);
			con.updatePriceEname(DomainConst.DOMAIN_TYPE_4_NET);

			con.updatePrice4N(FetcherPriceFrom4N.PRICE_ALL);
		}

		System.out.println("-----end------");
	}


	static class FetchWhoisFromEnameRunable implements Runnable {
		controller con;
		int tag;

		public FetchWhoisFromEnameRunable(controller con, int tag) {
			super();
			this.con = con;
			this.tag = tag;
		}

		@Override
		public void run() {

			if ((tag & ACTION_WHOIS_ALL) > 0) {
				System.out.println("-----WHOIS_ALL 3------"
						+ DomainUtil.getSystemTime());
				FetchWhoisFromEname fetchor = new FetchWhoisFromEname();
				con.updateWhois(fetchor, DomainConst.DOMAIN_TYPE_2_COM, 0, 100,
						100);
				con.updateWhois(fetchor, DomainConst.DOMAIN_TYPE_3_COM, 0,
						1000, 500);
				con.updateWhois(fetchor, DomainConst.DOMAIN_TYPE_4_COM, 0,
						10000, 500);
			}
		}
	}

	static class FetchWhoisFromPhenixxRunnable implements Runnable {
		controller con;
		int tag;

		public FetchWhoisFromPhenixxRunnable(controller con, int tag) {
			super();
			this.con = con;
			this.tag = tag;
		}

		@Override
		public void run() {

			if ((tag & ACTION_WHOIS_ALL) > 0) {
				System.out.println("-----WHOIS_ALL 3------"
						+ DomainUtil.getSystemTime());
				FetchWhoisFrompheenix fetchor = new FetchWhoisFrompheenix();

				con.updateWhois(fetchor, DomainConst.DOMAIN_TYPE_2_NET, 0, 100,
						100);
				con.updateWhois(fetchor, DomainConst.DOMAIN_TYPE_3_NET, 0,
						1000, 500);
				con.updateWhois(fetchor, DomainConst.DOMAIN_TYPE_4_NET, 0,
						10000, 500);

				con.updateEmptywhois(DomainConst.DOMAIN_TYPE_2_NET);
				con.updateEmptywhois(DomainConst.DOMAIN_TYPE_3_NET);
				con.updateEmptywhois(DomainConst.DOMAIN_TYPE_4_NET);
			}
		}
	}
}
