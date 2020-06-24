<template>
  <div>
    <el-upload
      class="upload-demo"
      ref="upload"
      :action="coverUrl"
      accept=".jpg, .png"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :file-list="fileList"
      :limit="maxCount"
      :auto-upload="false"
      :on-exceed="handleExceed"
      list-type="picture"
    >
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
    </el-upload>
  </div>
</template>
<script>
import axios from "axios";
import { getToken, get } from "@/utils/auth";
export default {
  name: "multiUpload",
  props: {
    //图片属性数组
    value: Array,
    //最大上传图片数量
    maxCount: {
      type: Number,
      default: 5
    }
  },
  data() {
    return {
      coverUrl:
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/uploadLocal",
      dialogVisible: false,
      dialogImageUrl: null,
      file: {}
    };
  },
  computed: {
    fileList() {
      let fileList = [];
      for (let i = 0; i < this.value.length; i++) {
        fileList.push({ url: this.value[i] });
      }
      return fileList;
    }
  },
  methods: {
    submitUpload() {
      this.$refs.upload.submit();
      // const fd = new FormData();
      // fd.append("file", this.file);
      
      // axios({
      //   method: "POST",
      //   url:
      //     process.env.BASE_API +
      //     "/merchat/merchatBusinessMaterials/imageUpload",
      //   headers: {
      //     "Content-Type": "multipart/form-data",
      //     Authorization: getToken()
      //   },
      //   data: fd
      // })
      //   .then(response => {
      //     if (response.data.code == 200) {
      //       this.$message.success(response.data.msg);
      //       this.emitInput(response.data.data);
      //     } else {
      //       this.$message.error(response.data.msg);
      //     }
      //   })
      //   .catch(function(err) {
      //     console.log(err);
      //   });
    },
    emitInput(fileList) {
      let value = [];
      for (let i = 0; i < fileList.length; i++) {
        value.push(fileList[i].url);
      }
      this.$emit("input", value);
    },
    handleRemove(file, fileList) {
      this.emitInput(fileList);
    },
    handlePreview(file) {
      this.dialogVisible = true;
      this.dialogImageUrl = file.url;
    },
    handleUploadSuccess(res, file) {
      this.$message({
        message: res.msg,
        type: "success",
        duration: 1000
      });

      console.log(res)
      console.log(file)

      this.fileList.push({
        name: file.name,
        url: file.response.data
      });
      this.emitInput(this.fileList);
    },
    handleExceed(files, fileList) {
      this.$message({
        message: "最多只能上传" + this.maxCount + "张图片",
        type: "warning",
        duration: 1000
      });
    }
  }
};
</script>
<style>
</style>


