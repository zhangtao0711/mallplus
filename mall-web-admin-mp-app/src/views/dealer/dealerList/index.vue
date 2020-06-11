<template>
  <div class="app-container">
    <!-- <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基本" name="first"></el-tab-pane>
      <el-tab-pane label="应用权限" name="second"></el-tab-pane>
      <el-tab-pane label="关联公众号" :disabled="disabled" name="third"></el-tab-pane>
      <el-tab-pane label="关联小程序" :disabled="disabled" name="fourth"></el-tab-pane>
    </el-tabs>-->

    <div style="display:flex;justify-content : center;">
      <el-button type="primary" @click="applying">待审核</el-button>
      <el-button type="primary" @click="waiting">待入驻</el-button>
      <el-button type="primary" @click="settledin">已入驻</el-button>
    </div>

    <applying-list v-show="first"></applying-list>
    <waiting-list v-show="second"></waiting-list>
    <settled-in-list v-show="third"></settled-in-list>
  </div>
</template>
<script>
import SettledInList from "./components/settledInList";
import WaitingList from "./components/waitingList";
import ApplyingList from "./components/applyingList";

export default {
  name: "addDealer",
  components: {
    SettledInList,
    WaitingList,
    ApplyingList
  },
  data() {
    return {
      first: true,
      second: false,
      third: false
    };
  },
  created() {},
  methods: {
    applying() {
      this.first = true;
      this.second = false;
      this.third = false;
    },
    waiting() {
      this.first = false;
      this.second = true;
      this.third = false;
    },
    settledin() {
      this.first = false;
      this.second = false;
      this.third = true;
    }
  }
};
</script>
<style>
</style>


