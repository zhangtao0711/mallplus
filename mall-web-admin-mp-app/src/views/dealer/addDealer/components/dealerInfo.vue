<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="value" :rules="rules" ref="dealerInfoFrom" label-width="150px">
      <el-form-item label="商户名称" prop="dealerName">
        <el-input v-model="value.dealerName" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="商户logo" prop="icon">
        <single-upload-img v-model="value.icon"></single-upload-img>
      </el-form-item>

      <el-form-item label="商户类型" prop="type">
        <el-select v-model="value.type" style="width: 370px;">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="联系人" prop="realname">
        <el-input v-model="value.realname" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="联系电话" prop="dealerPhone">
        <el-input v-model="value.dealerPhone" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="所属区域">
        <el-select
          style="width: 370px;"
          v-model="value.province"
          @change="getSecondData"
          clearable
          placeholder="请选择所在省"
        >
          <el-option
            v-for="item in parentArea"
            :key="item.id"
            :label="item.extName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select
          style="width: 370px;"
          v-model="value.city"
          @change="getThirdData"
          clearable
          placeholder="请选择所在市"
        >
          <el-option
            v-for="item in secondArea"
            :key="item.id"
            :label="item.extName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select style="width: 370px;" v-model="value.county" clearable placeholder="请选择所在区/县">
          <el-option
            v-for="item in thirdArea"
            :key="item.id"
            :label="item.extName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="详细地址" prop="address">
        <el-input v-model="value.address" style="width: 370px;" />
      </el-form-item>

      <div v-show="isEdit&&isUniacid">
        <el-form-item label="公众号/小程序名称">
          <el-input v-model="name" :disabled="true" style="width: 370px;" />
        </el-form-item>

        <el-form-item label="总经销商账号" v-show="isgName">
          <el-input v-model="gName" :disabled="true" style="width: 370px;" />
        </el-form-item>

        <el-form-item label="经销商账号" v-show="ispName">
          <el-input v-model="pName" :disabled="true" style="width: 370px;" />
        </el-form-item>

        <el-form-item label="分销商账号" v-show="issName">
          <el-input v-model="sName" :disabled="true" style="width: 370px;" />
        </el-form-item>
      </div>

      <el-form-item label="经销商等级" prop="levelId">
        <el-radio-group v-model="value.levelId" @change="level">
          <el-radio label="1">总经销商</el-radio>
          <el-radio label="2">经销商</el-radio>
          <el-radio label="3">分销商</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="经销商折扣" prop="thirdSeparate">
        <el-input v-model="value.thirdSeparate" style="width: 370px;" />
      </el-form-item>

      <div v-show="ownerShow">
        <h3>业务归属</h3>
        <el-form-item
          prop="pid"
          v-show="jxsShow"
          label="总经销商账号"
          :rules="[
              { required: jxsShow, message: '请选择总经销商账号' },
            ]"
        >
          <el-input
            :disabled="true"
            placeholder="请选择总经销商账号"
            v-model="value.pid"
            class="input-with-select"
          >
            <el-button slot="append" @click="chooseZ" type="primary" icon="el-icon-search">选择</el-button>
          </el-input>
        </el-form-item>

        <div v-show="fxsShow">
          <el-form-item
            prop="pid"
            label="经销商账号"
            :rules="[
              { required: fxsShow, message: '请选择经销商账号', trigger: 'change' },
            ]"
          >
            <el-input
              :disabled="true"
              placeholder="请选择经销商账号"
              v-model="value.pid"
              class="input-with-select"
            >
              <el-button slot="append" @click="chooseZ1" type="primary" icon="el-icon-search">选择</el-button>
            </el-input>
          </el-form-item>

          <el-form-item label="总经销商账号">
            <el-input :disabled="true" v-model="value.gid" style="width: 370px;" />
          </el-form-item>
        </div>
      </div>

      <div>
        <h3>收款设置</h3>
        <el-form-item label="收款商户号" prop="selfType">
          <el-radio-group v-model="value.selfType" @change="selftypeChange">
            <el-radio label="1">商户自有</el-radio>
            <el-radio label="2">代收商户号</el-radio>
          </el-radio-group>
        </el-form-item>

        <div v-show="appShow">
          <el-form-item
            label="AppId"
            :rules="[
              { required: appShow, message: '请输入AppId' },
            ]"
          >
            <el-input v-model="value.appid" style="width: 370px;" />
          </el-form-item>

          <el-form-item
            label="Appsckey"
            :rules="[
              { required: appShow, message: '请输入APPsckey', },
            ]"
          >
            <el-input v-model="value.appsecret" style="width: 370px;" />
          </el-form-item>

          <el-form-item
            label="商户号"
            :rules="[
              { required: appShow, message: '请输入商户号' },
            ]"
          >
            <el-input v-model="value.mchid" style="width: 370px;" />
          </el-form-item>
        </div>
        <div v-show="accountShow">
          <el-form-item
            label="上级账号"
            prop="parentUserId"
            :rules="[
              { required: accountShow, message: '请选择上级账号' },
            ]"
          >
            <el-input
              :disabled="true"
              placeholder="请选择上级账号"
              v-model="value.parentUserId"
              class="input-with-select"
            >
              <el-button slot="append" @click="chooseZ2" type="primary" icon="el-icon-search">选择</el-button>
            </el-input>

            <el-button type="primary" @click="monitorFirmPay">检测</el-button>
          </el-form-item>

          <el-form-item
            label="收款账号"
            :rules="[
              { required: accountShow, message: '请输入收款账号' },
            ]"
          >
            <el-input v-model="value.receiptAccount" style="width: 370px;" />
            <el-button type="primary" @click="monitorWeChantPay">检测</el-button>
          </el-form-item>
        </div>
      </div>

      <div v-show="zproportionShow || proportionShow">
        <h3>分成</h3>

        <el-form-item
          prop="firstSeparate"
          label="总经销商分成比例"
          v-show="zproportionShow"
          :rules="[
              { required: zproportionShow, message: '请输入总经销商分成比例' },
            ]"
        >
          <el-input v-model="value.pid" :disabled="true" />
          <el-input v-model="value.firstSeparate">
            <template slot="append">%</template>
          </el-input>
        </el-form-item>

        <!-- <el-button type="primary">申请商户号</el-button> -->
        <el-form-item
          prop="firstSeparate"
          label="总经销商分成比例"
          v-show="proportionShow"
          :rules="[
              { required: proportionShow, message: '请输入总经销商分成比例' },
            ]"
        >
          <el-input v-model="value.gid" :disabled="true" />
          <el-input v-model="value.firstSeparate">
            <template slot="append">%</template>
          </el-input>
        </el-form-item>

        <el-form-item
          prop="secondSeparate"
          label="经销商分成比例"
          v-show="proportionShow"
          :rules="[
              { required: proportionShow, message: '请输入经销商分成比例' },
            ]"
        >
          <el-input v-model="value.pid" :disabled="true" />
          <el-input v-model="value.secondSeparate">
            <template slot="append">%</template>
          </el-input>
        </el-form-item>

        <el-form-item v-show="detection">
          <el-button type="primary" @click="monitorProfitShare">检测</el-button>
        </el-form-item>
      </div>

      <div>
        <h3>入驻状态</h3>
        <el-form-item label="状态" prop="applyStatus">
          <el-radio-group v-model="value.applyStatus">
            <el-radio label="1">待入驻</el-radio>
            <el-radio label="2">允许入驻</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('dealerInfoFrom')">提交</el-button>
        <el-button @click="goback">返回列表</el-button>
      </el-form-item>
    </el-form>

    <div>
      <el-dialog title="数据选择器" :visible.sync="blance.dialogVisible" width="40%">
        <div>
          <el-row :gutter="20" style="margin-bottom:5px">
            <!-- <el-col :span="12">
              <el-select size="small" v-model="listQuery.level" placeholder="请选择经销商等级">
                <el-option label="总经销商" value="1"></el-option>
                <el-option label="经销商" value="2"></el-option>
                <el-option label="分销商" value="3"></el-option>
              </el-select>
            </el-col>-->

            <el-col :span="12">
              <el-input
                size="small"
                placeholder="按手机号/商户名称/商户号搜索"
                v-model="listQuery.value"
                suffix-icon="el-icon-search"
              ></el-input>
            </el-col>

            <el-col :span="12">
              <el-button @click="searchBrandList()" type="primary" size="small">查询结果</el-button>
            </el-col>
          </el-row>
          <!-- <el-button
            style="float: right;margin-bottom:15px;"
            @click="searchBrandList()"
            type="primary"
            size="small"
          >查询结果</el-button>-->
        </div>

        <el-table
          ref="merchatBusinessMaterialsTable"
          :data="list"
          style="width: 100%;"
          v-loading="listLoading"
          border
        >
          <el-table-column prop="id" label="总经销商账号" width="80" align="center">
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

      <el-dialog title="数据选择器" :visible.sync="blance1.dialogVisible" width="40%">
        <div>
          <el-row :gutter="20" style="margin-bottom:5px">
            <!-- <el-col :span="12">
              <el-select size="small" v-model="listQuery.level" placeholder="请选择经销商等级">
                <el-option label="总经销商" value="1"></el-option>
                <el-option label="经销商" value="2"></el-option>
                <el-option label="分销商" value="3"></el-option>
              </el-select>
            </el-col>-->

            <el-col :span="12">
              <el-input
                size="small"
                placeholder="按手机号/商户名称/商户号搜索"
                v-model="listQuery.value"
                suffix-icon="el-icon-search"
              ></el-input>
            </el-col>
            <el-col :span="12">
              <el-button
                style="float: right;margin-bottom:15px;"
                @click="searchBrandList()"
                type="primary"
                size="small"
              >查询结果</el-button>
            </el-col>
          </el-row>
        </div>
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
                @click="handleChoose1(scope.$index, scope.row)"
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

      <el-dialog title="数据选择器" :visible.sync="blance2.dialogVisible" width="40%">
        <div>
          <el-row :gutter="20" style="margin-bottom:5px">
            <el-col :span="12">
              <el-input
                size="small"
                placeholder="按手机号/商户名称/商户号搜索"
                v-model="listQuery1.value"
                suffix-icon="el-icon-search"
              ></el-input>
            </el-col>

            <el-col :span="12">
              <el-button @click="searchBrandList1()" type="primary" size="small">查询结果</el-button>
            </el-col>
          </el-row>
        </div>
        <el-table
          ref="merchatBusinessMaterialsTable"
          :data="list1"
          style="width: 100%;"
          v-loading="listLoading1"
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
                @click="handleChoose2(scope.$index, scope.row)"
              >选择</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="batch-operate-container"></div>
        <div style="text-align:right">
          <el-pagination
            background
            @size-change="handleSizeChange1"
            @current-change="handleCurrentChange1"
            layout="total, sizes,prev, pager, next,jumper"
            :page-size="listQuery1.pageSize"
            :page-sizes="[5, 10, 15]"
            :current-page.sync="listQuery1.pageNum"
            :total="total1"
          ></el-pagination>
        </div>
      </el-dialog>
    </div>
  </el-card>
</template>
<script>
import {
  getAreaList,
  createDealer,
  getUserInfo,
  monitorWeChantPay,
  monitorFirmPay,
  monitorProfitShare,
  lastDealer,
  updateDealer,
  getOriginByUniacid
} from "@/api/dealer/dealer";
import { get } from "@/utils/auth";
import SingleUploadImg from "@/components/Upload/singleUploadPublic";
import { fetchList, getAdmin, listDealer } from "@/api/admin";

export default {
  name: "dealerInfo",
  props: {
    value: Object,
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  components: {
    SingleUploadImg
  },
  data() {
    return {
      name: null,
      gName: null,
      pName: null,
      sName: null,
      isgName: false,
      ispName: false,
      issName: false,
      isUniacid: false,
      detection: false,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        storeId: get("storeId")
      },
      list: null,
      total: null,
      listLoading: true,

      listQuery1: {
        pageNum: 1,
        pageSize: 10,
        storeId: get("storeId")
      },
      list1: null,
      total1: null,
      listLoading1: true,
      blance: {
        dialogVisible: false,
        id: null
      },
      blance1: {
        dialogVisible: false,
        id: null
      },
      blance2: {
        dialogVisible: false,
        id: null
      },
      proportionShow: false,
      zproportionShow: false,
      accountShow: false,
      appShow: false,
      jxsShow: false,
      fxsShow: false,
      ownerShow: false,
      options: [
        {
          value: "1",
          label: "个人"
        },
        {
          value: "2",
          label: "个体工商户"
        },
        {
          value: "3",
          label: "企业"
        }
      ],
      parentArea: [],
      secondArea: [],
      thirdArea: [],
      rules: {
        dealerName: [
          { required: true, message: "请输入商户名称", trigger: "blur" }
        ],
        type: [{ required: true, message: "请选择商户类型", trigger: "blur" }],
        realname: [
          { required: true, message: "请输入联系人姓名", trigger: "blur" }
        ],
        dealerPhone: [
          { required: true, message: "请输入联系人电话", trigger: "blur" }
        ],
        levelId: [{ required: true, message: "请选择经销商等级" }],
        selfType: [{ required: true, message: "请选择收款方式" }],
        applyStatus: [{ required: true, message: "请选择入驻状态" }]
      }
    };
  },
  created() {
    this.getAreaList();
    if (this.isEdit) {
      this.getUserInfo();
    }
    
  },
  methods: {
    getUserInfo() {
      getUserInfo(this.$route.query.id).then(response => {
        this.value = JSON.parse(
          (
            JSON.stringify(response.data.appletSet) +
            JSON.stringify(response.data.user)
          ).replace(/}{/, ",")
        );
        this.value.type = String(this.value.type);
        this.value.city = Number(this.value.city);
        this.value.county = Number(this.value.county);
        this.value.levelId = String(this.value.level);
        this.value.applyStatus = String(this.value.status);
        this.value.selfType = String(this.value.selfType);

        if (this.value.selfType == "1") {
          this.detection = true;
          this.appShow = true;
          this.accountShow = false;
        } else if (this.value.selfType == "2") {
          this.detection = false;
          this.appShow = false;
          this.accountShow = true;
        }
        this.level(this.value.level);
        if (this.value.province) {
          this.getSecondData(this.value.province);
        }
        if (this.value.city) {
          this.getThirdData(this.value.city);
        }
        if (response.data.user.uniacid) {
          this.isUniacid = true;
          getOriginByUniacid(response.data.user.uniacid).then(response => {
            this.name = response.data.name

            // 总经销商
            if (response.data.gName) {
              this.gName = response.data.gName
              this.isgName = true;
            }
            // 经销商
            if (response.data.pName) {
              this.pName = response.data.pName
              this.ispName = true;
            }
            // 分销商
            if (response.data.sName) {
              this.sName = response.data.sName
              this.issName = true;
            }
          });
        }
      });
    },
    // 监测上级账号是否开通企业付款功能
    monitorFirmPay() {
      monitorFirmPay(Number(this.value.parentUserId)).then(response => {});
    },
    // 监测上级账号的企业付款到零钱功能
    monitorWeChantPay() {
      let param = {
        parentUserId: this.value.parentUserId,
        receiptAccount: this.value.receiptAccount
      };
      monitorWeChantPay(param).then(response => {});
    },
    monitorProfitShare() {
      let param = {
        appid: this.value.appid,
        appsecret: this.value.appsecret,
        mchid: this.value.mchid,
        notifyurl: process.env.BASE_API
      };
      // if (this.proportionShow) {
      //   param.receivers = [
      //     {
      //       type: "MERCHANT_ID",
      //       account: this.mchid,
      //       amount: 100,
      //       description: "分到商户"
      //     },
      //     {
      //       type: "MERCHANT_ID",
      //       account: this.mchid1,
      //       amount: 100,
      //       description: "分到商户"
      //     }
      //   ];
      // }

      // if (this.zproportionShow) {
      //   param.receivers = [
      //     {
      //       type: "MERCHANT_ID",
      //       account: this.mchid,
      //       amount: 100,
      //       description: "分到商户"
      //     }
      //   ];
      // }

      monitorProfitShare(param).then(response => {});
    },
    searchBrandList() {
      this.list = null;
      this.listQuery.pageNum = 1;
      this.getList();
    },
    getList() {
      this.listLoading = true;
      listDealer(this.listQuery).then(response => {
        this.listLoading = false;
        this.list = response.data;
        this.total = response.data.total;
        this.totalPage = response.data.pages;
        this.pageSize = response.data.size;
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
    chooseZ() {
      this.listQuery = {
        level: 1,
        pageNum: 1,
        pageSize: 10,
        storeId: get("storeId")
      };
      this.list = null;
      this.listLoading = false;
      this.blance.dialogVisible = true;
    },
    handleChoose(index, row) {
      this.value.pid = row.id;
      this.value.gid = row.pid;
      this.blance.dialogVisible = false;
    },
    chooseZ1() {
      this.listQuery = {
        level: 2,
        pageNum: 1,
        pageSize: 10,
        storeId: get("storeId")
      };
      this.list = null;
      this.listLoading = false;
      this.blance1.dialogVisible = true;
    },

    handleChoose1(index, row) {
      this.value.pid = row.id;
      this.value.gid = row.pid;
      // getAdmin(row.pid).then(response => {
      // console.log(response.data)
      // });
      this.blance1.dialogVisible = false;
    },
    chooseZ2() {
      this.listQuery1 = {
        pageNum: 1,
        pageSize: 10,
        storeId: get("storeId")
      };
      this.list1 = null;
      this.listLoading1 = false;
      this.blance2.dialogVisible = true;
    },
    handleChoose2(index, row) {
      this.value.parentUserId = row.id;
      this.blance2.dialogVisible = false;
    },
    searchBrandList1() {
      this.list1 = null;
      this.listQuery1.pageNum = 1;
      this.listQuery1.level = this.value.levelId;
      this.getList1();
    },
    handleSizeChange1(val) {
      this.listQuery1.pageNum = 1;
      this.listQuery1.pageSize = val;
      this.getList1();
    },
    handleCurrentChange1(val) {
      this.listQuery1.pageNum = val;
      this.getLis1t();
    },
    getList1() {
      this.listLoading1 = true;
      lastDealer(this.listQuery1).then(response => {
        this.listLoading1 = false;
        this.list1 = response.data;
        this.total1 = response.data.total;
        this.totalPage1 = response.data.pages;
        this.pageSize1 = response.data.size;
      });
    },

    getAreaList() {
      let param = {
        deep: 0
      };
      getAreaList(param).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          getAreaList({ deep: 0, pageNum: i + 1 }).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.parentArea.push(res.data.records[j]);
            }
          });
        }
      });
    },
    getSecondData(e) {
      this.secondArea = [];
      let param = {
        pid: e
      };
      getAreaList(param).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          getAreaList({ pid: e, pageNum: i + 1 }).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.secondArea.push(res.data.records[j]);
            }
          });
        }
      });
    },
    getThirdData(e) {
      this.thirdArea = [];
      let param = {
        pid: e
      };
      getAreaList(param).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          getAreaList({ pid: e, pageNum: i + 1 }).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.thirdArea.push(res.data.records[j]);
            }
          });
        }
      });
    },
    goback() {
      this.$router.back();
    },
    level(e) {
      // this.value.pid = "";
      // this.value.gid = "";

      // this.value.firstSeparate = "";
      // this.value.secondSeparate = "";
      if (e == "1") {
        this.ownerShow = false;
        this.jxsShow = false;
        this.fxsShow = false;
        this.proportionShow = false;
        this.zproportionShow = false;
        this.value.pid = 0;
        this.value.gid = 0;
      } else if (e == "2") {
        this.ownerShow = true;
        this.jxsShow = true;
        this.fxsShow = false;
        this.proportionShow = false;
        this.zproportionShow = true;
      } else if (e == "3") {
        this.ownerShow = true;
        this.jxsShow = false;
        this.fxsShow = true;
        this.proportionShow = true;
        this.zproportionShow = false;
      }
    },
    selftypeChange(e) {
      if (e == "1") {
        this.detection = true;
        this.appShow = true;
        this.accountShow = false;
        if (this.value.parentUserId) {
          this.value.parentUserId = null;
        }
        if (this.value.receiptAccount) {
          this.value.receiptAccount = null;
        }
      } else if (e == "2") {
        this.detection = false;
        this.appShow = false;
        this.accountShow = true;
        if (this.value.appid) {
          this.value.appid = null;
        }
        if (this.value.appsecret) {
          this.value.appsecret = null;
        }
        if (this.value.mchid) {
          this.value.mchid = null;
        }
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
              let formData = {
                appletSet: {
                  appid: this.value.appid,
                  appsecret: this.value.appsecret,
                  firstSeparate: this.value.firstSeparate,
                  levelId: this.value.levelId,
                  mchid: this.value.mchid,
                  parentUserId: this.value.parentUserId,
                  receiptAccount: this.value.receiptAccount,
                  secondSeparate: this.value.secondSeparate,
                  selfType: this.value.selfType,
                  thirdSeparate: this.value.thirdSeparate
                },
                user: {
                  address: this.value.address,
                  applyStatus: this.value.applyStatus,
                  city: this.value.city,
                  county: this.value.county,
                  dealerName: this.value.dealerName,
                  dealerPhone: this.value.dealerPhone,
                  gid: this.value.gid,
                  icon: this.value.icon,
                  pid: this.value.pid,
                  province: this.value.province,
                  realname: this.value.realname,
                  type: this.value.type,
                  username: this.value.dealerPhone,
                  nickName: this.value.dealerName,
                  level: this.value.levelId,
                  id: this.$route.query.id
                }
              };

              updateDealer(this.$route.query.id, formData).then(response => {
                if (response.code == 200) {
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                  this.getUserInfo();
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            } else {
              this.$emit("submitDealerInfo", this.isEdit);
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
</style>


