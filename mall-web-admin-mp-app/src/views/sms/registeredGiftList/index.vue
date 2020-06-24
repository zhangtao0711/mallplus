<template>
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
    </el-card>
    <div class="table-container">
      <el-table
        ref="SmsBasicGiftsTable"
        :data="list"
        style="width: 100%;"
        v-loading="listLoading"
        border
      >
        <el-table-column label="编号" width="120" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>

        <el-table-column label="用户名称" align="center">
          <template slot-scope="scope">{{scope.row.bigType}}</template>
        </el-table-column>

        <el-table-column label="赠送积分" align="center">
          <template slot-scope="scope">{{scope.row.smallType}}</template>
        </el-table-column>

        <el-table-column label="注册时间" align="center">
          <template slot-scope="scope">
            <p>开始时间：{{scope.row.startTime | formatTime}}</p>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

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
  updateStatus,
  deleteSmsBasicGifts
} from "@/api/sms/smsBasicGifts";
import { formatDate } from "@/utils/date";
const defaultListQuery = {
  pageNum: 1,
  pageSize: 5,
  name: null,
  type: null,
  endTime: null
};
export default {
  name: "registeredGiftList",
  data() {
    return {
      listQuery: Object.assign({}, defaultListQuery),
      list: null,
      total: null,
      listLoading: false
    };
  },
  created() {
    this.getList();
  },
  filters: {
    formatTime(time) {
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
    },
    handleSearchList() {
      this.listQuery.pageNum = 1;
      this.getList();
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
    handleDelete(index, row) {
      this.deleteSmsBasicGifts(row.id);
    },
    handleAdd() {
      this.$router.push({ path: "/sms/addSmsBasicGifts" });
    },
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then(response => {
        this.listLoading = false;
        this.list = response.data.records;
        this.total = response.data.total;
      });
    },
    deleteHomeAdvertise(ids) {
      this.$confirm("是否要删除该广告?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let params = new URLSearchParams();
        params.append("ids", ids);
        deleteHomeAdvertise(params).then(response => {
          this.getList();
          this.$message({
            type: "success",
            message: "删除成功!"
          });
        });
      });
    }
  }
};
</script>
<style scoped>
.input-width {
  width: 203px;
}
</style>


