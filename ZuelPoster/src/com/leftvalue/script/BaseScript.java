package com.leftvalue.script;

import java.time.LocalDateTime;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseScript {
	private String author;

	private String number;

	private LocalDateTime pushTime;

	private String script_name;

	private String script_description;

	public BaseScript() {
		pushTime = LocalDateTime.now();
	}

	public BaseScript(String author, String number, String script_name, String script_description) {
		super();
		pushTime = LocalDateTime.now();
		this.author = author;
		this.number = number;
		this.script_name = script_name;
		this.script_description = script_description;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	// 存储脚本
	public abstract boolean store();

	// 运行
	public abstract JSONObject execute(String JSESSIONID);

	public void setPushTime(LocalDateTime pushTime) {
		this.pushTime = pushTime;
	}

	public String getAuthor() {
		return author;
	}

	public LocalDateTime getPushTime() {
		return pushTime;
	}

	public String getScript_description() {
		return script_description;
	}

	public String getScript_name() {
		return script_name;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setScript_description(String script_description) {
		this.script_description = script_description;
	}

	public void setScript_name(String script_name) {
		this.script_name = script_name;
	}

}
