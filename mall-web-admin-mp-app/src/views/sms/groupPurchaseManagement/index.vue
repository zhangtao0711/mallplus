<template>
  <div class="app-container">
    <div>
      <el-tabs v-model="status" type="card" @tab-click="handleOrder">
        <el-tab-pane name="0">
          <span slot="label">
            <i class="el-icon-s-order"></i> 全部拼团
          </span>
        </el-tab-pane>
        <el-tab-pane name="12">
          <span slot="label">
            <i class="el-icon-bank-card"></i> 拼团成功
          </span>
        </el-tab-pane>
        <el-tab-pane name="2">
          <span slot="label">
            <i class="el-icon-refrigerator"></i> 拼团中
          </span>
        </el-tab-pane>
        <el-tab-pane name="3">
          <span slot="label">
            <i class="el-icon-truck"></i> 拼团失败
          </span>
        </el-tab-pane>
      </el-tabs>
      <!--工具栏-->
      <el-card class="filter-container" shadow="never">
        <div>
          <i class="el-icon-search"></i>
          <span>筛选搜索</span>
          <el-button
            style="float:right"
            type="primary"
            @click="handleSearchList()"
            size="small"
          >查询搜索</el-button>
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
              <el-select
                v-model="listQuery.orderType"
                class="input-width"
                placeholder="全部"
                clearable
              >
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
          v-loading="listLoading"
          border
        >
          <el-table-column label="编号" width="100" align="center">
            <template slot-scope="scope">{{scope.row.id}}</template>
          </el-table-column>

          <el-table-column label="商品" align="center">
            <template slot-scope="scope">{{scope.row.goodsName}}</template>
          </el-table-column>
          
          <el-table-column label="团购进度" align="center">
            <template slot-scope="scope">{{scope.row.memberUsername}}</template>
          </el-table-column>

          <el-table-column label="开团时间" align="center">
            <template slot-scope="scope">{{scope.row.createTime | formatCreateTime}}</template>
          </el-table-column>
          
          <el-table-column label="到期时间" align="center">
            <template slot-scope="scope">{{scope.row.createTime | formatCreateTime}}</template>
          </el-table-column>

          <el-table-column label="订单状态" width="120" align="center">
            <template slot-scope="scope">{{scope.row.status | formatStatus}}</template>
          </el-table-column>
          
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleViewOrder(scope.$index, scope.row)">查看订单</el-button>
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
          :current-page.sync="listQuery.pageNum"
          :page-size="listQuery.pageSize"
          :page-sizes="[10,15,50]"
          :total="total"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import checkPermission from "@/utils/permission";
import { formatTime } from "@/utils/index";
import { fetchList, deleteOrderById, orderData } from "@/api/order";
import { formatDate } from "@/utils/date";
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
  data() {
    return {
      delLoading: false,
      status: "0",
      orderType: "0",
      createTime: "",
      checkList: [],
      listContent: [],
      listQuery: Object.assign({}, defaultListQuery),
      listLoading: true,
      list: null,
      total: null,
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
    handleOrder(tab, event) {
      this.listQuery.status = tab.name;
      this.getList();
      this.orderDatas(tab.name);
    },
    handleResetSearch() {
      this.listQuery = Object.assign({}, defaultListQuery);
    },
    handleSearchList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    handleViewOrder(index, row) {
      this.$router.push({ path: "/sms/orderDetail", query: { id: row.id } });
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
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then(response => {
        this.listLoading = false;
        this.list = response.data.records;
        this.total = response.data.total;
      });
    },
    orderDatas(status) {
      orderData({ status: status }).then(response => {
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
