<template>
  <el-card class="form-container" shadow="never">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="业务申请编号"></el-step>
      <!-- <el-step title="超级管理员信息"></el-step>
      <el-step title="主体资料"></el-step>
      <el-step title="经营资料"></el-step>
      <el-step title="结算规则"></el-step>
      <el-step title="结算银行账户"></el-step>
      <el-step title="补充材料"></el-step> -->
    </el-steps>

    <product-info-detail v-show="showStatus[0]" :is-edit="isEdit" @nextStep="nextStep"></product-info-detail>

  </el-card>
</template>
<script>
import {
  createMerchatBusinessMaterials,
  getMerchatBusinessMaterials,
  updateMerchatBusinessMaterials
} from "@/api/merchat/merchatBusinessMaterials";
import SingleUpload from "@/components/Upload/singleUpload";
// import ProductInfoDetail from "./ProductInfoDetail";

export default {
  name: "MerchatBusinessMaterialsDetail",
  components: { SingleUpload, ProductInfoDetail },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      merchatBusinessMaterials: Object.assign(
        {},
        defaultMerchatBusinessMaterials
      ),
      rules: {
        businessCode: [
          { required: true, message: "请输入业务申请编号", trigger: "blur" }
        ]
      },
        active: 0,
        showStatus: [true, false, false, false]
    };
  },
  created() {
    if (this.isEdit) {
      getMerchatBusinessMaterials(this.$route.query.id).then(response => {
        this.merchatBusinessMaterials = response.data;
      });
    } else {
      this.merchatBusinessMaterials = Object.assign(
        {},
        defaultMerchatBusinessMaterials
      );
    }
  },
  methods: {
      hideAll() {
        for (let i = 0; i < this.showStatus.length; i++) {
          this.showStatus[i] = false;
        }
      },
      nextStep() {
        if (this.active < this.showStatus.length - 1) {
          this.active++;
          this.hideAll();
          this.showStatus[this.active] = true;
        }
      },
  }
};
</script>
<style></style>
