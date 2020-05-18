<template>
	<view class="container">
		<view class="list-cell b-b m-t" @click="navTo('../../pagesU/user/profile')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">个人资料</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell b-b" @click="navTo('../../pagesU/address/address')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">收货地址</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell" @click="navTo('/pages/set/certification')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">实名认证</text>
			<text class="cell-more yticon icon-you"></text>
		</view>

		<view class="list-cell m-t">
			<text class="cell-tit">消息推送</text>
			<switch checked color="#fa436a" @change="switchChange" />
		</view>
		<view class="list-cell m-t b-b" @click="navTo('清除缓存')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">清除缓存</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell b-b" @click="navTo('关于Dcloud')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">关于{{sysInfo.name}}</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell">
			<text class="cell-tit">检查更新</text>
			<text class="cell-tip">当前版本 {{sysInfo.version}}</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell log-out-btn" @click="toLogout">
			<text class="cell-tit">退出登录</text>
		</view>
<mallplusCopyright></mallplusCopyright>
	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import { mapMutations } from 'vuex';
export default {
components: {
		mallplusCopyright
	},
	data() {
		return {
			sysInfo:'',
		};
	},
	onLoad() {
		this.sysInfo = this.$db.get('sysInfo')
	},
	methods: {
		...mapMutations(['logout']),

		navTo(url) {
			uni.navigateTo({
				url: url
			});
			this.$api.msg(`跳转到${url}`);
		},
		//退出登录
		toLogout() {
			uni.showModal({
				content: '确定要退出登录么',
				success: e => {
					if (e.confirm) {
						this.logout();
						this.$db.del('userInfos')
						this.$db.del('token')
						Api.apiCall('post', Api.index.logout, {});
						setTimeout(() => {
							uni.navigateBack();
						}, 200);
					}
				}
			});
		},
		//switch
		switchChange(e) {
			let statusTip = e.detail.value ? '打开' : '关闭';
			this.$api.msg(`${statusTip}消息推送`);
		}
	}
};
</script>

<style lang="scss">
page {
	background: $page-color-base;
}
.list-cell {
	display: flex;
	align-items: baseline;
	padding: 20upx;
	line-height: 60upx;
	position: relative;
	background: #fff;
	justify-content: center;
	&.log-out-btn {
		margin-top: 40upx;
		border-radius: 50upx;
		width: 95%;
		margin-left: 2.5%;
		background: $uni-color-primary;
		.cell-tit {
			color: #FFFFFF;
			text-align: center;
			margin-right: 0;
		}
	}
	&.cell-hover {
		background: #fafafa;
	}
	&.b-b:after {
		left: 30upx;
	}
	&.m-t {
		margin-top: 16upx;
	}
	.cell-more {
		align-self: baseline;
		font-size: $font-lg;
		color: $font-color-light;
		margin-left: 10upx;
	}
	.cell-tit {
		flex: 1;
		font-size: $font-base + 2upx;
		color: $font-color-dark;
		margin-right: 10upx;
	}
	.cell-tip {
		font-size: $font-base;
		color: $font-color-light;
	}
	switch {
		transform: translateX(16upx) scale(0.84);
	}
}
</style>
