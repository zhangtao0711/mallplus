<template>
	<view class="content">
		<view class="scroll-top" >
			<time-filter @chooseTime='chooseTime' :type="type"></time-filter>
		</view>
		<view class="part tj padding">
			<view class="tjTitle">
				<view class="left">订单数量</view>
				<view class="right">{{homedata.orderCount}}个</view>
			</view>
			<view class="tjBody flexRow">
				<circle-percent titleOne="当月订单" :titleTwo="homedata.nowOrderCount" bg="#fbad4c"  :percent="(homedata.nowOrderCount*100/homedata.orderCount) | numFilter"></circle-percent>
				<circle-percent titleOne="当月总额" :titleTwo="homedata.nowOrderPay" bg="#87d7a5"  :percent="(homedata.nowOrderCount*100/homedata.orderCount) | numFilter"></circle-percent>
			</view>
		</view>
		<view class="part tj padding">
			<view class="tjTitle">
				<view class="left">会员统计</view>
				<view class="right">{{homedata.memberCount}}人</view>
			</view>
			<view class="tjBody flexRow">
				<circle-percent titleOne="当月新增会员" :titleTwo="homedata.mallCount+homedata.femallount" bg="#87d7a5"  :percent="((homedata.mallCount+homedata.femallount)/homedata.memberCount)| numFilter"></circle-percent>
				<circle-percent titleOne="男会员" :titleTwo="homedata.mallCount" bg="#00adc7"  :percent="(homedata.mallCount/homedata.memberCount)| numFilter"></circle-percent>
				<circle-percent titleOne="女会员" :titleTwo="homedata.femallount" bg="#FF6A6A"  :percent="(homedata.femallount/homedata.memberCount)| numFilter"></circle-percent>

			</view>
		</view>
		<view class="part tj padding">
			<view class="tjTitle">
				<view class="left">商品统计</view>
				<view class="right">{{homedata.goodsCount}}个</view>
			</view>
			<view class="tjBody flexRow">
				<circle-percent titleOne="新增商品" :titleTwo="homedata.onCount+homedata.offCount" bg="#87d7a5"  :percent="((homedata.onCount+homedata.offCount)/homedata.goodsCount)| numFilter"></circle-percent>
				<circle-percent titleOne="下架商品" :titleTwo="homedata.offCount" bg="#00adc7"  :percent="(homedata.offCount/homedata.goodsCount)| numFilter"></circle-percent>
				<circle-percent titleOne="缺库存商品" :titleTwo="homedata.noStock" bg="#FF6A6A"  :percent="(homedata.noStock/homedata.goodsCount)| numFilter"></circle-percent>

			</view>
		</view>
	</view>

</template>

<script>
	import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
    import timeFilter from '@/components/time-filter/time-filter'
    import circlePercent from '@/components/circle-percent/circle-percent'
	import { formatDate } from '@/common/date';
	export default {
		data() {
			return {
				date: '2019-11-01',
				homedata:null,
				singleElection: false,
				type: 'month'
			}
		},
        components: {
          timeFilter,
          circlePercent
        },
		onLoad() {
		},
		async onShow() {
			this.date = this.dateFormat(new Date());
			 let params = {date:this.date,type:2};
			 let resp = await Api.apiAdminCall('get', Api.admin.dayStatic, params,true);
			 console.log(resp)
			 this.homedata = resp;
		},
        computed: {
        },
		methods: {
			async	chooseTime(item, index) {
				let params = {date:this.getMonth(item.value),type:2};
				let resp = await Api.apiAdminCall('get', Api.admin.dayStatic, params,true);
				console.log(resp)
				this.homedata = resp;
			},
			addDate(days) {
				var d = new Date();
				d.setDate(d.getDate() + days);
				var m = d.getMonth() + 1;
				if(m < 10){
					m='0'+m
				}
				var day = d.getDate()
				if(day < 10){
					day='0'+day
				}
				return d.getFullYear() + '-' + m + '-' + day;
			},
			getMonth(month) {
				var time = new Date();
				time.setMonth(time.getMonth() + month);//设置month月后的时间
				var y = time.getFullYear();
				var m = time.getMonth() + 1;//获取当前月份
				var d = time.getDate();
				if(m<10){
					m = "0"+m
				}
				return y + "-" + m + '-' + d;
			},

		dateFormat(time) {
			if (time == null || time === '') {
				return 'N/A';
			}
			let date = new Date(time);
			return formatDate(date, 'yyyy-MM-dd');
		}

		}
		,filters: {
			numFilter (value) {
				let realVal = ''
				if (value) {
					// 截取当前数据到小数点后两位
					realVal = parseFloat(value).toFixed(2)
				} else {
					realVal = '0'
				}
				return realVal
			}
		}
	}
</script>

<style lang="scss">
	.content{
		background-color: $uni-bg-color;
	}

	.part{
		background: $uni-bg-color;
		font-size: $uni-font-size-lg;
		color: $uni-text-color;
		margin-bottom: 20upx;
	}

	.padding {
		padding: 30upx 40upx;
	}

	.flexRow{
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		justify-content: space-around;
	}

    .tj{
		margin-bottom: 0;
		padding-bottom: 0;
		.tjTitle{
			display: flex;
			flex-direction: row;
			border-bottom: 1upx solid #ebedf0;
			padding: 40upx 0;
            .left{
				flex: 1;
				text-align: left;
				font-size: $uni-font-size-base;
				font-weight: bold;
			}
			.right{
				flex: 1;
				text-align: right;
				color: $uni-text-color-grey;
			}
		}
		.tjBody{
			justify-content: flex-start;
			border-bottom: 1upx solid #f5f5f5;
		}
	}
</style>
