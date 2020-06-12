<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchWtFilterElementList()"
          type="primary"
          size="small"
        >查询结果</el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="设备号">
            <el-input style="width: 203px" v-model="listQuery.eqcode" placeholder="按设备号查询"></el-input>
          </el-form-item>
          <el-form-item label="滤芯名称">
            <el-input
              style="width: 203px"
              v-model="listQuery.filterElementTypeId"
              placeholder="按滤芯名称查询"
            ></el-input>
          </el-form-item>
          <el-form-item label="滤芯级别">
            <el-input
              style="width: 203px"
              v-model="listQuery.filterElementLevel"
              placeholder="按滤芯级别查询"
            ></el-input>
          </el-form-item>
          <el-form-item label="已使用百分比">
            <el-input style="width: 203px" v-model="listQuery.usePercent" placeholder="按已使用百分比查询查询"></el-input>
          </el-form-item>
          <el-form-item label="滤芯状态">
            <el-input style="width: 203px" v-model="listQuery.state" placeholder="按滤芯状态查询"></el-input>
          </el-form-item>
          <el-form-item label="计费模式">
            <el-input style="width: 203px" v-model="listQuery.billingMode" placeholder="按计费模式查询"></el-input>
          </el-form-item>
          <el-form-item label="过滤水量">
            <el-input style="width: 203px" v-model="listQuery.purifierNum" placeholder="按过滤水量查询"></el-input>
          </el-form-item>
          <el-form-item label="水量标准">
            <el-input style="width: 203px" v-model="listQuery.purifiertotal" placeholder="按水量标准查询"></el-input>
          </el-form-item>
          <el-form-item label="滤芯更换时间">
            <el-input style="width: 203px" v-model="listQuery.changeTime" placeholder="按滤芯更换时间查询"></el-input>
          </el-form-item>
          <el-form-item label="到期时间">
            <el-input style="width: 203px" v-model="listQuery.endTime" placeholder="按到期时间查询"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="addWtFilterElement()" size="mini">添加</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="wtFilterElementTable"
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
        <el-table-column prop="eqcode" label="设备号" align="center">
          <template slot-scope="scope">{{scope.row.eqcode }}</template>
        </el-table-column>
         <el-table-column prop="name" label="滤芯名称" align="center">
          <template slot-scope="scope">{{scope.row.name }}</template>
        </el-table-column>
        <!-- <el-table-column prop="filterElementTypeId" label="滤芯类型id" align="center">
          <template slot-scope="scope">{{scope.row.filterElementTypeId }}</template>
        </el-table-column> -->
        <el-table-column prop="filterElementLevel" label="滤芯级别" align="center">
          <template slot-scope="scope">{{scope.row.filterElementLevel }}</template>
        </el-table-column>
        <el-table-column prop="billingMode" label="计费模式" align="center">
          <template slot-scope="scope">{{scope.row.billingMode }}</template>
        </el-table-column>
        <el-table-column prop="purifierNum" label="过滤水量（吨）" align="center">
          <template slot-scope="scope">{{scope.row.purifierNum }}</template>
        </el-table-column>
        <el-table-column prop="usePercent" label="已使用百分比" align="center">
          <template slot-scope="scope">{{scope.row.usePercent }}</template>
        </el-table-column>
        <el-table-column prop="state" label="滤芯状态" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.state === '0'">正常使用</span>
            <span v-else-if="scope.row.state === '1'">准备更换</span>
            <span v-else-if="scope.row.state === '2'">等待更换</span>
          </template>
        </el-table-column>
        <el-table-column prop="changeTime" label="滤芯更换时间" align="center">
          <template slot-scope="scope">{{scope.row.changeTime }}</template>
        </el-table-column>
        <el-table-column prop="endTime" label="到期时间" align="center">
          <template slot-scope="scope">{{scope.row.endTime }}</template>
        </el-table-column>
        <el-table-column prop="purifiertotal" label="水量标准(吨)" align="center">
          <template slot-scope="scope">{{scope.row.purifiertotal }}</template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
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
import { fetchList, deleteWtFilterElement } from "@/api/water/wtFilterElement";
import { formatDate } from "@/utils/date";

export default {
  name: "wtFilterElementList",
  data() {
    return {
      operates: [],
      operateType: null,
      listQuery: {
        eqcode: null,
        filterElementTypeId: null,
        filterElementLevel: null,
        usePercent: null,
        state: null,
        billingMode: null,
        purifierNum: null,
        purifiertotal: null,
        changeTime: null,
        endTime: null,
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
    handleUpdate(index, row) {
      this.$router.push({
        path: "/equipment/updateWtFilterElement",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtFilterElement(row.id).then(response => {
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
    searchWtFilterElementList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    addWtFilterElement() {
      //手动将router,改成$router
      this.$router.push({ path: "/equipment/addWtFilterElement" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


