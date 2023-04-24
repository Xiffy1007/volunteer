import { request, getWxLogin } from "../../utils/index.js";
var WxParse = require('../../wxParse/wxParse.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    notice: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getnotice_detail(options.id);
  },
  async getnotice_detail(id) {
    request({ url: '/notice/detail/'+id,method:'GET'}).then(res => {
      this.setData({
        notice:res.data
      })
      var that = this;
      WxParse.wxParse('content', 'html', res.data.content, that, 5); 
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