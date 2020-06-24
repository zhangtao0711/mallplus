<template>
  <div>
    <el-upload
      :action="coverUrl"
      :multiple="false"
      :show-file-list="showFileList"
      :file-list="fileList"
      list-type="picture"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :before-upload="beforeUpload"
      :on-preview="handlePreview"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传图片</div>
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
    value: Object
  },
  computed: {
    imageUrl() {
      return this.value.url;
    },
    imageName() {
      if (this.value.url != null && this.value.url !== "") {
        return this.value.url.substr(this.value.url.lastIndexOf("/") + 1);
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
    showFileList: {
      get: function() {
        return (
          this.value !== null && this.value !== "" && this.value !== undefined
        );
      },
      set: function(newValue) {}
    }
  },
  data() {
    return {
      coverUrl:
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/uploadLocal",
      dialogVisible: false,
      toWechat:
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/imageUpload",
      MediaID: '',
      data: {}
    };
  },
  methods: {
    emitInput(val) {
      this.data.url = val
      this.$emit("input", this.data);
    },
    handleRemove(file, fileList) {
      this.emitInput("");
    },
    beforeUpload(file) {
      const fd = new FormData();
      fd.append("multipartFile", file);
      axios({
        method: "POST",
        url: this.toWechat,
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: getToken()
        },
        data: fd
      })
        .then(response => {
          if (response.data.code == 200) {
            this.data.MediaID = response.data.data
            // this.$message.success(response.data.msg);
          } else {
            this.$message.error(response.data.msg);
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    handlePreview(file) {
      this.dialogVisible = true;
    },
    handleUploadSuccess(res, file) {
      this.showFileList = true;
      this.fileList.pop();
      this.fileList.push({
        name: file.name,
        url: file.response.data
      });

      console.log(res)
      console.log(file.response)

      this.emitInput(this.fileList[0].url);
    }
  }
};
</script>
<style>
</style>


