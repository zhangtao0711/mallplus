<template>
  <div style="margin-top: 50px">
    <el-form
      :model="value"
      ref="productSaleForm"
      label-width="120px"
      style="width: 600px"
      size="small"
    >
      <el-form-item label="法人开户承诺函" prop="legalPersonCommitment">
        <el-input v-model="value.legalPersonCommitment" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="法人开户承诺函" prop="legalPersonCommitmentMediaId">
        <el-input
          v-model="value.legalPersonCommitmentMediaId"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="法人开户意愿视频" prop="legalPersonVideo">
        <el-input v-model="value.legalPersonVideo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="法人开户意愿视频" prop="legalPersonVideoMediaId">
        <el-input v-model="value.legalPersonVideoMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="补充材料" prop="businessAdditionPics">
        <el-input v-model="value.businessAdditionPics" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="补充材料" prop="businessAdditionPicsMediaId">
        <el-input
          v-model="value.businessAdditionPicsMediaId"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="补充说明 " prop="businessAdditionMsg">
        <el-input v-model="value.businessAdditionMsg" style="width: 370px;" />
      </el-form-item>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写结算银行账号</el-button>
        <el-button type="primary" size="medium" @click="handleFinishCommit">下一步，提交</el-button>
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
      
    };
  },
  created() {
    if (this.isEdit) {
    } else {
    }
  },
  computed: {},
  methods: {
    handlePrev() {
      this.$emit("prevStep");
    },
    handleFinishCommit() {
      this.$emit("finishCommit", this.isEdit);
    }
  }
};
</script>

<style scoped>
.littleMargin {
  margin-top: 10px;
}
</style>
