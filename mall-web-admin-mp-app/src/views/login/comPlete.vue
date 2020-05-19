<template>
  <div class="header">
    <p>
      <span style="color:#1162d7">▎</span>
      <span style="font-size:18px">找回密码</span>
    </p>
    <el-row class="mt-5 pl-5 pr-5 pt-5">
      <!--// -->
      <el-col>
        <el-steps :active="active">
          <el-step title="1.验证手机号码"></el-step>
          <el-step title="2.设置新密码"></el-step>
          <el-step title="3.完成"></el-step>
        </el-steps>
      </el-col>
    </el-row>
    <div class="texts">
      <img src="@/assets/images/success.png" width="150px" />
      <p>修改成功请重新登录</p>
      <el-button type="primary" style="margin-top: 12px;" @click="next">返回登录</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "comPlete",
  data() {
    return {
      active: 3
    };
  },
  methods: {
    next() {
      this.$router.push({ path: "/login" });
    }
  }
};
</script>

<style scoped>
.header {
  width: 1024px;
  margin: auto;
  margin-top: 150px;
}
.texts{
  text-align: center;
  margin-top: 15px;
}
</style>
