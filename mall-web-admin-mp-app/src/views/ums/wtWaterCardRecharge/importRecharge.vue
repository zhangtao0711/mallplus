<template>
  <el-card class="form-container" shadow="never">
    <h3>批量导入充值</h3>

    <!-- <el-radio-group v-model="cardType">
      <el-radio :label="0">实体卡</el-radio>
      <el-radio :label="1">虚拟卡</el-radio>
    </el-radio-group> -->

    <el-upload
      :headers="headers"
      class="upload-demo"
      :action="coverUrl"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :multiple="false"
      :limit="1"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">请上传文件，且不超过500kb</div>
    </el-upload>
  </el-card>
</template>
<script>
import { createImportExcel } from "@/api/water/wtWaterCardRecharge";
import { get, getToken } from "@/utils/auth";
export default {
  name: "backstageRecharge",
  data() {
    return {
      headers: {},
      cardType: "",
      coverUrl:
        // process.env.BASE_API + "water/wtWaterCardCreate/importExcel"
        process.env.BASE_API + "/water/wtWaterCardRecharge/importExcelCreate"
    };
  },
  created() {
    this.headers.storeid = get("storeId");
    this.headers.Authorization = getToken();
  },
  methods: {
    emitInput(val) {
      this.$emit("input", val);
    },
    handleRemove(file, fileList) {
      this.emitInput("");
    },
    handleUploadSuccess(res, file) {
      if (res.code == 500) {
        this.$message({
          message: res.msg,
          type: "error",
          duration: 1000
        });
      } else {
        this.$message({
          message: res.msg,
          type: "success",
          duration: 1000
        });
        this.$router.back();
      }

      this.showFileList = true;
      // this.fileList.pop();
      // this.fileList.push({
      //   name: file.name,
      //   url: file.response.data
      // });
      // this.emitInput(this.fileList[0].url);
    }
  }
};
</script>
<style>
</style>


