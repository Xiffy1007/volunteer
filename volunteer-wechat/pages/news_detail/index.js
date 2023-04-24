import { request, getWxLogin } from "../../utils/index.js";
var WxParse = require('../../wxParse/wxParse.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    hiddenmodalput: true,
    content: "",


    //字数限制
    maxWord: 200,
    currentWord: 0,


    news: [],//新闻详情
    userinfo: [],//用户信息

    commentList: [],//评论信息

    like: 0,//点赞人数
    showlike: false,//点赞
    showcol: false,//收藏

  },

  // 接口参数
  QueryParams: {
    newsId: 0,//新闻id
    page: 1, // 第几页
    pageSize: 10// 每页记录数

  },
  // 总页数
  totalPage: 1,
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    console.log("onload")
    let user = wx.getStorageSync('user');

   
    this.QueryParams.newsId =parseInt( options.id);
    
    this.getnews_detail(parseInt(options.id));
    this.QueryParams.page = 1;
    this.getcomments();
   
    if (user) {
      let data = { userId: user.id, newsId:parseInt(options.id) }
      console.log(data)
      this.getlikeorcol(data);
    }

    

  },
  async getnews_detail(id) {
    request({ url: '/news/detail/' + id, method: 'GET' }).then(res => {
      this.setData({
        news: res.data
      })
      var that = this;
      WxParse.wxParse('content', 'html', res.data.content, that, 5); 
    })
  },

  async getlikeorcol(data) {
    request({ url: '/likeorcol/islike', data: data, method: 'GET' }).then(res => {
      if (res) {
        this.setData({
          showlike: true
        })
      } else {
        this.setData({
          showlike: false
        })
      }
    })
    request({ url: '/likeorcol/iscol', data: data, method: 'GET' }).then(res => {
      if (res) {
        this.setData({
          showcol: true
        })
      } else {
        this.setData({
          showcol: false
        })
      }
    })
  },

  async getcomments() {
    request({ url: '/comment/list', data: this.QueryParams, method: 'GET' }).then(res => {
      this.totalPage = res.data.totalPage;
      this.setData({
        commentList: [...this.data.commentList, ...res.data.commentList]
      })
    });
  },

  //发布评论
  modalinput: function () {
    if (!wx.getStorageSync('user')) {
      wx.showModal({
        title: '友情提示',
        content: '请先登录！',
        success: res => {
          if (res.confirm == true) {
            wx.navigateTo({
              url: '/pages/login/index',
            })
            let user = wx.getStorageSync('user');
          

            let data = { userId: this.data.userinfo.id, newsId: this.data.news.id }
          //  console.log(this.data.userinfo.id);
           // this.getlikeorcol(data);


          }
        }
      })
    } else {
      this.setData({
        //注意到模态框的取消按钮也是绑定的这个函数，
        //所以这里直接取反hiddenmodalput，也是没有毛病
        hiddenmodalput: !this.data.hiddenmodalput,
      })
    }

  },
  praise: function () {
    if (!wx.getStorageSync('user')) {
      wx.showModal({
        title: '友情提示',
        content: '请先登录！',
        success: res => {
          if (res.confirm == true) {
            wx.navigateTo({
              url: '/pages/login/index',
            })
            let user = wx.getStorageSync('user');
            this.setData({
              userinfo: user,
            })
            let data = { userId: this.data.userinfo.id, newsId: this.data.news.id }
          }
        }
      })
    } else {
      let data = { userId: this.data.userinfo.id, newsId: this.data.news.id };
      if (this.data.showlike) {
        request({ url: '/likeorcol/dislike', data: data, method: 'DELETE' }).then(res => {

          if (res.code === '0') {
            this.setData({
              showlike: false
            })
            this.getnews_detail(this.data.news.id );

          }
        });
      } else {
        request({ url: '/likeorcol/like', data: data, method: 'PUT' }).then(res => {
          if (res.code === '0') {
            this.setData({
              showlike: true
            })
            this.getnews_detail(this.data.news.id );

          }

        });
      }
    }




  },

  collect: function () {
    if (!wx.getStorageSync('user')) {
      wx.showModal({
        title: '友情提示',
        content: '请先登录！',
        success: res => {
          if (res.confirm == true) {
            wx.navigateTo({
              url: '/pages/login/index',
            })
            let user = wx.getStorageSync('user');
            this.setData({
              userinfo: user,
            })
            let data = { userId: this.data.userinfo.id, newsId: this.data.news.id }
        
          }
        }
      })
    } else {
      let data = { userId: this.data.userinfo.id, newsId: this.data.news.id };
      if (this.data.showcol) {
        request({ url: '/likeorcol/discol', data: data, method: 'DELETE' }).then(res => {

          if (res.code === '0') {
            this.setData({
              showcol: false
            })
            this.getnews_detail(this.data.news.id );
          }
        });
        
      } else {
        request({ url: '/likeorcol/col', data: data, method: 'PUT' }).then(res => {
          if (res.code === '0') {
            this.setData({
              showcol: true
            })
            this.getnews_detail(this.data.news.id );
          }

        });
      }
    }




  },

  //获取评论内容
  comment: function (e) {
    
    var that = this;
    var value = e.detail.value;
    console.log(value);
    //解析字符串长度转换成整数。
    var wordLength = parseInt(value.length);
    if (that.data.maxWord < wordLength) {
      return;
    }
    that.setData({
      currentWord: wordLength,
      judge:value
    });
    
    

  },
  //确认提交评论
  confirm: function () {

    this.setData({
      hiddenmodalput: true
    })
    let data = { userId: this.data.userinfo.id, content: this.data.judge, newsId: this.data.news.id };
    request({ url: '/comment/add/', data: data, method: 'POST' }).then(res => {
      if (res.code === "0") {
        console.log("成功评论")
        this.setData({
          judge: "",
          currentWord: 0,
          commentList: []
        })
        this.QueryParams.page = 1;
        this.getcomments();
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
    console.log("onshow")
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo: user
    })
    if(user){
      if(this.data.news.id){
        let data = { userId: user.id, newsId: this.data.news.id}
      console.log(data)
      this.getlikeorcol(data);
      }
    }


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
    this.QueryParams.page = 1;
    this.setData({
      commentList: []
    })
    this.getcomments();
    // 手动关闭等待效果
    wx.stopPullDownRefresh({

    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
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
      this.getcomments();
    }
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})