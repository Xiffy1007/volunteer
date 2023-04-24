// 导入request请求工具类
import { request } from "../../utils/index.js";

Page({

  /**
   * 页面的初始数据
   */
  data: {
    productList: [], // 返回结果数组
    inputValue: "", // 输入框的值
    isFocus: false,// 取消 按钮 是否显示
    searchType: "",//搜索类型
    filterList:[]
  },

  TimeoutId: -1,
  
  async search(key) {
    if (this.data.searchType ==="activity") {
      request({ url: "/activity/search/" + key, method: "GET" }).then(res => {
        this.setData({
          productList: res.data
          
        })
        if(this.data.inputValue.length>0){
          let arr=[];
          let idList=[];
          for(let i=0;i<this.data.productList.length;i++){
            if(this.data.productList[i].name.indexOf(this.data.inputValue)>-1){
              arr.push({name:this.data.productList[i].name})
              idList.push({id:this.data.productList[i].id})
            }}
            
            this.setData({filterList:arr},()=>{
              this.getlight(arr,this.data.inputValue,idList)
            })
            console.log(arr)

          
        }
        else{
          this.setData({filterList:[]})
        }
      })
      
    }
    if (this.data.searchType ==="news") {
      request({ url: "/news/search/" + key, method: "GET" }).then(res => {
        this.setData({
          productList: res.data
          
        })
        if(this.data.inputValue.length>0){
          let arr=[];
          let idList=[];
          for(let i=0;i<this.data.productList.length;i++){
            if(this.data.productList[i].name.indexOf(this.data.inputValue)>-1){
              arr.push({name:this.data.productList[i].name})
              idList.push({id:this.data.productList[i].id})
            }}
            
            this.setData({filterList:arr},()=>{
              this.getlight(arr,this.data.inputValue,idList)
            })
            console.log(arr)

          
        }
        else{
          this.setData({filterList:[]})
        }
      })
      
    }
   

  },
  getlight(datalist,val,idList){
    for(let i=0;i<datalist.length;i++){
      datalist[i].id=idList[i].id;
    }
    datalist.forEach(item=>{
      let text=item.name.split("");
      let key=val.split("");
      let list=[];
      
      for(let i=0;i<text.length;i++){
        let obj={
          set:false,
          val:text[i]
        }
        list.push(obj);
      };
      for(let k=0;k<key.length;k++){
        list.forEach(io=>{
          if(io.val===key[k]){
            io.set=true;
          }
        })
      }
      item.list=list;
      
    });

    this.setData({
      filterList:datalist
    })
   
  }
  ,
  // 处理input事件
  handleInput(e) {
    const { value } = e.detail;
    if (!value.trim()) {
      this.setData({
        productList: [],
        isFocus: false
      })
      return;
    }
    this.setData({
      isFocus: true
    })
    clearTimeout(this.TimeoutId);
    this.TimeoutId = setTimeout(() => {
      this.search(value)
    }, 1000)
  },
  // 点击 取消按钮
  handleCancel() {
    this.setData({
      productList: [], // 商品数组
      inputValue: "", // 输入框的值
      isFocus: false // 取消 按钮 是否显示
    })
  },

  /**
 * 获取详情
 */


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      searchType:options.searchType
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