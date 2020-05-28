<template>
	<view class="member">
		<u-cell-group><u-cell-item v-for="(item,i) in cellList" :key="i" :icon="item.icon" :title="item.title"></u-cell-item></u-cell-group>
	</view>
</template>

<script>
export default {
	props: {
		cellList: {
			type: Array,
			default: [
				{ title: '我的积分', icon: 'rmb-circle' },
				{ title: '积分游戏', icon: 'coupon' },
				{ title: '用户推荐', icon: 'thumb-up' },
				{ title: '会员卡管理', icon: 'grid' },
				{ title: '积分签到', icon: 'edit-pen' },
				{ title: '收货地址管理', icon: 'shopping-cart' },
				{ title: '地图模式', icon: 'map' },
				{ title: '联系客服', icon: 'kefu-ermai' },
				{ title: '帮助中心', icon: 'question-circle' }
			]
		}
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
	width: 95%;
	margin: 35rpx auto;
}
</style>
