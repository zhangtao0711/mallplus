<template>
  <div class="app-container">
    <!-- <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchMerchatBusinessMaterialsList()"
          type="primary"
          size="small"
        >查询结果</el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="输入搜索：">
            <el-input style="width: 203px" v-model="listQuery.keyword" placeholder="类型名称/关键字"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card> -->
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button type="primary" size="mini" style="float:right" @click="goback">返回列表</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="merchatBusinessMaterialsTable"
        :data="list"
        style="width: 100%"
        v-loading="listLoading"
        border
      >
        <el-table-column prop="id" label="编号" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>

        <el-table-column prop="userName" label="用户" align="center">
          <template slot-scope="scope">{{ scope.row.userName }}</template>
        </el-table-column>

        <el-table-column prop="money" label="金额" align="center">
          <template slot-scope="scope">{{ scope.row.money }}</template>
        </el-table-column>

        <el-table-column prop="time" label="上传时间" align="center">
          <template slot-scope="scope">{{scope.row.time}}</template>
        </el-table-column>

        <el-table-column prop="type" label="类型" align="center">
          <template slot-scope="scope">{{scope.row.type}}</template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container"></div>
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes,prev, pager, next,jumper"
        :page-size="listQuery.pageSize"
        :page-sizes="[5, 10, 15]"
        :current-page.sync="listQuery.pageNum"
        :total="total"
      ></el-pagination>
    </div>
  </div>
</template>
<script>
import { getConsumption } from "@/api/dealer/dealerRecord";
import { getToken } from "@/utils/auth";
import { Message } from "element-ui";
import { formatDate } from "@/utils/date";

export default {
  name: "consumptionRecord",
  data() {
    return {
      listQuery: {
        keyword: null,
        pageNum: 1,
        pageSize: 10,
        status: 0
      },
      list: null,
      total: null,
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
    goback() {
      this.$router.back();
    },
    getList() {
      this.listLoading = true;
      getConsumption({ dealerId: this.$route.query.id }).then(response => {
        this.listLoading = false;
        this.list = response.data;
        // this.total = response.data.total;
        // this.totalPage = response.data.pages;
        // this.pageSize = response.data.size;
      });
    },
    handleSizeChange(val) {
      this.listQuery.pageNum = 1;
      this.listQuery.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val;
      this.getList();
    },
    searchMerchatBusinessMaterialsList() {
      this.listQuery.pageNum = 1;
      this.getList();
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped></style>
