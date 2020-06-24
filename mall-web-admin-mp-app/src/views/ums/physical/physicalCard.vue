<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchWtWaterCardList()"
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
    </el-card>

    <el-card class="operate-container" shadow="never">
      <el-button type="primary" @click="addWtWaterCard()" size="small">添加</el-button>
      <el-button type="primary" size="small" @click="derived">会员卡导出</el-button>
      <el-button type="primary" size="small" @click="limit">限制消费</el-button>
      <el-button type="primary" size="small" @click="bindCard">批量绑卡</el-button>
      <el-button type="primary" size="small" @click="limit">分配</el-button>
      <el-button type="primary" size="small" @click="bindCard">取款</el-button>
    </el-card>

    <div class="table-container">
      <el-table
        ref="wtWaterCardTable"
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

        <el-table-column prop="cardNo" label="会员卡号" align="center">
          <template slot-scope="scope">{{scope.row.cardNo }}</template>
        </el-table-column>

        <el-table-column prop="umsMemberName" label="用户" align="center">
          <template slot-scope="scope">{{scope.row.umsMemberName }}</template>
        </el-table-column>

        <el-table-column prop="memberLevelName" label="会员等级" align="center">
          <template slot-scope="scope">{{scope.row.memberLevelName }}</template>
        </el-table-column>

        <el-table-column prop="cardMoney" label="余额" align="center">
          <template slot-scope="scope">{{scope.row.cardMoney }}</template>
        </el-table-column>

        <el-table-column prop="saleState" label="出售状态" align="center">
          <template slot-scope="scope">{{scope.row.saleState }}</template>
          <!-- 0未售出1已售出 -->
        </el-table-column>

        <el-table-column prop="activationState" label="激活状态" align="center">
          <template slot-scope="scope">{{scope.row.activationState }}</template>
          <!-- 0-未激活,1-激活 -->
        </el-table-column>

        <el-table-column prop="rechargeMoney" label="充值状态" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.rechargeMoney>0">已充值</span>
            <span v-else>未充值</span>
          </template>
        </el-table-column>

        <el-table-column prop="saleByName" label="售卡人" align="center">
          <template slot-scope="scope">{{scope.row.saleByName }}</template>
        </el-table-column>

        <el-table-column prop="qrCode" label="设备" align="center">
          <template slot-scope="scope">{{scope.row.qrCode }}</template>
        </el-table-column>

        <el-table-column prop="rechargeMoney" label="充值金额" align="center">
          <template slot-scope="scope">{{scope.row.rechargeMoney }}</template>
        </el-table-column>

        <el-table-column prop="giveMoney" label="赠送金额" align="center">
          <template slot-scope="scope">{{scope.row.giveMoney }}</template>
        </el-table-column>

        <el-table-column prop="experienceMoney" label="体验金额" align="center">
          <template slot-scope="scope">{{scope.row.experienceMoney }}</template>
        </el-table-column>

        <el-table-column prop="experienceEndData" label="体验有效期" align="center">
          <template slot-scope="scope">{{scope.row.experienceEndData }}</template>
        </el-table-column>

        <el-table-column prop="uniacName" label="公众号" align="center">
          <template slot-scope="scope">{{scope.row.uniacName }}</template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button v-if="!scope.row.saleByName" size="mini">分配</el-button>
            <el-button size="mini" @click="toDetail">详情</el-button>
            <!-- <el-button size="mini" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button> -->
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
import { fetchList, deleteWtWaterCard } from "@/api/water/wtWaterCard";
import { formatDate } from "@/utils/date";

export default {
  name: "wtWaterCardList",
  data() {
    return {
      operates: [],
      operateType: null,
      listQuery: {
        keyword: null,
        pageNum: 1,
        pageSize: 10,
        cardType: 0
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
    toDetail(index, row) {
      this.$router.push({
        path: "/ums/PhysicalDetail",
        query: { id: row.id }
      });
    },
    derived() {
      this.$router.push({ path: "/ums/derivedCard" });
    },
    limit() {
      this.$router.push({ path: "/ums/wtWaterCardLimit" });
    },
    bindCard() {
      this.$router.push({ path: "/ums/batchCardBinding" });
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
        path: "/ums/updatePhysicalCard",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtWaterCard(row.id).then(response => {
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
    searchWtWaterCardList() {
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
      if (this.operateType === "showWtWaterCard") {
        showStatus = 1;
      } else if (this.operateType === "hideWtWaterCard") {
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
    addWtWaterCard() {
      //手动将router,改成$router
      this.$router.push({ path: "/ums/addPhysicalCard" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


