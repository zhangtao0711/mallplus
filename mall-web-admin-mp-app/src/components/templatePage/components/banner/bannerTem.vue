<style lang="less" scoped>
@import "./bannerTem.less";
</style>

<template>
  <div class="banner diyitem">
    <el-carousel
      class="banneritem"
      v-model="options.bannerActive"
      dots="none"
      @change="changeSwiper"
    >
      <el-carousel-item v-for="(item,index) in bannerLists" :key="index">
        <div class="carousel">
          <img v-if="item.adImg" :src="item.adImg" alt>
          <img v-else src="../../static/img/default_onegoods.jpg" alt>
        </div>
      </el-carousel-item>
    </el-carousel>

  </div>
</template>

<script>
export default {
  props: ["options"],
  data() {
    return {
      currentId: 0,
      bannerLists: [
        {
          adImg: "",
          adLink: ""
        }
      ]
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
        _this.bannerLists = options.colorGroup;
      }
    },
    // 恢复初始
    restore() {
      let _this = this;
      _this.bannerLists = [
        {
          adImg: "",
          adLink: ""
        }
      ];
    },
    // 更换banner
    changeSwiper(oldvalue, value) {
      let _this = this;
      _this.currentId = value;
    }
  }
};
</script>
