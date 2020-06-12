<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="dealerInfoData" :rules="rules" ref="dealerInfoFrom" label-width="150px">
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="dealerInfoData.nickName" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="商户名称" prop="dealerName">
        <el-input v-model="dealerInfoData.dealerName" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="联系人" prop="realname">
        <el-input v-model="dealerInfoData.realname" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="联系电话" prop="dealerPhone">
        <el-input v-model="dealerInfoData.dealerPhone" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="所属区域">
        <el-select
          style="width: 370px;"
          v-model="dealerInfoData.province"
          @change="getSecondData"
          clearable
          placeholder="请选择所在省"
        >
          <el-option
            v-for="item in parentArea"
            :key="item.id"
            :label="item.extName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select
          style="width: 370px;"
          v-model="dealerInfoData.city"
          @change="getThirdData"
          clearable
          placeholder="请选择所在市"
        >
          <el-option
            v-for="item in secondArea"
            :key="item.id"
            :label="item.extName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select
          style="width: 370px;"
          v-model="dealerInfoData.county"
          clearable
          placeholder="请选择所在区/县"
        >
          <el-option
            v-for="item in thirdArea"
            :key="item.id"
            :label="item.extName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="详细地址" prop="address">
        <el-input v-model="dealerInfoData.address" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="类型" prop="type">
        <el-select v-model="dealerInfoData.type" style="width: 370px;">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>

      <!-- <el-form-item label="是否有售水机">
        <el-radio-group v-model="dealerInfoData.levelId">
          <el-radio label="0">否</el-radio>
          <el-radio label="1">是</el-radio>
        </el-radio-group>
      </el-form-item>-->

      <div v-show="isEdit&&isUniacid">
        <el-form-item label="公众号/小程序名称">
          <el-input v-model="name" :disabled="true" style="width: 370px;" />
        </el-form-item>

        <el-form-item label="总经销商账号" v-show="isgName">
          <el-input v-model="gName" :disabled="true" style="width: 370px;" />
        </el-form-item>

        <el-form-item label="经销商账号" v-show="ispName">
          <el-input v-model="pName" :disabled="true" style="width: 370px;" />
        </el-form-item>

        <el-form-item label="分销商账号" v-show="issName">
          <el-input v-model="sName" :disabled="true" style="width: 370px;" />
        </el-form-item>
      </div>

      <div>
        <h3>申请操作</h3>
        <el-form-item label="申请状态" prop="applyStatus">
          <el-radio-group v-model="dealerInfoData.applyStatus">
            <el-radio label="0">申请中</el-radio>
            <el-radio label="3">驳回申请</el-radio>
            <el-radio label="2">允许入驻</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('dealerInfoFrom')">提交</el-button>
        <el-button @click="goback">返回列表</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  getAreaList,
  getUserInfo,
  updateDealer,
  getOriginByUniacid
} from "@/api/dealer/dealer";
import { get } from "@/utils/auth";

const defaultDealerInfo = {};
export default {
  name: "dealerInfo",
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dealerInfoData: Object.assign({}, defaultDealerInfo),
      name: null,
      gName: null,
      pName: null,
      sName: null,
      isgName: false,
      ispName: false,
      issName: false,
      isUniacid: false,
      options: [
        {
          value: "1",
          label: "个人"
        },
        {
          value: "2",
          label: "个体工商户"
        },
        {
          value: "3",
          label: "企业"
        }
      ],
      parentArea: [],
      secondArea: [],
      thirdArea: [],
      rules: {
        dealerName: [
          { required: true, message: "请输入商户名称", trigger: "blur" }
        ],
        type: [{ required: true, message: "请选择商户类型", trigger: "blur" }],
        realname: [
          { required: true, message: "请输入联系人姓名", trigger: "blur" }
        ],
        dealerPhone: [
          { required: true, message: "请输入联系人电话", trigger: "blur" }
        ],
        applyStatus: [{ required: true, message: "请选择入驻状态" }]
      }
    };
  },
  created() {
    this.getAreaList();
    if (this.isEdit) {
      this.getUserInfo();
    }
  },
  methods: {
    getUserInfo() {
      getUserInfo(this.$route.query.id).then(response => {
        this.dealerInfoData = JSON.parse(
          (
            JSON.stringify(response.data.appletSet) +
            JSON.stringify(response.data.user)
          ).replace(/}{/, ",")
        );
        this.dealerInfoData.type = String(this.dealerInfoData.type);
        this.dealerInfoData.city = Number(this.dealerInfoData.city);
        this.dealerInfoData.county = Number(this.dealerInfoData.county);
        this.dealerInfoData.applyStatus = String(this.dealerInfoData.status);

        if (this.dealerInfoData.province) {
          this.getSecondData(this.dealerInfoData.province);
        }
        if (this.dealerInfoData.city) {
          this.getThirdData(this.dealerInfoData.city);
        }
        if (response.data.user.uniacid) {
          this.isUniacid = true;
          getOriginByUniacid(response.data.user.uniacid).then(response => {
            this.name = response.data.name;

            // 总经销商
            if (response.data.gName) {
              this.gName = response.data.gName;
              this.isgName = true;
            }
            // 经销商
            if (response.data.pName) {
              this.pName = response.data.pName;
              this.ispName = true;
            }
            // 分销商
            if (response.data.sName) {
              this.sName = response.data.sName;
              this.issName = true;
            }
          });
        }
      });
    },

    getAreaList() {
      let param = {
        deep: 0
      };
      getAreaList(param).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          getAreaList({ deep: 0, pageNum: i + 1 }).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.parentArea.push(res.data.records[j]);
            }
          });
        }
      });
    },
    getSecondData(e) {
      this.secondArea = [];
      let param = {
        pid: e
      };
      getAreaList(param).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          getAreaList({ pid: e, pageNum: i + 1 }).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.secondArea.push(res.data.records[j]);
            }
          });
        }
      });
    },
    getThirdData(e) {
      this.thirdArea = [];
      let param = {
        pid: e
      };
      getAreaList(param).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          getAreaList({ pid: e, pageNum: i + 1 }).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.thirdArea.push(res.data.records[j]);
            }
          });
        }
      });
    },
    goback() {
      this.$router.back();
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
              var now = new Date();
              let year = now.getFullYear();
              let month =
                now.getMonth() + 1 < 10
                  ? "0" + (now.getMonth() + 1)
                  : now.getMonth() + 1;
              let day =
                now.getDate() < 10 ? "0" + (now.getDate() - 1) : now.getDate();

              let formData = {
                appletSet: {
                  appid: this.dealerInfoData.appid,
                  appsecret: this.dealerInfoData.appsecret,
                  firstSeparate: this.dealerInfoData.firstSeparate,
                  levelId: this.dealerInfoData.levelId,
                  mchid: this.dealerInfoData.mchid,
                  parentUserId: this.dealerInfoData.parentUserId,
                  receiptAccount: this.dealerInfoData.receiptAccount,
                  secondSeparate: this.dealerInfoData.secondSeparate,
                  thirdSeparate: this.dealerInfoData.thirdSeparate
                },
                user: {
                  address: this.dealerInfoData.address,
                  applyStatus: this.dealerInfoData.applyStatus,
                  applyTime: year + "-" + month + "-" + day,
                  city: this.dealerInfoData.city,
                  county: this.dealerInfoData.county,
                  dealerName: this.dealerInfoData.dealerName,
                  dealerPhone: this.dealerInfoData.dealerPhone,
                  gid: this.dealerInfoData.gid,
                  icon: this.dealerInfoData.icon,
                  pid: this.dealerInfoData.pid,
                  province: this.dealerInfoData.province,
                  realname: this.dealerInfoData.realname,
                  type: this.dealerInfoData.type,
                  username: this.dealerInfoData.dealerPhone,
                  nickName: this.dealerInfoData.dealerName,
                  id: this.$route.query.id
                }
              };

              updateDealer(this.$route.query.id, formData).then(response => {
                if (response.code == 200) {
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                  this.getUserInfo();
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            } else {
              this.$emit("submitDealerInfo", this.isEdit);
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


