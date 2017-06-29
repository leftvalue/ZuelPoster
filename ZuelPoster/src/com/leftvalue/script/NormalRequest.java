package com.leftvalue.script;

import com.alibaba.fastjson.JSONObject;
import com.leftvalue.core.ScriptRunner;
import com.leftvalue.model.Script;

public class NormalRequest extends BaseScript {
	private String url;
	private String method;
	private String regex;
	private String[] parameter;

	public NormalRequest() {
		super();
	}

	public NormalRequest(String author, String number, String script_name, String script_description, String url,
			String method, String[] parameter, String regex) {
		super(author, number, script_name, script_description);
		this.url = url;
		this.method = method;
		this.parameter = parameter;
		this.regex = regex;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String[] getParameter() {
		return parameter;
	}

	public void setParameter(String[] parameter) {
		this.parameter = parameter;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public boolean store() {
		Script temp = new Script();
		temp.set("type", "NormalRequest");
		temp.set("author", super.getAuthor());
		temp.set("number", super.getNumber());
		temp.set("script_name", super.getScript_name());
		temp.set("script_description", super.getScript_description());
		temp.set("pushtime", super.getPushTime().toString());// ?
		temp.set("url", url);
		temp.set("method", method);
		String paras = "";
		for (String str : parameter) {
			paras += str + "###";
		}
		temp.set("parameter", paras);
		temp.set("regex", regex);
		temp.save();
		return true;
	}

	@Override
	public JSONObject execute(String JSESSIONID) {
		return ScriptRunner.run(JSESSIONID, url, method, parameter, regex);
	}

}
