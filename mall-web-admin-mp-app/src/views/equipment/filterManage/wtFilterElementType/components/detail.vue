<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtFilterElementType"
      :rules="rules"
      ref="WtFilterElementTypeFrom"
      label-width="160px"
    >
      <el-form-item label="滤芯类型名称" prop="filterElementTypeName">
        <el-input v-model="wtFilterElementType.filterElementTypeName" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="更换周期（天数）" prop="changeCycle">
        <el-input-number v-model="wtFilterElementType.changeCycle" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="更换滤芯提前提醒天数" prop="remindDay">
        <el-input-number v-model="wtFilterElementType.remindDay" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="净水总量（吨）" prop="purifierTotal">
        <el-input-number v-model="wtFilterElementType.purifierTotal" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtFilterElementTypeFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtFilterElementTypeFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createWtFilterElementType,
  getWtFilterElementType,
  updateWtFilterElementType
} from "@/api/water/wtFilterElementType";
import { get } from "@/utils/auth";

const defaultWtFilterElementType = {
  name: ""
};
export default {
  name: "WtFilterElementTypeDetail",
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      wtFilterElementType: Object.assign({}, defaultWtFilterElementType),
      rules: {
        filterElementTypeName: [
          { required: true, message: "请输入滤芯名称名称", trigger: "blur" }
        ],
        changeCycle: [
          { required: true, message: "请输入更换周期", trigger: "blur" },
          { type: "number", message: "更换周期必须为数字", trigger: "blur" }
        ],
        remindDay: [
          {
            required: true,
            message: "请输入更换滤芯提前提醒天数",
            trigger: "blur"
          },
          { type: "number", message: "更换滤芯提前提醒天数必须为数字" }
        ],
        purifierTotal: [
          {
            required: true,
            message: "请输入净水总量",
            trigger: "blur"
          },
          { type: "number", message: "净水总量必须为数字" }
        ]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getWtFilterElementType(this.$route.query.id).then(response => {
        this.wtFilterElementType = response.data;
      });
    } else {
      this.wtFilterElementType = Object.assign({}, defaultWtFilterElementType);
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
              updateWtFilterElementType(
                this.$route.query.id,
                this.wtFilterElementType
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
              this.wtFilterElementType.delFlag = 1;
              this.wtFilterElementType.createBy = get("userID");
              this.wtFilterElementType.dealerId = get("userID");
              createWtFilterElementType(this.wtFilterElementType).then(
                response => {
                  if (response.code == 200) {
                    this.$refs[formName].resetFields();
                    this.wtFilterElementType = Object.assign(
                      {},
                      defaultWtFilterElementType
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
      this.wtFilterElementType = Object.assign({}, defaultWtFilterElementType);
    }
  }
};
</script>
<style>
</style>


