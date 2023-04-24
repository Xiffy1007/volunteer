
// app.js
App({
  List:[
    {
      num:1,
      data:"../../icons/backimage/p1.png"
    },
    {
      num:2,
      data:"../../icons/backimage/p2.png"
    },
    {
      num:3,
      data:"../../icons/backimage/p3.png"
    },
    {
      num:4,
      data:"../../icons/backimage/p4.png"
    },
    {
      num:5,
      data:"../../icons/backimage/p5.png"
    },
    {
      num:6,
      data:"../../icons/backimage/p6.png"
    },
  ],
    async onLaunch(){
      /*
    // 验证登录是否过期
    wx.checkSession({
      success: function (res) {
       /* wx.showToast({
          title: '登录未过期'
        });
      },
      fail: function (res) {
        wx.showModal({
          title: '提示',
          content: '登录已过期，请重新登录',
          success(res) {
            if (res.confirm) {
              // 调用接口获取登录凭证（code）
              wx.login({
                success(res) {
                  console.log(res);
                  if (res.code) {
                    // 发起网络请求
                    wx.request({
                      url: 'http://localhost:8088/user/wxlogin',
                      method: 'post',
                      data: {
                        code: res.code
                      },
                      header: {
                        'content-type': 'application/x-www-form-urlencoded'
                      },
                      success(res) {
                        console.log(res);
                        if (res.data.code === 1) {
                          wx.switchTab({
                            url: '/pages/index/index'
                          });
                        } else {
                          console.log('登录失败！');
                          console.log(res);
                        }
                      },
                      fail(msg) {
                        console.log(msg);
                      }
                    });
                  } else {
                    console.log('登录失败！' + res.errMsg);
                  }
                }
              });
            } else if (res.cancel) {
              wx.switchTab({
                url: '/pages/index/index'
              });
            }
          }
        });
      }
    });*/
  }
  });
  
  
  