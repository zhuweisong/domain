/*
 * This class is for determine whether data is insert/update to DB 
 *
 */

package com.domain.DB;

import java.util.List;
import java.util.Map;

import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.Struct.DomainPrice;

public class DomainPriceUpdateStrategy {

	DomainPriceUpdateStrategy() {
		super();
	}

	public void MergePrice(List<DomainPrice> dpl, Map<String, DomainPrice> dp2) {

		for (DomainPrice dpA : dpl) {
			String domain = dpA.domain;
			DomainPrice dpB = dp2.get(domain);

			// there is no this domain in DB
			if (dpB == null) {
				dpA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
				continue;
			}

			if (dpA.pricetype > dpB.pricetype)
				MergePriceAMorethanB(dpA, dpB);
			else if (dpA.pricetype == dpB.pricetype)
				MergePriceAEqualB(dpA, dpB);
			else
				MergePriceALessB(dpA, dpB);
		}
	}

	private void MergePriceAEqualB(DomainPrice dpA, DomainPrice dpB) {

		if (DomainUtil.dateEquals(dpA.priceDate, dpB.priceDate)) {
			// on the same day, price is the min
			if (dpA.price < dpB.price)
				dpA.op = DomainConst.DOMAIN_PRICE_OPRATOR_UPDATE;
		} else
			// not on the same day, insert a record
			dpA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
	}

	private void MergePriceAMorethanB(DomainPrice dpA, DomainPrice dpB) {

		// on the same day
		if (DomainUtil.dateEquals(dpA.priceDate, dpB.priceDate)) //
			dpA.op = DomainConst.DOMAIN_PRICE_OPRATOR_UPDATE;
		else
			// not on the same day, insert a record
			dpA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
	}

	private void MergePriceALessB(DomainPrice dpA, DomainPrice dpB) {
		if (!DomainUtil.dateEquals(dpA.priceDate, dpB.priceDate)) //
			dpA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
	}
}
