<style lang="less" scoped>
@import "./buttonConfig.less";
.pannelItem {
  width: 100%;
  padding: 20px 23px;
  box-sizing: border-box;
  border-bottom: 5px solid #f6f7f9;
  font-size: 12px;
  position: relative;
}
</style>
<template :options="options">
  <div class="d-config">
    <el-tabs value="content" class="d-tab">
      <el-tab-pane label="按钮设置" name="content">
        <div class="pannelcontent">

          <div class="pannelItem" style="border-bottom:0px">
              <div class="tit">上下边距</div>
              <div class="list-group slide">
                <el-slider class="slide-content" @change="changeSlide" v-model="udPadding" :max="100"></el-slider>
                <div class="slide-tips">{{udPadding}}PX</div>
              </div>
            </div>
            <div class="pannelItem" style="border-bottom:0px">
              <div class="tit">左右边距</div>
              <div class="list-group slide">
                <el-slider class="slide-content" @change="changeSlide" v-model="lrPadding" :max="100"></el-slider>
                <div class="slide-tips">{{lrPadding}}PX</div>
              </div>
            </div>

          <div class="pannelItem" style="align-items:center;">
            <div class="titspe">按钮名称</div>
            <el-input
              v-model="btnName"
              class="itemInput"
              @input="changeContent"
              placeholder="请输入关键字进行搜索"
            ></el-input>
          </div>

          <div class="pannelItem">
            <div class="titspe">按钮类型</div>
            <el-radio-group v-model="type" @change="changeType">
              <el-radio label="default">
                <span>默认按钮</span>
              </el-radio>
              <el-radio label="primary">
                <span>主要按钮</span>
              </el-radio>
              <el-radio label="info">
                <span>信息按钮</span>
              </el-radio>
              <el-radio label="warning">
                <span>警告按钮</span>
              </el-radio>
              <el-radio label="danger">
                <span>危险按钮</span>
              </el-radio>
            </el-radio-group>
          </div>
          <div class="pannelItem">
            <div class="titspe">按钮大小</div>
            <el-radio-group v-model="size" @change="changePlain">
              <el-radio label="large">
                <span>大号按钮</span>
              </el-radio>
              <el-radio label="normal">
                <span>普通按钮</span>
              </el-radio>
              <el-radio label="small">
                <span>小型按钮</span>
              </el-radio>
              <el-radio label="mini">
                <span>迷你按钮</span>
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
      udPadding:0,
      lrPadding:0,
      btnName: "按钮",
      type: "default",
      size: "normal"
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
        _this.btnName = optionsParams.btnName;
        _this.type = optionsParams.type;
        _this.size = optionsParams.size;
        _this.udPadding = optionsParams.udPadding || 0
        _this.lrPadding = optionsParams.lrPadding || 0
      }
    },

    changeSlide(e){
      this.changeForm();
    },
    // 改变内容
    changeContent(e) {
      this.changeForm();
    },
    changeType(e) {
      this.changeForm();
    },
    changePlain(e) {
      this.changeForm();
    },
    // 恢复初始状态
    restore() {
      let _this = this;
      _this.btnName = "按钮";
      _this.type = "default";
      _this.size = "normal";
      _this.udPadding = 0
      _this.lrPadding = 0

      _this.changeForm();
    },

    //改变值并且给父组件传递事件
    changeForm() {
      let _this = this;
      let changeData;
      changeData = {
        btnName: _this.btnName,
        type: _this.type,
        size: _this.size,
        udPadding:_this.udPadding,
        lrPadding:_this.lrPadding,
      };

      console.log(changeData)

      _this.$emit("listenToForm", changeData);
    }
  }
};
</script>
