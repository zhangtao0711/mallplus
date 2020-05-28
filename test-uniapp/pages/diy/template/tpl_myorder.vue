<template>
	<view class="member">
		<view class="cell">
			<u-cell-group><u-cell-item title="我的订单" value="更多"></u-cell-item></u-cell-group>
		</view>

		<view class="icon">
			<view v-for="(item,i) in iconList" :key="i" class="list">
				<image :src="item.src" class="u-badge-wrap"></image>
				<text>{{ item.title }}</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		iconList: {
			type: Array,
			default: [
				{
					src: '../../../static/logo.png',
					title: '待付款'
				},
				{
					src: '../../../static/logo.png',
					title: '待发货'
				},
				{
					src: '../../../static/logo.png',
					title: '待收货'
				},
				{
					src: '../../../static/logo.png',
					title: '已完成'
				},
				{
					src: '../../../static/logo.png',
					title: '退款'
				}
			]
		},
	},
	data() {
		return {
		};
	},
	onLoad() {},
	methods: {}
};
</script>

<style lang="scss">
.member {
	// height: 180rpx;
	width: 95%;
	margin: 0 auto;
	border: 1rpx solid #c0c0c0;
	box-shadow: 5rpx 5rpx 25rpx;
	border-radius: 25rpx;
	.cell {
		margin: 15rpx 5rpx;
	}
	.icon {
		display: flex;
		justify-content: space-around;
		.list {
			margin: 15rpx auto;
			display: flex;
			flex-direction: column;
			justify-content: center;
			text-align: center;
		}
		.u-badge-wrap {
			width: 40px;
			height: 40px;
			border-radius: 6px;
		}
	}
}
</style>
