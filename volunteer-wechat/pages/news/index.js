import { request, getWxLogin } from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orders: [],
    searchType:"news",//搜索类型
    scrollTop:0, // 设置竖向滚动条位置
    currentIndex:0,// 当前选中左侧菜单的索引
    leftMenuList:[],  // 左侧菜单数据
    rightContext:[], // 右侧活动数据
  
  },
    // 接口参数
    QueryParams: {
      typeId:1,//新闻类别
      page: 1, // 第几页
      pageSize: 10 // 每页记录数
    },

     // 总页数
  totalPage: 1,

  //获取活动分类数据
  async getCates() {
    request({ url: "/newstype/findnewsTypes", method: 'GET' }).then(res => {
      
      let leftMenuList = res.data;
      this.setData({
        leftMenuList,//存储分类
      })
    })

  },
  async getOrders() {
    request({ url: '/news/list', data:this.QueryParams,method:'GET'}).then(res => {
    this.totalPage = res.data.totalPage;
     this.setData({
        orders: [...this.data.orders, ...res.data.newsList]
      })
    });
  },

// 左侧菜单点击切换事件
handleMenuItemChange(e) {
  const { index } = e.currentTarget.dataset;//解构获取当前索引
  let typeId = this.data.leftMenuList[index].id;
  this.setData({
    typeId,
    currentIndex: index,
    scrollTop: 0//设置滚动条位置置顶
  })
  this.QueryParams.typeId = this.data.typeId;//设置活动类别
  this.setData({
    orders: []
  }) 
  this.getOrders();
},
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.QueryParams.typeId = 1;
    this.QueryParams.page = 1;
    this.setData({
      orders: []
    }) 
    this.getCates();
    this.getOrders();
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

  },
    /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    console.log("下拉刷新")
    this.QueryParams.page=1;
    this.setData({
      orders:[]
    })
    this.getOrders();
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
      this.getOrders();
    }
  },
})