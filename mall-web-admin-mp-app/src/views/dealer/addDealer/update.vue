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
    ></dealer-info>
    <application-permissions v-show="second"></application-permissions>
    <related-official-account v-show="third"></related-official-account>
    <related-mini-program v-show="fourth"></related-mini-program>
  </div>
</template>
<script>
import { getUserInfo, getDealerUse, updateDealer } from "@/api/dealer/dealer";
import DealerInfo from "./components/dealerInfo";
import ApplicationPermissions from "./components/applicationPermissions";
import RelatedOfficialAccount from "./components/relatedOfficialAccount";
import RelatedMiniProgram from "./components/RelatedMiniProgram";

const defaultDealerInfo = {};

export default {
  name: "updateDealer",
  components: {
    DealerInfo,
    ApplicationPermissions,
    RelatedOfficialAccount,
    RelatedMiniProgram
  },
  data() {
    return {
      isEdit: true,
      disabled: true,
      dealerId: "",
      activeName: "first",
      first: false,
      second: false,
      third: false,
      fourth: false
    };
  },
  created() {
    this.first = true;
    if (this.isEdit) {
      this.dealerId = this.$route.query.id;
    }

    this.getDealerUse();
  },
  methods: {
    getDealerUse() {
      getDealerUse(this.$route.query.id).then(response => {
        if (!response.data.length) {
          if (response.data.isRelation == "1") {
            this.disabled = false;
          }
        }
      });
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
  }
};
</script>
<style>
</style>


