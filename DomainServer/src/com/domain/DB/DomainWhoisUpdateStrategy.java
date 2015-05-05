package com.domain.DB;

import java.util.List;
import java.util.Map;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct.DomainWhois;

public class DomainWhoisUpdateStrategy {

	public void Merge(List<DomainWhois> dwl, Map<String, DomainWhois> dw2) {
		// TODO Auto-generated method stub

		for (DomainWhois dA : dwl) {
			DomainWhois dB = dw2.get(dA.domain);
			if (dB==null) {
				dA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
				continue;
			}
			if (!dA.equals(dB)) {
				if (DomainUtil.dateEquals(dA.reg_date, dB.reg_date)) {
					dA.op = DomainConst.DOMAIN_PRICE_OPRATOR_UPDATE;
				}
				else 
					dA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
			}
		}
	}
	
	
	public void MergeTest(List<DomainWhois> dwl, Map<String, DomainWhois> dw2) {
		// TODO Auto-generated method stub

		for (DomainWhois dA : dwl) {
			DomainWhois dB = dw2.get(dA.domain);

			if (dB==null) {
				dA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
				continue;
			}
			System.out.println(dA.domain+ "|" + dB.domain);
			if (dA.equals(dB) && !DomainUtil.dateEquals(dA.reg_date, dB.reg_date) ) {
				//System.out.println(dA.domain+ " Merge, dA.equals(dB)");
				dA.reg_country = dB.reg_country;
				dA.reg_phone = dB.reg_phone;
				dA.reg_URL = dB.reg_URL;
				
				dA.op = DomainConst.DOMAIN_PRICE_OPRATOR_INSERT;
				dB.op = DomainConst.DOMAIN_PRICE_OPRATOR_DELETE;
			}
		}
	}
}
