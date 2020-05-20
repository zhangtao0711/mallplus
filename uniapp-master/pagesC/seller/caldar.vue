<template>
	<view class="content">
		<reserve-date
			@changeDay='changeDay'
			@changeMonth='changeMonth'
			:startDate='startDate'
			:price='price'
			:isNowDate='isNowDate'
			:disableBefore='disableBefore'
			:endDate='endDate'
			:singleElection='singleElection'
			:date='date'
		/>
	</view>
</template>

<script>
	import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
	import reserveDate from '@/components/reserve-date/reserve-date.vue'
	import { formatDate } from '@/common/date';

	export default {
		data() {
			return {
				date: '2019-08-07',
				price: {
					type: true,
					data: []
				},
				isNowDate: true,
				disableBefore: false ,
				endDate: '',
				startDate: '',
				singleElection: false
			}
		},
		onLoad(options) {
			this.date = this.dateFormat(new Date())
		},
		async onShow() {
			this.date = this.dateFormat(new Date())
		},
		mounted() {
			this.date = this.dateFormat(new Date())
			console.log(this.date);
			this.changeMonth(['', '', 31])
		},
		methods: {
			// 日期改变
			changeDay(date) {
				console.log(date)
			},
			// 月改变
			async changeMonth(data) {
				console.log(data)
				if (!this.price.type) return false
				uni.showLoading({
					title: '加载中',
					mask: true
				})
				let params = {date:data.join('-')};
				let orderResp = await Api.apiAdminCall('get', Api.admin.orderDayStatic, params,true);
				console.log(orderResp)
				let price = []
				price.push(orderResp.a1);
				price.push(orderResp.a2);
				price.push(orderResp.a3);
				price.push(orderResp.a4);
				price.push(orderResp.a5);
				price.push(orderResp.a6);
				price.push(orderResp.a7);
				price.push(orderResp.a8);
				price.push(orderResp.a9);
				price.push(orderResp.a10);
				price.push(orderResp.a11);
				price.push(orderResp.a12);
				price.push(orderResp.a13);
				price.push(orderResp.a14);
				price.push(orderResp.a15);
				price.push(orderResp.a16);
				price.push(orderResp.a17);
				price.push(orderResp.a18);
				price.push(orderResp.a19);
				price.push(orderResp.a20);
				price.push(orderResp.a21);
				price.push(orderResp.a22);
				price.push(orderResp.a23);
				price.push(orderResp.a24);
				price.push(orderResp.a25);
				price.push(orderResp.a26);
				price.push(orderResp.a27);
				price.push(orderResp.a28);
				price.push(orderResp.a29);
				price.push(orderResp.a30);
				price.push(orderResp.a31);

				this.price.data = price

			},
			dateFormat(time) {
				console.log(time);
				if (time == null || time === '') {
					return 'N/A';
				}
				let date = new Date(time);
				return formatDate(date, 'yyyy-MM-dd');
			}
		},
		components: {
			reserveDate
		}
	}
</script>
