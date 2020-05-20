<template>
  <div class="header">
    <p style="font-size:18px;cursor: pointer;" @click="prev">返回上一页</p>
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
      <el-form :label-position="labelPosition" label-width="80px">
        <el-form-item label="手机号：" label-width="110px">
          <el-col style="width:67%;">
            <el-input name="phone" type="text" placeholder="请输入手机号" v-model="phone" />
          </el-col>
        </el-form-item>
        <el-form-item label="短信验证码：" label-width="110px">
          <el-row>
            <el-col style="width:52%;">
              <el-input type="text" v-model="code" placeholder="请输入短信验证码" />
            </el-col>
            <el-col style="width:44%;">
              <el-button type="primary"
                :disabled="disabled"  
                @click="getVerifyCode"              
                class="btns"
              >{{btntxt}}</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>

    <el-button type="primary" style="margin-top: 12px;" :disabled="isClick" @click="next">下一步</el-button>
  </div>
</template>

<script>
export default {
  name: "retrievePassword",
  data() {
    return {
      active: 1,
      btntxt: "免费获取验证码",
      btntext: "重新发送",
      labelPosition: "left",
      phone:"",
      code:"",
      disabled: false,
    };
  },
computed: {
    //手机号和验证码都不能为空
    isClick() {
      if (!this.phone || !this.code) {
        return true;
      } else {
        return false;
      }
    }
  },
  methods: {
     getVerifyCode() {
      //获取验证码
      if (this.sendcode());
      //发送网络请求
      // this.$http({
      //   url: this.$http.adornUrl(``),
      //   method: "post",
      //   params: this.$http.adornParams({
      //     phone: this.phone
      //   })
      // })
      //   .then(({ data }) => {
      //     if (data.code != 0) {
      //       this.disabled = false;
      //       this.$message.warning(data.msg);
      //     } else {
      //       this.timer();
      //     }
      //   })
      //   .catch(e => {
      //     this.disabled = false;
      //     this.$message.warning(e);
      //   });
    },
    //手机号正则
    sendcode() {
      var reg = 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/;
      if (this.phone == "") {
        this.$message.warning("请输入手机号码");
      } else if (!reg.test(this.phone)) {
        this.$message.warning("手机格式不正确");
      } else {
        this.time = 60;
        this.disabled = true;
        return true;
      }
    },
    // 获取验证码
    timer() {
      if (this.time > 0) {
        this.time--;
        this.btntxt = this.time + "s后重新获取";
        setTimeout(this.timer, 1000);
      } else {
        this.time = 0;
        this.btntxt = "获取验证码";
        this.disabled = false;
      }
    },
    // 上一步
    prev() {
      this.$router.go(-1);
    },
    // 下一步
    next() {
      this.$router.push("/newPasword");
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
