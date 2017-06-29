/**
 * 主页js
 */
var getIMG = function() {
	$.ajax({
		url : "login",
		async : false,
		type : "post",
		success : function(data) {
			if (data != "0") {
				$("#jpg").attr("src", "login/img");
			}
		}
	})
}
var dt = null;
$(document).ready(function() {
	var datatables_source;
	var course_table;
	var info;
	var writable = true;
	getImg();
	$("#login").click(function() {
		var account = encodeInp($("#username").val());
		var passwd = encodeInp($("#password").val());
		var encoded_ = account + "%%%" + passwd;
		$.ajax({
			url : "login/login",
			async : false,// 同步ajax
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			type : "post",
			data : {
				encoded : encoded_,
				xcode : $("#xcode").val(),
				number : $("#username").val()
			},
			success : function(data) {
				if (data == "-1") {
					course_table = "-1"
					layer.confirm('好像哪里输错了呢，让我们再来一次~', {
						time : 20000, // 20s后自动关闭
						btn : [ '哦好的' ]
					}, function() {
						location.reload();
					});
				} else {
					course_table = data.data;// 初始化课表信息
					info = data.info;
				}
			}
		})
	})
})