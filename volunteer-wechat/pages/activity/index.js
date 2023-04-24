import { request, getWxLogin } from "../../utils/index.js";

Page({

  /**
   * 页面的初始数据
   */
  data: {
    orders: [],
    tabs: [
      {
        id: 0,
        value: "报名中",
        isActive: false
      },
      {
        id: 1,
        value: "进行中",
        isActive: false
      },
      {
        id: 2,
        value: "已完成",
        isActive: false
      },
    ],

    scrollTop: 0, // 设置竖向滚动条位置
    currentIndex: 0,// 当前选中左侧菜单的索引
    leftMenuList: [],  // 左侧菜单数据
    rightContext: [], // 右侧活动数据
    searchType: "activity",//搜索类型
    typeId: 1,//活动类别
  },


  // 接口参数
  QueryParams: {
    typeId: 1,//活动类别
    type: 0,//活动状态
    page: 1, // 第几页
    pageSize: 10 // 每页记录数
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
    this.QueryParams.type = index;//设置活动状态
    this.QueryParams.page = 1;//页数为1
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
    this.changeTitleByIndex(0);
    this.QueryParams.typeId = 1;
    this.QueryParams.page = 1;
    this.setData({
      orders: []
    }) 
    this.getCates();
    this.getOrders();
  },

  //获取活动分类数据
  async getCates() {
    request({ url: "/type/findTypes", method: 'GET' }).then(res => {
      
      let leftMenuList = res.data;
      this.setData({
        leftMenuList,//存储分类
      })
    })

  },

  async getOrders() {
    request({ url: '/activity/list', data:this.QueryParams,method:'GET'}).then(res => {
    this.totalPage = res.data.totalPage;
     this.setData({
        orders: [...this.data.orders, ...res.data.activityList]
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
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
   
   
  
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