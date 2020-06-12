<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="sysApaySet" :rules="rules" ref="SysApaySetFrom" label-width="150px">

      <el-form-item label="是否开启" prop="enable">
        <el-radio-group v-model="sysApaySet.enable">
          <el-radio :label="0">否</el-radio>
          <el-radio :label="1">是</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="商户账号（APPID）" prop="mchAppid">
        <el-input v-model="sysApaySet.mchAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="商户号（mchId）" prop="mchId">
        <el-input v-model="sysApaySet.mchId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="证书文件（CERT）" prop="certPath">
        <single-upload-apay v-model="sysApaySet.certPath"></single-upload-apay>
      </el-form-item>

      <el-form-item label="秘钥证书（Key）" prop="keyPath">
        <single-upload-apay v-model="sysApaySet.keyPath"></single-upload-apay>
      </el-form-item>

      <el-form-item label="PKCS12文件（p12格式）" prop="apiclientCert">
        <single-upload-apay v-model="sysApaySet.apiclientCert"></single-upload-apay>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('SysApaySetFrom')">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createSysApaySet,
  getSysApaySet,
  updateSysApaySet
} from "@/api/ums/sysApaySet";
import singleUploadApay from "@/components/Upload/singleUploadApay";
import { get } from "@/utils/auth";
const defaultSysApaySet = {};

export default {
  name: "sysApaySet",
  data() {
    return {
      isEdit: false,
      sysApaySet: Object.assign({}, defaultSysApaySet),
      rules: {
        name: [{ required: true, message: "请输入品牌名称", trigger: "blur" }]
      }
    };
  },
  components: { singleUploadApay },
  created() {
    this.getDetail();
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否要提交修改?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              updateSysApaySet(get("userId"), this.sysApaySet).then(
                response => {
                  if (response.code == 200) {
                    this.$refs[formName].resetFields();
                    this.$message({
                      message: "修改成功",
                      type: "success",
                      duration: 1000
                    });
                    this.$router.back();
                  } else {
                    this.$message({
                      message: response.msg,
                      type: "error",
                      duration: 1000
                    });
                  }
                }
              );
            } else {
              this.sysApaySet.userId = get("userId");
              createSysApaySet(this.sysApaySet).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.sysApaySet = Object.assign({}, defaultSysApaySet);
                  this.$message({
                    message: "提交成功",
                    type: "success",
                    duration: 1000
                  });
                  this.getDetail();
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
            message: "提交参数不合法",
            type: "warning"
          });
          return false;
        }
      });
    },
    getDetail() {
      getSysApaySet(get("userId")).then(response => {
        if (response.data) {
          this.isEdit = true;
          this.sysApaySet = response.data;
        }
      });
    }
  }
};
</script>

