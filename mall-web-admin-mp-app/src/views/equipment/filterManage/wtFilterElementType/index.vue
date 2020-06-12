<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchWtFilterElementTypeList()"
          type="primary"
          size="small"
        >查询结果</el-button>
        <el-button
          style="float:right;margin-right: 15px"
          @click="handleResetSearch()"
          size="small"
        >重置</el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="滤芯类型名称">
            <el-input
              style="width: 203px"
              v-model="listQuery.filterElementTypeName"
              placeholder="按滤芯类型名称搜索"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" type="primary" @click="addWtFilterElementType()" size="mini">添加</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="wtFilterElementTypeTable"
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
        <el-table-column prop="filterElementTypeName" label="滤芯类型名称" align="center">
          <template slot-scope="scope">{{scope.row.filterElementTypeName }}</template>
        </el-table-column>
        <el-table-column prop="changeCycle" label="更换周期(天数)" align="center">
          <template slot-scope="scope">{{scope.row.changeCycle }}</template>
        </el-table-column>
        <el-table-column prop="remindDay" label="更换滤芯提前提醒(天数)" align="center">
          <template slot-scope="scope">{{scope.row.remindDay }}</template>
        </el-table-column>
        <el-table-column prop="purifierTotal" label="净水总量(吨)" align="center">
          <template slot-scope="scope">{{scope.row.purifierTotal }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center">
          <template slot-scope="scope">{{scope.row.createTime }}</template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <i
              class="el-icon-view"
              style="cursor: pointer"
              @click="handleView(scope.$index, scope.row)"
            ></i>
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
  deleteWtFilterElementType
} from "@/api/water/wtFilterElementType";
import { formatDate } from "@/utils/date";
const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  filterElementTypeName: null
};

export default {
  name: "wtFilterElementTypeList",
  data() {
    return {
      operates: [],
      operateType: null,
      listQuery: Object.assign({}, defaultListQuery),
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
    }
  },
  methods: {
    handleResetSearch() {
      this.listQuery = Object.assign({}, defaultListQuery);
      this.getList();
    },
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
        path: "/equipment/updateWtFilterElementType",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtFilterElementType(row.id).then(response => {
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
    searchWtFilterElementTypeList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    handleBatchOperate() {
      if (this.multipleSelection < 1) {
        this.$message({
          message: "请选择一条记录",
          type: "warning",
          duration: 1000
        });
        return;
      }
      let showStatus = 0;
      if (this.operateType === "showWtFilterElementType") {
        showStatus = 1;
      } else if (this.operateType === "hideWtFilterElementType") {
        showStatus = 0;
      } else {
        this.$message({
          message: "请选择批量操作类型",
          type: "warning",
          duration: 1000
        });
        return;
      }
      let ids = [];
      for (let i = 0; i < this.multipleSelection.length; i++) {
        ids.push(this.multipleSelection[i].id);
      }
      let data = new URLSearchParams();
      data.append("ids", ids);
      data.append("showStatus", showStatus);
      updateShowStatus(data).then(response => {
        this.getList();
        this.$message({
          message: "修改成功",
          type: "success",
          duration: 1000
        });
      });
    },
    addWtFilterElementType() {
      this.$router.push({ path: "/equipment/addWtFilterElementType" });
    },
    handleView(index, row) {
      this.$router.push({
        path: "/equipment/typeDetail",
        query: { id: row.id }
      });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


