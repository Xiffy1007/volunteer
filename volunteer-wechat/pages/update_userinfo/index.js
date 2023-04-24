// 导入request请求工具类
import { request } from "../../utils/index.js";

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userinfo: [],//用户信息
    sex: 0
  },
  radioChange(e) {
    this.setData({
      sex: e.detail.value
    })
    
  },
  
  formSubmit: function (e) {
    var _this=this;
    wx.showModal({
      title: '友情提示',
      content: '您确定要修改信息吗？',
      success:res=> {
        if(res.confirm==true){
        var user = e.detail.value;
        user['sex'] = _this.data.sex;//增加对象
        user['openid'] = _this.data.userinfo.openid;
        request({ url: "/user/update", data: user, method: 'PUT' }).then(res => {
          if (res.code === '0') {
            wx.setStorageSync('user', res.data);
            _this.setData({
              userinfo: res.data
            })
            wx.showToast({
              title: '修改成功',
              icon: 'success'
            })
          }
        })
        }
        
      }
    })
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

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
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo: user,
      sex:user.sex
    })
    console.log(user.sex)
    
   
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