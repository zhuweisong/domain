package com.house.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.house.constvalue.DataStruct.DealInfoSecondHand;



public abstract class Fetcher {
	protected Document doc = null;
	
	protected abstract String getURL();
	
	protected abstract List<DealInfoSecondHand> getInfoBySecondHand();
	
	
	protected Boolean connect(String url) {
		Boolean res = true;
		try {
			Connection con = Jsoup.connect(url);
			con.timeout(8000);
			doc = con.get();
		} catch (MalformedURLException e1) {
			res = false;
			e1.printStackTrace();
		} catch (IOException e1) {
			res = false;
			e1.printStackTrace();
		}
		return res && doc != null;
	}
}
