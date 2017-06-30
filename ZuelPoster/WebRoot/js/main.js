/**
 * 主页js
 */
var dt = null;
var scripts = null;
var responseHtml = null;
var ifSource = true;
var changePreviewType = function() {
	console.log("changePreviewType called")
	document.getElementById("htmlShower").className = 'html';// 先消除原有的class
	if (ifSource) {
		console.log("browser")
		var html = responseHtml;
		html = html.replace(/&lt;/g, "<");
		html = html.replace(/&gt;/g, ">");
		html = html
				.replace(
						"Copyright (C) 湖南强智科技发展有限公司 All Rights Reserved 湘ICP 备12010071号 ",
						"");
		console.log(html);
		$("#source").hide();
		$("#preview").show();
		$("#preview").html(html);
		$("#showPattern").html("源代码");
	} else {
		console.log("html source")
		console.log(responseHtml);
		$("#preview").hide();
		$("#source").show();
		$("#htmlShower").html(responseHtml);
		$('pre code').each(function(i, block) {
			hljs.highlightBlock(block);
		});
		$("#showPattern").html("预览");
	}
	ifSource = !ifSource;
	console.log("change completed")
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
			$("#preview").hide();
			$("#source").show();
			$("#showPattern").html("预览");
			$("#htmlShower").html("" + data.result);
			ifSource = true;
			responseHtml = data.result;
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
		}
	});
}
var delScript = function(sid) {
	document.getElementById("htmlShower").className = 'html';// 先消除原有的class
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
				// refresh datatables
				listScripts();
			}
		}
	});
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
					var datatables_source;
					var course_table;
					var info;
					var writable = true;
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
															info = data.info;
															$("#form").hide();
															$("#result").show();
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
														success : function(data) {
															console
																	.log("耗时"
																			+ data.time
																			+ "毫秒, "
																			+ data.state);
															$("#preview")
																	.hide();
															$("#source").show();
															$("#showPattern")
																	.html("预览");
															$("#htmlShower")
																	.html(
																			""
																					+ data.result);
															ifSource = true;
															responseHtml = data.result;
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
									});
					$("#saveScript")
							.click(
									function() {
										var script_name = $("#script_name")
												.val();
										var script_description = $(
												"#script_description").val();
										var author_name = $("#author").val();
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
														success : function(data) {
															layer
																	.msg(data.info);
															listScripts();
														}
													});
										}
									})
				})