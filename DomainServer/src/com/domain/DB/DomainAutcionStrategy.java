package com.domain.DB;

import java.util.List;
import java.util.Map;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct.DomainAuction;


public class DomainAutcionStrategy {

	public void MergePrice(List<DomainAuction> dpl,
			Map<String, DomainAuction> dp2) {
		
		for (DomainAuction dA : dpl) {
			DomainAuction dB = dp2.get(dA.domain);
			if (dB==null) {
				dA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
				continue;
			}
			if (!dA.equals(dB)) {
				if (DomainUtil.dateEquals(dA.priceDate, dB.priceDate)) {
					dA.op = DomainConst.DOMAIN_PRICE_OPRATOR_UPDATE;
				}
				else 
					dA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
			}
		}
	}
}
