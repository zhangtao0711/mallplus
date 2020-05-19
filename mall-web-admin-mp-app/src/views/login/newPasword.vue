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

    <div style="margin-top:20px">
      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="密码" prop="pass">
          <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <!-- <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button> -->
        </el-form-item>
      </el-form>
    </div>

    <el-button type="info" style="margin-top: 12px;" @click="prev">上一步</el-button>
    <el-button type="primary" style="margin-top: 12px;" @click="submitForm('ruleForm')">下一步</el-button>
  </div>
</template>

<script>
export default {
  name: "newPasword",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      active: 2,
      ruleForm: {
        pass: "",
        checkPass: "",
      },
      rules: {
        pass: [{ validator: validatePass, trigger: "blur" }],
        checkPass: [{ validator: validatePass2, trigger: "blur" }],
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$router.push("/comPlete");
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    // 上一步
    prev() {
      this.$router.go(-1);
    }
    // 下一步
    // next2() {
    //   this.$router.push("/CertionEnt");
    // }
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
