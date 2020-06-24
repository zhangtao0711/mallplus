<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="wtWaterCard" :rules="rules" ref="WtWaterCardFrom" label-width="150px">
      <!-- <el-form-item label="生成id" prop="cardCreateId">
        <el-input v-model="wtWaterCard.cardCreateId" style="width: 370px;" />
      </el-form-item>-->

      <el-form-item label="卡号" prop="cardNo">
        <el-input v-model="wtWaterCard.cardNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="姓名" prop="umsMemberName">
        <el-input v-model="wtWaterCard.umsMemberName" style="width: 370px;" />
      </el-form-item>

      <!-- <el-form-item label="登录账号" prop="weixinOpenid">
        <el-input v-model="wtWaterCard.weixinOpenid" style="width: 370px;" />
      </el-form-item>-->

      <el-form-item label="会员等级" prop="memberLevelName">
        <el-input :disabled="true" v-model="wtWaterCard.memberLevelName" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="余额" prop="cardMoney">
        <el-input v-model="wtWaterCard.cardMoney" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="状态" prop="state">
        <el-input :disabled="true" v-model="wtWaterCard.state" />
      </el-form-item>

      <el-form-item label="设备" prop="qrCode">
        <el-input v-model="wtWaterCard.qrCode" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="充值金额" prop="rechargeMoney">
        <el-input-number
          v-model="wtWaterCard.rechargeMoney"
          controls-position="right"
          :min="1"
          style="width: 370px;"
        ></el-input-number>
      </el-form-item>

      <el-form-item label="赠送金额" prop="giveMoney">
        <el-input-number
          v-model="wtWaterCard.giveMoney"
          controls-position="right"
          :min="1"
          style="width: 370px;"
        ></el-input-number>
      </el-form-item>

      <el-form-item label="推荐人" prop="umsMemberReferrerId">
        <el-input v-model="wtWaterCard.umsMemberReferrerId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="公众号" prop="uniacName">
        <el-input v-model="wtWaterCard.uniacName" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtWaterCardFrom')">重置</el-button>
        <el-button type="primary" @click="goback">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createWtWaterCard,
  getWtWaterCard,
  updateWtWaterCard
} from "@/api/water/wtWaterCard";
import SingleUpload from "@/components/Upload/singleUpload";

const defaultWtWaterCard = {};
export default {
  name: "WtWaterCardDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      wtWaterCard: Object.assign({}, defaultWtWaterCard),
      rules: {
        cardNo: [
          { required: true, message: "请输入会员卡号", trigger: "blur" }
        ],
        umsMemberName: [
          { required: true, message: "请输入用户姓名", trigger: "blur" }
        ],
        sort: [{ type: "number", message: "排序必须为数字" }]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getWtWaterCard(this.$route.query.id).then(response => {
        this.wtWaterCard = response.data;
      });
    } else {
      this.wtWaterCard = Object.assign({}, defaultWtWaterCard);
    }
  },
  methods: {
    goback() {
      this.$router.back();
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
              updateWtWaterCard(this.$route.query.id, this.wtWaterCard).then(
                response => {
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
                }
              );
            } else {
              this.wtWaterCard.createBy = get("userId");
              this.wtWaterCard.dealerId = get("userId");
              this.wtWaterCard.carType = '1';
              createWtWaterCard(this.wtWaterCard).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtWaterCard = Object.assign({}, defaultWtWaterCard);
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
      this.wtWaterCard = Object.assign({}, defaultWtWaterCard);
    }
  }
};
</script>
<style>
</style>


