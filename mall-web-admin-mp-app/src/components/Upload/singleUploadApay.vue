<template>
  <div>
    <el-upload
      :action="coverUrl"
      :multiple="false"
      :show-file-list="showFileList"
      :file-list="fileList"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div> -->
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0].url" alt />
    </el-dialog>
  </div>
</template>
<script>
import { policy } from "@/api/oss";

export default {
  name: "singleUploadPublic",
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
        process.env.BASE_API + "/ums/sysApaySet/uploadLocal",
      dialogVisible: false
    };
  },
  methods: {
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
      this.showFileList = true;
      this.fileList.pop();
      this.fileList.push({
        name: file.name,
        url: file.response.data
      });
      this.emitInput(this.fileList[0].url);
    }
  }
};
</script>
<style>
</style>


