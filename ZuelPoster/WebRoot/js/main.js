/**
 * 主页js
 */
var dt = null;// script table数据源
var scripts = null;// 当前使用脚本
var ifLogin = false;
var initPreview = function(data) {
	var html = data;
	html = html.replace(/&lt;/g, "<");
	html = html.replace(/&gt;/g, ">");
	html = html.replace(
			"Copyright (C) 湖南强智科技发展有限公司 All Rights Reserved 湘ICP 备12010071号 ",
			"");
	$("#preview").html(html);
}
var getIMG = function() {
	$.ajax({
		url : "login",
		async : false,
		type : "post",
		success : function(data) {
			if (data != "0") {
				$("#jpg").attr("src",
						"login/img?" + new Date().getSeconds().toString());
			}
		}
	});
}
var runScript = function(sid) {
	if (ifLogin) {
		document.getElementById("htmlShower").className = 'html';// 先消除原有的class
		$.ajax({
			url : "script/run",
			async : true,
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			type : "post",
			data : {
				sid : sid
			},
			success : function(data) {
				console.log("耗时" + data.time + "毫秒, " + data.state);
				$("#htmlShower").html("" + data.result);
				initPreview(data.result);
				$('pre code').each(function(i, block) {
					hljs.highlightBlock(block);
				});
			}
		});
	} else {
		layer.msg("请先登录以获取相关权限!");
	}
}
var delScript = function(sid) {
	if (ifLogin) {
		$.ajax({
			url : "script/del",
			async : true,
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			type : "post",
			data : {
				sid : sid
			},
			success : function(data) {
				if (data.state == "-1") {
					layer.msg(data.info);
				} else if (data.state == "1") {
					layer.msg(data.info);
					listScripts();
				}
			}
		});
	} else {
		layer.msg("请先登录以获取相关权限!");
	}
}
var listScripts = function() {
	$
			.ajax({
				url : "script/list",
				async : false,// 同步ajax
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				type : "post",
				success : function(data) {
					if (data.state == "-1") {
						layer.msg(data.info, function() {
							// 关闭后的操作
						});
					} else {
						scripts = data;
						dt = dt = $("#script_table")
								.DataTable(
										{
											"filter" : false,
											"destroy" : true,// 为销毁上次的
											"paging" : true,
											"info" : true,
											"searching" : true,
											// "ordering" : true,
											"data" : scripts,
											"columns" : [ {
												"data" : "sid"
											}, {
												"data" : "script_name"
											}, {
												"data" : "script_description"
											}, {
												"data" : "author"
											}, {
												"data" : "pushTime"
											}, {
												"data" : "sid"
											} ],
											"columnDefs" : [ {
												"render" : function(data, type,
														row) {
													return "<button onclick=\"runScript('"
															+ row.sid
															+ "')\">运行</button>&nbsp;<button onclick=\"delScript('"
															+ row.sid
															+ "')\">删除</button>";
												},
												"targets" : 5
											} ]
										});
					}
				}
			});
}
$(document)
		.ready(
				function() {
					var info;
					listScripts();
					$("#login")
							.click(
									function() {
										var account = encodeInp($("#username")
												.val());
										var passwd = encodeInp($("#password")
												.val());
										var encoded_ = account + "%%%" + passwd;
										$
												.ajax({
													url : "login/login",
													async : false,// 同步ajax
													contentType : "application/x-www-form-urlencoded; charset=utf-8",
													type : "post",
													data : {
														encoded : encoded_,
														xcode : $("#xcode")
																.val(),
														number : $("#username")
																.val()
													},
													success : function(data) {
														if (data == "-1") {
															layer
																	.msg("验证码或密码错误");
															getIMG();
														} else {
															ifLogin = true;//标记登录完成
															info = data.info;
															$("#beforelogin").hide();
															$("#afterlogin").show();
															$("#info").html(
																	"" + info);
															listScripts();
														}
													}
												})
									});
					getIMG();
					$("#testScript")
							.click(
									function() {
										if (ifLogin) {
											var url = $("#url").val();
											var method = $("#method").val();
											var regex = $("#regex").val();
											if (url == null || method == null
													|| regex == null) {
												layer.msg("必要输入欠缺,请检查并补全后重试");
											} else {
												$
														.ajax({
															url : "script/test",
															async : true,
															contentType : "application/x-www-form-urlencoded; charset=utf-8",
															type : "post",
															data : {
																url : url,
																method : method,
																regex : regex,
																parameter : null
															},
															success : function(
																	data) {
																console
																		.log("耗时"
																				+ data.time
																				+ "毫秒, "
																				+ data.state);
																$("#htmlShower")
																		.html(
																				""
																						+ data.result);
																initPreview(data.result);
																$('pre code')
																		.each(
																				function(
																						i,
																						block) {
																					hljs
																							.highlightBlock(block);
																				});
															}
														});
											}
										} else {
											layer.msg("请先登录获取相关权限!");
										}

									});
					$("#saveScript")
							.click(
									function() {
										if (ifLogin) {
											var script_name = $("#script_name")
													.val();
											var script_description = $(
													"#script_description")
													.val();
											var author_name = $("#author")
													.val();
											var url = $("#url").val();
											var method = $("#method").val();
											var regex = $("#regex").val();
											if (script_name == null
													|| script_description == null
													|| url == null
													|| method == null
													|| regex == null) {
												layer.msg("必要输入欠缺,请检查并补全后重试");
											} else {
												$
														.ajax({
															url : "script/add",
															async : true,
															contentType : "application/x-www-form-urlencoded; charset=utf-8",
															type : "post",
															data : {
																url : url,
																method : method,
																regex : regex,
																parameter : null,// 暂未写
																script_type : "NormalRequest",// 预留扩展
																author : author_name,
																script_name : script_name,
																script_description : script_description
															},
															success : function(
																	data) {
																layer
																		.msg(data.info);
																listScripts();
															}
														});
											}
										} else {
											layer.msg("请先登录获取相关权限!");
										}
									})
				})