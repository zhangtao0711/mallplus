<template>
  <el-card class="form-container" shadow="never">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="普通充值" name="first">
        <el-form
          :model="wtWaterCardRecharge"
          :rules="rules"
          ref="WtWaterCardRechargeFrom"
          label-width="150px"
        >
          <el-form-item label="卡号" prop="cardNo">
            <el-input v-model="wtWaterCardRecharge.cardNo" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="充值金额" prop="rechargeMoney">
            <el-input-number v-model="wtWaterCardRecharge.rechargeMoney" :precision="2" :step="0.1" :min="0" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="实收金额" prop="receivedMoney">
            <el-input-number v-model="wtWaterCardRecharge.receivedMoney" :precision="2" :step="0.1" :min="0" style="width: 370px;" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="onSubmit('WtWaterCardRechargeFrom')">提交</el-button>
            <el-button v-if="!isEdit" @click="resetForm('WtWaterCardRechargeFrom')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="套餐充值" name="second">
        <el-form
          :model="wtWaterCardRecharge"
          :rules="rules"
          ref="WtWaterCardRechargeFrom"
          label-width="150px"
        >
          <el-form-item label="卡号" prop="cardNo">
            <el-input v-model="wtWaterCardRecharge.cardNo" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="充值金额" prop="rechargeMoney">
            <el-input-number v-model="wtWaterCardRecharge.rechargeMoney" :precision="2" :step="0.1" :min="0" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="实收金额" prop="receivedMoney">
            <el-input-number v-model="wtWaterCardRecharge.receivedMoney" :precision="2" :step="0.1" :min="0" style="width: 370px;" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="onSubmit('WtWaterCardRechargeFrom')">提交</el-button>
            <el-button v-if="!isEdit" @click="resetForm('WtWaterCardRechargeFrom')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script>
import {
  createWtWaterCardRecharge,
  createSingle,
  getWtWaterCardRecharge,
  updateWtWaterCardRecharge
} from "@/api/water/wtWaterCardRecharge";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWaterCardRecharge = {
  rechargeType: 0
};
export default {
  name: "backstageRecharge",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      activeName: 'first',
      wtWaterCardRecharge: Object.assign({}, defaultWtWaterCardRecharge),
      rules: {
        cardNo: [{ required: true, message: "请输入卡号", trigger: "blur" }],
        rechargeMoney: [
          { required: true, message: "请输入充值金额", trigger: "blur" }
        ],
        receivedMoney: [{ required: true, message: "请输入实收金额" }]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getWtWaterCardRecharge(this.$route.query.id).then(response => {
        this.wtWaterCardRecharge = response.data;
      });
    } else {
      this.wtWaterCardRecharge = Object.assign({}, defaultWtWaterCardRecharge);
    }
  },
  methods: {
    handleClick(tab, event) {
      if (tab.name == 'first') {
        this.wtWaterCardRecharge.rechargeType = 0;
        this.wtWaterCardRecharge.cardNo = ''
        this.wtWaterCardRecharge.rechargeMoney = ''
        this.wtWaterCardRecharge.receivedMoney = ''
      } else if (tab.name == 'second') {
        this.wtWaterCardRecharge.rechargeType = 1;
        this.wtWaterCardRecharge.cardNo = ''
        this.wtWaterCardRecharge.rechargeMoney = ''
        this.wtWaterCardRecharge.receivedMoney = ''
      }
    },
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              updateWtWaterCardRecharge(
                this.$route.query.id,
                this.wtWaterCardRecharge
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
              this.wtWaterCardRecharge.createBy = get("userId");
              this.wtWaterCardRecharge.dealerId = get("storeId");
              createSingle(this.wtWaterCardRecharge).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtWaterCardRecharge = Object.assign(
                    {},
                    defaultWtWaterCardRecharge
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
              });
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
      this.wtWaterCardRecharge = Object.assign({}, defaultWtWaterCardRecharge);
    }
  }
};
</script>
<style>
</style>


