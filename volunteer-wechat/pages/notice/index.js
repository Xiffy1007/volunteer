// pages/notice/index.js
import { request, getWxLogin } from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    noticeList:[]//公告列表
  },
    // 接口参数
    QueryParams: {
      page: 1, // 第几页
      pageSize: 8// 每页记录数
    },
     // 总页数
  totalPage: 1,

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.QueryParams.page=1;
    this.getnotices()

    },

  async getnotices(){
    request({ url: '/notice/list', data:this.QueryParams,method:'GET'}).then(res => {
      this.totalPage = res.data.totalPage;
      console.log(this.totalPage)
       this.setData({
        noticeList: [...this.data.noticeList, ...res.data.noticeList]
        })
      });
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
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    console.log("下拉刷新")
    this.QueryParams.page=1;
    this.setData({
      noticeList:[]
    })
    this.getnotices();
    // 手动关闭等待效果
    wx.stopPullDownRefresh({
   
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("触底了")
    if(this.QueryParams.page>=this.totalPage){
      // 没有下一页数据
      console.log("没有下一页数据");
      wx.showToast({
        title: '没有下一页数据了',
        icon:"error"
      })
    }else{
      console.log("有下一页数据");
      this.QueryParams.page++;
      this.getnotices();
    }
  },
    /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})