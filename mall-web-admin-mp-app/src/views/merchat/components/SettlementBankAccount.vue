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
      <el-form-item label="账户类型" prop="bankAccountType">
        <el-radio-group v-model="value.bankAccountType">
          <el-radio label="BANK_ACCOUNT_TYPE_CORPORATE">对公银行账户</el-radio>
          <el-radio label="BANK_ACCOUNT_TYPE_PERSONAL">经营者个人银行卡</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="开户名称" prop="accountName">
        <el-input v-model="value.accountName" style="width: 370px;" />
        <div class="message">
          <div>1、选择“经营者个人银行卡”时，开户名称必须与“经营者证件姓名”一致。</div>
          <div>2、选择“对公银行账户”时，开户名称必须与营业执照/登记证书的“商户名称”一致。</div>
        </div>
      </el-form-item>

      <el-form-item label="开户银行" prop="accountBank">
        <el-input v-model="value.accountBank" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        详细参见
        <a
          target="_blank"
          href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/applyments/chapter4_1.shtml#menu1"
        >开户银行对照表</a>
      </el-form-item>

      <el-form-item label="开户银行省市编码" prop="bankAddressCode">
        <el-input v-model="value.bankAddressCode" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        详细参见
        <a
          target="_blank"
          href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/applyments/chapter4_1.shtml#menu2"
        >省市区编号对照表</a>
      </el-form-item>

      <el-form-item label="开户银行联行号/开户银行全称（含支行)" prop="choose">
        <el-radio-group v-model="choose" @change="chooseOne">
          <el-radio label="开户银行联行号"></el-radio>
          <el-radio label="开户银行全称（含支行)"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="开户银行联行号" prop="bankBranchId" v-show="bankIDShow"
      :rules="[
      { required: bankIDShow, message: '请填写开户银行联行号', trigger: 'blur' },
    ]">
        <el-input v-model="value.bankBranchId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="开户银行全称（含支行)" prop="bankName" v-show="bankNameShow"
      :rules="[
      { required: bankNameShow, message: '请填写开户银行全称（含支行)', trigger: 'blur' },
    ]">
        <el-input v-model="value.bankName" style="width: 370px;" />
      </el-form-item>

      <el-form-item v-show="bankIDShow || bankNameShow">
        详细参见
        <a
          target="_blank"
          href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/applyments/chapter4_1.shtml#menu3"
        >开户银行全称（含支行）对照表</a>
      </el-form-item>

      <el-form-item label="银行账号" prop="accountNumber">
        <el-input v-model="value.accountNumber" style="width: 370px;" />
      </el-form-item>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写结算规则</el-button>
        <el-button type="primary" size="medium" @click="handleNext('productSaleForm')">下一步，填写补充资料</el-button>
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
      bankIDShow: false,
      bankNameShow: false,
      choose:'',
      rules: {
        choose: [
          {
            required: true,
            message:
              "“开户银行联行号”与“开户银行全称（含支行)”，二选一必填",
            trigger: "change"
          }
        ],
        bankAccountType: [
          {
            required: true,
            message: "请选择账户类型",
            trigger: "change"
          }
        ],
        accountName: [
          { required: true, message: "请填写开户名称", trigger: "blur" }
        ],
        accountBank: [
          { required: true, message: "请填写开户银行", trigger: "blur" }
        ],
        bankAddressCode: [
          { required: true, message: "请填写开户银行省市编码", trigger: "blur" }
        ],
        accountNumber: [
          { required: true, message: "请填写银行账号", trigger: "blur" }
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
      if (e == "开户银行联行号") {
        this.bankIDShow = true;
        this.bankNameShow = false;
      } else if (e == "开户银行全称（含支行)") {
        this.bankIDShow = false;
        this.bankNameShow = true;
      }
    },
    handlePrev() {
      this.$emit("prevStep");
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
a{
  text-decoration: underline;
  color: deepskyblue;
}
.message {
  font-size: 10px;
}
</style>
