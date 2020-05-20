<template>
  <div class="header">
    <p style="font-size:18px;cursor: pointer;" @click="prev">返回上一页</p>
    <p style="padding:10px 0;font-size:18px">管理员基本信息</p>
    <span>姓名：XXX</span>
    <el-divider></el-divider>
    <span>电话:XXX</span>
    <el-divider></el-divider>
    <span>QQ：XXX</span>
    <el-divider></el-divider>
    <span>邮箱：XXX</span>
    <el-divider></el-divider>
    <span>微信：XXX</span>
    <el-divider></el-divider>
  </div>
</template>
<script>
export default {
  name: "contactAdmin",
  methods: {
    // 上一步
    prev() {
      this.$router.go(-1);
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
</style>