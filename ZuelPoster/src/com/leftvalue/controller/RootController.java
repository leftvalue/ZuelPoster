package com.leftvalue.controller;


import com.jfinal.core.Controller;

public class RootController extends Controller {
	public void index() {
		render("index.html");// 配置默认页面
	}
}
