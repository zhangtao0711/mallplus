<template>
  <div>
    <!-- ref="upload" -->
    <el-upload
      list-type="picture"
      class="upload-demo"
      :action="coverUrl"
      :file-list="fileList"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :multiple="false"
      :http-request="getFile"
    >
          <!-- :auto-upload="false" -->
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
    </el-upload>

    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0].url" alt />
    </el-dialog>
  </div>
</template>
<script>
import axios from "axios";
import { getToken, get } from "@/utils/auth";

export default {
  name: "singleUploadTysh",
  props: {
    value: String
  },
  computed: {
    imageUrl() {
      return this.value;
    },
    imageName() {
      if (this.value != null && this.value !== "") {
        return this.value.substr(this.value.lastIndexOf("/") + 1);
      } else {
        return null;
      }
    },
    fileList() {
      return [
        {
          name: this.imageName,
          url: this.imageUrl
        }
      ];
    },
  },
  data() {
    return {
      file: {},
      coverUrl:
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/imageUpload",
      dialogVisible: false
    };
  },
  methods: {
    getFile(item) {
      this.file = item.file;
    },
    emitInput(val) {
      this.$emit("input", val);
    },
    handleRemove(file, fileList) {
      this.emitInput("");
    },
    handlePreview(file) {
      this.dialogVisible = true;
    },
    handleUploadSuccess(res, file) {
      this.fileList.pop();
      this.fileList.push({
        name: file.name,
        url: file.response.data
      });
      // this.emitInput(this.fileList[0].url);
    },
    submitUpload() {

      const fd = new FormData();
      fd.append("multipartFile", this.file);
      axios({
        method: "POST",
        url:
          process.env.BASE_API +
          "/merchat/merchatBusinessMaterials/imageUpload",
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: getToken()
        },
        data: fd
      })
        .then(response => {
          if (response.data.code == 200) {
            this.$message.success(response.data.msg);
            this.emitInput(response.data.data);
          } else {
            this.$message.error(response.data.msg);
          }
        })
        .catch(function(err) {
          console.log(err);
        });

    },
  }
};
</script>
<style>
</style>


