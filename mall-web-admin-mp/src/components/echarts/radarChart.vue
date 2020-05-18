<template>
    <div :id="id" class="orderArea"></div>
</template>

<script>
    import echarts from 'echarts'
    import echartsTheme from "@/components/echarts/theme/westeros.json";
 import {goodsStatic, orderStatic, userStatic,goodsSort,goodsCollect} from '@/api/home'
    export default {
        name:'radarChart',
        data(){
            return {
                 id:"radarChart",
                 indicator:[],
                 value:[],
                 myChart:null,
            }
        },
        created() {
                 let text,value=[],indicator=[];
                  goodsCollect({pageSize:7}).then(res => {
                                                         if (res.code == 200) {
                                                             let list = res.data;
                                                             let max = 0;
                                                              list.forEach((item, index) => {
                                                                                                                              const counts = item.meno1;

                                                                                                                              let color='#87DE75';

                                                                                                                               if(index==0){
                                                                                                                               max =max+parseInt(counts);
console.log(max)
                                                                                                                                                                                               }else if(index==2){
                                                                                                                                 color='#FFA3A1';
                                                                                                                                   max =max+parseInt(counts);
                                                                                                                              }else  if(index==3){
                                                                                                                                max =max+parseInt(counts);
                                                                                                                                 color='#FF9900';
                                                                                                                                }else  if(index==4){
                                                                                                                                  max =max+parseInt(counts);
                                                                                                                                               color='#5FB4FA';
                                                                                                                                    }
                                                                                                                                     else  if(index==5){
                                                                                                                                       max =max+parseInt(counts);
                                                                                                                                       color='#a9d86e';
                                                                                                                                   }
                                                                                                                                      else  if(index==6){
                                                                                                                                        max =max+parseInt(counts);
                                                                                                                                            color='#FF6C60';
                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                          else  if(index==7){
                                                                                                                                            max =max+parseInt(counts);
                                                                                                                                                color='#3b5999';
                                                                                                                                                                                                                                                                                                                                                                                                             }


                                                                                                                            })
                                                             list.forEach((item, index) => {
                                                                 const counts = item.meno1;

                                                                 let color='#87DE75';

                                                                  if(index==1){


                                                                                                                                  }else if(index==2){
                                                                    color='#FFA3A1';

                                                                 }else  if(index==3){

                                                                    color='#FF9900';
                                                                   }else  if(index==4){

                                                                                  color='#5FB4FA';
                                                                       }
                                                                        else  if(index==5){

                                                                          color='#a9d86e';
                                                                      }
                                                                         else  if(index==6){

                                                                               color='#FF6C60';
                                                                                                                                                                                                                                                                             }
                                                                             else  if(index==7){

                                                                                   color='#3b5999';
                                                                                                                                                                                                                                                                                                                                                }
console.log(max)
                                                                 value.push(counts);
                                                                 indicator.push({text: item.name,max:max, color: color});
                                                               })
                                                               this.value=value;
                                                                 this.indicator=indicator;
                                                                 console.log(value)
                                                                   console.log(indicator)
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
                    this.myChart = echarts.init(document.getElementById(this.id));
                    this.myChart.setOption(this.initOption());
                })
            },
         	initOption(){
               let option = {
                    radar: [{
                    name: {
                        fontSize: 11 // 顶点字体大小 不能设置 radio 否则不能生效
                    },
                    splitLine: {
                        lineStyle: {
                          color: '#00aaff' // 每一圈的边界颜色
                        }
                    },
                    axisLabel: {
                        inside: true
                    },
                    axisLine: {
                        lineStyle: {
                          color: '#00aaff' // 半径线颜色
                        }
                    },
                    splitArea: {
                        areaStyle: {
                        // color: ['#00aaff', '#0099ff', '#00aaff', '#0099ff', '#00aaff'] // 每一圈的颜色
                        }
                    },
                     indicator: this.indicator,
                    indicator1: [
                        {text: '东北区域', max: 100, color: '#87DE75'}, // 选中颜色
                        {text: '华南区域', max: 100,color: '#FFA3A1'},
                        {text: '华中区域', max: 100,color: '#FF9900'},
                        {text: '华北区域', max: 100,color: '#5FB4FA'},
                        {text: '西北区域', max: 100,color: '#a9d86e'},
                        {text: '西南区域', max: 100,color: '#FF6C60'},
                        {text: '东北区域', max: 100,color: '#18a689'},
                        {text: '港澳台', max: 100,color: '#3b5999'}
                    ]
                    }],
                    series: [{
                    type: 'radar',
                    data: [{
                        value: this.value,
                        areaStyle: {
                        normal: {
                            opacity: 0.8, // 图表透明度
                            color: '#87DE75' // 图表颜色
                        }
                        },
                        lineStyle: {
                        color: '#6397ff' // 图表边框颜色
                        },
                        label: {
                        normal: {
                            show: true,
                            color: '#6397ff', // 顶点数字颜色
                            fontSize: 16,  // 顶点数字大小
                            formatter: function (params) {
                              return params.value
                            }
                        }
                        }
                    }]
                    }]
                }
				return option;
			},
        },
        watch: {
        }
    }
</script>

<style lang="less">

</style>
