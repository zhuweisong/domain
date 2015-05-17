package com.domain.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.domain.DB.DBUpdateProxy;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct;
import com.domain.constvalue.Struct.DomainAuction;
import com.domain.constvalue.Struct.DomainPrice;
import com.domain.constvalue.Struct.DomainWhois;
import com.domain.price.FetcherAuctionPriceFromEname;
import com.domain.price.FetcherAuctionURLFromEname;
import com.domain.price.FetcherIntFromEname;
import com.domain.price.FetcherPriceFrom4N;
import com.domain.whois.FetchWhois;
import com.domain.whois.FetchWhoisBase;

public class controller {

	private DBUpdateProxy mDbUpdateProxy = new DBUpdateProxy();
	FetcherIntFromEname eFetchor = new FetcherIntFromEname();

	void init() {
		mDbUpdateProxy.init();
	}

	void updatePrice4N(int type) {
		FetcherPriceFrom4N fetchor = new FetcherPriceFrom4N();

		int page = 1;
		List<DomainPrice> dpl = null;
		do {
			dpl = fetchor.getAllDominaPrice(type, page);

			if (dpl.size() > 100) {
				mDbUpdateProxy.updateprice(dpl);
				dpl.clear();
			}

			page++;
			DomainUtil.Sleep(1500);
		} while (page <= fetchor.getPage());

		if (dpl.size() > 0) {
			mDbUpdateProxy.updateprice(dpl);
			dpl.clear();
		}
	}

	void updatePriceEname(int type) {
		int i = 0;
		int page = 0;

		do {
			List<DomainPrice> dpl = eFetchor.getAllDominaPrice(type, page * 30);
			if (dpl.size() > 0)
				mDbUpdateProxy.updateprice(dpl);
			page++;
			i = dpl.size();

			DomainUtil.Sleep(2000);
		} while (i >= 30);
	}

	void updateWhois(FetchWhoisBase whoise, int type, int mStart, int mEnd,
			int mStep) {
		int start = mStart;

		do {
			List<DomainWhois> dwl = whoise.getAllWhois(type, start, start
					+ mStep);
			mDbUpdateProxy.updateWhois(dwl, type, start, start + mStep);

			start += mStep;
		} while (start < mEnd);
	}

	void updateAuctionURL(int type) {
		int i = 0;
		int page = 0;
		FetcherAuctionURLFromEname afetchor = new FetcherAuctionURLFromEname();

		do {
			List<DomainAuction> dpl = afetchor
					.getAllAuctionURL(type, page * 30);
			i = dpl.size();
			if (i > 0)
				mDbUpdateProxy.updateBiddingURL(dpl);
			page++;

			// ͬʱ��׼����������ֱ����۸��
			List<DomainPrice> dp2 = new ArrayList<DomainPrice>();
			for (DomainAuction dw : dpl) {
				if (dw.pricetype == DomainConst.DOMAIN_BIN_AUCTION_WITH_PRICE)
					dp2.add(dw);
				;
			}

			mDbUpdateProxy.updateprice(dp2);
			DomainUtil.Sleep(2000);
		} while (i >= 30);
	}

	void updateAuctionPrice(int type) {
		FetcherAuctionPriceFromEname fetch = new FetcherAuctionPriceFromEname();
		Map<String, DomainAuction> da = mDbUpdateProxy.getAuctionURL();
		List<DomainPrice> dpl = fetch.getAutcionPrice(da);
		if (dpl.size() > 0)
			mDbUpdateProxy.updateprice(dpl);
	}

	void updateEmptywhois(int type) {
		Map<String, DomainWhois> dw = mDbUpdateProxy.getEmptyWhois(type,
				DomainUtil.getCurrentSqlDate());
		FetchWhois f = new FetchWhois();
		List<Struct.DomainWhois> dl = f.getAllWhois(dw);
		mDbUpdateProxy.updateWhois(dl, type);
	}

	void updateStatis() {
		mDbUpdateProxy.updateStatis();
	}

	void updatePin(FetchWhoisBase f) {
		Map<String, DomainPrice> dw = mDbUpdateProxy.getPrice(
				DomainConst.DOMAIN_TYPE_PIN, DomainConst.DOMAIN_QQ_BID,
				DomainUtil.getCurrentSqlDate());

		List<Struct.DomainWhois> dl = f.getAllWhois(dw,
				DomainConst.DOMAIN_TYPE_PIN);
		mDbUpdateProxy.updateWhois(dl, DomainConst.DOMAIN_TYPE_PIN);
	}


}
