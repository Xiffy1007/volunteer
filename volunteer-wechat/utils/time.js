// time.js
/** 
 * new Date() ---> 转化为 年 月 日 时 分 秒
 * let date = new Date();
 * date: 传入参数日期 Date
*/
function formatTime(date) {
	var year = date.getFullYear()
	var month = date.getMonth() + 1
	var day = date.getDate()

	var hour = date.getHours()
	var minute = date.getMinutes()
	var second = date.getSeconds()


	return [year, month, day].map(formatNumber).join('-') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

function add(date,time) {
	
	var year = date.getFullYear()
	var month = date.getMonth() + 1
	var day = date.getDate()

	var hour = date.getHours()
	var minute = date.getMinutes()+time
	var second = date.getSeconds()

	if(minute>=60){
		hour=hour+1;
		minute=minute-60;
	}


	return [year, month, day].map(formatNumber).join('-') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

function formatNumber(n) {
	n = n.toString()
	return n[1] ? n : '0' + n
}

/** 
* 时间戳转化为年 月 日 时 分 秒 
* number: 传入时间戳 
* format：返回格式，支持自定义，但参数必须与formateArr里保持一致 
*/
function formatTimeTwo(number, format) {

	var n = number;

	var date = new Date(n);
	
	var y = date.getFullYear();
	
	var m = date.getMonth() + 1;
	
	m = m < 10 ? ('0' + m) : m;
	
	var d = date.getDate();
	
	d = d < 10 ? ('0' + d) : d;
	
	var h = date.getHours();
	
	h = h < 10 ? ('0' + h) : h;
	
	var minute = date.getMinutes();
	
	var second = date.getSeconds();
	
	minute = minute < 10 ? ('0' + minute) : minute;
	
	second = second < 10 ? ('0' + second) : second;
	
	return y + '-' + m + '-' + d + ' ' + h + ':' + minute+ ':'+ second;
}

module.exports = {
	formatTime: formatTime,
	formatTimeTwo: formatTimeTwo  
}