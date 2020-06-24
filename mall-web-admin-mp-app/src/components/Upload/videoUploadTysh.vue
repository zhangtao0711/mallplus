<template>
  <div>
    <el-upload
      class="avatar-uploader"
      accept=".avi, .wmv, .mpeg, .mp4, .mov, .mkv, .flv, .f4v, .m4v, .rmvb"
      :action="coverUrl"
      :on-remove="handleRemove"
      :on-progress="uploadVideoProcess"
      :on-success="handleVideoSuccess"
      :before-upload="beforeUploadVideo"
      :show-file-list="false"
      :multiple="false"
    >
      <video
        v-if="videoForm.showVideoPath !='' && !videoFlag"
        v-bind:src="videoForm.showVideoPath"
        class="avatar video-avatar"
        controls="controls"
      >您的浏览器不支持视频播放</video>
      <i
        v-else-if="videoForm.showVideoPath =='' && !videoFlag"
        class="el-icon-plus avatar-uploader-icon"
      ></i>
      <el-progress
        v-if="videoFlag == true"
        type="circle"
        v-bind:percentage="videoUploadPercent"
        style="margin-top:7px;"
      ></el-progress>
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
  },
  data() {
    return {
      videoFlag: false,
      //是否显示进度条
      videoUploadPercent: "",
      //进度条的进度，
      isShowUploadVideo: false,
      //显示上传按钮
      videoForm: {
        showVideoPath: ""
      },
      coverUrl:
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/uploadLocal",
      dialogVisible: false,
      toWechat:
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/videoUpload",
      MediaID: "",
      data: {}
    };
  },
  methods: {
    emitInput(val) {
      this.data.url = val;
      this.$emit("input", this.data);
    },
    handleRemove(file, fileList) {
      this.emitInput("");
    },
    handlePreview(file) {
      this.dialogVisible = true;
    },
    //上传前回调
    beforeUploadVideo(file) {
      var fileSize = file.size / 1024 / 1024 < 20;
      if (
        [
          "video/mp4",
          "video/ogg",
          "video/flv",
          "video/avi",
          "video/wmv",
          "video/rmvb",
          "video/mov",
          "video/mpeg",
          "video/mkv",
          "video/f4v",
          "video/m4v"
        ].indexOf(file.type) == -1
      ) {
        this.$message({
          message: "请上传正确的视频格式",
          type: "warning"
        });
        return false;
      }
      if (!fileSize) {
        this.$message({
          message: "视频大小不能超过20MB",
          type: "warning"
        });
        return false;
      }

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
            this.data.MediaID = response.data.data;
            // this.$message.success(response.data.msg);
          } else {
            this.$message.error(response.data.msg);
          }
        })
        .catch(function(err) {
          console.log(err);
        });

      this.isShowUploadVideo = false;
    },
    //进度条
    uploadVideoProcess(event, file, fileList) {
      this.videoFlag = true;
      this.videoUploadPercent = file.percentage.toFixed(0) * 1;
    },
    //上传成功回调
    handleVideoSuccess(res, file) {
      this.isShowUploadVideo = true;
      this.videoFlag = false;
      this.videoUploadPercent = 0;
      this.videoForm.showVideoPath = process.env.BASE_API + file.response.data
      this.emitInput(file.response.data);

      //后台上传地址
      // if (res.Code == 0) {
      //   this.videoForm.showVideoPath = res.Data;
      // } else {
      //   layer.msg(res.Message);
      // }
    }
  }
};
</script>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>


