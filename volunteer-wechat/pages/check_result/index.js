// pages/myacts/index.js
import { request, getWxLogin } from "../../utils/index.js";

Page({

  /**
   * 页面的初始数据
   */
  data: {
    total:0,
    userinfo: [],
    checkList: [],
    tabs: [
      {
        id: 0,
        value: "审核中",
        isActive: false
      },
      {
        id: 1,
        value: "通过",
        isActive: false
      },
      {
        id: 2,
        value: "未通过",
        isActive: false
      },
      {
        id:3,
        value: "所有",
        isActive: false
      },
     


    ],
  },
  // 接口参数
  QueryParams: {
    userId:0,//用户id
    state: 0,//活动状态
    page: 1, // 第几页
    pageSize: 10// 每页记录数
  },
  // 总页数
  totalPage: 1,
  // 根据标题索引激活选中的标签
  changeTitleByIndex(index) {
    let { tabs } = this.data;
    tabs.forEach((v, i) => i == index ? v.isActive = true : v.isActive = false);
    this.setData({
      tabs
    })
  },
  /**
   * tab点击事件处理
   * @param {} e 
   */
  handleTabsItemChange(e) {
    //const {index}=e.currentTarget.dataset;
    const { index } = e.detail;
    // 切换标题
    this.changeTitleByIndex(index);
    // 获取活动列表
    this.QueryParams.state = index;//设置活动状态
    this.QueryParams.page = 1;//页数为1
    this.setData({
      checkList: []
    })
    this.getcheck();
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo:user
    })
    console.log(user=='')
   
      this.changeTitleByIndex(0);
      this.QueryParams.state = 0;
      this.QueryParams.page = 1;
      this.QueryParams.userId=user.id;
      this.setData({
        checkList: []
      }) 
      this.getcheck();
    
    
  },

  async getcheck() {
    request({ url: '/check/list', data:this.QueryParams, method: 'GET' }).then(res => {
      this.totalPage = res.data.totalPage;
      this.setData({
        total:res.data.total,
        checkList: [...this.data.checkList, ...res.data.list]
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
  onPullDownRefresh: function () {
    console.log("下拉刷新")
    this.QueryParams.page=1;
    this.setData({
      checkList:[]
    })
    this.getcheck();
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
      this.getcheck();
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})