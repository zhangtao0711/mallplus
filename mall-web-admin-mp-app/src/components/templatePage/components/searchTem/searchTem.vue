<style lang="less" scoped>
@import "./searchTem.less";
</style>

<template>
  <div
    class="search diyitem"
    :style="{padding:udPadding+'px '+lrPadding+'px',backgroundSize:'cover',backgroundPosition:'center'}"
  >
    <div
      class="searchContent"
      :style="{
          border:'1px solid #f1f1f1',
          borderRadius:search_style == '1'
          ?
          '0px'
          :
          search_style == '2'
            ?
            '6px'
            :
            '50px'
          }"
    >
      <i class="iconsearch" type="ios-search-outline"></i>
      <div
        class="searchBorder"
        :style="{
          color:'#666',
          textAlign:
          serach_align == '1'
          ?
          'left'
          :
            serach_align == '2'
            ?
            'center'
            :
            'right',
          padding:
          serach_align == '1'
          ?
          '0px 0px 0px 30px'
          :
            serach_align == '2'
            ?
            '0px'
            :
            '0px 6px 0px 0px'

        }"
      >{{search_tips ? search_tips : '请输入关键字进行搜索'}}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["options"],
  data() {
    return {
      udPadding: 6,
      lrPadding: 3,

      serach_align: "1",
      search_style: "1",
      search_tips: "",
      newOptions: {},
    };
  },
  watch: {
    options() {
      let _this = this;
      _this.newOptions = _this.options;
      console.log("选项卡模板", _this.newOptions);
      _this.init(_this.newOptions);
    }
  },
  created() {
    let _this = this;
    _this.init(_this.options);
  },
  methods: {
    // 初始化
    init(options) {
      let _this = this;
      if (JSON.stringify(options) == "{}") {
        _this.restore();
      } else {
        _this.udPadding = options.udPadding;
        _this.lrPadding = options.lrPadding;
        _this.serach_align = options.serach_align;
        _this.search_style = options.search_style;
        _this.search_tips = options.search_tips;

      }
    },
    // 点击tab
    clickTab(index) {
      console.log(index);
      this.current = index;
    },
    // 恢复初始
    restore() {
      let _this = this;
      _this.udPadding = 6;
      _this.lrPadding = 3;
      _this.serach_align = "1";
      _this.search_style = "1";
      _this.search_tips = "";
    }
  }
};
</script>
