<template>
  <el-card class="form-container" shadow="never">
    <el-steps :active="active" finish-status="success">
      <el-step title="设置微信小程序信息"></el-step>
      <el-step title="新建版本"></el-step>
    </el-steps>

    <el-form
      :model="wxappData"
      :rules="rules"
      ref="wxappFrom"
      label-width="150px"
      v-show="showStatus[0]"
    >
      <el-form-item label="微信小程序名称" prop="name">
        <el-input v-model="wxappData.name" placeholder="请填写微信小程序名称" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="微信账号" prop="account">
        <el-input v-model="wxappData.account" placeholder="请填写微信账号" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="类型" prop="level">
        <el-select v-model="wxappData.level" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="小程序状态" prop="status">
        <el-radio-group v-model="wxappData.status">
          <el-radio-button label="0">关闭</el-radio-button>
          <el-radio-button label="1">开启</el-radio-button>
          <!-- <el-radio-button :label="2">已连接</el-radio-button>
          <el-radio-button :label="3">连接失败</el-radio-button>-->
        </el-radio-group>
      </el-form-item>

      <el-form-item label="token" prop="token">
        <el-input v-model="wxappData.token" placeholder="请填写随机生成的密钥" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="encodingaeskey" prop="encodingaeskey">
        <el-input v-model="wxappData.encodingaeskey" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="原始Id" prop="original">
        <el-input v-model="wxappData.original" placeholder="请填写原始Id" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="AppId" prop="key">
        <el-input v-model="wxappData.key" placeholder="请填写微信公众平台后台的AppId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="AppSecret" prop="secret">
        <el-input
          v-model="wxappData.secret"
          placeholder="请填写微信公众平台后台的AppSecret"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="头像" prop="logo">
        <single-upload-img v-model="wxappData.logo"></single-upload-img>
      </el-form-item>

      <el-form-item label="二维码" prop="qrCode">
        <single-upload-img v-model="wxappData.qrCode"></single-upload-img>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="nextStep('wxappFrom')" v-show="showStatus[0]">下一步</el-button>
      </el-form-item>
    </el-form>

    <el-form
      v-show="showStatus[1]"
      :model="wxappVData"
      :rules="rules1"
      ref="wxappVFrom"
      label-width="150px"
    >
      <el-form-item label="微信小程序名称" prop="name">
        <el-input
          :disabled="true"
          v-model="wxappVData.name"
          placeholder="请填写微信小程序名称"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="版本描述" prop="versionDescribed">
        <el-input
          v-model="wxappVData.versionDescribed"
          placeholder="请填写版本描述"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="版本号" prop="version">
        <el-input v-model="wxappVData.version" placeholder="请填写微信小程序版本号" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="添加应用" prop="icon">
        <single-upload-img v-model="wxappVData.icon"></single-upload-img>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="prevStep" v-show="showStatus[1]">上一步</el-button>
        <el-button type="primary" @click="onSubmit('wxappVFrom')" v-show="showStatus[1]">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createWxapp,
  updateWxapp,
  getWxapp,
  createWxappV,
  updateWxappV,
  getWxappV
} from "@/api/dealer/miniProgramOfficialAccount";
import { get } from "@/utils/auth";
import SingleUploadImg from "@/components/Upload/singleUploadPublic";
import guide01 from "@/assets/images/guide-01.png";
import guide02 from "@/assets/images/guide-02.png";
import guide03 from "@/assets/images/guide-03.png";

const defaultWxappData = {
  status: "1"
};
const defaultWxappVData = {};
export default {
  name: "relatedOfficialAccount",
  props: {
    dealerId: Number
  },
  components: {
    SingleUploadImg
  },
  data() {
    return {
      options: [
        {
          value: "1",
          label: "普通服务号"
        },
        {
          value: "2",
          label: "认证服务号"
        }
      ],
      isEdit: null,
      isEditV: null,
      active: 0,
      showStatus: [true, false],
      wxappData: Object.assign({}, defaultWxappData),
      wxappVData: Object.assign({}, defaultWxappVData),
      rules: {
        account: [
          { required: true, message: "请填写小程序账号", trigger: "blur" }
        ],
        level: [
          { required: true, message: "请选择小程序类型", trigger: "blur" }
        ],
        status: [{ required: true, message: "请选择小程序状态", trigger: "blur" }],
        // token: [{ required: true, message: "请输入token", trigger: "blur" }],
        encodingaeskey: [
          { required: true, message: "请输入encodingaeskey", trigger: "blur" }
        ],
        name: [
          { required: true, message: "请填写小程序名称", trigger: "blur" }
        ],
        original: [
          { required: true, message: "请输入原始Id", trigger: "blur" }
        ],
        key: [
          {
            required: true,
            message: "请填写微信公众平台后台的AppId",
            trigger: "blur"
          }
        ],
        secret: [
          {
            required: true,
            message: "请填写微信公众平台后台的AppSecret",
            trigger: "blur"
          }
        ],
        logo: [{ required: true, message: "请上传头像", trigger: "blur" }],
        qrCode: [{ required: true, message: "请上传二维码", trigger: "blur" }]
      },
      rules1: {
        name: [
          { required: true, message: "请填写小程序名称", trigger: "blur" }
        ],
        versionDescribed: [
          { required: true, message: "请填写小程序的版本描述", trigger: "blur" }
        ],
        version: [
          {
            required: true,
            message: "请填写微信小程序的版本号",
            trigger: "blur"
          }
        ],
        secret: [
          {
            required: true,
            message: "请填写微信公众平台后台的AppSecret",
            trigger: "blur"
          }
        ],
        logo: [{ required: true, message: "请上传头像", trigger: "blur" }],
        qrCode: [{ required: true, message: "请上传二维码", trigger: "blur" }]
      }
    };
  },
  created() {
    if (this.dealerId) {
      this.getWxapp();
      this.getWxappV();
    }
  },
  methods: {
    getWxapp() {
      getWxapp(this.dealerId).then(response => {
        if (response.data != null) {
          this.isEdit = true;
          this.wxappData = response.data;
          this.wxappData.level = String(this.wxappData.level);
          this.wxappData.status = Number(this.wxappData.status);
          this.wxappVData.name = this.wxappData.name;
        } else {
          this.isEdit = false;
          this.wxappData = Object.assign({}, defaultWxappData);
        }
      });
    },
    getWxappV() {
      getWxappV(this.dealerId).then(response => {
        if (response.data != null) {
          this.isEditV = true;
          this.wxappVData = response.data;
        } else {
          this.isEditV = false;
          this.wxappVData = Object.assign({}, defaultWxappVData);
        }
      });
    },
    nextStep(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              // this.wxappData.storeName = get("storeName");
              updateWxapp(this.wxappData.acid, this.wxappData).then(
                response => {
                  if (response.code == 200) {
                    // this.$refs[formName].resetFields();
                    this.$message({
                      message: "修改成功",
                      type: "success",
                      duration: 1000
                    });
                    this.getWxapp();
                    if (this.active < this.showStatus.length - 1) {
                      this.active++;
                      this.hideAll();
                      this.showStatus[this.active] = true;
                    }
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
              this.wxappData.createBy = this.dealerId;
              this.wxappData.dealerId = this.dealerId;
              // this.wxappData.storeId = get("storeId");
              this.wxappData.storeName = get("storeName");
              createWxapp(this.wxappData).then(response => {
                if (response.code == 200) {
                  this.$message({
                    message: "提交成功",
                    type: "success",
                    duration: 1000
                  });
                  this.getWxapp();
                  if (this.active < this.showStatus.length - 1) {
                    this.active++;
                    this.hideAll();
                    this.showStatus[this.active] = true;
                  }
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
    prevStep() {
      if (this.active > 0 && this.active < this.showStatus.length) {
        this.active--;
        this.hideAll();
        this.showStatus[this.active] = true;
      }
    },
    hideAll() {
      for (let i = 0; i < this.showStatus.length; i++) {
        this.showStatus[i] = false;
      }
    },
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEditV) {
              updateWxappV(this.$route.query.id, this.wxappVData).then(
                response => {
                  if (response.code == 200) {
                    this.getWxappV();
                    this.$message({
                      message: "修改成功",
                      type: "success",
                      duration: 1000
                    });
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
              this.wxappVData.createBy = this.dealerId;
              this.wxappVData.dealerId = this.dealerId;
              this.wxappVData.uniacid = this.wxappData.acid;
              // this.wxappVData.storeId = get("storeId");
              this.wxappVData.storeName = get("storeName");
              createWxappV(this.wxappVData).then(response => {
                if (response.code == 200) {
                  this.getWxappV();
                  this.$message({
                    message: "提交成功",
                    type: "success",
                    duration: 1000
                  });
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
    }
  }
};
</script>
<style>
</style>


