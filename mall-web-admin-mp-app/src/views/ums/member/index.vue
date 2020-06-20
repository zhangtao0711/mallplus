<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button style="float: right" @click="searchMemberList()" type="primary" size="small">查询结果</el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="输入搜索：">
            <el-input style="width: 203px" v-model="listQuery.phone" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="会员等级：">
            <el-select v-model="listQuery.memberLevelId" placeholder="请选择会员等级" clearable>
              <el-option
                v-for="item in brandOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>

      <div style="float:right">
        <el-button type="primary" size="small" @click="derived">会员卡导出</el-button>
        <el-button type="primary" size="small" @click="limit">限制消费</el-button>
        <el-button type="primary" size="small" @click="bindCard">批量绑卡</el-button>
        <el-button size="mini" type="primary" @click="toDetail(11)">详情</el-button>
      </div>
    </el-card>
    <div class="table-container">
      <el-table
        ref="brandTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="60" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>

        <el-table-column label="用户昵称" width="140" align="center">
          <template slot-scope="scope">{{scope.row.nickname}}</template>
        </el-table-column>
        <el-table-column label="手机号" width="140" align="center">
          <template slot-scope="scope">{{scope.row.phone}}</template>
        </el-table-column>

        <el-table-column label="虚拟卡" width="140" align="center">
          <template slot-scope="scope">{{scope.row.cardInventedNum}}</template>
        </el-table-column>

        <el-table-column label="实体卡" width="140" align="center">
          <template slot-scope="scope">{{scope.row.cardNum}}</template>
        </el-table-column>

        <el-table-column label="公众号" width="140" align="center">
          <template slot-scope="scope">{{scope.row.uniacName}}</template>
        </el-table-column>

        <el-table-column label="会员等级" width="140" align="center">
          <template slot-scope="scope">{{scope.row.memberLevelName}}</template>
        </el-table-column>

        <el-table-column label="会员标签" width="140" align="center">
          <template slot-scope="scope">{{scope.row.labelName}}</template>
        </el-table-column>

        <el-table-column label="来源" width="140" align="center">
          <template slot-scope="scope">{{scope.row.sourceType | formatType}}</template>
        </el-table-column>

        <el-table-column label="推荐人" width="140" align="center">
          <template slot-scope="scope">{{scope.row.recommendName}}</template>
        </el-table-column>

        <el-table-column label="推荐人数" width="140" align="center">
          <template slot-scope="scope">{{scope.row.recommendNum}}</template>
        </el-table-column>

        <el-table-column label="用户积分" width="80" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.integration}}</p>
            <!-- <p>
              <el-button
                type="text"
                @click="handleShowIntegrationDialog(scope.$index, scope.row)"
              >积分记录</el-button>
            </p> -->
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="addBlacne(scope.$index, scope.row)">限制消费金额</el-button>
            <el-button size="mini" type="primary" @click="toDetail(scope.$index, scope.row)">详情</el-button>
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

    <el-dialog title="积分记录" :visible.sync="dialogVisible1" width="40%">
      <el-table style="width: 100%;margin-top: 20px" :data="integrationList" border>
        <el-table-column label="编号" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="会员" align="center">
          <template slot-scope="scope">{{scope.row.memberId}}</template>
        </el-table-column>
        <el-table-column label="价格" align="center">
          <template slot-scope="scope">{{scope.row.changeCount}}</template>
        </el-table-column>
        <el-table-column label="类别" align="center">
          <template slot-scope="scope">{{scope.row.changeType |formatchangeType}}</template>
        </el-table-column>
        <el-table-column label="来源" align="center">
          <template slot-scope="scope">{{scope.row.sourceType |formatsourceType}}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{scope.row.operateNote}}</template>
        </el-table-column>
        <el-table-column label="创建时间" align="center">
          <template slot-scope="scope">{{scope.row.createTime | formatTime}}</template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog title="限制消费金额" :visible.sync="blance.dialogVisible" width="40%">
      <el-form :model="blance" ref="brandFrom" label-width="150px">
        <el-form-item label="单次消费：" prop="detail">
          <el-input v-model="blance.xfxeLimit"></el-input>
        </el-form-item>

        <el-form-item label="累计消费：" prop="detail">
          <el-input placeholder="24小时刷新一次" v-model="blance.xfNumLimit"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button @click="blance.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleEditBlance">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { formatDate } from "@/utils/date";
import {
  fetchList,
  updateShowStatus,
  updateFactoryStatus,
  deleteMember,
  updateMemberOrderInfo,
  handleEditIntegration,
  handleEditBlance,
  updateLimit
} from "@/api/ums/member";
import { fetchList as fetchBlanceList } from "@/api/ums/memberBlanceLog";
import { fetchList as fetchIntegrationList } from "@/api/ums/memberIntegration";
import { fetchList as fetchMberLevelList } from "@/api/memberLevel";

import axios from "axios";
import { getToken, get } from "@/utils/auth";

import { fetchList as fetchOrderList } from "@/api/order";
export default {
  name: "memberList",
  data() {
    return {
      dialogVisible1: false,
      blanceList: null,
      integrationList: null,
      orderList: null,
      blance: {
        dialogVisible: false,
        id: null
      },
      brandOptions: [],
      listQuery: {
        phone: null,
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
    // this.getList();
    this.geteMberLevelList();
  },
  filters: {
    formatType(value) {
      if (value === 1) {
        return "小程序";
      } else if (value === 2) {
        return "h5";
      } else if (value === 3) {
        return "app";
      }
    },
  },
  methods: {
    derived() {
      this.$router.push({ path: "/ums/updateMember" });
    },
    limit() {
      this.$router.push({ path: "/ums/wtWaterCardLimit" });
    },
    bindCard() {
      this.$router.push({ path: "/ums/batchCardBinding" });
    },
    // toDetail(index, row) {
    //   this.$router.push({ path: "/ums/updateMember", query: { id: row.id } });
    // },
    toDetail(id) {
      this.$router.push({ path: "/ums/updateMember", query: { id: id } });
    },
    geteMberLevelList() {
      fetchMberLevelList({ pageNum: 1, pageSize: 100 }).then(response => {
        this.brandOptions = [];
        let brandList = response.data.records;
        for (let i = 0; i < brandList.length; i++) {
          this.brandOptions.push({
            label: brandList[i].name,
            value: brandList[i].id
          });
        }
      });
    },
    addBlacne(index, row) {
      this.blance.dialogVisible = true;
      this.blance.id = row.cardNo;
    },
    handleEditBlance() {
      if (this.blance.xfxeLimit == null) {
        this.$message({
          message: "请输入单次消费",
          type: "warning",
          duration: 1000
        });
        return;
      }
      if (this.blance.xfNumLimit == null) {
        this.$message({
          message: "请输入累计消费",
          type: "warning",
          duration: 1000
        });
        return;
      }
      this.$confirm("是否要进行限制消费金额", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // updateLimit(this.blance.id,params).then(response => {
        //   this.$message({
        //     message: "操作成功",
        //     type: "success",
        //     duration: 1000
        //   });
        //   this.getList();
        // });

        axios({
          method: "POST",
          url: process.env.BASE_API + "/water/wtWaterCard/updateLimit",
          headers: {
            // "Content-Type": "multipart/form-data",
            Authorization: getToken()
          },
          data: {
            cardNo: this.blance.id,
            xfxeLimit: this.blance.xfxeLimit,
            xfNumLimit: this.blance.xfNumLimit
          }
        })
          .then(response => {
            if (response.data.code == 200) {
              // console.log(response);
              this.blance.xfxeLimit = "";
              this.blance.xfNumLimit = "";
            } else {
              this.$message.error(response.data.msg);
            }
          })
          .catch(function(err) {
            console.log(err);
          });

        this.blance.dialogVisible = false;
      });
    },
    handleShowIntegrationDialog(index, row) {
      this.dialogVisible1 = true;
      fetchIntegrationList({ memberId: row.id, pageSize: 1000 }).then(
        response => {
          this.integrationList = response.data.records;
        }
      );
    },

    handleShowChange(index, row) {
      let params = new URLSearchParams();
      params.append("ids", row.id);
      params.append("showStatus", row.showStatus);
      updateShowStatus(params).then(response => {
        this.$message({
          message: "修改成功",
          type: "success",
          duration: 1000
        });
      });
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
    handleDelete(index, row) {
      this.$confirm("是否要删除该会员", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteMember(row.id).then(response => {
          this.$message({
            message: "删除成功",
            type: "success",
            duration: 1000
          });
          this.getList();
        });
      });
    },
    handleFactoryStatusChange(index, row) {
      var data = new URLSearchParams();
      data.append("ids", row.id);
      data.append("factoryStatus", row.factoryStatus);
      updateFactoryStatus(data)
        .then(response => {
          this.$message({
            message: "修改成功",
            type: "success",
            duration: 1000
          });
        })
        .catch(error => {
          if (row.factoryStatus === 0) {
            row.factoryStatus = 1;
          } else {
            row.factoryStatus = 0;
          }
        });
    },
    handleShowStatusChange(index, row) {
      let data = new URLSearchParams();
      data.append("ids", row.id);
      data.append("showStatus", row.showStatus);
      updateShowStatus(data)
        .then(response => {
          this.$message({
            message: "修改成功",
            type: "success",
            duration: 1000
          });
        })
        .catch(error => {
          if (row.showStatus === 0) {
            row.showStatus = 1;
          } else {
            row.showStatus = 0;
          }
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
    searchMemberList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


