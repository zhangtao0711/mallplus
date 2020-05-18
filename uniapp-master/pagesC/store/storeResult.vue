<template>
	<view class="content">
		<text class="success-icon yticon icon-xuanzhong2"></text>
		<text class="tit">{{orderInfo.name}}</text>
		<view class="result-mid red-price" v-if="orderInfo.status==1">
			申请中----
		</view>
		<view class="result-mid red-price" v-if="orderInfo.status==2">
        			申请拒绝----
        </view>
		<view class="btn-group">
			<navigator url="/pages/order/order?status=0" open-type="redirect" class="mix-btn">用户中心</navigator>
			<navigator url="/pages/index/user" open-type="switchTab" class="mix-btn hollow">返回首页</navigator>
		</view>

	</view>
</template>

<script>
	import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
	export default {
		components: {
                		mallplusCopyright
                	},
		data() {

			return {
				paymentId: 0,
				orderInfo: {}, // 支付单详情
				orderId: 0,
				status: false
			}
		},
		async onLoad(options) {
			console.log(options)
        		let params = {  };
        		let data = await Api.apiCall('get', Api.member.storeDetail1, params);
			console.log(data)
			this.orderInfo = data.store;
			if (data.member){
				uni.setStorageSync('userInfo', data.member);
			  }
			} ,



		methods: {
			status(time) {
        			if (time == null || time === '') {
        				return 'N/A';
        			}
        			let date = new Date(time);
        			return formatDate(date, 'yyyy-MM-dd hh:mm:ss');
        		}}
	};
</script>

<style lang="scss">
	.content {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	.success-icon {
		font-size: 160upx;
		color: #fa436a;
		margin-top: 100upx;
	}
	.tit {
		font-size: 38upx;
		color: #303133;
	}
	.btn-group {
		padding-top: 100upx;
	}
	.mix-btn {
		margin-top: 30upx;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 600upx;
		height: 80upx;
		font-size: $font-lg;
		color: #fff;
		background-color: $base-color;
		border-radius: 10upx;
		&.hollow {
			background: #fff;
			color: #303133;
			border: 1px solid #ccc;
		}
	}
</style>
