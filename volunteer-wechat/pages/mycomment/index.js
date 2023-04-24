import { request, getWxLogin } from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    CommentsList: [],
    userinfo: [],
    totalCom:0
  },

  // 接口参数
  QueryParams: {
    userId: 0,
    page: 1, // 第几页
    pageSize: 8// 每页记录数
  },
  // 总页数
  totalPage: 1,

  async delMycom(e) {
  var _this=this;
    wx.showModal({
      title: '友情提示',
      content: '您确定要删除吗？',
      success: res => {
        if (res.confirm == true) {
          request({ url: '/comment/delete/'+  e.currentTarget.dataset.index, method: 'DELETE' }).then(res => {
            _this.setData({
              CommentsList:[],
             

            })
            _this.getmycom();
          });
        }
      }

    })
  },
  async getmycom() {
    request({ url: '/comment/myComList', data: this.QueryParams, method: 'GET' }).then(res => {
      this.totalPage = res.data.totalPage;
      console.log(this.totalPage)
      this.setData({
        CommentsList: [...this.data.CommentsList, ...res.data.CommentsList],
        totalCom:res.data.totalCom
      })
    });
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
    this.QueryParams.page = 1;
    
    let user = wx.getStorageSync('user');
    this.QueryParams.userId=user.id;
    console.log( this.QueryParams.userId)
    this.setData({
      CommentsList: [],
    }) 
    this.getmycom();
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
    console.log("下拉刷新")
    this.QueryParams.page = 1;
    this.setData({
      CommentsList: []
    })
    this.getmycom();
    // 手动关闭等待效果
    wx.stopPullDownRefresh({

    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    console.log("触底了")
    if (this.QueryParams.page >= this.totalPage) {
      // 没有下一页数据
      console.log("没有下一页数据");
      wx.showToast({
        title: '没有下一页数据了',
        icon: "error"
      })
    } else {
      console.log("有下一页数据");
      this.QueryParams.page++;
      this.getmycom();
    }
  },


  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})