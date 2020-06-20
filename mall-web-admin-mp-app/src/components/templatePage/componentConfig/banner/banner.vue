<style lang="less" scoped>
@import "./banner.less";
</style>
<template :options="options">
  <div class="d-config">
    <el-tabs value="content" class="d-tab">
      <el-tab-pane label="内容" name="content">
        <div class="pannelcontent">
          <div class="pannelItem">
            <div class="tit">上传图片</div>
            <div class="colorGroup">
              <draggable v-model="colorGroup" v-bind="dragOptions" handle=".handle" @change="changeDisplay">
                <transition-group type="transition">
                  <div class="colorItem" v-for="(item,index) in colorGroup" :key="index">
                    <i  class="iconfont icon-drag handle"
                        size="24"
                        style="position:absolute;top:0px;right:25px;"></i>
                    <i type="ios-trash img-trash"
                       size="24"
                       @click="deleteColor(index)"
                       v-if="index != 0"></i>
                    <img v-if="item.adImg" class="colorIcon" :src="item.adImg" alt>
                    <img v-else class="colorIcon" src="../../static/img/default_onegoods.jpg" alt>
                    <div class="colorInput">
                      <el-input v-model="item.adImg" class="itemInput" disabled placeholder="请选择图片">
                        <el-button slot="append" @click="showImgSelect(index)" :disabled="editable != 'enable'">选择图片</el-button>
                      </el-input>
                      <el-input v-model="item.adLink" class="itemInput" disabled placeholder="请选择或填写小程序路径">
                        <el-button slot="append" @click="showLinkSelect(index)" :disabled="editable != 'enable'">选择跳转</el-button>
                      </el-input>
                    </div>
                  </div>
                </transition-group>
              </draggable>
            </div>

            <div class="colorAdd" @click="addAd" v-if="editable == 'enable'">
              +
              <span class="colortips">添加广告图片</span>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import draggable from "vuedraggable";
export default {
  props: ["options","editable"],
  components:{
    draggable
  },
  data() {
    return {
      newOptions: {},
      colorGroup: [
        {
          adImg: "",
          adLink: ""
        }
      ]
    };
  },
  computed: {
    dragOptions() {
      return {
        animation: 200,
        group: "description",
        disabled: false,
        ghostClass: "ghost"
      };
    }
  },
  created() {
    let _this = this;
    console.log(this.editable)
    _this.init(_this.options);
  },
  watch: {
    options() {
      let _this = this;
      _this.newOptions = _this.options;
      _this.init(_this.newOptions);
    },
    editable(){
      console.log(this.editable)
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
        _this.colorGroup = optionsParams.colorGroup;
      }
    },
    // 恢复初始状态
    restore() {
      let _this = this;
      _this.colorGroup = [
        {
          adImg: "",
          adLink: ""
        }
      ];
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

    //删除广告图片
    deleteColor(index) {
      let _this = this;
      _this.colorGroup.splice(index, 1);
      _this.changeForm();
    },

    // 添加广告图片

    addAd() {
      let _this = this;
      // 判断数量
      let length = _this.colorGroup.length;
      if (length >= 10) {
        _this.$Message.error("不能超过10个");
        return false;
      }
      _this.colorGroup.push({
        adImg: "",
        adLink: ""
      });
      _this.changeForm();
    },

    // 点击显示图片的选择弹框
    showImgSelect(index) {
      let _this = this;
      _this.changeForm(index + 1);
      let changeData = {
        openImg: true,
        mode: "diy",
        type: "image",
        id: "banner"
      };
      _this.$emit("listenToOpenImgSelect", changeData);
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
    },

    // 更换位置
    changeDisplay(e){
      this.changeForm();
    }
  }
};
</script>
