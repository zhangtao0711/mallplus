<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchMerchatBusinessMaterialsList()"
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
          <el-form-item label="商户名称">
            <el-input style="width: 203px" v-model="listQuery.dealerName" placeholder="按商户名称搜索"></el-input>
          </el-form-item>

          <el-form-item label="联系人">
            <el-input style="width: 203px" v-model="listQuery.realname" placeholder="按联系人搜索"></el-input>
          </el-form-item>

          <el-form-item label="手机号">
            <el-input style="width: 203px" v-model="listQuery.dealerPhone" placeholder="按手机号查询"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click="addMerchatBusinessMaterials()"
        type="primary"
        size="small"
      >添加经销商</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="merchatBusinessMaterialsTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column prop="dealerName" width="150" label="商户名称" align="center">
          <template slot-scope="scope">{{ scope.row.dealerName }}</template>
        </el-table-column>

        <el-table-column prop="balance" width="200" label="余额" align="center">
          <template slot-scope="scope">{{ scope.row.balance }}</template>
        </el-table-column>

        <el-table-column prop="realname" width="250" label="联系人" align="center">
          <template slot-scope="scope">{{ scope.row.realname }}</template>
        </el-table-column>

        <el-table-column prop="dealerPhone" label="联系电话" align="center">
          <template slot-scope="scope">{{ scope.row.dealerPhone }}</template>
        </el-table-column>

        <el-table-column prop="createTime" label="入驻时间" align="center">
          <template slot-scope="scope">{{scope.row.createTime | formatDate}}</template>
        </el-table-column>

        <el-table-column prop="status" width="120" label="状态" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.status===2">允许入驻</span>
          </template>
        </el-table-column>

        <el-table-column prop="contactName" label="审核人" align="center">
          <template slot-scope="scope">{{ scope.row.contactName }}</template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleInfo(scope.$index, scope.row)">详情</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            <el-button size="mini" type="success" @click="handleCharge(scope.$index, scope.row)">充值</el-button>
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
        :page-sizes="[5, 10, 15]"
        :current-page.sync="listQuery.pageNum"
        :total="total"
      ></el-pagination>
    </div>

    <div>
      <el-dialog title="充值" :visible.sync="blance.dialogVisible" width="40%">
        <el-form :model="blance" :rules="rules" ref="brandFrom" label-width="150px">
          <el-form-item label="当前余额" prop="balance">
            <el-input :disabled="true" v-model="blance.balance" style="width:370px"></el-input>
          </el-form-item>

          <el-form-item label="充值数目" prop="recharge">
            <el-input v-model="blance.recharge" style="width:370px"></el-input>
          </el-form-item>

          <el-form-item label="备注" prop="note">
            <el-input v-model="blance.note" style="width:370px"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="blance.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleEditBlance">确认充值余额</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import { fetchList, deleteAdmin } from "@/api/admin";
import { getToken, get } from "@/utils/auth";
import { Message } from "element-ui";
import { formatDate } from "@/utils/date";
import { createRecharge } from "@/api/dealer/dealer";

const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  status: 2,
  dealerName: null,
  realname: null,
  dealerPhone: null
};

export default {
  name: "settledInList",
  data() {
    return {
      listQuery: Object.assign({}, defaultListQuery),
      rules: {
        recharge: [
          { required: true, message: "请输入充值数目", trigger: "blur" }
        ]
      },
      blance: {
        dialogVisible: false,
        id: null
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
        path: "/dealer/updateDealer",
        query: { id: row.id }
      });
    },
    handleInfo(index, row) {
      this.$router.push({
        path: "/dealer/detailsList",
        query: { id: row.id }
      });
    },
    handleCharge(index, row) {
      this.blance.dialogVisible = true;
      if (row.balance) {
        this.blance.balance = row.balance;
      } else {
        this.blance.balance = 0;
      }
      this.blance.dealerId = row.id;
    },

    handleEditBlance() {
      let params = new URLSearchParams();

      let data = {
        balance: Number(this.blance.balance),
        note: this.blance.note,
        createBy: get("userId"),
        dealerId: this.blance.dealerId,
        recharge: Number(this.blance.recharge),
        newBalance: Number(this.blance.balance) + Number(this.blance.recharge)
      };

      createRecharge(data).then(response => {
        this.$message({
          message: "充值成功",
          type: "success",
          duration: 1000
        });
        this.getList();
      });
      this.blance.dialogVisible = false;
    },

    handleDelete(index, row) {
      this.$confirm("是否要删除该经销商数据", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteAdmin(row.id).then(response => {
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
    searchMerchatBusinessMaterialsList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    addMerchatBusinessMaterials() {
      //手动将router,改成$router
      this.$router.push({ path: "/dealer/addDealer" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped></style>
