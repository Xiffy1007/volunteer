// pages/userinfo/index.js
import { request, getWxLogin } from "../../utils/index.js";

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userinfo:[],
    comlist:[],
    actlist:[]
  },
  async getuser_detail(id) {
    request({ url: '/user/detail/'+id, method: 'GET' }).then(res => {
      this.setData({
        userinfo: res.data,
      })
    })

  },
  async getcom_detail(id) {
    id=id;
    console.log(id);
    request({ url: '/comment/user/list3/'+id,method: 'GET' }).then(res => {
      this.setData({
        comlist: res.data,
      })
    })

  },
  async getact_detail(id) {
    
    request({ url: '/activity/user/list3/'+id,method: 'GET' }).then(res => {
      this.setData({
        actlist: res.data,
      })
    })

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getuser_detail(options.id);
    this.getcom_detail(options.id);
    this.getact_detail(options.id);

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