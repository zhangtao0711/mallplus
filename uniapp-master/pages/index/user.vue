<template>
	<view class="container">
		<view class="user-section">
			<image class="bg" src="/static/user-bg.jpg"></image>
			<view class="user-info-box">
				<view class="member-top-c">
					<template v-if="userDetailInfo && userDetailInfo.id">
						<view class="" style="display: flex;align-items: center;" >
							<image class="portrait" mode="aspectFill" :src="userDetailInfo.icon" @click="toUserInfo"></image>
							<view class="user-name" style="margin-left: 10upx;color: #FFFFFF;" @click="toUserInfo">{{ userDetailInfo.nickname || userDetailInfo.username }}</view>
							<view  v-if="userDetailInfo.storeId" class="vip-card-box1" @click="toNav('../../pagesC/seller/index')">
                            <view class="b-btn">卖家中心</view>
                            </view>
						</view>
					</template>
					<template v-else>
						<!-- #ifdef H5 || APP-PLUS -->
						<image class="portrait" mode="aspectFill" src="/static/missing-face.png" @click="toLogin"></image>
						<!-- #endif -->
						<!-- #ifdef MP-WEIXIN -->
						<image class="portrait" mode="aspectFill" src="/static/missing-face.png" @click="toWeChatLogin"></image>
                        <!-- #endif -->
						<!-- #ifdef MP-ALIPAY -->
						<view class="portrait"></view>
						<view><button class="login-btn" open-type="getAuthorize" @click="getALICode" hover-class="btn-hover">授权登录</button></view>
						<!-- #endif -->
					</template>
				</view>
			</view>
			<view class="vip-card-box" @click="toNav('../../pagesU/user/applyMember')">
				<image class="card-bg" src="/static/vip-card-bg.png" mode=""></image>
				<view class="b-btn">立即升级</view>
				<view class="tit" v-if="userDetailInfo">
					<text class="yticon icon-iLinkapp-"></text>
					{{ userDetailInfo.memberLevelName || '开通会员' }}
				</view>
				<text class="e-m">Mallplus</text>
				<text class="e-b">升级会员享受更多折扣 一测就上线</text>
			</view>
		</view>

		<view
			class="cover-container"
			:style="[
				{
					transform: coverTransform,
					transition: coverTransition
				}
			]"
			@touchstart="coverTouchstart"
			@touchmove="coverTouchmove"
			@touchend="coverTouchend"
		>
			<image class="arc" src="/static/arc.png"></image>

			<view class="tj-sction">
				<view class="tj-item" @click="toNav('../../pagesU/user/deposit')">
					<text class="num">{{ userDetailInfo.blance || 0 }}</text>
					<text>余额</text>
				</view>
				<view class="tj-item" @click="toNav('../../pagesU/user/coupon')">
					<text class="num">{{ couponList.length || 0 }}</text>
					<text>优惠券</text>
				</view>
				<view class="tj-item" @click="toNav('/pages/integral/home/home')">
					<text class="num">{{ userDetailInfo.integration || 0 }}</text>
					<text>积分</text>
				</view>
			</view>
			<!-- 订单 -->
			<view class="order-section">
				<view class="order-item" @click="navTo('/pages/order/order?status=0')" hover-class="common-hover" :hover-stay-time="50">
					<text class="yticon icon-shouye"></text>
					<text>全部订单</text>
				</view>
				<view class="order-item" @click="navTo('/pages/order/order?status=2')" hover-class="common-hover" :hover-stay-time="50">
					<text class="yticon icon-daifukuan"></text>
					<text>待付款</text>
				</view>
				<view class="order-item" @click="navTo('/pages/order/order?status=3')" hover-class="common-hover" :hover-stay-time="50">
					<text class="yticon icon-yishouhuo"></text>
					<text>待收货</text>
				</view>

				<view class="order-item" @click="navTo('../../pagesA/after_sale/list')" hover-class="common-hover" :hover-stay-time="50">
					<text class="yticon icon-shouhoutuikuan"></text>
					<text>退款/售后</text>
				</view>
			</view>
			<!-- 浏览历史 -->
			<view class="history-section icon">
				<view class="sec-header" v-if="viewList && viewList.length > 0">
					<text class="yticon icon-lishijilu"></text>
					<text>浏览历史</text>
				</view>
				<scroll-view scroll-x class="h-list" v-if="viewList && viewList.length > 0">
					<image v-for="(item, index) in viewList" :key="index" @click="navToDetailPage(item)" :src="item.pic" mode="aspectFill"></image>
				</scroll-view>
				<!--<list-cell icon="icon-iconfontweixin" iconColor="#e07472" title="我的钱包" tips="您的会员还有3天过期"></list-cell>-->
				<list-cell icon="icon-dizhi" iconColor="#5fcda2" title="地址管理" @eventClick="navTo('../../pagesU/address/address')"></list-cell>
				<list-cell icon="icon-tuandui" iconColor="#EE82EE" title="个人资料" @eventClick="navTo('../../pagesU/user/profile')"></list-cell>
				<!--<list-cell icon="icon-share" iconColor="#9789f7" title="分享" tips="邀请好友赢10万大礼"></list-cell>
				<list-cell icon="icon-pinglun-copy" iconColor="#ee883b" title="晒单" tips="晒单抢红包"></list-cell>-->
				<list-cell icon="icon-shoucang_xuanzhongzhuangtai" iconColor="#54b4ef" title="我的收藏" @eventClick="navTo('../../pagesU/user/collect')"></list-cell>
				<list-cell icon="icon-share cgtt" iconColor="#0e68d7" v-if="!userDetailInfo.storeId" title="商户入驻" @eventClick="navTo('../../pagesC/store/applyBusiness')"></list-cell>
				<list-cell icon="icon-share cgtt" iconColor="#0e68d7" v-if="userDetailInfo.storeId" title="商户主页" @eventClick="navTo(`../../pagesC/store/businessDetails?id=${userDetailInfo.storeId}`)"></list-cell>

				<list-cell icon="icon-shoucang_xuanzhongzhuangtai cgtt" iconColor="#0e68d7" v-if="!userDetailInfo.roomNums" title="绑定社区" @eventClick="navTo('../../pagesA/build/bindCommunity')"></list-cell>
				<list-cell icon="icon-shoucang_xuanzhongzhuangtai cgtt" iconColor="#0e68d7" v-if="userDetailInfo.roomNums" title="社区主页" @eventClick="navTo('../../pagesA/build/community')"></list-cell>
					<list-cell icon="icon-pinglun-copy" iconColor="#0e68d7" title="我的邀请码" :tips="userDetailInfo.id"  @eventClick="navTo('../../pagesU/user/invite')"></list-cell>
					<list-cell icon="icon-pinglun-copy" iconColor="#0e68d7" v-if="!userDetailInfo.invitecode" title="推荐邀请码" @eventClick="inputShowModal('invitecode')"></list-cell>
				<list-cell icon="icon-shezhi1" iconColor="#e07472" title="系统设置" border="" @eventClick="navTo('/pages/set/set')"></list-cell>
				<!-- <list-cell icon="icon-shezhi1" iconColor="#e07472" title="test" border="" @eventClick="navTo('/pages/search/test')"></list-cell> -->
			</view>
		</view>
		<neil-modal :show="inputShow" @close="cancel" title="编辑" @cancel="cancel" @confirm="confirm">
        				<input v-model="inputContent" style="margin:20upx" placeholder="请输入..." />
        			</neil-modal>
<mallplusCopyright></mallplusCopyright>
	</view>
</template>
<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import listCell from '@/components/mix-list-cell';
import neilModal from '@/components/neil-modal.vue';
import { mapState,mapMutations } from 'vuex';
let startY = 0,
	moveY = 0,
	pageAtTop = true;
export default {

	components: {
    		mallplusCopyright,
		listCell,neilModal
	},
	data() {
		return {
			inputShow: false,
        			feild: undefined,
        			inputContent: '',
			coverTransform: 'translateY(0px)',
			coverTransition: '0s',
			moving: false,
			userDetailInfo: {
				blance: 0,
				integration: 0
			},

			couponList: [],
			viewList: []
		};
	},

	async onLoad() {
    		this.getData()

    	},
	async onShow() {
		this.getData()

	},

	// #ifndef MP
	onNavigationBarButtonTap(e) {
		const index = e.index;
		if (index === 0) {
			this.navTo('/pages/set/set');
		} else if (index === 1) {
			// #ifdef APP-PLUS
			const pages = getCurrentPages();
			const page = pages[pages.length - 1];
			const currentWebview = page.$getAppWebview();
			currentWebview.hideTitleNViewButtonRedDot({
				index
			});
			// #endif
			uni.navigateTo({
				url: '../../pagesU/notice/notice'
			});
		}
	},
	// #endif

	computed: {
		...mapState(['hasLogin', 'userInfo']),
		// 获取店铺联系人手机号
		kfmobile() {
			return '13146587722' || 0;
		}
	},
	methods: {
	...mapMutations(['logout']),

inputShowModal(feild) {
			this.feild = feild;
			this.inputShow = true;
			this.inputContent = '';
		},
    		cancel() {
            			this.inputShow = false;

            		},

            		confirm() {
            			const that = this;
            			if (!that.inputContent) {
            				that.$api.msg('输入不能为空');
            				return;
            			}
            			let obj = {	id:this.userDetailInfo.id};
            			obj[that.feild] = that.inputContent;
            			Api.apiCall('post', Api.member.updateMember, obj);
            			that.$api.msg('修改成功');
            			that.userInfos[that.feild] = that.inputContent

            		},
		async getData(){
			this.getuserinfo();
			this.getHistory();
		},
		// 获取用户信息
		async getuserinfo(){

				let params = {  };
				let data1 = await Api.apiCall('get', Api.index.userInfo, params);
				if(!data1){
                			this.userDetailInfo={};
                	}
				this.userDetailInfo = data1.member;
				if(!data1.member){
					this.logout();
				}else{
				uni.setStorageSync('userInfos', data1.member);
                				console.log(this.userDetailInfo)
                				let couponList = data1.histories;
                				this.couponList = couponList;
				}


		},
		// 获取浏览历史
		async getHistory(){
			if(this.hasLogin){
				let params = {  };
				let data = await Api.apiCall('get', Api.goods.viewList, params);
				this.viewList = data.result;
			}
		},

		toNav(url){
			uni.navigateTo({
				url: url
			});
		},
		toUserInfo(){
			uni.navigateTo({
				url: '../../pagesU/user/profile'
			});
		},

		toWeChatLogin(){
			uni.navigateTo({
				url: '/pages/public/login',
			});
		},

		//详情页
		navToDetailPage(item) {
			//测试数据没有写id，用title代替
			let id = item.id;
			uni.navigateTo({
				url: `../../pagesA/product/product?id=${id}`
			});
		},
		/**
		 * 统一跳转接口,拦截未登录路由
		 * navigator标签现在默认没有转场动画，所以用view
		 */
		navTo(url) {
			if (!this.hasLogin) {
				url = '/pages/public/login';
			}
			uni.navigateTo({
				url: url
			});
		},

		/**
		 *  会员卡下拉和回弹
		 *  1.关闭bounce避免ios端下拉冲突
		 *  2.由于touchmove事件的缺陷（以前做小程序就遇到，比如20跳到40，h5反而好很多），下拉的时候会有掉帧的感觉
		 *    transition设置0.1秒延迟，让css来过渡这段空窗期
		 *  3.回弹效果可修改曲线值来调整效果，推荐一个好用的bezier生成工具 http://cubic-bezier.com/
		 */
		coverTouchstart(e) {
			if (pageAtTop === false) {
				return;
			}
			this.coverTransition = 'transform .1s linear';
			startY = e.touches[0].clientY;
		},
		coverTouchmove(e) {
			moveY = e.touches[0].clientY;
			let moveDistance = moveY - startY;
			if (moveDistance < 0) {
				this.moving = false;
				return;
			}
			this.moving = true;
			if (moveDistance >= 80 && moveDistance < 100) {
				moveDistance = 80;
			}

			if (moveDistance > 0 && moveDistance <= 80) {
				this.coverTransform = `translateY(${moveDistance}px)`;
			}
		},
		coverTouchend() {
			if (this.moving === false) {
				return;
			}
			this.moving = false;
			this.coverTransition = 'transform 0.3s cubic-bezier(.21,1.93,.53,.64)';
			this.coverTransform = 'translateY(0px)';
		},
		getUserInfo(e) {
			let _this = this;
			//return false;
			console.log('eeeee',e)
			if (e.detail.errMsg == 'getUserInfo:fail auth deny') {
				_this.$common.errorToShow('未授权');
			} else {
				var data = {
					open_id: _this.open_id,
					iv: e.detail.iv,
					edata: e.detail.encryptedData,
					signature: e.detail.signature
				};
				//有推荐码的话，带上
				var invitecode = _this.$db.get('invitecode');
				if (invitecode) {
					data.invitecode = invitecode;
				}
				_this.toWxLogin(data);
			}
		},
		getALICode() {
			let that = this;
			uni.login({
				scopes: 'auth_user',
				success: res => {
					if (res.authCode) {
						uni.getUserInfo({
							provider: 'alipay',
							success: function(infoRes) {
								if (infoRes.errMsg == 'getUserInfo:ok') {
									let user_info = {
										nickname: infoRes.nickName,
										avatar: infoRes.avatar
									};
									that.aLiLoginStep1(res.authCode, user_info);
								}
							},
							fail: function(errorRes) {
								this.$common.errorToShow('未取得用户昵称头像信息');
							}
						});
					} else {
						this.$common.errorToShow('未取得code');
					}
				},
				fail: function(res) {
					this.$common.errorToShow('用户授权失败my.login');
				}
			});
		},
		getWxCode(e) {
			console.log('-------',e)
			let that = this;
			uni.login({
				provider: 'weixin',
				success: function(res) {
					if (res.code) {
						console.log(res.code)
						that.wxLoginStep1(res.code);
					} else {
						this.$common.errorToShow('未取得code');
					}
				},
				fail: function(res) {
					this.$common.errorToShow('用户授权失败wx.login');
				}
			});
		},
		wxLoginStep1(code) {
			var data = {
				code: code
			}
			this.$api.login1(data, res => {
				if (res.status) {
					this.open_id = res.data;

					this.getUserInfo()
				} else {
					this.$common.errorToShow(res.msg, function() {
						uni.navigateBack({
							delta: 1
						});
					});
				}
			});
		},
		aLiLoginStep1(code, user_info) {
			let data = {
				code: code,
				user_info: user_info
			};
			this.$api.alilogin1(data, res => {
				this.alipayNoLogin = false;
				if (res.status) {
					this.open_id = res.data.user_wx_id;
					//判断是否返回了token，如果没有，就说明没有绑定账号，跳转到绑定页面
					if (!res.data.hasOwnProperty('token')) {
						this.$common.redirectTo('/pages/public/index?user_wx_id=' + res.data.user_wx_id);
					} else {
						this.$db.set('userToken', res.data.token);
						this.initData();
					}
				} else {
					this.$common.errorToShow(res.msg);
				}
			});
		},
		toWxLogin(data) {
			console.log('----------data---------', data);
			let _this = this;
			_this.$api.login2(data, function(res) {
				if (res.status) {
					//判断是否返回了token，如果没有，就说明没有绑定账号，跳转到绑定页面
					if (typeof res.data.token == 'undefined') {
						uni.redirectTo({
							url: '/pages/public/index?user_wx_id=' + res.data.user_wx_id
						});
					} else {
						_this.$db.set('userToken', res.data.token);
						_this.initData();
					}
				} else {
					_this.$common.errorToShow('登录失败，请重试');
				}
			});
		},
		toLogin() {
			uni.navigateTo({
				url: '/pages/public/login'
			});
		}, //在线客服,只有手机号的，请自己替换为手机号
		showChat() {
			// #ifdef H5
			let _this = this;
			window._AIHECONG('ini', {
				entId: this.config.ent_id,
				button: false,
				appearance: {
					panelMobile: {
						tone: '#FF7159',
						sideMargin: 30,
						ratio: 'part',
						headHeight: 50
					}
				}
			});
			//传递客户信息
			window._AIHECONG('customer', {
				head: _this.userInfo.avatar,
				名称: _this.userInfo.nickname,
				手机: _this.userInfo.mobile
			});
			window._AIHECONG('showChat');
			// #endif

			// 拨打电话
			// #ifdef APP-PLUS
			if (this.kfmobile) {
				uni.makePhoneCall({
					phoneNumber: '' + this.kfmobile,
					success: () => {
						// console.log("成功拨打电话")
					}
				});
			} else {
				this.$common.errorToShow('商户未设置客服手机号');
			}
			// #endif
		}
	}
};
</script>
<style lang="scss">
@font-face {
		font-family: cgtt;
		font-weight: normal;
		font-style: normal;
		src: url('//at.alicdn.com/t/font_1475801_5innv59qqcr.ttf') format('truetype'),
	}
page{
	background: #F3F3F3;
}
.cgtt {
		font-family: "cgtt" !important;
		font-size: 16px;
		font-style: normal;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
	}

	.icon-userShare:before {
		content: "\c600";
	}

	.icon-userJoin:before {
		content: "\c601";
	}

%flex-center {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}
%section {
	display: flex;
	justify-content: space-around;
	align-content: center;
	background: #fff;
	border-radius: 10upx;
}

.user-section {
	height: 520upx;
	padding: 100upx 30upx 0;
	position: relative;
	.bg {
		position: absolute;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		filter: blur(1px);
		// opacity: 0.7;
	}
}
.user-info-box {
	height: 180upx;
	display: flex;
	align-items: center;
	position: relative;
	z-index: 1;
	.portrait {
		width: 130upx;
		height: 130upx;
		border: 5upx solid #fff;
		border-radius: 50%;
	}
	.username {
		font-size: $font-lg + 6upx;
		color: $font-color-dark;
		margin-left: 20upx;
	}
}
.login-btn {
	color: #fff;
	width: 180upx;
	height: 50upx;
	line-height: 50upx;
	border-radius: 25upx;
	background: #ff7159;
	font-size: 12px;
}
.vip-card-box {
	display: flex;
	flex-direction: column;
	color: #f7d680;
	height: 240upx;
	background: linear-gradient(left, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.8));
	border-radius: 16upx 16upx 0 0;
	overflow: hidden;
	position: relative;
	padding: 20upx 24upx;
	.card-bg {
		position: absolute;
		top: 20upx;
		right: 0;
		width: 380upx;
		height: 260upx;
	}
	.b-btn {
		position: absolute;
		right: 20upx;
		top: 16upx;
		width: 132upx;
		height: 40upx;
		text-align: center;
		line-height: 40upx;
		font-size: 22upx;
		color: #36343c;
		border-radius: 20px;
		background: linear-gradient(left, #f9e6af, #ffd465);
		z-index: 1;
	}
	.tit {
		font-size: $font-base + 2upx;
		color: #f7d680;
		margin-bottom: 28upx;
		.yticon {
			color: #f6e5a3;
			margin-right: 16upx;
		}
	}
	.e-b {
		font-size: $font-sm;
		color: #d8cba9;
		margin-top: 10upx;
	}
}
.vip-card-box1 {
	display: flex;
	flex-direction: column;
	color: #f7d680;
	height: 60upx;
	width: 160upx;
	background: linear-gradient(left, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.8));
	border-radius: 16upx 16upx 0 0;
	overflow: hidden;
	position: relative;
	padding: 20upx 24upx;

	.b-btn {
		position: absolute;
		right: 10upx;
		top: 10upx;
		width: 132upx;
		height: 40upx;
		text-align: center;
		line-height: 40upx;
		font-size: 22upx;
		color: #36343c;
		border-radius: 20px;
		background: linear-gradient(left, #f9e6af, #ffd465);
		z-index: 1;
	}
	.tit {
		font-size: $font-base + 2upx;
		color: #f7d680;
		margin-bottom: 28upx;
		.yticon {
			color: #f6e5a3;
			margin-right: 16upx;
		}
	}
	.e-b {
		font-size: $font-sm;
		color: #d8cba9;
		margin-top: 10upx;
	}
}
.cover-container {
	background: $page-color-base;
	margin-top: -150upx;
	padding: 0 20upx;
	position: relative;
	background: #f5f5f5;
	padding-bottom: 20upx;
	.arc {
		position: absolute;
		left: 0;
		top: -34upx;
		width: 100%;
		height: 36upx;
	}
}
.tj-sction {
	@extend %section;
	.tj-item {
		@extend %flex-center;
		flex-direction: column;
		height: 140upx;
		font-size: $font-sm;
		color: #75787d;
	}
	.num {
		font-size: $font-lg;
		color: $font-color-dark;
		margin-bottom: 8upx;
	}
}
.order-section {
	@extend %section;
	padding: 28upx 0;
	margin-top: 20upx;
	.order-item {
		@extend %flex-center;
		width: 120upx;
		height: 120upx;
		border-radius: 10upx;
		font-size: $font-sm;
		color: $font-color-dark;
	}
	.yticon {
		font-size: 48upx;
		margin-bottom: 18upx;
		color: #fa436a;
	}
	.icon-shouhoutuikuan {
		font-size: 44upx;
	}
}
.history-section {
	padding: 30upx 0 0;
	margin-top: 20upx;
	background: #fff;
	border-radius: 10upx;
	.sec-header {
		display: flex;
		align-items: center;
		font-size: $font-base;
		color: $font-color-dark;
		line-height: 40upx;
		margin-left: 30upx;
		.yticon {
			font-size: 44upx;
			color: #5eba8f;
			margin-right: 16upx;
			line-height: 40upx;
		}
	}
	.h-list {
		white-space: nowrap;
		padding: 30upx 30upx 0;
		image {
			display: inline-block;
			width: 160upx;
			height: 160upx;
			margin-right: 20upx;
			border-radius: 10upx;
		}
	}
}
</style>
