<template>
  <div class="app-container">
    <!-- <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchWtWarterCardEmpowerList()"
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
      <el-button class="btn-add" @click="addWtWarterCardEmpower()" size="mini">添加</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="wtWarterCardEmpowerTable"
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
        <el-table-column prop="umsMemberId" label="授权人账号" align="center">
          <template slot-scope="scope">{{scope.row.umsMemberId }}</template>
        </el-table-column>
        <el-table-column prop="umsMemberNickname" label="授权人昵称" align="center">
          <template slot-scope="scope">{{scope.row.umsMemberNickname }}</template>
        </el-table-column>
        <el-table-column prop="umsMemberToId" label="被授权人账号" align="center">
          <template slot-scope="scope">{{scope.row.umsMemberToId }}</template>
        </el-table-column>
        <el-table-column prop="umsMemberToNickname" label="被授权人昵称" align="center">
          <template slot-scope="scope">{{scope.row.umsMemberToNickname }}</template>
        </el-table-column>
        <el-table-column prop="empowerRelation" label="与被授权人关系" align="center">
            <!-- (字典empower_relation) -->
          <template slot-scope="scope">{{scope.row.empowerRelation }}</template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <!-- <el-button size="mini" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button> -->
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">取消授权</el-button>
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
import {
  fetchList,
  deleteWtWarterCardEmpower
} from "@/api/water/wtWarterCardEmpower";

export default {
  name: "wtWarterCardEmpowerList",
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
    handleUpdate(index, row) {
      this.$router.push({
        path: "/water/updateWtWarterCardEmpower",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtWarterCardEmpower(row.id).then(response => {
          this.$message({
            message: "取消成功",
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
    searchWtWarterCardEmpowerList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    addWtWarterCardEmpower() {
      //手动将router,改成$router
      this.$router.push({ path: "/ums/addWtWarterCardEmpower" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


