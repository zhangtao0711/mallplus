<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchWtWaterCardCreateList()"
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
          <el-form-item label="代号">
            <el-input style="width: 203px" v-model="listQuery.code" placeholder="按代号查询"></el-input>
          </el-form-item>
          <el-form-item label="起始卡号">
            <el-input style="width: 203px" v-model="listQuery.startNo" placeholder="按起始卡号查询"></el-input>
          </el-form-item>
          <el-form-item label="终止卡号">
            <el-input style="width: 203px" v-model="listQuery.endNo" placeholder="按终止卡号查询"></el-input>
          </el-form-item>
          <el-form-item label="识别码">
            <el-input style="width: 203px" v-model="listQuery.distinguishNum" placeholder="按识别码查询"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="addWtWaterCardCreate()" size="mini">批量制卡</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="wtWaterCardCreateTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column type="selection" width="60" align="center"></el-table-column>

        <el-table-column prop="id" label="编号" width="100" align="center">
          <template slot-scope="scope">{{scope.row.id }}</template>
        </el-table-column>
        <el-table-column prop="code" label="代号" align="center">
          <template slot-scope="scope">{{scope.row.code }}</template>
        </el-table-column>
        <el-table-column prop="startNo" label="起始卡号" align="center">
          <template slot-scope="scope">{{scope.row.startNo }}</template>
        </el-table-column>
        <el-table-column prop="endNo" label="终止卡号" align="center">
          <template slot-scope="scope">{{scope.row.endNo }}</template>
        </el-table-column>
        <el-table-column prop="number" label="数量" align="center">
          <template slot-scope="scope">{{scope.row.number }}</template>
        </el-table-column>
        <el-table-column prop="acid" label="关联公众号id" align="center">
          <template slot-scope="scope">{{scope.row.acid }}</template>
        </el-table-column>
        <el-table-column prop="distinguishNum" label="识别码" align="center">
          <template slot-scope="scope">{{scope.row.distinguishNum }}</template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
            <!-- <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button> -->
            <el-button size="mini" type="warning" @click="handleAllot(scope.$index, scope.row)">分配</el-button>
            <el-button
              @click="exportExcel(scope.$index, scope.row)"
              class="btn-add"
              type="primary"
              size="mini"
              icon="el-icon-download"
            >导出</el-button>
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

    <div>
      <el-dialog title="分配" :visible.sync="blance.dialogVisible" width="40%">
        <el-form :model="blance" ref="brandFrom" label-width="150px">
          <el-form-item label="经销商：" prop="id" v-show="false">
            <el-input v-model="blance.id"></el-input>
          </el-form-item>

          <el-form-item label="经销商：" prop="dealerId">
            <el-input placeholder="请选择经销商" v-model="blance.dealerId" class="input-with-select">
              <el-button slot="append" @click="chooseZ" icon="el-icon-search">选择</el-button>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button @click="blance.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleEditBlance">确 定</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>

    <el-dialog title="数据选择器" :visible.sync="dealer.dialogVisible" width="40%">
      <el-table
        ref="merchatBusinessMaterialsTable"
        :data="dealerlist"
        style="width: 100%;"
        v-loading="dealerlistLoading"
        border
      >
        <el-table-column prop="id" label="经销商账号" width="80" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>

        <el-table-column prop="nickName" label="用户昵称 " align="center">
          <template slot-scope="scope">{{ scope.row.nickName }}</template>
        </el-table-column>

        <el-table-column prop="mchid" label="商户号 " align="center">
          <template slot-scope="scope">{{ scope.row.mchid }}</template>
        </el-table-column>

        <el-table-column prop="phone" label="手机号 " align="center">
          <template slot-scope="scope">{{ scope.row.phone }}</template>
        </el-table-column>

        <el-table-column label="操作" width="150" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="handleChoose(scope.$index, scope.row)"
              >选择</el-button>
            </template>
          </el-table-column>

      </el-table>

      <div class="batch-operate-container"></div>
      <div style="text-align:right">
        <el-pagination
          background
          @size-change="handleSizeDealerChange"
          @current-change="handleCurrentDealerChange"
          layout="total, sizes,prev, pager, next,jumper"
          :page-size="dealerlistQuery.pageSize"
          :page-sizes="[5, 10, 15]"
          :current-page.sync="dealerlistQuery.pageNum"
          :total="dealertotal"
        ></el-pagination>
      </div>
      <div style="text-align:right;margin-top:20px">
        <el-button @click="dealer.dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
//将$都替换为$
import {
  fetchList,
  deleteWtWaterCardCreate,
  exportExcel,
  updateDealerId
} from "@/api/water/wtWaterCardCreate";
import { formatDate } from "@/utils/date";
import { fetchList as fetchDealerList } from "@/api/admin";

const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  code: null,
  startNo: null,
  endNo: null,
  distinguishNum: null
};

export default {
  name: "wtWaterCardCreateList",
  data() {
    return {
      loginRules: {
        dealerId: [{ required: true, message: "请选择经销商" }]
      },
      blance: {
        dialogVisible: false,
        id: null
      },
      dealer: {
        dialogVisible: false,
        id: null
      },
      operates: [],
      operateType: null,
      listQuery: Object.assign({}, defaultListQuery),
      list: null,
      total: null,
      listLoading: true,

      dealerlistQuery: Object.assign({}, defaultListQuery),
      dealerlist: null,
      dealertotal: null,
      dealerlistLoading: true,

      multipleSelection: []
    };
  },
  created() {
    this.getList();
    this.getList1()
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
    getList1() {
      this.dealerlistLoading = true;
      fetchDealerList(this.dealerlistQuery).then(response => {
        this.dealerlistLoading = false;
        this.dealerlist = response.data.records;
        this.dealertotal = response.data.total;
      });
    },
    chooseZ() {
      this.dealer.dialogVisible = true;
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
    handleSizeDealerChange(val) {
      this.listQuery.pageNum = 1;
      this.listQuery.pageSize = val;
      this.getList1();
    },
    handleCurrentDealerChange(val) {
      this.listQuery.pageNum = val;
      this.getList1();
    },
    handleChoose(index, row) {
      this.blance.dealerId = row.id;
      this.dealer.dialogVisible = false;
    },
    handleResetSearch() {
      this.listQuery = Object.assign({}, defaultListQuery);
      this.getList();
    },
    handleAllot(index, row) {
      this.blance.dialogVisible = true;
      this.blance.id = row.id;
    },
    handleEditBlance() {
      if (this.blance.dealerId == null) {
        this.$message({
          message: "请选择经销商",
          type: "warning",
          duration: 1000
        });
        return;
      }
      let data = {
        id: this.blance.id,
        dealerId: this.blance.dealerId
      };

      updateDealerId(this.blance.id, data).then(response => {
        this.$message({
          message: response.msg,
          type: "success",
          duration: 1000
        });
      });
      this.blance.dialogVisible = false;
    },
    exportExcel(index, row) {
      window.open(
        process.env.BASE_API +
          "/water/wtWaterCardCreate/exportExcel?id=" +
          row.id
        // "/water/wtWaterCardCreate?id=" + row.id
      );
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
        path: "/ums/updateWtWaterCardCreate",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtWaterCardCreate(row.id).then(response => {
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
    searchWtWaterCardCreateList() {
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
      if (this.operateType === "showWtWaterCardCreate") {
        showStatus = 1;
      } else if (this.operateType === "hideWtWaterCardCreate") {
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
    addWtWaterCardCreate() {
      //手动将router,改成$router
      this.$router.push({ path: "/ums/addWtWaterCardCreate" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


