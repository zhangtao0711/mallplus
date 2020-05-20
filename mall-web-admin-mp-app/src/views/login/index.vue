<template>
  <div class="bg1">
     <div class="fonts">
      <span class="text1">四两科技管理平台登录页</span>
      <br />
      <span class="text2">Four two science and technology</span>
    </div>
    <el-card class="login-form-layout">
      <el-form
        autocomplete="on"
        :model="loginForm"
        :rules="loginRules"
        ref="loginForm"
        label-position="left"
      >
        <h2 class="login-title color-main">用户登录</h2>
        <el-form-item prop="username">
          <el-input
            name="username"
            type="text"
            v-model="loginForm.username"
            autocomplete="on"
            placeholder="请输入登录ID"
          >
           <i slot="prefix" class="el-input__icon el-icon-user" style="color:#0092ff;font-size:20px"></i>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            name="password"
            :type="pwdType"
            @keyup.enter.native="handleLogin"
            v-model="loginForm.password"
            autocomplete="on"
            placeholder="请输入密码"
          >
            <!-- <span slot="prefix">
              <svg-icon icon-class="password" class="color-main"></svg-icon>
            </span> -->
             <i slot="prefix" class="el-input__icon el-icon-lock" style="color:#0092ff;font-size:20px"></i>
            <span slot="suffix" @click="showPwd">
              <svg-icon icon-class="eye" class="color-main"></svg-icon>
            </span>
          </el-input>
        </el-form-item>
        <el-form-item style="margin-bottom: 30px">
          <el-button
            style="width: 100%"
            type="primary"
            :loading="loading"
            @click.native.prevent="handleLogin"
          >登录</el-button>
        </el-form-item>
        
        <el-form-item style="float: left">
          <el-checkbox-group v-model="type">
            <el-checkbox label="记住密码" name="type"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-button type="text" @click.native.prevent="forgetPassword" style="color:#fff;">忘记密码？</el-button>
        </el-form-item>
        
        <!-- <el-form-item style="margin-bottom: 10px">
          <el-button style="width: 100%" type="primary" @click.native.prevent="handleStore">商家入驻</el-button>
        </el-form-item> -->
      </el-form>
    </el-card>
    <!-- <img :src="login_center_bg" class="login-center-layout" /> -->
    <!-- <el-dialog title="下载地址" :visible.sync="dialogVisible" width="40%">
      <el-form ref="brandFrom" label-width="150px">
        <div class="table-layout">
          <el-row>
            <el-col :span="6" class="table-cell-title">平台账户</el-col>
            <el-col :span="6" class="table-cell-title">商家账户</el-col>
            <el-col :span="6" class="table-cell-title">店员账户</el-col>
          </el-row>
          <el-row>
            <el-col :span="6" class="table-cell">admin/123456</el-col>
            <el-col :span="6" class="table-cell">shangjia/123456</el-col>
            <el-col :span="6" class="table-cell">dianyuan/123456</el-col>
          </el-row>
        </div>
        <el-form-item style="margin-bottom: 10px">
          <el-button style="width: 100%" type="primary" @click.native.prevent="handleGit">下载地址</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>-->
  </div>
</template>

<script>
import Config from "@/settings";
import { isvalidUsername } from "@/utils/validate";
import login_center_bg from "@/assets/images/login_center_bg.png";

export default {
  name: "login",
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!isvalidUsername(value)) {
        callback(new Error("请输入正确的用户名"));
      } else {
        callback();
      }
    };
    const validatePass = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error("密码不能小于3位"));
      } else {
        callback();
      }
    };
    return {
      type: [],
      loginForm: {
        username: "admin",
        password: "123456"
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", validator: validateUsername }
        ],
        password: [{ required: true, trigger: "blur", validator: validatePass }]
      },
      loading: false,
      dialogVisible: true,
      pwdType: "password",
      login_center_bg
    };
  },
  methods: {
    showPwd() {
      if (this.pwdType === "password") {
        this.pwdType = "";
      } else {
        this.pwdType = "password";
      }
    },
    handleReg() {
      this.$router.push({ path: "/reg" });
    },
    handleStore() {
      this.$router.push({ path: "/acceptStore" });
    },
    forgetPassword() {
      this.$router.push({ path: "/forgetPassword" });
    },
    handleGit() {
      window.location.href = "https://gitee.com/zscat/mallplus";
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          console.log("login");
          this.loading = true;
          this.$store
            .dispatch("Login", this.loginForm)
            .then(res => {
              console.log(res);
              this.loading = false;
              this.$router.push({ path: "/" });
            })
            .catch(e => {
              console.log(e);
              this.loading = false;
            });
        } else {
          console.log("参数验证不合法！");
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.login-form-layout {
  position: absolute;
  border: 0;
  left: 0;
  right: 0;
  width: 550px;
  margin: 50px auto;
  background: url("~@/assets/images/frame.png") no-repeat top center;
  background-size: 550px 350px;
}
.login-title {
  text-align: center;
}

.login-center-layout {
  background: #409eff;
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
  margin-top: 200px;
}
.el-input /deep/ .el-input__inner{
  background-color: transparent;
}
.bg1 {
  width: 100%;
  height: 1920px;
  background: url("~@/assets/images/login_center_bg.jpg") no-repeat;
  background-size: 100% 100%;
}
.fonts {
  padding-top: 165px;
  color: #00a1ff;
  width: 100%;
  text-align: center;
}
.text1 {
  letter-spacing: 8px;
  font-size: 28px;
  line-height: 25px;
}
.text2 {
  letter-spacing: 4px;
  font-size: 22px;
  line-height: 25px;
}
.logotext {
  color: #0092ff;
  padding-top: 120px;
}
.el-checkbox{
  color: #fff;
}
</style>
