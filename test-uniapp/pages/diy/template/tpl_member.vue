<template>
	<view class="member">
		<view><u-avatar :src="src" :size="140" mode="square"></u-avatar></view>
		<view class="info">
			<view>
				<view>
					<text class="nickname">{{ nickname }}</text>
					<u-tag type="warning" :text="level" style="margin-left: 30rpx;" />
				</view>
			</view>
			<view class="note">{{ note }}</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		nickname: {
			type: String,
			default: '艾杜'
		},
		level: {
			type: String,
			default: '黄金'
		},
		note: {
			type: String,
			default: '填写个人信息更容易被别人关注哦'
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
	height: 180rpx;
	width: 95%;
	margin: 0 auto;
	display: flex;
	justify-content: flex-start;
	align-items: center;
	.info {
		margin-left: 25rpx;
		.nickname{
			font-size: 36rpx;
			font-family: 'PingFang-SC-Regular';
		}
		.note{
			font-size: 30rpx;
			color: #666666;
			height: 75rpx;
			line-height: 75rpx;
		}
	}
}
</style>
