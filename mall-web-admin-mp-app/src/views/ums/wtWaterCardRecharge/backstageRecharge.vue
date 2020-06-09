<template>
  <el-card class="form-container" shadow="never">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="普通充值" name="first">
        <el-form
          :model="wtWaterCardRecharge"
          :rules="rules"
          ref="WtWaterCardRechargeFrom"
          label-width="150px"
        >
          <el-form-item label="充值金额类型" prop="cardType">
            <el-radio-group v-model="wtWaterCardRecharge.cardType" @change="chooseCardType">
              <el-radio :label="0">实体卡</el-radio>
              <el-radio :label="1">虚拟卡</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="卡号" prop="cardNo">
            <el-input v-model="wtWaterCardRecharge.cardNo" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="充值金额" prop="rechargeMoney">
            <el-input-number
              v-model="wtWaterCardRecharge.rechargeMoney"
              :precision="2"
              :step="0.1"
              :min="0"
              style="width: 370px;"
            />
          </el-form-item>

          <el-form-item label="实收金额" prop="receivedMoney">
            <el-input-number
              v-model="wtWaterCardRecharge.receivedMoney"
              :precision="2"
              :step="0.1"
              :min="0"
              style="width: 370px;"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="onSubmit('WtWaterCardRechargeFrom')">提交</el-button>
            <el-button v-if="!isEdit" @click="resetForm('WtWaterCardRechargeFrom')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="套餐充值" name="second">
        <el-form
          :model="rechargePackage"
          :rules="rules"
          ref="rechargePackageFrom"
          label-width="150px"
        >
          <el-form-item label="卡号" prop="cardNo">
            <el-input v-model="rechargePackage.cardNo" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="选择套餐" prop="rechargePackage">
            <el-input
              placeholder="请输入内容"
              v-model="rechargePackage.rechargePackage"
              class="input-with-select"
            >
              <el-button slot="append" @click="choosePackage" icon="el-icon-search">选择</el-button>
            </el-input>
          </el-form-item>

          <el-form-item label="实收金额" prop="receivedMoney">
            <el-input-number
              v-model="rechargePackage.receivedMoney"
              :precision="2"
              :step="0.1"
              :min="0"
              style="width: 370px;"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="onSubmit1('rechargePackageFrom')">提交</el-button>
            <el-button v-if="!isEdit" @click="resetForm1('rechargePackageFrom')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>

    <div>
      <el-dialog title="数据选择器" :visible.sync="blance.dialogVisible" width="40%">
        <el-table
          ref="merchatBusinessMaterialsTable"
          :data="list"
          style="width: 100%;"
          v-loading="listLoading"
          border
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="总经销商账号" width="80" align="center">
            <template slot-scope="scope">{{ scope.row.id }}</template>
          </el-table-column>

          <!-- <el-table-column prop="realname" label="联系人 " align="center">
            <template slot-scope="scope">{{ scope.row.realname }}</template>
          </el-table-column>

          <el-table-column prop="dealerName" label="商户名称 " align="center">
            <template slot-scope="scope">{{ scope.row.dealerName }}</template>
          </el-table-column>

          <el-table-column prop="dealerPhone" label="手机号 " align="center">
            <template slot-scope="scope">{{ scope.row.dealerPhone }}</template>
          </el-table-column>-->
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
        <div style="text-align:right;margin-top:20px">
          <el-button type="primary" @click="handleEditBlance">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </el-card>
</template>
<script>
import {
  createSingle,
  createSinglePackage,
  getWtWaterCardRecharge,
  updateWtWaterCardRecharge
} from "@/api/water/wtWaterCardRecharge";

import { fetchList } from "@/api/sms/smsRechargePackage";

import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWaterCardRecharge = {
  rechargeType: 0
};
const defaultRechargePackage = {
  rechargeType: 0
};
export default {
  name: "backstageRecharge",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      listQuery: {
        pageNum: 1,
        pageSize: 10
      },
      list: null,
      total: null,
      listLoading: true,
      blance: {
        dialogVisible: false,
        id: null
      },
      activeName: "first",
      wtWaterCardRecharge: Object.assign({}, defaultWtWaterCardRecharge),
      rechargePackage: Object.assign({}, defaultRechargePackage),
      rules: {
        cardNo: [{ required: true, message: "请输入卡号", trigger: "blur" }],
        rechargeMoney: [
          { required: true, message: "请输入充值金额", trigger: "blur" }
        ],
        receivedMoney: [{ required: true, message: "请输入实收金额" }]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getWtWaterCardRecharge(this.$route.query.id).then(response => {
        this.wtWaterCardRecharge = response.data;
      });
    } else {
      this.wtWaterCardRecharge = Object.assign({}, defaultWtWaterCardRecharge);
    }
  },
  methods: {
    choosePackage() {
      this.getList();
      this.listLoading = false;
      this.blance.dialogVisible = true;
    },
    handleEditBlance() {
      this.blance.dialogVisible = false;
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
    handleSelectionChange(val) {},
    handleSizeChange(val) {
      this.listQuery.pageNum = 1;
      this.listQuery.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val;
      this.getList();
    },

    handleClick(tab, event) {
      if (tab.name == "first") {
        this.wtWaterCardRecharge.rechargeType = 0;
        this.wtWaterCardRecharge.cardNo = "";
        this.wtWaterCardRecharge.rechargeMoney = "";
        this.wtWaterCardRecharge.receivedMoney = "";
      } else if (tab.name == "second") {
        this.wtWaterCardRecharge.rechargeType = 1;
        this.wtWaterCardRecharge.cardNo = "";
        this.wtWaterCardRecharge.rechargeMoney = "";
        this.wtWaterCardRecharge.receivedMoney = "";
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
              updateWtWaterCardRecharge(
                this.$route.query.id,
                this.wtWaterCardRecharge
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
              this.wtWaterCardRecharge.createBy = get("userId");
              this.wtWaterCardRecharge.dealerId = get("storeId");
              this.wtWaterCardRecharge.storeId = get("storeId");
              createSingle(this.wtWaterCardRecharge).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtWaterCardRecharge = Object.assign(
                    {},
                    defaultWtWaterCardRecharge
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
      this.wtWaterCardRecharge = Object.assign({}, defaultWtWaterCardRecharge);
    },
    onSubmit1(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              updateWtWaterCardRecharge(
                this.$route.query.id,
                this.rechargePackage
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
              this.rechargePackage.createBy = get("userId");
              this.rechargePackage.dealerId = get("storeId");
              this.rechargePackage.storeId = get("storeId");
              this.rechargePackage.rechargePackage = "1,2";
              createSinglePackage(this.rechargePackage).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.rechargePackage = Object.assign(
                    {},
                    defaultRechargePackage
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
    resetForm1(formName) {
      this.$refs[formName].resetFields();
      this.rechargePackage = Object.assign({}, defaultRechargePackage);
    }
  }
};
</script>
<style>
</style>


