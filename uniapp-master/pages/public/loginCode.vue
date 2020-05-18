<template>
	<view class="container">
		<view class="left-bottom-sign"></view>
		<view class="back-btn yticon icon-zuojiantou-up" @click="navBack"></view>
		<view class="right-top-sign"></view>
		<!-- 设置白色背景防止软键盘把下部绝对定位元素顶上来盖住输入框等 -->
		<view class="wrapper">
			<view class="left-top-sign">LOGIN</view>
			<view class="welcome">欢迎回来！</view>
			<view class="input-content">
				<view class="input-item">
					<text class="tit">手机号码</text>
					<input type="number" v-model="phone" placeholder="请输入手机号码" />
				</view>
				<view class="input-item">
					<text class="tit">验证码</text>
					<view class="input-item-right">
						<input type="number" maxlength="6" placeholder="请输入验证码" v-model="code" @confirm="toLogin" style="width: 70%;" />
						<view class="codeText" v-if="coding == false" @click="getCode()">获取验证码</view>
						<view class="authTime" v-else>{{ auth_time }}秒</view>
					</view>
				</view>
			</view>
			<button class="confirm-btn" @click="toLogin" :disabled="logining">登录</button>
			<view class="forget-section" @click="toLoginPwd">密码登录</view>
		</view>
		<view class="register-section">
			还没有账号?
			<text @click="toRegist">马上注册</text>
		</view>
<mallplusCopyright></mallplusCopyright>
	</view>
</template>

<script>
import { mapMutations, mapState } from 'vuex';
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import store from '@/store/index';
export default {
components: {
		mallplusCopyright
	},
	data() {
		return {
			phone: '',
			code: '',
				logining: false,
			coding: false,
			auth_time: 60
		};
	},
	onLoad() {
		let isWeiXin = this.$common.isWeiXinBrowser();
		if (isWeiXin) {
			this.isWeiXin = 1;
		}
	},
	computed: {
		...mapState(['hasLogin', 'userInfo'])
	},
	methods: {
		...mapMutations(['login']),
		inputChange(e) {
			const key = e.currentTarget.dataset.key;
			this[key] = e.detail.value;
		},
		toLoginPwd() {
			uni.navigateTo({
				url: '/pages/public/login'
			});
		},
		navBack() {
			uni.switchTab({
				url: '/pages/index/index'
			});
		},
		toRegist() {
			uni.navigateTo({
				url: '/pages/public/reg'
			});
		},
async toLogin() {
			var that = this;
			let phoneReg = /^1[1-9][0-9]\d{8}$/;
			try {
				if (this.phone == '') {
					throw '请填写手机号';
				}
				if (!phoneReg.test(this.phone)) {
					throw '手机号格式有误';
				}
				if (this.code == '') {
					throw '请填写密码';
				}
			} catch (err) {
				this.$api.msg(err);
				return;
			}
			this.logining = false;
			let params = { phone: this.phone, authCode: this.code };
			let data = await Api.apiCall('post', Api.index.loginByCode, params);
			//this.logining = false;
			if (data) {
				console.log(data);
				that.login(data)
				uni.setStorageSync('userInfos', data.userInfo);
				uni.setStorageSync('token', data.tokenHead + data.token);
				console.log(uni.getStorageSync('token'))
				uni.switchTab({
					url: '/pages/index/user'
				});
			}
		},
		// 获取验证码
		async getCode() {
			var myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
			if (!myreg.test(this.phone)) {
				uni.showToast({
					icon: 'none',
					title: '请输入正确的手机号码'
				});
				return false;
			}
			//设置倒计时秒
			this.auth_time = 60;
			this.coding = true;
			var auth_timetimer = setInterval(() => {
				this.auth_time--;
				if (this.auth_time < 0) {
					this.coding = false;
					clearInterval(auth_timetimer);
				}
			}, 1000);
			//获取验证码
			let params = { phone: this.phone};
			let data = await Api.apiCall('post', Api.index.sendCodes, params);
			if (data) {
				
			}
		},

		/**
		 * 获取openid
		 */
		getAppletOpenId(params) {
			var that = this;
			wx.request({
				url: Api.BASEURI + Api.index.getAppletOpenId,
				data: params,
				method: 'post',
				success: function(info) {
					// openid seession_key 存入缓存
					that.$db.set('openId', info.data.data.openid);
					that.$db.set('session_key', info.data.data.session_key);

					that.info = {
						openid: info.data.data.openid,
						code: params.code,
						userInfo: params.userInfo,
						cloudID: params.cloudID,
						encrypted_data: params.encryptedData,
						iv: params.iv,
						source: 2,
						signature: params.signature
					};
					that.loginByWeixin(that.info);
				},
				fail: function(e) {
					console.log(e);
				}
			});
		},
		loginByWeixin(datas) {
			var that = this;
			wx.request({
				url: Api.BASEURI + Api.index.appletLogin_by_weixin,
				method: 'post',
				data: datas,
				success: function(res) {
					console.log('--loginByWeixin--', res);
					if (res.data.code == 500) {
						if (res.data.data == '手机号为空，请先绑定手机号') {
							uni.showToast({
								title: res.data.data,
								icon: 'none'
							});
							that.isGetPhone = true;
						}
					} else {
						uni.showToast({
							title: '登录成功'
						});
						that.login(res.data.data);
						that.$db.set('token', res.data.data.tokenHead + res.data.data.token);
						that.$db.set('userInfos', res.data.data.userInfo);

						setTimeout(function() {
							uni.switchTab({
								url: '/pages/index/index'
							});
						}, 1000);
					}
				}
			});
		}
	}
};
</script>

<style lang="scss">
page {
	background: #fff;
}
.container {
	padding-top: 115px;
	position: relative;
	width: 100vw;
	height: 100vh;
	overflow: hidden;
	background: #fff;
}
.wrapper {
	position: relative;
	z-index: 90;
	background: #fff;
	padding-bottom: 40upx;
}
.input-item-right {
	display: flex;
	flex-direction: row;
	align-items: center;
	width: 100%;
	justify-content: space-between;
}
.codeText {
	font-size: 28upx;
	width: 25%;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #fa436a;
}
.authTime {
	font-size: 28upx;
	width: 25%;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #fa436a;
}
.back-btn {
	position: absolute;
	left: 40upx;
	z-index: 9999;
	padding-top: var(--status-bar-height);
	top: 40upx;
	font-size: 40upx;
	color: $font-color-dark;
}
.left-top-sign {
	font-size: 120upx;
	color: $page-color-base;
	position: relative;
	left: -16upx;
}
.right-top-sign {
	position: absolute;
	top: 80upx;
	right: -30upx;
	z-index: 95;
	&:before,
	&:after {
		display: block;
		content: '';
		width: 400upx;
		height: 80upx;
		background: #b4f3e2;
	}
	&:before {
		transform: rotate(50deg);
		border-radius: 0 50px 0 0;
	}
	&:after {
		position: absolute;
		right: -198upx;
		top: 0;
		transform: rotate(-50deg);
		border-radius: 50px 0 0 0;
		/* background: pink; */
	}
}
.left-bottom-sign {
	position: absolute;
	left: -270upx;
	bottom: -320upx;
	border: 100upx solid #d0d1fd;
	border-radius: 50%;
	padding: 180upx;
}
.welcome {
	position: relative;
	left: 50upx;
	top: -90upx;
	font-size: 46upx;
	color: #555;
	text-shadow: 1px 0px 1px rgba(0, 0, 0, 0.3);
}
.input-content {
	padding: 0 60upx;
}
.input-item {
	display: flex;
	flex-direction: column;
	align-items: flex-start;
	justify-content: center;
	padding: 0 30upx;
	background: $page-color-light;
	height: 120upx;
	border-radius: 4px;
	margin-bottom: 50upx;
	&:last-child {
		margin-bottom: 0;
	}
	.tit {
		height: 50upx;
		line-height: 56upx;
		font-size: $font-sm + 2upx;
		color: $font-color-base;
	}
	input {
		height: 60upx;
		font-size: $font-base + 2upx;
		color: $font-color-dark;
		width: 100%;
	}
}

.confirm-btn {
	width: 630upx;
	height: 76upx;
	line-height: 76upx;
	border-radius: 50px;
	margin-top: 70upx;
	background: $uni-color-primary;
	color: #fff;
	font-size: $font-lg;
	&:after {
		border-radius: 100px;
	}
}
.forget-section {
	font-size: $font-sm + 2upx;
	color: $font-color-spec;
	text-align: center;
	margin-top: 40upx;
}
.register-section {
	position: absolute;
	left: 0;
	bottom: 50upx;
	width: 100%;
	font-size: $font-sm + 2upx;
	color: $font-color-base;
	text-align: center;
	text {
		color: $font-color-spec;
		margin-left: 10upx;
	}
}
</style>
