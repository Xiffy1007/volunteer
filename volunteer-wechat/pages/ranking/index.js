// pages/ranking/index.js
import { request, getWxLogin } from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    ranklist: [],//排名列表
    myrank:0,//我的排名
    userinfo:{},
    flag:false
  },
  // 接口参数
  QueryParams: {
    page: 1, // 第几页
    pageSize: 10,// 每页记录数
    id:-1//用户id
  },
  // 总页数
  totalPage: 1,

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },
  async getranking(){
    request({ url: '/user/ranklist', data:this.QueryParams,method:'GET'}).then(res => {
      this.totalPage = res.data.totalPage;
      console.log(this.totalPage)
       this.setData({
        ranklist: [...this.data.ranklist, ...res.data.ranklist],
        myrank:res.data.myrank
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
    let user = wx.getStorageSync('user');
    if(user){
      this.setData({
        userinfo: user,
        flag:true
      })
      this.QueryParams.id=user.id;

    }else{
      this.QueryParams.id=-1;
    }
    
    this.QueryParams.page=1;
    this.setData({
      ranklist: []
    }) 
    this.getranking();
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
  onPullDownRefresh: function () {
    console.log("下拉刷新")
    this.QueryParams.page=1;
    this.setData({
      ranklist:[]
    })
    this.getranking();
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
      this.getranking();
    }
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})