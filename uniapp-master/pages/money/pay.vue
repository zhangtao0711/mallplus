<template>
	<view class="app">
		<view class="price-box">
			<text>支付金额</text>
			<text class="price">{{orderInfo.payAmount}}</text>
		</view>

		<view class="pay-type-list">

			<view class="type-item b-b" @click="changePayType(2)">
				<text class="icon yticon icon-weixinzhifu"></text>
				<view class="con">
					<text class="tit">微信支付</text>
					<text>推荐使用微信支付</text>
				</view>
				<label class="radio">
					<radio value="" color="#fa436a" :checked='payType == 2' />

				</label>
			</view>
			<view class="type-item b-b" @click="changePayType(1)">
				<text class="icon yticon icon-alipay"></text>
				<view class="con">
					<text class="tit">支付宝支付</text>
				</view>
				<label class="radio">
					<radio value="" color="#fa436a" :checked='payType == 1' />

				</label>
			</view>
			<view class="type-item" @click="changePayType(3)">
				<text class="icon yticon icon-erjiye-yucunkuan"></text>
				<view class="con">
					<text class="tit">预存款支付</text>
					<text>可用余额 ¥{{orderInfo.blance}}</text>
				</view>
				<label class="radio">
					<radio value="" color="#fa436a" :checked='payType == 3' />

				</label>
			</view>
		</view>

		<text class="mix-btn" @click="confirm">确认支付</text>
	</view>
</template>

<script>
    import { mapState } from 'vuex';
    import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
	export default {
	components: {
    		mallplusCopyright
    	},
		data() {
			return {
				payType: 1,
				orderInfo: {}
			};
		},
        computed: {
            ...mapState(['hasLogin','userInfo'])
    },
        async onLoad(options) {
            let params = {'id':options.id};
            this.orderInfo = await Api.apiCall('get',Api.order.orderDetail,params);
		},
        computed: {
            ...mapState(['hasLogin','userInfo'])
   		 },
		methods: {
			//选择支付方式
			changePayType(type) {
				this.payType = type;
			},
			//确认支付
            async  confirm() {

                if (this.payType ==2){
                     let params = {'orderId':this.orderInfo.id};

// #ifdef APP-PLUS
			let appRes = await Api.apiCall('get',Api.order.appPay,params);
                                                            console.log(appRes);
			// #endif
                            // #ifdef H5
                            //微信h5支付调用

                    		 let h5Res = await Api.apiCall('get',Api.order.wapPay,params);
                                                console.log(h5Res);
                                                if (h5Res) {

                                                }
                                                 let webPay = await Api.apiCall('get',Api.order.webPay,params);
                                                  console.log('webPay');
                                                                                                console.log(webPay);
                                                                                                if (webPay) {

                                                                                                }
                    		// #endif

                    // #ifdef MP-WEIXIN
                    // 小程序支付调用

                    let res = await Api.apiCall('post',Api.order.weixinAppletPay,params);
                    console.log(res);
                    if (res) {
                        uni.requestPayment({
                            timeStamp: res.timeStamp,
                            nonceStr: res.nonceStr,
                            package: res.package,
                            signType: res.signType,
                            paySign: res.paySign,
                            success(datas) {
                                console.log(datas);
                                uni.redirectTo({
                                    url: '/pages/money/paySuccess'
                                })
                            },
                            fail(err) {
                                fail(JSON.stringify(err))
                            }
                        });

                    }else {
                        this.$api.msg('微信小程序失败');
                    }
                    // #endif
                }else if (this.payType ==3){
                    let params1 = {'payAmount':this.orderInfo.payAmount,'balance':this.userInfo.userInfo.blance,'orderId':this.orderInfo.id};
                    let data1 = await Api.apiCall('post',Api.order.balancePay,params1);
                    if (data1) {
                        uni.redirectTo({
                            url: '/pages/money/paySuccess'
                        })
                    }else {
                        this.$api.msg('余额支付失败');
                    }
                }

			},
		}
	}
</script>

<style lang='scss'>
	.app {
		width: 100%;
	}

	.price-box {
		background-color: #fff;
		height: 265upx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		font-size: 28upx;
		color: #909399;

		.price{
			font-size: 50upx;
			color: #303133;
			margin-top: 12upx;
			&:before{
				content: '￥';
				font-size: 40upx;
			}
		}
	}

	.pay-type-list {
		margin-top: 20upx;
		background-color: #fff;
		padding-left: 60upx;

		.type-item{
			height: 120upx;
			padding: 20upx 0;
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding-right: 60upx;
			font-size: 30upx;
			position:relative;
		}

		.icon{
			width: 100upx;
			font-size: 52upx;
		}
		.icon-erjiye-yucunkuan {
			color: #fe8e2e;
		}
		.icon-weixinzhifu {
			color: #36cb59;
		}
		.icon-alipay {
			color: #01aaef;
		}
		.tit{
			font-size: $font-lg;
			color: $font-color-dark;
			margin-bottom: 4upx;
		}
		.con{
			flex: 1;
			display: flex;
			flex-direction: column;
			font-size: $font-sm;
			color: $font-color-light;
		}
	}
	.mix-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 630upx;
		height: 80upx;
		margin: 80upx auto 30upx;
		font-size: $font-lg;
		color: #fff;
		background-color: $base-color;
		border-radius: 10upx;
		box-shadow: 1px 2px 5px rgba(219, 63, 96, 0.4);
	}

</style>
