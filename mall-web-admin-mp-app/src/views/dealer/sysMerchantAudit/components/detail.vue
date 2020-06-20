<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="sysMerchantAudit"
      :rules="rules"
      ref="SysMerchantAuditFrom"
      label-width="150px"
    >
      <el-form-item label="授权商家" prop="dealerId">
        <!-- <el-input v-model="sysMerchantAudit.dealerId" style="width: 370px;" /> -->
        <el-input
              :disabled="true"
              placeholder="请选择上级账号"
              v-model="sysMerchantAudit.dealerId"
              class="input-with-select"
            >
              <el-button slot="append" @click="choose" type="primary" icon="el-icon-search">选择</el-button>
            </el-input>
      </el-form-item>

      <el-form-item label="消费授权" prop="sale">
        <el-checkbox v-model="sale" @change="saleChange">消费授权</el-checkbox>
      </el-form-item>

      <el-form-item label="组内消费" prop="groupSale">
        <el-checkbox v-model="groupSale" @change="groupSaleChange">组内消费</el-checkbox>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('SysMerchantAuditFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('SysMerchantAuditFrom')">重置</el-button>
      </el-form-item>
    </el-form>

    <div>
      <el-dialog title="充值" :visible.sync="blance.dialogVisible" width="40%">
        <el-table
          ref="merchatBusinessMaterialsTable"
          :data="list"
          style="width: 100%;"
          v-loading="listLoading"
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
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            layout="total, sizes,prev, pager, next,jumper"
            :page-size="listQuery.pageSize"
            :page-sizes="[5, 10, 15]"
            :current-page.sync="listQuery.pageNum"
            :total="total"
          ></el-pagination>
        </div>
      </el-dialog>
    </div>

  </el-card>
</template>
<script>
import {
  createSysMerchantAudit,
  getSysMerchantAudit,
  updateSysMerchantAudit
} from "@/api/dealer/sysMerchantAudit";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";
import { fetchList } from "@/api/admin";

const defaultSysMerchantAudit = {};
export default {
  name: "SysMerchantAuditDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      blance: {
        dialogVisible: false,
        id: null
      },
      groupSale: false,
      sale: false,
      sysMerchantAudit: Object.assign({}, defaultSysMerchantAudit),
      rules: {
        name: [{ required: true, message: "请输入品牌名称", trigger: "blur" }]
      },
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        applyStatus: 2,
        dealerName: null,
        realname: null,
        dealerPhone: null
      },
      list: null,
      total: null,
      listLoading: true
    };
  },
  created() {
    this.getList();
    if (this.isEdit) {
      getSysMerchantAudit(this.$route.query.id).then(response => {
        this.sysMerchantAudit = response.data;
        if (this.sysMerchantAudit.sale == "1") {
          this.sale = true;
        } else {
          this.sale = false;
        }
        if (this.sysMerchantAudit.groupSale == "1") {
          this.groupSale = true;
        } else {
          this.groupSale = false;
        }
      });
    } else {
      this.sysMerchantAudit = Object.assign({}, defaultSysMerchantAudit);
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
    choose() {
      this.blance.dialogVisible = true;
    },
    handleChoose(index, row) {
      this.sysMerchantAudit.dealerId = row.id;
      this.blance.dialogVisible = false;
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
    saleChange(e) {
      if (e === true) {
        this.sysMerchantAudit.sale = 1;
      } else {
        this.sysMerchantAudit.sale = 0;
      }
    },
    groupSaleChange(e) {
      if (e === true) {
        this.sysMerchantAudit.groupSale = 1;
      } else {
        this.sysMerchantAudit.groupSale = 0;
      }
    },
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              updateSysMerchantAudit(
                this.$route.query.id,
                this.sysMerchantAudit
              ).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                  this.$router.back();
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            } else {
              this.sysMerchantAudit.initiateId = get("userId");
              this.sysMerchantAudit.status = 0;
              // 0是等待授权的或者不勾的
              // 1是授权成功或者勾了的
              createSysMerchantAudit(this.sysMerchantAudit).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.sysMerchantAudit = Object.assign(
                    {},
                    defaultSysMerchantAudit
                  );
                  this.$message({
                    message: "提交成功",
                    type: "success",
                    duration: 1000
                  });
                  this.$router.back();
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            }
          });
        } else {
          this.$message({
            message: "验证失败",
            type: "error",
            duration: 1000
          });
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.sysMerchantAudit = Object.assign({}, defaultSysMerchantAudit);
    }
  }
};
</script>
<style>
</style>


