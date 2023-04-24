// components/SearchBar/SearchBar.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    searchType:String//搜索类型
  },

  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
    handletrans(){
      this.triggerEvent('buttonChange',this.properties.index)
    }
  }
})
