<template>
  <div style="text-align:center">
    <van-form @submit="onSubmit">
      <van-field
        v-model="username"
        name="用户名"
        label="用户名"
        placeholder="用户名"
        :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
        v-model="password"
        type="password"
        name="密码"
        label="密码"
        placeholder="密码"
        :rules="[{ required: true, message: '请填写密码' }]"
      />
      <div style="margin: 16px;">
        <van-button round block type="info" native-type="submit">登录</van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
export default {
  props: ["options"],
  data() {
    return {
      newOptions: {},
      username: '',
      password: '',
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
        _this.udPadding = options.udPadding || 0;
        _this.lrPadding = options.lrPadding || 0;
      }
    },
    // 点击tab
    clickTab(index) {
      this.current = index;
    },
    // 恢复初始
    restore() {
      let _this = this;
      _this.btnName = "按钮";
      _this.type = "default";
      _this.size = "normal";
      _this.udPadding = 0;
      _this.lrPadding = 0;
    },
    onSubmit(values) {
      console.log('submit', values);
    },
  }
};
</script>
