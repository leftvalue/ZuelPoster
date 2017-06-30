package com.leftvalue.model;


import com.jfinal.plugin.activerecord.Model;
import com.leftvalue.script.BaseScript;
import com.leftvalue.script.NormalRequest;

public class Script extends Model<Script> {
	private static final long serialVersionUID = 8534282594223276727L;
	public static Script DAO = new Script();

	public BaseScript convert() {
		String type = this.getType();
		if (type.equals("NormalRequest")) {
			return this.convertToNormalRequest();
		} else {
			System.out.println("未定义脚本类型[这里不应该发生的]");
			return null;
		}
	}

	public com.leftvalue.script.NormalRequest convertToNormalRequest() {
		NormalRequest temp = new NormalRequest();
		temp.setAuthor(this.getStr("author"));
		temp.setMethod(this.getStr("method"));
		temp.setParameter(this.getStr("parameter") != null ? this.getStr("parameter").split("###") : null);
		temp.setRegex(this.getStr("regex"));
		temp.setScript_description(this.getStr("script_description"));
		temp.setScript_name(this.getStr("script_name"));
		temp.setUrl(this.getStr("url"));
		// temp.setPushTime(LocalDateTime.parse(this.getTime("pushtime").toString()));
		// 这里大概会报错吧,有网了再测数据库的返回值
		temp.setNumber(this.getStr("number"));
		return temp;
	}

	public String getAuthor() {
		return this.getStr("author");
	}

	public int getSid() {
		return this.getInt("sid");
	}

	public String getPushTime() {
		java.sql.Timestamp time = this.getTimestamp("pushtime");
		System.out.println(time);
		return time.toString();
	}

	public String getUrl() {
		return this.getStr("url");
	}

	public String[] getParameter() {
		return this.getStr("parameter") != null ? this.getStr("parameter").split("###") : null;
	}

	public String getMethod() {
		return this.getStr("method");
	}

	public String getScript_name() {
		return this.getStr("script_name");
	}

	public String getScript_description() {
		return this.getStr("script_description");
	}

	public String getRegex() {
		return this.getStr("regex");
	}

	public String getType() {
		return this.getStr("type");
	}
}
