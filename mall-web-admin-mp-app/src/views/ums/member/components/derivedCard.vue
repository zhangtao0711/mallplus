<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="umsDerived" :rules="rules" ref="derivedFrom" label-width="150px">
      <el-form-item label="卡类型" prop="cardType">
        <el-radio-group v-model="umsDerived.cardType">
          <el-radio label="0">实体卡</el-radio>
          <el-radio label="1">虚拟卡</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="卡号">
        <el-input placeholder="请输入会员卡号" v-model="umsDerived.cardNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="用户">
        <el-input placeholder="请输入会员账号" v-model="umsDerived.umsMemberName" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="会员卡状态">
        <el-radio-group v-model="umsDerived.state">
          <el-radio label="2">已挂失</el-radio>
          <el-radio label="0">正常</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="设备编号">
        <el-input placeholder="请输入设备编号" v-model="umsDerived.eqcode" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('derivedFrom')">导出</el-button>
        <el-button v-if="!isEdit" @click="resetForm('derivedFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
const defaultumsDerived = {
  cardNo: "",
  umsMemberName: "",
  eqcode: "",
  state: ""
};
export default {
  name: "DerivedCard",
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      umsDerived: Object.assign({}, defaultumsDerived),
      rules: {
        cardType: [{ required: true, message: "请选择卡状态", trigger: "blur" }]
      }
    };
  },
  created() {},
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否导出数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            window.open(
              process.env.BASE_API +
                "/water/wtWaterCard/exportExcelCard?cardType=" +
                this.umsDerived.cardType +
                "&cardNo=" +
                this.umsDerived.cardNo +
                "&umsMemberName=" +
                this.umsDerived.umsMemberName +
                "&eqcode=" +
                this.umsDerived.eqcode +
                "&state=" +
                this.umsDerived.state
            );
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
      this.umsDerived = Object.assign({}, defaultumsDerived);
    }
  }
};
</script>
<style>
</style>


