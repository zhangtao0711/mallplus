<template>
  <div style="margin-top: 50px">
    <el-form
      :model="value"
      :rules="rules"
      ref="productSaleForm"
      label-width="120px"
      style="width: 600px"
      size="small"
    >
      <el-form-item label="超级管理员姓名：" prop="contactName">
        <el-input v-model="value.contactName"></el-input>
      </el-form-item>

      <el-form-item label="超级管理员身份证件号码/超级管理员微信openid：" prop="choose">
        <el-radio-group v-model="value.choose" @change="chooseOne">
          <el-radio label="超级管理员身份证件号码"></el-radio>
          <el-radio label="超级管理员微信openid"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item
        label="超级管理员身份证件号码："
        prop="contactIdNumber"
        :rules="[
          { required: IdcardShow, message: '请输入超级管理员身份证件号码', trigger: 'blur' },
          { validator: validateIdCard, trigger: 'change' }
        ]"
        v-show="IdcardShow"
      >
        <el-input v-model="value.contactIdNumber"></el-input>
      </el-form-item>

      <el-form-item
        label="超级管理员微信openid："
        prop="openid"
        :rules="[
          { required: openIdShow, message: '请输入超级管理员微信openid', trigger: 'blur' },
        ]"
        v-show="openIdShow"
      >
        <el-input v-model="value.openid"></el-input>
      </el-form-item>

      <el-form-item label="联系手机：" prop="mobilePhone">
        <el-input v-model="value.mobilePhone"></el-input>
      </el-form-item>

      <el-form-item label="联系邮箱：" prop="contactEmail">
        <el-input v-model="value.contactEmail"></el-input>
      </el-form-item>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写业务申请编号</el-button>
        <el-button type="primary" size="medium" @click="handleNext('productSaleForm')">下一步，填写主体资料</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "ProductSaleDetail",
  props: {
    value: Object,
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      IdcardShow: false,
      openIdShow: false,
      choose: "",
      rules: {
        choose: [
          {
            required: true,
            message: "超级管理员身份证号码”与“超级管理员微信openid”，二选一必填"
          }
        ],
        contactName: [
          { required: true, message: "请输入超级管理员姓名", trigger: "blur" }
        ],
        mobilePhone: [
          { required: true, message: "请输入联系手机", trigger: "blur" },
          {
            pattern: /^1[3456789]\d{9}$/,
            message: "请输入正确的手机号",
            trigger: ["blur", "change"]
          }
        ],
        contactEmail: [
          { required: true, message: "请输入联系邮箱", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ]
      }
    };
  },
  computed: {
    //商品的编号
    superAdminValue() {
      return this.value;
    }
  },
  watch: {
    superAdminValue: function(newValue) {
      if (!this.isEdit) return;
      if (newValue.contactIdNumber) {
        this.value.choose = "超级管理员身份证件号码";
        this.IdcardShow = true;
        this.openIdShow = false;
      } else if (newValue.openid) {
        this.value.choose = "超级管理员微信openid";
        this.IdcardShow = false;
        this.openIdShow = true;
      }
    }
  },
  created() {},
  methods: {
    chooseOne(e) {
      console.log(e)
      if (e == "超级管理员身份证件号码") {
        this.IdcardShow = true;
        this.openIdShow = false;
      } else if (e == "超级管理员微信openid") {
        this.IdcardShow = false;
        this.openIdShow = true;
      }
    },
    handlePrev() {
      this.$emit("prevStep");
    },
    validateIdCard(rule, value, callback) {
      if (this.IdcardShow === true) {
        if (
          new RegExp(
            /(^[1-9]\d{5}(19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/
          ).test(value)
        ) {
          callback();
        } else {
          callback(new Error("请输入正确格式的身份证号"));
        }
      } else {
        callback();
      }
    },
    handleNext(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$emit("nextStep");
        } else {
          this.$message({
            message: "验证失败",
            type: "error",
            duration: 1000
          });
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.littleMargin {
  margin-top: 10px;
}
</style>
