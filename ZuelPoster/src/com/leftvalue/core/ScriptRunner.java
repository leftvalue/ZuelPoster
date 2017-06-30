package com.leftvalue.core;

import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import com.alibaba.fastjson.JSONObject;
import com.leftvalue.tools.HTML;

public class ScriptRunner {
	private static final int maxTime = 10_000;

	public static JSONObject run(String JSESSIONID, String url, String method, String[] paras, String regex) {
		long start = System.currentTimeMillis();
		Method m = null;
		if (method == null) {
			m = Method.GET;
		} else if (method.equals("post")) {
			m = Method.POST;
		} else {
			m = Method.GET;
		}
		JSONObject object = new JSONObject();
		String result = "";
		try {
			Document doc = null;
			if (paras == null || paras.length == 0) {
				doc = Jsoup.connect(url).method(m).cookie("JSESSIONID", JSESSIONID).timeout(maxTime).execute().parse();
			} else {
				Map<String, String> datas = new HashMap<>();
				for (int i = 0; i < paras.length; i += 2) {
					datas.put(paras[i], paras[i + 1]);// [键0,值0,键1,值1]->{[键0,值0],[键1,值1]}
				}
				doc = Jsoup.connect(url).data(datas).method(m).cookie("JSESSIONID", JSESSIONID).timeout(maxTime)
						.execute().parse();
			}

			if (regex == null || "".equals(regex)) {
				// result = Jsoup.clean(doc.html(), Whitelist.basic());//
				// 这里的过滤级别有待商榷,另外是否需要设为用户可选?
				result = doc.html();
				result = HTML.parse(result);
			} else {
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(doc.html());
				int i = 0;
				while (matcher.find()) {
					result += matcher.group(i) + "\n";// 查找并拼接所有匹配结果
					i++;
				}
			}
			object.put("state", "success");// 处理状态
		} catch (Exception e) {
			e.printStackTrace();
			object.put("state", "fail");
			result = "" + e;
		}
		object.put("result", result);// 处理结果
		object.put("time", System.currentTimeMillis() - start);// 处理时间
		return object;
	}
}
