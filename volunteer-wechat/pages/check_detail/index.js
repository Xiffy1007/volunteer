import { request, getWxLogin } from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    check: [],
    images: [],
    imgArr:[
     
    ]


  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getcheck_detail(options.id);
  },
  async getcheck_detail(id) {
    request({ url: '/check/detail/' + id, method: 'GET' }).then(res => {
      console.log(res.data.check)
      var images=[]
      this.setData({
        check: res.data.check,

      })

      for (var i =0; i < res.data.idList.length; i++) {
        images.push( "http://localhost:8088/check/download/" + res.data.idList[i])
      }
      this.setData({
       images:images

      })
    })
  },
  previewImg: function (e) {
    console.log(e.currentTarget.dataset.index);
    var index = e.currentTarget.dataset.index;
    var imgArr = this.data.images;
    wx.previewImage({
      current: imgArr[index],     //当前图片地址
      urls: imgArr,               //所有要预览的图片的地址集合 数组形式
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  }
  ,

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