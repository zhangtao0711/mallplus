<template>
  <div style="text-align:center">
    <van-button
      :type="type"
      :size="size"
    >{{ btnName }}</van-button>
  </div>
</template>

<script>
export default {
  props: ["options"],
  data() {
    return {
      newOptions: {},

      btnName: "按钮",
      type: "default",
	    size: "normal",
    };
  },
  watch: {
    options() {
      let _this = this;
      _this.newOptions = _this.options;
      // console.log('选项卡模板',_this.newOptions)
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
        _this.btnName = options.btnName;
        _this.type = options.type;
        _this.size = options.size;


      }
    },
    // 点击tab
    clickTab(index){
      this.current = index
    },
    // 恢复初始
    restore() {
      let _this = this;
      _this.btnName = "按钮";
      _this.type = "default";
      _this.size = "normal";
    }
  }
};
</script>
