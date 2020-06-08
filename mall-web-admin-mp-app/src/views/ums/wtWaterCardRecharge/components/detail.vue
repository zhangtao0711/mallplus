<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtWaterCardRecharge"
      :rules="rules"
      ref="WtWaterCardRechargeFrom"
      label-width="150px"
    >
      <el-form-item label="充值金额类型" prop="rechargeMoneyType">
        <!-- <el-input v-model="wtWaterCardRecharge.rechargeMoneyType" style="width: 370px;" /> -->

        <el-radio-group v-model="wtWaterCardRecharge.rechargeMoneyType" @change="rechargeMoneyTypeChange">
          <el-radio :label="0">充值金额</el-radio>
          <el-radio :label="1">充值体验金</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="充值类型" prop="rechargeType">
        <!-- <el-input v-model="wtWaterCardRecharge.rechargeType" style="width: 370px;" /> -->
        <el-radio-group v-model="wtWaterCardRecharge.rechargeType" @change="rechargeTypeChange">
          <el-radio :label="0">单卡</el-radio>
          <el-radio :label="1">批量</el-radio>
          <el-radio :label="2">筛选充值</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="起始卡号" prop="startNo">
        <el-input v-model="wtWaterCardRecharge.startNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="终止卡号" prop="endNo">
        <el-input v-model="wtWaterCardRecharge.endNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="卡号" prop="cardNo">
        <el-input v-model="wtWaterCardRecharge.cardNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="充值金额" prop="rechargeMoney">
        <el-input v-model="wtWaterCardRecharge.rechargeMoney" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="实收金额" prop="receivedMoney">
        <el-input v-model="wtWaterCardRecharge.receivedMoney" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="体验金额" prop="experienceMoney">
        <el-input v-model="wtWaterCardRecharge.experienceMoney" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="体验到期日" prop="experienceEndData">
        <el-input v-model="wtWaterCardRecharge.experienceEndData" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="体验金有效天数" prop="experienceEndDay">
        <el-input v-model="wtWaterCardRecharge.experienceEndDay" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="会员卡余额-筛选记号(字典where_mark)" prop="umsBalanceMark">
        <el-input v-model="wtWaterCardRecharge.umsBalanceMark" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="会员卡余额-筛选条件" prop="umsBalanceWhere">
        <el-input v-model="wtWaterCardRecharge.umsBalanceWhere" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="用水频次-筛选记号(字典where_mark)" prop="umsUseMark">
        <el-input v-model="wtWaterCardRecharge.umsUseMark" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="用水频次-筛选条件" prop="umsUseWhere">
        <el-input v-model="wtWaterCardRecharge.umsUseWhere" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="用水频次-筛选周期(字典where_period)" prop="umsUsePeriod">
        <el-input v-model="wtWaterCardRecharge.umsUsePeriod" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="推荐频次-筛选记号(字典where_mark)" prop="umsRecommendMark">
        <el-input v-model="wtWaterCardRecharge.umsRecommendMark" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="推荐频次-筛选条件" prop="umsRecommendWhere">
        <el-input v-model="wtWaterCardRecharge.umsRecommendWhere" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="推荐频次-筛选周期(字典where_period)" prop="umsRecommendPeriod">
        <el-input v-model="wtWaterCardRecharge.umsRecommendPeriod" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="用户社区" prop="umsCommunity">
        <el-input v-model="wtWaterCardRecharge.umsCommunity" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="会员等级" prop="umsMemberLevel">
        <el-input v-model="wtWaterCardRecharge.umsMemberLevel" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardRechargeFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtWaterCardRechargeFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createWtWaterCardRecharge,
  getWtWaterCardRecharge,
  updateWtWaterCardRecharge
} from "@/api/water/wtWaterCardRecharge";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWaterCardRecharge = {
  name: ""
};
export default {
  name: "WtWaterCardRechargeDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      wtWaterCardRecharge: Object.assign({}, defaultWtWaterCardRecharge),
      rules: {
        name: [
          { required: true, message: "请输入品牌名称", trigger: "blur" },
          {
            min: 2,
            max: 140,
            message: "长度在 2 到 140 个字符",
            trigger: "blur"
          }
        ],
        logo: [{ required: true, message: "请输入品牌logo", trigger: "blur" }],
        sort: [{ type: "number", message: "排序必须为数字" }]
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
    rechargeMoneyTypeChange (e) {
        console.log(e)
    },
    rechargeTypeChange (e) {
        console.log(e)
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
              createWtWaterCardRecharge(this.wtWaterCardRecharge).then(
                response => {
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
      this.wtWaterCardRecharge = Object.assign({}, defaultWtWaterCardRecharge);
    }
  }
};
</script>
<style>
</style>


