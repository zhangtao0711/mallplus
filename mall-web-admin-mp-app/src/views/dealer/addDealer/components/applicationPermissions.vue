<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="dealerUse" :rules="rules" ref="WtWaterCardActivateFrom" label-width="150px">
      <h3>是否可购买营销功能</h3>
      <span>选择可以开通的营销功能，用户可以购买</span>
      <el-form-item prop="permissionIds">
        <el-checkbox-group
          :disabled="disabled"
          v-model="permissionIds"
          @change="selectedPermissionIds"
        >
          <el-checkbox
            v-for="permission in permissionList"
            :label="permission.permission_id"
            :key="permission.permission_id"
          >{{permission.permission_name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-divider></el-divider>

      <div class="share">
        <h3>是否开通共享水卡</h3>
        <el-switch v-model="isShareCard" @change="isOpenShare"></el-switch>
      </div>

      <span>开启后用户可在不同设备上消费</span>

      <div v-show="isOpen">
        <el-form-item label="结算价格" prop="settleMoney">
          <el-input v-model="dealerUse.settleMoney" style="width: 370px;" />
        </el-form-item>

        <el-form-item label="共享方式" prop="shareWay">
          <el-radio-group v-model="dealerUse.shareWay" @change="isShare">
            <el-radio label="1">
              单向共享
              <p>允许其他经销商的用户到我的售水机上打水</p>
            </el-radio>
            <el-radio label="2">
              双向共享
              <p>双方允许用户到对方的售水机上打水</p>
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="选择共享商户" prop="share" v-show="shareUser">
          <el-input
            placeholder="请输入商户"
            v-model="dealerUse.shareDealerNames"
            class="input-with-select"
          >
            <el-button slot="append" @click="chooseZ" icon="el-icon-search">选择</el-button>
          </el-input>

          <el-input
            v-show="false"
            placeholder="请输入商户"
            v-model="dealerUse.shareDealerIds"
            class="input-with-select"
          ></el-input>
        </el-form-item>
      </div>

      <el-divider></el-divider>

      <div class="share">
        <h3>是否开通商户审核权限</h3>
        <el-switch v-model="isDealerAudit" @change="isOPenDealerAudit"></el-switch>
      </div>
      <span>只有总经销商才可以开通商户审核权限，经销商和分销商没有开通权限</span>

      <el-divider></el-divider>

      <div class="share">
        <h3>是否开通商户关联公众号、小程序</h3>
        <el-switch v-model="isRelation" @change="isOpenRelation"></el-switch>
      </div>
      <span>开启后可以进行公众号、小程序关联</span>

      <el-divider></el-divider>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardActivateFrom')">保存设置</el-button>
      </el-form-item>
    </el-form>

    <div>
      <el-dialog title="数据选择器" :visible.sync="blance.dialogVisible" width="40%">
        <div>
          <el-row :gutter="20" style="margin-bottom:5px">
            <el-col :span="8">
              <el-input
                size="small"
                placeholder="按商户名称搜索"
                v-model="listQuery.dealerName"
                suffix-icon="el-icon-search"
              ></el-input>
            </el-col>
            <el-col :span="8">
              <el-input
                size="small"
                placeholder="按联系人姓名搜索"
                v-model="listQuery.realname"
                suffix-icon="el-icon-search"
              ></el-input>
            </el-col>
            <el-col :span="8">
              <el-input
                size="small"
                placeholder="按手机号搜索"
                v-model="listQuery.dealerPhone"
                suffix-icon="el-icon-search"
              ></el-input>
            </el-col>
          </el-row>
          <el-button style="float: right;margin-bottom:15px" size="small" @click="clear">重置</el-button>
          <el-button
            style="float: right;margin-bottom:15px;margin-right:5px"
            @click="searchBrandList()"
            type="primary"
            size="small"
          >查询结果</el-button>
        </div>

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

          <el-table-column prop="realname" label="联系人 " align="center">
            <template slot-scope="scope">{{ scope.row.realname }}</template>
          </el-table-column>

          <el-table-column prop="dealerName" label="商户名称 " align="center">
            <template slot-scope="scope">{{ scope.row.dealerName }}</template>
          </el-table-column>

          <el-table-column prop="dealerPhone" label="手机号 " align="center">
            <template slot-scope="scope">{{ scope.row.dealerPhone }}</template>
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
        <div style="text-align:right;margin-top:20px">
          <el-button type="primary" @click="handleEditBlance">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </el-card>
</template>
<script>
import {
  createDealerUse,
  getDealerUse,
  getPermission,
  updateDealerUse
} from "@/api/dealer/dealer";
import { get } from "@/utils/auth";
import { fetchList } from "@/api/admin";

const defaultDealerInfo = {};
export default {
  name: "applicationPermissions",
  props: {
    dealerId: String,
    storeId: String,
    storeName: String,
    value: Object,
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
      disabled: false,
      isEdit: false,
      permissionList: [],
      permissionIds: [],
      isShareCard: false,
      isDealerAudit: false,
      isRelation: false,
      isOpen: false,
      shareUser: false,
      dealerUse: Object.assign({}, defaultDealerInfo),
      rules: {
        name: [{ required: true, message: "请输入品牌名称", trigger: "blur" }],
        logo: [{ required: true, message: "请输入品牌logo", trigger: "blur" }],
        sort: [{ type: "number", message: "排序必须为数字" }]
      }
    };
  },
  created() {
    if (this.$route.query.id) {
      this.dealerId = this.$route.query.id
    }
    if (this.isShareCard === true) {
      this.dealerUse.isShareCard = "1";
    } else {
      this.dealerUse.isShareCard = "0";
    }
    if (this.isDealerAudit === true) {
      this.dealerUse.isDealerAudit = "1";
    } else {
      this.dealerUse.isDealerAudit = "0";
    }
    if (this.isRelation === true) {
      this.dealerUse.isRelation = "1";
    } else {
      this.dealerUse.isRelation = "0";
    }
    this.getDealerUse();
  },
  methods: {
    searchBrandList() {
      this.list = null;
      this.listQuery.pageNum = 1;
      this.getList();
    },
    clear() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 10
      };
      this.getList();
    },
    getDealerUse() {
      getDealerUse(this.dealerId).then(response => {
        if (response.data.length) {
          this.isEdit = false;
          this.disabled = false;
          this.dealerUse = Object.assign({}, defaultDealerInfo);
          this.permissionList = response.data;
        } else {
          this.isEdit = true;
          this.disabled = true;
          this.dealerUse = response.data;
          // 是否开通共享水卡
          if (this.dealerUse.isShareCard == "1") {
            this.isShareCard = true;
            this.isOpen = true;
          } else {
            this.isShareCard = false;
            this.isOpen = false;
          }

          if (this.dealerUse.shareWay == "1") {
            this.shareUser = false;
          } else {
            this.shareUser = true;
          }

          // 是否开通商户审核权限
          if (this.dealerUse.isDealerAudit == "1") {
            this.isDealerAudit = true;
          } else {
            this.isDealerAudit = false;
          }

          // 是否开通商户关联公众号/小程序
          if (this.dealerUse.isRelation == "1") {
            this.isRelation = true;
          } else {
            this.isRelation = false;
          }
          var reg = /,$/gi;
          this.dealerUse.permissionIds = this.dealerUse.permissionIds.replace(
            reg,
            ""
          );
          this.permissionIds = this.dealerUse.permissionIds.split(",");

          this.permissionNames = this.dealerUse.permissionNames.split(",");
          let permissionList = [];
          for (var i = 0; i < this.permissionIds.length; i++) {
            permissionList.push({
              permission_id: this.permissionIds[i]
            });
          }
          for (var j = 0; j < this.permissionNames.length; j++) {
            permissionList[j].permission_name = this.permissionNames[j];
          }
          this.permissionList = permissionList;
        }
      });
    },
    chooseZ() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 10
      };
      this.list = null;
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
    handleSelectionChange(val) {
      let names = [];
      let ids = [];
      for (var i = 0; i < val.length; i++) {
        ids.push(val[i].id);
        names.push(val[i].dealerName);
      }
      this.dealerUse.shareDealerNames = names.join(",");
      this.dealerUse.shareDealerIds = ids.join(",");
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
    selectedPermissionIds(e) {
      this.dealerUse.permissionIds = e.join(",") + ",";
      let names = [];
      for (var i = 0; i < this.permissionList.length; i++) {
        for (var j = 0; j < e.length; j++) {
          if (this.permissionList[i].permission_id == e[j]) {
            names.push(this.permissionList[i].permission_name);
          }
        }
      }
      this.dealerUse.permissionNames = names.join(",");
    },
    isOpenShare(e) {
      this.isShareCard = e;
      if (this.isShareCard === true) {
        this.isOpen = true;
        this.dealerUse.isShareCard = "1";
      } else {
        this.isOpen = false;
        this.dealerUse.isShareCard = "0";
      }
    },
    isShare(e) {
      if (e == "1") {
        this.shareUser = false;
      } else if (e == "2") {
        this.shareUser = true;
      }
    },
    isOPenDealerAudit(e) {
      this.isDealerAudit = e;
      if (this.isDealerAudit === true) {
        this.dealerUse.isDealerAudit = "1";
      } else {
        this.dealerUse.isDealerAudit = "0";
      }
    },
    isOpenRelation(e) {
      this.isRelation = e;
      if (this.isRelation === true) {
        this.dealerUse.isRelation = "1";
      } else {
        this.dealerUse.isRelation = "0";
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
              this.dealerUse.updateBy = get("userId");
              updateDealerUse(this.dealerId, this.dealerUse).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                  location.reload()
                  // this.$router.back();
                  // this.getDealerUse();
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            } else {
              this.dealerUse.dealerId = this.dealerId;
              this.dealerUse.createBy = get("userId");
              this.dealerUse.storeId = this.storeId;
              this.dealerUse.storeName = this.storeName;
              createDealerUse(this.dealerUse).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.dealerUse = Object.assign({}, defaultDealerInfo);
                  this.$message({
                    message: "提交成功",
                    type: "success",
                    duration: 1000
                  });
                  location.reload()
                  // this.$router.back();
                  // this.getDealerUse();
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
    }
  }
};
</script>
<style>
.share {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>


