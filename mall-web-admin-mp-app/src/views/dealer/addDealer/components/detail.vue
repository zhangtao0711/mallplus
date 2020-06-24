<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基本" name="first"></el-tab-pane>
      <el-tab-pane label="应用权限" name="second"></el-tab-pane>
      <el-tab-pane label="关联公众号" :disabled="disabled" name="third"></el-tab-pane>
      <el-tab-pane label="关联小程序" :disabled="disabled" name="fourth"></el-tab-pane>
    </el-tabs>

    <dealer-info
      :is-edit="isEdit"
      v-show="first"
      v-model="dealerInfoData"
      @submitDealerInfo="submitDealerInfo"
    ></dealer-info>

    <application-permissions
      :permission-list="permissionList"
      :is-edit="isEdit"
      v-show="second"
      v-model="dealerUse"
      @submitPermissions="submitPermissions"
    ></application-permissions>

    <related-official-account v-show="third" :dealer-id="dealerId"></related-official-account>
    <related-mini-program :dealer-id="dealerId" v-show="fourth"></related-mini-program>
  </div>
</template>
<script>
import DealerInfo from "./dealerInfo";
import ApplicationPermissions from "./applicationPermissions";
import RelatedOfficialAccount from "./relatedOfficialAccount";
import RelatedMiniProgram from "./relatedMiniProgram";
import { get } from "@/utils/auth";
import {
  createDealer,
  getUserInfo,
  getDealerUse,
  createDealerUse,
  updateDealerUse
} from "@/api/dealer/dealer";
import {
  createAccount,
  updateAccount,
  getAccount
} from "@/api/dealer/miniProgramOfficialAccount";

const defaultDealerInfo = {};
const defaultPermissions = {};

export default {
  name: "WtWaterCardActivateDetail",
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  components: {
    DealerInfo,
    ApplicationPermissions,
    RelatedOfficialAccount,
    RelatedMiniProgram
  },
  data() {
    return {
      isEditAccount: true,
      disabled: true,
      dealerId: null,
      storeId: null,
      dealerInfoData: Object.assign({}, defaultDealerInfo),
      dealerUse: Object.assign({}, defaultPermissions),
      activeName: "first",
      first: false,
      second: false,
      third: false,
      fourth: false,
      isEditUse: null,
      permissionList: []
    };
  },
  created() {
    this.first = true;
    if (this.isEdit) {
      let dealerId = Number(this.$route.query.id);
      this.dealerId = Number(dealerId);
      if (this.dealerId) {
        this.getUserInfo();
        this.getDealerUse();
      }
    }
  },
  methods: {
    getUserInfo() {
      getUserInfo(this.dealerId).then(response => {
        this.dealerInfoData = JSON.parse(
          (
            JSON.stringify(response.data.appletSet) +
            JSON.stringify(response.data.user)
          ).replace(/}{/, ",")
        );
        this.storeId = Number(this.dealerInfoData.storeId);
        this.dealerInfoData.type = String(this.dealerInfoData.type);
        this.dealerInfoData.city = Number(this.dealerInfoData.city);
        this.dealerInfoData.county = Number(this.dealerInfoData.county);
        this.dealerInfoData.levelId = String(this.dealerInfoData.level);
        this.dealerInfoData.applyStatus = String(
          this.dealerInfoData.applyStatus
        );
        this.dealerInfoData.selfType = String(this.dealerInfoData.selfType);
      });
    },
    getDealerUse() {
      if (this.dealerId) {
        getDealerUse(this.dealerId).then(response => {
          if (response.data.coupon) {
            this.isEditUse = true;
            this.dealerUse = response.data.coupon;
            if (response.data.coupon.isRelation == "1") {
              this.disabled = false;
            }
            this.permissionList = response.data.permisions;
          } else if (!response.data.length) {
            this.isEditUse = true;
            this.dealerUse = response.data;
            if (response.data.isRelation == "1") {
              this.disabled = false;
            }

            if (this.dealerUse.permissionIds || this.dealerUse.permissionNames) {
              var reg = /,$/gi;
              this.dealerUse.permissionIds = this.dealerUse.permissionIds.replace(
                reg,
                ""
              );
              this.permissionIds = this.dealerUse.permissionIds.split(",");

              this.permissionNames = this.dealerUse.permissionNames.split(",");
              let permissionList = [];
              for (var i = 0; i < this.permissionIds.length; i++) {
                permissionList.push({
                  permission_id: this.permissionIds[i]
                });
              }
              for (var j = 0; j < this.permissionNames.length; j++) {
                permissionList[j].permission_name = this.permissionNames[j];
              }
              this.permissionList = permissionList;
            }
          } else {
            this.isEditUse = false;
            this.dealerUse = Object.assign({}, defaultPermissions);
            this.permissionList = response.data;
          }
        });
      }
    },
    handleClick(tab, event) {
      if (tab.name == "first") {
        this.first = !this.first;
        this.second = false;
        this.third = false;
        this.fourth = false;
      } else if (tab.name == "second") {
        this.first = false;
        this.second = !this.second;
        this.third = false;
        this.fourth = false;
        if (!this.$route.query.id) {
          this.$message({
            message: "请先添加经销商",
            type: "warning",
            duration: 1000
          });
        }
      } else if (tab.name == "third") {
        this.first = false;
        this.second = false;
        this.third = !this.third;
        this.fourth = false;
      } else if (tab.name == "fourth") {
        this.first = false;
        this.second = false;
        this.third = false;
        this.fourth = !this.fourth;
      }
    },
    submitPermissions() {
      if (this.isEditUse) {
        this.dealerUse.updateBy = get("userId");
        updateDealerUse(this.dealerId, this.dealerUse).then(response => {
          if (response.code == 200) {
            this.$message({
              message: "修改成功",
              type: "success",
              duration: 1000
            });
            this.getDealerUse();
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
        this.dealerUse.dealerId = this.dealerId;
        this.dealerUse.createBy = get("userId");
        this.dealerUse.storeId = this.storeId;
        this.dealerUse.storeName = this.storeName;
        createDealerUse(this.dealerUse).then(response => {
          if (response.code == 200) {
            this.dealerUse = Object.assign({}, defaultPermissions);
            this.$message({
              message: "提交成功",
              type: "success",
              duration: 1000
            });
            this.getDealerUse();
            this.getUserInfo();
          } else {
            this.$message({
              message: response.msg,
              type: "error",
              duration: 1000
            });
          }
        });
      }
    },
    submitDealerInfo(isEdit) {
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
          selfType: this.dealerInfoData.selfType,
          thirdSeparate: this.dealerInfoData.thirdSeparate,
          storeId: get("storeId")
        },
        user: {
          address: this.dealerInfoData.address,
          applyStatus: this.dealerInfoData.applyStatus,
          city: this.dealerInfoData.city,
          county: this.dealerInfoData.county,
          dealerName: this.dealerInfoData.dealerName,
          dealerPhone: this.dealerInfoData.dealerPhone,
          phone: this.dealerInfoData.dealerPhone,
          gid: this.dealerInfoData.gid,
          icon: this.dealerInfoData.icon,
          pid: this.dealerInfoData.pid,
          province: this.dealerInfoData.province,
          realname: this.dealerInfoData.realname,
          type: this.dealerInfoData.type,
          username: this.dealerInfoData.dealerPhone,
          nickName: this.dealerInfoData.dealerName,
          level: this.dealerInfoData.levelId,
          storeId: get("storeId"),
          status: 1 // 0否1是
        }
      };

      createDealer(formData).then(response => {
        if (response.code == 200) {
          this.dealerId = response.data.user.id;
          this.$message({
            message: "提交成功",
            type: "success",
            duration: 1000
          });
          // this.$router.back();
          this.getDealerUse();
          this.getUserInfo();
        } else {
          this.$message({
            message: response.msg,
            type: "error",
            duration: 1000
          });
        }
      });
    }
  }
};
</script>
<style>
</style>


