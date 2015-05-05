package com.domain.price;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetcherBase {
	protected Document doc = null;
	
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
