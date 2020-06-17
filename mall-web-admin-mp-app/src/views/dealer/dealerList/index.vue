<template>
  <div class="app-container">
    <el-tabs v-model="activeName">
      <el-tab-pane label="待审核" name="first">
        <applying-list></applying-list>
      </el-tab-pane>
      <el-tab-pane label="待入驻" name="second">
        <waiting-list></waiting-list>
      </el-tab-pane>
      <el-tab-pane label="已入驻" name="third">
        <settled-in-list></settled-in-list>
      </el-tab-pane>
    </el-tabs>
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
      activeName: "first",
    };
  },
  created() {},
  methods: {}
};
</script>
<style>
</style>


