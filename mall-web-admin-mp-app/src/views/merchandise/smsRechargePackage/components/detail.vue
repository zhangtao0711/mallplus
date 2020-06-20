<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="smsRechargePackage"
      :rules="rules"
      ref="SmsRechargePackageFrom"
      label-width="150px"
    >
      <el-form-item label="充值条件" prop="termFee">
        <el-input v-model="smsRechargePackage.termFee" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="赠送金额" prop="donateFee">
        <el-input v-model="smsRechargePackage.donateFee" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="实际到账金额" prop="actualFee">
        <el-input v-model="smsRechargePackage.actualFee" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="赠品名称" prop="giftOne">
        <el-input v-model="smsRechargePackage.giftOne" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="赠品名称路径" prop="giftOneUrl">
        <single-upload-public v-model="smsRechargePackage.giftOneUrl"></single-upload-public>
      </el-form-item>

      <el-form-item label="赠品名称" prop="giftTwo">
        <el-input v-model="smsRechargePackage.giftTwo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="赠品名称路径" prop="giftTwoUrl">
        <single-upload-public v-model="smsRechargePackage.giftTwoUrl"></single-upload-public>
      </el-form-item>

      <el-form-item label="赠品名称" prop="giftThree">
        <el-input v-model="smsRechargePackage.giftThree" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="赠品名称路径" prop="giftThreeUrl">
        <single-upload-public v-model="smsRechargePackage.giftThreeUrl"></single-upload-public>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('SmsRechargePackageFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('SmsRechargePackageFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createSmsRechargePackage,
  getSmsRechargePackage,
  updateSmsRechargePackage
} from "@/api/sms/smsRechargePackage";
import SingleUploadPublic from "@/components/Upload/singleUploadPublic";

const defaultSmsRechargePackage = {};
export default {
  name: "SmsRechargePackageDetail",
  components: { SingleUploadPublic },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      smsRechargePackage: Object.assign({}, defaultSmsRechargePackage),
      rules: {
        termFee: [
          { required: true, message: "请输入充值条件", trigger: "blur" }
        ],
        donateFee: [
          { required: true, message: "请输入赠送金额", trigger: "blur" }
        ],
        actualFee: [
          { required: true, message: "请输入实际到账金额", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getSmsRechargePackage(this.$route.query.id).then(response => {
        this.smsRechargePackage = response.data;
      });
    } else {
      this.smsRechargePackage = Object.assign({}, defaultSmsRechargePackage);
    }
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              updateSmsRechargePackage(
                this.$route.query.id,
                this.smsRechargePackage
              ).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                  this.$router.back();
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            } else {
              createSmsRechargePackage(this.smsRechargePackage).then(
                response => {
                  if (response.code == 200) {
                    this.$refs[formName].resetFields();
                    this.smsRechargePackage = Object.assign(
                      {},
                      defaultSmsRechargePackage
                    );
                    this.$message({
                      message: "提交成功",
                      type: "success",
                      duration: 1000
                    });
                    this.$router.back();
                  } else {
                    this.$message({
                      message: response.msg,
                      type: "error",
                      duration: 1000
                    });
                  }
                }
              );
            }
          });
        } else {
          this.$message({
            message: "验证失败",
            type: "error",
            duration: 1000
          });
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.smsRechargePackage = Object.assign({}, defaultSmsRechargePackage);
    }
  }
};
</script>
<style>
</style>


