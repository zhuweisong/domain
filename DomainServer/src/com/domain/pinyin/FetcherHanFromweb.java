package com.domain.pinyin;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;


import com.domain.price.FetcherBase;


public class FetcherHanFromweb extends FetcherBase {
	static private final String preURL = "http://zd.diyifanwen.com/zidian/py/";
	List<String> dpl = new ArrayList<String>();
	
	public List<String> getAllDominaPrice(String pin) {

		String url = preURL+pin+".htm";
//		System.out.println(url);
		Boolean res = connect(url);
		if (res) {
			Element es = doc.select("dl.wordlist").first();

			vistorpinyin formatter = new vistorpinyin(dpl);
			NodeTraversor traversor = new NodeTraversor(formatter);
			traversor.traverse(es);
		}
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		return dpl;
	}
}
