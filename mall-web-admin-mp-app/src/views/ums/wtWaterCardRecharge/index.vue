<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchWtWaterCardRechargeList()"
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
      <el-button class="btn-add" @click="addWtWaterCardRecharge()" size="mini">批量充值</el-button>
      <el-button class="btn-add" @click="addBackstageRecharge()" size="mini">后台充值</el-button>
      <el-button class="btn-add" @click="addWtWaterCardRecharge()" size="mini">批量导入充值</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="wtWaterCardRechargeTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column type="selection" width="60" align="center"></el-table-column>

        <el-table-column prop="id" label="id">
          <template slot-scope="scope">{{scope.row.id }}</template>
        </el-table-column>
        <el-table-column prop="rechargeMoneyType" label="充值金额类型（0充值金额1充值体验金）字典recharge_money_type">
          <template slot-scope="scope">{{scope.row.rechargeMoneyType }}</template>
        </el-table-column>
        <el-table-column prop="rechargeType" label="充值类型（0单卡 1批量 2筛选充值）字典	recharge_type">
          <template slot-scope="scope">{{scope.row.rechargeType }}</template>
        </el-table-column>
        <el-table-column prop="startNo" label="起始卡号">
          <template slot-scope="scope">{{scope.row.startNo }}</template>
        </el-table-column>
        <el-table-column prop="endNo" label="终止卡号">
          <template slot-scope="scope">{{scope.row.endNo }}</template>
        </el-table-column>
        <el-table-column prop="cardNo" label="卡号">
          <template slot-scope="scope">{{scope.row.cardNo }}</template>
        </el-table-column>
        <el-table-column prop="rechargeMoney" label="充值金额">
          <template slot-scope="scope">{{scope.row.rechargeMoney }}</template>
        </el-table-column>
        <el-table-column prop="receivedMoney" label="实收金额">
          <template slot-scope="scope">{{scope.row.receivedMoney }}</template>
        </el-table-column>
        <el-table-column prop="experienceMoney" label="体验金额">
          <template slot-scope="scope">{{scope.row.experienceMoney }}</template>
        </el-table-column>
        <el-table-column prop="experienceEndData" label="体验到期日">
          <template slot-scope="scope">{{scope.row.experienceEndData }}</template>
        </el-table-column>
        <el-table-column prop="experienceEndDay" label="体验金有效天数">
          <template slot-scope="scope">{{scope.row.experienceEndDay }}</template>
        </el-table-column>
        <el-table-column prop="umsBalanceMark" label="会员卡余额-筛选记号(字典where_mark)">
          <template slot-scope="scope">{{scope.row.umsBalanceMark }}</template>
        </el-table-column>
        <el-table-column prop="umsBalanceWhere" label="会员卡余额-筛选条件">
          <template slot-scope="scope">{{scope.row.umsBalanceWhere }}</template>
        </el-table-column>
        <el-table-column prop="umsUseMark" label="用水频次-筛选记号(字典where_mark)">
          <template slot-scope="scope">{{scope.row.umsUseMark }}</template>
        </el-table-column>
        <el-table-column prop="umsUseWhere" label="用水频次-筛选条件">
          <template slot-scope="scope">{{scope.row.umsUseWhere }}</template>
        </el-table-column>
        <el-table-column prop="umsUsePeriod" label="用水频次-筛选周期(字典where_period)">
          <template slot-scope="scope">{{scope.row.umsUsePeriod }}</template>
        </el-table-column>
        <el-table-column prop="umsRecommendMark" label="推荐频次-筛选记号(字典where_mark)">
          <template slot-scope="scope">{{scope.row.umsRecommendMark }}</template>
        </el-table-column>
        <el-table-column prop="umsRecommendWhere" label="推荐频次-筛选条件">
          <template slot-scope="scope">{{scope.row.umsRecommendWhere }}</template>
        </el-table-column>
        <el-table-column prop="umsRecommendPeriod" label="推荐频次-筛选周期(字典where_period)">
          <template slot-scope="scope">{{scope.row.umsRecommendPeriod }}</template>
        </el-table-column>
        <el-table-column prop="umsCommunity" label="用户社区">
          <template slot-scope="scope">{{scope.row.umsCommunity }}</template>
        </el-table-column>
        <el-table-column prop="umsMemberLevel" label="会员等级">
          <template slot-scope="scope">{{scope.row.umsMemberLevel }}</template>
        </el-table-column>

        <!-- <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column> -->
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
  deleteWtWaterCardRecharge
} from "@/api/water/wtWaterCardRecharge";
import { formatDate } from "@/utils/date";

export default {
  name: "wtWaterCardRechargeList",
  data() {
    return {
      operates: [],
      operateType: null,
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
      if (value === 1) {
        return "未开始";
      } else if (value === 2) {
        return "活动中";
      } else if (value === 3) {
        return "已结束";
      } else if (value === 4) {
        return "已失效";
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
    handleUpdate(index, row) {
      this.$router.push({
        path: "/ums/updateWtWaterCardRecharge",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtWaterCardRecharge(row.id).then(response => {
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
    searchWtWaterCardRechargeList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    handleBatchOperate() {
      console.log(this.multipleSelection);
      if (this.multipleSelection < 1) {
        this.$message({
          message: "请选择一条记录",
          type: "warning",
          duration: 1000
        });
        return;
      }
      let showStatus = 0;
      if (this.operateType === "showWtWaterCardRecharge") {
        showStatus = 1;
      } else if (this.operateType === "hideWtWaterCardRecharge") {
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
    addWtWaterCardRecharge() {
      //手动将router,改成$router
      this.$router.push({ path: "/ums/addWtWaterCardRecharge" });
    },
    addBackstageRecharge() {
      this.$router.push({ path: "/ums/backstageRecharge" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


