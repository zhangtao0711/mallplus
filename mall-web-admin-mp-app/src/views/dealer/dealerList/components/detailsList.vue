<template>
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <el-button type="primary" @click="consumptionRecord">消费记录</el-button>
      <el-button type="success" @click="chargeRecord">充值记录</el-button>
      <el-button type="warning" :disabled="true" @click="equipmentRecord">设备列表</el-button>
    </el-card>
    <div class="table-container">
      <el-row>
        <el-col :span="8">
          <div class="bg-purple">商户名称</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ list[0].dealerName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">联系人</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ list[0].realname }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">联系电话</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ list[0].dealerPhone }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">地址</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ list[0].address }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">入驻时间</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ list[0].createTime | formatDate }}</div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { fetchList, deleteAdmin } from "@/api/admin";
import { getToken } from "@/utils/auth";
import { Message } from "element-ui";
import { formatDate } from "@/utils/date";

export default {
  name: "applyingList",
  data() {
    return {
      list: null,
      listLoading: true
    };
  },
  created() {
    this.getList();
  },
  filters: {
    formatDate(time) {
      if (time == null || time === "") {
        return "N/A";
      }
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    }
  },
  methods: {
    // 前往消费记录
    consumptionRecord() {
      this.$router.push({
        path: "/dealer/consumptionRecord",
        query: { id: this.list[0].id }
      });
    },
    // 前往充值记录
    chargeRecord() {
      this.$router.push({
        path: "/dealer/chargeRecord",
        query: { id: this.list[0].id }
      });
    },
    // 前往设备列表
    equipmentRecord() {
      this.$router.push({
        path: "/dealer/equipmentRecord",
        query: { id: this.list[0].id }
      });
    },
    getList() {
      this.listLoading = true;
      let data = {
        id: this.$route.query.id
      };
      fetchList(data).then(response => {
        this.listLoading = false;
        this.list = response.data.records;
      });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.table-container {
  width: 65%;
  margin: 20px auto;
}
.el-row {
  margin-bottom: 20px;
  text-align: center;
  height: 40px;

  &:last-child {
    margin-bottom: 0;
  }
  .bg-purple {
    height: 40px;
    padding: 10px;
    background: #d3dce6;
  }
  .bg-purple-light {
    height: 40px;
    padding: 10px;
    background: #e5e9f2;
  }
}
</style>
