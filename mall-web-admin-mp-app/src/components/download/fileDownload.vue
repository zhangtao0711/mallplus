<template>
  <div>
     <!-- @click="download()" -->
    <el-button type="primary">
      下载
      <i class="el-icon-download el-icon--right"></i>
    </el-button>
  </div>
</template>
<script>
import { policy } from "@/api/oss";
import axios from "axios";

export default {
  name: "fileDownload",
  props: {
    value: String
  },
  computed: {},
  data() {
    return {
      coverUrl:
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/download"
    };
  },
  methods: {
    download() {
      axios({
        method: "get",
        url: this.coverUrl,
        responseType: "blob",
        // header: {
        //   contentType: "application/x-www-form-urlencoded; charset=utf-8"
        // }
      }).then(res => {
        console.log(res)
        if (!res) {
          this.$message.warning("文件下载失败");
          return;
        }

        let blob = new Blob([res.data]);
        console.log(blob)

        let fileName = '证书文件'
        if (window.navigator.msSaveOrOpenBlob) { // IE10
          navigator.msSaveBlob(blob, fileName);
        } else {
          let url = window.URL.createObjectURL(blob);
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", fileName); // 文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); // 下载完成移除元素
          window.URL.revokeObjectURL(url); // 释放掉blob对象
        }
      });
    }
  }
};
</script>
<style>
</style>


