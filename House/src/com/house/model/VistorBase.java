package com.house.model;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;



public class VistorBase implements NodeVisitor{

	public StringBuilder accum = new StringBuilder(); // holds the accumulated

	public String tail = "tr";
	
	protected VistorBase(int mtype) {
		super();
	}
	
	@Override
	public void head(Node node, int arg1) {
		//
		if (node instanceof TextNode) {
			String txt = ((TextNode) node).text();
			append(txt); // TextNodes carry all user-readable text in the DOM.
		}
	}

	@Override
	public void tail(Node node, int arg1) {
		String name = node.nodeName();
		if (name.equals(tail)) {
			if (accum.length()>1)
				compose();
			accum.setLength(0);
		}
        else if (name.equals("a"))
            append(String.format(" <%s>", node.absUrl("href")));
	}
	
	public void compose() {
		return;
	}

	// appends text to the string builder with a simple word wrap method
	private void append(String text) {

		if (text.equals(" ")
				&& (accum.length() == 0 || StringUtil.in(
						accum.substring(accum.length() - 1), " ", "\n")))
			return; // don't accumulate long runs of empty spaces

		if (text.length() > 1)
			text = text.replace(" ", "").replace("\n", "");

		accum.append(text);
	}
}
