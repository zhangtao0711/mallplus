<template>
  <div class="app-container">

    <!--工具栏-->
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button style="float:right" type="primary" @click="handleSearchList()" size="small">查询搜索</el-button>
        <el-button
          style="float:right;margin-right: 15px"
          @click="handleResetSearch()"
          size="small"
        >重置</el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="输入搜索：">
            <el-input v-model="listQuery.id" class="input-width" placeholder="编号"></el-input>
          </el-form-item>
          <el-form-item label="订单分类：">
            <el-select v-model="listQuery.orderType" class="input-width" placeholder="全部" clearable>
              <el-option
                v-for="item in orderTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="提交时间：">
            <el-date-picker
              class="input-width"
              v-model="listQuery.createTime"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="请选择时间"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="收货信息  ：">
            <el-input v-model="listQuery.receiverPhone" class="input-width" placeholder="收货人电话"></el-input>
          </el-form-item>

          <el-form-item label="订单来源：">
            <el-select
              v-model="listQuery.sourceType"
              class="input-width"
              placeholder="全部"
              clearable
            >
              <el-option
                v-for="item in sourceTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!--订单数据统计-->
    <div class="order-caculate">
      <a class="caculate-title">
        订单数 :
        <span class="caculate-num">{{caculateInfo.orderCount}}</span>
      </a>

      <a class="caculate-title">
        订单金额 :
        <span class="caculate-num">{{caculateInfo.orderPay}}</span>
      </a>
      <a class="caculate-title">
        客户数 :
        <span class="caculate-num">{{caculateInfo.memberCount}}</span>
      </a>
    </div>

    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
    </el-card>
    <div class="table-container">
      <el-table
        ref="orderTable"
        :data="list"
        style="width: 100%;"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="80" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="订单编号" width="180" align="center">
          <template slot-scope="scope">{{scope.row.orderSn}}</template>
        </el-table-column>
        <el-table-column label="商品" width="180" align="center">
          <template slot-scope="scope">{{scope.row.goodsName}}</template>
        </el-table-column>
        <el-table-column label="下单时间" width="180" align="center">
          <template slot-scope="scope">{{scope.row.createTime | formatCreateTime}}</template>
        </el-table-column>
        <el-table-column label="用户账号" align="center">
          <template slot-scope="scope">{{scope.row.memberUsername}}</template>
        </el-table-column>
        <el-table-column label="订单金额" width="120" align="center">
          <template slot-scope="scope">￥{{scope.row.totalAmount}}</template>
        </el-table-column>
        <el-table-column label="支付金额" width="120" align="center">
          <template slot-scope="scope">￥{{scope.row.payAmount}}</template>
        </el-table-column>
        <el-table-column label="支付方式" width="120" align="center">
          <template slot-scope="scope">{{scope.row.payType | formatPayType}}</template>
        </el-table-column>
        <el-table-column label="订单来源" width="120" align="center">
          <template slot-scope="scope">{{scope.row.sourceType | formatSourceType}}</template>
        </el-table-column>
        <el-table-column label="订单类型" width="120" align="center">
          <template slot-scope="scope">{{scope.row.orderType | formatOrderType}}</template>
        </el-table-column>

        <el-table-column label="订单状态" width="120" align="center">
          <template slot-scope="scope">{{scope.row.status | formatStatus}}</template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleViewOrder(scope.$index, scope.row)">查看订单</el-button>
            <el-button
              size="mini"
              @click="handleCloseOrder(scope.$index, scope.row)"
              v-show="scope.row.status===12"
            >关闭订单</el-button>
            <el-button
              size="mini"
              @click="handleDeliveryOrder(scope.$index, scope.row)"
              v-show="scope.row.status===2"
            >订单发货</el-button>
            <el-button
              size="mini"
              @click="handleViewLogistics(scope.$index, scope.row)"
              v-show="scope.row.status>0 && scope.row.status<9"
            >订单跟踪</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDeleteOrder(scope.$index, scope.row)"
              v-show="scope.row.status===15"
            >删除订单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container">
      <el-checkbox v-model="printChecked" @change="batchSelection" style="margin-right: 20px;"></el-checkbox>
      <el-select
        v-model="batchHandle"
        @change="handlePrintOption"
        clearable
        placeholder="批量操作"
        class="filter-item"
        style="width: 130px; margin-right: 8px;"
      >
        <el-option
          v-for="item in handleOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="batchExport"
        @change="handleExportOption"
        clearable
        placeholder="批量导出"
        class="filter-item"
        style="width: 130px"
      >
        <el-option
          v-for="item in exportOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div>
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes,prev, pager, next,jumper"
        :current-page.sync="listQuery.pageNum"
        :page-size="listQuery.pageSize"
        :page-sizes="[10,15,50]"
        :total="total"
      ></el-pagination>
    </div>


    <el-dialog title="关闭订单" :visible.sync="closeOrder.dialogVisible">
      <span style="vertical-align: top">操作备注：</span>
      <el-input
        style="width: 80%"
        type="textarea"
        :rows="5"
        placeholder="请输入内容"
        v-model="closeOrder.content"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeOrder.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleCloseOrderConfirm">确 定</el-button>
      </span>
    </el-dialog>
    <logistics-dialog v-model="logisticsDialogVisible"></logistics-dialog>
  </div>
</template>

<script>
import checkPermission from "@/utils/permission";
import { formatTime } from "@/utils/index";
import { fetchList, closeOrder, deleteOrderById, orderData } from "@/api/order";
import { formatDate } from "@/utils/date";
import LogisticsDialog from "@/views/oms/order/components/logisticsDialog";
const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  orderSn: null,
  receiverKeyword: null,
  status: 0,
  orderType: null,
  sourceType: null,
  createTime: null
};

export default {
  name: "orderList",
  components: { LogisticsDialog },
  data() {
    return {
      delLoading: false,
      status: "-9",
      orderType: "0",
      createTime: "",
      checkList: [],
      printChecked: false,
      batchHandle: "",
      batchExport: "",
      listContent: [],
      listQuery: Object.assign({}, defaultListQuery),
      listLoading: true,
      list: null,
      total: null,
      operateType: null,
      multipleSelection: [],
      closeOrder: {
        dialogVisible: false,
        content: null,
        orderIds: []
      },
      statusOptions: [
        {
          label: "待付款",
          value: 12
        },
        {
          label: "待发货",
          value: 2
        },
        {
          label: "已发货",
          value: 3
        },
        {
          label: "已完成",
          value: 5
        },
        {
          label: "申请退款",
          value: 13
        },
        {
          label: "已退款",
          value: 14
        },
        {
          label: "已关闭",
          value: 15
        }
      ],
      orderTypeOptions: [
        {
          label: "正常订单",
          value: 1
        },
        {
          label: "秒杀订单",
          value: 6
        },
        {
          value: 7,
          label: "门店自取订单"
        },
        {
          label: " 拼团订单",
          value: 2
        },
        {
          label: "团购订单",
          value: 3
        },
        {
          label: "砍价订单",
          value: 4
        },
        {
          label: "积分订单",
          value: 5
        }
      ],
      sourceTypeOptions: [
        {
          label: "小程序订单",
          value: 1
        },
        {
          label: "APP订单",
          value: 4
        },
        {
          label: "h5订单",
          value: 2
        },
        {
          label: "pc订单",
          value: 3
        }
      ],
      operateOptions: [
        {
          label: "批量发货",
          value: 1
        },
        {
          label: "关闭订单",
          value: 2
        },
        {
          label: "删除订单",
          value: 3
        }
      ],
      logisticsDialogVisible: false,

      handleOptions: [
        { value: "", label: "批量操作" },
        { value: "0", label: "批量打印" }
      ],
      exportOptions: [
        { value: "", label: "批量导出" },
        { value: "0", label: "导出全部" },
        { value: "1", label: "导出选中" }
      ],
      caculateInfo: {
        orderCount: 0,
        orderPay: 0,
        memberCount: 0
      }
    };
  },
  created() {
    this.$nextTick(() => {
      this.getList();
      this.orderDatas(0);
    });
  },
  filters: {
    formatCreateTime(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
    formatPayType(value) {
      if (value === 2) {
        return "支付宝";
      } else if (value === 1) {
        return "微信小程序";
      } else if (value === 3) {
        return "余额支付";
      } else if (value === 5) {
        return "积分兑换";
      }
    },
    formatSourceType(value) {
      if (value === 1) {
        return "小程序";
      } else if (value === 2) {
        return "h5订单";
      } else if (value === 3) {
        return "PC订单";
      } else if (value === 4) {
        return "android订单";
      } else if (value === 5) {
        return "ios订单";
      }
    },
    formatOrderType(value) {
      if (value === 2) {
        return "拼团订单";
      } else if (value === 3) {
        return "团购订单";
      } else if (value === 6) {
        return "秒杀订单";
      } else if (value === 1) {
        return "普通订单";
      } else if (value === 4) {
        return "砍价订单";
      } else if (value === 5) {
        return "积分订单";
      }
    },
    formatStatus(value) {
      if (value === 12) {
        return "待付款";
      }
      if (value === 1) {
        return "支付成功，没有回掉";
      } else if (value === 2) {
        return "待发货";
      } else if (value === 3) {
        return "待收货";
      } else if (value === 4) {
        return "待评价";
      } else if (value === 5) {
        return "已完成";
      } else if (value === 6) {
        return "维权中";
      } else if (value === 7) {
        return "维权已完成";
      } else if (value === 8) {
        return "待分享";
      } else if (value === 13) {
        return "申请退款";
      } else if (value === 14) {
        return "已退款";
      } else if (value === 15) {
        return "已关闭";
      } else if (value === 16) {
        return "无效订单";
      } else if (value === 17) {
        return "已删除";
      }
    }
  },
  methods: {
    formatTime,
    checkPermission,
    handleResetSearch() {
      this.listQuery = Object.assign({}, defaultListQuery);
    },
    handleSearchList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleViewOrder(index, row) {
      this.$router.push({ path: "/oms/orderDetail", query: { id: row.id } });
    },
    handleCloseOrder(index, row) {
      console.log("handleCloseOrder");
      this.closeOrder.dialogVisible = true;
      this.closeOrder.orderIds = [row.id];
    },
    handleDeliveryOrder(index, row) {
      let listItem = this.covertOrder(row);
      this.$router.push({
        path: "/oms/deliverOrderList",
        query: { list: [listItem] }
      });
    },
    handleViewLogistics(index, row) {
      this.logisticsDialogVisible = true;
    },
    handleDeleteOrder(index, row) {
      console.log("handleDeleteOrder=" + row.id);
      this.$confirm("是否要删除订单", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteOrderById(row.id).then(response => {
          this.$message({
            message: "删除成功",
            type: "success",
            duration: 1000
          });
          this.getList();
        });
      });
    },
    handleBatchOperate() {
      if (this.multipleSelection == null || this.multipleSelection.length < 1) {
        this.$message({
          message: "请选择要操作的订单",
          type: "warning",
          duration: 1000
        });
        return;
      }
      if (this.operateType === 1) {
        //批量发货
        let list = [];
        for (let i = 0; i < this.multipleSelection.length; i++) {
          if (this.multipleSelection[i].status === 1) {
            list.push(this.covertOrder(this.multipleSelection[i]));
          }
        }
        if (list.length === 0) {
          this.$message({
            message: "选中订单中没有可以发货的订单",
            type: "warning",
            duration: 1000
          });
          return;
        }
        this.$router.push({
          path: "/oms/deliverOrderList",
          query: { list: list }
        });
      } else if (this.operateType === 2) {
        //关闭订单
        this.closeOrder.orderIds = [];
        for (let i = 0; i < this.multipleSelection.length; i++) {
          this.closeOrder.orderIds.push(this.multipleSelection[i].id);
        }
        this.closeOrder.dialogVisible = true;
      } else if (this.operateType === 3) {
        //删除订单
        let ids = [];
        for (let i = 0; i < this.multipleSelection.length; i++) {
          ids.push(this.multipleSelection[i].id);
        }
        this.deleteOrder1(ids);
      }
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
    handleCloseOrderConfirm() {
      if (this.closeOrder.content == null || this.closeOrder.content === "") {
        this.$message({
          message: "操作备注不能为空",
          type: "warning",
          duration: 1000
        });
        return;
      }
      let params = new URLSearchParams();
      params.append("ids", this.closeOrder.orderIds);
      params.append("note", this.closeOrder.content);
      closeOrder(params).then(response => {
        this.closeOrder.orderIds = [];
        this.closeOrder.dialogVisible = false;
        this.getList();
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
      });
    },
    orderDatas() {
      orderData({ status: 0 }).then(response => {
        this.caculateInfo = response.data;
      });
    },

    deleteOrder1(ids) {
      this.$confirm("是否要进行该删除操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let params = new URLSearchParams();
        params.append("ids", ids);
        deleteOrder1(params).then(response => {
          this.$message({
            message: "删除成功！",
            type: "success",
            duration: 1000
          });
          this.getList();
        });
      });
    },
    covertOrder(order) {
      let address =
        order.receiverProvince +
        order.receiverCity +
        order.receiverRegion +
        order.receiverDetailAddress;
      let listItem = {
        orderId: order.id,
        orderSn: order.orderSn,
        receiverName: order.receiverName,
        receiverPhone: order.receiverPhone,
        receiverPostCode: order.receiverPostCode,
        address: address,
        deliveryCompany: null,
        deliverySn: null
      };
      return listItem;
    },
    beforeInit() {
      this.url = "api/yxStoreOrder";
      const sort = "id,desc";
      this.params = {
        page: this.page,
        size: this.size,
        sort: sort,
        orderStatus: this.status,
        orderType: this.orderType,
        addTime: this.createTime,
        listContent: this.listContent
      };
      const query = this.query;
      const type = query.type;

      return true;
    },

    getCaculateInfo() {},
    clearCaculateInfo() {
      this.caculateInfo = {
        orderCount: 0,
        orderPay: 0,
        memberCount: 0
      };
    },

    batchSelection(val) {
      let rows = this.data;
      if (val) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handlePrintOption(val) {
      switch (val) {
        case "0":
          this.getPrintList();
          this.batchHandle = "";
          break;
        default:
          break;
      }
    },
    handleExportOption(val) {
      let list = this.checkList;
      this.page = 0;
      this.size = 10000;
      switch (val) {
        case "0":
          this.listContent = "";
          this.beforeInit();
          this.downloadMethod();
          break;
        case "1":
          if (list.length == 0) {
            this.$message({
              message: "请选择订单",
              type: "warning"
            });
          } else {
            this.listContent = [];
            list.forEach(item => {
              this.listContent.push(item.orderId);
            });
            this.listContent = JSON.stringify(this.listContent);
            this.beforeInit();
            this.downloadMethod();
          }
          break;
        default:
          break;
      }
      this.batchExport = "";
    },
    downloadMethod() {
      this.beforeInit();
      this.downloadLoading = true;
      download(process.env.BASE_API + "/download", this.params)
        .then(result => {
          this.downloadFile(result, this.title + "数据", "xlsx");
          this.downloadLoading = false;
        })
        .catch(() => {
          this.downloadLoading = false;
        });
    },
    // 下载文件
    downloadFile(obj, name, suffix) {
      const url = window.URL.createObjectURL(new Blob([obj]));
      const link = document.createElement("a");
      link.style.display = "none";
      link.href = url;
      const fileName = parseTime(new Date()) + "-" + name + "." + suffix;
      link.setAttribute("download", fileName);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },
    getPrintList() {
      let list = this.checkList;
      if (list.length == 0) {
        this.$message({
          message: "请选择订单",
          type: "warning"
        });
      } else {
        const _this = this.$refs.form5;
        _this.dialog = true;
      }
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1;
    }
  }
};
</script>

<style scoped lang="scss">
.order-caculate {
  font-size: 14px;
  color: #909399;
  border-top: 1px solid #f0f0f0;
  padding: 20px 10px;
  .caculate-title {
    display: inline-block;
    margin-right: 50px;
  }
  .caculate-num {
    font-size: 20px;
    color: #ff4949;
  }
}

.el-table th {
  background-color: #fafafa;
}

.footer-contains {
  position: absolute;
  display: -ms-flexbox;
  display: flex;
  background-color: #f6f6f6;
  bottom: 0;
  flex-align: center;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  z-index: 999;
  padding: 0 20px 0 13px;
}

/*打印单样式编辑*/
.order-list {
  /*  height: 297mm;*/
  margin: 0 auto;
  width: 210mm;

  .order-title {
    height: 16mm;
    line-height: 16mm;
    font-size: 8mm;
    font-weight: bolder;
    text-align: center;
  }
  .order-info {
    span {
      display: inline-block;
      padding: 0 0 10px 0;
      font-size: 3mm;
    }
    span.info {
      width: 60mm;
    }
  }
  .el-table--small th,
  .el-table--small td {
    padding: 4px 0;
  }
}
</style>
