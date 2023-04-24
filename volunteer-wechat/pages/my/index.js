// 导入request请求工具类
import { request, getWxLogin } from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userinfo: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo: user
    })

  },
  login() {
    wx.navigateTo({
      url: '/pages/login/index'
    })
  },
  mycol() {
    wx.navigateTo({
      url: '/pages/mycollect/index'
    })
  },
  mylike() {
    wx.navigateTo({
      url: '/pages/mylike/index'
    })
  },

  updateinfo() {
    wx.navigateTo({
      url: '/pages/update_userinfo/index'
    })
  },


  /**
 * 生命周期函数--监听页面显示
 */
  onShow() {
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo: user
    })


  },

  mycheck: function () {
    wx.navigateTo({
      url: '/pages/check_result/index'
    })
  },
  //查询我的活动
  myActs: function () {
    wx.navigateTo({
      url: '/pages/myacts/index'
    })
  },
  mycom: function () {
    wx.navigateTo({
      url: '/pages/mycomment/index'
    })
  },
  
  logout: function (e) {
    var _this = this;
    wx.showModal({
      title: '友情提示',
      content: '您确定要退出登录吗？',
      success: res => {
        if (res.confirm == true) {
          wx.clearStorageSync('user');
          _this.setData({
            userinfo: []
          })
          _this.onShow();
        }
      }

    })
  }
})