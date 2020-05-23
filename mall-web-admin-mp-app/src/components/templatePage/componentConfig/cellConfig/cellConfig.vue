<style lang="less" scoped>
@import "./cellConfig.less";
</style>
<template :options="options">
  <div class="d-config">
    <el-tabs value="content" class="d-tab">
      <el-tab-pane label="TabBar设置" name="content">
        <div class="pannelcontent">
          <div class="pannelItemspe">
            <div class="pannelcontent">
              <div class="colorGroup">
                <div class="colorItem" v-for="(item,index) in colorGroup" :key="index">
                  <div class="colorImg">
                    <span
                      v-if="item.adImg"
                      :class="['icon iconfont colorIcon',item.adImg]"
                      :src="item.adImg"
                    ></span>
                    <img v-else class="colorIcon" src="../../static/img/default_onegoods.jpg" alt />
                    <el-button
                      class="chooseIcon"
                      @click="showIconSelect(index)"
                      :disabled="editable != 'enable'"
                    >选择图标</el-button>
                  </div>

                  <div class="colorInput">
                    <el-input
                      v-model="item.adText"
                      class="itemInput"
                      :maxlength="10"
                      placeholder="请输入标题"
                    ></el-input>
                    <el-input
                      v-model="item.adLink"
                      class="itemInput"
                      disabled
                      placeholder="请选择或填写小程序路径"
                    >
                      <el-button
                        slot="append"
                        @click="showLinkSelect(index)"
                        :disabled="editable != 'enable'"
                      >选择跳转</el-button>
                    </el-input>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="colorAdd" @click="addCell" v-if="editable == 'enable'">
            +
            <span class="colortips">添加选项卡</span>
          </div>
          <div class="colorAdd" @click="delCell" v-if="editable == 'enable'">
            -
            <span class="colortips">删除选项卡</span>
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
      order_num: "1",
      newOptions: {},
      colorGroup: []
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
        _this.order_num = optionsParams.order_num || "1";
        _this.colorGroup = optionsParams.colorGroup || [];
      }
    },
    // 恢复初始状态
    restore() {
      let _this = this;
      _this.order_num = "1";
      _this.colorGroup = [];
      for (let i = 0; i < _this.order_num; i++) {
        let newObj = {
          adImg: "",
          adText: "我的",
          adLink: ""
        };
        _this.colorGroup.push(newObj);
      }
      _this.changeForm();
    },

    //改变值并且给父组件传递事件
    changeForm(index) {
      let _this = this;
      console.log(index);
      let changeData;
      if (index) {
        changeData = {
          order_num: _this.order_num,
          colorGroup: _this.colorGroup,
          addIndex: index - 1
        };
      } else {
        changeData = {
          order_num: _this.order_num,
          colorGroup: _this.colorGroup
        };
      }

      _this.$emit("listenToForm", changeData);
    },

    addCell() {
      let _this = this;
      _this.colorGroup.push({
        adImg: "",
        adText: "",
        adLink: ""
      });
      _this.changeForm();
    },
    delCell() {
      let _this = this;
      let length = _this.colorGroup.length;
      if (length==1) {
        return false;
      }
      _this.colorGroup.pop()
      _this.changeForm();
    },

    // 点击显示图片的选择弹框
    showIconSelect(index) {
      let _this = this;
      _this.changeForm(index + 1);
      let changeData = {
        openImg: true,
        mode: "diy",
        type: "image",
        id: "banner"
      };
      _this.$emit("listenToOpenIconSelect", changeData);
    },

    // 点击显示链接的选择弹框
    showLinkSelect(index) {
      let _this = this;
      _this.changeForm(index + 1);
      let changeData = {
        mode: "diy",
        id: "banner",
        link: _this.colorGroup[index].adLink
      };
      _this.$emit("listenToOpenLinkSelect", changeData);
    }
  }
};
</script>
