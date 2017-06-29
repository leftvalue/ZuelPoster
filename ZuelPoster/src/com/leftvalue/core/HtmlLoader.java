package com.leftvalue.core;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;

public class HtmlLoader {
	private static final int maxTime = 10_000;

	public static String load(String JSESSIONID, String url, String method, String[] paras) {
		String result = "";
		try {
			Method m = null;
			if (method.equals("post")) {
				m = Method.POST;
			} else {
				m = Method.GET;
			}
			Document doc = Jsoup.connect(url).method(m).cookie("JSESSIONID", JSESSIONID).timeout(maxTime).execute()
					.parse();
			result = doc.html();
			// result.replace("<", "&lt");
			// result.replace(">", "&gt");
		} catch (Exception e) {
			result = e.toString();
		}
		return result;
	}
}
