package com.leftvalue.tools;

import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class HTML {
	public static String beauty(String html) {
		// String temp = Jsoup.clean(html, Whitelist.relaxed());
		String temp = html;
		Document document = null;
		StringWriter writer = null;
		HTMLWriter htmlWriter = null;
		try {
			if (System.currentTimeMillis() > 1) {
				throw new Exception("这是特意抛出的错误,暂时忽略html格式化模块");
			}
			System.out.println(temp);
			document = DocumentHelper.parseText(temp);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			writer = new StringWriter();
			htmlWriter = new HTMLWriter(writer, format);
			htmlWriter.write(document);
			temp = writer.toString();
			htmlWriter.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return temp;
	}

	public static String parse(String html) {
		String temp = html;
		temp = beauty(html);
		temp = temp.replace("<", "&lt;");
		temp = temp.replace(">", "&gt;");
		return temp;
	}

	public static void main(String[] args) throws Exception {
		String html = "<div class=\"layui-form-item\"><label class=\"layui-form-label\">密码</label><div class=\"layui-input-inline\"><input type=\"password\" id=\"password\" requiredlay-verify=\"required\" placeholder=\"请输入密码\" autocomplete=\"off\" class=\"layui-input\"/></div></div>";
		System.out.println(html + "\n--------------------------");
		System.out.println(beauty(html));
	}
}
