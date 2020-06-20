<template>
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="addSmsRechargePackage()" size="mini">添加充值套餐</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="smsRechargePackageTable"
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

        <el-table-column prop="termFee" label="充值条件" align="center">
          <template slot-scope="scope">{{scope.row.termFee }}</template>
        </el-table-column>

        <el-table-column prop="donateFee" label="赠送金额" align="center">
          <template slot-scope="scope">{{scope.row.donateFee }}</template>
        </el-table-column>

        <el-table-column prop="actualFee" label="实际到账金额" align="center">
          <template slot-scope="scope">{{scope.row.actualFee }}</template>
        </el-table-column>

        <el-table-column prop="giftOne" label="赠品名称" align="center">
          <template slot-scope="scope">{{scope.row.giftOne }}</template>
        </el-table-column>

        <el-table-column prop="giftOneUrl" label="赠品名称路径" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.giftOneUrl">
              <el-image
                style="width: 100px; height: 100px"
                :src="url + scope.row.giftOneUrl"
                fit="fill"
              ></el-image>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="giftTwo" label="赠品名称" align="center">
          <template slot-scope="scope">{{scope.row.giftTwo }}</template>
        </el-table-column>

        <el-table-column prop="giftTwoUrl" label="赠品名称路径" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.giftTwoUrl">
              <el-image
                style="width: 100px; height: 100px"
                :src="url + scope.row.giftTwoUrl"
                fit="fill"
              ></el-image>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="giftThree" label="赠品名称" align="center">
          <template slot-scope="scope">{{scope.row.giftThree }}</template>
        </el-table-column>

        <el-table-column prop="giftThreeUrl" label="赠品名称路径" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.giftThreeUrl">
              <el-image
                style="width: 100px; height: 100px"
                :src="url + scope.row.giftThreeUrl"
                fit="fill"
              ></el-image>
            </div>
          </template>
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
import {
  fetchList,
  deleteSmsRechargePackage
} from "@/api/sms/smsRechargePackage";
import { formatDate } from "@/utils/date";

export default {
  name: "smsRechargePackageList",
  data() {
    return {
      url: process.env.IMG_API,
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
        path: "/merchandise/updateSmsRechargePackage",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteSmsRechargePackage(row.id).then(response => {
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
    searchSmsRechargePackageList() {
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
      if (this.operateType === "showSmsRechargePackage") {
        showStatus = 1;
      } else if (this.operateType === "hideSmsRechargePackage") {
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
    addSmsRechargePackage() {
      //手动将router,改成$router
      this.$router.push({ path: "/merchandise/addSmsRechargePackage" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


