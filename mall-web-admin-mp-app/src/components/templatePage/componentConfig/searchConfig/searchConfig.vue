<style lang="less" scoped>
@import "./searchConfig.less";
</style>
<template :options="options">
  <div class="d-config">
    <el-tabs value="content" class="d-tab">
      <el-tab-pane label="选项卡设置" name="content">
        <div class="pannelcontent">
          <div class="pannelItem" style="align-items:center;">
            <div class="titspe">提示文字</div>
            <el-input
              v-model="search_tips"
              class="itemInput"
              @input="changeContent"
              placeholder="请输入关键字进行搜索"
            ></el-input>
          </div>
          <div class="pannelItem" style="align-items:center;">
            <div class="titspe">上下边距</div>
            <div class="slideDiv">
              <el-slider
                class="slide-content"
                v-model="udPadding"
                @change="changeContent"
                :max="48"
              ></el-slider>
              <div class="slide-tips">{{udPadding}}px</div>
            </div>
          </div>
          <div class="pannelItem" style="align-items:center;">
            <div class="titspe">左右边距</div>
            <div class="slideDiv">
              <el-slider
                class="slide-content"
                v-model="lrPadding"
                @change="changeContent"
                :max="48"
              ></el-slider>
              <div class="slide-tips">{{lrPadding}}px</div>
            </div>
          </div>
          <div class="pannelItem">
            <div class="titspe">搜索框样式</div>
            <el-radio-group v-model="search_style" @change="changeContent">
              <el-radio label="1">
                <span>方形</span>
              </el-radio>
              <el-radio label="2">
                <span>圆角</span>
              </el-radio>
              <el-radio label="3">
                <span>圆弧</span>
              </el-radio>
            </el-radio-group>
          </div>
          <div class="pannelItem">
            <div class="titspe">文字对齐</div>
            <el-radio-group v-model="serach_align" @change="changeContent">
              <el-radio label="1">
                <span>居左</span>
              </el-radio>
              <el-radio label="2">
                <span>居中</span>
              </el-radio>
              <el-radio label="3">
                <span>居右</span>
              </el-radio>
            </el-radio-group>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  props: ["options", "editable"],
  data() {
    return {
      udPadding: 6,
      lrPadding: 3,
      serach_align: "1",
      search_style: "1",
      search_tips: "",
      newOptions: {}
    };
  },
  created() {
    let _this = this;
    console.log(this.editable);
    _this.init(_this.options);
  },
  watch: {
    options() {
      let _this = this;
      _this.newOptions = _this.options;
      console.log("选项卡", _this.newOptions);
      _this.init(_this.newOptions);
    },
    editable() {
      console.log(this.editable);
    }
  },
  methods: {
    // 初始化
    init(op) {
      let _this = this;
      let optionsParams = op.options;
      if (JSON.stringify(optionsParams) == "{}") {
        _this.restore();
      } else {
        _this.udPadding = optionsParams.udPadding;
        _this.lrPadding = optionsParams.lrPadding;
        _this.serach_align = optionsParams.serach_align;
        _this.search_style = optionsParams.search_style;
        _this.search_tips = optionsParams.search_tips;
      }
    },
    // 添加内容
    addContent(index) {
      let _this = this;
      let length = _this.colorGroup[index].tabContent.length;
      if (length >= 10) {
        _this.$Message.error("不能超过10个");
        return false;
      }
      _this.colorGroup[index].tabContent.push({
        tabImg: "",
        tabLink: ""
      });

      _this.changeForm();
    },
    //改变颜色
    changeColor(e) {
      this.changeForm();
    },

    // 改变内容
    changeContent(e) {
      this.changeForm();
    },
    // 恢复初始状态
    restore() {
      let _this = this;
      _this.udPadding = 6;
      _this.lrPadding = 3;
      _this.serach_align = "1";
      _this.search_style = "1";
      _this.search_tips = "";
      _this.changeForm();
    },

    //改变值并且给父组件传递事件
    changeForm() {
      let _this = this;
      let changeData;
      changeData = {
        udPadding: _this.udPadding,
        lrPadding: _this.lrPadding,
        serach_align: _this.serach_align,
        search_style: _this.search_style,
        search_tips: _this.search_tips
      };

      _this.$emit("listenToForm", changeData);
    },

    // 点击显示图片的选择弹框
    showImgSelect() {
      let _this = this;
      _this.changeForm();
      let changeData = {
        openImg: true,
        mode: "diy",
        type: "image",
        id: "search"
      };
      _this.$emit("listenToOpenImgSelect", changeData);
    }
  }
};
</script>
