/**
 * 主页js
 */
var dt = null;
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
	})
}
$(document).ready(function() {
	var datatables_source;
	var course_table;
	var info;
	var writable = true;

	getIMG();
	$("#script1").click(function() {
		document.getElementById("htmlShower").className = 'html';
		$.ajax({
			url : "script/run",
			async : true,
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			type : "post",
			data : {
				sid : 1
			},
			success : function(data) {
				console.log("耗时" + data.time + "毫秒, " + data.state)
				$("#htmlShower").html("" + data.result);
				$('pre code').each(function(i, block) {
				    hljs.highlightBlock(block);
				  });
			}
		})
	});
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
						// location.reload();
						getIMG();
					});
				} else {
					info = data.info;
					$("#form").hide();
					$("#result").show();
					$("#info").html("" + info);
				}
			}
		})
	})
})