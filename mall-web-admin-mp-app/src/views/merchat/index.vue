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
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="业务申请编号：">
            <el-input
              style="width: 203px"
              v-model="listQuery.businessCode"
              placeholder="请输入业务申请编号搜索"
            ></el-input>
          </el-form-item>

          <el-form-item label="超级管理员姓名：">
            <el-input
              style="width: 203px"
              v-model="listQuery.contactName"
              placeholder="请输入超级管理员姓名搜索"
            ></el-input>
          </el-form-item>

          <el-form-item label="商户简称：">
            <el-input
              style="width: 203px"
              v-model="listQuery.merchantShortname"
              placeholder="请输入商户简称搜索"
            ></el-input>
          </el-form-item>

          <el-form-item label="特约商户号：">
            <el-input
              style="width: 203px"
              v-model="listQuery.subMchid"
              placeholder="请输入特约商户号搜索"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="addMerchatBusinessMaterials()" size="mini">添加</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="merchatBusinessMaterialsTable"
        :data="list"
        style="width: 100%"
        v-loading="listLoading"
        border
      >
        <el-table-column prop="id" label="主键" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>

        <el-table-column prop="businessCode" label="业务申请编号" align="center">
          <template slot-scope="scope">{{ scope.row.businessCode }}</template>
        </el-table-column>

        <el-table-column prop="contactName" label="超级管理员姓名" align="center">
          <template slot-scope="scope">{{ scope.row.contactName }}</template>
        </el-table-column>

        <el-table-column prop="mobilePhone" label="联系手机" align="center">
          <template slot-scope="scope">{{ scope.row.mobilePhone }}</template>
        </el-table-column>

        <el-table-column prop="contactEmail" label="联系邮箱" align="center">
          <template slot-scope="scope">{{ scope.row.contactEmail }}</template>
        </el-table-column>

        <el-table-column prop="subjectType" label="主体类型" align="center">
          <template slot-scope="scope">{{ scope.row.subjectType | formatStatus }}</template>
        </el-table-column>

        <el-table-column prop="merchantShortname" label="商户简称" align="center">
          <template slot-scope="scope">{{ scope.row.merchantShortname }}</template>
        </el-table-column>

        <el-table-column prop="servicePhone" label="客服电话" align="center">
          <template slot-scope="scope">{{ scope.row.servicePhone }}</template>
        </el-table-column>

        <el-table-column prop="applymentId" label="微信支付申请单号" align="center">
          <template slot-scope="scope">{{ scope.row.applymentId }}</template>
        </el-table-column>

        <el-table-column prop="applymentState" label="申请单状态" align="center">
          <template slot-scope="scope">{{ scope.row.applymentState | formatApplymentState }}</template>
        </el-table-column>

        <el-table-column prop="subMchid" label="特约商户号" align="center">
          <template slot-scope="scope">{{ scope.row.subMchid }}</template>
        </el-table-column>

        <el-table-column prop="signUrl" label="超级管理员签约链接" align="center">
          <template slot-scope="scope">{{ scope.row.signUrl }}</template>
        </el-table-column>

        <el-table-column prop="applymentStateMsg" label="申请状态描述" align="center">
          <template slot-scope="scope">{{ scope.row.applymentStateMsg }}</template>
        </el-table-column>

        <el-table-column prop="rejectReason" label="驳回原因" align="center">
          <template slot-scope="scope">{{ scope.row.rejectReason }}</template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              v-if="!scope.row.applymentState || scope.row.applymentState!='APPLYMENT_STATE_FINISHED' ||  scope.row.applymentState!='APPLYMENT_STATE_CANCELED'"
              @click="handleUpdate(scope.$index, scope.row)"
            >编辑</el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleQueryResults(scope.$index, scope.row)"
            >查询结果</el-button>
            <el-button size="mini" type="danger" @click="toView(scope.$index, scope.row)">查看</el-button>
            <!-- <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button> -->
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

    <el-dialog title="提示" :visible.sync="dialogVisible" width="45%">
      <div v-if="resultList">
        <div>
          <h3>申请单状态：</h3>
          <p class="content">{{ resultList.applymentState | formatApplymentState }}</p>
        </div>
        <div>
          <h3>超级管理员签约链接：</h3>
          <p class="content">{{ resultList.signUrl }}</p>
        </div>
        <div>
          <h3>驳回原因：</h3>
          <p class="content">{{ resultList.rejectReason }}</p>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  fetchList,
  deleteMerchatBusinessMaterials,
  queryByApplymentId,
  queryByBusinessCode
} from "@/api/merchat/merchatBusinessMaterials";
import { formatDate } from "@/utils/date";
import axios from "axios";
import { getToken } from "@/utils/auth";
import { Message } from "element-ui";

export default {
  name: "merchatBusinessMaterialsList",
  data() {
    return {
      resultList: null,
      dialogVisible: false,
      listQuery: {
        keyword: null,
        pageNum: 1,
        pageSize: 10
      },
      list: null,
      total: null,
      listLoading: true,
      url: process.env.BASE_API + "/merchat/merchatBusinessMaterials/list"
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
    // 账户类型
    formatBankAccountType(value) {
      if (value === "BANK_ACCOUNT_TYPE_CORPORATE") {
        return "对公银行账户";
      } else if (value === "BANK_ACCOUNT_TYPE_PERSONAL") {
        return "经营者个人银行卡";
      }
    },
    formatScenesType(value) {
      if (value === "SALES_SCENES_STORE") {
        return "线下门店";
      } else if (value === "SALES_SCENES_MP") {
        return "公众号";
      } else if (value === "SALES_SCENES_MINI_PROGRAM") {
        return "小程序";
      } else if (value === "SALES_SCENES_WEB") {
        return "互联网";
      } else if (value === "SALES_SCENES_APP") {
        return "APP";
      } else if (value === "SALES_SCENES_WEWORK") {
        return "企业微信";
      }
    },
    formatApplymentState(value) {
      if (value === "APPLYMENT_STATE_EDITTING") {
        return "编辑中：提交申请发生错误导致，请尝试重新提交";
      } else if (value === "APPLYMENT_STATE_AUDITING") {
        return "审核中：申请单正在审核中，超级管理员用微信打开“签约链接”，完成绑定微信号后，申请单进度将通过微信公众号通知超级管理员，引导完成后续步骤";
      } else if (value === "APPLYMENT_STATE_REJECTED") {
        return "已驳回：请按照驳回原因修改申请资料，超级管理员用微信打开“签约链接”，完成绑定微信号，后续申请单进度将通过微信公众号通知超级管理员";
      } else if (value === "APPLYMENT_STATE_TO_BE_CONFIRMED") {
        return "待账户验证：请超级管理员使用微信打开返回的“签约链接”，根据页面指引完成账户验证";
      } else if (value === "APPLYMENT_STATE_TO_BE_SIGNED") {
        return "待签约：请超级管理员使用微信打开返回的“签约链接”，根据页面指引完成签约";
      } else if (value === "APPLYMENT_STATE_SIGNING") {
        return "开通中：系统开通相关权限中，请耐心等待";
      } else if (value === "APPLYMENT_STATE_FINISHED") {
        return "已完成：商户入驻申请已完成";
      } else if (value === "APPLYMENT_STATE_CANCELED") {
        return "已作废：申请单已被撤销";
      }
    },
    formatStatus(value) {
      if (value === "SUBJECT_TYPE_INDIVIDUAL") {
        return "个体户";
      } else if (value === "SUBJECT_TYPE_ENTERPRISE") {
        return "企业";
      } else if (value === "SUBJECT_TYPE_INSTITUTIONS") {
        return "党政、机关及事业单位";
      } else if (value === "SUBJECT_TYPE_OTHERS") {
        return "其他组织";
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
    handleUpdate(index, row) {
      this.$router.push({
        path: "/merchat/updateMerchatBusinessMaterials",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteMerchatBusinessMaterials(row.id).then(response => {
          this.$message({
            message: "删除成功",
            type: "success",
            duration: 1000
          });
          this.getList();
        });
      });
    },
    toView(index, row) {
      this.$router.push({
        path: "/merchat/merchatDetail",
        query: { businessCode: row.businessCode }
      });
    },
    handleQueryResults(index, row) {
      this.dialogVisible = true;
      queryByBusinessCode({ businessCode: row.businessCode }).then(response => {
        this.resultList = response.data;
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
      this.$router.push({ path: "/merchat/addMerchatBusinessMaterials" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .content{
    font-size: 18px;
    text-indent: 2em;
  }
</style>
