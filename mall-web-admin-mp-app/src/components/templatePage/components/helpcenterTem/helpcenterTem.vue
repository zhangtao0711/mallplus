<style lang="less" scoped>
@import "./helpcenterTem.less";
</style>
<template>
  <div>
    <el-steps simple>
      <el-step v-for="(item,index) in colorGroup" :key="index" :title="item.adText" :to="item.adLink" is-link></el-step>
    </el-steps>
  </div>
</template>

<script>
export default {
  props: ["options"],
  data() {
    return {
      order_num: "1",
      newOptions: {},
      colorGroup: []
    };
  },
  watch: {
    options() {
      let _this = this;
      _this.newOptions = _this.options;
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
        _this.order_num = options.order_num;
        _this.colorGroup = options.colorGroup;
      }
    },
    // 恢复初始
    restore() {
      let _this = this;
      _this.order_num = "1";
      _this.colorGroup = [];
      for (let i = 0; i < _this.order_num; i++) {
        let newObj = {
          adImg: "",
          adText: "小程序",
          adLink: ""
        };
        _this.colorGroup.push(newObj);
      }
    },
    // 更换banner
    changeSwiper(oldvalue, value) {
      let _this = this;
      _this.currentId = value;
    }
  }
};
</script>
