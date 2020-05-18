<template>
    <div :id="id" class="orderArea orderbarArea"></div>
</template>

<script>
import {orderStatusStatics, memberMonthStatic, userStatic,goodsSort,goodsCollect} from '@/api/home'
    import echarts from 'echarts'
    import echartsTheme from "@/components/echarts/theme/westeros.json";
    export default {
        name:'barChat',
        data(){
            return {
                 id:'barChart',
                  series_data:[],
                   legend_data:[],
                 myChart:null,
            }
        },
       created() {

                        memberMonthStatic({}).then(res => {
                                                               if (res.code == 200) {
                                                                   let map = res.data;
                                                                  for (var key in map) {
                                                                   let data=[];
                                                                   if(map[key] && map[key]!=null){
      data.push(map[key]['a1']);data.push(map[key]['a2']);data.push(map[key]['a3']);data.push(map[key]['a4']);data.push(map[key]['a5']);data.push(map[key]['a6']);
      data.push(map[key]['a7']);data.push(map[key]['a8']);data.push(map[key]['a9']);data.push(map[key]['a10']);data.push(map[key]['a11']);data.push(map[key]['a12']);

                                                                   }

this.legend_data=data;


                                                                   }



                                                                      this.loadChart();
                                                               }

                                                           });


                             },
        beforeDestroy() {
			if (!this.myChart) {
				return
			}
			this.myChart.dispose();
			this.myChart = null;
        },
        methods: {
            loadChart(){
                this.$nextTick(() => {
                    echarts.registerTheme('westeros', echartsTheme)
                    this.myChart = echarts.init(document.getElementById(this.id),'westeros');
                    this.myChart.setOption(this.initOption());
                })
            },
         	initOption(){
                let option = {
                    tooltip : {
                        trigger: 'axis'
                    },
                    legend: {
                        data:['访问量','下载量']
                    },
                    xAxis : [
                        {
                            type : 'category',
                            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value'
                        }
                    ],
                    series : [
                        {
                            name:'会员统计',
                            type:'bar',
                            data:this.legend_data,
                            markPoint : {
                                data : [
                                    {type : 'max', name: '最大值'},
                                    {type : 'min', name: '最小值'}
                                ]
                            }
                        }

                    ]
                };
				return option;
			},
        },
        watch: {
        }
    }
</script>

<style lang="less">

</style>
