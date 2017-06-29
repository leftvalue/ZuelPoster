/**
 * 用来随机获取马卡龙配色
 */
var colors = [ "#95e987", "#ffb67e", "#8cc7fe", "#7ba3eb", "#e3ade8",
		"#f9728b", "#85e9cd", "#f5a8cf", "#a9e2a0", "#70cec7", ];
var randomColor = function() {
	rand = Math.ceil(Math.random() * 10);
	return colors[rand];
}