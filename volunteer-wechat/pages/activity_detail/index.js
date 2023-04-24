var time = require("../../utils/time.js");
import { request, getWxLogin } from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    button:0,
    activity: [],
    userinfo: [],
    checkSign: false,//是否报名
    state: 0,//活动状态
    userList: [],//报名人员信息
    actTime: '',
    nowTime: '',
    actendTime: '',
    signTime:'',
    lat:0,
    lng:0,
   

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo: user
    })
    this.getactivity_detail(options.id);
    this.getUserlist(options.id);

    if (user) {
      let data = { userId: user.id, activityId: parseInt(options.id) }
      this.getSignUp(data);

    }
   

  },
  // 获取当前地理位置 授权验证
  async getCurrentLocal() {
    var date = new Date();
      var t=time.formatTime(date);
    //  console.log(t)
     // console.log(this.data.signTime)
      //console.log(t<=this.data.signTime&&t>=this.data.actTime&&this.data.activity.state==1)
      if(t<=this.data.signTime&&t>=this.data.actTime&&this.data.activity.state==1){
        let that = this;
        wx.getSetting({
          success(res) {
            if (res.authSetting['scope.userLocation'] == false) {// 如果已拒绝授权，则打开设置页面
              wx.openSetting({
                success(res) { }
              })
            } else { // 第一次授权，或者已授权，直接调用相关api
              wx.getLocation({
                isHighAccuracy: true, // 开启地图精准定位
                type: 'gcj02', // 地图类型写这个
                success: res => {
                  request({ url: '/activity/signin/'+res.latitude+"/"+res.longitude+"/"+that.data.activity.id+"/"+that.data.userinfo.id, method: 'POST' }).then(res => {
                    if(res.code === '0'){
                      wx.showToast({
                        title: '签到成功！', //提示的内容
                        duration: 2000, //持续的时间
                        icon: 'success', //图标有success、error、loading、none四种
                        mask: true //显示透明蒙层 防止触摸穿透
                      })
                    }else{
                      wx.showToast({
                        title: '签到失败！', //提示的内容
                        duration: 2000, //持续的时间
                        icon: 'error', //图标有success、error、loading、none四种
                        mask: true //显示透明蒙层 防止触摸穿透
                      })
                    }
                 
                    
                   
              
                  })
                  
                },
                fail: err => { // 获取定位失败
                  console.log(err, '定位失败')
                }
              })
             
          

            }
          }
        })
      }else{
          wx.showToast({
            title: '签到时间已过', //提示的内容
            duration: 2000, //持续的时间
            icon: 'error', //图标有success、error、loading、none四种
            mask: true //显示透明蒙层 防止触摸穿透
          })
        

      }

  },
  // 获取当前地理位置
  async getMyLocation() {
    let that = this
    wx.getLocation({
      isHighAccuracy: true, // 开启地图精准定位
      type: 'gcj02', // 地图类型写这个
      success: res => {
        this.setData({
          lng:res.longitude,
          lat:res.latitude
        })
      },
      fail: err => { // 获取定位失败
        console.log(err, '定位失败')
      }
    })

  },
   
  async getactivity_detail(id) {
    request({ url: '/activity/detail/' + id, method: 'GET' }).then(res => {
      var timestamp = Date.parse(res.data.actTime);
      timestamp = timestamp + (10) * 60 * 1000;
      var signTime=time.formatTimeTwo(timestamp,'Y-M-D h:m:s');
      
      this.setData({
        activity: res.data,
        state: res.data.state,
        actTime: res.data.actTime,
        actendTime: res.data.actendTime,
        signTime:signTime
      })
      var date = new Date();
      var t=time.formatTime(date);
      console.log(t)
      if(t<=signTime&&t>=res.data.actTime&&res.data.state==1){
        this.setData({
          button:1
        })
      }
      
     

    })

  },
  async getUserlist(id) {
    request({ url: '/signup/detail/' + id, method: 'GET' }).then(res => {
      this.setData({
        userList: res.data,

      })
    })
  },
  async getSignUp(data) {
    request({ url: '/activity/checkSign/', data: data, method: 'GET' }).then(res => {
      this.setData({
        checkSign: res
      })
    })
  },

  signUp: function (data) {
    if (!wx.getStorageSync('user')) {
      wx.showModal({
        title: '友情提示',
        content: '请先登录！',
        success: res => {
          if (res.confirm == true) {
            wx.navigateTo({
              url: '/pages/login/index',
            })
          }
        }
      })
    } else {
      request({ url: '/activity/credit/'+this.data.userinfo.id, method: 'GET' }).then(res => {
        if(res.data>=10){
          let data = { userId: this.data.userinfo.id, activityId: this.data.activity.id }
          request({ url: '/activity/signup', data: data, method: 'POST' }).then(res => {
            if (res.code === '0') {
              wx.showToast({
                title: '报名成功！',
                icon: "success"
              })
              this.setData({
                checkSign: true
              })
            } else {
              wx.showToast({
                title: '报名失败！',
                icon: "error"
              })
            }
          })
        }else{
          wx.showModal({
            title: '提示', //提示的标题
            content: '您的阳光信用不足，请联系管理员！', //提示的内容
            showCancel:false
          })
        
       
        }
      })
      
    
    }



  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow(options) {
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo: user
    })
    if (user) {
      if (this.data.activity.id) {
        let data = { userId: user.id, activityId: this.data.activity.id }
        this.getSignUp(data)
      }
    }

    
    

   /* var date = new Date();
    var t=time.formatTime(date);
    console.log(t)

    var timestamp = Date.parse(t);
    console.log(timestamp)

    timestamp = timestamp + (5) * 60 * 1000;

    var t=time.formatTimeTwo(timestamp,'Y-M-D h:m:s');
    console.log(t)*/



  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})