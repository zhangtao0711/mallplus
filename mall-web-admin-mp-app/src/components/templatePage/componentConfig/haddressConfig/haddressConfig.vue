<style lang="less" scoped>
@import "./haddressConfig.less";
</style>
<template :options="options">
  <div class="d-config">
    <el-tabs value="content" class="d-tab">
      <el-tab-pane label="收货地址设置" name="content">
        <div class="pannelcontent">
          <div class="pannelItemspe">
            <div class="pannelcontent">
              <div class="colorGroup">
                <div class="colorItem" v-for="(item,index) in colorGroup" :key="index">
                  <!-- <i  type="ios-trash img-trash"
                      size="24"
                      @click="deleteColor(index)"
                      v-if="index != 0"
                  style="position:absolute;top:0px;right:25px;"></i>-->
                  <div class="colorInput">
                    <div class="pannelItem">
                      <div class="titspe">收货人</div>
                      <el-input v-model="item.adTextshouhuo" placeholder="请输入收货人"></el-input>
                    </div>
                    <div class="pannelItem">
                      <div class="titspe">手机号</div>
                      <el-input v-model="item.adTextcell" placeholder="请输入手机号"></el-input>
                    </div>
                    <div class="pannelItem">
                      <div class="titspe">详细地址</div>
                      <el-input v-model="item.adTextdizhi" placeholder="请输入详细地址"></el-input>
                    </div>
                    <div class="pannelItem">
                      <div class="titspe">按钮类型</div>
                      <el-switch
                        @change="changeType"
                        v-model="item.valuea"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                      ></el-switch>
                    </div>

                    <!-- <el-input v-model="item.adLink" class="itemInput" disabled placeholder="请选择或填写小程序路径">
                      <el-button slot="append" @click="showLinkSelect(index)" :disabled="editable != 'enable'">选择跳转</el-button>
                    </el-input>-->
                  </div>
                </div>

                <!-- <div class="colorAdd" @click="addList">
                    +<span class="colortips">添加</span>
                </div>-->
              </div>
            </div>
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
      newOptions: {},
      colorGroup: [],
      colorGrouz: [],
      value: true
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
    changeType(e) {
      this.changeForm();
    },
    // 初始化
    init(op) {
      let _this = this;
      let optionsParams = op.options;
      if (JSON.stringify(optionsParams) == "{}") {
        _this.restore();
      } else {
        _this.colorGroup = optionsParams.colorGroup || [];
      }
    },

    // 添加列表
    addList() {
      let _this = this;
      _this.colorGroup.push({
        btna: "",
        adImg: "",
        adLink: "",
        adTextshouhuo: "",
        adTextdizhi: "",
        adTextcell: "",
        valuea:""
      });
    },

    //删除图片
    deleteColor(index) {
      let _this = this;
      _this.colorGroup.splice(index, 1);
      _this.changeForm();
    },

    // 恢复初始状态
    restore() {
      let _this = this;
      _this.colorGroup = [];
      for (let i = 0; i < 1; i++) {
        let newObj = {
          btna: "",
          adImg: "",
          adLink: "",
          adTextshouhuo: "",
          adTextdizhi: "",
          adTextcell: "",
          valuea:""
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
          colorGroup: _this.colorGroup,
          addIndex: index - 1
        };
      } else {
        changeData = {
          colorGroup: _this.colorGroup
        };
      }

      _this.$emit("listenToForm", changeData);
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
        id: "list",
        link: _this.colorGroup[index].adLink
      };
      _this.$emit("listenToOpenLinkSelect", changeData);
    }
  }
};
</script>
