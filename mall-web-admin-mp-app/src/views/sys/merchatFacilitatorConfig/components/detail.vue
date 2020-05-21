<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="merchatFacilitatorConfig"
      :rules="rules"
      ref="MerchatFacilitatorConfigFrom"
      label-width="170px"
    >
      <el-form-item label="服务商信息主键" prop="id" v-show="false">
        <el-input v-model="merchatFacilitatorConfig.id" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="用于证书解密的密钥" prop="apiv3key">
        <el-input
          type="password"
          v-model="merchatFacilitatorConfig.apiv3key"
          style="width: 370px;"
          show-password
        />
      </el-form-item>

      <el-form-item label="商户号" prop="mchId">
        <el-input v-model="merchatFacilitatorConfig.mchId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="商户证书" prop="apiclientCert">
        <file-upload v-model="merchatFacilitatorConfig.apiclientCert"></file-upload>

        <el-button type="primary" @click="download(merchatFacilitatorConfig.apiclientCert)">
          下载
          <i class="el-icon-download el-icon--right"></i>
        </el-button>
      </el-form-item>

      <el-form-item label="商户私钥" prop="privateKeyPath">
        <file-upload v-model="merchatFacilitatorConfig.privateKeyPath"></file-upload>

        <el-button type="primary" @click="download(merchatFacilitatorConfig.privateKeyPath)">
          下载
          <i class="el-icon-download el-icon--right"></i>
        </el-button>
      </el-form-item>

      <el-form-item label="商户证书（.p12格式）" prop="apiclientCertP12">
        <file-upload v-model="merchatFacilitatorConfig.apiclientCertP12"></file-upload>
        <el-button type="primary" @click="download(merchatFacilitatorConfig.apiclientCertP12)">
          下载
          <i class="el-icon-download el-icon--right"></i>
        </el-button>
      </el-form-item>

      <el-form-item label="微信支付平台证书" prop="publicKeyPath">
        <el-button
          v-model="merchatFacilitatorConfig.publicKeyPath"
          type="primary"
          @click="downloadW()"
        >
          下载
          <i class="el-icon-download el-icon--right"></i>
        </el-button>

        <!-- <el-input v-model="merchatFacilitatorConfig.publicKeyPath" style="width: 370px;" /> -->
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('MerchatFacilitatorConfigFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('MerchatFacilitatorConfigFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createMerchatFacilitatorConfig,
  getMerchatFacilitatorConfig,
  updateMerchatFacilitatorConfig,
  uploadFile,
  fetchList
} from "@/api/merchat/merchatFacilitatorConfig";
import FileUpload from "@/components/Upload/fileUpload";
import FileDownload from "@/components/download/fileDownload";
import axios from "axios";
import { getToken, get } from "@/utils/auth";

const defaultMerchatFacilitatorConfig = {
  name: ""
};
export default {
  name: "MerchatFacilitatorConfigDetail",
  components: { FileUpload, FileDownload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      id:null,
      listQuery: {
        keyword: null,
        pageNum: 1,
        pageSize: 10
      },
      coverUrl:
        process.env.BASE_API + "/merchat/merchatBusinessMaterials/download",
      coverUrlW:
        process.env.BASE_API +
        "/merchat/merchatFacilitatorConfig/downloadPlatformCredential",
      merchatFacilitatorConfig: Object.assign(
        {},
        defaultMerchatFacilitatorConfig
      ),
      rules: {
        apiv3key: [
          {
            required: true,
            message: "请输入用于证书解密的密钥",
            trigger: "blur"
          }
        ],
        mchId: [{ required: true, message: "请输入商户号", trigger: "blur" }],
        privateKeyPath: [
          { required: true, message: "请上传商户私钥", trigger: "blur" }
        ],
        apiclientCert: [
          { required: true, message: "请上传商户证书", trigger: "blur" }
        ],
        apiclientCertP12: [
          {
            required: true,
            message: "请上传商户证书（.p12格式）",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();

    // if (this.isEdit) {
    //   getMerchatFacilitatorConfig(this.$route.query.id).then(response => {
    //     this.merchatFacilitatorConfig = response.data;
    //   });
    // } else {
    //   this.merchatFacilitatorConfig = Object.assign(
    //     {},
    //     defaultMerchatFacilitatorConfig
    //   );
    // }
  },
  methods: {
    getList() {
      fetchList(this.listQuery).then(response => {
        if (response.data.records.length > 0) {
          this.isEdit = true;
          this.id = response.data.records[0].id
          getMerchatFacilitatorConfig(response.data.records[0].id).then(
            response => {
              this.merchatFacilitatorConfig = response.data;
              this.merchatFacilitatorConfig.updateBy = get("userId")
            }
          );
        } else {
          this.merchatFacilitatorConfig = Object.assign(
            {},
            defaultMerchatFacilitatorConfig
          );
        }
      });
    },
    download(path) {
      axios({
        method: "get",
        url: this.coverUrl,
        params: {
          path: path
        },
        // responseType: "blob",
        headers: {
          contentType: "application/x-www-form-urlencoded; charset=utf-8",
          Authorization: getToken()
        }
      }).then(res => {
        console.log(res);
        // if (!res) {
        //   this.$message.warning("文件下载失败");
        //   return;
        // }

        // let blob = new Blob([res.data]);
        // console.log(blob)

        // let fileName = '证书文件'
        // if (window.navigator.msSaveOrOpenBlob) { // IE10
        //   navigator.msSaveBlob(blob, fileName);
        // } else {
        //   let url = window.URL.createObjectURL(blob);
        //   let link = document.createElement("a");
        //   link.style.display = "none";
        //   link.href = url;
        //   link.setAttribute("download", fileName); // 文件名
        //   document.body.appendChild(link);
        //   link.click();
        //   document.body.removeChild(link); // 下载完成移除元素
        //   window.URL.revokeObjectURL(url); // 释放掉blob对象
        // }
      });
    },

    downloadW() {
      axios({
        method: "post",
        url: this.coverUrlW,
        data: {
          // apiv3key: "65fdd8532fbsc784936909f1a7r5872f",
          // mchId: "1527256251",
          // apiclientCert:
          //   "/opt/merchant/upload/apiclient_cert_1589590742710.pem",
          // privateKeyPath:
          //   "/opt/merchant/upload/apiclient_key_1589590774651.pem",
          // apiclientCertP12:
          //   "/opt/merchant/upload/apiclient_cert_1589590780157.p12"

          apiv3key: this.merchatFacilitatorConfig.apiV3key,
          mchId: this.merchatFacilitatorConfig.mchId,
          apiclientCert: this.merchatFacilitatorConfig.apiclientCert,
          privateKeyPath: this.merchatFacilitatorConfig.privateKeyPath,
          apiclientCertP12: this.merchatFacilitatorConfig.apiclientCertP12
        },
        headers: {
          // contentType: "application/x-www-form-urlencoded; charset=utf-8"
          Authorization: getToken(),
          access_token: getToken()
        }
      }).then(res => {
        console.log(res);
      });
    },

    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              updateMerchatFacilitatorConfig(
                this.id,
                this.merchatFacilitatorConfig
              ).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                  this.getList()
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            } else {
              createMerchatFacilitatorConfig(
                this.merchatFacilitatorConfig
              ).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.merchatFacilitatorConfig = Object.assign(
                    {},
                    defaultMerchatFacilitatorConfig
                  );
                  this.$message({
                    message: "提交成功",
                    type: "success",
                    duration: 1000
                  });
                  this.getList()
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            }
          });
        } else {
          this.$message({
            message: "验证失败",
            type: "error",
            duration: 1000
          });
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.merchatFacilitatorConfig = Object.assign(
        {},
        defaultMerchatFacilitatorConfig
      );
    },
    beforeUpload(file) {
      let isLt2M = true;
      isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过 100MB!");
      }
      return isLt2M;
    },
    handleSuccess(response, file, fileList) {
      this.resetForm();
      this.$refs.upload.clearFiles();
      this.$parent.init();
    },
    // 监听上传失败
    handleError(e, file, fileList) {
      const msg = JSON.parse(e.message);
      this.$notify({
        title: msg.message,
        type: "error",
        duration: 2500
      });
    }
  }
};
</script>
<style>
</style>


