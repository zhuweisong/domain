package com.domain.pinyin;

import java.util.List;

import com.domain.price.VistorBase;

public class vistorpinyin extends VistorBase  {
	List<String> l = null;
	public vistorpinyin(List<String> d) {
		super(0, 0);
		super.tail = "dt";
		this.l = d;
	}
	
	public void compose() {
		String words[] = accum.toString().split(" ");
		int index = accum.indexOf("±Ê»­Êý");
		if (accum.length()> 4 && index>0) {
			for (int i=0;i<index;i++) {
				String e = accum.substring(i, i+1);
				l.add(e);
			}
		}
	}
}
