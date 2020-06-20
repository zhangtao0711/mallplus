<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchWtWaterCardProblemList()"
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
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="addWtWaterCardProblem()" size="mini" type="primary">添加</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="wtWaterCardProblemTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column type="selection" width="60" align="center"></el-table-column>

        <el-table-column prop="id" label="编号" align="center">
          <template slot-scope="scope">{{scope.row.id }}</template>
        </el-table-column>

        <el-table-column prop="cardNo" label="卡号" align="center">
          <template slot-scope="scope">{{scope.row.cardNo }}</template>
        </el-table-column>

        <el-table-column prop="state" label="类型" align="center">
          <template slot-scope="scope">{{scope.row.state | formatStatus }}</template>
        </el-table-column>

        <el-table-column prop="remarks" label="备注" align="center">
          <template slot-scope="scope">{{scope.row.remarks }}</template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
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
        :page-sizes="[10,15,50]"
        :current-page.sync="listQuery.pageNum"
        :total="total"
      ></el-pagination>
    </div>
  </div>
</template>
<script>
//将$都替换为$
import {
  fetchList,
  updateStateRemove
} from "@/api/water/wtWaterCardProblem";
import { formatDate } from "@/utils/date";

export default {
  name: "wtWaterCardProblemList",
  data() {
    return {
      listQuery: {
        keyword: null,
        pageNum: 1,
        pageSize: 10
      },
      list: null,
      total: null,
      listLoading: true,
      multipleSelection: []
    };
  },
  created() {
    this.getList();
  },
  filters: {
    formatCreateTime(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },

    formatStatus(value) {
      if (value === '1') {
        return "复制卡";
      } else if (value === '2') {
        return "挂失卡";
      } else if (value === '3') {
        return "删除卡";
      } else if (value === '4') {
        return "非经销商卡";
      }
    }
  },
  methods: {
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then(response => {
        this.listLoading = false;
        this.list = response.data.records;
        this.total = response.data.total;
        this.totalPage = response.data.pages;
        this.pageSize = response.data.size;
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该问题卡", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        updateStateRemove(row.id).then(response => {
          this.$message({
            message: "删除成功",
            type: "success",
            duration: 1000
          });
          this.getList();
        });
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
    searchWtWaterCardProblemList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    addWtWaterCardProblem() {
      //手动将router,改成$router
      this.$router.push({ path: "/ums/addWtWaterCardProblem" });
    },
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


