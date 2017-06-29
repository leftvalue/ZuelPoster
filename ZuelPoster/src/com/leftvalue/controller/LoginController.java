package com.leftvalue.controller;

import java.io.File;

import javax.servlet.http.Cookie;

import com.jfinal.core.Controller;

import top.leftvalue.login.Core;

public class LoginController extends Controller {
	public void index() {
		// 自动模拟访问一次教务部，拿JSESSIONID写入用户cookie
		String jsessionid = Core.getJsessionID();
		if (jsessionid != null) {
			getResponse().addCookie(new Cookie("JSESSIONID", jsessionid));
			renderText(jsessionid);
		} else {
			renderText("0");
		}
	}

	public void img() {
		// 根据上面拿的cookie中的JSESSIONID，get一次验证码
		// 返回给前端图片
		String jsessionid = getCookie("JSESSIONID");
		if (jsessionid == null) {
			renderText("0");
		} else {
			String path = Core.getIMG_Addr(jsessionid);
			if (path != null) {
				renderFile(new File(path + ".jpg"));
			} else {
				renderText("0");
			}
		}
	}

	public void login() {
		// 用户名+密码进行加密，
		/*
		 * 附带验证码，请求登录教务系统 如果登录失败，重定向到当前页面
		 */
		String number = getPara("number");
		String encoded = getPara("encoded");
		String xcode = getPara("xcode");
		String jsessionid = getCookie("JSESSIONID");
		if (encoded == null || "".equals(encoded) || xcode == null || "".equals(xcode) || jsessionid == null
				|| "".equals(jsessionid)) {
			renderText("-1");
		} else {
			try {
				String username = Core.login(encoded, xcode, jsessionid);
				setCookie("JSESSIONID", jsessionid, 3600);
				setCookie("username", username, 3600);
				setCookie("number",number,3600);
				renderJson(username);
			} catch (Exception e) {
				System.err.println("~登录失败");
				renderText("-1");
			}
		}
	}
}
