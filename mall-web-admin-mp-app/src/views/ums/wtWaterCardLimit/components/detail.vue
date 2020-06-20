<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtWaterCardLimit"
      :rules="rules"
      ref="WtWaterCardLimitFrom"
      label-width="150px"
    >
      <el-form-item label="卡号" prop="cardNo">
        <el-input v-model="wtWaterCardLimit.cardNo" style="width: 370px;" />
      </el-form-item>

      <!-- <el-form-item label="允许使用的设备号" prop="eqcode">
        <el-input v-model="wtWaterCardLimit.eqcode" style="width: 370px;" />
      </el-form-item>-->

      <el-form-item label="备注" prop="remarks">
        <el-input v-model="wtWaterCardLimit.remarks" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardLimitFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtWaterCardLimitFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createWtWaterCardLimit,
  getWtWaterCardLimit,
  updateWtWaterCardLimit
} from "@/api/water/wtWaterCardLimit";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWaterCardLimit = {};
export default {
  name: "WtWaterCardLimitDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      wtWaterCardLimit: Object.assign({}, defaultWtWaterCardLimit),
      rules: {
        cardNo: [
          { required: true, message: "请输入会员卡号", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getWtWaterCardLimit(this.$route.query.id).then(response => {
        this.wtWaterCardLimit = response.data;
      });
    } else {
      this.wtWaterCardLimit = Object.assign({}, defaultWtWaterCardLimit);
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
              updateWtWaterCardLimit(
                this.$route.query.id,
                this.wtWaterCardLimit
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
              this.wtWaterCardLimit.dealerId = get('userId')
              this.wtWaterCardLimit.createBy = get('userId')
              createWtWaterCardLimit(this.wtWaterCardLimit).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtWaterCardLimit = Object.assign(
                    {},
                    defaultWtWaterCardLimit
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
      this.wtWaterCardLimit = Object.assign({}, defaultWtWaterCardLimit);
    }
  }
};
</script>
<style>
</style>


