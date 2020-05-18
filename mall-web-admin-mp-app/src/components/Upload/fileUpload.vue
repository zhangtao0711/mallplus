<template>
  <div>

    <el-upload
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
  </div>
</template>
<script>
import { policy } from "@/api/oss";
import { uploadFile } from "@/api/merchat/merchatFacilitatorConfig";

export default {
  name: "singleUpload",
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
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/uploadLocal"
    };
  },
  methods: {
    emitInput(val) {
      this.$emit("input", val);
    },
    handleRemove(file, fileList) {
      this.emitInput("");
    },
    handleUploadSuccess(res, file) {
      this.$message({
        message: res.msg,
        type: "success",
        duration: 1000
      });

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


