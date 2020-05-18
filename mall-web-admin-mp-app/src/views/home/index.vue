<template>
    <section class="data_section" ref="data_section">
        <a :href="github" target="_blank">
            <el-row :gutter="20">
                 <el-col :span="6">
                                    <div class="total-frame">
                                        <svg-icon icon-class="order" class="total-icon">
                                        </svg-icon>
                                        <div class="total-title">今日下单</div>
                                        <div class="total-value">{{orderData.nowOrderCount}}</div>
                                    </div>
                                </el-col>
                                <el-col :span="6">
                                    <div class="total-frame">
                                        <svg-icon icon-class="total-today" class="total-icon">
                                        </svg-icon>
                                        <div class="total-title">今日销售总额</div>
                                        <div class="total-value">￥{{orderData.nowOrderPay}}</div>
                                    </div>
                                </el-col>
                                <el-col :span="6">
                                    <div class="total-frame">
                                        <svg-icon icon-class="total-yesterday" class="total-icon">
                                        </svg-icon>
                                        <div class="total-title">今日商品</div>
                                        <div class="total-value">￥{{goods.nowCount}}</div>
                                    </div>
                                </el-col>
                                <el-col :span="6">
                                    <div class="total-frame">
                                        <svg-icon icon-class="total-week" class="total-icon">
                                        </svg-icon>
                                        <div class="total-title">今日会员</div>
                                        <div class="total-value">￥{{user.nowCount}}</div>
                                    </div>
                                </el-col>
            </el-row>
        </a>
        <el-row :gutter="10" class="row_list order_list">
            <el-col :span="7">
                <log-list></log-list>
            </el-col>
            <el-col :span="17">
                <bar-chart type="barChart"></bar-chart>
            </el-col>
        </el-row>
        <el-row :gutter="10" class="row_list order_list">
            <el-col :span="7">
                <pie-chart type="ordertype"></pie-chart>
            </el-col>
            <el-col :span="10">
                <line-chart></line-chart>
            </el-col>
            <el-col :span="7">
                <radar-chart></radar-chart>
            </el-col>
        </el-row>
 <el-row>
                <el-col :span="4">
                    <div style="padding: 20px">
                        <div>
                            <div style="color: #909399;font-size: 14px">本月订单总数</div>
                            <div style="color: #606266;font-size: 24px;padding: 10px 0">{{orderData.monthOrderCount}}
                            </div>
                            <div>
                                <span class="color-success" style="font-size: 14px">{{((orderData.monthOrderCount-orderData.lastMonthOrderCount)/orderData.monthOrderCount*100).toFixed(3)}}%</span>
                                <span style="color: #C0C4CC;font-size: 14px">同比上月</span>
                            </div>
                        </div>
                        <div style="margin-top: 20px;">
                            <div style="color: #909399;font-size: 14px">本周订单总数</div>
                            <div style="color: #606266;font-size: 24px;padding: 10px 0">{{orderData.weekOrderCount}}
                            </div>
                            <div>
                                <span class="color-danger" style="font-size: 14px">{{((orderData.weekOrderCount-orderData.lastWeekOrderCount)/orderData.weekOrderCount*100).toFixed(3)}}%</span>
                                <span style="color: #C0C4CC;font-size: 14px">同比上周</span>
                            </div>
                        </div>
                        <div style="margin-top: 20px;">
                            <div style="color: #909399;font-size: 14px">本月销售总额</div>
                            <div style="color: #606266;font-size: 24px;padding: 10px 0">{{orderData.monthOrderPay}}
                            </div>
                            <div>
                                <span class="color-success" style="font-size: 14px">{{((orderData.monthOrderPay-orderData.lastMonthOrderPay)/orderData.monthOrderPay*100).toFixed(3)}}%</span>
                                <span style="color: #C0C4CC;font-size: 14px">同比上月</span>
                            </div>
                        </div>
                        <div style="margin-top: 20px;">
                            <div style="color: #909399;font-size: 14px">本周销售总额</div>
                            <div style="color: #606266;font-size: 24px;padding: 10px 0">{{orderData.weekOrderPay}}
                            </div>
                            <div>
                                <span class="color-danger" style="font-size: 14px">{{((orderData.weekOrderPay-orderData.lastWeekOrderPay)/orderData.weekOrderPay*100).toFixed(3)}}%</span>
                                <span style="color: #C0C4CC;font-size: 14px">同比上周</span>
                            </div>
                        </div>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div style="padding: 10px;border-left:1px solid #DCDFE6">
                        <el-date-picker
                                style="float: right;z-index: 1"
                                size="small"
                                v-model="orderCountDate"
                                type="daterange"
                                align="right"
                                unlink-panels
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                @change="handleDateChange"
                                :picker-options="pickerOptions">
                        </el-date-picker>
                        <div>
                            <ve-line
                                    :data="chartData"
                                    :legend-visible="false"
                                    :loading="loading"
                                    :data-empty="dataEmpty"
                                    :settings="chartSettings"></ve-line>
                        </div>
                    </div>
                </el-col>
            </el-row>

    </section>
</template>

<script>
 import {str2Date} from '@/utils/date';
    import echarts from 'echarts'
    import salesTable from "./components/salesTable";  // 销售数据表格
    import commentList from "./components/commentList";  // 用户评论列表
    import cardList from "./components/cardList";  // card列表
    import logList from "./components/logList";  // 日志列表
    import barChart from '@/components/echarts/barChart' // 用户投资类型 柱状图
    import pieChart from '@/components/echarts/pieChart' // 用户投资类型 饼状图
    import radarChart from '@/components/echarts/radarChart' // 用户投资类型 雷达图
    import lineChart from '@/components/echarts/lineChart' // 用户投资类型 折线图
     import {goodsStatic, orderStatic, userStatic} from '@/api/home'
    import { github } from "@/utils/env";
 const DATA_FROM_BACKEND = {
        columns: ['time', 'totalCount', 'totalPayAmount'],
        rows: [

        ]
    };
    export default {
        data(){
            return {
                github:github,
                 pickerOptions: {
                                    shortcuts: [{
                                        text: '最近一周',
                                        onClick(picker) {
                                            const end = new Date();
                                            let start = new Date();
                                            start.setFullYear(2018);
                                            start.setMonth(10);
                                            start.setDate(1);
                                            end.setTime(start.getTime() + 3600 * 1000 * 24 * 7);
                                            picker.$emit('pick', [start, end]);
                                        }
                                    }, {
                                        text: '最近一月',
                                        onClick(picker) {
                                            const end = new Date();
                                            let start = new Date();
                                            start.setFullYear(2018);
                                            start.setMonth(10);
                                            start.setDate(1);
                                            end.setTime(start.getTime() + 3600 * 1000 * 24 * 30);
                                            picker.$emit('pick', [start, end]);
                                        }
                                    }]
                                },
                 orderData: '',
                                goods: '',
                                user: '',
                                orderStatusCount: '',
                                orderCountDate: '',
                                chartSettings: {
                                    xAxisType: 'time',
                                    area: true,
                                    axisSite: {right: ['totalPayAmount']},
                                    labelMap: {'totalCount': '订单数量', 'totalPayAmount': '订单金额'}
                                },
                                chartData: {
                                    columns: [],
                                    rows: []
                                },
                                 loading: false,
                                 dataEmpty: false
            }
        },
        components: {
            salesTable,
            commentList,
            cardList,
            logList,
            barChart,
            pieChart,
            radarChart,
            lineChart
        },
        created(){
          this.initOrderCountDate();
                    this.getData();

        },
        mounted(){
        },
        methods: {
        handleDateChange() {
                 //       this.getData();
                    },
                    initOrderCountDate() {
                         this.getData();

                        goodsStatic().then(res => {
                            if (res.code == 200) {
                                this.goods = res.data;
                            }

                        });
                        userStatic().then(res => {
                            if (res.code == 200) {
                                this.user = res.data;
                            }
                        });



                        let start = new Date();

                        const end = new Date();
                        start.setTime(start.getTime() - 1000 * 60 * 60 * 24 * 7);
                        this.orderCountDate = [start, end];
                    },
                    getData() {
                        orderStatic().then(res => {
                                                   if (res.code == 200) {
                                                       this.orderData = res.data;
                                                       this.orderStatusCount = res.data.orderStatusCount;
                                                       this.rows=res.data.orderStsticList;
                                                        this.chartData = {
                                                                                       columns: ['time', 'totalCount', 'totalPayAmount'],
                                                                                       rows: []
                                                                                   };
                                                                                   for (let i = 0; i < this.rows.length; i++) {
                                                                                       let item = this.rows[i];
                                                                                       let currDate = str2Date(item.time);
                                                                                       let start = this.orderCountDate[0];
                                                                                       let end = this.orderCountDate[1];

                                                                                       if (currDate.getTime() >= start.getTime() && currDate.getTime() <= end.getTime()) {
                         this.chartData.rows.push(item);
                                                                                       }
                                                                                   }
                                                                                   this.dataEmpty = false;
                                                                                   this.loading = false
                                                   }

                                               });
                    }
        }
    }
</script>

<style lang="less" scoped>
    .total-frame {
        border: 1px solid #DCDFE6;
        padding: 20px;
        height: 100px;
    }

    .total-icon {
        913656
        color: #409EFF;
        width: 60px;
        height: 60px;
    }

    .total-title {
        position: relative;
        font-size: 16px;
        color: #909399;
        left: 70px;
        top: -35px;
    }

    .total-value {
        position: relative;
        font-size: 18px;
        color: #606266;
        left: 70px;
        top: -25px;
    }
    .data_section{
        margin: 20px;
        border-radius: 2px;
        .row_list{
            margin-bottom: 20px;
            .row_base{
                padding: 10px;
                box-sizing: border-box;
                background: #fff;
                border-radius: 6px;
                height: 120px;
            }
        }
        .order_list{
            .orderArea{
                width: 100%;
                height: 300px;
                background: #fff !important;
                border-radius: 6px;
                box-sizing: border-box;
                padding: 10px;
                padding-top: 40px;
                overflow: hidden;
            }
            .orderbarArea{
                height: 370px;
            }
        }
        .data_list{
            text-align: center;
            font-size: 14px;
            border-radius: 6px;
            padding: 10px;
            color:#fff;
            height: 80px;
            .leftItem{
                align-items: start;
                justify-content: space-between;
                text-align: left;
            }
            .rightItem{
                width:62px;

                align-items: center;
                justify-content: center;
                .svg-icon{
                    font-size: 30px;
                }
            }
            .number{
                font-size: 22px;
                font-weight: bold;
                .perTitle{
                    font-size: 13px;
                    margin-left: 5px;
                }
            }
        }
        .pay{
            .leftItem{
                justify-content: space-around;
            }
        }

    }

</style>
