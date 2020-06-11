<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基本" name="first"></el-tab-pane>
      <el-tab-pane label="应用权限" name="second"></el-tab-pane>
      <el-tab-pane label="关联公众号" :disabled="disabled" name="third"></el-tab-pane>
      <el-tab-pane label="关联小程序" :disabled="disabled" name="fourth"></el-tab-pane>
    </el-tabs>
    <dealer-info
      :is-edit="false"
      v-show="first"
      v-model="dealerInfoData"
      @submitDealerInfo="submitDealerInfo"
    ></dealer-info>
    <application-permissions v-show="second" :dealer-id="dealerId"></application-permissions>
    <related-official-account :dealer-id="dealerId" v-show="third"></related-official-account>
    <related-mini-program :dealer-id="dealerId" v-show="fourth"></related-mini-program>
  </div>
</template>
<script>
import { createDealer, getDealerUse } from "@/api/dealer/dealer";
import DealerInfo from "./components/dealerInfo";
import ApplicationPermissions from "./components/applicationPermissions";
import RelatedOfficialAccount from "./components/relatedOfficialAccount";
import RelatedMiniProgram from "./components/RelatedMiniProgram";

const defaultDealerInfo = {};

export default {
  name: "addDealer",
  components: {
    DealerInfo,
    ApplicationPermissions,
    RelatedOfficialAccount,
    RelatedMiniProgram
  },
  data() {
    return {
      disabled: true,
      dealerId: "",
      dealerInfoData: Object.assign({}, defaultDealerInfo),
      activeName: "first",
      first: false,
      second: false,
      third: false,
      fourth: false
    };
  },
  created() {
    this.first = true;
    this.getDealerUse();
  },
  methods: {
    getDealerUse() {
      if (this.dealerId) {
        getDealerUse(this.dealerId).then(response => {
          if (!response.data.length) {
            if (response.data.isRelation == "1") {
              this.disabled = false;
            }
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
        if (!this.dealerId) {
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
          thirdSeparate: this.dealerInfoData.thirdSeparate
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
          level: this.dealerInfoData.levelId
        }
      };

      createDealer(formData).then(response => {
        if (response.code == 200) {
          this.dealerId = response.data.user.id;
          this.dealerInfoData = Object.assign({}, defaultDealerInfo);
          this.$message({
            message: "提交成功",
            type: "success",
            duration: 1000
          });
          // this.$router.back();
          this.getDealerUse();
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


