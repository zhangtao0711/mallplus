<template>
	<view class="container">
		<view class="left-bottom-sign"></view>
		<view class="back-btn yticon icon-zuojiantou-up" @click="navBack"></view>
		<view class="right-top-sign"></view>
		<!-- 设置白色背景防止软键盘把下部绝对定位元素顶上来盖住输入框等 -->
		<view class="wrapper">
			<!-- #ifdef H5 -->
			<view class="left-top-sign">{{ sysInfo.name }}LOGIN</view>
			<view class="welcome">欢迎回来！{{ sysInfo.name }}</view>
			<view class="input-content">
				<view class="input-item">
					<text class="tit">手机号码</text>
					<input type="number" v-model="phone" placeholder="请输入手机号码" />
				</view>
				<view class="input-item">
					<text class="tit">密码</text>
					<input type="password" placeholder="请输入密码" v-model="password" @confirm="toLogin" />
				</view>
			</view>
			<button class="confirm-btn" @click="toLogin" :disabled="logining">登录</button>

			<!-- <button v-if="isWeiXin == 1" class="confirm-btn" @click="wechatH5Login" :disabled="logining">微信授权登录</button>  -->

			<view class="forget-section" @click="toForget">忘记密码?</view>
			<view class="forget-section" @click="toLoginCode">验证码登录</view>
			<!-- #endif -->
			<!-- #ifdef MP-WEIXIN -->
			<button class="confirm-btn" open-type="getUserInfo" @getuserinfo="wxGetUserInfo" withCredentials="true" v-if="isGetPhone == false">微信登录</button>
			<button class="confirm-btn" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber" v-if="isGetPhone == true">获取手机号</button>
			<!-- #endif -->

			<!-- #ifdef APP-PLUS -->
			<button class="confirm-btn" type="primary" open-type="getUserInfo" @click="getuserinfoh5appwx" withCredentials="true">微信登录</button>
			<!-- #endif -->
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
			phone: '13146587722',
			password: '123456',
			isWeiXin: 0,
			sysInfo: '',
			logining: false,
			isGetPhone: false,
			info: ''
		};
	},
	onLoad() {
		let isWeiXin = this.$common.isWeiXinBrowser();
		if (isWeiXin) {
			this.isWeiXin = 1;
		}
		this.sysInfo = this.$db.get('sysInfo');
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
		toForget() {
			uni.navigateTo({
				url: '../../pagesU/user/forget'
			});
		},
		toLoginCode() {
			uni.navigateTo({
				url: '/pages/public/loginCode'
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
		wechatH5Login() {
			const that = this;
			let href = window.location.origin;
			let page = that.$api.prePage();
			let prePath = '/pages/index/index';
			if (page) {
				prePath = page.__page__.path;
			}

			window.location =
				'https://open.weixin.qq.com/connect/oauth2/authorize?' +
				'appid=' +
				Api.h5Appid +
				'&redirect_uri=' +
				escape(href) +
				'&response_type=code&scope=snsapi_userinfo&state=mallplus#wechat_redirect';
		},
		wxGetUserInfo: function(res) {
			if (!res.detail.iv) {
				uni.showToast({
					title: '您取消了授权,登录失败',
					icon: 'none'
				});
				return false;
			}
			// this.login(res.detail.rawData);
			const that = this;
			let datas = '';
			let errmsg = '';
			uni.login({
				provider: 'weixin',
				success: function(loginRes) {

					var params = {
                    						code: loginRes.code,
                    						userInfo: res.detail.rawData,
                    						cloudID: res.detail.cloudID,
                    						encrypted_data: res.detail.encryptedData,
                    						iv: res.detail.iv,
                    						source: 2,
                    						signature: res.detail.signature
                    					};
                    					that.loginByWeixinNoPhone(params);
				}
			});
		},
		loginByWeixinNoPhone(datas) {
        			var that = this;
        			wx.request({
        				url: Api.BASEURI + Api.index.appletLogin_by_weixin,
        				method: 'post',
        				data: datas,
        				success: function(res) {
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
        			});
        		},
		/**
		 * 微信登录
		 */
		wxGetUserInfo1: function(res) {
			let ress = res;
			console.log('-----getUserInfo-----', ress);
			if (!ress.detail.iv) {
				uni.showToast({
					title: '您取消了授权,登录失败',
					icon: 'none'
				});
				return false;
			}

			const that = this;
			let datas = '';
			uni.login({
				provider: 'weixin',
				success: function(loginRes) {
					console.log('=====login=====', loginRes);
					var params = {
						code: loginRes.code,
						userInfo: ress.detail.rawData,
						cloudID: ress.detail.cloudID,
						encrypted_data: ress.detail.encryptedData,
						iv: ress.detail.iv,
						source: 2,
						signature: ress.detail.signature
					};
					that.getAppletOpenId(params);
				}
			});
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
						if (res.data.data == '登录失败 请先绑定手机号') {
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
		},
		/**
		 * 微信获取手机号
		 */
		getPhoneNumber(params) {
			var that = this;
			wx.request({
				url: Api.BASEURI + Api.index.getWxPhone,
				method: 'post',
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				data: {
					openid: uni.getStorageSync('openId'),
					keyStr: uni.getStorageSync('session_key'),
					ivStr: params.detail.iv,
					encDataStr: params.detail.encryptedData
				},
				success: function(res) {
					console.log('--getPhoneNumber--', res);
					if (res.data.code == 200) {
						uni.setStorageSync('phone', res.data.data);
						that.info['phone'] = res.data.data;
						that.loginByWeixin(that.info);
					}
				}
			});
		},

		appLogin() {
			uni.getProvider({
				service: 'oauth',
				success: function(res) {
					console.log(res.provider);
					//支持微信、qq和微博等
					if (~res.provider.indexOf('weixin')) {
						uni.login({
							provider: 'weixin',
							success: function(loginRes) {
								console.log('-------获取openid(unionid)-----');
								console.log(JSON.stringify(loginRes));
								// 获取用户信息
								uni.getUserInfo({
									provider: 'weixin',
									success: function(info) {
										console.log('-------获取微信用户所有-----');
										console.log(JSON.stringify(info.userInfo));
										let logparams1 = { logs: JSON.stringify(info.userInfo) };
										Api.apiCall('get', Api.index.logs, logparams1);
										// 与服务器交互将数据提交到服务端数据库
										uni.request({
											url: Api.BASEURI + Api.index.appLogin,
											method: 'POST',
											header: { 'content-type': 'application/x-www-form-urlencoded' },
											data: {
												sex: info.userInfo.gender,
												city: info.userInfo.country + '-' + info.userInfo.province + '-' + info.userInfo.city,
												source: 1,
												openid: info.userInfo.openId,
												unionid: info.userInfo.openId,
												nickname: info.userInfo.nickName,
												headimgurl: info.userInfo.avatarUrl
											},
											success: res => {
												console.log(JSON.stringify(res));

												// 登录成功 记录会员信息到本地
												if (res) {
													uni.setStorageSync('userInfo', res.data.userInfo);
													uni.setStorageSync('token', res.data.tokenHead + res.data.token);
													uni.switchTab({
														url: '/pages/index/index'
													});
												} else {
													uni.showToast({ title: res.data });
												}
											},
											fail: e => {
												console.log(JSON.stringify(e));
											}
										});
										let logparams = { logs: JSON.stringify(info.userInfo) };
										Api.apiCall('get', Api.index.logs, logparams);
									}
								});
							}
						});
					}
				}
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
				if (this.password == '') {
					throw '请填写密码';
				}
			} catch (err) {
				this.$api.msg(err);
				return;
			}
			this.logining = false;
			let params = { phone: this.phone, password: this.password };
			let data = await Api.apiCall('post', Api.index.login, params);
			//this.logining = false;
			if (data) {
				console.log(data);
				that.login(data);
				uni.setStorageSync('userInfos', data.userInfo);
				uni.setStorageSync('token', data.tokenHead + data.token);
				console.log(uni.getStorageSync('token'));
				uni.switchTab({
					url: '/pages/index/user'
				});
			}
		},
		getuserinfoh5appwx() {
			var that = this;
			uni.login({
				success: res => {
					// res 对象格式

					uni.getUserInfo({
						success: info => {
							// 与服务器交互将数据提交到服务端数据库
							uni.request({
								url: Api.BASEURI + Api.index.appLogin,
								method: 'POST',
								header: { 'content-type': 'application/x-www-form-urlencoded' },
								data: {
									sex: info.userInfo.gender,
									city: info.userInfo.country + '-' + info.userInfo.province + '-' + info.userInfo.city,

									source: 1,
									unionid: info.userInfo.openId,
									openid: info.userInfo.openId,
									nickname: info.userInfo.nickName,
									headimgurl: info.userInfo.avatarUrl
								},
								success: res => {
									console.log(JSON.stringify(res.data.data));
									// 登录成功 记录会员信息到本地
									if (res) {
										console.log(res);
										that.login(res.data.data);
										uni.setStorageSync('userInfos', res.data.data.userInfo);
										uni.setStorageSync('token', res.data.data.tokenHead + res.data.data.token);

										uni.switchTab({
											url: '/pages/index/index'
										});
									} else {
										uni.showToast({ title: res.data });
									}
								},
								fail: e => {
									console.log(JSON.stringify(e));
								}
							});
						},
						fail: () => {
							uni.showToast({ title: '微信登录授权失败' });
						}
					});
				},
				fail: () => {
					uni.showToast({ title: '微信登录授权失败' });
					uni.hideLoading();
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
