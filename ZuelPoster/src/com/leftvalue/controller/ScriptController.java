package com.leftvalue.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.leftvalue.core.HtmlLoader;
import com.leftvalue.core.ScriptRunner;
import com.leftvalue.model.Script;
import com.leftvalue.returnValue.ReturnValue;
import com.leftvalue.script.BaseScript;
import com.leftvalue.script.NormalRequest;

/**
 * 
 * @author linxi 脚本控制器,包括但不限于 脚本的增删改查(参考github的权限管理模式?待考虑) 查看已有脚本
 *         接收消息决定运行哪个脚本,command pattern
 * 
 */
public class ScriptController extends Controller {
	/**
	 * 脚本新建 我们在这里需要归纳常见的任务所需要的参数 包括但不限于 URL request parameter request method :
	 * post or get 另外为了确保整体项目的可拓展性 考虑到未来可能进行更精细化的脚本划分 我们增加一个脚本类型参量:script_type
	 */
	public void add() {
		String script_type = getPara("script_type");
		// 允许匿名用户上传脚本,但必须在数据库内保留上传者的学号,以便进行删除以及修改操作时鉴权
		String author = getPara("author");
		String number = getCookie("number");

		String script_name = getPara("script_name");
		String script_description = getPara("script_description");
		if (script_type == null || script_type.equals("")) {
			renderJson(ReturnValue.std_fail("未定义脚本类型 ><"));
		} else if (script_type.equals("NormalRequest")) {// 标准模式,URL+REQUEST
			// METHOD+PARAMETER,
			String url = getPara("url");
			String method = getPara("method");
			String[] parameter = getParaValues("parameter");
			String regex = getPara("regex");// 对模拟请求结果进行查找处理的regular expression
			BaseScript script = new NormalRequest(author, number, script_name, script_description, url, method,
					parameter, regex);
			/**
			 * 为了确保上传脚本的质量,必须对其可用性进行确认
			 */
			JSONObject test = ScriptRunner.run(getCookie("JSESSIONID"), url, method, parameter, regex);
			if (test.getString("state").equals("success")) {
				// 可用脚本
				boolean ifSuccess = script.store();
				if (ifSuccess) {
					renderJson(ReturnValue.std_success("上传成功!"));
				} else {
					renderJson(ReturnValue.std_fail("上传失败 ><"));
				}
			} else {
				// 脚本有问题,拒绝上传
				renderJson(ReturnValue.std_fail("脚本有问题,服务器已拒绝您的上传,请检查无误后重试"));
			}
		}
	}

	/**
	 * 删除脚本 只有上传者本人有权限
	 */
	public void del() {
		int sid = getParaToInt("sid");
		// int len = Db.find("SELECT * FROM script where number='" +
		// getCookie("number") + "'").size();
		int effect = Db.update("DELETE FROM script WHERE sid=" + sid + " AND number='" + getCookie("number") + "'");
		if (effect == 0) {
			renderJson(ReturnValue.std_fail("不是创建者,无此项权限"));
		} else {
			renderJson(ReturnValue.std_success("操作成功,影响了" + effect + "行数据"));
		}
	}

	/**
	 * 列出所有可用脚本
	 */
	public void list() {
		String result = JSON.toJSONString(com.leftvalue.model.Script.DAO.find("select * from script"));// 后期可能增加新的script类型,所以这里留出了扩展空间
		renderJson(result);
	}

	/**
	 * 网页加载
	 */
	public void loadHTML() {
		String url = getPara("url");
		String[] parameter = getParaValues("parameter");
		String method = getPara("method");
		String JSESSIONID = getCookie("JSESSIONID");
		String result = HtmlLoader.load(JSESSIONID, url, method, parameter);
		renderText(result);
	}

	/**
	 * 列出'我'创建的所有脚本,以便进行删除删改操作
	 */
	public void my() {
		String result = JSON
				.toJSONString(Script.DAO.find("select * from script where number='" + getCookie("number") + "'"));
		renderJson(result);
	}

	/**
	 * 运行已有脚本 一次运行一个就好
	 */
	public void run() {
		int sid = getParaToInt("sid");
		Script mn = Script.DAO.findById(sid);
		if (mn != null) {
			BaseScript script = mn.convert();
			String JSESSIONID = getCookie("JSESSIONID");
			renderJson(JSON.toJSONString(script.execute(JSESSIONID)));
		} else {
			renderJson(ReturnValue.std_fail("找不到指定脚本"));
		}
	}

	/**
	 * 临时脚本测试
	 */
	public void test() {
		String url = getPara("url");
		String[] parameter = getParaValues("parameter");
		String method = getPara("method");
		String regex = getPara("regex");
		String JSESSIONID = getCookie("JSESSIONID");
		JSONObject result = ScriptRunner.run(JSESSIONID, url, method, parameter, regex);
		renderJson(JSON.toJSONString(result));
	}
}
