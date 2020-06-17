<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchWtSimCardList()"
          type="primary"
          size="small"
        >查询结果</el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="物联网卡号">
            <el-input style="width: 203px" v-model="listQuery.cardno" placeholder="类型名称/关键字"></el-input>
          </el-form-item>

          <el-form-item label="主板设备号">
            <el-input style="width: 203px" v-model="listQuery.deviceId" placeholder="按主板设备号查询"></el-input>
          </el-form-item>

          <el-form-item label="产品编码">
            <el-input style="width: 203px" v-model="listQuery.productId" placeholder="按产品编码查询"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="addWtSimCard()" size="mini">添加</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="wtSimCardTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column type="selection" width="60" align="center"></el-table-column>

        <el-table-column prop="id" label="id" align="center">
          <template slot-scope="scope">{{scope.row.id }}</template>
        </el-table-column>

        <el-table-column prop="cardno" label="卡号" align="center">
          <template slot-scope="scope">{{scope.row.cardno }}</template>
        </el-table-column>

        <el-table-column prop="state" label="卡状态" align="center">
          <template slot-scope="scope">{{scope.row.state | formatStatus }}</template>
        </el-table-column>

        <el-table-column prop="deviceId" label="主版设备号" align="center">
          <template slot-scope="scope">{{scope.row.deviceId }}</template>
        </el-table-column>

        <el-table-column prop="address" label="地址" align="center">
          <template slot-scope="scope">{{scope.row.address }}</template>
        </el-table-column>

        <el-table-column prop="address" label="商家" align="center">
          <template slot-scope="scope">{{scope.row.address }}</template>
        </el-table-column>

        <el-table-column prop="address" label="套餐价格" align="center">
          <template slot-scope="scope">{{scope.row.address }}</template>
        </el-table-column>

        <el-table-column prop="address" label="运营商" align="center">
          <template slot-scope="scope">{{scope.row.address }}</template>
        </el-table-column>

        <el-table-column prop="expiredAt" label="卡有效期" align="center">
          <template slot-scope="scope">{{scope.row.expiredAt }}</template>
        </el-table-column>

        <el-table-column prop="prechargeOffsetsTime" label="预充值生效日期" align="center">
          <template slot-scope="scope">{{scope.row.prechargeOffsetsTime | formatPrechargeTime }}</template>
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
import { fetchList, deleteWtSimCard } from "@/api/water/wtSimCard";
import { formatDate } from "@/utils/date";

export default {
  name: "wtSimCardList",
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
    formatStatus(value) {
      if (value == "0") {
        return "未知";
      } else if (value == "00") {
        return "正常";
      } else if (value == "01") {
        return "单向停机";
      } else if (value == "02") {
        return "停机";
      } else if (value == "03") {
        return "预销号";
      } else if (value == "04") {
        return "销号/拆机";
      } else if (value == "05") {
        return "过户";
      } else if (value == "06") {
        return "休眠";
      } else if (value == "07") {
        return "待激";
      } else if (value == "08") {
        return "已停用";
      } else if (value == "09") {
        return "库存";
      } else if (value == "10") {
        return "已失效";
      } else if (value == "11") {
        return "违章停机";
      } else if (value == "12") {
        return "挂失";
      } else if (value == "13") {
        return "用户报停";
      } else if (value == "14") {
        return "断网";
      } else if (value == "99") {
        return "不存在";
      }
    },
    formatPrechargeTime(value) {
      if (value == "0") {
        return "当月";
      } else if (value == "1") {
        return "次月";
      }
    },
    formatCreateTime(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    }
  },
  methods: {
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then(response => {
        console.log(response.data);
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
        path: "/equipment/updateWtSimCard",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtSimCard(row.id).then(response => {
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
    searchWtSimCardList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    addWtSimCard() {
      //手动将router,改成$router
      this.$router.push({ path: "/equipment/addWtSimCard" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


