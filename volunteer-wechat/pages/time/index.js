Page({
	data: {
		nowDate: '2023-02-22 15:55:53', //结束时间
    	countdown: '', //倒计时
    	days: '00', //天
    	hours: '00', //时
    	minutes: '00', //分
    	seconds: '00', //秒
	},
	
 countTime() {
    let days,hours, minutes, seconds;
    let nowDate = this.data.nowDate;
    console.log(nowDate)
    let that = this;
    let now = new Date().getTime();
    let end = new Date(nowDate).getTime(); //设置截止时间
    // console.log("开始时间：" + now, "截止时间:" + end);
    let leftTime = end - now; //时间差        
    if (leftTime >= 0) {
      days = Math.floor(leftTime / 1000 / 60 / 60 / 24);
      hours = Math.floor(leftTime / 1000 / 60 / 60 % 24);
      minutes = Math.floor(leftTime / 1000 / 60 % 60);
      seconds = Math.floor(leftTime / 1000 % 60);
      seconds = seconds < 10 ? "0" + seconds : seconds
      minutes = minutes < 10 ? "0" + minutes : minutes
      hours = hours < 10 ? "0" + hours : hours
      that.setData({
        countdown: days+":"+hours + "：" + minutes + "：" + seconds,
        days,
        hours,
        minutes,
        seconds
      })
      // console.log(that.data.countdown)
      //递归每秒调用countTime方法，显示动态时间效果
      setTimeout(that.countTime, 1000);
    } else {
      that.setData({
        countdown: '已截止'
      })
    }
 },
 onLoad: function (options) {
    this.countTime();
    

 },
})

