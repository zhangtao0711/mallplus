<template>
    <div :id="id" class="orderArea"></div>
</template>

<script>
	import echarts from 'echarts'
    import echartsTheme from "@/components/echarts/theme/westeros.json";
    import {goodsStatic, orderStatic, userStatic,goodsSort,goodsCollect} from '@/api/home'
    export default {
        data(){
            return {
				 id:'ordertype',
				 series_data:[],
				 legend_data:[],
                 myChart:null,
            }
        },
		props: ['type'],
        mounted(){
			this.loadChart();
		},
		beforeDestroy() {
			if (!this.myChart) {
				return
			}
			this.myChart.dispose();
			this.myChart = null;
        },
          created() {
          let text,legend_data=[],series_data=[];
           goodsSort({pageSize:5,sortCol:'sale',cols:'id,name,sale'}).then(res => {
                                                  if (res.code == 200) {
                                                      let list = res.data.records;
                                                      list.forEach((item, index) => {
                                                          const name = item.name;
                                                          legend_data.push(name);
                                                          series_data.push({name: name, value: item.sale});
                                                        })
                                                        this.legend_data=legend_data;
                                                          this.series_data=series_data;
                                                         this.loadChart();
                                                  }

                                              });


                },
        methods: {
			 loadChart(){
                this.$nextTick(() => {
                    echarts.registerTheme('westeros', echartsTheme)
                    this.myChart = echarts.init(document.getElementById(this.id),'westeros');
                    this.myChart.setOption(this.initOption(this.type));
                })
            },
         	initOption(type){
				let text,legend_data=[],series_data=[];
				if(type == "ordertype"){
					text = "商品销量排行";
					 legend_data=this.legend_data;
                    series_data=   this.series_data;


				}else{
					text = "用户投资区域";
					legend_data = ['华东区','华南区','华中区','华北区','西南区','东北区','台港澳'];
					series_data = [
						{value:335, name:'华东区'},
						{value:310, name:'华南区'},
						{value:234, name:'华中区'},
						{value:835, name:'华北区'},
						{value:1548, name:'西南区'},
						{value:335, name:'东北区'},
						{value:454, name:'台港澳'}
					]
				}
				let data = {
					  title : {
						text: text,
						x:'center'
					},
					tooltip : {
						trigger: 'item',
						formatter: "{a} <br/>{b} : {c} ({d}%)"
					},
					legend: {
						orient: 'vertical',
						left: 'left',
						data: legend_data
					},
					series : [
						{
							name: '访问来源',
							type: 'pie',
							radius : '50%',
                            center: ['50%', '60%'],
							data:series_data,
							itemStyle: {
								emphasis: {
									shadowBlur: 10,
									shadowOffsetX: 0,
									shadowColor: 'rgba(0, 0, 0, 0.5)'
								}
							}
						}
					]
				}
				return data;
			},
        },

    }
</script>

<style lang="less">

</style>
