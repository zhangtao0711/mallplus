<style lang="less" scoped>
@import "./helpcenterConfig.less";
</style>
<template :options="options">
  <div class="d-config">
    <el-tabs value="content" class="d-tab">
      <el-tab-pane label="帮助中心设置" name="content">
        <div class="pannelcontent">
          <div class="pannelItemspe">
            <div class="pannelcontent">
              <div class="colorGroup">
                <div class="colorItem" v-for="(item,index) in colorGroup" :key="index">
                  <div class="colorInput">
                    <el-input
                      v-model="item.adText"
                      class="itemInput"
                      :maxlength="10"
                      placeholder="请输入标题"
                    ></el-input>
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
          adText: "小程序",
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
  }
};
</script>
