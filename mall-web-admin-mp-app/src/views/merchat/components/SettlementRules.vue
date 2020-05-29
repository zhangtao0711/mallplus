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
      <el-form-item label="入驻结算规则ID" prop="settlementId">
        <el-input v-model="value.settlementId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="所属行业 " prop="qualificationType">
        <el-input v-model="value.qualificationType" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="特殊资质图片" prop="qualifications">
        <el-input v-model="value.qualifications" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="特殊资质图片" prop="qualificationsMediaId">
        <el-input v-model="value.qualificationsMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        不知道属于什么结算规则、所属行业以及特殊资质？点击这里查看
        <a
          target="_blank"
          href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/applyments/chapter4_1.shtml#menu4"
        >费率结算规则对照表</a>
      </el-form-item>

      <el-form-item label="优惠费率活动ID " prop="activitiesId">
        <el-input v-model="value.activitiesId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="优惠费率活动值" prop="activitiesRate">
        <el-input v-model="value.activitiesRate" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="优惠费率活动补充材料 " prop="activitiesAdditions">
        <el-input v-model="value.activitiesAdditions" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="优惠费率活动补充材料 " prop="activitiesAdditionsMedia">
        <el-input v-model="value.activitiesAdditionsMedia" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        不知道属于什么优惠费率活动？点击这里查看
        <a
          target="_blank"
          href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/applyments/chapter4_1.shtml#menu5"
        >优惠费率活动对照表</a>
      </el-form-item>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写经营资料</el-button>
        <el-button type="primary" size="medium" @click="handleNext('productSaleForm')">下一步，填写结算银行账户</el-button>
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
      rules: {
        settlementId: [
          {
            required: true,
            message: "请填写入驻结算规则ID",
            trigger: "change"
          }
        ],
        qualificationType: [
          { required: true, message: "请填写所属行业", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    if (this.isEdit) {
    } else {
    }
  },
  computed: {},
  methods: {
    chooseOne(e) {
      console.log(e);
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
