<template> 
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
        <div>
          <i class="el-icon-search"></i>
          <span>筛选搜索</span>
          <el-button
            style="float: right"
            @click="searchMemberList()"
            type="primary"
            size="small">
            查询结果
          </el-button>
          <el-button
                      style="float: right"
                      @click="updateOrderInfo()"
                      type="primary"
                      size="small">
                      同步订单统计
                    </el-button>
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
                            :value="item.value">
                          </el-option>
                        </el-select>
                      </el-form-item>
          </el-form>
        </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>

    </el-card>
    <div class="table-container">
      <el-table ref="brandTable"
                :data="list"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                v-loading="listLoading"
                border>

        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="60" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
         <el-table-column label="推荐人" width="140" align="center">
                  <template slot-scope="scope">{{scope.row.invitecode}}</template>
          </el-table-column>

        <el-table-column label="用户账号" width="140" align="center">
          <template slot-scope="scope">{{scope.row.username}}</template>
        </el-table-column>
        <el-table-column label="电话号码" width="140" align="center">
          <template slot-scope="scope">{{scope.row.phone}}</template>
        </el-table-column>
         <el-table-column label="会员等级" width="140" align="center">
                  <template slot-scope="scope">{{scope.row.memberLevelName}}</template>
                </el-table-column>
        <el-table-column label="用户余额" width="80" align="center">
          <template slot-scope="scope">
            <p> {{scope.row.blance}}</p>
            <p>
              <el-button
                type="text"
                @click="handleShowVeriyEditDialog(scope.$index, scope.row)">余额记录
              </el-button>
            </p>
          </template>
        </el-table-column>
        <el-table-column label="用户积分" width="80" align="center">
                  <template slot-scope="scope">
                    <p> {{scope.row.integration}}</p>
                    <p>
                      <el-button
                        type="text"
                        @click="handleShowIntegrationDialog(scope.$index, scope.row)">积分记录
                      </el-button>
                    </p>
                  </template>
                </el-table-column>
        <el-table-column label="是否显示" width="70" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleShowChange(scope.$index, scope.row)"
              :active-value="1"
              :inactive-value="0"
              v-model="scope.row.status">
            </el-switch>
          </template>
        </el-table-column>
          <el-table-column label="购买订单" width="80" align="center">
              <template slot-scope="scope">{{scope.row.buyCount}}</template>
          </el-table-column>
          <el-table-column label="消费金额" width="80" align="center">
              <template slot-scope="scope">{{scope.row.buyMoney}}</template>
          </el-table-column>
           <el-table-column label="注册时间" width="160" align="center">
                    <template slot-scope="scope">{{scope.row.createTime | formatTime}}</template>
                  </el-table-column>
        <el-table-column label="操作"  align="center">
          <template slot-scope="scope">
             <p>
              <el-button
                      size="mini"
                      type="danger"
                      @click="addBlacne(scope.$index, scope.row)">余额充值
              </el-button>
               <el-button
                            size="mini"
                            type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除
                          </el-button>
                          </p>
                          <p>
              <el-button
                      size="mini"
                      type="danger"
                      @click="addIntegration(scope.$index, scope.row)">积分充值
              </el-button>
               <el-button
                                    size="mini"
                                    type="danger"
                                    @click="userOrder(scope.$index, scope.row)">订单
                            </el-button>
</p>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container">

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
        :total="total">
      </el-pagination>
    </div>
     <el-dialog
          title="会员订单记录"
          :visible.sync="dialogVisible2"
          width="60%">
          <el-table style="width: 100%;margin-top: 20px"
                    :data="orderList"
                    border>

             <el-table-column label="编号" align="center">
                                <template slot-scope="scope">{{scope.row.id}}</template>
                            </el-table-column>
                           <el-table-column label="订单编号" width="80" align="center">
                                          <template slot-scope="scope">{{scope.row.id}}</template>
                                        </el-table-column>
                                        <el-table-column label="商品" width="180" align="center">
                                          <template slot-scope="scope">{{scope.row.goodsName}}</template>
                                        </el-table-column>
                                        <el-table-column label="下单时间" width="180" align="center">
                                          <template slot-scope="scope">{{scope.row.createTime | formatTime}}</template>
                                        </el-table-column>
                                       <el-table-column label="订单状态" width="80" align="center">
                                                <template slot-scope="scope">{{scope.row.status | formatOrderStatus}}</template>
                                         </el-table-column>
                                        <el-table-column label="支付金额" width="80" align="center">
                                          <template slot-scope="scope">￥{{scope.row.payAmount}}</template>
                                        </el-table-column>
                                        <el-table-column label="支付方式" width="120" align="center">
                                          <template slot-scope="scope">{{scope.row.payType | formatPayType}}</template>
                                        </el-table-column>
                                        <el-table-column label="订单来源" width="120" align="center">
                                          <template slot-scope="scope">{{scope.row.sourceType | formatSourceType}}</template>
                                        </el-table-column>
                                        <el-table-column label="订单类型"  align="center">
                                          <template slot-scope="scope">{{scope.row.orderType | formatOrderType}}</template>
                                        </el-table-column>



          </el-table>
        </el-dialog>

    <el-dialog
      title="积分记录"
      :visible.sync="dialogVisible1"
      width="40%">
      <el-table style="width: 100%;margin-top: 20px"
                :data="integrationList"
                border>

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
                         <el-table-column label="创建时间"  align="center">
                                  <template slot-scope="scope">{{scope.row.createTime | formatTime}}</template>
                                </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog
          title="余额记录"
          :visible.sync="dialogVisible"
          width="40%">
          <el-table style="width: 100%;margin-top: 20px"
                    :data="blanceList"
                    border>

            <el-table-column label="会员" align="center">
                                       <template slot-scope="scope">{{scope.row.memberId}}</template>
                                   </el-table-column>
                                   <el-table-column label="价格" align="center">
                                       <template slot-scope="scope">{{scope.row.price}}</template>
                                   </el-table-column>
                                   <el-table-column label="类别" align="center">
                                       <template slot-scope="scope">{{scope.row.type |formatStatus}}</template>
                                   </el-table-column>
                                   <el-table-column label="备注" align="center">
                                       <template slot-scope="scope">{{scope.row.note}}</template>
                                   </el-table-column>
                                   <el-table-column label="创建时间"  align="center">
                                                                     <template slot-scope="scope">{{scope.row.createTime | formatTime}}</template>
                                                                   </el-table-column>
          </el-table>
        </el-dialog>
      <el-dialog
              title="余额充值"
              :visible.sync="blance.dialogVisible"
              width="40%">
          <el-form :model="blance"  ref="brandFrom" label-width="150px">

              <el-form-item label="充值金额：" prop="detail">
                  <el-input v-model="blance.blance"></el-input>
              </el-form-item>

              <el-form-item>
                  <el-button @click="blance.dialogVisible = false">取 消</el-button>
                  <el-button type="primary" @click="handleEditBlance">确 定</el-button>
              </el-form-item>
          </el-form>

      </el-dialog>

      <el-dialog
              title="积分充值"
              :visible.sync="integration.dialogVisible"
              width="40%">
          <el-form :model="integration"  ref="brandFrom" label-width="150px">

              <el-form-item label="充值积分：" prop="detail">
                  <el-input v-model="integration.integration"></el-input>
              </el-form-item>

              <el-form-item>
                  <el-button @click="integration.dialogVisible = false">取 消</el-button>
                  <el-button type="primary" @click="handleEditIntegration">确 定</el-button>
              </el-form-item>
          </el-form>

      </el-dialog>
  </div>
</template>
<script>
  import {formatDate} from '@/utils/date';
  import {fetchList, updateShowStatus, updateFactoryStatus, deleteMember,updateMemberOrderInfo,handleEditIntegration,handleEditBlance} from '@/api/ums/member'
    import {fetchList as fetchBlanceList} from '@/api/ums/memberBlanceLog'
    import {fetchList as fetchIntegrationList} from '@/api/ums/memberIntegration'
     import {fetchList as fetchMberLevelList} from '@/api/memberLevel'

 import {fetchList as fetchOrderList} from '@/api/order'
  export default {
    name: 'memberList',
    data() {
      return {
        dialogVisible:false,
         dialogVisible1:false,
              dialogVisible2:false,
        blanceList:null,
        integrationList:null,
        orderList:null,
          blance:{
              dialogVisible:false,
              id:null,
          },
          integration:{
              dialogVisible:false,
              id:null,
          },
        operates: [
          {
            label: "显示会员",
            value: "showMember"
          },
          {
            label: "隐藏会员",
            value: "hideMember"
          }
        ],
        operateType: null,
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
      }
    },
    created() {
      this.getList();
       this.geteMberLevelList();
    },
    filters:{
     formatPayType(value) {
                     if (value === 2) {
                       return '支付宝';
                     } else if (value === 1) {
                       return '微信小程序';
                     } else  if (value === 3){
                       return '余额支付';
                     } else  if (value === 5){
                                return '积分兑换';
                              }
                   },
                   formatSourceType(value) {
                     if (value === 1) {
                       return '小程序';
                     } else if (value === 2){
                       return 'h5订单';
                     }else if (value === 3){
                       return 'PC订单';
                     }else if (value === 4){
                       return 'android订单';
                     }else if (value === 5){
                       return 'ios订单';
                     }
                   },
                   formatOrderType(value) {
                     if (value === 2) {
                       return '拼团订单';
                     } else if (value === 3) {
                       return '团购订单';
                     } else  if (value === 6){
                       return '秒杀订单';
                     }else if (value === 1) {
                       return '普通订单';
                     }else if (value === 4) {
                                return '砍价订单';
                              } else if (value === 5) {
                                     return '积分订单';
                                 }
                   },
              formatOrderStatus(value) {
                if (value === 12) {
                  return '待付款';
                }if (value === 1) {
                  return '支付成功，没有回掉';
                }else if (value === 2) {
                  return '待发货';
                } else if (value === 3) {
                  return '待收货';
                } else if (value === 4) {
                  return '待评价';
                } else if (value === 5) {
                  return '已完成';
                } else if (value === 6) {
                  return '维权中';
                } else if (value === 7) {
                  return '维权已完成';
                } else if (value === 8) {
                  return '待分享';
                } else if (value === 13) {
                  return '申请退款';
                } else if (value === 14) {
                  return '已退款';
                }  else if (value === 15) {
                  return '已关闭';
                } else if (value === 16) {
                  return '无效订单';
                } else if (value === 17) {
                            return '已删除';
                          }
              },
      formatTime(time) {
        if(time==null||time===''){
          return 'N/A';
        }
        let date = new Date(time);
        return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
      },
      verifyStatusFilter(value) {
        if (value === 1) {
          return '消费';
        } else {
          return '收入';
        }
      },
      formatchangeType(value) {
              if (value === 1) {
                return '增加';
              } else {
                return '减少';
              }
            },
            formatsourceType(value) {
                    if (value === 1) {
                      return '下单';
                    } if (value === 2) {
                                            return '登录';
                                          }else {
                      return '注册';
                    }
                  },
      formatStatus(value){
        if (value === 1) {
                    return '消费';
                  } if (value === 2) {
                                return '退款';
                              }
                              if (value === 3) {
                                          return '充值';
                                        }
                                        if (value === 4) {
                                                    return '提现';
                                                  }
                                                  if (value === 5) {
                                                              return '佣金';
                                                            }
                                                            if (value === 6) {
                                                                        return '平台调整';
                                                                      }else {
                    return '收入';
                  }
      },
      formatReturnAmount(row){
        return row.productRealPrice*row.productCount;
      }
    },
    methods: {
     geteMberLevelList() {
            fetchMberLevelList({pageNum: 1, pageSize: 100}).then(response => {
              this.brandOptions = [];
              let brandList = response.data.records;
              for (let i = 0; i < brandList.length; i++) {
                this.brandOptions.push({label: brandList[i].name, value: brandList[i].id});
              }
            });
          },
        addBlacne(index,row){
            this.blance.dialogVisible=true;
            this.blance.id=row.id;

        },
        addIntegration(index,row){
            this.integration.dialogVisible=true;
            this.integration.id=row.id;

        },
        handleEditBlance(){
            if(this.blance.blance==null){
                this.$message({
                    message: '请输入余额',
                    type: 'warning',
                    duration: 1000
                });
                return
            }
            this.$confirm('是否要进行余额充值', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(()=>{
                let params = new URLSearchParams();
                params.append('id', this.blance.id);
                params.append('blance', this.blance.blance);


                handleEditBlance(params).then(response => {
                    this.$message({
                        message: '余额充值成功',
                        type: 'success',
                        duration: 1000
                    });
                    this.getList();
                });
                this.blance.dialogVisible=false;

            });
        },
        handleEditIntegration(){
            if(this.integration.integration==null){
                this.$message({
                    message: '请输入积分',
                    type: 'warning',
                    duration: 1000
                });
                return
            }
            this.$confirm('是否要进行积分充值', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(()=>{
                let params = new URLSearchParams();
                params.append('id', this.integration.id);
                params.append('integration', this.integration.integration);


                handleEditIntegration(params).then(response => {
                    this.$message({
                        message: '积分充值成功',
                        type: 'success',
                        duration: 1000
                    });
                    this.getList();
                });
                this.integration.dialogVisible=false;

            });
        },
      handleShowVeriyEditDialog(index,row){
        this.dialogVisible=true;
        fetchBlanceList({memberId:row.id,pageSize:1000}).then(response=>{
          this.blanceList=response.data.records;
       });
      },
       handleShowIntegrationDialog(index,row){
              this.dialogVisible1=true;
              fetchIntegrationList({memberId:row.id,pageSize:1000}).then(response=>{
                this.integrationList=response.data.records;
             });
            },
            userOrder(index,row){
                       this.dialogVisible2=true;
                       fetchOrderList({memberId:row.id,pageSize:1000}).then(response=>{
                         this.orderList=response.data.records;
                      });
                     },

      handleShowChange(index, row) {
        let params = new URLSearchParams();
        params.append('ids', row.id);
        params.append('showStatus', row.showStatus);
        updateShowStatus(params).then(response => {
          this.$message({
          message: '修改成功',
          type: 'success',
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
      handleUpdate(index, row) {
        this.$router.push({path: '/ums/updateMember', query: {id: row.id}})
      },
      handleDelete(index, row) {
        this.$confirm('是否要删除该会员', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteMember(row.id).then(response => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1000
            });
            this.getList();
          });
        });
      },
      getProductList(index, row) {
        console.log(index, row);
      },
      getProductCommentList(index, row) {
        console.log(index, row);
      },
      handleFactoryStatusChange(index, row) {
        var data = new URLSearchParams();
        data.append("ids", row.id);
        data.append("factoryStatus", row.factoryStatus);
        updateFactoryStatus(data).then(response => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000
          });
        }).catch(error => {
          if (row.factoryStatus === 0) {
            row.factoryStatus = 1;
          } else {
            row.factoryStatus = 0;
          }
        });
      },
      handleShowStatusChange(index, row) {
        let data = new URLSearchParams();
        ;
        data.append("ids", row.id);
        data.append("showStatus", row.showStatus);
        updateShowStatus(data).then(response => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000
          });
        }).catch(error => {
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
      updateOrderInfo(){
       updateMemberOrderInfo().then(response => {
                this.getList();
                this.$message({
                  message: '同步成功',
                  type: 'success',
                  duration: 1000
                });
              });
      },
      handleBatchOperate() {
        console.log(this.multipleSelection);
        if (this.multipleSelection < 1) {
          this.$message({
            message: '请选择一条记录',
            type: 'warning',
            duration: 1000
          });
          return;
        }
        let showStatus = 0;
        if (this.operateType === 'showMember') {
          showStatus = 1;
        } else if (this.operateType === 'hideMember') {
          showStatus = 0;
        } else {
          this.$message({
            message: '请选择批量操作类型',
            type: 'warning',
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
            message: '修改成功',
            type: 'success',
            duration: 1000
          });
        });
      },
      addMember() {
        this.$router.push({path: '/pms/addMember'})
      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>


</style>


