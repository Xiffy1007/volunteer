// pages/upload/upload.js
// 导入request请求工具类
import { request } from "../../utils/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgs: [],
    lenMore: 0,
    content: '',
    idList: '',
    userinfo: [],

  },
  formSubmit: function (e) {
    if (!wx.getStorageSync('user')) {
      wx.showModal({
        title: '友情提示',
        content: '请先登录！',
        success: res => {
          if (res.confirm == true) {
            wx.navigateTo({
              url: '/pages/login/index',
            })
            // let user = wx.getStorageSync('user');


            //let data = { userId: this.data.userinfo.id, newsId: this.data.news.id }
            //  console.log(this.data.userinfo.id);
            // this.getlikeorcol(data);


          }
        }
      })
    } else{
      var _this = this;
      wx.showModal({
        title: '友情提示',
        content: '您确定要提交吗？',
        success: res => {
          if (res.confirm == true) {
            var check = e.detail.value;
            check['images'] = _this.data.idList;//增加对象
            check['userId'] = _this.data.userinfo.id;//增加对象
            console.log(check)
            request({ url: "/check/content", data: check, method: 'PUT' }).then(res => {
              if (res.code === '0') {
              
                wx.showToast({
                  title: '提交成功',
                  icon: 'success'
                })
                this.setData({
                  content:'',
                  imgs:[],
                  idList: '',
          
                });
                
              }
            })
          }
  
        }
      })
    }



    
  },

  chooseImg: function (e) {


    if (!wx.getStorageSync('user')) {
      wx.showModal({
        title: '友情提示',
        content: '请先登录！',
        success: res => {
          if (res.confirm == true) {
            wx.navigateTo({
              url: '/pages/login/index',
            })
            // let user = wx.getStorageSync('user');


            //let data = { userId: this.data.userinfo.id, newsId: this.data.news.id }
            //  console.log(this.data.userinfo.id);
            // this.getlikeorcol(data);


          }
        }
      })
    } else {

      var that = this;
      var imgs = this.data.imgs;
      if (imgs.length >= 9) {
        this.setData({
          lenMore: 1
        });
        setTimeout(function () {
          that.setData({
            lenMore: 0
          });
        }, 2500);
        return false;
      }
      wx.chooseImage({
        // count: 1, // 默认9
        sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
          // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
          var tempFilePaths = res.tempFilePaths;
          var imgs = that.data.imgs;

          // console.log(tempFilePaths + '----');
          for (var i = 0; i < tempFilePaths.length; i++) {
            if (imgs.length >= 9) {
              that.setData({
                imgs: imgs
              });
              return false;
            } else {
              imgs.push(tempFilePaths[i]);
            }
            console.log("---->   " + tempFilePaths[i])
            wx.uploadFile({
              url: 'http://localhost:8088/check/upload',
              filePath: tempFilePaths[i],
              name: "file",
              header: {
                "content-type": "multipart/form-data"
              },
              success: res => {
                var res = JSON.parse(res.data)
                if (res.code === '0') {

                  that.setData({
                    idList: that.data.idList + ',' + res.data
                  })

                  wx.showToast({
                    title: "上传成功",
                    icon: "none",
                    duration: 1500
                  })
                  // that.data.imgs.push(JSON.parse(res.data).data)
                }
              },
              fail: function (err) {
                wx.showToast({
                  title: "上传失败",
                  icon: "none",
                  duration: 2000
                })
              },
              complete: function (result) {
                console.log(result.errMsg)
              }
            })
          }
          // console.log(imgs);
          that.setData({
            imgs: imgs,

          });
        }
      });
    }



  },
  // 删除图片
  deleteImg: function (e) {
    var imgs = this.data.imgs;
    var index = e.currentTarget.dataset.index;
    imgs.splice(index, 1);
    this.setData({
      imgs: imgs
    });
  },
  // 预览图片
  previewImg: function (e) {
    //获取当前图片的下标
    var index = e.currentTarget.dataset.index;
    //所有图片
    var imgs = this.data.imgs;
    wx.previewImage({
      //当前显示图片
      current: imgs[index],
      //所有图片
      urls: imgs
    })
  },


  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    let user = wx.getStorageSync('user');
    this.setData({
      userinfo: user
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})