<style lang="less" scoped>
@import "./cellTem.less";
</style>

<template>
  <div>
    <!-- <div class="order diyitem">
      <div class="order-group">
        <div class="order-item" v-for="(item,index) in colorGroup" :key="index">
          <div class="iconImg">
            <span :class="['icon iconfont',item.adImg]"></span>
          </div>
          <div class="iconText">{{item.adText}}</div>
        </div>
      </div>
    </div> -->

    <van-cell-group>
      <van-cell v-for="(item,index) in colorGroup" :key="index" :title="item.adText" is-link />
    </van-cell-group>
  </div>
</template>

<script>
export default {
  props: ["options"],
  data() {
    return {
      order_num: "4",
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
      _this.order_num = "4";
      _this.colorGroup = [];
      for (let i = 0; i < _this.order_num; i++) {
        let newObj = {
          adImg: "",
          adText: "",
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
