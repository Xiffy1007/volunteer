// 导入request请求工具类

import { request,  getWxLogin} from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  userlogin: function () {
    getWxLogin().then((res) => {
      request({ url: "/user/wxlogin", data: res.code, method: 'POST' }).then(res => {
        if (res.code === '0') {
          wx.showToast({
            title: '登录成功',
            icon: 'success'
          })
          wx.setStorageSync('user', res.data);
          if (res.data.sno === null) {//
            wx.navigateTo({
              url: '/pages/update_userinfo/index' //修改个人资料
            })
          } else {
            wx.navigateBack({
              delta: 1
            });
          
          }

        }



      })



    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

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