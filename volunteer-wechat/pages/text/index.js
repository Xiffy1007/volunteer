// pages/text/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },
  // 获取当前地理位置 授权验证
  getCurrentLocal() {
    let that = this;
    wx.getSetting({
      success(res) {
        if (res.authSetting['scope.userLocation'] == false) {// 如果已拒绝授权，则打开设置页面
          wx.openSetting({
            success(res) { }
          })
        } else { // 第一次授权，或者已授权，直接调用相关api
          that.getMyLocation()
        }
      }
    })
  },
  // 获取当前地理位置
  getMyLocation() {
    let that = this
    wx.getLocation({
      isHighAccuracy: true, // 开启地图精准定位
      type: 'gcj02', // 地图类型写这个
      success: res => {
        console.log(res.latitude)
        console.log(res.longitude)
      },
      fail: err => { // 获取定位失败
        console.log(err, '定位失败')
      }
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