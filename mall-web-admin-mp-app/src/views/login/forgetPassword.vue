<template>
  <div class="header">
    <p style="text-align:center;font-size:18px">
      <b>选择找回方式</b>
    </p>
    <!-- 手机号验证 -->
    <el-row class="elrow1" @click.native.prevent="retrieve">
      <el-col :span="1">
        <i class="el-icon-user-solid" style="font-size:45px;color:#1162d7"></i>
      </el-col>
      <el-col :span="20" class="fotsz">
        <span>手机号验证找回</span>
        <br />
        <span class="coll">您需要手机号码短信验证</span>
      </el-col>
      <el-col :span="1">
        <i class="el-icon-arrow-right el-icon--right" style="font-size:45px;color:#ccc"></i>
      </el-col>
    </el-row>
    <!-- 联系管理员找回 -->
    <el-row class="elrow1" @click.native.prevent="contact" style="margin-top:20px">
      <el-col :span="1">
        <i class="el-icon-lock" style="font-size:45px;color:#1162d7"></i>
      </el-col>
      <el-col :span="20" class="fotsz">
        <span>联系管理员找回</span>
        <br />
        <span class="coll">您需要下线联系管理员进行认证找回</span>
      </el-col>
      <el-col :span="1">
        <i class="el-icon-arrow-right el-icon--right" style="font-size:45px;color:#ccc"></i>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "forgetPassword",
  data() {
    return {};
  },
  methods: {
    retrieve() {
      this.$router.push({ path: "/retrievePassword" });
    },    
    contact() {
      this.$router.push({ path: "/contactAdmin" });
    },
  }
};
</script>

<style scoped>
.elrow1 {
  border: 1px solid #ccc;
  width: 1000px;
  padding: 20px;
  cursor: pointer;
}
.fotsz {
  padding-left: 10px;
  line-height: 25px;
}
.coll {
  padding-bottom: 15px;
  color: #666;
  font-size: 12px;
}
.header {
  width: 1024px;
  margin: auto;
  margin-top: 150px;
}
</style>
