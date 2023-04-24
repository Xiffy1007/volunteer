import { request } from "../../utils/index.js";
import { config } from "../../utils/config.js";
const app=getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {

    noticeList:[],//公告
    activityList:[],//活动
    backimage:[],//背景图
    newsList:{},
    userinfo:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo: user
    })
    this.getnotice();
    this.getacts();
    this.getimage();
    this.getnews();
  },
  async getnews() {
    request({ url: '/index/news/',method:'GET'}).then(res => {
      this.setData({
        newsList:res.data
      })
      console.log(res.data)
      
    })
  },

  async getnotice() {
    request({ url: '/index/notice/',method:'GET'}).then(res => {
      this.setData({
        noticeList:res.data
      })
    })
  },

  async getacts() {
    request({ url: '/index/activity/',method:'GET'}).then(res => {
      this.setData({
        activityList:res.data
      })
    })
  },
  async getimage(){
 
      this.setData({
        backimage:app.List
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